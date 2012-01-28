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

package ponzu.impl.list.mutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Generator;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.partition.list.PartitionMutableList;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.multimap.list.FastListMultimap;
import ponzu.impl.utility.ArrayIterate;
import ponzu.impl.utility.Iterate;
import ponzu.impl.utility.ListIterate;

/**
 * This class provides a MutableList wrapper around a JDK Collections List interface instance.  All of the MutableList
 * interface methods are supported in addition to the JDK List interface methods.
 * <p/>
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
    public T find(Predicate<? super T> predicate)
    {
        return ListIterate.find(this.delegate, predicate);
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
    {
        T result = this.find(predicate);
        return result == null ? function.value() : result;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return ListIterate.count(this.delegate, predicate);
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
    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ListIterate.foldLeft(initialValue, this.delegate, function);
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

    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
    {
        return this.sortThis(Comparators.byFunction(function));
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
    public MutableList<T> filter(Predicate<? super T> predicate)
    {
        return ListIterate.filter(this.delegate, predicate, FastList.<T>newList());
    }

    @Override
    public MutableList<T> filterNot(Predicate<? super T> predicate)
    {
        return ListIterate.filterNot(this.delegate, predicate, FastList.<T>newList());
    }

    @Override
    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return ListIterate.partition(this.delegate, predicate);
    }

    @Override
    public <V> MutableList<V> transform(Function<? super T, ? extends V> function)
    {
        return ListIterate.transform(this.delegate, function, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public <V> MutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return ListIterate.flatTransform(this.delegate, function, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public <V> MutableList<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return ListIterate.transformIf(this.delegate, predicate, function, FastList.<V>newList());
    }

    @Override
    public <V> FastListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return ListIterate.groupBy(this.delegate, function, FastListMultimap.<V, T>newMultimap());
    }

    @Override
    public <V> FastListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return ListIterate.groupByEach(this.delegate, function, FastListMultimap.<V, T>newMultimap());
    }

    @Override
    public <P> MutableList<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.filterWith(this.delegate, predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P> MutableList<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.filterNotWith(this.delegate, predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, V> MutableList<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return ListIterate.transformWith(this.delegate, function, parameter, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return ListIterate.zip(this.delegate, that, FastList.<Pair<T, S>>newList(this.delegate.size()));
    }

    @Override
    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        return ListIterate.zipWithIndex(this.delegate, FastList.<Pair<T, Integer>>newList(this.delegate.size()));
    }
}
