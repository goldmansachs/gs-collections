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

package com.gs.collections.impl.utility.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.MaxSizeFunction;
import com.gs.collections.impl.block.function.MinSizeFunction;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link IterableIterate}.
 */
public class IterableIterateTest
{
    @Test
    public void injectInto()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(iList(1, 2, 3));
        Assert.assertEquals(
                1 + 1 + 2 + 3,
                Iterate.injectInto(1, iterable, AddFunction.INTEGER).intValue());
    }

    @Test
    public void injectIntoOver30()
    {
        MutableList<Integer> list = Lists.mutable.of();
        for (int i = 0; i < 31; i++)
        {
            list.add(1);
        }
        Iterable<Integer> iterable = new IterableAdapter<Integer>(list);
        Assert.assertEquals(32, Iterate.injectInto(1, iterable, AddFunction.INTEGER).intValue());
    }

    @Test
    public void injectIntoDouble()
    {
        Iterable<Double> iterable = new IterableAdapter<Double>(iList(1.0, 2.0, 3.0));
        Assert.assertEquals(
                1.0 + 1.0 + 2.0 + 3.0,
                Iterate.injectInto(1.0, iterable, AddFunction.DOUBLE).doubleValue(),
                0.0);
    }

    @Test
    public void injectIntoString()
    {
        Iterable<String> iterable = new IterableAdapter<String>(iList("1", "2", "3"));
        Assert.assertEquals("0123", Iterate.injectInto("0", iterable, AddFunction.STRING));
    }

    @Test
    public void injectIntoMaxString()
    {
        Iterable<String> iterable = new IterableAdapter<String>(iList("1", "12", "123"));
        Assert.assertEquals(3, Iterate.injectInto(Integer.MIN_VALUE, iterable, MaxSizeFunction.STRING).intValue());
    }

    @Test
    public void injectIntoMinString()
    {
        Iterable<String> iterable = new IterableAdapter<String>(iList("1", "12", "123"));
        Assert.assertEquals(1, Iterate.injectInto(Integer.MAX_VALUE, iterable, MinSizeFunction.STRING).intValue());
    }

    @Test
    public void collect()
    {
        Iterable<Boolean> iterable = new IterableAdapter<Boolean>(iList(Boolean.TRUE, Boolean.FALSE, null));
        Collection<String> result = Iterate.collect(iterable, Functions.getToString());
        Assert.assertEquals(iList("true", "false", "null"), result);
    }

    @Test
    public void collectWithTarget()
    {
        Iterable<Boolean> iterable = new IterableAdapter<Boolean>(iList(Boolean.TRUE, Boolean.FALSE, null));
        Collection<String> result = Iterate.collect(iterable, Functions.getToString(), FastList.<String>newList());
        Assert.assertEquals(iList("true", "false", "null"), result);
    }

    @Test
    public void collectOver30()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(31));
        Collection<Class<?>> result = Iterate.collect(iterable, Functions.getToClass());
        Assert.assertEquals(Collections.nCopies(31, Integer.class), result);
    }

    private List<Integer> getIntegerList()
    {
        return Interval.toReverseList(1, 5);
    }

    @Test
    public void forEachWithIndex()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Iterate.sortThis(this.getIntegerList()));
        Iterate.forEachWithIndex(iterable, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                Assert.assertEquals(index, object - 1);
            }
        });
    }

    @Test
    public void forEachWithIndexOver30()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Iterate.sortThis(Interval.oneTo(31).toList()));
        Iterate.forEachWithIndex(iterable, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                Assert.assertEquals(index, object - 1);
            }
        });
    }

    @Test
    public void detect()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertEquals(1, Iterate.detect(iterable, Predicates.equal(1)).intValue());
        //noinspection CachedNumberConstructorCall,UnnecessaryBoxing
        Integer firstInt = new Integer(2);
        //noinspection CachedNumberConstructorCall,UnnecessaryBoxing
        Integer secondInt = new Integer(2);
        Assert.assertNotSame(firstInt, secondInt);
        ImmutableList<Integer> list2 = iList(1, firstInt, secondInt);
        Assert.assertSame(list2.get(1), Iterate.detect(list2, Predicates.equal(2)));
    }

    @Test
    public void detectOver30()
    {
        List<Integer> list = Interval.oneTo(31);
        Iterable<Integer> iterable = new IterableAdapter<Integer>(list);
        Assert.assertEquals(1, Iterate.detect(iterable, Predicates.equal(1)).intValue());
    }

    @Test
    public void detectWith()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertEquals(1, Iterate.detectWith(iterable, Predicates2.equal(), 1).intValue());
        //noinspection CachedNumberConstructorCall,UnnecessaryBoxing
        Integer firstInt = new Integer(2);
        //noinspection CachedNumberConstructorCall,UnnecessaryBoxing
        Integer secondInt = new Integer(2);
        Assert.assertNotSame(firstInt, secondInt);
        ImmutableList<Integer> list2 = iList(1, firstInt, secondInt);
        Assert.assertSame(list2.get(1), Iterate.detectWith(list2, Predicates2.equal(), 2));
    }

    @Test
    public void detectWithOver30()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(31));
        Assert.assertEquals(1, Iterate.detectWith(iterable, Predicates2.equal(), 1).intValue());
    }

    @Test
    public void detectIfNone()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertNull(Iterate.detectIfNone(iterable, Predicates.equal(6), null));
    }

    @Test
    public void detectIfNoneOver30()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(31));
        Assert.assertNull(Iterate.detectIfNone(iterable, Predicates.equal(32), null));
    }

    @Test
    public void detectWithIfNone()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertNull(Iterate.detectWithIfNone(iterable, Predicates2.equal(), 6, null));
    }

    @Test
    public void detectWithIfNoneOver30()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(31));
        Assert.assertNull(Iterate.detectWithIfNone(iterable, Predicates2.equal(), 32, null));
    }

    @Test
    public void select()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Verify.assertSize(5, Iterate.select(iterable, Predicates.instanceOf(Integer.class)));
        Verify.assertSize(5, Iterate.select(iterable, Predicates.instanceOf(Integer.class), FastList.<Integer>newList()));
    }

    @Test
    public void reject()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Verify.assertSize(5, Iterate.reject(iterable, Predicates.instanceOf(String.class)));
        Verify.assertSize(5,
                Iterate.reject(iterable, Predicates.instanceOf(String.class), FastList.<Integer>newList()));
    }

    @Test
    public void distinct()
    {
        Collection<Integer> iterable = FastList.newListWith(2, 1, 3, 2, 1, 3);
        FastList<Integer> result = FastList.newList();
        FastList<Integer> actualList = IterableIterate.distinct(iterable, result);
        FastList<Integer> expectedList = FastList.newListWith(2, 1, 3);
        Verify.assertListsEqual(expectedList, result);
        Verify.assertListsEqual(expectedList, actualList);
        Verify.assertSize(6, iterable);
    }

    @Test
    public void selectWith()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Verify.assertSize(5, Iterate.selectWith(iterable, Predicates2.instanceOf(), Integer.class));
        Verify.assertSize(5,
                Iterate.selectWith(iterable, Predicates2.instanceOf(), Integer.class, FastList.<Integer>newList()));
    }

    @Test
    public void rejectWith()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Verify.assertEmpty(Iterate.rejectWith(iterable, Predicates2.instanceOf(), Integer.class));
        Verify.assertEmpty(Iterate.rejectWith(iterable,
                Predicates2.instanceOf(),
                Integer.class,
                FastList.<Integer>newList()));
    }

    @Test
    public void selectInstancesOf()
    {
        Iterable<Number> iterable = new IterableAdapter<Number>(FastList.<Number>newListWith(1, 2.0, 3, 4.0, 5));
        Collection<Integer> result = Iterate.selectInstancesOf(iterable, Integer.class);
        Assert.assertEquals(iList(1, 3, 5), result);
    }

    @Test
    public void injectIntoWith()
    {
        Sum result = new IntegerSum(0);
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(5));
        Function3<Sum, Integer, Integer, Sum> function = new Function3<Sum, Integer, Integer, Sum>()
        {
            public Sum value(Sum sum, Integer element, Integer withValue)
            {
                return sum.add(element.intValue() * withValue.intValue());
            }
        };
        Sum sumOfDoubledValues = Iterate.injectIntoWith(result, iterable, function, 2);
        Assert.assertEquals(30, sumOfDoubledValues.getValue().intValue());
    }

    @Test
    public void selectAndRejectWith()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Twin<MutableList<Integer>> result = Iterate.selectAndRejectWith(iterable, Predicates2.in(), iList(1));
        Assert.assertEquals(iList(1), result.getOne());
        Assert.assertEquals(iList(5, 4, 3, 2), result.getTwo());
    }

    @Test
    public void partition()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        PartitionIterable<Integer> partition = Iterate.partition(iterable, IntegerPredicates.isEven());
        Assert.assertEquals(iList(4, 2), partition.getSelected());
        Assert.assertEquals(iList(5, 3, 1), partition.getRejected());
    }

    @Test
    public void anySatisfyWith()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertTrue(Iterate.anySatisfyWith(iterable, Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(Iterate.anySatisfyWith(iterable, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void anySatisfy()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertTrue(Iterate.anySatisfy(iterable, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(Iterate.anySatisfy(iterable, Predicates.instanceOf(Double.class)));
    }

    @Test
    public void allSatisfyWith()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertTrue(Iterate.allSatisfyWith(iterable, Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(Iterate.allSatisfyWith(iterable, Predicates2.<Integer>greaterThan(), 2));
    }

    @Test
    public void allSatisfy()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertTrue(Iterate.allSatisfy(iterable, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(Iterate.allSatisfy(iterable, Predicates.greaterThan(2)));
    }

    @Test
    public void countWith()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Assert.assertEquals(5, Iterate.countWith(iterable, Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(0, Iterate.countWith(iterable, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void selectWithRandomAccess()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        Collection<Integer> results = Iterate.selectWith(iterable, Predicates2.instanceOf(), Integer.class);
        Assert.assertEquals(iList(5, 4, 3, 2, 1), results);
        Verify.assertSize(5, results);
    }

    @Test
    public void selectWithRandomAccessWithTarget()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(this.getIntegerList());
        MutableList<Integer> results =
                Iterate.selectWith(iterable, Predicates2.instanceOf(), Integer.class, FastList.<Integer>newList());
        Assert.assertEquals(iList(5, 4, 3, 2, 1), results);
        Verify.assertSize(5, results);
    }

    @Test
    public void collectIf()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(31));
        Collection<Class<?>> result = Iterate.collectIf(iterable, Predicates.equal(31), Functions.getToClass());
        Assert.assertEquals(iList(Integer.class), result);
    }

    @Test
    public void collectIfWithTarget()
    {
        Iterable<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(31));
        Collection<Class<?>> result =
                Iterate.collectIf(iterable, Predicates.equal(31), Functions.getToClass(), FastList.<Class<?>>newList());
        Assert.assertEquals(iList(Integer.class), result);
    }

    @Test
    public void collectWithOver30()
    {
        List<Integer> list = Interval.oneTo(31);
        Iterable<Integer> iterable = new IterableAdapter<Integer>(list);
        Collection<String> result = Iterate.collectWith(iterable, new Function2<Integer, Integer, String>()
        {
            public String value(Integer argument1, Integer argument2)
            {
                return argument1.equals(argument2) ? "31" : null;
            }
        }, 31);
        Verify.assertSize(31, result);
        Verify.assertContainsAll(result, null, "31");
        Verify.assertCount(30, result, Predicates.isNull());
    }

    @Test
    public void detectIndexOver30()
    {
        MutableList<Integer> list = Interval.toReverseList(1, 31);
        Iterable<Integer> iterable = new IterableAdapter<Integer>(list);
        Assert.assertEquals(30, Iterate.detectIndex(iterable, Predicates.equal(1)));
        Assert.assertEquals(0, Iterate.detectIndex(iterable, Predicates.equal(31)));
    }

    @Test
    public void detectIndexWithOver30()
    {
        MutableList<Integer> list = Interval.toReverseList(1, 31);
        Iterable<Integer> iterable = new IterableAdapter<Integer>(list);
        Assert.assertEquals(30, Iterate.detectIndexWith(iterable, Predicates2.equal(), 1));
        Assert.assertEquals(0, Iterate.detectIndexWith(iterable, Predicates2.equal(), 31));
    }

    @Test
    public void injectIntoWithOver30()
    {
        Sum result = new IntegerSum(0);
        Integer parameter = 2;
        List<Integer> integers = Interval.oneTo(31);
        Function3<Sum, Integer, Integer, Sum> function = new Function3<Sum, Integer, Integer, Sum>()
        {
            public Sum value(Sum sum, Integer element, Integer withValue)
            {
                return sum.add((element.intValue() - element.intValue()) * withValue.intValue());
            }
        };
        Sum sumOfDoubledValues = Iterate.injectIntoWith(result, integers, function, parameter);
        Assert.assertEquals(0, sumOfDoubledValues.getValue().intValue());
    }

    @Test
    public void removeIf()
    {
        MutableList<Integer> objects = mList(1, 2, 3, null);
        Iterable<Integer> iterable = new IterableAdapter<Integer>(objects);
        Iterate.removeIf(iterable, Predicates.isNull());
        Verify.assertIterableSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
        MutableList<Integer> objects4 = mList(null, 1, 2, 3);
        Iterable<Integer> iterable4 = new IterableAdapter<Integer>(objects4);
        Iterate.removeIf(iterable4, Predicates.isNull());
        Verify.assertIterableSize(3, objects4);
        Verify.assertContainsAll(objects4, 1, 2, 3);
        MutableList<Integer> objects3 = mList(1, null, 2, 3);
        Iterable<Integer> iterable3 = new IterableAdapter<Integer>(objects3);
        Iterate.removeIf(iterable3, Predicates.isNull());
        Verify.assertIterableSize(3, objects3);
        Verify.assertContainsAll(objects3, 1, 2, 3);
        MutableList<Integer> objects2 = mList(null, null, null, (Integer) null);
        Iterable<Integer> iterable2 = new IterableAdapter<Integer>(objects2);
        Iterate.removeIf(iterable2, Predicates.isNull());
        Verify.assertIterableEmpty(objects2);
        MutableList<Integer> objects1 = mList(1, 2, 3);
        Iterable<Integer> iterable1 = new IterableAdapter<Integer>(objects1);
        Iterate.removeIf(iterable1, Predicates.isNull());
        Verify.assertIterableSize(3, objects1);
        Verify.assertContainsAll(objects1, 1, 2, 3);
    }

    @Test
    public void removeIfWith()
    {
        MutableList<Integer> objects1 = mList(1, 2, 3, null);
        Iterable<Integer> iterable = new IterableAdapter<Integer>(objects1);
        Iterate.removeIfWith(iterable, Predicates2.isNull(), null);
        Assert.assertEquals(iList(1, 2, 3), objects1);

        MutableList<Integer> objects2 = mList(null, 1, 2, 3);
        Iterable<Integer> iterable4 = new IterableAdapter<Integer>(objects2);
        Iterate.removeIfWith(iterable4, Predicates2.isNull(), null);
        Assert.assertEquals(iList(1, 2, 3), objects2);

        MutableList<Integer> objects3 = mList(1, null, 2, 3);
        Iterable<Integer> iterable3 = new IterableAdapter<Integer>(objects3);
        Iterate.removeIfWith(iterable3, Predicates2.isNull(), null);
        Assert.assertEquals(iList(1, 2, 3), objects3);

        MutableList<Integer> objects4 = mList(null, null, null, (Integer) null);
        Iterable<Integer> iterable2 = new IterableAdapter<Integer>(objects4);
        Iterate.removeIfWith(iterable2, Predicates2.isNull(), null);
        Verify.assertIterableEmpty(objects4);

        MutableList<Integer> objects5 = mList(null, 1, 2, 3, null);
        Iterate.removeIfWith(objects5, Predicates2.isNull(), null);
        Assert.assertEquals(iList(1, 2, 3), objects5);

        MutableList<Integer> objects6 = mList(1, 2, 3);
        Iterable<Integer> iterable1 = new IterableAdapter<Integer>(objects6);
        Iterate.removeIfWith(iterable1, Predicates2.isNull(), null);
        Assert.assertEquals(iList(1, 2, 3), objects6);
    }

    @Test
    public void forEach()
    {
        final MutableList<Integer> newCollection = Lists.mutable.of();
        IterableAdapter<Integer> iterable = new IterableAdapter<Integer>(Interval.oneTo(10));
        Iterate.forEach(iterable, new Procedure<Integer>()
        {
            public void value(Integer value)
            {
                newCollection.add(value);
            }
        });
        Assert.assertEquals(Interval.oneTo(10), newCollection);
    }

    @Test
    public void forEachWith()
    {
        final Sum result = new IntegerSum(0);
        Iterable<Integer> integers = new IterableAdapter<Integer>(Interval.oneTo(5));
        Iterate.forEachWith(integers, new Procedure2<Integer, Integer>()
        {
            public void value(Integer each, Integer parm)
            {
                result.add(each.intValue() * parm.intValue());
            }
        }, 2);
        Assert.assertEquals(30, result.getValue().intValue());
    }

    @Test
    public void collectWith()
    {
        Iterable<Boolean> iterable =
                new IterableAdapter<Boolean>(FastList.<Boolean>newList().with(Boolean.TRUE, Boolean.FALSE));
        Assert.assertEquals(
                FastList.newListWith("true", "false"),
                Iterate.collectWith(iterable, new Function2<Boolean, Boolean, String>()
                {
                    public String value(Boolean argument1, Boolean argument2)
                    {
                        return Boolean.toString(argument1.booleanValue() && argument2.booleanValue());
                    }
                }, Boolean.TRUE));
    }

    @Test
    public void collectWithToTarget()
    {
        Iterable<Boolean> iterable =
                new IterableAdapter<Boolean>(FastList.<Boolean>newList().with(Boolean.TRUE, Boolean.FALSE));
        Assert.assertEquals(
                FastList.newListWith("true", "false"),
                Iterate.collectWith(iterable, new Function2<Boolean, Boolean, String>()
                {
                    public String value(Boolean argument1, Boolean argument2)
                    {
                        return Boolean.toString(argument1.booleanValue() && argument2.booleanValue());
                    }
                }, Boolean.TRUE, new ArrayList<String>()));
    }

    @Test
    public void take()
    {
        List<Integer> list = this.getIntegerList();
        Iterable<Integer> iterable = new IterableAdapter<Integer>(list);

        Collection<Integer> results = Iterate.take(iterable, 2);
        Assert.assertEquals(FastList.newListWith(5, 4), results);

        Verify.assertEmpty(Iterate.take(iterable, 0));
        Verify.assertSize(5, Iterate.take(iterable, 5));
        Verify.assertSize(5, Iterate.take(iterable, 10));
    }

    @Test
    public void take_empty()
    {
        Verify.assertEmpty(Iterate.take(new IterableAdapter<Integer>(FastList.<Integer>newList()), 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_negative_throws()
    {
        Iterate.take(new IterableAdapter<Integer>(FastList.<Integer>newList()), -1);
    }

    @Test
    public void drop()
    {
        List<Integer> list = this.getIntegerList();
        Iterable<Integer> iterable = new IterableAdapter<Integer>(list);

        Collection<Integer> results = Iterate.drop(iterable, 2);
        Assert.assertEquals(FastList.newListWith(3, 2, 1), results);

        Verify.assertEmpty(Iterate.drop(iterable, 5));
        Verify.assertEmpty(Iterate.drop(iterable, 6));
        Verify.assertSize(5, Iterate.drop(iterable, 0));
    }

    @Test
    public void drop_empty()
    {
        Verify.assertEmpty(Iterate.drop(new IterableAdapter<Integer>(FastList.<Integer>newList()), 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_negative_throws()
    {
        Iterate.drop(new IterableAdapter<Integer>(FastList.<Integer>newList()), -1);
    }

    private static final class IterableAdapter<E>
            implements Iterable<E>
    {
        private final Iterable<E> iterable;

        private IterableAdapter(Iterable<E> newIterable)
        {
            this.iterable = newIterable;
        }

        public Iterator<E> iterator()
        {
            return this.iterable.iterator();
        }
    }

    @Test
    public void maxWithoutComparator()
    {
        Iterable<Integer> iterable = FastList.newListWith(1, 5, 2, 99, 7);
        Assert.assertEquals(99, IterableIterate.max(iterable).intValue());
    }

    @Test
    public void minWithoutComparator()
    {
        Iterable<Integer> iterable = FastList.newListWith(99, 5, 2, 1, 7);
        Assert.assertEquals(1, IterableIterate.min(iterable).intValue());
    }

    @Test
    public void max()
    {
        Iterable<Integer> iterable = FastList.newListWith(1, 5, 2, 99, 7);
        Assert.assertEquals(99, IterableIterate.max(iterable, Comparators.naturalOrder()).intValue());
    }

    @Test
    public void min()
    {
        Iterable<Integer> iterable = FastList.newListWith(99, 5, 2, 1, 7);
        Assert.assertEquals(1, IterableIterate.min(iterable, Comparators.naturalOrder()).intValue());
    }

    @Test
    public void forEachUsingFromTo()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();

        this.assertForEachUsingFromTo(integers);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(reverseResults);
        this.assertForEachUsingFromTo(new LinkedList<Integer>(integers));
    }

    private void assertForEachUsingFromTo(final List<Integer> integers)
    {
        MutableList<Integer> results = Lists.mutable.of();
        IterableIterate.forEach(integers, 0, 4, CollectionAddProcedure.on(results));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(reverseResults);

        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEach(integers, 4, -1, procedure);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEach(integers, -1, 4, procedure);
            }
        });
    }

    @Test
    public void forEachWithIndexUsingFromTo()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        this.assertForEachWithIndexUsingFromTo(integers);
        this.assertForEachWithIndexUsingFromTo(new LinkedList<Integer>(integers));
    }

    private void assertForEachWithIndexUsingFromTo(final List<Integer> integers)
    {
        MutableList<Integer> results = Lists.mutable.of();
        IterableIterate.forEachWithIndex(integers, 0, 4, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(results)));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResults));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.forEachWithIndex(integers, 4, -1, objectIntProcedure);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.forEachWithIndex(integers, -1, 4, objectIntProcedure);
            }
        });
    }
}
