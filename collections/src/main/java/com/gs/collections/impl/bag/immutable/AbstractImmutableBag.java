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

package com.gs.collections.impl.bag.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.bag.Bag;
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
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.checked.CheckedObjectIntProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Bags;

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

    public <P> ImmutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    @Override
    public abstract <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that);

    @Override
    public abstract ImmutableBag<Pair<T, Integer>> zipWithIndex();

    @Override
    public abstract PartitionImmutableBag<T> partition(Predicate<? super T> predicate);

    @Override
    public abstract <V> ImmutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    public abstract <V> ImmutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
                builder.append(each.toString());
                builder.append('=');
                builder.append(occurrences);
                builder.append(", ");
            }
        });
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        return builder.append('}').toString();
    }

    protected static class ImmutableBagSerializationProxy<T> implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        private Bag<T> bag;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableBagSerializationProxy()
        {
            // Empty constructor for Externalizable class
        }

        protected ImmutableBagSerializationProxy(Bag<T> bag)
        {
            this.bag = bag;
        }

        public void writeExternal(final ObjectOutput out) throws IOException
        {
            out.writeInt(this.bag.sizeDistinct());
            try
            {
                this.bag.forEachWithOccurrences(new CheckedObjectIntProcedure<T>()
                {
                    @Override
                    public void safeValue(T object, int index) throws IOException
                    {
                        out.writeObject(object);
                        out.writeInt(index);
                    }
                });
            }
            catch (RuntimeException e)
            {
                if (e.getCause() instanceof IOException)
                {
                    throw (IOException) e.getCause();
                }
                throw e;
            }
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
        {
            int size = in.readInt();
            MutableBag<T> deserializedBag = new HashBag<T>(size);

            for (int i = 0; i < size; i++)
            {
                deserializedBag.addOccurrences((T) in.readObject(), in.readInt());
            }

            this.bag = deserializedBag;
        }

        protected Object readResolve()
        {
            return this.bag.toImmutable();
        }
    }
}
