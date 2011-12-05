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

import java.util.Map;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;

/**
 * MapEntryToProcedure2 translates the result of calling entrySet() on a Map, which results in a collection
 * of Map.Entry objects into corresponding Procedure2s.  This removes the need to deal with Map.Entry when
 * iterating over an entrySet.
 */
public final class MapEntryToProcedure2<K, V> implements Procedure<Map.Entry<K, V>>
{
    private static final long serialVersionUID = 1L;

    private final Procedure2<? super K, ? super V> procedure;

    public MapEntryToProcedure2(Procedure2<? super K, ? super V> procedure)
    {
        this.procedure = procedure;
    }

    public void value(Map.Entry<K, V> entry)
    {
        this.procedure.value(entry.getKey(), entry.getValue());
    }
}
