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

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import org.junit.Test;

public class UnifiedSetWithHashingStrategyParallelTest extends ParallelUnsortedSetIterableTestCase
{
    private static final HashingStrategy<Integer> INTEGER_TO_STRING_HASHING_STRATEGY = HashingStrategies.fromFunction(Integer::valueOf);

    @Override
    protected ParallelUnsortedSetIterable<Integer> classUnderTest()
    {
        return this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    }

    @Override
    protected ParallelUnsortedSetIterable<Integer> newWith(Integer... littleElements)
    {
        return UnifiedSetWithHashingStrategy.newSetWith(INTEGER_TO_STRING_HASHING_STRATEGY, littleElements).asParallel(this.executorService, this.batchSize);
    }

    @Override
    protected MutableSet<Integer> getExpected()
    {
        return UnifiedSetWithHashingStrategy.newSetWith(INTEGER_TO_STRING_HASHING_STRATEGY, 1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    }

    @Override
    protected MutableSet<Integer> getExpectedWith(Integer... littleElements)
    {
        return UnifiedSetWithHashingStrategy.newSetWith(INTEGER_TO_STRING_HASHING_STRATEGY, littleElements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void asParallel_small_batch()
    {
        UnifiedSetWithHashingStrategy.newSetWith(INTEGER_TO_STRING_HASHING_STRATEGY, 1, 2, 2, 3, 3, 3, 4, 4, 4, 4).asParallel(this.executorService, 0);
    }

    @Test(expected = NullPointerException.class)
    public void asParallel_null_executorService()
    {
        UnifiedSetWithHashingStrategy.newSetWith(INTEGER_TO_STRING_HASHING_STRATEGY, 1, 2, 2, 3, 3, 3, 4, 4, 4, 4).asParallel(null, 2);
    }
}
