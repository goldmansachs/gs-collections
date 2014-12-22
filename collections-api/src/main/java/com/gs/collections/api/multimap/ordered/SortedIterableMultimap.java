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

package com.gs.collections.api.multimap.ordered;

import java.util.Comparator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.ordered.SortedIterable;

/**
 * @since 5.0
 */
public interface SortedIterableMultimap<K, V>
        extends OrderedIterableMultimap<K, V>
{
    SortedIterableMultimap<K, V> newEmpty();

    SortedIterable<V> get(K key);

    Comparator<? super V> comparator();

    SortedIterableMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    SortedIterableMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    SortedIterableMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    SortedIterableMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <V2> ListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
