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

package com.webguys.ponzu.api.multimap.list;

import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.api.multimap.ImmutableMultimap;

/**
 * @since 1.0
 */
public interface ImmutableListMultimap<K, V>
        extends ListMultimap<K, V>, ImmutableMultimap<K, V>
{
    ImmutableListMultimap<K, V> newEmpty();

    ImmutableList<V> get(K key);

    ImmutableListMultimap<K, V> newWith(K key, V value);

    ImmutableListMultimap<K, V> newWithout(Object key, Object value);

    ImmutableListMultimap<K, V> newWithAll(K key, Iterable<? extends V> values);

    ImmutableListMultimap<K, V> newWithoutAll(Object key);
}
