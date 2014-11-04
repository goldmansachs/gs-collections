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

package com.gs.collections.impl.list.immutable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.partition.list.PartitionImmutableList;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.partition.list.PartitionImmutableListImpl;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import com.gs.collections.impl.utility.internal.InternalArrayIterate;
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
        return Arrays.hashCode(this.items);
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
        return InternalArrayIterate.randomAccessListEquals(this.items, this.items.length, otherList);
    }

    @Override
    protected boolean regularListEquals(List<?> otherList)
    {
        return InternalArrayIterate.regularListEquals(this.items, this.items.length, otherList);
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
        this.each(procedure);
    }

    public void each(Procedure<? super T> procedure)
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
        InternalArrayIterate.batchForEach(procedure, this.items, this.items.length, sectionIndex, sectionCount);
    }

    public int getBatchCount(int batchSize)
    {
        return Math.max(1, this.size() / batchSize);
    }

    @Override
    public void forEachWithIndex(int from, int to, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.rangeCheck(from, to, this.items.length);
        InternalArrayIterate.forEachWithIndexWithoutChecks(this.items, from, to, objectIntProcedure);
    }

    public ImmutableList<T> select(Predicate<? super T> predicate)
    {
        return ArrayIterate.select(this.items, predicate).toImmutable();
    }

    public <P> ImmutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.selectWith(this.items, predicate, parameter).toImmutable();
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ArrayIterate.selectWith(this.items, predicate, parameter, targetCollection);
    }

    public ImmutableList<T> reject(Predicate<? super T> predicate)
    {
        return ArrayIterate.reject(this.items, predicate).toImmutable();
    }

    public <P> ImmutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.rejectWith(this.items, predicate, parameter, FastList.<T>newList()).toImmutable();
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ArrayIterate.rejectWith(this.items, predicate, parameter, targetCollection);
    }

    public PartitionImmutableList<T> partition(Predicate<? super T> predicate)
    {
        return ArrayIterate.partition(this.items, predicate).toImmutable();
    }

    public <P> PartitionImmutableList<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.partitionWith(this.items, predicate, parameter).toImmutable();
    }

    public <S> ImmutableList<S> selectInstancesOf(Class<S> clazz)
    {
        return ArrayIterate.selectInstancesOf(this.items, clazz).toImmutable();
    }

    public <V> ImmutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.collect(this.items, function).toImmutable();
    }

    public <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return ArrayIterate.collectWith(this.items, function, parameter).toImmutable();
    }

    public <V> ImmutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return ArrayIterate.collectIf(this.items, predicate, function).toImmutable();
    }

    @Override
    public <P, V, R extends Collection<V>> R collectWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection)
    {
        return ArrayIterate.collectWith(this.items, function, parameter, targetCollection);
    }

    public <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return ArrayIterate.flatCollect(this.items, function).toImmutable();
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return ArrayIterate.flatCollect(this.items, function, target);
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
        return result == null ? function.value() : result;
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
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.noneSatisfy(this.items, predicate);
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
    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        InternalArrayIterate.appendString(this, this.items, this.items.length, appendable, start, separator, end);
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

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.minBy(this.items, function);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.maxBy(this.items, function);
    }

    @Override
    public ImmutableList<T> takeWhile(Predicate<? super T> predicate)
    {
        int endIndex = this.detectNotIndex(predicate);
        T[] result = (T[]) new Object[endIndex];
        System.arraycopy(this.items, 0, result, 0, endIndex);
        return new ImmutableArrayList<T>(result);
    }

    @Override
    public ImmutableList<T> dropWhile(Predicate<? super T> predicate)
    {
        int startIndex = this.detectNotIndex(predicate);
        int resultSize = this.size() - startIndex;
        T[] result = (T[]) new Object[resultSize];
        System.arraycopy(this.items, startIndex, result, 0, resultSize);
        return new ImmutableArrayList<T>(result);
    }

    @Override
    public PartitionImmutableList<T> partitionWhile(Predicate<? super T> predicate)
    {
        int partitionIndex = this.detectNotIndex(predicate);
        int rejectedSize = this.size() - partitionIndex;
        T[] selectedArray = (T[]) new Object[partitionIndex];
        T[] rejectedArray = (T[]) new Object[rejectedSize];
        System.arraycopy(this.items, 0, selectedArray, 0, partitionIndex);
        System.arraycopy(this.items, partitionIndex, rejectedArray, 0, rejectedSize);
        ImmutableArrayList<T> selected = new ImmutableArrayList<T>(selectedArray);
        ImmutableArrayList<T> rejected = new ImmutableArrayList<T>(rejectedArray);
        return new PartitionImmutableListImpl<T>(selected, rejected);
    }

    private int detectNotIndex(Predicate<? super T> predicate)
    {
        for (int index = 0; index < this.size(); index++)
        {
            if (!predicate.accept(this.items[index]))
            {
                return index;
            }
        }
        return this.size();
    }
}
