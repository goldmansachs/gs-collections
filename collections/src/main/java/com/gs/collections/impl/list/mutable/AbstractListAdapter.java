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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ExecutorService;

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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.collection.mutable.AbstractCollectionAdapter;
import com.gs.collections.impl.lazy.ReverseIterable;
import com.gs.collections.impl.lazy.parallel.list.ListIterableParallelIterable;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.ListIterate;

@SuppressWarnings("AbstractMethodOverridesConcreteMethod")
public abstract class AbstractListAdapter<T>
        extends AbstractCollectionAdapter<T>
        implements MutableList<T>
{
    @Override
    public MutableList<T> clone()
    {
        try
        {
            return (MutableList<T>) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    @Override
    protected abstract List<T> getDelegate();

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        return this.getDelegate().addAll(index, collection);
    }

    public MutableList<T> toReversed()
    {
        return FastList.newList(this).reverseThis();
    }

    public MutableList<T> reverseThis()
    {
        Collections.reverse(this);
        return this;
    }

    public MutableList<T> shuffleThis()
    {
        Collections.shuffle(this);
        return this;
    }

    public MutableList<T> shuffleThis(Random rnd)
    {
        Collections.shuffle(this, rnd);
        return this;
    }

    public T get(int index)
    {
        return this.getDelegate().get(index);
    }

    public T set(int index, T element)
    {
        return this.getDelegate().set(index, element);
    }

    public void add(int index, T element)
    {
        this.getDelegate().add(index, element);
    }

    public T remove(int index)
    {
        return this.getDelegate().remove(index);
    }

    public int indexOf(Object o)
    {
        return this.getDelegate().indexOf(o);
    }

    public int lastIndexOf(Object o)
    {
        return this.getDelegate().lastIndexOf(o);
    }

    public ListIterator<T> listIterator()
    {
        return this.getDelegate().listIterator();
    }

    public ListIterator<T> listIterator(int index)
    {
        return this.getDelegate().listIterator(index);
    }

    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
    {
        return this.sortThis(Comparators.byFunction(function));
    }

    public MutableList<T> sortThisByInt(IntFunction<? super T> function)
    {
        return this.sortThis(Functions.toIntComparator(function));
    }

    public MutableList<T> sortThisByBoolean(BooleanFunction<? super T> function)
    {
        return this.sortThis(Functions.toBooleanComparator(function));
    }

    public MutableList<T> sortThisByChar(CharFunction<? super T> function)
    {
        return this.sortThis(Functions.toCharComparator(function));
    }

    public MutableList<T> sortThisByByte(ByteFunction<? super T> function)
    {
        return this.sortThis(Functions.toByteComparator(function));
    }

    public MutableList<T> sortThisByShort(ShortFunction<? super T> function)
    {
        return this.sortThis(Functions.toShortComparator(function));
    }

    public MutableList<T> sortThisByFloat(FloatFunction<? super T> function)
    {
        return this.sortThis(Functions.toFloatComparator(function));
    }

    public MutableList<T> sortThisByLong(LongFunction<? super T> function)
    {
        return this.sortThis(Functions.toLongComparator(function));
    }

    public MutableList<T> sortThisByDouble(DoubleFunction<? super T> function)
    {
        return this.sortThis(Functions.toDoubleComparator(function));
    }

    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        return ListAdapter.adapt(this.getDelegate().subList(fromIndex, toIndex));
    }

    @Override
    public boolean equals(Object o)
    {
        return this.getDelegate().equals(o);
    }

    @Override
    public int hashCode()
    {
        return this.getDelegate().hashCode();
    }

    public MutableStack<T> toStack()
    {
        return ArrayStack.newStack(this.getDelegate());
    }

    public ReverseIterable<T> asReversed()
    {
        return ReverseIterable.adapt(this);
    }

    public ParallelListIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return new ListIterableParallelIterable<T>(this, executorService, batchSize);
    }

    public int binarySearch(T key, Comparator<? super T> comparator)
    {
        return Collections.binarySearch(this, key, comparator);
    }

    public int binarySearch(T key)
    {
        return Collections.binarySearch((List<? extends Comparable<? super T>>) this, key);
    }

    @Override
    public <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return ListIterate.groupBy(this.getDelegate(), function);
    }

    @Override
    public <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return ListIterate.groupByEach(this.getDelegate(), function);
    }

    @Override
    public MutableList<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public MutableList<T> select(Predicate<? super T> predicate)
    {
        return ListIterate.select(this.getDelegate(), predicate);
    }

    @Override
    public MutableList<T> reject(Predicate<? super T> predicate)
    {
        return ListIterate.reject(this.getDelegate(), predicate);
    }

    @Override
    public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
    {
        return ListIterate.selectInstancesOf(this.getDelegate(), clazz);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return ListIterate.collect(this.getDelegate(), function);
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return ListIterate.collectBoolean(this.getDelegate(), booleanFunction);
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return ListIterate.collectByte(this.getDelegate(), byteFunction);
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return ListIterate.collectChar(this.getDelegate(), charFunction);
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return ListIterate.collectDouble(this.getDelegate(), doubleFunction);
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return ListIterate.collectFloat(this.getDelegate(), floatFunction);
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return ListIterate.collectInt(this.getDelegate(), intFunction);
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return ListIterate.collectLong(this.getDelegate(), longFunction);
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return ListIterate.collectShort(this.getDelegate(), shortFunction);
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return ListIterate.flatCollect(this.getDelegate(), function);
    }

    @Override
    public <V> MutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return ListIterate.collectIf(this.getDelegate(), predicate, function);
    }

    @Override
    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return ListIterate.partition(this.getDelegate(), predicate);
    }

    @Override
    public <P> PartitionMutableList<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.partitionWith(this.getDelegate(), predicate, parameter);
    }

    @Override
    public <P> MutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.selectWith(this.getDelegate(), predicate, parameter);
    }

    @Override
    public <P> MutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.rejectWith(this.getDelegate(), predicate, parameter);
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return ListIterate.collectWith(this.getDelegate(), function, parameter);
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return ListIterate.zip(this.getDelegate(), that);
    }

    @Override
    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        return ListIterate.zipWithIndex(this.getDelegate());
    }
}
