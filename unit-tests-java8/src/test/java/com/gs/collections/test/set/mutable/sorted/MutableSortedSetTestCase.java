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

package com.gs.collections.test.set.mutable.sorted;

import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.test.MutableSortedIterableTestCase;
import com.gs.collections.test.collection.mutable.MutableCollectionUniqueTestCase;
import com.gs.collections.test.set.sorted.SortedSetIterableTestCase;
import com.gs.collections.test.set.sorted.SortedSetTestCase;

public interface MutableSortedSetTestCase extends SortedSetIterableTestCase, MutableCollectionUniqueTestCase, SortedSetTestCase, MutableSortedIterableTestCase
{
    @Override
    <T> MutableSortedSet<T> newWith(T... elements);

    @Override
    default void Iterable_remove()
    {
        // Both implementations are the same
        SortedSetTestCase.super.Iterable_remove();
        MutableSortedIterableTestCase.super.Iterable_remove();
    }
}
