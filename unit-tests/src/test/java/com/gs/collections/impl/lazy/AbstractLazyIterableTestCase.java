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

package com.gs.collections.impl.lazy;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public abstract class AbstractLazyIterableTestCase
{
    private final LazyIterable<Integer> lazyIterable = this.newWith(1, 2, 3, 4, 5, 6, 7);

    protected abstract LazyIterable<Integer> newWith(Integer... integers);

    @Test
    public abstract void iterator();

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(
                FastList.newListWith(1, 2).toArray(),
                this.lazyIterable.select(Predicates.lessThan(3)).toArray());
        Assert.assertArrayEquals(
                FastList.newListWith(1, 2).toArray(),
                this.lazyIterable.select(Predicates.lessThan(3)).toArray(new Object[2]));
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(this.lazyIterable.contains(3));
        Assert.assertFalse(this.lazyIterable.contains(8));
    }

    @Test
    public void containsAllIterable()
    {
        Assert.assertTrue(this.lazyIterable.containsAllIterable(FastList.newListWith(3)));
        Assert.assertFalse(this.lazyIterable.containsAllIterable(FastList.newListWith(8)));
    }

    @Test
    public void containsAllArray()
    {
        Assert.assertTrue(this.lazyIterable.containsAllArguments(3));
        Assert.assertFalse(this.lazyIterable.containsAllArguments(8));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(
                FastList.newListWith(1, 2),
                this.lazyIterable.select(Predicates.lessThan(3)).toList());
    }

    @Test
    public void selectWith()
    {
        Assert.assertEquals(
                FastList.newListWith(1, 2),
                this.lazyIterable.selectWith(Predicates2.<Integer>lessThan(), 3, FastList.<Integer>newList()));
    }

    @Test
    public void selectWithTarget()
    {
        Assert.assertEquals(
                FastList.newListWith(1, 2),
                this.lazyIterable.select(Predicates.lessThan(3), FastList.<Integer>newList()));
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(FastList.newListWith(3, 4, 5, 6, 7), this.lazyIterable.reject(Predicates.lessThan(3)).toList());
    }

    @Test
    public void rejectWith()
    {
        Assert.assertEquals(
                FastList.newListWith(3, 4, 5, 6, 7),
                this.lazyIterable.rejectWith(Predicates2.<Integer>lessThan(), 3, FastList.<Integer>newList()));
    }

    @Test
    public void rejectWithTarget()
    {
        Assert.assertEquals(
                FastList.newListWith(3, 4, 5, 6, 7),
                this.lazyIterable.reject(Predicates.lessThan(3), FastList.<Integer>newList()));
    }

    @Test
    public void partition()
    {
        PartitionIterable<Integer> partition = this.lazyIterable.partition(IntegerPredicates.isEven());
        Assert.assertEquals(iList(2, 4, 6), partition.getSelected());
        Assert.assertEquals(iList(1, 3, 5, 7), partition.getRejected());
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(
                FastList.newListWith("1", "2", "3", "4", "5", "6", "7"),
                this.lazyIterable.collect(Functions.getToString()).toList());
    }

    @Test
    public void collectWith()
    {
        Assert.assertEquals(
                FastList.newListWith("1 ", "2 ", "3 ", "4 ", "5 ", "6 ", "7 "),
                this.lazyIterable.collectWith(new Function2<Integer, String, String>()
                {
                    public String value(Integer argument1, String argument2)
                    {
                        return argument1.toString() + argument2;
                    }
                }, " ", FastList.<String>newList()));
    }

    @Test
    public void collectWithTarget()
    {
        Assert.assertEquals(
                FastList.newListWith("1", "2", "3", "4", "5", "6", "7"),
                this.lazyIterable.collect(Functions.getToString(), FastList.<String>newList()));
    }

    @Test
    public void take()
    {
        Assert.assertEquals(FastList.newListWith(1, 2), this.lazyIterable.take(2).toList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_negative_throws()
    {
        this.lazyIterable.take(-1);
    }

    @Test
    public void drop()
    {
        Assert.assertEquals(FastList.newListWith(3, 4, 5, 6, 7), this.lazyIterable.drop(2).toList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_negative_throws()
    {
        this.lazyIterable.drop(-1);
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.lazyIterable.detect(Predicates.equal(3)));
        Assert.assertNull(this.lazyIterable.detect(Predicates.equal(8)));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws()
    {
        this.newWith().min(Comparators.naturalOrder());
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws()
    {
        this.newWith().max(Comparators.naturalOrder());
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws()
    {
        this.newWith(1, null, 2).min(Comparators.naturalOrder());
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws()
    {
        this.newWith(1, null, 2).max(Comparators.naturalOrder());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1, 3, 2).min(Comparators.naturalOrder()));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 3, 2).max(Comparators.naturalOrder()));
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1, 3, 2).minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 3, 2).maxBy(Functions.getToString()));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws_without_comparator()
    {
        this.newWith().min();
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws_without_comparator()
    {
        this.newWith().max();
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws_without_comparator()
    {
        this.newWith(1, null, 2).min();
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws_without_comparator()
    {
        this.newWith(1, null, 2).max();
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(3, 1, 2).min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 3, 2).max());
    }

    @Test
    public void detectIfNoneWithBlock()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(9);
        Assert.assertEquals(Integer.valueOf(3), this.lazyIterable.detectIfNone(Predicates.equal(3), function));
        Assert.assertEquals(Integer.valueOf(9), this.lazyIterable.detectIfNone(Predicates.equal(8), function));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.lazyIterable.allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(this.lazyIterable.allSatisfy(Predicates.equal(1)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.lazyIterable.anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(this.lazyIterable.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(7, this.lazyIterable.count(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void collectIf()
    {
        Assert.assertEquals(
                FastList.newListWith("1", "2", "3"),
                this.newWith(1, 2, 3).collectIf(
                        Predicates.instanceOf(Integer.class),
                        Functions.getToString()).toList());
    }

    @Test
    public void collectIfWithTarget()
    {
        Assert.assertEquals(
                FastList.newListWith("1", "2", "3"),
                this.newWith(1, 2, 3).collectIf(
                        Predicates.instanceOf(Integer.class),
                        Functions.getToString(),
                        FastList.<String>newList()));
    }

    @Test
    public void getFirst()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1, 2, 3).getFirst());
        Verify.assertNotEquals(Integer.valueOf(3), this.newWith(1, 2, 3).getFirst());
    }

    @Test
    public void getLast()
    {
        Verify.assertNotEquals(Integer.valueOf(1), this.newWith(1, 2, 3).getLast());
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 2, 3).getLast());
    }

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(this.newWith().isEmpty());
        Assert.assertTrue(this.newWith(1, 2).notEmpty());
    }

    @Test
    public void injectInto()
    {
        RichIterable<Integer> objects = this.newWith(1, 2, 3);
        Integer result = objects.injectInto(1, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(7), result);
    }

    @Test
    public void toList()
    {
        MutableList<Integer> list = this.newWith(1, 2, 3, 4).toList();
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), list);
    }

    @Test
    public void toSortedListNaturalOrdering()
    {
        RichIterable<Integer> integers = this.newWith(2, 1, 5, 3, 4);
        MutableList<Integer> list = integers.toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4, 5), list);
    }

    @Test
    public void toSortedList()
    {
        RichIterable<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(FastList.newListWith(4, 3, 2, 1), list);
    }

    @Test
    public void toSortedListBy()
    {
        LazyIterable<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedListBy(Functions.getToString());
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), list);
    }

    @Test
    public void toSortedSet()
    {
        LazyIterable<Integer> integers = this.newWith(2, 4, 1, 3, 2, 1, 3, 4);
        MutableSortedSet<Integer> set = integers.toSortedSet();
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 3, 4), set);
    }

    @Test
    public void toSortedSet_with_comparator()
    {
        LazyIterable<Integer> integers = this.newWith(2, 4, 4, 2, 1, 4, 1, 3);
        MutableSortedSet<Integer> set = integers.toSortedSet(Collections.<Integer>reverseOrder());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 1, 2, 3, 4), set);
    }

    @Test
    public void toSortedSetBy()
    {
        LazyIterable<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableSortedSet<Integer> set = integers.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 3, 4), set);
    }

    @Test
    public void toSet()
    {
        RichIterable<Integer> integers = this.newWith(1, 2, 3, 4);
        MutableSet<Integer> set = integers.toSet();
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), set);
    }

    @Test
    public void toMap()
    {
        RichIterable<Integer> integers = this.newWith(1, 2, 3, 4);
        MutableMap<String, String> map =
                integers.toMap(Functions.getToString(), Functions.getToString());
        Assert.assertEquals(UnifiedMap.<String, String>newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"), map);
    }

    @Test
    public void toSortedMap()
    {
        LazyIterable<Integer> integers = this.newWith(1, 2, 3);
        MutableSortedMap<Integer, String> map = integers.toSortedMap(Functions.getIntegerPassThru(), Functions.getToString());
        Verify.assertMapsEqual(TreeSortedMap.newMapWith(1, "1", 2, "2", 3, "3"), map);
        Verify.assertListsEqual(FastList.newListWith(1, 2, 3), map.keySet().toList());
    }

    @Test
    public void toSortedMap_with_comparator()
    {
        LazyIterable<Integer> integers = this.newWith(1, 2, 3);
        MutableSortedMap<Integer, String> map = integers.toSortedMap(Comparators.<Integer>reverseNaturalOrder(),
                Functions.getIntegerPassThru(), Functions.getToString());
        Verify.assertMapsEqual(TreeSortedMap.newMapWith(Comparators.<Integer>reverseNaturalOrder(), 1, "1", 2, "2", 3, "3"), map);
        Verify.assertListsEqual(FastList.newListWith(3, 2, 1), map.keySet().toList());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[1, 2, 3]", this.newWith(1, 2, 3).toString());
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("[1, 2, 3]", '[' + this.newWith(1, 2, 3).makeString() + ']');
    }

    @Test
    public void appendString()
    {
        Appendable builder = new StringBuilder();
        this.newWith(1, 2, 3).appendString(builder);
        Assert.assertEquals("1, 2, 3", builder.toString());
    }

    @Test
    public void groupBy()
    {
        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };

        MutableMap<Boolean, RichIterable<Integer>> expected =
                UnifiedMap.<Boolean, RichIterable<Integer>>newWithKeysValues(
                        Boolean.TRUE, FastList.newListWith(1, 3, 5, 7),
                        Boolean.FALSE, FastList.newListWith(2, 4, 6));

        Multimap<Boolean, Integer> multimap =
                this.lazyIterable.groupBy(isOddFunction);
        Assert.assertEquals(expected, multimap.toMap());

        Multimap<Boolean, Integer> multimap2 =
                this.lazyIterable.groupBy(isOddFunction, FastListMultimap.<Boolean, Integer>newMultimap());
        Assert.assertEquals(expected, multimap2.toMap());
    }

    @Test
    public void groupByEach()
    {
        MutableMultimap<Integer, Integer> expected = FastListMultimap.newMultimap();
        for (int i = 1; i < 8; i++)
        {
            expected.putAll(-i, Interval.fromTo(i, 7));
        }

        Multimap<Integer, Integer> actual =
                this.lazyIterable.groupByEach(new NegativeIntervalFunction());
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                this.lazyIterable.groupByEach(new NegativeIntervalFunction(), FastListMultimap.<Integer, Integer>newMultimap());
        Assert.assertEquals(expected, actualWithTarget);
    }

    @Test
    public void zip()
    {
        List<Object> nulls = Collections.nCopies(this.lazyIterable.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(this.lazyIterable.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(this.lazyIterable.size() - 1, null);

        LazyIterable<Pair<Integer, Object>> pairs = this.lazyIterable.zip(nulls);
        Assert.assertEquals(
                this.lazyIterable.toSet(),
                pairs.collect(Functions.<Integer>firstOfPair()).toSet());
        Assert.assertEquals(
                nulls,
                pairs.collect(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        LazyIterable<Pair<Integer, Object>> pairsPlusOne = this.lazyIterable.zip(nullsPlusOne);
        Assert.assertEquals(
                this.lazyIterable.toSet(),
                pairsPlusOne.collect(Functions.<Integer>firstOfPair()).toSet());
        Assert.assertEquals(nulls, pairsPlusOne.collect(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        LazyIterable<Pair<Integer, Object>> pairsMinusOne = this.lazyIterable.zip(nullsMinusOne);
        Assert.assertEquals(this.lazyIterable.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(this.lazyIterable.containsAllIterable(pairsMinusOne.collect(Functions.<Integer>firstOfPair())));

        Assert.assertEquals(
                this.lazyIterable.zip(nulls).toSet(),
                this.lazyIterable.zip(nulls, UnifiedSet.<Pair<Integer, Object>>newSet()));
    }

    @Test
    public void zipWithIndex()
    {
        LazyIterable<Pair<Integer, Integer>> pairs = this.lazyIterable.zipWithIndex();

        Assert.assertEquals(
                this.lazyIterable.toSet(),
                pairs.collect(Functions.<Integer>firstOfPair()).toSet());
        Assert.assertEquals(
                Interval.zeroTo(this.lazyIterable.size() - 1).toSet(),
                pairs.collect(Functions.<Integer>secondOfPair(), UnifiedSet.<Integer>newSet()));

        Assert.assertEquals(
                this.lazyIterable.zipWithIndex().toSet(),
                this.lazyIterable.zipWithIndex(UnifiedSet.<Pair<Integer, Integer>>newSet()));
    }

    @Test
    public void chunk()
    {
        LazyIterable<RichIterable<Integer>> groups = this.lazyIterable.chunk(2);
        RichIterable<Integer> sizes = groups.collect(new Function<RichIterable<Integer>, Integer>()
        {
            public Integer valueOf(RichIterable<Integer> richIterable)
            {
                return richIterable.size();
            }
        });
        Assert.assertEquals(Bags.mutable.of(2, 2, 2, 1), sizes.toBag());
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.lazyIterable.chunk(0);
    }

    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.lazyIterable.toBag(), this.lazyIterable.chunk(10).getFirst().toBag());
    }

    @Test
    public void asLazy()
    {
        Assert.assertSame(this.lazyIterable, this.lazyIterable.asLazy());
    }

    @Test
    public void flatCollect()
    {
        LazyIterable<Integer> collection = this.newWith(1, 2, 3, 4);
        Function<Integer, MutableList<String>> function =
                new Function<Integer, MutableList<String>>()
                {
                    public MutableList<String> valueOf(Integer object)
                    {
                        return FastList.newListWith(String.valueOf(object));
                    }
                };

        Verify.assertListsEqual(
                FastList.newListWith("1", "2", "3", "4"),
                collection.flatCollect(function).toSortedList());

        Verify.assertSetsEqual(
                UnifiedSet.newSetWith("1", "2", "3", "4"),
                collection.flatCollect(function, UnifiedSet.<String>newSet()));
    }
}
