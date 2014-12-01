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

import java.util.HashMap;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

/**
 * JUnit test for {@link MapAdapter}.
 */
public class MapAdapterTest extends MutableMapTestCase
{
    @Override
    public <K, V> MutableMap<K, V> newMap()
    {
        return MapAdapter.adapt(new HashMap<>());
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return MapAdapter.adapt(new HashMap<K, V>()).withKeyValue(key, value);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return MapAdapter.adapt(new HashMap<K, V>()).withKeyValue(key1, value1).withKeyValue(key2, value2);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return MapAdapter.adapt(new HashMap<K, V>())
                .withKeyValue(key1, value1)
                .withKeyValue(key2, value2)
                .withKeyValue(key3, value3);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return MapAdapter.adapt(new HashMap<K, V>())
                .withKeyValue(key1, value1)
                .withKeyValue(key2, value2)
                .withKeyValue(key3, value3)
                .withKeyValue(key4, value4);
    }

    @Test
    public void adaptNull()
    {
        Verify.assertThrows(NullPointerException.class, () -> new MapAdapter<>(null));

        Verify.assertThrows(NullPointerException.class, () -> MapAdapter.adapt(null));
    }
}
