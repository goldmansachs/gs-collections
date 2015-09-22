/*
 * Copyright 2015 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.test.bag;

import java.util.Iterator;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.test.RichIterableWithDuplicatesTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

public interface BagTestCase extends RichIterableWithDuplicatesTestCase
{
    @Override
    <T> Bag<T> newWith(T... elements);

    @Override
    default MutableCollection<Integer> expectedIterationOrder()
    {
        MutableCollection<Integer> forEach = this.newMutableForFilter();
        Bag<Integer> bag = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        bag.forEachWithOccurrences((Integer each, int parameter) -> forEach.add(each));
        return forEach;
    }

    @Override
    default void RichIterable_iterator_iterationOrder()
    {
        MutableCollection<Integer> iterationOrder = this.newMutableForFilter();
        Iterator<Integer> iterator = this.getInstanceUnderTest().iterator();
        while (iterator.hasNext())
        {
            iterationOrder.add(iterator.next());
        }
        assertEquals(RichIterableWithDuplicatesTestCase.super.expectedIterationOrder(), iterationOrder);

        MutableCollection<Integer> forEachWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().forEachWith((each, param) -> forEachWithIterationOrder.add(each), null);
        assertEquals(RichIterableWithDuplicatesTestCase.super.expectedIterationOrder(), forEachWithIterationOrder);

        MutableCollection<Integer> forEachWithIndexIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().forEachWithIndex((each, index) -> forEachWithIndexIterationOrder.add(each));
        assertEquals(RichIterableWithDuplicatesTestCase.super.expectedIterationOrder(), forEachWithIndexIterationOrder);
    }

    @Test
    default void Bag_sizeDistinct()
    {
        Bag<Integer> bag = this.newWith(3, 3, 3, 2, 2, 1);
        assertEquals(3, bag.sizeDistinct());
    }

    @Test
    default void Bag_occurrencesOf()
    {
        Bag<Integer> bag = this.newWith(3, 3, 3, 2, 2, 1);
        assertEquals(0, bag.occurrencesOf(0));
        assertEquals(1, bag.occurrencesOf(1));
        assertEquals(2, bag.occurrencesOf(2));
        assertEquals(3, bag.occurrencesOf(3));
    }

    @Test
    default void Bag_toStringOfItemToCount()
    {
        assertEquals("{}", this.newWith().toStringOfItemToCount());
        assertThat(this.newWith(2, 2, 1).toStringOfItemToCount(), isOneOf("{1=1, 2=2}", "{2=2, 1=1}"));
    }
}
