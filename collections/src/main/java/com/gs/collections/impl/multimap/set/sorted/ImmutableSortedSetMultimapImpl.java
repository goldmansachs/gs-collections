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

package com.gs.collections.impl.multimap.set.sorted;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Comparator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.multimap.AbstractImmutableMultimap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;
import com.gs.collections.impl.multimap.ImmutableMultimapSerializationProxy;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.utility.Iterate;

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
        return SortedSets.immutable.with(this.comparator());
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
            extends ImmutableMultimapSerializationProxy<K, V, ImmutableSortedSet<V>> implements Externalizable
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

    public ImmutableSetMultimap<V, K> flip()
    {
        return Iterate.flip(this).toImmutable();
    }

    public ImmutableSortedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, TreeSortedSetMultimap.<K, V>newMultimap(this.comparator())).toImmutable();
    }

    public ImmutableSortedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, TreeSortedSetMultimap.<K, V>newMultimap(this.comparator())).toImmutable();
    }

    public ImmutableSortedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, TreeSortedSetMultimap.<K, V>newMultimap(this.comparator())).toImmutable();
    }

    public ImmutableSortedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, TreeSortedSetMultimap.<K, V>newMultimap(this.comparator())).toImmutable();
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
