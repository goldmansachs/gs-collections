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

package com.gs.collections.impl.multimap.set.sorted;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Comparator;

import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.multimap.AbstractImmutableMultimap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;
import com.gs.collections.impl.multimap.ImmutableMultimapSerializationProxy;

/**
 * The default ImmutableSortedSetMultimap implementation.
 *
 * @since 1.0
 */
public final class ImmutableSortedSetMultimapImpl<K, V>
        extends AbstractImmutableMultimap<K, V, ImmutableSortedSet<V>>
        implements ImmutableSortedSetMultimap<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final Comparator<? super V> comparator;

    ImmutableSortedSetMultimapImpl(MutableMap<K, ImmutableSortedSet<V>> map)
    {
        super(map);
        this.comparator = null;
    }

    public ImmutableSortedSetMultimapImpl(MutableMap<K, ImmutableSortedSet<V>> map, Comparator<? super V> comparator)
    {
        super(map);
        this.comparator = comparator;
    }

    ImmutableSortedSetMultimapImpl(ImmutableMap<K, ImmutableSortedSet<V>> map)
    {
        super(map);
        this.comparator = null;
    }

    public ImmutableSortedSetMultimapImpl(ImmutableMap<K, ImmutableSortedSet<V>> map, Comparator<? super V> comparator)
    {
        super(map);
        this.comparator = comparator;
    }

    @Override
    protected ImmutableSortedSet<V> createCollection()
    {
        return SortedSets.immutable.of(this.comparator());
    }

    public ImmutableSortedSetMultimap<K, V> newEmpty()
    {
        return new ImmutableSortedSetMultimapImpl<K, V>(Maps.immutable.<K, ImmutableSortedSet<V>>with(), this.comparator());
    }

    public Comparator<? super V> comparator()
    {
        return this.comparator;
    }

    public MutableSortedSetMultimap<K, V> toMutable()
    {
        return new TreeSortedSetMultimap<K, V>(this);
    }

    @Override
    public ImmutableSortedSetMultimap<K, V> toImmutable()
    {
        return this;
    }

    private Object writeReplace()
    {
        return new ImmutableSortedSetMultimapSerializationProxy<K, V>(this.map, this.comparator());
    }

    private static final class ImmutableSortedSetMultimapSerializationProxy<K, V>
            extends ImmutableMultimapSerializationProxy<K, V, ImmutableSortedSet<V>>
    {
        private static final long serialVersionUID = 1L;
        private Comparator<? super V> comparator;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableSortedSetMultimapSerializationProxy()
        {
            // For Externalizable use only
        }

        private ImmutableSortedSetMultimapSerializationProxy(ImmutableMap<K, ImmutableSortedSet<V>> map, Comparator<? super V> comparator)
        {
            super(map);
            this.comparator = comparator;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException
        {
            out.writeObject(this.comparator);
            super.writeExternal(out);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
        {
            this.comparator = (Comparator<? super V>) in.readObject();
            super.readExternal(in);
        }

        @Override
        protected AbstractMutableMultimap<K, V, MutableSortedSet<V>> createEmptyMutableMultimap()
        {
            return new TreeSortedSetMultimap<K, V>(this.comparator);
        }
    }

    @Override
    public ImmutableSortedSetMultimap<K, V> newWith(K key, V value)
    {
        return (ImmutableSortedSetMultimap<K, V>) super.newWith(key, value);
    }

    @Override
    public ImmutableSortedSetMultimap<K, V> newWithout(Object key, Object value)
    {
        return (ImmutableSortedSetMultimap<K, V>) super.newWithout(key, value);
    }

    @Override
    public ImmutableSortedSetMultimap<K, V> newWithAll(K key, Iterable<? extends V> values)
    {
        return (ImmutableSortedSetMultimap<K, V>) super.newWithAll(key, values);
    }

    @Override
    public ImmutableSortedSetMultimap<K, V> newWithoutAll(Object key)
    {
        return (ImmutableSortedSetMultimap<K, V>) super.newWithoutAll(key);
    }
}
