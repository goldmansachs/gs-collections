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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;
import com.gs.collections.impl.lazy.parallel.OrderedBatch;
import com.gs.collections.impl.utility.Iterate;

@Beta
public class ParallelFlatCollectListIterable<T, V> extends AbstractParallelListIterable<V, ListBatch<V>>
{
    private final AbstractParallelIterable<T, ? extends OrderedBatch<T>> parallelIterable;
    private final Function<? super T, ? extends Iterable<V>> function;

    public ParallelFlatCollectListIterable(AbstractParallelIterable<T, ? extends OrderedBatch<T>> parallelIterable, Function<? super T, ? extends Iterable<V>> function)
    {
        this.parallelIterable = parallelIterable;
        this.function = function;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelIterable.getExecutorService();
    }

    @Override
    public int getBatchSize()
    {
        return this.parallelIterable.getBatchSize();
    }

    @Override
    public LazyIterable<ListBatch<V>> split()
    {
        return this.parallelIterable.split().collect(new Function<OrderedBatch<T>, ListBatch<V>>()
        {
            public ListBatch<V> valueOf(OrderedBatch<T> batch)
            {
                return batch.flatCollect(ParallelFlatCollectListIterable.this.function);
            }
        });
    }

    public void forEach(final Procedure<? super V> procedure)
    {
        this.parallelIterable.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                Iterate.forEach(ParallelFlatCollectListIterable.this.function.valueOf(each), procedure);
            }
        });
    }

    public V detect(final Predicate<? super V> predicate)
    {
        // Some predicates are stateful, so they cannot be called more than once pre element,
        // that's why we use an AtomicReference to return the accepted element
        final AtomicReference<V> result = new AtomicReference<V>();
        this.parallelIterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return Iterate.anySatisfy(ParallelFlatCollectListIterable.this.function.valueOf(each), new Predicate<V>()
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
        return this.parallelIterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return Iterate.anySatisfy(ParallelFlatCollectListIterable.this.function.valueOf(each), predicate);
            }
        });
    }

    public boolean allSatisfy(final Predicate<? super V> predicate)
    {
        return this.parallelIterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return Iterate.allSatisfy(ParallelFlatCollectListIterable.this.function.valueOf(each), predicate);
            }
        });
    }
}
