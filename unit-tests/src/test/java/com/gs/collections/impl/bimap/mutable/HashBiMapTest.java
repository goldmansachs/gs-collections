/*
 * Copyright 2016 Goldman Sachs.
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
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Key;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class HashBiMapTest extends AbstractMutableBiMapTestCase
{
    @Override
    public HashBiMap<Integer, Character> classUnderTest()
    {
        HashBiMap<Integer, Character> map = HashBiMap.newMap();
        map.put(1, null);
        map.put(null, 'b');
        map.put(3, 'c');
        return map;
    }

    @Override
    public HashBiMap<Integer, Character> getEmptyMap()
    {
        return HashBiMap.newMap();
    }

    @Override
    protected <K, V> HashBiMap<K, V> newMap()
    {
        return HashBiMap.newMap();
    }

    @Override
    protected <K, V> HashBiMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return HashBiMap.newWithKeysValues(key, value);
    }

    @Override
    protected <K, V> HashBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return HashBiMap.newWithKeysValues(key1, value1, key2, value2);
    }

    @Override
    protected <K, V> HashBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return HashBiMap.newWithKeysValues(key1, value1, key2, value2, key3, value3);
    }

    @Override
    protected <K, V> HashBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return HashBiMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Test
    public void newMap_throws()
    {
        Verify.assertThrows(IllegalArgumentException.class, () -> new HashBiMap<>(UnifiedMap.newMap(), null));

        Verify.assertThrows(IllegalArgumentException.class, () -> new HashBiMap<>(null, null));

        Verify.assertThrows(IllegalArgumentException.class, () -> new HashBiMap<>(null, UnifiedMap.newMap()));

        UnifiedMap<Object, Object> map = UnifiedMap.newMap();
        Verify.assertThrows(IllegalArgumentException.class, () -> new HashBiMap<>(map, map));
    }

    @Test
    public void withKeysValues()
    {
        HashBiMap<Integer, Character> map = this.getEmptyMap();
        HashBiMap<Integer, Character> map1 = map.withKeysValues(1, 'a');
        AbstractMutableBiMapTestCase.assertBiMapsEqual(HashBiMap.newWithKeysValues(1, 'a'), map1);
        Assert.assertSame(map, map1);
        HashBiMap<Integer, Character> map2 = map1.withKeysValues(2, 'b');
        HashBiMap<Integer, Character> map22 = map.withKeysValues(1, 'a', 2, 'b');
        AbstractMutableBiMapTestCase.assertBiMapsEqual(HashBiMap.newWithKeysValues(1, 'a', 2, 'b'), map2);
        AbstractMutableBiMapTestCase.assertBiMapsEqual(HashBiMap.newWithKeysValues(1, 'a', 2, 'b'), map22);
        Assert.assertSame(map, map22);
        Assert.assertSame(map1, map2);
        HashBiMap<Integer, Character> map3 = map2.withKeysValues(3, 'c');
        HashBiMap<Integer, Character> map33 = map.withKeysValues(1, 'a', 2, 'b', 3, 'c');
        AbstractMutableBiMapTestCase.assertBiMapsEqual(HashBiMap.newWithKeysValues(1, 'a', 2, 'b', 3, 'c'), map3);
        AbstractMutableBiMapTestCase.assertBiMapsEqual(HashBiMap.newWithKeysValues(1, 'a', 2, 'b', 3, 'c'), map33);
        Assert.assertSame(map, map33);
        Assert.assertSame(map2, map3);
        HashBiMap<Integer, Character> map4 = map3.withKeysValues(4, 'd');
        HashBiMap<Integer, Character> map44 = map.withKeysValues(1, 'a', 2, 'b', 3, 'c', 4, 'd');
        AbstractMutableBiMapTestCase.assertBiMapsEqual(HashBiMap.newWithKeysValues(1, 'a', 2, 'b', 3, 'c', 4, 'd'), map4);
        AbstractMutableBiMapTestCase.assertBiMapsEqual(HashBiMap.newWithKeysValues(1, 'a', 2, 'b', 3, 'c', 4, 'd'), map44);
        Assert.assertSame(map, map44);
        Assert.assertSame(map3, map4);
    }

    @Test
    public void inverseKeyPreservation()
    {
        Key key = new Key("key");
        Key duplicateKey = new Key("key");

        MutableBiMap<Key, Integer> biMap = this.newMapWithKeysValues(key, 1, duplicateKey, 2);
        Assert.assertSame(key, Iterate.getFirst(biMap.entrySet()).getKey());
        Assert.assertSame(key, Iterate.getFirst(biMap.inverse().entrySet()).getValue());
    }

    @Test
    public void valuePreservation()
    {
        Key value = new Key("value");
        Key duplicateValue = new Key("value");

        MutableBiMap<Integer, Key> biMap = this.newMapWithKeyValue(1, value);
        biMap.forcePut(2, duplicateValue);
        Assert.assertSame(value, Iterate.getFirst(biMap.entrySet()).getValue());
        Assert.assertSame(value, Iterate.getFirst(biMap.inverse().entrySet()).getKey());
    }

    @Test
    public void forcePut_inverseKeyAndValuePreservation()
    {
        Key key1 = new Key("1");
        Key value2 = new Key("xyz");

        HashBiMap<Key, Key> biMap = this.newMapWithKeysValues(key1, new Key("abc"), new Key("2"), value2);

        Key duplicateOfKey1 = new Key("1");
        Key duplicateOfValue2 = new Key("xyz");

        biMap.forcePut(duplicateOfKey1, duplicateOfValue2);

        Verify.assertSize(1, biMap);

        Assert.assertSame(key1, Iterate.getFirst(biMap.entrySet()).getKey());
        Assert.assertSame(key1, Iterate.getFirst(biMap.inverse().entrySet()).getValue());

        Assert.assertSame(value2, Iterate.getFirst(biMap.entrySet()).getValue());
        Assert.assertSame(value2, Iterate.getFirst(biMap.inverse().entrySet()).getKey());
    }
}
