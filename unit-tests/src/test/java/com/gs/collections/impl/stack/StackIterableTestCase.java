/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.stack;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.partition.stack.PartitionStack;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.stack.StackIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public abstract class StackIterableTestCase
{
    protected abstract <T> StackIterable<T> newStackWith(T... elements);

    protected abstract <T> StackIterable<T> newStackFromTopToBottom(T... elements);

    protected abstract <T> StackIterable<T> newStackFromTopToBottom(Iterable<T> elements);

    protected abstract <T> StackIterable<T> newStack(Iterable<T> elements);

    @Test
    public void testNewStackFromTopToBottom()
    {
        Assert.assertEquals(
                this.newStackWith(3, 2, 1),
                this.newStackFromTopToBottom(1, 2, 3));
    }

    @Test(expected = EmptyStackException.class)
    public void peek_empty_throws()
    {
        this.newStackWith().peek();
    }

    @Test(expected = EmptyStackException.class)
    public void peek_int_empty_throws()
    {
        this.newStackWith().peek(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void peek_int_count_throws()
    {
        this.newStackWith(1, 2, 3).peek(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void peek_int_neg_throws()
    {
        this.newStackWith(1, 2, 3).peek(-1);
    }

    @Test
    public void peek_illegal_arguments()
    {
        final StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                stack.peek(-1);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                stack.peek(4);
            }
        });

        Assert.assertEquals(FastList.newListWith(1, 2, 3), stack.peek(3));
    }

    @Test
    public void peekAt()
    {
        Assert.assertEquals("3", this.newStackWith("1", "2", "3").peekAt(0));
        Assert.assertEquals("2", this.newStackWith("1", "2", "3").peekAt(1));
        Assert.assertEquals("1", this.newStackWith("1", "2", "3").peekAt(2));
    }

    @Test
    public void size()
    {
        StackIterable<Integer> stack1 = this.newStackWith();
        Assert.assertEquals(0, stack1.size());

        StackIterable<Integer> stack2 = this.newStackWith(1, 2);
        Assert.assertEquals(2, stack2.size());
    }

    @Test
    public void isEmpty()
    {
        StackIterable<Integer> stack = this.newStackWith();
        Assert.assertTrue(stack.isEmpty());
        Assert.assertFalse(stack.notEmpty());
    }

    @Test
    public void notEmpty()
    {
        StackIterable<Integer> stack = this.newStackWith(1);
        Assert.assertTrue(stack.notEmpty());
        Assert.assertFalse(stack.isEmpty());
    }

    @Test
    public void getFirst()
    {
        StackIterable<Integer> stack = this.newStackWith(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(3), stack.getFirst());
        Assert.assertEquals(stack.peek(), stack.getFirst());
        Verify.assertThrows(EmptyStackException.class, new Runnable()
        {
            public void run()
            {
                StackIterableTestCase.this.newStackWith().getFirst();
            }
        });
        StackIterable<Integer> stack2 = this.newStackFromTopToBottom(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(1), stack2.getFirst());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getLast()
    {
        StackIterable<Integer> stack = this.newStackWith(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(1), stack.getLast());
    }

    @Test
    public void contains()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3);
        Assert.assertTrue(stack.contains(2));
        Assert.assertTrue(stack.contains(3));
        Assert.assertFalse(stack.contains(4));
    }

    @Test
    public void containsAllIterable()
    {
        StackIterable<Integer> stack = this.newStackWith(1, 2, 3);
        Assert.assertTrue(stack.containsAllIterable(Interval.fromTo(2, 3)));
        Assert.assertFalse(stack.containsAllIterable(Interval.fromTo(2, 4)));
    }

    @Test
    public void containsAll()
    {
        StackIterable<Integer> stack = this.newStackWith(1, 2, 3, 4);
        Assert.assertTrue(stack.containsAll(Interval.oneTo(2)));
        Assert.assertFalse(stack.containsAll(FastList.newListWith(1, 2, 5)));
    }

    @Test
    public void containsAllArguments()
    {
        StackIterable<Integer> stack = this.newStackWith(1, 2, 3, 4);
        Assert.assertTrue(stack.containsAllArguments(2, 1, 3));
        Assert.assertFalse(stack.containsAllArguments(2, 1, 3, 5));
    }

    @Test
    public void collect()
    {
        StackIterable<Boolean> stack = this.newStackFromTopToBottom(Boolean.TRUE, Boolean.FALSE, null);
        CountingFunction<Object, String> function = CountingFunction.of(Functions.getToString());
        Assert.assertEquals(
                this.newStackFromTopToBottom("true", "false", "null"),
                stack.collect(function));
        Assert.assertEquals(3, function.count);

        Assert.assertEquals(
                FastList.newListWith("true", "false", "null"),
                stack.collect(Functions.getToString(), FastList.<String>newList()));
    }

    @Test
    public void collectIf()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3, 4, 5);

        CountingPredicate<Integer> predicate1 = CountingPredicate.of(Predicates.lessThan(3));
        CountingFunction<Object, String> function1 = CountingFunction.of(Functions.getToString());
        Assert.assertEquals(
                this.newStackFromTopToBottom("1", "2"),
                stack.collectIf(predicate1, function1));
        Assert.assertEquals(5, predicate1.count);
        Assert.assertEquals(2, function1.count);

        CountingPredicate<Integer> predicate2 = CountingPredicate.of(Predicates.lessThan(3));
        CountingFunction<Object, String> function2 = CountingFunction.of(Functions.getToString());
        Assert.assertEquals(
                FastList.newListWith("1", "2"),
                stack.collectIf(predicate2, function2, FastList.<String>newList()));
        Assert.assertEquals(5, predicate2.count);
        Assert.assertEquals(2, function2.count);
    }

    @Test
    public void collectWith()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(3, 2, 1);
        Assert.assertEquals(
                FastList.newListWith(4, 3, 2),
                stack.collectWith(AddFunction.INTEGER, 1, FastList.<Integer>newList()));
    }

    @Test
    public void flatCollect()
    {
        StackIterable<String> stack = this.newStackFromTopToBottom("1", "One", "2", "Two");

        CountingFunction<String, Iterable<Character>> function = CountingFunction.of(new Function<String, Iterable<Character>>()
        {
            public Iterable<Character> valueOf(String object)
            {
                MutableList<Character> result = Lists.mutable.of();
                char[] chars = object.toCharArray();
                for (char aChar : chars)
                {
                    result.add(Character.valueOf(aChar));
                }
                return result;
            }
        });

        Assert.assertEquals(
                this.newStackFromTopToBottom('1', 'O', 'n', 'e', '2', 'T', 'w', 'o'),
                stack.flatCollect(function));
        Assert.assertEquals(4, function.count);

        Assert.assertEquals(
                FastList.newListWith('1', 'O', 'n', 'e', '2', 'T', 'w', 'o'),
                stack.flatCollect(function, FastList.<Character>newList()));
    }

    @Test
    public void select()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3);
        CountingPredicate<Object> predicate = new CountingPredicate<Object>(Predicates.equal(1));
        StackIterable<Integer> actual = stack.select(predicate);
        Assert.assertEquals(this.newStackFromTopToBottom(1), actual);
        Assert.assertEquals(3, predicate.count);
        Assert.assertEquals(
                this.newStackFromTopToBottom(2, 3),
                stack.select(Predicates.greaterThan(1)));
        Assert.assertEquals(
                FastList.newListWith(2, 3),
                stack.select(Predicates.greaterThan(1), FastList.<Integer>newList()));
    }

    @Test
    public void selectInstancesOf()
    {
        StackIterable<Number> numbers = this.<Number>newStackFromTopToBottom(1, 2.0, 3, 4.0, 5);
        Assert.assertEquals(this.<Integer>newStackFromTopToBottom(1, 3, 5), numbers.selectInstancesOf(Integer.class));
        Assert.assertEquals(this.<Number>newStackFromTopToBottom(1, 2.0, 3, 4.0, 5), numbers.selectInstancesOf(Number.class));
    }

    @Test
    public void selectWith()
    {
        Assert.assertEquals(
                UnifiedSet.newSetWith(2, 1),
                this.newStackFromTopToBottom(5, 4, 3, 2, 1).selectWith(Predicates2.<Integer>lessThan(), 3, UnifiedSet.<Integer>newSet()));
    }

    @Test
    public void reject()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(3, 2, 1);
        CountingPredicate<Integer> predicate = new CountingPredicate<Integer>(Predicates.greaterThan(2));
        Assert.assertEquals(
                this.newStackFromTopToBottom(2, 1),
                stack.reject(predicate));
        Assert.assertEquals(3, predicate.count);
        Assert.assertEquals(
                FastList.newListWith(2, 1),
                stack.reject(Predicates.greaterThan(2), FastList.<Integer>newList()));
    }

    @Test
    public void rejectWith()
    {
        Assert.assertEquals(
                UnifiedSet.newSetWith(5, 4, 3),
                this.newStackFromTopToBottom(5, 4, 3, 2, 1).rejectWith(Predicates2.<Integer>lessThan(), 3, UnifiedSet.<Integer>newSet()));
    }

    @Test
    public void detect()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3);
        CountingPredicate<Integer> predicate = new CountingPredicate<Integer>(Predicates.lessThan(3));
        Assert.assertEquals(Integer.valueOf(1), stack.detect(predicate));
        Assert.assertEquals(1, predicate.count);
        Assert.assertNull(stack.detect(Predicates.equal(4)));
    }

    @Test
    public void detectIfNone()
    {
        Function0<Integer> defaultResultFunction = new PassThruFunction0<Integer>(-1);
        CountingPredicate<Integer> predicate = new CountingPredicate<Integer>(Predicates.lessThan(3));
        Assert.assertEquals(
                Integer.valueOf(1),
                this.newStackFromTopToBottom(1, 2, 3, 4, 5).detectIfNone(predicate, defaultResultFunction));
        Assert.assertEquals(1, predicate.count);
        Assert.assertEquals(
                Integer.valueOf(-1),
                this.newStackWith(1, 2, 3, 4, 5).detectIfNone(Predicates.lessThan(-1), defaultResultFunction));
    }

    @Test
    public void partition()
    {
        CountingPredicate<Integer> predicate = new CountingPredicate<Integer>(Predicates.lessThan(3));
        PartitionStack<Integer> partition = this.newStackFromTopToBottom(1, 2, 3, 4, 5).partition(predicate);
        Assert.assertEquals(5, predicate.count);
        Assert.assertEquals(this.newStackFromTopToBottom(1, 2), partition.getSelected());
        Assert.assertEquals(this.newStackFromTopToBottom(3, 4, 5), partition.getRejected());
    }

    @Test
    public void zip()
    {
        StackIterable<String> stack = this.newStackFromTopToBottom("7", "6", "5", "4", "3", "2", "1");
        List<Integer> interval = Interval.oneTo(7);

        StackIterable<Pair<String, Integer>> expected = this.newStackFromTopToBottom(
                Tuples.pair("7", 1),
                Tuples.pair("6", 2),
                Tuples.pair("5", 3),
                Tuples.pair("4", 4),
                Tuples.pair("3", 5),
                Tuples.pair("2", 6),
                Tuples.pair("1", 7)
        );

        Assert.assertEquals(expected, stack.zip(interval));

        Assert.assertEquals(
                expected.toSet(),
                stack.zip(interval, UnifiedSet.<Pair<String, Integer>>newSet()));
    }

    @Test
    public void zipWithIndex()
    {
        StackIterable<String> stack = this.newStackFromTopToBottom("4", "3", "2", "1");

        StackIterable<Pair<String, Integer>> expected = this.newStackFromTopToBottom(
                Tuples.pair("4", 0),
                Tuples.pair("3", 1),
                Tuples.pair("2", 2),
                Tuples.pair("1", 3));
        Assert.assertEquals(expected, stack.zipWithIndex());
        Assert.assertEquals(expected.toSet(), stack.zipWithIndex(UnifiedSet.<Pair<String, Integer>>newSet()));
    }

    @Test
    public void count()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3, 4, 5);
        CountingPredicate<Integer> predicate = new CountingPredicate<Integer>(Predicates.greaterThan(2));
        Assert.assertEquals(3, stack.count(predicate));
        Assert.assertEquals(5, predicate.count);
        Assert.assertEquals(0, stack.count(Predicates.greaterThan(6)));
    }

    @Test
    public void anySatisfy()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3);
        CountingPredicate<Object> predicate = new CountingPredicate<Object>(Predicates.equal(1));
        Assert.assertTrue(stack.anySatisfy(predicate));
        Assert.assertEquals(1, predicate.count);
        Assert.assertFalse(stack.anySatisfy(Predicates.equal(4)));
    }

    @Test
    public void allSatisfy()
    {
        StackIterable<Integer> stack = this.newStackWith(3, 3, 3);
        CountingPredicate<Object> predicate = new CountingPredicate<Object>(Predicates.equal(3));
        Assert.assertTrue(stack.allSatisfy(predicate));
        Assert.assertEquals(3, predicate.count);
        Assert.assertFalse(stack.allSatisfy(Predicates.equal(2)));
    }

    @Test
    public void injectInto()
    {
        Assert.assertEquals(
                Integer.valueOf(10),
                this.newStackWith(1, 2, 3, 4).injectInto(Integer.valueOf(0), AddFunction.INTEGER));
        Assert.assertEquals(
                10,
                this.newStackWith(1, 2, 3, 4).injectInto(0, AddFunction.INTEGER_TO_INT));
        Assert.assertEquals(
                7.0,
                this.newStackWith(1.0, 2.0, 3.0).injectInto(1.0d, AddFunction.DOUBLE_TO_DOUBLE), 0.001);
        Assert.assertEquals(
                7,
                this.newStackWith(1, 2, 3).injectInto(1L, AddFunction.INTEGER_TO_LONG));
        Assert.assertEquals(
                7.0,
                this.newStackWith(1, 2, 3).injectInto(1.0f, AddFunction.INTEGER_TO_FLOAT), 0.001);
    }

    @Test
    public void sumOf()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3, 4);
        Assert.assertEquals(10, stack.sumOfInt(new IntFunction<Integer>()
        {
            public int intValueOf(Integer integer)
            {
                return integer;
            }
        }));

        Assert.assertEquals(10, stack.sumOfLong(new LongFunction<Integer>()
        {
            public long longValueOf(Integer integer)
            {
                return integer.longValue();
            }
        }));

        Assert.assertEquals(10.0d, stack.sumOfDouble(new DoubleFunction<Integer>()
        {
            public double doubleValueOf(Integer integer)
            {
                return integer.doubleValue();
            }
        }), 0.001);

        Assert.assertEquals(10.0f, stack.sumOfFloat(new FloatFunction<Integer>()
        {
            public float floatValueOf(Integer integer)
            {
                return integer.floatValue();
            }
        }), 0.001);
    }

    @Test
    public void max()
    {
        Assert.assertEquals(
                Integer.valueOf(4),
                this.newStackFromTopToBottom(4, 3, 2, 1).max());
        Assert.assertEquals(
                Integer.valueOf(1),
                this.newStackFromTopToBottom(4, 3, 2, 1).max(Comparators.<Integer>reverseNaturalOrder()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(
                Integer.valueOf(3),
                this.newStackWith(1, 2, 3).maxBy(Functions.getToString()));
    }

    @Test
    public void min()
    {
        Assert.assertEquals(
                Integer.valueOf(1),
                this.newStackWith(1, 2, 3, 4).min());
        Assert.assertEquals(
                Integer.valueOf(4),
                this.newStackWith(1, 2, 3, 4).min(Comparators.<Integer>reverseNaturalOrder()));
    }

    @Test
    public void minBy()
    {
        CountingFunction<Object, String> function = CountingFunction.of(Functions.getToString());
        Assert.assertEquals(
                Integer.valueOf(1),
                this.newStackWith(1, 2, 3).minBy(function));
        Assert.assertEquals(4, function.count);
    }

    @Test
    public void testToString()
    {
        StackIterable<Integer> stack = this.newStackFromTopToBottom(4, 3, 2, 1);
        Assert.assertEquals("[4, 3, 2, 1]", stack.toString());
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("3, 2, 1", this.newStackFromTopToBottom(3, 2, 1).makeString());
        Assert.assertEquals("3~2~1", this.newStackFromTopToBottom(3, 2, 1).makeString("~"));
        Assert.assertEquals("[3/2/1]", this.newStackFromTopToBottom(3, 2, 1).makeString("[", "/", "]"));
    }

    @Test
    public void appendString()
    {
        StackIterable<String> stack = this.newStackFromTopToBottom("3", "2", "1");
        Appendable appendable = new StringBuilder();

        stack.appendString(appendable);
        Assert.assertEquals("3, 2, 1", appendable.toString());

        Appendable appendable2 = new StringBuilder();
        stack.appendString(appendable2, "/");
        Assert.assertEquals("3/2/1", appendable2.toString());

        Appendable appendable3 = new StringBuilder();
        stack.appendString(appendable3, "[", "/", "]");
        Assert.assertEquals("[3/2/1]", appendable3.toString());
    }

    @Test
    public void groupBy()
    {
        StackIterable<String> stack = this.newStackWith("1", "2", "3");
        ListMultimap<Boolean, String> expected = FastListMultimap.newMultimap(
                Tuples.pair(Boolean.TRUE, "3"),
                Tuples.pair(Boolean.FALSE, "2"),
                Tuples.pair(Boolean.TRUE, "1"));
        Assert.assertEquals(expected, stack.groupBy(new Function<String, Boolean>()
        {
            public Boolean valueOf(String object)
            {
                return IntegerPredicates.isOdd().accept(Integer.parseInt(object));
            }
        }));

        Assert.assertEquals(expected, stack.groupBy(new Function<String, Boolean>()
        {
            public Boolean valueOf(String object)
            {
                return IntegerPredicates.isOdd().accept(Integer.parseInt(object));
            }
        }, FastListMultimap.<Boolean, String>newMultimap()));
    }

    @Test
    public void groupByEach()
    {
        final StackIterable<Integer> stack = this.newStackFromTopToBottom(1, 2, 3);

        final MutableMultimap<Integer, Integer> expected = FastListMultimap.newMultimap();
        stack.forEach(new Procedure<Integer>()
        {
            public void value(Integer value)
            {
                expected.putAll(-value, Interval.fromTo(value, stack.size()));
            }
        });

        Multimap<Integer, Integer> actual =
                stack.groupByEach(new NegativeIntervalFunction());
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                stack.groupByEach(new NegativeIntervalFunction(), FastListMultimap.<Integer, Integer>newMultimap());
        Assert.assertEquals(expected, actualWithTarget);
    }

    @Test
    public void chunk()
    {
        Verify.assertIterablesEqual(
                FastList.<RichIterable<String>>newListWith(
                        FastList.newListWith("7", "6"),
                        FastList.newListWith("5", "4"),
                        FastList.newListWith("3", "2"),
                        FastList.newListWith("1")),
                this.newStackFromTopToBottom("7", "6", "5", "4", "3", "2", "1").chunk(2));
    }

    @Test
    public void forEach()
    {
        StackIterable<String> stack = this.newStackWith("1", "2", "3", "4", "5");
        Appendable builder = new StringBuilder();
        Procedure<String> appendProcedure = Procedures.append(builder);
        stack.forEach(appendProcedure);
        Assert.assertEquals("54321", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        StackIterable<String> stack = this.newStackWith("1", "2", "3", "4");
        final StringBuilder builder = new StringBuilder();
        stack.forEachWith(new Procedure2<String, Integer>()
        {
            public void value(String argument1, Integer argument2)
            {
                builder.append(argument1).append(argument2);
            }
        }, 0);
        Assert.assertEquals("40302010", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        StackIterable<String> stack = this.newStackFromTopToBottom("5", "4", "3", "2", "1");
        final StringBuilder builder = new StringBuilder();
        stack.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String each, int index)
            {
                builder.append(each).append(index);
            }
        });
        Assert.assertEquals("5041322314", builder.toString());
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(
                FastList.newListWith(4, 3, 2, 1),
                this.newStackFromTopToBottom(4, 3, 2, 1).toList());
    }

    @Test
    public void toStack()
    {
        Assert.assertEquals(this.newStackFromTopToBottom(3, 2, 1), this.newStackFromTopToBottom(3, 2, 1).toStack());
    }

    @Test
    public void toSortedList()
    {
        Assert.assertEquals(
                Interval.oneTo(4),
                this.newStackFromTopToBottom(4, 3, 1, 2).toSortedList());
        Assert.assertEquals(
                Interval.fromTo(4, 1),
                this.newStackFromTopToBottom(4, 3, 1, 2).toSortedList(Collections.<Integer>reverseOrder()));
    }

    @Test
    public void toSortedListBy()
    {
        MutableList<Integer> list = FastList.newList(Interval.oneTo(10));
        Collections.shuffle(list);
        Assert.assertEquals(
                Interval.oneTo(10),
                this.newStack(list).toSortedListBy(Functions.getIntegerPassThru()));
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(UnifiedSet.newSetWith(4, 3, 2, 1),
                this.newStackWith(1, 2, 3, 4).toSet());
    }

    @Test
    public void toSortedSet()
    {
        SetIterable<Integer> expected = TreeSortedSet.newSetWith(1, 2, 4, 5);
        StackIterable<Integer> stack = this.newStackWith(2, 1, 5, 4);

        Assert.assertEquals(expected, stack.toSortedSet());
        Assert.assertEquals(FastList.newListWith(1, 2, 4, 5), stack.toSortedSet().toList());

        Assert.assertEquals(expected, stack.toSortedSet(Comparators.reverseNaturalOrder()));
        Assert.assertEquals(
                FastList.newListWith(5, 4, 2, 1),
                stack.toSortedSet(Comparators.reverseNaturalOrder()).toList());
    }

    @Test
    public void toSortedSetBy()
    {
        SetIterable<Integer> expected = UnifiedSet.newSetWith(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);

        StackIterable<Integer> stack = this.newStackWith(5, 2, 4, 3, 1, 6, 7, 8, 9, 10);
        Assert.assertEquals(expected,
                stack.toSortedSetBy(Functions.getToString()));
        Assert.assertEquals(
                FastList.newListWith(1, 10, 2, 3, 4, 5, 6, 7, 8, 9),
                stack.toSortedSetBy(Functions.getToString()).toList());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(Bags.mutable.of("C", "B", "A"),
                this.newStackFromTopToBottom("C", "B", "A").toBag());
    }

    @Test
    public void toMap()
    {
        Assert.assertEquals(UnifiedMap.<String, String>newWithKeysValues("4", "4", "3", "3", "2", "2", "1", "1"),
                this.newStackFromTopToBottom(4, 3, 2, 1).toMap(Functions.getToString(), Functions.getToString()));
    }

    @Test
    public void toSortedMap()
    {
        Assert.assertEquals(UnifiedMap.newWithKeysValues(3, "3", 2, "2", 1, "1"),
                this.newStackFromTopToBottom(3, 2, 1).toSortedMap(Functions.getIntegerPassThru(), Functions.getToString()));

        Assert.assertEquals(TreeSortedMap.newMapWith(Comparators.<Integer>reverseNaturalOrder(), 3, "3", 2, "2", 1, "1"),
                this.newStackFromTopToBottom(3, 2, 1).toSortedMap(Comparators.<Integer>reverseNaturalOrder(),
                        Functions.getIntegerPassThru(), Functions.getToString()));
    }

    @Test
    public void asLazy()
    {
        Assert.assertEquals(FastList.newListWith("3", "2", "1"),
                this.newStackFromTopToBottom("3", "2", "1").asLazy().toList());
    }

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(new Object[]{4, 3, 2, 1}, this.newStackFromTopToBottom(4, 3, 2, 1).toArray());
        Assert.assertArrayEquals(new Integer[]{4, 3, 2, 1}, this.newStackFromTopToBottom(4, 3, 2, 1).toArray(new Integer[0]));
    }

    @Test
    public void iterator()
    {
        StringBuilder builder = new StringBuilder();
        StackIterable<String> stack = this.newStackFromTopToBottom("5", "4", "3", "2", "1");
        for (String string : stack)
        {
            builder.append(string);
        }
        Assert.assertEquals("54321", builder.toString());
    }

    @Test
    public void testEquals()
    {
        StackIterable<Integer> stack1 = this.newStackFromTopToBottom(1, 2, 3, 4);
        StackIterable<Integer> stack2 = this.newStackFromTopToBottom(1, 2, 3, 4);
        StackIterable<Integer> stack3 = this.newStackFromTopToBottom(5, 2, 1, 4);
        StackIterable<Integer> stack4 = this.newStackFromTopToBottom(1, 2, 3);
        StackIterable<Integer> stack5 = this.newStackFromTopToBottom(1, 2, 3, 4, 5);
        StackIterable<Integer> stack6 = this.newStackFromTopToBottom(1, 2, 3, null);

        Verify.assertEqualsAndHashCode(stack1, stack2);
        Verify.assertPostSerializedEqualsAndHashCode(this.newStackWith(1, 2, 3, 4));
        Verify.assertNotEquals(stack1, stack3);
        Verify.assertNotEquals(stack1, stack4);
        Verify.assertNotEquals(stack1, stack5);
        Verify.assertNotEquals(stack1, stack6);

        Verify.assertPostSerializedEqualsAndHashCode(this.newStackWith(null, null, null));

        Assert.assertEquals(Stacks.mutable.of(), this.newStackWith());
    }

    @Test
    public void testHashCode()
    {
        StackIterable<Integer> stack1 = this.newStackWith(1, 2, 3, 5);
        StackIterable<Integer> stack2 = this.newStackWith(1, 2, 3, 4);
        Verify.assertNotEquals(stack1.hashCode(), stack2.hashCode());

        Assert.assertEquals(31 * 31 * 31 * 31 + 1 * 31 * 31 * 31 + 2 * 31 * 31 + 3 * 31 + 4,
                this.newStackFromTopToBottom(1, 2, 3, 4).hashCode());
        Assert.assertEquals(31 * 31 * 31, this.newStackFromTopToBottom(null, null, null).hashCode());

        Verify.assertNotEquals(
                this.newStackFromTopToBottom(1, 2, 3, 4).hashCode(),
                this.newStackFromTopToBottom(4, 3, 2, 1).hashCode());
    }

    @Test
    public void aggregateByMutating()
    {
        Function0<AtomicInteger> valueCreator = new Function0<AtomicInteger>()
        {
            public AtomicInteger value()
            {
                return new AtomicInteger(0);
            }
        };
        Procedure2<AtomicInteger, Integer> sumAggregator = new Procedure2<AtomicInteger, Integer>()
        {
            public void value(AtomicInteger aggregate, Integer value)
            {
                aggregate.addAndGet(value);
            }
        };
        StackIterable<Integer> collection = this.newStackWith(1, 1, 1, 2, 2, 3);
        MapIterable<String, AtomicInteger> aggregation = collection.aggregateBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(3, aggregation.get("1").intValue());
        Assert.assertEquals(4, aggregation.get("2").intValue());
        Assert.assertEquals(3, aggregation.get("3").intValue());
    }

    @Test
    public void aggregateByNonMutating()
    {
        Function0<Integer> valueCreator = new Function0<Integer>()
        {
            public Integer value()
            {
                return Integer.valueOf(0);
            }
        };
        Function2<Integer, Integer, Integer> sumAggregator = new Function2<Integer, Integer, Integer>()
        {
            public Integer value(Integer aggregate, Integer value)
            {
                return aggregate + value;
            }
        };
        StackIterable<Integer> collection = this.newStackWith(1, 1, 1, 2, 2, 3);
        MapIterable<String, Integer> aggregation = collection.aggregateBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(3, aggregation.get("1").intValue());
        Assert.assertEquals(4, aggregation.get("2").intValue());
        Assert.assertEquals(3, aggregation.get("3").intValue());
    }

    private static final class CountingPredicate<T>
            implements Predicate<T>
    {
        private static final long serialVersionUID = 1L;
        private final Predicate<T> predicate;
        private int count;

        private CountingPredicate(Predicate<T> predicate)
        {
            this.predicate = predicate;
        }

        private static <T> CountingPredicate<T> of(Predicate<T> predicate)
        {
            return new CountingPredicate<T>(predicate);
        }

        public boolean accept(T anObject)
        {
            this.count++;
            return this.predicate.accept(anObject);
        }
    }

    private static final class CountingFunction<T, V>
            implements Function<T, V>
    {
        private static final long serialVersionUID = 1L;
        private int count;
        private final Function<T, V> function;

        private CountingFunction(Function<T, V> function)
        {
            this.function = function;
        }

        private static <T, V> CountingFunction<T, V> of(Function<T, V> function)
        {
            return new CountingFunction<T, V>(function);
        }

        public V valueOf(T object)
        {
            this.count++;
            return this.function.valueOf(object);
        }
    }
}
