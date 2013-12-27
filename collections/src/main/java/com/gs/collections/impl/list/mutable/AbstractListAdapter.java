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

package com.gs.collections.impl.list.mutable;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

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
import com.gs.collections.api.list.MutableList;
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
import com.gs.collections.impl.block.procedure.primitive.CollectBooleanProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectByteProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectCharProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectDoubleProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectFloatProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectIntProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectLongProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectShortProcedure;
import com.gs.collections.impl.collection.mutable.AbstractCollectionAdapter;
import com.gs.collections.impl.lazy.ReverseIterable;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.stack.mutable.ArrayStack;

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
        int mid = this.size() >> 1;
        int j = this.size() - 1;
        for (int i = 0; i < mid; i++, j--)
        {
            this.swapElements(i, j);
        }
        return this;
    }

    protected void swapElements(int i, int j)
    {
        this.set(i, this.set(j, this.get(i)));
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

    @Override
    public abstract <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    public abstract <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    @Override
    public abstract MutableList<T> select(Predicate<? super T> predicate);

    @Override
    public abstract MutableList<T> reject(Predicate<? super T> predicate);

    @Override
    public abstract <S> MutableList<S> selectInstancesOf(Class<S> clazz);

    @Override
    public abstract <V> MutableList<V> collect(Function<? super T, ? extends V> function);

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
    public abstract <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    @Override
    public abstract <V> MutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    @Override
    public abstract PartitionMutableList<T> partition(Predicate<? super T> predicate);

    @Override
    public abstract <P> MutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    public abstract <P> MutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    public abstract <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    @Override
    public abstract <S> MutableList<Pair<T, S>> zip(Iterable<S> that);

    @Override
    public abstract MutableList<Pair<T, Integer>> zipWithIndex();
}
