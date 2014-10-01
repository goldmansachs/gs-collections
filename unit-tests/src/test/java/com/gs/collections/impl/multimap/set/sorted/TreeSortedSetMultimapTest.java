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

package com.gs.collections.impl.multimap.set.sorted;

import java.util.Collections;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link TreeSortedSetMultimap}.
 */
public class TreeSortedSetMultimapTest extends AbstractMutableSortedSetMultimapTestCase
{
    @Override
    public <K, V> TreeSortedSetMultimap<K, V> newMultimap()
    {
        return TreeSortedSetMultimap.newMultimap();
    }

    @Override
    public <K, V> TreeSortedSetMultimap<K, V> newMultimapWithKeyValue(K key, V value)
    {
        TreeSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key, value);
        return mutableMultimap;
    }

    @Override
    public <K, V> TreeSortedSetMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        TreeSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        return mutableMultimap;
    }

    @Override
    public <K, V> TreeSortedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        TreeSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        return mutableMultimap;
    }

    @Override
    public <K, V> TreeSortedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        TreeSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        mutableMultimap.put(key4, value4);
        return mutableMultimap;
    }

    @SafeVarargs
    @Override
    public final <K, V> TreeSortedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return TreeSortedSetMultimap.newMultimap(pairs);
    }

    @Override
    protected <K, V> TreeSortedSetMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable)
    {
        return TreeSortedSetMultimap.newMultimap(inputIterable);
    }

    @SafeVarargs
    @Override
    protected final <V> TreeSortedSet<V> createCollection(V... args)
    {
        return TreeSortedSet.newSetWith(args);
    }

    @Test
    public void testEmptyConstructor()
    {
        MutableSortedSetMultimap<Integer, Integer> map = TreeSortedSetMultimap.newMultimap();
        for (int i = 1; i < 6; ++i)
        {
            for (int j = 1; j < i + 1; ++j)
            {
                map.put(i, j);
            }
        }
        Verify.assertSize(5, map.keysView().toList());
        for (int i = 1; i < 6; ++i)
        {
            Verify.assertSortedSetsEqual(TreeSortedSet.newSet(Interval.oneTo(i)), map.get(i));
        }
    }

    @Test
    public void testComparatorConstructors()
    {
        MutableSortedSetMultimap<Boolean, Integer> revMap = TreeSortedSetMultimap.newMultimap(Collections.<Integer>reverseOrder());
        for (int i = 1; i < 10; ++i)
        {
            revMap.put(IntegerPredicates.isOdd().accept(i), i);
        }
        Verify.assertSize(2, revMap.keysView().toList());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 9, 7, 5, 3, 1), revMap.get(Boolean.TRUE));
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 8, 6, 4, 2), revMap.get(Boolean.FALSE));
        MutableSortedSetMultimap<Boolean, Integer> revMap2 = TreeSortedSetMultimap.newMultimap(revMap);
        Verify.assertMapsEqual(revMap2.toMap(), revMap.toMap());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 9, 7, 5, 3, 1), revMap2.get(Boolean.TRUE));
    }

    @Test
    public void testMultimapConstructor()
    {
        MutableSetMultimap<Integer, Integer> map = UnifiedSetMultimap.newMultimap();
        TreeSortedSetMultimap<Integer, Integer> map2 = TreeSortedSetMultimap.newMultimap();
        for (int i = 1; i < 6; ++i)
        {
            map.putAll(i, Interval.oneTo(i));
            map2.putAll(i, Interval.oneTo(i));
        }
        TreeSortedSetMultimap<Integer, Integer> sortedMap = TreeSortedSetMultimap.newMultimap(map);
        TreeSortedSetMultimap<Integer, Integer> sortedMap2 = TreeSortedSetMultimap.newMultimap(map2);
        for (int i = 1; i < 6; ++i)
        {
            Verify.assertSortedSetsEqual(map.get(i).toSortedSet(), sortedMap.get(i));
            Verify.assertSortedSetsEqual(map.get(i).toSortedSet(), sortedMap2.get(i));
        }
    }

    @Test
    public void testCollection()
    {
        TreeSortedSetMultimap<Integer, Integer> setMultimap = TreeSortedSetMultimap.newMultimap(Collections.<Integer>reverseOrder());
        MutableSortedSet<Integer> collection = setMultimap.createCollection();
        collection.addAll(FastList.newListWith(1, 4, 2, 3, 5));
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 5, 4, 3, 2, 1), collection);
        setMultimap.putAll(1, collection);
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 5, 4, 3, 2, 1), collection);
        setMultimap.put(1, 0);
        Assert.assertEquals(Integer.valueOf(0), setMultimap.get(1).getLast());
        setMultimap.putAll(2, FastList.newListWith(0, 1, 2, 4, 2, 1, 4, 5, 3, 4, 5));
        Verify.assertSortedSetsEqual(setMultimap.get(1), setMultimap.get(2));
    }

    @Test
    public void testNewEmpty()
    {
        TreeSortedSetMultimap<Object, Integer> expected = TreeSortedSetMultimap.newMultimap(Collections.<Integer>reverseOrder());
        TreeSortedSetMultimap<Object, Integer> actual = expected.newEmpty();
        expected.putAll(1, FastList.newListWith(4, 3, 1, 2));
        expected.putAll(2, FastList.newListWith(5, 7, 6, 8));
        actual.putAll(1, FastList.newListWith(4, 3, 1, 2));
        actual.putAll(2, FastList.newListWith(5, 7, 6, 8));
        Verify.assertMapsEqual(expected.toMap(), actual.toMap());
        Verify.assertSortedSetsEqual(expected.get(1), actual.get(1));
        Verify.assertSortedSetsEqual(expected.get(2), actual.get(2));
    }

    @Override
    @Test
    public void serialization()
    {
        TreeSortedSetMultimap<Integer, Integer> map = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        map.putAll(1, FastList.newListWith(1, 2, 3, 4));
        map.putAll(2, FastList.newListWith(2, 3, 4, 5));
        Verify.assertPostSerializedEqualsAndHashCode(map);

        TreeSortedSetMultimap<Integer, Integer> deserialized = SerializeTestHelper.serializeDeserialize(map);
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 1, 2, 3, 4),
                deserialized.get(1));

        deserialized.putAll(3, FastList.newListWith(8, 9, 10));
        Verify.assertListsEqual(FastList.newListWith(10, 9, 8), deserialized.get(3).toList());
    }

    @Override
    public void testClear()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        multimap.clear();
        Verify.assertEmpty(multimap);
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        TreeSortedSetMultimap<String, Integer> multimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        TreeSortedSetMultimap<String, Integer> selectedMultimap = multimap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        TreeSortedSetMultimap<String, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll("Two", FastList.newListWith(4, 2));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSortedSetsEqual(expectedMultimap.get("Two"), selectedMultimap.get("Two"));
        Assert.assertEquals(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        TreeSortedSetMultimap<String, Integer> multimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        TreeSortedSetMultimap<String, Integer> rejectedMultimap = multimap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        TreeSortedSetMultimap<String, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll("One", FastList.newListWith(3, 1));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
        Verify.assertSortedSetsEqual(expectedMultimap.get("One"), rejectedMultimap.get("One"));
        Assert.assertEquals(expectedMultimap.comparator(), rejectedMultimap.comparator());
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        TreeSortedSetMultimap<Integer, Integer> multimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll(1, FastList.newListWith(4, 3, 1));
        multimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(3, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(4, FastList.newListWith(4, 3, 1));
        TreeSortedSetMultimap<Integer, Integer> selectedMultimap = multimap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        TreeSortedSetMultimap<Integer, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSortedSetsEqual(expectedMultimap.get(2), selectedMultimap.get(2));
        Assert.assertEquals(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        TreeSortedSetMultimap<Integer, Integer> multimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll(1, FastList.newListWith(4, 3, 2, 1));
        multimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        multimap.putAll(4, FastList.newListWith(4, 3, 1));
        TreeSortedSetMultimap<Integer, Integer> selectedMultimap = multimap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 3));
        TreeSortedSetMultimap<Integer, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSortedSetsEqual(expectedMultimap.get(3), selectedMultimap.get(3));
        Assert.assertEquals(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        TreeSortedSetMultimap<String, Integer> multimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        HashBagMultimap<Integer, String> collectedMultimap1 = multimap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value.toString() + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap1 = HashBagMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap1, collectedMultimap1);

        HashBagMultimap<Integer, String> collectedMultimap2 = multimap.collectKeysValues((key, value) -> Tuples.pair(1, value.toString() + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        TreeSortedSetMultimap<String, Integer> multimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        FastListMultimap<String, String> collectedMultimap = multimap.collectValues(value -> value.toString() + "Value");
        FastListMultimap<String, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap.putAll("2", FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap, collectedMultimap);
        Verify.assertListsEqual(expectedMultimap.get("1"), collectedMultimap.get("1"));
        Verify.assertListsEqual(expectedMultimap.get("2"), collectedMultimap.get("2"));
    }
}
