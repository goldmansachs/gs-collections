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

import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.mutable.FastList;

public class ParallelSelectListIterableTest extends AbstractParallelListIterableTestCase
{
    @Override
    protected ParallelListIterable<Integer> classUnderTest()
    {
        return FastList.newListWith(-1, 1, -1, 2, -1, 2, -1, 3, -1, 3, -1, 3, 5, 4, 5, 4, 5, 4, 5, 4, 5)
                .asParallel(this.executorService, 2)
                .select(Predicates.greaterThan(0)).select(Predicates.lessThan(5));
    }
}
