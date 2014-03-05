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

package com.gs.collections.impl.lazy.parallel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions0;
import com.gs.collections.impl.block.factory.Functions2;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.function.checked.CheckedFunction;
import com.gs.collections.impl.block.predicate.checked.CheckedPredicate;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractParallelIterableTestCase
{
    protected ExecutorService executorService;

    @Before
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(10);
        Assert.assertFalse(Thread.interrupted());
    }

    @After
    public void tearDown()
    {
        this.executorService.shutdownNow();
        Thread.interrupted();
    }

    // 1, 2, 2, 3, 3, 3, 4, 4, 4, 4
    protected abstract ParallelIterable<Integer> classUnderTest();

    // 1, 2, 2, 3, 3, 3, 4, 4, 4, 4
    protected abstract RichIterable<Integer> getExpected();

    protected abstract <T> RichIterable<T> getActual(ParallelIterable<T> actual);

    protected abstract boolean isOrdered();

    @Test(expected = UnsupportedOperationException.class)
    public void toArray()
    {
        Assert.assertArrayEquals(
                this.getExpected().toArray(),
                this.classUnderTest().toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toArray_array()
    {
        Assert.assertArrayEquals(
                this.getExpected().toArray(new Object[10]),
                this.classUnderTest().toArray(new Object[10]));
    }

    @Test
    public void forEach()
    {
        MutableCollection<Integer> actual = HashBag.<Integer>newBag().asSynchronized();
        this.classUnderTest().forEach(CollectionAddProcedure.on(actual));
        Assert.assertEquals(this.getExpected().toBag(), actual);
    }

    @Test
    public void forEachWith()
    {
        MutableCollection<Integer> actual = HashBag.<Integer>newBag().asSynchronized();
        this.classUnderTest().forEachWith(Procedures2.<Integer>addToCollection(), actual);
        Assert.assertEquals(this.getExpected().toBag(), actual);
    }

    @Test
    public void select()
    {
        Predicate<Integer> predicate = Predicates.greaterThan(1).and(Predicates.lessThan(4));

        Assert.assertEquals(
                this.getExpected().select(predicate),
                this.getActual(this.classUnderTest().select(predicate)));

        Assert.assertEquals(
                this.getExpected().select(predicate).toList().toBag(),
                this.classUnderTest().select(predicate).toList().toBag());

        Assert.assertEquals(
                this.getExpected().select(predicate).toBag(),
                this.classUnderTest().select(predicate).toBag());
    }

    @Test
    public void selectWith()
    {
        Assert.assertEquals(
                this.getExpected().selectWith(Predicates2.<Integer>greaterThan(), 1).selectWith(Predicates2.<Integer>lessThan(), 4),
                this.getActual(this.classUnderTest().selectWith(Predicates2.<Integer>greaterThan(), 1).selectWith(Predicates2.<Integer>lessThan(), 4)));

        Assert.assertEquals(
                this.getExpected().selectWith(Predicates2.<Integer>greaterThan(), 1).selectWith(Predicates2.<Integer>lessThan(), 4).toList().toBag(),
                this.classUnderTest().selectWith(Predicates2.<Integer>greaterThan(), 1).selectWith(Predicates2.<Integer>lessThan(), 4).toList().toBag());

        Assert.assertEquals(
                this.getExpected().selectWith(Predicates2.<Integer>greaterThan(), 1).selectWith(Predicates2.<Integer>lessThan(), 4).toBag(),
                this.classUnderTest().selectWith(Predicates2.<Integer>greaterThan(), 1).selectWith(Predicates2.<Integer>lessThan(), 4).toBag());
    }

    @Test
    public void reject()
    {
        Predicate<Integer> predicate = Predicates.lessThanOrEqualTo(1).and(Predicates.greaterThanOrEqualTo(4));

        Assert.assertEquals(
                this.getExpected().reject(predicate),
                this.getActual(this.classUnderTest().reject(predicate)));

        Assert.assertEquals(
                this.getExpected().reject(predicate).toList().toBag(),
                this.classUnderTest().reject(predicate).toList().toBag());

        Assert.assertEquals(
                this.getExpected().reject(predicate).toBag(),
                this.classUnderTest().reject(predicate).toBag());
    }

    @Test
    public void rejectWith()
    {
        Assert.assertEquals(
                this.getExpected().rejectWith(Predicates2.<Integer>lessThanOrEqualTo(), 1).rejectWith(Predicates2.<Integer>greaterThanOrEqualTo(), 4),
                this.getActual(this.classUnderTest().rejectWith(Predicates2.<Integer>lessThanOrEqualTo(), 1).rejectWith(Predicates2.<Integer>greaterThanOrEqualTo(), 4)));

        Assert.assertEquals(
                this.getExpected().rejectWith(Predicates2.<Integer>lessThanOrEqualTo(), 1).rejectWith(Predicates2.<Integer>greaterThanOrEqualTo(), 4).toList().toBag(),
                this.classUnderTest().rejectWith(Predicates2.<Integer>lessThanOrEqualTo(), 1).rejectWith(Predicates2.<Integer>greaterThanOrEqualTo(), 4).toList().toBag());

        Assert.assertEquals(
                this.getExpected().rejectWith(Predicates2.<Integer>lessThanOrEqualTo(), 1).rejectWith(Predicates2.<Integer>greaterThanOrEqualTo(), 4).toBag(),
                this.classUnderTest().rejectWith(Predicates2.<Integer>lessThanOrEqualTo(), 1).rejectWith(Predicates2.<Integer>greaterThanOrEqualTo(), 4).toBag());
    }

    @Test
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                this.getExpected().selectInstancesOf(Integer.class),
                this.getActual(this.classUnderTest().selectInstancesOf(Integer.class)));

        Assert.assertEquals(
                this.getExpected().selectInstancesOf(String.class),
                this.getActual(this.classUnderTest().selectInstancesOf(String.class)));

        Assert.assertEquals(
                this.getExpected().selectInstancesOf(Integer.class).toList().toBag(),
                this.classUnderTest().selectInstancesOf(Integer.class).toList().toBag());

        Assert.assertEquals(
                this.getExpected().selectInstancesOf(Integer.class).toBag(),
                this.classUnderTest().selectInstancesOf(Integer.class).toBag());
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(
                this.getExpected().collect(Functions.getToString()),
                this.getActual(this.classUnderTest().collect(Functions.getToString())));

        Assert.assertEquals(
                this.getExpected().collect(Functions.getToString()).toList().toBag(),
                this.classUnderTest().collect(Functions.getToString()).toList().toBag());

        Assert.assertEquals(
                this.getExpected().collect(Functions.getToString()).toBag(),
                this.classUnderTest().collect(Functions.getToString()).toBag());
    }

    @Test
    public void collectWith()
    {
        Function2<Integer, String, String> appendFunction = new Function2<Integer, String, String>()
        {
            public String value(Integer argument1, String argument2)
            {
                return argument1 + argument2;
            }
        };

        Assert.assertEquals(
                this.getExpected().collectWith(appendFunction, "!"),
                this.getActual(this.classUnderTest().collectWith(appendFunction, "!")));

        Assert.assertEquals(
                this.getExpected().collectWith(appendFunction, "!").toList().toBag(),
                this.classUnderTest().collectWith(appendFunction, "!").toList().toBag());

        Assert.assertEquals(
                this.getExpected().collectWith(appendFunction, "!").toBag(),
                this.classUnderTest().collectWith(appendFunction, "!").toBag());
    }

    @Test
    public void collectIf()
    {
        Predicate<Integer> predicate = Predicates.greaterThan(1).and(Predicates.lessThan(4));

        Assert.assertEquals(
                this.getExpected().collectIf(predicate, Functions.getToString()),
                this.getActual(this.classUnderTest().collectIf(predicate, Functions.getToString())));

        Assert.assertEquals(
                this.getExpected().collectIf(predicate, Functions.getToString()).toList().toBag(),
                this.classUnderTest().collectIf(predicate, Functions.getToString()).toList().toBag());

        Assert.assertEquals(
                this.getExpected().collectIf(predicate, Functions.getToString()).toBag(),
                this.classUnderTest().collectIf(predicate, Functions.getToString()).toBag());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void flatCollect()
    {
        Function<Integer, Interval> intervalFunction = new Function<Integer, Interval>()
        {
            public Interval valueOf(Integer each)
            {
                return Interval.oneTo(each);
            }
        };

        Assert.assertEquals(
                this.getExpected().flatCollect(intervalFunction),
                this.getActual(this.classUnderTest().flatCollect(intervalFunction)));

        Assert.assertEquals(
                this.getExpected().flatCollect(intervalFunction).toList().toBag(),
                this.classUnderTest().flatCollect(intervalFunction).toList().toBag());

        Assert.assertEquals(
                this.getExpected().flatCollect(intervalFunction).toBag(),
                this.classUnderTest().flatCollect(intervalFunction).toBag());
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detect(Predicates.equal(3)));
        Assert.assertNull(this.classUnderTest().detect(Predicates.equal(8)));
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detectIfNone(Predicates.equal(3), Functions0.value(8)));
        Assert.assertEquals(Integer.valueOf(8), this.classUnderTest().detectIfNone(Predicates.equal(6), Functions0.value(8)));
    }

    @Test
    public void detectWith()
    {
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detectWith(Predicates2.equal(), Integer.valueOf(3)));
        Assert.assertNull(this.classUnderTest().detectWith(Predicates2.equal(), Integer.valueOf(8)));
    }

    @Test
    public void detectWithIfNone()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(Integer.valueOf(1000));
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detectWithIfNone(Predicates2.equal(), Integer.valueOf(3), function));
        Assert.assertEquals(Integer.valueOf(1000), this.classUnderTest().detectWithIfNone(Predicates2.equal(), Integer.valueOf(8), function));
    }

    // @Test(expected = NoSuchElementException.class)
    @Test(expected = UnsupportedOperationException.class)
    public void min_empty_throws()
    {
        this.classUnderTest().select(Predicates.alwaysFalse()).min(Comparators.naturalOrder());
    }

    // @Test(expected = NoSuchElementException.class)
    @Test(expected = UnsupportedOperationException.class)
    public void max_empty_throws()
    {
        this.classUnderTest().select(Predicates.alwaysFalse()).max(Comparators.naturalOrder());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min(Comparators.naturalOrder()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(4), this.classUnderTest().max(Comparators.naturalOrder()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().minBy(Functions.getToString()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(4), this.classUnderTest().maxBy(Functions.getToString()));
    }

    // @Test(expected = NoSuchElementException.class)
    @Test(expected = UnsupportedOperationException.class)
    public void min_empty_throws_without_comparator()
    {
        this.classUnderTest().select(Predicates.alwaysFalse()).min();
    }

    // @Test(expected = NoSuchElementException.class)
    @Test(expected = UnsupportedOperationException.class)
    public void max_empty_throws_without_comparator()
    {
        this.classUnderTest().select(Predicates.alwaysFalse()).max();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void max_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(4), this.classUnderTest().max());
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.classUnderTest().anySatisfy(Predicates.lessThan(2)));
        Assert.assertTrue(this.classUnderTest().anySatisfy(Predicates.lessThan(5)));
        Assert.assertFalse(this.classUnderTest().anySatisfy(Predicates.lessThan(1)));
        Assert.assertFalse(this.classUnderTest().anySatisfy(Predicates.greaterThan(5)));
    }

    @Test
    public void anySatisfyWith()
    {
        Assert.assertTrue(this.classUnderTest().anySatisfyWith(Predicates2.<Integer>lessThan(), 2));
        Assert.assertTrue(this.classUnderTest().anySatisfyWith(Predicates2.<Integer>lessThan(), 5));
        Assert.assertFalse(this.classUnderTest().anySatisfyWith(Predicates2.<Integer>lessThan(), 1));
        Assert.assertFalse(this.classUnderTest().anySatisfyWith(Predicates2.<Integer>greaterThan(), 5));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.classUnderTest().allSatisfy(Predicates.greaterThan(0)));
        Assert.assertTrue(this.classUnderTest().allSatisfy(Predicates.lessThan(5)));
        Assert.assertFalse(this.classUnderTest().allSatisfy(Predicates.lessThan(3)));
        Assert.assertFalse(this.classUnderTest().allSatisfy(Predicates.greaterThan(5)));
    }

    @Test
    public void allSatisfyWith()
    {
        Assert.assertTrue(this.classUnderTest().allSatisfyWith(Predicates2.<Integer>greaterThan(), 0));
        Assert.assertTrue(this.classUnderTest().allSatisfyWith(Predicates2.<Integer>lessThan(), 5));
        Assert.assertFalse(this.classUnderTest().allSatisfyWith(Predicates2.<Integer>lessThan(), 3));
        Assert.assertFalse(this.classUnderTest().allSatisfyWith(Predicates2.<Integer>greaterThan(), 5));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertTrue(this.classUnderTest().noneSatisfy(Predicates.greaterThan(5)));
        Assert.assertTrue(this.classUnderTest().noneSatisfy(Predicates.lessThan(0)));
        Assert.assertFalse(this.classUnderTest().noneSatisfy(Predicates.lessThan(5)));
        Assert.assertFalse(this.classUnderTest().noneSatisfy(Predicates.lessThan(3)));
    }

    @Test
    public void noneSatisfyWith()
    {
        Assert.assertTrue(this.classUnderTest().noneSatisfyWith(Predicates2.<Integer>greaterThan(), 5));
        Assert.assertTrue(this.classUnderTest().noneSatisfyWith(Predicates2.<Integer>lessThan(), 0));
        Assert.assertFalse(this.classUnderTest().noneSatisfyWith(Predicates2.<Integer>lessThan(), 5));
        Assert.assertFalse(this.classUnderTest().noneSatisfyWith(Predicates2.<Integer>lessThan(), 3));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(
                this.getExpected().count(IntegerPredicates.isEven()),
                this.classUnderTest().count(IntegerPredicates.isEven()));
    }

    @Test
    public void countWith()
    {
        Assert.assertEquals(
                this.getExpected().countWith(Predicates2.<Integer>greaterThan(), 2),
                this.classUnderTest().countWith(Predicates2.<Integer>greaterThan(), 2));
    }

    @Test
    public void toList()
    {
        if (this.isOrdered())
        {
            Assert.assertEquals(
                    this.getExpected().toList(),
                    this.classUnderTest().toList());
        }
        else
        {
            Assert.assertEquals(
                    this.getExpected().toList().toBag(),
                    this.classUnderTest().toList().toBag());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toSortedList()
    {
        Assert.assertEquals(
                this.getExpected().toSortedList(),
                this.classUnderTest().toSortedList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toSortedList_comparator()
    {
        Assert.assertEquals(
                this.getExpected().toSortedList(Comparators.reverseNaturalOrder()),
                this.classUnderTest().toSortedList(Comparators.reverseNaturalOrder()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toSortedListBy()
    {
        Assert.assertEquals(
                this.getExpected().toSortedListBy(Functions.getToString()),
                this.classUnderTest().toSortedListBy(Functions.getToString()));
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), this.classUnderTest().toSet());
    }

    @Test
    public void toSortedSet()
    {
        Verify.assertSortedSetsEqual(
                TreeSortedSet.newSetWith(1, 2, 3, 4),
                this.classUnderTest().toSortedSet());
    }

    @Test
    public void toSortedSet_comparator()
    {
        Verify.assertSortedSetsEqual(
                TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder(), 1, 2, 3, 4),
                this.classUnderTest().toSortedSet(Comparators.reverseNaturalOrder()));
    }

    @Test
    public void toSortedSetBy()
    {
        Verify.assertSortedSetsEqual(
                TreeSortedSet.newSetWith(1, 2, 3, 4),
                this.classUnderTest().toSortedSetBy(Functions.getToString()));
    }

    @Test
    public void toMap()
    {
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"),
                this.classUnderTest().toMap(Functions.getToString(), Functions.getToString()));
    }

    @Test
    public void toSortedMap()
    {
        MutableSortedMap<Integer, String> map = this.classUnderTest().toSortedMap(Functions.getIntegerPassThru(), Functions.getToString());
        Verify.assertSortedMapsEqual(TreeSortedMap.newMapWith(1, "1", 2, "2", 3, "3", 4, "4"), map);
        Verify.assertListsEqual(FastList.newListWith(1, 2, 3, 4), map.keySet().toList());
    }

    @Test
    public void toSortedMap_comparator()
    {
        MutableSortedMap<Integer, String> map = this.classUnderTest().toSortedMap(
                Comparators.<Integer>reverseNaturalOrder(),
                Functions.getIntegerPassThru(),
                Functions.getToString());
        Verify.assertSortedMapsEqual(TreeSortedMap.newMapWith(Comparators.<Integer>reverseNaturalOrder(), 1, "1", 2, "2", 3, "3", 4, "4"), map);
        Verify.assertListsEqual(FastList.newListWith(4, 3, 2, 1), map.keySet().toList());
    }

    @Test
    public void testToString()
    {
        String expectedString = this.getExpected().toString();
        String actualString = this.classUnderTest().toString();
        this.assertStringsEqual("\\[\\d(, \\d)*\\]", expectedString, actualString);
    }

    @Test
    public void makeString()
    {
        String expectedString = this.getExpected().makeString();
        String actualString = this.classUnderTest().makeString();
        this.assertStringsEqual("\\d(, \\d)*", expectedString, actualString);
    }

    @Test
    public void makeString_separator()
    {
        String expectedString = this.getExpected().makeString("~");
        String actualString = this.classUnderTest().makeString("~");
        this.assertStringsEqual("\\d(~\\d)*", expectedString, actualString);
    }

    @Test
    public void makeString_start_separator_end()
    {
        String expectedString = this.getExpected().makeString("<", "~", ">");
        String actualString = this.classUnderTest().makeString("<", "~", ">");
        this.assertStringsEqual("<\\d(~\\d)*>", expectedString, actualString);
    }

    @Test
    public void appendString()
    {
        StringBuilder expected = new StringBuilder();
        this.getExpected().appendString(expected);
        String expectedString = expected.toString();

        StringBuilder actual = new StringBuilder();
        this.classUnderTest().appendString(actual);
        String actualString = actual.toString();

        this.assertStringsEqual("\\d(, \\d)*", expectedString, actualString);
    }

    @Test
    public void appendString_separator()
    {
        StringBuilder expected = new StringBuilder();
        this.getExpected().appendString(expected, "~");
        String expectedString = expected.toString();

        StringBuilder actual = new StringBuilder();
        this.classUnderTest().appendString(actual, "~");
        String actualString = actual.toString();

        this.assertStringsEqual("\\d(~\\d)*", expectedString, actualString);
    }

    @Test
    public void appendString_start_separator_end()
    {
        StringBuilder expected = new StringBuilder();
        this.getExpected().appendString(expected, "<", "~", ">");
        String expectedString = expected.toString();

        StringBuilder actual = new StringBuilder();
        this.classUnderTest().appendString(actual, "<", "~", ">");
        String actualString = actual.toString();

        this.assertStringsEqual("<\\d(~\\d)*>", expectedString, actualString);
    }

    @Test
    public void appendString_throws()
    {
        try
        {
            this.classUnderTest().appendString(new Appendable()
            {
                public Appendable append(CharSequence csq) throws IOException
                {
                    throw new IOException("Test exception");
                }

                public Appendable append(CharSequence csq, int start, int end) throws IOException
                {
                    throw new IOException("Test exception");
                }

                public Appendable append(char c) throws IOException
                {
                    throw new IOException("Test exception");
                }
            });
            Assert.fail();
        }
        catch (RuntimeException e)
        {
            IOException cause = (IOException) e.getCause();
            Assert.assertEquals("Test exception", cause.getMessage());
        }
    }

    private void assertStringsEqual(String regex, String expectedString, String actualString)
    {
        if (this.isOrdered())
        {
            Assert.assertEquals(expectedString, actualString);
        }
        else
        {
            Assert.assertEquals(
                    CharHashBag.newBagWith(expectedString.toCharArray()),
                    CharHashBag.newBagWith(actualString.toCharArray()));
            Assert.assertTrue(Pattern.matches(regex, actualString));
        }
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

        Assert.assertEquals(
                this.getExpected().groupBy(isOddFunction),
                this.classUnderTest().groupBy(isOddFunction));
    }

    @Test
    public void groupByEach()
    {
        Assert.assertEquals(
                this.getExpected().groupByEach(new NegativeIntervalFunction()),
                this.classUnderTest().groupByEach(new NegativeIntervalFunction()));
    }

    @Test
    public void aggregateBy()
    {
        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };

        Assert.assertEquals(
                this.getExpected().aggregateBy(isOddFunction, Functions0.value(0), Functions2.integerAddition()),
                this.classUnderTest().aggregateBy(isOddFunction, Functions0.value(0), Functions2.integerAddition()));
    }

    @Test
    public void aggregateInPlaceBy()
    {
        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };

        Procedure2<AtomicInteger, Integer> sumAggregator = new

                Procedure2<AtomicInteger, Integer>()
                {
                    public void value(AtomicInteger aggregate, Integer value)
                    {
                        aggregate.addAndGet(value);
                    }
                };

        Function2<Boolean, AtomicInteger, Pair<Boolean, Integer>> atomicIntToInt = new Function2<Boolean, AtomicInteger, Pair<Boolean, Integer>>()
        {
            public Pair<Boolean, Integer> value(Boolean argument1, AtomicInteger argument2)
            {
                return Tuples.pair(argument1, argument2.get());
            }
        };

        Assert.assertEquals(
                this.getExpected().aggregateInPlaceBy(isOddFunction, Functions0.zeroAtomicInteger(), sumAggregator).collect(atomicIntToInt),
                this.classUnderTest().aggregateInPlaceBy(isOddFunction, Functions0.zeroAtomicInteger(), sumAggregator).collect(atomicIntToInt));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sumOfInt()
    {
        IntFunction<Integer> intFunction = new IntFunction<Integer>()
        {
            public int intValueOf(Integer integer)
            {
                return integer.intValue();
            }
        };
        Assert.assertEquals(
                this.getExpected().sumOfInt(intFunction),
                this.classUnderTest().sumOfInt(intFunction));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sumOfLong()
    {
        LongFunction<Integer> longFunction = new LongFunction<Integer>()
        {
            public long longValueOf(Integer integer)
            {
                return integer.longValue();
            }
        };
        Assert.assertEquals(
                this.getExpected().sumOfLong(longFunction),
                this.classUnderTest().sumOfLong(longFunction));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sumOfFloat()
    {
        FloatFunction<Integer> floatFunction = new FloatFunction<Integer>()
        {
            public float floatValueOf(Integer integer)
            {
                return integer.floatValue();
            }
        };
        Assert.assertEquals(
                this.getExpected().sumOfFloat(floatFunction),
                this.classUnderTest().sumOfFloat(floatFunction),
                0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sumOfDouble()
    {
        DoubleFunction<Integer> doubleFunction = new DoubleFunction<Integer>()
        {
            public double doubleValueOf(Integer integer)
            {
                return integer.doubleValue();
            }
        };
        Assert.assertEquals(
                this.getExpected().sumOfDouble(doubleFunction),
                this.classUnderTest().sumOfDouble(doubleFunction),
                0.0);
    }

    @Test
    public void asUnique()
    {
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), this.classUnderTest().asUnique().toSet());
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), this.classUnderTest().asUnique().toList().toSet());

        Assert.assertEquals(FastList.newListWith("!"), this.classUnderTest().collect(new Function<Integer, String>()
        {
            public String valueOf(Integer each)
            {
                return "!";
            }
        }).asUnique().toList());
    }

    @Test
    public void forEach_executionException()
    {
        try
        {
            this.classUnderTest().forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    throw new RuntimeException("Execution exception");
                }
            });
        }
        catch (RuntimeException e)
        {
            ExecutionException executionException = (ExecutionException) e.getCause();
            RuntimeException runtimeException = (RuntimeException) executionException.getCause();
            Assert.assertEquals("Execution exception", runtimeException.getMessage());
        }
    }

    @Test
    public void collect_executionException()
    {
        try
        {
            this.classUnderTest().collect(new Function<Integer, String>()
            {
                public String valueOf(Integer each)
                {
                    throw new RuntimeException("Execution exception");
                }
            }).toString();
        }
        catch (RuntimeException e)
        {
            ExecutionException executionException = (ExecutionException) e.getCause();
            RuntimeException runtimeException = (RuntimeException) executionException.getCause();
            Assert.assertEquals("Execution exception", runtimeException.getMessage());
        }
    }

    @Test
    public void anySatisfy_executionException()
    {
        try
        {
            this.classUnderTest().anySatisfy(new Predicate<Integer>()
            {
                public boolean accept(Integer each)
                {
                    throw new RuntimeException("Execution exception");
                }
            });
        }
        catch (RuntimeException e)
        {
            ExecutionException executionException = (ExecutionException) e.getCause();
            RuntimeException runtimeException = (RuntimeException) executionException.getCause();
            Assert.assertEquals("Execution exception", runtimeException.getMessage());
        }
    }

    @Test
    public void allSatisfy_executionException()
    {
        try
        {
            this.classUnderTest().allSatisfy(new Predicate<Integer>()
            {
                public boolean accept(Integer each)
                {
                    throw new RuntimeException("Execution exception");
                }
            });
        }
        catch (RuntimeException e)
        {
            ExecutionException executionException = (ExecutionException) e.getCause();
            RuntimeException runtimeException = (RuntimeException) executionException.getCause();
            Assert.assertEquals("Execution exception", runtimeException.getMessage());
        }
    }

    @Test
    public void detect_executionException()
    {
        try
        {
            this.classUnderTest().detect(new Predicate<Integer>()
            {
                public boolean accept(Integer each)
                {
                    throw new RuntimeException("Execution exception");
                }
            });
        }
        catch (RuntimeException e)
        {
            ExecutionException executionException = (ExecutionException) e.getCause();
            RuntimeException runtimeException = (RuntimeException) executionException.getCause();
            Assert.assertEquals("Execution exception", runtimeException.getMessage());
        }
    }

    @Test
    public void forEach_interruptedException()
    {
        final MutableCollection<Integer> actual1 = HashBag.<Integer>newBag().asSynchronized();

        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, new Runnable()
        {
            public void run()
            {
                AbstractParallelIterableTestCase.this.classUnderTest().forEach(new CheckedProcedure<Integer>()
                {
                    @Override
                    public void safeValue(Integer each) throws InterruptedException
                    {
                        Thread.sleep(1000);
                        actual1.add(each);
                    }
                });
            }
        });
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        MutableCollection<Integer> actual2 = HashBag.<Integer>newBag().asSynchronized();
        this.classUnderTest().forEach(CollectionAddProcedure.on(actual2));
        Assert.assertEquals(this.getExpected().toBag(), actual2);
    }

    @Test
    public void anySatisfy_interruptedException()
    {
        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, new Runnable()
        {
            public void run()
            {
                AbstractParallelIterableTestCase.this.classUnderTest().anySatisfy(new CheckedPredicate<Integer>()
                {
                    @Override
                    public boolean safeAccept(Integer each) throws InterruptedException
                    {
                        Thread.sleep(1000);
                        return each < 1;
                    }
                });
            }
        });
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        Assert.assertFalse(this.classUnderTest().anySatisfy(Predicates.lessThan(1)));
    }

    @Test
    public void allSatisfy_interruptedException()
    {
        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, new Runnable()
        {
            public void run()
            {
                AbstractParallelIterableTestCase.this.classUnderTest().allSatisfy(new CheckedPredicate<Integer>()
                {
                    @Override
                    public boolean safeAccept(Integer each) throws InterruptedException
                    {
                        Thread.sleep(1000);
                        return each < 5;
                    }
                });
            }
        });
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        Assert.assertTrue(this.classUnderTest().allSatisfy(Predicates.lessThan(5)));
    }

    @Test
    public void detect_interruptedException()
    {
        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, new Runnable()
        {
            public void run()
            {
                AbstractParallelIterableTestCase.this.classUnderTest().detect(new CheckedPredicate<Integer>()
                {
                    @Override
                    public boolean safeAccept(Integer each) throws InterruptedException
                    {
                        Thread.sleep(1000);
                        return each.intValue() == 3;
                    }
                });
            }
        });
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detect(Predicates.equal(3)));
    }

    @Test
    public void toString_interruptedException()
    {
        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, new Runnable()
        {
            public void run()
            {
                AbstractParallelIterableTestCase.this.classUnderTest().collect(new CheckedFunction<Integer, String>()
                {
                    @Override
                    public String safeValueOf(Integer each) throws InterruptedException
                    {
                        Thread.sleep(1000);
                        return String.valueOf(each);
                    }
                }).toString();
            }
        });
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        MutableCollection<Integer> actual = HashBag.<Integer>newBag().asSynchronized();
        this.classUnderTest().forEach(CollectionAddProcedure.on(actual));
        Assert.assertEquals(this.getExpected().toBag(), actual);
    }
}
