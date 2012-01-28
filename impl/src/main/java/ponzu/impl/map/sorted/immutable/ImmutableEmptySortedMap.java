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

package ponzu.impl.map.sorted.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import net.jcip.annotations.Immutable;
import ponzu.api.RichIterable;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Generator;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.map.ImmutableMap;
import ponzu.api.map.sorted.ImmutableSortedMap;
import ponzu.api.tuple.Pair;
import ponzu.impl.factory.Lists;
import ponzu.impl.factory.Maps;
import ponzu.impl.factory.Sets;
import ponzu.impl.factory.SortedMaps;
import ponzu.impl.utility.LazyIterate;

/**
 * This is a zero element {@link ImmutableSortedMap} which is created by calling SortedMaps.immutable.of().
 */
@Immutable
final class ImmutableEmptySortedMap<K, V>
        extends AbstractImmutableSortedMap<K, V>
        implements Serializable
{
    static final ImmutableSortedMap<?, ?> INSTANCE = new ImmutableEmptySortedMap();
    private static final long serialVersionUID = 1L;

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
    public void forEach(Procedure<? super V> procedure)
    {
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
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
    public V getIfAbsent(K key, Generator<? extends V> function)
    {
        return function.value();
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
    public <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return Maps.immutable.of();
    }

    @Override
    public <R> ImmutableSortedMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return SortedMaps.immutable.of(this.comparator);
    }

    @Override
    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        return null;
    }

    @Override
    public ImmutableSortedMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        return this;
    }

    @Override
    public ImmutableSortedMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
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
        return Lists.immutable.<V>of().castToList();
    }

    public K firstKey()
    {
        throw new NoSuchElementException();
    }

    public K lastKey()
    {
        throw new NoSuchElementException();
    }

    private Object readResolve()
    {
        if (this.comparator == null)
        {
            return INSTANCE;
        }
        return this;
    }
}
