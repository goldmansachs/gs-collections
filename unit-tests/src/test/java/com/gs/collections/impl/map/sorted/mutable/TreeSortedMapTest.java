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

package com.gs.collections.impl.map.sorted.mutable;

import java.util.Comparator;

import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class TreeSortedMapTest extends MutableSortedMapTestCase
{
    @Override
    public <K, V> TreeSortedMap<K, V> newMap()
    {
        return TreeSortedMap.newMap();
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return TreeSortedMap.newMapWith(key, value);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeysValues(
            K key1, V value1, K key2, V value2, K key3,
            V value3)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2, key3, value3);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeysValues(
            K key1, V value1, K key2, V value2, K key3,
            V value3, K key4, V value4)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMap(Comparator<? super K> comparator)
    {
        return TreeSortedMap.newMap(comparator);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeyValue(Comparator<? super K> comparator, K key, V value)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key, value);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator, K key1, V value1, K key2, V value2)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator,
            K key1, V value1, K key2, V value2, K key3,
            V value3)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2, key3, value3);
    }

    @Override
    public <K, V> TreeSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator,
            K key1, V value1, K key2, V value2, K key3,
            V value3, K key4, V value4)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Test
    public void testConstructors()
    {
        UnifiedMap<Integer, String> unifiedMap = UnifiedMap.newWithKeysValues(1, "One", 2, "Two", 3, "Three");
        TreeSortedMap<Integer, String> sortedMap = TreeSortedMap.newMap(unifiedMap);
        TreeSortedMap<Integer, String> revSortedMap = TreeSortedMap.newMap(Comparators.<Integer>reverseNaturalOrder(),
                unifiedMap);

        Verify.assertMapsEqual(unifiedMap, sortedMap);
        Verify.assertMapsEqual(unifiedMap, revSortedMap);
        Verify.assertListsEqual(FastList.newListWith(1, 2, 3), sortedMap.keySet().toList());
        Verify.assertListsEqual(FastList.newListWith(3, 2, 1), revSortedMap.keySet().toList());

        TreeSortedMap<Integer, String> sortedMap2 = TreeSortedMap.newMap(revSortedMap);
        Assert.assertEquals(revSortedMap.comparator(), sortedMap2.comparator());
        Verify.assertMapsEqual(revSortedMap, sortedMap2);
    }

    @Test
    public void newMapWithPairs()
    {
        TreeSortedMap<Integer, Integer> revSortedMap = TreeSortedMap.newMapWith(Comparators.<Integer>reverseNaturalOrder(),
                Tuples.pair(1, 4), Tuples.pair(2, 3), Tuples.pair(3, 2), Tuples.pair(4, 1));

        Verify.assertSize(4, revSortedMap);

        Verify.assertMapsEqual(UnifiedMap.newMapWith(Tuples.pair(1, 4), Tuples.pair(2, 3), Tuples.pair(3, 2), Tuples.pair(4, 1)),
                revSortedMap);
        Verify.assertListsEqual(FastList.newListWith(4, 3, 2, 1), revSortedMap.keySet().toList());
        Verify.assertListsEqual(FastList.newListWith(1, 2, 3, 4), revSortedMap.valuesView().toList());
    }

    @Override
    @Test
    public void testClone()
    {
        super.testClone();
        TreeSortedMap<Integer, Integer> sortedMap = TreeSortedMap.<Integer, Integer>newMapWith(Tuples.pair(1, 4), Tuples.pair(2, 3), Tuples.pair(3, 2), Tuples.pair(4, 1));
        MutableSortedMap<Integer, Integer> clone = sortedMap.clone();
        Assert.assertNotSame(sortedMap, clone);
        Assert.assertEquals(sortedMap, clone);
        sortedMap.removeKey(1);
        Assert.assertTrue(clone.containsKey(1));
    }
}
