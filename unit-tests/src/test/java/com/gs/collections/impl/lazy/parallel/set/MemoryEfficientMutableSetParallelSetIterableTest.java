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

package com.gs.collections.impl.lazy.parallel.set;

import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import org.junit.Test;

public class MemoryEfficientMutableSetParallelSetIterableTest extends ParallelUnsortedSetIterableTestCase
{
    @Override
    protected ParallelUnsortedSetIterable<Integer> classUnderTest()
    {
        return this.newWith(1, 2, 3, 4);
    }

    @Override
    protected ParallelUnsortedSetIterable<Integer> newWith(Integer... littleElements)
    {
        return Sets.fixedSize.withAll(ArrayAdapter.adapt(littleElements)).asParallel(this.executorService, this.batchSize);
    }

    @Override
    protected MutableSet<Integer> getExpectedWith(Integer... littleElements)
    {
        return Sets.fixedSize.withAll(ArrayAdapter.adapt(littleElements));
    }

    @Test(expected = IllegalArgumentException.class)
    public void asParallel_small_batch()
    {
        Sets.fixedSize.with(1, 2, 3, 4).asParallel(this.executorService, 0);
    }

    @Test(expected = NullPointerException.class)
    public void asParallel_null_executorService()
    {
        Sets.fixedSize.with(1, 2, 3, 4).asParallel(null, 2);
    }
}
