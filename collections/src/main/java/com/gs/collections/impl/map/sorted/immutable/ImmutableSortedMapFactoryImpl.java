/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.map.sorted.immutable;

import java.util.Comparator;
import java.util.SortedMap;

import com.gs.collections.api.factory.map.sorted.ImmutableSortedMapFactory;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;

public final class ImmutableSortedMapFactoryImpl implements ImmutableSortedMapFactory
{
    public <K, V> ImmutableSortedMap<K, V> of()
    {
        return (ImmutableSortedMap<K, V>) ImmutableEmptySortedMap.INSTANCE;
    }

    public <K, V> ImmutableSortedMap<K, V> of(K key, V value)
    {
        return TreeSortedMap.newMapWith(key, value).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> of(K key1, V value1, K key2, V value2)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2, key3, value3).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2, key3, value3, key4, value4).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator)
    {
        if (comparator == null)
        {
            return this.of();
        }
        return new ImmutableEmptySortedMap<K, V>(comparator);
    }

    public <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key, V value)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key, value).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2, key3, value3).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2, key3, value3, key4, value4).toImmutable();
    }

    public <K, V> ImmutableSortedMap<K, V> ofSortedMap(SortedMap<K, V> map)
    {
        if (map instanceof ImmutableSortedMap)
        {
            return (ImmutableSortedMap<K, V>) map;
        }
        if (map.isEmpty())
        {
            return this.of(map.comparator());
        }
        return ImmutableTreeMap.newMap(map);
    }
}
