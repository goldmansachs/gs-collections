/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.map;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.ByteIterable;
import com.gs.collections.api.CharIterable;
import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.LongIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.ShortIterable;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.IntegerWithCast;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.StringFunctions;
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
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;
import static org.hamcrest.CoreMatchers.*;

public abstract class MapIterableTestCase
{
    protected abstract <K, V> MapIterable<K, V> newMap();

    protected abstract <K, V> MapIterable<K, V> newMapWithKeysValues(
            K key1, V value1,
            K key2, V value2);

    protected abstract <K, V> MapIterable<K, V> newMapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    protected abstract <K, V> MapIterable<K, V> newMapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);

    @Test
    public void isEmpty()
    {
        Assert.assertFalse(this.newMapWithKeysValues(1, "1", 2, "2").isEmpty());
        Assert.assertTrue(this.<Object, Object>newMap().isEmpty());
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.newMap().notEmpty());
        Assert.assertTrue(this.newMapWithKeysValues(1, "1", 2, "2").notEmpty());
    }

    @Test
    public void ifPresentApply()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2);
        Assert.assertEquals("1", map.ifPresentApply("1", Functions.getToString()));
        Assert.assertNull(map.ifPresentApply("3", Functions.getToString()));
    }

    @Test
    public void getIfAbsent_function()
    {
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsent(4, new PassThruFunction0<String>("4")));
        Assert.assertEquals("3", map.getIfAbsent(3, new PassThruFunction0<String>("3")));
        Assert.assertNull(map.get(4));
    }

    @Test
    public void getIfAbsent()
    {
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentValue(4, "4"));
        Assert.assertEquals("3", map.getIfAbsentValue(3, "3"));
        Assert.assertNull(map.get(4));
    }

    @Test
    public void getIfAbsentWith()
    {
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentWith(4, Functions.getToString(), 4));
        Assert.assertEquals("3", map.getIfAbsentWith(3, Functions.getToString(), 3));
        Assert.assertNull(map.get(4));
    }

    @Test
    public void forEach()
    {
        MutableBag<String> result = Bags.mutable.of();
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        map.forEach(CollectionAddProcedure.<String>on(result));
        Assert.assertEquals(Bags.mutable.of("One", "Two", "Three", "Four"), result);
    }

    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MapIterable<Integer, Integer> map = this.newMapWithKeysValues(-1, 1, -2, 2, -3, 3, -4, 4);
        map.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 10);
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 11, 12, 13, 14);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<String> result = Lists.mutable.of();
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        map.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                result.add(value);
                result.add(String.valueOf(index));
            }
        });
        Verify.assertSize(8, result);
        Verify.assertContainsAll(result,
                "One", "Two", "Three", "Four", // Map values
                "0", "1", "2", "3");  // Stringified index values
    }

    @Test
    public void forEachKey()
    {
        UnifiedSet<Integer> result = UnifiedSet.newSet();
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        map.forEachKey(CollectionAddProcedure.<Integer>on(result));
        Verify.assertSetsEqual(UnifiedSet.newSetWith(1, 2, 3), result);
    }

    @Test
    public void forEachValue()
    {
        UnifiedSet<String> result = UnifiedSet.newSet();
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        map.forEachValue(CollectionAddProcedure.<String>on(result));
        Verify.assertSetsEqual(UnifiedSet.newSetWith("1", "2", "3"), result);
    }

    @Test
    public void forEachKeyValue()
    {
        final UnifiedMap<Integer, String> result = UnifiedMap.newMap();
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        map.forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                result.put(key, value);
            }
        });
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "1", 2, "2", 3, "3"), result);
    }

    @Test
    public void collectMap()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        MapIterable<Integer, String> actual = map.collect(new Function2<String, String, Pair<Integer, String>>()
        {
            public Pair<Integer, String> value(String argument1, String argument2)
            {
                return Tuples.pair(Integer.valueOf(argument1), argument1 + ':' + new StringBuilder(argument2).reverse());
            }
        });
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "1:enO", 2, "2:owT", 3, "3:eerhT"), actual);
    }

    @Test
    public void collectBoolean()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "true", "Two", "nah", "Three", "TrUe");
        BooleanIterable actual = map.collectBoolean(StringFunctions.toPrimitiveBoolean());
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), actual.toBag());
    }

    @Test
    public void collectByte()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "1", "Two", "2", "Three", "3");
        ByteIterable actual = map.collectByte(StringFunctions.toPrimitiveByte());
        Assert.assertEquals(ByteHashBag.newBagWith((byte) 1, (byte) 2, (byte) 3), actual.toBag());
    }

    @Test
    public void collectChar()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "A1", "Two", "B", "Three", "C#++");
        CharIterable actual = map.collectChar(StringFunctions.toFirstChar());
        Assert.assertEquals(CharHashBag.newBagWith('A', 'B', 'C'), actual.toBag());
    }

    @Test
    public void collectDouble()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "1", "Two", "2", "Three", "3");
        DoubleIterable actual = map.collectDouble(StringFunctions.toPrimitiveDouble());
        Assert.assertEquals(DoubleHashBag.newBagWith(1.0d, 2.0d, 3.0d), actual.toBag());
    }

    @Test
    public void collectFloat()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "1", "Two", "2", "Three", "3");
        FloatIterable actual = map.collectFloat(StringFunctions.toPrimitiveFloat());
        Assert.assertEquals(FloatHashBag.newBagWith(1.0f, 2.0f, 3.0f), actual.toBag());
    }

    @Test
    public void collectInt()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "1", "Two", "2", "Three", "3");
        IntIterable actual = map.collectInt(StringFunctions.toPrimitiveInt());
        Assert.assertEquals(IntHashBag.newBagWith(1, 2, 3), actual.toBag());
    }

    @Test
    public void collectLong()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "1", "Two", "2", "Three", "3");
        LongIterable actual = map.collectLong(StringFunctions.toPrimitiveLong());
        Assert.assertEquals(LongHashBag.newBagWith(1L, 2L, 3L), actual.toBag());
    }

    @Test
    public void collectShort()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("One", "1", "Two", "2", "Three", "3");
        ShortIterable actual = map.collectShort(StringFunctions.toPrimitiveShort());
        Assert.assertEquals(ShortHashBag.newBagWith((short) 1, (short) 2, (short) 3), actual.toBag());
    }

    @Test
    public void collectValues()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        MapIterable<String, String> actual = map.collectValues(new Function2<String, String, String>()
        {
            public String value(String argument1, String argument2)
            {
                return new StringBuilder(argument2).reverse().toString();
            }
        });
        Assert.assertEquals(UnifiedMap.newWithKeysValues("1", "enO", "2", "owT", "3", "eerhT"), actual);
    }

    @Test
    public void select()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        RichIterable<String> actual = map.select(Predicates.equal("Two"));
        Assert.assertEquals(HashBag.newBagWith("Two"), actual.toBag());
    }

    @Test
    public void selectWith()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        RichIterable<String> actual = map.selectWith(Predicates2.equal(), "Two");
        Assert.assertEquals(HashBag.newBagWith("Two"), actual.toBag());
    }

    @Test
    public void reject()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        RichIterable<String> actual = map.reject(Predicates.equal("Two"));
        Assert.assertEquals(HashBag.newBagWith("One", "Three"), actual.toBag());
    }

    @Test
    public void rejectWith()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        RichIterable<String> actual = map.rejectWith(Predicates2.equal(), "Two");
        Assert.assertEquals(HashBag.newBagWith("One", "Three"), actual.toBag());
    }

    @Test
    public void collect()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        RichIterable<String> actual = map.collect(StringFunctions.toLowerCase());
        Assert.assertEquals(HashBag.newBagWith("one", "two", "three"), actual.toBag());
    }

    @Test
    public void selectMap()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        MapIterable<String, String> actual = map.select(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "1".equals(argument1) || "Two".equals(argument2);
            }
        });
        Assert.assertEquals(2, actual.size());
        Assert.assertTrue(actual.keysView().containsAllArguments("1", "2"));
        Assert.assertTrue(actual.valuesView().containsAllArguments("One", "Two"));
    }

    @Test
    public void rejectMap()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        MapIterable<String, String> actual = map.reject(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "1".equals(argument1) || "Two".equals(argument2);
            }
        });
        Assert.assertEquals(UnifiedMap.newWithKeysValues("3", "Three"), actual);
    }

    @Test
    public void detect()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        Pair<String, String> one = map.detect(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "1".equals(argument1);
            }
        });
        Assert.assertNotNull(one);
        Assert.assertEquals("1", one.getOne());
        Assert.assertEquals("One", one.getTwo());

        Pair<String, String> two = map.detect(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "Two".equals(argument2);
            }
        });
        Assert.assertNotNull(two);
        Assert.assertEquals("2", two.getOne());
        Assert.assertEquals("Two", two.getTwo());

        Assert.assertNull(map.detect(Predicates2.alwaysFalse()));
    }

    @Test
    public void anySatisfy()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Verify.assertAnySatisfy((Map<String, String>) map, Predicates.instanceOf(String.class));
        Assert.assertFalse(map.anySatisfy(Predicates.equal("Monkey")));
    }

    @Test
    public void anySatisfyWith()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Assert.assertTrue(map.anySatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertFalse(map.anySatisfyWith(Predicates2.equal(), "Monkey"));
    }

    @Test
    public void allSatisfy()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Verify.assertAllSatisfy((Map<String, String>) map, Predicates.instanceOf(String.class));
        Assert.assertFalse(map.allSatisfy(Predicates.equal("Monkey")));
    }

    @Test
    public void allSatisfyWith()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Assert.assertTrue(map.allSatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertFalse(map.allSatisfyWith(Predicates2.equal(), "Monkey"));
    }

    @Test
    public void noneSatisfy()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Verify.assertNoneSatisfy((Map<String, String>) map, Predicates.instanceOf(Integer.class));
        Assert.assertTrue(map.noneSatisfy(Predicates.equal("Monkey")));
        Assert.assertFalse(map.noneSatisfy(Predicates.equal("Two")));
    }

    @Test
    public void noneSatisfyWith()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Assert.assertTrue(map.noneSatisfyWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertTrue(map.noneSatisfyWith(Predicates2.equal(), "Monkey"));
        Assert.assertFalse(map.noneSatisfyWith(Predicates2.equal(), "Two"));
    }

    @Test
    public void appendString()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        StringBuilder builder1 = new StringBuilder();
        map.appendString(builder1);
        String defaultString = builder1.toString();
        Assert.assertEquals(15, defaultString.length());

        StringBuilder builder2 = new StringBuilder();
        map.appendString(builder2, "|");
        String delimitedString = builder2.toString();
        Assert.assertEquals(13, delimitedString.length());
        Verify.assertContains("|", delimitedString);

        StringBuilder builder3 = new StringBuilder();
        map.appendString(builder3, "{", "|", "}");
        String wrappedString = builder3.toString();
        Assert.assertEquals(15, wrappedString.length());
        Verify.assertContains("|", wrappedString);
        Assert.assertTrue(wrappedString.startsWith("{"));
        Assert.assertTrue(wrappedString.endsWith("}"));
    }

    @Test
    public void toBag()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        MutableBag<String> bag = map.toBag();
        Assert.assertEquals(Bags.mutable.of("One", "Two", "Three"), bag);
    }

    @Test
    public void asLazy()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        LazyIterable<String> lazy = map.asLazy();
        Verify.assertContainsAll(lazy.toList(), "One", "Two", "Three");
    }

    @Test
    public void toList()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");
        MutableList<String> list = map.toList();
        Verify.assertContainsAll(list, "One", "Two", "Three");
    }

    @Test
    public void toMap()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "3", "Three", "4", "Four");

        MapIterable<Integer, String> actual = map.toMap(new Function<String, Integer>()
        {
            public Integer valueOf(String object)
            {
                return object.length();
            }
        }, Functions.getToString());

        Assert.assertEquals(UnifiedMap.newWithKeysValues(3, "One", 5, "Three", 4, "Four"), actual);
    }

    @Test
    public void toSet()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        MutableSet<String> set = map.toSet();
        Verify.assertContainsAll(set, "One", "Two", "Three");
    }

    @Test
    public void toSortedList()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        MutableList<Integer> sorted = map.toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), sorted);

        MutableList<Integer> reverse = map.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(FastList.newListWith(4, 3, 2, 1), reverse);
    }

    @Test
    public void toSortedListBy()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        MutableList<Integer> list = map.toSortedListBy(Functions.getToString());
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), list);
    }

    @Test
    public void toSortedSet()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        MutableSortedSet<Integer> sorted = map.toSortedSet();
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 3, 4), sorted);

        MutableSortedSet<Integer> reverse = map.toSortedSet(Collections.<Integer>reverseOrder());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 1, 2, 3, 4), reverse);
    }

    @Test
    public void toSortedSetBy()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        MutableSortedSet<Integer> sorted = map.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 3, 4), sorted);
    }

    @Test
    public void toSortedMap()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "3", "Three", "4", "Four");

        MapIterable<Integer, String> actual = map.toSortedMap(new Function<String, Integer>()
        {
            public Integer valueOf(String object)
            {
                return object.length();
            }
        }, Functions.getToString());

        MapIterable<Integer, String> actualWithComparator = map.toSortedMap(Comparators.reverseNaturalOrder(), new Function<String, Integer>()
        {
            public Integer valueOf(String object)
            {
                return object.length();
            }
        }, Functions.getToString());

        Verify.assertIterablesEqual(TreeSortedMap.newMapWith(3, "One", 5, "Three", 4, "Four"), actual);
        TreeSortedMap<Object, Object> expectedIterable = TreeSortedMap.newMap(Comparators.reverseNaturalOrder());
        expectedIterable.put(3, "One");
        expectedIterable.put(5, "Three");
        expectedIterable.put(4, "Four");
        Verify.assertIterablesEqual(expectedIterable, actualWithComparator);
    }

    @Test
    public void chunk()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        RichIterable<RichIterable<String>> chunks = map.chunk(2).toList();

        RichIterable<Integer> sizes = chunks.collect(new Function<RichIterable<String>, Integer>()
        {
            public Integer valueOf(RichIterable<String> object)
            {
                return object.size();
            }
        });
        Assert.assertEquals(FastList.newListWith(2, 1), sizes);
    }

    @Test
    public void collect_value()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Verify.assertContainsAll(
                map.collect(Functions.getToString()).toSet(),
                "1", "2", "3", "4");
        Verify.assertContainsAll(
                map.collect(
                        Functions.getToString(),
                        UnifiedSet.<String>newSet()), "1", "2", "3", "4");
    }

    @Test
    public void collectIf()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Bag<String> odd = map.collectIf(IntegerPredicates.isOdd(), Functions.getToString()).toBag();
        Assert.assertEquals(Bags.mutable.of("1", "3"), odd);

        Bag<String> even = map.collectIf(IntegerPredicates.isEven(), Functions.getToString(), HashBag.<String>newBag());
        Assert.assertEquals(Bags.mutable.of("2", "4"), even);
    }

    @Test
    public void collectWith()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        RichIterable<Integer> actual = map.collectWith(AddFunction.INTEGER, 1);
        Verify.assertContainsAll(actual, 2, 3, 4, 5);
    }

    @Test
    public void collectWithToTarget()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        FastList<Integer> actual = map.collectWith(AddFunction.INTEGER, 1, FastList.<Integer>newList());
        Verify.assertContainsAll(actual, 2, 3, 4, 5);
    }

    @Test
    public void contains()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Assert.assertTrue(map.contains("Two"));
    }

    @Test
    public void containsAll()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Assert.assertTrue(map.containsAll(FastList.newListWith("One", "Two")));
        Assert.assertTrue(map.containsAll(FastList.newListWith("One", "Two", "Three")));
        Assert.assertFalse(map.containsAll(FastList.newListWith("One", "Two", "Three", "Four")));
    }

    @Test
    public void getFirst()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        String value = map.getFirst();
        Assert.assertNotNull(value);
        Assert.assertTrue(value, map.valuesView().contains(value));
    }

    @Test
    public void getLast()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        String value = map.getLast();
        Assert.assertNotNull(value);
        Assert.assertTrue(value, map.valuesView().contains(value));
    }

    @Test
    public void containsAllIterable()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Assert.assertTrue(map.containsAllIterable(FastList.newListWith("One", "Two")));
        Assert.assertFalse(map.containsAllIterable(FastList.newListWith("One", "Four")));
    }

    @Test
    public void containsAllArguments()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Assert.assertTrue(map.containsAllArguments("One", "Two"));
        Assert.assertFalse(map.containsAllArguments("One", "Four"));
    }

    @Test
    public void count()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        int actual = map.count(Predicates.equal("One").or(Predicates.equal("Three")));

        Assert.assertEquals(2, actual);
    }

    @Test
    public void countWith()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        int actual = map.countWith(Predicates2.equal(), "One");

        Assert.assertEquals(1, actual);
    }

    @Test
    public void detect_value()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        String resultFound = map.detect(Predicates.equal("One"));
        Assert.assertEquals("One", resultFound);

        String resultNotFound = map.detect(Predicates.equal("Four"));
        Assert.assertNull(resultNotFound);
    }

    @Test
    public void detectWith()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        String resultFound = map.detectWith(Predicates2.equal(), "One");
        Assert.assertEquals("One", resultFound);

        String resultNotFound = map.detectWith(Predicates2.equal(), "Four");
        Assert.assertNull(resultNotFound);
    }

    @Test
    public void detectIfNone_value()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Function0<String> function = new Function0<String>()
        {
            public String value()
            {
                return "Zero";
            }
        };
        String resultNotFound = map.detectIfNone(Predicates.equal("Four"), function);
        Assert.assertEquals("Zero", resultNotFound);

        String resultFound = map.detectIfNone(Predicates.equal("One"), function);
        Assert.assertEquals("One", resultFound);
    }

    @Test
    public void detectWithIfNone()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        Function0<String> function = new Function0<String>()
        {
            public String value()
            {
                return "Zero";
            }
        };
        String resultNotFound = map.detectWithIfNone(Predicates2.equal(), "Four", function);
        Assert.assertEquals("Zero", resultNotFound);

        String resultFound = map.detectWithIfNone(Predicates2.equal(), "One", function);
        Assert.assertEquals("One", resultFound);
    }

    @Test
    public void flatten_value()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two");

        Function<String, Iterable<Character>> function = new Function<String, Iterable<Character>>()
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
        };

        RichIterable<Character> blob = map.flatCollect(function);
        Assert.assertTrue(blob.containsAllArguments(
                Character.valueOf('O'),
                Character.valueOf('n'),
                Character.valueOf('e'),
                Character.valueOf('T'),
                Character.valueOf('w'),
                Character.valueOf('o')));

        RichIterable<Character> blobFromTarget = map.flatCollect(function, FastList.<Character>newList());
        Assert.assertTrue(blobFromTarget.containsAllArguments(
                Character.valueOf('O'),
                Character.valueOf('n'),
                Character.valueOf('e'),
                Character.valueOf('T'),
                Character.valueOf('w'),
                Character.valueOf('o')));
    }

    @Test
    public void groupBy()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };

        final Multimap<Boolean, Integer> expected = FastListMultimap.newMultimap(
                Tuples.pair(Boolean.TRUE, 1), Tuples.pair(Boolean.TRUE, 3),
                Tuples.pair(Boolean.FALSE, 2), Tuples.pair(Boolean.FALSE, 4)
        );

        final Multimap<Boolean, Integer> actual = map.groupBy(isOddFunction);
        expected.forEachKey(new Procedure<Boolean>()
        {
            public void value(Boolean each)
            {
                Assert.assertTrue(actual.containsKey(each));
                MutableList<Integer> values = actual.get(each).toList();
                Verify.assertNotEmpty(values);
                Assert.assertTrue(expected.get(each).containsAllIterable(values));
            }
        });

        final Multimap<Boolean, Integer> actualFromTarget = map.groupBy(isOddFunction, FastListMultimap.<Boolean, Integer>newMultimap());
        expected.forEachKey(new Procedure<Boolean>()
        {
            public void value(Boolean each)
            {
                Assert.assertTrue(actualFromTarget.containsKey(each));
                MutableList<Integer> values = actualFromTarget.get(each).toList();
                Verify.assertNotEmpty(values);
                Assert.assertTrue(expected.get(each).containsAllIterable(values));
            }
        });
    }

    @Test
    public void groupByEach()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        final MutableMultimap<Integer, Integer> expected = FastListMultimap.newMultimap();
        for (int i = 1; i < 4; i++)
        {
            expected.putAll(-i, Interval.fromTo(i, 4));
        }

        NegativeIntervalFunction function = new NegativeIntervalFunction();
        final Multimap<Integer, Integer> actual = map.groupByEach(function);
        expected.forEachKey(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                Assert.assertTrue(actual.containsKey(each));
                MutableList<Integer> values = actual.get(each).toList();
                Verify.assertNotEmpty(values);
                Assert.assertTrue(expected.get(each).containsAllIterable(values));
            }
        });

        final Multimap<Integer, Integer> actualFromTarget = map.groupByEach(function, FastListMultimap.<Integer, Integer>newMultimap());
        expected.forEachKey(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                Assert.assertTrue(actualFromTarget.containsKey(each));
                MutableList<Integer> values = actualFromTarget.get(each).toList();
                Verify.assertNotEmpty(values);
                Assert.assertTrue(expected.get(each).containsAllIterable(values));
            }
        });
    }

    @Test
    public void injectInto()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Integer actual = map.injectInto(0, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(10), actual);

        Sum sum = map.injectInto(new IntegerSum(0), SumProcedure.number());
        Assert.assertEquals(new IntegerSum(10), sum);
    }

    @Test
    public void injectIntoInt()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        int actual = map.injectInto(0, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(10, actual);
    }

    @Test
    public void injectIntoLong()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        long actual = map.injectInto(0, AddFunction.INTEGER_TO_LONG);
        Assert.assertEquals(10, actual);
    }

    @Test
    public void injectIntoFloat()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        float actual = map.injectInto(0, AddFunction.INTEGER_TO_FLOAT);
        Assert.assertEquals(10.0F, actual, 0.01);
    }

    @Test
    public void injectIntoDouble()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        double actual = map.injectInto(0, AddFunction.INTEGER_TO_DOUBLE);
        Assert.assertEquals(10.0d, actual, 0.01);
    }

    @Test
    public void sumInt()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        long actual = map.sumOfInt(new IntFunction<Integer>()
        {
            public int intValueOf(Integer integer)
            {
                return integer;
            }
        });
        Assert.assertEquals(10L, actual);
    }

    @Test
    public void sumLong()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        long actual = map.sumOfLong(new LongFunction<Integer>()
        {
            public long longValueOf(Integer integer)
            {
                return integer.longValue();
            }
        });
        Assert.assertEquals(10, actual);
    }

    @Test
    public void sumFloat()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        double actual = map.sumOfFloat(new FloatFunction<Integer>()
        {
            public float floatValueOf(Integer integer)
            {
                return integer.floatValue();
            }
        });
        Assert.assertEquals(10.0d, actual, 0.01);
    }

    @Test
    public void sumDouble()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        double actual = map.sumOfDouble(new DoubleFunction<Integer>()
        {
            public double doubleValueOf(Integer integer)
            {
                return integer.doubleValue();
            }
        });
        Assert.assertEquals(10.0d, actual, 0.01);
    }

    @Test
    public void makeString()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        String defaultString = map.makeString();
        Assert.assertEquals(15, defaultString.length());

        String delimitedString = map.makeString("|");
        Assert.assertEquals(13, delimitedString.length());
        Verify.assertContains("|", delimitedString);

        String wrappedString = map.makeString("{", "|", "}");
        Assert.assertEquals(15, wrappedString.length());
        Verify.assertContains("|", wrappedString);
        Assert.assertTrue(wrappedString.startsWith("{"));
        Assert.assertTrue(wrappedString.endsWith("}"));
    }

    @Test
    public void min()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Assert.assertEquals(Integer.valueOf(1), map.min());
        Assert.assertEquals(Integer.valueOf(1), map.min(Comparators.<Integer>naturalOrder()));
    }

    @Test
    public void max()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Assert.assertEquals(Integer.valueOf(4), map.max());
        Assert.assertEquals(Integer.valueOf(4), map.max(Comparators.<Integer>naturalOrder()));
    }

    @Test
    public void minBy()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Assert.assertEquals(Integer.valueOf(1), map.minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Assert.assertEquals(Integer.valueOf(4), map.maxBy(Functions.getToString()));
    }

    @Test
    public void reject_value()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Verify.assertContainsAll(map.reject(Predicates.lessThan(3)).toSet(), 3, 4);
        Verify.assertContainsAll(map.reject(Predicates.lessThan(3), UnifiedSet.<Integer>newSet()), 3, 4);
    }

    @Test
    public void rejectWith_value()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Verify.assertContainsAll(map.rejectWith(Predicates2.<Integer>lessThan(), 3, UnifiedSet.<Integer>newSet()), 3, 4);
    }

    @Test
    public void select_value()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Verify.assertContainsAll(map.select(Predicates.lessThan(3)).toSet(), 1, 2);
        Verify.assertContainsAll(map.select(Predicates.lessThan(3), UnifiedSet.<Integer>newSet()), 1, 2);
    }

    @Test
    public void selectWith_value()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Verify.assertContainsAll(map.selectWith(Predicates2.<Integer>lessThan(), 3, UnifiedSet.<Integer>newSet()), 1, 2);
    }

    @Test
    public void partition_value()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues(
                "A", 1,
                "B", 2,
                "C", 3,
                "D", 4);
        PartitionIterable<Integer> partition = map.partition(IntegerPredicates.isEven());
        Assert.assertEquals(iSet(4, 2), partition.getSelected().toSet());
        Assert.assertEquals(iSet(3, 1), partition.getRejected().toSet());
    }

    @Test
    public void selectInstancesOf_value()
    {
        MapIterable<String, Number> map = this.<String, Number>newMapWithKeysValues("1", 1, "2", 2.0, "3", 3, "4", 4.0);
        Assert.assertEquals(iBag(1, 3), map.selectInstancesOf(Integer.class).toBag());
    }

    @Test
    public void toArray()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4);

        Object[] array = map.toArray();
        Verify.assertSize(4, array);
        Integer[] array2 = map.toArray(new Integer[0]);
        Verify.assertSize(4, array2);
        Integer[] array3 = map.toArray(new Integer[4]);
        Verify.assertSize(4, array3);
        Integer[] array4 = map.toArray(new Integer[5]);
        Verify.assertSize(5, array4);
    }

    @Test
    public void zip()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        List<Object> nulls = Collections.nCopies(map.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(map.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(map.size() - 1, null);

        RichIterable<Pair<String, Object>> pairs = map.zip(nulls);
        Assert.assertEquals(
                map.toSet(),
                pairs.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(
                nulls,
                pairs.collect(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        RichIterable<Pair<String, Object>> pairsPlusOne = map.zip(nullsPlusOne);
        Assert.assertEquals(
                map.toSet(),
                pairsPlusOne.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(nulls, pairsPlusOne.collect(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        RichIterable<Pair<String, Object>> pairsMinusOne = map.zip(nullsMinusOne);
        Assert.assertEquals(map.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(map.valuesView().containsAllIterable(pairsMinusOne.collect(Functions.<String>firstOfPair()).toSet()));

        Assert.assertEquals(
                map.zip(nulls).toSet(),
                map.zip(nulls, UnifiedSet.<Pair<String, Object>>newSet()));
    }

    @Test
    public void zipWithIndex()
    {
        MapIterable<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        RichIterable<Pair<String, Integer>> pairs = map.zipWithIndex();

        Assert.assertEquals(
                map.toSet(),
                pairs.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(
                Interval.zeroTo(map.size() - 1).toSet(),
                pairs.collect(Functions.<Integer>secondOfPair(), UnifiedSet.<Integer>newSet()));

        Assert.assertEquals(
                map.zipWithIndex().toSet(),
                map.zipWithIndex(UnifiedSet.<Pair<String, Integer>>newSet()));
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
        RichIterable<Integer> collection = this.newMapWithKeysValues(1, 1, 2, 2, 3, 3);
        MapIterable<String, AtomicInteger> aggregation = collection.aggregateInPlaceBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(1, aggregation.get("1").intValue());
        Assert.assertEquals(2, aggregation.get("2").intValue());
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
        RichIterable<Integer> collection = this.newMapWithKeysValues(1, 1, 2, 2, 3, 3);
        MapIterable<String, Integer> aggregation = collection.aggregateBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(1, aggregation.get("1").intValue());
        Assert.assertEquals(2, aggregation.get("2").intValue());
        Assert.assertEquals(3, aggregation.get("3").intValue());
    }

    @Test
    public void keyValuesView()
    {
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "A", 2, "B", 3, "C", 4, "D");
        MutableSet<Pair<Integer, String>> keyValues = map.keyValuesView().toSet();
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair(1, "A"), Tuples.pair(2, "B"), Tuples.pair(3, "C"), Tuples.pair(4, "D")), keyValues);
    }

    @Test
    public void nullCollisionWithCastInEquals()
    {
        Assume.assumeThat(this.newMap(), not(instanceOf(SortedMap.class)));
        Assume.assumeThat(this.newMap(), not(instanceOf(ConcurrentMap.class)));
        MapIterable<IntegerWithCast, String> mutableMap = this.newMapWithKeysValues(
                new IntegerWithCast(0), "Test 2",
                new IntegerWithCast(0), "Test 3",
                null, "Test 1");
        Assert.assertEquals(
                this.newMapWithKeysValues(
                        new IntegerWithCast(0), "Test 3",
                        null, "Test 1"),
                mutableMap);
        Assert.assertEquals("Test 3", mutableMap.get(new IntegerWithCast(0)));
        Assert.assertEquals("Test 1", mutableMap.get(null));
    }
}
