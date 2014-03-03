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

package com.gs.collections.impl.lazy.parallel;

import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.list.DistinctBatch;
import com.gs.collections.impl.lazy.parallel.set.AbstractParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

@Beta
public class ParallelDistinctIterable<T> extends AbstractParallelUnsortedSetIterable<T, UnsortedSetBatch<T>>
{
    private final AbstractParallelIterable<T, ? extends Batch<T>> parallelIterable;

    public ParallelDistinctIterable(AbstractParallelIterable<T, ? extends Batch<T>> parallelIterable)
    {
        this.parallelIterable = parallelIterable;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelIterable.getExecutorService();
    }

    @Override
    public LazyIterable<UnsortedSetBatch<T>> split()
    {
        // TODO: Replace the map with a concurrent set once it's implemented
        final ConcurrentHashMap<T, Boolean> distinct = new ConcurrentHashMap<T, Boolean>();
        return this.parallelIterable.split().collect(new Function<Batch<T>, UnsortedSetBatch<T>>()
        {
            public UnsortedSetBatch<T> valueOf(Batch<T> batch)
            {
                return new DistinctBatch<T>(batch, distinct);
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
        this.parallelIterable.forEach(new Procedure<T>()
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

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.parallelIterable.anySatisfy(new DistinctAndPredicate<T>(predicate));
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.parallelIterable.allSatisfy(new DistinctOrPredicate<T>(predicate));
    }

    public T detect(Predicate<? super T> predicate)
    {
        return this.parallelIterable.detect(new DistinctAndPredicate<T>(predicate));
    }

    private static final class DistinctAndPredicate<T> implements Predicate<T>
    {
        // TODO: Replace the map with a concurrent set once it's implemented
        private final ConcurrentHashMap<T, Boolean> distinct = new ConcurrentHashMap<T, Boolean>();
        private final Predicate<? super T> predicate;

        private DistinctAndPredicate(Predicate<? super T> predicate)
        {
            this.predicate = predicate;
        }

        public boolean accept(T each)
        {
            return this.distinct.put(each, true) == null && this.predicate.accept(each);
        }
    }

    private static final class DistinctOrPredicate<T> implements Predicate<T>
    {
        // TODO: Replace the map with a concurrent set once it's implemented
        private final ConcurrentHashMap<T, Boolean> distinct = new ConcurrentHashMap<T, Boolean>();
        private final Predicate<? super T> predicate;

        private DistinctOrPredicate(Predicate<? super T> predicate)
        {
            this.predicate = predicate;
        }

        public boolean accept(T each)
        {
            return this.distinct.put(each, true) != null || this.predicate.accept(each);
        }
    }
}
