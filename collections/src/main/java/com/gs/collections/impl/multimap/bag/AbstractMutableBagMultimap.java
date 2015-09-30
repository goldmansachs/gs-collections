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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.procedure.checked.CheckedObjectIntProcedure;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure2;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;

public abstract class AbstractMutableBagMultimap<K, V> extends AbstractMutableMultimap<K, V, MutableBag<V>> implements MutableBagMultimap<K, V>
{
    protected AbstractMutableBagMultimap()
    {
    }

    protected AbstractMutableBagMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    protected AbstractMutableBagMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    protected AbstractMutableBagMultimap(int size)
    {
        super(size);
    }

    public MutableBagMultimap<K, V> toMutable()
    {
        MutableBagMultimap<K, V> mutableBagMultimap = this.newEmpty();
        mutableBagMultimap.putAll(this);
        return mutableBagMultimap;
    }

    public ImmutableBagMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableBag<V>> result = (MutableMap<K, ImmutableBag<V>>) (MutableMap<?, ?>) this.createMapWithKeyCount(this.map.size());

        this.map.forEachKeyValue(new Procedure2<K, MutableBag<V>>()
        {
            public void value(K key, MutableBag<V> bag)
            {
                result.put(key, bag.toImmutable());
            }
        });

        return new ImmutableBagMultimapImpl<K, V>(result);
    }

    public <K2, V2> HashBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.<K2, V2>newMultimap());
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException
    {
        int keysCount = this.map.size();
        out.writeInt(keysCount);
        this.map.forEachKeyValue(new CheckedProcedure2<K, MutableBag<V>>()
        {
            public void safeValue(K key, MutableBag<V> bag) throws IOException
            {
                out.writeObject(key);
                out.writeInt(bag.sizeDistinct());
                bag.forEachWithOccurrences(new CheckedObjectIntProcedure<V>()
                {
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
        this.map = this.createMapWithKeyCount(keyCount);
        for (int i = 0; i < keyCount; i++)
        {
            K key = (K) in.readObject();
            int valuesSize = in.readInt();
            MutableBag<V> bag = this.createCollection();
            for (int j = 0; j < valuesSize; j++)
            {
                V value = (V) in.readObject();
                int count = in.readInt();

                bag.addOccurrences(value, count);
            }
            this.putAll(key, bag);
        }
    }

    public void putOccurrences(K key, V value, int occurrences)
    {
        if (occurrences < 0)
        {
            throw new IllegalArgumentException("Cannot add a negative number of occurrences");
        }
        if (occurrences > 0)
        {
            MutableBag<V> bag = this.map.getIfAbsentPutWith(key, this.createCollectionBlock(), this);

            bag.addOccurrences(value, occurrences);
            this.addToTotalSize(occurrences);
        }
    }
}
