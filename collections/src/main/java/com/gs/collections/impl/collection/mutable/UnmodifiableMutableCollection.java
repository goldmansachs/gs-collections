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

package com.gs.collections.impl.collection.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.UnmodifiableIteratorAdapter;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

/**
 * An unmodifiable view of a collection.
 *
 * @see MutableCollection#asUnmodifiable()
 */
public class UnmodifiableMutableCollection<T>
        implements MutableCollection<T>, Serializable
{
    private static final long serialVersionUID = 1L;

    private final MutableCollection<? extends T> collection;

    protected UnmodifiableMutableCollection(MutableCollection<? extends T> mutableCollection)
    {
        if (mutableCollection == null)
        {
            throw new NullPointerException();
        }
        this.collection = mutableCollection;
    }

    public int size()
    {
        return this.collection.size();
    }

    public boolean isEmpty()
    {
        return this.collection.isEmpty();
    }

    public boolean contains(Object o)
    {
        return this.collection.contains(o);
    }

    public Iterator<T> iterator()
    {
        return new UnmodifiableIteratorAdapter<T>(this.collection.iterator());
    }

    public Object[] toArray()
    {
        return this.collection.toArray();
    }

    public <S> S[] toArray(S[] a)
    {
        return this.collection.toArray(a);
    }

    public boolean add(T o)
    {
        throw new UnsupportedOperationException("Cannot add to an UnmodifiableMutableCollection.");
    }

    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot remove from an UnmodifiableMutableCollection.");
    }

    public boolean containsAll(Collection<?> c)
    {
        return this.collection.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c)
    {
        throw new UnsupportedOperationException("Cannot add to a UnmodifiableMutableCollection.");
    }

    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Cannot remove from UnmodifiableMutableCollection.");
    }

    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException("Cannot remove from an UnmodifiableMutableCollection.");
    }

    public void clear()
    {
        throw new UnsupportedOperationException("Cannot clear an UnmodifiableMutableCollection.");
    }

    /**
     * This method will take a MutableCollection and wrap it directly in a UnmodifiableMutableCollection.  It will
     * take any other non-GS-collection and first adapt it will a CollectionAdapter, and then return a
     * UnmodifiableMutableCollection that wraps the adapter.
     */
    public static <E, C extends Collection<E>> UnmodifiableMutableCollection<E> of(C collection)
    {
        if (collection == null)
        {
            throw new IllegalArgumentException("cannot create a UnmodifiableMutableCollection for null");
        }
        return new UnmodifiableMutableCollection<E>(CollectionAdapter.adapt(collection));
    }

    protected MutableCollection<T> getMutableCollection()
    {
        return (MutableCollection<T>) this.collection;
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        throw new UnsupportedOperationException();
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException();
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException();
    }

    public MutableCollection<T> asUnmodifiable()
    {
        return this;
    }

    public MutableCollection<T> asSynchronized()
    {
        return SynchronizedMutableCollection.of(this);
    }

    public ImmutableCollection<T> toImmutable()
    {
        return this.getMutableCollection().toImmutable();
    }

    public LazyIterable<T> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.getMutableCollection().forEach(procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.getMutableCollection().forEachWithIndex(objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        this.getMutableCollection().forEachWith(procedure, parameter);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return this.getMutableCollection().containsAllIterable(source);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return this.getMutableCollection().containsAllArguments(elements);
    }

    public boolean notEmpty()
    {
        return this.getMutableCollection().notEmpty();
    }

    public MutableCollection<T> newEmpty()
    {
        return this.getMutableCollection().newEmpty();
    }

    public T getFirst()
    {
        return this.getMutableCollection().getFirst();
    }

    public T getLast()
    {
        return this.getMutableCollection().getLast();
    }

    public MutableCollection<T> select(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().select(predicate);
    }

    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return this.getMutableCollection().select(predicate, target);
    }

    public <P> MutableCollection<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().selectWith(predicate, parameter);
    }

    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return this.getMutableCollection().selectWith(predicate, parameter, targetCollection);
    }

    public MutableCollection<T> reject(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().reject(predicate);
    }

    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return this.getMutableCollection().reject(predicate, target);
    }

    public <P> MutableCollection<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().rejectWith(predicate, parameter);
    }

    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return this.getMutableCollection().rejectWith(predicate, parameter, targetCollection);
    }

    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.getMutableCollection().selectAndRejectWith(predicate, parameter);
    }

    public PartitionMutableCollection<T> partition(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().partition(predicate);
    }

    public void removeIf(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException();
    }

    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    public <V> MutableCollection<V> collect(Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().collect(function);
    }

    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return this.getMutableCollection().collect(function, target);
    }

    public <V> MutableCollection<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableCollection().flatCollect(function);
    }

    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return this.getMutableCollection().flatCollect(function, target);
    }

    public <P, A> MutableCollection<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getMutableCollection().collectWith(function, parameter);
    }

    public <P, A, R extends Collection<A>> R collectWith(
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        return this.getMutableCollection().collectWith(function, parameter, targetCollection);
    }

    public <V> MutableCollection<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().collectIf(predicate, function);
    }

    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        return this.getMutableCollection().collectIf(predicate, function, target);
    }

    public T detect(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().detect(predicate);
    }

    public T min(Comparator<? super T> comparator)
    {
        return this.getMutableCollection().min(comparator);
    }

    public T max(Comparator<? super T> comparator)
    {
        return this.getMutableCollection().max(comparator);
    }

    public T min()
    {
        return this.getMutableCollection().min();
    }

    public T max()
    {
        return this.getMutableCollection().max();
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().minBy(function);
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().maxBy(function);
    }

    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return this.getMutableCollection().detectIfNone(predicate, function);
    }

    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().detectWith(predicate, parameter);
    }

    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        return this.getMutableCollection().detectWithIfNone(predicate, parameter, function);
    }

    public int count(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().count(predicate);
    }

    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().countWith(predicate, parameter);
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().anySatisfy(predicate);
    }

    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().anySatisfyWith(predicate, parameter);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().allSatisfy(predicate);
    }

    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().allSatisfyWith(predicate, parameter);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return this.getMutableCollection().injectInto(injectedValue, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        return this.getMutableCollection().injectInto(injectedValue, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        return this.getMutableCollection().injectInto(injectedValue, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return this.getMutableCollection().injectInto(injectedValue, function);
    }

    public <IV, P> IV injectIntoWith(
            IV injectValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        return this.getMutableCollection().injectIntoWith(injectValue, function, parameter);
    }

    public MutableList<T> toList()
    {
        return this.getMutableCollection().toList();
    }

    public MutableList<T> toSortedList()
    {
        return this.getMutableCollection().toSortedList();
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return this.getMutableCollection().toSortedList(comparator);
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().toSortedList(Comparators.byFunction(function));
    }

    public MutableSortedSet<T> toSortedSet()
    {
        return this.getMutableCollection().toSortedSet();
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return this.getMutableCollection().toSortedSet(comparator);
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().toSortedSetBy(function);
    }

    public MutableSet<T> toSet()
    {
        return this.getMutableCollection().toSet();
    }

    public MutableBag<T> toBag()
    {
        return this.getMutableCollection().toBag();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return this.getMutableCollection().toMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return this.getMutableCollection().toSortedMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return this.getMutableCollection().toSortedMap(comparator, keyFunction, valueFunction);
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        IterableIterate.appendString(this, appendable, start, separator, end);
    }

    public <V> MutableMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().groupBy(function);
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        return this.getMutableCollection().groupBy(function, target);
    }

    public <V> MutableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableCollection().groupByEach(function);
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return this.getMutableCollection().groupByEach(function, target);
    }

    public <S> MutableCollection<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.getMutableCollection().zip(that);
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return this.getMutableCollection().zip(that, target);
    }

    public MutableCollection<Pair<T, Integer>> zipWithIndex()
    {
        return this.getMutableCollection().zipWithIndex();
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return this.getMutableCollection().zipWithIndex(target);
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        return this.getMutableCollection().chunk(size);
    }

    public MutableCollection<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
    }

    public MutableCollection<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
    }

    public MutableCollection<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
    }

    public MutableCollection<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
    }
}
