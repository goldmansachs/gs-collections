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

package com.gs.collections.impl.bimap.mutable;

import java.io.Externalizable;
import java.util.Map;

import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;

/**
 * A {@link MutableBiMap} which uses two hash tables as its underlying data store.
 *
 * @since 4.2
 */
public class HashBiMap<K, V> extends AbstractMutableBiMap<K, V> implements Externalizable
{
    private static final long serialVersionUID = 1L;

    public HashBiMap()
    {
        super(UnifiedMap.<K, V>newMap(), UnifiedMap.<V, K>newMap());
    }

    public HashBiMap(int initialSize)
    {
        super(UnifiedMap.<K, V>newMap(initialSize), UnifiedMap.<V, K>newMap(initialSize));
    }

    public HashBiMap(Map<K, V> map)
    {
        super(map);
    }

    HashBiMap(Map<K, V> keysToValues, Map<V, K> valuesToKeys)
    {
        super(keysToValues, valuesToKeys);
    }

    public static <K, V> HashBiMap<K, V> newMap()
    {
        return new HashBiMap<K, V>();
    }

    public static <K, V> HashBiMap<K, V> newWithKeysValues(K key, V value)
    {
        return new HashBiMap<K, V>(1).withKeysValues(key, value);
    }

    public static <K, V> HashBiMap<K, V> newWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new HashBiMap<K, V>(2).withKeysValues(key1, value1, key2, value2);
    }

    public static <K, V> HashBiMap<K, V> newWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new HashBiMap<K, V>(3).withKeysValues(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> HashBiMap<K, V> newWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return new HashBiMap<K, V>(4).withKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public HashBiMap<K, V> withKeysValues(K key, V value)
    {
        this.put(key, value);
        return this;
    }

    public HashBiMap<K, V> withKeysValues(K key1, V value1, K key2, V value2)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        return this;
    }

    public HashBiMap<K, V> withKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        this.put(key3, value3);
        return this;
    }

    public HashBiMap<K, V> withKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        this.put(key3, value3);
        this.put(key4, value4);
        return this;
    }
}
