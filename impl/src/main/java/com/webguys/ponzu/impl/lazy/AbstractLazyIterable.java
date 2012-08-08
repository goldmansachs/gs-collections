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

package com.webguys.ponzu.impl.lazy;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.bag.MutableBag;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.webguys.ponzu.api.block.function.primitive.IntObjectToIntFunction;
import com.webguys.ponzu.api.block.function.primitive.LongObjectToLongFunction;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.map.sorted.MutableSortedMap;
import com.webguys.ponzu.api.multimap.ImmutableMultimap;
import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.set.sorted.MutableSortedSet;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.block.procedure.CountProcedure;
import com.webguys.ponzu.impl.block.procedure.MapTransformProcedure;
import com.webguys.ponzu.impl.factory.Bags;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.map.sorted.mutable.TreeSortedMap;
import com.webguys.ponzu.impl.multimap.list.FastListMultimap;
import com.webguys.ponzu.impl.partition.list.PartitionFastList;
import com.webguys.ponzu.impl.set.mutable.UnifiedSet;
import com.webguys.ponzu.impl.set.sorted.mutable.TreeSortedSet;
import com.webguys.ponzu.impl.utility.ArrayIterate;
import com.webguys.ponzu.impl.utility.Iterate;
import com.webguys.ponzu.impl.utility.LazyIterate;
import com.webguys.ponzu.impl.utility.internal.IterableIterate;
import net.jcip.annotations.Immutable;

/**
 * AbstractLazyIterable provides a base from which deferred iterables such as FilterIterable,
 * FilterNotIterable and TransformIterable can be derived.
 */
@Immutable
public abstract class AbstractLazyIterable<T>
        implements LazyIterable<T>
{
    public LazyIterable<T> asLazy()
    {
        return this;
    }

    public <R extends Collection<T>> R into(R target)
    {
        this.forEach(CollectionAddProcedure.on(target));
        return target;
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public Object[] toArray()
    {
        final Object[] result = new Object[this.size()];
        this.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                result[index] = each;
            }
        });
        return result;
    }

    public <E> E[] toArray(E[] array)
    {
        int size = this.size();

        final E[] result = array.length < size
                ? (E[]) Array.newInstance(array.getClass().getComponentType(), size)
                : array;

        this.forEachWithIndex(new ObjectIntProcedure<Object>()
        {
            public void value(Object each, int index)
            {
                result[index] = (E) each;
            }
        });
        if (result.length > size)
        {
            result[size] = null;
        }
        return result;
    }

    public boolean contains(Object object)
    {
        return this.anySatisfy(Predicates.equal(object));
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.allSatisfy(source, new Predicate<Object>()
        {
            public boolean accept(Object each)
            {
                return AbstractLazyIterable.this.contains(each);
            }
        });
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.containsAllIterable(source);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.allSatisfy(elements, new Predicate<Object>()
        {
            public boolean accept(Object each)
            {
                return AbstractLazyIterable.this.contains(each);
            }
        });
    }

    public int size()
    {
        return this.count(Predicates.alwaysTrue());
    }

    public boolean isEmpty()
    {
        return IterableIterate.isEmpty(this);
    }

    public boolean notEmpty()
    {
        return IterableIterate.notEmpty(this);
    }

    public T getFirst()
    {
        return IterableIterate.getFirst(this);
    }

    public T getLast()
    {
        return IterableIterate.getLast(this);
    }

    public LazyIterable<T> filter(Predicate<? super T> predicate)
    {
        return LazyIterate.filter(this, predicate);
    }

    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        return IterableIterate.filter(this, predicate, target);
    }

    public <P, R extends Collection<T>> R filterWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return IterableIterate.filterWith(this, predicate, parameter, targetCollection);
    }

    public LazyIterable<T> filterNot(Predicate<? super T> predicate)
    {
        return LazyIterate.filterNot(this, predicate);
    }

    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return IterableIterate.filterNot(this, predicate, target);
    }

    public <P, R extends Collection<T>> R filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return IterableIterate.filterNotWith(this, predicate, parameter, targetCollection);
    }

    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return PartitionFastList.of(this, predicate);
    }

    public <V> LazyIterable<V> transform(Function<? super T, ? extends V> function)
    {
        return LazyIterate.transform(this, function);
    }

    public <V, R extends Collection<V>> R transform(Function<? super T, ? extends V> function, R target)
    {
        return IterableIterate.transform(this, function, target);
    }

    public <P, V, R extends Collection<V>> R transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return IterableIterate.transformWith(this, function, parameter, targetCollection);
    }

    public <V> LazyIterable<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return LazyIterate.flatTransform(this, function);
    }

    public LazyIterable<T> concatenate(Iterable<T> iterable)
    {
        return LazyIterate.concatenate(this, iterable);
    }

    public <V, R extends Collection<V>> R flatTransform(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return IterableIterate.flatTransform(this, function, target);
    }

    public <V> LazyIterable<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return LazyIterate.transformIf(this, predicate, function);
    }

    public <V, R extends Collection<V>> R transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return IterableIterate.tranformIf(this, predicate, function, target);
    }

    public LazyIterable<T> take(int count)
    {
        return LazyIterate.take(this, count);
    }

    public LazyIterable<T> drop(int count)
    {
        return LazyIterate.drop(this, count);
    }

    public T find(Predicate<? super T> predicate)
    {
        return IterableIterate.find(this, predicate);
    }

    public T min(Comparator<? super T> comparator)
    {
        return Iterate.min(this, comparator);
    }

    public T max(Comparator<? super T> comparator)
    {
        return Iterate.max(this, comparator);
    }

    public T min()
    {
        return Iterate.min(this);
    }

    public T max()
    {
        return Iterate.max(this);
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return Iterate.min(this, Comparators.byFunction(function));
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return Iterate.max(this, Comparators.byFunction(function));
    }

    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
    {
        T result = this.find(predicate);
        return result == null ? function.value() : result;
    }

    public int count(Predicate<? super T> predicate)
    {
        CountProcedure<T> procedure = new CountProcedure<T>(predicate);
        this.forEach(procedure);
        return procedure.getCount();
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return IterableIterate.anySatisfy(this, predicate);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return IterableIterate.allSatisfy(this, predicate);
    }

    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super T> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super T> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public MutableList<T> toList()
    {
        MutableList<T> list = Lists.mutable.of();
        this.forEach(CollectionAddProcedure.on(list));
        return list;
    }

    public MutableList<T> toSortedList()
    {
        return this.toList().sortThis();
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return this.toList().sortThis(comparator);
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedList(Comparators.byFunction(function));
    }

    public MutableSortedSet<T> toSortedSet()
    {
        MutableSortedSet<T> treeSet = TreeSortedSet.newSet();
        this.forEach(CollectionAddProcedure.on(treeSet));
        return treeSet;
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        MutableSortedSet<T> treeSet = TreeSortedSet.newSet(comparator);
        this.forEach(CollectionAddProcedure.on(treeSet));
        return treeSet;
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public MutableSet<T> toSet()
    {
        MutableSet<T> set = UnifiedSet.newSet();
        this.forEach(CollectionAddProcedure.on(set));
        return set;
    }

    public MutableBag<T> toBag()
    {
        MutableBag<T> bag = Bags.mutable.of();
        this.forEach(CollectionAddProcedure.on(bag));
        return bag;
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        UnifiedMap<NK, NV> map = UnifiedMap.newMap();
        this.forEach(new MapTransformProcedure<T, NK, NV>(map, keyFunction, valueFunction));
        return map;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        TreeSortedMap<NK, NV> sortedMap = TreeSortedMap.newMap();
        this.forEach(new MapTransformProcedure<T, NK, NV>(sortedMap, keyFunction, valueFunction));
        return sortedMap;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        TreeSortedMap<NK, NV> sortedMap = TreeSortedMap.newMap(comparator);
        this.forEach(new MapTransformProcedure<T, NK, NV>(sortedMap, keyFunction, valueFunction));
        return sortedMap;
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        IterableIterate.appendString(this, appendable, start, separator, end);
    }

    public <V> ImmutableMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return IterableIterate.groupBy(this, function, FastListMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        return IterableIterate.groupBy(this, function, target);
    }

    public <V> ImmutableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return IterableIterate.groupByEach(this, function, FastListMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return IterableIterate.groupByEach(this, function, target);
    }

    public <S> LazyIterable<Pair<T, S>> zip(Iterable<S> that)
    {
        return LazyIterate.zip(this, that);
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return IterableIterate.zip(this, that, target);
    }

    public LazyIterable<Pair<T, Integer>> zipWithIndex()
    {
        return LazyIterate.zipWithIndex(this);
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return IterableIterate.zipWithIndex(this, target);
    }

    public LazyIterable<RichIterable<T>> chunk(int size)
    {
        return LazyIterate.chunk(this, size);
    }
}
