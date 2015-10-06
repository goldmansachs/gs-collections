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
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
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
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.bag.immutable.AbstractImmutableBagIterable;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;
import com.gs.collections.impl.partition.bag.sorted.PartitionTreeBag;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * @since 7.0
 */
@Immutable
abstract class AbstractImmutableSortedBag<T>
        extends AbstractImmutableBagIterable<T>
        implements ImmutableSortedBag<T>
{
    public ImmutableSortedBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this.reject(Predicates.in(elements));
    }

    public ImmutableSortedBag<T> toImmutable()
    {
        return this;
    }

    public ImmutableSortedBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    protected Object writeReplace()
    {
        return new ImmutableSortedBagSerializationProxy<T>(this);
    }

    public <V> ImmutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, TreeBagMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public <V> ImmutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, TreeBagMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public <V> ImmutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V, T>newMap()).toImmutable();
    }

    public ImmutableSortedBag<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, TreeBag.newBag(this.comparator())).toImmutable();
    }

    public <P> ImmutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.selectWith(predicate, parameter, TreeBag.newBag(this.comparator())).toImmutable();
    }

    public ImmutableSortedBag<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, TreeBag.newBag(this.comparator())).toImmutable();
    }

    public <P> ImmutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.rejectWith(predicate, parameter, TreeBag.newBag(this.comparator())).toImmutable();
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
                MutableSortedBag<T> bucket = predicate.accept(each, parameter)
                        ? result.getSelected()
                        : result.getRejected();
                bucket.addOccurrences(each, index);
            }
        });
        return result.toImmutable();
    }

    public <V> ImmutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, FastList.<V>newList()).toImmutable();
    }

    public <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collectWith(function, parameter, FastList.<V>newList()).toImmutable();
    }

    public <V> ImmutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, FastList.<V>newList()).toImmutable();
    }

    public ImmutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, new BooleanArrayList(this.size())).toImmutable();
    }

    public ImmutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, new ByteArrayList(this.size())).toImmutable();
    }

    public ImmutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, new CharArrayList(this.size())).toImmutable();
    }

    public ImmutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, new DoubleArrayList(this.size())).toImmutable();
    }

    public ImmutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, new FloatArrayList(this.size())).toImmutable();
    }

    public ImmutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, new IntArrayList(this.size())).toImmutable();
    }

    public ImmutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, new LongArrayList(this.size())).toImmutable();
    }

    public ImmutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, new ShortArrayList(this.size())).toImmutable();
    }

    public <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, FastList.<V>newList()).toImmutable();
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

    public MutableStack<T> toStack()
    {
        return Stacks.mutable.withAll(this);
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        MutableList<RichIterable<T>> result = Lists.mutable.empty();
        T[] objects = (T[]) this.toArray();
        MutableCollection<T> batch = TreeBag.newBag(this.comparator());
        int j = 0;

        while (j < objects.length)
        {
            for (int i = 0; i < size && j < objects.length; i++)
            {
                batch.add(objects[j]);
                j++;
            }
            result.add(batch.toImmutable());
        }
        return result.toImmutable();
    }

    public ImmutableSortedBag<T> toReversed()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".toReversed() not implemented yet");
    }

    public int detectLastIndex(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".detectLastIndex() not implemented yet");
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".reverseForEach() not implemented yet");
    }

    public LazyIterable<T> asReversed()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asReversed() not implemented yet");
    }
}
