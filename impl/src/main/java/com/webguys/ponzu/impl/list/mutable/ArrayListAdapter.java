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

package com.webguys.ponzu.impl.list.mutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.RandomAccess;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.multimap.list.FastListMultimap;
import com.webguys.ponzu.impl.utility.ArrayIterate;
import com.webguys.ponzu.impl.utility.ArrayListIterate;
import com.webguys.ponzu.impl.utility.Iterate;

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
    public T find(Predicate<? super T> predicate)
    {
        return ArrayListIterate.find(this.delegate, predicate);
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
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
    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ArrayListIterate.foldLeft(initialValue, this.delegate, function);
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
    public ArrayListAdapter<T> filter(Predicate<? super T> predicate)
    {
        return this.wrap(ArrayListIterate.filter(this.delegate, predicate));
    }

    @Override
    public ArrayListAdapter<T> filterNot(Predicate<? super T> predicate)
    {
        return this.wrap(ArrayListIterate.filterNot(this.delegate, predicate));
    }

    @Override
    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return ArrayListIterate.partition(this.delegate, predicate);
    }

    @Override
    public <V> ArrayListAdapter<V> transform(Function<? super T, ? extends V> function)
    {
        return this.wrap(ArrayListIterate.transform(this.delegate, function));
    }

    @Override
    public <V> ArrayListAdapter<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.wrap(ArrayListIterate.flatTransform(this.delegate, function));
    }

    @Override
    public <V> ArrayListAdapter<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.wrap(ArrayListIterate.transformIf(this.delegate, predicate, function));
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
    public <P> ArrayListAdapter<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.wrap(ArrayListIterate.filterWith(this.delegate, predicate, parameter));
    }

    @Override
    public <P> ArrayListAdapter<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.wrap(ArrayListIterate.filterNotWith(this.delegate, predicate, parameter));
    }

    @Override
    public <P, A> ArrayListAdapter<A> transformWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.wrap(ArrayListIterate.transformWith(this.delegate, function, parameter));
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
