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

import java.util.Comparator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.bag.sorted.ParallelSortedBag;
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
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.AbstractMutableBagIterable;
import com.gs.collections.impl.factory.SortedBags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.partition.bag.sorted.PartitionTreeBag;
import com.gs.collections.impl.utility.internal.IterableIterate;

public abstract class AbstractMutableSortedBag<T>
        extends AbstractMutableBagIterable<T>
        implements MutableSortedBag<T>
{
    @SuppressWarnings("AbstractMethodOverridesAbstractMethod")
    public abstract MutableSortedBag<T> clone();

    public ImmutableSortedBag<T> toImmutable()
    {
        return SortedBags.immutable.ofSortedBag(this);
    }

    public UnmodifiableSortedBag<T> asUnmodifiable()
    {
        return UnmodifiableSortedBag.of(this);
    }

    public MutableSortedBag<T> asSynchronized()
    {
        return SynchronizedSortedBag.of(this);
    }

    public MutableSortedBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public MutableSortedMap<T, Integer> toMapOfItemToCount()
    {
        final MutableSortedMap<T, Integer> map = TreeSortedMap.newMap(this.comparator());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T item, int count)
            {
                map.put(item, count);
            }
        });
        return map;
    }

    public <S> MutableSortedBag<S> selectInstancesOf(final Class<S> clazz)
    {
        Comparator<? super S> comparator = (Comparator<? super S>) this.comparator();
        final MutableSortedBag<S> result = TreeBag.newBag(comparator);
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (clazz.isInstance(each))
                {
                    result.addOccurrences(clazz.cast(each), occurrences);
                }
            }
        });
        return result;
    }

    public MutableSortedBag<T> takeWhile(Predicate<? super T> predicate)
    {
        MutableSortedBag<T> result = TreeBag.newBag(this.comparator());
        return IterableIterate.takeWhile(this, predicate, result);
    }

    public MutableSortedBag<T> dropWhile(Predicate<? super T> predicate)
    {
        MutableSortedBag<T> result = TreeBag.newBag(this.comparator());
        return IterableIterate.dropWhile(this, predicate, result);
    }

    public MutableSortedBag<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, TreeBag.newBag(this.comparator()));
    }

    public <P> MutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.selectWith(predicate, parameter, TreeBag.newBag(this.comparator()));
    }

    public MutableSortedBag<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, TreeBag.newBag(this.comparator()));
    }

    public <P> MutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.rejectWith(predicate, parameter, TreeBag.newBag(this.comparator()));
    }

    public PartitionMutableSortedBag<T> partition(final Predicate<? super T> predicate)
    {
        final PartitionMutableSortedBag<T> result = new PartitionTreeBag<T>(this.comparator());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                MutableSortedBag<T> bucket = predicate.accept(each) ? result.getSelected() : result.getRejected();
                bucket.addOccurrences(each, index);
            }
        });
        return result;
    }

    public <P> PartitionMutableSortedBag<T> partitionWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final PartitionMutableSortedBag<T> result = new PartitionTreeBag<T>(this.comparator());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                MutableSortedBag<T> bucket = predicate.accept(each, parameter) ? result.getSelected() : result.getRejected();
                bucket.addOccurrences(each, index);
            }
        });
        return result;
    }

    public PartitionMutableSortedBag<T> partitionWhile(Predicate<? super T> predicate)
    {
        PartitionTreeBag<T> result = new PartitionTreeBag<T>(this.comparator());
        return IterableIterate.partitionWhile(this, predicate, result);
    }

    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, FastList.<V>newList(this.size()));
    }

    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collectWith(function, parameter, FastList.<V>newList());
    }

    public <V> MutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, FastList.<V>newList());
    }

    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, FastList.<V>newList());
    }

    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, new BooleanArrayList());
    }

    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, new ByteArrayList());
    }

    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, new CharArrayList());
    }

    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, new DoubleArrayList());
    }

    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, new FloatArrayList());
    }

    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, new IntArrayList());
    }

    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, new LongArrayList());
    }

    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, new ShortArrayList());
    }

    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<T, S>>newList());
    }

    public MutableSortedBag<T> toReversed()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".toReversed() not implemented yet");
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".reverseForEach() not implemented yet");
    }

    public LazyIterable<T> asReversed()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asReversed() not implemented yet");
    }

    public int detectLastIndex(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".detectLastIndex() not implemented yet");
    }

    @Beta
    public ParallelSortedBag<T> asParallel(ExecutorService executorService, int batchSize)
    {
        if (executorService == null)
        {
            throw new NullPointerException();
        }
        if (batchSize < 1)
        {
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asParallel() not implemented yet");
    }
}
