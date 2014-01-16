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

import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.multimap.ordered.ReversibleIterableMultimap;

public interface ListMultimap<K, V>
        extends ReversibleIterableMultimap<K, V>
{
    ListMultimap<K, V> newEmpty();

    ListIterable<V> get(K key);

    MutableListMultimap<K, V> toMutable();

    ImmutableListMultimap<K, V> toImmutable();
}
