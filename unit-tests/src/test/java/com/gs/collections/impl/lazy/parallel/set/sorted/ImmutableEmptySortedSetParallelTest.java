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

import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.set.sorted.ParallelSortedSetIterable;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableEmptySortedSetParallelTest extends NonParallelSortedSetIterableTestCase
{
    @Override
    protected SortedSetIterable<Integer> getExpected()
    {
        return TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder());
    }

    @Override
    protected SortedSetIterable<Integer> getExpectedWith(Integer... littleElements)
    {
        return TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder());
    }

    @Override
    protected ParallelSortedSetIterable<Integer> classUnderTest()
    {
        return this.newWith();
    }

    @Override
    protected ParallelSortedSetIterable<Integer> newWith(Integer... littleElements)
    {
        return SortedSets.immutable.with(Comparators.<Integer>reverseNaturalOrder()).asParallel(this.executorService, this.batchSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void asParallel_small_batch()
    {
        SortedSets.immutable.with(Comparators.reverseNaturalOrder()).asParallel(this.executorService, 0);
    }

    @Test(expected = NullPointerException.class)
    public void asParallel_null_executorService()
    {
        SortedSets.immutable.with(Comparators.reverseNaturalOrder()).asParallel(null, 2);
    }

    @Override
    public void allSatisfy()
    {
        Assert.assertTrue(this.classUnderTest().allSatisfy(Predicates.lessThan(0)));
        Assert.assertTrue(this.classUnderTest().allSatisfy(Predicates.greaterThanOrEqualTo(0)));
    }

    @Override
    public void allSatisfyWith()
    {
        Assert.assertTrue(this.classUnderTest().allSatisfyWith(Predicates2.<Integer>lessThan(), 0));
        Assert.assertTrue(this.classUnderTest().allSatisfyWith(Predicates2.<Integer>greaterThanOrEqualTo(), 0));
    }

    @Override
    public void anySatisfy()
    {
        Assert.assertFalse(this.classUnderTest().anySatisfy(Predicates.lessThan(0)));
        Assert.assertFalse(this.classUnderTest().anySatisfy(Predicates.greaterThanOrEqualTo(0)));
    }

    @Override
    public void anySatisfyWith()
    {
        Assert.assertFalse(this.classUnderTest().anySatisfyWith(Predicates2.<Integer>lessThan(), 0));
        Assert.assertFalse(this.classUnderTest().anySatisfyWith(Predicates2.<Integer>greaterThanOrEqualTo(), 0));
    }

    @Override
    public void noneSatisfy()
    {
        Assert.assertTrue(this.classUnderTest().noneSatisfy(Predicates.lessThan(0)));
        Assert.assertTrue(this.classUnderTest().noneSatisfy(Predicates.greaterThanOrEqualTo(0)));
    }

    @Override
    public void noneSatisfyWith()
    {
        Assert.assertTrue(this.classUnderTest().noneSatisfyWith(Predicates2.<Integer>lessThan(), 0));
        Assert.assertTrue(this.classUnderTest().noneSatisfyWith(Predicates2.<Integer>greaterThanOrEqualTo(), 0));
    }

    @Override
    public void appendString_throws()
    {
        // Not applicable for empty collections
    }

    @Override
    public void detect()
    {
        Assert.assertNull(this.classUnderTest().detect(Integer.valueOf(0)::equals));
    }

    @Override
    public void detectIfNone()
    {
        Assert.assertEquals(Integer.valueOf(10), this.classUnderTest().detectIfNone(Integer.valueOf(0)::equals, () -> 10));
    }

    @Override
    public void detectWith()
    {
        Assert.assertNull(this.classUnderTest().detectWith(Object::equals, Integer.valueOf(0)));
    }

    @Override
    public void detectWithIfNone()
    {
        Function0<Integer> function = new PassThruFunction0<>(Integer.valueOf(1000));
        Assert.assertEquals(Integer.valueOf(1000), this.classUnderTest().detectWithIfNone(Object::equals, Integer.valueOf(0), function));
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        this.classUnderTest().min(Integer::compareTo);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        this.classUnderTest().max(Integer::compareTo);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        this.classUnderTest().minBy(String::valueOf);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        this.classUnderTest().maxBy(String::valueOf);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min_without_comparator()
    {
        this.classUnderTest().min();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max_without_comparator()
    {
        this.classUnderTest().max();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minWithEmptyBatch()
    {
        super.minWithEmptyBatch();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxWithEmptyBatch()
    {
        super.minWithEmptyBatch();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min_null_throws()
    {
        this.classUnderTest().min(Integer::compareTo);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max_null_throws()
    {
        this.classUnderTest().max(Integer::compareTo);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy_null_throws()
    {
        this.classUnderTest().minBy(Integer::valueOf);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy_null_throws()
    {
        this.classUnderTest().maxBy(Integer::valueOf);
    }
}
