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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.lazy.parallel.set.ParallelUnsortedSetIterableTestCase;
import org.junit.Assert;
import org.junit.Test;

public class ParallelCollectDistinctSortedSetIterableTest extends ParallelUnsortedSetIterableTestCase
{
    @Override
    protected ParallelUnsortedSetIterable<Integer> classUnderTest()
    {
        return this.newWith(44, 43, 42, 41, 33, 32, 31, 22, 21, 11);
    }

    @Override
    protected ParallelUnsortedSetIterable<Integer> newWith(Integer... littleElements)
    {
        return SortedSets.immutable.with(Comparators.reverseNaturalOrder(), littleElements)
                .asParallel(this.executorService, this.batchSize)
                .collect(i -> i / 10)
                .asUnique();
    }

    @Override
    protected MutableSet<Integer> getExpectedWith(Integer... littleElements)
    {
        return SortedSets.immutable.with(Comparators.reverseNaturalOrder(), littleElements)
                .collect(i -> i / 10, SortedSets.mutable.of()).toSet();
    }

    @Test
    @Override
    public void groupBy()
    {
        Function<Integer, Boolean> isOddFunction = object -> IntegerPredicates.isOdd().accept(object);

        Assert.assertEquals(
                this.getExpected().toSet().groupBy(isOddFunction),
                this.classUnderTest().groupBy(isOddFunction));
    }

    @Test
    @Override
    public void groupByEach()
    {
        Assert.assertEquals(
                this.getExpected().toSet().groupByEach(new NegativeIntervalFunction()),
                this.classUnderTest().groupByEach(new NegativeIntervalFunction()));
    }
}
