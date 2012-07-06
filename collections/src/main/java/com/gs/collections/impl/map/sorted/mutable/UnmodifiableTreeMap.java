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

package com.gs.collections.impl.map.sorted.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
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
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.UnmodifiableIteratorAdapter;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollection;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.set.mutable.UnmodifiableMutableSet;
import com.gs.collections.impl.tuple.AbstractImmutableEntry;
import com.gs.collections.impl.utility.LazyIterate;

/**
 * An unmodifiable view of a map.
 *
 * @see MutableSortedMap#asUnmodifiable()
 */
public class UnmodifiableTreeMap<K, V>
        extends UnmodifiableSortedMap<K, V>
        implements MutableSortedMap<K, V>
{
    private static final long serialVersionUID = 1L;

    protected UnmodifiableTreeMap(MutableSortedMap<K, V> map)
    {
        super(map);
    }

    /**
     * This method will take a MutableSortedMap and wrap it directly in a UnmodifiableMutableMap.  It will
     * take any other non-GS-SortedMap and first adapt it will a SortedMapAdapter, and then return a
     * UnmodifiableSortedMap that wraps the adapter.
     */
    public static <K, V, M extends SortedMap<K, V>> UnmodifiableTreeMap<K, V> of(M map)
    {
        if (map == null)
        {
            throw new IllegalArgumentException("cannot create a UnmodifiableSortedMap for null");
        }
        return new UnmodifiableTreeMap<K, V>(SortedMapAdapter.adapt(map));
    }

    public V removeKey(K key)
    {
        throw new UnsupportedOperationException();
    }

    public MutableSortedMap<K, V> with(Pair<K, V>... pairs)
    {
        throw new UnsupportedOperationException();
    }

    public <E> MutableSortedMap<K, V> collectKeysAndValues(
            Collection<E> collection,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction)
    {
        throw new UnsupportedOperationException();
    }

    public V getIfAbsentPut(K key, Function0<? extends V> function)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            throw new UnsupportedOperationException();
        }
        return result;
    }

    public <P> V getIfAbsentPutWith(
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            throw new UnsupportedOperationException();
        }
        return result;
    }

    private boolean isAbsent(V result, K key)
    {
        return result == null && !this.containsKey(key);
    }

    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            return function.value();
        }
        return result;
    }

    public <P> V getIfAbsentWith(
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            return function.valueOf(parameter);
        }
        return result;
    }

    @Override
    public MutableSortedMap<K, V> clone()
    {
        return this;
    }

    public MutableSortedMap<K, V> asUnmodifiable()
    {
        return this;
    }

    public MutableSortedMap<K, V> asSynchronized()
    {
        return SynchronizedSortedMap.of(this);
    }

    public ImmutableSortedMap<K, V> toImmutable()
    {
        return SortedMaps.immutable.ofSortedMap(this);
    }

    public Iterator<V> iterator()
    {
        return new UnmodifiableIteratorAdapter<V>(this.getMutableSortedMap().iterator());
    }

    @Override
    public int hashCode()
    {
        return this.getMutableSortedMap().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.getMutableSortedMap().equals(obj);
    }

    public RichIterable<K> keysView()
    {
        return LazyIterate.adapt(this.keySet());
    }

    public RichIterable<V> valuesView()
    {
        return LazyIterate.adapt(this.values());
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return LazyIterate.adapt(this.entrySet()).collect(AbstractImmutableEntry.<K, V>getPairFunction());
    }

    protected MutableSortedMap<K, V> getMutableSortedMap()
    {
        return (MutableSortedMap<K, V>) this.delegate;
    }

    public MutableSortedMap<K, V> newEmpty()
    {
        return this.getMutableSortedMap().newEmpty();
    }

    public boolean notEmpty()
    {
        return this.getMutableSortedMap().notEmpty();
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        this.getMutableSortedMap().forEachValue(procedure);
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        this.getMutableSortedMap().forEachKey(procedure);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        this.getMutableSortedMap().forEachKeyValue(procedure);
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        return this.getMutableSortedMap().ifPresentApply(key, function);
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.getMutableSortedMap().forEach(procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.getMutableSortedMap().forEachWithIndex(objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        this.getMutableSortedMap().forEachWith(procedure, parameter);
    }

    public <R> MutableSortedMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().collectValues(function);
    }

    public <K2, V2> MutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.getMutableSortedMap().collect(function);
    }

    public MutableSortedMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        return this.getMutableSortedMap().select(predicate);
    }

    public MutableSortedMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        return this.getMutableSortedMap().reject(predicate);
    }

    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        return this.getMutableSortedMap().detect(predicate);
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.getMutableSortedMap().allSatisfy(predicate);
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.getMutableSortedMap().anySatisfy(predicate);
    }

    public void appendString(Appendable appendable)
    {
        this.getMutableSortedMap().appendString(appendable);
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.getMutableSortedMap().appendString(appendable, separator);
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.getMutableSortedMap().appendString(appendable, start, separator, end);
    }

    public MutableBag<V> toBag()
    {
        return this.getMutableSortedMap().toBag();
    }

    public LazyIterable<V> asLazy()
    {
        return this.getMutableSortedMap().asLazy();
    }

    public MutableList<V> toList()
    {
        return this.getMutableSortedMap().toList();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.getMutableSortedMap().toMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.getMutableSortedMap().toSortedMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Comparator<? super NK> comparator,
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.getMutableSortedMap().toSortedMap(comparator, keyFunction, valueFunction);
    }

    public MutableSet<V> toSet()
    {
        return this.getMutableSortedMap().toSet();
    }

    public MutableList<V> toSortedList()
    {
        return this.getMutableSortedMap().toSortedList();
    }

    public MutableList<V> toSortedList(Comparator<? super V> comparator)
    {
        return this.getMutableSortedMap().toSortedList(comparator);
    }

    public <R extends Comparable<? super R>> MutableList<V> toSortedListBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().toSortedListBy(function);
    }

    public MutableSortedSet<V> toSortedSet()
    {
        return this.getMutableSortedMap().toSortedSet();
    }

    public MutableSortedSet<V> toSortedSet(Comparator<? super V> comparator)
    {
        return this.getMutableSortedMap().toSortedSet(comparator);
    }

    public <R extends Comparable<? super R>> MutableSortedSet<V> toSortedSetBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().toSortedSetBy(function);
    }

    public RichIterable<RichIterable<V>> chunk(int size)
    {
        return this.getMutableSortedMap().chunk(size);
    }

    public <R, C extends Collection<R>> C collect(Function<? super V, ? extends R> function, C target)
    {
        return this.getMutableSortedMap().collect(function, target);
    }

    public <R, C extends Collection<R>> C collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function, C target)
    {
        return this.getMutableSortedMap().collectIf(predicate, function, target);
    }

    public <P, R, C extends Collection<R>> C collectWith(Function2<? super V, ? super P, ? extends R> function, P parameter, C targetCollection)
    {
        return this.getMutableSortedMap().collectWith(function, parameter, targetCollection);
    }

    public boolean contains(Object object)
    {
        return this.getMutableSortedMap().contains(object);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return this.getMutableSortedMap().containsAllArguments(elements);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return this.getMutableSortedMap().containsAllIterable(source);
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.getMutableSortedMap().containsAll(source);
    }

    public int count(Predicate<? super V> predicate)
    {
        return this.getMutableSortedMap().count(predicate);
    }

    public V detect(Predicate<? super V> predicate)
    {
        return this.getMutableSortedMap().detect(predicate);
    }

    public V detectIfNone(Predicate<? super V> predicate, Function0<? extends V> function)
    {
        return this.getMutableSortedMap().detectIfNone(predicate, function);
    }

    public <R, C extends Collection<R>> C flatCollect(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.getMutableSortedMap().flatCollect(function, target);
    }

    public V getFirst()
    {
        return this.getMutableSortedMap().getFirst();
    }

    public V getLast()
    {
        return this.getMutableSortedMap().getLast();
    }

    public <R> MutableListMultimap<R, V> groupBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().groupBy(function);
    }

    public <R, C extends MutableMultimap<R, V>> C groupBy(Function<? super V, ? extends R> function, C target)
    {
        return this.getMutableSortedMap().groupBy(function, target);
    }

    public <R> MutableListMultimap<R, V> groupByEach(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.getMutableSortedMap().groupByEach(function);
    }

    public <R, C extends MutableMultimap<R, V>> C groupByEach(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.getMutableSortedMap().groupByEach(function, target);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super V, ? extends IV> function)
    {
        return this.getMutableSortedMap().injectInto(injectedValue, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super V> function)
    {
        return this.getMutableSortedMap().injectInto(injectedValue, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super V> function)
    {
        return this.getMutableSortedMap().injectInto(injectedValue, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super V> function)
    {
        return this.getMutableSortedMap().injectInto(injectedValue, function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super V> function)
    {
        return this.getMutableSortedMap().injectInto(injectedValue, function);
    }

    public int sumOf(IntFunction<? super V> function)
    {
        return this.getMutableSortedMap().sumOf(function);
    }

    public float sumOf(FloatFunction<? super V> function)
    {
        return this.getMutableSortedMap().sumOf(function);
    }

    public long sumOf(LongFunction<? super V> function)
    {
        return this.getMutableSortedMap().sumOf(function);
    }

    public double sumOf(DoubleFunction<? super V> function)
    {
        return this.getMutableSortedMap().sumOf(function);
    }

    public String makeString()
    {
        return this.getMutableSortedMap().makeString();
    }

    public String makeString(String separator)
    {
        return this.getMutableSortedMap().makeString(separator);
    }

    public String makeString(String start, String separator, String end)
    {
        return this.getMutableSortedMap().makeString(start, separator, end);
    }

    public V max()
    {
        return this.getMutableSortedMap().max();
    }

    public V max(Comparator<? super V> comparator)
    {
        return this.getMutableSortedMap().max(comparator);
    }

    public <R extends Comparable<? super R>> V maxBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().maxBy(function);
    }

    public V min()
    {
        return this.getMutableSortedMap().min();
    }

    public V min(Comparator<? super V> comparator)
    {
        return this.getMutableSortedMap().min(comparator);
    }

    public <R extends Comparable<? super R>> V minBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().minBy(function);
    }

    public <R extends Collection<V>> R reject(Predicate<? super V> predicate, R target)
    {
        return this.getMutableSortedMap().reject(predicate, target);
    }

    public <P, R extends Collection<V>> R rejectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.getMutableSortedMap().rejectWith(predicate, parameter, targetCollection);
    }

    public <R extends Collection<V>> R select(Predicate<? super V> predicate, R target)
    {
        return this.getMutableSortedMap().select(predicate, target);
    }

    public <P, R extends Collection<V>> R selectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.getMutableSortedMap().selectWith(predicate, parameter, targetCollection);
    }

    public Object[] toArray()
    {
        return this.getMutableSortedMap().toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return this.getMutableSortedMap().toArray(a);
    }

    public <S, R extends Collection<Pair<V, S>>> R zip(Iterable<S> that, R target)
    {
        return this.getMutableSortedMap().zip(that, target);
    }

    public <R extends Collection<Pair<V, Integer>>> R zipWithIndex(R target)
    {
        return this.getMutableSortedMap().zipWithIndex(target);
    }

    public <R> MutableList<R> collect(Function<? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().collect(function);
    }

    public <R> MutableList<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.getMutableSortedMap().collectIf(predicate, function);
    }

    public <R> MutableList<R> flatCollect(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.getMutableSortedMap().flatCollect(function);
    }

    public MutableList<V> reject(Predicate<? super V> predicate)
    {
        return this.getMutableSortedMap().reject(predicate);
    }

    public MutableList<V> select(Predicate<? super V> predicate)
    {
        return this.getMutableSortedMap().select(predicate);
    }

    public PartitionMutableList<V> partition(Predicate<? super V> predicate)
    {
        return this.getMutableSortedMap().partition(predicate);
    }

    public <S> MutableList<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.getMutableSortedMap().zip(that);
    }

    public MutableList<Pair<V, Integer>> zipWithIndex()
    {
        return this.getMutableSortedMap().zipWithIndex();
    }

    @Override
    public MutableSet<K> keySet()
    {
        return UnmodifiableMutableSet.of(this.getMutableSortedMap().keySet());
    }

    @Override
    public MutableCollection<V> values()
    {
        return UnmodifiableMutableCollection.of(this.getMutableSortedMap().values());
    }

    @Override
    public MutableSortedMap<K, V> headMap(K toKey)
    {
        return of(this.getMutableSortedMap().headMap(toKey));
    }

    @Override
    public MutableSortedMap<K, V> tailMap(K fromKey)
    {
        return of(this.getMutableSortedMap().tailMap(fromKey));
    }

    @Override
    public MutableSortedMap<K, V> subMap(K fromKey, K toKey)
    {
        return of(this.getMutableSortedMap().subMap(fromKey, toKey));
    }

    @Override
    public MutableSet<Entry<K, V>> entrySet()
    {
        return UnmodifiableMutableSet.of(super.entrySet());
    }
}
