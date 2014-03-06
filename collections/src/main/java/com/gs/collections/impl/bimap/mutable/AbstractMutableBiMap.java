/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.bimap.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableByteCollection;
import com.gs.collections.api.collection.primitive.MutableCharCollection;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;
import com.gs.collections.api.collection.primitive.MutableIntCollection;
import com.gs.collections.api.collection.primitive.MutableLongCollection;
import com.gs.collections.api.collection.primitive.MutableShortCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.MapIterate;

abstract class AbstractMutableBiMap<K, V> implements MutableBiMap<K, V>
{
    private UnifiedMap<K, V> delegate;
    private AbstractMutableBiMap<V, K> inverse;

    private AbstractMutableBiMap(Map<K, V> delegate, AbstractMutableBiMap<V, K> valuesToKeys)
    {
        this.delegate = UnifiedMap.newMap(delegate);
        this.inverse = valuesToKeys;
    }

    AbstractMutableBiMap(Map<K, V> map)
    {
        this.delegate = UnifiedMap.newMap();
        this.inverse = new Inverse<V, K>(UnifiedMap.<V, K>newMap(), this);
        this.putAll(map);
    }

    AbstractMutableBiMap(Map<K, V> delegate, Map<V, K> inverse)
    {
        this.checkNull(delegate, inverse);
        this.checkSame(delegate, inverse);
        this.delegate = UnifiedMap.newMap(delegate);
        this.inverse = new Inverse<V, K>(inverse, this);
    }

    private void checkNull(Map<K, V> delegate, Map<V, K> inverse)
    {
        if (delegate == null || inverse == null)
        {
            throw new IllegalArgumentException("The delegate maps cannot be null.");
        }
    }

    private void checkSame(Map<K, V> keysToValues, Map<V, K> valuesToKeys)
    {
        if (keysToValues == valuesToKeys)
        {
            throw new IllegalArgumentException("The delegate maps cannot be same.");
        }
    }

    private static boolean nullSafeEquals(Object value, Object other)
    {
        if (value == null)
        {
            if (other == null)
            {
                return true;
            }
        }
        else if (other == value || value.equals(other))
        {
            return true;
        }
        return false;
    }

    public MutableSetMultimap<V, K> flip()
    {
        // TODO: We could optimize this since we know the values are unique
        return MapIterate.flip(this);
    }

    @Override
    public MutableBiMap<K, V> clone()
    {
        return new HashBiMap<K, V>(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Map))
        {
            return false;
        }

        Map<?, ?> map = (Map<?, ?>) obj;

        return this.delegate.equals(map);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public String toString()
    {
        return this.delegate.toString();
    }

    public V put(K key, V value)
    {
        if (this.inverse.delegate.containsKey(value))
        {
            if (AbstractMutableBiMap.nullSafeEquals(key, this.inverse.delegate.get(value)))
            {
                return value;
            }
            throw new IllegalArgumentException("Value " + value + " already exists in map!");
        }

        boolean containsKey = this.delegate.containsKey(key);
        V put = this.delegate.put(key, value);
        if (containsKey)
        {
            this.inverse.delegate.removeKey(put);
        }
        this.inverse.delegate.put(value, key);
        return put;
    }

    public V forcePut(K key, V value)
    {
        boolean containsValue = this.inverse.delegate.containsKey(value);
        if (containsValue)
        {
            if (AbstractMutableBiMap.nullSafeEquals(key, this.inverse.delegate.get(value)))
            {
                return value;
            }
        }

        boolean containsKey = this.delegate.containsKey(key);
        V put = this.delegate.put(key, value);
        if (containsKey)
        {
            this.inverse.delegate.removeKey(put);
        }
        K oldKey = this.inverse.delegate.put(value, key);
        if (containsValue)
        {
            this.delegate.removeKey(oldKey);
        }
        return put;
    }

    public V remove(Object key)
    {
        if (!this.delegate.containsKey(key))
        {
            return null;
        }
        V oldValue = this.delegate.remove(key);
        this.inverse.delegate.remove(oldValue);
        return oldValue;
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        Set<? extends Map.Entry<? extends K, ? extends V>> entries = map.entrySet();
        for (Map.Entry<? extends K, ? extends V> entry : entries)
        {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void clear()
    {
        this.delegate.clear();
        this.inverse.delegate.clear();
    }

    public Set<K> keySet()
    {
        return new KeySet();
    }

    public Collection<V> values()
    {
        return new ValuesCollection();
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
        return new EntrySet();
    }

    public boolean containsKey(Object key)
    {
        return this.delegate.containsKey(key);
    }

    public int size()
    {
        return this.delegate.size();
    }

    public boolean isEmpty()
    {
        return this.delegate.isEmpty();
    }

    public boolean notEmpty()
    {
        return this.delegate.notEmpty();
    }

    public V getFirst()
    {
        return this.delegate.getFirst();
    }

    public V getLast()
    {
        return this.delegate.getLast();
    }

    public boolean contains(Object object)
    {
        return this.inverse.delegate.containsKey(object);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return this.inverse.delegate.keysView().containsAllIterable(source);
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.inverse.delegate.keysView().containsAll(source);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return this.inverse.delegate.keysView().containsAllArguments(elements);
    }

    public <R extends Collection<V>> R select(Predicate<? super V> predicate, R target)
    {
        return this.delegate.select(predicate, target);
    }

    public <P> RichIterable<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.selectWith(predicate, parameter);
    }

    public <P> RichIterable<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.rejectWith(predicate, parameter);
    }

    public <VV> MutableCollection<VV> collect(Function<? super V, ? extends VV> function)
    {
        return this.delegate.collect(function);
    }

    public MutableBooleanCollection collectBoolean(BooleanFunction<? super V> booleanFunction)
    {
        return this.delegate.collectBoolean(booleanFunction);
    }

    public <R extends MutableBooleanCollection> R collectBoolean(BooleanFunction<? super V> booleanFunction, R target)
    {
        return this.delegate.collectBoolean(booleanFunction, target);
    }

    public MutableByteCollection collectByte(ByteFunction<? super V> byteFunction)
    {
        return this.delegate.collectByte(byteFunction);
    }

    public <R extends MutableByteCollection> R collectByte(ByteFunction<? super V> byteFunction, R target)
    {
        return this.delegate.collectByte(byteFunction, target);
    }

    public MutableCharCollection collectChar(CharFunction<? super V> charFunction)
    {
        return this.delegate.collectChar(charFunction);
    }

    public <R extends MutableCharCollection> R collectChar(CharFunction<? super V> charFunction, R target)
    {
        return this.delegate.collectChar(charFunction, target);
    }

    public <P, R extends Collection<V>> R selectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.delegate.selectWith(predicate, parameter, targetCollection);
    }

    public MutableDoubleCollection collectDouble(DoubleFunction<? super V> doubleFunction)
    {
        return this.delegate.collectDouble(doubleFunction);
    }

    public <R extends MutableDoubleCollection> R collectDouble(DoubleFunction<? super V> doubleFunction, R target)
    {
        return this.delegate.collectDouble(doubleFunction, target);
    }

    public MutableFloatCollection collectFloat(FloatFunction<? super V> floatFunction)
    {
        return this.delegate.collectFloat(floatFunction);
    }

    public <R extends MutableFloatCollection> R collectFloat(FloatFunction<? super V> floatFunction, R target)
    {
        return this.delegate.collectFloat(floatFunction, target);
    }

    public MutableIntCollection collectInt(IntFunction<? super V> intFunction)
    {
        return this.delegate.collectInt(intFunction);
    }

    public <R extends MutableIntCollection> R collectInt(IntFunction<? super V> intFunction, R target)
    {
        return this.delegate.collectInt(intFunction, target);
    }

    public MutableLongCollection collectLong(LongFunction<? super V> longFunction)
    {
        return this.delegate.collectLong(longFunction);
    }

    public <R extends MutableLongCollection> R collectLong(LongFunction<? super V> longFunction, R target)
    {
        return this.delegate.collectLong(longFunction, target);
    }

    public MutableShortCollection collectShort(ShortFunction<? super V> shortFunction)
    {
        return this.delegate.collectShort(shortFunction);
    }

    public <R extends MutableShortCollection> R collectShort(ShortFunction<? super V> shortFunction, R target)
    {
        return this.delegate.collectShort(shortFunction, target);
    }

    public <VV> MutableCollection<VV> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends VV> function)
    {
        return this.delegate.collectIf(predicate, function);
    }

    public MutableCollection<V> reject(Predicate<? super V> predicate)
    {
        return this.delegate.reject(predicate);
    }

    public MutableCollection<V> select(Predicate<? super V> predicate)
    {
        return this.delegate.select(predicate);
    }

    public PartitionMutableCollection<V> partition(Predicate<? super V> predicate)
    {
        return this.delegate.partition(predicate);
    }

    public <P> PartitionMutableCollection<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.partitionWith(predicate, parameter);
    }

    public <S> MutableCollection<S> selectInstancesOf(Class<S> clazz)
    {
        return this.delegate.selectInstancesOf(clazz);
    }

    public MutableCollection<Pair<V, Integer>> zipWithIndex()
    {
        return this.delegate.zipWithIndex();
    }

    public <R extends Collection<V>> R reject(Predicate<? super V> predicate, R target)
    {
        return this.delegate.reject(predicate, target);
    }

    public <P, R extends Collection<V>> R rejectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.delegate.rejectWith(predicate, parameter, targetCollection);
    }

    public <K2, V2> MutableMap<K2, V2> aggregateBy(Function<? super V, ? extends K2> groupBy, Function0<? extends V2> zeroValueFactory, Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator)
    {
        return this.delegate.aggregateBy(groupBy, zeroValueFactory, nonMutatingAggregator);
    }

    public <VV, R extends Collection<VV>> R collect(Function<? super V, ? extends VV> function, R target)
    {
        return this.delegate.collect(function, target);
    }

    public <P, VV> MutableList<VV> collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter)
    {
        return this.delegate.collectWith(function, parameter);
    }

    public <P, VV, R extends Collection<VV>> R collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter, R targetCollection)
    {
        return this.delegate.collectWith(function, parameter, targetCollection);
    }

    public <VV, R extends Collection<VV>> R collectIf(Predicate<? super V> predicate, Function<? super V, ? extends VV> function, R target)
    {
        return this.delegate.collectIf(predicate, function, target);
    }

    public <VV> MutableCollection<VV> flatCollect(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.delegate.flatCollect(function);
    }

    public <VV, R extends Collection<VV>> R flatCollect(Function<? super V, ? extends Iterable<VV>> function, R target)
    {
        return this.delegate.flatCollect(function, target);
    }

    public V detect(Predicate<? super V> predicate)
    {
        return this.delegate.detect(predicate);
    }

    public <P> V detectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.detectWith(predicate, parameter);
    }

    public V detectIfNone(Predicate<? super V> predicate, Function0<? extends V> function)
    {
        return this.delegate.detectIfNone(predicate, function);
    }

    public <P> V detectWithIfNone(Predicate2<? super V, ? super P> predicate, P parameter, Function0<? extends V> function)
    {
        return this.delegate.detectWithIfNone(predicate, parameter, function);
    }

    public int count(Predicate<? super V> predicate)
    {
        return this.delegate.count(predicate);
    }

    public <P> int countWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.countWith(predicate, parameter);
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.delegate.anySatisfy(predicate);
    }

    public <P> boolean anySatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.anySatisfyWith(predicate, parameter);
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.delegate.allSatisfy(predicate);
    }

    public <P> boolean allSatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.allSatisfyWith(predicate, parameter);
    }

    public boolean noneSatisfy(Predicate<? super V> predicate)
    {
        return this.delegate.noneSatisfy(predicate);
    }

    public <P> boolean noneSatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.noneSatisfyWith(predicate, parameter);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super V, ? extends IV> function)
    {
        return this.delegate.injectInto(injectedValue, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super V> function)
    {
        return this.delegate.injectInto(injectedValue, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super V> function)
    {
        return this.delegate.injectInto(injectedValue, function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super V> function)
    {
        return this.delegate.injectInto(injectedValue, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super V> function)
    {
        return this.delegate.injectInto(injectedValue, function);
    }

    public MutableList<V> toList()
    {
        return this.delegate.toList();
    }

    public MutableList<V> toSortedList()
    {
        return this.delegate.toSortedList();
    }

    public MutableList<V> toSortedList(Comparator<? super V> comparator)
    {
        return this.delegate.toSortedList(comparator);
    }

    public <VV extends Comparable<? super VV>> MutableList<V> toSortedListBy(Function<? super V, ? extends VV> function)
    {
        return this.delegate.toSortedListBy(function);
    }

    public MutableSet<V> toSet()
    {
        return this.delegate.toSet();
    }

    public MutableSortedSet<V> toSortedSet()
    {
        return this.delegate.toSortedSet();
    }

    public MutableSortedSet<V> toSortedSet(Comparator<? super V> comparator)
    {
        return this.delegate.toSortedSet(comparator);
    }

    public <VV extends Comparable<? super VV>> MutableSortedSet<V> toSortedSetBy(Function<? super V, ? extends VV> function)
    {
        return this.delegate.toSortedSetBy(function);
    }

    public MutableBag<V> toBag()
    {
        return this.delegate.toBag();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(Function<? super V, ? extends NK> keyFunction, Function<? super V, ? extends NV> valueFunction)
    {
        return this.delegate.toMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Function<? super V, ? extends NK> keyFunction, Function<? super V, ? extends NV> valueFunction)
    {
        return this.delegate.toSortedMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator, Function<? super V, ? extends NK> keyFunction, Function<? super V, ? extends NV> valueFunction)
    {
        return this.delegate.toSortedMap(comparator, keyFunction, valueFunction);
    }

    public LazyIterable<V> asLazy()
    {
        return this.delegate.asLazy();
    }

    public Object[] toArray()
    {
        return this.delegate.toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return this.delegate.toArray(a);
    }

    public V min(Comparator<? super V> comparator)
    {
        return this.delegate.min(comparator);
    }

    public V max(Comparator<? super V> comparator)
    {
        return this.delegate.max(comparator);
    }

    public V min()
    {
        return this.delegate.min();
    }

    public V max()
    {
        return this.delegate.max();
    }

    public <VV extends Comparable<? super VV>> V minBy(Function<? super V, ? extends VV> function)
    {
        return this.delegate.minBy(function);
    }

    public <VV extends Comparable<? super VV>> V maxBy(Function<? super V, ? extends VV> function)
    {
        return this.delegate.maxBy(function);
    }

    public long sumOfInt(IntFunction<? super V> function)
    {
        return this.delegate.sumOfInt(function);
    }

    public double sumOfFloat(FloatFunction<? super V> function)
    {
        return this.delegate.sumOfFloat(function);
    }

    public long sumOfLong(LongFunction<? super V> function)
    {
        return this.delegate.sumOfLong(function);
    }

    public double sumOfDouble(DoubleFunction<? super V> function)
    {
        return this.delegate.sumOfDouble(function);
    }

    public String makeString()
    {
        return this.delegate.makeString();
    }

    public String makeString(String separator)
    {
        return this.delegate.makeString(separator);
    }

    public String makeString(String start, String separator, String end)
    {
        return this.delegate.makeString(start, separator, end);
    }

    public void appendString(Appendable appendable)
    {
        this.delegate.appendString(appendable);
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.delegate.appendString(appendable, separator);
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.delegate.appendString(appendable, start, separator, end);
    }

    public <VV> MutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function)
    {
        return this.delegate.groupBy(function);
    }

    public <VV, R extends MutableMultimap<VV, V>> R groupBy(Function<? super V, ? extends VV> function, R target)
    {
        return this.delegate.groupBy(function, target);
    }

    public <VV> MutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.delegate.groupByEach(function);
    }

    public <VV, R extends MutableMultimap<VV, V>> R groupByEach(Function<? super V, ? extends Iterable<VV>> function, R target)
    {
        return this.delegate.groupByEach(function, target);
    }

    public <VV> MutableMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function)
    {
        return this.delegate.groupByUniqueKey(function);
    }

    public <S> MutableCollection<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.delegate.zip(that);
    }

    public <S, R extends Collection<Pair<V, S>>> R zip(Iterable<S> that, R target)
    {
        return this.delegate.zip(that, target);
    }

    public <R extends Collection<Pair<V, Integer>>> R zipWithIndex(R target)
    {
        return this.delegate.zipWithIndex(target);
    }

    public RichIterable<RichIterable<V>> chunk(int size)
    {
        return this.delegate.chunk(size);
    }

    public <K2, V2> MutableMap<K2, V2> aggregateInPlaceBy(Function<? super V, ? extends K2> groupBy, Function0<? extends V2> zeroValueFactory, Procedure2<? super V2, ? super V> mutatingAggregator)
    {
        return this.delegate.aggregateInPlaceBy(groupBy, zeroValueFactory, mutatingAggregator);
    }

    public boolean containsValue(Object value)
    {
        return this.inverse.delegate.containsKey(value);
    }

    public V get(Object key)
    {
        return this.delegate.get(key);
    }

    public HashBiMap<K, V> select(final Predicate2<? super K, ? super V> predicate)
    {
        final HashBiMap<K, V> result = HashBiMap.newMap();
        this.delegate.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                if (predicate.accept(key, value))
                {
                    result.put(key, value);
                }
            }
        });
        return result;
    }

    public <R> HashBiMap<K, R> collectValues(final Function2<? super K, ? super V, ? extends R> function)
    {
        final HashBiMap<K, R> result = HashBiMap.newMap();
        this.delegate.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                result.put(key, function.value(key, value));
            }
        });
        return result;
    }

    public <K2, V2> HashBiMap<K2, V2> collect(final Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        final HashBiMap<K2, V2> result = HashBiMap.newMap();
        this.delegate.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                Pair<K2, V2> pair = function.value(key, value);
                result.put(pair.getOne(), pair.getTwo());
            }
        });
        return result;
    }

    public HashBiMap<K, V> reject(final Predicate2<? super K, ? super V> predicate)
    {
        final HashBiMap<K, V> result = HashBiMap.newMap();
        this.delegate.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                if (!predicate.accept(key, value))
                {
                    result.put(key, value);
                }
            }
        });
        return result;
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        this.inverse.delegate.forEachKey(procedure);
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        this.delegate.forEachKey(procedure);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        this.delegate.forEachKeyValue(procedure);
    }

    public MutableBiMap<V, K> flipUniqueValues()
    {
        return new HashBiMap<V, K>(this.inverse());
    }

    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        return this.delegate.getIfAbsent(key, function);
    }

    public V getIfAbsentValue(K key, V value)
    {
        return this.delegate.getIfAbsentValue(key, value);
    }

    public <P> V getIfAbsentWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        return this.delegate.getIfAbsentWith(key, function, parameter);
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        return this.delegate.ifPresentApply(key, function);
    }

    public RichIterable<K> keysView()
    {
        return this.delegate.keysView();
    }

    public RichIterable<V> valuesView()
    {
        return this.delegate.valuesView();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return this.delegate.keyValuesView();
    }

    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        return this.delegate.detect(predicate);
    }

    public ImmutableBiMap<K, V> toImmutable()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".toImmutable() not implemented yet");
    }

    public MutableBiMap<K, V> asSynchronized()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asSynchronized() not implemented yet");
    }

    public MutableBiMap<K, V> asUnmodifiable()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asUnmodifiable() not implemented yet");
    }

    public <E> MutableMap<K, V> collectKeysAndValues(Iterable<E> iterable, Function<? super E, ? extends K> keyFunction, Function<? super E, ? extends V> valueFunction)
    {
        Iterate.forEach(iterable, new MapCollectProcedure<E, K, V>(this, keyFunction, valueFunction));
        return this;
    }

    public V removeKey(K key)
    {
        return this.remove(key);
    }

    public V getIfAbsentPut(K key, Function0<? extends V> function)
    {
        V value = this.delegate.get(key);

        if (value != null || this.delegate.containsKey(key))
        {
            return value;
        }

        V newValue = function.value();
        this.put(key, newValue);
        return newValue;
    }

    public V getIfAbsentPut(K key, V value)
    {
        V oldValue = this.delegate.get(key);

        if (oldValue != null || this.delegate.containsKey(key))
        {
            return oldValue;
        }

        this.put(key, value);
        return value;
    }

    public V getIfAbsentPutWithKey(K key, Function<? super K, ? extends V> function)
    {
        V value = this.delegate.get(key);

        if (value != null || this.delegate.containsKey(key))
        {
            return value;
        }

        V newValue = function.valueOf(key);
        this.put(key, newValue);
        return newValue;
    }

    public <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        V value = this.delegate.get(key);

        if (value != null || this.delegate.containsKey(key))
        {
            return value;
        }

        V newValue = function.valueOf(parameter);
        this.put(key, newValue);
        return newValue;
    }

    public V add(Pair<K, V> keyValuePair)
    {
        return this.put(keyValuePair.getOne(), keyValuePair.getTwo());
    }

    public MutableBiMap<K, V> withKeyValue(K key, V value)
    {
        this.put(key, value);
        return this;
    }

    public MutableBiMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        for (Pair<? extends K, ? extends V> keyVal : keyValues)
        {
            this.put(keyVal.getOne(), keyVal.getTwo());
        }
        return this;
    }

    public MutableBiMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        return this.withAllKeyValues(ArrayAdapter.adapt(keyValuePairs));
    }

    public MutableBiMap<K, V> withoutKey(K key)
    {
        this.removeKey(key);
        return this;
    }

    public MutableMap<K, V> withoutAllKeys(Iterable<? extends K> keys)
    {
        for (K key : keys)
        {
            this.removeKey(key);
        }
        return this;
    }

    public HashBiMap<K, V> newEmpty()
    {
        return new HashBiMap<K, V>();
    }

    public V updateValue(K key, Function0<? extends V> factory, Function<? super V, ? extends V> function)
    {
        if (this.delegate.containsKey(key))
        {
            V newValue = function.valueOf(this.delegate.get(key));
            this.put(key, newValue);
            return newValue;
        }

        V newValue = function.valueOf(factory.value());
        this.put(key, newValue);
        return newValue;
    }

    public <P> V updateValueWith(K key, Function0<? extends V> factory, Function2<? super V, ? super P, ? extends V> function, P parameter)
    {
        if (this.delegate.containsKey(key))
        {
            V newValue = function.value(this.delegate.get(key), parameter);
            this.put(key, newValue);
            return newValue;
        }

        V newValue = function.value(factory.value(), parameter);
        this.put(key, newValue);
        return newValue;
    }

    public MutableBiMap<V, K> inverse()
    {
        return this.inverse;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.inverse.delegate.forEachKey(procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.delegate.forEachWithIndex(objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        this.delegate.forEachWith(procedure, parameter);
    }

    public Iterator<V> iterator()
    {
        return new InternalIterator();
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        this.delegate.writeExternal(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.delegate = UnifiedMap.newMap();
        this.delegate.readExternal(in);
        final UnifiedMap<V, K> inverseDelegate = UnifiedMap.newMap();
        this.delegate.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                inverseDelegate.put(value, key);
            }
        });
        this.inverse = new Inverse<V, K>(inverseDelegate, this);
    }

    private class InternalIterator implements Iterator<V>
    {
        private final Iterator<V> iterator = AbstractMutableBiMap.this.delegate.iterator();
        private V currentValue;

        public boolean hasNext()
        {
            return this.iterator.hasNext();
        }

        public V next()
        {
            V next = this.iterator.next();
            this.currentValue = next;
            return next;
        }

        public void remove()
        {
            this.iterator.remove();
            AbstractMutableBiMap.this.inverse.delegate.remove(this.currentValue);
        }
    }

    private class KeySet implements Set<K>
    {
        @Override
        public boolean equals(Object obj)
        {
            return AbstractMutableBiMap.this.delegate.keySet().equals(obj);
        }

        @Override
        public int hashCode()
        {
            return AbstractMutableBiMap.this.delegate.keySet().hashCode();
        }

        public int size()
        {
            return AbstractMutableBiMap.this.size();
        }

        public boolean isEmpty()
        {
            return AbstractMutableBiMap.this.isEmpty();
        }

        public boolean contains(Object key)
        {
            return AbstractMutableBiMap.this.delegate.containsKey(key);
        }

        public Object[] toArray()
        {
            return AbstractMutableBiMap.this.delegate.keySet().toArray();
        }

        public <T> T[] toArray(T[] a)
        {
            return AbstractMutableBiMap.this.delegate.keySet().toArray(a);
        }

        public boolean add(K key)
        {
            throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
        }

        public boolean remove(Object key)
        {
            int oldSize = AbstractMutableBiMap.this.size();
            AbstractMutableBiMap.this.remove(key);
            return AbstractMutableBiMap.this.size() != oldSize;
        }

        public boolean containsAll(Collection<?> source)
        {
            for (Object key : source)
            {
                if (!AbstractMutableBiMap.this.delegate.containsKey(key))
                {
                    return false;
                }
            }
            return true;
        }

        public boolean addAll(Collection<? extends K> source)
        {
            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
        }

        public boolean retainAll(Collection<?> collection)
        {
            int oldSize = AbstractMutableBiMap.this.size();
            Iterator<K> iterator = this.iterator();
            while (iterator.hasNext())
            {
                K next = iterator.next();
                if (!collection.contains(next))
                {
                    this.remove(next);
                }
            }
            return oldSize != AbstractMutableBiMap.this.size();
        }

        public boolean removeAll(Collection<?> collection)
        {
            int oldSize = AbstractMutableBiMap.this.size();
            for (Object object : collection)
            {
                AbstractMutableBiMap.this.remove(object);
            }
            return oldSize != AbstractMutableBiMap.this.size();
        }

        public void clear()
        {
            AbstractMutableBiMap.this.clear();
        }

        public Iterator<K> iterator()
        {
            return AbstractMutableBiMap.this.inverse().iterator();
        }

        @Override
        public String toString()
        {
            return Iterate.makeString(this, "[", ", ", "]");
        }
    }

    private class ValuesCollection implements Collection<V>
    {
        public int size()
        {
            return AbstractMutableBiMap.this.size();
        }

        public boolean isEmpty()
        {
            return AbstractMutableBiMap.this.isEmpty();
        }

        public boolean contains(Object key)
        {
            return AbstractMutableBiMap.this.inverse.delegate.containsKey(key);
        }

        public Object[] toArray()
        {
            return AbstractMutableBiMap.this.delegate.values().toArray();
        }

        public <T> T[] toArray(T[] a)
        {
            return AbstractMutableBiMap.this.delegate.values().toArray(a);
        }

        public boolean add(V v)
        {
            throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
        }

        public boolean remove(Object value)
        {
            int oldSize = AbstractMutableBiMap.this.size();
            AbstractMutableBiMap.this.inverse().remove(value);
            return oldSize != AbstractMutableBiMap.this.size();
        }

        public boolean containsAll(Collection<?> collection)
        {
            for (Object key : collection)
            {
                if (!AbstractMutableBiMap.this.inverse.delegate.containsKey(key))
                {
                    return false;
                }
            }
            return true;
        }

        public boolean addAll(Collection<? extends V> collection)
        {
            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
        }

        public boolean removeAll(Collection<?> collection)
        {
            int oldSize = AbstractMutableBiMap.this.size();
            for (Object object : collection)
            {
                this.remove(object);
            }
            return oldSize != AbstractMutableBiMap.this.size();
        }

        public boolean retainAll(Collection<?> collection)
        {
            int oldSize = AbstractMutableBiMap.this.size();
            Iterator<V> iterator = this.iterator();
            while (iterator.hasNext())
            {
                V next = iterator.next();
                if (!collection.contains(next))
                {
                    this.remove(next);
                }
            }
            return oldSize != AbstractMutableBiMap.this.size();
        }

        public void clear()
        {
            AbstractMutableBiMap.this.clear();
        }

        public Iterator<V> iterator()
        {
            return AbstractMutableBiMap.this.iterator();
        }

        @Override
        public String toString()
        {
            return Iterate.makeString(this, "[", ", ", "]");
        }
    }

    private class EntrySet implements Set<Map.Entry<K, V>>
    {
        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Set)
            {
                Set<?> other = (Set<?>) obj;
                if (other.size() == this.size())
                {
                    return this.containsAll(other);
                }
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return AbstractMutableBiMap.this.hashCode();
        }

        public int size()
        {
            return AbstractMutableBiMap.this.size();
        }

        public boolean isEmpty()
        {
            return AbstractMutableBiMap.this.isEmpty();
        }

        public boolean contains(Object o)
        {
            if (!(o instanceof Map.Entry))
            {
                return false;
            }
            Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            K key = entry.getKey();
            V actualValue = AbstractMutableBiMap.this.get(key);
            if (actualValue != null)
            {
                return actualValue.equals(entry.getValue());
            }
            return entry.getValue() == null && AbstractMutableBiMap.this.containsKey(key);
        }

        public Object[] toArray()
        {
            Object[] result = new Object[AbstractMutableBiMap.this.size()];
            return this.copyEntries(result);
        }

        public <T> T[] toArray(T[] result)
        {
            int size = AbstractMutableBiMap.this.size();
            if (result.length < size)
            {
                result = (T[]) Array.newInstance(result.getClass().getComponentType(), size);
            }
            this.copyEntries(result);
            if (size < result.length)
            {
                result[size] = null;
            }
            return result;
        }

        public boolean add(Map.Entry<K, V> entry)
        {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object e)
        {
            if (!(e instanceof Map.Entry))
            {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) e;
            K key = (K) entry.getKey();
            V value = (V) entry.getValue();

            V actualValue = AbstractMutableBiMap.this.delegate.get(key);
            if (actualValue != null)
            {
                if (actualValue.equals(value))
                {
                    AbstractMutableBiMap.this.remove(key);
                    return true;
                }
                return false;
            }
            if (value == null && AbstractMutableBiMap.this.delegate.containsKey(key))
            {
                AbstractMutableBiMap.this.remove(key);
                return true;
            }
            return false;
        }

        public boolean containsAll(Collection<?> collection)
        {
            for (Object obj : collection)
            {
                if (!this.contains(obj))
                {
                    return false;
                }
            }
            return true;
        }

        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection)
        {
            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
        }

        public boolean retainAll(Collection<?> collection)
        {
            int oldSize = AbstractMutableBiMap.this.size();
            Iterator<Map.Entry<K, V>> iterator = this.iterator();
            while (iterator.hasNext())
            {
                Map.Entry<K, V> next = iterator.next();
                if (!collection.contains(next))
                {
                    iterator.remove();
                }
            }
            return oldSize != AbstractMutableBiMap.this.size();
        }

        public boolean removeAll(Collection<?> collection)
        {
            boolean changed = false;
            for (Object obj : collection)
            {
                if (this.remove(obj))
                {
                    changed = true;
                }
            }
            return changed;
        }

        public void clear()
        {
            AbstractMutableBiMap.this.clear();
        }

        public Iterator<Map.Entry<K, V>> iterator()
        {
            return new InternalEntrySetIterator();
        }

        private Object[] copyEntries(Object[] result)
        {
            int count = 0;
            for (Pair<K, V> pair : AbstractMutableBiMap.this.keyValuesView())
            {
                result[count] = new InternalEntry(pair.getOne(), pair.getTwo());
                count++;
            }
            return result;
        }

        private class InternalEntrySetIterator implements Iterator<Entry<K, V>>
        {
            private final Iterator<Entry<K, V>> iterator = AbstractMutableBiMap.this.delegate.entrySet().iterator();
            private V currentValue;

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }

            public Entry<K, V> next()
            {
                Entry<K, V> next = this.iterator.next();
                Entry<K, V> result = new InternalEntry(next.getKey(), next.getValue());
                this.currentValue = result.getValue();
                return result;
            }

            public void remove()
            {
                this.iterator.remove();
                AbstractMutableBiMap.this.inverse.delegate.removeKey(this.currentValue);
            }
        }

        private final class InternalEntry implements Entry<K, V>
        {
            private final K key;
            private V value;

            private InternalEntry(K key, V value)
            {
                this.key = key;
                this.value = value;
            }

            public K getKey()
            {
                return this.key;
            }

            public V getValue()
            {
                return this.value;
            }

            @Override
            public boolean equals(Object obj)
            {
                if (obj instanceof Entry)
                {
                    Entry<?, ?> other = (Entry<?, ?>) obj;
                    Object otherKey = other.getKey();
                    Object otherValue = other.getValue();
                    return AbstractMutableBiMap.nullSafeEquals(this.key, otherKey)
                            && AbstractMutableBiMap.nullSafeEquals(this.value, otherValue);
                }
                return false;
            }

            @Override
            public int hashCode()
            {
                return (this.key == null ? 0 : this.key.hashCode())
                        ^ (this.value == null ? 0 : this.value.hashCode());
            }

            @Override
            public String toString()
            {
                return this.key + "=" + this.value;
            }

            public V setValue(V value)
            {
                V result = AbstractMutableBiMap.this.put(this.key, value);
                this.value = value;
                return result;
            }
        }
    }

    private static class Inverse<K, V> extends AbstractMutableBiMap<K, V> implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        public Inverse()
        {
            // Empty constructor for Externalizable class
            super(UnifiedMap.<K, V>newMap(), UnifiedMap.<V, K>newMap());
        }

        Inverse(Map<K, V> delegate, AbstractMutableBiMap<V, K> inverse)
        {
            super(delegate, inverse);
        }
    }
}
