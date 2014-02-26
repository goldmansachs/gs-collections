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

package com.gs.collections.impl.lazy.parallel.list;

import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;

@Beta
class ParallelCollectListIterable<T, V> extends AbstractParallelListIterable<V>
{
    private final AbstractParallelListIterable<T> parallelListIterable;
    private final Function<? super T, ? extends V> function;

    ParallelCollectListIterable(AbstractParallelListIterable<T> parallelListIterable, Function<? super T, ? extends V> function)
    {
        this.parallelListIterable = parallelListIterable;
        this.function = function;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelListIterable.getExecutorService();
    }

    @Override
    public LazyIterable<ListBatch<V>> split()
    {
        return this.parallelListIterable.split().collect(new Function<ListBatch<T>, ListBatch<V>>()
        {
            public ListBatch<V> valueOf(ListBatch<T> eachBatch)
            {
                return eachBatch.collect(ParallelCollectListIterable.this.function);
            }
        });
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.parallelListIterable.forEach(Functions.bind(procedure, this.function));
    }

    @Override
    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.parallelListIterable.anySatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    @Override
    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.parallelListIterable.allSatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    @Override
    public V detect(Predicate<? super V> predicate)
    {
        T resultItem = this.parallelListIterable.detect(Predicates.attributePredicate(this.function, predicate));
        return resultItem == null ? null : this.function.valueOf(resultItem);
    }
}
