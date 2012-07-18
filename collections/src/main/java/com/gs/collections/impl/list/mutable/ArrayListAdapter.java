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

package com.gs.collections.impl.list.mutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.ArrayListIterate;
import com.gs.collections.impl.utility.Iterate;

/**
 * This class provides a MutableList wrapper around a JDK Collections ArrayList instance.  All of the MutableList
 * interface methods are supported in addition to the JDK ArrayList methods.
 * <p/>
 * To create a new wrapper around an existing ArrayList instance, use the {@link #adapt(ArrayList)} factory method.  To
 * create a new empty wrapper, use the {@link #newList()} or {@link #newList(int)} factory methods.
 */
public final class ArrayListAdapter<T>
        extends AbstractListAdapter<T>
        implements RandomAccess, Serializable
{
    private static final long serialVersionUID = 1L;
    private final ArrayList<T> delegate;

    private ArrayListAdapter(ArrayList<T> newDelegate)
    {
        this.delegate = newDelegate;
    }

    @Override
    protected ArrayList<T> getDelegate()
    {
        return this.delegate;
    }

    public static <E> ArrayListAdapter<E> newList()
    {
        return new ArrayListAdapter<E>(new ArrayList<E>());
    }

    public static <E> ArrayListAdapter<E> newList(int size)
    {
        return new ArrayListAdapter<E>(new ArrayList<E>(size));
    }

    public static <E> ArrayListAdapter<E> adapt(ArrayList<E> newDelegate)
    {
        return new ArrayListAdapter<E>(newDelegate);
    }

    public MutableList<T> asUnmodifiable()
    {
        return UnmodifiableMutableList.of(this);
    }

    public MutableList<T> asSynchronized()
    {
        return SynchronizedMutableList.of(this);
    }

    public ImmutableList<T> toImmutable()
    {
        return Lists.immutable.ofAll(this);
    }

    @Override
    public ArrayListAdapter<T> clone()
    {
        return new ArrayListAdapter<T>((ArrayList<T>) this.delegate.clone());
    }

    public ArrayListAdapter<T> newEmpty()
    {
        return ArrayListAdapter.newList();
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
        ArrayListIterate.forEach(this.delegate, procedure);
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        ArrayListIterate.reverseForEach(this.delegate, procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ArrayListIterate.forEachWithIndex(this.delegate, objectIntProcedure);
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ArrayListIterate.forEachWithIndex(this.delegate, fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return ArrayListIterate.detect(this.delegate, predicate);
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
        return ArrayListIterate.count(this.delegate, predicate);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ArrayListIterate.anySatisfy(this.delegate, predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ArrayListIterate.allSatisfy(this.delegate, predicate);
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ArrayListIterate.injectInto(injectedValue, this.delegate, function);
    }

    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        ArrayListIterate.forEach(this.delegate, fromIndex, toIndex, procedure);
    }

    public ArrayListAdapter<T> sortThis(Comparator<? super T> comparator)
    {
        Iterate.sortThis(this.delegate, comparator);
        return this;
    }

    public ArrayListAdapter<T> sortThis()
    {
        return this.sortThis(Comparators.naturalOrder());
    }

    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
    {
        return this.sortThis(Comparators.byFunction(function));
    }

    public ArrayListAdapter<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public ArrayListAdapter<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public ArrayListAdapter<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public ArrayListAdapter<T> with(T... elements)
    {
        ArrayIterate.forEach(elements, CollectionAddProcedure.on(this.delegate));
        return this;
    }

    public ArrayListAdapter<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public ArrayListAdapter<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public ArrayListAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    private <E> ArrayListAdapter<E> wrap(ArrayList<E> list)
    {
        return ArrayListAdapter.adapt(list);
    }

    @Override
    public ArrayListAdapter<T> select(Predicate<? super T> predicate)
    {
        return this.wrap(ArrayListIterate.select(this.delegate, predicate));
    }

    @Override
    public ArrayListAdapter<T> reject(Predicate<? super T> predicate)
    {
        return this.wrap(ArrayListIterate.reject(this.delegate, predicate));
    }

    @Override
    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return ArrayListIterate.partition(this.delegate, predicate);
    }

    @Override
    public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
    {
        return ArrayListIterate.selectInstancesOf(this.delegate, clazz);
    }

    @Override
    public <V> ArrayListAdapter<V> collect(Function<? super T, ? extends V> function)
    {
        return this.wrap(ArrayListIterate.collect(this.delegate, function));
    }

    @Override
    public <V> ArrayListAdapter<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.wrap(ArrayListIterate.flatCollect(this.delegate, function));
    }

    @Override
    public <V> ArrayListAdapter<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.wrap(ArrayListIterate.collectIf(this.delegate, predicate, function));
    }

    @Override
    public <V> FastListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return ArrayListIterate.groupBy(this.delegate, function);
    }

    @Override
    public <V> FastListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return ArrayListIterate.groupByEach(this.delegate, function);
    }

    @Override
    public <P> ArrayListAdapter<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.wrap(ArrayListIterate.selectWith(this.delegate, predicate, parameter));
    }

    @Override
    public <P> ArrayListAdapter<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.wrap(ArrayListIterate.rejectWith(this.delegate, predicate, parameter));
    }

    @Override
    public <P, A> ArrayListAdapter<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.wrap(ArrayListIterate.collectWith(this.delegate, function, parameter));
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return ArrayListIterate.zip(this.delegate, that);
    }

    @Override
    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        return ArrayListIterate.zipWithIndex(this.delegate);
    }
}
