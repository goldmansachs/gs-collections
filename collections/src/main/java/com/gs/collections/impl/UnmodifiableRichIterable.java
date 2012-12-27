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

package com.gs.collections.impl;

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
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.utility.internal.IterableIterate;

/**
 * An unmodifiable view of a RichIterable.
 */
public class UnmodifiableRichIterable<T>
        implements RichIterable<T>, Serializable
{
    private static final long serialVersionUID = 1L;

    protected final RichIterable<T> iterable;

    protected UnmodifiableRichIterable(RichIterable<T> richIterable)
    {
        this.iterable = richIterable;
    }

    /**
     * This method will take a RichIterable and wrap it directly in a UnmodifiableRichIterable.
     */
    public static <E, RI extends RichIterable<E>> UnmodifiableRichIterable<E> of(RI iterable)
    {
        if (iterable == null)
        {
            throw new IllegalArgumentException("cannot create a UnmodifiableRichIterable for null");
        }
        return new UnmodifiableRichIterable<E>(iterable);
    }

    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return this.iterable.select(predicate, target);
    }

    public <P, R extends Collection<T>> R selectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.iterable.selectWith(predicate, parameter, targetCollection);
    }

    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return this.iterable.reject(predicate, target);
    }

    public <P, R extends Collection<T>> R rejectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return this.iterable.rejectWith(predicate, parameter, targetCollection);
    }

    public PartitionIterable<T> partition(Predicate<? super T> predicate)
    {
        return this.iterable.partition(predicate);
    }

    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return this.iterable.collect(function, target);
    }

    public <P, V, R extends Collection<V>> R collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return this.iterable.collectWith(function, parameter, targetCollection);
    }

    public <V, R extends Collection<V>> R collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return this.iterable.collectIf(predicate, function, target);
    }

    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return this.iterable.flatCollect(function, target);
    }

    public boolean contains(Object object)
    {
        return this.iterable.contains(object);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return this.iterable.containsAllIterable(source);
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.containsAllIterable(source);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return this.iterable.containsAllArguments(elements);
    }

    public Object[] toArray()
    {
        return this.iterable.toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return this.iterable.toArray(a);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.iterable.forEach(procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.iterable.forEachWithIndex(objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        this.iterable.forEachWith(procedure, parameter);
    }

    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
    }

    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    public int size()
    {
        return this.iterable.size();
    }

    public T getFirst()
    {
        return this.iterable.getFirst();
    }

    public T getLast()
    {
        return this.iterable.getLast();
    }

    public RichIterable<T> select(Predicate<? super T> predicate)
    {
        return this.iterable.select(predicate);
    }

    public RichIterable<T> reject(Predicate<? super T> predicate)
    {
        return this.iterable.reject(predicate);
    }

    public <S> RichIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return this.iterable.selectInstancesOf(clazz);
    }

    public <V> RichIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return this.iterable.collect(function);
    }

    public <V> RichIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.iterable.flatCollect(function);
    }

    public <V> RichIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.iterable.collectIf(predicate, function);
    }

    public T detect(Predicate<? super T> predicate)
    {
        return this.iterable.detect(predicate);
    }

    public T min(Comparator<? super T> comparator)
    {
        return this.iterable.min(comparator);
    }

    public T max(Comparator<? super T> comparator)
    {
        return this.iterable.max(comparator);
    }

    public T min()
    {
        return this.iterable.min();
    }

    public T max()
    {
        return this.iterable.max();
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return this.iterable.minBy(function);
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return this.iterable.maxBy(function);
    }

    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return this.iterable.detectIfNone(predicate, function);
    }

    public int count(Predicate<? super T> predicate)
    {
        return this.iterable.count(predicate);
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.iterable.anySatisfy(predicate);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.iterable.allSatisfy(predicate);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return this.iterable.injectInto(injectedValue, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        return this.iterable.injectInto(injectedValue, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        return this.iterable.injectInto(injectedValue, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return this.iterable.injectInto(injectedValue, function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        return this.iterable.injectInto(injectedValue, function);
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        return this.iterable.sumOfInt(function);
    }

    public double sumOfFloat(FloatFunction<? super T> function)
    {
        return this.iterable.sumOfFloat(function);
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        return this.iterable.sumOfLong(function);
    }

    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        return this.iterable.sumOfDouble(function);
    }

    public MutableList<T> toList()
    {
        return this.iterable.toList();
    }

    public MutableList<T> toSortedList()
    {
        return this.iterable.toSortedList();
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return this.iterable.toSortedList(comparator);
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return this.iterable.toSortedListBy(function);
    }

    public MutableSortedSet<T> toSortedSet()
    {
        return this.iterable.toSortedSet();
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return this.iterable.toSortedSet(comparator);
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return this.iterable.toSortedSetBy(function);
    }

    public MutableSet<T> toSet()
    {
        return this.iterable.toSet();
    }

    public MutableBag<T> toBag()
    {
        return this.iterable.toBag();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return this.iterable.toMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return this.iterable.toSortedMap(keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return this.iterable.toSortedMap(comparator, keyFunction, valueFunction);
    }

    public LazyIterable<T> asLazy()
    {
        return this.iterable.asLazy();
    }

    public Iterator<T> iterator()
    {
        return new UnmodifiableIteratorAdapter<T>(this.iterable.iterator());
    }

    @Override
    public String toString()
    {
        return this.iterable.toString();
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

    public <V> Multimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.iterable.groupBy(function);
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        return this.iterable.groupBy(function, target);
    }

    public <V> Multimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.iterable.groupByEach(function);
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return this.iterable.groupByEach(function, target);
    }

    public <S> RichIterable<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.iterable.zip(that);
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return this.iterable.zip(that, target);
    }

    public RichIterable<Pair<T, Integer>> zipWithIndex()
    {
        return this.iterable.zipWithIndex();
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return this.iterable.zipWithIndex(target);
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        return this.iterable.chunk(size);
    }

    public <K, V> MapIterable<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K, V> MapIterable<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }
}
