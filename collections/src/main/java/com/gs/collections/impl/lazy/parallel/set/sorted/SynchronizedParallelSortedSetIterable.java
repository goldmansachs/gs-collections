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

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.api.multimap.sortedset.SortedSetMultimap;
import com.gs.collections.api.set.sorted.ParallelSortedSetIterable;
import com.gs.collections.impl.lazy.parallel.AbstractSynchronizedParallelIterable;

@Beta
public final class SynchronizedParallelSortedSetIterable<T> extends AbstractSynchronizedParallelIterable<T, ParallelSortedSetIterable<T>> implements ParallelSortedSetIterable<T>
{
    public SynchronizedParallelSortedSetIterable(ParallelSortedSetIterable<T> delegate, Object lock)
    {
        super(delegate, lock);
    }

    public Comparator<? super T> comparator()
    {
        return this.delegate.comparator();
    }

    public ParallelSortedSetIterable<T> asUnique()
    {
        return this.wrap(this.delegate.asUnique());
    }

    public ParallelSortedSetIterable<T> select(Predicate<? super T> predicate)
    {
        return this.wrap(this.delegate.select(predicate));
    }

    public <P> ParallelSortedSetIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.wrap(this.delegate.selectWith(predicate, parameter));
    }

    public ParallelSortedSetIterable<T> reject(Predicate<? super T> predicate)
    {
        return this.wrap(this.delegate.reject(predicate));
    }

    public <P> ParallelSortedSetIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.wrap(this.delegate.rejectWith(predicate, parameter));
    }

    public <S> ParallelSortedSetIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return this.wrap(this.delegate.selectInstancesOf(clazz));
    }

    public <V> ParallelListIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return this.wrap(this.delegate.collect(function));
    }

    public <P, V> ParallelListIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.wrap(this.delegate.collectWith(function, parameter));
    }

    public <V> ParallelListIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.wrap(this.delegate.collectIf(predicate, function));
    }

    public <V> ParallelListIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.wrap(this.delegate.flatCollect(function));
    }

    public <V> SortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.delegate.groupBy(function);
        }
    }

    public <V> SortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.lock)
        {
            return this.delegate.groupByEach(function);
        }
    }
}
