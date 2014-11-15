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

package com.gs.collections.impl.block.procedure;

import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;

public class GroupByUniqueKeyProcedure<T, K> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Map<K, T> map;
    private final Function<? super T, ? extends K> keyFunction;

    public GroupByUniqueKeyProcedure(Map<K, T> newMap, Function<? super T, ? extends K> newKeyFunction)
    {
        this.map = newMap;
        this.keyFunction = newKeyFunction;
    }

    public void value(T object)
    {
        K key = this.keyFunction.valueOf(object);
        if (this.map.put(key, object) != null)
        {
            throw new IllegalStateException("Key " + key + " already exists in map!");
        }
    }
}
