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
import java.util.Map;

import com.gs.collections.api.factory.map.sorted.MutableSortedMapFactory;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import net.jcip.annotations.Immutable;

@Immutable
public final class MutableSortedMapFactoryImpl implements MutableSortedMapFactory
{
    public <K, V> MutableSortedMap<K, V> empty()
    {
        return TreeSortedMap.newMap();
    }

    public <K, V> MutableSortedMap<K, V> of()
    {
        return this.empty();
    }

    public <K, V> MutableSortedMap<K, V> with()
    {
        return this.empty();
    }

    public <K, V> MutableSortedMap<K, V> of(K key, V value)
    {
        return this.with(key, value);
    }

    public <K, V> MutableSortedMap<K, V> with(K key, V value)
    {
        return TreeSortedMap.newMapWith(key, value);
    }

    public <K, V> MutableSortedMap<K, V> of(K key1, V value1, K key2, V value2)
    {
        return this.with(key1, value1, key2, value2);
    }

    public <K, V> MutableSortedMap<K, V> with(K key1, V value1, K key2, V value2)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2);
    }

    public <K, V> MutableSortedMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return this.with(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> MutableSortedMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> MutableSortedMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return this.with(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public <K, V> MutableSortedMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return TreeSortedMap.newMapWith(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public <K, V> MutableSortedMap<K, V> of(Comparator<? super K> comparator)
    {
        return this.with(comparator);
    }

    public <K, V> MutableSortedMap<K, V> with(Comparator<? super K> comparator)
    {
        if (comparator == null)
        {
            return this.of();
        }
        return TreeSortedMap.newMap(comparator);
    }

    public <K, V> MutableSortedMap<K, V> of(Comparator<? super K> comparator, K key, V value)
    {
        return this.with(comparator, key, value);
    }

    public <K, V> MutableSortedMap<K, V> with(Comparator<? super K> comparator, K key, V value)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key, value);
    }

    public <K, V> MutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2)
    {
        return this.with(comparator, key1, value1, key2, value2);
    }

    public <K, V> MutableSortedMap<K, V> with(Comparator<? super K> comparator, K key1, V value1, K key2, V value2)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2);
    }

    public <K, V> MutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return this.with(comparator, key1, value1, key2, value2, key3, value3);
    }

    public <K, V> MutableSortedMap<K, V> with(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> MutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return this.with(comparator, key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public <K, V> MutableSortedMap<K, V> with(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return TreeSortedMap.<K, V>newMap(comparator).with(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public <K, V> MutableSortedMap<K, V> ofSortedMap(Map<? extends K, ? extends V> map)
    {
        return this.withSortedMap(map);
    }

    public <K, V> MutableSortedMap<K, V> withSortedMap(Map<? extends K, ? extends V> map)
    {
        return TreeSortedMap.newMap(map);
    }
}
