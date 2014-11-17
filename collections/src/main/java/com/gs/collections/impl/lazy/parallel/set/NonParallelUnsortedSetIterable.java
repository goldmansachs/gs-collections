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

package com.gs.collections.impl.lazy.parallel.set;

import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.set.UnsortedSetMultimap;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.lazy.parallel.NonParallelIterable;
import com.gs.collections.impl.lazy.parallel.bag.NonParallelUnsortedBag;

public class NonParallelUnsortedSetIterable<T> extends NonParallelIterable<T, UnsortedSetIterable<T>> implements ParallelUnsortedSetIterable<T>
{
    public NonParallelUnsortedSetIterable(UnsortedSetIterable<T> delegate)
    {
        super(delegate);
    }

    public ParallelUnsortedSetIterable<T> asUnique()
    {
        return this;
    }

    public ParallelUnsortedSetIterable<T> select(Predicate<? super T> predicate)
    {
        return new NonParallelUnsortedSetIterable<T>(this.delegate.select(predicate));
    }

    public <P> ParallelUnsortedSetIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return new NonParallelUnsortedSetIterable<T>(this.delegate.selectWith(predicate, parameter));
    }

    public ParallelUnsortedSetIterable<T> reject(Predicate<? super T> predicate)
    {
        return new NonParallelUnsortedSetIterable<T>(this.delegate.reject(predicate));
    }

    public <P> ParallelUnsortedSetIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return new NonParallelUnsortedSetIterable<T>(this.delegate.rejectWith(predicate, parameter));
    }

    public <S> ParallelUnsortedSetIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return new NonParallelUnsortedSetIterable<S>(this.delegate.selectInstancesOf(clazz));
    }

    public <V> ParallelIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.collect(function, new HashBag<V>()));
    }

    public <P, V> ParallelIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.collectWith(function, parameter, new HashBag<V>()));
    }

    public <V> ParallelIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.collectIf(predicate, function, new HashBag<V>()));
    }

    public <V> ParallelIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.flatCollect(function, new HashBag<V>()));
    }

    public <V> UnsortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.delegate.groupBy(function);
    }

    public <V> UnsortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.delegate.groupByEach(function);
    }
}
