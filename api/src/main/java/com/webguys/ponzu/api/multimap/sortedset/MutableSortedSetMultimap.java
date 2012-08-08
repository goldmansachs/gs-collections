/*
 * Copyright 2011 Goldman Sachs.
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

package com.webguys.ponzu.api.multimap.sortedset;

import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.set.sorted.MutableSortedSet;

/**
 * @since 1.0
 */
public interface MutableSortedSetMultimap<K, V>
        extends MutableMultimap<K, V>, SortedSetMultimap<K, V>
{
    MutableSortedSet<V> replaceValues(K key, Iterable<? extends V> values);

    MutableSortedSet<V> removeAll(Object key);

    MutableSortedSetMultimap<K, V> newEmpty();

    MutableSortedSet<V> get(K key);

    MutableSortedSetMultimap<K, V> toMutable();

    ImmutableSortedSetMultimap<K, V> toImmutable();
}
