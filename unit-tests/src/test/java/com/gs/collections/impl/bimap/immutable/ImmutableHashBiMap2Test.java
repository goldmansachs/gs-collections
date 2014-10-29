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

package com.gs.collections.impl.bimap.immutable;

import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.impl.IntegerWithCast;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.map.MapIterableTestCase;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableHashBiMap2Test extends MapIterableTestCase
{
    @Override
    protected <K, V> ImmutableHashBiMap<K, V> newMap()
    {
        return new ImmutableHashBiMap<K, V>();
    }

    @Override
    protected <K, V> ImmutableHashBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new ImmutableHashBiMap<K, V>(Maps.immutable.of(key1, value1, key2, value2));
    }

    @Override
    protected <K, V> ImmutableHashBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new ImmutableHashBiMap<K, V>(Maps.immutable.of(key1, value1, key2, value2, key3, value3));
    }

    @Override
    protected <K, V> ImmutableHashBiMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return new ImmutableHashBiMap<K, V>(Maps.immutable.of(key1, value1, key2, value2, key3, value3, key4, value4));
    }

    @Override
    @Test
    public void flipUniqueValues()
    {
        ImmutableHashBiMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        ImmutableBiMap<String, Integer> result = map.flipUniqueValues();
        ImmutableHashBiMap<String, Integer> expectedMap = this.newMapWithKeysValues("1", 1, "2", 2, "3", 3);
        Assert.assertEquals(expectedMap, result);
    }

    @Override
    @Test
    public void flip()
    {
        ImmutableHashBiMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        ImmutableSetMultimap<String, Integer> result = map.flip();
        UnifiedSetMultimap<String, Integer> expected = UnifiedSetMultimap.<String, Integer>newMultimap(Tuples.pair("1", 1), Tuples.pair("2", 2), Tuples.pair("3", 3));
        Assert.assertEquals(expected, result);
    }

    @Override
    @Test
    public void nullCollisionWithCastInEquals()
    {
        ImmutableHashBiMap<IntegerWithCast, String> map = this.newMapWithKeysValues(
                new IntegerWithCast(0), "Test 2",
                null, "Test 1");
        Assert.assertEquals(
                this.newMapWithKeysValues(
                        new IntegerWithCast(0), "Test 2",
                        null, "Test 1"),
                map);
        Assert.assertEquals("Test 2", map.get(new IntegerWithCast(0)));
        Assert.assertEquals("Test 1", map.get(null));
    }
}
