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

package com.webguys.ponzu.impl.block.procedure;

import java.util.Map;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.impl.block.factory.Functions;

/**
 * MapCollectProcedure uses an Function to calculate the key for an object and puts the object with the key
 * into the specified Map.
 */
public final class MapTransformProcedure<T, K, V> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Map<K, V> map;
    private final Function<? super T, ? extends K> keyFunction;
    private final Function<? super T, ? extends V> valueFunction;

    public MapTransformProcedure(Map<K, V> newMap, Function<? super T, ? extends K> newKeyFunction)
    {
        this(newMap, newKeyFunction, (Function<T, V>) Functions.getPassThru());
    }

    public MapTransformProcedure(Map<K, V> newMap, Function<? super T, ? extends K> newKeyFunction, Function<? super T, ? extends V> newValueFunction)
    {
        this.map = newMap;
        this.keyFunction = newKeyFunction;
        this.valueFunction = newValueFunction;
    }

    @Override
    public void value(T object)
    {
        this.map.put(this.keyFunction.valueOf(object), this.valueFunction.valueOf(object));
    }
}
