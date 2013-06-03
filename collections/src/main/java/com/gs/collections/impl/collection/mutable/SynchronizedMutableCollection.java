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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
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
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A synchronized view of a {@link MutableCollection}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableCollection#asSynchronized()
 */
@ThreadSafe
public class SynchronizedMutableCollection<T>
        implements MutableCollection<T>, Serializable
{
    private static final long serialVersionUID = 1L;

    private final Object lock;
    @GuardedBy("this.lock")
    private final MutableCollection<T> collection;

    protected SynchronizedMutableCollection(MutableCollection<T> newCollection)
    {
        this(newCollection, null);
    }

    protected SynchronizedMutableCollection(MutableCollection<T> newCollection, Object newLock)
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

    protected MutableCollection<T> getCollection()
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

    /**
     * Must be called in a synchronized block.
     */
    public Iterator<T> iterator()
    {
        return this.collection.iterator();
    }

    public boolean add(T o)
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

    public boolean addAll(Collection<? extends T> coll)
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

    public void forEach(Procedure<? super T> procedure)
    {
        synchronized (this.lock)
        {
            this.collection.forEach(procedure);
        }
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        synchronized (this.lock)
        {
            this.collection.forEachWith(procedure, parameter);
        }
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        synchronized (this.lock)
        {
            this.collection.forEachWithIndex(objectIntProcedure);
        }
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.allSatisfy(predicate);
        }
    }

    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.allSatisfyWith(predicate, parameter);
        }
    }

    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.noneSatisfy(predicate);
        }
    }

    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.noneSatisfyWith(predicate, parameter);
        }
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.anySatisfy(predicate);
        }
    }

    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.anySatisfyWith(predicate, parameter);
        }
    }

    public MutableList<T> toList()
    {
        synchronized (this.lock)
        {
            return this.collection.toList();
        }
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.collection.toMap(keyFunction, valueFunction);
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedMap(keyFunction, valueFunction);
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedMap(comparator, keyFunction, valueFunction);
        }
    }

    public LazyIterable<T> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public MutableSet<T> toSet()
    {
        synchronized (this.lock)
        {
            return this.collection.toSet();
        }
    }

    public MutableBag<T> toBag()
    {
        synchronized (this.lock)
        {
            return this.collection.toBag();
        }
    }

    public MutableList<T> toSortedList()
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedList();
        }
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedList(comparator);
        }
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.toSortedList(Comparators.byFunction(function));
        }
    }

    public MutableSortedSet<T> toSortedSet()
    {
        synchronized (this.lock)
        {
            return TreeSortedSet.newSet(null, this);
        }
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        synchronized (this.lock)
        {
            return TreeSortedSet.newSet(comparator, this);
        }
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.toSortedSet(Comparators.byFunction(function));
        }
    }

    public MutableCollection<T> asUnmodifiable()
    {
        synchronized (this.lock)
        {
            return new UnmodifiableMutableCollection<T>(this);
        }
    }

    public MutableCollection<T> asSynchronized()
    {
        return this;
    }

    public ImmutableCollection<T> toImmutable()
    {
        synchronized (this.lock)
        {
            return this.collection.toImmutable();
        }
    }

    public <V> MutableCollection<V> collect(Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.collect(function);
        }
    }

    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.collect(function, target);
        }
    }

    public <V> MutableCollection<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.lock)
        {
            return this.collection.flatCollect(function);
        }
    }

    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.flatCollect(function, target);
        }
    }

    public <V> MutableCollection<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.collectIf(predicate, function);
        }
    }

    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.collection.collectIf(predicate, function, target);
        }
    }

    public <P, A> MutableCollection<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.collectWith(function, parameter);
        }
    }

    public <P, A, R extends Collection<A>> R collectWith(
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.collection.collectWith(function, parameter, targetCollection);
        }
    }

    public int count(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.count(predicate);
        }
    }

    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.countWith(predicate, parameter);
        }
    }

    public T detect(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.detect(predicate);
        }
    }

    public T min(Comparator<? super T> comparator)
    {
        synchronized (this.lock)
        {
            return this.collection.min(comparator);
        }
    }

    public T max(Comparator<? super T> comparator)
    {
        synchronized (this.lock)
        {
            return this.collection.max(comparator);
        }
    }

    public T min()
    {
        synchronized (this.lock)
        {
            return this.collection.min();
        }
    }

    public T max()
    {
        synchronized (this.lock)
        {
            return this.collection.max();
        }
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.minBy(function);
        }
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.maxBy(function);
        }
    }

    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.detectIfNone(predicate, function);
        }
    }

    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.detectWith(predicate, parameter);
        }
    }

    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.detectWithIfNone(predicate, parameter, function);
        }
    }

    public T getFirst()
    {
        synchronized (this.lock)
        {
            return this.collection.getFirst();
        }
    }

    public T getLast()
    {
        synchronized (this.lock)
        {
            return this.collection.getLast();
        }
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        synchronized (this.lock)
        {
            return this.collection.injectInto(injectedValue, function);
        }
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.injectInto(injectedValue, function);
        }
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.injectInto(injectedValue, function);
        }
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.injectInto(injectedValue, function);
        }
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.injectInto(injectedValue, function);
        }
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.sumOfInt(function);
        }
    }

    public double sumOfFloat(FloatFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.sumOfFloat(function);
        }
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.sumOfLong(function);
        }
    }

    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        synchronized (this.lock)
        {
            return this.collection.sumOfDouble(function);
        }
    }

    public <IV, P> IV injectIntoWith(
            IV injectValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.injectIntoWith(injectValue, function, parameter);
        }
    }

    public MutableCollection<T> newEmpty()
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

    public MutableCollection<T> reject(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.reject(predicate);
        }
    }

    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.reject(predicate, target);
        }
    }

    public <P> MutableCollection<T> rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.rejectWith(predicate, parameter);
        }
    }

    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.collection.rejectWith(predicate, parameter, targetCollection);
        }
    }

    public void removeIf(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            this.collection.removeIf(predicate);
        }
    }

    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            this.collection.removeIfWith(predicate, parameter);
        }
    }

    public MutableCollection<T> select(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.select(predicate);
        }
    }

    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.select(predicate, target);
        }
    }

    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.selectAndRejectWith(predicate, parameter);
        }
    }

    public PartitionMutableCollection<T> partition(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.collection.partition(predicate);
        }
    }

    public <P> MutableCollection<T> selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.collection.selectWith(predicate, parameter);
        }
    }

    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        synchronized (this.lock)
        {
            return this.collection.selectWith(predicate, parameter, targetCollection);
        }
    }

    public <S> MutableCollection<S> selectInstancesOf(Class<S> clazz)
    {
        synchronized (this.lock)
        {
            return this.collection.selectInstancesOf(clazz);
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

    public <V> MutableMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.collection.groupBy(function);
        }
    }

    public <V> MutableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.lock)
        {
            return this.collection.groupByEach(function);
        }
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.collection.groupBy(function, target);
        }
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        synchronized (this.lock)
        {
            return this.collection.groupByEach(function, target);
        }
    }

    public <S> MutableCollection<Pair<T, S>> zip(Iterable<S> that)
    {
        synchronized (this.lock)
        {
            return this.collection.zip(that);
        }
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.lock)
        {
            return this.collection.zip(that, target);
        }
    }

    public MutableCollection<Pair<T, Integer>> zipWithIndex()
    {
        synchronized (this.lock)
        {
            return this.collection.zipWithIndex();
        }
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.lock)
        {
            return this.collection.zipWithIndex(target);
        }
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        synchronized (this.lock)
        {
            return this.collection.chunk(size);
        }
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
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

    public MutableCollection<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public MutableCollection<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public MutableCollection<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public MutableCollection<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    public <K, V> MutableMap<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K, V> MutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }
}
