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

package com.gs.collections.impl.lazy.parallel.set.sorted;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.IfProcedure;

@Beta
class ParallelSelectSortedSetIterable<T> extends AbstractParallelSortedSetIterable<T, SortedSetBatch<T>>
{
    private final AbstractParallelSortedSetIterable<T, ? extends SortedSetBatch<T>> parallelIterable;
    private final Predicate<? super T> predicate;

    ParallelSelectSortedSetIterable(AbstractParallelSortedSetIterable<T, ? extends SortedSetBatch<T>> parallelIterable, Predicate<? super T> predicate)
    {
        this.parallelIterable = parallelIterable;
        this.predicate = predicate;
    }

    public Comparator<? super T> comparator()
    {
        return this.parallelIterable.comparator();
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelIterable.getExecutorService();
    }

    @Override
    public int getBatchSize()
    {
        return this.parallelIterable.getBatchSize();
    }

    @Override
    public LazyIterable<SortedSetBatch<T>> split()
    {
        return this.parallelIterable.split().collect(new Function<SortedSetBatch<T>, SortedSetBatch<T>>()
        {
            public SortedSetBatch<T> valueOf(SortedSetBatch<T> eachBatch)
            {
                return eachBatch.select(ParallelSelectSortedSetIterable.this.predicate);
            }
        });
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.parallelIterable.forEach(new IfProcedure<T>(this.predicate, procedure));
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.parallelIterable.anySatisfy(Predicates.and(this.predicate, predicate));
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.parallelIterable.allSatisfy(new SelectAllSatisfyPredicate<T>(this.predicate, predicate));
    }

    public T detect(Predicate<? super T> predicate)
    {
        return this.parallelIterable.detect(Predicates.and(this.predicate, predicate));
    }

    private static final class SelectAllSatisfyPredicate<T> implements Predicate<T>
    {
        private final Predicate<? super T> left;
        private final Predicate<? super T> right;

        private SelectAllSatisfyPredicate(Predicate<? super T> left, Predicate<? super T> right)
        {
            this.left = left;
            this.right = right;
        }

        public boolean accept(T each)
        {
            boolean leftResult = this.left.accept(each);
            return !leftResult || this.right.accept(each);
        }
    }
}
