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

package com.gs.collections.impl.map.strategy.immutable;

import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.map.MapIterableTestCase;
import com.gs.collections.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;
import com.gs.collections.impl.tuple.Tuples;

public class ImmutableUnifiedMapWithHashingStrategy2Test extends MapIterableTestCase
{
    @Override
    protected <K, V> ImmutableMap<K, V> newMap()
    {
        return new ImmutableUnifiedMapWithHashingStrategy<>(
                UnifiedMapWithHashingStrategy.<K, V>newMap(HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<K>defaultStrategy())));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeyValue(K key1, V value1)
    {
        return new ImmutableUnifiedMapWithHashingStrategy<>(
                UnifiedMapWithHashingStrategy.newWithKeysValues(HashingStrategies.nullSafeHashingStrategy(
                        HashingStrategies.<K>defaultStrategy()), key1, value1));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new ImmutableUnifiedMapWithHashingStrategy<>(
                UnifiedMapWithHashingStrategy.newWithKeysValues(HashingStrategies.nullSafeHashingStrategy(
                        HashingStrategies.<K>defaultStrategy()), key1, value1, key2, value2));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new ImmutableUnifiedMapWithHashingStrategy<>(
                UnifiedMapWithHashingStrategy.newWithKeysValues(HashingStrategies.nullSafeHashingStrategy(
                        HashingStrategies.<K>defaultStrategy()), key1, value1, key2, value2, key3, value3));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return new ImmutableUnifiedMapWithHashingStrategy<>(
                UnifiedMapWithHashingStrategy.newMapWith(HashingStrategies.nullSafeHashingStrategy(
                        HashingStrategies.<K>defaultStrategy()), Tuples.pair(key1, value1), Tuples.pair(key2, value2), Tuples.pair(key3, value3), Tuples.pair(key4, value4)));
    }
}
