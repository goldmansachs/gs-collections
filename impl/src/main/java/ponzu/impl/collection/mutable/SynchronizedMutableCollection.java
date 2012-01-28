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

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
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
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.set.sorted.mutable.TreeSortedSet;
import ponzu.impl.utility.LazyIterate;
import ponzu.impl.utility.internal.IterableIterate;

/**
 * A synchronized view of a collection.
 *
 * @see MutableCollection#asSynchronized()
 */
@ThreadSafe
public class SynchronizedMutableCollection<E>
        implements MutableCollection<E>, Serializable
{
    private static final long serialVersionUID = 1L;

    private final Object lock;
    @GuardedBy("this.lock")
    private final MutableCollection<E> collection;

    protected SynchronizedMutableCollection(MutableCollection<E> newCollection)
    {
        this(newCollection, null);
    }

    protected SynchronizedMutableCollection(MutableCollection<E> newCollection, Object newLock)
    {
        if (newCollection == null)
        {
            throw new IllegalArgumentException("Cannot create a SynchronizedMutableCollection on a null collection");
        }
        this.collection = newCollection;
        this.lock = newLock == null ? this : newLock;
    }

    /**
     * This method will take a MutableCollection and wrap it directly in a SynchronizedMutableCollection.  It will
     * take any other non-GS-collection and first adapt it will a CollectionAdapter, and then return a
     * SynchronizedMutableCollection that wraps the adapter.
     */
    public static <E, C extends Collection<E>> SynchronizedMutableCollection<E> of(C collection)
    {
        return new SynchronizedMutableCollection<E>(CollectionAdapter.adapt(collection));
    }

    /**
     * This method will take a MutableCollection and wrap it directly in a SynchronizedMutableCollection.  It will
     * take any other non-GS-collection and first adapt it will a CollectionAdapter, and then return a
     * SynchronizedMutableCollection that wraps the adapter.  Additionally, a developer specifies which lock to use
     * with the collection.
     */
    public static <E, C extends Collection<E>> SynchronizedMutableCollection<E> of(C collection, Object lock)
    {
        return new SynchronizedMutableCollection<E>(CollectionAdapter.adapt(collection), lock);
    }

    protected Object getLock()
    {
        return this.lock;
    }

    protected MutableCollection<E> getCollection()
    {
        return this.collection;
    }

    public int size()
    {
        synchronized (this.lock)
        {
            return this.collection.size();
        }
    }

    public boolean isEmpty()
    {
        synchronized (this.lock)
        {
            return this.collection.isEmpty();
        }
    }

    public boolean contains(Object o)
    {
        synchronized (this.lock)
        {
            return this.collection.contains(o);
        }
    }

    public Object[] toArray()
    {
        synchronized (this.lock)
        {
            return this.collection.toArray();
        }
    }

    public <T> T[] toArray(T[] a)
    {
        synchronized (this.lock)
        {
            return this.collection.toArray(a);
        }
    }

    @Override
    public String toString()
    {
        synchronized (this.lock)
        {
            return this.collection.toString();
        }
    }

    public Iterator<E> iterator()
    {
        return this.collection.iterator();  // this must be manually synchronized by the developer
    }

    public boolean add(E o)
    {
        synchronized (this.lock)
        {
            return this.collection.add(o);
        }
    }

    public boolean remove(Object o)
    {
        synchronized (this.lock)
        {
            return this.collection.remove(o);
        }
    }

    public boolean containsAll(Collection<?> coll)
    {
        synchronized (this.lock)
        {
            return this.collection.containsAll(coll);
        }
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        synchronized (this.lock)
        {
            return this.collection.containsAllIterable(source);
        }
    }

    public boolean containsAllArguments(Object... elements)
    {
        synchronized (this.lock)
        {
            return this.collection.containsAllArguments(elements);
        }
    }

    public boolean addAll(Collection<? extends E> coll)
    {
        synchronized (this.lock)
        {
            return this.collection.addAll(coll);
        }
    }

    public boolean removeAll(Collection<?> coll)
    {
        synchronized (this.lock)
        {
            return this.collection.removeAll(coll);
        }
    }

    public boolean retainAll(Collection<?> coll)
    {
        synchronized (this.lock)
        {
            return this.collection.retainAll(coll);
        }
    }

    public void clear()
    {
        synchronized (this.lock)
        {
            this.collection.clear();
        }
    }

    public void forEach(Procedure<? super E> procedure)
    {
        synchronized (this.lock)
        {
            this.collection.forEach(procedure);
        }
    }

    public <P> void forEachWith(Procedure2<? super E, ? super P> procedure, P parameter)
    {
        synchronized (this.lock)
        {
            this.collection.forEachWith(procedure, parameter);
        }
    }

    public void forEachWithIndex(ObjectIntProcedure<? super E> objectIntProcedure)
    {
        synchronized (this.lock)
        {
            this.collection.forEachWithIndex(objectIntProcedure);
        }
    }

    public boolean allSatisfy(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.allSatisfy(predicate);
        }
    }

    public <P> boolean allSatisfyWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.allSatisfyWith(predicate, parameter);
        }
    }

    public boolean anySatisfy(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.anySatisfy(predicate);
        }
    }

    public <P> boolean anySatisfyWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.anySatisfyWith(predicate, parameter);
        }
    }

    public MutableList<E> toList()
    {
        synchronized (this.lock)
        {
            return this.collection.toList();
        }
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super E, ? extends NK> keyFunction,
            Function<? super E, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.collection.toMap(keyFunction, valueFunction);
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super E, ? extends NK> keyFunction,
            Function<? super E, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedMap(keyFunction, valueFunction);
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super E, ? extends NK> keyFunction,
            Function<? super E, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedMap(comparator, keyFunction, valueFunction);
        }
    }

    public LazyIterable<E> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public MutableSet<E> toSet()
    {
        synchronized (this.lock)
        {
            return this.collection.toSet();
        }
    }

    public MutableBag<E> toBag()
    {
        synchronized (this.lock)
        {
            return this.collection.toBag();
        }
    }

    public MutableList<E> toSortedList()
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedList();
        }
    }

    public MutableList<E> toSortedList(Comparator<? super E> comparator)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedList(comparator);
        }
    }

    public <V extends Comparable<? super V>> MutableList<E> toSortedListBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedList(Comparators.byFunction(function));
        }
    }

    public MutableSortedSet<E> toSortedSet()
    {
        synchronized (this.lock)
        {
            return TreeSortedSet.newSet(null, this);
        }
    }

    public MutableSortedSet<E> toSortedSet(Comparator<? super E> comparator)
    {
        synchronized (this.lock)
        {
            return TreeSortedSet.newSet(comparator, this);
        }
    }

    public <V extends Comparable<? super V>> MutableSortedSet<E> toSortedSetBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.toSortedSet(Comparators.byFunction(function));
        }
    }

    public MutableCollection<E> asUnmodifiable()
    {
        synchronized (this.lock)
        {
            return new UnmodifiableMutableCollection<E>(this);
        }
    }

    public MutableCollection<E> asSynchronized()
    {
        return this;
    }

    public ImmutableCollection<E> toImmutable()
    {
        synchronized (this.lock)
        {
            return this.collection.toImmutable();
        }
    }

    public <V> MutableCollection<V> transform(Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.transform(function);
        }
    }

    public <V, R extends Collection<V>> R transform(Function<? super E, ? extends V> function, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.transform(function, target);
        }
    }

    public <V> MutableCollection<V> flatTransform(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.lock)
        {
            return this.collection.flatTransform(function);
        }
    }

    public <V, R extends Collection<V>> R flatTransform(Function<? super E, ? extends Iterable<V>> function, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.flatTransform(function, target);
        }
    }

    public <V> MutableCollection<V> transformIf(
            Predicate<? super E> predicate,
            Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.transformIf(predicate, function);
        }
    }

    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super E> predicate,
            Function<? super E, ? extends V> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.collection.transformIf(predicate, function, target);
        }
    }

    public <P, A> MutableCollection<A> transformWith(Function2<? super E, ? super P, ? extends A> function, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.transformWith(function, parameter);
        }
    }

    public <P, A, R extends Collection<A>> R transformWith(
            Function2<? super E, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.collection.transformWith(function, parameter, targetCollection);
        }
    }

    public int count(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.count(predicate);
        }
    }

    public <P> int countWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.countWith(predicate, parameter);
        }
    }

    public E find(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.find(predicate);
        }
    }

    public E min(Comparator<? super E> comparator)
    {
        synchronized (this.lock)
        {
            return this.collection.min(comparator);
        }
    }

    public E max(Comparator<? super E> comparator)
    {
        synchronized (this.lock)
        {
            return this.collection.max(comparator);
        }
    }

    public E min()
    {
        synchronized (this.lock)
        {
            return this.collection.min();
        }
    }

    public E max()
    {
        synchronized (this.lock)
        {
            return this.collection.max();
        }
    }

    public <V extends Comparable<? super V>> E minBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.minBy(function);
        }
    }

    public <V extends Comparable<? super V>> E maxBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.maxBy(function);
        }
    }

    public E findIfNone(Predicate<? super E> predicate, Generator<? extends E> function)
    {
        synchronized (this.lock)
        {
            return this.collection.findIfNone(predicate, function);
        }
    }

    public <P> E findWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.findWith(predicate, parameter);
        }
    }

    public <P> E findWithIfNone(
            Predicate2<? super E, ? super P> predicate,
            P parameter,
            Generator<? extends E> function)
    {
        synchronized (this.lock)
        {
            return this.collection.findWithIfNone(predicate, parameter, function);
        }
    }

    public E getFirst()
    {
        synchronized (this.lock)
        {
            return this.collection.getFirst();
        }
    }

    public E getLast()
    {
        synchronized (this.lock)
        {
            return this.collection.getLast();
        }
    }

    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super E, ? extends IV> function)
    {
        synchronized (this.lock)
        {
            return this.collection.foldLeft(initialValue, function);
        }
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super E> function)
    {
        synchronized (this.lock)
        {
            return this.collection.foldLeft(initialValue, function);
        }
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super E> function)
    {
        synchronized (this.lock)
        {
            return this.collection.foldLeft(initialValue, function);
        }
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super E> function)
    {
        synchronized (this.lock)
        {
            return this.collection.foldLeft(initialValue, function);
        }
    }

    public <IV, P> IV foldLeftWith(
            IV initialValue,
            Function3<? super IV, ? super E, ? super P, ? extends IV> function,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.foldLeftWith(initialValue, function, parameter);
        }
    }

    public MutableCollection<E> newEmpty()
    {
        synchronized (this.lock)
        {
            return this.collection.newEmpty();
        }
    }

    public boolean notEmpty()
    {
        synchronized (this.lock)
        {
            return this.collection.notEmpty();
        }
    }

    public MutableCollection<E> filterNot(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.filterNot(predicate);
        }
    }

    public <R extends Collection<E>> R filterNot(Predicate<? super E> predicate, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.filterNot(predicate, target);
        }
    }

    public <P> MutableCollection<E> filterNotWith(
            Predicate2<? super E, ? super P> predicate,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.filterNotWith(predicate, parameter);
        }
    }

    public <P, R extends Collection<E>> R filterNotWith(
            Predicate2<? super E, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.collection.filterNotWith(predicate, parameter, targetCollection);
        }
    }

    public void removeIf(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            this.collection.removeIf(predicate);
        }
    }

    public <P> void removeIfWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            this.collection.removeIfWith(predicate, parameter);
        }
    }

    public MutableCollection<E> filter(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.filter(predicate);
        }
    }

    public <R extends Collection<E>> R filter(Predicate<? super E> predicate, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.filter(predicate, target);
        }
    }

    public <P> Twin<MutableList<E>> partitionWith(
            Predicate2<? super E, ? super P> predicate,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.partitionWith(predicate, parameter);
        }
    }

    public PartitionMutableCollection<E> partition(Predicate<? super E> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.partition(predicate);
        }
    }

    public <P> MutableCollection<E> filterWith(
            Predicate2<? super E, ? super P> predicate,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.filterWith(predicate, parameter);
        }
    }

    public <P, R extends Collection<E>> R filterWith(
            Predicate2<? super E, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.collection.filterWith(predicate, parameter, targetCollection);
        }
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
        synchronized (this.lock)
        {
            IterableIterate.appendString(this, appendable, start, separator, end);
        }
    }

    public <V> MutableMultimap<V, E> groupBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.groupBy(function);
        }
    }

    public <V> MutableMultimap<V, E> groupByEach(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.lock)
        {
            return this.collection.groupByEach(function);
        }
    }

    public <V, R extends MutableMultimap<V, E>> R groupBy(
            Function<? super E, ? extends V> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.collection.groupBy(function, target);
        }
    }

    public <V, R extends MutableMultimap<V, E>> R groupByEach(
            Function<? super E, ? extends Iterable<V>> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.collection.groupByEach(function, target);
        }
    }

    public <S> MutableCollection<Pair<E, S>> zip(Iterable<S> that)
    {
        synchronized (this.lock)
        {
            return this.collection.zip(that);
        }
    }

    public <S, R extends Collection<Pair<E, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.zip(that, target);
        }
    }

    public MutableCollection<Pair<E, Integer>> zipWithIndex()
    {
        synchronized (this.lock)
        {
            return this.collection.zipWithIndex();
        }
    }

    public <R extends Collection<Pair<E, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.lock)
        {
            return this.collection.zipWithIndex(target);
        }
    }

    public RichIterable<RichIterable<E>> chunk(int size)
    {
        synchronized (this.lock)
        {
            return this.collection.chunk(size);
        }
    }

    public boolean addAllIterable(Iterable<? extends E> iterable)
    {
        synchronized (this.lock)
        {
            return this.collection.addAllIterable(iterable);
        }
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        synchronized (this.lock)
        {
            return this.collection.removeAllIterable(iterable);
        }
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        synchronized (this.lock)
        {
            return this.collection.retainAllIterable(iterable);
        }
    }

    public MutableCollection<E> with(E element)
    {
        this.add(element);
        return this;
    }

    public MutableCollection<E> without(E element)
    {
        this.remove(element);
        return this;
    }

    public MutableCollection<E> withAll(Iterable<? extends E> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public MutableCollection<E> withoutAll(Iterable<? extends E> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }
}
