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

package com.gs.collections.api.map;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.tuple.Pair;

/**
 * An iterable Map whose elements are unsorted.
 */
public interface UnsortedMapIterable<K, V>
        extends MapIterable<K, V>
{
    UnsortedMapIterable<K, V> select(Predicate2<? super K, ? super V> predicate);

    UnsortedMapIterable<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <R> UnsortedMapIterable<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    <K2, V2> UnsortedMapIterable<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    /**
     * Converts the UnsortedMapIterable to an immutable implementation. Returns this for immutable maps.
     *
     * @since 5.0
     */
    ImmutableMap<K, V> toImmutable();
}
