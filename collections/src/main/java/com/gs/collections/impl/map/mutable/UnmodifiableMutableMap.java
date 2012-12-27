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

package com.gs.collections.impl.map.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

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
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.UnmodifiableIteratorAdapter;
import com.gs.collections.impl.UnmodifiableMap;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.tuple.AbstractImmutableEntry;
import com.gs.collections.impl.utility.LazyIterate;

/**
 * An unmodifiable view of a map.
 *
 * @see MutableMap#asUnmodifiable()
 */
public class UnmodifiableMutableMap<K, V>
        extends UnmodifiableMap<K, V>
        implements MutableMap<K, V>
{
    private static final long serialVersionUID = 1L;

    protected UnmodifiableMutableMap(MutableMap<K, V> map)
    {
        super(map);
    }

    /**
     * This method will take a MutableMap and wrap it directly in a UnmodifiableMutableMap.  It will
     * take any other non-GS-map and first adapt it will a MapAdapter, and then return a
     * UnmodifiableMutableMap that wraps the adapter.
     */
    public static <K, V, M extends Map<K, V>> UnmodifiableMutableMap<K, V> of(M map)
    {
        if (map == null)
        {
            throw new IllegalArgumentException("cannot create a UnmodifiableMutableMap for null");
        }
        return new UnmodifiableMutableMap<K, V>(MapAdapter.adapt(map));
    }

    public MutableMap<K, V> newEmpty()
    {
        return this.getMutableMap().newEmpty();
    }

    public boolean notEmpty()
    {
        return this.getMutableMap().notEmpty();
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        this.getMutableMap().forEachValue(procedure);
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        this.getMutableMap().forEachKey(procedure);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        this.getMutableMap().forEachKeyValue(procedure);
    }

    public <E> MutableMap<K, V> collectKeysAndValues(
            Collection<E> collection,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction)
    {
        throw new UnsupportedOperationException();
    }

    public V removeKey(K key)
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

    public V getIfAbsentPutWithKey(K key, Function<? super K, ? extends V> function)
    {
        return this.getIfAbsentPutWith(key, function, key);
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

    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            return function.value();
        }
        return result;
    }

    public V getIfAbsentValue(K key, V value)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            return value;
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

    private boolean isAbsent(V result, K key)
    {
        return result == null && !this.containsKey(key);
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        return this.getMutableMap().ifPresentApply(key, function);
    }

    public MutableMap<K, V> withKeyValue(K key, V value)
    {
        throw new UnsupportedOperationException("Cannot call withKeyValue() on " + this.getClass().getSimpleName());
    }

    public MutableMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        throw new UnsupportedOperationException("Cannot call withAllKeyValues() on " + this.getClass().getSimpleName());
    }

    public MutableMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        throw new UnsupportedOperationException("Cannot call withAllKeyValueArguments() on " + this.getClass().getSimpleName());
    }

    public MutableMap<K, V> withoutKey(K key)
    {
        throw new UnsupportedOperationException("Cannot call withoutKey() on " + this.getClass().getSimpleName());
    }

    public MutableMap<K, V> withoutAllKeys(Iterable<? extends K> keys)
    {
        throw new UnsupportedOperationException("Cannot call withoutAllKeys() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableMap<K, V> clone()
    {
        return this;
    }

    public MutableMap<K, V> asUnmodifiable()
    {
        return this;
    }

    public MutableMap<K, V> asSynchronized()
    {
        return SynchronizedMutableMap.of(this);
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.getMutableMap().forEach(procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.getMutableMap().forEachWithIndex(objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        this.getMutableMap().forEachWith(procedure, parameter);
    }

    public Iterator<V> iterator()
    {
        return new UnmodifiableIteratorAdapter<V>(this.getMutableMap().iterator());
    }

    @Override
    public int hashCode()
    {
        return this.getMutableMap().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.getMutableMap().equals(obj);
    }

    protected MutableMap<K, V> getMutableMap()
    {
        return (MutableMap<K, V>) this.delegate;
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

    public ImmutableMap<K, V> toImmutable()
    {
        return Maps.immutable.ofAll(this);
    }

    public <R> MutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return this.getMutableMap().collectValues(function);
    }

    public <K2, V2> MutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.getMutableMap().collect(function);
    }

    public MutableMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        return this.getMutableMap().select(predicate);
    }

    public MutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        return this.getMutableMap().reject(predicate);
    }

    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        return this.getMutableMap().detect(predicate);
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.getMutableMap().allSatisfy(predicate);
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.getMutableMap().anySatisfy(predicate);
    }

    public void appendString(Appendable appendable)
    {
        this.getMutableMap().appendString(appendable);
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.getMutableMap().appendString(appendable, separator);
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.getMutableMap().appendString(appendable, start, separator, end);
    }

    public MutableBag<V> toBag()
    {
        return this.getMutableMap().toBag();
    }

    public LazyIterable<V> asLazy()
    {
        return this.getMutableMap().asLazy();
    }

    public MutableList<V> toList()
    {
        return this.getMutableMap().toList();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.getMutableMap().toMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.getMutableMap().toSortedMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Comparator<? super NK> comparator,
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.getMutableMap().toSortedMap(comparator, keyFunction, valueFunction);
    }

    public MutableSet<V> toSet()
    {
        return this.getMutableMap().toSet();
    }

    public MutableList<V> toSortedList()
    {
        return this.getMutableMap().toSortedList();
    }

    public MutableList<V> toSortedList(Comparator<? super V> comparator)
    {
        return this.getMutableMap().toSortedList(comparator);
    }

    public <R extends Comparable<? super R>> MutableList<V> toSortedListBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableMap().toSortedListBy(function);
    }

    public MutableSortedSet<V> toSortedSet()
    {
        return this.getMutableMap().toSortedSet();
    }

    public MutableSortedSet<V> toSortedSet(Comparator<? super V> comparator)
    {
        return this.getMutableMap().toSortedSet(comparator);
    }

    public <R extends Comparable<? super R>> MutableSortedSet<V> toSortedSetBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableMap().toSortedSetBy(function);
    }

    public RichIterable<RichIterable<V>> chunk(int size)
    {
        return this.getMutableMap().chunk(size);
    }

    public <R, C extends Collection<R>> C collect(Function<? super V, ? extends R> function, C target)
    {
        return this.getMutableMap().collect(function, target);
    }

    public <R, C extends Collection<R>> C collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function, C target)
    {
        return this.getMutableMap().collectIf(predicate, function, target);
    }

    public <P, R, C extends Collection<R>> C collectWith(Function2<? super V, ? super P, ? extends R> function, P parameter, C targetCollection)
    {
        return this.getMutableMap().collectWith(function, parameter, targetCollection);
    }

    public boolean contains(Object object)
    {
        return this.containsValue(object);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return this.getMutableMap().containsAllArguments(elements);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return this.getMutableMap().containsAllIterable(source);
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.containsAllIterable(source);
    }

    public int count(Predicate<? super V> predicate)
    {
        return this.getMutableMap().count(predicate);
    }

    public V detect(Predicate<? super V> predicate)
    {
        return this.getMutableMap().detect(predicate);
    }

    public V detectIfNone(Predicate<? super V> predicate, Function0<? extends V> function)
    {
        return this.getMutableMap().detectIfNone(predicate, function);
    }

    public <R, C extends Collection<R>> C flatCollect(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.getMutableMap().flatCollect(function, target);
    }

    public V getFirst()
    {
        return this.getMutableMap().getFirst();
    }

    public V getLast()
    {
        return this.getMutableMap().getLast();
    }

    public <R> MutableMultimap<R, V> groupBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableMap().groupBy(function);
    }

    public <R, C extends MutableMultimap<R, V>> C groupBy(Function<? super V, ? extends R> function, C target)
    {
        return this.getMutableMap().groupBy(function, target);
    }

    public <R> MutableMultimap<R, V> groupByEach(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.getMutableMap().groupByEach(function);
    }

    public <R, C extends MutableMultimap<R, V>> C groupByEach(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.getMutableMap().groupByEach(function, target);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super V, ? extends IV> function)
    {
        return this.getMutableMap().injectInto(injectedValue, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super V> function)
    {
        return this.getMutableMap().injectInto(injectedValue, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super V> function)
    {
        return this.getMutableMap().injectInto(injectedValue, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super V> function)
    {
        return this.getMutableMap().injectInto(injectedValue, function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super V> function)
    {
        return this.getMutableMap().injectInto(injectedValue, function);
    }

    public long sumOfInt(IntFunction<? super V> function)
    {
        return this.getMutableMap().sumOfInt(function);
    }

    public double sumOfFloat(FloatFunction<? super V> function)
    {
        return this.getMutableMap().sumOfFloat(function);
    }

    public long sumOfLong(LongFunction<? super V> function)
    {
        return this.getMutableMap().sumOfLong(function);
    }

    public double sumOfDouble(DoubleFunction<? super V> function)
    {
        return this.getMutableMap().sumOfDouble(function);
    }

    public String makeString()
    {
        return this.getMutableMap().makeString();
    }

    public String makeString(String separator)
    {
        return this.getMutableMap().makeString(separator);
    }

    public String makeString(String start, String separator, String end)
    {
        return this.getMutableMap().makeString(start, separator, end);
    }

    public V max()
    {
        return this.getMutableMap().max();
    }

    public V max(Comparator<? super V> comparator)
    {
        return this.getMutableMap().max(comparator);
    }

    public <R extends Comparable<? super R>> V maxBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableMap().maxBy(function);
    }

    public V min()
    {
        return this.getMutableMap().min();
    }

    public V min(Comparator<? super V> comparator)
    {
        return this.getMutableMap().min(comparator);
    }

    public <R extends Comparable<? super R>> V minBy(Function<? super V, ? extends R> function)
    {
        return this.getMutableMap().minBy(function);
    }

    public <R extends Collection<V>> R reject(Predicate<? super V> predicate, R target)
    {
        return this.getMutableMap().reject(predicate, target);
    }

    public <P, R extends Collection<V>> R rejectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.getMutableMap().rejectWith(predicate, parameter, targetCollection);
    }

    public <R extends Collection<V>> R select(Predicate<? super V> predicate, R target)
    {
        return this.getMutableMap().select(predicate, target);
    }

    public <P, R extends Collection<V>> R selectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.getMutableMap().selectWith(predicate, parameter, targetCollection);
    }

    public Object[] toArray()
    {
        return this.getMutableMap().toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return this.getMutableMap().toArray(a);
    }

    public <S, R extends Collection<Pair<V, S>>> R zip(Iterable<S> that, R target)
    {
        return this.getMutableMap().zip(that, target);
    }

    public <R extends Collection<Pair<V, Integer>>> R zipWithIndex(R target)
    {
        return this.getMutableMap().zipWithIndex(target);
    }

    public <R> MutableCollection<R> collect(Function<? super V, ? extends R> function)
    {
        return this.getMutableMap().collect(function);
    }

    public <R> MutableCollection<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.getMutableMap().collectIf(predicate, function);
    }

    public <R> MutableCollection<R> flatCollect(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.getMutableMap().flatCollect(function);
    }

    public MutableCollection<V> reject(Predicate<? super V> predicate)
    {
        return this.getMutableMap().reject(predicate);
    }

    public MutableCollection<V> select(Predicate<? super V> predicate)
    {
        return this.getMutableMap().select(predicate);
    }

    public PartitionMutableCollection<V> partition(Predicate<? super V> predicate)
    {
        return this.getMutableMap().partition(predicate);
    }

    public <S> MutableCollection<S> selectInstancesOf(Class<S> clazz)
    {
        return this.getMutableMap().selectInstancesOf(clazz);
    }

    public <S> MutableCollection<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.getMutableMap().zip(that);
    }

    public MutableCollection<Pair<V, Integer>> zipWithIndex()
    {
        return this.getMutableMap().zipWithIndex();
    }

    public <K2, V2> MutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator)
    {
        MutableMap<K2, V2> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<V, K2, V2>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K2, V2> MutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator)
    {
        MutableMap<K2, V2> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<V, K2, V2>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }
}
