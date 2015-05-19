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

package com.gs.collections.impl.bag.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.ParallelUnsortedBag;
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
import com.gs.collections.api.block.function.Function0;
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
import com.gs.collections.api.block.predicate.primitive.ObjectIntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.primitive.MutableObjectIntMap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
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
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.primitive.IntToIntFunctions;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.lazy.parallel.bag.NonParallelUnsortedBag;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.mutable.primitive.ObjectIntHashMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.partition.bag.PartitionHashBag;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;

/**
 * A HashBag is a MutableBag which uses a Map as its underlying data store.  Each key in the Map represents some item,
 * and the value in the map represents the current number of occurrences of that item.
 *
 * @since 1.0
 */
public class HashBag<T>
        extends AbstractMutableBag<T>
        implements Externalizable, MutableBag<T>
{
    private static final long serialVersionUID = 1L;

    private MutableObjectIntMap<T> items;
    private int size;

    public HashBag()
    {
        this.items = ObjectIntHashMap.newMap();
    }

    public HashBag(int size)
    {
        this.items = new ObjectIntHashMap<T>(size);
    }

    private HashBag(MutableObjectIntMap<T> map)
    {
        this.items = map;
        this.size = (int) map.sum();
    }

    public static <E> HashBag<E> newBag()
    {
        return new HashBag<E>();
    }

    public static <E> HashBag<E> newBag(int size)
    {
        return new HashBag<E>(size);
    }

    public static <E> HashBag<E> newBag(Bag<? extends E> source)
    {
        HashBag<E> result = HashBag.newBag(source.sizeDistinct());
        result.addAllBag(source);
        return result;
    }

    public static <E> HashBag<E> newBag(Iterable<? extends E> source)
    {
        if (source instanceof Bag)
        {
            return HashBag.newBag((Bag<E>) source);
        }
        return HashBag.newBagWith((E[]) Iterate.toArray(source));
    }

    public static <E> HashBag<E> newBagWith(E... elements)
    {
        HashBag<E> result = HashBag.newBag();
        ArrayIterate.addAllTo(elements, result);
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends T> source)
    {
        if (source instanceof Bag)
        {
            return this.addAllBag((Bag<T>) source);
        }
        return super.addAll(source);
    }

    private boolean addAllBag(Bag<? extends T> source)
    {
        source.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                HashBag.this.addOccurrences(each, occurrences);
            }
        });
        return source.notEmpty();
    }

    public void addOccurrences(T item, int occurrences)
    {
        if (occurrences < 0)
        {
            throw new IllegalArgumentException("Cannot add a negative number of occurrences");
        }
        if (occurrences > 0)
        {
            this.items.updateValue(item, 0, IntToIntFunctions.add(occurrences));
            this.size += occurrences;
        }
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof Bag))
        {
            return false;
        }
        final Bag<?> bag = (Bag<?>) other;
        if (this.sizeDistinct() != bag.sizeDistinct())
        {
            return false;
        }

        return this.items.keyValuesView().allSatisfy(new Predicate<ObjectIntPair<T>>()
        {
            public boolean accept(ObjectIntPair<T> each)
            {
                return bag.occurrencesOf(each.getOne()) == each.getTwo();
            }
        });
    }

    public int sizeDistinct()
    {
        return this.items.size();
    }

    @Override
    public int hashCode()
    {
        final Counter counter = new Counter();
        this.items.forEachKeyValue(new ObjectIntProcedure<T>()
        {
            public void value(T item, int count)
            {
                counter.add((item == null ? 0 : item.hashCode()) ^ count);
            }
        });
        return counter.getCount();
    }

    public int occurrencesOf(Object item)
    {
        return this.items.get(item);
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.items.forEachKeyValue(objectIntProcedure);
    }

    public MutableBag<T> selectByOccurrences(final IntPredicate predicate)
    {
        MutableObjectIntMap<T> map = this.items.select(new ObjectIntPredicate<T>()
        {
            public boolean accept(T each, int occurrences)
            {
                return predicate.accept(occurrences);
            }
        });
        return new HashBag<T>(map);
    }

    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        final MutableMap<T, Integer> map = UnifiedMap.newMap(this.items.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T item, int count)
            {
                map.put(item, count);
            }
        });
        return map;
    }

    public String toStringOfItemToCount()
    {
        return this.items.toString();
    }

    @Override
    public boolean remove(Object item)
    {
        int newValue = this.items.updateValue((T) item, 0, IntToIntFunctions.decrement());
        if (newValue <= 0)
        {
            this.items.removeKey((T) item);
            if (newValue == -1)
            {
                return false;
            }
        }
        this.size--;
        return true;
    }

    public void clear()
    {
        this.items.clear();
        this.size = 0;
    }

    @Override
    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        ((ObjectIntHashMap<T>) this.items).writeExternal(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.items = new ObjectIntHashMap<T>();
        ((ObjectIntHashMap<T>) this.items).readExternal(in);
        this.size = (int) this.items.sum();
    }

    public MutableBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public void each(final Procedure<? super T> procedure)
    {
        this.items.forEachKeyValue(new ObjectIntProcedure<T>()
        {
            public void value(T key, int count)
            {
                for (int i = 0; i < count; i++)
                {
                    procedure.value(key);
                }
            }
        });
    }

    @Override
    public void forEachWithIndex(final ObjectIntProcedure<? super T> objectIntProcedure)
    {
        final Counter index = new Counter();
        this.items.forEachKeyValue(new ObjectIntProcedure<T>()
        {
            public void value(T key, int count)
            {
                for (int i = 0; i < count; i++)
                {
                    objectIntProcedure.value(key, index.getCount());
                    index.increment();
                }
            }
        });
    }

    @Override
    public <P> void forEachWith(final Procedure2<? super T, ? super P> procedure, final P parameter)
    {
        this.items.forEachKeyValue(new ObjectIntProcedure<T>()
        {
            public void value(T key, int count)
            {
                for (int i = 0; i < count; i++)
                {
                    procedure.value(key, parameter);
                }
            }
        });
    }

    public Iterator<T> iterator()
    {
        return new InternalIterator();
    }

    public boolean removeOccurrences(Object item, int occurrences)
    {
        if (occurrences < 0)
        {
            throw new IllegalArgumentException("Cannot remove a negative number of occurrences");
        }

        if (occurrences == 0)
        {
            return false;
        }

        int newValue = this.items.updateValue((T) item, 0, IntToIntFunctions.subtract(occurrences));

        if (newValue <= 0)
        {
            this.size -= occurrences + newValue;
            this.items.remove(item);
            return newValue + occurrences != 0;
        }

        this.size -= occurrences;
        return true;
    }

    public boolean setOccurrences(T item, int occurrences)
    {
        if (occurrences < 0)
        {
            throw new IllegalArgumentException("Cannot set a negative number of occurrences");
        }

        int originalOccurrences = this.items.get(item);

        if (originalOccurrences == occurrences)
        {
            return false;
        }

        if (occurrences == 0)
        {
            this.items.remove(item);
        }
        else
        {
            this.items.put(item, occurrences);
        }

        this.size -= originalOccurrences - occurrences;
        return true;
    }

    public HashBag<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public MutableBag<T> newEmpty()
    {
        return HashBag.newBag();
    }

    public <P, V> MutableBag<V> collectWith(
            final Function2<? super T, ? super P, ? extends V> function,
            final P parameter)
    {
        final HashBag<V> result = HashBag.newBag(this.items.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(function.value(each, parameter), occurrences);
            }
        });
        return result;
    }

    public HashBag<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public SynchronizedBag<T> asSynchronized()
    {
        return new SynchronizedBag<T>(this);
    }

    public HashBag<T> withAll(Iterable<? extends T> iterable)
    {
        this.addAllIterable(iterable);
        return this;
    }

    public HashBag<T> withoutAll(Iterable<? extends T> iterable)
    {
        this.removeAllIterable(iterable);
        return this;
    }

    public <P> MutableBag<T> selectWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final MutableBag<T> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each, parameter))
                {
                    result.addOccurrences(each, occurrences);
                }
            }
        });
        return result;
    }

    public <P> MutableBag<T> rejectWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final MutableBag<T> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                if (!predicate.accept(each, parameter))
                {
                    result.addOccurrences(each, index);
                }
            }
        });
        return result;
    }

    @Override
    public void removeIf(Predicate<? super T> predicate)
    {
        for (Iterator<T> iterator = this.items.keySet().iterator(); iterator.hasNext(); )
        {
            T key = iterator.next();
            if (predicate.accept(key))
            {
                this.size -= this.items.get(key);
                iterator.remove();
            }
        }
    }

    @Override
    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (Iterator<T> iterator = this.items.keySet().iterator(); iterator.hasNext(); )
        {
            T key = iterator.next();
            if (predicate.accept(key, parameter))
            {
                this.size -= this.items.get(key);
                iterator.remove();
            }
        }
    }

    @Override
    public <P> T detectWithIfNone(
            final Predicate2<? super T, ? super P> predicate,
            final P parameter,
            Function0<? extends T> function)
    {
        return this.items.keysView().detectIfNone(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        }, function);
    }

    public UnmodifiableBag<T> asUnmodifiable()
    {
        return UnmodifiableBag.of(this);
    }

    public ImmutableBag<T> toImmutable()
    {
        return Bags.immutable.withAll(this);
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        int oldSize = this.size;
        if (iterable instanceof Bag)
        {
            Bag<?> source = (Bag<?>) iterable;
            source.forEachWithOccurrences(new ObjectIntProcedure<Object>()
            {
                public void value(Object each, int parameter)
                {
                    int removed = HashBag.this.items.removeKeyIfAbsent((T) each, 0);
                    HashBag.this.size -= removed;
                }
            });
        }
        else
        {
            for (Object each : iterable)
            {
                int removed = this.items.removeKeyIfAbsent((T) each, 0);
                this.size -= removed;
            }
        }
        return this.size != oldSize;
    }

    @Override
    public boolean retainAllIterable(Iterable<?> iterable)
    {
        int oldSize = this.size;
        this.removeIfWith(Predicates2.notIn(), UnifiedSet.newSet(iterable));
        return this.size != oldSize;
    }

    public int size()
    {
        return this.size;
    }

    @Override
    public boolean contains(Object o)
    {
        return this.items.containsKey(o);
    }

    public MutableBag<T> select(final Predicate<? super T> predicate)
    {
        final MutableBag<T> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each))
                {
                    result.addOccurrences(each, occurrences);
                }
            }
        });
        return result;
    }

    public PartitionMutableBag<T> partition(final Predicate<? super T> predicate)
    {
        final PartitionMutableBag<T> result = new PartitionHashBag<T>();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                MutableBag<T> bucket = predicate.accept(each) ? result.getSelected() : result.getRejected();
                bucket.addOccurrences(each, index);
            }
        });
        return result;
    }

    public <P> PartitionMutableBag<T> partitionWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final PartitionMutableBag<T> result = new PartitionHashBag<T>();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                MutableBag<T> bucket = predicate.accept(each, parameter) ? result.getSelected() : result.getRejected();
                bucket.addOccurrences(each, index);
            }
        });
        return result;
    }

    public <S> MutableBag<S> selectInstancesOf(final Class<S> clazz)
    {
        final MutableBag<S> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (clazz.isInstance(each))
                {
                    result.addOccurrences((S) each, occurrences);
                }
            }
        });
        return result;
    }

    public MutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, new BooleanHashBag());
    }

    public MutableByteBag collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, new ByteHashBag());
    }

    public MutableCharBag collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, new CharHashBag());
    }

    public MutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, new DoubleHashBag());
    }

    public T getFirst()
    {
        return this.items.keysView().getFirst();
    }

    public <V> HashBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, HashBagMultimap.<V, T>newMultimap());
    }

    @Override
    public <V> MutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V, T>newMap());
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(UnifiedSet.<Pair<T, Integer>>newSet());
    }

    public T getLast()
    {
        return this.items.keysView().getLast();
    }

    public MutableBag<T> reject(final Predicate<? super T> predicate)
    {
        final MutableBag<T> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                if (!predicate.accept(each))
                {
                    result.addOccurrences(each, index);
                }
            }
        });
        return result;
    }

    public <V> MutableBag<V> collect(final Function<? super T, ? extends V> function)
    {
        final HashBag<V> result = HashBag.newBag(this.items.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(function.valueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public <P, V, R extends Collection<V>> R collectWith(final Function2<? super T, ? super P, ? extends V> function, final P parameter, final R target)
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

    public MutableFloatBag collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, new FloatHashBag());
    }

    public MutableIntBag collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, new IntHashBag());
    }

    public MutableLongBag collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, new LongHashBag());
    }

    public MutableShortBag collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, new ShortHashBag());
    }

    public <V> MutableBag<V> collectIf(
            final Predicate<? super T> predicate,
            final Function<? super T, ? extends V> function)
    {
        final MutableBag<V> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each))
                {
                    result.addOccurrences(function.valueOf(each), occurrences);
                }
            }
        });
        return result;
    }

    public <V> MutableBag<V> flatCollect(final Function<? super T, ? extends Iterable<V>> function)
    {
        final MutableBag<V> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, final int occurrences)
            {
                Iterable<V> values = function.valueOf(each);
                Iterate.forEach(values, new Procedure<V>()
                {
                    public void value(V each)
                    {
                        result.addOccurrences(each, occurrences);
                    }
                });
            }
        });
        return result;
    }

    public <V> HashBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, HashBagMultimap.<V, T>newMultimap());
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    public <S> MutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, HashBag.<Pair<T, S>>newBag());
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return this.items.keysView().detect(predicate);
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.items.keysView().detectWith(predicate, parameter);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return this.items.keysView().detectIfNone(predicate, function);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.items.keysView().anySatisfy(predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.items.keysView().anySatisfyWith(predicate, parameter);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.items.keysView().allSatisfy(predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.items.keysView().allSatisfyWith(predicate, parameter);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return this.items.keysView().noneSatisfy(predicate);
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.items.keysView().noneSatisfyWith(predicate, parameter);
    }

    @Override
    public MutableSortedBag<T> toSortedBag()
    {
        final TreeBag<T> treeBag = TreeBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T item, int occurrences)
            {
                treeBag.addOccurrences(item, occurrences);
            }
        });
        return treeBag;
    }

    @Override
    public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
    {
        final TreeBag<T> treeBag = TreeBag.newBag(comparator);
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T item, int occurrences)
            {
                treeBag.addOccurrences(item, occurrences);
            }
        });
        return treeBag;
    }

    @Override
    public <V extends Comparable<? super V>> MutableSortedBag<T> toSortedBagBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedBag(Comparators.byFunction(function));
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return this.items.keysView().min(comparator);
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return this.items.keysView().max(comparator);
    }

    @Override
    public T min()
    {
        return this.items.keysView().min();
    }

    @Override
    public T max()
    {
        return this.items.keysView().max();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return this.items.keysView().minBy(function);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return this.items.keysView().maxBy(function);
    }

    public HashBag<T> with(T... elements)
    {
        this.addAll(Arrays.asList(elements));
        return this;
    }

    public HashBag<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    @Override
    public boolean add(T item)
    {
        this.items.updateValue(item, 0, IntToIntFunctions.increment());
        this.size++;
        return true;
    }

    public HashBag<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    private class InternalIterator implements Iterator<T>
    {
        private final Iterator<T> iterator = HashBag.this.items.keySet().iterator();

        private T currentItem;
        private int occurrences;
        private boolean canRemove;

        public boolean hasNext()
        {
            return this.occurrences > 0 || this.iterator.hasNext();
        }

        public T next()
        {
            if (this.occurrences == 0)
            {
                this.currentItem = this.iterator.next();
                this.occurrences = HashBag.this.occurrencesOf(this.currentItem);
            }
            this.occurrences--;
            this.canRemove = true;
            return this.currentItem;
        }

        public void remove()
        {
            if (!this.canRemove)
            {
                throw new IllegalStateException();
            }
            if (this.occurrences == 0)
            {
                this.iterator.remove();
                HashBag.this.size--;
            }
            else
            {
                HashBag.this.remove(this.currentItem);
            }
            this.canRemove = false;
        }
    }

    @Beta
    public ParallelUnsortedBag<T> asParallel(ExecutorService executorService, int batchSize)
    {
        if (executorService == null)
        {
            throw new NullPointerException();
        }
        if (batchSize < 1)
        {
            throw new IllegalArgumentException();
        }
        return new NonParallelUnsortedBag<T>(this);
    }
}
