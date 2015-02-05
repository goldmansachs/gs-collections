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
import com.gs.collections.impl.lazy.parallel.bag.CollectUnsortedBagBatch;
import com.gs.collections.impl.lazy.parallel.bag.FlatCollectUnsortedBagBatch;
import com.gs.collections.impl.lazy.parallel.bag.UnsortedBagBatch;
import com.gs.collections.impl.lazy.parallel.set.SelectUnsortedSetBatch;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

@Beta
public class DistinctBatch<T> extends AbstractBatch<T> implements UnsortedSetBatch<T>
{
    private final Batch<T> batch;
    private final ConcurrentHashMap<T, Boolean> distinct;

    public DistinctBatch(Batch<T> batch, ConcurrentHashMap<T, Boolean> distinct)
    {
        this.batch = batch;
        this.distinct = distinct;
    }

    public void forEach(final Procedure<? super T> procedure)
    {
        this.batch.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                if (DistinctBatch.this.distinct.put(each, true) == null)
                {
                    procedure.value(each);
                }
            }
        });
    }

    public UnsortedSetBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectUnsortedSetBatch<T>(this, predicate);
    }

    public <V> UnsortedBagBatch<V> collect(Function<? super T, ? extends V> function)
    {
        return new CollectUnsortedBagBatch<T, V>(this, function);
    }

    public <V> UnsortedBagBatch<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new FlatCollectUnsortedBagBatch<T, V>(this, function);
    }
}
