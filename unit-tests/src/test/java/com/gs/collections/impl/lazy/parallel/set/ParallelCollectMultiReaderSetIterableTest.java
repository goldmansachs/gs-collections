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

import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.MultiReaderHashBag;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.lazy.parallel.ParallelIterableTestCase;
import com.gs.collections.impl.set.mutable.MultiReaderUnifiedSet;
import org.junit.Assert;
import org.junit.Test;

public class ParallelCollectMultiReaderSetIterableTest extends ParallelIterableTestCase
{
    @Override
    protected ParallelIterable<Integer> classUnderTest()
    {
        return this.newWith(44, 43, 42, 41, 33, 32, 31, 22, 21, 11);
    }

    @Override
    protected ParallelIterable<Integer> newWith(Integer... littleElements)
    {
        return MultiReaderUnifiedSet.newSetWith(littleElements)
                .asParallel(this.executorService, this.batchSize)
                .collect(i -> i / 10);
    }

    @Override
    protected MutableBag<Integer> getExpected()
    {
        return HashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    }

    @Override
    protected MutableBag<Integer> getExpectedWith(Integer... littleElements)
    {
        return MultiReaderHashBag.newBagWith(littleElements)
                .collect(i -> i / 10);
    }

    @Override
    protected boolean isOrdered()
    {
        return false;
    }

    @Override
    protected boolean isUnique()
    {
        return false;
    }

    @Test
    @Override
    public void groupBy()
    {
        Function<Integer, Boolean> isOddFunction = object -> IntegerPredicates.isOdd().accept(object);

        Assert.assertEquals(
                this.getExpected().toBag().groupBy(isOddFunction),
                this.classUnderTest().groupBy(isOddFunction));
    }

    @Test
    @Override
    public void groupByEach()
    {
        Assert.assertEquals(
                this.getExpected().toBag().groupByEach(new NegativeIntervalFunction()),
                this.classUnderTest().groupByEach(new NegativeIntervalFunction()));
    }
}
