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

import java.util.Collection;
import java.util.Collections;

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
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableByteCollection;
import com.gs.collections.api.collection.primitive.MutableCharCollection;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;
import com.gs.collections.api.collection.primitive.MutableIntCollection;
import com.gs.collections.api.collection.primitive.MutableLongCollection;
import com.gs.collections.api.collection.primitive.MutableShortCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.Counter;
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
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.partition.bag.PartitionHashBag;
import com.gs.collections.impl.tuple.primitive.PrimitiveTuples;
import com.gs.collections.impl.utility.Iterate;

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
        return Bags.mutable.empty();
    }

    public ImmutableBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public <R extends Collection<T>> R select(final Predicate<? super T> predicate, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each))
                {
                    for (int i = 0; i < occurrences; i++)
                    {
                        target.add(each);
                    }
                }
            }
        });
        return target;
    }

    public <P> ImmutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(Predicate2<? super T, ? super P> predicate, P parameter, R target)
    {
        return this.select(Predicates.bind(predicate, parameter), target);
    }

    @Override
    public <R extends Collection<T>> R reject(final Predicate<? super T> predicate, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (!predicate.accept(each))
                {
                    for (int i = 0; i < occurrences; i++)
                    {
                        target.add(each);
                    }
                }
            }
        });
        return target;
    }

    public <P> ImmutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(Predicate2<? super T, ? super P> predicate, P parameter, R target)
    {
        return this.reject(Predicates.bind(predicate, parameter), target);
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

    public <P> PartitionImmutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.partition(Predicates.bind(predicate, parameter));
    }

    @Override
    public <V, R extends Collection<V>> R collect(final Function<? super T, ? extends V> function, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                V value = function.valueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
    }

    public <P, V> ImmutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(final Predicate<? super T> predicate, final Function<? super T, ? extends V> function, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each))
                {
                    V value = function.valueOf(each);
                    for (int i = 0; i < occurrences; i++)
                    {
                        target.add(value);
                    }
                }
            }
        });
        return target;
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(final Function<? super T, ? extends Iterable<V>> function, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                Iterable<V> iterable = function.valueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    Iterate.addAllIterable(iterable, target);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableBooleanCollection> R collectBoolean(final BooleanFunction<? super T> booleanFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                boolean value = booleanFunction.booleanValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableByteCollection> R collectByte(final ByteFunction<? super T> byteFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                byte value = byteFunction.byteValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableCharCollection> R collectChar(final CharFunction<? super T> charFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                char value = charFunction.charValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableDoubleCollection> R collectDouble(final DoubleFunction<? super T> doubleFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                double value = doubleFunction.doubleValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableFloatCollection> R collectFloat(final FloatFunction<? super T> floatFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                Float value = floatFunction.floatValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableIntCollection> R collectInt(final IntFunction<? super T> intFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                int value = intFunction.intValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableLongCollection> R collectLong(final LongFunction<? super T> longFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                long value = longFunction.longValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
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

    @Override
    public <R extends MutableShortCollection> R collectShort(final ShortFunction<? super T> shortFunction, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                short value = shortFunction.shortValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    target.add(value);
                }
            }
        });
        return target;
    }

    @Override
    public int count(final Predicate<? super T> predicate)
    {
        final Counter result = new Counter();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each))
                {
                    result.add(occurrences);
                }
            }
        });
        return result.getCount();
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupBy(
            final Function<? super T, ? extends V> function,
            final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                V value = function.valueOf(each);
                target.putAll(value, Collections.nCopies(occurrences, each));
            }
        });
        return target;
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            final Function<? super T, ? extends Iterable<V>> function,
            final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(final T each, final int occurrences)
            {
                Iterable<V> values = function.valueOf(each);
                Iterate.forEach(values, new Procedure<V>()
                {
                    public void value(V value)
                    {
                        target.putAll(value, Collections.nCopies(occurrences, each));
                    }
                });
            }
        });
        return target;
    }

    @Override
    public long sumOfInt(final IntFunction<? super T> function)
    {
        final long[] sum = {0L};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                long value = function.intValueOf(each);
                sum[0] += value * (long) occurrences;
            }
        });
        return sum[0];
    }

    @Override
    public long sumOfLong(final LongFunction<? super T> function)
    {
        final long[] sum = {0L};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                long value = function.longValueOf(each);
                sum[0] += value * (long) occurrences;
            }
        });
        return sum[0];
    }

    @Override
    public double sumOfFloat(final FloatFunction<? super T> function)
    {
        final double[] sum = {0.0};
        final double[] compensation = {0.0};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                float f = function.floatValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    double y = (double) f - compensation[0];
                    double t = sum[0] + y;
                    compensation[0] = t - sum[0] - y;
                    sum[0] = t;
                }
            }
        });
        return sum[0];
    }

    @Override
    public double sumOfDouble(final DoubleFunction<? super T> function)
    {
        final double[] sum = {0.0};
        final double[] compensation = {0.0};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                double d = function.doubleValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    double adjustedValue = d - compensation[0];
                    double nextSum = sum[0] + adjustedValue;
                    compensation[0] = nextSum - sum[0] - adjustedValue;
                    sum[0] = nextSum;
                }
            }
        });
        return sum[0];
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, final Function2<? super IV, ? super T, ? extends IV> function)
    {
        final IV[] result = (IV[]) new Object[]{injectedValue};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                for (int i = 0; i < occurrences; i++)
                {
                    result[0] = function.value(result[0], each);
                }
            }
        });
        return result[0];
    }

    @Override
    public int injectInto(int injectedValue, final IntObjectToIntFunction<? super T> function)
    {
        final int[] result = {injectedValue};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                for (int i = 0; i < occurrences; i++)
                {
                    result[0] = function.intValueOf(result[0], each);
                }
            }
        });
        return result[0];
    }

    @Override
    public long injectInto(long injectedValue, final LongObjectToLongFunction<? super T> function)
    {
        final long[] result = {injectedValue};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                for (int i = 0; i < occurrences; i++)
                {
                    result[0] = function.longValueOf(result[0], each);
                }
            }
        });
        return result[0];
    }

    @Override
    public double injectInto(double injectedValue, final DoubleObjectToDoubleFunction<? super T> function)
    {
        final double[] result = {injectedValue};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                for (int i = 0; i < occurrences; i++)
                {
                    result[0] = function.doubleValueOf(result[0], each);
                }
            }
        });
        return result[0];
    }

    @Override
    public float injectInto(float injectedValue, final FloatObjectToFloatFunction<? super T> function)
    {
        final float[] result = {injectedValue};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                for (int i = 0; i < occurrences; i++)
                {
                    result[0] = function.floatValueOf(result[0], each);
                }
            }
        });
        return result[0];
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

    protected MutableList<ObjectIntPair<T>> toListWithOccurrences()
    {
        final MutableList<ObjectIntPair<T>> result = FastList.newList(this.sizeDistinct());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int count)
            {
                result.add(PrimitiveTuples.pair(each, count));
            }
        });
        return result;
    }

    public ImmutableList<ObjectIntPair<T>> topOccurrences(int n)
    {
        return this.occurrencesSortingBy(n, new IntFunction<ObjectIntPair<T>>()
        {
            public int intValueOf(ObjectIntPair<T> item)
            {
                return -item.getTwo();
            }
        }).toImmutable();
    }

    public ImmutableList<ObjectIntPair<T>> bottomOccurrences(int n)
    {
        return this.occurrencesSortingBy(n, new IntFunction<ObjectIntPair<T>>()
        {
            public int intValueOf(ObjectIntPair<T> item)
            {
                return item.getTwo();
            }
        }).toImmutable();
    }

    private MutableList<ObjectIntPair<T>> occurrencesSortingBy(int n, IntFunction<ObjectIntPair<T>> function)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("Cannot use a value of n < 0");
        }
        if (n == 0)
        {
            return Lists.fixedSize.empty();
        }
        int keySize = Math.min(n, this.sizeDistinct());
        MutableList<ObjectIntPair<T>> sorted = this.toListWithOccurrences().sortThisByInt(function);
        MutableList<ObjectIntPair<T>> results = sorted.subList(0, keySize).toList();
        while (keySize < sorted.size() && results.getLast().getTwo() == sorted.get(keySize).getTwo())
        {
            results.add(sorted.get(keySize));
            keySize++;
        }
        return results;
    }
}
