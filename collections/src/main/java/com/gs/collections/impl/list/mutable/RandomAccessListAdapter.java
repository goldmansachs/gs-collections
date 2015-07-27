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

package com.gs.collections.impl.list.mutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectBooleanProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectByteProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectCharProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectDoubleProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectFloatProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectIntProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectLongProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectShortProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import com.gs.collections.impl.utility.internal.RandomAccessListIterate;

/**
 * This class provides a MutableList wrapper around a JDK Collections List interface instance.  All of the MutableList
 * interface methods are supported in addition to the JDK List interface methods.
 * <p>
 * To create a new wrapper around an existing List instance, use the {@link #adapt(List)} factory method.
 */
public final class RandomAccessListAdapter<T>
        extends AbstractListAdapter<T>
        implements RandomAccess, Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<T> delegate;

    RandomAccessListAdapter(List<T> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("RandomAccessListAdapter may not wrap null");
        }
        if (!(newDelegate instanceof RandomAccess))
        {
            throw new IllegalArgumentException("RandomAccessListAdapter may not wrap a non RandomAccess list");
        }
        this.delegate = newDelegate;
    }

    @Override
    protected List<T> getDelegate()
    {
        return this.delegate;
    }

    public static <E> MutableList<E> adapt(List<E> list)
    {
        if (list instanceof MutableList)
        {
            return (MutableList<E>) list;
        }
        if (list instanceof ArrayList)
        {
            return ArrayListAdapter.adapt((ArrayList<E>) list);
        }
        return new RandomAccessListAdapter<E>(list);
    }

    public ImmutableList<T> toImmutable()
    {
        return Lists.immutable.withAll(this.delegate);
    }

    public MutableList<T> asUnmodifiable()
    {
        return UnmodifiableMutableList.of(this);
    }

    public MutableList<T> asSynchronized()
    {
        return SynchronizedMutableList.of(this);
    }

    @Override
    public MutableList<T> clone()
    {
        return FastList.newList(this.delegate);
    }

    /**
     * @deprecated use {@link FastList#newList()} instead (inlineable)
     */
    @Deprecated
    public MutableList<T> newEmpty()
    {
        return Lists.mutable.empty();
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
        this.each(procedure);
    }

    @Override
    public void each(Procedure<? super T> procedure)
    {
        RandomAccessListIterate.forEach(this.delegate, procedure);
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        ListIterate.reverseForEach(this.delegate, procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        RandomAccessListIterate.forEachWithIndex(this.delegate, objectIntProcedure);
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        RandomAccessListIterate.forEachWithIndex(this.delegate, fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.detect(this.delegate, predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.detect(predicate);
        return result == null ? function.value() : result;
    }

    public int detectIndex(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.detectIndex(this.delegate, predicate);
    }

    public int detectLastIndex(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.detectLastIndex(this.delegate, predicate);
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.count(this.delegate, predicate);
    }

    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return RandomAccessListIterate.corresponds(this.delegate, other, predicate);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.anySatisfy(this.delegate, predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.allSatisfy(this.delegate, predicate);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.noneSatisfy(this.delegate, predicate);
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return RandomAccessListIterate.injectInto(injectedValue, this.delegate, function);
    }

    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        RandomAccessListIterate.forEach(this.delegate, fromIndex, toIndex, procedure);
    }

    public RandomAccessListAdapter<T> sortThis(Comparator<? super T> comparator)
    {
        Iterate.sortThis(this.delegate, comparator);
        return this;
    }

    public RandomAccessListAdapter<T> sortThis()
    {
        return this.sortThis(Comparators.naturalOrder());
    }

    public RandomAccessListAdapter<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public RandomAccessListAdapter<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public RandomAccessListAdapter<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public RandomAccessListAdapter<T> with(T... elements)
    {
        ArrayIterate.forEach(elements, CollectionAddProcedure.on(this.delegate));
        return this;
    }

    public RandomAccessListAdapter<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public RandomAccessListAdapter<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public RandomAccessListAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    @Override
    public MutableList<T> select(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.select(this.delegate, predicate, FastList.<T>newList());
    }

    @Override
    public MutableList<T> reject(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.reject(this.delegate, predicate, FastList.<T>newList());
    }

    @Override
    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.partition(this.delegate, predicate);
    }

    @Override
    public <P> PartitionMutableList<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return RandomAccessListIterate.partitionWith(this.delegate, predicate, parameter);
    }

    @Override
    public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
    {
        return RandomAccessListIterate.selectInstancesOf(this.delegate, clazz);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return RandomAccessListIterate.collect(this.delegate, function, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        BooleanArrayList result = new BooleanArrayList(this.size());
        this.forEach(new CollectBooleanProcedure<T>(booleanFunction, result));
        return result;
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        ByteArrayList result = new ByteArrayList(this.size());
        this.forEach(new CollectByteProcedure<T>(byteFunction, result));
        return result;
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        CharArrayList result = new CharArrayList(this.size());
        this.forEach(new CollectCharProcedure<T>(charFunction, result));
        return result;
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        DoubleArrayList result = new DoubleArrayList(this.size());
        this.forEach(new CollectDoubleProcedure<T>(doubleFunction, result));
        return result;
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        FloatArrayList result = new FloatArrayList(this.size());
        this.forEach(new CollectFloatProcedure<T>(floatFunction, result));
        return result;
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        IntArrayList result = new IntArrayList(this.size());
        this.forEach(new CollectIntProcedure<T>(intFunction, result));
        return result;
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        LongArrayList result = new LongArrayList(this.size());
        this.forEach(new CollectLongProcedure<T>(longFunction, result));
        return result;
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        ShortArrayList result = new ShortArrayList(this.size());
        this.forEach(new CollectShortProcedure<T>(shortFunction, result));
        return result;
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return RandomAccessListIterate.flatCollect(this.delegate, function, FastList.<V>newList(this.delegate.size()));
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return RandomAccessListIterate.collectIf(this.delegate, predicate, function, FastList.<V>newList());
    }

    @Override
    public <V> FastListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return RandomAccessListIterate.groupBy(this.delegate, function, FastListMultimap.<V, T>newMultimap());
    }

    @Override
    public <V> FastListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return RandomAccessListIterate.groupByEach(this.delegate, function, FastListMultimap.<V, T>newMultimap());
    }

    @Override
    public <P> MutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return RandomAccessListIterate.selectWith(this.delegate, predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P> MutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return RandomAccessListIterate.rejectWith(this.delegate, predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return RandomAccessListIterate.collectWith(this.delegate, function, parameter, FastList.<V>newList(this.delegate.size()));
    }

    public MutableList<T> distinct()
    {
        return RandomAccessListIterate.distinct(this.delegate);
    }

    public MutableList<T> distinct(HashingStrategy<? super T> hashingStrategy)
    {
        return RandomAccessListIterate.distinct(this.delegate, hashingStrategy);
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return RandomAccessListIterate.zip(this.delegate, that, FastList.<Pair<T, S>>newList(this.delegate.size()));
    }

    @Override
    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        return RandomAccessListIterate.zipWithIndex(this.delegate, FastList.<Pair<T, Integer>>newList(this.delegate.size()));
    }

    public MutableList<T> take(int count)
    {
        return RandomAccessListIterate.take(this.delegate, count);
    }

    public MutableList<T> takeWhile(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.takeWhile(this.delegate, predicate);
    }

    public MutableList<T> drop(int count)
    {
        return RandomAccessListIterate.drop(this.delegate, count);
    }

    public MutableList<T> dropWhile(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.dropWhile(this.delegate, predicate);
    }

    public PartitionMutableList<T> partitionWhile(Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.partitionWhile(this.delegate, predicate);
    }
}
