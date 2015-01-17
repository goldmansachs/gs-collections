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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.EmptyIterator;
import com.gs.collections.impl.factory.HashingStrategySets;
import com.gs.collections.impl.set.immutable.AbstractImmutableSet;
import net.jcip.annotations.Immutable;

/**
 * This is a zero element {@link ImmutableUnifiedSetWithHashingStrategy} which is created by calling the HashingStrategySets.immutable.empty() method.
 */
@Immutable
final class ImmutableEmptySetWithHashingStrategy<T>
        extends AbstractImmutableSet<T>
        implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final HashingStrategy<? super T> hashingStrategy;

    ImmutableEmptySetWithHashingStrategy(HashingStrategy<? super T> hashingStrategy)
    {
        this.hashingStrategy = hashingStrategy;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        return other instanceof Set && ((Collection<?>) other).isEmpty();
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    @Override
    public ImmutableSet<T> newWith(T element)
    {
        return HashingStrategySets.immutable.with(this.hashingStrategy, element);
    }

    @Override
    public ImmutableSet<T> newWithAll(Iterable<? extends T> elements)
    {
        return HashingStrategySets.immutable.withAll(this.hashingStrategy, elements);
    }

    @Override
    public ImmutableSet<T> newWithout(T element)
    {
        return this;
    }

    @Override
    public ImmutableSet<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this;
    }

    public int size()
    {
        return 0;
    }

    @Override
    public boolean contains(Object object)
    {
        return false;
    }

    public void each(Procedure<? super T> procedure)
    {
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
    }

    public Iterator<T> iterator()
    {
        return EmptyIterator.getInstance();
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T min()
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max()
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return target;
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return target;
    }

    public T getFirst()
    {
        return null;
    }

    public T getLast()
    {
        return null;
    }

    private Object writeReplace()
    {
        return new ImmutableSetWithHashingStrategySerializationProxy<T>(this, this.hashingStrategy);
    }
}
