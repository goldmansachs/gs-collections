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

package com.webguys.ponzu.impl.bag.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.webguys.ponzu.api.bag.Bag;
import com.webguys.ponzu.api.bag.ImmutableBag;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.multimap.bag.ImmutableBagMultimap;
import com.webguys.ponzu.api.partition.bag.PartitionImmutableBag;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.bag.mutable.HashBag;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.factory.Predicates2;
import com.webguys.ponzu.impl.block.procedure.MultimapEachPutProcedure;
import com.webguys.ponzu.impl.factory.Bags;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.bag.HashBagMultimap;
import com.webguys.ponzu.impl.partition.bag.PartitionHashBag;
import com.webguys.ponzu.impl.tuple.Tuples;
import com.webguys.ponzu.impl.utility.ArrayIterate;
import com.webguys.ponzu.impl.utility.Iterate;

/**
 * @since 1.0
 */
final class ImmutableSingletonBag<T>
        extends AbstractImmutableBag<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final T value;

    ImmutableSingletonBag(T object)
    {
        this.value = object;
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return predicate.accept(this.value);
    }

    @Override
    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return function.value(initialValue, this.value);
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return this.value;
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return this.value;
    }

    @Override
    public T min()
    {
        return this.value;
    }

    @Override
    public T max()
    {
        return this.value;
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return this.value;
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return this.value;
    }

    public ImmutableBag<T> newWith(T element)
    {
        return Bags.immutable.of(this.value, element);
    }

    public ImmutableBag<T> newWithout(T element)
    {
        return this.emptyIfMatchesOrThis(Predicates.equal(element));
    }

    private ImmutableBag<T> emptyIfMatchesOrThis(Predicate<Object> predicate)
    {
        return predicate.accept(this.value) ? Bags.immutable.<T>of() : this;
    }

    public ImmutableBag<T> newWithAll(Iterable<? extends T> elements)
    {
        return HashBag.newBag(elements).with(this.value).toImmutable();
    }

    public ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this.emptyIfMatchesOrThis(Predicates.in(elements));
    }

    public int size()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean notEmpty()
    {
        return true;
    }

    public T getFirst()
    {
        return this.value;
    }

    public T getLast()
    {
        return this.value;
    }

    @Override
    public boolean contains(Object object)
    {
        return Comparators.nullSafeEquals(this.value, object);
    }

    @Override
    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.allSatisfy(source, Predicates.equal(this.value));
    }

    @Override
    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.allSatisfy(elements, Predicates.equal(this.value));
    }

    public ImmutableBag<T> filter(Predicate<? super T> predicate)
    {
        return predicate.accept(this.value)
                ? this
                : Bags.immutable.<T>of();
    }

    @Override
    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        if (predicate.accept(this.value))
        {
            target.add(this.value);
        }
        return target;
    }

    @Override
    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        if (predicate.accept(this.value, parameter))
        {
            targetCollection.add(this.value);
        }
        return targetCollection;
    }

    public ImmutableBag<T> filterNot(Predicate<? super T> predicate)
    {
        return this.filter(Predicates.not(predicate));
    }

    @Override
    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return this.filter(Predicates.not(predicate), target);
    }

    @Override
    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return this.filterWith(Predicates2.not(predicate), parameter, targetCollection);
    }

    public PartitionImmutableBag<T> partition(Predicate<? super T> predicate)
    {
        return PartitionHashBag.of(this, predicate).toImmutable();
    }

    public <V> ImmutableBag<V> transform(Function<? super T, ? extends V> function)
    {
        return Bags.immutable.of(function.valueOf(this.value));
    }

    public <V> ImmutableBag<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return predicate.accept(this.value)
                ? Bags.immutable.of(function.valueOf(this.value))
                : Bags.immutable.<V>of();
    }

    @Override
    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        if (predicate.accept(this.value))
        {
            target.add(function.valueOf(this.value));
        }
        return target;
    }

    public <V> ImmutableBag<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.<V, HashBag<V>>flatTransform(function, HashBag.<V>newBag()).toImmutable();
    }

    @Override
    public <V, R extends Collection<V>> R flatTransform(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        Iterate.addAllTo(function.valueOf(this.value), target);
        return target;
    }

    @Override
    public T find(Predicate<? super T> predicate)
    {
        return predicate.accept(this.value)
                ? this.value
                : null;
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
    {
        return predicate.accept(this.value)
                ? this.value
                : function.value();
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return predicate.accept(this.value)
                ? 1
                : 0;
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return predicate.accept(this.value);
    }

    public <V> ImmutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, HashBagMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function, R target)
    {
        target.putAll(function.valueOf(this.value), this);
        return target;
    }

    public <V> ImmutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, HashBagMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        this.forEach(MultimapEachPutProcedure.on(target, function));
        return target;
    }

    public int sizeDistinct()
    {
        return 1;
    }

    public int occurrencesOf(Object item)
    {
        return Comparators.nullSafeEquals(this.value, item) ? 1 : 0;
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        objectIntProcedure.value(this.value, 1);
    }

    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        return UnifiedMap.newWithKeysValues(this.value, 1);
    }

    public ImmutableBag<T> toImmutable()
    {
        return this;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        procedure.value(this.value);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        objectIntProcedure.value(this.value, 0);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        procedure.value(this.value, parameter);
    }

    public <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        Iterator<S> iterator = that.iterator();
        if (!iterator.hasNext())
        {
            return Bags.immutable.of();
        }
        return Bags.immutable.of(Tuples.pair(this.value, iterator.next()));
    }

    public ImmutableBag<Pair<T, Integer>> zipWithIndex()
    {
        return Bags.immutable.of(Tuples.pair(this.value, 0));
    }

    public Iterator<T> iterator()
    {
        return new SingletonIterator();
    }

    private class SingletonIterator
            implements Iterator<T>
    {
        private boolean next = true;

        public boolean hasNext()
        {
            return this.next;
        }

        public T next()
        {
            if (this.next)
            {
                this.next = false;
                return ImmutableSingletonBag.this.value;
            }
            throw new NoSuchElementException("i=" + this.next);
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove from an ImmutableBag");
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Bag))
        {
            return false;
        }
        Bag<?> bag = (Bag<?>) obj;
        if (this.size() != bag.size())
        {
            return false;
        }
        return this.occurrencesOf(this.value) == bag.occurrencesOf(this.value);
    }

    @Override
    public String toString()
    {
        return '[' + this.makeString() + ']';
    }

    @Override
    public int hashCode()
    {
        return (this.value == null ? 0 : this.value.hashCode()) ^ 1;
    }

    private Object writeReplace()
    {
        return new ImmutableBagSerializationProxy<T>(this);
    }
}
