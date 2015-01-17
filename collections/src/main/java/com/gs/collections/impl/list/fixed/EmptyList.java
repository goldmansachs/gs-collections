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

package com.gs.collections.impl.list.fixed;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.EmptyIterator;
import com.gs.collections.impl.factory.Lists;
import net.jcip.annotations.Immutable;

/**
 * This class is a memory efficient list with no elements.  It is created by calling Lists.fixedSize.of() which
 * actually returns a singleton instance.
 */
@Immutable
final class EmptyList<T>
        extends AbstractMemoryEfficientMutableList<T>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Object readResolve()
    {
        return Lists.fixedSize.empty();
    }

    @Override
    public SingletonList<T> with(T value)
    {
        return new SingletonList<T>(value);
    }

    // Weird implementation of clone() is ok on final classes

    @Override
    public EmptyList<T> clone()
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

    public T get(int index)
    {
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    public T set(int index, T element)
    {
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    @Override
    public EmptyList<T> sortThis(Comparator<? super T> comparator)
    {
        return this;
    }

    @Override
    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByInt(IntFunction<? super T> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByBoolean(BooleanFunction<? super T> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByChar(CharFunction<? super T> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByByte(ByteFunction<? super T> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByShort(ShortFunction<? super T> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByFloat(FloatFunction<? super T> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByLong(LongFunction<? super T> function)
    {
        return this;
    }

    @Override
    public MutableList<T> sortThisByDouble(DoubleFunction<? super T> function)
    {
        return this;
    }

    @Override
    public T getFirst()
    {
        return null;
    }

    @Override
    public T getLast()
    {
        return null;
    }

    @Override
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

    @Override
    public Iterator<T> iterator()
    {
        return EmptyIterator.getInstance();
    }

    @Override
    public ListIterator<T> listIterator()
    {
        return EmptyIterator.getInstance();
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        if (index != 0)
        {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
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
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return Lists.fixedSize.empty();
    }

    @Override
    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        return Lists.fixedSize.empty();
    }

    @Override
    public ImmutableList<T> toImmutable()
    {
        return Lists.immutable.empty();
    }
}
