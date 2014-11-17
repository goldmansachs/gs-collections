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
import java.util.NoSuchElementException;
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
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
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

    protected abstract boolean isUnique();

    @Test
    public void toArray()
    {
        Assert.assertEquals(
                HashBag.newBagWith(this.getExpected().toArray()),
                HashBag.newBagWith(this.classUnderTest().toArray()));
    }

    @Test
    public void toArray_array()
    {
        Assert.assertEquals(
                HashBag.newBagWith(this.getExpected().toArray(new Object[10])),
                HashBag.newBagWith(this.classUnderTest().toArray(new Object[10])));
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

        Function<Integer, Number> numberFunction = integer -> {
            if (IntegerPredicates.isEven().accept(integer))
            {
                return Double.valueOf(integer.doubleValue());
            }
            return integer;
        };

        Assert.assertEquals(
                this.getExpected().collect(numberFunction).selectInstancesOf(Integer.class),
                this.getActual(this.classUnderTest().collect(numberFunction).selectInstancesOf(Integer.class)));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(
                this.getExpected().collect(String::valueOf),
                this.getActual(this.classUnderTest().collect(String::valueOf)));

        Assert.assertEquals(
                this.getExpected().collect(String::valueOf, HashBag.newBag()),
                this.classUnderTest().collect(String::valueOf).toList().toBag());

        Assert.assertEquals(
                this.getExpected().collect(String::valueOf).toBag(),
                this.classUnderTest().collect(String::valueOf).toBag());

        Object constant = new Object();
        Assert.assertEquals(
                this.getExpected().collect(ignored -> constant, HashBag.newBag()),
                this.classUnderTest().collect(ignored -> constant).toList().toBag());
    }

    @Test
    public void collectWith()
    {
        Function2<Integer, String, String> appendFunction = (argument1, argument2) -> argument1 + argument2;

        Assert.assertEquals(
                this.getExpected().collectWith(appendFunction, "!"),
                this.getActual(this.classUnderTest().collectWith(appendFunction, "!")));

        Assert.assertEquals(
                this.getExpected().collectWith(appendFunction, "!", HashBag.newBag()),
                this.classUnderTest().collectWith(appendFunction, "!").toList().toBag());

        Assert.assertEquals(
                this.getExpected().collectWith(appendFunction, "!").toBag(),
                this.classUnderTest().collectWith(appendFunction, "!").toBag());

        Object constant = new Object();
        Assert.assertEquals(
                this.getExpected().collectWith((ignored1, ignored2) -> constant, "!", HashBag.newBag()),
                this.classUnderTest().collectWith((ignored1, ignored2) -> constant, "!").toList().toBag());
    }

    @Test
    public void collectIf()
    {
        Predicate<Integer> predicate = Predicates.greaterThan(1).and(Predicates.lessThan(4));

        Assert.assertEquals(
                this.getExpected().collectIf(predicate, String::valueOf),
                this.getActual(this.classUnderTest().collectIf(predicate, String::valueOf)));

        Assert.assertEquals(
                this.getExpected().collectIf(predicate, String::valueOf, HashBag.newBag()),
                this.classUnderTest().collectIf(predicate, String::valueOf).toList().toBag());

        Assert.assertEquals(
                this.getExpected().collectIf(predicate, String::valueOf).toBag(),
                this.classUnderTest().collectIf(predicate, String::valueOf).toBag());

        Object constant = new Object();
        Assert.assertEquals(
                this.getExpected().collectIf(predicate, ignored -> constant, HashBag.newBag()),
                this.classUnderTest().collectIf(predicate, ignored -> constant).toList().toBag());
    }

    @Test
    public void flatCollect()
    {
        Function<Integer, Iterable<Integer>> intervalFunction = Interval::oneTo;
        Assert.assertEquals(
                this.getExpected().flatCollect(intervalFunction),
                this.getActual(this.classUnderTest().flatCollect(intervalFunction)));

        Assert.assertEquals(
                this.getExpected().flatCollect(intervalFunction, HashBag.newBag()),
                this.classUnderTest().flatCollect(intervalFunction).toList().toBag());

        Assert.assertEquals(
                this.getExpected().flatCollect(intervalFunction, HashBag.newBag()),
                this.classUnderTest().flatCollect(intervalFunction).toBag());
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detect(Integer.valueOf(3)::equals));
        Assert.assertNull(this.classUnderTest().detect(Integer.valueOf(8)::equals));
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detectIfNone(Integer.valueOf(3)::equals, () -> 8));
        Assert.assertEquals(Integer.valueOf(8), this.classUnderTest().detectIfNone(Integer.valueOf(6)::equals, () -> 8));
    }

    @Test
    public void detectWith()
    {
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detectWith(Object::equals, Integer.valueOf(3)));
        Assert.assertNull(this.classUnderTest().detectWith(Object::equals, Integer.valueOf(8)));
    }

    @Test
    public void detectWithIfNone()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(Integer.valueOf(1000));
        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detectWithIfNone(Object::equals, Integer.valueOf(3), function));
        Assert.assertEquals(Integer.valueOf(1000), this.classUnderTest().detectWithIfNone(Object::equals, Integer.valueOf(8), function));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws()
    {
        this.classUnderTest().select(ignored -> false).min(Integer::compareTo);
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws()
    {
        this.classUnderTest().select(ignored -> false).max(Integer::compareTo);
    }

    @Test
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min(Integer::compareTo));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(4), this.classUnderTest().max(Integer::compareTo));
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().minBy(String::valueOf));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(4), this.classUnderTest().maxBy(String::valueOf));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws_without_comparator()
    {
        this.classUnderTest().select(ignored -> false).min();
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws_without_comparator()
    {
        this.classUnderTest().select(ignored -> false).max();
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min());
    }

    @Test
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

    @Test
    public void toSortedList()
    {
        Assert.assertEquals(
                this.getExpected().toSortedList(),
                this.classUnderTest().toSortedList());
    }

    @Test
    public void toSortedList_comparator()
    {
        Assert.assertEquals(
                this.getExpected().toSortedList(Comparators.reverseNaturalOrder()),
                this.classUnderTest().toSortedList(Comparators.reverseNaturalOrder()));
    }

    @Test
    public void toSortedListBy()
    {
        Assert.assertEquals(
                this.getExpected().toSortedListBy(String::valueOf),
                this.classUnderTest().toSortedListBy(String::valueOf));
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
                this.classUnderTest().toSortedSetBy(String::valueOf));
    }

    @Test
    public void toSortedBag()
    {
        Assert.assertEquals(
                this.getExpected().toSortedBag(),
                this.classUnderTest().toSortedBag());
    }

    @Test
    public void toSortedBag_comparator()
    {
        Assert.assertEquals(
                this.getExpected().toSortedBag(Comparators.reverseNaturalOrder()),
                this.classUnderTest().toSortedBag(Comparators.reverseNaturalOrder()));
    }

    @Test
    public void toSortedBagBy()
    {
        Assert.assertEquals(
                this.getExpected().toSortedBagBy(String::valueOf),
                this.classUnderTest().toSortedBagBy(String::valueOf));
    }

    @Test
    public void toMap()
    {
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"),
                this.classUnderTest().toMap(String::valueOf, String::valueOf));
    }

    @Test
    public void toSortedMap()
    {
        MutableSortedMap<Integer, String> map = this.classUnderTest().toSortedMap(Functions.getIntegerPassThru(), String::valueOf);
        Verify.assertSortedMapsEqual(TreeSortedMap.newMapWith(1, "1", 2, "2", 3, "3", 4, "4"), map);
        Verify.assertListsEqual(FastList.newListWith(1, 2, 3, 4), map.keySet().toList());
    }

    @Test
    public void toSortedMap_comparator()
    {
        MutableSortedMap<Integer, String> map = this.classUnderTest().toSortedMap(
                Comparators.<Integer>reverseNaturalOrder(),
                Functions.getIntegerPassThru(),
                String::valueOf);
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

    protected void assertStringsEqual(String regex, String expectedString, String actualString)
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
        Function<Integer, Boolean> isOddFunction = object -> IntegerPredicates.isOdd().accept(object);

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
    public void groupByUniqueKey()
    {
        if (this.isUnique())
        {
            Assert.assertEquals(
                    this.getExpected().groupByUniqueKey(id -> id),
                    this.classUnderTest().groupByUniqueKey(id -> id));
        }
        else
        {
            // IllegalStateException in serial, RuntimeException with IllegalStateException cause in parallel
            try
            {
                this.classUnderTest().groupByUniqueKey(id -> id);
            }
            catch (RuntimeException ignored)
            {
                return;
            }
            Assert.fail();
        }
    }

    @Test
    public void aggregateBy()
    {
        Function<Integer, Boolean> isOddFunction = object -> IntegerPredicates.isOdd().accept(object);

        Assert.assertEquals(
                this.getExpected().aggregateBy(isOddFunction, () -> 0, (integer11, integer21) -> integer11 + integer21),
                this.classUnderTest().aggregateBy(isOddFunction, () -> 0, (integer1, integer2) -> integer1 + integer2));
    }

    @Test
    public void aggregateInPlaceBy()
    {
        Function<Integer, Boolean> isOddFunction = object -> IntegerPredicates.isOdd().accept(object);

        Function2<Boolean, AtomicInteger, Pair<Boolean, Integer>> atomicIntToInt = (argument1, argument2) -> Tuples.pair(argument1, argument2.get());

        Assert.assertEquals(
                this.getExpected().aggregateInPlaceBy(isOddFunction, AtomicInteger::new, AtomicInteger::addAndGet).collect(atomicIntToInt),
                this.classUnderTest().aggregateInPlaceBy(isOddFunction, AtomicInteger::new, AtomicInteger::addAndGet).collect(atomicIntToInt));
    }

    @Test
    public void sumOfInt()
    {
        Assert.assertEquals(
                this.getExpected().sumOfInt(Integer::intValue),
                this.classUnderTest().sumOfInt(Integer::intValue));
    }

    @Test
    public void sumOfLong()
    {
        Assert.assertEquals(
                this.getExpected().sumOfLong(Integer::longValue),
                this.classUnderTest().sumOfLong(Integer::longValue));
    }

    @Test
    public void sumOfFloat()
    {
        Assert.assertEquals(
                this.getExpected().sumOfFloat(Integer::floatValue),
                this.classUnderTest().sumOfFloat(Integer::floatValue),
                0.0);
    }

    @Test
    public void sumOfDouble()
    {
        Assert.assertEquals(
                this.getExpected().sumOfDouble(Integer::doubleValue),
                this.classUnderTest().sumOfDouble(Integer::doubleValue),
                0.0);
    }

    @Test
    public void asUnique()
    {
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), this.classUnderTest().asUnique().toSet());
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), this.classUnderTest().asUnique().toList().toSet());

        Assert.assertEquals(FastList.newListWith("!"), this.classUnderTest().collect(each -> "!").asUnique().toList());
    }

    @Test
    public void forEach_executionException()
    {
        try
        {
            this.classUnderTest().forEach(each -> { throw new RuntimeException("Execution exception"); });
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
            this.classUnderTest().collect(each -> { throw new RuntimeException("Execution exception"); }).toString();
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
            this.classUnderTest().anySatisfy(each -> { throw new RuntimeException("Execution exception"); });
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
            this.classUnderTest().allSatisfy(each -> { throw new RuntimeException("Execution exception"); });
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
            this.classUnderTest().detect(each -> { throw new RuntimeException("Execution exception"); });
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
        Verify.assertThrowsWithCause(
                RuntimeException.class,
                InterruptedException.class,
                () -> this.classUnderTest().forEach(new CheckedProcedure<Integer>()
                {
                    @Override
                    public void safeValue(Integer each) throws InterruptedException
                    {
                        Thread.sleep(1000);
                        actual1.add(each);
                    }
                }));
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
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, () -> this.classUnderTest().anySatisfy(new CheckedPredicate<Integer>()
        {
            @Override
            public boolean safeAccept(Integer each) throws InterruptedException
            {
                Thread.sleep(1000);
                return each < 1;
            }
        }));
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        Assert.assertFalse(this.classUnderTest().anySatisfy(Predicates.lessThan(1)));
    }

    @Test
    public void allSatisfy_interruptedException()
    {
        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, () -> this.classUnderTest().allSatisfy(new CheckedPredicate<Integer>()
        {
            @Override
            public boolean safeAccept(Integer each) throws InterruptedException
            {
                Thread.sleep(1000);
                return each < 5;
            }
        }));
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        Assert.assertTrue(this.classUnderTest().allSatisfy(Predicates.lessThan(5)));
    }

    @Test
    public void detect_interruptedException()
    {
        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, () -> this.classUnderTest().detect(new CheckedPredicate<Integer>()
        {
            @Override
            public boolean safeAccept(Integer each) throws InterruptedException
            {
                Thread.sleep(1000);
                return each.intValue() == 3;
            }
        }));
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        Assert.assertEquals(Integer.valueOf(3), this.classUnderTest().detect(Integer.valueOf(3)::equals));
    }

    @Test
    public void toString_interruptedException()
    {
        Thread.currentThread().interrupt();
        Verify.assertThrowsWithCause(RuntimeException.class, InterruptedException.class, () -> this.classUnderTest().collect(new CheckedFunction<Integer, String>()
        {
            @Override
            public String safeValueOf(Integer each) throws InterruptedException
            {
                Thread.sleep(1000);
                return String.valueOf(each);
            }
        }).toString());
        Assert.assertTrue(Thread.interrupted());
        Assert.assertFalse(Thread.interrupted());

        MutableCollection<Integer> actual = HashBag.<Integer>newBag().asSynchronized();
        this.classUnderTest().forEach(CollectionAddProcedure.on(actual));
        Assert.assertEquals(this.getExpected().toBag(), actual);
    }
}
