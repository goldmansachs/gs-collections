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

package com.webguys.ponzu.impl.bag.immutable;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.webguys.ponzu.api.bag.ImmutableBag;
import com.webguys.ponzu.api.map.sorted.MutableSortedMap;
import com.webguys.ponzu.api.partition.bag.PartitionImmutableBag;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.bag.mutable.HashBag;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.factory.Bags;
import com.webguys.ponzu.impl.map.sorted.mutable.TreeSortedMap;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableEmptyBagTest extends ImmutableBagTestCase
{
    @Override
    protected ImmutableBag<String> newBag()
    {
        return (ImmutableBag<String>) ImmutableEmptyBag.INSTANCE;
    }

    @Override
    protected int numKeys()
    {
        return 0;
    }

    @Test
    public void testFactory()
    {
        Verify.assertInstanceOf(ImmutableEmptyBag.class, Bags.immutable.of());
    }

    @Test
    @Override
    public void testNewWith()
    {
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> newBag = bag.newWith("1");
        Verify.assertNotEquals(bag, newBag);
        Assert.assertEquals(newBag.size(), bag.size() + 1);
        ImmutableBag<String> newBag2 = bag.newWith("5");
        Verify.assertNotEquals(bag, newBag2);
        Assert.assertEquals(newBag2.size(), bag.size() + 1);
        Assert.assertEquals(1, newBag2.sizeDistinct());
    }

    @Test
    @Override
    public void testSelect()
    {
        ImmutableBag<String> strings = this.newBag();
        Verify.assertIterableEmpty(strings.filter(Predicates.lessThan("0")));
    }

    @Test
    @Override
    public void testReject()
    {
        ImmutableBag<String> strings = this.newBag();
        Verify.assertIterableEmpty(strings.filterNot(Predicates.greaterThan("0")));
    }

    @Override
    public void partition()
    {
        PartitionImmutableBag<String> partition = this.newBag().partition(Predicates.lessThan("0"));
        Verify.assertIterableEmpty(partition.getSelected());
        Verify.assertIterableEmpty(partition.getRejected());
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();
        Assert.assertEquals("[]", this.newBag().toString());
    }

    @Override
    @Test
    public void testSize()
    {
        Verify.assertIterableSize(0, this.newBag());
    }

    @Override
    @Test
    public void testNewWithout()
    {
        Assert.assertSame(this.newBag(), this.newBag().newWithout("1"));
    }

    @Override
    @Test
    public void testDetect()
    {
        Assert.assertNull(this.newBag().find(Predicates.equal("1")));
    }

    @Override
    @Test
    public void allSatisfy()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertTrue(strings.allSatisfy(Predicates.instanceOf(String.class)));
    }

    @Override
    @Test
    public void testAnySatisfy()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertFalse(strings.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    @Test
    public void testGetFirst()
    {
        Assert.assertNull(this.newBag().getFirst());
    }

    @Override
    @Test
    public void testGetLast()
    {
        Assert.assertNull(this.newBag().getLast());
    }

    @Override
    @Test
    public void testIsEmpty()
    {
        ImmutableBag<String> bag = this.newBag();
        Assert.assertTrue(bag.isEmpty());
        Assert.assertFalse(bag.notEmpty());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        this.newBag().min(Comparators.naturalOrder());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        this.newBag().max(Comparators.naturalOrder());
    }

    @Test
    @Override
    public void min_null_throws()
    {
        super.min_null_throws();
    }

    @Test
    @Override
    public void max_null_throws()
    {
        super.max_null_throws();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min_without_comparator()
    {
        this.newBag().min();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max_without_comparator()
    {
        this.newBag().max();
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
        this.newBag().minBy(Functions.getToString());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        this.newBag().maxBy(Functions.getToString());
    }

    @Override
    @Test
    public void zip()
    {
        ImmutableBag<String> immutableBag = this.newBag();
        List<Object> nulls = Collections.nCopies(immutableBag.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableBag.size() + 1, null);

        ImmutableBag<Pair<String, Object>> pairs = immutableBag.zip(nulls);
        Assert.assertEquals(
                immutableBag,
                pairs.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                HashBag.newBag(nulls),
                pairs.transform(Functions.<Object>secondOfPair()));

        ImmutableBag<Pair<String, Object>> pairsPlusOne = immutableBag.zip(nullsPlusOne);
        Assert.assertEquals(
                immutableBag,
                pairsPlusOne.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                HashBag.newBag(nulls),
                pairsPlusOne.transform(Functions.<Object>secondOfPair()));

        Assert.assertEquals(immutableBag.zip(nulls), immutableBag.zip(nulls, HashBag.<Pair<String, Object>>newBag()));
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        ImmutableBag<String> immutableBag = this.newBag();
        ImmutableBag<Pair<String, Integer>> pairs = immutableBag.zipWithIndex();

        Assert.assertEquals(immutableBag, pairs.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(HashBag.<Integer>newBag(), pairs.transform(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableBag.zipWithIndex(), immutableBag.zipWithIndex(HashBag.<Pair<String, Integer>>newBag()));
    }

    @Test
    public void chunk()
    {
        Assert.assertEquals(this.newBag(), this.newBag().chunk(2));
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.newBag().chunk(0);
    }

    @Override
    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.newBag(), this.newBag().chunk(10));
        Verify.assertInstanceOf(ImmutableBag.class, this.newBag().chunk(10));
    }

    @Override
    @Test
    public void toSortedMap()
    {
        MutableSortedMap<String, String> map = this.newBag().toSortedMap(Functions.getStringPassThru(), Functions.getStringPassThru());
        Verify.assertEmpty(map);
        Verify.assertInstanceOf(TreeSortedMap.class, map);
    }

    @Override
    @Test
    public void toSortedMap_with_comparator()
    {
        MutableSortedMap<String, String> map = this.newBag().toSortedMap(Comparators.<String>reverseNaturalOrder(),
                Functions.getStringPassThru(), Functions.getStringPassThru());
        Verify.assertEmpty(map);
        Verify.assertInstanceOf(TreeSortedMap.class, map);
        Assert.assertEquals(Comparators.<String>reverseNaturalOrder(), map.comparator());
    }

    @Override
    @Test
    public void serialization()
    {
        ImmutableBag<String> bag = this.newBag();
        Verify.assertPostSerializedIdentity(bag);
    }
}
