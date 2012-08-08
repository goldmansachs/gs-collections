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

package com.webguys.ponzu.impl.map.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.map.ImmutableMap;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.block.factory.Predicates2;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.factory.Maps;
import com.webguys.ponzu.impl.factory.Sets;
import com.webguys.ponzu.impl.tuple.Tuples;

final class ImmutableDoubletonMap<K, V>
        extends AbstractImmutableMap<K, V>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final K key1;
    private final V value1;
    private final K key2;
    private final V value2;

    ImmutableDoubletonMap(K key1, V value1, K key2, V value2)
    {
        this.key1 = key1;
        this.value1 = value1;
        this.key2 = key2;
        this.value2 = value2;
    }

    public int size()
    {
        return 2;
    }

    public RichIterable<K> keysView()
    {
        return Lists.immutable.of(this.key1, this.key2).asLazy();
    }

    public RichIterable<V> valuesView()
    {
        return Lists.immutable.of(this.value1, this.value2).asLazy();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return Lists.immutable.<Pair<K, V>>of(
                Tuples.pair(this.key1, this.value1),
                Tuples.pair(this.key2, this.value2)).asLazy();
    }

    public boolean containsKey(Object key)
    {
        return Comparators.nullSafeEquals(this.key2, key) || Comparators.nullSafeEquals(this.key1, key);
    }

    public boolean containsValue(Object value)
    {
        return Comparators.nullSafeEquals(this.value2, value) || Comparators.nullSafeEquals(this.value1, value);
    }

    public V get(Object key)
    {
        if (Comparators.nullSafeEquals(this.key2, key))
        {
            return this.value2;
        }
        if (Comparators.nullSafeEquals(this.key1, key))
        {
            return this.value1;
        }
        return null;
    }

    public Set<K> keySet()
    {
        return Sets.immutable.of(this.key1, this.key2).castToSet();
    }

    public Collection<V> values()
    {
        return Lists.immutable.of(this.value1, this.value2).castToList();
    }

    @Override
    public int hashCode()
    {
        return this.keyAndValueHashCode(this.key1, this.value1) + this.keyAndValueHashCode(this.key2, this.value2);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }
        Map<K, V> that = (Map<K, V>) other;
        return that.size() == this.size()
                && this.keyAndValueEquals(this.key1, this.value1, that)
                && this.keyAndValueEquals(this.key2, this.value2, that);
    }

    @Override
    public String toString()
    {
        return "{"
                + this.key1 + '=' + this.value1 + ", "
                + this.key2 + '=' + this.value2 + '}';
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        procedure.value(this.key1, this.value1);
        procedure.value(this.key2, this.value2);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        procedure.value(this.key1);
        procedure.value(this.key2);
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        procedure.value(this.value1);
        procedure.value(this.value2);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        objectIntProcedure.value(this.value1, 0);
        objectIntProcedure.value(this.value2, 1);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        procedure.value(this.value1, parameter);
        procedure.value(this.value2, parameter);
    }

    @Override
    public <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        Pair<K2, V2> pair1 = function.value(this.key1, this.value1);
        Pair<K2, V2> pair2 = function.value(this.key2, this.value2);

        return Maps.immutable.of(pair1.getOne(), pair1.getTwo(), pair2.getOne(), pair2.getTwo());
    }

    @Override
    public <R> ImmutableMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return Maps.immutable.of(this.key1, function.value(this.key1, this.value1), this.key2, function.value(this.key2, this.value2));
    }

    @Override
    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Tuples.pair(this.key1, this.value1);
        }
        if (predicate.accept(this.key2, this.value2))
        {
            return Tuples.pair(this.key2, this.value2);
        }
        return null;
    }

    @Override
    public ImmutableMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        return this.basicFilter(predicate);
    }

    @Override
    public ImmutableMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        return this.basicFilter(Predicates2.not(predicate));
    }

    private ImmutableMap<K, V> basicFilter(Predicate2<? super K, ? super V> predicate)
    {
        int result = 0;

        if (predicate.accept(this.key1, this.value1))
        {
            result |= 1;
        }
        if (predicate.accept(this.key2, this.value2))
        {
            result |= 2;
        }

        switch (result)
        {
            case 1:
                return Maps.immutable.of(this.key1, this.value1);
            case 2:
                return Maps.immutable.of(this.key2, this.value2);
            case 3:
                return this;
            default:
                return Maps.immutable.of();
        }
    }
}
