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

package com.gs.collections.impl.list.mutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.lazy.parallel.list.NonParallelListIterable;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import com.gs.collections.impl.utility.OrderedIterate;

/**
 * This class provides a MutableList wrapper around a JDK Collections List interface instance.  All of the MutableList
 * interface methods are supported in addition to the JDK List interface methods.
 * <p>
 * To create a new wrapper around an existing List instance, use the {@link #adapt(List)} factory method.
 */
public final class ListAdapter<T>
        extends AbstractListAdapter<T>
        implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<T> delegate;

    ListAdapter(List<T> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("ListAdapter may not wrap null");
        }
        if (newDelegate instanceof RandomAccess)
        {
            throw new IllegalArgumentException("Use RandomAccessListAdapter instead");
        }
        this.delegate = newDelegate;
    }

    @Override
    protected List<T> getDelegate()
    {
        return this.delegate;
    }

    public static <E> MutableList<E> adapt(List<E> list)
    {
        if (list instanceof MutableList)
        {
            return (MutableList<E>) list;
        }
        if (list instanceof ArrayList)
        {
            return ArrayListAdapter.adapt((ArrayList<E>) list);
        }
        if (list instanceof RandomAccess)
        {
            return new RandomAccessListAdapter<E>(list);
        }
        return new ListAdapter<E>(list);
    }

    public ImmutableList<T> toImmutable()
    {
        return Lists.immutable.withAll(this.delegate);
    }

    public MutableList<T> asUnmodifiable()
    {
        return UnmodifiableMutableList.of(this);
    }

    public MutableList<T> asSynchronized()
    {
        return SynchronizedMutableList.of(this);
    }

    @Override
    public MutableList<T> clone()
    {
        return FastList.newList(this.delegate);
    }

    /**
     * @deprecated use {@link FastList#newList()} instead (inlineable)
     */
    @Deprecated
    public MutableList<T> newEmpty()
    {
        return FastList.newList();
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
        this.each(procedure);
    }

    @Override
    public void each(Procedure<? super T> procedure)
    {
        ListIterate.forEach(this.delegate, procedure);
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        ListIterate.reverseForEach(this.delegate, procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.forEachWithIndex(this.delegate, objectIntProcedure);
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.forEachWithIndex(this.delegate, fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return ListIterate.detect(this.delegate, predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.detect(predicate);
        return result == null ? function.value() : result;
    }

    public int detectIndex(Predicate<? super T> predicate)
    {
        return ListIterate.detectIndex(this.delegate, predicate);
    }

    public int detectLastIndex(Predicate<? super T> predicate)
    {
        return ListIterate.detectLastIndex(this.delegate, predicate);
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return ListIterate.count(this.delegate, predicate);
    }

    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return OrderedIterate.corresponds(this, other, predicate);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ListIterate.anySatisfy(this.delegate, predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ListIterate.allSatisfy(this.delegate, predicate);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return ListIterate.noneSatisfy(this.delegate, predicate);
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ListIterate.injectInto(injectedValue, this.delegate, function);
    }

    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        ListIterate.forEach(this.delegate, fromIndex, toIndex, procedure);
    }

    public ListAdapter<T> sortThis(Comparator<? super T> comparator)
    {
        Iterate.sortThis(this.delegate, comparator);
        return this;
    }

    public ListAdapter<T> sortThis()
    {
        return this.sortThis(Comparators.naturalOrder());
    }

    public ListAdapter<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public ListAdapter<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public ListAdapter<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public ListAdapter<T> with(T... elements)
    {
        ArrayIterate.forEach(elements, CollectionAddProcedure.on(this.delegate));
        return this;
    }

    public ListAdapter<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public ListAdapter<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public ListAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    @Override
    public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
    {
        return ListIterate.selectInstancesOf(this.delegate, clazz);
    }

    public MutableList<T> distinct()
    {
        return ListIterate.distinct(this.delegate);
    }

    public MutableList<T> distinct(HashingStrategy<? super T> hashingStrategy)
    {
        return ListIterate.distinct(this.delegate, hashingStrategy);
    }

    public MutableList<T> take(int count)
    {
        return ListIterate.take(this, count);
    }

    public MutableList<T> takeWhile(Predicate<? super T> predicate)
    {
        return ListIterate.takeWhile(this.delegate, predicate);
    }

    public MutableList<T> drop(int count)
    {
        return ListIterate.drop(this, count);
    }

    public MutableList<T> dropWhile(Predicate<? super T> predicate)
    {
        return ListIterate.dropWhile(this.delegate, predicate);
    }

    public PartitionMutableList<T> partitionWhile(Predicate<? super T> predicate)
    {
        return ListIterate.partitionWhile(this.delegate, predicate);
    }

    @Override
    public MutableStack<T> toStack()
    {
        return ArrayStack.newStack(this.delegate);
    }

    @Override
    public ParallelListIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return new NonParallelListIterable<T>(this);
    }
}
