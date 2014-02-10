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

package com.gs.collections.impl.map.strategy.immutable;

import java.util.Map;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.factory.map.strategy.ImmutableHashingStrategyMapFactory;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;
import net.jcip.annotations.Immutable;

@Immutable
public final class ImmutableHashingStrategyMapFactoryImpl implements ImmutableHashingStrategyMapFactory
{
    public <K, V> ImmutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy)
    {
        return this.with(hashingStrategy);
    }

    public <K, V> ImmutableMap<K, V> with(HashingStrategy<? super K> hashingStrategy)
    {
        return new ImmutableEmptyMapWithHashingStrategy<K, V>(hashingStrategy);
    }

    public <K, V> ImmutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key, V value)
    {
        return this.with(hashingStrategy, key, value);
    }

    public <K, V> ImmutableMap<K, V> with(HashingStrategy<? super K> hashingStrategy, K key, V value)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(hashingStrategy, key, value).toImmutable();
    }

    public <K, V> ImmutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key1, V value1, K key2, V value2)
    {
        return this.with(
                hashingStrategy,
                key1, value1,
                key2, value2);
    }

    public <K, V> ImmutableMap<K, V> with(HashingStrategy<? super K> hashingStrategy, K key1, V value1, K key2, V value2)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(
                hashingStrategy,
                key1, value1,
                key2, value2).toImmutable();
    }

    public <K, V> ImmutableMap<K, V> of(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        return this.with(
                hashingStrategy,
                key1, value1,
                key2, value2,
                key3, value3);
    }

    public <K, V> ImmutableMap<K, V> with(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(
                hashingStrategy,
                key1, value1,
                key2, value2,
                key3, value3).toImmutable();
    }

    public <K, V> ImmutableMap<K, V> of(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        return this.with(
                hashingStrategy,
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4);
    }

    public <K, V> ImmutableMap<K, V> with(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(
                hashingStrategy,
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4).toImmutable();
    }

    /**
     * @deprecated use {@link #ofAll(Map)} instead (inlineable)
     */
    @Deprecated
    public <K, V> ImmutableMap<K, V> ofMap(Map<K, V> map)
    {
        return this.ofAll(map);
    }

    public <K, V> ImmutableMap<K, V> ofAll(Map<K, V> map)
    {
        return this.withAll(map);
    }

    public <K, V> ImmutableMap<K, V> withAll(Map<K, V> map)
    {
        if (!(map instanceof UnifiedMapWithHashingStrategy<?, ?>))
        {
            throw new IllegalArgumentException();
        }

        UnifiedMapWithHashingStrategy<K, V> mapWithHashingStrategy = (UnifiedMapWithHashingStrategy<K, V>) map;

        if (mapWithHashingStrategy.isEmpty())
        {
            return this.of(mapWithHashingStrategy.hashingStrategy());
        }

        return new ImmutableUnifiedMapWithHashingStrategy<K, V>(mapWithHashingStrategy);
    }
}
