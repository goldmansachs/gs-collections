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

package com.webguys.ponzu.api.multimap.set;

import com.webguys.ponzu.api.multimap.Multimap;
import com.webguys.ponzu.api.set.SetIterable;

public interface SetMultimap<K, V>
        extends Multimap<K, V>
{
    SetMultimap<K, V> newEmpty();

    SetIterable<V> get(K key);

    MutableSetMultimap<K, V> toMutable();

    ImmutableSetMultimap<K, V> toImmutable();
}
