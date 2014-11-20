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

package com.gs.collections.api.factory.bimap;

import java.util.Map;

import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.api.map.ImmutableMap;

public interface ImmutableBiMapFactory
{
    /**
     * @since 6.0
     */
    <K, V> ImmutableBiMap<K, V> empty();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> ImmutableBiMap<K, V> of();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> ImmutableBiMap<K, V> with();

    /**
     * Same as {@link #with(Object, Object)}.
     */
    <K, V> ImmutableBiMap<K, V> of(K key, V value);

    <K, V> ImmutableBiMap<K, V> with(K key, V value);

    /**
     * Same as {@link #with(Object, Object, Object, Object)}.
     */
    <K, V> ImmutableBiMap<K, V> of(K key1, V value1, K key2, V value2);

    <K, V> ImmutableBiMap<K, V> with(K key1, V value1, K key2, V value2);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> ImmutableBiMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3);

    <K, V> ImmutableBiMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> ImmutableBiMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    <K, V> ImmutableBiMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    /**
     * Same as {@link #withAll(Map)}.
     */
    <K, V> ImmutableBiMap<K, V> ofAll(Map<K, V> map);

    <K, V> ImmutableBiMap<K, V> withAll(Map<K, V> map);

    <K, V> ImmutableBiMap<K, V> ofAll(MutableBiMap<K, V> biMap);

    <K, V> ImmutableBiMap<K, V> withAll(MutableBiMap<K, V> biMap);

    <K, V> ImmutableBiMap<K, V> ofAll(ImmutableMap<K, V> immutableMap);

    <K, V> ImmutableBiMap<K, V> withAll(ImmutableMap<K, V> immutableMap);
}
