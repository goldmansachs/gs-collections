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

package com.gs.collections.api.factory.map.strategy;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.map.MutableMap;

public interface MutableHashingStrategyMapFactory
{
    /**
     * Same as {@link #with(HashingStrategy)}.
     */
    <K, V> MutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy);

    <K, V> MutableMap<K, V> with(HashingStrategy<? super K> hashingStrategy);

    /**
     * Same as {@link #with(HashingStrategy, Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key, V value);

    <K, V> MutableMap<K, V> with(HashingStrategy<? super K> hashingStrategy, K key, V value);

    /**
     * Same as {@link #with(HashingStrategy, Object, Object, Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(HashingStrategy<? super K> hashingStrategy, K key1, V value1, K key2, V value2);

    <K, V> MutableMap<K, V> with(HashingStrategy<? super K> hashingStrategy, K key1, V value1, K key2, V value2);

    /**
     * Same as {@link #with(HashingStrategy, Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    <K, V> MutableMap<K, V> with(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    /**
     * Same as {@link #with(HashingStrategy, Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);

    <K, V> MutableMap<K, V> with(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);
}
