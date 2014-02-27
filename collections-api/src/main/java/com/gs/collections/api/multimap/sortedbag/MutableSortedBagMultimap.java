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

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.multimap.MutableMultimap;

/**
 * @since 4.2
 */
public interface MutableSortedBagMultimap<K, V>
        extends MutableMultimap<K, V>, SortedBagMultimap<K, V>
{
    MutableSortedBag<V> replaceValues(K key, Iterable<? extends V> values);

    MutableSortedBag<V> removeAll(Object key);

    MutableSortedBagMultimap<K, V> newEmpty();

    MutableSortedBag<V> get(K key);

    MutableSortedBagMultimap<K, V> toMutable();

    ImmutableSortedBagMultimap<K, V> toImmutable();
}
