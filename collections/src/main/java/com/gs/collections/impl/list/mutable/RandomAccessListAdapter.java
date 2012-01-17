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
import java.util.List;
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
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import com.gs.collections.impl.utility.internal.RandomAccessListIterate;

/**
 * This class provides a MutableList wrapper around a JDK Collections List interface instance.  All of the MutableList
 * interface methods are supported in addition to the JDK List interface methods.
 * <p/>
 * To create a new wrapper around an existing List instance, use the {@link #adapt(List)} factory method.
 */
public final class RandomAccessListAdapter<T>
        extends AbstractListAdapter<T>
        implements RandomAccess, Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<T> delegate;

    RandomAccessListAdapter(List<T> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("RandomAccessListAdapter may not wrap null");
        }
        if (!(newDelegate instanceof RandomAccess))
        {
            throw new IllegalArgumentException("RandomAccessListAdapter may not wrap a non RandomAccess list");
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
        return new RandomAccessListAdapter<E>(list);
    }

    public ImmutableList<T> toImmutable()
    {
        return Lists.immutable.ofAll(this.delegate);
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
        return Lists.mutable.of();
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
        RandomAccessListIterate.forEach(this.delegate, procedure);
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        ListIterate.reverseForEach(this.delegate, procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        RandomAccessListIterate.forEachWithIndex(this.delegate, objectIntProcedure);
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        RandomAccessListIterate.forEachWithIndex(this.delegate, fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public T find(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.find(this.delegate, predicate);
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.find(predicate);
        return result == null ? function.value() : result;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.count(this.delegate, predicate);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.anySatisfy(this.delegate, predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.allSatisfy(this.delegate, predicate);
    }

    @Override
    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return RandomAccessListIterate.foldLeft(initialValue, this.delegate, function);
    }

    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        RandomAccessListIterate.forEach(this.delegate, fromIndex, toIndex, procedure);
    }

    public RandomAccessListAdapter<T> sortThis(Comparator<? super T> comparator)
    {
        Iterate.sortThis(this.delegate, comparator);
        return this;
    }

    public RandomAccessListAdapter<T> sortThis()
    {
        return this.sortThis(Comparators.naturalOrder());
    }

    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
    {
        return this.sortThis(Comparators.byFunction(function));
    }

    public RandomAccessListAdapter<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public RandomAccessListAdapter<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public RandomAccessListAdapter<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public RandomAccessListAdapter<T> with(T... elements)
    {
        ArrayIterate.forEach(elements, CollectionAddProcedure.on(this.delegate));
        return this;
    }

    public RandomAccessListAdapter<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public RandomAccessListAdapter<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public RandomAccessListAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    @Override
    public MutableList<T> filter(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.filter(this.delegate, predicate, FastList.<T>newList());
    }

    @Override
    public MutableList<T> filterNot(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.filterNot(this.delegate, predicate, FastList.<T>newList());
    }

    @Override
    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.partition(this.delegate, predicate);
    }

    @Override
    public <V> MutableList<V> transform(Function<? super T, ? extends V> function)
    {
        return RandomAccessListIterate.transform(this.delegate, function, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public <V> MutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return RandomAccessListIterate.flatTransform(this.delegate, function, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public <V> MutableList<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return RandomAccessListIterate.tranformIf(this.delegate, predicate, function, FastList.<V>newList());
    }

    @Override
    public <V> FastListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return RandomAccessListIterate.groupBy(this.delegate, function, FastListMultimap.<V, T>newMultimap());
    }

    @Override
    public <V> FastListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return RandomAccessListIterate.groupByEach(this.delegate, function, FastListMultimap.<V, T>newMultimap());
    }

    @Override
    public <P> MutableList<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return RandomAccessListIterate.filterWith(this.delegate, predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P> MutableList<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return RandomAccessListIterate.filterNotWith(this.delegate, predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, V> MutableList<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return RandomAccessListIterate.transformWith(this.delegate, function, parameter, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return RandomAccessListIterate.zip(this.delegate, that, FastList.<Pair<T, S>>newList(this.delegate.size()));
    }

    @Override
    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        return RandomAccessListIterate.zipWithIndex(this.delegate, FastList.<Pair<T, Integer>>newList(this.delegate.size()));
    }
}
