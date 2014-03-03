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

package com.gs.collections.impl.lazy.parallel.set;

import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterableImpl;
import com.gs.collections.impl.lazy.parallel.Batch;

@Beta
public class ParallelCollectIterable<T, V> extends AbstractParallelIterableImpl<V, Batch<V>>
{
    private final AbstractParallelIterable<T, ? extends Batch<T>> parallelIterable;
    private final Function<? super T, ? extends V> function;

    public ParallelCollectIterable(AbstractParallelIterable<T, ? extends Batch<T>> parallelIterable, Function<? super T, ? extends V> function)
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
    public LazyIterable<Batch<V>> split()
    {
        return this.parallelIterable.split().collect(new Function<Batch<T>, Batch<V>>()
        {
            public Batch<V> valueOf(Batch<T> eachBatch)
            {
                return eachBatch.collect(ParallelCollectIterable.this.function);
            }
        });
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.parallelIterable.forEach(Functions.bind(procedure, this.function));
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.parallelIterable.anySatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.parallelIterable.allSatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    public V detect(Predicate<? super V> predicate)
    {
        T resultItem = this.parallelIterable.detect(Predicates.attributePredicate(this.function, predicate));
        return resultItem == null ? null : this.function.valueOf(resultItem);
    }
}
