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

package com.gs.collections.impl.bag.immutable;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.bag.primitive.ImmutableByteBag;
import com.gs.collections.api.bag.primitive.ImmutableCharBag;
import com.gs.collections.api.bag.primitive.ImmutableDoubleBag;
import com.gs.collections.api.bag.primitive.ImmutableFloatBag;
import com.gs.collections.api.bag.primitive.ImmutableIntBag;
import com.gs.collections.api.bag.primitive.ImmutableLongBag;
import com.gs.collections.api.bag.primitive.ImmutableShortBag;
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
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
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
import com.gs.collections.impl.block.procedure.PartitionPredicate2Procedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.partition.bag.PartitionHashBag;

/**
 * @since 1.0
 */
public abstract class AbstractImmutableBag<T>
        extends AbstractImmutableCollection<T>
        implements ImmutableBag<T>
{
    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return Bags.mutable.of();
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

    public PartitionImmutableBag<T> partition(Predicate<? super T> predicate)
    {
        PartitionMutableBag<T> partitionMutableBag = new PartitionHashBag<T>();
        this.forEach(new PartitionProcedure<T>(predicate, partitionMutableBag));
        return partitionMutableBag.toImmutable();
    }

    public <P> PartitionImmutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        PartitionMutableBag<T> partitionMutableBag = new PartitionHashBag<T>();
        this.forEach(new PartitionPredicate2Procedure<T, P>(predicate, parameter, partitionMutableBag));
        return partitionMutableBag.toImmutable();
    }

    public ImmutableBooleanBag collectBoolean(final BooleanFunction<? super T> booleanFunction)
    {
        final BooleanHashBag result = new BooleanHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(booleanFunction.booleanValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public ImmutableByteBag collectByte(final ByteFunction<? super T> byteFunction)
    {
        final ByteHashBag result = new ByteHashBag(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(byteFunction.byteValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public ImmutableCharBag collectChar(final CharFunction<? super T> charFunction)
    {
        final CharHashBag result = new CharHashBag(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(charFunction.charValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public ImmutableDoubleBag collectDouble(final DoubleFunction<? super T> doubleFunction)
    {
        final DoubleHashBag result = new DoubleHashBag(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(doubleFunction.doubleValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public ImmutableFloatBag collectFloat(final FloatFunction<? super T> floatFunction)
    {
        final FloatHashBag result = new FloatHashBag(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(floatFunction.floatValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public ImmutableIntBag collectInt(final IntFunction<? super T> intFunction)
    {
        final IntHashBag result = new IntHashBag(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(intFunction.intValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public ImmutableLongBag collectLong(final LongFunction<? super T> longFunction)
    {
        final LongHashBag result = new LongHashBag(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(longFunction.longValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public ImmutableShortBag collectShort(final ShortFunction<? super T> shortFunction)
    {
        final ShortHashBag result = new ShortHashBag(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(shortFunction.shortValueOf(each), occurrences);
            }
        });
        return result.toImmutable();
    }

    public <P, V> ImmutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public String toStringOfItemToCount()
    {
        if (this.isEmpty())
        {
            return "{}";
        }
        final StringBuilder builder = new StringBuilder().append('{');
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                builder.append(each);
                builder.append('=');
                builder.append(occurrences);
                builder.append(", ");
            }
        });
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        return builder.append('}').toString();
    }
}
