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

package ponzu.impl.set.immutable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import ponzu.api.set.ImmutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.factory.Lists;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.test.Verify;

public abstract class AbstractImmutableEmptySetTestCase extends AbstractImmutableSetTestCase
{
    @Test
    public void testContainsAll()
    {
        Assert.assertTrue(this.classUnderTest().castToSet().containsAll(new HashSet<Object>()));
        Assert.assertFalse(this.classUnderTest().castToSet().containsAll(UnifiedSet.newSetWith(1)));
    }

    @Override
    @Test
    public void testNewWith()
    {
        ImmutableSet<Integer> immutable = this.classUnderTest();
        Verify.assertSize(1, immutable.newWith(1).castToSet());
    }

    @Override
    @Test
    public void testFind()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.find(Predicates.equal(1)));
    }

    @Override
    @Test
    public void testAllSatisfy()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    @Test
    public void testAnySatisfy()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    @Test
    public void testGetFirst()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.getFirst());
    }

    @Override
    @Test
    public void testGetLast()
    {
        ImmutableSet<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.getLast());
    }

    @Override
    @Test
    public void testIsEmpty()
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
        Assert.assertEquals(immutableSet, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(UnifiedSet.<Object>newSet(nulls), pairs.transform(Functions.<Object>secondOfPair()));

        ImmutableSet<Pair<Integer, Object>> pairsPlusOne = immutableSet.zip(nullsPlusOne);
        Assert.assertEquals(immutableSet, pairsPlusOne.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(UnifiedSet.<Object>newSet(nulls), pairsPlusOne.transform(Functions.<Object>secondOfPair()));

        Assert.assertEquals(immutableSet.zip(nulls), immutableSet.zip(nulls, UnifiedSet.<Pair<Integer, Object>>newSet()));
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        ImmutableSet<Integer> immutableSet = this.classUnderTest();
        ImmutableSet<Pair<Integer, Integer>> pairs = immutableSet.zipWithIndex();

        Assert.assertEquals(immutableSet, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(
                UnifiedSet.<Integer>newSet(),
                pairs.transform(Functions.<Integer>secondOfPair()));

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
}
