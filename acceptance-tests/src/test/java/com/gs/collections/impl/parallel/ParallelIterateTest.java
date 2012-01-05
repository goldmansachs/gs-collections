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

package com.gs.collections.impl.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.MultiReaderUnifiedSet;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.ArrayIterate;
import org.junit.Assert;
import org.junit.Test;

public class ParallelIterateTest
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

    private static final MutableSet<Integer> INTEGER_SET = Interval.toSet(1, 20000);
    private static final MutableList<Integer> INTEGER_LIST = Interval.oneTo(20000).toList();

    private static final Function<Integer, Collection<String>> INT_TO_TWO_STRINGS = new Function<Integer, Collection<String>>()
    {
        public Collection<String> valueOf(Integer integer)
        {
            return Lists.fixedSize.of(integer.toString(), integer.toString());
        }
    };

    private int count;
    private final MutableSet<String> threadNames = MultiReaderUnifiedSet.newSet();

    @Test
    public void testOneLevelCall()
    {
        new RecursiveProcedure().value(1);

        synchronized (this)
        {
            Assert.assertEquals("all iterations completed", 20000, this.count);
        }
    }

    @Test
    public void testNestedCall()
    {
        new RecursiveProcedure().value(2);

        synchronized (this)
        {
            Assert.assertEquals("all iterations completed", 419980, this.count);
        }
        Assert.assertTrue("uses multiple threads", this.threadNames.size() > 1);
    }

    @Test
    public void testForEachUsingSet()
    {
        //Tests the default batch size calculations
        IntegerSum sum = new IntegerSum(0);
        MutableSet<Integer> set = Interval.toSet(1, 10000);
        ParallelIterate.forEach(set, new SumProcedure(sum), new SumCombiner(sum));
        Assert.assertEquals(50005000, sum.getSum());

        //Testing batch size 1
        IntegerSum sum2 = new IntegerSum(0);
        UnifiedSet<Integer> set2 = UnifiedSet.newSet(Interval.oneTo(100));
        ParallelIterate.forEach(set2, new SumProcedure(sum2), new SumCombiner(sum2), 1, set2.getBatchCount(set2.size()));
        Assert.assertEquals(5050, sum2.getSum());

        //Testing an uneven batch size
        IntegerSum sum3 = new IntegerSum(0);
        UnifiedSet<Integer> set3 = UnifiedSet.newSet(Interval.oneTo(100));
        ParallelIterate.forEach(set3, new SumProcedure(sum3), new SumCombiner(sum3), 1, set3.getBatchCount(13));
        Assert.assertEquals(5050, sum3.getSum());

        //Testing divideByZero exception by passing 1 as batchSize
        IntegerSum sum4 = new IntegerSum(0);
        UnifiedSet<Integer> set4 = UnifiedSet.newSet(Interval.oneTo(100));
        ParallelIterate.forEach(set4, new SumProcedure(sum4), new SumCombiner(sum4), 1);
        Assert.assertEquals(5050, sum4.getSum());
    }

    @Test
    public void testForEachUsingMap()
    {
        //Test the default batch size calculations
        IntegerSum sum1 = new IntegerSum(0);
        MutableMap<String, Integer> map1 = Interval.fromTo(1, 10000).toMap(Functions.getToString(), Functions.getIntegerPassThru());
        ParallelIterate.forEach(map1, new SumProcedure(sum1), new SumCombiner(sum1));
        Assert.assertEquals(50005000, sum1.getSum());

        //Testing batch size 1
        IntegerSum sum2 = new IntegerSum(0);
        UnifiedMap<String, Integer> map2 = (UnifiedMap<String, Integer>) Interval.fromTo(1, 100).toMap(Functions.getToString(), Functions.getIntegerPassThru());
        ParallelIterate.forEach(map2, new SumProcedure(sum2), new SumCombiner(sum2), 1, map2.getBatchCount(map2.size()));
        Assert.assertEquals(5050, sum2.getSum());

        //Testing an uneven batch size
        IntegerSum sum3 = new IntegerSum(0);
        UnifiedMap<String, Integer> set3 = (UnifiedMap<String, Integer>) Interval.fromTo(1, 100).toMap(Functions.getToString(), Functions.getIntegerPassThru());
        ParallelIterate.forEach(set3, new SumProcedure(sum3), new SumCombiner(sum3), 1, set3.getBatchCount(13));
        Assert.assertEquals(5050, sum3.getSum());
    }

    @Test
    public void testForEach()
    {
        IntegerSum sum1 = new IntegerSum(0);
        List<Integer> list1 = createIntegerList(16);
        ParallelIterate.forEach(list1, new SumProcedure(sum1), new SumCombiner(sum1), 1, list1.size() / 2);
        Assert.assertEquals(16, sum1.getSum());

        IntegerSum sum2 = new IntegerSum(0);
        List<Integer> list2 = createIntegerList(7);
        ParallelIterate.forEach(list2, new SumProcedure(sum2), new SumCombiner(sum2));
        Assert.assertEquals(7, sum2.getSum());

        IntegerSum sum3 = new IntegerSum(0);
        List<Integer> list3 = createIntegerList(15);
        ParallelIterate.forEach(list3, new SumProcedure(sum3), new SumCombiner(sum3), 1, list3.size() / 2);
        Assert.assertEquals(15, sum3.getSum());

        IntegerSum sum4 = new IntegerSum(0);
        List<Integer> list4 = createIntegerList(35);
        ParallelIterate.forEach(list4, new SumProcedure(sum4), new SumCombiner(sum4));
        Assert.assertEquals(35, sum4.getSum());

        IntegerSum sum5 = new IntegerSum(0);
        MutableList<Integer> list5 = FastList.newList(list4);
        ParallelIterate.forEach(list5, new SumProcedure(sum5), new SumCombiner(sum5));
        Assert.assertEquals(35, sum5.getSum());

        IntegerSum sum6 = new IntegerSum(0);
        List<Integer> list6 = createIntegerList(40);
        ParallelIterate.forEach(list6, new SumProcedure(sum6), new SumCombiner(sum6), 1, list6.size() / 2);
        Assert.assertEquals(40, sum6.getSum());

        IntegerSum sum7 = new IntegerSum(0);
        MutableList<Integer> list7 = FastList.newList(list6);
        ParallelIterate.forEach(list7, new SumProcedure(sum7), new SumCombiner(sum7), 1, list6.size() / 2);
        Assert.assertEquals(40, sum7.getSum());
    }

    @Test
    public void testForEachWithException()
    {
        Verify.assertThrows(RuntimeException.class, new Runnable()
        {
            public void run()
            {
                ParallelIterate.forEach(
                        createIntegerList(5),
                        new PassThruProcedureFactory<Procedure<Integer>>(EXCEPTION_PROCEDURE),
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
        ParallelIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
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
        ParallelIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
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
        List<Integer> list = new ArrayList<Integer>(Interval.oneTo(200));
        Assert.assertTrue(ArrayIterate.allSatisfy(array, Predicates.isNull()));
        ParallelIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
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
        ParallelIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
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
                ParallelIterate.forEachWithIndex(
                        createIntegerList(5),
                        new PassThruObjectIntProcedureFactory<ObjectIntProcedure<Integer>>(EXCEPTION_OBJECT_INT_PROCEDURE),
                        new PassThruCombiner<ObjectIntProcedure<Integer>>(),
                        1,
                        5);
            }
        });
    }

    @Test
    public void testSelect()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<Integer> result = ParallelIterate.select(collection, Predicates.greaterThan(10000));
        Verify.assertSize(10000, result);
        Verify.assertContains(20000, result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void testSelectUseCombineOne()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<Integer> result = ParallelIterate.select(collection, Predicates.greaterThan(10000), true);
        Verify.assertSize(10000, result);
        Verify.assertContains(20000, result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void testCount()
    {
        Collection<Integer> collection = INTEGER_LIST;
        int result = ParallelIterate.count(collection, Predicates.greaterThan(10000));
        Assert.assertEquals(10000, result);
    }

    @Test
    public void testReject()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<Integer> result = ParallelIterate.reject(collection, Predicates.greaterThan(10000));
        Verify.assertSize(10000, result);
        Verify.assertContains(1, result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void testRejectUseCombineOne()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<Integer> result = ParallelIterate.reject(collection, Predicates.greaterThan(10000), true);
        Verify.assertSize(10000, result);
        Verify.assertContains(1, result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void testSelectForSet()
    {
        Collection<Integer> collection = INTEGER_SET;
        Collection<Integer> result = ParallelIterate.select(collection, Predicates.greaterThan(10000));
        Verify.assertSize(10000, result);
        Verify.assertInstanceOf(Set.class, result);
    }

    @Test
    public void testSelectForSetToList()
    {
        Collection<Integer> collection = INTEGER_SET;
        Collection<Integer> result = ParallelIterate.select(
                collection,
                Predicates.greaterThan(10000),
                FastList.<Integer>newList(),
                true);
        Verify.assertSize(10000, result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void testCollect()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<String> result = ParallelIterate.collect(collection, Functions.getToString());
        Verify.assertSize(20000, result);
        Verify.assertContains(String.valueOf(20000), result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void testCollectUseCombineOne()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<String> result = ParallelIterate.collect(collection, Functions.getToString(), true);
        Verify.assertSize(20000, result);
        Verify.assertContains(String.valueOf(20000), result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void testCollectForSet()
    {
        Collection<Integer> collection = INTEGER_SET;
        Collection<String> result = ParallelIterate.collect(collection, Functions.getToString());
        Verify.assertSize(20000, result);
        Verify.assertContains(String.valueOf(20000), result);
        Verify.assertInstanceOf(Set.class, result);
    }

    @Test
    public void testCollectIf()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Predicate<Integer> greaterThan = Predicates.greaterThan(10000);
        Collection<String> result = ParallelIterate.collectIf(collection, greaterThan, Functions.getToString());
        Verify.assertSize(10000, result);
        Verify.assertNotContains(String.valueOf(9000), result);
        Verify.assertNotContains(String.valueOf(21000), result);
        Verify.assertContains(String.valueOf(15976), result);
    }

    @Test
    public void testCollectIfForSet()
    {
        Collection<Integer> collection = INTEGER_SET;
        Predicate<Integer> greaterThan = Predicates.greaterThan(10000);
        Collection<String> result = ParallelIterate.collectIf(collection, greaterThan, Functions.getToString());
        Verify.assertSize(10000, result);
        Verify.assertNotContains(String.valueOf(9000), result);
        Verify.assertNotContains(String.valueOf(21000), result);
        Verify.assertContains(String.valueOf(15976), result);
    }

    private static List<Integer> createIntegerList(int size)
    {
        return Collections.nCopies(size, Integer.valueOf(1));
    }

    private class RecursiveProcedure implements Procedure<Integer>
    {
        private static final long serialVersionUID = 1L;
        private final ExecutorService executorService = ParallelIterate.newPooledExecutor("ParallelIterateTest", false);

        public void value(Integer level)
        {
            if (level > 0)
            {
                ParallelIterateTest.this.threadNames.add(Thread.currentThread().getName());
                this.executeParallelIterate(level - 1, this.executorService);
            }
            else
            {
                this.simulateWork();
            }
        }

        private void simulateWork()
        {
            synchronized (ParallelIterateTest.this)
            {
                ParallelIterateTest.this.count++;
            }
        }

        private void executeParallelIterate(int level, ExecutorService executorService)
        {
            MutableList<Integer> items = Lists.mutable.of();
            for (int i = 0; i < 20000; i++)
            {
                items.add(i % 1000 == 0 ? level : 0);
            }
            ParallelIterate.forEach(items, new RecursiveProcedure(), executorService);
        }
    }

    @Test
    public void flatCollect()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<String> result = ParallelIterate.flatCollect(collection, INT_TO_TWO_STRINGS);
        Verify.assertSize(40000, result);
        Verify.assertContains(String.valueOf(20000), result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void flatCollectUseCombineOne()
    {
        Collection<Integer> collection = INTEGER_LIST;
        Collection<String> result = ParallelIterate.flatCollect(collection, INT_TO_TWO_STRINGS, true);
        Verify.assertSize(40000, result);
        Verify.assertContains(String.valueOf(20000), result);
        Verify.assertInstanceOf(List.class, result);
    }

    @Test
    public void flatCollectForSet()
    {
        Collection<Integer> collection = INTEGER_SET;
        Collection<String> result = ParallelIterate.flatCollect(collection, INT_TO_TWO_STRINGS);
        Verify.assertSize(20000, result);
        Verify.assertContains(String.valueOf(20000), result);
        Verify.assertInstanceOf(Set.class, result);
    }

    public static final class IntegerSum
    {
        private int sum = 0;

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

        public SumProcedure create()
        {
            return new SumProcedure(new IntegerSum(0));
        }

        public IntegerSum value(IntegerSum s1, Integer s2)
        {
            return s1.add(s2);
        }

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

        public void combineOne(SumProcedure sumProcedure)
        {
            this.sum.add(sumProcedure.getSum());
        }
    }
}
