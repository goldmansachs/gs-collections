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

package ponzu.impl.map.strategy.immutable;

import ponzu.api.block.HashingStrategy;
import ponzu.api.factory.map.strategy.ImmutableHashingStrategyMapFactory;
import ponzu.api.map.ImmutableMap;
import ponzu.api.map.MutableMap;
import ponzu.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;

public final class ImmutableHashingStrategyMapFactoryImpl implements ImmutableHashingStrategyMapFactory
{
    public <K, V> ImmutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy)
    {
        return new ImmutableEmptyMapWithHashingStrategy<K, V>(hashingStrategy);
    }

    public <K, V> ImmutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key, V value)
    {
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(hashingStrategy, key, value).toImmutable();
    }

    public <K, V> ImmutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key1, V value1, K key2, V value2)
    {
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(
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
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(
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
        return UnifiedMapWithHashingStrategy.<K, V>newWithKeysValues(
                hashingStrategy,
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4).toImmutable();
    }

    public <K, V> ImmutableMap<K, V> ofMap(MutableMap<K, V> map)
    {
        if (!(map instanceof UnifiedMapWithHashingStrategy<?, ?>))
        {
            throw new IllegalArgumentException();
        }

        UnifiedMapWithHashingStrategy mapWithHashingStrategy = (UnifiedMapWithHashingStrategy) map;

        if (mapWithHashingStrategy.isEmpty())
        {
            return this.of(mapWithHashingStrategy.hashingStrategy());
        }

        return new ImmutableUnifiedMapWithHashingStrategy<K, V>(mapWithHashingStrategy);
    }
}
