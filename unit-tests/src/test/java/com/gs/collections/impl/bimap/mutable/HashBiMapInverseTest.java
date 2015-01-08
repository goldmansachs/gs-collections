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

package com.gs.collections.impl.bimap.mutable;

import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Key;
import org.junit.Test;

public class HashBiMapInverseTest extends AbstractMutableBiMapTestCase
{
    @Override
    public MutableBiMap<Integer, Character> classUnderTest()
    {
        HashBiMap<Character, Integer> biMap = HashBiMap.newMap();
        biMap.put(null, 1);
        biMap.put('b', null);
        biMap.put('c', 3);
        return biMap.inverse();
    }

    @Override
    public MutableBiMap<Integer, Character> getEmptyMap()
    {
        return HashBiMap.<Character, Integer>newMap().inverse();
    }

    @Override
    protected <K, V> MutableBiMap<K, V> newMap()
    {
        return HashBiMap.<V, K>newMap().inverse();
    }

    @Override
    protected <K, V> MutableBiMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return HashBiMap.newWithKeysValues(value, key).inverse();
    }

    @Override
    protected <K, V> MutableBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return HashBiMap.newWithKeysValues(value1, key1, value2, key2).inverse();
    }

    @Override
    protected <K, V> MutableBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return HashBiMap.newWithKeysValues(value1, key1, value2, key2, value3, key3).inverse();
    }

    @Override
    protected <K, V> MutableBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return HashBiMap.newWithKeysValues(value1, key1, value2, key2, value3, key3, value4, key4).inverse();
    }

    @Override
    @Test
    public void keyPreservation()
    {
        Key key = new Key("key");

        Key duplicateKey1 = new Key("key");
        MutableBiMap<Integer, Key> map1 = HashBiMap.newWithKeysValues(key, 1, duplicateKey1, 2).inverse();
        Verify.assertSize(1, map1);
        Verify.assertContainsKeyValue(2, key, map1);

        Key duplicateKey2 = new Key("key");
        MutableBiMap<Integer, Key> map2 = HashBiMap.newWithKeysValues(key, 1, duplicateKey1, 2, duplicateKey2, 3).inverse();
        Verify.assertSize(1, map2);
        Verify.assertContainsKeyValue(3, key, map2);
    }
}
