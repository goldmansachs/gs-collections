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

package com.gs.collections.impl.lazy.parallel.bag;

import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.set.AbstractParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;

@Beta
class ParallelUnsortedBagDistinctIterable<T> extends AbstractParallelUnsortedSetIterable<T, UnsortedSetBatch<T>>
{
    private final AbstractParallelUnsortedBag<T, UnsortedBagBatch<T>> parallelBagIterable;

    ParallelUnsortedBagDistinctIterable(AbstractParallelUnsortedBag<T, UnsortedBagBatch<T>> parallelBagIterable)
    {
        this.parallelBagIterable = parallelBagIterable;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelBagIterable.getExecutorService();
    }

    @Override
    public LazyIterable<UnsortedSetBatch<T>> split()
    {
        throw new UnsupportedOperationException();
    }

    public ParallelUnsortedSetIterable<T> asUnique()
    {
        return this;
    }

    public void forEach(final Procedure<? super T> procedure)
    {
        this.parallelBagIterable.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                procedure.value(each);
            }
        });
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.parallelBagIterable.anySatisfy(predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.parallelBagIterable.allSatisfy(predicate);
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return this.parallelBagIterable.detect(predicate);
    }
}
