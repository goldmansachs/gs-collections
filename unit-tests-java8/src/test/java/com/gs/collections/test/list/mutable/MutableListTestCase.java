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

package com.gs.collections.test.list.mutable;

import java.util.Collections;
import java.util.Random;

import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.test.MutableOrderedIterableTestCase;
import com.gs.collections.test.collection.mutable.MutableCollectionTestCase;
import com.gs.collections.test.list.ListIterableTestCase;
import com.gs.collections.test.list.ListTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertSame;

public interface MutableListTestCase extends MutableCollectionTestCase, ListTestCase, ListIterableTestCase, MutableOrderedIterableTestCase
{
    @Override
    <T> MutableList<T> newWith(T... elements);

    @Test
    default void MutableList_sortThis()
    {
        MutableList<Integer> mutableList = this.newWith(5, 1, 4, 2, 3);
        MutableList<Integer> sortedList = mutableList.sortThis();
        assertSame(mutableList, sortedList);
        assertEquals(Lists.immutable.with(1, 2, 3, 4, 5), sortedList);
    }

    @Test
    default void MutableList_shuffleThis()
    {
        Integer[] integers = Interval.oneTo(50).toArray();
        MutableList<Integer> mutableList1 = this.newWith(integers);
        MutableList<Integer> mutableList2 = this.newWith(integers);
        Collections.shuffle(mutableList1, new Random(10));
        assertEquals(mutableList1, mutableList2.shuffleThis(new Random(10)));

        MutableList<Integer> list = this.newWith(1, 2, 3);
        UnifiedSet<ImmutableList<Integer>> objects = UnifiedSet.newSet();
        while (objects.size() < 6)
        {
            objects.add(list.shuffleThis().toImmutable());
        }

        Interval interval = Interval.oneTo(1000);
        MutableList<Integer> bigList = this.newWith(interval.toArray());
        MutableList<Integer> shuffledBigList = bigList.shuffleThis(new Random(8));
        MutableList<Integer> integers1 = this.newWith(interval.toArray());
        assertEquals(integers1.shuffleThis(new Random(8)), bigList);
        assertSame(bigList, shuffledBigList);
        assertSame(bigList, bigList.shuffleThis());
        assertSame(bigList, bigList.shuffleThis(new Random(8)));
        assertEquals(interval.toBag(), bigList.toBag());
    }

    @Test
    default void MutableList_sortThis_comparator()
    {
        MutableList<Integer> mutableList = this.newWith(5, 1, 4, 2, 3);
        MutableList<Integer> sortedList = mutableList.sortThis(Comparators.reverseNaturalOrder());
        assertSame(mutableList, sortedList);
        assertEquals(Lists.immutable.with(5, 4, 3, 2, 1), sortedList);
    }
}
