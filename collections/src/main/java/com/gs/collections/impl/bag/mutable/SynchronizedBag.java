/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.bag.mutable;

import java.util.Collection;
import java.util.Collections;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollection;
import com.gs.collections.impl.factory.Bags;
import net.jcip.annotations.GuardedBy;

/**
 * A synchronized view of a {@link MutableBag}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableBag#asSynchronized()
 * @since 1.0
 */
public class SynchronizedBag<T>
        extends SynchronizedMutableCollection<T>
        implements MutableBag<T>
{
    private static final long serialVersionUID = 1L;

    public SynchronizedBag(MutableBag<T> bag)
    {
        super(bag);
    }

    public SynchronizedBag(MutableBag<T> bag, Object newLock)
    {
        super(bag, newLock);
    }

    /**
     * This method will take a MutableBag and wrap it directly in a SynchronizedBag.
     */
    public static <E, B extends MutableBag<E>> SynchronizedBag<E> of(B bag)
    {
        return new SynchronizedBag<E>(bag);
    }

    @GuardedBy("getLock()")
    private MutableBag<T> getMutableBag()
    {
        return (MutableBag<T>) this.getCollection();
    }

    @Override
    public MutableBag<T> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableBag.of(this);
        }
    }

    @Override
    public ImmutableBag<T> toImmutable()
    {
        return Bags.immutable.ofAll(this);
    }

    @Override
    public MutableBag<T> asSynchronized()
    {
        return this;
    }

    @Override
    public <V> MutableBag<V> collect(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().collect(function);
        }
    }

    @Override
    public <V> MutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().flatCollect(function);
        }
    }

    @Override
    public <V> MutableBag<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().collectIf(predicate, function);
        }
    }

    @Override
    public <P, V> MutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().collectWith(function, parameter);
        }
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().groupBy(function);
        }
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().groupByEach(function);
        }
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().newEmpty();
        }
    }

    @Override
    public MutableBag<T> reject(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().reject(predicate);
        }
    }

    @Override
    public <P> MutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().rejectWith(predicate, parameter);
        }
    }

    public MutableBag<T> selectByOccurrences(IntPredicate predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().selectByOccurrences(predicate);
        }
    }

    @Override
    public MutableBag<T> select(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().select(predicate);
        }
    }

    @Override
    public <P> MutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().selectWith(predicate, parameter);
        }
    }

    @Override
    public PartitionMutableBag<T> partition(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().partition(predicate);
        }
    }

    @Override
    public <S> MutableBag<S> selectInstancesOf(Class<S> clazz)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().selectInstancesOf(clazz);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().equals(obj);
        }
    }

    @Override
    public int hashCode()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().hashCode();
        }
    }

    public void addOccurrences(T item, int occurrences)
    {
        synchronized (this.getLock())
        {
            this.getMutableBag().addOccurrences(item, occurrences);
        }
    }

    public boolean removeOccurrences(Object item, int occurrences)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().removeOccurrences(item, occurrences);
        }
    }

    public int sizeDistinct()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().sizeDistinct();
        }
    }

    public int occurrencesOf(Object item)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().occurrencesOf(item);
        }
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        synchronized (this.getLock())
        {
            this.getMutableBag().forEachWithOccurrences(objectIntProcedure);
        }
    }

    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().toMapOfItemToCount();
        }
    }

    @Override
    public <S> MutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zip(that);
        }
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zip(that, target);
        }
    }

    @Override
    public MutableBag<Pair<T, Integer>> zipWithIndex()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zipWithIndex();
        }
    }

    public String toStringOfItemToCount()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().toStringOfItemToCount();
        }
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zipWithIndex(target);
        }
    }

    @Override
    public MutableBag<T> with(T element)
    {
        this.add(element);
        return this;
    }

    @Override
    public MutableBag<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public MutableBag<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }
}
