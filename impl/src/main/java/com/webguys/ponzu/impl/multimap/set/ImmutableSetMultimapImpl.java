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

package com.webguys.ponzu.impl.multimap.set;

import java.io.Serializable;

import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.set.ImmutableSetMultimap;
import com.webguys.ponzu.api.multimap.set.MutableSetMultimap;
import com.webguys.ponzu.api.set.ImmutableSet;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.impl.factory.Sets;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.AbstractImmutableMultimap;
import com.webguys.ponzu.impl.multimap.AbstractMutableMultimap;
import com.webguys.ponzu.impl.multimap.ImmutableMultimapSerializationProxy;

/**
 * @since 1.0
 */
public final class ImmutableSetMultimapImpl<K, V>
        extends AbstractImmutableMultimap<K, V, ImmutableSet<V>>
        implements ImmutableSetMultimap<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;

    public ImmutableSetMultimapImpl(MutableMap<K, ImmutableSet<V>> map)
    {
        super(map);
    }

    @Override
    protected ImmutableSet<V> createCollection()
    {
        return Sets.immutable.of();
    }

    public ImmutableSetMultimap<K, V> newEmpty()
    {
        return new ImmutableSetMultimapImpl<K, V>(UnifiedMap.<K, ImmutableSet<V>>newMap());
    }

    public MutableSetMultimap<K, V> toMutable()
    {
        return new UnifiedSetMultimap<K, V>(this);
    }

    @Override
    public ImmutableSetMultimap<K, V> toImmutable()
    {
        return this;
    }

    private Object writeReplace()
    {
        return new ImmutableSetMultimapSerializationProxy<K, V>(this.map);
    }

    private static final class ImmutableSetMultimapSerializationProxy<K, V>
            extends ImmutableMultimapSerializationProxy<K, V, ImmutableSet<V>>
    {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableSetMultimapSerializationProxy()
        {
            // For Externalizable use only
        }

        private ImmutableSetMultimapSerializationProxy(MutableMap<K, ImmutableSet<V>> map)
        {
            super(map);
        }

        @Override
        protected AbstractMutableMultimap<K, V, MutableSet<V>> createEmptyMutableMultimap()
        {
            return new UnifiedSetMultimap<K, V>();
        }
    }

    @Override
    public ImmutableSetMultimap<K, V> newWith(K key, V value)
    {
        return (ImmutableSetMultimap<K, V>) super.newWith(key, value);
    }

    @Override
    public ImmutableSetMultimap<K, V> newWithout(Object key, Object value)
    {
        return (ImmutableSetMultimap<K, V>) super.newWithout(key, value);
    }

    @Override
    public ImmutableSetMultimap<K, V> newWithAll(K key, Iterable<? extends V> values)
    {
        return (ImmutableSetMultimap<K, V>) super.newWithAll(key, values);
    }

    @Override
    public ImmutableSetMultimap<K, V> newWithoutAll(Object key)
    {
        return (ImmutableSetMultimap<K, V>) super.newWithoutAll(key);
    }
}
