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

package com.gs.collections.impl.bag.sorted.immutable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.bag.primitive.MutableShortBag;
import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
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
import com.gs.collections.api.list.primitive.ImmutableBooleanList;
import com.gs.collections.api.list.primitive.ImmutableByteList;
import com.gs.collections.api.list.primitive.ImmutableCharList;
import com.gs.collections.api.list.primitive.ImmutableDoubleList;
import com.gs.collections.api.list.primitive.ImmutableFloatList;
import com.gs.collections.api.list.primitive.ImmutableIntList;
import com.gs.collections.api.list.primitive.ImmutableLongList;
import com.gs.collections.api.list.primitive.ImmutableShortList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.partition.bag.sorted.PartitionTreeBag;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.tuple.primitive.PrimitiveTuples;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

@Immutable
abstract class AbstractImmutableSortedBag<T> extends AbstractImmutableCollection<T> implements ImmutableSortedBag<T>
{
    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return TreeBag.newBag(this.comparator());
    }

    protected Object writeReplace()
    {
        return new ImmutableSortedBagSerializationProxy<T>(this);
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return this.containsAllIterable(collection);
    }

    public ImmutableSortedBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public ImmutableSortedBag<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, TreeBag.newBag(this.comparator())).toImmutable();
    }

    @Override
    public <R extends Collection<T>> R select(final Predicate<? super T> predicate, final R target)
    {
        if (target instanceof MutableBag)
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

    public <P> ImmutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    public ImmutableSortedBag<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, TreeBag.newBag(this.comparator())).toImmutable();
    }

    @Override
    public <R extends Collection<T>> R reject(final Predicate<? super T> predicate, final R target)
    {
        if (target instanceof MutableBag)
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

    public <P> ImmutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    public PartitionImmutableSortedBag<T> partition(final Predicate<? super T> predicate)
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
        return result.toImmutable();
    }

    public <P> PartitionImmutableSortedBag<T> partitionWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
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
        return result.toImmutable();
    }

    public ImmutableSortedBag<T> selectByOccurrences(final IntPredicate predicate)
    {
        final MutableSortedBag<T> result = TreeBag.newBag(this.comparator());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(occurrences))
                {
                    result.addOccurrences(each, occurrences);
                }
            }
        });
        return result.toImmutable();
    }

    public <S> ImmutableSortedBag<S> selectInstancesOf(final Class<S> clazz)
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
        return result.toImmutable();
    }

    public <V> ImmutableList<V> collect(final Function<? super T, ? extends V> function)
    {
        final MutableList<V> result = FastList.newList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                V value = function.valueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    @Override
    public <V, R extends Collection<V>> R collect(final Function<? super T, ? extends V> function, final R target)
    {
        if (target instanceof MutableBag)
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
                public void value(T each, int occurrence)
                {
                    V value = function.valueOf(each);
                    for (int i = 0; i < occurrence; i++)
                    {
                        target.add(value);
                    }
                }
            });
        }
        return target;
    }

    public <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public <V> ImmutableList<V> collectIf(final Predicate<? super T> predicate, final Function<? super T, ? extends V> function)
    {
        final MutableList<V> result = FastList.newList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each))
                {
                    V element = function.valueOf(each);
                    for (int i = 0; i < occurrences; i++)
                    {
                        result.add(element);
                    }
                }
            }
        });
        return result.toImmutable();
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(final Predicate<? super T> predicate, final Function<? super T, ? extends V> function, final R target)
    {
        if (target instanceof MutableBag)
        {
            final MutableBag<V> targetBag = (MutableBag<V>) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (predicate.accept(each))
                    {
                        V item = function.valueOf(each);
                        targetBag.addOccurrences(item, occurrences);
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
                        V element = function.valueOf(each);
                        for (int i = 0; i < occurrences; i++)
                        {
                            target.add(element);
                        }
                    }
                }
            });
        }
        return target;
    }

    public ImmutableBooleanList collectBoolean(final BooleanFunction<? super T> booleanFunction)
    {
        final BooleanArrayList result = new BooleanArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                boolean value = booleanFunction.booleanValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    public ImmutableByteList collectByte(final ByteFunction<? super T> byteFunction)
    {
        final ByteArrayList result = new ByteArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                byte value = byteFunction.byteValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    public ImmutableCharList collectChar(final CharFunction<? super T> charFunction)
    {
        final CharArrayList result = new CharArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                char value = charFunction.charValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    public ImmutableDoubleList collectDouble(final DoubleFunction<? super T> doubleFunction)
    {
        final DoubleArrayList result = new DoubleArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                double value = doubleFunction.doubleValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    public ImmutableFloatList collectFloat(final FloatFunction<? super T> floatFunction)
    {
        final FloatArrayList result = new FloatArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                float value = floatFunction.floatValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    public ImmutableIntList collectInt(final IntFunction<? super T> intFunction)
    {
        final IntArrayList result = new IntArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                int value = intFunction.intValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    public ImmutableLongList collectLong(final LongFunction<? super T> longFunction)
    {
        final LongArrayList result = new LongArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                long value = longFunction.longValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
    }

    public ImmutableShortList collectShort(final ShortFunction<? super T> shortFunction)
    {
        final ShortArrayList result = new ShortArrayList(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                short value = shortFunction.shortValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result.toImmutable();
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

    public <V> ImmutableList<V> flatCollect(final Function<? super T, ? extends Iterable<V>> function)
    {
        final MutableList<V> result = FastList.newList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                Iterable<V> values = function.valueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    Iterate.forEachWith(values, Procedures2.<V>addToCollection(), result);
                }
            }
        });
        return result.toImmutable();
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(final Function<? super T, ? extends Iterable<V>> function, final R target)
    {
        if (target instanceof MutableBag)
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
                        Iterate.forEachWith(values, Procedures2.<V>addToCollection(), target);
                    }
                }
            });
        }
        return target;
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupBy(final Function<? super T, ? extends V> function, final R target)
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
    public <V, R extends MutableMultimap<V, T>> R groupByEach(final Function<? super T, ? extends Iterable<V>> function, final R target)
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
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.detect(Predicates.bind(predicate, parameter));
    }

    @Override
    public int count(final Predicate<? super T> predicate)
    {
        final Counter count = new Counter();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each))
                {
                    count.add(occurrences);
                }
            }
        });
        return count.getCount();
    }

    @Override
    public MutableBag<T> toBag()
    {
        final MutableBag<T> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(each, occurrences);
            }
        });

        return result;
    }

    public <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        final MutableList<Pair<T, S>> list = FastList.newList();
        final Iterator<S> iterator = that.iterator();

        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int parameter)
            {
                for (int i = 0; i < parameter; i++)
                {
                    if (iterator.hasNext())
                    {
                        list.add(Tuples.pair(each, iterator.next()));
                    }
                }
            }
        });
        return list.toImmutable();
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, final R target)
    {
        final Iterator<S> iterator = that.iterator();

        if (target instanceof MutableBag)
        {
            final MutableBag<S> targetBag = (MutableBag<S>) target;
            this.forEachWithOccurrences(new ObjectIntProcedure<T>()
            {
                public void value(T each, int occurrences)
                {
                    if (iterator.hasNext())
                    {
                        targetBag.addOccurrences((S) Tuples.pair(each, iterator.next()), occurrences);
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
                    for (int i = 0; i < occurrences; i++)
                    {
                        if (iterator.hasNext())
                        {
                            target.add(Tuples.pair(each, iterator.next()));
                        }
                    }
                }
            });
        }
        return target;
    }

    public ImmutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        //todo ??
        Comparator<? super T> comparator = (Comparator<? super T>) (this.comparator() == null ? Comparators.naturalOrder() : this.comparator());
        TreeSortedSet<Pair<T, Integer>> pairs = TreeSortedSet.newSet(
                Comparators.chain(
                        Comparators.<Pair<T, Integer>, T>byFunction(Functions.<T>firstOfPair(), (Comparator<T>) comparator),
                        Comparators.<Pair<T, Integer>, Integer>byFunction(Functions.<Integer>secondOfPair())));
        return Iterate.zipWithIndex(this, pairs).toImmutable();
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

    public ImmutableSortedBag<T> toImmutable()
    {
        return this;
    }
}
