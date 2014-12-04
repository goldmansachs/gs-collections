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
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.set.UnsortedSetMultimap;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.set.AbstractParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;

@Beta
class ParallelDistinctListIterable<T> extends AbstractParallelUnsortedSetIterable<T, UnsortedSetBatch<T>>
{
    private final AbstractParallelListIterable<T, ? extends ListBatch<T>> delegate;

    ParallelDistinctListIterable(AbstractParallelListIterable<T, ? extends ListBatch<T>> delegate)
    {
        this.delegate = delegate;
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
    public LazyIterable<UnsortedSetBatch<T>> split()
    {
        // TODO: Replace the map with a concurrent set once it's implemented
        final ConcurrentHashMap<T, Boolean> distinct = new ConcurrentHashMap<T, Boolean>();
        return this.delegate.split().collect(new Function<ListBatch<T>, UnsortedSetBatch<T>>()
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
        this.delegate.forEach(new Procedure<T>()
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
        return this.delegate.anySatisfy(new DistinctAndPredicate<T>(predicate));
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.delegate.allSatisfy(new DistinctOrPredicate<T>(predicate));
    }

    public T detect(Predicate<? super T> predicate)
    {
        return this.delegate.detect(new DistinctAndPredicate<T>(predicate));
    }

    @Override
    public Object[] toArray()
    {
        // TODO: Implement in parallel
        return this.delegate.toList().distinct().toArray();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        // TODO: Implement in parallel
        return this.delegate.toList().distinct().toArray(array);
    }

    @Override
    public <V> UnsortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toSet().groupBy(function, new UnifiedSetMultimap<V, T>());
    }

    @Override
    public <V> UnsortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toSet().groupByEach(function, new UnifiedSetMultimap<V, T>());
    }

    @Override
    public <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.delegate.toSet().groupByUniqueKey(function);
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
            boolean distinct = this.distinct.put(each, true) == null;
            return distinct && this.predicate.accept(each) || !distinct;
        }
    }
}
