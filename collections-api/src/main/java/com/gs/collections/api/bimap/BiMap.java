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

package com.gs.collections.api.bimap;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * A map that allows users to look up key-value pairs from either direction. Uniqueness is enforced on both the keys and values.
 *
 * @since 4.2
 */
public interface BiMap<K, V> extends MapIterable<K, V>
{
    /**
     * Returns an inversed view of this BiMap, where the associations are in the direction of this bimap's values to keys.
     */
    BiMap<V, K> inverse();

    /**
     * Converts the BiMap to an ImmutableBiMap.  If the bimap is immutable, it returns itself.
     */
    ImmutableBiMap<K, V> toImmutable();

    <R> BiMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    BiMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    <K2, V2> BiMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    BiMap<K, V> reject(Predicate2<? super K, ? super V> predicate);
}
