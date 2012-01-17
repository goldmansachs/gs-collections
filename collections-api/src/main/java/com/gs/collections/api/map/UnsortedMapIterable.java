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

package com.gs.collections.api.map;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.tuple.Pair;

/**
 * An iterable whose elements are unique.
 */
public interface UnsortedMapIterable<K, V>
        extends MapIterable<K, V>
{
    UnsortedMapIterable<K, V> filter(Predicate2<? super K, ? super V> predicate);

    UnsortedMapIterable<K, V> filterNot(Predicate2<? super K, ? super V> predicate);

    <R> UnsortedMapIterable<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function);

    <K2, V2> UnsortedMapIterable<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function);
}
