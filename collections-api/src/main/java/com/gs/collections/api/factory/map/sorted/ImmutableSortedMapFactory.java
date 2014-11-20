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

package com.gs.collections.api.factory.map.sorted;

import java.util.Comparator;
import java.util.SortedMap;

import com.gs.collections.api.map.sorted.ImmutableSortedMap;

public interface ImmutableSortedMapFactory
{
    /**
     * @since 6.0
     */
    <K, V> ImmutableSortedMap<K, V> empty();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> ImmutableSortedMap<K, V> of();

    /**
     * Same as {@link #empty()}.
     */
    <K, V> ImmutableSortedMap<K, V> with();

    /**
     * Same as {@link #with(Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(K key, V value);

    <K, V> ImmutableSortedMap<K, V> with(K key, V value);

    /**
     * Same as {@link #with(Object, Object, Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(K key1, V value1, K key2, V value2);

    <K, V> ImmutableSortedMap<K, V> with(K key1, V value1, K key2, V value2);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3);

    <K, V> ImmutableSortedMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    <K, V> ImmutableSortedMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    /**
     * Same as {@link #with(Comparator)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator);

    <K, V> ImmutableSortedMap<K, V> with(Comparator<? super K> comparator);

    /**
     * Same as {@link #with(Comparator, Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key, V value);

    <K, V> ImmutableSortedMap<K, V> with(Comparator<? super K> comparator, K key, V value);

    /**
     * Same as {@link #with(Comparator, Object, Object, Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2);

    <K, V> ImmutableSortedMap<K, V> with(Comparator<? super K> comparator, K key1, V value1, K key2, V value2);

    /**
     * Same as {@link #with(Comparator, Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3);

    <K, V> ImmutableSortedMap<K, V> with(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3);

    /**
     * Same as {@link #with(Comparator, Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    <K, V> ImmutableSortedMap<K, V> with(Comparator<? super K> comparator, K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    /**
     * Same as {@link #withSortedMap(SortedMap)}.
     */
    <K, V> ImmutableSortedMap<K, V> ofSortedMap(SortedMap<K, V> map);

    <K, V> ImmutableSortedMap<K, V> withSortedMap(SortedMap<K, V> map);
}
