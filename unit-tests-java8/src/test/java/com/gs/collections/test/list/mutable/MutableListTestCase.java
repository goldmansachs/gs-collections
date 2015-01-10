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

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
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
    default void MutableList_sortThis_comparator()
    {
        MutableList<Integer> mutableList = this.newWith(5, 1, 4, 2, 3);
        MutableList<Integer> sortedList = mutableList.sortThis(Comparators.reverseNaturalOrder());
        assertSame(mutableList, sortedList);
        assertEquals(Lists.immutable.with(5, 4, 3, 2, 1), sortedList);
    }
}
