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
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.test.MutableSortedNaturalOrderTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.addAllTo;
import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public interface MutableSortedBagNoComparatorTestCase extends SortedBagTestCase, MutableBagIterableTestCase, MutableSortedNaturalOrderTestCase
{
    @Override
    <T> MutableSortedBag<T> newWith(T... elements);

    @Override
    default <T> SortedBag<T> getExpectedFiltered(T... elements)
    {
        return this.newMutableForFilter(elements);
    }

    @Override
    default <T> MutableSortedBag<T> newMutableForFilter(T... elements)
    {
        TreeBag<T> result = new TreeBag<>();
        addAllTo(elements, result);
        return result;
    }

    @Override
    @Test
    default void Bag_toStringOfItemToCount()
    {
        assertEquals("{}", this.newWith().toStringOfItemToCount());
        assertEquals("{1=1, 2=2, 3=3}", this.newWith(3, 3, 3, 2, 2, 1).toStringOfItemToCount());
    }

    @Test
    default void MutableBag_addOccurrences()
    {
        MutableSortedBag<Integer> mutableSortedBag = this.newWith(1, 2, 2, 3, 3, 3);
        mutableSortedBag.addOccurrences(4, 4);
        assertEquals(TreeBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4), mutableSortedBag);
        mutableSortedBag.addOccurrences(1, 2);
        assertEquals(TreeBag.newBagWith(1, 1, 1, 2, 2, 3, 3, 3, 4, 4, 4, 4), mutableSortedBag);
        mutableSortedBag.addOccurrences(1, 0);
        assertEquals(TreeBag.newBagWith(1, 1, 1, 2, 2, 3, 3, 3, 4, 4, 4, 4), mutableSortedBag);
    }

    @Test
    default void MutableBag_removeOccurrences()
    {
        MutableSortedBag<Integer> mutableBag = this.newWith(1, 2, 2, 3, 3, 3);
        assertFalse(mutableBag.removeOccurrences(4, 4));
        assertEquals(TreeBag.newBagWith(1, 2, 2, 3, 3, 3), mutableBag);
        assertFalse(mutableBag.removeOccurrences(3, 0));
        assertEquals(TreeBag.newBagWith(1, 2, 2, 3, 3, 3), mutableBag);
        assertTrue(mutableBag.removeOccurrences(1, 2));
        assertEquals(TreeBag.newBagWith(2, 2, 3, 3, 3), mutableBag);
        assertTrue(mutableBag.removeOccurrences(3, 2));
        assertEquals(TreeBag.newBagWith(2, 2, 3), mutableBag);
        assertTrue(mutableBag.removeOccurrences(2, 1));
        assertEquals(TreeBag.newBagWith(2, 3), mutableBag);
        assertTrue(mutableBag.removeOccurrences(2, 2));
        assertEquals(TreeBag.newBagWith(3), mutableBag);
    }

    @Test
    default void SortedBag_forEachWith()
    {
        SortedBag<Integer> bag = this.newWith(1, 2, 2, 3, 3, 3);
        MutableList<Integer> result = Lists.mutable.with();
        bag.forEachWith((argument1, argument2) -> {
            result.add(argument1);
            result.add(argument2);
        }, 0);
        assertEquals(Lists.immutable.with(1, 0, 2, 0, 2, 0, 3, 0, 3, 0, 3, 0), result);
    }

    @Override
    default void SortedIterable_comparator()
    {
        MutableSortedNaturalOrderTestCase.super.SortedIterable_comparator();
    }
}
