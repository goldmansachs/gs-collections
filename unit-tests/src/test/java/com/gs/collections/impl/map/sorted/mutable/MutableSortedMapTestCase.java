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

package com.gs.collections.impl.map.sorted.mutable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.MapIterableTestCase;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.immutable.ImmutableTreeMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Key;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * Abstract JUnit TestCase for {@link MutableSortedMap}s.
 */
public abstract class MutableSortedMapTestCase extends MapIterableTestCase
{
    static final Comparator<Integer> REV_INT_ORDER = Comparators.reverseNaturalOrder();

    public abstract <K, V> MutableSortedMap<K, V> newMap(Comparator<? super K> comparator);

    public abstract <K, V> MutableSortedMap<K, V> newMapWithKeyValue(Comparator<? super K> comparator, K key, V value);

    public abstract <K, V> MutableSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator, K key1, V value1, K key2, V value2);

    public abstract <K, V> MutableSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3);

    public abstract <K, V> MutableSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    @Override
    protected abstract <K, V> MutableSortedMap<K, V> newMap();

    protected abstract <K, V> MutableSortedMap<K, V> newMapWithKeyValue(K key, V value);

    @Override
    protected abstract <K, V> MutableSortedMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2);

    @Override
    protected abstract <K, V> MutableSortedMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3);

    @Override
    protected abstract <K, V> MutableSortedMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    @Test
    public void testNewEmpty()
    {
        MutableSortedMap<Integer, Integer> map = this.newMapWithKeysValues(1, 1, 2, 2);
        MutableSortedMap<Integer, Integer> revMap = this.newMapWithKeysValues(Comparators.<Integer>reverseNaturalOrder(), 1, 1, 2, 2);
        Verify.assertEmpty(map.newEmpty());
        Verify.assertEmpty(revMap.newEmpty());
        Assert.assertEquals(Comparators.<Integer>reverseNaturalOrder(), revMap.newEmpty().comparator());
    }

    @Test
    public void testNewMap()
    {
        MutableSortedMap<Integer, Integer> map = this.newMap();
        Verify.assertEmpty(map);

        MutableSortedMap<Integer, Integer> revMap = this.newMap(REV_INT_ORDER);
        Verify.assertEmpty(revMap);
    }

    @Test
    public void toImmutable()
    {
        MutableSortedMap<Integer, String> sortedMap = this.newMapWithKeyValue(1, "One");
        ImmutableSortedMap<Integer, String> result = sortedMap.toImmutable();
        Verify.assertSize(1, result.castToSortedMap());
        Assert.assertEquals("One", result.get(1));
        Verify.assertInstanceOf(ImmutableTreeMap.class, result);
    }

    @Test
    public void testNewMapWithKeyValue()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeyValue(1, "One");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "One"), map);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeyValue(REV_INT_ORDER, 4, "Four");
        Verify.assertNotEmpty(revMap);
        Verify.assertListsEqual(FastList.<String>newListWith("Four"), revMap.valuesView().toList());
    }

    @Test
    public void newMapWith_2()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "One", 2, "Two"), map);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 3, "Three", 4, "Four");
        Verify.assertNotEmpty(revMap);
        Verify.assertListsEqual(FastList.<String>newListWith("Four", "Three"), revMap.valuesView().toList());
    }

    @Test
    public void newMapWith_3()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "One", 2, "Two", 3, "Three"), map);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 3, "Three", 2, "Two", 4, "Four");
        Verify.assertNotEmpty(revMap);
        Verify.assertListsEqual(FastList.<String>newListWith("Four", "Three", "Two"), revMap.valuesView().toList());
    }

    @Test
    public void newMapWith_4()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four"), map);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 3, "Three", 2, "Two", 4, "Four");
        Verify.assertNotEmpty(revMap);
        Verify.assertListsEqual(FastList.<String>newListWith("Four", "Three", "Two", "One"), revMap.valuesView().toList());
    }

    @Test
    public void with()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2");
        Verify.assertMapsEqual(TreeSortedMap.newMapWith(1, "1", 2, "2", 3, "3"), map.with(Tuples.pair(3, "3")));
        Verify.assertMapsEqual(TreeSortedMap.newMapWith(1, "1", 2, "2", 3, "3", 4, "4"), map.with(Tuples.pair(3, "3"), Tuples.pair(4, "4")));

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "1", 2, "2");
        Verify.assertMapsEqual(TreeSortedMap.newMap(REV_INT_ORDER).with(1, "1", 2, "2", 3, "3"), revMap.with(Tuples.pair(3, "3")));
        Verify.assertMapsEqual(TreeSortedMap.newMap(REV_INT_ORDER).with(1, "1", 2, "2", 3, "3", 4, "4"), revMap.with(Tuples.pair(3, "3"), Tuples.pair(4, "4")));
    }

    @Override
    @Test
    public void forEach()
    {
        super.forEach();

        MutableList<String> list = Lists.mutable.of();
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 3, "Three", 2, "Two", 4, "Four");
        map.forEach(CollectionAddProcedure.<String>on(list));
        Verify.assertListsEqual(FastList.<String>newListWith("One", "Two", "Three", "Four"), list);

        MutableList<String> list2 = Lists.mutable.of();
        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 3, "Three", 2, "Two", 4, "Four");
        revMap.forEach(CollectionAddProcedure.<String>on(list2));
        Verify.assertListsEqual(FastList.<String>newListWith("Four", "Three", "Two", "One"), list2);
    }

    @Override
    @Test
    public void forEachWith()
    {
        super.forEachWith();
        final MutableList<Integer> list = Lists.mutable.of();
        MutableSortedMap<Integer, Integer> map = this.newMapWithKeysValues(-1, 1, -2, 2, -3, 3, -4, 4);
        map.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                list.add(argument1 + argument2);
            }
        }, 10);
        Verify.assertListsEqual(FastList.<Integer>newListWith(14, 13, 12, 11), list);
    }

    @Test
    public void forEachWith_reverse()
    {
        final MutableList<Integer> list2 = Lists.mutable.of();
        MutableSortedMap<Integer, Integer> revMap = this.newMapWithKeysValues(REV_INT_ORDER, -1, 1, -2, 2, -3, 3, -4, 4);
        revMap.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                list2.add(argument1 + argument2);
            }
        }, 10);
        Verify.assertListsEqual(FastList.<Integer>newListWith(11, 12, 13, 14), list2);
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        super.forEachWithIndex();

        final MutableList<String> list = Lists.mutable.of();
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        map.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                list.add(value);
                list.add(String.valueOf(index));
            }
        });
        Verify.assertListsEqual(FastList.<String>newListWith("One", "0", "Two", "1", "Three", "2", "Four", "3"), list);
    }

    @Test
    public void forEachWithIndex_reverse()
    {
        final MutableList<String> list2 = Lists.mutable.of();
        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 2, "Two", 3, "Three", 4, "Four");
        revMap.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                list2.add(value);
                list2.add(String.valueOf(index));
            }
        });
        Verify.assertListsEqual(FastList.<String>newListWith("Four", "0", "Three", "1", "Two", "2", "One", "3"), list2);
    }

    @Test
    public void forEachKeyValue()
    {
        final MutableList<String> result = Lists.mutable.of();
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 2, "Two", 3, "Three");
        map.forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                result.add(key + value);
            }
        });
        Verify.assertListsEqual(FastList.<String>newListWith("3Three", "2Two", "1One"), result);
    }

    @Override
    @Test
    public void forEachKey()
    {
        super.forEachKey();
        MutableList<Integer> result = Lists.mutable.of();
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(REV_INT_ORDER, 1, "1", 2, "2", 3, "3");
        map.forEachKey(CollectionAddProcedure.<Integer>on(result));
        Verify.assertListsEqual(FastList.<Integer>newListWith(3, 2, 1), result);
    }

    @Override
    @Test
    public void collectValues()
    {
        super.collectValues();
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 2, "Two", 3, "Three");
        MutableSortedMap<Integer, String> actual = map.transformValues(new Function2<Integer, String, String>()
        {
            public String value(Integer argument1, String argument2)
            {
                return new StringBuilder(argument2).reverse().toString();
            }
        });
        Assert.assertEquals(TreeSortedMap.<Integer, String>
                newMap(REV_INT_ORDER).with(1, "enO", 2, "owT", 3, "eerhT"), actual);
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        super.zipWithIndex();
        MutableSortedMap<String, String> map = this.newMapWithKeysValues("1", "One", "2", "Two", "3", "Three");

        MutableList<Pair<String, Integer>> pairs = map.zipWithIndex();

        Verify.assertListsEqual(
                map.toList(),
                pairs.transform(Functions.<String>firstOfPair()));

        Verify.assertListsEqual(
                Interval.zeroTo(map.size() - 1),
                pairs.transform(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(
                map.zipWithIndex().toSet(),
                map.zipWithIndex(UnifiedSet.<Pair<String, Integer>>newSet()));
    }

    @Override
    @Test
    public void zip()
    {
        super.zip();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("A", 1, "B", 2, "C", 3);
        MutableList<Pair<Integer, String>> zip = map.zip(FastList.newListWith("One", "Two", "Three"));
        Verify.assertListsEqual(FastList.newListWith(Tuples.pair(1, "One"), Tuples.pair(2, "Two"), Tuples.pair(3, "Three")), zip);
    }

    @Override
    @Test
    public void select_value()
    {
        super.select_value();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "A", 1, "B", 2, "C", 3);
        Verify.assertListsEqual(FastList.newListWith(2, 1), map.filter(Predicates.lessThan(3)));
    }

    @Override
    @Test
    public void reject_value()
    {
        super.reject_value();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "A", 1, "B", 2, "C", 3);
        Verify.assertListsEqual(FastList.newListWith(2, 1), map.filterNot(Predicates.greaterThan(2)));
    }

    @Override
    @Test
    public void partition_value()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(
                Comparators.<String>reverseNaturalOrder(),
                "A", 1,
                "B", 2,
                "C", 3,
                "D", 4);
        PartitionMutableList<Integer> partition = map.partition(IntegerPredicates.isEven());
        Assert.assertEquals(iList(4, 2), partition.getSelected());
        Assert.assertEquals(iList(3, 1), partition.getRejected());
    }

    @Override
    @Test
    public void collect_value()
    {
        super.collect_value();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "A", 1, "B", 2, "C", 3);
        MutableList<String> collect = map.transform(Functions.getToString());
        Verify.assertListsEqual(FastList.<String>newListWith("3", "2", "1"), collect);
    }

    @Override
    @Test
    public void flatten_value()
    {
        super.flatten_value();
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(REV_INT_ORDER, 1, "cd", 2, "ab");

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
        Verify.assertListsEqual(FastList.newListWith('a', 'b', 'c', 'd'), map.flatTransform(function));
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "1", 1, "2", 2, "3", 3);
        MutableMap<Integer, String> collect = map.transform(new Function2<String, Integer, Pair<Integer, String>>()
        {
            public Pair<Integer, String> value(String argument1, Integer argument2)
            {
                return Tuples.pair(argument2.intValue(), String.valueOf(argument2));
            }
        });
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "1", 2, "2", 3, "3"), collect);
    }

    @Override
    @Test
    public void select()
    {
        super.select();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "1", 1, "2", 3, "3", 2, "4", 1);
        MutableSortedMap<String, Integer> select = map.filter(new Predicate2<String, Integer>()
        {
            public boolean accept(String argument1, Integer argument2)
            {
                return 1 != argument2;
            }
        });
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("2", 3, "3", 2), select);
        Verify.assertListsEqual(FastList.newListWith("3", "2"), select.keySet().toList());
    }

    @Override
    @Test
    public void reject()
    {
        super.reject();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "1", 1, "2", 3, "3", 2, "4", 1);
        MutableSortedMap<String, Integer> select = map.filterNot(new Predicate2<String, Integer>()
        {
            public boolean accept(String argument1, Integer argument2)
            {
                return 1 == argument2;
            }
        });
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("2", 3, "3", 2), select);
        Verify.assertListsEqual(FastList.newListWith("3", "2"), select.keySet().toList());
    }

    @Override
    @Test
    public void collectIf()
    {
        super.collectIf();
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "1", 4, "2", 3, "3", 2, "4", 1);
        MutableList<String> collect = map.transformIf(Predicates.greaterThan(1), Functions.getToString());
        Verify.assertListsEqual(FastList.newListWith("2", "3", "4"), collect);
    }

    @Test
    public void clear()
    {
        MutableSortedMap<Integer, Object> map =
                this.<Integer, Object>newMapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        map.clear();
        Verify.assertEmpty(map);
    }

    @Test
    public void iterator()
    {
        MutableSortedMap<Integer, Integer> map = this.newMapWithKeysValues(REV_INT_ORDER, -1, 1, -2, 2, -3, 3);
        Iterator<Integer> iterator = map.iterator();
        Assert.assertTrue(iterator.hasNext());
        for (int i = 1; i < 4; ++i)
        {
            Assert.assertEquals(i, iterator.next().intValue());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void removeObject()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        map.remove("Two");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void removeFromEntrySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertTrue(map.entrySet().remove(ImmutableEntry.of("Two", 2)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);

        Assert.assertFalse(map.entrySet().remove(ImmutableEntry.of("Four", 4)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);

        Assert.assertFalse(map.entrySet().remove(null));
    }

    @Test
    public void removeAllFromEntrySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertTrue(map.entrySet().removeAll(FastList.newListWith(
                ImmutableEntry.of("One", 1),
                ImmutableEntry.of("Three", 3))));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("Two", 2), map);

        Assert.assertFalse(map.entrySet().removeAll(FastList.newListWith(ImmutableEntry.of("Four", 4))));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("Two", 2), map);

        Assert.assertFalse(map.entrySet().remove(null));
    }

    @Test
    public void retainAllFromEntrySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.entrySet().retainAll(FastList.newListWith(
                ImmutableEntry.of("One", 1),
                ImmutableEntry.of("Two", 2),
                ImmutableEntry.of("Three", 3),
                ImmutableEntry.of("Four", 4))));

        Assert.assertTrue(map.entrySet().retainAll(FastList.newListWith(
                ImmutableEntry.of("One", 1),
                ImmutableEntry.of("Three", 3),
                ImmutableEntry.of("Four", 4))));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void clearEntrySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        map.entrySet().clear();
        Verify.assertEmpty(map);
    }

    @Test
    public void entrySetEqualsAndHashCode()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Verify.assertEqualsAndHashCode(
                UnifiedSet.newSetWith(
                        ImmutableEntry.of("One", 1),
                        ImmutableEntry.of("Two", 2),
                        ImmutableEntry.of("Three", 3)),
                map.entrySet());
    }

    @Test
    public void keySet()
    {
        MutableSortedMap<Integer, Integer> map = this.newMapWithKeysValues(1, -1, 2, -2, 3, -3);
        Verify.assertListsEqual(FastList.newListWith(1, 2, 3), map.keySet().toList());
        Verify.assertInstanceOf(MutableSet.class, map.keySet());

        MutableSortedMap<Integer, Integer> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, -1, 2, -2, 3, -3);
        Verify.assertListsEqual(FastList.newListWith(3, 2, 1), revMap.keySet().toList());
    }

    @Test
    public void removeFromKeySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.keySet().remove("Four"));

        Assert.assertTrue(map.keySet().remove("Two"));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void removeAllFromKeySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.keySet().removeAll(FastList.newListWith("Four")));

        Assert.assertTrue(map.keySet().removeAll(FastList.newListWith("Two", "Four")));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void retainAllFromKeySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.keySet().retainAll(FastList.newListWith("One", "Two", "Three", "Four")));

        Assert.assertTrue(map.keySet().retainAll(FastList.newListWith("One", "Three")));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void clearKeySet()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        map.keySet().clear();
        Verify.assertEmpty(map);
    }

    @Test
    public void keySetEqualsAndHashCode()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Verify.assertEqualsAndHashCode(TreeSortedSet.newSetWith("One", "Two", "Three"), map.keySet());
    }

    @Test
    public void keySetToArray()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        MutableList<String> expected = FastList.newListWith("One", "Two", "Three").toSortedList();
        MutableSet<String> keySet = map.keySet();
        Verify.assertListsEqual(expected, FastList.newListWith(keySet.toArray()).toSortedList());
        Assert.assertEquals(expected, FastList.newListWith(keySet.toArray(new String[keySet.size()])).toSortedList());
    }

    @Test
    public void removeFromValues()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().remove(4));

        Assert.assertTrue(map.values().remove(2));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void removeNullFromValues()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().remove(null));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Two", 2, "Three", 3), map);
        map.put("Four", null);
        Assert.assertTrue(map.values().remove(null));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Two", 2, "Three", 3), map);
    }

    @Test
    public void removeAllFromValues()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().removeAll(FastList.newListWith(4)));

        Assert.assertTrue(map.values().removeAll(FastList.newListWith(2, 4)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void retainAllFromValues()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().retainAll(FastList.newListWith(1, 2, 3, 4)));

        Assert.assertTrue(map.values().retainAll(FastList.newListWith(1, 3)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void put()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        Assert.assertNull(map.put(3, "Three"));
        Assert.assertEquals(TreeSortedMap.newMapWith(1, "One", 2, "Two", 3, "Three"), map);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 2, "Two");
        Assert.assertNull(revMap.put(0, "Zero"));
        Assert.assertEquals(TreeSortedMap.<Integer, String>newMap(REV_INT_ORDER).with(0, "Zero", 1, "One", 2, "Two"), revMap);
    }

    @Test
    public void putAll()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "2");
        MutableSortedMap<Integer, String> toAdd = this.newMapWithKeysValues(2, "Two", 3, "Three");
        map.putAll(toAdd);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "One", 2, "Two", 3, "Three"), map);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 2, "2");
        revMap.putAll(toAdd);
        Assert.assertEquals(TreeSortedMap.<Integer, String>newMap(REV_INT_ORDER).with(1, "One", 2, "Two", 3, "Three"), revMap);
    }

    @Test
    public void putAllFromCollection()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        MutableList<Integer> toAdd = FastList.newListWith(2, 3);
        map.transformKeysAndValues(toAdd, Functions.getIntegerPassThru(), Functions.getToString());
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "1", 2, "Two");
        revMap.transformKeysAndValues(toAdd, Functions.getIntegerPassThru(), Functions.getToString());
        Assert.assertEquals(TreeSortedMap.<Integer, String>newMap(REV_INT_ORDER).with(1, "1", 2, "2", 3, "3"), revMap);
    }

    @Test
    public void removeKey()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");

        Assert.assertEquals("1", map.removeKey(1));
        Verify.assertSize(1, map);
        Verify.denyContainsKey(1, map);

        Assert.assertNull(map.removeKey(42));
        Verify.assertSize(1, map);

        Assert.assertEquals("Two", map.removeKey(2));
        Verify.assertEmpty(map);
    }

    @Test
    public void getIfAbsentPut()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentPut(4, new PassThruFunction0<String>("4")));
        Assert.assertEquals("3", map.getIfAbsentPut(3, new PassThruFunction0<String>("3")));
        Verify.assertContainsKeyValue(4, "4", map);
    }

    @Test
    public void getIfAbsentPutWith()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentPutWith(4, Functions.getToString(), 4));
        Assert.assertEquals("3", map.getIfAbsentPutWith(3, Functions.getToString(), 3));
        Verify.assertContainsKeyValue(4, "4", map);
    }

    @Test
    public void getKeysAndGetValues()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertContainsAll(map.keySet(), 1, 2, 3);
        Verify.assertContainsAll(map.values(), "1", "2", "3");
    }

    @Test
    public void testEquals()
    {
        MutableSortedMap<Integer, String> map1 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableSortedMap<Integer, String> map2 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableSortedMap<Integer, String> map3 = this.newMapWithKeysValues(2, "2", 3, "3", 4, "4");
        MutableSortedMap<Integer, String> revMap1 = this.newMapWithKeysValues(REV_INT_ORDER, 1, "1", 2, "2", 3, "3");
        MutableSortedMap<Integer, String> revMap3 = this.newMapWithKeysValues(REV_INT_ORDER, 2, "2", 3, "3", 4, "4");

        Verify.assertMapsEqual(map1, map2);
        Verify.assertMapsEqual(revMap1, map2);
        Verify.assertMapsEqual(revMap3, map3);

        Verify.assertNotEquals(map2, map3);
        Verify.assertNotEquals(revMap1, revMap3);
        Verify.assertNotEquals(map1, revMap3);
    }

    @Test
    public void testHashCode()
    {
        MutableSortedMap<Integer, String> map1 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableSortedMap<Integer, String> map2 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertEqualsAndHashCode(map1, map2);
    }

    @Test
    public void equalsAndHashCode()
    {
        Map<Integer, String> hashMap = new HashMap<Integer, String>();
        hashMap.put(1, "One");
        hashMap.put(2, "Two");

        MutableSortedMap<Integer, String> mutableMap = this.newMapWithKeysValues(1, "One", 2, "Two");

        Verify.assertEqualsAndHashCode(hashMap, mutableMap);
    }

    @Test
    public void serialization()
    {
        MutableSortedMap<Integer, String> original = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableSortedMap<Integer, String> copy = SerializeTestHelper.serializeDeserialize(original);
        Verify.assertMapsEqual(original, copy);

        MutableSortedMap<Integer, String> revMap = this.newMapWithKeysValues(REV_INT_ORDER, 1, "One", 2, "Two");
        MutableSortedMap<Integer, String> deserialized = SerializeTestHelper.serializeDeserialize(revMap);
        Verify.assertMapsEqual(revMap, deserialized);
        Verify.assertListsEqual(FastList.newListWith(2, 1), deserialized.keySet().toList());
    }

    @Test
    public void containsValue()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertTrue(map.containsValue("1"));
        Assert.assertFalse(map.containsValue("4"));
    }

    @Test
    public void containsKey()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertTrue(map.containsKey(1));
        Assert.assertFalse(map.containsKey(4));
    }

    @Test
    public void newEmpty()
    {
        MutableSortedMap<Integer, Integer> map = this.newMapWithKeysValues(1, 1, 2, 2);
        Verify.assertEmpty(map.newEmpty());
    }

    @Test
    public void keysView()
    {
        MutableList<Integer> keys = this.newMapWithKeysValues(1, 1, 2, 2).keysView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2), keys);
    }

    @Test
    public void valuesView()
    {
        MutableList<Integer> values = this.newMapWithKeysValues(1, 1, 2, 2).valuesView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2), values);
    }

    @Test
    public void asUnmodifiable()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                MutableSortedMapTestCase.this.newMapWithKeysValues(1, 1, 2, 2).asUnmodifiable().put(3, 3);
            }
        });

        Verify.assertInstanceOf(UnmodifiableTreeMap.class, this.newMapWithKeysValues(1, "1", 2, "2").asUnmodifiable());
    }

    @Test
    public void asSynchronized()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two").asSynchronized();
        Verify.assertInstanceOf(SynchronizedSortedMap.class, map);
    }

    @Test
    public void testClone()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        MutableSortedMap<Integer, String> clone = map.clone();
        Assert.assertNotSame(map, clone);
        Verify.assertEqualsAndHashCode(map, clone);
    }

    @Test
    public void keyPreservation()
    {
        Key key = new Key("key");

        Key duplicateKey1 = new Key("key");
        MutableSortedMap<Key, Integer> map1 = this.newMapWithKeysValues(key, 1, duplicateKey1, 2);
        Verify.assertSize(1, map1);
        Verify.assertContainsKeyValue(key, 2, map1);
        Assert.assertSame(key, map1.keysView().getFirst());

        Key duplicateKey2 = new Key("key");
        MutableSortedMap<Key, Integer> map2 = this.newMapWithKeysValues(key, 1, duplicateKey1, 2, duplicateKey2, 3);
        Verify.assertSize(1, map2);
        Verify.assertContainsKeyValue(key, 3, map2);
        Assert.assertSame(key, map1.keysView().getFirst());

        Key duplicateKey3 = new Key("key");
        MutableSortedMap<Key, Integer> map3 = this.newMapWithKeysValues(key, 1, new Key("not a dupe"), 2, duplicateKey3, 3);
        Verify.assertSize(2, map3);
        Verify.assertContainsAllKeyValues(map3, key, 3, new Key("not a dupe"), 2);
        Assert.assertSame(key, map3.keysView().find(Predicates.equal(key)));

        Key duplicateKey4 = new Key("key");
        MutableSortedMap<Key, Integer> map4 = this.newMapWithKeysValues(key, 1, new Key("still not a dupe"), 2, new Key("me neither"), 3, duplicateKey4, 4);
        Verify.assertSize(3, map4);
        Verify.assertContainsAllKeyValues(map4, key, 4, new Key("still not a dupe"), 2, new Key("me neither"), 3);
        Assert.assertSame(key, map4.keysView().find(Predicates.equal(key)));

        MutableSortedMap<Key, Integer> map5 = this.newMapWithKeysValues(key, 1, duplicateKey1, 2, duplicateKey3, 3, duplicateKey4, 4);
        Verify.assertSize(1, map5);
        Verify.assertContainsKeyValue(key, 4, map5);
        Assert.assertSame(key, map5.keysView().getFirst());
    }

    @Test
    public void firstKey()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Four", 4);
        Assert.assertEquals("Four", map.firstKey());

        MutableSortedMap<String, Integer> revMap = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "One", 1, "Two", 2, "Four", 4);
        Assert.assertEquals("Two", revMap.firstKey());

        final MutableSortedMap<Object, Object> emptyMap = this.newMap();
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                emptyMap.firstKey();
            }
        });
    }

    @Test
    public void lastKey()
    {
        MutableSortedMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Four", 4);
        Assert.assertEquals("Two", map.lastKey());

        MutableSortedMap<String, Integer> revMap = this.newMapWithKeysValues(Comparators.<String>reverseNaturalOrder(),
                "One", 1, "Two", 2, "Four", 4);
        Assert.assertEquals("Four", revMap.lastKey());

        final MutableSortedMap<Object, Object> emptyMap = this.newMap();
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                emptyMap.lastKey();
            }
        });
    }

    @Test
    public void headMap()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        final MutableSortedMap<Integer, String> subMap = map.headMap(3);

        Verify.assertMapsEqual(TreeSortedMap.newMapWith(1, "One", 2, "Two"), subMap);
        Verify.assertListsEqual(FastList.newListWith(1, 2), subMap.keySet().toList());

        subMap.put(0, "Zero");
        Verify.assertContainsKeyValue(0, "Zero", map);

        subMap.removeKey(2);
        Verify.assertNotContainsKey(2, map);

        map.clear();
        Verify.assertEmpty(subMap);

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                subMap.put(4, "Illegal");
            }
        });
    }

    @Test
    public void tailMap()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        final MutableSortedMap<Integer, String> subMap = map.tailMap(2);

        Verify.assertMapsEqual(TreeSortedMap.newMapWith(2, "Two", 3, "Three", 4, "Four"), subMap);
        Verify.assertListsEqual(FastList.newListWith(2, 3, 4), subMap.keySet().toList());

        subMap.put(5, "Five");
        Verify.assertContainsKeyValue(5, "Five", map);

        subMap.removeKey(2);
        Verify.assertNotContainsKey(2, map);

        map.clear();
        Verify.assertEmpty(subMap);

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                subMap.put(1, "Illegal");
            }
        });
    }

    @Test
    public void subMap()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        final MutableSortedMap<Integer, String> subMap = map.subMap(2, 4);

        Verify.assertMapsEqual(TreeSortedMap.newMapWith(3, "Three", 2, "Two"), subMap);
        Verify.assertListsEqual(FastList.newListWith(2, 3), subMap.keySet().toList());

        map.clear();
        Verify.assertEmpty(subMap);
        Verify.assertEmpty(map);

        subMap.put(2, "Two");
        map.put(3, "Three");
        Verify.assertContainsKeyValue(2, "Two", map);
        Verify.assertContainsKeyValue(3, "Three", subMap);

        subMap.removeKey(2);
        Verify.assertNotContainsKey(2, map);

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                subMap.put(4, "Illegal");
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                subMap.put(1, "Illegal");
            }
        });
    }

    @Test
    public void testToString()
    {
        MutableSortedMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        Assert.assertEquals("{1=One, 2=Two}", map.toString());
    }
}
