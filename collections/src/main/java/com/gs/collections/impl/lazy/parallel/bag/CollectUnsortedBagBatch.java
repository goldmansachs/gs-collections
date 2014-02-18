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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.block.factory.Functions;

@Beta
class CollectUnsortedBagBatch<T, V> extends AbstractUnsortedBagBatch<V>
{
    private final UnsortedBagBatch<T> unsortedBagBatch;
    private final Function<? super T, ? extends V> function;

    CollectUnsortedBagBatch(UnsortedBagBatch<T> unsortedBagBatch, Function<? super T, ? extends V> function)
    {
        this.unsortedBagBatch = unsortedBagBatch;
        this.function = function;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.unsortedBagBatch.forEach(Functions.bind(procedure, this.function));
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super V> procedure)
    {
        this.unsortedBagBatch.forEachWithOccurrences(Functions.bind(procedure, this.function));
    }

    /*
    public <VV> BagBatch<VV> collect(Function<? super V, ? extends VV> function)
    {
        return new CollectBagBatch<T, VV>(this.bagBatch, Functions.chain(this.function, function));
    }
    */
}
