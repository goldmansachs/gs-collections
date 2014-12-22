/*
 * Copyright 2014 Goldman Sachs.
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.FixedSizeList;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.utility.Iterate;

/**
 * This class provides a MutableList wrapper around an array.  All of the internal iteration methods of the MutableList
 * interface as well as the JDK Collections List interface are provided.  However, the pre-determined fixed-sized
 * semantics of an array are maintained and thus mutating List interface methods such as {@link #add(Object)}, {@link
 * #addAll(Collection)}, {@link #remove(Object)}, {@link #removeAll(Collection)}, etc. are not supported and will throw
 * an {@link UnsupportedOperationException}.  In addition, the mutating iteration methods {@link
 * #removeIf(Predicate)} and {@link #removeIfWith(Predicate2, Object)} are not supported and will also
 * throw an {@link UnsupportedOperationException}.
 * <p>
 * The {@link #with(Object)} method is not an exception to the above restrictions, as it will create a new
 * instance of this class with the existing contents plus the new item.
 * <p>
 * To create a wrapper around an existing array, use the {@link #adapt(Object[])} factory method.  To wrap the contents
 * of an existing Collection instance, use the {@link #newArray(Iterable)} or {@link #newArrayWithItem(Iterable, Object)}
 * factory methods.  To wrap existing objects in a new array, use one of the {@link #newArrayWith(Object)} factory methods.
 */
public final class ArrayAdapter<T>
        extends AbstractArrayAdapter<T>
        implements Serializable, FixedSizeList<T>
{
    private static final long serialVersionUID = 1L;
    private static final Object[] EMPTY_ARRAY = {};

    private ArrayAdapter(T[] newElements)
    {
        super(newElements);
    }

    public static <E> ArrayAdapter<E> adapt(E... array)
    {
        return new ArrayAdapter<E>(array);
    }

    public static <E> ArrayAdapter<E> newArray()
    {
        return ArrayAdapter.newArrayWith((E[]) EMPTY_ARRAY);
    }

    public static <E> ArrayAdapter<E> newArray(Iterable<? extends E> source)
    {
        return new ArrayAdapter<E>((E[]) Iterate.toArray(source));
    }

    public static <E> ArrayAdapter<E> newArrayWithItem(Iterable<? extends E> iterable, E itemToAdd)
    {
        int oldSize = Iterate.sizeOf(iterable);
        E[] array = (E[]) new Object[oldSize + 1];
        Iterate.toArray(iterable, array);
        array[oldSize] = itemToAdd;
        return new ArrayAdapter<E>(array);
    }

    public static <E> ArrayAdapter<E> newArrayWith(E one)
    {
        return new ArrayAdapter<E>((E[]) new Object[]{one});
    }

    public static <E> ArrayAdapter<E> newArrayWith(E one, E two)
    {
        return new ArrayAdapter<E>((E[]) new Object[]{one, two});
    }

    public static <E> ArrayAdapter<E> newArrayWith(E one, E two, E three)
    {
        return new ArrayAdapter<E>((E[]) new Object[]{one, two, three});
    }

    public static <E> ArrayAdapter<E> newArrayWith(E one, E two, E three, E four)
    {
        return new ArrayAdapter<E>((E[]) new Object[]{one, two, three, four});
    }

    public static <E> ArrayAdapter<E> newArrayWith(E one, E two, E three, E four, E five)
    {
        return new ArrayAdapter<E>((E[]) new Object[]{one, two, three, four, five});
    }

    public static <E> ArrayAdapter<E> newArrayWith(E one, E two, E three, E four, E five, E six)
    {
        return new ArrayAdapter<E>((E[]) new Object[]{one, two, three, four, five, six});
    }

    public static <E> ArrayAdapter<E> newArrayWith(E one, E two, E three, E four, E five, E six, E seven)
    {
        return new ArrayAdapter<E>((E[]) new Object[]{one, two, three, four, five, six, seven});
    }

    public static <E> ArrayAdapter<E> newArrayWith(E... elements)
    {
        return new ArrayAdapter<E>(elements.clone());
    }

    public T set(int index, T element)
    {
        T oldValue = this.items[index];
        this.items[index] = element;
        return oldValue;
    }

    @Override
    public ArrayAdapter<T> with(T value)
    {
        return ArrayAdapter.newArrayWithItem(this, value);
    }

    @Override
    public ArrayAdapter<T> without(T element)
    {
        if (this.contains(element))
        {
            return ArrayAdapter.newArray(this.toList().without(element));
        }
        return this;
    }

    @Override
    public ArrayAdapter<T> withAll(Iterable<? extends T> elements)
    {
        if (Iterate.isEmpty(elements))
        {
            return this;
        }
        return ArrayAdapter.newArray(this.toList().withAll(elements));
    }

    @Override
    public ArrayAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        if (Iterate.isEmpty(elements))
        {
            return this;
        }
        if (Iterate.anySatisfyWith(elements, Predicates2.in(), this))
        {
            return ArrayAdapter.newArray(this.toList().withoutAll(elements));
        }
        return this;
    }

    @Override
    public ArrayAdapter<T> clone()
    {
        return new ArrayAdapter<T>(this.items.clone());
    }

    @Override
    public ArrayAdapter<T> sortThis(Comparator<? super T> comparator)
    {
        if (this.size() > 1)
        {
            Arrays.sort(this.items, 0, this.size(), comparator);
        }
        return this;
    }

    @Override
    public FixedSizeList<T> toReversed()
    {
        ArrayAdapter<T> result = this.clone();
        result.reverseThis();
        return result;
    }

    private void writeObject(ObjectOutputStream objectOutputStream)
            throws IOException
    {
        T[] localItems = this.items;
        int size = localItems.length;
        objectOutputStream.writeInt(size);
        for (int i = 0; i < size; i++)
        {
            objectOutputStream.writeObject(localItems[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream)
            throws IOException, ClassNotFoundException
    {
        // Read in array length and allocate array
        int arrayLength = objectInputStream.readInt();
        this.items = (T[]) new Object[arrayLength];
        Object[] localItems = this.items;

        // Read in all elements in the proper order.
        for (int i = 0; i < arrayLength; i++)
        {
            localItems[i] = objectInputStream.readObject();
        }
    }
}
