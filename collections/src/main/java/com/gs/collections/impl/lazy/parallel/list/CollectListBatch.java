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

package com.gs.collections.impl.lazy.parallel.list;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Functions;

@Beta
class CollectListBatch<T, V> extends AbstractListBatch<V>
{
    private final ListBatch<T> listBatch;
    private final Function<? super T, ? extends V> function;

    CollectListBatch(ListBatch<T> listBatch, Function<? super T, ? extends V> function)
    {
        this.listBatch = listBatch;
        this.function = function;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.listBatch.forEach(Functions.bind(procedure, this.function));
    }

    /*
    public <VV> ListBatch<VV> collect(Function<? super V, ? extends VV> function)
    {
        return new CollectListBatch<T, VV>(this.listBatch, Functions.chain(this.function, function));
    }
    */
}
