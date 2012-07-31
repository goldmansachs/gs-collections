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

package com.gs.collections.impl.collection.mutable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * Abstract JUnit test for {@link MutableCollection}s.
 */
public abstract class AbstractCollectionTestCase
{
    protected abstract <T> MutableCollection<T> classUnderTest();

    @Test
    public void testNewCollection()
    {
        MutableCollection<Object> collection = this.classUnderTest();
        Verify.assertEmpty(collection);
        Verify.assertSize(0, collection);
    }

    @Test
    public void equalsAndHashCode()
    {
        Object same = this.newWith(1, 2, 3);
        Verify.assertEqualsAndHashCode(this.newWith(1, 2, 3), this.newWith(1, 2, 3));
        Verify.assertNotEquals(this.newWith(1, 2, 3), this.newWith(1, 2));
    }

    @Test
    public void newEmpty()
    {
        MutableCollection<Object> collection = this.classUnderTest().newEmpty();
        Verify.assertEmpty(collection);
        Verify.assertSize(0, collection);
        Assert.assertFalse(collection.notEmpty());
    }

    @Test
    public void toImmutable()
    {
        Verify.assertInstanceOf(MutableCollection.class, this.<Object>classUnderTest());
        Verify.assertInstanceOf(ImmutableCollection.class, this.classUnderTest().toImmutable());
    }

    protected <T> MutableCollection<T> newWith(T one)
    {
        MutableCollection<T> result = this.classUnderTest();
        result.add(one);
        return result;
    }

    protected <T> MutableCollection<T> newWith(T one, T two)
    {
        MutableCollection<T> result = this.classUnderTest();
        result.add(one);
        result.add(two);
        return result;
    }

    protected <T> MutableCollection<T> newWith(T one, T two, T three)
    {
        MutableCollection<T> result = this.classUnderTest();
        result.add(one);
        result.add(two);
        result.add(three);
        return result;
    }

    protected <T> MutableCollection<T> newWith(T... littleElements)
    {
        MutableCollection<T> result = this.classUnderTest();
        for (int i = 0; i < littleElements.length; i++)
        {
            result.add(littleElements[i]);
        }
        return result;
    }

    @Test
    public void testNewWith()
    {
        MutableCollection<Integer> collection = this.newWith(1);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(1, collection);
        Verify.assertContains(1, collection);
    }

    @Test
    public void testNewWithWith()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(2, collection);
        Verify.assertContainsAll(collection, 1, 2);
    }

    @Test
    public void testNewWithWithWith()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(3, collection);
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void testNewWithVarArgs()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(4, collection);
        Verify.assertContainsAll(collection, 1, 2, 3, 4);
    }

    @Test
    public void containsAllIterable()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        Assert.assertTrue(collection.containsAllIterable(FastList.newListWith(1, 2)));
        Assert.assertFalse(collection.containsAllIterable(FastList.newListWith(1, 5)));
    }

    @Test
    public void containsAllArray()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        Assert.assertTrue(collection.containsAllArguments(1, 2));
        Assert.assertFalse(collection.containsAllArguments(1, 5));
    }

    @Test
    public void forEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        collection.forEach(CollectionAddProcedure.on(result));
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        collection.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        collection.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        Assert.assertEquals(FastList.newListWith(1, 3, 5, 7), result);
    }

    @Test
    public void select()
    {
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 1, 2);
        Verify.denyContainsAny(this.newWith(-1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 3, 4, 5);
        Verify.assertContainsAll(
                this.newWith(1, 2, 3, 4, 5).select(Predicates.lessThan(3), UnifiedSet.<Integer>newSet()), 1, 2);
    }

    @Test
    public void selectWith()
    {
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4, 5).selectWith(Predicates2.<Integer>lessThan(), 3), 1, 2);
        Verify.denyContainsAny(this.newWith(-1, 2, 3, 4, 5).selectWith(Predicates2.<Integer>lessThan(), 3), 3, 4, 5);
        Verify.assertContainsAll(
                this.newWith(1, 2, 3, 4, 5).selectWith(
                        Predicates2.<Integer>lessThan(),
                        3,
                        UnifiedSet.<Integer>newSet()),
                1, 2);
    }

    @Test
    public void reject()
    {
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4).reject(Predicates.lessThan(3)), 3, 4);
        Verify.assertContainsAll(
                this.newWith(1, 2, 3, 4).reject(Predicates.lessThan(3), UnifiedSet.<Integer>newSet()), 3, 4);
    }

    @Test
    public void rejectWith()
    {
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4).rejectWith(Predicates2.<Integer>lessThan(), 3), 3, 4);
        Verify.assertContainsAll(
                this.newWith(1, 2, 3, 4).rejectWith(Predicates2.<Integer>lessThan(), 3, UnifiedSet.<Integer>newSet()),
                3, 4);
    }

    @Test
    public void selectInstancesOf()
    {
        MutableCollection<Number> numbers = this.<Number>newWith(1, 2.0, 3, 4.0, 5);
        Assert.assertEquals(HashBag.newBagWith(1, 3, 5), numbers.selectInstancesOf(Integer.class).toBag());
        Assert.assertEquals(HashBag.newBagWith(1, 2.0, 3, 4.0, 5), numbers.selectInstancesOf(Number.class).toBag());
    }

    @Test
    public void collect()
    {
        Verify.assertContainsAll(
                this.newWith(1, 2, 3, 4).collect(Functions.getToString()),
                "1", "2", "3", "4");
        Verify.assertContainsAll(
                this.newWith(1, 2, 3, 4).collect(
                        Functions.getToString(),
                        UnifiedSet.<String>newSet()), "1", "2", "3", "4");
    }

    @Test
    public void flatCollect()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
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

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 2, 3, 4, 5).detect(Predicates.equal(3)));
        Assert.assertNull(this.newWith(1, 2, 3, 4, 5).detect(Predicates.equal(6)));
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
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1, 3, 2).minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 3, 2).maxBy(Functions.getToString()));
    }

    @Test
    public void detectWith()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 2, 3, 4, 5).detectWith(Predicates2.equal(), 3));
        Assert.assertNull(this.newWith(1, 2, 3, 4, 5).detectWith(Predicates2.equal(), 6));
    }

    @Test
    public void detectIfNoneWithBlock()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(6);
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 2, 3, 4, 5).detectIfNone(Predicates.equal(3), function));
        Assert.assertEquals(Integer.valueOf(6), this.newWith(1, 2, 3, 4, 5).detectIfNone(Predicates.equal(6), function));
    }

    @Test
    public void detectWithIfNoneBlock()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(-42);
        Assert.assertEquals(
                Integer.valueOf(5),
                this.newWith(1, 2, 3, 4, 5).detectWithIfNone(
                        Predicates2.<Integer>greaterThan(),
                        4,
                        function));
        Assert.assertEquals(
                Integer.valueOf(-42),
                this.newWith(1, 2, 3, 4, 5).detectWithIfNone(
                        Predicates2.<Integer>lessThan(),
                        0,
                        function));
    }

    @Test
    public void addAll()
    {
        MutableCollection<Integer> collection = this.classUnderTest();
        Assert.assertTrue(collection.addAll(FastList.newListWith(1, 2, 3)));
        Verify.assertContainsAll(collection, 1, 2, 3);

        boolean result = collection.addAll(FastList.newListWith(1, 2, 3));
        if (collection.size() == 3)
        {
            Assert.assertFalse("addAll did not modify the collection", result);
        }
        else
        {
            Assert.assertTrue("addAll modified the collection", result);
        }
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void addAllIterable()
    {
        MutableCollection<Integer> collection = this.classUnderTest();
        Assert.assertTrue(collection.addAllIterable(FastList.newListWith(1, 2, 3)));
        Verify.assertContainsAll(collection, 1, 2, 3);

        boolean result = collection.addAllIterable(FastList.newListWith(1, 2, 3));
        if (collection.size() == 3)
        {
            Assert.assertFalse("addAllIterable did not modify the collection", result);
        }
        else
        {
            Assert.assertTrue("addAllIterable modified the collection", result);
        }
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.newWith(1, 2, 3).allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(this.newWith(1, 2, 3).allSatisfy(Predicates.equal(1)));
    }

    @Test
    public void allSatisfyWith()
    {
        Assert.assertTrue(this.newWith(1, 2, 3).allSatisfyWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(this.newWith(1, 2, 3).allSatisfyWith(Predicates2.equal(), 1));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.newWith(1, 2, 3).anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(this.newWith(1, 2, 3).anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void anySatisfyWith()
    {
        Assert.assertFalse(this.newWith(1, 2, 3).anySatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertTrue(this.newWith(1, 2, 3).anySatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(3, this.newWith(1, 2, 3).count(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void countWith()
    {
        Assert.assertEquals(3, this.newWith(1, 2, 3).countWith(Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void collectIf()
    {
        Verify.assertContainsAll(
                this.newWith(1, 2, 3).collectIf(
                        Predicates.instanceOf(Integer.class),
                        Functions.getToString()),
                "1", "2", "3");
        Verify.assertContainsAll(
                this.newWith(1, 2, 3).collectIf(
                        Predicates.instanceOf(Integer.class),
                        Functions.getToString(),
                        UnifiedSet.<String>newSet()),
                "1", "2", "3");
    }

    @Test
    public void collectWith()
    {
        Assert.assertEquals(
                Bags.mutable.of(2, 3, 4),
                this.newWith(1, 2, 3).collectWith(AddFunction.INTEGER, 1).toBag());
        Assert.assertEquals(
                Bags.mutable.of(2, 3, 4),
                this.newWith(1, 2, 3).collectWith(AddFunction.INTEGER, 1, FastList.<Integer>newList()).toBag());
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
        Verify.assertEmpty(this.classUnderTest());
        Verify.assertNotEmpty(this.newWith(1, 2));
        Assert.assertTrue(this.newWith(1, 2).notEmpty());
    }

    @Test
    public void removeAll()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.removeAll(FastList.newListWith(1, 2, 4)));
        Assert.assertEquals(Bags.mutable.of(3), objects.toBag());

        MutableCollection<Integer> objects2 = this.newWith(1, 2, 3);
        Assert.assertFalse(objects2.removeAll(FastList.newListWith(4, 5)));
        Assert.assertEquals(Bags.mutable.of(1, 2, 3), objects2.toBag());
    }

    @Test
    public void removeAllIterable()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.removeAllIterable(FastList.newListWith(1, 2, 4)));
        Assert.assertEquals(Bags.mutable.of(3), objects.toBag());

        MutableCollection<Integer> objects2 = this.newWith(1, 2, 3);
        Assert.assertFalse(objects2.removeAllIterable(FastList.newListWith(4, 5)));
        Assert.assertEquals(Bags.mutable.of(1, 2, 3), objects2.toBag());
    }

    @Test
    public void retainAll()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.retainAll(mSet(1, 2)));
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 1, 2);

        MutableCollection<Integer> integers = this.newWith(0);
        Assert.assertFalse(integers.retainAll(FastList.newListWith(1, 0)));
        Assert.assertEquals(Bags.mutable.of(0), integers.toBag());
    }

    @Test
    public void retainAllIterable()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertTrue(objects.retainAllIterable(iList(1, 2)));
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 1, 2);

        MutableCollection<Integer> integers = this.newWith(0);
        Assert.assertFalse(integers.retainAllIterable(FastList.newListWith(1, 0)));
        Assert.assertEquals(Bags.mutable.of(0), integers.toBag());
    }

    @Test
    public void clear()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        objects.clear();
        Verify.assertEmpty(objects);
        objects.clear();
        Verify.assertEmpty(objects);
    }

    @Test
    public void iterator()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Iterator<Integer> iterator = objects.iterator();
        for (int i = objects.size(); i-- > 0; )
        {
            Integer integer = iterator.next();
            Assert.assertEquals(3, integer.intValue() + i);
        }
    }

    @Test
    public void injectInto()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Integer result = objects.injectInto(1, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(7), result);
        int sum = objects.injectInto(0, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(6, sum);
    }

    @Test
    public void injectIntoInt()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        int result = objects.injectInto(1, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(7, result);
        int sum = objects.injectInto(0, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(6, sum);
    }

    @Test
    public void injectIntoLong()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        long result = objects.injectInto(1, AddFunction.INTEGER_TO_LONG);
        Assert.assertEquals(7, result);
        long sum = objects.injectInto(0, AddFunction.INTEGER_TO_LONG);
        Assert.assertEquals(6, sum);
    }

    @Test
    public void injectIntoDouble()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        double result = objects.injectInto(1, AddFunction.INTEGER_TO_DOUBLE);
        Assert.assertEquals(7.0d, result, 0.001);
        double sum = objects.injectInto(0, AddFunction.INTEGER_TO_DOUBLE);
        Assert.assertEquals(6.0d, sum, 0.001);
    }

    @Test
    public void injectIntoFloat()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        float result = objects.injectInto(1, AddFunction.INTEGER_TO_FLOAT);
        Assert.assertEquals(7.0f, result, 0.001f);
        float sum = objects.injectInto(0, AddFunction.INTEGER_TO_FLOAT);
        Assert.assertEquals(6.0f, sum, 0.001f);
    }

    @Test
    public void sumFloat()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        float expected = objects.injectInto(0, AddFunction.INTEGER_TO_FLOAT);
        double actual = objects.sumOfFloat(new FloatFunction<Integer>()
        {
            public float floatValueOf(Integer integer)
            {
                return integer.floatValue();
            }
        });
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void sumDouble()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        double expected = objects.injectInto(0, AddFunction.INTEGER_TO_DOUBLE);
        double actual = objects.sumOfDouble(new DoubleFunction<Integer>()
        {
            public double doubleValueOf(Integer integer)
            {
                return integer.doubleValue();
            }
        });
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void sumInteger()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        long expected = objects.injectInto(0L, AddFunction.INTEGER_TO_LONG);
        long actual = objects.sumOfInt(new IntFunction<Integer>()
        {
            public int intValueOf(Integer integer)
            {
                return integer;
            }
        });
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void sumLong()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        long expected = objects.injectInto(0, AddFunction.INTEGER_TO_LONG);
        long actual = objects.sumOfLong(new LongFunction<Integer>()
        {
            public long longValueOf(Integer integer)
            {
                return integer.longValue();
            }
        });
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void injectIntoWith()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Integer result = objects.injectIntoWith(1, new Function3<Integer, Integer, Integer, Integer>()
        {
            public Integer value(Integer injectedValued, Integer item, Integer parameter)
            {
                return injectedValued + item + parameter;
            }
        }, 0);
        Assert.assertEquals(Integer.valueOf(7), result);
    }

    @Test
    public void toArray()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        Object[] array = objects.toArray();
        Verify.assertSize(3, array);
        Integer[] array2 = objects.toArray(new Integer[3]);
        Verify.assertSize(3, array2);
    }

    @Test
    public void removeObject()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        objects.remove(3);
        Verify.assertSize(2, objects);
    }

    @Test
    public void selectAndRejectWith()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2);
        Twin<MutableList<Integer>> result = objects.selectAndRejectWith(Predicates2.equal(), 1);
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(1, result.getTwo());
    }

    @Test
    public void partition()
    {
        MutableCollection<Integer> integers = this.newWith(-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        PartitionMutableCollection<Integer> result = integers.partition(IntegerPredicates.isEven());
        Assert.assertEquals(this.newWith(-2, 0, 2, 4, 6, 8), result.getSelected());
        Assert.assertEquals(this.newWith(-3, -1, 1, 3, 5, 7, 9), result.getRejected());
    }

    @Test
    public void remove()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        objects.add(null);
        objects.removeIf(Predicates.isNull());
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
    }

    @Test
    public void removeIfWith()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3, 4);
        objects.removeIfWith(Predicates2.<Integer>lessThan(), 3);
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 3, 4);
    }

    @Test
    public void toList()
    {
        MutableList<Integer> list = this.newWith(1, 2, 3, 4).toList();
        Verify.assertContainsAll(list, 1, 2, 3, 4);
    }

    @Test
    public void toBag()
    {
        MutableBag<Integer> bag = this.newWith(1, 2, 3, 4).toBag();
        Verify.assertContainsAll(bag, 1, 2, 3, 4);
    }

    @Test
    public void toSortedListNaturalOrdering()
    {
        MutableCollection<Integer> integers = this.newWith(2, 1, 5, 3, 4);
        MutableList<Integer> list = integers.toSortedList();
        Verify.assertStartsWith(list, 1, 2, 3, 4, 5);
    }

    @Test
    public void toSortedList()
    {
        MutableCollection<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), list);
    }

    @Test
    public void toSortedList_with_comparator()
    {
        MutableCollection<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(FastList.newListWith(4, 3, 2, 1), list);
    }

    @Test(expected = NullPointerException.class)
    public void toSortedList_with_null()
    {
        this.newWith(2, 4, null, 1, 3).toSortedList();
    }

    @Test
    public void toSortedListBy()
    {
        MutableCollection<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedListBy(Functions.getToString());
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), list);
    }

    @Test
    public void toSortedSet()
    {
        MutableCollection<Integer> integers = this.newWith(2, 4, 1, 3, 2, 1, 3, 4);
        MutableSortedSet<Integer> set = integers.toSortedSet();
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 3, 4), set);
    }

    @Test
    public void toSortedSet_with_comparator()
    {
        MutableCollection<Integer> integers = this.newWith(2, 4, 4, 2, 1, 4, 1, 3);
        MutableSortedSet<Integer> set = integers.toSortedSet(Collections.<Integer>reverseOrder());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 1, 2, 3, 4), set);
    }

    @Test
    public void toSortedSetBy()
    {
        MutableCollection<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableSortedSet<Integer> set = integers.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 3, 4), set);
    }

    @Test(expected = NullPointerException.class)
    public void toSortedListBy_with_null()
    {
        this.newWith(2, 4, null, 1, 3).toSortedListBy(Functions.getIntegerPassThru());
    }

    @Test
    public void toSet()
    {
        MutableCollection<Integer> integers = this.newWith(1, 2, 3, 4);
        MutableSet<Integer> set = integers.toSet();
        Verify.assertContainsAll(set, 1, 2, 3, 4);
    }

    @Test
    public void toMap()
    {
        MutableCollection<Integer> integers = this.newWith(1, 2, 3, 4);
        MutableMap<String, String> map =
                integers.toMap(Functions.getToString(), Functions.getToString());
        Assert.assertEquals(UnifiedMap.newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"), map);
    }

    @Test
    public void toSortedMap()
    {
        MutableCollection<Integer> integers = this.newWith(1, 2, 3);
        MutableSortedMap<Integer, String> map = integers.toSortedMap(Functions.getIntegerPassThru(), Functions.getToString());
        Verify.assertMapsEqual(TreeSortedMap.newMapWith(1, "1", 2, "2", 3, "3"), map);
        Verify.assertListsEqual(FastList.newListWith(1, 2, 3), map.keySet().toList());
    }

    @Test
    public void toSortedMap_with_comparator()
    {
        MutableCollection<Integer> integers = this.newWith(1, 2, 3);
        MutableSortedMap<Integer, String> map = integers.toSortedMap(Comparators.<Integer>reverseNaturalOrder(),
                Functions.getIntegerPassThru(), Functions.getToString());
        Verify.assertMapsEqual(TreeSortedMap.newMapWith(Comparators.<Integer>reverseNaturalOrder(), 1, "1", 2, "2", 3, "3"), map);
        Verify.assertListsEqual(FastList.newListWith(3, 2, 1), map.keySet().toList());
    }

    @Test
    public void testToString()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1);
        collection.add(collection);
        String simpleName = collection.getClass().getSimpleName();
        String string = collection.toString();
        Assert.assertTrue(
                ("[1, (this " + simpleName + ")]").equals(string)
                        || ("[(this " + simpleName + "), 1]").equals(string));
    }

    @Test
    public void makeString()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        collection.add(collection);
        Assert.assertEquals(collection.toString(), '[' + collection.makeString() + ']');
    }

    @Test
    public void makeStringWithSeparator()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        Assert.assertEquals(collection.toString(), '[' + collection.makeString(", ") + ']');
    }

    @Test
    public void makeStringWithSeparatorAndStartAndEnd()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        Assert.assertEquals(collection.toString(), collection.makeString("[", ", ", "]"));
    }

    @Test
    public void appendString()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        collection.add(collection);
        Appendable builder = new StringBuilder();
        collection.appendString(builder);
        Assert.assertEquals(collection.toString(), '[' + builder.toString() + ']');
    }

    @Test
    public void appendStringWithSeparator()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        Appendable builder = new StringBuilder();
        collection.appendString(builder, ", ");
        Assert.assertEquals(collection.toString(), '[' + builder.toString() + ']');
    }

    @Test
    public void appendStringWithSeparatorAndStartAndEnd()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        Appendable builder = new StringBuilder();
        collection.appendString(builder, "[", ", ", "]");
        Assert.assertEquals(collection.toString(), builder.toString());
    }

    @Test
    public void groupBy()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4, 5, 6, 7);
        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };

        MutableMap<Boolean, RichIterable<Integer>> expected =
                UnifiedMap.<Boolean, RichIterable<Integer>>newWithKeysValues(
                        Boolean.TRUE, this.newWith(1, 3, 5, 7),
                        Boolean.FALSE, this.newWith(2, 4, 6));

        Multimap<Boolean, Integer> multimap = collection.groupBy(isOddFunction);
        Assert.assertEquals(expected, multimap.toMap());

        Function<Integer, Boolean> booleanFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return true;
            }
        };
        MutableMultimap<Boolean, Integer> multimap2 = collection.groupBy(
                isOddFunction,
                this.<Integer>classUnderTest().groupBy(booleanFunction));
        Assert.assertEquals(expected, multimap2.toMap());
    }

    @Test
    public void groupByEach()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4, 5, 6, 7);

        NegativeIntervalFunction function = new NegativeIntervalFunction();
        MutableMultimap<Integer, Integer> expected = this.<Integer>classUnderTest().groupByEach(function);
        for (int i = 1; i < 8; i++)
        {
            expected.putAll(-i, Interval.fromTo(i, 7));
        }

        Multimap<Integer, Integer> actual =
                collection.groupByEach(function);
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                collection.groupByEach(function, this.<Integer>classUnderTest().groupByEach(function));
        Assert.assertEquals(expected, actualWithTarget);
    }

    @Test
    public void zip()
    {
        MutableCollection<String> collection = this.newWith("1", "2", "3", "4", "5", "6", "7");
        List<Object> nulls = Collections.nCopies(collection.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(collection.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(collection.size() - 1, null);

        MutableCollection<Pair<String, Object>> pairs = collection.zip(nulls);
        Assert.assertEquals(
                collection.toSet(),
                pairs.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(
                nulls,
                pairs.collect(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        MutableCollection<Pair<String, Object>> pairsPlusOne = collection.zip(nullsPlusOne);
        Assert.assertEquals(
                collection.toSet(),
                pairsPlusOne.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(nulls, pairsPlusOne.collect(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        MutableCollection<Pair<String, Object>> pairsMinusOne = collection.zip(nullsMinusOne);
        Assert.assertEquals(collection.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(collection.containsAll(pairsMinusOne.collect(Functions.<String>firstOfPair())));

        Assert.assertEquals(
                collection.zip(nulls).toSet(),
                collection.zip(nulls, UnifiedSet.<Pair<String, Object>>newSet()));
    }

    @Test
    public void zipWithIndex()
    {
        MutableCollection<String> collection = this.newWith("1", "2", "3", "4", "5", "6", "7");
        MutableCollection<Pair<String, Integer>> pairs = collection.zipWithIndex();

        Assert.assertEquals(
                collection.toSet(),
                pairs.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(
                Interval.zeroTo(collection.size() - 1).toSet(),
                pairs.collect(Functions.<Integer>secondOfPair(), UnifiedSet.<Integer>newSet()));

        Assert.assertEquals(
                collection.zipWithIndex().toSet(),
                collection.zipWithIndex(UnifiedSet.<Pair<String, Integer>>newSet()));
    }

    @Test
    public void chunk()
    {
        MutableCollection<String> collection = this.newWith("1", "2", "3", "4", "5", "6", "7");
        RichIterable<RichIterable<String>> groups = collection.chunk(2);
        RichIterable<Integer> sizes = groups.collect(new Function<RichIterable<String>, Integer>()
        {
            public Integer valueOf(RichIterable<String> richIterable)
            {
                return richIterable.size();
            }
        });
        Assert.assertEquals(FastList.newListWith(2, 2, 2, 1), sizes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        MutableCollection<String> collection = this.newWith("1", "2", "3", "4", "5", "6", "7");
        collection.chunk(0);
    }

    @Test
    public void chunk_large_size()
    {
        MutableCollection<String> collection = FastList.newListWith("1", "2", "3", "4", "5", "6", "7");
        Assert.assertEquals(collection, collection.chunk(10).getFirst());
    }

    @Test
    public void empty()
    {
        Verify.assertEmpty(this.classUnderTest());
        Assert.assertTrue(this.classUnderTest().isEmpty());
        Assert.assertFalse(this.classUnderTest().notEmpty());
    }

    @Test
    public void notEmpty()
    {
        MutableCollection<Integer> notEmpty = this.newWith(1);
        Verify.assertNotEmpty(notEmpty);
    }

    @Test
    public void with()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWith = coll.with(4);
        Assert.assertSame(coll, collWith);
        Assert.assertEquals(this.newWith(1, 2, 3, 4), collWith);
    }

    @Test
    public void withAll()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWith = coll.withAll(FastList.newListWith(4, 5));
        Assert.assertSame(coll, collWith);
        Assert.assertEquals(this.newWith(1, 2, 3, 4, 5), collWith);
    }

    @Test
    public void without()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWithout = coll.without(2);
        Assert.assertSame(coll, collWithout);
        Assert.assertEquals(this.newWith(1, 3), collWithout);
    }

    @Test
    public void withoutAll()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3, 4, 5);
        MutableCollection<Integer> collWithout = coll.withoutAll(FastList.newListWith(2, 4));
        Assert.assertSame(coll, collWithout);
        Assert.assertEquals(this.newWith(1, 3, 5), collWithout);
    }

    @Test
    public abstract void asSynchronized();

    @Test
    public abstract void asUnmodifiable();
}
