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

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.EmptyIterator;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.factory.primitive.BooleanBags;
import com.gs.collections.impl.factory.primitive.ByteBags;
import com.gs.collections.impl.factory.primitive.CharBags;
import com.gs.collections.impl.factory.primitive.DoubleBags;
import com.gs.collections.impl.factory.primitive.FloatBags;
import com.gs.collections.impl.factory.primitive.IntBags;
import com.gs.collections.impl.factory.primitive.LongBags;
import com.gs.collections.impl.factory.primitive.ShortBags;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.partition.bag.PartitionHashBag;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import net.jcip.annotations.Immutable;

/**
 * This is a zero element {@link ImmutableBag} which is created by calling the Bags.immutable.empty().
 *
 * @since 1.0
 */
@Immutable
final class ImmutableEmptyBag<T>
        extends AbstractImmutableBag<T>
        implements Serializable
{
    static final ImmutableBag<?> INSTANCE = new ImmutableEmptyBag<Object>();

    private static final long serialVersionUID = 1L;

    private static final LazyIterable<?> LAZY_ITERABLE = LazyIterate.adapt(INSTANCE);
    private static final Object[] TO_ARRAY = new Object[0];
    private static final PartitionImmutableBag<Object> IMMUTABLE_EMPTY_PARTITION = new PartitionHashBag<Object>().toImmutable();

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        return obj instanceof Bag && ((Bag<?>) obj).isEmpty();
    }

    public int sizeDistinct()
    {
        return 0;
    }

    public int occurrencesOf(Object item)
    {
        return 0;
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        return Maps.mutable.empty();
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    public int size()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public boolean notEmpty()
    {
        return false;
    }

    @Override
    public boolean contains(Object object)
    {
        return false;
    }

    @Override
    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.isEmpty(source);
    }

    @Override
    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.isEmpty(elements);
    }

    @Override
    public ImmutableBag<T> tap(Procedure<? super T> procedure)
    {
        return this;
    }

    public void each(Procedure<? super T> procedure)
    {
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
    }

    public T getFirst()
    {
        return null;
    }

    public T getLast()
    {
        return null;
    }

    public Iterator<T> iterator()
    {
        return EmptyIterator.getInstance();
    }

    public ImmutableBag<T> newWith(T element)
    {
        return Bags.immutable.with(element);
    }

    public ImmutableBag<T> newWithout(T element)
    {
        return this;
    }

    public ImmutableBag<T> newWithAll(Iterable<? extends T> elements)
    {
        return HashBag.newBag(elements).toImmutable();
    }

    public ImmutableBag<T> selectByOccurrences(IntPredicate predicate)
    {
        return this;
    }

    public ImmutableBag<T> select(Predicate<? super T> predicate)
    {
        return this;
    }

    @Override
    public <P> ImmutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this;
    }

    public ImmutableBag<T> reject(Predicate<? super T> predicate)
    {
        return this;
    }

    @Override
    public <P> ImmutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this;
    }

    @Override
    public PartitionImmutableBag<T> partition(Predicate<? super T> predicate)
    {
        return (PartitionImmutableBag<T>) IMMUTABLE_EMPTY_PARTITION;
    }

    @Override
    public <P> PartitionImmutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return (PartitionImmutableBag<T>) IMMUTABLE_EMPTY_PARTITION;
    }

    public <S> ImmutableBag<S> selectInstancesOf(Class<S> clazz)
    {
        return (ImmutableBag<S>) INSTANCE;
    }

    public <V> ImmutableBag<V> collect(Function<? super T, ? extends V> function)
    {
        return (ImmutableBag<V>) INSTANCE;
    }

    @Override
    public ImmutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return BooleanBags.immutable.empty();
    }

    @Override
    public ImmutableByteBag collectByte(ByteFunction<? super T> byteFunction)
    {
        return ByteBags.immutable.empty();
    }

    @Override
    public ImmutableCharBag collectChar(CharFunction<? super T> charFunction)
    {
        return CharBags.immutable.empty();
    }

    @Override
    public ImmutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return DoubleBags.immutable.empty();
    }

    @Override
    public ImmutableFloatBag collectFloat(FloatFunction<? super T> floatFunction)
    {
        return FloatBags.immutable.empty();
    }

    @Override
    public ImmutableIntBag collectInt(IntFunction<? super T> intFunction)
    {
        return IntBags.immutable.empty();
    }

    @Override
    public ImmutableLongBag collectLong(LongFunction<? super T> longFunction)
    {
        return LongBags.immutable.empty();
    }

    @Override
    public ImmutableShortBag collectShort(ShortFunction<? super T> shortFunction)
    {
        return ShortBags.immutable.empty();
    }

    @Override
    public <P, V> ImmutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return (ImmutableBag<V>) INSTANCE;
    }

    public <V> ImmutableBag<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return (ImmutableBag<V>) INSTANCE;
    }

    public <V> ImmutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return (ImmutableBag<V>) INSTANCE;
    }

    public <V> ImmutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        // TODO: Create a Singleton ImmutableEmptyBagMultimap for efficiency
        return HashBagMultimap.<V, T>newMultimap().toImmutable();
    }

    public <V> ImmutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        // TODO: Create a Singleton ImmutableEmptyBagMultimap for efficiency
        return HashBagMultimap.<V, T>newMultimap().toImmutable();
    }

    @Override
    public <V> ImmutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return Maps.immutable.empty();
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return null;
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return function.value();
    }

    @Override
    public <P> T detectWithIfNone(Predicate2<? super T, ? super P> predicate, P parameter, Function0<? extends T> function)
    {
        return function.value();
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return 0;
    }

    @Override
    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return 0;
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return true;
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return true;
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return false;
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return true;
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return true;
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return injectedValue;
    }

    @Override
    public MutableList<T> toList()
    {
        return Lists.mutable.empty();
    }

    @Override
    public MutableList<T> toSortedList()
    {
        return Lists.mutable.empty();
    }

    @Override
    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return Lists.mutable.empty();
    }

    @Override
    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return Lists.mutable.empty();
    }

    @Override
    public MutableSet<T> toSet()
    {
        return Sets.mutable.empty();
    }

    @Override
    public MutableBag<T> toBag()
    {
        return Bags.mutable.empty();
    }

    @Override
    public MutableSortedBag<T> toSortedBag()
    {
        return TreeBag.newBag();
    }

    @Override
    public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
    {
        return TreeBag.newBag(comparator);
    }

    @Override
    public <V extends Comparable<? super V>> MutableSortedBag<T> toSortedBagBy(Function<? super T, ? extends V> function)
    {
        return TreeBag.newBag(Comparators.byFunction(function));
    }

    @Override
    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return UnifiedMap.newMap();
    }

    @Override
    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return TreeSortedMap.newMap();
    }

    @Override
    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return TreeSortedMap.newMap(comparator);
    }

    @Override
    public LazyIterable<T> asLazy()
    {
        return (LazyIterable<T>) LAZY_ITERABLE;
    }

    @Override
    public Object[] toArray()
    {
        return TO_ARRAY;
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        if (a.length > 0)
        {
            a[0] = null;
        }
        return a;
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T min()
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max()
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public String toString()
    {
        return "[]";
    }

    @Override
    public String makeString()
    {
        return "";
    }

    @Override
    public String makeString(String separator)
    {
        return "";
    }

    @Override
    public String makeString(String start, String separator, String end)
    {
        return start + end;
    }

    @Override
    public void appendString(Appendable appendable)
    {
    }

    @Override
    public void appendString(Appendable appendable, String separator)
    {
    }

    @Override
    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    public <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return Bags.immutable.empty();
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    public ImmutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return Sets.immutable.empty();
    }

    @Override
    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }
        return Bags.immutable.empty();
    }

    private Object writeReplace()
    {
        return new ImmutableBagSerializationProxy<T>(this);
    }
}
