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

package com.gs.collections.impl.map.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.tuple.Tuples;

final class ImmutableSingletonMap<K, V>
        extends AbstractImmutableMap<K, V>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final K key1;
    private final V value1;

    ImmutableSingletonMap(K key1, V value1)
    {
        this.key1 = key1;
        this.value1 = value1;
    }

    public int size()
    {
        return 1;
    }

    public RichIterable<K> keysView()
    {
        return Lists.immutable.with(this.key1).asLazy();
    }

    public RichIterable<V> valuesView()
    {
        return Lists.immutable.with(this.value1).asLazy();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return Lists.immutable.with(Tuples.pair(this.key1, this.value1)).asLazy();
    }

    public boolean containsKey(Object key)
    {
        return Comparators.nullSafeEquals(this.key1, key);
    }

    public boolean containsValue(Object value)
    {
        return Comparators.nullSafeEquals(this.value1, value);
    }

    public V get(Object key)
    {
        if (Comparators.nullSafeEquals(this.key1, key))
        {
            return this.value1;
        }

        return null;
    }

    public Set<K> keySet()
    {
        return Sets.immutable.with(this.key1).castToSet();
    }

    public Collection<V> values()
    {
        return Lists.immutable.with(this.value1).castToList();
    }

    @Override
    public String toString()
    {
        return "{" + this.key1 + '=' + this.value1 + '}';
    }

    @Override
    public int hashCode()
    {
        return this.keyAndValueHashCode(this.key1, this.value1);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }
        Map<K, V> that = (Map<K, V>) other;
        return that.size() == this.size() && this.keyAndValueEquals(this.key1, this.value1, that);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        procedure.value(this.key1, this.value1);
    }

    @Override
    public ImmutableMap<V, K> flipUniqueValues()
    {
        return Maps.immutable.with(this.value1, this.key1);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        procedure.value(this.key1);
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        procedure.value(this.value1);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        objectIntProcedure.value(this.value1, 0);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        procedure.value(this.value1, parameter);
    }

    @Override
    public ImmutableMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return this;
        }
        return Maps.immutable.empty();
    }

    @Override
    public ImmutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Maps.immutable.empty();
        }
        return this;
    }

    @Override
    public <K2, V2> ImmutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        Pair<K2, V2> pair = function.value(this.key1, this.value1);
        return Maps.immutable.with(pair.getOne(), pair.getTwo());
    }

    @Override
    public <R> ImmutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return Maps.immutable.with(this.key1, function.value(this.key1, this.value1));
    }

    @Override
    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Tuples.pair(this.key1, this.value1);
        }
        return null;
    }

    private Object writeReplace()
    {
        return new ImmutableMapSerializationProxy<K, V>(this);
    }
}
