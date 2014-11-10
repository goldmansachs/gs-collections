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

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.impl.lazy.AbstractLazyIterable;

@Beta
public final class ListIterableParallelIterable<T> extends AbstractParallelListIterable<T, RootListBatch<T>>
{
    private final ListIterable<T> list;
    private final ExecutorService executorService;
    private final int batchSize;

    public ListIterableParallelIterable(ListIterable<T> list, ExecutorService executorService, int batchSize)
    {
        if (executorService == null)
        {
            throw new NullPointerException();
        }
        if (batchSize < 1)
        {
            throw new IllegalArgumentException();
        }
        this.list = list;
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

    public void each(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        forEach(this, procedure);
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return anySatisfy(this, predicate);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return allSatisfy(this, predicate);
    }

    public T detect(Predicate<? super T> predicate)
    {
        return detect(this, predicate);
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
            return this.chunkIndex * ListIterableParallelIterable.this.getBatchSize() < ListIterableParallelIterable.this.list.size();
        }

        public RootListBatch<T> next()
        {
            int chunkStartIndex = this.chunkIndex * ListIterableParallelIterable.this.getBatchSize();
            int chunkEndIndex = (this.chunkIndex + 1) * ListIterableParallelIterable.this.getBatchSize();
            int truncatedChunkEndIndex = Math.min(chunkEndIndex, ListIterableParallelIterable.this.list.size());
            this.chunkIndex++;
            return new ListIterableBatch<T>(ListIterableParallelIterable.this.list, chunkStartIndex, truncatedChunkEndIndex);
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot call remove() on " + ListIterableParallelIterable.this.list.getClass().getSimpleName());
        }
    }

    private class ListIterableParallelBatchLazyIterable
            extends AbstractLazyIterable<RootListBatch<T>>
    {
        public void forEach(Procedure<? super RootListBatch<T>> procedure)
        {
            this.each(procedure);
        }

        public void each(Procedure<? super RootListBatch<T>> procedure)
        {
            for (RootListBatch<T> chunk : this)
            {
                procedure.value(chunk);
            }
        }

        public <P> void forEachWith(Procedure2<? super RootListBatch<T>, ? super P> procedure, P parameter)
        {
            for (RootListBatch<T> chunk : this)
            {
                procedure.value(chunk, parameter);
            }
        }

        public void forEachWithIndex(ObjectIntProcedure<? super RootListBatch<T>> objectIntProcedure)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".forEachWithIndex() not implemented yet");
        }

        public Iterator<RootListBatch<T>> iterator()
        {
            return new ListIterableParallelBatchIterator();
        }
    }
}
