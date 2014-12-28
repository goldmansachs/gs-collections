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

package com.gs.collections.impl.lazy.parallel.list;

import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.ListAdapter;
import com.gs.collections.impl.list.mutable.RandomAccessListAdapter;
import org.junit.Test;

public class RandomAccessListAdapterParallelListIterableTest extends ParallelListIterableTestCase
{
    @Override
    protected ParallelListIterable<Integer> classUnderTest()
    {
        return this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    }

    @Override
    protected ParallelListIterable<Integer> newWith(Integer... littleElements)
    {
        return RandomAccessListAdapter.adapt(Lists.mutable.of(littleElements)).asParallel(this.executorService, this.batchSize);
    }

    @Override
    protected ListIterable<Integer> getExpectedWith(Integer... littleElements)
    {
        return RandomAccessListAdapter.adapt(Lists.mutable.of(littleElements));
    }

    @Test(expected = IllegalArgumentException.class)
    public void asParallel_small_batch()
    {
        ListAdapter.adapt(Lists.mutable.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)).asParallel(this.executorService, 0);
    }

    @Test(expected = NullPointerException.class)
    public void asParallel_null_executorService()
    {
        ListAdapter.adapt(Lists.mutable.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)).asParallel(null, 2);
    }
}
