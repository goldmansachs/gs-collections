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

import java.util.TreeSet;

import com.gs.collections.api.set.sorted.ParallelSortedSetIterable;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.set.sorted.mutable.SortedSetAdapter;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;

public class SortedSetAdapterParallelSetIterableTest extends NonParallelSortedSetIterableTestCase
{
    @Override
    protected ParallelSortedSetIterable<Integer> classUnderTest()
    {
        return this.newWith(4, 3, 2, 1);
    }

    @Override
    protected ParallelSortedSetIterable<Integer> newWith(Integer... littleElements)
    {
        return SortedSetAdapter.adapt(new TreeSet<>(TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder(), littleElements))).asParallel(this.executorService, this.batchSize);
    }
}
