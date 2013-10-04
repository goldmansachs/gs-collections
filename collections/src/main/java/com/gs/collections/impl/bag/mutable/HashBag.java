/*
 * Copyright 2013 Goldman Sachs.
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
import java.util.NoSuchElementException;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.bag.primitive.MutableShortBag;
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
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.primitive.MutableObjectIntMap;
import com.gs.collections.api.multimap.MutableMultimap;
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
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.primitive.IntToIntFunctions;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.collection.mutable.AbstractMutableCollection;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.mutable.primitive.ObjectIntHashMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.partition.bag.PartitionHashBag;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;

/**
 * A HashBag is a MutableBag which uses a Map as its underlying data store.  Each key in the Map represents some item,
 * and the value in the map represents the current number of occurrences of that item.
 *
 * @since 1.0
 */
public class HashBag<T>
        extends AbstractMutableCollection<T>
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

    public static <E> HashBag<E> newBag(Iterable<? extends E> source)
    {
        return Iterate.addAllTo(source, HashBag.<E>newBag());
    }

    public static <E> HashBag<E> newBag(Bag<? extends E> source)
    {
        final HashBag<E> result = HashBag.newBag();
        source.forEachWithOccurrences(new ObjectIntProcedure<E>()
        {
            public void value(E each, int occurrences)
            {
                result.addOccurrences(each, occurrences);
            }
        });
        return result;
    }

    public static <E> HashBag<E> newBagWith(E... elements)
    {
        //noinspection SSBasedInspection
        return HashBag.newBag(Arrays.asList(elements));
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        return HashBag.newBag();
    }

    @Override
    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    @Override
    public int size()
    {
        return this.size;
    }

    public int sizeDistinct()
    {
        return this.items.size();
    }

    @Override
    public void clear()
    {
        this.items.clear();
        this.size = 0;
    }

    public HashBag<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public HashBag<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public HashBag<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public HashBag<T> with(T... elements)
    {
        this.addAll(Arrays.asList(elements));
        return this;
    }

    public HashBag<T> withAll(Iterable<? extends T> iterable)
    {
        this.addAllIterable(iterable);
        return this;
    }

    public HashBag<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public HashBag<T> withoutAll(Iterable<? extends T> iterable)
    {
        this.removeAllIterable(iterable);
        return this;
    }

    @Override
    public void forEach(final Procedure<? super T> procedure)
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

    @Override
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

    @Override
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

    @Override
    public <P, R extends Collection<T>> R selectWith(
            final Predicate2<? super T, ? super P> predicate,
            final P parameter,
            final R target)
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
        return target;
    }

    @Override
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

    @Override
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
    public <P, R extends Collection<T>> R rejectWith(
            final Predicate2<? super T, ? super P> predicate,
            final P parameter,
            final R target)
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
        return target;
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

    @Override
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

    @Override
    public MutableBooleanBag collectBoolean(final BooleanFunction<? super T> booleanFunction)
    {
        final BooleanHashBag result = new BooleanHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(booleanFunction.booleanValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableByteBag collectByte(final ByteFunction<? super T> byteFunction)
    {
        final ByteHashBag result = new ByteHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(byteFunction.byteValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableCharBag collectChar(final CharFunction<? super T> charFunction)
    {
        final CharHashBag result = new CharHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(charFunction.charValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableDoubleBag collectDouble(final DoubleFunction<? super T> doubleFunction)
    {
        final DoubleHashBag result = new DoubleHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(doubleFunction.doubleValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableFloatBag collectFloat(final FloatFunction<? super T> floatFunction)
    {
        final FloatHashBag result = new FloatHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(floatFunction.floatValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableIntBag collectInt(final IntFunction<? super T> intFunction)
    {
        final IntHashBag result = new IntHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(intFunction.intValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableLongBag collectLong(final LongFunction<? super T> longFunction)
    {
        final LongHashBag result = new LongHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(longFunction.longValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
    public MutableShortBag collectShort(final ShortFunction<? super T> shortFunction)
    {
        final ShortHashBag result = new ShortHashBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                result.addOccurrences(shortFunction.shortValueOf(each), occurrences);
            }
        });
        return result;
    }

    @Override
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

    @Override
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

    @Override
    public <V, R extends Collection<V>> R collectIf(
            final Predicate<? super T> predicate,
            final Function<? super T, ? extends V> function,
            final R target)
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

    @Override
    public <V, R extends Collection<V>> R flatCollect(final Function<? super T, ? extends Iterable<V>> function, final R target)
    {
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, final int occurrences)
            {
                Iterable<V> values = function.valueOf(each);
                Iterate.forEach(values, new Procedure<V>()
                {
                    public void value(V each)
                    {
                        for (int i = 0; i < occurrences; i++)
                        {
                            target.add(each);
                        }
                    }
                });
            }
        });
        return target;
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().detect(predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().detectIfNone(predicate, function);
    }

    @Override
    public <P> T detectWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().detect(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        });
    }

    @Override
    public <P> T detectWithIfNone(
            final Predicate2<? super T, ? super P> predicate,
            final P parameter,
            Function0<? extends T> function)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().detectIfNone(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        }, function);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().anySatisfy(predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        });
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().allSatisfy(predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        });
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

        return ((ObjectIntHashMap<T>) this.items).keyValuesView().allSatisfy(new Predicate<ObjectIntPair<T>>()
        {
            public boolean accept(ObjectIntPair<T> each)
            {
                return bag.occurrencesOf(each.getOne()) == each.getTwo();
            }
        });
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
    public boolean removeAll(Collection<?> collection)
    {
        return this.removeAllIterable(collection);
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        int oldSize = this.size;
        for (Object each : iterable)
        {
            int removed = this.items.removeKeyIfAbsent((T) each, 0);
            this.size -= removed;
        }
        return this.size != oldSize;
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        return this.retainAllIterable(collection);
    }

    @Override
    public boolean retainAllIterable(Iterable<?> iterable)
    {
        int oldSize = this.size;
        this.removeIfWith(Predicates2.notIn(), UnifiedSet.newSet(iterable));
        return this.size != oldSize;
    }

    @Override
    public MutableSet<T> toSet()
    {
        UnifiedSet<T> result = UnifiedSet.newSet(this.sizeDistinct());
        this.items.forEachKey(CollectionAddProcedure.on(result));
        return result;
    }

    public String toStringOfItemToCount()
    {
        return this.items.toString();
    }

    @Override
    public boolean contains(Object o)
    {
        return this.items.containsKey(o);
    }

    @Override
    public MutableBag<T> toBag()
    {
        return HashBag.newBag(this);
    }

    @Override
    public MutableList<T> toList()
    {
        FastList<T> result = FastList.newList(this.size());
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    public int occurrencesOf(Object item)
    {
        return this.items.get(item);
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
            this.items.remove((T) item);
            return newValue + occurrences != 0;
        }

        this.size -= occurrences;
        return true;
    }

    public void forEachWithOccurrences(final ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.items.forEachKeyValue(new ObjectIntProcedure<T>()
        {
            public void value(T item, int count)
            {
                objectIntProcedure.value(item, count);
            }
        });
    }

    @Override
    public Iterator<T> iterator()
    {
        return new InternalIterator();
    }

    public ImmutableBag<T> toImmutable()
    {
        return Bags.immutable.withAll(this);
    }

    @Override
    public boolean add(T item)
    {
        this.items.updateValue(item, 0, IntToIntFunctions.increment());
        this.size++;
        return true;
    }

    public <V> HashBagMultimap<V, T> groupBy(
            Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, HashBagMultimap.<V, T>newMultimap());
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        this.forEach(MultimapPutProcedure.on(target, function));
        return target;
    }

    public <V> HashBagMultimap<V, T> groupByEach(
            Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, HashBagMultimap.<V, T>newMultimap());
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        this.forEach(MultimapEachPutProcedure.on(target, function));
        return target;
    }

    @Override
    public UnmodifiableBag<T> asUnmodifiable()
    {
        return UnmodifiableBag.of(this);
    }

    @Override
    public SynchronizedBag<T> asSynchronized()
    {
        return new SynchronizedBag<T>(this);
    }

    public <S> MutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, HashBag.<Pair<T, S>>newBag());
    }

    public MutableBag<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(HashBag.<Pair<T, Integer>>newBag());
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().min(comparator);
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().max(comparator);
    }

    @Override
    public T min()
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().min();
    }

    @Override
    public T max()
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().max();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().minBy(function);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().maxBy(function);
    }

    @Override
    public T getFirst()
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().getFirst();
    }

    @Override
    public T getLast()
    {
        return ((ObjectIntHashMap<T>) this.items).keysView().getLast();
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
    public <P> int countWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final Counter result = new Counter();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(each, parameter))
                {
                    result.add(occurrences);
                }
            }
        });
        return result.getCount();
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
    public double sumOfFloat(final FloatFunction<? super T> function)
    {
        final double[] sum = {0.0};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                float floatValue = function.floatValueOf(each);
                sum[0] += floatValue * (double) occurrences;
            }
        });
        return sum[0];
    }

    @Override
    public double sumOfDouble(final DoubleFunction<? super T> function)
    {
        final double[] sum = {0.0};
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                double doubleValue = function.doubleValueOf(each);
                sum[0] += doubleValue * (double) occurrences;
            }
        });
        return sum[0];
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

    private class InternalIterator implements Iterator<T>
    {
        private int position;
        private boolean isCurrentKeySet;
        private int currentKeyPosition;
        private int currentKeyOccurrences;
        private Iterator<ObjectIntPair<T>> keyValueIterator = ((ObjectIntHashMap<T>) HashBag.this.items).keyValuesView().iterator();
        private ObjectIntPair<T> currentKeyValue;

        public boolean hasNext()
        {
            return this.position != HashBag.this.size;
        }

        public T next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            this.isCurrentKeySet = true;
            if (this.currentKeyPosition < this.currentKeyOccurrences)
            {
                this.currentKeyPosition++;
                this.position++;
                return this.currentKeyValue.getOne();
            }
            this.currentKeyValue = this.keyValueIterator.next();
            this.currentKeyPosition = 1;
            this.currentKeyOccurrences = this.currentKeyValue.getTwo();
            this.position++;
            return this.currentKeyValue.getOne();
        }

        public void remove()
        {
            if (!this.isCurrentKeySet)
            {
                throw new IllegalStateException();
            }
            this.isCurrentKeySet = false;
            this.position--;

            HashBag.this.remove(this.currentKeyValue.getOne());
            this.keyValueIterator = ((ObjectIntHashMap<T>) HashBag.this.items).keyValuesView().iterator();
            this.currentKeyOccurrences--;
            this.currentKeyPosition--;
        }
    }
}
