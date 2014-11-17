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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.NonParallelIterable;
import com.gs.collections.impl.lazy.parallel.set.NonParallelUnsortedSetIterable;

public class NonParallelListIterable<T> extends NonParallelIterable<T, ListIterable<T>> implements ParallelListIterable<T>
{
    public NonParallelListIterable(ListIterable<T> delegate)
    {
        super(delegate);
    }

    public ParallelUnsortedSetIterable<T> asUnique()
    {
        return new NonParallelUnsortedSetIterable<T>(this.delegate.toSet());
    }

    public ParallelListIterable<T> select(Predicate<? super T> predicate)
    {
        return new NonParallelListIterable<T>(this.delegate.select(predicate));
    }

    public <P> ParallelListIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return new NonParallelListIterable<T>(this.delegate.selectWith(predicate, parameter));
    }

    public ParallelListIterable<T> reject(Predicate<? super T> predicate)
    {
        return new NonParallelListIterable<T>(this.delegate.reject(predicate));
    }

    public <P> ParallelListIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return new NonParallelListIterable<T>(this.delegate.rejectWith(predicate, parameter));
    }

    public <S> ParallelListIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return new NonParallelListIterable<S>(this.delegate.selectInstancesOf(clazz));
    }

    public <V> ParallelListIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return new NonParallelListIterable<V>(this.delegate.collect(function));
    }

    public <P, V> ParallelListIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return new NonParallelListIterable<V>(this.delegate.collectWith(function, parameter));
    }

    public <V> ParallelListIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return new NonParallelListIterable<V>(this.delegate.collectIf(predicate, function));
    }

    public <V> ParallelListIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new NonParallelListIterable<V>(this.delegate.flatCollect(function));
    }

    public <V> ListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.delegate.groupBy(function);
    }

    public <V> ListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.delegate.groupByEach(function);
    }
}
