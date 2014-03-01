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

@Beta
class ParallelCollectUnsortedSetIterable<T, V> extends AbstractParallelUnsortedSetIterable<V, UnsortedSetBatch<V>>
{
    private final AbstractParallelUnsortedSetIterable<T, ? extends UnsortedSetBatch<T>> parallelSetIterable;
    private final Function<? super T, ? extends V> function;

    ParallelCollectUnsortedSetIterable(AbstractParallelUnsortedSetIterable<T, ? extends UnsortedSetBatch<T>> parallelSetIterable, Function<? super T, ? extends V> function)
    {
        this.parallelSetIterable = parallelSetIterable;
        this.function = function;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelSetIterable.getExecutorService();
    }

    @Override
    public LazyIterable<UnsortedSetBatch<V>> split()
    {
        return this.parallelSetIterable.split().collect(new Function<UnsortedSetBatch<T>, UnsortedSetBatch<V>>()
        {
            public UnsortedSetBatch<V> valueOf(UnsortedSetBatch<T> eachBatch)
            {
                return eachBatch.collect(ParallelCollectUnsortedSetIterable.this.function);
            }
        });
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.parallelSetIterable.forEach(Functions.bind(procedure, this.function));
    }

    @Override
    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.parallelSetIterable.anySatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    @Override
    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.parallelSetIterable.allSatisfy(Predicates.attributePredicate(this.function, predicate));
    }

    @Override
    public V detect(Predicate<? super V> predicate)
    {
        T resultItem = this.parallelSetIterable.detect(Predicates.attributePredicate(this.function, predicate));
        return resultItem == null ? null : this.function.valueOf(resultItem);
    }
}
