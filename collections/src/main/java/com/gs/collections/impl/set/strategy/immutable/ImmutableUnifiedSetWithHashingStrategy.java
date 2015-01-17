/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.set.strategy.immutable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.UnmodifiableIteratorAdapter;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.set.immutable.AbstractImmutableSet;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import net.jcip.annotations.Immutable;

@Immutable
final class ImmutableUnifiedSetWithHashingStrategy<T>
        extends AbstractImmutableSet<T>
        implements Serializable, BatchIterable<T>
{
    private static final long serialVersionUID = 1L;

    private final UnifiedSetWithHashingStrategy<T> delegate;

    private ImmutableUnifiedSetWithHashingStrategy(UnifiedSetWithHashingStrategy<T> delegate)
    {
        this.delegate = delegate;
    }

    public static <T> ImmutableSet<T> newSetWith(HashingStrategy<? super T> hashingStrategy, T... elements)
    {
        return new ImmutableUnifiedSetWithHashingStrategy<T>(UnifiedSetWithHashingStrategy.newSetWith(hashingStrategy, elements));
    }

    public static <T> ImmutableSet<T> newSet(HashingStrategy<? super T> hashingStrategy, Iterable<T> iterable)
    {
        return new ImmutableUnifiedSetWithHashingStrategy<T>(UnifiedSetWithHashingStrategy.newSet(hashingStrategy, iterable));
    }

    public int size()
    {
        return this.delegate.size();
    }

    @Override
    public boolean equals(Object other)
    {
        return this.delegate.equals(other);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public boolean contains(Object object)
    {
        return this.delegate.contains(object);
    }

    public Iterator<T> iterator()
    {
        return new UnmodifiableIteratorAdapter<T>(this.delegate.iterator());
    }

    public T getFirst()
    {
        return this.delegate.getFirst();
    }

    public T getLast()
    {
        return this.delegate.getLast();
    }

    public void each(Procedure<? super T> procedure)
    {
        this.delegate.forEach(procedure);
    }

    public int getBatchCount(int batchSize)
    {
        return this.delegate.getBatchCount(batchSize);
    }

    public void batchForEach(Procedure<? super T> procedure, int sectionIndex, int sectionCount)
    {
        this.delegate.batchForEach(procedure, sectionIndex, sectionCount);
    }

    private Object writeReplace()
    {
        return new ImmutableSetWithHashingStrategySerializationProxy<T>(this, this.delegate.hashingStrategy());
    }

    @Override
    public ParallelUnsortedSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return this.delegate.asParallel(executorService, batchSize);
    }
}
