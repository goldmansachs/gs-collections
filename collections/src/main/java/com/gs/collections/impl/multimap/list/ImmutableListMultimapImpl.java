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

package com.gs.collections.impl.multimap.list;

import java.io.Externalizable;
import java.io.Serializable;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.multimap.AbstractImmutableMultimap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;
import com.gs.collections.impl.multimap.ImmutableMultimapSerializationProxy;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.utility.Iterate;

/**
 * The default ImmutableListMultimap implementation.
 *
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

    public ImmutableListMultimapImpl(ImmutableMap<K, ImmutableList<V>> map)
    {
        super(map);
    }

    @Override
    protected ImmutableList<V> createCollection()
    {
        return Lists.immutable.empty();
    }

    public ImmutableListMultimap<K, V> newEmpty()
    {
        return new ImmutableListMultimapImpl<K, V>(Maps.immutable.<K, ImmutableList<V>>of());
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
            extends ImmutableMultimapSerializationProxy<K, V, ImmutableList<V>> implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableListMultimapSerializationProxy()
        {
            // For Externalizable use only
        }

        public ImmutableListMultimapSerializationProxy(ImmutableMap<K, ImmutableList<V>> map)
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

    public ImmutableBagMultimap<V, K> flip()
    {
        return Iterate.flip(this).toImmutable();
    }

    public ImmutableListMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, FastListMultimap.<K, V>newMultimap()).toImmutable();
    }

    public ImmutableListMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, FastListMultimap.<K, V>newMultimap()).toImmutable();
    }

    public ImmutableListMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, FastListMultimap.<K, V>newMultimap()).toImmutable();
    }

    public ImmutableListMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, FastListMultimap.<K, V>newMultimap()).toImmutable();
    }

    public <K2, V2> ImmutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.<K2, V2>newMultimap()).toImmutable();
    }

    public <V2> ImmutableListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, FastListMultimap.<K, V2>newMultimap()).toImmutable();
    }
}
