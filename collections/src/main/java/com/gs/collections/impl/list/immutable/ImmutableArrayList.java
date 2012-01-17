/*
 * Copyright 2011 Goldman Sachs.
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
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * An ImmutableArrayList wraps a Java array but it cannot be modified after creation.
 */
@Immutable
final class ImmutableArrayList<T>
        extends AbstractImmutableList<T>
        implements Serializable, RandomAccess
{
    private static final long serialVersionUID = 1L;
    private final T[] items;

    private ImmutableArrayList(T[] newElements)
    {
        this.items = newElements;
    }

    public static <E> ImmutableArrayList<E> newList(Iterable<? extends E> iterable)
    {
        return new ImmutableArrayList<E>((E[]) Iterate.toArray(iterable));
    }

    public static <E> ImmutableArrayList<E> newListWith(E... elements)
    {
        return new ImmutableArrayList<E>(elements.clone());
    }

    @Override
    public boolean notEmpty()
    {
        return ArrayIterate.notEmpty(this.items);
    }

    @Override
    public T getFirst()
    {
        return ArrayIterate.getFirst(this.items);
    }

    @Override
    public T getLast()
    {
        return ArrayIterate.getLast(this.items);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        ArrayIterate.forEach(this.items, procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ArrayIterate.forEachWithIndex(this.items, objectIntProcedure);
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        T[] localItems = this.items;
        for (int i = fromIndex; i <= toIndex; i++)
        {
            objectIntProcedure.value(localItems[i], i);
        }
    }

    @Override
    public T find(Predicate<? super T> predicate)
    {
        return ArrayIterate.find(this.items, predicate);
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.find(predicate);
        if (result == null)
        {
            return function.value();
        }
        return result;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return ArrayIterate.count(this.items, predicate);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.anySatisfy(this.items, predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.allSatisfy(this.items, predicate);
    }

    @Override
    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ArrayIterate.foldLeft(initialValue, this.items, function);
    }

    public int size()
    {
        return this.items.length;
    }

    @Override
    public boolean isEmpty()
    {
        return ArrayIterate.isEmpty(this.items);
    }

    @Override
    public boolean contains(Object o)
    {
        return this.anySatisfy(Predicates.equal(o));
    }

    @Override
    public Iterator<T> iterator()
    {
        return Arrays.asList(this.items).iterator();
    }

    @Override
    public Object[] toArray()
    {
        return this.items.clone();
    }

    @Override
    public <E> E[] toArray(E[] a)
    {
        int size = this.size();
        if (a.length < size)
        {
            a = (E[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(this.items, 0, a, 0, size);
        if (a.length > size)
        {
            a[size] = null;
        }
        return a;
    }

    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append('[');

        int localSize = this.items.length;
        T[] localItems = this.items;
        for (int i = 0; i < localSize; i++)
        {
            T item = localItems[i];
            if (i > 0)
            {
                buf.append(", ");
            }
            buf.append(item == this ? "(this ImmutableArrayList)" : String.valueOf(item));
        }

        buf.append(']');
        return buf.toString();
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return Iterate.allSatisfy(collection, Predicates.in(this.items));
    }

    @Override
    public T get(int index)
    {
        return this.items[index];
    }

    @Override
    public int indexOf(Object item)
    {
        return ArrayIterate.indexOf(this.items, item);
    }

    @Override
    public int lastIndexOf(Object item)
    {
        return Arrays.asList(this.items).lastIndexOf(item);
    }

    public ImmutableList<T> newWith(T newItem)
    {
        int oldSize = this.size();
        T[] array = (T[]) new Object[oldSize + 1];
        this.toArray(array);
        array[oldSize] = newItem;
        return new ImmutableArrayList<T>(array);
    }
}
