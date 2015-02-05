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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.lazy.parallel.AbstractBatch;
import com.gs.collections.impl.lazy.parallel.Batch;
import com.gs.collections.impl.utility.Iterate;

@Beta
public class FlatCollectUnsortedBagBatch<T, V> extends AbstractBatch<V> implements UnsortedBagBatch<V>
{
    private final Batch<T> unsortedBagBatch;
    private final Function<? super T, ? extends Iterable<V>> function;

    public FlatCollectUnsortedBagBatch(Batch<T> unsortedBagBatch, Function<? super T, ? extends Iterable<V>> function)
    {
        this.unsortedBagBatch = unsortedBagBatch;
        this.function = function;
    }

    public void forEach(final Procedure<? super V> procedure)
    {
        this.unsortedBagBatch.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                Iterate.forEach(FlatCollectUnsortedBagBatch.this.function.valueOf(each), new Procedure<V>()
                {
                    public void value(V each)
                    {
                        procedure.value(each);
                    }
                });
            }
        });
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super V> procedure)
    {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public UnsortedBagBatch<V> select(Predicate<? super V> predicate)
    {
        return new SelectUnsortedBagBatch<V>(this, predicate);
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
