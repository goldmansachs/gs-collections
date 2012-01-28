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

package ponzu.impl.collection.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import ponzu.api.LazyIterable;
import ponzu.api.RichIterable;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Function3;
import ponzu.api.block.function.Generator;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.collection.ImmutableCollection;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.map.sorted.MutableSortedMap;
import ponzu.api.multimap.MutableMultimap;
import ponzu.api.partition.PartitionMutableCollection;
import ponzu.api.set.MutableSet;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.api.tuple.Pair;
import ponzu.api.tuple.Twin;
import ponzu.impl.UnmodifiableIteratorAdapter;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.utility.LazyIterate;
import ponzu.impl.utility.internal.IterableIterate;

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

    public MutableCollection<T> filter(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().filter(predicate);
    }

    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        return this.getMutableCollection().filter(predicate, target);
    }

    public <P> MutableCollection<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().filterWith(predicate, parameter);
    }

    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return this.getMutableCollection().filterWith(predicate, parameter, targetCollection);
    }

    public MutableCollection<T> filterNot(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().filterNot(predicate);
    }

    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return this.getMutableCollection().filterNot(predicate, target);
    }

    public <P> MutableCollection<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().filterNotWith(predicate, parameter);
    }

    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return this.getMutableCollection().filterNotWith(predicate, parameter, targetCollection);
    }

    public <P> Twin<MutableList<T>> partitionWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.getMutableCollection().partitionWith(predicate, parameter);
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

    public <V> MutableCollection<V> transform(Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().transform(function);
    }

    public <V, R extends Collection<V>> R transform(Function<? super T, ? extends V> function, R target)
    {
        return this.getMutableCollection().transform(function, target);
    }

    public <V> MutableCollection<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableCollection().flatTransform(function);
    }

    public <V, R extends Collection<V>> R flatTransform(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return this.getMutableCollection().flatTransform(function, target);
    }

    public <P, A> MutableCollection<A> transformWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getMutableCollection().transformWith(function, parameter);
    }

    public <P, A, R extends Collection<A>> R transformWith(
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        return this.getMutableCollection().transformWith(function, parameter, targetCollection);
    }

    public <V> MutableCollection<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getMutableCollection().transformIf(predicate, function);
    }

    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        return this.getMutableCollection().transformIf(predicate, function, target);
    }

    public T find(Predicate<? super T> predicate)
    {
        return this.getMutableCollection().find(predicate);
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

    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
    {
        return this.getMutableCollection().findIfNone(predicate, function);
    }

    public <P> T findWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableCollection().findWith(predicate, parameter);
    }

    public <P> T findWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Generator<? extends T> function)
    {
        return this.getMutableCollection().findWithIfNone(predicate, parameter, function);
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

    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return this.getMutableCollection().foldLeft(initialValue, function);
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super T> function)
    {
        return this.getMutableCollection().foldLeft(initialValue, function);
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super T> function)
    {
        return this.getMutableCollection().foldLeft(initialValue, function);
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return this.getMutableCollection().foldLeft(initialValue, function);
    }

    public <IV, P> IV foldLeftWith(
            IV initialValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        return this.getMutableCollection().foldLeftWith(initialValue, function, parameter);
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
