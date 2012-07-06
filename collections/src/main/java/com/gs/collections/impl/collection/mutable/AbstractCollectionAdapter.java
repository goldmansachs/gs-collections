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

package com.gs.collections.impl.collection.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;
import com.gs.collections.impl.utility.internal.MutableCollectionIterate;

public abstract class AbstractCollectionAdapter<T>
        implements MutableCollection<T>
{
    protected abstract Collection<T> getDelegate();

    protected <E> MutableCollection<E> wrap(Collection<E> collection)
    {
        return CollectionAdapter.adapt(collection);
    }

    public boolean notEmpty()
    {
        return !this.getDelegate().isEmpty();
    }

    public T getFirst()
    {
        return Iterate.getFirst(this.getDelegate());
    }

    public T getLast()
    {
        return Iterate.getLast(this.getDelegate());
    }

    public void forEach(Procedure<? super T> procedure)
    {
        Iterate.forEach(this.getDelegate(), procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        Iterate.forEachWithIndex(this.getDelegate(), objectIntProcedure);
    }

    public void removeIf(Predicate<? super T> predicate)
    {
        Iterate.removeIf(this.getDelegate(), predicate);
    }

    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        Iterate.removeIfWith(this.getDelegate(), predicate, parameter);
    }

    public T detect(Predicate<? super T> predicate)
    {
        return Iterate.detect(this.getDelegate(), predicate);
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
        if (result == null)
        {
            result = function.value();
        }
        return result;
    }

    public int count(Predicate<? super T> predicate)
    {
        return Iterate.count(this.getDelegate(), predicate);
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return Iterate.anySatisfy(this.getDelegate(), predicate);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return Iterate.allSatisfy(this.getDelegate(), predicate);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return Iterate.injectInto(injectedValue, this.getDelegate(), function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        return Iterate.injectInto(injectedValue, this.getDelegate(), function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        return Iterate.injectInto(injectedValue, this.getDelegate(), function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return Iterate.injectInto(injectedValue, this.getDelegate(), function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        return Iterate.injectInto(injectedValue, this.getDelegate(), function);
    }

    public int sumOf(IntFunction<? super T> function)
    {
        return Iterate.sumOf(this.getDelegate(), function);
    }

    public float sumOf(FloatFunction<? super T> function)
    {
        return Iterate.sumOf(this.getDelegate(), function);
    }

    public long sumOf(LongFunction<? super T> function)
    {
        return Iterate.sumOf(this.getDelegate(), function);
    }

    public double sumOf(DoubleFunction<? super T> function)
    {
        return Iterate.sumOf(this.getDelegate(), function);
    }

    public MutableCollection<T> select(Predicate<? super T> predicate)
    {
        return this.wrap(Iterate.select(this.getDelegate(), predicate));
    }

    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return Iterate.select(this.getDelegate(), predicate, target);
    }

    public MutableCollection<T> reject(Predicate<? super T> predicate)
    {
        return this.wrap(Iterate.reject(this.getDelegate(), predicate));
    }

    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return Iterate.reject(this.getDelegate(), predicate, target);
    }

    public <V> MutableCollection<V> collect(Function<? super T, ? extends V> function)
    {
        return this.wrap(Iterate.collect(this.getDelegate(), function));
    }

    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return Iterate.collect(this.getDelegate(), function, target);
    }

    public <V> MutableCollection<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.wrap(Iterate.flatCollect(this.getDelegate(), function));
    }

    public <V, R extends Collection<V>> R flatCollect(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return Iterate.flatCollect(this.getDelegate(), function, target);
    }

    public <V> MutableCollection<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.wrap(Iterate.collectIf(this.getDelegate(), predicate, function));
    }

    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        return Iterate.collectIf(this.getDelegate(), predicate, function, target);
    }

    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return Iterate.selectAndRejectWith(this.getDelegate(), predicate, parameter);
    }

    public PartitionMutableCollection<T> partition(Predicate<? super T> predicate)
    {
        return (PartitionMutableCollection<T>) Iterate.partition(this.getDelegate(), predicate);
    }

    public int size()
    {
        return this.getDelegate().size();
    }

    public boolean isEmpty()
    {
        return this.getDelegate().isEmpty();
    }

    public boolean contains(Object o)
    {
        return this.getDelegate().contains(o);
    }

    public Iterator<T> iterator()
    {
        return this.getDelegate().iterator();
    }

    public Object[] toArray()
    {
        return this.getDelegate().toArray();
    }

    public <E> E[] toArray(E[] a)
    {
        return this.getDelegate().toArray(a);
    }

    public boolean add(T o)
    {
        return this.getDelegate().add(o);
    }

    public boolean remove(Object o)
    {
        return this.getDelegate().remove(o);
    }

    public boolean containsAll(Collection<?> collection)
    {
        return this.getDelegate().containsAll(collection);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.allSatisfyWith(source, Predicates2.in(), this.getDelegate());
    }

    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.allSatisfyWith(elements, Predicates2.in(), this.getDelegate());
    }

    public boolean addAll(Collection<? extends T> collection)
    {
        boolean result = false;
        for (T each : collection)
        {
            result |= this.add(each);
        }
        return result;
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        return Iterate.addAllIterable(iterable, this);
    }

    public boolean removeAll(Collection<?> collection)
    {
        int currentSize = this.size();
        this.removeIfWith(Predicates2.in(), collection);
        return currentSize != this.size();
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        return this.removeAll(CollectionAdapter.wrapSet(iterable));
    }

    public boolean retainAll(Collection<?> collection)
    {
        int currentSize = this.size();
        this.removeIfWith(Predicates2.notIn(), collection);
        return currentSize != this.size();
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        return this.retainAll(CollectionAdapter.wrapSet(iterable));
    }

    public void clear()
    {
        this.getDelegate().clear();
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.getDelegate(), procedure, parameter);
    }

    public <P> MutableCollection<T> selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.wrap(Iterate.selectWith(this.getDelegate(), predicate, parameter));
    }

    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return Iterate.selectWith(this.getDelegate(), predicate, parameter, targetCollection);
    }

    public <P> MutableCollection<T> rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.wrap(Iterate.rejectWith(this.getDelegate(), predicate, parameter));
    }

    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return Iterate.rejectWith(this.getDelegate(), predicate, parameter, targetCollection);
    }

    public <P, V> MutableCollection<V> collectWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter)
    {
        return this.wrap(Iterate.collectWith(this.getDelegate(), function, parameter));
    }

    public <P, A, R extends Collection<A>> R collectWith(
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        return Iterate.collectWith(this.getDelegate(), function, parameter, targetCollection);
    }

    public <IV, P> IV injectIntoWith(
            IV injectValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        return Iterate.injectIntoWith(injectValue, this.getDelegate(), function, parameter);
    }

    public MutableList<T> toList()
    {
        return Lists.mutable.ofAll(this.getDelegate());
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
        return TreeSortedSet.newSet(null, this);
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return TreeSortedSet.newSet(comparator, this);
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(
            Function<? super T, ? extends V> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public MutableSet<T> toSet()
    {
        return UnifiedSet.newSet(this.getDelegate());
    }

    public MutableBag<T> toBag()
    {
        return HashBag.newBag(this.getDelegate());
    }

    public <K, V> MutableMap<K, V> toMap(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this.size());
        map.collectKeysAndValues(this.getDelegate(), keyFunction, valueFunction);
        return map;
    }

    public <K, V> MutableSortedMap<K, V> toSortedMap(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return TreeSortedMap.<K, V>newMap().collectKeysAndValues(this.getDelegate(), keyFunction, valueFunction);
    }

    public <K, V> MutableSortedMap<K, V> toSortedMap(Comparator<? super K> comparator,
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return TreeSortedMap.<K, V>newMap(comparator).collectKeysAndValues(this.getDelegate(), keyFunction, valueFunction);
    }

    public LazyIterable<T> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.detectWith(this.getDelegate(), predicate, parameter);
    }

    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        T result = this.detectWith(predicate, parameter);
        if (result == null)
        {
            result = function.value();
        }
        return result;
    }

    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.countWith(this.getDelegate(), predicate, parameter);
    }

    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.anySatisfyWith(this.getDelegate(), predicate, parameter);
    }

    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.allSatisfyWith(this.getDelegate(), predicate, parameter);
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
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

    public <V> MutableMultimap<V, T> groupBy(
            Function<? super T, ? extends V> function)
    {
        return Iterate.groupBy(this.getDelegate(), function);
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        return Iterate.groupBy(this.getDelegate(), function, target);
    }

    public <V> MutableMultimap<V, T> groupByEach(
            Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.groupByEach(this.getDelegate(), function);
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return Iterate.groupByEach(this.getDelegate(), function, target);
    }

    public <S> MutableCollection<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.wrap(Iterate.zip(this.getDelegate(), that));
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return Iterate.zip(this.getDelegate(), that, target);
    }

    public MutableCollection<Pair<T, Integer>> zipWithIndex()
    {
        return this.wrap(Iterate.zipWithIndex(this.getDelegate()));
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return Iterate.zipWithIndex(this.getDelegate(), target);
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        return MutableCollectionIterate.chunk(this, size);
    }
}
