/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.lazy.parallel.set;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.lazy.parallel.AbstractBatch;
import com.gs.collections.impl.lazy.parallel.bag.CollectUnsortedBagBatch;
import com.gs.collections.impl.lazy.parallel.bag.FlatCollectUnsortedBagBatch;
import com.gs.collections.impl.lazy.parallel.bag.UnsortedBagBatch;

@Beta
public class CollectUnsortedSetBatch<T, V> extends AbstractBatch<V> implements UnsortedSetBatch<V>
{
    private final UnsortedSetBatch<T> unsortedSetBatch;
    private final Function<? super T, ? extends V> function;

    public CollectUnsortedSetBatch(UnsortedSetBatch<T> unsortedSetBatch, Function<? super T, ? extends V> function)
    {
        this.unsortedSetBatch = unsortedSetBatch;
        this.function = function;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.unsortedSetBatch.forEach(Functions.bind(procedure, this.function));
    }

    /*
    public <VV> SetBatch<VV> collect(Function<? super V, ? extends VV> function)
    {
        return new CollectSetBatch<T, VV>(this.setBatch, Functions.chain(this.function, function));
    }
    */

    public UnsortedSetBatch<V> select(Predicate<? super V> predicate)
    {
        return new SelectUnsortedSetBatch<V>(this, predicate);
    }

    public <VV> UnsortedBagBatch<VV> collect(Function<? super V, ? extends VV> function)
    {
        return new CollectUnsortedBagBatch<V, VV>(this, function);
    }

    public <V1> UnsortedBagBatch<V1> flatCollect(Function<? super V, ? extends Iterable<V1>> function)
    {
        return new FlatCollectUnsortedBagBatch<V, V1>(this, function);
    }
}
