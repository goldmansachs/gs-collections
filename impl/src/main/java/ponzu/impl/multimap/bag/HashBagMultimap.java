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

package ponzu.impl.multimap.bag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import ponzu.api.bag.ImmutableBag;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.map.MutableMap;
import ponzu.api.multimap.Multimap;
import ponzu.api.multimap.bag.ImmutableBagMultimap;
import ponzu.api.multimap.bag.MutableBagMultimap;
import ponzu.api.tuple.Pair;
import ponzu.impl.bag.mutable.HashBag;
import ponzu.impl.block.procedure.checked.CheckedObjectIntProcedure;
import ponzu.impl.block.procedure.checked.CheckedProcedure2;
import ponzu.impl.factory.Bags;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.multimap.AbstractMutableMultimap;

public final class HashBagMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableBag<V>>
        implements MutableBagMultimap<K, V>, Externalizable
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

    public MutableBagMultimap<K, V> toMutable()
    {
        return new HashBagMultimap<K, V>(this);
    }

    public ImmutableBagMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableBag<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableBag<V>>()
        {
            public void value(K key, MutableBag<V> bag)
            {
                map.put(key, bag.toImmutable());
            }
        });

        return new ImmutableBagMultimapImpl<K, V>(map);
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
}
