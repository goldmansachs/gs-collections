/*
 * Copyright 2012 Goldman Sachs.
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
import com.gs.collections.api.multimap.MutableMultimap;

/**
 * MultimapPutProcedure uses an Function to calculate the key for an object and puts the object with the key
 * into the specified {@link MutableMultimap}.
 */
public final class MultimapPutProcedure<K, V> implements Procedure<V>
{
    private static final long serialVersionUID = 1L;

    private final MutableMultimap<K, V> multimap;
    private final Function<? super V, ? extends K> keyFunction;

    public MultimapPutProcedure(
            MutableMultimap<K, V> multimap,
            Function<? super V, ? extends K> keyFunction)
    {
        this.multimap = multimap;
        this.keyFunction = keyFunction;
    }

    public static <K, V> MultimapPutProcedure<K, V> on(
            MutableMultimap<K, V> multimap,
            Function<? super V, ? extends K> keyFunction)
    {
        return new MultimapPutProcedure<K, V>(multimap, keyFunction);
    }

    public void value(V each)
    {
        K key = this.keyFunction.valueOf(each);
        this.multimap.put(key, each);
    }
}
