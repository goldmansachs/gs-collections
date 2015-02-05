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
import java.util.concurrent.atomic.AtomicReference;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterableImpl;
import com.gs.collections.impl.lazy.parallel.Batch;
import com.gs.collections.impl.utility.Iterate;

@Beta
public class ParallelFlatCollectIterable<T, V> extends AbstractParallelIterableImpl<V, Batch<V>>
{
    private final AbstractParallelIterable<T, ? extends Batch<T>> delegate;
    private final Function<? super T, ? extends Iterable<V>> function;

    public ParallelFlatCollectIterable(AbstractParallelIterable<T, ? extends Batch<T>> delegate, Function<? super T, ? extends Iterable<V>> function)
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
            public Batch<V> valueOf(Batch<T> batch)
            {
                return batch.flatCollect(ParallelFlatCollectIterable.this.function);
            }
        });
    }

    public void forEach(final Procedure<? super V> procedure)
    {
        this.delegate.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                Iterate.forEach(ParallelFlatCollectIterable.this.function.valueOf(each), procedure);
            }
        });
    }

    public V detect(final Predicate<? super V> predicate)
    {
        final AtomicReference<V> result = new AtomicReference<V>();
        this.delegate.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return Iterate.anySatisfy(ParallelFlatCollectIterable.this.function.valueOf(each), new Predicate<V>()
                {
                    public boolean accept(V each)
                    {
                        if (predicate.accept(each))
                        {
                            result.compareAndSet(null, each);
                            return true;
                        }

                        return false;
                    }
                });
            }
        });

        return result.get();
    }

    public boolean anySatisfy(final Predicate<? super V> predicate)
    {
        return this.delegate.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return Iterate.anySatisfy(ParallelFlatCollectIterable.this.function.valueOf(each), predicate);
            }
        });
    }

    public boolean allSatisfy(final Predicate<? super V> predicate)
    {
        return this.delegate.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return Iterate.allSatisfy(ParallelFlatCollectIterable.this.function.valueOf(each), predicate);
            }
        });
    }

    @Override
    public Object[] toArray()
    {
        // TODO: Implement in parallel
        return this.delegate.toList().flatCollect(this.function).toArray();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        // TODO: Implement in parallel
        return this.delegate.toList().flatCollect(this.function).toArray(array);
    }

    @Override
    public <V1> UnsortedBagMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toBag().flatCollect(this.function).groupBy(function);
    }

    @Override
    public <V1> UnsortedBagMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toBag().flatCollect(this.function).groupByEach(function);
    }

    @Override
    public <V1> MapIterable<V1, V> groupByUniqueKey(Function<? super V, ? extends V1> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toBag().flatCollect(this.function).groupByUniqueKey(function);
    }
}
