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

package com.webguys.ponzu.impl.map.sorted.mutable;

import java.util.Collection;
import java.util.Iterator;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.map.sorted.ImmutableSortedMap;
import com.webguys.ponzu.api.map.sorted.MutableSortedMap;
import com.webguys.ponzu.api.multimap.list.MutableListMultimap;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.procedure.MapTransformProcedure;
import com.webguys.ponzu.impl.factory.SortedMaps;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.AbstractMapIterable;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.list.FastListMultimap;
import com.webguys.ponzu.impl.partition.list.PartitionFastList;
import com.webguys.ponzu.impl.tuple.AbstractImmutableEntry;
import com.webguys.ponzu.impl.utility.Iterate;
import com.webguys.ponzu.impl.utility.LazyIterate;
import com.webguys.ponzu.impl.utility.MapIterate;

public abstract class AbstractMutableSortedMap<K, V> extends AbstractMapIterable<K, V>
        implements MutableSortedMap<K, V>
{
    public MutableSortedMap<K, V> asUnmodifiable()
    {
        return UnmodifiableTreeMap.of(this);
    }

    public ImmutableSortedMap<K, V> toImmutable()
    {
        return SortedMaps.immutable.ofSortedMap(this);
    }

    public MutableSortedMap<K, V> asSynchronized()
    {
        return SynchronizedSortedMap.of(this);
    }

    public V getIfAbsentPut(K key, Generator<? extends V> function)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.value();
            this.put(key, result);
        }
        return result;
    }

    public <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.valueOf(parameter);
            this.put(key, result);
        }
        return result;
    }

    public RichIterable<K> keysView()
    {
        return LazyIterate.adapt(this.keySet());
    }

    public RichIterable<V> valuesView()
    {
        return LazyIterate.adapt(this.values());
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return LazyIterate.adapt(this.entrySet()).transform(AbstractImmutableEntry.<K, V>getPairFunction());
    }

    public Iterator<V> iterator()
    {
        return this.values().iterator();
    }

    public <K2, V2> MutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return MapIterate.transform(this, function, UnifiedMap.<K2, V2>newMap(this.size()));
    }

    public <E> MutableSortedMap<K, V> transformKeysAndValues(Collection<E> collection, Function<? super E, ? extends K> keyFunction, Function<? super E, ? extends V> valueFunction)
    {
        Iterate.forEach(collection, new MapTransformProcedure<E, K, V>(this, keyFunction, valueFunction));
        return this;
    }

    public <R> MutableSortedMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return MapIterate.transformValues(this, function, TreeSortedMap.<K, R>newMap(this.comparator()));
    }

    public MutableSortedMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterMapOnEntry(this, predicate, this.newEmpty());
    }

    public MutableSortedMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterNotMapOnEntry(this, predicate, this.newEmpty());
    }

    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.find(this, predicate);
    }

    @Override
    public <R> MutableList<R> transform(Function<? super V, ? extends R> function)
    {
        return this.transform(function, FastList.<R>newList(this.size()));
    }

    @Override
    public <R> MutableList<R> transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.transformIf(predicate, function, FastList.<R>newList(this.size()));
    }

    @Override
    public <R> MutableList<R> flatTransform(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.flatTransform(function, FastList.<R>newList(this.size()));
    }

    @Override
    public MutableList<V> filterNot(Predicate<? super V> predicate)
    {
        return this.filterNot(predicate, FastList.<V>newList(this.size()));
    }

    @Override
    public MutableList<V> filter(Predicate<? super V> predicate)
    {
        return this.filter(predicate, FastList.<V>newList(this.size()));
    }

    public PartitionMutableList<V> partition(Predicate<? super V> predicate)
    {
        return PartitionFastList.of(this, predicate);
    }

    @Override
    public <S> MutableList<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<V, S>>newList(this.size()));
    }

    @Override
    public MutableList<Pair<V, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(FastList.<Pair<V, Integer>>newList(this.size()));
    }

    @Override
    public MutableSortedMap<K, V> clone()
    {
        try
        {
            return (MutableSortedMap<K, V>) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    public <VV> MutableListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function)
    {
        return this.groupBy(function, FastListMultimap.<VV, V>newMultimap());
    }

    public <VV> MutableListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.groupByEach(function, FastListMultimap.<VV, V>newMultimap());
    }
}
