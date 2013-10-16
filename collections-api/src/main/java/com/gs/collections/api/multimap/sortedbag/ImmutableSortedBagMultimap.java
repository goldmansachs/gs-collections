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

package com.gs.collections.api.multimap.sortedbag;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;

/**
 * @since 4.2
 */
public interface ImmutableSortedBagMultimap<K, V>
        extends ImmutableBagMultimap<K, V>,
        SortedBagMultimap<K, V>
{
    ImmutableSortedBag<V> get(K key);

    ImmutableSortedBagMultimap<K, V> newEmpty();

    ImmutableSortedBagMultimap<K, V> newWith(K key, V value);

    ImmutableSortedBagMultimap<K, V> newWithout(Object key, Object value);

    ImmutableSortedBagMultimap<K, V> newWithAll(K key, Iterable<? extends V> values);

    ImmutableSortedBagMultimap<K, V> newWithoutAll(Object key);
}
