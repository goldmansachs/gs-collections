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

package com.gs.collections.impl.set.fixed;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.Comparators;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
final class DoubletonSet<T>
        extends AbstractMemoryEfficientMutableSet<T>
        implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private T element1;
    private T element2;

    @SuppressWarnings("UnusedDeclaration")
    public DoubletonSet()
    {
        // For Externalizable use only
    }

    DoubletonSet(T obj1, T obj2)
    {
        this.element1 = obj1;
        this.element2 = obj2;
    }

    public int size()
    {
        return 2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (!(o instanceof Set))
        {
            return false;
        }
        Set<?> collection = (Set<?>) o;
        return collection.size() == this.size() && collection.contains(this.element1) && collection.contains(this.element2);
    }

    @Override
    public int hashCode()
    {
        return this.nullSafeHashCode(this.element1) + this.nullSafeHashCode(this.element2);
    }

    // Weird implementation of clone() is ok on final classes
    @Override
    public DoubletonSet<T> clone()
    {
        return new DoubletonSet<T>(this.element1, this.element2);
    }

    @Override
    public boolean contains(Object obj)
    {
        return Comparators.nullSafeEquals(obj, this.element1)
                || Comparators.nullSafeEquals(obj, this.element2);
    }

    public Iterator<T> iterator()
    {
        return new DoubletonSetIterator();
    }

    public T getFirst()
    {
        return this.element1;
    }

    public T getLast()
    {
        return this.element2;
    }

    public void each(Procedure<? super T> procedure)
    {
        procedure.value(this.element1);
        procedure.value(this.element2);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        objectIntProcedure.value(this.element1, 0);
        objectIntProcedure.value(this.element2, 1);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        procedure.value(this.element1, parameter);
        procedure.value(this.element2, parameter);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.element1);
        out.writeObject(this.element2);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.element1 = (T) in.readObject();
        this.element2 = (T) in.readObject();
    }

    private class DoubletonSetIterator
            extends MemoryEfficientSetIterator
    {
        @Override
        protected T getElement(int i)
        {
            if (i == 0)
            {
                return DoubletonSet.this.element1;
            }
            if (i == 1)
            {
                return DoubletonSet.this.element2;
            }
            throw new NoSuchElementException("i=" + i);
        }
    }

    public MutableSet<T> with(T element)
    {
        return this.contains(element) ? this : new TripletonSet<T>(this.element1, this.element2, element);
    }

    public MutableSet<T> without(T element)
    {
        if (Comparators.nullSafeEquals(element, this.element1))
        {
            return new SingletonSet<T>(this.element2);
        }
        if (Comparators.nullSafeEquals(element, this.element2))
        {
            return new SingletonSet<T>(this.element1);
        }
        return this;
    }
}
