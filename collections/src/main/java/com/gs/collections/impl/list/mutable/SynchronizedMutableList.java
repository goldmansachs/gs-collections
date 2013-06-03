/*
 * Copyright 2013 Goldman Sachs.
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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollection;
import com.gs.collections.impl.factory.Lists;
import net.jcip.annotations.GuardedBy;

/**
 * A synchronized view of a {@link MutableList}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableList#asSynchronized()
 */
public class SynchronizedMutableList<T>
        extends SynchronizedMutableCollection<T>
        implements MutableList<T>
{
    private static final long serialVersionUID = 1L;

    protected SynchronizedMutableList(MutableList<T> newCollection)
    {
        super(newCollection);
    }

    protected SynchronizedMutableList(MutableList<T> newCollection, Object newLock)
    {
        super(newCollection, newLock);
    }

    /**
     * This method will take a MutableList and wrap it directly in a SynchronizedMutableList.  It will
     * take any other non-GS-collection and first adapt it will a ListAdapter, and then return a
     * SynchronizedMutableList that wraps the adapter.
     */
    public static <E, L extends List<E>> SynchronizedMutableList<E> of(L list)
    {
        MutableList<E> mutableList =
                list instanceof MutableList ? (MutableList<E>) list : ListAdapter.adapt(list);
        return new SynchronizedMutableList<E>(mutableList);
    }

    /**
     * This method will take a MutableList and wrap it directly in a SynchronizedMutableList.  It will
     * take any other non-GS-collection and first adapt it will a ListAdapter, and then return a
     * SynchronizedMutableList that wraps the adapter.  Additionally, a developer specifies which lock to use
     * with the collection.
     */
    public static <E, L extends List<E>> SynchronizedMutableList<E> of(L list, Object lock)
    {
        MutableList<E> mutableList =
                list instanceof MutableList ? (MutableList<E>) list : ListAdapter.adapt(list);
        return new SynchronizedMutableList<E>(mutableList, lock);
    }

    @GuardedBy("getLock()")
    private MutableList<T> getMutableList()
    {
        return (MutableList<T>) this.getCollection();
    }

    @Override
    public boolean equals(Object obj)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().equals(obj);
        }
    }

    @Override
    public int hashCode()
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().hashCode();
        }
    }

    @Override
    public MutableList<T> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableMutableList.of(this);
        }
    }

    @Override
    public ImmutableList<T> toImmutable()
    {
        synchronized (this.getLock())
        {
            return Lists.immutable.ofAll(this);
        }
    }

    @Override
    public MutableList<T> asSynchronized()
    {
        return this;
    }

    @Override
    public MutableList<T> clone()
    {
        synchronized (this.getLock())
        {
            return of(this.getMutableList().clone());
        }
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().collect(function);
        }
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().flatCollect(function);
        }
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().collectIf(predicate, function);
        }
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().collectWith(function, parameter);
        }
    }

    @Override
    public <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().groupBy(function);
        }
    }

    @Override
    public <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().groupByEach(function);
        }
    }

    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.getMutableList().forEach(fromIndex, toIndex, procedure);
        }
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.getMutableList().reverseForEach(procedure);
        }
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        synchronized (this.getLock())
        {
            this.getMutableList().forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
        }
    }

    @Override
    public MutableList<T> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().newEmpty();
        }
    }

    @Override
    public MutableList<T> reject(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().reject(predicate);
        }
    }

    @Override
    public <P> MutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().rejectWith(predicate, parameter);
        }
    }

    @Override
    public MutableList<T> select(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().select(predicate);
        }
    }

    @Override
    public <P> MutableList<T> selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().selectWith(predicate, parameter);
        }
    }

    @Override
    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().partition(predicate);
        }
    }

    @Override
    public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().selectInstancesOf(clazz);
        }
    }

    public MutableList<T> distinct()
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().distinct();
        }
    }

    public MutableList<T> sortThis()
    {
        synchronized (this.getLock())
        {
            this.getMutableList().sortThis();
            return this;
        }
    }

    public MutableList<T> sortThis(Comparator<? super T> comparator)
    {
        synchronized (this.getLock())
        {
            this.getMutableList().sortThis(comparator);
            return this;
        }
    }

    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            this.getMutableList().sortThisBy(function);
            return this;
        }
    }

    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        synchronized (this.getLock())
        {
            return of(this.getMutableList().subList(fromIndex, toIndex), this.getLock());
        }
    }

    public void add(int index, T element)
    {
        synchronized (this.getLock())
        {
            this.getMutableList().add(index, element);
        }
    }

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().addAll(index, collection);
        }
    }

    public T get(int index)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().get(index);
        }
    }

    public int indexOf(Object o)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().indexOf(o);
        }
    }

    public int lastIndexOf(Object o)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().lastIndexOf(o);
        }
    }

    public ListIterator<T> listIterator()
    {
        return this.getMutableList().listIterator();
    }

    public ListIterator<T> listIterator(int index)
    {
        return this.getMutableList().listIterator(index);
    }

    public T remove(int index)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().remove(index);
        }
    }

    public T set(int index, T element)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().set(index, element);
        }
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().zip(that);
        }
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().zip(that, target);
        }
    }

    @Override
    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().zipWithIndex();
        }
    }

    public MutableList<T> toReversed()
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().toReversed();
        }
    }

    public MutableList<T> reverseThis()
    {
        synchronized (this.getLock())
        {
            this.getMutableList().reverseThis();
            return this;
        }
    }

    public MutableStack<T> toStack()
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().toStack();
        }
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().zipWithIndex(target);
        }
    }

    public MutableList<T> takeWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().takeWhile(predicate);
        }
    }

    public MutableList<T> dropWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().dropWhile(predicate);
        }
    }

    public PartitionMutableList<T> partitionWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableList().partitionWhile(predicate);
        }
    }

    @Override
    public MutableList<T> with(T element)
    {
        this.add(element);
        return this;
    }

    @Override
    public MutableList<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public MutableList<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public MutableList<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }
}
