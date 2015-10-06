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

package com.gs.collections.impl.map.sorted.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.utility.LazyIterate;
import net.jcip.annotations.Immutable;

/**
 * This is a zero element {@link ImmutableSortedMap} which is created by calling SortedMaps.immutable.empty().
 */
@Immutable
final class ImmutableEmptySortedMap<K, V>
        extends AbstractImmutableSortedMap<K, V>
        implements Serializable
{
    static final ImmutableSortedMap<?, ?> INSTANCE = new ImmutableEmptySortedMap<Object, Object>();
    private static final long serialVersionUID = 2L;

    private final Comparator<? super K> comparator;

    ImmutableEmptySortedMap()
    {
        this.comparator = null;
    }

    ImmutableEmptySortedMap(Comparator<? super K> comparator)
    {
        this.comparator = comparator;
    }

    public int size()
    {
        return 0;
    }

    public RichIterable<K> keysView()
    {
        return LazyIterate.empty();
    }

    public RichIterable<V> valuesView()
    {
        return LazyIterate.empty();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return LazyIterate.empty();
    }

    public boolean containsKey(Object key)
    {
        return false;
    }

    public boolean containsValue(Object value)
    {
        return false;
    }

    public V get(Object key)
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "{}";
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }

        return ((Map<K, V>) other).isEmpty();
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    @Override
    public ImmutableSortedMap<K, V> tap(Procedure<? super V> procedure)
    {
        return this;
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
    }

    public ImmutableMap<V, K> flipUniqueValues()
    {
        return Maps.immutable.with();
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
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
    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        return null;
    }

    @Override
    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        return function.value();
    }

    @Override
    public V getIfAbsentValue(K key, V value)
    {
        return value;
    }

    @Override
    public <P> V getIfAbsentWith(
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        return function.valueOf(parameter);
    }

    @Override
    public <K2, V2> ImmutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return Maps.immutable.empty();
    }

    @Override
    public <R> ImmutableSortedMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return SortedMaps.immutable.with(this.comparator);
    }

    @Override
    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        return null;
    }

    @Override
    public ImmutableSortedMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        return this;
    }

    public Set<Entry<K, V>> entrySet()
    {
        return Sets.immutable.<Entry<K, V>>with().castToSet();
    }

    @Override
    public ImmutableSortedMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        return this;
    }

    public Comparator<? super K> comparator()
    {
        return this.comparator;
    }

    public Set<K> keySet()
    {
        return Sets.immutable.<K>of().castToSet();
    }

    public Collection<V> values()
    {
        return Lists.immutable.<V>empty().castToList();
    }

    public K firstKey()
    {
        throw new NoSuchElementException();
    }

    public K lastKey()
    {
        throw new NoSuchElementException();
    }

    private Object writeReplace()
    {
        return new ImmutableSortedMapSerializationProxy<K, V>(this);
    }

    public ImmutableSortedMap<K, V> take(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }

        return this;
    }

    public ImmutableSortedMap<K, V> drop(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }

        return this;
    }
}
