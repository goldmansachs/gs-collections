/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.api.multimap.sortedset;

import java.util.Comparator;

import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.set.sorted.SortedSetIterable;

/**
 * @since 1.0
 */
public interface SortedSetMultimap<K, V>
        extends Multimap<K, V>
{
    SortedSetMultimap<K, V> newEmpty();

    SortedSetIterable<V> get(K key);

    Comparator<? super V> comparator();

    MutableSortedSetMultimap<K, V> toMutable();

    ImmutableSortedSetMultimap<K, V> toImmutable();
}
