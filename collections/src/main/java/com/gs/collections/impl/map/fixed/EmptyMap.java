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

package com.gs.collections.impl.map.fixed;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.FixedSizeMap;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Multimaps;
import com.gs.collections.impl.factory.Sets;
import net.jcip.annotations.Immutable;

@Immutable
final class EmptyMap<K, V>
        extends AbstractMemoryEfficientMutableMap<K, V>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Object readResolve()
    {
        return Maps.fixedSize.of();
    }

    public int size()
    {
        return 0;
    }

    @Override
    public MutableMap<K, V> withKeyValue(K addKey, V addValue)
    {
        return new SingletonMap<K, V>(addKey, addValue);
    }

    @Override
    public MutableMap<K, V> withoutKey(K key)
    {
        return this;
    }

    // Weird implementation of clone() is ok on final classes
    @Override
    public EmptyMap<K, V> clone()
    {
        return this;
    }

    @Override
    public ImmutableMap<K, V> toImmutable()
    {
        return Maps.immutable.empty();
    }

    @Override
    public MutableSetMultimap<V, K> flip()
    {
        return Multimaps.mutable.set.with();
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

    public Set<K> keySet()
    {
        return Sets.fixedSize.of();
    }

    public Collection<V> values()
    {
        return Lists.fixedSize.of();
    }

    public MutableSet<Entry<K, V>> entrySet()
    {
        return Sets.fixedSize.of();
    }

    @Override
    public String toString()
    {
        return "{}";
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }
        Map<K, V> that = (Map<K, V>) other;
        return that.size() == this.size();
    }

    @Override
    public MutableMap<V, K> flipUniqueValues()
    {
        return Maps.fixedSize.with();
    }

    @Override
    public FixedSizeMap<K, V> tap(Procedure<? super V> procedure)
    {
        return this;
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
    public FixedSizeMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        return Maps.fixedSize.of();
    }

    @Override
    public <R> FixedSizeMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return Maps.fixedSize.of();
    }

    @Override
    public <K2, V2> FixedSizeMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return Maps.fixedSize.of();
    }

    @Override
    public FixedSizeMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        return Maps.fixedSize.of();
    }

    @Override
    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        return null;
    }
}
