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
import java.util.Collection;
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
class SingletonSet<T>
        extends AbstractMemoryEfficientMutableSet<T>
        implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private T element1;

    SingletonSet(T obj1)
    {
        this.element1 = obj1;
    }

    @SuppressWarnings("UnusedDeclaration")
    public SingletonSet()
    {
        // For Externalizable use only
    }

    public int size()
    {
        return 1;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (!(o instanceof Set<?>))
        {
            return false;
        }
        Collection<?> collection = (Collection<?>) o;
        return collection.size() == this.size() && collection.contains(this.element1);
    }

    @Override
    public int hashCode()
    {
        return this.nullSafeHashCode(this.element1);
    }

    // Weird implementation of clone() is ok on final classes
    @Override
    public SingletonSet<T> clone()
    {
        return new SingletonSet<T>(this.element1);
    }

    @Override
    public boolean contains(Object obj)
    {
        return Comparators.nullSafeEquals(obj, this.element1);
    }

    public Iterator<T> iterator()
    {
        return new SingletonSetIterator();
    }

    private class SingletonSetIterator
            extends MemoryEfficientSetIterator
    {
        @Override
        protected T getElement(int i)
        {
            if (i == 0)
            {
                return SingletonSet.this.element1;
            }
            throw new NoSuchElementException("i=" + i);
        }
    }

    public T getFirst()
    {
        return this.element1;
    }

    public T getLast()
    {
        return this.element1;
    }

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

    public MutableSet<T> with(T element)
    {
        return this.contains(element) ? this : new DoubletonSet<T>(this.element1, element);
    }

    public MutableSet<T> without(T element)
    {
        if (Comparators.nullSafeEquals(element, this.element1))
        {
            return new EmptySet<T>();
        }
        return this;
    }
}
