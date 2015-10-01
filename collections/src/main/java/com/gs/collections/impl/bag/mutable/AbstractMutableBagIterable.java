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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBagIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.bag.AbstractBag;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

public abstract class AbstractMutableBagIterable<T>
        extends AbstractBag<T>
        implements MutableBagIterable<T>
{
    protected abstract RichIterable<T> getKeysView();

    public boolean addAll(Collection<? extends T> source)
    {
        return this.addAllIterable(source);
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        int oldSize = this.size();
        Iterate.forEachWith(iterable, Procedures2.<T>addToCollection(), this);
        return oldSize != this.size();
    }

    public boolean removeAll(Collection<?> collection)
    {
        return this.removeAllIterable(collection);
    }

    public boolean retainAll(Collection<?> collection)
    {
        return this.retainAllIterable(collection);
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        int oldSize = this.size();
        this.removeIfWith(Predicates2.notIn(), UnifiedSet.newSet(iterable));
        return this.size() != oldSize;
    }

    public <P> Twin<MutableList<T>> selectAndRejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return IterableIterate.selectAndRejectWith(this, predicate, parameter);
    }

    public T getFirst()
    {
        return this.getKeysView().getFirst();
    }

    public T getLast()
    {
        return this.getKeysView().getLast();
    }

    public <V> MutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V, T>newMap());
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        Iterator<T> iterator = this.iterator();
        MutableList<RichIterable<T>> result = Lists.mutable.empty();
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

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return this.getKeysView().detect(predicate);
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getKeysView().detectWith(predicate, parameter);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return this.getKeysView().detectIfNone(predicate, function);
    }

    @Override
    public <P> T detectWithIfNone(
            final Predicate2<? super T, ? super P> predicate,
            final P parameter,
            Function0<? extends T> function)
    {
        return this.getKeysView().detectIfNone(new Predicate<T>()
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
        return this.getKeysView().anySatisfy(predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getKeysView().anySatisfyWith(predicate, parameter);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.getKeysView().allSatisfy(predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getKeysView().allSatisfyWith(predicate, parameter);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return this.getKeysView().noneSatisfy(predicate);
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getKeysView().noneSatisfyWith(predicate, parameter);
    }

    @Override
    public T min()
    {
        return this.getKeysView().min();
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return this.getKeysView().min(comparator);
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return this.getKeysView().minBy(function);
    }

    @Override
    public T max()
    {
        return this.getKeysView().max();
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return this.getKeysView().max(comparator);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return this.getKeysView().maxBy(function);
    }

    public <K, V> MutableMap<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K, V> MutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }

    public MutableList<ObjectIntPair<T>> topOccurrences(int n)
    {
        return this.occurrencesSortingBy(n, new IntFunction<ObjectIntPair<T>>()
        {
            public int intValueOf(ObjectIntPair<T> item)
            {
                return -item.getTwo();
            }
        }, Lists.mutable.<ObjectIntPair<T>>empty());
    }

    public MutableList<ObjectIntPair<T>> bottomOccurrences(int n)
    {
        return this.occurrencesSortingBy(n, new IntFunction<ObjectIntPair<T>>()
        {
            public int intValueOf(ObjectIntPair<T> item)
            {
                return item.getTwo();
            }
        }, Lists.mutable.<ObjectIntPair<T>>empty());
    }
}
