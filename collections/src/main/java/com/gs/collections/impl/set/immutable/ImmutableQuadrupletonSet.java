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
final class ImmutableQuadrupletonSet<T>
        extends AbstractImmutableSet<T>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final T element1;
    private final T element2;
    private final T element3;
    private final T element4;

    ImmutableQuadrupletonSet(T obj1, T obj2, T obj3, T obj4)
    {
        this.element1 = obj1;
        this.element2 = obj2;
        this.element3 = obj3;
        this.element4 = obj4;
    }

    public int size()
    {
        return 4;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof Set))
        {
            return false;
        }
        Set<?> set = (Set<?>) other;
        return set.size() == this.size()
                && set.contains(this.element1)
                && set.contains(this.element2)
                && set.contains(this.element3)
                && set.contains(this.element4);
    }

    @Override
    public int hashCode()
    {
        return this.nullSafeHashCode(this.element1)
                + this.nullSafeHashCode(this.element2)
                + this.nullSafeHashCode(this.element3)
                + this.nullSafeHashCode(this.element4);
    }

    @Override
    public ImmutableSet<T> newWith(T element)
    {
        if (!this.contains(element))
        {
            return Sets.immutable.with(this.element1, this.element2, this.element3, this.element4, element);
        }
        return this;
    }

    @Override
    public ImmutableSet<T> newWithout(T element)
    {
        if (this.contains(element))
        {
            if (Comparators.nullSafeEquals(element, this.element1))
            {
                return Sets.immutable.with(this.element2, this.element3, this.element4);
            }
            if (Comparators.nullSafeEquals(element, this.element2))
            {
                return Sets.immutable.with(this.element1, this.element3, this.element4);
            }
            if (Comparators.nullSafeEquals(element, this.element3))
            {
                return Sets.immutable.with(this.element1, this.element2, this.element4);
            }
            return Sets.immutable.with(this.element1, this.element2, this.element3);
        }
        return this;
    }

    @Override
    public boolean contains(Object obj)
    {
        return Comparators.nullSafeEquals(obj, this.element1)
                || Comparators.nullSafeEquals(obj, this.element2)
                || Comparators.nullSafeEquals(obj, this.element3)
                || Comparators.nullSafeEquals(obj, this.element4);
    }

    public Iterator<T> iterator()
    {
        return new QuadrupletonSetIterator();
    }

    public T getFirst()
    {
        return this.element1;
    }

    public T getLast()
    {
        return this.element4;
    }

    public void each(Procedure<? super T> procedure)
    {
        procedure.value(this.element1);
        procedure.value(this.element2);
        procedure.value(this.element3);
        procedure.value(this.element4);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        objectIntProcedure.value(this.element1, 0);
        objectIntProcedure.value(this.element2, 1);
        objectIntProcedure.value(this.element3, 2);
        objectIntProcedure.value(this.element4, 3);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        procedure.value(this.element1, parameter);
        procedure.value(this.element2, parameter);
        procedure.value(this.element3, parameter);
        procedure.value(this.element4, parameter);
    }

    protected class QuadrupletonSetIterator
            extends ImmutableSetIterator
    {
        @Override
        protected T getElement(int i)
        {
            if (i == 0)
            {
                return ImmutableQuadrupletonSet.this.element1;
            }
            if (i == 1)
            {
                return ImmutableQuadrupletonSet.this.element2;
            }
            if (i == 2)
            {
                return ImmutableQuadrupletonSet.this.element3;
            }
            if (i == 3)
            {
                return ImmutableQuadrupletonSet.this.element4;
            }
            throw new NoSuchElementException("i=" + i);
        }
    }

    private Object writeReplace()
    {
        return new ImmutableSetSerializationProxy<T>(this);
    }
}
