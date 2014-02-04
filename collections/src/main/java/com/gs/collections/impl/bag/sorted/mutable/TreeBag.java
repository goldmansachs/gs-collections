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

package com.gs.collections.impl.bag.sorted.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
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
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure2;
import com.gs.collections.impl.collection.mutable.AbstractMutableCollection;
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
import com.gs.collections.impl.multimap.bag.TreeBagMultimap;
import com.gs.collections.impl.partition.bag.sorted.PartitionTreeBag;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

/**
 * A TreeBag is a MutableSortedBag which uses a SortedMap as its underlying data store.  Each key in the SortedMap represents some item,
 * and the value in the map represents the current number of occurrences of that item.
 *
 * @since 4.2
 */
public class TreeBag<T>
        extends AbstractMutableCollection<T>
        implements Externalizable, MutableSortedBag<T>
{
    private static final Function0<Counter> NEW_COUNTER_BLOCK = new Function0<Counter>()
    {
        public Counter value()
        {
            return new Counter();
        }
    };
    private static final long serialVersionUID = 1L;
    private MutableSortedMap<T, Counter> items;
    private int size;

    public TreeBag()
    {
        this.items = TreeSortedMap.newMap();
    }

    private TreeBag(MutableSortedMap<T, Counter> map)
    {
        this.items = map;
        this.size = (int) map.valuesView().sumOfInt(Counter.TO_COUNT);
    }

    public TreeBag(Comparator<? super T> comparator)
    {
        this.items = TreeSortedMap.newMap(comparator);
    }

    public TreeBag(SortedBag<T> sortedBag)
    {
        this(sortedBag.comparator(), sortedBag);
    }

    public TreeBag(Comparator<? super T> comparator, Iterable<? extends T> iterable)
    {
        this(comparator);
        this.addAllIterable(iterable);
    }

    public static <E> TreeBag<E> newBag()
    {
        return new TreeBag<E>();
    }

    public static <E> TreeBag<E> newBag(Comparator<? super E> comparator)
    {
        return new TreeBag<E>(comparator);
    }

    public static <E> TreeBag<E> newBag(Iterable<? extends E> source)
    {
        if (source instanceof SortedBag<?>)
        {
            return new TreeBag<E>((SortedBag<E>) source);
        }
        return Iterate.addAllTo(source, TreeBag.<E>newBag());
    }

    public static <E> TreeBag<E> newBag(Comparator<? super E> comparator, Iterable<? extends E> iterable)
    {
        return new TreeBag<E>(comparator, iterable);
    }

    public static <E> TreeBag<E> newBagWith(E... elements)
    {
        //noinspection SSBasedInspection
        return TreeBag.newBag(Arrays.asList(elements));
    }

    public static <E> TreeBag<E> newBagWith(Comparator<? super E> comparator, E... elements)
    {
        //noinspection SSBasedInspection
        return TreeBag.newBag(comparator, Arrays.asList(elements));
    }

    private static <T> int compare(SortedBag<T> bagA, SortedBag<T> bagB)
    {
        Iterator<T> itrA = bagA.iterator();
        Iterator<T> itrB = bagB.iterator();
        if (bagA.comparator() != null)
        {
            Comparator<? super T> comparator = bagA.comparator();
            while (itrA.hasNext())
            {
                if (itrB.hasNext())
                {
                    int val = comparator.compare(itrA.next(), itrB.next());
                    if (val != 0)
                    {
                        return val;
                    }
                }
                else
                {
                    return 1;
                }
            }
            return itrB.hasNext() ? -1 : 0;
        }

        while (itrA.hasNext())
        {
            if (itrB.hasNext())
            {
                int val = ((Comparable<T>) itrA.next()).compareTo(itrB.next());
                if (val != 0)
                {
                    return val;
                }
            }
            else
            {
                return 1;
            }
        }
        return itrB.hasNext() ? -1 : 0;
    }

    @Override
    public TreeBag<T> clone()
    {
        try
        {
            TreeBag<T> clone = (TreeBag<T>) super.clone();
            clone.items = this.items.clone();
            return clone;
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
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

        return this.items.keyValuesView().allSatisfy(new Predicate<Pair<T, Counter>>()
        {
            public boolean accept(Pair<T, Counter> each)
            {
                return bag.occurrencesOf(each.getOne()) == each.getTwo().getCount();
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
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int count)
            {
                counter.add((each == null ? 0 : each.hashCode()) ^ count);
            }
        });
        return counter.getCount();
    }

    public void forEachWithOccurrences(final ObjectIntProcedure<? super T> procedure)
    {
        this.items.forEachKeyValue(new Procedure2<T, Counter>()
        {
            public void value(T item, Counter count)
            {
                procedure.value(item, count.getCount());
            }
        });
    }

    public MutableSortedBag<T> selectByOccurrences(final IntPredicate predicate)
    {
        MutableSortedMap<T, Counter> map = this.items.select(new Predicate2<T, Counter>()
        {
            public boolean accept(T each, Counter occurrences)
            {
                return predicate.accept(occurrences.getCount());
            }
        });
        return new TreeBag<T>(map);
    }

    public int occurrencesOf(Object item)
    {
        Counter counter = this.items.get(item);
        return counter == null ? 0 : counter.getCount();
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

    public String toStringOfItemToCount()
    {
        return this.items.toString();
    }

    @Override
    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    @Override
    public boolean remove(Object item)
    {
        Counter counter = this.items.get(item);
        if (counter != null)
        {
            if (counter.getCount() > 1)
            {
                counter.decrement();
            }
            else
            {
                this.items.remove(item);
            }
            this.size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        return this.removeAllIterable(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        return this.retainAllIterable(collection);
    }

    @Override
    public void clear()
    {
        this.items.clear();
        this.size = 0;
    }

    @Override
    public boolean contains(Object o)
    {
        return this.items.containsKey(o);
    }

    public int compareTo(SortedBag<T> otherBag)
    {
        return TreeBag.compare(this, otherBag);
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeObject(this.comparator());
        out.writeInt(this.items.size());
        try
        {
            this.items.forEachKeyValue(new CheckedProcedure2<T, Counter>()
            {
                @Override
                public void safeValue(T object, Counter parameter) throws Exception
                {
                    out.writeObject(object);
                    out.writeInt(parameter.getCount());
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
        this.items = new TreeSortedMap<T, Counter>((Comparator<T>) in.readObject());
        int size = in.readInt();
        for (int i = 0; i < size; i++)
        {
            this.addOccurrences((T) in.readObject(), in.readInt());
        }
    }

    @Override
    public void forEach(final Procedure<? super T> procedure)
    {
        this.items.forEachKeyValue(new Procedure2<T, Counter>()
        {
            public void value(T key, Counter value)
            {
                for (int i = 0; i < value.getCount(); i++)
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
        this.items.forEachKeyValue(new Procedure2<T, Counter>()
        {
            public void value(T key, Counter value)
            {
                for (int i = 0; i < value.getCount(); i++)
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
        this.items.forEachKeyValue(new Procedure2<T, Counter>()
        {
            public void value(T key, Counter value)
            {
                for (int i = 0; i < value.getCount(); i++)
                {
                    procedure.value(key, parameter);
                }
            }
        });
    }

    @Override
    public Iterator<T> iterator()
    {
        return new InternalIterator();
    }

    public void addOccurrences(T item, int occurrences)
    {
        if (occurrences < 0)
        {
            throw new IllegalArgumentException("Cannot add a negative number of occurrences");
        }
        if (occurrences > 0)
        {
            this.items.getIfAbsentPut(item, NEW_COUNTER_BLOCK).add(occurrences);
            this.size += occurrences;
        }
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

        Counter counter = this.items.get(item);
        if (counter == null)
        {
            return false;
        }
        int startCount = counter.getCount();

        if (occurrences >= startCount)
        {
            this.items.remove(item);
            this.size -= startCount;
            return true;
        }

        counter.add(occurrences * -1);
        this.size -= occurrences;
        return true;
    }

    public TreeBag<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public TreeBag<T> withAll(Iterable<? extends T> iterable)
    {
        this.addAllIterable(iterable);
        return this;
    }

    public TreeBag<T> withoutAll(Iterable<? extends T> iterable)
    {
        this.removeAllIterable(iterable);
        return this;
    }

    public ImmutableSortedBag<T> toImmutable()
    {
        throw new UnsupportedOperationException("toImmutable not implemented yet!");
    }

    @Override
    public <P, V> MutableList<V> collectWith(
            final Function2<? super T, ? super P, ? extends V> function,
            final P parameter)
    {
        final MutableList<V> result = FastList.newList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                V value = function.value(each, parameter);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(value);
                }
            }
        });
        return result;
    }

    public TreeBag<T> with(T element)
    {
        this.add(element);
        return this;
    }

    @Override
    public MutableSortedBag<T> newEmpty()
    {
        return TreeBag.newBag(this.items.comparator());
    }

    @Override
    public <P> MutableSortedBag<T> selectWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final MutableSortedBag<T> result = TreeBag.newBag(this.comparator());
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
    public <P> MutableSortedBag<T> rejectWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final MutableSortedBag<T> result = TreeBag.newBag(this.comparator());
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
        Set<Map.Entry<T, Counter>> entries = this.items.entrySet();
        for (Iterator<Map.Entry<T, Counter>> iterator = entries.iterator(); iterator.hasNext(); )
        {
            Map.Entry<T, Counter> entry = iterator.next();
            if (predicate.accept(entry.getKey()))
            {
                this.size -= entry.getValue().getCount();
                iterator.remove();
            }
        }
    }

    @Override
    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        Set<Map.Entry<T, Counter>> entries = this.items.entrySet();
        for (Iterator<Map.Entry<T, Counter>> iterator = entries.iterator(); iterator.hasNext(); )
        {
            Map.Entry<T, Counter> entry = iterator.next();
            if (predicate.accept(entry.getKey(), parameter))
            {
                this.size -= entry.getValue().getCount();
                iterator.remove();
            }
        }
    }

    @Override
    public <P> T detectWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        return this.items.keysView().detect(new Predicate<T>()
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
        return this.items.keysView().detectIfNone(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        }, function);
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
    public <P> boolean anySatisfyWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        return this.items.keysView().anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        });
    }

    @Override
    public <P> boolean allSatisfyWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        return this.items.keysView().allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(each, parameter);
            }
        });
    }

    @Override
    public UnmodifiableSortedBag<T> asUnmodifiable()
    {
        return UnmodifiableSortedBag.of(this);
    }

    @Override
    public MutableSortedBag<T> asSynchronized()
    {
        throw new UnsupportedOperationException("asSynchronized not implemented yet!");
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        int oldSize = this.size;
        for (Object each : iterable)
        {
            Counter removed = this.items.remove(each);
            if (removed != null)
            {
                this.size -= removed.getCount();
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

    @Override
    public int size()
    {
        return this.size;
    }

    @Override
    public MutableSortedBag<T> reject(final Predicate<? super T> predicate)
    {
        final MutableSortedBag<T> result = TreeBag.newBag(this.comparator());
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

    public PartitionMutableSortedBag<T> partitionWhile(Predicate<? super T> predicate)
    {
        PartitionTreeBag<T> result = new PartitionTreeBag<T>(this.comparator());
        return IterableIterate.partitionWhile(this, predicate, result);
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

    public <V> TreeBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, TreeBagMultimap.<V, T>newMultimap(this.comparator()));
    }

    public <V> TreeBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, TreeBagMultimap.<V, T>newMultimap(this.comparator()));
    }

    @Override
    public MutableByteList collectByte(final ByteFunction<? super T> byteFunction)
    {
        final MutableByteList result = new ByteArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                byte element = byteFunction.byteValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public T getFirst()
    {
        return this.items.keysView().getFirst();
    }

    public MutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(TreeSortedSet.<Pair<T, Integer>>newSet());
    }

    @Override
    public T getLast()
    {
        return this.items.keysView().getLast();
    }

    @Override
    public <V> MutableList<V> collect(final Function<? super T, ? extends V> function)
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
        return result;
    }

    @Override
    public <V> MutableList<V> flatCollect(final Function<? super T, ? extends Iterable<V>> function)
    {
        final MutableList<V> result = FastList.newList();
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
                            result.add(each);
                        }
                    }
                });
            }
        });
        return result;
    }

    public MutableSortedSet<T> distinct()
    {
        return TreeSortedSet.newSet(this.comparator(), this.items.keySet());
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
    public MutableSortedBag<T> select(final Predicate<? super T> predicate)
    {
        final MutableSortedBag<T> result = TreeBag.newBag(this.comparator());
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

    @Override
    public MutableBooleanList collectBoolean(final BooleanFunction<? super T> booleanFunction)
    {
        final MutableBooleanList result = new BooleanArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                boolean element = booleanFunction.booleanValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public MutableCharList collectChar(final CharFunction<? super T> charFunction)
    {
        final MutableCharList result = new CharArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                char element = charFunction.charValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public MutableDoubleList collectDouble(final DoubleFunction<? super T> doubleFunction)
    {
        final MutableDoubleList result = new DoubleArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                double element = doubleFunction.doubleValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public MutableFloatList collectFloat(final FloatFunction<? super T> floatFunction)
    {
        final MutableFloatList result = new FloatArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                float element = floatFunction.floatValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public MutableIntList collectInt(final IntFunction<? super T> intFunction)
    {
        final MutableIntList result = new IntArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                int element = intFunction.intValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public MutableLongList collectLong(final LongFunction<? super T> longFunction)
    {
        final MutableLongList result = new LongArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                long element = longFunction.longValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public MutableShortList collectShort(final ShortFunction<? super T> shortFunction)
    {
        final MutableShortList result = new ShortArrayList();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                short element = shortFunction.shortValueOf(each);
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(element);
                }
            }
        });
        return result;
    }

    @Override
    public <V> MutableList<V> collectIf(
            final Predicate<? super T> predicate,
            final Function<? super T, ? extends V> function)
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
        return result;
    }

    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<T, S>>newList());
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return this.items.keysView().detect(predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return this.items.keysView().detectIfNone(predicate, function);
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
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.items.keysView().anySatisfy(predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.items.keysView().allSatisfy(predicate);
    }

    @Override
    public MutableList<T> toList()
    {
        return FastList.newList(this);
    }

    @Override
    public MutableSet<T> toSet()
    {
        UnifiedSet<T> result = UnifiedSet.newSet(this.sizeDistinct());
        this.items.forEachKey(CollectionAddProcedure.on(result));
        return result;
    }

    @Override
    public MutableBag<T> toBag()
    {
        return HashBag.newBag(this);
    }

    public MutableStack<T> toStack()
    {
        return ArrayStack.newStack(this);
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

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        this.forEach(MultimapPutProcedure.on(target, function));
        return target;
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        this.forEach(MultimapEachPutProcedure.on(target, function));
        return target;
    }

    public Comparator<? super T> comparator()
    {
        return this.items.comparator();
    }

    public TreeBag<T> with(T... elements)
    {
        this.addAll(Arrays.asList(elements));
        return this;
    }

    public TreeBag<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    @Override
    public boolean add(T item)
    {
        Counter counter = this.items.getIfAbsentPut(item, NEW_COUNTER_BLOCK);
        counter.increment();
        this.size++;
        return true;
    }

    public TreeBag<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    private class InternalIterator implements Iterator<T>
    {
        private int position;
        private boolean isCurrentKeySet;
        private int currentKeyPosition;
        private int currentKeyOccurrences;
        private Iterator<Pair<T, Counter>> keyValueIterator = TreeBag.this.items.keyValuesView().iterator();
        private Pair<T, Counter> currentKeyValue;

        public boolean hasNext()
        {
            return this.position != TreeBag.this.size;
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
            this.currentKeyOccurrences = this.currentKeyValue.getTwo().getCount();
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

            TreeBag.this.remove(this.currentKeyValue.getOne());
            this.keyValueIterator = TreeBag.this.items.keyValuesView().iterator();
            this.currentKeyOccurrences--;
            this.currentKeyPosition--;
        }
    }
}
