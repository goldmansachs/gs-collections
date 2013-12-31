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

package com.gs.collections.impl.map;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.ByteIterable;
import com.gs.collections.api.CharIterable;
import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.LongIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.ShortIterable;
import com.gs.collections.api.bag.MutableBag;
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
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;

public abstract class AbstractMapIterable<K, V> implements MapIterable<K, V>
{
    protected int keyAndValueHashCode(K key, V value)
    {
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    protected boolean keyAndValueEquals(K key, V value, Map<K, V> map)
    {
        if (value == null && !map.containsKey(key))
        {
            return false;
        }

        V oValue = map.get(key);
        return oValue == value || oValue != null && oValue.equals(value);
    }

    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    public boolean notEmpty()
    {
        return !this.isEmpty();
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        V result = this.get(key);
        return this.isAbsent(result, key) ? null : function.valueOf(result);
    }

    protected boolean isAbsent(V result, K key)
    {
        return result == null && !this.containsKey(key);
    }

    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        V result = this.get(key);
        if (!this.isAbsent(result, key))
        {
            return result;
        }
        return function.value();
    }

    public V getIfAbsentValue(K key, V value)
    {
        V result = this.get(key);
        if (!this.isAbsent(result, key))
        {
            return result;
        }
        return value;
    }

    public <P> V getIfAbsentWith(
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.valueOf(parameter);
        }
        return result;
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().anySatisfy(predicate);
    }

    public <P> boolean anySatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().anySatisfyWith(predicate, parameter);
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().allSatisfy(predicate);
    }

    public <P> boolean allSatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().allSatisfyWith(predicate, parameter);
    }

    public boolean noneSatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().noneSatisfy(predicate);
    }

    public <P> boolean noneSatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().noneSatisfyWith(predicate, parameter);
    }

    public void appendString(Appendable appendable)
    {
        this.valuesView().appendString(appendable);
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.valuesView().appendString(appendable, separator);
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.valuesView().appendString(appendable, start, separator, end);
    }

    public MutableBag<V> toBag()
    {
        return this.valuesView().toBag();
    }

    public LazyIterable<V> asLazy()
    {
        return this.valuesView().asLazy();
    }

    public MutableList<V> toList()
    {
        return this.valuesView().toList();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.valuesView().toMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.valuesView().toSortedMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Comparator<? super NK> comparator,
            Function<? super V, ? extends NK> keyFunction,
            Function<? super V, ? extends NV> valueFunction)
    {
        return this.valuesView().toSortedMap(comparator, keyFunction, valueFunction);
    }

    public MutableSet<V> toSet()
    {
        return this.valuesView().toSet();
    }

    public MutableList<V> toSortedList()
    {
        return this.valuesView().toSortedList();
    }

    public MutableList<V> toSortedList(Comparator<? super V> comparator)
    {
        return this.valuesView().toSortedList(comparator);
    }

    public <R extends Comparable<? super R>> MutableList<V> toSortedListBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().toSortedListBy(function);
    }

    public MutableSortedSet<V> toSortedSet()
    {
        return this.valuesView().toSortedSet();
    }

    public MutableSortedSet<V> toSortedSet(Comparator<? super V> comparator)
    {
        return this.valuesView().toSortedSet(comparator);
    }

    public <R extends Comparable<? super R>> MutableSortedSet<V> toSortedSetBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().toSortedSetBy(function);
    }

    public RichIterable<RichIterable<V>> chunk(int size)
    {
        return this.valuesView().chunk(size);
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.forEachValue(procedure);
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure2, P parameter)
    {
        this.valuesView().forEachWith(procedure2, parameter);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.valuesView().forEachWithIndex(objectIntProcedure);
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        this.keysView().forEach(procedure);
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        this.valuesView().forEach(procedure);
    }

    public <R> RichIterable<R> collect(Function<? super V, ? extends R> function)
    {
        return this.valuesView().collect(function);
    }

    public BooleanIterable collectBoolean(BooleanFunction<? super V> booleanFunction)
    {
        return this.valuesView().collectBoolean(booleanFunction);
    }

    public ByteIterable collectByte(ByteFunction<? super V> byteFunction)
    {
        return this.valuesView().collectByte(byteFunction);
    }

    public CharIterable collectChar(CharFunction<? super V> charFunction)
    {
        return this.valuesView().collectChar(charFunction);
    }

    public DoubleIterable collectDouble(DoubleFunction<? super V> doubleFunction)
    {
        return this.valuesView().collectDouble(doubleFunction);
    }

    public FloatIterable collectFloat(FloatFunction<? super V> floatFunction)
    {
        return this.valuesView().collectFloat(floatFunction);
    }

    public IntIterable collectInt(IntFunction<? super V> intFunction)
    {
        return this.valuesView().collectInt(intFunction);
    }

    public LongIterable collectLong(LongFunction<? super V> longFunction)
    {
        return this.valuesView().collectLong(longFunction);
    }

    public ShortIterable collectShort(ShortFunction<? super V> shortFunction)
    {
        return this.valuesView().collectShort(shortFunction);
    }

    public <R, C extends Collection<R>> C collect(Function<? super V, ? extends R> function, C target)
    {
        return this.valuesView().collect(function, target);
    }

    public <R> RichIterable<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.valuesView().collectIf(predicate, function);
    }

    public <R, C extends Collection<R>> C collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function, C target)
    {
        return this.valuesView().collectIf(predicate, function, target);
    }

    public <P, R, C extends Collection<R>> C collectWith(Function2<? super V, ? super P, ? extends R> function, P parameter, C targetCollection)
    {
        return this.valuesView().collectWith(function, parameter, targetCollection);
    }

    public boolean contains(Object object)
    {
        return this.containsValue(object);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return this.valuesView().containsAllArguments(elements);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return this.valuesView().containsAllIterable(source);
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.containsAllIterable(source);
    }

    public int count(Predicate<? super V> predicate)
    {
        return this.valuesView().count(predicate);
    }

    public V detect(Predicate<? super V> predicate)
    {
        return this.valuesView().detect(predicate);
    }

    public V detectIfNone(Predicate<? super V> predicate, Function0<? extends V> function)
    {
        return this.valuesView().detectIfNone(predicate, function);
    }

    public <R> RichIterable<R> flatCollect(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.valuesView().flatCollect(function);
    }

    public <R, C extends Collection<R>> C flatCollect(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.valuesView().flatCollect(function, target);
    }

    public V getFirst()
    {
        return this.valuesView().getFirst();
    }

    public V getLast()
    {
        return this.valuesView().getLast();
    }

    public <R, C extends MutableMultimap<R, V>> C groupBy(Function<? super V, ? extends R> function, C target)
    {
        return this.valuesView().groupBy(function, target);
    }

    public <R, C extends MutableMultimap<R, V>> C groupByEach(Function<? super V, ? extends Iterable<R>> function, C target)
    {
        return this.valuesView().groupByEach(function, target);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super V, ? extends IV> function)
    {
        return this.valuesView().injectInto(injectedValue, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super V> function)
    {
        return this.valuesView().injectInto(injectedValue, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super V> function)
    {
        return this.valuesView().injectInto(injectedValue, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super V> function)
    {
        return this.valuesView().injectInto(injectedValue, function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super V> function)
    {
        return this.valuesView().injectInto(injectedValue, function);
    }

    public long sumOfInt(IntFunction<? super V> function)
    {
        return this.valuesView().sumOfInt(function);
    }

    public double sumOfFloat(FloatFunction<? super V> function)
    {
        return this.valuesView().sumOfFloat(function);
    }

    public long sumOfLong(LongFunction<? super V> function)
    {
        return this.valuesView().sumOfLong(function);
    }

    public double sumOfDouble(DoubleFunction<? super V> function)
    {
        return this.valuesView().sumOfDouble(function);
    }

    public String makeString()
    {
        return this.valuesView().makeString();
    }

    public String makeString(String separator)
    {
        return this.valuesView().makeString(separator);
    }

    public String makeString(String start, String separator, String end)
    {
        return this.valuesView().makeString(start, separator, end);
    }

    public V max()
    {
        return this.valuesView().max();
    }

    public V max(Comparator<? super V> comparator)
    {
        return this.valuesView().max(comparator);
    }

    public <R extends Comparable<? super R>> V maxBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().maxBy(function);
    }

    public V min()
    {
        return this.valuesView().min();
    }

    public V min(Comparator<? super V> comparator)
    {
        return this.valuesView().min(comparator);
    }

    public <R extends Comparable<? super R>> V minBy(Function<? super V, ? extends R> function)
    {
        return this.valuesView().minBy(function);
    }

    public RichIterable<V> reject(Predicate<? super V> predicate)
    {
        return this.valuesView().reject(predicate);
    }

    public <P> RichIterable<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().rejectWith(predicate, parameter);
    }

    public <R extends Collection<V>> R reject(Predicate<? super V> predicate, R target)
    {
        return this.valuesView().reject(predicate, target);
    }

    public <P, R extends Collection<V>> R rejectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.valuesView().rejectWith(predicate, parameter, targetCollection);
    }

    public RichIterable<V> select(Predicate<? super V> predicate)
    {
        return this.valuesView().select(predicate);
    }

    public <P> RichIterable<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().selectWith(predicate, parameter);
    }

    public <R extends Collection<V>> R select(Predicate<? super V> predicate, R target)
    {
        return this.valuesView().select(predicate, target);
    }

    public <P, R extends Collection<V>> R selectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.valuesView().selectWith(predicate, parameter, targetCollection);
    }

    public Object[] toArray()
    {
        return this.valuesView().toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return this.valuesView().toArray(a);
    }

    public <S> RichIterable<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.valuesView().zip(that);
    }

    public <S, R extends Collection<Pair<V, S>>> R zip(Iterable<S> that, R target)
    {
        return this.valuesView().zip(that, target);
    }

    public RichIterable<Pair<V, Integer>> zipWithIndex()
    {
        return this.valuesView().zipWithIndex();
    }

    public <R extends Collection<Pair<V, Integer>>> R zipWithIndex(R target)
    {
        return this.valuesView().zipWithIndex(target);
    }
}
