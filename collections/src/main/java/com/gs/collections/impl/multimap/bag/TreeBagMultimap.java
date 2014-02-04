/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.multimap.bag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.api.multimap.sortedbag.SortedBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;

public final class TreeBagMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableSortedBag<V>>
        implements MutableSortedBagMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;
    private Comparator<? super V> comparator;

    public TreeBagMultimap()
    {
        this.comparator = null;
    }

    public TreeBagMultimap(Comparator<? super V> comparator)
    {
        this.comparator = comparator;
    }

    public TreeBagMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.keysView().size() * 2, 16));
        this.comparator = multimap instanceof SortedBagMultimap<?, ?> ? ((SortedBagMultimap<K, V>) multimap).comparator() : null;
        this.putAll(multimap);
    }

    public TreeBagMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
        this.comparator = null;
    }

    public static <K, V> TreeBagMultimap<K, V> newMultimap()
    {
        return new TreeBagMultimap<K, V>();
    }

    public static <K, V> TreeBagMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new TreeBagMultimap<K, V>(multimap);
    }

    public static <K, V> TreeBagMultimap<K, V> newMultimap(Comparator<? super V> comparator)
    {
        return new TreeBagMultimap<K, V>(comparator);
    }

    public static <K, V> TreeBagMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new TreeBagMultimap<K, V>(pairs);
    }

    @Override
    protected MutableMap<K, MutableSortedBag<V>> createMap()
    {
        return UnifiedMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableSortedBag<V>> createMapWithKeyCount(int keyCount)
    {
        return UnifiedMap.newMap(keyCount);
    }

    @Override
    protected MutableSortedBag<V> createCollection()
    {
        return TreeBag.newBag(this.comparator);
    }

    public TreeBagMultimap<K, V> newEmpty()
    {
        return new TreeBagMultimap<K, V>();
    }

    public Comparator<? super V> comparator()
    {
        return this.comparator;
    }

    public MutableSortedBagMultimap<K, V> toMutable()
    {
        return new TreeBagMultimap<K, V>(this);
    }

    public ImmutableSortedBagMultimap<K, V> toImmutable()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.comparator());
        super.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.comparator = (Comparator<? super V>) in.readObject();
        super.readExternal(in);
    }
}
