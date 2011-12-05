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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.utility.Iterate;

/**
 * MultimapEachPutProcedure uses an Function to calculate the keys for an object and puts the object with each of
 * the keys into the specified {@link MutableMultimap}.
 */
public final class MultimapEachPutProcedure<K, V> implements Procedure<V>
{
    private static final long serialVersionUID = 1L;
    private final MutableMultimap<K, V> multimap;
    private final Function<? super V, ? extends Iterable<K>> keyFunction;
    private final Procedure2<K, V> eachProcedure = new Procedure2<K, V>()
    {
        public void value(K key, V value)
        {
            MultimapEachPutProcedure.this.multimap.put(key, value);
        }
    };

    public MultimapEachPutProcedure(
            MutableMultimap<K, V> multimap,
            Function<? super V, ? extends Iterable<K>> keyFunction)
    {
        this.multimap = multimap;
        this.keyFunction = keyFunction;
    }

    public static <K, V> MultimapEachPutProcedure<K, V> on(
            MutableMultimap<K, V> multimap,
            Function<? super V, ? extends Iterable<K>> keyFunction)
    {
        return new MultimapEachPutProcedure<K, V>(multimap, keyFunction);
    }

    public void value(V each)
    {
        Iterate.forEachWith(this.keyFunction.valueOf(each), this.eachProcedure, each);
    }
}
