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

package com.gs.collections.api.multimap;

import com.gs.collections.api.collection.ImmutableCollection;

/**
 * @since 1.0
 */
public interface ImmutableMultimap<K, V>
        extends Multimap<K, V>
{
    ImmutableMultimap<K, V> newEmpty();

    ImmutableCollection<V> get(K key);

    ImmutableMultimap<K, V> newWith(K key, V value);

    ImmutableMultimap<K, V> newWithout(Object key, Object value);

    ImmutableMultimap<K, V> newWithAll(K key, Iterable<? extends V> values);

    ImmutableMultimap<K, V> newWithoutAll(Object key);
}
