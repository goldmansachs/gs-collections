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
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.set.AbstractParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

@Beta
class ParallelListDistinctIterable<T> extends AbstractParallelUnsortedSetIterable<T>
{
    private final AbstractParallelListIterable<T> parallelListIterable;

    ParallelListDistinctIterable(AbstractParallelListIterable<T> parallelListIterable)
    {
        this.parallelListIterable = parallelListIterable;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelListIterable.getExecutorService();
    }

    @Override
    public LazyIterable<UnsortedSetBatch<T>> split()
    {
        // TODO: Replace the map with a concurrent set once it's implemented
        final ConcurrentHashMap<T, Boolean> distinct = new ConcurrentHashMap<T, Boolean>();
        return this.parallelListIterable.split().collect(new Function<ListBatch<T>, UnsortedSetBatch<T>>()
        {
            public UnsortedSetBatch<T> valueOf(ListBatch<T> listBatch)
            {
                return listBatch.distinct(distinct);
            }
        });
    }

    @Override
    public ParallelUnsortedSetIterable<T> asUnique()
    {
        return this;
    }

    public void forEach(final Procedure<? super T> procedure)
    {
        // TODO: Replace the map with a concurrent set once it's implemented
        final ConcurrentHashMap<T, Boolean> distinct = new ConcurrentHashMap<T, Boolean>();
        this.parallelListIterable.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                if (distinct.put(each, true) == null)
                {
                    procedure.value(each);
                }
            }
        });
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.parallelListIterable.anySatisfy(new DistinctPredicate<T>(predicate));
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.parallelListIterable.allSatisfy(new DistinctPredicate<T>(predicate));
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return this.parallelListIterable.detect(new DistinctPredicate<T>(predicate));
    }

    private static final class DistinctPredicate<T> implements Predicate<T>
    {
        // TODO: Replace the map with a concurrent set once it's implemented
        private final ConcurrentHashMap<T, Boolean> distinct = new ConcurrentHashMap<T, Boolean>();
        private final Predicate<? super T> predicate;

        private DistinctPredicate(Predicate<? super T> predicate)
        {
            this.predicate = predicate;
        }

        public boolean accept(T each)
        {
            return this.distinct.put(each, true) == null && this.predicate.accept(each);
        }
    }
}
