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

package com.gs.collections.impl.bag;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.bag.primitive.MutableShortBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
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
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableByteCollection;
import com.gs.collections.api.collection.primitive.MutableCharCollection;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;
import com.gs.collections.api.collection.primitive.MutableIntCollection;
import com.gs.collections.api.collection.primitive.MutableLongCollection;
import com.gs.collections.api.collection.primitive.MutableShortCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.AbstractRichIterable;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.primitive.PrimitiveTuples;
import com.gs.collections.impl.utility.Iterate;

/**
 * @since 7.0
 */
public abstract class AbstractBag<T>
        extends AbstractRichIterable<T>
        implements Collection<T>, Bag<T>
{
    @Override
    public <R extends Collection<T>> R select(final Predicate<? super T> predicate, final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<T> targetBag = (MutableBag<T>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (predicate.accept(each))
                    {
                        targetBag.addOccurrences(each, occurrences);
                    }
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(
            final Predicate2<? super T, ? super P> predicate,
            final P parameter,
            final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<T> targetBag = (MutableBag<T>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (predicate.accept(each, parameter))
                    {
                        targetBag.addOccurrences(each, occurrences);
                    }
                }
            });
        }
        else
        {
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (predicate.accept(each, parameter))
                    {
                        for (int i = 0; i < occurrences; i++)
                        {
                            target.add(each);
                        }
                    }
                }
            });
        }
        return target;
    }

    @Override
    public <R extends Collection<T>> R reject(final Predicate<? super T> predicate, final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<T> targetBag = (MutableBag<T>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (!predicate.accept(each))
                    {
                        targetBag.addOccurrences(each, occurrences);
                    }
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(
            final Predicate2<? super T, ? super P> predicate,
            final P parameter,
            final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<T> targetBag = (MutableBag<T>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (!predicate.accept(each, parameter))
                    {
                        targetBag.addOccurrences(each, occurrences);
                    }
                }
            });
        }
        else
        {
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (!predicate.accept(each, parameter))
                    {
                        for (int i = 0; i < occurrences; i++)
                        {
                            target.add(each);
                        }
                    }
                }
            });
        }
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
    public <V, R extends Collection<V>> R collect(final Function<? super T, ? extends V> function, final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<V> targetBag = (MutableBag<V>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(function.valueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <P, V, R extends Collection<V>> R collectWith(
            final Function2<? super T, ? super P, ? extends V> function,
            final P parameter,
            final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<V> targetBag = (MutableBag<V>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(function.value(each, parameter), occurrences);
                }
            });
        }
        else
        {
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    V value = function.value(each, parameter);
                    for (int i = 0; i < occurrences; i++)
                    {
                        target.add(value);
                    }
                }
            });
        }
        return target;
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(
            final Predicate<? super T> predicate,
            final Function<? super T, ? extends V> function,
            final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<V> targetBag = (MutableBag<V>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (predicate.accept(each))
                    {
                        targetBag.addOccurrences(function.valueOf(each), occurrences);
                    }
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(final Function<? super T, ? extends Iterable<V>> function, final R target)
    {
        if (target instanceof MutableBag<?>)
        {
            final MutableBag<V> targetBag = (MutableBag<V>) target;

            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, final int occurrences)
                {
                    Iterable<V> values = function.valueOf(each);
                    Iterate.forEach(values, new Procedure<V>()
                    {
                        public void value(V each)
                        {
                            targetBag.addOccurrences(each, occurrences);
                        }
                    });
                }
            });
        }
        else
        {
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    Iterable<V> values = function.valueOf(each);
                    for (int i = 0; i < occurrences; i++)
                    {
                        Iterate.forEach(values, new Procedure<V>()
                        {
                            public void value(V each)
                            {
                                target.add(each);
                            }
                        });
                    }
                }
            });
        }
        return target;
    }

    @Override
    public <R extends MutableBooleanCollection> R collectBoolean(final BooleanFunction<? super T> booleanFunction, final R target)
    {
        if (target instanceof MutableBooleanBag)
        {
            final MutableBooleanBag targetBag = (MutableBooleanBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(booleanFunction.booleanValueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <R extends MutableByteCollection> R collectByte(final ByteFunction<? super T> byteFunction, final R target)
    {
        if (target instanceof MutableByteBag)
        {
            final MutableByteBag targetBag = (MutableByteBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(byteFunction.byteValueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <R extends MutableCharCollection> R collectChar(final CharFunction<? super T> charFunction, final R target)
    {
        if (target instanceof MutableCharBag)
        {
            final MutableCharBag targetBag = (MutableCharBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(charFunction.charValueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <R extends MutableDoubleCollection> R collectDouble(final DoubleFunction<? super T> doubleFunction, final R target)
    {
        if (target instanceof MutableDoubleBag)
        {
            final MutableDoubleBag targetBag = (MutableDoubleBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(doubleFunction.doubleValueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <R extends MutableFloatCollection> R collectFloat(final FloatFunction<? super T> floatFunction, final R target)
    {
        if (target instanceof MutableFloatBag)
        {
            final MutableFloatBag targetBag = (MutableFloatBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(floatFunction.floatValueOf(each), occurrences);
                }
            });
        }
        else
        {
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    float value = floatFunction.floatValueOf(each);
                    for (int i = 0; i < occurrences; i++)
                    {
                        target.add(value);
                    }
                }
            });
        }
        return target;
    }

    @Override
    public <R extends MutableIntCollection> R collectInt(final IntFunction<? super T> intFunction, final R target)
    {
        if (target instanceof MutableIntBag)
        {
            final MutableIntBag targetBag = (MutableIntBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(intFunction.intValueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <R extends MutableLongCollection> R collectLong(final LongFunction<? super T> longFunction, final R target)
    {
        if (target instanceof MutableLongBag)
        {
            final MutableLongBag targetBag = (MutableLongBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(longFunction.longValueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
    }

    @Override
    public <R extends MutableShortCollection> R collectShort(final ShortFunction<? super T> shortFunction, final R target)
    {
        if (target instanceof MutableShortBag)
        {
            final MutableShortBag targetBag = (MutableShortBag) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    targetBag.addOccurrences(shortFunction.shortValueOf(each), occurrences);
                }
            });
        }
        else
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
        }
        return target;
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
                int intValue = function.intValueOf(each);
                sum[0] += (long) intValue * (long) occurrences;
            }
        });
        return sum[0];
    }

    @Override
    public double sumOfFloat(final FloatFunction<? super T> function)
    {
        final double[] sum = {0.0d};
        final double[] compensation = {0.0d};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                float f = function.floatValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    double adjustedValue = f - compensation[0];
                    double nextSum = sum[0] + adjustedValue;
                    compensation[0] = nextSum - sum[0] - adjustedValue;
                    sum[0] = nextSum;
                }
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
                long longValue = function.longValueOf(each);
                sum[0] += longValue * (long) occurrences;
            }
        });
        return sum[0];
    }

    @Override
    public double sumOfDouble(final DoubleFunction<? super T> function)
    {
        final double[] sum = {0.0d};
        final double[] compensation = {0.0d};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                double d = function.doubleValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    double y = d - compensation[0];
                    double t = sum[0] + y;
                    compensation[0] = t - sum[0] - y;
                    sum[0] = t;
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

    public <IV, P> IV injectIntoWith(IV injectedValue, final Function3<? super IV, ? super T, ? super P, ? extends IV> function, final P parameter)
    {
        final IV[] result = (IV[]) new Object[]{injectedValue};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                for (int i = 0; i < occurrences; i++)
                {
                    result[0] = function.value(result[0], each, parameter);
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

    @Override
    public MutableList<T> toList()
    {
        final MutableList<T> result = FastList.newList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(each);
                }
            }
        });
        return result;
    }

    @Override
    public MutableList<T> toSortedList(final Comparator<? super T> comparator)
    {
        MutableList<ObjectIntPair<T>> sorted = this.toListWithOccurrences().sortThis(new Comparator<ObjectIntPair<T>>()
        {
            public int compare(ObjectIntPair<T> o1, ObjectIntPair<T> o2)
            {
                return comparator.compare(o1.getOne(), o2.getOne());
            }
        });

        final MutableList<T> result = FastList.newList(this.size());
        sorted.forEach(new Procedure<ObjectIntPair<T>>()
        {
            public void value(ObjectIntPair<T> each)
            {
                T object = each.getOne();
                int occurrences = each.getTwo();
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(object);
                }
            }
        });
        return result;
    }

    @Override
    public MutableSet<T> toSet()
    {
        final MutableSet<T> result = UnifiedSet.newSet(this.sizeDistinct());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.add(each);
            }
        });
        return result;
    }

    @Override
    public MutableSortedSet<T> toSortedSet()
    {
        final MutableSortedSet<T> result = SortedSets.mutable.empty();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.add(each);
            }
        });
        return result;
    }

    @Override
    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        final MutableSortedSet<T> result = SortedSets.mutable.with(comparator);
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.add(each);
            }
        });
        return result;
    }

    @Override
    public MutableBag<T> toBag()
    {
        final MutableBag<T> result = HashBag.newBag(this.sizeDistinct());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(each, occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableSortedBag<T> toSortedBag()
    {
        final MutableSortedBag<T> result = TreeBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(each, occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
    {
        final MutableSortedBag<T> result = TreeBag.newBag(comparator);
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(each, occurrences);
            }
        });
        return result;
    }

    protected MutableList<ObjectIntPair<T>> occurrencesSortingBy(int n, IntFunction<ObjectIntPair<T>> function, MutableList<ObjectIntPair<T>> returnWhenEmpty)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("Cannot use a value of n < 0");
        }
        if (n == 0)
        {
            return returnWhenEmpty;
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
