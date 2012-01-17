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

import java.util.AbstractCollection;
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
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
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
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

public abstract class AbstractMutableCollection<T>
        extends AbstractCollection<T>
        implements MutableCollection<T>
{
    public boolean notEmpty()
    {
        return !this.isEmpty();
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.allSatisfyWith(source, Predicates2.in(), this);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.allSatisfyWith(elements, Predicates2.in(), this);
    }

    public <K, V> MutableMap<K, V> toMap(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return UnifiedMap.<K, V>newMap(this.size()).transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public <K, V> MutableSortedMap<K, V> toSortedMap(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return TreeSortedMap.<K, V>newMap().transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public <K, V> MutableSortedMap<K, V> toSortedMap(Comparator<? super K> comparator,
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return TreeSortedMap.<K, V>newMap(comparator).transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public LazyIterable<T> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public MutableCollection<T> filter(Predicate<? super T> predicate)
    {
        return this.filter(predicate, FastList.<T>newList());
    }

    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        return IterableIterate.filter(this, predicate, target);
    }

    public <P> MutableCollection<T> filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.filterWith(predicate, parameter, FastList.<T>newList());
    }

    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return IterableIterate.filterWith(this, predicate, parameter, targetCollection);
    }

    public MutableCollection<T> filterNot(Predicate<? super T> predicate)
    {
        return this.filterNot(predicate, FastList.<T>newList());
    }

    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return IterableIterate.filterNot(this, predicate, target);
    }

    public <P> MutableCollection<T> filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.filterNotWith(predicate, parameter, FastList.<T>newList());
    }

    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return IterableIterate.filterNotWith(this, predicate, parameter, targetCollection);
    }

    public <P> Twin<MutableList<T>> partitionWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return IterableIterate.partitionWith(this, predicate, parameter);
    }

    public <V> MutableCollection<V> transform(Function<? super T, ? extends V> function)
    {
        return this.transform(function, Lists.mutable.<V>of());
    }

    public <V, R extends Collection<V>> R transform(
            Function<? super T, ? extends V> function,
            R target)
    {
        return IterableIterate.transform(this, function, target);
    }

    public <V> MutableCollection<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatTransform(function, FastList.<V>newList());
    }

    public <V, R extends Collection<V>> R flatTransform(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return IterableIterate.flatTransform(this, function, target);
    }

    public <P, V> MutableCollection<V> transformWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter)
    {
        return this.transformWith(function, parameter, FastList.<V>newList());
    }

    public <P, V, R extends Collection<V>> R transformWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection)
    {
        return IterableIterate.transformWith(this,
                function,
                parameter,
                targetCollection);
    }

    public <V> MutableCollection<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.transformIf(predicate, function, FastList.<V>newList());
    }

    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        return IterableIterate.tranformIf(this, predicate, function, target);
    }

    public T find(Predicate<? super T> predicate)
    {
        return IterableIterate.find(this, predicate);
    }

    public T findIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = IterableIterate.find(this, predicate);
        return result == null ? function.value() : result;
    }

    public <P> T findWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return IterableIterate.findWith(this, predicate, parameter);
    }

    public <P> T findWithIfNone(Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        T result = IterableIterate.findWith(this, predicate, parameter);
        return result == null ? function.value() : result;
    }

    public int count(Predicate<? super T> predicate)
    {
        return IterableIterate.count(this, predicate);
    }

    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return IterableIterate.countWith(this, predicate, parameter);
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return IterableIterate.anySatisfy(this, predicate);
    }

    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return IterableIterate.anySatisfyWith(this, predicate, parameter);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return IterableIterate.allSatisfy(this, predicate);
    }

    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return IterableIterate.allSatisfyWith(this, predicate, parameter);
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

    public <IV, P> IV foldLeftWith(
            IV initialValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        return IterableIterate.foldLeftWith(initialValue, this, function, parameter);
    }

    public MutableList<T> toList()
    {
        return FastList.newList(this);
    }

    public MutableList<T> toSortedList()
    {
        return this.toSortedList(Comparators.naturalOrder());
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return this.toList().sortThis(comparator);
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedList(Comparators.byFunction(function));
    }

    public MutableSet<T> toSet()
    {
        return UnifiedSet.newSet(this);
    }

    public MutableSortedSet<T> toSortedSet()
    {
        return TreeSortedSet.newSet(null, this);
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return TreeSortedSet.newSet(comparator, this);
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public MutableBag<T> toBag()
    {
        return HashBag.newBag(this);
    }

    public MutableCollection<T> asUnmodifiable()
    {
        return UnmodifiableMutableCollection.of(this);
    }

    public MutableCollection<T> asSynchronized()
    {
        return SynchronizedMutableCollection.of(this);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        IterableIterate.forEach(this, procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        IterableIterate.forEachWithIndex(this, objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        IterableIterate.forEachWith(this, procedure, parameter);
    }

    public void removeIf(Predicate<? super T> predicate)
    {
        IterableIterate.removeIf(this, predicate);
    }

    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        IterableIterate.removeIfWith(this, predicate, parameter);
    }

    public MutableCollection<T> newEmpty()
    {
        throw new UnsupportedOperationException();
    }

    public T getFirst()
    {
        return this.isEmpty() ? null : this.iterator().next();
    }

    public T getLast()
    {
        return this.getFirst(); // not a mistake -- Sets have no implicit ordering!
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

    public void appendString(
            Appendable appendable,
            String start,
            String separator,
            String end)
    {
        IterableIterate.appendString(this, appendable, start, separator, end);
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

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        return IterableIterate.groupBy(this, function, target);
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return IterableIterate.groupByEach(this, function, target);
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(
            Iterable<S> that,
            R target)
    {
        return IterableIterate.zip(this, that, target);
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return IterableIterate.zipWithIndex(this, target);
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        Iterator<T> iterator = this.iterator();
        MutableList<RichIterable<T>> result = Lists.mutable.of();
        while (iterator.hasNext())
        {
            MutableCollection<T> batch = this.newEmpty();
            for (int i = 0; i < size && iterator.hasNext(); i++)
            {
                batch.add(iterator.next());
            }
            result.add(batch);
        }
        return result;
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        return Iterate.addAllIterable(iterable, this);
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        return this.removeAll(CollectionAdapter.wrapSet(iterable));
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        return this.retainAll(CollectionAdapter.wrapSet(iterable));
    }
}
