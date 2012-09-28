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

package com.gs.collections.impl.lazy;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.primitive.DoubleIterable;
import com.gs.collections.api.primitive.FloatIterable;
import com.gs.collections.api.primitive.IntIterable;
import com.gs.collections.api.primitive.LongIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.lazy.primitive.CollectDoubleIterable;
import com.gs.collections.impl.lazy.primitive.CollectFloatIterable;
import com.gs.collections.impl.lazy.primitive.CollectIntIterable;
import com.gs.collections.impl.lazy.primitive.CollectLongIterable;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;
import net.jcip.annotations.Immutable;

/**
 * AbstractLazyIterable provides a base from which deferred iterables such as SelectIterable,
 * RejectIterable and CollectIterable can be derived.
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
        this.forEachWith(Procedures2.<T>addToCollection(), target);
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

    public LazyIterable<T> select(Predicate<? super T> predicate)
    {
        return LazyIterate.select(this, predicate);
    }

    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return IterableIterate.select(this, predicate, target);
    }

    public <P, R extends Collection<T>> R selectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return IterableIterate.selectWith(this, predicate, parameter, targetCollection);
    }

    public LazyIterable<T> reject(Predicate<? super T> predicate)
    {
        return LazyIterate.reject(this, predicate);
    }

    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return IterableIterate.reject(this, predicate, target);
    }

    public <P, R extends Collection<T>> R rejectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return IterableIterate.rejectWith(this, predicate, parameter, targetCollection);
    }

    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return PartitionFastList.of(this, predicate);
    }

    public <S> LazyIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return LazyIterate.selectInstancesOf(this, clazz);
    }

    public <V> LazyIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return LazyIterate.collect(this, function);
    }

    public IntIterable collectInt(IntFunction<? super T> function)
    {
        return new CollectIntIterable<T>(this, function);
    }

    public FloatIterable collectFloat(FloatFunction<? super T> function)
    {
        return new CollectFloatIterable<T>(this, function);
    }

    public DoubleIterable collectDouble(DoubleFunction<? super T> function)
    {
        return new CollectDoubleIterable<T>(this, function);
    }

    public LongIterable collectLong(LongFunction<? super T> function)
    {
        return new CollectLongIterable<T>(this, function);
    }

    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return IterableIterate.collect(this, function, target);
    }

    public <P, V, R extends Collection<V>> R collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return IterableIterate.collectWith(this, function, parameter, targetCollection);
    }

    public <V> LazyIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return LazyIterate.flatCollect(this, function);
    }

    public LazyIterable<T> concatenate(Iterable<T> iterable)
    {
        return LazyIterate.concatenate(this, iterable);
    }

    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return IterableIterate.flatCollect(this, function, target);
    }

    public <V> LazyIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return LazyIterate.collectIf(this, predicate, function);
    }

    public <V, R extends Collection<V>> R collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return IterableIterate.collectIf(this, predicate, function, target);
    }

    public LazyIterable<T> take(int count)
    {
        return LazyIterate.take(this, count);
    }

    public LazyIterable<T> drop(int count)
    {
        return LazyIterate.drop(this, count);
    }

    public T detect(Predicate<? super T> predicate)
    {
        return IterableIterate.detect(this, predicate);
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

    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.detect(predicate);
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

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        return IterableIterate.sumOfInt(this, function);
    }

    public double sumOfFloat(FloatFunction<? super T> function)
    {
        return IterableIterate.sumOfFloat(this, function);
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        return IterableIterate.sumOfLong(this, function);
    }

    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        return IterableIterate.sumOfDouble(this, function);
    }

    public MutableList<T> toList()
    {
        MutableList<T> list = Lists.mutable.of();
        this.forEachWith(Procedures2.<T>addToCollection(), list);
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
        this.forEachWith(Procedures2.<T>addToCollection(), treeSet);
        return treeSet;
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        MutableSortedSet<T> treeSet = TreeSortedSet.newSet(comparator);
        this.forEachWith(Procedures2.<T>addToCollection(), treeSet);
        return treeSet;
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public MutableSet<T> toSet()
    {
        MutableSet<T> set = UnifiedSet.newSet();
        this.forEachWith(Procedures2.<T>addToCollection(), set);
        return set;
    }

    public MutableBag<T> toBag()
    {
        MutableBag<T> bag = Bags.mutable.of();
        this.forEachWith(Procedures2.<T>addToCollection(), bag);
        return bag;
    }

    public MutableStack<T> toStack()
    {
        return ArrayStack.newStack(this);
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        UnifiedMap<NK, NV> map = UnifiedMap.newMap();
        this.forEach(new MapCollectProcedure<T, NK, NV>(map, keyFunction, valueFunction));
        return map;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        TreeSortedMap<NK, NV> sortedMap = TreeSortedMap.newMap();
        this.forEach(new MapCollectProcedure<T, NK, NV>(sortedMap, keyFunction, valueFunction));
        return sortedMap;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        TreeSortedMap<NK, NV> sortedMap = TreeSortedMap.newMap(comparator);
        this.forEach(new MapCollectProcedure<T, NK, NV>(sortedMap, keyFunction, valueFunction));
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
