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

package ponzu.impl.bag.immutable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.jcip.annotations.Immutable;
import ponzu.api.LazyIterable;
import ponzu.api.RichIterable;
import ponzu.api.bag.Bag;
import ponzu.api.bag.ImmutableBag;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Generator;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.map.sorted.MutableSortedMap;
import ponzu.api.multimap.MutableMultimap;
import ponzu.api.multimap.bag.ImmutableBagMultimap;
import ponzu.api.partition.bag.PartitionImmutableBag;
import ponzu.api.set.MutableSet;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.EmptyIterator;
import ponzu.impl.bag.mutable.HashBag;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.factory.Bags;
import ponzu.impl.factory.Lists;
import ponzu.impl.factory.Maps;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.map.sorted.mutable.TreeSortedMap;
import ponzu.impl.multimap.bag.HashBagMultimap;
import ponzu.impl.partition.bag.PartitionHashBag;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.set.sorted.mutable.TreeSortedSet;
import ponzu.impl.utility.ArrayIterate;
import ponzu.impl.utility.Iterate;
import ponzu.impl.utility.LazyIterate;

/**
 * This is a zero element {@link ImmutableBag} which is created by calling the Bags.immutable.of().
 *
 * @since 1.0
 */
@Immutable
final class ImmutableEmptyBag<T>
        implements ImmutableBag<T>, Serializable
{
    static final ImmutableBag<?> INSTANCE = new ImmutableEmptyBag();

    private static final long serialVersionUID = 1L;

    private static final LazyIterable<?> LAZY_ITERABLE = LazyIterate.adapt(INSTANCE);
    private static final Object[] TO_ARRAY = new Object[0];

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
        return Maps.mutable.of();
    }

    public ImmutableBag<T> toImmutable()
    {
        return this;
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

    public boolean isEmpty()
    {
        return true;
    }

    public boolean notEmpty()
    {
        return false;
    }

    public boolean contains(Object object)
    {
        return false;
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.containsAllIterable(source);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.isEmpty(source);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.isEmpty(elements);
    }

    public void forEach(Procedure<? super T> procedure)
    {
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

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
        return Bags.immutable.of(element);
    }

    public ImmutableBag<T> newWithout(T element)
    {
        return this;
    }

    public ImmutableBag<T> newWithAll(Iterable<? extends T> elements)
    {
        return HashBag.newBag(elements).toImmutable();
    }

    public ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this;
    }

    public ImmutableBag<T> filter(Predicate<? super T> predicate)
    {
        return this;
    }

    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        return target;
    }

    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return targetCollection;
    }

    public ImmutableBag<T> filterNot(Predicate<? super T> predicate)
    {
        return this;
    }

    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return target;
    }

    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return targetCollection;
    }

    public PartitionImmutableBag<T> partition(Predicate<? super T> predicate)
    {
        return PartitionHashBag.of(this, predicate).toImmutable();
    }

    public <V> ImmutableBag<V> transform(Function<? super T, ? extends V> function)
    {
        return (ImmutableBag<V>) INSTANCE;
    }

    public <V, R extends Collection<V>> R transform(Function<? super T, ? extends V> function, R target)
    {
        return target;
    }

    public <P, V, R extends Collection<V>> R transformWith(
            Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return targetCollection;
    }

    public <V> ImmutableBag<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return (ImmutableBag<V>) INSTANCE;
    }

    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        return target;
    }

    public <V> ImmutableBag<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return (ImmutableBag<V>) INSTANCE;
    }

    public <V, R extends Collection<V>> R flatTransform(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return target;
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

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function, R target)
    {
        return target;
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return target;
    }

    public T find(Predicate<? super T> predicate)
    {
        return null;
    }

    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
    {
        return function.value();
    }

    public int count(Predicate<? super T> predicate)
    {
        return 0;
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return true;
    }

    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return initialValue;
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super T> function)
    {
        return initialValue;
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super T> function)
    {
        return initialValue;
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return initialValue;
    }

    public MutableList<T> toList()
    {
        return Lists.mutable.of();
    }

    public MutableList<T> toSortedList()
    {
        return Lists.mutable.of();
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return Lists.mutable.of();
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return Lists.mutable.of();
    }

    public MutableSortedSet<T> toSortedSet()
    {
        return TreeSortedSet.newSet();
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return TreeSortedSet.newSet(comparator);
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return TreeSortedSet.newSet(Comparators.byFunction(function));
    }

    public MutableSet<T> toSet()
    {
        return UnifiedSet.newSet();
    }

    public MutableBag<T> toBag()
    {
        return Bags.mutable.of();
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return UnifiedMap.newMap();
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return TreeSortedMap.newMap();
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        return TreeSortedMap.newMap(comparator);
    }

    public LazyIterable<T> asLazy()
    {
        return (LazyIterable<T>) LAZY_ITERABLE;
    }

    public Object[] toArray()
    {
        return TO_ARRAY;
    }

    public <T> T[] toArray(T[] a)
    {
        if (a.length > 0)
        {
            a[0] = null;
        }
        return a;
    }

    public T min(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    public T max(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    public T min()
    {
        throw new NoSuchElementException();
    }

    public T max()
    {
        throw new NoSuchElementException();
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public String toString()
    {
        return "[]";
    }

    public String makeString()
    {
        return "";
    }

    public String makeString(String separator)
    {
        return "";
    }

    public String makeString(String start, String separator, String end)
    {
        return start + end;
    }

    public void appendString(Appendable appendable)
    {
    }

    public void appendString(Appendable appendable, String separator)
    {
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new AssertionError(e);
        }
    }

    public <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return Bags.immutable.of();
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return target;
    }

    public ImmutableBag<Pair<T, Integer>> zipWithIndex()
    {
        return Bags.immutable.of();
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return target;
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }
        return Bags.immutable.of();
    }

    private Object writeReplace()
    {
        return new AbstractImmutableBag.ImmutableBagSerializationProxy<T>(this);
    }
}
