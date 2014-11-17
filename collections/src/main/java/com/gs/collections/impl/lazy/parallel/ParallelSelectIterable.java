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

import java.util.Comparator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.IfProcedure;

@Beta
public class ParallelSelectIterable<T> extends AbstractParallelIterableImpl<T, Batch<T>>
{
    private final AbstractParallelIterable<T, ? extends Batch<T>> parallelIterable;
    private final Predicate<? super T> predicate;

    public ParallelSelectIterable(AbstractParallelIterable<T, ? extends Batch<T>> parallelIterable, Predicate<? super T> predicate)
    {
        this.parallelIterable = parallelIterable;
        this.predicate = predicate;
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
    public LazyIterable<Batch<T>> split()
    {
        return this.parallelIterable.split().collect(new Function<Batch<T>, Batch<T>>()
        {
            public Batch<T> valueOf(Batch<T> eachBatch)
            {
                return eachBatch.select(ParallelSelectIterable.this.predicate);
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

    @Override
    public <V> ParallelIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).flatCollect(function).asParallel(this.getExecutorService(), this.getBatchSize());
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).min(comparator);
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).max(comparator);
    }

    @Override
    public T min()
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).min();
    }

    @Override
    public T max()
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).max();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).minBy(function);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).maxBy(function);
    }

    @Override
    public long sumOfInt(IntFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).sumOfInt(function);
    }

    @Override
    public double sumOfFloat(FloatFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).sumOfFloat(function);
    }

    @Override
    public long sumOfLong(LongFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).sumOfLong(function);
    }

    @Override
    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).sumOfDouble(function);
    }

    @Override
    public Object[] toArray()
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).toArray();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        // TODO: Implement in parallel
        return this.parallelIterable.toList().select(this.predicate).toArray(array);
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
