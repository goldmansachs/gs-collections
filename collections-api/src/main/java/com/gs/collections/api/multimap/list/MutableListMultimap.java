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

package com.gs.collections.api.multimap.list;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;

/**
 * @since 1.0
 */
public interface MutableListMultimap<K, V>
        extends ListMultimap<K, V>, MutableMultimap<K, V>
{
    MutableList<V> replaceValues(K key, Iterable<? extends V> values);

    MutableList<V> removeAll(Object key);

    MutableListMultimap<K, V> newEmpty();

    MutableList<V> get(K key);

    MutableListMultimap<K, V> toMutable();

    ImmutableListMultimap<K, V> toImmutable();
}
