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

package com.gs.collections.api.multimap;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 1.0
 */
public interface MutableMultimap<K, V>
        extends Multimap<K, V>
{
    MutableMultimap<K, V> newEmpty();

    MutableCollection<V> get(K key);

    // Modification Operations

    boolean put(K key, V value);

    /**
     * Modification operation similar to put, however, takes the key-value pair as the input.
     *
     * @param keyValuePair
     *         key value pair to add in the multimap
     * @see #put(Object, Object)
     * @since 6.0
     */
    boolean add(Pair<K, V> keyValuePair);

    boolean remove(Object key, Object value);

    // Bulk Operations
    boolean putAllPairs(Pair<K, V>... pairs);

    boolean putAll(K key, Iterable<? extends V> values);

    <KK extends K, VV extends V> boolean putAll(Multimap<KK, VV> multimap);

    RichIterable<V> replaceValues(K key, Iterable<? extends V> values);

    RichIterable<V> removeAll(Object key);

    void clear();

    MutableMultimap<V, K> flip();

    MutableMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    MutableMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    MutableMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    MutableMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> MutableMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> MutableMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
