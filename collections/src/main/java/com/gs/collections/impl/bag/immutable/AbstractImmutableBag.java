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

package com.gs.collections.impl.bag.immutable;

import java.util.Iterator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.bag.primitive.ImmutableByteBag;
import com.gs.collections.api.bag.primitive.ImmutableCharBag;
import com.gs.collections.api.bag.primitive.ImmutableDoubleBag;
import com.gs.collections.api.bag.primitive.ImmutableFloatBag;
import com.gs.collections.api.bag.primitive.ImmutableIntBag;
import com.gs.collections.api.bag.primitive.ImmutableLongBag;
import com.gs.collections.api.bag.primitive.ImmutableShortBag;
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
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.partition.bag.PartitionHashBag;

/**
 * @since 1.0
 */
public abstract class AbstractImmutableBag<T>
        extends AbstractImmutableBagIterable<T>
        implements ImmutableBag<T>
{
    public ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this.reject(Predicates.in(elements));
    }

    public ImmutableBag<T> toImmutable()
    {
        return this;
    }

    public ImmutableBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public <P> ImmutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    public <P> ImmutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    public PartitionImmutableBag<T> partition(final Predicate<? super T> predicate)
    {
        final PartitionMutableBag<T> partitionMutableBag = new PartitionHashBag<T>();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                MutableBag<T> bucket = predicate.accept(each)
                        ? partitionMutableBag.getSelected()
                        : partitionMutableBag.getRejected();
                bucket.addOccurrences(each, occurrences);
            }
        });
        return partitionMutableBag.toImmutable();
    }

    public <P> PartitionImmutableBag<T> partitionWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final PartitionMutableBag<T> partitionMutableBag = new PartitionHashBag<T>();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                MutableBag<T> bucket = predicate.accept(each, parameter)
                        ? partitionMutableBag.getSelected()
                        : partitionMutableBag.getRejected();
                bucket.addOccurrences(each, occurrences);
            }
        });
        return partitionMutableBag.toImmutable();
    }

    public <P, V> ImmutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public ImmutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, new BooleanHashBag()).toImmutable();
    }

    public ImmutableByteBag collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, new ByteHashBag()).toImmutable();
    }

    public ImmutableCharBag collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, new CharHashBag()).toImmutable();
    }

    public ImmutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, new DoubleHashBag()).toImmutable();
    }

    public ImmutableFloatBag collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, new FloatHashBag()).toImmutable();
    }

    public ImmutableIntBag collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, new IntHashBag()).toImmutable();
    }

    public ImmutableLongBag collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, new LongHashBag()).toImmutable();
    }

    public ImmutableShortBag collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, new ShortHashBag()).toImmutable();
    }

    public ImmutableList<ObjectIntPair<T>> topOccurrences(int n)
    {
        return this.occurrencesSortingBy(n,
                new IntFunction<ObjectIntPair<T>>()
                {
                    public int intValueOf(ObjectIntPair<T> item)
                    {
                        return -item.getTwo();
                    }
                },
                Lists.fixedSize.<ObjectIntPair<T>>empty()
        ).toImmutable();
    }

    public ImmutableList<ObjectIntPair<T>> bottomOccurrences(int n)
    {
        return this.occurrencesSortingBy(n,
                new IntFunction<ObjectIntPair<T>>()
                {
                    public int intValueOf(ObjectIntPair<T> item)
                    {
                        return item.getTwo();
                    }
                },
                Lists.fixedSize.<ObjectIntPair<T>>empty()
        ).toImmutable();
    }

    public <V> ImmutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V, T>newMap()).toImmutable();
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        Iterator<T> iterator = this.iterator();
        MutableList<RichIterable<T>> result = Lists.mutable.empty();
        while (iterator.hasNext())
        {
            MutableCollection<T> batch = Bags.mutable.empty();
            for (int i = 0; i < size && iterator.hasNext(); i++)
            {
                batch.add(iterator.next());
            }
            result.add(batch.toImmutable());
        }
        return result.toImmutable();
    }
}
