/*
 * Copyright 2014 Goldman Sachs.
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
import java.util.Comparator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.multimap.sortedset.SortedSetMultimap;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.utility.Iterate;

public final class TreeSortedSetMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableSortedSet<V>>
        implements MutableSortedSetMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;
    private Comparator<? super V> comparator;

    public TreeSortedSetMultimap()
    {
        this.comparator = null;
    }

    public TreeSortedSetMultimap(Comparator<? super V> comparator)
    {
        this.comparator = comparator;
    }

    public TreeSortedSetMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.sizeDistinct() * 2, 16));
        this.comparator = multimap instanceof SortedSetMultimap<?, ?> ? ((SortedSetMultimap<K, V>) multimap).comparator() : null;
        this.putAll(multimap);
    }

    public TreeSortedSetMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
        this.comparator = null;
    }

    public TreeSortedSetMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
        this.comparator = null;
    }

    public static <K, V> TreeSortedSetMultimap<K, V> newMultimap()
    {
        return new TreeSortedSetMultimap<K, V>();
    }

    public static <K, V> TreeSortedSetMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new TreeSortedSetMultimap<K, V>(multimap);
    }

    public static <K, V> TreeSortedSetMultimap<K, V> newMultimap(Comparator<? super V> comparator)
    {
        return new TreeSortedSetMultimap<K, V>(comparator);
    }

    public static <K, V> TreeSortedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new TreeSortedSetMultimap<K, V>(pairs);
    }

    public static <K, V> TreeSortedSetMultimap<K, V> newMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        return new TreeSortedSetMultimap<K, V>(inputIterable);
    }

    @Override
    protected MutableMap<K, MutableSortedSet<V>> createMap()
    {
        return UnifiedMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableSortedSet<V>> createMapWithKeyCount(int keyCount)
    {
        return UnifiedMap.newMap(keyCount);
    }

    @Override
    protected MutableSortedSet<V> createCollection()
    {
        return new TreeSortedSet<V>(this.comparator);
    }

    public TreeSortedSetMultimap<K, V> newEmpty()
    {
        return new TreeSortedSetMultimap<K, V>(this.comparator());
    }

    public Comparator<? super V> comparator()
    {
        return this.comparator;
    }

    public MutableSortedSetMultimap<K, V> toMutable()
    {
        return new TreeSortedSetMultimap<K, V>(this);
    }

    public ImmutableSortedSetMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableSortedSet<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableSortedSet<V>>()
        {
            public void value(K key, MutableSortedSet<V> set)
            {
                map.put(key, set.toImmutable());
            }
        });
        return new ImmutableSortedSetMultimapImpl<K, V>(map, this.comparator());
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

    public MutableSetMultimap<V, K> flip()
    {
        return Iterate.flip(this);
    }

    public TreeSortedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, this.newEmpty());
    }

    public TreeSortedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, this.newEmpty());
    }

    public TreeSortedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, this.newEmpty());
    }

    public TreeSortedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, this.newEmpty());
    }

    public <K2, V2> HashBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.<K2, V2>newMultimap());
    }

    public <V2> FastListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, FastListMultimap.<K, V2>newMultimap());
    }
}
