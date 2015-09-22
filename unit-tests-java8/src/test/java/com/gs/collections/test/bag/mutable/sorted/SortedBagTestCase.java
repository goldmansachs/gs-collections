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

package com.gs.collections.test.bag.mutable.sorted;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.test.SortedIterableTestCase;
import com.gs.collections.test.bag.BagTestCase;
import com.gs.collections.test.domain.A;
import com.gs.collections.test.domain.B;
import com.gs.collections.test.domain.C;
import com.gs.collections.test.list.TransformsToListTrait;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;

// TODO linked bag
public interface SortedBagTestCase extends SortedIterableTestCase, BagTestCase, TransformsToListTrait
{
    @Override
    <T> SortedBag<T> newWith(T... elements);

    @Override
    default <T> SortedBag<T> getExpectedFiltered(T... elements)
    {
        return new TreeBag<>(Comparators.reverseNaturalOrder(), Lists.immutable.with(elements));
    }

    @Override
    default <T> MutableSortedBag<T> newMutableForFilter(T... elements)
    {
        return new TreeBag<>(Comparators.reverseNaturalOrder(), Lists.immutable.with(elements));
    }

    @Override
    @Test
    default void RichIterable_selectInstancesOf()
    {
        // Must test with two classes that are mutually Comparable

        SortedBag<A> numbers = this.<A>newWith(
                new C(4.0), new C(4.0), new C(4.0), new C(4.0),
                new B(3), new B(3), new B(3),
                new C(2.0), new C(2.0),
                new B(1));
        assertEquals(
                this.<B>getExpectedFiltered(new B(3), new B(3), new B(3), new B(1)),
                numbers.selectInstancesOf(B.class));
        assertEquals(
                this.getExpectedFiltered(
                        new C(4.0), new C(4.0), new C(4.0), new C(4.0),
                        new B(3), new B(3), new B(3),
                        new C(2.0), new C(2.0),
                        new B(1)),
                numbers.selectInstancesOf(A.class));
    }

    @Override
    @Test
    default void Bag_sizeDistinct()
    {
        SortedBag<Integer> bag = this.newWith(3, 3, 3, 2, 2, 1);
        assertEquals(3, bag.sizeDistinct());
    }

    @Override
    @Test
    default void Bag_occurrencesOf()
    {
        SortedBag<Integer> bag = this.newWith(3, 3, 3, 2, 2, 1);
        assertEquals(0, bag.occurrencesOf(0));
        assertEquals(1, bag.occurrencesOf(1));
        assertEquals(2, bag.occurrencesOf(2));
        assertEquals(3, bag.occurrencesOf(3));
    }

    @Override
    @Test
    default void Bag_toStringOfItemToCount()
    {
        assertEquals("{}", this.newWith().toStringOfItemToCount());
        assertEquals("{3=3, 2=2, 1=1}", this.newWith(3, 3, 3, 2, 2, 1).toStringOfItemToCount());
    }

    @Test
    default void SortedBag_forEachWith()
    {
        SortedBag<Integer> bag = this.newWith(3, 3, 3, 2, 2, 1);
        MutableList<Integer> result = Lists.mutable.with();
        bag.forEachWith((argument1, argument2) -> {
            result.add(argument1);
            result.add(argument2);
        }, 0);
        assertEquals(Lists.immutable.with(3, 0, 3, 0, 3, 0, 2, 0, 2, 0, 1, 0), result);
    }
}
