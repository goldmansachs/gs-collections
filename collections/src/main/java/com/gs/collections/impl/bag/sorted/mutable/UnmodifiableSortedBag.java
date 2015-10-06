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

package com.gs.collections.impl.bag.sorted.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;

import com.gs.collections.api.LazyIterable;
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
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.collection.mutable.AbstractUnmodifiableMutableCollection;
import com.gs.collections.impl.collection.mutable.UnmodifiableCollectionSerializationProxy;

/**
 * An unmodifiable view of a SortedBag.
 *
 * @see MutableSortedBag#asUnmodifiable()
 * @since 4.2
 */
public class UnmodifiableSortedBag<T>
        extends AbstractUnmodifiableMutableCollection<T>
        implements MutableSortedBag<T>, Serializable
{
    UnmodifiableSortedBag(MutableSortedBag<? extends T> sortedBag)
    {
        super(sortedBag);
    }

    /**
     * This method will take a MutableSortedBag and wrap it directly in a UnmodifiableSortedBag.
     */
    public static <E, S extends MutableSortedBag<E>> UnmodifiableSortedBag<E> of(S bag)
    {
        if (bag == null)
        {
            throw new IllegalArgumentException("cannot create an UnmodifiableSortedBag for null");
        }
        return new UnmodifiableSortedBag<E>(bag);
    }

    protected MutableSortedBag<T> getSortedBag()
    {
        return (MutableSortedBag<T>) this.getMutableCollection();
    }

    @Override
    public MutableSortedBag<T> asUnmodifiable()
    {
        return this;
    }

    @Override
    public MutableSortedBag<T> asSynchronized()
    {
        return SynchronizedSortedBag.of(this);
    }

    @Override
    public ImmutableSortedBag<T> toImmutable()
    {
        return this.getSortedBag().toImmutable();
    }

    @Override
    public UnmodifiableSortedBag<T> clone()
    {
        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.getSortedBag().equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.getSortedBag().hashCode();
    }

    @Override
    public MutableSortedBag<T> newEmpty()
    {
        return this.getSortedBag().newEmpty();
    }

    public void addOccurrences(T item, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot call addOccurrences() on " + this.getClass().getSimpleName());
    }

    public boolean removeOccurrences(Object item, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot call removeOccurences() on " + this.getClass().getSimpleName());
    }

    public boolean setOccurrences(T item, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot call setOccurrences() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSortedBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public MutableSortedBag<T> select(Predicate<? super T> predicate)
    {
        return this.getSortedBag().select(predicate);
    }

    @Override
    public <P> MutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getSortedBag().selectWith(predicate, parameter);
    }

    @Override
    public MutableSortedBag<T> reject(Predicate<? super T> predicate)
    {
        return this.getSortedBag().reject(predicate);
    }

    @Override
    public <P> MutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getSortedBag().rejectWith(predicate, parameter);
    }

    @Override
    public PartitionMutableSortedBag<T> partition(Predicate<? super T> predicate)
    {
        return this.getSortedBag().partition(predicate);
    }

    @Override
    public <P> PartitionMutableSortedBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getSortedBag().partitionWith(predicate, parameter);
    }

    public PartitionMutableSortedBag<T> partitionWhile(Predicate<? super T> predicate)
    {
        return this.getSortedBag().partition(predicate);
    }

    public int sizeDistinct()
    {
        return this.getSortedBag().sizeDistinct();
    }

    public int occurrencesOf(Object item)
    {
        return this.getSortedBag().occurrencesOf(item);
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
        this.getSortedBag().forEachWithOccurrences(procedure);
    }

    public MutableSortedMap<T, Integer> toMapOfItemToCount()
    {
        return this.getSortedBag().toMapOfItemToCount();
    }

    public String toStringOfItemToCount()
    {
        return this.getSortedBag().toStringOfItemToCount();
    }

    public MutableSortedBag<T> selectByOccurrences(IntPredicate predicate)
    {
        return this.getSortedBag().selectByOccurrences(predicate);
    }

    public MutableList<ObjectIntPair<T>> topOccurrences(int count)
    {
        return this.getSortedBag().topOccurrences(count);
    }

    public MutableList<ObjectIntPair<T>> bottomOccurrences(int count)
    {
        return this.getSortedBag().bottomOccurrences(count);
    }

    @Override
    public <S> MutableSortedBag<S> selectInstancesOf(Class<S> clazz)
    {
        return this.getSortedBag().selectInstancesOf(clazz);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.getSortedBag().collect(function);
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.getSortedBag().collectBoolean(booleanFunction);
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.getSortedBag().collectByte(byteFunction);
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return this.getSortedBag().collectChar(charFunction);
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.getSortedBag().collectDouble(doubleFunction);
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.getSortedBag().collectFloat(floatFunction);
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return this.getSortedBag().collectInt(intFunction);
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return this.getSortedBag().collectLong(longFunction);
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.getSortedBag().collectShort(shortFunction);
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getSortedBag().flatCollect(function);
    }

    public MutableSortedSet<T> distinct()
    {
        return this.getSortedBag().distinct();
    }

    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return this.getSortedBag().corresponds(other, predicate);
    }

    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
        this.getSortedBag().forEach(startIndex, endIndex, procedure);
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.getSortedBag().forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
    }

    public MutableStack<T> toStack()
    {
        return this.getSortedBag().toStack();
    }

    public int indexOf(Object object)
    {
        return this.getSortedBag().indexOf(object);
    }

    public MutableSortedBag<T> takeWhile(Predicate<? super T> predicate)
    {
        return this.getSortedBag().takeWhile(predicate);
    }

    public MutableSortedBag<T> dropWhile(Predicate<? super T> predicate)
    {
        return this.getSortedBag().dropWhile(predicate);
    }

    @Override
    public <P, A> MutableList<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getSortedBag().collectWith(function, parameter);
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getSortedBag().collectIf(predicate, function);
    }

    public int detectIndex(Predicate<? super T> predicate)
    {
        return this.getSortedBag().detectIndex(predicate);
    }

    @Override
    public <V> MutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.getSortedBag().groupBy(function);
    }

    @Override
    public <V> MutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getSortedBag().groupByEach(function);
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.getSortedBag().zip(that);
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return this.getSortedBag().zip(that, target);
    }

    @Override
    public MutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.getSortedBag().zipWithIndex();
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return this.getSortedBag().zipWithIndex(target);
    }

    public MutableSortedBag<T> toReversed()
    {
        return this.getSortedBag().toReversed();
    }

    public MutableSortedBag<T> take(int count)
    {
        return this.getSortedBag().take(count);
    }

    public MutableSortedBag<T> drop(int count)
    {
        return this.getSortedBag().drop(count);
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        this.getSortedBag().reverseForEach(procedure);
    }

    public LazyIterable<T> asReversed()
    {
        return this.getSortedBag().asReversed();
    }

    public int detectLastIndex(Predicate<? super T> predicate)
    {
        return this.getSortedBag().detectLastIndex(predicate);
    }

    public Comparator<? super T> comparator()
    {
        return this.getSortedBag().comparator();
    }

    @Override
    public MutableSortedBag<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSortedBag<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSortedBag<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSortedBag<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
    }

    public int compareTo(SortedBag<T> o)
    {
        return this.getSortedBag().compareTo(o);
    }

    protected Object writeReplace()
    {
        return new UnmodifiableCollectionSerializationProxy<T>(this.getSortedBag());
    }
}
