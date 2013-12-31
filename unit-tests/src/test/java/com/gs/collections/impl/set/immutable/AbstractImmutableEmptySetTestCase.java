/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.set.immutable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.primitive.ImmutableBooleanSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractImmutableEmptySetTestCase extends AbstractImmutableSetTestCase
{
    @Test
    public void containsAll()
    {
        Assert.assertTrue(this.classUnderTest().castToSet().containsAll(new HashSet<Object>()));
        Assert.assertFalse(this.classUnderTest().castToSet().containsAll(UnifiedSet.newSetWith(1)));
    }

    @Override
    @Test
    public void newWith()
    {
        ImmutableSet<Integer> immutable = this.classUnderTest();
        Verify.assertSize(1, immutable.newWith(1).castToSet());
    }

    @Override
    @Test
    public void detect()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.detect(Predicates.equal(1)));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    public void anySatisfyWith()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Override
    @Test
    public void allSatisfy()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    public void allSatisfyWith()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Override
    public void noneSatisfy()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.noneSatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    public void noneSatisfyWith()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.noneSatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Override
    @Test
    public void getFirst()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.getFirst());
    }

    @Override
    @Test
    public void getLast()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.getLast());
    }

    @Override
    @Test
    public void isEmpty()
    {
        ImmutableSet<Integer> list = this.classUnderTest();
        Assert.assertTrue(list.isEmpty());
        Assert.assertFalse(list.notEmpty());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        this.classUnderTest().min(Comparators.naturalOrder());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        this.classUnderTest().max(Comparators.naturalOrder());
    }

    @Test
    @Override
    public void min_null_throws()
    {
        // Not applicable for empty collections
        super.min_null_throws();
    }

    @Test
    @Override
    public void max_null_throws()
    {
        // Not applicable for empty collections
        super.max_null_throws();
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

    @Test
    @Override
    public void min_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.min_null_throws_without_comparator();
    }

    @Test
    @Override
    public void max_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.max_null_throws_without_comparator();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        this.classUnderTest().minBy(Functions.getToString());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        this.classUnderTest().maxBy(Functions.getToString());
    }

    @Override
    @Test
    public void zip()
    {
        ImmutableSet<Integer> immutableSet = this.classUnderTest();
        List<Object> nulls = Collections.nCopies(immutableSet.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableSet.size() + 1, null);

        ImmutableSet<Pair<Integer, Object>> pairs = immutableSet.zip(nulls);
        Assert.assertEquals(immutableSet, pairs.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(UnifiedSet.<Object>newSet(nulls), pairs.collect(Functions.<Object>secondOfPair()));

        ImmutableSet<Pair<Integer, Object>> pairsPlusOne = immutableSet.zip(nullsPlusOne);
        Assert.assertEquals(immutableSet, pairsPlusOne.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(UnifiedSet.<Object>newSet(nulls), pairsPlusOne.collect(Functions.<Object>secondOfPair()));

        Assert.assertEquals(immutableSet.zip(nulls), immutableSet.zip(nulls, UnifiedSet.<Pair<Integer, Object>>newSet()));
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        ImmutableSet<Integer> immutableSet = this.classUnderTest();
        ImmutableSet<Pair<Integer, Integer>> pairs = immutableSet.zipWithIndex();

        Assert.assertEquals(immutableSet, pairs.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(
                UnifiedSet.<Integer>newSet(),
                pairs.collect(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(
                immutableSet.zipWithIndex(),
                immutableSet.zipWithIndex(UnifiedSet.<Pair<Integer, Integer>>newSet()));
    }

    @Test
    public void chunk()
    {
        Assert.assertEquals(Lists.mutable.of(), this.classUnderTest().chunk(2));
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.classUnderTest().chunk(0);
    }

    @Override
    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(Lists.mutable.of(), this.classUnderTest().chunk(10));
    }

    @Override
    @Test
    public void union()
    {
        Assert.assertEquals(
                UnifiedSet.newSetWith(1, 2, 3),
                this.classUnderTest().union(UnifiedSet.newSetWith(1, 2, 3)));
    }

    @Override
    @Test
    public void unionInto()
    {
        Assert.assertEquals(
                UnifiedSet.newSetWith(1, 2, 3),
                this.classUnderTest().unionInto(UnifiedSet.newSetWith(1, 2, 3), UnifiedSet.<Integer>newSet()));
    }

    @Override
    @Test
    public void intersect()
    {
        Assert.assertEquals(
                UnifiedSet.<String>newSet(),
                this.classUnderTest().intersect(UnifiedSet.newSetWith(1, 2, 3)));
    }

    @Override
    @Test
    public void intersectInto()
    {
        Assert.assertEquals(
                UnifiedSet.<String>newSet(),
                this.classUnderTest().intersectInto(UnifiedSet.newSetWith(1, 2, 3), UnifiedSet.<Integer>newSet()));
    }

    @Override
    @Test
    public void difference()
    {
        ImmutableSet<Integer> set = this.classUnderTest();
        ImmutableSet<Integer> difference = set.difference(UnifiedSet.newSetWith(1, 2, 3, 999));
        Assert.assertEquals(UnifiedSet.<Integer>newSet(), difference);
        Assert.assertEquals(set, set.difference(UnifiedSet.newSetWith(999)));
    }

    @Override
    @Test
    public void differenceInto()
    {
        ImmutableSet<Integer> set = this.classUnderTest();
        MutableSet<Integer> difference = set.differenceInto(UnifiedSet.newSetWith(1, 2, 3, 999), UnifiedSet.<Integer>newSet());
        Assert.assertEquals(UnifiedSet.<Integer>newSet(), difference);
        Assert.assertEquals(set, set.differenceInto(UnifiedSet.newSetWith(99), UnifiedSet.<Integer>newSet()));
    }

    @Override
    @Test
    public void symmetricDifference()
    {
        Assert.assertEquals(
                UnifiedSet.newSetWith(999),
                this.classUnderTest().symmetricDifference(UnifiedSet.newSetWith(999)));
    }

    @Override
    @Test
    public void symmetricDifferenceInto()
    {
        Assert.assertEquals(
                UnifiedSet.newSetWith(999),
                this.classUnderTest().symmetricDifferenceInto(UnifiedSet.newSetWith(999), UnifiedSet.<Integer>newSet()));
    }

    @Override
    @Test
    public void collectBoolean()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        ImmutableBooleanSet actual = integers.collectBoolean(PrimitiveFunctions.integerIsPositive());
        Verify.assertEmpty(actual);
    }
}
