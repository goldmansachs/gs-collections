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

package com.gs.collections.impl.set.immutable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Sets;
import net.jcip.annotations.Immutable;

@Immutable
final class ImmutableDoubletonSet<T>
        extends AbstractImmutableSet<T>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final T element1;
    private final T element2;

    ImmutableDoubletonSet(T obj1, T obj2)
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
        Set<?> set = (Set<?>) o;
        return set.size() == this.size()
                && set.contains(this.element1)
                && set.contains(this.element2);
    }

    @Override
    public int hashCode()
    {
        return this.nullSafeHashCode(this.element1) + this.nullSafeHashCode(this.element2);
    }

    @Override
    public ImmutableSet<T> newWith(T element)
    {
        if (!this.contains(element))
        {
            return Sets.immutable.with(this.element1, this.element2, element);
        }
        return this;
    }

    @Override
    public ImmutableSet<T> newWithout(T element)
    {
        if (this.contains(element))
        {
            return Comparators.nullSafeEquals(element, this.element1)
                    ? Sets.immutable.with(this.element2)
                    : Sets.immutable.with(this.element1);
        }
        return this;
    }

    @Override
    public boolean contains(Object obj)
    {
        return Comparators.nullSafeEquals(obj, this.element1) || Comparators.nullSafeEquals(obj, this.element2);
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

    private class DoubletonSetIterator
            extends ImmutableSetIterator
    {
        @Override
        protected T getElement(int i)
        {
            if (i == 0)
            {
                return ImmutableDoubletonSet.this.element1;
            }
            if (i == 1)
            {
                return ImmutableDoubletonSet.this.element2;
            }
            throw new NoSuchElementException("i=" + i);
        }
    }

    private Object writeReplace()
    {
        return new ImmutableSetSerializationProxy<T>(this);
    }
}
