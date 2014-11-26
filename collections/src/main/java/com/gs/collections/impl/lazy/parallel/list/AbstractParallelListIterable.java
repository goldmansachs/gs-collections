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

import java.util.Comparator;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;

@Beta
public abstract class AbstractParallelListIterable<T, B extends ListBatch<T>> extends AbstractParallelIterable<T, B> implements ParallelListIterable<T>
{
    @Override
    protected boolean isOrdered()
    {
        return true;
    }

    public ParallelUnsortedSetIterable<T> asUnique()
    {
        return new ParallelDistinctListIterable<T>(this);
    }

    public ParallelListIterable<T> select(Predicate<? super T> predicate)
    {
        return new ParallelSelectListIterable<T>(this, predicate);
    }

    public <P> ParallelListIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    public <S> ParallelListIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return (ParallelListIterable<S>) this.select(Predicates.instanceOf(clazz));
    }

    public ParallelListIterable<T> reject(Predicate<? super T> predicate)
    {
        return this.select(Predicates.not(predicate));
    }

    public <P> ParallelListIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    public <V> ParallelListIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return new ParallelCollectListIterable<T, V>(this, function);
    }

    public <P, V> ParallelListIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public <V> ParallelListIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.select(predicate).collect(function);
    }

    public <V> ParallelListIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        // TODO: Implement in parallel
        return this.toList().flatCollect(function).asParallel(this.getExecutorService(), this.getBatchSize());
    }

    public <V> ListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.toList().groupBy(function);
    }

    public <V> ListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        // TODO: Implement in parallel
        return this.toList().groupByEach(function);
    }

    @Override
    public <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.toList().groupByUniqueKey(function);
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        // TODO: Implement in parallel
        return this.toList().min(comparator);
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        // TODO: Implement in parallel
        return this.toList().max(comparator);
    }

    @Override
    public T min()
    {
        // TODO: Implement in parallel
        return this.toList().min();
    }

    @Override
    public T max()
    {
        // TODO: Implement in parallel
        return this.toList().max();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.toList().minBy(function);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        // TODO: Implement in parallel
        return this.toList().maxBy(function);
    }

    @Override
    public long sumOfInt(IntFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.toList().sumOfInt(function);
    }

    @Override
    public double sumOfFloat(FloatFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.toList().sumOfFloat(function);
    }

    @Override
    public long sumOfLong(LongFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.toList().sumOfLong(function);
    }

    @Override
    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        // TODO: Implement in parallel
        return this.toList().sumOfDouble(function);
    }

    @Override
    public Object[] toArray()
    {
        // TODO: Implement in parallel
        return this.toList().toArray();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        // TODO: Implement in parallel
        return this.toList().toArray(array);
    }
}
