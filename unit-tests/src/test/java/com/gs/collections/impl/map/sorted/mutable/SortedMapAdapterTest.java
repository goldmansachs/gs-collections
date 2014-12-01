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

package com.gs.collections.impl.map.sorted.mutable;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SortedMapAdapter}.
 */
public class SortedMapAdapterTest extends MutableSortedMapTestCase
{
    @Override
    public <K, V> MutableSortedMap<K, V> newMap(Comparator<? super K> comparator)
    {
        return SortedMapAdapter.adapt(new TreeMap<>(comparator));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeyValue(Comparator<? super K> comparator, K key, V value)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>(comparator)).with(Tuples.pair(key, value));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator, K key1, V value1, K key2, V value2)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>(comparator)).with(Tuples.pair(key1, value1), Tuples.pair(key2, value2));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>(comparator)).with(Tuples.pair(key1, value1), Tuples.pair(key2, value2), Tuples.pair(key3, value3));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeysValues(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>(comparator)).with(Tuples.pair(key1, value1), Tuples.pair(key2, value2), Tuples.pair(key3, value3), Tuples.pair(key4, value4));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMap()
    {
        return SortedMapAdapter.adapt(new TreeMap<>());
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>()).with(Tuples.pair(key, value));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>()).with(Tuples.pair(key1, value1), Tuples.pair(key2, value2));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>()).with(Tuples.pair(key1, value1), Tuples.pair(key2, value2), Tuples.pair(key3, value3));
    }

    @Override
    public <K, V> MutableSortedMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return SortedMapAdapter.adapt(new TreeMap<K, V>()).with(Tuples.pair(key1, value1), Tuples.pair(key2, value2), Tuples.pair(key3, value3), Tuples.pair(key4, value4));
    }

    @Test(expected = NullPointerException.class)
    public void testNewNull()
    {
        SortedMapAdapter.adapt(null);
    }

    @Test
    public void testAdapt()
    {
        TreeSortedMap<Integer, String> sortedMap = TreeSortedMap.newMapWith(1, "1", 2, "2");
        MutableSortedMap<Integer, String> adapt = SortedMapAdapter.adapt(sortedMap);
        Assert.assertSame(sortedMap, adapt);

        SortedMap<Integer, String> treeMap = new TreeMap<>(sortedMap);
        MutableSortedMap<Integer, String> treeAdapt = SortedMapAdapter.adapt(treeMap);
        Assert.assertNotSame(treeMap, treeAdapt);
        Assert.assertEquals(treeMap, treeAdapt);
    }
}
