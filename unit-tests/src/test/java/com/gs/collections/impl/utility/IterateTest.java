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

package com.gs.collections.impl.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.StringFunctions;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.MaxSizeFunction;
import com.gs.collections.impl.block.function.MinSizeFunction;
import com.gs.collections.impl.block.predicate.PairPredicate;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public class IterateTest
{
    private static final Function<Integer, Integer> TIMES_10_FUNCTION = new Times10Function();

    private MutableList<Iterable<Integer>> iterables;

    @Before
    public void setUp()
    {
        this.iterables = Lists.mutable.of();
        this.iterables.add(Interval.oneTo(5).toList());
        this.iterables.add(Interval.oneTo(5).toSet());
        this.iterables.add(Interval.oneTo(5).addAllTo(new ArrayList<Integer>(5)));
        this.iterables.add(Collections.unmodifiableList(new ArrayList<Integer>(Interval.oneTo(5))));
        this.iterables.add(Collections.unmodifiableCollection(new ArrayList<Integer>(Interval.oneTo(5))));
        this.iterables.add(Interval.oneTo(5));
        this.iterables.add(Interval.oneTo(5).asLazy());
        this.iterables.add(new IterableAdapter<Integer>(Interval.oneTo(5)));
    }

    @Test
    public void addAllTo()
    {
        Verify.assertContainsAll(
                Iterate.addAllTo(FastList.<Integer>newListWith(1, 2, 3), FastList.<Integer>newList()), 1, 2, 3);
    }

    @Test
    public void sizeOf()
    {
        Assert.assertEquals(5, Iterate.sizeOf(Interval.oneTo(5)));
        Assert.assertEquals(5, Iterate.sizeOf(Interval.oneTo(5).toList()));
        Assert.assertEquals(5, Iterate.sizeOf(Interval.oneTo(5).asLazy()));
        Assert.assertEquals(3, Iterate.sizeOf(UnifiedMap.newWithKeysValues(1, 1, 2, 2, 3, 3)));
        Assert.assertEquals(5, Iterate.sizeOf(new IterableAdapter<Integer>(Interval.oneTo(5))));
    }

    @Test
    public void toArray()
    {
        Object[] expected = {1, 2, 3, 4, 5};
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5)));
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5).toList()));
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5).asLazy()));
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5).toSortedMap(Functions.<Object>getPassThru(), Functions.<Object>getPassThru())));
        Assert.assertArrayEquals(expected, Iterate.toArray(new IterableAdapter<Integer>(Interval.oneTo(5))));
    }

    @Test(expected = NullPointerException.class)
    public void toArray_NullParameter()
    {
        Iterate.toArray(null);
    }

    @Test
    public void toArray_with_array()
    {
        Object[] expected = {1, 2, 3, 4, 5};
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5), new Object[5]));
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5).toList(), new Object[5]));
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5).asLazy(), new Object[5]));
        Assert.assertArrayEquals(expected, Iterate.toArray(Interval.oneTo(5).toSortedMap(Functions.<Object>getPassThru(), Functions.<Object>getPassThru()), new Object[5]));
        Assert.assertArrayEquals(expected, Iterate.toArray(new IterableAdapter<Integer>(Interval.oneTo(5)), new Object[5]));
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7}, Iterate.toArray(new IterableAdapter<Integer>(Interval.oneTo(7)), new Object[5]));
    }

    @Test
    public void fromToDoit()
    {
        MutableList<Integer> list = Lists.mutable.of();
        Interval.fromTo(6, 10).forEach(CollectionAddProcedure.<Integer>on(list));
        Verify.assertContainsAll(list, 6, 10);
    }

    @Test
    public void injectInto()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertEquals(Integer.valueOf(15), Iterate.injectInto(0, each, AddFunction.INTEGER));
            }
        });
    }

    @Test
    public void injectIntoInt()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertEquals(15, Iterate.injectInto(0, each, AddFunction.INTEGER_TO_INT));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.injectInto(0, null, AddFunction.INTEGER_TO_INT);
            }
        });
    }

    @Test
    public void injectIntoLong()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertEquals(15L, Iterate.injectInto(0L, each, AddFunction.INTEGER_TO_LONG));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.injectInto(0L, null, AddFunction.INTEGER_TO_LONG);
            }
        });
    }

    @Test
    public void injectIntoDouble()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertEquals(15.0d, Iterate.injectInto(0.0d, each, AddFunction.INTEGER_TO_DOUBLE), 0.001);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.injectInto(0.0d, null, AddFunction.INTEGER_TO_DOUBLE);
            }
        });
    }

    @Test
    public void injectIntoFloat()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertEquals(15.0d, Iterate.injectInto(0.0f, each, AddFunction.INTEGER_TO_FLOAT), 0.001);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.injectInto(0.0f, null, AddFunction.INTEGER_TO_FLOAT);
            }
        });
    }

    @Test
    public void injectInto2()
    {
        Assert.assertEquals(
                new Double(7),
                Iterate.injectInto(1.0, iList(1.0, 2.0, 3.0), AddFunction.DOUBLE));
    }

    @Test
    public void injectIntoString()
    {
        Assert.assertEquals("0123", Iterate.injectInto("0", iList("1", "2", "3"), AddFunction.STRING));
    }

    @Test
    public void injectIntoMaxString()
    {
        Assert.assertEquals(
                Integer.valueOf(3),
                Iterate.injectInto(Integer.MIN_VALUE, iList("1", "12", "123"), MaxSizeFunction.STRING));
    }

    @Test
    public void injectIntoMinString()
    {
        Assert.assertEquals(
                Integer.valueOf(1),
                Iterate.injectInto(Integer.MAX_VALUE, iList("1", "12", "123"), MinSizeFunction.STRING));
    }

    @Test
    public void flatCollectFromAttributes()
    {
        MutableList<ListContainer<String>> list = mList(
                new ListContainer<String>(Lists.mutable.of("One", "Two")),
                new ListContainer<String>(Lists.mutable.of("Two-and-a-half", "Three", "Four")),
                new ListContainer<String>(Lists.mutable.<String>of()),
                new ListContainer<String>(Lists.mutable.of("Five")));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(list, ListContainer.<String>getListFunction()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(Collections.synchronizedList(list), ListContainer.<String>getListFunction()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(Collections.synchronizedCollection(list), ListContainer.<String>getListFunction()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(LazyIterate.adapt(list), ListContainer.<String>getListFunction()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(new ArrayList<ListContainer<String>>(list), ListContainer.<String>getListFunction()));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.flatCollect(null, null);
            }
        });
    }

    @Test
    public void flatCollectFromAttributesWithTarget()
    {
        MutableList<ListContainer<String>> list = Lists.fixedSize.of(
                new ListContainer<String>(Lists.mutable.of("One", "Two")),
                new ListContainer<String>(Lists.mutable.of("Two-and-a-half", "Three", "Four")),
                new ListContainer<String>(Lists.mutable.<String>of()),
                new ListContainer<String>(Lists.mutable.of("Five")));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(list, ListContainer.<String>getListFunction(), FastList.<String>newList()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(Collections.synchronizedList(list), ListContainer.<String>getListFunction(), FastList.<String>newList()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(Collections.synchronizedCollection(list), ListContainer.<String>getListFunction(), FastList.<String>newList()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(LazyIterate.adapt(list), ListContainer.<String>getListFunction(), FastList.<String>newList()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(new ArrayList<ListContainer<String>>(list), ListContainer.<String>getListFunction(), FastList.<String>newList()));
        Assert.assertEquals(
                iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"),
                Iterate.flatCollect(new IterableAdapter<ListContainer<String>>(new ArrayList<ListContainer<String>>(list)), ListContainer.<String>getListFunction(), FastList.<String>newList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void flatCollectFromAttributesWithTarget_NullParameter()
    {
        Iterate.flatCollect(null, null, UnifiedSet.newSet());
    }

    @Test
    public void flatCollectFromAttributesUsingStringFunction()
    {
        MutableList<ListContainer<String>> list = Lists.mutable.of(
                new ListContainer<String>(Lists.mutable.of("One", "Two")),
                new ListContainer<String>(Lists.mutable.of("Two-and-a-half", "Three", "Four")),
                new ListContainer<String>(Lists.mutable.<String>of()),
                new ListContainer<String>(Lists.mutable.of("Five")));
        Function<ListContainer<String>, List<String>> function = new Function<ListContainer<String>, List<String>>()
        {
            public List<String> valueOf(ListContainer<String> container)
            {
                return container.getList();
            }
        };
        Collection<String> result = Iterate.flatCollect(list, function);
        FastList<String> result2 = Iterate.flatCollect(list, function, FastList.<String>newList());
        Assert.assertEquals(iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"), result);
        Assert.assertEquals(iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"), result2);
    }

    @Test
    public void flatCollectOneLevel()
    {
        MutableList<MutableList<String>> list = Lists.mutable.of(
                Lists.mutable.of("One", "Two"),
                Lists.mutable.of("Two-and-a-half", "Three", "Four"),
                Lists.mutable.<String>of(),
                Lists.mutable.of("Five"));
        Collection<String> result = Iterate.flatten(list);
        Assert.assertEquals(iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"), result);
    }

    @Test
    public void flatCollectOneLevel_target()
    {
        MutableList<MutableList<String>> list = Lists.mutable.of(
                Lists.mutable.of("One", "Two"),
                Lists.mutable.of("Two-and-a-half", "Three", "Four"),
                Lists.mutable.<String>of(),
                Lists.mutable.of("Five"));
        MutableList<String> target = Lists.mutable.of();
        Collection<String> result = Iterate.flatten(list, target);
        Assert.assertSame(result, target);
        Assert.assertEquals(iList("One", "Two", "Two-and-a-half", "Three", "Four", "Five"), result);
    }

    @Test
    public void groupBy()
    {
        FastList<String> source = FastList.newListWith("Ted", "Sally", "Mary", "Bob", "Sara");
        Multimap<Character, String> result1 = Iterate.groupBy(source, StringFunctions.firstLetter());
        Multimap<Character, String> result2 = Iterate.groupBy(Collections.synchronizedList(source), StringFunctions.firstLetter());
        Multimap<Character, String> result3 = Iterate.groupBy(Collections.synchronizedCollection(source), StringFunctions.firstLetter());
        Multimap<Character, String> result4 = Iterate.groupBy(LazyIterate.adapt(source), StringFunctions.firstLetter());
        Multimap<Character, String> result5 = Iterate.groupBy(new ArrayList<String>(source), StringFunctions.firstLetter());
        MutableMultimap<Character, String> expected = FastListMultimap.newMultimap();
        expected.put('T', "Ted");
        expected.put('S', "Sally");
        expected.put('M', "Mary");
        expected.put('B', "Bob");
        expected.put('S', "Sara");
        Assert.assertEquals(expected, result1);
        Assert.assertEquals(expected, result2);
        Assert.assertEquals(expected, result3);
        Assert.assertEquals(expected, result4);
        Assert.assertEquals(expected, result5);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.groupBy(null, null);
            }
        });
    }

    @Test
    public void groupByEach()
    {
        MutableList<String> source = FastList.newListWith("Ted", "Sally", "Sally", "Mary", "Bob", "Sara");
        Function<String, Set<Character>> uppercaseSetFunction = new WordToItsLetters();
        Multimap<Character, String> result1 = Iterate.groupByEach(source, uppercaseSetFunction);
        Multimap<Character, String> result2 = Iterate.groupByEach(Collections.synchronizedList(source), uppercaseSetFunction);
        Multimap<Character, String> result3 = Iterate.groupByEach(Collections.synchronizedCollection(source), uppercaseSetFunction);
        Multimap<Character, String> result4 = Iterate.groupByEach(LazyIterate.adapt(source), uppercaseSetFunction);
        Multimap<Character, String> result5 = Iterate.groupByEach(new ArrayList<String>(source), uppercaseSetFunction);
        MutableMultimap<Character, String> expected = FastListMultimap.newMultimap();
        expected.put('T', "Ted");
        expected.putAll('E', FastList.newListWith("Ted"));
        expected.put('D', "Ted");
        expected.putAll('S', FastList.newListWith("Sally", "Sally", "Sara"));
        expected.putAll('A', FastList.newListWith("Sally", "Sally", "Mary", "Sara"));
        expected.putAll('L', FastList.newListWith("Sally", "Sally"));
        expected.putAll('Y', FastList.newListWith("Sally", "Sally", "Mary"));
        expected.put('M', "Mary");
        expected.putAll('R', FastList.newListWith("Mary", "Sara"));
        expected.put('B', "Bob");
        expected.put('O', "Bob");
        Assert.assertEquals(expected, result1);
        Assert.assertEquals(expected, result2);
        Assert.assertEquals(expected, result3);
        Assert.assertEquals(expected, result4);
        Assert.assertEquals(expected, result5);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.groupByEach(null, null);
            }
        });
    }

    @Test
    public void groupByWithTarget()
    {
        FastList<String> source = FastList.newListWith("Ted", "Sally", "Mary", "Bob", "Sara");
        Multimap<Character, String> result1 = Iterate.groupBy(source, StringFunctions.firstLetter(), FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result2 = Iterate.groupBy(Collections.synchronizedList(source), StringFunctions.firstLetter(), FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result3 = Iterate.groupBy(Collections.synchronizedCollection(source), StringFunctions.firstLetter(), FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result4 = Iterate.groupBy(LazyIterate.adapt(source), StringFunctions.firstLetter(), FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result5 = Iterate.groupBy(new ArrayList<String>(source), StringFunctions.firstLetter(), FastListMultimap.<Character, String>newMultimap());
        MutableMultimap<Character, String> expected = FastListMultimap.newMultimap();
        expected.put('T', "Ted");
        expected.put('S', "Sally");
        expected.put('M', "Mary");
        expected.put('B', "Bob");
        expected.put('S', "Sara");
        Assert.assertEquals(expected, result1);
        Assert.assertEquals(expected, result2);
        Assert.assertEquals(expected, result3);
        Assert.assertEquals(expected, result4);
        Assert.assertEquals(expected, result5);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.groupBy(null, null, null);
            }
        });
    }

    @Test
    public void groupByEachWithTarget()
    {
        MutableList<String> source = FastList.newListWith("Ted", "Sally", "Sally", "Mary", "Bob", "Sara");
        Function<String, Set<Character>> uppercaseSetFunction = new Function<String, Set<Character>>()
        {
            public Set<Character> valueOf(String name)
            {
                return StringIterate.asUppercaseSet(name);
            }
        };
        Multimap<Character, String> result1 = Iterate.groupByEach(source, uppercaseSetFunction, FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result2 = Iterate.groupByEach(Collections.synchronizedList(source), uppercaseSetFunction, FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result3 = Iterate.groupByEach(Collections.synchronizedCollection(source), uppercaseSetFunction, FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result4 = Iterate.groupByEach(LazyIterate.adapt(source), uppercaseSetFunction, FastListMultimap.<Character, String>newMultimap());
        Multimap<Character, String> result5 = Iterate.groupByEach(new ArrayList<String>(source), uppercaseSetFunction, FastListMultimap.<Character, String>newMultimap());
        MutableMultimap<Character, String> expected = FastListMultimap.newMultimap();
        expected.put('T', "Ted");
        expected.putAll('E', FastList.newListWith("Ted"));
        expected.put('D', "Ted");
        expected.putAll('S', FastList.newListWith("Sally", "Sally", "Sara"));
        expected.putAll('A', FastList.newListWith("Sally", "Sally", "Mary", "Sara"));
        expected.putAll('L', FastList.newListWith("Sally", "Sally"));
        expected.putAll('Y', FastList.newListWith("Sally", "Sally"));
        expected.put('M', "Mary");
        expected.putAll('R', FastList.newListWith("Mary", "Sara"));
        expected.put('Y', "Mary");
        expected.put('B', "Bob");
        expected.put('O', "Bob");
        Assert.assertEquals(expected, result1);
        Assert.assertEquals(expected, result2);
        Assert.assertEquals(expected, result3);
        Assert.assertEquals(expected, result4);
        Assert.assertEquals(expected, result5);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.groupByEach(null, null, null);
            }
        });
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(FastList.newListWith(1, 2, 3), FastList.newList(Interval.oneTo(3)));
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(Iterate.contains(FastList.newListWith(1, 2, 3), 1));
        Assert.assertFalse(Iterate.contains(FastList.newListWith(1, 2, 3), 4));
        Assert.assertTrue(Iterate.contains(FastList.newListWith(1, 2, 3).asLazy(), 1));
        Assert.assertTrue(Iterate.contains(UnifiedMap.newWithKeysValues(1, 1, 2, 2, 3, 3), 1));
        Assert.assertFalse(Iterate.contains(UnifiedMap.newWithKeysValues(1, 1, 2, 2, 3, 3), 4));
        Assert.assertTrue(Iterate.contains(Interval.oneTo(3), 1));
        Assert.assertFalse(Iterate.contains(Interval.oneTo(3), 4));
    }

    public static final class ListContainer<T>
    {
        private final List<T> list;

        private ListContainer(List<T> list)
        {
            this.list = list;
        }

        private static <V> Function<ListContainer<V>, List<V>> getListFunction()
        {
            return new Function<ListContainer<V>, List<V>>()
            {
                public List<V> valueOf(ListContainer<V> anObject)
                {
                    return anObject.list;
                }
            };
        }

        public List<T> getList()
        {
            return this.list;
        }
    }

    @Test
    public void getFirstAndLast()
    {
        MutableList<Boolean> list = mList(Boolean.TRUE, null, Boolean.FALSE);
        Assert.assertEquals(Boolean.TRUE, Iterate.getFirst(list));
        Assert.assertEquals(Boolean.FALSE, Iterate.getLast(list));
        Assert.assertEquals(Boolean.TRUE, Iterate.getFirst(Collections.unmodifiableList(list)));
        Assert.assertEquals(Boolean.FALSE, Iterate.getLast(Collections.unmodifiableList(list)));
        Assert.assertEquals(Boolean.TRUE, Iterate.getFirst(new IterableAdapter<Boolean>(list)));
        Assert.assertEquals(Boolean.FALSE, Iterate.getLast(new IterableAdapter<Boolean>(list)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFirst_null_throws()
    {
        Iterate.getFirst(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getList_null_throws()
    {
        Iterate.getLast(null);
    }

    @Test
    public void getFirstAndLastInterval()
    {
        Assert.assertEquals(Integer.valueOf(1), Iterate.getFirst(Interval.oneTo(5)));
        Assert.assertEquals(Integer.valueOf(5), Iterate.getLast(Interval.oneTo(5)));
    }

    @Test
    public void getFirstAndLastLinkedList()
    {
        List<Boolean> list = new LinkedList<Boolean>(mList(Boolean.TRUE, null, Boolean.FALSE));
        Assert.assertEquals(Boolean.TRUE, Iterate.getFirst(list));
        Assert.assertEquals(Boolean.FALSE, Iterate.getLast(list));
    }

    @Test
    public void getFirstAndLastTreeSet()
    {
        Set<String> set = new TreeSet<String>(mList("1", "2"));
        Assert.assertEquals("1", Iterate.getFirst(set));
        Assert.assertEquals("2", Iterate.getLast(set));
    }

    @Test
    public void getFirstAndLastCollection()
    {
        Collection<Boolean> list = mList(Boolean.TRUE, null, Boolean.FALSE).asSynchronized();
        Assert.assertEquals(Boolean.TRUE, Iterate.getFirst(list));
        Assert.assertEquals(Boolean.FALSE, Iterate.getLast(list));
    }

    @Test
    public void getFirstAndLastOnEmpty()
    {
        Assert.assertNull(Iterate.getFirst(mList()));
        Assert.assertNull(Iterate.getLast(mList()));
    }

    @Test
    public void getFirstAndLastForSet()
    {
        Set<Integer> orderedSet = new TreeSet<Integer>();
        orderedSet.add(1);
        orderedSet.add(2);
        orderedSet.add(3);
        Assert.assertEquals(Integer.valueOf(1), Iterate.getFirst(orderedSet));
        Assert.assertEquals(Integer.valueOf(3), Iterate.getLast(orderedSet));
    }

    @Test
    public void getFirstAndLastOnEmptyForSet()
    {
        MutableSet<Object> set = UnifiedSet.newSet();
        Assert.assertNull(Iterate.getFirst(set));
        Assert.assertNull(Iterate.getLast(set));
    }

    private MutableSet<Integer> getIntegerSet()
    {
        return Interval.toSet(1, 5);
    }

    @Test
    public void selectDifferentTargetCollection()
    {
        MutableSet<Integer> set = this.getIntegerSet();
        List<Integer> list = Iterate.select(set, Predicates.instanceOf(Integer.class), new ArrayList<Integer>());
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4, 5), list);
    }

    @Test
    public void rejectWithDifferentTargetCollection()
    {
        MutableSet<Integer> set = this.getIntegerSet();
        Collection<Integer> result = Iterate.reject(set, Predicates.instanceOf(Integer.class), FastList.<Integer>newList());
        Verify.assertEmpty(result);
    }

    @Test
    public void count_empty()
    {
        Assert.assertEquals(0, Iterate.count(iList(), Predicates.alwaysTrue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_null_throws()
    {
        Iterate.count(null, Predicates.alwaysTrue());
    }

    @Test
    public void toMap()
    {
        MutableSet<Integer> set = UnifiedSet.newSet(this.getIntegerSet());
        MutableMap<String, Integer> map = Iterate.toMap(set, Functions.getToString());
        Verify.assertSize(5, map);
        Object expectedValue = 1;
        Object expectedKey = "1";
        Verify.assertContainsKeyValue(expectedKey, expectedValue, map);
        Verify.assertContainsKeyValue("2", 2, map);
        Verify.assertContainsKeyValue("3", 3, map);
        Verify.assertContainsKeyValue("4", 4, map);
        Verify.assertContainsKeyValue("5", 5, map);
    }

    @Test
    public void addToMap()
    {
        MutableSet<Integer> set = Interval.toSet(1, 5);
        MutableMap<String, Integer> map = UnifiedMap.newMap();
        map.put("the answer", 42);

        Iterate.addToMap(set, Functions.getToString(), map);

        Verify.assertSize(6, map);
        Verify.assertContainsAllKeyValues(
                map,
                "the answer", 42,
                "1", 1,
                "2", 2,
                "3", 3,
                "4", 4,
                "5", 5);
    }

    @Test
    public void toMapSelectingKeyAndValue()
    {
        MutableSet<Integer> set = UnifiedSet.newSet(this.getIntegerSet());
        MutableMap<String, Integer> map = Iterate.toMap(set, Functions.getToString(), TIMES_10_FUNCTION);
        Verify.assertSize(5, map);
        Verify.assertContainsKeyValue("1", 10, map);
        Verify.assertContainsKeyValue("2", 20, map);
        Verify.assertContainsKeyValue("3", 30, map);
        Verify.assertContainsKeyValue("4", 40, map);
        Verify.assertContainsKeyValue("5", 50, map);
    }

    @Test
    public void forEachWithIndex()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                UnifiedSet<Integer> set = UnifiedSet.newSet();
                Iterate.forEachWithIndex(each, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(set)));
                Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4, 5), set);
            }
        });
    }

    @Test
    public void selectPairs()
    {
        ImmutableList<Twin<String>> twins = iList(
                Tuples.twin("1", "2"),
                Tuples.twin("2", "1"),
                Tuples.twin("3", "3"));
        Collection<Twin<String>> results = Iterate.select(twins, new PairPredicate<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "1".equals(argument1) || "1".equals(argument2);
            }
        });
        Verify.assertSize(2, results);
    }

    @Test
    public void detect()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Integer result = Iterate.detect(each, Predicates.instanceOf(Integer.class));
                Assert.assertTrue(UnifiedSet.newSet(each).contains(result));
            }
        });
    }

    @Test
    public void detectIndex()
    {
        MutableList<Integer> list = Interval.toReverseList(1, 5);
        Assert.assertEquals(4, Iterate.detectIndex(list, Predicates.equal(1)));
        Assert.assertEquals(0, Iterate.detectIndex(list, Predicates.equal(5)));
        Assert.assertEquals(-1, Iterate.detectIndex(Lists.immutable.<Integer>of(), Predicates.equal(1)));
        Assert.assertEquals(-1, Iterate.detectIndex(Sets.immutable.<Integer>of(), Predicates.equal(1)));
    }

    @Test
    public void detectIndexWithTreeSet()
    {
        Set<Integer> treeSet = new TreeSet<Integer>(Interval.toReverseList(1, 5));
        Assert.assertEquals(0, Iterate.detectIndex(treeSet, Predicates.equal(1)));
        Assert.assertEquals(4, Iterate.detectIndex(treeSet, Predicates.equal(5)));
    }

    @Test
    public void detectIndexWith()
    {
        MutableList<Integer> list = Interval.toReverseList(1, 5);
        Assert.assertEquals(4, Iterate.detectIndexWith(list, Predicates2.equal(), 1));
        Assert.assertEquals(0, Iterate.detectIndexWith(list, Predicates2.equal(), 5));
        Assert.assertEquals(-1, Iterate.detectIndexWith(iList(), Predicates2.equal(), 5));
        Assert.assertEquals(-1, Iterate.detectIndexWith(iSet(), Predicates2.equal(), 5));
    }

    @Test
    public void detectIndexWithWithTreeSet()
    {
        Set<Integer> treeSet = new TreeSet<Integer>(Interval.toReverseList(1, 5));
        Assert.assertEquals(0, Iterate.detectIndexWith(treeSet, Predicates2.equal(), 1));
        Assert.assertEquals(4, Iterate.detectIndexWith(treeSet, Predicates2.equal(), 5));
    }

    @Test
    public void detectWithIfNone()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Integer result = Iterate.detectWithIfNone(each, Predicates2.instanceOf(), Integer.class, 5);
                Verify.assertContains(result, UnifiedSet.newSet(each));
            }
        });
    }

    @Test
    public void selectWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.selectWith(each, Predicates2.<Integer>greaterThan(), 3);
                Assert.assertTrue(result.containsAll(FastList.newListWith(4, 5)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.selectWith(null, null, null);
            }
        });
    }

    @Test
    public void selectWithWithTarget()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.selectWith(each, Predicates2.<Integer>greaterThan(), 3, FastList.<Integer>newList());
                Assert.assertTrue(result.containsAll(FastList.newListWith(4, 5)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.selectWith(null, null, null, null);
            }
        });
    }

    @Test
    public void rejectWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.rejectWith(each, Predicates2.<Integer>greaterThan(), 3);
                Assert.assertTrue(result.containsAll(FastList.newListWith(1, 2, 3)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.rejectWith(null, null, null);
            }
        });
    }

    @Test
    public void rejectWithTarget()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.rejectWith(each, Predicates2.<Integer>greaterThan(), 3, FastList.<Integer>newList());
                Assert.assertTrue(result.containsAll(FastList.newListWith(1, 2, 3)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.rejectWith(null, null, null, null);
            }
        });
    }

    @Test
    public void selectAndRejectWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Twin<MutableList<Integer>> result = Iterate.selectAndRejectWith(each, Predicates2.<Integer>greaterThan(), 3);
                Assert.assertEquals(iBag(4, 5), result.getOne().toBag());
                Assert.assertEquals(iBag(1, 2, 3), result.getTwo().toBag());
            }
        });
    }

    @Test
    public void partition()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                PartitionIterable<Integer> result = Iterate.partition(each, Predicates.greaterThan(3));
                Assert.assertEquals(iBag(4, 5), result.getSelected().toBag());
                Assert.assertEquals(iBag(1, 2, 3), result.getRejected().toBag());
            }
        });
    }

    @Test
    public void rejectTargetCollection()
    {
        MutableList<Integer> list = Interval.toReverseList(1, 5);
        MutableList<Integer> results =
                Iterate.reject(list, Predicates.instanceOf(Integer.class), FastList.<Integer>newList());
        Verify.assertEmpty(results);
    }

    @Test
    public void rejectOnRandomAccessTargetCollection()
    {
        List<Integer> list = Collections.synchronizedList(Interval.toReverseList(1, 5));
        MutableList<Integer> results =
                Iterate.reject(list, Predicates.instanceOf(Integer.class), FastList.<Integer>newList());
        Verify.assertEmpty(results);
    }

    @Test
    public void rejectWithTargetCollection()
    {
        MutableList<Integer> list = Interval.toReverseList(1, 5);
        MutableList<Integer> results =
                Iterate.rejectWith(list, Predicates2.instanceOf(), Integer.class, FastList.<Integer>newList());
        Verify.assertEmpty(results);
    }

    @Test
    public void rejectWithOnRandomAccessTargetCollection()
    {
        List<Integer> list = Collections.synchronizedList(Interval.toReverseList(1, 5));
        MutableList<Integer> results =
                Iterate.rejectWith(list, Predicates2.instanceOf(), Integer.class, FastList.<Integer>newList());
        Verify.assertEmpty(results);
    }

    @Test
    public void anySatisfy()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertTrue(Iterate.anySatisfy(each, Predicates.instanceOf(Integer.class)));
            }
        });
    }

    @Test
    public void anySatisfyWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertTrue(Iterate.anySatisfyWith(each, Predicates2.instanceOf(), Integer.class));
            }
        });
    }

    @Test
    public void allSatisfy()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertTrue(Iterate.allSatisfy(each, Predicates.instanceOf(Integer.class)));
            }
        });
    }

    @Test
    public void allSatisfyWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Assert.assertTrue(Iterate.allSatisfyWith(each, Predicates2.instanceOf(), Integer.class));
            }
        });
    }

    @Test
    public void selectWithSet()
    {
        Verify.assertSize(1, Iterate.selectWith(this.getIntegerSet(), Predicates2.equal(), 1));
        Verify.assertSize(
                1,
                Iterate.selectWith(this.getIntegerSet(), Predicates2.equal(), 1, FastList.<Integer>newList()));
    }

    @Test
    public void rejectWithSet()
    {
        Verify.assertSize(4, Iterate.rejectWith(this.getIntegerSet(), Predicates2.equal(), 1));
        Verify.assertSize(
                4,
                Iterate.rejectWith(this.getIntegerSet(), Predicates2.equal(), 1, FastList.<Integer>newList()));
    }

    @Test
    public void detectWithSet()
    {
        Assert.assertEquals(Integer.valueOf(1), Iterate.detectWith(this.getIntegerSet(), Predicates2.equal(), 1));
    }

    @Test
    public void detectWithRandomAccess()
    {
        List<Integer> list = Collections.synchronizedList(Interval.oneTo(5));
        Assert.assertEquals(Integer.valueOf(1), Iterate.detectWith(list, Predicates2.equal(), 1));
    }

    @Test
    public void anySatisfyWithSet()
    {
        Assert.assertTrue(Iterate.anySatisfyWith(this.getIntegerSet(), Predicates2.equal(), 1));
    }

    @Test
    public void allSatisfyWithSet()
    {
        Assert.assertFalse(Iterate.allSatisfyWith(this.getIntegerSet(), Predicates2.equal(), 1));
        Assert.assertTrue(Iterate.allSatisfyWith(this.getIntegerSet(), Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void selectAndRejectWithSet()
    {
        Twin<MutableList<Integer>> result = Iterate.selectAndRejectWith(this.getIntegerSet(), Predicates2.in(), iList(1));
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(4, result.getTwo());
    }

    @Test
    public void forEach()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                UnifiedSet<Integer> set = UnifiedSet.newSet();
                Iterate.forEach(each, CollectionAddProcedure.on(set));
                Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4, 5), set);
            }
        });
    }

    @Test
    public void collectIf()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<String> result = Iterate.collectIf(each, Predicates.greaterThan(3), Functions.getToString());
                Assert.assertTrue(result.containsAll(FastList.newListWith("4", "5")));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collectIf(null, null, null);
            }
        });
    }

    @Test
    public void collectIfTarget()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<String> result = Iterate.collectIf(each, Predicates.greaterThan(3), Functions.getToString(), FastList.<String>newList());
                Assert.assertTrue(result.containsAll(FastList.newListWith("4", "5")));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collectIf(null, null, null, null);
            }
        });
    }

    @Test
    public void collect()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<String> result = Iterate.collect(each, Functions.getToString());
                Assert.assertTrue(result.containsAll(FastList.newListWith("1", "2", "3", "4", "5")));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collect(null, Functions.<Object>getPassThru());
            }
        });
    }

    @Test
    public void collect_sortedSetSource()
    {
        class Foo implements Comparable<Foo>
        {
            private final int value;

            Foo(int value)
            {
                this.value = value;
            }

            public int getValue()
            {
                return this.value;
            }

            public int compareTo(Foo that)
            {
                return Comparators.naturalOrder().compare(this.value, that.value);
            }

            @Override
            public boolean equals(Object o)
            {
                if (this == o)
                {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass())
                {
                    return false;
                }

                Foo foo = (Foo) o;

                return this.value == foo.value;
            }

            @Override
            public int hashCode()
            {
                return this.value;
            }
        }

        class Bar
        {
            private final int value;

            Bar(int value)
            {
                this.value = value;
            }

            public int getValue()
            {
                return this.value;
            }

            @Override
            public boolean equals(Object o)
            {
                if (this == o)
                {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass())
                {
                    return false;
                }

                Bar bar = (Bar) o;

                return this.value == bar.value;
            }

            @Override
            public int hashCode()
            {
                return this.value;
            }
        }

        class BarFunction implements Function<Foo, Bar>
        {
            private static final long serialVersionUID = 1L;

            public Bar valueOf(Foo foo)
            {
                return new Bar(foo.getValue());
            }
        }

        Set<Foo> foos = new TreeSet<Foo>();
        foos.add(new Foo(1));
        foos.add(new Foo(2));
        Collection<Bar> bars = Iterate.collect(foos, new BarFunction());

        Assert.assertEquals(FastList.newListWith(new Bar(1), new Bar(2)), bars);
    }

    @Test
    public void collectTarget()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<String> result = Iterate.collect(each, Functions.getToString(), UnifiedSet.<String>newSet());
                Assert.assertTrue(result.containsAll(FastList.newListWith("1", "2", "3", "4", "5")));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collect(null, Functions.<Object>getPassThru(), null);
            }
        });
    }

    @Test
    public void collectWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<String> result = Iterate.collectWith(each, new Function2<Integer, Object, String>()
                {
                    public String value(Integer each, Object parm)
                    {
                        return each.toString() + parm.toString();
                    }
                }, " ");
                Assert.assertTrue(result.containsAll(FastList.newListWith("1 ", "2 ", "3 ", "4 ", "5 ")));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collectWith(null, null, null);
            }
        });
    }

    @Test
    public void collectWithWithTarget()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<String> result = Iterate.collectWith(each, new Function2<Integer, Object, String>()
                {
                    public String value(Integer each, Object parm)
                    {
                        return each.toString() + parm.toString();
                    }
                }, " ", UnifiedSet.<String>newSet());
                Assert.assertTrue(result.containsAll(FastList.newListWith("1 ", "2 ", "3 ", "4 ", "5 ")));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.collectWith(null, null, null, null);
            }
        });
    }

    @Test
    public void removeIf()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        this.assertRemoveIfFromList(integers);
        this.assertRemoveIfFromList(Collections.synchronizedList(integers));
        this.assertRemoveIfFromList(FastList.newList(integers));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.removeIf(null, null);
            }
        });
    }

    private void assertRemoveIfFromList(List<Integer> newIntegers)
    {
        Collection<Integer> result = Iterate.removeIf(newIntegers, IntegerPredicates.isEven());
        Assert.assertSame(newIntegers, result);
        Verify.assertContainsAll(newIntegers, 1, 3, 5);
        Verify.assertSize(3, newIntegers);
    }

    @Test
    public void removeIfLinkedList()
    {
        List<Integer> integers = new LinkedList<Integer>(Interval.oneTo(5).toList());
        this.assertRemoveIfFromList(integers);
    }

    @Test
    public void removeIfAll()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        Collection<Integer> result = Iterate.removeIf(integers, Predicates.alwaysTrue());
        Assert.assertSame(integers, result);
        Verify.assertSize(0, integers);
    }

    @Test
    public void removeIfNone()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        Collection<Integer> result = Iterate.removeIf(integers, Predicates.alwaysFalse());
        Assert.assertSame(integers, result);
        Verify.assertSize(5, integers);
    }

    @Test
    public void removeIfFromSet()
    {
        MutableSet<Integer> integers = Interval.toSet(1, 5);
        Collection<Integer> result = Iterate.removeIf(integers, IntegerPredicates.isEven());
        Assert.assertSame(integers, result);
        Verify.assertContainsAll(integers, 1, 3, 5);
        Verify.assertSize(3, integers);
    }

    @Test
    public void injectIntoIfProcedure()
    {
        Integer newItemToIndex = 99;
        MutableMap<String, Integer> index1 = createPretendIndex(1);
        MutableMap<String, Integer> index2 = createPretendIndex(2);
        MutableMap<String, Integer> index3 = createPretendIndex(3);
        MutableMap<String, Integer> index4 = createPretendIndex(4);
        MutableMap<String, Integer> index5 = createPretendIndex(5);
        MutableMap<String, MutableMap<String, Integer>> allIndexes = UnifiedMap.newMapWith(
                Tuples.pair("pretend index 1", index1),
                Tuples.pair("pretend index 2", index2),
                Tuples.pair("pretend index 3", index3),
                Tuples.pair("pretend index 4", index4),
                Tuples.pair("pretend index 5", index5));
        MutableSet<MutableMap<String, Integer>> systemIndexes = Sets.fixedSize.of(index3, index5);
        Function2<Integer, Map<String, Integer>, Integer> addFunction =
                new Function2<Integer, Map<String, Integer>, Integer>()
                {
                    public Integer value(Integer itemToAdd, Map<String, Integer> index)
                    {
                        index.put(itemToAdd.toString(), itemToAdd);
                        return itemToAdd;
                    }
                };

        MapIterate.injectIntoIf(newItemToIndex, allIndexes, Predicates.notIn(systemIndexes), addFunction);

        Verify.assertSize(5, allIndexes);

        Verify.assertContainsKey("pretend index 2", allIndexes);
        Verify.assertContainsKey("pretend index 3", allIndexes);

        Verify.assertContainsKeyValue("99", newItemToIndex, index1);
        Verify.assertContainsKeyValue("99", newItemToIndex, index2);
        Verify.assertNotContainsKey("99", index3);
        Verify.assertContainsKeyValue("99", newItemToIndex, index4);
        Verify.assertNotContainsKey("99", index5);
    }

    public static MutableMap<String, Integer> createPretendIndex(int initialEntry)
    {
        return UnifiedMap.newWithKeysValues(String.valueOf(initialEntry), initialEntry);
    }

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(Iterate.isEmpty(null));
        Assert.assertTrue(Iterate.isEmpty(Lists.fixedSize.of()));
        Assert.assertFalse(Iterate.isEmpty(Lists.fixedSize.of("1")));
        Assert.assertTrue(Iterate.isEmpty(Maps.fixedSize.of()));
        Assert.assertFalse(Iterate.isEmpty(Maps.fixedSize.of("1", "1")));
        Assert.assertTrue(Iterate.isEmpty(new IterableAdapter<Object>(Lists.fixedSize.of())));
        Assert.assertFalse(Iterate.isEmpty(new IterableAdapter<String>(Lists.fixedSize.of("1"))));
        Assert.assertTrue(Iterate.isEmpty(Lists.fixedSize.of().asLazy()));
        Assert.assertFalse(Iterate.isEmpty(Lists.fixedSize.of("1").asLazy()));
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(Iterate.notEmpty(null));
        Assert.assertFalse(Iterate.notEmpty(Lists.fixedSize.of()));
        Assert.assertTrue(Iterate.notEmpty(Lists.fixedSize.of("1")));
        Assert.assertFalse(Iterate.notEmpty(Maps.fixedSize.of()));
        Assert.assertTrue(Iterate.notEmpty(Maps.fixedSize.of("1", "1")));
        Assert.assertFalse(Iterate.notEmpty(new IterableAdapter<Object>(Lists.fixedSize.of())));
        Assert.assertTrue(Iterate.notEmpty(new IterableAdapter<String>(Lists.fixedSize.of("1"))));
        Assert.assertFalse(Iterate.notEmpty(Lists.fixedSize.of().asLazy()));
        Assert.assertTrue(Iterate.notEmpty(Lists.fixedSize.of("1").asLazy()));
    }

    @Test
    public void toSortedList()
    {
        MutableList<Integer> list = Interval.toReverseList(1, 5);
        MutableList<Integer> sorted = Iterate.toSortedList(list);
        Verify.assertStartsWith(sorted, 1, 2, 3, 4, 5);
    }

    @Test
    public void toSortedListWithComparator()
    {
        MutableList<Integer> list = Interval.oneTo(5).toList();
        MutableList<Integer> sorted = Iterate.toSortedList(list, Collections.<Integer>reverseOrder());
        Verify.assertStartsWith(sorted, 5, 4, 3, 2, 1);
    }

    @Test
    public void select()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.select(each, Predicates.greaterThan(3));
                Assert.assertTrue(result.containsAll(FastList.newListWith(4, 5)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.select(null, null);
            }
        });
    }

    @Test
    public void selectTarget()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.select(each, Predicates.greaterThan(3), FastList.<Integer>newList());
                Assert.assertTrue(result.containsAll(FastList.newListWith(4, 5)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.select(null, null, null);
            }
        });
    }

    @Test
    public void reject()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.reject(each, Predicates.greaterThan(3));
                Assert.assertTrue(result.containsAll(FastList.newListWith(1, 2, 3)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.reject(null, null);
            }
        });
    }

    @Test
    public void rejectTarget()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.reject(each, Predicates.greaterThan(3), FastList.<Integer>newList());
                Assert.assertTrue(result.containsAll(FastList.newListWith(1, 2, 3)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.reject(null, null, null);
            }
        });
    }

    @Test
    public void selectInstancesOf()
    {
        Iterable<Number> numbers1 = Collections.unmodifiableList(new ArrayList<Number>(FastList.<Number>newListWith(1, 2.0, 3, 4.0, 5)));
        Iterable<Number> numbers2 = Collections.unmodifiableCollection(new ArrayList<Number>(FastList.<Number>newListWith(1, 2.0, 3, 4.0, 5)));

        Verify.assertContainsAll(Iterate.selectInstancesOf(numbers1, Integer.class), 1, 3, 5);
        Verify.assertContainsAll(Iterate.selectInstancesOf(numbers2, Integer.class), 1, 3, 5);
    }

    @Test
    public void count()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                int result = Iterate.count(each, Predicates.greaterThan(3));
                Assert.assertEquals(2, result);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void count_null_null_throws()
    {
        Iterate.count(null, null);
    }

    @Test
    public void countWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                int result = Iterate.countWith(each, Predicates2.<Integer>greaterThan(), 3);
                Assert.assertEquals(2, result);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void countWith_null_throws()
    {
        Iterate.countWith(null, null, null);
    }

    @Test
    public void injectIntoWith()
    {
        Sum result = new IntegerSum(0);
        Integer parameter = 2;
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        this.basicTestDoubleSum(result, integers, parameter);
    }

    @Test
    public void injectIntoWithRandomAccess()
    {
        Sum result = new IntegerSum(0);
        Integer parameter = 2;
        MutableList<Integer> integers = Interval.oneTo(5).toList().asSynchronized();
        this.basicTestDoubleSum(result, integers, parameter);
    }

    private void basicTestDoubleSum(Sum newResult, Collection<Integer> newIntegers, Integer newParameter)
    {
        Function3<Sum, Integer, Integer, Sum> function = new Function3<Sum, Integer, Integer, Sum>()
        {
            public Sum value(Sum sum, Integer element, Integer withValue)
            {
                return sum.add(element.intValue() * withValue.intValue());
            }
        };
        Sum sumOfDoubledValues = Iterate.injectIntoWith(newResult, newIntegers, function, newParameter);
        Assert.assertEquals(30, sumOfDoubledValues.getValue().intValue());
    }

    @Test
    public void injectIntoWithHashSet()
    {
        Sum result = new IntegerSum(0);
        Integer parameter = 2;
        MutableSet<Integer> integers = Interval.toSet(1, 5);
        this.basicTestDoubleSum(result, integers, parameter);
    }

    @Test
    public void forEachWith()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                final Sum result = new IntegerSum(0);
                Iterate.forEachWith(each, new Procedure2<Integer, Integer>()
                {
                    public void value(Integer integer, Integer parm)
                    {
                        result.add(integer.intValue() * parm.intValue());
                    }
                }, 2);
                Assert.assertEquals(30, result.getValue().intValue());
            }
        });
    }

    @Test
    public void forEachWithSets()
    {
        final Sum result = new IntegerSum(0);
        MutableSet<Integer> integers = Interval.toSet(1, 5);
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
    public void removeIfWith()
    {
        MutableList<Integer> objects = FastList.newList(Lists.fixedSize.of(1, 2, 3, null));
        Iterate.removeIfWith(objects, Predicates2.isNull(), null);
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
        MutableList<Integer> objects1 = FastList.newList(Lists.fixedSize.of(null, 1, 2, 3));
        Iterate.removeIfWith(objects1, Predicates2.isNull(), null);
        Verify.assertSize(3, objects1);
        Verify.assertContainsAll(objects1, 1, 2, 3);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.removeIfWith(null, null, null);
            }
        });
    }

    @Test
    public void removeIfWithFastList()
    {
        MutableList<Integer> objects = FastList.newListWith(1, 2, 3, null);
        Iterate.removeIfWith(objects, Predicates2.isNull(), null);
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
        MutableList<Integer> objects1 = FastList.newListWith(null, 1, 2, 3);
        Iterate.removeIfWith(objects1, Predicates2.isNull(), null);
        Verify.assertSize(3, objects1);
        Verify.assertContainsAll(objects1, 1, 2, 3);
    }

    @Test
    public void removeIfWithRandomAccess()
    {
        List<Integer> objects = Collections.synchronizedList(new ArrayList<Integer>(Lists.fixedSize.of(1, 2, 3, null)));
        Iterate.removeIfWith(objects, Predicates2.isNull(), null);
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
        List<Integer> objects1 =
                Collections.synchronizedList(new ArrayList<Integer>(Lists.fixedSize.of(null, 1, 2, 3)));
        Iterate.removeIfWith(objects1, Predicates2.isNull(), null);
        Verify.assertSize(3, objects1);
        Verify.assertContainsAll(objects1, 1, 2, 3);
    }

    @Test
    public void removeIfWithLinkedList()
    {
        List<Integer> objects = new LinkedList<Integer>(Lists.fixedSize.of(1, 2, 3, null));
        Iterate.removeIfWith(objects, Predicates2.isNull(), null);
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
        List<Integer> objects1 = new LinkedList<Integer>(Lists.fixedSize.of(null, 1, 2, 3));
        Iterate.removeIfWith(objects1, Predicates2.isNull(), null);
        Verify.assertSize(3, objects1);
        Verify.assertContainsAll(objects1, 1, 2, 3);
    }

    @Test
    public void sortThis()
    {
        MutableList<Integer> list = Interval.oneTo(5).toList();
        Collections.shuffle(list);
        Verify.assertStartsWith(Iterate.sortThis(list), 1, 2, 3, 4, 5);
        List<Integer> list3 = Interval.oneTo(5).addAllTo(new LinkedList<Integer>());
        Collections.shuffle(list3);
        Verify.assertStartsWith(Iterate.sortThis(list3), 1, 2, 3, 4, 5);
        List<Integer> listOfSizeOne = new LinkedList<Integer>();
        listOfSizeOne.add(1);
        Iterate.sortThis(listOfSizeOne);
        Assert.assertEquals(FastList.newListWith(1), listOfSizeOne);
    }

    @Test
    public void sortThisWithPredicate()
    {
        MutableList<Integer> list = Interval.oneTo(5).toList();
        Interval.oneTo(5).addAllTo(list);
        Collections.shuffle(list);
        Verify.assertStartsWith(Iterate.sortThis(list, Predicates2.<Integer>lessThan()), 1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
        MutableList<Integer> list2 = Interval.oneTo(5).toList();
        Interval.oneTo(5).addAllTo(list2);
        Collections.shuffle(list2);
        Verify.assertStartsWith(Iterate.sortThis(list2, Predicates2.<Integer>greaterThan()), 5, 5, 4, 4, 3, 3, 2, 2, 1, 1);
        List<Integer> list3 = Interval.oneTo(5).addAllTo(new LinkedList<Integer>());
        Interval.oneTo(5).addAllTo(list3);
        Collections.shuffle(list3);
        Verify.assertStartsWith(Iterate.sortThis(list3, Predicates2.<Integer>lessThan()), 1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
    }

    @Test
    public void sortThisBy()
    {
        MutableList<Integer> list = Interval.oneTo(5).toList();
        Interval.oneTo(5).addAllTo(list);
        Collections.shuffle(list);
        Verify.assertStartsWith(Iterate.sortThisBy(list, Functions.getToString()), 1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
    }

    @Test
    public void sortThisWithComparator()
    {
        MutableList<Integer> list = Interval.oneTo(5).toList();
        Verify.assertStartsWith(Iterate.sortThis(list, Collections.reverseOrder()), 5, 4, 3, 2, 1);
        List<Integer> list3 = Interval.oneTo(5).addAllTo(new LinkedList<Integer>());
        Verify.assertStartsWith(Iterate.sortThis(list3, Collections.reverseOrder()), 5, 4, 3, 2, 1);
    }

    @Test
    public void take()
    {
        MutableSet<Integer> set = this.getIntegerSet();
        Verify.assertSize(2, Iterate.take(set, 2));

        Verify.assertEmpty(Iterate.take(set, 0));
        Verify.assertSize(5, Iterate.take(set, 5));
        Verify.assertSize(5, Iterate.take(set, 10));

        MutableSet<Integer> set2 = UnifiedSet.newSet();
        Verify.assertEmpty(Iterate.take(set2, 2));

        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.take(each, 2);
                Verify.assertSize(2, result);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_null_throws()
    {
        Iterate.take(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_negative_throws()
    {
        Iterate.take(this.getIntegerSet(), -1);
    }

    @Test
    public void drop()
    {
        MutableSet<Integer> set = this.getIntegerSet();
        Verify.assertSize(3, Iterate.drop(set, 2));

        Verify.assertEmpty(Iterate.drop(set, 5));
        Verify.assertEmpty(Iterate.drop(set, 6));
        Verify.assertSize(5, Iterate.drop(set, 0));

        MutableSet<Integer> set2 = UnifiedSet.newSet();
        Verify.assertEmpty(Iterate.drop(set2, 2));

        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                Collection<Integer> result = Iterate.drop(each, 2);
                Verify.assertSize(3, result);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_null_throws()
    {
        Iterate.drop(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_negative_throws()
    {
        Iterate.drop(this.getIntegerSet(), -1);
    }

    @Test
    public void getOnlySingleton()
    {
        Object value = new Object();
        Assert.assertSame(value, Iterate.getOnly(Lists.fixedSize.of(value)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOnlyEmpty()
    {
        Iterate.getOnly(Lists.fixedSize.<String>of());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOnlyMultiple()
    {
        Iterate.getOnly(Lists.fixedSize.of(new Object(), new Object()));
    }

    private static class Times10Function implements Function<Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer valueOf(Integer object)
        {
            return 10 * object;
        }
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
    public void zip()
    {
        this.zip(FastList.newListWith("1", "2", "3", "4", "5", "6", "7"));
        this.zip(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        this.zip(new HashSet<String>(FastList.<String>newListWith("1", "2", "3", "4", "5", "6", "7")));
        this.zip(FastList.newListWith("1", "2", "3", "4", "5", "6", "7").asLazy());
        this.zip(new ArrayList<String>(Interval.oneTo(101).collect(Functions.getToString()).toList()));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.zip(null, null);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.zip(null, null, null);
            }
        });
    }

    private void zip(Iterable<String> iterable)
    {
        List<Object> nulls = Collections.nCopies(Iterate.sizeOf(iterable), null);
        Collection<Pair<String, Object>> pairs = Iterate.zip(iterable, nulls);
        Assert.assertEquals(
                UnifiedSet.newSet(iterable),
                Iterate.collect(pairs, Functions.<String>firstOfPair(), UnifiedSet.<String>newSet()));
        Assert.assertEquals(
                nulls,
                Iterate.collect(pairs, Functions.<Object>secondOfPair(), Lists.mutable.of()));
    }

    @Test
    public void zipWithIndex()
    {
        this.zipWithIndex(FastList.newListWith("1", "2", "3", "4", "5", "6", "7"));
        this.zipWithIndex(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        this.zipWithIndex(new HashSet<String>(FastList.<String>newListWith("1", "2", "3", "4", "5", "6", "7")));
        this.zipWithIndex(FastList.newListWith("1", "2", "3", "4", "5", "6", "7").asLazy());
        this.zipWithIndex(Lists.immutable.of("1", "2", "3", "4", "5", "6", "7"));
        this.zipWithIndex(new ArrayList<String>(Interval.oneTo(101).collect(Functions.getToString()).toList()));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.zipWithIndex(null);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.zipWithIndex(null, null);
            }
        });
    }

    private void zipWithIndex(Iterable<String> iterable)
    {
        Collection<Pair<String, Integer>> pairs = Iterate.zipWithIndex(iterable);
        Assert.assertEquals(
                UnifiedSet.newSet(iterable),
                Iterate.collect(pairs, Functions.<String>firstOfPair(), UnifiedSet.<String>newSet()));
        Assert.assertEquals(
                Interval.zeroTo(Iterate.sizeOf(iterable) - 1).toSet(),
                Iterate.collect(pairs, Functions.<Integer>secondOfPair(), UnifiedSet.<Integer>newSet()));
    }

    @Test
    public void chunk()
    {
        FastList<String> fastList = FastList.newListWith("1", "2", "3", "4", "5", "6", "7");
        RichIterable<RichIterable<String>> groups1 = Iterate.chunk(fastList, 2);
        RichIterable<Integer> sizes1 = groups1.collect(Functions.getSizeOf());
        Assert.assertEquals(FastList.newListWith(2, 2, 2, 1), sizes1);
        ArrayList<String> arrayList = new ArrayList<String>(fastList);
        RichIterable<RichIterable<String>> groups2 = Iterate.chunk(arrayList, 2);
        RichIterable<Integer> sizes2 = groups1.collect(Functions.getSizeOf());
        Assert.assertEquals(FastList.newListWith(2, 2, 2, 1), sizes2);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.chunk(null, 1);
            }
        });
    }

    @Test
    public void getOnly()
    {
        Assert.assertEquals("first", Iterate.getOnly(FastList.newListWith("first")));
        Assert.assertEquals("first", Iterate.getOnly(FastList.newListWith("first").asLazy()));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.getOnly(FastList.newListWith("first", "second"));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.getOnly(null);
            }
        });
    }

    private static class WordToItsLetters implements Function<String, Set<Character>>
    {
        private static final long serialVersionUID = 1L;

        public Set<Character> valueOf(String name)
        {
            return StringIterate.asUppercaseSet(name);
        }
    }

    @Test
    public void makeString()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                String result = Iterate.makeString(each);
                Assert.assertEquals("1, 2, 3, 4, 5", result);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.makeString(null);
            }
        });
    }

    @Test
    public void appendString()
    {
        this.iterables.forEach(new Procedure<Iterable<Integer>>()
        {
            public void value(Iterable<Integer> each)
            {
                StringBuilder stringBuilder = new StringBuilder();
                Iterate.appendString(each, stringBuilder);
                String result = stringBuilder.toString();
                Assert.assertEquals("1, 2, 3, 4, 5", result);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.appendString(null, new StringBuilder());
            }
        });
    }

    @Test
    public void aggregateByMutating()
    {
        this.aggregateByMutableResult(FastList.newListWith(1, 1, 1, 2, 2, 3));
        this.aggregateByMutableResult(UnifiedSet.newSetWith(1, 1, 1, 2, 2, 3));
        this.aggregateByMutableResult(HashBag.newBagWith(1, 1, 1, 2, 2, 3));
        this.aggregateByMutableResult(new HashSet(UnifiedSet.newSetWith(1, 1, 1, 2, 2, 3)));
        this.aggregateByMutableResult(new LinkedList(FastList.newListWith(1, 1, 1, 2, 2, 3)));
        this.aggregateByMutableResult(new ArrayList(FastList.newListWith(1, 1, 1, 2, 2, 3)));
        this.aggregateByMutableResult(Arrays.asList(1, 1, 1, 2, 2, 3));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                IterateTest.this.aggregateByMutableResult(null);
            }
        });
    }

    private void aggregateByMutableResult(Iterable<Integer> iterable)
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
        MapIterable<String, AtomicInteger> aggregation = Iterate.aggregateInPlaceBy(iterable, Functions.getToString(), valueCreator, sumAggregator);
        if (iterable instanceof Set)
        {
            Assert.assertEquals(1, aggregation.get("1").intValue());
            Assert.assertEquals(2, aggregation.get("2").intValue());
            Assert.assertEquals(3, aggregation.get("3").intValue());
        }
        else
        {
            Assert.assertEquals(3, aggregation.get("1").intValue());
            Assert.assertEquals(4, aggregation.get("2").intValue());
            Assert.assertEquals(3, aggregation.get("3").intValue());
        }
    }

    @Test
    public void aggregateByImmutableResult()
    {
        this.aggregateByImmutableResult(FastList.newListWith(1, 1, 1, 2, 2, 3));
        this.aggregateByImmutableResult(UnifiedSet.newSetWith(1, 1, 1, 2, 2, 3));
        this.aggregateByImmutableResult(HashBag.newBagWith(1, 1, 1, 2, 2, 3));
        this.aggregateByImmutableResult(new HashSet(UnifiedSet.newSetWith(1, 1, 1, 2, 2, 3)));
        this.aggregateByImmutableResult(new LinkedList(FastList.newListWith(1, 1, 1, 2, 2, 3)));
        this.aggregateByImmutableResult(new ArrayList(FastList.newListWith(1, 1, 1, 2, 2, 3)));
        this.aggregateByImmutableResult(Arrays.asList(1, 1, 1, 2, 2, 3));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                IterateTest.this.aggregateByImmutableResult(null);
            }
        });
    }

    private void aggregateByImmutableResult(Iterable<Integer> iterable)
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
        MapIterable<String, Integer> aggregation = Iterate.aggregateBy(iterable, Functions.getToString(), valueCreator, sumAggregator);
        if (iterable instanceof Set)
        {
            Assert.assertEquals(1, aggregation.get("1").intValue());
            Assert.assertEquals(2, aggregation.get("2").intValue());
            Assert.assertEquals(3, aggregation.get("3").intValue());
        }
        else
        {
            Assert.assertEquals(3, aggregation.get("1").intValue());
            Assert.assertEquals(4, aggregation.get("2").intValue());
            Assert.assertEquals(3, aggregation.get("3").intValue());
        }
    }

    @Test
    public void sumOfInt()
    {
        Assert.assertEquals(6, Iterate.sumOfInt(FastList.newListWith(1, 2, 3), new IntFunction<Integer>()
        {
            public int intValueOf(Integer value)
            {
                return value;
            }
        }));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.sumOfInt(null, null);
            }
        });
    }

    @Test
    public void sumOfLong()
    {
        Assert.assertEquals(6L, Iterate.sumOfLong(FastList.<Long>newListWith(Long.valueOf(1), Long.valueOf(2), Long.valueOf(3)), new LongFunction<Long>()
        {
            public long longValueOf(Long value)
            {
                return value;
            }
        }));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.sumOfLong(null, null);
            }
        });
    }

    @Test
    public void sumOfFloat()
    {
        Assert.assertEquals(6.0d, Iterate.sumOfFloat(FastList.<Float>newListWith(Float.valueOf(1), Float.valueOf(2), Float.valueOf(3)), new FloatFunction<Float>()
        {
            public float floatValueOf(Float value)
            {
                return value;
            }
        }), 0.0d);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.sumOfFloat(null, null);
            }
        });
    }

    @Test
    public void sumOfDouble()
    {
        Assert.assertEquals(6.0d, Iterate.sumOfDouble(FastList.<Double>newListWith(Double.valueOf(1), Double.valueOf(2), Double.valueOf(3)), new DoubleFunction<Double>()
        {
            public double doubleValueOf(Double value)
            {
                return value;
            }
        }), 0.0d);
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.sumOfDouble(null, null);
            }
        });
    }
}
