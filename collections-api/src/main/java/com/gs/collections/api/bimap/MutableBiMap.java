/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.api.bimap;

import java.util.Map;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;

/**
 * A {@link BiMap} whose contents can be altered after initialization.
 *
 * @since 4.2
 */
public interface MutableBiMap<K, V> extends BiMap<K, V>, MutableMap<K, V>
{
    MutableBiMap<K, V> clone();

    MutableBiMap<K, V> newEmpty();

    MutableBiMap<V, K> inverse();

    /**
     * Similar to {@link Map#put(Object, Object)}, except that it throws on the addition of a duplicate value.
     *
     * @throws IllegalArgumentException if the value already exists in the bimap.
     */
    V put(K key, V value);

    /**
     * Similar to {@link #put(Object, Object)}, except that it quietly removes any existing entry with the same
     * value before putting the key-value pair.
     */
    V forcePut(K key, V value);

    MutableBiMap<K, V> asSynchronized();

    MutableBiMap<K, V> asUnmodifiable();

    MutableBiMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    <R> MutableBiMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    <K2, V2> MutableBiMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    MutableBiMap<K, V> reject(Predicate2<? super K, ? super V> predicate);
}
