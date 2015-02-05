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

import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterableImpl;
import com.gs.collections.impl.lazy.parallel.Batch;

@Beta
public class ParallelCollectIterable<T, V> extends AbstractParallelIterableImpl<V, Batch<V>>
{
    private final AbstractParallelIterable<T, ? extends Batch<T>> delegate;
    private final Function<? super T, ? extends V> function;

    public ParallelCollectIterable(AbstractParallelIterable<T, ? extends Batch<T>> delegate, Function<? super T, ? extends V> function)
    {
        this.delegate = delegate;
        this.function = function;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.delegate.getExecutorService();
    }

    @Override
    public int getBatchSize()
    {
        return this.delegate.getBatchSize();
    }

    @Override
    public LazyIterable<Batch<V>> split()
    {
        return this.delegate.split().collect(new Function<Batch<T>, Batch<V>>()
        {
            public Batch<V> valueOf(Batch<T> eachBatch)
            {
                return eachBatch.collect(ParallelCollectIterable.this.function);
            }
        });
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.delegate.forEach(Functions.bind(procedure, this.function));
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.delegate.anySatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.delegate.allSatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    public V detect(Predicate<? super V> predicate)
    {
        T resultItem = this.delegate.detect(Predicates.attributePredicate(this.function, predicate));
        return resultItem == null ? null : this.function.valueOf(resultItem);
    }

    @Override
    public <V1> ParallelIterable<V1> flatCollect(Function<? super V, ? extends Iterable<V1>> function)
    {
        return new ParallelFlatCollectIterable<V, V1>(this, function);
    }

    @Override
    public Object[] toArray()
    {
        // TODO: Implement in parallel
        return this.delegate.toList().collect(this.function).toArray();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        // TODO: Implement in parallel
        return this.delegate.toList().collect(this.function).toArray(array);
    }

    @Override
    public <V1> UnsortedBagMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toBag().collect(this.function).groupBy(function);
    }

    @Override
    public <V1> UnsortedBagMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toBag().collect(this.function).groupByEach(function);
    }

    @Override
    public <V1> MapIterable<V1, V> groupByUniqueKey(Function<? super V, ? extends V1> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toBag().collect(this.function).groupByUniqueKey(function);
    }
}
