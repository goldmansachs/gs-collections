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

package com.gs.collections.impl.bag.sorted.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.bag.ParallelBag;
import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.collection.mutable.AbstractSynchronizedMutableCollection;
import com.gs.collections.impl.collection.mutable.SynchronizedCollectionSerializationProxy;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A synchronized view of a {@link MutableSortedBag}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableSortedBag#asSynchronized()
 */
@ThreadSafe
public class SynchronizedSortedBag<T>
        extends AbstractSynchronizedMutableCollection<T>
        implements MutableSortedBag<T>, Serializable
{
    SynchronizedSortedBag(MutableSortedBag<T> bag)
    {
        super(bag);
    }

    SynchronizedSortedBag(MutableSortedBag<T> bag, Object newLock)
    {
        super(bag, newLock);
    }

    public static <E> SynchronizedSortedBag<E> of(MutableSortedBag<E> bag)
    {
        return new SynchronizedSortedBag<E>(bag);
    }

    public static <E> MutableSortedBag<E> of(MutableSortedBag<E> bag, Object lock)
    {
        return new SynchronizedSortedBag<E>(bag, lock);
    }

    @GuardedBy("getLock()")
    private MutableSortedBag<T> getSortedBag()
    {
        return (MutableSortedBag<T>) this.getCollection();
    }

    public void addOccurrences(T item, int occurrences)
    {
        synchronized (this.getLock())
        {
            this.getSortedBag().addOccurrences(item, occurrences);
        }
    }

    public boolean removeOccurrences(Object item, int occurrences)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().removeOccurrences(item, occurrences);
        }
    }

    public MutableSortedBag<T> selectByOccurrences(IntPredicate predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().selectByOccurrences(predicate);
        }
    }

    public MutableList<ObjectIntPair<T>> topOccurrences(int count)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().topOccurrences(count);
        }
    }

    public MutableList<ObjectIntPair<T>> bottomOccurrences(int count)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().bottomOccurrences(count);
        }
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.getSortedBag().forEachWithOccurrences(procedure);
        }
    }

    public int occurrencesOf(Object item)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().occurrencesOf(item);
        }
    }

    public int sizeDistinct()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().sizeDistinct();
        }
    }

    public String toStringOfItemToCount()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().toStringOfItemToCount();
        }
    }

    public MutableSortedMap<T, Integer> toMapOfItemToCount()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().toMapOfItemToCount();
        }
    }

    @Override
    public MutableSortedBag<T> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableSortedBag.of(this);
        }
    }

    @Override
    public ImmutableSortedBag<T> toImmutable()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().toImmutable();
        }
    }

    public MutableStack<T> toStack()
    {
        synchronized (this.getLock())
        {
            return ArrayStack.newStack(this);
        }
    }

    @Override
    public MutableSortedBag<T> asSynchronized()
    {
        return this;
    }

    @Override
    public MutableSortedBag<T> clone()
    {
        synchronized (this.getLock())
        {
            return SynchronizedSortedBag.of(this.getSortedBag().clone());
        }
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collect(function);
        }
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectBoolean(booleanFunction);
        }
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectByte(byteFunction);
        }
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectChar(charFunction);
        }
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectDouble(doubleFunction);
        }
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectFloat(floatFunction);
        }
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectInt(intFunction);
        }
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectLong(longFunction);
        }
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectShort(shortFunction);
        }
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().flatCollect(function);
        }
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectIf(predicate, function);
        }
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().collectWith(function, parameter);
        }
    }

    @Override
    public <V> MutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().groupBy(function);
        }
    }

    @Override
    public <V> MutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().groupByEach(function);
        }
    }

    @Override
    public MutableSortedBag<T> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().newEmpty().asSynchronized();
        }
    }

    @Override
    public MutableSortedBag<T> reject(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().reject(predicate);
        }
    }

    @Override
    public <P> MutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().rejectWith(predicate, parameter);
        }
    }

    @Override
    public MutableSortedBag<T> tap(Procedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.forEach(procedure);
            return this;
        }
    }

    @Override
    public MutableSortedBag<T> select(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().select(predicate);
        }
    }

    @Override
    public <P> MutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().selectWith(predicate, parameter);
        }
    }

    @Override
    public PartitionMutableSortedBag<T> partition(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().partition(predicate);
        }
    }

    @Override
    public <P> PartitionMutableSortedBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().partitionWith(predicate, parameter);
        }
    }

    public PartitionMutableSortedBag<T> partitionWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().partitionWhile(predicate);
        }
    }

    @Override
    public <S> MutableSortedBag<S> selectInstancesOf(Class<S> clazz)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().selectInstancesOf(clazz);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().equals(obj);
        }
    }

    @Override
    public int hashCode()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().hashCode();
        }
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().zip(that);
        }
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().zip(that, target);
        }
    }

    @Override
    public MutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().zipWithIndex();
        }
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().zipWithIndex(target);
        }
    }

    public MutableSortedBag<T> takeWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().takeWhile(predicate);
        }
    }

    public MutableSortedBag<T> dropWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().dropWhile(predicate);
        }
    }

    public MutableSortedSet<T> distinct()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().distinct();
        }
    }

    public Comparator<? super T> comparator()
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().comparator();
        }
    }

    public int compareTo(SortedBag<T> o)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().compareTo(o);
        }
    }

    public int indexOf(Object object)
    {
        synchronized (this.getLock())
        {
            return this.getSortedBag().indexOf(object);
        }
    }

    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.getSortedBag().forEach(startIndex, endIndex, procedure);
        }
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        synchronized (this.getLock())
        {
            this.getSortedBag().forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
        }
    }

    @Override
    public MutableSortedBag<T> with(T element)
    {
        this.add(element);
        return this;
    }

    @Override
    public MutableSortedBag<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public MutableSortedBag<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public MutableSortedBag<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    protected Object writeReplace()
    {
        return new SynchronizedCollectionSerializationProxy<T>(this.getSortedBag());
    }

    public ParallelBag<T> asParallel(ExecutorService executorService, int batchSize)
    {
        throw new UnsupportedOperationException("asParallel() method is not supported for " + this.getClass().getSimpleName() + '.');
    }
}
