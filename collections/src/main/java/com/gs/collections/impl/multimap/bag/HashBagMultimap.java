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

package com.gs.collections.impl.multimap.bag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.procedure.checked.CheckedObjectIntProcedure;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure2;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public final class HashBagMultimap<K, V>
        extends AbstractMutableBagMultimap<K, V> implements Externalizable
{
    private static final long serialVersionUID = 1L;

    public HashBagMultimap()
    {
    }

    public HashBagMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.keysView().size() * 2, 16));
        this.putAll(multimap);
    }

    public HashBagMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    public HashBagMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    public static <K, V> HashBagMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new HashBagMultimap<K, V>(multimap);
    }

    public static <K, V> HashBagMultimap<K, V> newMultimap()
    {
        return new HashBagMultimap<K, V>();
    }

    public static <K, V> HashBagMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new HashBagMultimap<K, V>(pairs);
    }

    public static <K, V> HashBagMultimap<K, V> newMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        return new HashBagMultimap<K, V>(inputIterable);
    }

    @Override
    protected MutableMap<K, MutableBag<V>> createMap()
    {
        return UnifiedMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableBag<V>> createMapWithKeyCount(int keyCount)
    {
        return UnifiedMap.newMap(keyCount);
    }

    @Override
    protected MutableBag<V> createCollection()
    {
        return HashBag.newBag();
    }

    public HashBagMultimap<K, V> newEmpty()
    {
        return new HashBagMultimap<K, V>();
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException
    {
        int keysCount = this.map.size();
        out.writeInt(keysCount);
        this.map.forEachKeyValue(new CheckedProcedure2<K, MutableBag<V>>()
        {
            @Override
            public void safeValue(K key, MutableBag<V> bag) throws IOException
            {
                out.writeObject(key);
                out.writeInt(bag.sizeDistinct());
                bag.forEachWithOccurrences(new CheckedObjectIntProcedure<V>()
                {
                    @Override
                    public void safeValue(V value, int count) throws IOException
                    {
                        out.writeObject(value);
                        out.writeInt(count);
                    }
                });
            }
        });
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int keyCount = in.readInt();
        this.map = UnifiedMap.newMap(keyCount);
        for (int i = 0; i < keyCount; i++)
        {
            K key = (K) in.readObject();
            int valuesSize = in.readInt();
            MutableBag<V> bag = Bags.mutable.of();
            for (int j = 0; j < valuesSize; j++)
            {
                V value = (V) in.readObject();
                int count = in.readInt();

                bag.addOccurrences(value, count);
            }
            this.putAll(key, bag);
        }
    }

    public HashBagMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, this.newEmpty());
    }

    public HashBagMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, this.newEmpty());
    }

    public HashBagMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, this.newEmpty());
    }

    public HashBagMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, this.newEmpty());
    }
}
