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

package com.gs.collections.impl.lazy.parallel.list;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.lazy.parallel.AbstractBatch;
import com.gs.collections.impl.lazy.parallel.Batch;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;
import com.gs.collections.impl.utility.Iterate;

@Beta
public class FlatCollectListBatch<T, V> extends AbstractBatch<V> implements ListBatch<V>
{
    private final Batch<T> batch;
    private final Function<? super T, ? extends Iterable<V>> function;

    public FlatCollectListBatch(Batch<T> batch, Function<? super T, ? extends Iterable<V>> function)
    {
        this.batch = batch;
        this.function = function;
    }

    public void forEach(final Procedure<? super V> procedure)
    {
        this.batch.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                Iterate.forEach(FlatCollectListBatch.this.function.valueOf(each), procedure);
            }
        });
    }

    public ListBatch<V> select(Predicate<? super V> predicate)
    {
        return new SelectListBatch<V>(this, predicate);
    }

    public <VV> ListBatch<VV> collect(Function<? super V, ? extends VV> function)
    {
        return new CollectListBatch<V, VV>(this, function);
    }

    public <VV> ListBatch<VV> flatCollect(Function<? super V, ? extends Iterable<VV>> function)
    {
        return new FlatCollectListBatch<V, VV>(this, function);
    }

    public UnsortedSetBatch<V> distinct(ConcurrentHashMap<V, Boolean> distinct)
    {
        return new DistinctBatch<V>(this, distinct);
    }
}
