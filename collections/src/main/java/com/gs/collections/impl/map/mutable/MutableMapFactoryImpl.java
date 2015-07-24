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

package com.gs.collections.impl.map.mutable;

import java.lang.reflect.Field;

import com.gs.collections.api.factory.map.MutableMapFactory;
import com.gs.collections.api.map.MutableMap;
import net.jcip.annotations.Immutable;

@Immutable
public final class MutableMapFactoryImpl implements MutableMapFactory
{
    public <K, V> MutableMap<K, V> empty()
    {
        return UnifiedMap.newMap();
    }

    public <K, V> MutableMap<K, V> of()
    {
        return this.empty();
    }

    public <K, V> MutableMap<K, V> with()
    {
        return this.empty();
    }

    public <K, V> MutableMap<K, V> ofInitialCapacity(int capacity)
    {
        return this.withInitialCapacity(capacity);
    }

    public <K, V> MutableMap<K, V> withInitialCapacity(int capacity)
    {
        return UnifiedMap.newMap(capacity);
    }

    public <K, V> MutableMap<K, V> of(K key, V value)
    {
        return this.with(key, value);
    }

    public <K, V> MutableMap<K, V> with(K key, V value)
    {
        return UnifiedMap.newWithKeysValues(key, value);
    }

    public <K, V> MutableMap<K, V> of(K key1, V value1, K key2, V value2)
    {
        return this.with(key1, value1, key2, value2);
    }

    public <K, V> MutableMap<K, V> with(K key1, V value1, K key2, V value2)
    {
        return UnifiedMap.newWithKeysValues(key1, value1, key2, value2);
    }

    public <K, V> MutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return this.with(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> MutableMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return UnifiedMap.newWithKeysValues(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> MutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return this.with(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public <K, V> MutableMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return UnifiedMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }
}
