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

package com.gs.collections.impl.lazy.parallel.set.sorted;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.procedure.IfProcedure;
import com.gs.collections.impl.lazy.parallel.AbstractBatch;
import com.gs.collections.impl.lazy.parallel.list.ListBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

@Beta
public class SelectSortedSetBatch<T> extends AbstractBatch<T> implements SortedSetBatch<T>
{
    private final SortedSetBatch<T> sortedSetBatch;
    private final Predicate<? super T> predicate;

    public SelectSortedSetBatch(SortedSetBatch<T> sortedSetBatch, Predicate<? super T> predicate)
    {
        this.sortedSetBatch = sortedSetBatch;
        this.predicate = predicate;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.sortedSetBatch.forEach(new IfProcedure<T>(this.predicate, procedure));
    }

    /*
    public SetBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectSetBatch<T>(this.setBatch, Predicates.and(this.predicate, predicate));
    }
    */

    public SortedSetBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectSortedSetBatch<T>(this, predicate);
    }

    public <V> ListBatch<V> collect(Function<? super T, ? extends V> function)
    {
        return new CollectSortedSetBatch<T, V>(this, function);
    }

    public <V> ListBatch<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new FlatCollectSortedSetBatch<T, V>(this, function);
    }

    public SortedSetBatch<T> distinct(ConcurrentHashMap<T, Boolean> distinct)
    {
        return this;
    }
}
