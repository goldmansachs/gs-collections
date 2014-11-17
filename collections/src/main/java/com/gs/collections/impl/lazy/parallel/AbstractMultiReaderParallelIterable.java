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

package com.gs.collections.impl.lazy.parallel;

import java.util.Comparator;
import java.util.concurrent.locks.ReadWriteLock;

import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.lazy.parallel.list.MultiReaderParallelListIterable;
import com.gs.collections.impl.lazy.parallel.set.MultiReaderParallelUnsortedSetIterable;

public abstract class AbstractMultiReaderParallelIterable<T, PI extends ParallelIterable<T>> implements ParallelIterable<T>
{
    protected final PI delegate;
    protected final ReadWriteLock lock;

    protected AbstractMultiReaderParallelIterable(PI delegate, ReadWriteLock lock)
    {
        this.delegate = delegate;
        this.lock = lock;
    }

    protected <A> ParallelListIterable<A> wrap(ParallelListIterable<A> wrapped)
    {
        return new MultiReaderParallelListIterable<A>(wrapped, this.lock);
    }

    protected <A> ParallelUnsortedSetIterable<A> wrap(ParallelUnsortedSetIterable<A> wrapped)
    {
        return new MultiReaderParallelUnsortedSetIterable<A>(wrapped, this.lock);
    }

    protected <A> ParallelIterable<A> wrap(ParallelIterable<A> wrapped)
    {
        return new MultiReaderParallelIterable<A>(wrapped, this.lock);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.lock.readLock().lock();
        try
        {
            this.delegate.forEach(procedure);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        this.lock.readLock().lock();
        try
        {
            this.delegate.forEachWith(procedure, parameter);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public T detect(Predicate<? super T> predicate)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.detect(predicate);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.detectWith(predicate, parameter);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.detectIfNone(predicate, function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <P> T detectWithIfNone(Predicate2<? super T, ? super P> predicate, P parameter, Function0<? extends T> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.detectWithIfNone(predicate, parameter, function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public int count(Predicate<? super T> predicate)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.count(predicate);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.countWith(predicate, parameter);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.anySatisfy(predicate);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.anySatisfyWith(predicate, parameter);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.allSatisfy(predicate);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.allSatisfyWith(predicate, parameter);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.noneSatisfy(predicate);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.noneSatisfyWith(predicate, parameter);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableList<T> toList()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toList();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableList<T> toSortedList()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedList();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedList(comparator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedListBy(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableSet<T> toSet()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSet();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableSortedSet<T> toSortedSet()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedSet();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedSet(comparator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedSetBy(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableBag<T> toBag()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toBag();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableSortedBag<T> toSortedBag()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedBag();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedBag(comparator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <V extends Comparable<? super V>> MutableSortedBag<T> toSortedBagBy(Function<? super T, ? extends V> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedBagBy(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <NK, NV> MutableMap<NK, NV> toMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toMap(keyFunction, valueFunction);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedMap(keyFunction, valueFunction);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator, Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toSortedMap(comparator, keyFunction, valueFunction);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public Object[] toArray()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toArray();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <T1> T1[] toArray(T1[] target)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toArray(target);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public T min(Comparator<? super T> comparator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.min(comparator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public T max(Comparator<? super T> comparator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.max(comparator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public T min()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.min();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public T max()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.max();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.minBy(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.maxBy(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.sumOfInt(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public double sumOfFloat(FloatFunction<? super T> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.sumOfFloat(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.sumOfLong(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.sumOfDouble(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public String makeString()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.makeString();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public String makeString(String separator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.makeString(separator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public String makeString(String start, String separator, String end)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.makeString(start, separator, end);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public void appendString(Appendable appendable)
    {
        this.lock.readLock().lock();
        try
        {
            this.delegate.appendString(appendable);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.lock.readLock().lock();
        try
        {
            this.delegate.appendString(appendable, separator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.lock.readLock().lock();
        try
        {
            this.delegate.appendString(appendable, start, separator, end);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.groupByUniqueKey(function);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <K, V> MapIterable<K, V> aggregateInPlaceBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Procedure2<? super V, ? super T> mutatingAggregator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.aggregateInPlaceBy(groupBy, zeroValueFactory, mutatingAggregator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    public <K, V> MapIterable<K, V> aggregateBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.aggregateBy(groupBy, zeroValueFactory, nonMutatingAggregator);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public String toString()
    {
        this.lock.readLock().lock();
        try
        {
            return this.delegate.toString();
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }
}
