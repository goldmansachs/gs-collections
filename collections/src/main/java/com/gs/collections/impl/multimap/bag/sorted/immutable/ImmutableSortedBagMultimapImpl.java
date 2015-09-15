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

package com.gs.collections.impl.multimap.bag.sorted.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Comparator;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.SortedBags;
import com.gs.collections.impl.multimap.AbstractImmutableMultimap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;
import com.gs.collections.impl.multimap.ImmutableMultimapSerializationProxy;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.utility.Iterate;

public class ImmutableSortedBagMultimapImpl<K, V>
        extends AbstractImmutableMultimap<K, V, ImmutableSortedBag<V>>
        implements ImmutableSortedBagMultimap<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final Comparator<? super V> comparator;

    ImmutableSortedBagMultimapImpl(MutableMap<K, ImmutableSortedBag<V>> map)
    {
        super(map);
        this.comparator = null;
    }

    public ImmutableSortedBagMultimapImpl(MutableMap<K, ImmutableSortedBag<V>> map, Comparator<? super V> comparator)
    {
        super(map);
        this.comparator = comparator;
    }

    ImmutableSortedBagMultimapImpl(ImmutableMap<K, ImmutableSortedBag<V>> map)
    {
        super(map);
        this.comparator = null;
    }

    public ImmutableSortedBagMultimapImpl(ImmutableMap<K, ImmutableSortedBag<V>> map, Comparator<? super V> comparator)
    {
        super(map);
        this.comparator = comparator;
    }

    @Override
    public ImmutableSortedBagMultimap<K, V> newWith(K key, V value)
    {
        return (ImmutableSortedBagMultimap<K, V>) super.newWith(key, value);
    }

    @Override
    public ImmutableSortedBagMultimap<K, V> newWithout(Object key, Object value)
    {
        return (ImmutableSortedBagMultimap<K, V>) super.newWithout(key, value);
    }

    @Override
    public ImmutableSortedBagMultimap<K, V> newWithAll(K key, Iterable<? extends V> values)
    {
        return (ImmutableSortedBagMultimap<K, V>) super.newWithAll(key, values);
    }

    @Override
    public ImmutableSortedBagMultimap<K, V> newWithoutAll(Object key)
    {
        return (ImmutableSortedBagMultimap<K, V>) super.newWithoutAll(key);
    }

    @Override
    protected ImmutableSortedBag<V> createCollection()
    {
        return SortedBags.immutable.with(this.comparator);
    }

    @Override
    public ImmutableSortedBagMultimapImpl<K, V> toImmutable()
    {
        return this;
    }

    public ImmutableSortedBagMultimap<K, V> newEmpty()
    {
        return new ImmutableSortedBagMultimapImpl<K, V>(Maps.immutable.<K, ImmutableSortedBag<V>>with(), this.comparator);
    }

    public Comparator<? super V> comparator()
    {
        return this.comparator;
    }

    public MutableSortedBagMultimap<K, V> toMutable()
    {
        return new TreeBagMultimap<K, V>(this);
    }

    public ImmutableBagMultimap<V, K> flip()
    {
        return Iterate.flip(this).toImmutable();
    }

    public ImmutableSortedBagMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, TreeBagMultimap.<K, V>newMultimap(this.comparator)).toImmutable();
    }

    public ImmutableSortedBagMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, TreeBagMultimap.<K, V>newMultimap(this.comparator)).toImmutable();
    }

    public ImmutableSortedBagMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, TreeBagMultimap.<K, V>newMultimap(this.comparator)).toImmutable();
    }

    public ImmutableSortedBagMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, TreeBagMultimap.<K, V>newMultimap(this.comparator)).toImmutable();
    }

    public <K2, V2> ImmutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.<K2, V2>newMultimap()).toImmutable();
    }

    public <V2> ImmutableListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, FastListMultimap.<K, V2>newMultimap()).toImmutable();
    }

    private Object writeReplace()
    {
        return new ImmutableSortedBagMultimapSerializationProxy<K, V>(this.map, this.comparator());
    }

    private static final class ImmutableSortedBagMultimapSerializationProxy<K, V>
            extends ImmutableMultimapSerializationProxy<K, V, ImmutableSortedBag<V>> implements Externalizable
    {
        private static final long serialVersionUID = 1L;
        private Comparator<? super V> comparator;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableSortedBagMultimapSerializationProxy()
        {
            // For Externalizable use only
        }

        private ImmutableSortedBagMultimapSerializationProxy(ImmutableMap<K, ImmutableSortedBag<V>> map, Comparator<? super V> comparator)
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
        protected AbstractMutableMultimap<K, V, MutableSortedBag<V>> createEmptyMutableMultimap()
        {
            return new TreeBagMultimap<K, V>(this.comparator);
        }
    }
}
