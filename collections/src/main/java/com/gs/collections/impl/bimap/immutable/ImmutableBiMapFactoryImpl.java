/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.bimap.immutable;

import java.util.Map;

import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.api.factory.bimap.ImmutableBiMapFactory;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.utility.MapIterate;

public class ImmutableBiMapFactoryImpl implements ImmutableBiMapFactory
{
    public static final ImmutableHashBiMap<?, ?> EMPTY_INSTANCE = new ImmutableHashBiMap(Maps.immutable.empty(), Maps.immutable.empty());

    public <K, V> ImmutableBiMap<K, V> empty()
    {
        return (ImmutableBiMap<K, V>) EMPTY_INSTANCE;
    }

    public <K, V> ImmutableBiMap<K, V> of()
    {
        return this.empty();
    }

    public <K, V> ImmutableBiMap<K, V> with()
    {
        return this.empty();
    }

    public <K, V> ImmutableBiMap<K, V> of(K key, V value)
    {
        return this.with(key, value);
    }

    public <K, V> ImmutableBiMap<K, V> with(K key, V value)
    {
        return new ImmutableHashBiMap<K, V>(
                Maps.immutable.with(key, value),
                Maps.immutable.with(value, key));
    }

    public <K, V> ImmutableBiMap<K, V> of(K key1, V value1, K key2, V value2)
    {
        return this.with(key1, value1, key2, value2);
    }

    public <K, V> ImmutableBiMap<K, V> with(K key1, V value1, K key2, V value2)
    {
        return new ImmutableHashBiMap<K, V>(
                Maps.immutable.with(key1, value1, key2, value2),
                Maps.immutable.with(value1, key1, value2, key2));
    }

    public <K, V> ImmutableBiMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return this.with(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> ImmutableBiMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new ImmutableHashBiMap<K, V>(
                Maps.immutable.with(key1, value1, key2, value2, key3, value3),
                Maps.immutable.with(value1, key1, value2, key2, value3, key3));
    }

    public <K, V> ImmutableBiMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return this.with(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public <K, V> ImmutableBiMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return new ImmutableHashBiMap<K, V>(
                Maps.immutable.with(key1, value1, key2, value2, key3, value3, key4, value4),
                Maps.immutable.with(value1, key1, value2, key2, value3, key3, value4, key4));
    }

    public <K, V> ImmutableBiMap<K, V> ofAll(Map<K, V> map)
    {
        return this.withAll(map);
    }

    public <K, V> ImmutableBiMap<K, V> withAll(Map<K, V> map)
    {
        if (map instanceof ImmutableBiMap<?, ?>)
        {
            return (ImmutableBiMap<K, V>) map;
        }
        if (map instanceof MutableBiMap<?, ?>)
        {
            return this.withAll((MutableBiMap<K, V>) map);
        }
        ImmutableMap<K, V> immutableMap = Maps.immutable.withAll(map);
        return new ImmutableHashBiMap<K, V>(immutableMap, Maps.immutable.withAll(MapIterate.flipUniqueValues(immutableMap)));
    }

    public <K, V> ImmutableBiMap<K, V> ofAll(MutableBiMap<K, V> biMap)
    {
        return this.withAll(biMap);
    }

    public <K, V> ImmutableBiMap<K, V> withAll(MutableBiMap<K, V> biMap)
    {
        return new ImmutableHashBiMap<K, V>(Maps.immutable.withAll(biMap), Maps.immutable.withAll(biMap.inverse()));
    }

    public <K, V> ImmutableBiMap<K, V> ofAll(ImmutableMap<K, V> immutableMap)
    {
        return this.withAll(immutableMap);
    }

    public <K, V> ImmutableBiMap<K, V> withAll(ImmutableMap<K, V> immutableMap)
    {
        if (immutableMap instanceof ImmutableBiMap)
        {
            return (ImmutableBiMap<K, V>) immutableMap;
        }
        return new ImmutableHashBiMap<K, V>(immutableMap, immutableMap.flipUniqueValues());
    }
}
