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

package com.gs.collections.impl.parallel;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.block.procedure.CollectProcedure;
import com.gs.collections.impl.list.mutable.FastList;

public final class CollectProcedureFactory<T, V> implements ProcedureFactory<CollectProcedure<T, V>>
{
    private final int collectionSize;
    private final Function<? super T, V> function;

    public CollectProcedureFactory(Function<? super T, V> function, int newTaskSize)
    {
        this.collectionSize = newTaskSize;
        this.function = function;
    }

    public CollectProcedure<T, V> create()
    {
        return new CollectProcedure<T, V>(this.function, new FastList<V>(this.collectionSize));
    }
}
