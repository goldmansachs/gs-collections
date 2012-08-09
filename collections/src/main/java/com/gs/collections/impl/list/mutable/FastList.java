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

package com.gs.collections.impl.list.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.block.procedure.FastListCollectIfProcedure;
import com.gs.collections.impl.block.procedure.FastListCollectProcedure;
import com.gs.collections.impl.block.procedure.FastListRejectProcedure;
import com.gs.collections.impl.block.procedure.FastListSelectProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.ArrayListIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import com.gs.collections.impl.utility.internal.InternalArrayIterate;
import net.jcip.annotations.NotThreadSafe;

/**
 * FastList is an attempt to provide the same functionality as ArrayList without the support for concurrent
 * modification exceptions.  It also attempts to correct the problem with subclassing ArrayList
 * in that the data elements are protected, not private.  It is this issue that caused this class
 * to be created in the first place.  The intent was to provide optimized internal iterators which use direct access
 * against the array of items, which is currently not possible by subclassing ArrayList.
 * <p/>
 * An empty FastList created by calling the default constructor starts with a shared reference to a static
 * empty array (DEFAULT_SIZED_EMPTY_ARRAY).  This makes empty FastLists very memory efficient.  The
 * first call to add will lazily create an array of size 10.
 * <p/>
 * An empty FastList created by calling the pre-size constructor with a value of 0 (new FastList(0)) starts
 * with a shared reference to a static  empty array (ZERO_SIZED_ARRAY).  This makes FastLists presized to 0 very
 * memory efficient as well.  The first call to add will lazily create an array of size 1.
 */
@NotThreadSafe
public class FastList<T>
        extends AbstractMutableList<T>
        implements Externalizable, RandomAccess, BatchIterable<T>
{
    private static final long serialVersionUID = 1L;
    private static final Object[] DEFAULT_SIZED_EMPTY_ARRAY = {};
    private static final Object[] ZERO_SIZED_ARRAY = {};

    protected int size;
    protected transient T[] items = (T[]) DEFAULT_SIZED_EMPTY_ARRAY;

    public FastList()
    {
    }

    public FastList(int initialCapacity)
    {
        this.items = initialCapacity == 0 ? (T[]) ZERO_SIZED_ARRAY : (T[]) new Object[initialCapacity];
    }

    protected FastList(T[] array)
    {
        this(array.length, array);
    }

    protected FastList(int size, T[] array)
    {
        this.size = size;
        this.items = array;
    }

    public FastList(Collection<? extends T> source)
    {
        this.items = (T[]) source.toArray();
        this.size = this.items.length;
    }

    public static <E> FastList<E> newList()
    {
        return new FastList<E>();
    }

    public static <E> FastList<E> wrapCopy(E... array)
    {
        E[] newArray = (E[]) new Object[array.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return new FastList<E>(newArray);
    }

    public static <E> FastList<E> newList(int initialCapacity)
    {
        return new FastList<E>(initialCapacity);
    }

    public static <E> FastList<E> newList(Iterable<? extends E> source)
    {
        return FastList.newListWith((E[]) Iterate.toArray(source));
    }

    /**
     * Creates a new list using the passed {@code elements} argument as the backing store.
     * <p/>
     * !!! WARNING: This method uses the passed in array, so can be very unsafe if the original
     * array is held onto anywhere else. !!!
     */
    public static <E> FastList<E> newListWith(E... elements)
    {
        return new FastList<E>(elements);
    }

    @Override
    public FastList<T> clone()
    {
        FastList<T> result = (FastList<T>) super.clone();
        if (this.items.length > 0)
        {
            result.items = this.items.clone();
        }
        return result;
    }

    @Override
    public void clear()
    {
        Arrays.fill(this.items, null);
        this.size = 0;
    }

    @Override
    public void forEach(int from, int to, Procedure<? super T> procedure)
    {
        ListIterate.rangeCheck(from, to, this.size);
        InternalArrayIterate.forEachWithoutChecks(this.items, from, to, procedure);
    }

    @Override
    public void forEachWithIndex(int from, int to, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.rangeCheck(from, to, this.size);
        InternalArrayIterate.forEachWithIndexWithoutChecks(this.items, from, to, objectIntProcedure);
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

    public <E> E[] toArray(E[] array, int sourceFromIndex, int sourceToIndex, int destinationIndex)
    {
        System.arraycopy(this.items, sourceFromIndex, array, destinationIndex, sourceToIndex - sourceFromIndex + 1);
        return array;
    }

    public <E> E[] toArray(int sourceFromIndex, int sourceToIndex)
    {
        return this.toArray((E[]) new Object[sourceToIndex - sourceFromIndex + 1], sourceFromIndex, sourceToIndex, 0);
    }

    @Override
    public FastList<T> sortThis(Comparator<? super T> comparator)
    {
        ArrayIterate.sort(this.items, this.size, comparator);
        return this;
    }

    @Override
    public FastList<T> sortThis()
    {
        ArrayIterate.sort(this.items, this.size, null);
        return this;
    }

    @Override
    public FastList<T> reverseThis()
    {
        ArrayIterate.reverse(this.items, this.size);
        return this;
    }

    @Override
    public boolean addAll(Collection<? extends T> source)
    {
        if (source.isEmpty())
        {
            return false;
        }

        if (source.getClass() == FastList.class)
        {
            this.addAllFastList((FastList<T>) source);
        }
        else if (source.getClass() == ArrayList.class)
        {
            this.addAllArrayList((ArrayList<T>) source);
        }
        else
        {
            this.addAllCollection(source);
        }

        return true;
    }

    private void addAllFastList(FastList<T> source)
    {
        int sourceSize = source.size();
        int newSize = this.size + sourceSize;
        this.ensureCapacity(newSize);
        System.arraycopy(source.items, 0, this.items, this.size, sourceSize);
        this.size = newSize;
    }

    private void addAllArrayList(ArrayList<T> source)
    {
        int sourceSize = source.size();
        int newSize = this.size + sourceSize;
        this.ensureCapacity(newSize);
        ArrayListIterate.toArray(source, this.items, this.size, sourceSize);
        this.size = newSize;
    }

    private void addAllCollection(Collection<? extends T> source)
    {
        this.ensureCapacity(this.size + source.size());
        Iterate.forEachWith(source, Procedures2.<T>addToCollection(), this);
    }

    @Override
    public boolean containsAll(Collection<?> source)
    {
        return Iterate.allSatisfyWith(source, Predicates2.in(), this);
    }

    @Override
    public boolean containsAllArguments(Object... source)
    {
        return ArrayIterate.allSatisfyWith(source, Predicates2.in(), this);
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        if (array.length < this.size)
        {
            array = (E[]) Array.newInstance(array.getClass().getComponentType(), this.size);
        }
        System.arraycopy(this.items, 0, array, 0, this.size);
        if (array.length > this.size)
        {
            array[this.size] = null;
        }
        return array;
    }

    @Override
    public Object[] toArray()
    {
        return this.copyItemsWithNewCapacity(this.size);
    }

    public T[] toTypedArray(Class<T> clazz)
    {
        T[] array = (T[]) Array.newInstance(clazz, this.size);
        System.arraycopy(this.items, 0, array, 0, this.size);
        return array;
    }

    private void throwOutOfBounds(int index)
    {
        throw this.newIndexOutOfBoundsException(index);
    }

    public T set(int index, T element)
    {
        T previous = this.get(index);
        this.items[index] = element;
        return previous;
    }

    @Override
    public int indexOf(Object object)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (Comparators.nullSafeEquals(this.items[i], object))
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object object)
    {
        for (int i = this.size - 1; i >= 0; i--)
        {
            if (Comparators.nullSafeEquals(this.items[i], object))
            {
                return i;
            }
        }
        return -1;
    }

    public void trimToSize()
    {
        if (this.size < this.items.length)
        {
            this.transferItemsToNewArrayWithCapacity(this.size);
        }
    }

    /**
     * Express load factor as 0.25 to trim a collection with more than 25% excess capacity
     */
    public boolean trimToSizeIfGreaterThanPercent(double loadFactor)
    {
        double excessCapacity = 1.0 - (double) this.size / (double) this.items.length;
        if (excessCapacity > loadFactor)
        {
            this.trimToSize();
            return true;
        }
        return false;
    }

    public void ensureCapacity(int minCapacity)
    {
        int oldCapacity = this.items.length;
        if (minCapacity > oldCapacity)
        {
            int newCapacity = Math.max(this.sizePlusFiftyPercent(oldCapacity), minCapacity);
            this.transferItemsToNewArrayWithCapacity(newCapacity);
        }
    }

    private void transferItemsToNewArrayWithCapacity(int newCapacity)
    {
        this.items = (T[]) this.copyItemsWithNewCapacity(newCapacity);
    }

    private Object[] copyItemsWithNewCapacity(int newCapacity)
    {
        Object[] newItems = new Object[newCapacity];
        System.arraycopy(this.items, 0, newItems, 0, Math.min(this.size, newCapacity));
        return newItems;
    }

    public FastList<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public FastList<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public FastList<T> with(T... elements)
    {
        return this.withArrayCopy(elements, 0, elements.length);
    }

    public FastList<T> withArrayCopy(T[] elements, int begin, int length)
    {
        this.ensureCapacity(this.size + length);
        System.arraycopy(elements, begin, this.items, this.size, length);
        this.size += length;
        return this;
    }

    @Override
    public T getFirst()
    {
        return this.isEmpty() ? null : this.items[0];
    }

    @Override
    public T getLast()
    {
        return this.isEmpty() ? null : this.items[this.size() - 1];
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
        for (int i = 0; i < this.size; i++)
        {
            procedure.value(this.items[i]);
        }
    }

    public void forEachIf(Predicate<? super T> predicate, Procedure<? super T> procedure)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (predicate.accept(item))
            {
                procedure.value(item);
            }
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        for (int i = 0; i < this.size; i++)
        {
            objectIntProcedure.value(this.items[i], i);
        }
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        for (int i = 0; i < this.size; i++)
        {
            procedure.value(this.items[i], parameter);
        }
    }

    @Override
    public FastList<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (predicate.accept(item))
            {
                target.add(item);
            }
        }
        return target;
    }

    @Override
    public <P> FastList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.selectWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (predicate.accept(item, parameter))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    @Override
    public FastList<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (!predicate.accept(item))
            {
                target.add(item);
            }
        }
        return target;
    }

    @Override
    public <P> FastList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.rejectWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R target)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (!predicate.accept(item, parameter))
            {
                target.add(item);
            }
        }
        return target;
    }

    @Override
    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        MutableList<T> positiveResult = FastList.newList();
        MutableList<T> negativeResult = FastList.newList();
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            (predicate.accept(item, parameter) ? positiveResult : negativeResult).add(item);
        }
        return Tuples.twin(positiveResult, negativeResult);
    }

    @Override
    public <S> FastList<S> selectInstancesOf(Class<S> clazz)
    {
        FastList<S> result = FastList.newList(this.size);
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (clazz.isInstance(item))
            {
                result.add((S) item);
            }
        }
        result.trimToSize();
        return result;
    }

    @Override
    public void removeIf(Predicate<? super T> predicate)
    {
        int currentFilledIndex = 0;
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (!predicate.accept(item))
            {
                // keep it
                if (currentFilledIndex != i)
                {
                    this.items[currentFilledIndex] = item;
                }
                currentFilledIndex++;
            }
        }
        this.wipeAndResetTheEnd(currentFilledIndex);
    }

    private void wipeAndResetTheEnd(int newCurrentFilledIndex)
    {
        for (int i = newCurrentFilledIndex; i < this.size; i++)
        {
            this.items[i] = null;
        }
        this.size = newCurrentFilledIndex;
    }

    @Override
    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        int currentFilledIndex = 0;
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (!predicate.accept(item, parameter))
            {
                // keep it
                if (currentFilledIndex != i)
                {
                    this.items[currentFilledIndex] = item;
                }
                currentFilledIndex++;
            }
        }
        this.wipeAndResetTheEnd(currentFilledIndex);
    }

    @Override
    public <V> FastList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, FastList.<V>newList(this.size()));
    }

    @Override
    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        for (int i = 0; i < this.size; i++)
        {
            target.add(function.valueOf(this.items[i]));
        }
        return target;
    }

    @Override
    public <V> FastList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, FastList.<V>newList(this.size()));
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        for (int i = 0; i < this.size; i++)
        {
            Iterate.addAllTo(function.valueOf(this.items[i]), target);
        }
        return target;
    }

    @Override
    public <P, V> FastList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collectWith(function, parameter, FastList.<V>newList(this.size()));
    }

    @Override
    public <P, V, R extends Collection<V>> R collectWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection)
    {
        for (int i = 0; i < this.size; i++)
        {
            targetCollection.add(function.value(this.items[i], parameter));
        }
        return targetCollection;
    }

    @Override
    public <V> FastList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (predicate.accept(item))
            {
                target.add(function.valueOf(item));
            }
        }
        return target;
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (predicate.accept(item))
            {
                return item;
            }
        }
        return null;
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> defaultValueBlock)
    {
        T result = this.detect(predicate);
        return result == null ? defaultValueBlock.value() : result;
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            if (predicate.accept(item, parameter))
            {
                return item;
            }
        }
        return null;
    }

    @Override
    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> defaultValueBlock)
    {
        T result = this.detectWith(predicate, parameter);
        return result == null ? defaultValueBlock.value() : result;
    }

    public T get(int index)
    {
        if (index < this.size)
        {
            return this.items[index];
        }
        throw this.newIndexOutOfBoundsException(index);
    }

    private IndexOutOfBoundsException newIndexOutOfBoundsException(int index)
    {
        return new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size);
    }

    @Override
    public boolean add(T newItem)
    {
        if (this.items.length == this.size)
        {
            this.ensureCapacityForAdd();
        }
        this.items[this.size++] = newItem;
        return true;
    }

    private void ensureCapacityForAdd()
    {
        if (this.items == DEFAULT_SIZED_EMPTY_ARRAY)
        {
            this.items = (T[]) new Object[10];
        }
        else
        {
            this.transferItemsToNewArrayWithCapacity(this.sizePlusFiftyPercent(this.size));
        }
    }

    public void add(int index, T element)
    {
        if (index > -1 && index < this.size)
        {
            this.addAtIndex(index, element);
        }
        else if (index == this.size)
        {
            this.add(element);
        }
        else
        {
            this.throwOutOfBounds(index);
        }
    }

    private void addAtIndex(int index, T element)
    {
        int oldSize = this.size++;
        if (this.items.length == oldSize)
        {
            T[] newItems = (T[]) new Object[this.sizePlusFiftyPercent(oldSize)];
            if (index > 0)
            {
                System.arraycopy(this.items, 0, newItems, 0, index);
            }
            System.arraycopy(this.items, index, newItems, index + 1, oldSize - index);
            this.items = newItems;
        }
        else
        {
            System.arraycopy(this.items, index, this.items, index + 1, oldSize - index);
        }
        this.items[index] = element;
    }

    private int sizePlusFiftyPercent(int oldSize)
    {
        return oldSize * 3 / 2 + 1;
    }

    public T remove(int index)
    {
        T previous = this.get(index);
        int totalOffset = this.size - index - 1;
        if (totalOffset > 0)
        {
            System.arraycopy(this.items, index + 1, this.items, index, totalOffset);
        }
        this.items[--this.size] = null;
        return previous;
    }

    @Override
    public boolean remove(Object object)
    {
        int index = this.indexOf(object);
        if (index >= 0)
        {
            this.remove(index);
            return true;
        }
        return false;
    }

    public boolean addAll(int index, Collection<? extends T> source)
    {
        if (index > this.size || index < 0)
        {
            this.throwOutOfBounds(index);
        }
        if (source.isEmpty())
        {
            return false;
        }

        if (source.getClass() == FastList.class)
        {
            this.addAllFastListAtIndex((FastList<T>) source, index);
        }
        else if (source.getClass() == ArrayList.class)
        {
            this.addAllArrayListAtIndex((ArrayList<T>) source, index);
        }
        else
        {
            this.addAllCollectionAtIndex(source, index);
        }
        return true;
    }

    private void addAllFastListAtIndex(FastList<T> source, int index)
    {
        int sourceSize = source.size();
        int newSize = this.size + sourceSize;
        this.ensureCapacity(newSize);
        this.shiftElementsAtIndex(index, sourceSize);
        System.arraycopy(source.items, 0, this.items, index, sourceSize);
        this.size = newSize;
    }

    private void addAllArrayListAtIndex(ArrayList<T> source, int index)
    {
        int sourceSize = source.size();
        int newSize = this.size + sourceSize;
        this.ensureCapacity(newSize);
        this.shiftElementsAtIndex(index, sourceSize);
        ArrayListIterate.toArray(source, this.items, index, sourceSize);
        this.size = newSize;
    }

    private void addAllCollectionAtIndex(Collection<? extends T> source, int index)
    {
        Object[] newItems = source.toArray();
        int sourceSize = newItems.length;
        int newSize = this.size + sourceSize;
        this.ensureCapacity(newSize);
        this.shiftElementsAtIndex(index, sourceSize);
        this.size = newSize;
        System.arraycopy(newItems, 0, this.items, index, sourceSize);
    }

    private void shiftElementsAtIndex(int index, int sourceSize)
    {
        int numberToMove = this.size - index;
        if (numberToMove > 0)
        {
            System.arraycopy(this.items, index, this.items, index + sourceSize, numberToMove);
        }
    }

    @Override
    public int size()
    {
        return this.size;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        int count = 0;
        for (int i = 0; i < this.size; i++)
        {
            if (predicate.accept(this.items[i]))
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        int count = 0;
        for (int i = 0; i < this.size; i++)
        {
            if (predicate.accept(this.items[i], parameter))
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (predicate.accept(this.items[i]))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (predicate.accept(this.items[i], parameter))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (!predicate.accept(this.items[i]))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (!predicate.accept(this.items[i], parameter))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        IV result = injectedValue;
        for (int i = 0; i < this.size; i++)
        {
            result = function.value(result, this.items[i]);
        }
        return result;
    }

    @Override
    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        int result = injectedValue;
        for (int i = 0; i < this.size; i++)
        {
            result = function.intValueOf(result, this.items[i]);
        }
        return result;
    }

    @Override
    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        long result = injectedValue;
        for (int i = 0; i < this.size; i++)
        {
            result = function.longValueOf(result, this.items[i]);
        }
        return result;
    }

    @Override
    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        double result = injectedValue;
        for (int i = 0; i < this.size; i++)
        {
            result = function.doubleValueOf(result, this.items[i]);
        }
        return result;
    }

    @Override
    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        float result = injectedValue;
        for (int i = 0; i < this.size; i++)
        {
            result = function.floatValueOf(result, this.items[i]);
        }
        return result;
    }

    @Override
    public long sumOfInt(IntFunction<? super T> function)
    {
        long result = 0L;
        for (int i = 0; i < this.size; i++)
        {
            result += (long) function.intValueOf(this.items[i]);
        }
        return result;
    }

    @Override
    public long sumOfLong(LongFunction<? super T> function)
    {
        long result = 0L;
        for (int i = 0; i < this.size; i++)
        {
            result += function.longValueOf(this.items[i]);
        }
        return result;
    }

    @Override
    public double sumOfFloat(FloatFunction<? super T> function)
    {
        double result = 0.0d;
        for (int i = 0; i < this.size; i++)
        {
            result += (double) function.floatValueOf(this.items[i]);
        }
        return result;
    }

    @Override
    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        double result = 0.0d;
        for (int i = 0; i < this.size; i++)
        {
            result += function.doubleValueOf(this.items[i]);
        }
        return result;
    }

    @Override
    public <IV, P> IV injectIntoWith(
            IV injectValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        IV result = injectValue;
        for (int i = 0; i < this.size; i++)
        {
            result = function.value(result, this.items[i], parameter);
        }
        return result;
    }

    @Override
    public FastList<T> toList()
    {
        return FastList.newList(this);
    }

    @Override
    public FastList<T> toSortedList()
    {
        return this.toSortedList(Comparators.naturalOrder());
    }

    @Override
    public FastList<T> toSortedList(Comparator<? super T> comparator)
    {
        return FastList.newList(this).sortThis(comparator);
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
        if (otherList instanceof FastList)
        {
            return this.fastListEquals((FastList<?>) otherList);
        }
        if (otherList instanceof RandomAccess)
        {
            return this.randomAccessListEquals(list);
        }
        return this.regularListEquals(list);
    }

    public boolean fastListEquals(FastList<?> otherFastList)
    {
        if (this.size() != otherFastList.size())
        {
            return false;
        }
        for (int i = 0; i < this.size; i++)
        {
            T one = this.items[i];
            Object two = otherFastList.items[i];
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @deprecated in 1.3
     */
    @Deprecated
    public boolean equals(FastList<?> otherList)
    {
        return this.fastListEquals(otherList);
    }

    private boolean regularListEquals(List<?> otherList)
    {
        Iterator<?> iterator = otherList.iterator();
        for (int i = 0; i < this.size; i++)
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

    private boolean randomAccessListEquals(List<?> otherList)
    {
        if (this.size() != otherList.size())
        {
            return false;
        }
        for (int i = 0; i < this.size; i++)
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
    public int hashCode()
    {
        int hashCode = 1;
        for (int i = 0; i < this.size; i++)
        {
            T item = this.items[i];
            hashCode = 31 * hashCode + (item == null ? 0 : item.hashCode());
        }
        return hashCode;
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.size());
        for (int i = 0; i < this.size; i++)
        {
            out.writeObject(this.items[i]);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.size = in.readInt();
        this.items = (T[]) new Object[this.size];
        for (int i = 0; i < this.size; i++)
        {
            this.items[i] = (T) in.readObject();
        }
    }
}
