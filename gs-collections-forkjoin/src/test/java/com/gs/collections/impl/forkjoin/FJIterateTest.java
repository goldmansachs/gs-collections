/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions0;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.StringFunctions;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.ArrayListAdapter;
import com.gs.collections.impl.list.mutable.CompositeFastList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.ListAdapter;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.bag.SynchronizedPutHashBagMultimap;
import com.gs.collections.impl.multimap.set.SynchronizedPutUnifiedSetMultimap;
import com.gs.collections.impl.parallel.AbstractProcedureCombiner;
import com.gs.collections.impl.parallel.PassThruCombiner;
import com.gs.collections.impl.parallel.PassThruObjectIntProcedureFactory;
import com.gs.collections.impl.parallel.PassThruProcedureFactory;
import com.gs.collections.impl.parallel.ProcedureFactory;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FJIterateTest
{
    private static final Procedure<Integer> EXCEPTION_PROCEDURE = new Procedure<Integer>()
    {
        public void value(Integer value)
        {
            throw new RuntimeException("Thread death on its way!");
        }
    };

    private static final ObjectIntProcedure<Integer> EXCEPTION_OBJECT_INT_PROCEDURE = new ObjectIntProcedure<Integer>()
    {
        public void value(Integer object, int index)
        {
            throw new RuntimeException("Thread death on its way!");
        }
    };

    private static final Function<Integer, Collection<String>> INT_TO_TWO_STRINGS = new Function<Integer, Collection<String>>()
    {
        public Collection<String> valueOf(Integer integer)
        {
            return Lists.fixedSize.of(integer.toString(), integer.toString());
        }
    };

    private static final Function0<AtomicInteger> ATOMIC_INTEGER_NEW = Functions0.zeroAtomicInteger();

    private static final Function0<Integer> INTEGER_NEW = Functions0.value(0);

    private static final Function<Integer, String> EVEN_OR_ODD = new Function<Integer, String>()
    {
        public String valueOf(Integer value)
        {
            return value % 2 == 0 ? "Even" : "Odd";
        }
    };

    private ImmutableList<RichIterable<Integer>> iterables;
    private final ForkJoinPool executor = new ForkJoinPool(2);

    @Before
    public void setUp()
    {
        Interval interval = Interval.oneTo(200);
        this.iterables = Lists.immutable.of(
                interval.toList(),
                interval.toList().asUnmodifiable(),
                interval.toList().asSynchronized(),
                interval.toList().toImmutable(),
                interval.toSet(),
                interval.toSet().asUnmodifiable(),
                interval.toSet().asSynchronized(),
                interval.toSet().toImmutable(),
                interval.toBag(),
                interval.toBag().asUnmodifiable(),
                interval.toBag().asSynchronized(),
                interval.toBag().toImmutable(),
                interval.toSortedSet(),
                interval.toSortedSet().asUnmodifiable(),
                interval.toSortedSet().asSynchronized(),
                interval.toSortedSet().toImmutable(),
                interval.toMap(Functions.<Integer>getPassThru(), Functions.<Integer>getPassThru()),
                interval.toMap(Functions.<Integer>getPassThru(), Functions.<Integer>getPassThru()).asUnmodifiable(),
                interval.toMap(Functions.<Integer>getPassThru(), Functions.<Integer>getPassThru()).asSynchronized(),
                interval.toMap(Functions.<Integer>getPassThru(), Functions.<Integer>getPassThru()).toImmutable(),
                new CompositeFastList<Integer>().withAll(interval.toList()),
                new CompositeFastList<Integer>().withAll(interval.toList()).asUnmodifiable(),
                new CompositeFastList<Integer>().withAll(interval.toList()).asSynchronized(),
                ArrayListAdapter.<Integer>newList().withAll(interval),
                ArrayListAdapter.<Integer>newList().withAll(interval).asUnmodifiable(),
                ArrayListAdapter.<Integer>newList().withAll(interval).asSynchronized(),
                ListAdapter.adapt(new LinkedList<Integer>()).withAll(interval),
                ListAdapter.adapt(new LinkedList<Integer>()).withAll(interval).asUnmodifiable(),
                ListAdapter.adapt(new LinkedList<Integer>()).withAll(interval).asSynchronized(),
                UnifiedSetWithHashingStrategy.<Integer>newSet(HashingStrategies.defaultStrategy()).withAll(interval),
                UnifiedSetWithHashingStrategy.<Integer>newSet(HashingStrategies.defaultStrategy()).withAll(interval).asUnmodifiable(),
                UnifiedSetWithHashingStrategy.<Integer>newSet(HashingStrategies.defaultStrategy()).withAll(interval).asSynchronized(),
                UnifiedSetWithHashingStrategy.<Integer>newSet(HashingStrategies.defaultStrategy()).withAll(interval).toImmutable());
    }

    @After
    public void tearDown()
    {
        this.executor.shutdown();
    }

    @Test
    public void testForEachUsingSet()
    {
        //Tests the default batch size calculations
        IntegerSum sum = new IntegerSum(0);
        MutableSet<Integer> set = Interval.toSet(1, 100);
        FJIterate.forEach(set, new SumProcedure(sum), new SumCombiner(sum));
        Assert.assertEquals(5050, sum.getSum());

        //Testing batch size 1
        IntegerSum sum2 = new IntegerSum(0);
        UnifiedSet<Integer> set2 = UnifiedSet.newSet(Interval.oneTo(100));
        FJIterate.forEach(set2, new SumProcedure(sum2), new SumCombiner(sum2), 1, set2.getBatchCount(set2.size()));
        Assert.assertEquals(5050, sum2.getSum());

        //Testing an uneven batch size
        IntegerSum sum3 = new IntegerSum(0);
        UnifiedSet<Integer> set3 = UnifiedSet.newSet(Interval.oneTo(100));
        FJIterate.forEach(set3, new SumProcedure(sum3), new SumCombiner(sum3), 1, set3.getBatchCount(13));
        Assert.assertEquals(5050, sum3.getSum());

        //Testing divideByZero exception by passing 1 as batchSize
        IntegerSum sum4 = new IntegerSum(0);
        UnifiedSet<Integer> set4 = UnifiedSet.newSet(Interval.oneTo(100));
        FJIterate.forEach(set4, new SumProcedure(sum4), new SumCombiner(sum4), 1);
        Assert.assertEquals(5050, sum4.getSum());
    }

    @Test
    public void testForEachUsingMap()
    {
        //Test the default batch size calculations
        IntegerSum sum1 = new IntegerSum(0);
        MutableMap<String, Integer> map1 = Interval.fromTo(1, 100).toMap(Functions.getToString(), Functions.getIntegerPassThru());
        FJIterate.forEach(map1, new SumProcedure(sum1), new SumCombiner(sum1));
        Assert.assertEquals(5050, sum1.getSum());

        //Testing batch size 1
        IntegerSum sum2 = new IntegerSum(0);
        UnifiedMap<String, Integer> map2 = (UnifiedMap<String, Integer>) Interval.fromTo(1, 100).toMap(Functions.getToString(), Functions.getIntegerPassThru());
        FJIterate.forEach(map2, new SumProcedure(sum2), new SumCombiner(sum2), 1, map2.getBatchCount(map2.size()));
        Assert.assertEquals(5050, sum2.getSum());

        //Testing an uneven batch size
        IntegerSum sum3 = new IntegerSum(0);
        UnifiedMap<String, Integer> set3 = (UnifiedMap<String, Integer>) Interval.fromTo(1, 100).toMap(Functions.getToString(), Functions.getIntegerPassThru());
        FJIterate.forEach(set3, new SumProcedure(sum3), new SumCombiner(sum3), 1, set3.getBatchCount(13));
        Assert.assertEquals(5050, sum3.getSum());
    }

    @Test
    public void testForEach()
    {
        IntegerSum sum1 = new IntegerSum(0);
        List<Integer> list1 = FJIterateTest.createIntegerList(16);
        FJIterate.forEach(list1, new SumProcedure(sum1), new SumCombiner(sum1), 1, list1.size() / 2);
        Assert.assertEquals(16, sum1.getSum());

        IntegerSum sum2 = new IntegerSum(0);
        List<Integer> list2 = FJIterateTest.createIntegerList(7);
        FJIterate.forEach(list2, new SumProcedure(sum2), new SumCombiner(sum2));
        Assert.assertEquals(7, sum2.getSum());

        IntegerSum sum3 = new IntegerSum(0);
        List<Integer> list3 = FJIterateTest.createIntegerList(15);
        FJIterate.forEach(list3, new SumProcedure(sum3), new SumCombiner(sum3), 1, list3.size() / 2);
        Assert.assertEquals(15, sum3.getSum());

        IntegerSum sum4 = new IntegerSum(0);
        List<Integer> list4 = FJIterateTest.createIntegerList(35);
        FJIterate.forEach(list4, new SumProcedure(sum4), new SumCombiner(sum4));
        Assert.assertEquals(35, sum4.getSum());

        IntegerSum sum5 = new IntegerSum(0);
        MutableList<Integer> list5 = FastList.newList(list4);
        FJIterate.forEach(list5, new SumProcedure(sum5), new SumCombiner(sum5));
        Assert.assertEquals(35, sum5.getSum());

        IntegerSum sum6 = new IntegerSum(0);
        List<Integer> list6 = FJIterateTest.createIntegerList(40);
        FJIterate.forEach(list6, new SumProcedure(sum6), new SumCombiner(sum6), 1, list6.size() / 2);
        Assert.assertEquals(40, sum6.getSum());

        IntegerSum sum7 = new IntegerSum(0);
        MutableList<Integer> list7 = FastList.newList(list6);
        FJIterate.forEach(list7, new SumProcedure(sum7), new SumCombiner(sum7), 1, list6.size() / 2);
        Assert.assertEquals(40, sum7.getSum());
    }

    @Test
    public void testForEachImmutable()
    {
        IntegerSum sum1 = new IntegerSum(0);
        ImmutableList<Integer> list1 = Lists.immutable.ofAll(FJIterateTest.createIntegerList(16));
        FJIterate.forEach(list1, new SumProcedure(sum1), new SumCombiner(sum1), 1, list1.size() / 2);
        Assert.assertEquals(16, sum1.getSum());

        IntegerSum sum2 = new IntegerSum(0);
        ImmutableList<Integer> list2 = Lists.immutable.ofAll(FJIterateTest.createIntegerList(7));
        FJIterate.forEach(list2, new SumProcedure(sum2), new SumCombiner(sum2));
        Assert.assertEquals(7, sum2.getSum());

        IntegerSum sum3 = new IntegerSum(0);
        ImmutableList<Integer> list3 = Lists.immutable.ofAll(FJIterateTest.createIntegerList(15));
        FJIterate.forEach(list3, new SumProcedure(sum3), new SumCombiner(sum3), 1, list3.size() / 2);
        Assert.assertEquals(15, sum3.getSum());

        IntegerSum sum4 = new IntegerSum(0);
        ImmutableList<Integer> list4 = Lists.immutable.ofAll(FJIterateTest.createIntegerList(35));
        FJIterate.forEach(list4, new SumProcedure(sum4), new SumCombiner(sum4));
        Assert.assertEquals(35, sum4.getSum());

        IntegerSum sum5 = new IntegerSum(0);
        ImmutableList<Integer> list5 = FastList.newList(list4).toImmutable();
        FJIterate.forEach(list5, new SumProcedure(sum5), new SumCombiner(sum5));
        Assert.assertEquals(35, sum5.getSum());

        IntegerSum sum6 = new IntegerSum(0);
        ImmutableList<Integer> list6 = Lists.immutable.ofAll(FJIterateTest.createIntegerList(40));
        FJIterate.forEach(list6, new SumProcedure(sum6), new SumCombiner(sum6), 1, list6.size() / 2);
        Assert.assertEquals(40, sum6.getSum());

        IntegerSum sum7 = new IntegerSum(0);
        ImmutableList<Integer> list7 = FastList.newList(list6).toImmutable();
        FJIterate.forEach(list7, new SumProcedure(sum7), new SumCombiner(sum7), 1, list6.size() / 2);
        Assert.assertEquals(40, sum7.getSum());
    }

    @Test
    public void testForEachWithException()
    {
        Verify.assertThrows(RuntimeException.class, new Runnable()
        {
            public void run()
            {
                FJIterate.forEach(
                        FJIterateTest.createIntegerList(5),
                        new PassThruProcedureFactory<>(EXCEPTION_PROCEDURE),
                        new PassThruCombiner<Procedure<Integer>>(),
                        1,
                        5);
            }
        });
    }

    @Test
    public void testForEachWithIndexToArrayUsingFastListSerialPath()
    {
        final Integer[] array = new Integer[200];
        FastList<Integer> list = (FastList<Integer>) Interval.oneTo(200).toList();
        Assert.assertTrue(ArrayIterate.allSatisfy(array, Predicates.isNull()));
        FJIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                array[index] = each;
            }
        });
        Assert.assertArrayEquals(array, list.toArray(new Integer[]{}));
    }

    @Test
    public void testForEachWithIndexToArrayUsingFastList()
    {
        final Integer[] array = new Integer[200];
        FastList<Integer> list = (FastList<Integer>) Interval.oneTo(200).toList();
        Assert.assertTrue(ArrayIterate.allSatisfy(array, Predicates.isNull()));
        FJIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                array[index] = each;
            }
        }, 10, 10);
        Assert.assertArrayEquals(array, list.toArray(new Integer[]{}));
    }

    @Test
    public void testForEachWithIndexToArrayUsingImmutableList()
    {
        final Integer[] array = new Integer[200];
        ImmutableList<Integer> list = Interval.oneTo(200).toList().toImmutable();
        Assert.assertTrue(ArrayIterate.allSatisfy(array, Predicates.isNull()));
        FJIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                array[index] = each;
            }
        }, 10, 10);
        Assert.assertArrayEquals(array, list.toArray(new Integer[]{}));
    }

    @Test
    public void testForEachWithIndexToArrayUsingArrayList()
    {
        final Integer[] array = new Integer[200];
        MutableList<Integer> list = FastList.newList(Interval.oneTo(200));
        Assert.assertTrue(ArrayIterate.allSatisfy(array, Predicates.isNull()));
        FJIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                array[index] = each;
            }
        }, 10, 10);
        Assert.assertArrayEquals(array, list.toArray(new Integer[]{}));
    }

    @Test
    public void testForEachWithIndexToArrayUsingFixedArrayList()
    {
        final Integer[] array = new Integer[10];
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Assert.assertTrue(ArrayIterate.allSatisfy(array, Predicates.isNull()));
        FJIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                array[index] = each;
            }
        }, 1, 2);
        Assert.assertArrayEquals(array, list.toArray(new Integer[list.size()]));
    }

    @Test
    public void testForEachWithIndexException()
    {
        Verify.assertThrows(RuntimeException.class, new Runnable()
        {
            public void run()
            {
                FJIterate.forEachWithIndex(
                        FJIterateTest.createIntegerList(5),
                        new PassThruObjectIntProcedureFactory<>(EXCEPTION_OBJECT_INT_PROCEDURE),
                        new PassThruCombiner<ObjectIntProcedure<Integer>>(),
                        1,
                        5);
            }
        });
    }

    @Test
    public void select()
    {
        this.iterables.forEach(new Procedure<RichIterable<Integer>>()
        {
            public void value(RichIterable<Integer> each)
            {
                FJIterateTest.this.basicSelect(each);
            }
        });
    }

    private void basicSelect(RichIterable<Integer> iterable)
    {
        Collection<Integer> actual1 = FJIterate.select(iterable, Predicates.greaterThan(100));
        Collection<Integer> actual2 = FJIterate.select(iterable, Predicates.greaterThan(100), HashBag.<Integer>newBag(), 3, this.executor, true);
        Collection<Integer> actual3 = FJIterate.select(iterable, Predicates.greaterThan(100), true);
        RichIterable<Integer> expected = iterable.select(Predicates.greaterThan(100));
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual1.getClass().getSimpleName(), expected, actual1);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual2.getClass().getSimpleName(), expected.toBag(), actual2);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual3.getClass().getSimpleName(), expected, actual3);
    }

    @Test
    public void selectSortedSet()
    {
        RichIterable<Integer> iterable = Interval.oneTo(200).toSortedSet();
        Collection<Integer> actual1 = FJIterate.select(iterable, Predicates.greaterThan(100));
        Collection<Integer> actual2 = FJIterate.select(iterable, Predicates.greaterThan(100), true);
        RichIterable<Integer> expected = iterable.select(Predicates.greaterThan(100));
        Assert.assertSame(expected.getClass(), actual1.getClass());
        Assert.assertSame(expected.getClass(), actual2.getClass());
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual1.getClass().getSimpleName(), expected, actual1);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual2.getClass().getSimpleName(), expected, actual2);
    }

    @Test
    public void count()
    {
        this.iterables.forEach(new Procedure<RichIterable<Integer>>()
        {
            public void value(RichIterable<Integer> each)
            {
                FJIterateTest.this.basicCount(each);
            }
        });
    }

    private void basicCount(RichIterable<Integer> iterable)
    {
        int actual1 = FJIterate.count(iterable, Predicates.greaterThan(100));
        int actual2 = FJIterate.count(iterable, Predicates.greaterThan(100), 6, this.executor);
        Assert.assertEquals(100, actual1);
        Assert.assertEquals(100, actual2);
    }

    @Test
    public void reject()
    {
        this.iterables.forEach(new Procedure<RichIterable<Integer>>()
        {
            public void value(RichIterable<Integer> each)
            {
                FJIterateTest.this.basicReject(each);
            }
        });
    }

    private void basicReject(RichIterable<Integer> iterable)
    {
        Collection<Integer> actual1 = FJIterate.reject(iterable, Predicates.greaterThan(100));
        Collection<Integer> actual2 = FJIterate.reject(iterable, Predicates.greaterThan(100), HashBag.<Integer>newBag(), 3, this.executor, true);
        Collection<Integer> actual3 = FJIterate.reject(iterable, Predicates.greaterThan(100), true);
        RichIterable<Integer> expected = iterable.reject(Predicates.greaterThan(100));
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual1.getClass().getSimpleName(), expected, actual1);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual2.getClass().getSimpleName(), expected.toBag(), actual2);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual3.getClass().getSimpleName(), expected, actual3);
    }

    @Test
    public void collect()
    {
        this.iterables.forEach(new Procedure<RichIterable<Integer>>()
        {
            public void value(RichIterable<Integer> each)
            {
                FJIterateTest.this.basicCollect(each);
            }
        });
    }

    private void basicCollect(RichIterable<Integer> iterable)
    {
        Collection<String> actual1 = FJIterate.collect(iterable, Functions.getToString());
        Collection<String> actual2 = FJIterate.collect(iterable, Functions.getToString(), HashBag.<String>newBag(), 3, this.executor, false);
        Collection<String> actual3 = FJIterate.collect(iterable, Functions.getToString(), true);
        RichIterable<String> expected = iterable.collect(Functions.getToString());
        Verify.assertSize(200, actual1);
        Verify.assertContains(String.valueOf(200), actual1);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual1.getClass().getSimpleName(), expected, actual1);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual2.getClass().getSimpleName(), expected.toBag(), actual2);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual3.getClass().getSimpleName(), expected.toBag(), HashBag.newBag(actual3));
    }

    @Test
    public void collectIf()
    {
        this.iterables.forEach(new Procedure<RichIterable<Integer>>()
        {
            public void value(RichIterable<Integer> each)
            {
                FJIterateTest.this.basicCollectIf(each);
            }
        });
    }

    private void basicCollectIf(RichIterable<Integer> collection)
    {
        Predicate<Integer> greaterThan = Predicates.greaterThan(100);
        Collection<String> actual1 = FJIterate.collectIf(collection, greaterThan, Functions.getToString());
        Collection<String> actual2 = FJIterate.collectIf(collection, greaterThan, Functions.getToString(), HashBag.<String>newBag(), 3, this.executor, true);
        Collection<String> actual3 = FJIterate.collectIf(collection, greaterThan, Functions.getToString(), HashBag.<String>newBag(), 3, this.executor, true);
        Bag<String> expected = collection.collectIf(greaterThan, Functions.getToString()).toBag();
        Verify.assertSize(100, actual1);
        Verify.assertNotContains(String.valueOf(90), actual1);
        Verify.assertNotContains(String.valueOf(210), actual1);
        Verify.assertContains(String.valueOf(159), actual1);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual1.getClass().getSimpleName(), expected, HashBag.newBag(actual1));
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual2.getClass().getSimpleName(), expected, actual2);
        Assert.assertEquals(expected.getClass().getSimpleName() + '/' + actual3.getClass().getSimpleName(), expected, actual3);
    }

    @Test
    public void groupByWithInterval()
    {
        LazyIterable<Integer> iterable = Interval.oneTo(1000).concatenate(Interval.oneTo(1000)).concatenate(Interval.oneTo(1000));
        Multimap<String, Integer> expected = iterable.toBag().groupBy(Functions.getToString());
        Multimap<String, Integer> expectedAsSet = iterable.toSet().groupBy(Functions.getToString());
        Multimap<String, Integer> result1 = FJIterate.groupBy(iterable.toList(), Functions.getToString(), 100);
        Multimap<String, Integer> result2 = FJIterate.groupBy(iterable.toList(), Functions.getToString());
        Multimap<String, Integer> result3 = FJIterate.groupBy(iterable.toSet(), Functions.getToString(), SynchronizedPutUnifiedSetMultimap.<String, Integer>newMultimap(), 100);
        Multimap<String, Integer> result4 = FJIterate.groupBy(iterable.toSet(), Functions.getToString(), SynchronizedPutUnifiedSetMultimap.<String, Integer>newMultimap());
        Multimap<String, Integer> result5 = FJIterate.groupBy(iterable.toSortedSet(), Functions.getToString(), SynchronizedPutUnifiedSetMultimap.<String, Integer>newMultimap(), 100);
        Multimap<String, Integer> result6 = FJIterate.groupBy(iterable.toSortedSet(), Functions.getToString(), SynchronizedPutUnifiedSetMultimap.<String, Integer>newMultimap());
        Multimap<String, Integer> result7 = FJIterate.groupBy(iterable.toBag(), Functions.getToString(), SynchronizedPutHashBagMultimap.<String, Integer>newMultimap(), 100);
        Multimap<String, Integer> result8 = FJIterate.groupBy(iterable.toBag(), Functions.getToString(), SynchronizedPutHashBagMultimap.<String, Integer>newMultimap());
        Multimap<String, Integer> result9 = FJIterate.groupBy(iterable.toList().toImmutable(), Functions.getToString());
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result1));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result2));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result9));
        Assert.assertEquals(expectedAsSet, result3);
        Assert.assertEquals(expectedAsSet, result4);
        Assert.assertEquals(expectedAsSet, result5);
        Assert.assertEquals(expectedAsSet, result6);
        Assert.assertEquals(expected, result7);
        Assert.assertEquals(expected, result8);
    }

    @Test
    public void groupBy()
    {
        FastList<String> source = FastList.newListWith("Ted", "Sally", "Mary", "Bob", "Sara");
        Multimap<Character, String> result1 = FJIterate.groupBy(source, StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result2 = FJIterate.groupBy(Collections.synchronizedList(source), StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result3 = FJIterate.groupBy(Collections.synchronizedCollection(source), StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result4 = FJIterate.groupBy(LazyIterate.adapt(source), StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result5 = FJIterate.groupBy(new ArrayList<>(source), StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result6 = FJIterate.groupBy(source.toSet(), StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result7 = FJIterate.groupBy(source.toMap(Functions.getStringPassThru(), Functions.getStringPassThru()), StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result8 = FJIterate.groupBy(source.toBag(), StringFunctions.firstLetter(), 1);
        Multimap<Character, String> result9 = FJIterate.groupBy(source.toImmutable(), StringFunctions.firstLetter(), 1);
        MutableMultimap<Character, String> expected = HashBagMultimap.newMultimap();
        expected.put('T', "Ted");
        expected.put('S', "Sally");
        expected.put('M', "Mary");
        expected.put('B', "Bob");
        expected.put('S', "Sara");
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result1));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result2));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result3));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result4));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result5));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result6));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result7));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result8));
        Assert.assertEquals(expected, HashBagMultimap.newMultimap(result9));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                FJIterate.groupBy(null, null, 1);
            }
        });
    }

    @Test
    public void aggregateInPlaceBy()
    {
        Procedure2<AtomicInteger, Integer> countAggregator = new Procedure2<AtomicInteger, Integer>()
        {
            public void value(AtomicInteger aggregate, Integer value)
            {
                aggregate.incrementAndGet();
            }
        };
        List<Integer> list = Interval.oneTo(2000);
        MutableMap<String, AtomicInteger> aggregation =
                FJIterate.aggregateInPlaceBy(list, EVEN_OR_ODD, ATOMIC_INTEGER_NEW, countAggregator);
        Assert.assertEquals(1000, aggregation.get("Even").intValue());
        Assert.assertEquals(1000, aggregation.get("Odd").intValue());
        FJIterate.aggregateInPlaceBy(list, EVEN_OR_ODD, ATOMIC_INTEGER_NEW, countAggregator, aggregation);
        Assert.assertEquals(2000, aggregation.get("Even").intValue());
        Assert.assertEquals(2000, aggregation.get("Odd").intValue());
    }

    @Test
    public void aggregateInPlaceByWithBatchSize()
    {
        Procedure2<AtomicInteger, Integer> sumAggregator = new Procedure2<AtomicInteger, Integer>()
        {
            public void value(AtomicInteger aggregate, Integer value)
            {
                aggregate.addAndGet(value);
            }
        };
        MutableList<Integer> list = LazyIterate.adapt(Collections.nCopies(100, 1))
                .concatenate(Collections.nCopies(200, 2))
                .concatenate(Collections.nCopies(300, 3))
                .toList()
                .shuffleThis();
        MapIterable<String, AtomicInteger> aggregation =
                FJIterate.aggregateInPlaceBy(list, Functions.getToString(), ATOMIC_INTEGER_NEW, sumAggregator, 50);
        Assert.assertEquals(100, aggregation.get("1").intValue());
        Assert.assertEquals(400, aggregation.get("2").intValue());
        Assert.assertEquals(900, aggregation.get("3").intValue());
    }

    @Test
    public void aggregateBy()
    {
        Function2<Integer, Integer, Integer> countAggregator = new Function2<Integer, Integer, Integer>()
        {
            public Integer value(Integer aggregate, Integer value)
            {
                return aggregate + 1;
            }
        };
        List<Integer> list = Interval.oneTo(20000);
        MutableMap<String, Integer> aggregation =
                FJIterate.aggregateBy(list, EVEN_OR_ODD, INTEGER_NEW, countAggregator);
        Assert.assertEquals(10000, aggregation.get("Even").intValue());
        Assert.assertEquals(10000, aggregation.get("Odd").intValue());
        FJIterate.aggregateBy(list, EVEN_OR_ODD, INTEGER_NEW, countAggregator, aggregation);
        Assert.assertEquals(20000, aggregation.get("Even").intValue());
        Assert.assertEquals(20000, aggregation.get("Odd").intValue());
    }

    @Test
    public void aggregateByWithBatchSize()
    {
        Function2<Integer, Integer, Integer> sumAggregator = new Function2<Integer, Integer, Integer>()
        {
            public Integer value(Integer aggregate, Integer value)
            {
                return aggregate + value;
            }
        };
        MutableList<Integer> list = LazyIterate.adapt(Collections.nCopies(1000, 1))
                .concatenate(Collections.nCopies(2000, 2))
                .concatenate(Collections.nCopies(3000, 3))
                .toList()
                .shuffleThis();
        MapIterable<String, Integer> aggregation =
                FJIterate.aggregateBy(list, Functions.getToString(), INTEGER_NEW, sumAggregator, 100);
        Assert.assertEquals(1000, aggregation.get("1").intValue());
        Assert.assertEquals(4000, aggregation.get("2").intValue());
        Assert.assertEquals(9000, aggregation.get("3").intValue());
    }

    private static List<Integer> createIntegerList(int size)
    {
        return Collections.nCopies(size, Integer.valueOf(1));
    }

    @Test
    public void flatCollect()
    {
        this.iterables.forEach(new Procedure<RichIterable<Integer>>()
        {
            public void value(RichIterable<Integer> each)
            {
                FJIterateTest.this.basicFlatCollect(each);
            }
        });
    }

    private void basicFlatCollect(RichIterable<Integer> iterable)
    {
        Collection<String> actual1 = FJIterate.flatCollect(iterable, INT_TO_TWO_STRINGS);
        Collection<String> actual2 = FJIterate.flatCollect(iterable, INT_TO_TWO_STRINGS, HashBag.<String>newBag(), 3, this.executor, false);
        Collection<String> actual3 = FJIterate.flatCollect(iterable, INT_TO_TWO_STRINGS, true);
        RichIterable<String> expected1 = iterable.flatCollect(INT_TO_TWO_STRINGS);
        RichIterable<String> expected2 = iterable.flatCollect(INT_TO_TWO_STRINGS, HashBag.<String>newBag());
        Verify.assertContains(String.valueOf(200), actual1);
        Assert.assertEquals(expected1.getClass().getSimpleName() + '/' + actual1.getClass().getSimpleName(), expected1, actual1);
        Assert.assertEquals(expected2.getClass().getSimpleName() + '/' + actual2.getClass().getSimpleName(), expected2, actual2);
        Assert.assertEquals(expected1.getClass().getSimpleName() + '/' + actual3.getClass().getSimpleName(), expected1, actual3);
    }

    public static final class IntegerSum
    {
        private int sum;

        public IntegerSum(int newSum)
        {
            this.sum = newSum;
        }

        public IntegerSum add(int value)
        {
            this.sum += value;
            return this;
        }

        public int getSum()
        {
            return this.sum;
        }
    }

    public static final class SumProcedure
            implements Procedure<Integer>, Function2<IntegerSum, Integer, IntegerSum>, ProcedureFactory<SumProcedure>
    {
        private static final long serialVersionUID = 1L;

        private final IntegerSum sum;

        public SumProcedure(IntegerSum newSum)
        {
            this.sum = newSum;
        }

        @Override
        public SumProcedure create()
        {
            return new SumProcedure(new IntegerSum(0));
        }

        @Override
        public IntegerSum value(IntegerSum s1, Integer s2)
        {
            return s1.add(s2);
        }

        @Override
        public void value(Integer object)
        {
            this.sum.add(object);
        }

        public int getSum()
        {
            return this.sum.getSum();
        }
    }

    public static final class SumCombiner extends AbstractProcedureCombiner<SumProcedure>
    {
        private static final long serialVersionUID = 1L;
        private final IntegerSum sum;

        public SumCombiner(IntegerSum initialSum)
        {
            super(true);
            this.sum = initialSum;
        }

        @Override
        public void combineOne(SumProcedure sumProcedure)
        {
            this.sum.add(sumProcedure.getSum());
        }
    }
}
