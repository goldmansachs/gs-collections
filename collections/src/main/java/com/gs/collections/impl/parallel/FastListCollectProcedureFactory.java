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

package com.gs.collections.impl.parallel;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.block.procedure.FastListCollectProcedure;
import com.gs.collections.impl.list.mutable.FastList;

public final class FastListCollectProcedureFactory<T, V> implements ProcedureFactory<FastListCollectProcedure<T, V>>
{
    private final int collectionSize;
    private final Function<? super T, V> function;

    public FastListCollectProcedureFactory(Function<? super T, V> function, int newTaskSize)
    {
        this.collectionSize = newTaskSize;
        this.function = function;
    }

    public FastListCollectProcedure<T, V> create()
    {
        return new FastListCollectProcedure<T, V>(this.function, FastList.<V>newList(this.collectionSize));
    }
}
