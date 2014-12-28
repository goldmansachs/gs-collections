/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.lazy.parallel.set.sorted;

import com.gs.collections.api.set.sorted.ParallelSortedSetIterable;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.lazy.parallel.ParallelIterableTestCase;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;

public abstract class ParallelSortedSetIterableTestCase extends ParallelIterableTestCase
{
    @Override
    protected abstract ParallelSortedSetIterable<Integer> classUnderTest();

    @Override
    protected SortedSetIterable<Integer> getExpected()
    {
        return TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder(), 4, 3, 2, 1);
    }

    @Override
    protected SortedSetIterable<Integer> getExpectedWith(Integer... littleElements)
    {
        return TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder(), littleElements);
    }

    @Override
    protected boolean isOrdered()
    {
        return true;
    }

    @Override
    protected boolean isUnique()
    {
        return true;
    }
}
