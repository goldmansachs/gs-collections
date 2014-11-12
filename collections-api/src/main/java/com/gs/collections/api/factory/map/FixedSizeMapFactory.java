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

package com.gs.collections.api.factory.map;

import com.gs.collections.api.map.FixedSizeMap;

public interface FixedSizeMapFactory
{
    /**
     * @since 6.0
     */
    <K, V> FixedSizeMap<K, V> empty();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> FixedSizeMap<K, V> of();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> FixedSizeMap<K, V> with();

    /**
     * Same as {@link #with(Object, Object)}.
     */
    <K, V> FixedSizeMap<K, V> of(K key, V value);

    <K, V> FixedSizeMap<K, V> with(K key, V value);

    /**
     * Same as {@link #with(Object, Object, Object, Object)}.
     */
    <K, V> FixedSizeMap<K, V> of(K key1, V value1, K key2, V value2);

    <K, V> FixedSizeMap<K, V> with(K key1, V value1, K key2, V value2);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> FixedSizeMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3);

    <K, V> FixedSizeMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3);
}
