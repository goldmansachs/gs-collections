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
import com.gs.collections.impl.block.procedure.IfProcedure;

@Beta
class ParallelSelectListIterable<T> extends AbstractParallelListIterable<T>
{
    private final AbstractParallelListIterable<T> parallelListIterable;
    private final Predicate<? super T> predicate;

    ParallelSelectListIterable(AbstractParallelListIterable<T> parallelListIterable, Predicate<? super T> predicate)
    {
        this.parallelListIterable = parallelListIterable;
        this.predicate = predicate;
    }

    public LazyIterable<ListBatch<T>> split()
    {
        return this.parallelListIterable.split().collect(new Function<ListBatch<T>, ListBatch<T>>()
        {
            public ListBatch<T> valueOf(ListBatch<T> eachBatch)
            {
                return eachBatch.select(ParallelSelectListIterable.this.predicate);
            }
        });
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.parallelListIterable.forEach(new IfProcedure<T>(this.predicate, procedure));
    }

    public ExecutorService getExecutorService()
    {
        return this.parallelListIterable.getExecutorService();
    }
}
