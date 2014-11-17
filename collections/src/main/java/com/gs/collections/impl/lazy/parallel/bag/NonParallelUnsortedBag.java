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

import com.gs.collections.api.bag.ParallelUnsortedBag;
import com.gs.collections.api.bag.UnsortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.NonParallelIterable;
import com.gs.collections.impl.lazy.parallel.set.NonParallelUnsortedSetIterable;

public class NonParallelUnsortedBag<T> extends NonParallelIterable<T, UnsortedBag<T>> implements ParallelUnsortedBag<T>
{
    public NonParallelUnsortedBag(UnsortedBag<T> delegate)
    {
        super(delegate);
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
        this.delegate.forEachWithOccurrences(procedure);
    }

    public ParallelUnsortedSetIterable<T> asUnique()
    {
        return new NonParallelUnsortedSetIterable<T>(this.toBag().toSet());
    }

    public ParallelUnsortedBag<T> select(Predicate<? super T> predicate)
    {
        return new NonParallelUnsortedBag<T>(this.delegate.select(predicate));
    }

    public <P> ParallelUnsortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return new NonParallelUnsortedBag<T>(this.delegate.selectWith(predicate, parameter));
    }

    public ParallelUnsortedBag<T> reject(Predicate<? super T> predicate)
    {
        return new NonParallelUnsortedBag<T>(this.delegate.reject(predicate));
    }

    public <P> ParallelUnsortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return new NonParallelUnsortedBag<T>(this.delegate.rejectWith(predicate, parameter));
    }

    public <S> ParallelUnsortedBag<S> selectInstancesOf(Class<S> clazz)
    {
        return new NonParallelUnsortedBag<S>(this.delegate.selectInstancesOf(clazz));
    }

    public <V> ParallelUnsortedBag<V> collect(Function<? super T, ? extends V> function)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.collect(function));
    }

    public <P, V> ParallelUnsortedBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.collectWith(function, parameter));
    }

    public <V> ParallelUnsortedBag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.collectIf(predicate, function));
    }

    public <V> ParallelUnsortedBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new NonParallelUnsortedBag<V>(this.delegate.flatCollect(function));
    }

    public <V> UnsortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.delegate.groupBy(function);
    }

    public <V> UnsortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.delegate.groupByEach(function);
    }
}
