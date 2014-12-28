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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.ParallelBag;
import com.gs.collections.impl.bag.mutable.HashBag;

public class ParallelCollectBagTest extends ParallelBagTestCase
{
    @Override
    protected ParallelBag<Integer> classUnderTest()
    {
        return this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    }

    @Override
    protected ParallelBag<Integer> newWith(Integer... littleElements)
    {
        return HashBag.newBagWith(littleElements)
                .asParallel(this.executorService, this.batchSize)
                .collect(String::valueOf)
                .collect(string -> "null".equals(string) ? null : Integer.valueOf(string));
    }

    @Override
    protected MutableBag<Integer> getExpectedWith(Integer... littleElements)
    {
        return HashBag.newBagWith(littleElements)
                .collect(String::valueOf)
                .collect(string -> "null".equals(string) ? null : Integer.valueOf(string));
    }
}
