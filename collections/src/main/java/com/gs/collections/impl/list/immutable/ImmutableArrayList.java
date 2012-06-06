/*
 * Copyright 2012 Goldman Sachs.
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
import java.util.List;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.block.procedure.FastListCollectIfProcedure;
import com.gs.collections.impl.block.procedure.FastListCollectProcedure;
import com.gs.collections.impl.block.procedure.FastListRejectProcedure;
import com.gs.collections.impl.block.procedure.FastListSelectProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * An ImmutableArrayList wraps a Java array but it cannot be modified after creation.
 */
@Immutable
final class ImmutableArrayList<T>
        extends AbstractImmutableList<T>
        implements Serializable, RandomAccess, BatchIterable<T>
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
    public int hashCode()
    {
        int hashCode = 1;
        int localSize = this.size();
        for (int i = 0; i < localSize; i++)
        {
            T item = this.items[i];
            hashCode = 31 * hashCode + (item == null ? 0 : item.hashCode());
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object otherList)
    {
        if (otherList == this)
        {
            return true;
        }
        if (!(otherList instanceof List))
        {
            return false;
        }
        List<?> list = (List<?>) otherList;
        if (otherList instanceof ImmutableArrayList)
        {
            return this.immutableArrayListEquals((ImmutableArrayList<?>) otherList);
        }
        if (list instanceof RandomAccess)
        {
            return this.randomAccessListEquals(list);
        }
        return this.regularListEquals(list);
    }

    public boolean immutableArrayListEquals(ImmutableArrayList<?> otherList)
    {
        return Arrays.equals(this.items, otherList.items);
    }

    @Override
    protected boolean randomAccessListEquals(List<?> otherList)
    {
        if (this.size() != otherList.size())
        {
            return false;
        }
        for (int i = 0; i < this.size(); i++)
        {
            T one = this.items[i];
            Object two = otherList.get(i);
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean regularListEquals(List<?> otherList)
    {
        Iterator<?> iterator = otherList.iterator();
        for (int i = 0; i < this.size(); i++)
        {
            T one = this.items[i];
            if (!iterator.hasNext())
            {
                return false;
            }
            Object two = iterator.next();
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return !iterator.hasNext();
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

    public void batchForEach(Procedure<? super T> procedure, int sectionIndex, int sectionCount)
    {
        int sectionSize = this.size() / sectionCount;
        int start = sectionSize * sectionIndex;
        int end = sectionIndex == sectionCount - 1 ? this.size() : start + sectionSize;
        if (procedure instanceof FastListSelectProcedure)
        {
            this.batchFastListSelect(start, end, (FastListSelectProcedure<T>) procedure);
        }
        else if (procedure instanceof FastListCollectProcedure)
        {
            this.batchFastListCollect(start, end, (FastListCollectProcedure<T, ?>) procedure);
        }
        else if (procedure instanceof FastListCollectIfProcedure)
        {
            this.batchFastListCollectIf(start, end, (FastListCollectIfProcedure<T, ?>) procedure);
        }
        else if (procedure instanceof CountProcedure)
        {
            this.batchCount(start, end, (CountProcedure<T>) procedure);
        }
        else if (procedure instanceof FastListRejectProcedure)
        {
            this.batchReject(start, end, (FastListRejectProcedure<T>) procedure);
        }
        else if (procedure instanceof MultimapPutProcedure)
        {
            this.batchGroupBy(start, end, (MultimapPutProcedure<?, T>) procedure);
        }
        else
        {
            for (int i = start; i < end; i++)
            {
                procedure.value(this.items[i]);
            }
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure
     */
    private void batchGroupBy(int start, int end, MultimapPutProcedure<?, T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(this.items[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure
     */
    private void batchReject(int start, int end, FastListRejectProcedure<T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(this.items[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure
     */
    private void batchCount(int start, int end, CountProcedure<T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(this.items[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure
     */
    private void batchFastListCollectIf(int start, int end, FastListCollectIfProcedure<T, ?> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(this.items[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure
     */
    private void batchFastListCollect(int start, int end, FastListCollectProcedure<T, ?> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(this.items[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure
     */
    private void batchFastListSelect(int start, int end, FastListSelectProcedure<T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(this.items[i]);
        }
    }

    public int getBatchCount(int batchSize)
    {
        return Math.max(1, this.size() / batchSize);
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
    public T detect(Predicate<? super T> predicate)
    {
        return ArrayIterate.detect(this.items, predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.detect(predicate);
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
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ArrayIterate.injectInto(injectedValue, this.items, function);
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
