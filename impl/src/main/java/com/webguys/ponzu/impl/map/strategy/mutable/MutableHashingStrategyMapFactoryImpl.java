/*
 * Copyright 2011 Goldman Sachs.
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

package com.webguys.ponzu.impl.map.strategy.mutable;

import com.webguys.ponzu.api.block.HashingStrategy;
import com.webguys.ponzu.api.factory.map.strategy.MutableHashingStrategyMapFactory;
import com.webguys.ponzu.api.map.MutableMap;

public final class MutableHashingStrategyMapFactoryImpl implements MutableHashingStrategyMapFactory
{
    public <K, V> MutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy);
    }

    public <K, V> MutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key, V value)
    {
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(hashingStrategy, key, value);
    }

    public <K, V> MutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key1, V value1, K key2, V value2)
    {
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(
                hashingStrategy,
                key1, value1,
                key2, value2);
    }

    public <K, V> MutableMap<K, V> of(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(
                hashingStrategy,
                key1, value1,
                key2, value2,
                key3, value3);
    }

    public <K, V> MutableMap<K, V> of(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(
                hashingStrategy,
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4);
    }
}
