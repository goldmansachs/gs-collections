/*
 * Copyright 2015 Goldman Sachs.
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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
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
import com.gs.collections.api.map.primitive.ObjectDoubleMap;
import com.gs.collections.api.map.primitive.ObjectLongMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.mutable.primitive.ObjectDoubleHashMap;
import com.gs.collections.impl.map.mutable.primitive.ObjectLongHashMap;

/**
 * AbstractMultiReaderMutableCollection is a common abstraction that provides thread-safe collection behaviors.
 * Subclasses of this class must provide implementations of getDelegate() and getLock().
 */
public abstract class AbstractMultiReaderMutableCollection<T> implements MutableCollection<T>
{
    protected abstract MutableCollection<T> getDelegate();

    protected abstract ReadWriteLock getLock();

    protected void acquireWriteLock()
    {
        this.getLock().writeLock().lock();
    }

    protected void unlockWriteLock()
    {
        this.getLock().writeLock().unlock();
    }

    protected void acquireReadLock()
    {
        this.getLock().readLock().lock();
    }

    protected void unlockReadLock()
    {
        this.getLock().readLock().unlock();
    }

    protected void withReadLockRun(Runnable block)
    {
        this.acquireReadLock();
        try
        {
            block.run();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean contains(Object item)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().contains(item);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean containsAll(Collection<?> collection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().containsAll(collection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().containsAllIterable(source);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean containsAllArguments(Object... elements)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().containsAllArguments(elements);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().noneSatisfy(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> boolean noneSatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().noneSatisfyWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().allSatisfy(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> boolean allSatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().allSatisfyWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().anySatisfy(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> boolean anySatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().anySatisfyWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> toList()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toList();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toMap(keyFunction, valueFunction);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedMap(keyFunction, valueFunction);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedMap(comparator, keyFunction, valueFunction);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public LazyIterable<T> asLazy()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().asLazy();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSet<T> toSet()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSet();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableBag<T> toBag()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toBag();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSortedBag<T> toSortedBag()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedBag();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedBag(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> MutableSortedBag<T> toSortedBagBy(
            Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedBagBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> toSortedList()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedList();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedList(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(
            Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedListBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSortedSet<T> toSortedSet()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedSet();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedSet(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(
            Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedSetBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public int count(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().count(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> int countWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().countWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T detect(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().detect(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T min(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().min(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T max(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().max(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T min()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().min();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T max()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().max();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().minBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().maxBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T detectIfNone(
            Predicate<? super T> predicate,
            Function0<? extends T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().detectIfNone(predicate, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> T detectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().detectWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().detectWithIfNone(predicate, parameter, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T getFirst()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().getFirst();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T getLast()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().getLast();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean notEmpty()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().notEmpty();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().selectAndRejectWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends Collection<V>> R collect(
            Function<? super T, ? extends V> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collect(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableBooleanCollection> R collectBoolean(BooleanFunction<? super T> booleanFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectBoolean(booleanFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableByteCollection> R collectByte(ByteFunction<? super T> byteFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectByte(byteFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableCharCollection> R collectChar(CharFunction<? super T> charFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectChar(charFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableDoubleCollection> R collectDouble(DoubleFunction<? super T> doubleFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectDouble(doubleFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableFloatCollection> R collectFloat(FloatFunction<? super T> floatFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectFloat(floatFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableIntCollection> R collectInt(IntFunction<? super T> intFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectInt(intFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableLongCollection> R collectLong(LongFunction<? super T> longFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectLong(longFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends MutableShortCollection> R collectShort(ShortFunction<? super T> shortFunction, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectShort(shortFunction, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends Collection<V>> R flatCollect(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().flatCollect(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectIf(predicate, function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P, V, R extends Collection<V>> R collectWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().collectWith(function, parameter, targetCollection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().selectWith(predicate, parameter, targetCollection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends Collection<T>> R reject(
            Predicate<? super T> predicate,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().reject(predicate, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().rejectWith(predicate, parameter, targetCollection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().select(predicate, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <IV> IV injectInto(
            IV injectedValue,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().injectInto(injectedValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().injectInto(injectedValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().injectInto(injectedValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().injectInto(injectedValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().injectInto(injectedValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().sumOfInt(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public double sumOfFloat(FloatFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().sumOfFloat(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().sumOfLong(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().sumOfDouble(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V> ObjectLongMap<V> sumByInt(Function<T, V> groupBy, IntFunction<? super T> function)
    {
        ObjectLongHashMap<V> result = ObjectLongHashMap.newMap();
        return this.injectInto(result, PrimitiveFunctions.sumByIntFunction(groupBy, function));
    }

    public <V> ObjectDoubleMap<V> sumByFloat(Function<T, V> groupBy, FloatFunction<? super T> function)
    {
        ObjectDoubleHashMap<V> result = ObjectDoubleHashMap.newMap();
        return this.injectInto(result, PrimitiveFunctions.sumByFloatFunction(groupBy, function));
    }

    public <V> ObjectLongMap<V> sumByLong(Function<T, V> groupBy, LongFunction<? super T> function)
    {
        ObjectLongHashMap<V> result = ObjectLongHashMap.newMap();
        return this.injectInto(result, PrimitiveFunctions.sumByLongFunction(groupBy, function));
    }

    public <V> ObjectDoubleMap<V> sumByDouble(Function<T, V> groupBy, DoubleFunction<? super T> function)
    {
        ObjectDoubleHashMap<V> result = ObjectDoubleHashMap.newMap();
        return this.injectInto(result, PrimitiveFunctions.sumByDoubleFunction(groupBy, function));
    }

    public <IV, P> IV injectIntoWith(
            IV injectValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().injectIntoWith(injectValue, function, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean removeIf(Predicate<? super T> predicate)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().removeIf(predicate);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public <P> boolean removeIfWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().removeIfWith(predicate, parameter);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean add(T item)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().add(item);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean addAll(Collection<? extends T> collection)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().addAll(collection);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().addAllIterable(iterable);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public void clear()
    {
        this.acquireWriteLock();
        try
        {
            this.getDelegate().clear();
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean isEmpty()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().isEmpty();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    /**
     * This method is not supported directly on MultiReader collections because it is not thread-safe. If you would like
     * to use an iterator with a MultiReader collection, then you must do the following:
     * <p>
     * <pre>
     * multiReaderList.withReadLockAndDelegate(new Procedure<MutableList<Person>>()
     * {
     *     public void value(MutableList<Person> people)
     *     {
     *         Iterator it = people.iterator();
     *         ....
     *     }
     * });
     * </pre>
     * <p>
     * <pre>
     * final Collection jdkSet = new HashSet();
     * final boolean containsAll = new boolean[1];
     * multiReaderList.withReadLockAndDelegate(new Procedure<MutableList<Person>>()
     * {
     *     public void value(MutableList<Person> people)
     *     {
     *         set.addAll(people); // addAll uses iterator() in AbstractCollection
     *         containsAll[0] = set.containsAll(people); // containsAll uses iterator() in AbstractCollection
     *     }
     * });
     * </pre>
     */
    public Iterator<T> iterator()
    {
        throw new UnsupportedOperationException(
                "Iterator is not supported directly on MultiReader collections.  "
                        + "If you would like to use an iterator, you must either use withReadLockAndDelegate() or withWriteLockAndDelegate().");
    }

    public boolean remove(Object item)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().remove(item);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean removeAll(Collection<?> collection)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().removeAll(collection);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().removeAllIterable(iterable);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean retainAll(Collection<?> collection)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().retainAll(collection);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().retainAllIterable(iterable);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public int size()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().size();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public Object[] toArray()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toArray();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <E> E[] toArray(E[] a)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toArray(a);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.each(procedure);
    }

    public void each(Procedure<? super T> procedure)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().forEach(procedure);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().forEachWith(procedure, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().forEachWithIndex(objectIntProcedure);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    @Override
    public String toString()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toString();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public String makeString()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().makeString();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public String makeString(String separator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().makeString(separator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public String makeString(String start, String separator, String end)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().makeString(start, separator, end);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void appendString(Appendable appendable)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().appendString(appendable);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().appendString(appendable, separator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().appendString(appendable, start, separator, end);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().groupBy(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().groupByEach(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends MutableMap<V, T>> R groupByUniqueKey(
            Function<? super T, ? extends V> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().groupByUniqueKey(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().zip(that, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().zipWithIndex(target);
        }
        finally
        {
            this.unlockReadLock();
        }
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

    protected abstract static class UntouchableMutableCollection<T>
            implements MutableCollection<T>
    {
        protected MutableCollection<T> delegate;

        public boolean allSatisfy(Predicate<? super T> predicate)
        {
            return this.delegate.allSatisfy(predicate);
        }

        public <P> boolean allSatisfyWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.allSatisfyWith(predicate, parameter);
        }

        public boolean noneSatisfy(Predicate<? super T> predicate)
        {
            return this.delegate.noneSatisfy(predicate);
        }

        public <P> boolean noneSatisfyWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.noneSatisfyWith(predicate, parameter);
        }

        public boolean anySatisfy(Predicate<? super T> predicate)
        {
            return this.delegate.anySatisfy(predicate);
        }

        public <P> boolean anySatisfyWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.anySatisfyWith(predicate, parameter);
        }

        public MutableList<T> toList()
        {
            return this.delegate.toList();
        }

        public <NK, NV> MutableMap<NK, NV> toMap(
                Function<? super T, ? extends NK> keyFunction,
                Function<? super T, ? extends NV> valueFunction)
        {
            return this.delegate.toMap(keyFunction, valueFunction);
        }

        public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
                Function<? super T, ? extends NK> keyFunction,
                Function<? super T, ? extends NV> valueFunction)
        {
            return this.delegate.toSortedMap(keyFunction, valueFunction);
        }

        public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
                Function<? super T, ? extends NK> keyFunction,
                Function<? super T, ? extends NV> valueFunction)
        {
            return this.delegate.toSortedMap(comparator, keyFunction, valueFunction);
        }

        public MutableSet<T> toSet()
        {
            return this.delegate.toSet();
        }

        public MutableBag<T> toBag()
        {
            return this.delegate.toBag();
        }

        public MutableSortedBag<T> toSortedBag()
        {
            return this.delegate.toSortedBag();
        }

        public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
        {
            return this.delegate.toSortedBag(comparator);
        }

        public <V extends Comparable<? super V>> MutableSortedBag<T> toSortedBagBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.toSortedBagBy(function);
        }

        public MutableList<T> toSortedList()
        {
            return this.delegate.toSortedList();
        }

        public MutableList<T> toSortedList(Comparator<? super T> comparator)
        {
            return this.delegate.toSortedList(comparator);
        }

        public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.toSortedListBy(function);
        }

        public MutableSortedSet<T> toSortedSet()
        {
            return this.delegate.toSortedSet();
        }

        public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
        {
            return this.delegate.toSortedSet(comparator);
        }

        public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.toSortedSetBy(function);
        }

        public <V, R extends Collection<V>> R collect(
                Function<? super T, ? extends V> function,
                R target)
        {
            return this.delegate.collect(function, target);
        }

        public <V, R extends Collection<V>> R flatCollect(
                Function<? super T, ? extends Iterable<V>> function,
                R target)
        {
            return this.delegate.flatCollect(function, target);
        }

        public <V, R extends Collection<V>> R collectIf(
                Predicate<? super T> predicate,
                Function<? super T, ? extends V> function,
                R target)
        {
            return this.delegate.collectIf(predicate, function, target);
        }

        public <P, V, R extends Collection<V>> R collectWith(
                Function2<? super T, ? super P, ? extends V> function,
                P parameter,
                R targetCollection)
        {
            return this.delegate.collectWith(function, parameter, targetCollection);
        }

        public <V, R extends MutableMultimap<V, T>> R groupBy(
                Function<? super T, ? extends V> function,
                R target)
        {
            return this.delegate.groupBy(function, target);
        }

        public <V, R extends MutableMultimap<V, T>> R groupByEach(
                Function<? super T, ? extends Iterable<V>> function,
                R target)
        {
            return this.delegate.groupByEach(function, target);
        }

        public <V, R extends MutableMap<V, T>> R groupByUniqueKey(
                Function<? super T, ? extends V> function,
                R target)
        {
            return this.delegate.groupByUniqueKey(function, target);
        }

        public int count(Predicate<? super T> predicate)
        {
            return this.delegate.count(predicate);
        }

        public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
        {
            return this.delegate.countWith(predicate, parameter);
        }

        public T detect(Predicate<? super T> predicate)
        {
            return this.delegate.detect(predicate);
        }

        public T min(Comparator<? super T> comparator)
        {
            return this.delegate.min(comparator);
        }

        public T max(Comparator<? super T> comparator)
        {
            return this.delegate.max(comparator);
        }

        public T min()
        {
            return this.delegate.min();
        }

        public T max()
        {
            return this.delegate.max();
        }

        public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.minBy(function);
        }

        public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.maxBy(function);
        }

        public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
        {
            return this.delegate.detectIfNone(predicate, function);
        }

        public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
        {
            return this.delegate.detectWith(predicate, parameter);
        }

        public <P> T detectWithIfNone(
                Predicate2<? super T, ? super P> predicate,
                P parameter,
                Function0<? extends T> function)
        {
            return this.delegate.detectWithIfNone(predicate, parameter, function);
        }

        public T getFirst()
        {
            return this.delegate.getFirst();
        }

        public T getLast()
        {
            return this.delegate.getLast();
        }

        public <IV> IV injectInto(
                IV injectedValue,
                Function2<? super IV, ? super T, ? extends IV> function)
        {
            return this.delegate.injectInto(injectedValue, function);
        }

        public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
        {
            return this.delegate.injectInto(injectedValue, function);
        }

        public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
        {
            return this.delegate.injectInto(injectedValue, function);
        }

        public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
        {
            return this.delegate.injectInto(injectedValue, function);
        }

        public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
        {
            return this.delegate.injectInto(injectedValue, function);
        }

        public long sumOfInt(IntFunction<? super T> function)
        {
            return this.delegate.sumOfInt(function);
        }

        public double sumOfFloat(FloatFunction<? super T> function)
        {
            return this.delegate.sumOfFloat(function);
        }

        public long sumOfLong(LongFunction<? super T> function)
        {
            return this.delegate.sumOfLong(function);
        }

        public double sumOfDouble(DoubleFunction<? super T> function)
        {
            return this.delegate.sumOfDouble(function);
        }

        public <V> ObjectLongMap<V> sumByInt(Function<T, V> groupBy, IntFunction<? super T> function)
        {
            return this.delegate.sumByInt(groupBy, function);
        }

        public <V> ObjectDoubleMap<V> sumByFloat(Function<T, V> groupBy, FloatFunction<? super T> function)
        {
            return this.delegate.sumByFloat(groupBy, function);
        }

        public <V> ObjectLongMap<V> sumByLong(Function<T, V> groupBy, LongFunction<? super T> function)
        {
            return this.delegate.sumByLong(groupBy, function);
        }

        public <V> ObjectDoubleMap<V> sumByDouble(Function<T, V> groupBy, DoubleFunction<? super T> function)
        {
            return this.delegate.sumByDouble(groupBy, function);
        }

        public <IV, P> IV injectIntoWith(
                IV injectValue,
                Function3<? super IV, ? super T, ? super P, ? extends IV> function,
                P parameter)
        {
            return this.delegate.injectIntoWith(injectValue, function, parameter);
        }

        public boolean notEmpty()
        {
            return this.delegate.notEmpty();
        }

        public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
        {
            return this.delegate.reject(predicate, target);
        }

        public <P, R extends Collection<T>> R rejectWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter,
                R targetCollection)
        {
            return this.delegate.rejectWith(predicate, parameter, targetCollection);
        }

        public boolean removeIf(Predicate<? super T> predicate)
        {
            return this.delegate.removeIf(predicate);
        }

        public <P> boolean removeIfWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.removeIfWith(predicate, parameter);
        }

        public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
        {
            return this.delegate.select(predicate, target);
        }

        public <P> Twin<MutableList<T>> selectAndRejectWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.selectAndRejectWith(predicate, parameter);
        }

        public <P, R extends Collection<T>> R selectWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter,
                R targetCollection)
        {
            return this.delegate.selectWith(predicate, parameter, targetCollection);
        }

        public boolean add(T o)
        {
            return this.delegate.add(o);
        }

        public boolean addAll(Collection<? extends T> collection)
        {
            return this.delegate.addAll(collection);
        }

        public boolean addAllIterable(Iterable<? extends T> iterable)
        {
            return this.delegate.addAllIterable(iterable);
        }

        public void clear()
        {
            this.delegate.clear();
        }

        public boolean contains(Object o)
        {
            return this.delegate.contains(o);
        }

        public boolean containsAll(Collection<?> collection)
        {
            return this.delegate.containsAll(collection);
        }

        public boolean containsAllIterable(Iterable<?> source)
        {
            return this.delegate.containsAllIterable(source);
        }

        public boolean containsAllArguments(Object... elements)
        {
            return this.delegate.containsAllArguments(elements);
        }

        @Override
        public boolean equals(Object o)
        {
            return this.delegate.equals(o);
        }

        @Override
        public int hashCode()
        {
            return this.delegate.hashCode();
        }

        public boolean isEmpty()
        {
            return this.delegate.isEmpty();
        }

        public boolean remove(Object o)
        {
            return this.delegate.remove(o);
        }

        public boolean removeAll(Collection<?> collection)
        {
            return this.delegate.removeAll(collection);
        }

        public boolean removeAllIterable(Iterable<?> iterable)
        {
            return this.delegate.removeAllIterable(iterable);
        }

        public boolean retainAll(Collection<?> collection)
        {
            return this.delegate.retainAll(collection);
        }

        public boolean retainAllIterable(Iterable<?> iterable)
        {
            return this.delegate.retainAllIterable(iterable);
        }

        public int size()
        {
            return this.delegate.size();
        }

        public Object[] toArray()
        {
            return this.delegate.toArray();
        }

        public <T> T[] toArray(T[] a)
        {
            return this.delegate.toArray(a);
        }

        public void forEach(Procedure<? super T> procedure)
        {
            this.each(procedure);
        }

        public void each(Procedure<? super T> procedure)
        {
            this.delegate.forEach(procedure);
        }

        public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
        {
            this.delegate.forEachWith(procedure, parameter);
        }

        public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
        {
            this.delegate.forEachWithIndex(objectIntProcedure);
        }

        @Override
        public String toString()
        {
            return this.delegate.toString();
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

        public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
        {
            return this.delegate.zip(that, target);
        }

        public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
        {
            return this.delegate.zipWithIndex(target);
        }

        public RichIterable<RichIterable<T>> chunk(int size)
        {
            return this.delegate.chunk(size);
        }

        public <K, V> MutableMap<K, V> aggregateInPlaceBy(
                final Function<? super T, ? extends K> groupBy,
                final Function0<? extends V> zeroValueFactory,
                final Procedure2<? super V, ? super T> mutatingAggregator)
        {
            final MutableMap<K, V> map = UnifiedMap.newMap();
            this.forEach(new Procedure<T>()
            {
                public void value(T each)
                {
                    K key = groupBy.valueOf(each);
                    V value = map.getIfAbsentPut(key, zeroValueFactory);
                    mutatingAggregator.value(value, each);
                }
            });
            return map;
        }

        public <K, V> MutableMap<K, V> aggregateBy(
                final Function<? super T, ? extends K> groupBy,
                final Function0<? extends V> zeroValueFactory,
                final Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
        {
            final MutableMap<K, V> map = UnifiedMap.newMap();
            this.forEach(new Procedure<T>()
            {
                public void value(T each)
                {
                    K key = groupBy.valueOf(each);
                    V value = map.getIfAbsentPut(key, zeroValueFactory);
                    map.put(key, nonMutatingAggregator.value(value, each));
                }
            });
            return map;
        }
    }
}
