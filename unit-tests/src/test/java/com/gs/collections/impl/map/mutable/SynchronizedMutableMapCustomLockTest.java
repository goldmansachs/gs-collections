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

package com.gs.collections.impl.map.mutable;

import com.gs.collections.api.map.MutableMap;

/**
 * JUnit test for {@link SynchronizedMutableMap}.
 */
public class SynchronizedMutableMapCustomLockTest extends MutableMapTestCase
{
    private static final Object LOCK = "lock";

    @Override
    public <K, V> MutableMap<K, V> newMap()
    {
        return new SynchronizedMutableMap<>(UnifiedMap.<K, V>newMap(), LOCK);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return new SynchronizedMutableMap<>(UnifiedMap.newWithKeysValues(key, value), LOCK);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new SynchronizedMutableMap<>(UnifiedMap.newWithKeysValues(key1, value1, key2, value2), LOCK);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new SynchronizedMutableMap<>(UnifiedMap.newWithKeysValues(key1, value1, key2, value2, key3, value3), LOCK);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return new SynchronizedMutableMap<>(UnifiedMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4), LOCK);
    }
}
