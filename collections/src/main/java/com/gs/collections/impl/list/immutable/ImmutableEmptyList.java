/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.list.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import net.jcip.annotations.Immutable;

/**
 * This is a zero element {@link ImmutableList} which is created by calling the Lists.immutable.of() method.
 */
@Immutable
final class ImmutableEmptyList<T>
        extends AbstractImmutableList<T>
        implements Serializable, RandomAccess
{
    static final ImmutableList<?> INSTANCE = new ImmutableEmptyList();

    private static final long serialVersionUID = 1L;

    private Object readResolve()
    {
        return INSTANCE;
    }

    @Override
    public ImmutableList<T> newWithout(T element)
    {
        return this;
    }

    @Override
    public ImmutableList<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this;
    }

    public int size()
    {
        return 0;
    }

    @Override
    public boolean contains(Object obj)
    {
        return false;
    }

    @Override
    public T get(int index)
    {
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    public void forEach(Procedure<? super T> procedure)
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

    public ImmutableList<T> newWith(T newItem)
    {
        return Lists.immutable.of(newItem);
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
    public <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return Lists.immutable.of();
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return target;
    }

    @Override
    public ImmutableList<Pair<T, Integer>> zipWithIndex()
    {
        return Lists.immutable.of();
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return target;
    }
}
