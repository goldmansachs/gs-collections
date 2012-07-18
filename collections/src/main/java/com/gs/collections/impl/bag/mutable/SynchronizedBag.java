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

package com.gs.collections.impl.bag.mutable;

import java.util.Collection;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollection;
import com.gs.collections.impl.factory.Bags;
import net.jcip.annotations.GuardedBy;

/**
 * A synchronized view of a bag.
 *
 * @see MutableBag#asSynchronized()
 * @since 1.0
 */
public class SynchronizedBag<E>
        extends SynchronizedMutableCollection<E>
        implements MutableBag<E>
{
    private static final long serialVersionUID = 1L;

    public SynchronizedBag(MutableBag<E> bag)
    {
        super(bag);
    }

    public SynchronizedBag(MutableBag<E> bag, Object newLock)
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
    private MutableBag<E> getMutableBag()
    {
        return (MutableBag<E>) this.getCollection();
    }

    @Override
    public MutableBag<E> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableBag.of(this);
        }
    }

    @Override
    public ImmutableBag<E> toImmutable()
    {
        return Bags.immutable.ofAll(this);
    }

    @Override
    public MutableBag<E> asSynchronized()
    {
        return this;
    }

    @Override
    public <V> MutableBag<V> collect(Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().collect(function);
        }
    }

    @Override
    public <V> MutableBag<V> flatCollect(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().flatCollect(function);
        }
    }

    @Override
    public <V> MutableBag<V> collectIf(
            Predicate<? super E> predicate,
            Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().collectIf(predicate, function);
        }
    }

    @Override
    public <P, V> MutableBag<V> collectWith(Function2<? super E, ? super P, ? extends V> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().collectWith(function, parameter);
        }
    }

    @Override
    public <V> MutableBagMultimap<V, E> groupBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().groupBy(function);
        }
    }

    @Override
    public <V> MutableBagMultimap<V, E> groupByEach(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().groupByEach(function);
        }
    }

    @Override
    public MutableBag<E> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().newEmpty();
        }
    }

    @Override
    public MutableBag<E> reject(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().reject(predicate);
        }
    }

    @Override
    public <P> MutableBag<E> rejectWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().rejectWith(predicate, parameter);
        }
    }

    @Override
    public MutableBag<E> select(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().select(predicate);
        }
    }

    @Override
    public <P> MutableBag<E> selectWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().selectWith(predicate, parameter);
        }
    }

    @Override
    public PartitionMutableBag<E> partition(Predicate<? super E> predicate)
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

    public void addOccurrences(E item, int occurrences)
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

    public void forEachWithOccurrences(ObjectIntProcedure<? super E> objectIntProcedure)
    {
        synchronized (this.getLock())
        {
            this.getMutableBag().forEachWithOccurrences(objectIntProcedure);
        }
    }

    public MutableMap<E, Integer> toMapOfItemToCount()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().toMapOfItemToCount();
        }
    }

    @Override
    public <S> MutableBag<Pair<E, S>> zip(Iterable<S> that)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zip(that);
        }
    }

    @Override
    public <S, R extends Collection<Pair<E, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zip(that, target);
        }
    }

    @Override
    public MutableBag<Pair<E, Integer>> zipWithIndex()
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zipWithIndex();
        }
    }

    @Override
    public <R extends Collection<Pair<E, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableBag().zipWithIndex(target);
        }
    }

    @Override
    public MutableBag<E> with(E element)
    {
        this.add(element);
        return this;
    }

    @Override
    public MutableBag<E> without(E element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public MutableBag<E> withAll(Iterable<? extends E> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public MutableBag<E> withoutAll(Iterable<? extends E> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }
}
