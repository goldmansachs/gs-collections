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

package com.gs.collections.api.factory.map;

import com.gs.collections.api.map.MutableMap;

public interface MutableMapFactory
{
    /**
     * @since 6.0
     */
    <K, V> MutableMap<K, V> empty();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> MutableMap<K, V> of();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> MutableMap<K, V> with();

    /**
     * Same as {@link #empty()}. but takes in an initial capacity
     */

    <K, V> MutableMap<K, V> ofInitialCapacity(int capacity);

    /**
     * Same as {@link #empty()}. but takes in an initial capacity
     */

    <K, V> MutableMap<K, V> withInitialCapacity(int capacity);

    /**
     * Same as {@link #with(Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(K key, V value);

    <K, V> MutableMap<K, V> with(K key, V value);

    /**
     * Same as {@link #with(Object, Object, Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(K key1, V value1, K key2, V value2);

    <K, V> MutableMap<K, V> with(K key1, V value1, K key2, V value2);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3);

    <K, V> MutableMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> MutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    <K, V> MutableMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);
}
