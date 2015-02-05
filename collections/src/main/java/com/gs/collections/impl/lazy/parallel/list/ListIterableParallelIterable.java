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

import java.util.Iterator;
import java.util.RandomAccess;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.impl.lazy.AbstractLazyIterable;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;

@Beta
public final class ListIterableParallelIterable<T> extends AbstractParallelListIterable<T, RootListBatch<T>>
{
    private final ListIterable<T> delegate;
    private final ExecutorService executorService;
    private final int batchSize;

    public ListIterableParallelIterable(ListIterable<T> delegate, ExecutorService executorService, int batchSize)
    {
        if (executorService == null)
        {
            throw new NullPointerException();
        }
        if (batchSize < 1)
        {
            throw new IllegalArgumentException();
        }
        if (!(delegate instanceof RandomAccess))
        {
            throw new IllegalArgumentException();
        }
        this.delegate = delegate;
        this.executorService = executorService;
        this.batchSize = batchSize;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.executorService;
    }

    @Override
    public LazyIterable<RootListBatch<T>> split()
    {
        return new ListIterableParallelBatchLazyIterable();
    }

    public void forEach(Procedure<? super T> procedure)
    {
        AbstractParallelIterable.forEach(this, procedure);
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return AbstractParallelIterable.anySatisfy(this, predicate);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return AbstractParallelIterable.allSatisfy(this, predicate);
    }

    public T detect(Predicate<? super T> predicate)
    {
        return AbstractParallelIterable.detect(this, predicate);
    }

    @Override
    public <V> ParallelListIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new ParallelFlatCollectListIterable<T, V>(this, function);
    }

    @Override
    public Object[] toArray()
    {
        // TODO: Implement in parallel
        return this.delegate.toArray();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        // TODO: Implement in parallel
        return this.delegate.toArray(array);
    }

    @Override
    public <V> ListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.delegate.groupBy(function);
    }

    @Override
    public <V> ListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        // TODO: Implement in parallel
        return this.delegate.groupByEach(function);
    }

    @Override
    public <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.delegate.groupByUniqueKey(function);
    }

    public int getBatchSize()
    {
        return this.batchSize;
    }

    private class ListIterableParallelBatchIterator implements Iterator<RootListBatch<T>>
    {
        protected int chunkIndex;

        public boolean hasNext()
        {
            return this.chunkIndex * ListIterableParallelIterable.this.getBatchSize() < ListIterableParallelIterable.this.delegate.size();
        }

        public RootListBatch<T> next()
        {
            int chunkStartIndex = this.chunkIndex * ListIterableParallelIterable.this.getBatchSize();
            int chunkEndIndex = (this.chunkIndex + 1) * ListIterableParallelIterable.this.getBatchSize();
            int truncatedChunkEndIndex = Math.min(chunkEndIndex, ListIterableParallelIterable.this.delegate.size());
            this.chunkIndex++;
            return new ListIterableBatch<T>(ListIterableParallelIterable.this.delegate, chunkStartIndex, truncatedChunkEndIndex);
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot call remove() on " + ListIterableParallelIterable.this.delegate.getClass().getSimpleName());
        }
    }

    private class ListIterableParallelBatchLazyIterable
            extends AbstractLazyIterable<RootListBatch<T>>
    {
        public void each(Procedure<? super RootListBatch<T>> procedure)
        {
            for (RootListBatch<T> chunk : this)
            {
                procedure.value(chunk);
            }
        }

        public Iterator<RootListBatch<T>> iterator()
        {
            return new ListIterableParallelBatchIterator();
        }
    }
}
