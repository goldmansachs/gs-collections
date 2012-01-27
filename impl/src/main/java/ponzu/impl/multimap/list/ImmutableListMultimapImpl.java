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

package ponzu.impl.multimap.list;

import java.io.Serializable;

import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.multimap.list.ImmutableListMultimap;
import ponzu.api.multimap.list.MutableListMultimap;
import ponzu.impl.factory.Lists;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.multimap.AbstractImmutableMultimap;
import ponzu.impl.multimap.AbstractMutableMultimap;
import ponzu.impl.multimap.ImmutableMultimapSerializationProxy;

/**
 * @since 1.0
 */
public final class ImmutableListMultimapImpl<K, V>
        extends AbstractImmutableMultimap<K, V, ImmutableList<V>>
        implements ImmutableListMultimap<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;

    public ImmutableListMultimapImpl(MutableMap<K, ImmutableList<V>> map)
    {
        super(map);
    }

    @Override
    protected ImmutableList<V> createCollection()
    {
        return Lists.immutable.of();
    }

    public ImmutableListMultimap<K, V> newEmpty()
    {
        return new ImmutableListMultimapImpl<K, V>(UnifiedMap.<K, ImmutableList<V>>newMap());
    }

    public MutableListMultimap<K, V> toMutable()
    {
        return new FastListMultimap<K, V>(this);
    }

    @Override
    public ImmutableListMultimap<K, V> toImmutable()
    {
        return this;
    }

    private Object writeReplace()
    {
        return new ImmutableListMultimapSerializationProxy<K, V>(this.map);
    }

    public static class ImmutableListMultimapSerializationProxy<K, V>
            extends ImmutableMultimapSerializationProxy<K, V, ImmutableList<V>>
    {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableListMultimapSerializationProxy()
        {
            // For Externalizable use only
        }

        public ImmutableListMultimapSerializationProxy(MutableMap<K, ImmutableList<V>> map)
        {
            super(map);
        }

        @Override
        protected AbstractMutableMultimap<K, V, MutableList<V>> createEmptyMutableMultimap()
        {
            return new FastListMultimap<K, V>();
        }
    }

    @Override
    public ImmutableListMultimap<K, V> newWith(K key, V value)
    {
        return (ImmutableListMultimap<K, V>) super.newWith(key, value);
    }

    @Override
    public ImmutableListMultimap<K, V> newWithout(Object key, Object value)
    {
        return (ImmutableListMultimap<K, V>) super.newWithout(key, value);
    }

    @Override
    public ImmutableListMultimap<K, V> newWithAll(K key, Iterable<? extends V> values)
    {
        return (ImmutableListMultimap<K, V>) super.newWithAll(key, values);
    }

    @Override
    public ImmutableListMultimap<K, V> newWithoutAll(Object key)
    {
        return (ImmutableListMultimap<K, V>) super.newWithoutAll(key);
    }
}
