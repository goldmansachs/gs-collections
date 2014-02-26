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
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.IfProcedure;

@Beta
class ParallelSelectUnsortedBag<T> extends AbstractParallelUnsortedBag<T>
{
    private final AbstractParallelUnsortedBag<T> parallelUnsortedBag;
    private final Predicate<? super T> predicate;

    ParallelSelectUnsortedBag(AbstractParallelUnsortedBag<T> parallelUnsortedBag, Predicate<? super T> predicate)
    {
        this.parallelUnsortedBag = parallelUnsortedBag;
        this.predicate = predicate;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.parallelUnsortedBag.forEach(new IfProcedure<T>(this.predicate, procedure));
    }

    public void forEachWithOccurrences(final ObjectIntProcedure<? super T> procedure)
    {
        this.parallelUnsortedBag.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int parameter)
            {
                if (ParallelSelectUnsortedBag.this.predicate.accept(each))
                {
                    procedure.value(each, parameter);
                }
            }
        });
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.parallelUnsortedBag.anySatisfy(Predicates.and(this.predicate, predicate));
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.parallelUnsortedBag.allSatisfy(new SelectUnsortedBagAllSatisfyPredicate<T>(this.predicate, predicate));
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.parallelUnsortedBag.getExecutorService();
    }

    @Override
    public LazyIterable<UnsortedBagBatch<T>> split()
    {
        return this.parallelUnsortedBag.split().collect(new Function<UnsortedBagBatch<T>, UnsortedBagBatch<T>>()
        {
            public UnsortedBagBatch<T> valueOf(UnsortedBagBatch<T> eachBatch)
            {
                return eachBatch.select(ParallelSelectUnsortedBag.this.predicate);
            }
        });
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return this.parallelUnsortedBag.detect(Predicates.and(this.predicate, predicate));
    }

    private static final class SelectUnsortedBagAllSatisfyPredicate<T> implements Predicate<T>
    {
        private final Predicate<? super T> left;
        private final Predicate<? super T> right;

        private SelectUnsortedBagAllSatisfyPredicate(Predicate<? super T> left, Predicate<? super T> right)
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
