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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;

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
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import net.jcip.annotations.NotThreadSafe;

/**
 * This class is a memory efficient list with one element.  Unlike Collections.singletonList(), it can be sorted.  It is
 * normally created by calling Lists.fixedSize.of(one).
 */
@NotThreadSafe
final class SingletonList<T>
        extends AbstractMemoryEfficientMutableList<T>
        implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private T element1;

    @SuppressWarnings("UnusedDeclaration")
    public SingletonList()
    {
        // For Externalizable use only
    }

    SingletonList(T obj1)
    {
        this.element1 = obj1;
    }

    @Override
    public DoubletonList<T> with(T value)
    {
        return new DoubletonList<T>(this.element1, value);
    }

    // Weird implementation of clone() is ok on final classes

    @Override
    public SingletonList<T> clone()
    {
        return new SingletonList<T>(this.element1);
    }

    public int size()
    {
        return 1;
    }

    @Override
    public boolean contains(Object obj)
    {
        return Comparators.nullSafeEquals(obj, this.element1);
    }

    public T get(int index)
    {
        if (index == 0)
        {
            return this.element1;
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    /**
     * set is implemented purely to allow the List to be sorted, not because this List should be considered mutable.
     */
    public T set(int index, T element)
    {
        if (index == 0)
        {
            T previousElement = this.element1;
            this.element1 = element;
            return previousElement;
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    @Override
    public SingletonList<T> sortThis(Comparator<? super T> comparator)
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
        return this.element1;
    }

    @Override
    public T getLast()
    {
        return this.element1;
    }

    @Override
    public void each(Procedure<? super T> procedure)
    {
        procedure.value(this.element1);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        objectIntProcedure.value(this.element1, 0);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        procedure.value(this.element1, parameter);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.element1);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.element1 = (T) in.readObject();
    }
}
