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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;

@Beta
public abstract class AbstractParallelUnsortedSetIterable<T> extends AbstractParallelIterable<T> implements ParallelUnsortedSetIterable<T>
{
    private static final Function0<AtomicInteger> ATOMIC_INTEGER_FUNCTION_0 = new Function0<AtomicInteger>()
    {
        public AtomicInteger value()
        {
            return new AtomicInteger(0);
        }
    };

    protected abstract LazyIterable<UnsortedSetBatch<T>> split();

    protected abstract ExecutorService getExecutorService();

    public ParallelUnsortedSetIterable<T> select(Predicate<? super T> predicate)
    {
        return new ParallelSelectUnsortedSetIterable<T>(this, predicate);
    }

    @Override
    public <P> ParallelUnsortedSetIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S> ParallelUnsortedSetIterable<S> selectInstancesOf(Class<S> clazz)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParallelUnsortedSetIterable<T> reject(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <P> ParallelUnsortedSetIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    public <V> ParallelUnsortedSetIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return new ParallelCollectUnsortedSetIterable<T, V>(this, function);
    }

    @Override
    public <P, V> ParallelUnsortedSetIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> ParallelUnsortedSetIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> ParallelUnsortedSetIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        throw new UnsupportedOperationException();
    }
}
