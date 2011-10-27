/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.multimap;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.procedure.checked.MultimapKeyValuesSerializingProcedure;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.utility.Iterate;

public abstract class AbstractMutableMultimap<K, V, C extends MutableCollection<V>>
        extends AbstractMultimap<K, V, C>
        implements MutableMultimap<K, V>
{
    protected MutableMap<K, C> map;

    protected int totalSize;

    protected AbstractMutableMultimap()
    {
        this.map = UnifiedMap.newMap();
    }

    protected AbstractMutableMultimap(int size)
    {
        this.map = UnifiedMap.newMap(size);
    }

    /**
     * Constructs a {@link Multimap} containing all the {@link Pair}s.
     *
     * @param pairs the mappings to initialize the multimap.
     */
    protected AbstractMutableMultimap(Pair<K, V>... pairs)
    {
        this(pairs.length);
        this.putAllPairs(pairs);
    }

    @Override
    protected MutableMap<K, C> getMap()
    {
        return this.map;
    }

    // Query Operations

    public int size()
    {
        return this.totalSize;
    }

    public int sizeDistinct()
    {
        return this.map.size();
    }

    public boolean isEmpty()
    {
        return this.totalSize == 0;
    }

    // Modification Operations

    public boolean put(K key, V value)
    {
        C collection = this.getIfAbsentPutCollection(key);

        if (collection.add(value))
        {
            this.totalSize++;
            return true;
        }
        return false;
    }

    public boolean remove(Object key, Object value)
    {
        C collection = this.map.get(key);
        if (collection == null)
        {
            return false;
        }

        boolean changed = collection.remove(value);
        if (changed)
        {
            this.totalSize--;
            if (collection.isEmpty())
            {
                this.map.remove(key);
            }
        }
        return changed;
    }

    // Bulk Operations
    public boolean putAllPairs(Pair<K, V>... pairs)
    {
        boolean changed = false;
        for (Pair<K, V> pair : pairs)
        {
            changed |= this.put(pair.getOne(), pair.getTwo());
        }
        return changed;
    }

    public boolean putAll(K key, Iterable<? extends V> values)
    {
        return Iterate.notEmpty(values) && this.putAllNotEmpty(key, values);
    }

    private boolean putAllNotEmpty(K key, Iterable<? extends V> values)
    {
        C collection = this.getIfAbsentPutCollection(key);
        int oldSize = collection.size();
        int newSize = Iterate.addAllTo(values, collection).size();
        this.totalSize += newSize - oldSize;
        return newSize > oldSize;
    }

    public <KK extends K, VV extends V> boolean putAll(Multimap<KK, VV> multimap)
    {
        if (multimap instanceof AbstractMutableMultimap)
        {
            return this.putAllAbstractMutableMultimap((AbstractMutableMultimap<KK, VV, MutableCollection<VV>>) multimap);
        }
        return this.putAllReadOnlyMultimap(multimap);
    }

    private <KK extends K, VV extends V> boolean putAllReadOnlyMultimap(Multimap<KK, VV> multimap)
    {
        class PutProcedure implements Procedure<Pair<KK, LazyIterable<VV>>>
        {
            private static final long serialVersionUID = 1L;
            private boolean changed;

            public void value(Pair<KK, LazyIterable<VV>> each)
            {
                this.changed |= AbstractMutableMultimap.this.putAll(each.getOne(), each.getTwo());
            }
        }

        PutProcedure putProcedure = new PutProcedure();
        multimap.keyMultiValuePairsView().forEach(putProcedure);
        return putProcedure.changed;
    }

    private <KK extends K, VV extends V> boolean putAllAbstractMutableMultimap(AbstractMutableMultimap<KK, VV, MutableCollection<VV>> other)
    {
        class PutProcedure implements Procedure2<KK, MutableCollection<VV>>
        {
            private static final long serialVersionUID = 1L;

            private boolean changed;

            public void value(KK key, MutableCollection<VV> value)
            {
                this.changed |= AbstractMutableMultimap.this.putAll(key, value);
            }
        }
        PutProcedure putProcedure = new PutProcedure();
        other.map.forEachKeyValue(putProcedure);
        return putProcedure.changed;
    }

    public C replaceValues(K key, Iterable<? extends V> values)
    {
        if (Iterate.isEmpty(values))
        {
            return this.removeAll(key);
        }

        C newValues = Iterate.addAllTo(values, this.createCollection());
        C oldValues = this.map.put(key, newValues);
        oldValues = oldValues == null ? this.createCollection() : oldValues;
        this.totalSize += newValues.size() - oldValues.size();
        return (C) oldValues.asUnmodifiable();
    }

    public C removeAll(Object key)
    {
        C collection = this.map.remove(key);
        collection = collection == null ? this.createCollection() : collection;
        this.totalSize -= collection.size();
        return (C) collection.asUnmodifiable();
    }

    public void clear()
    {
        // Clear each collection, to make previously returned collections empty.
        for (C collection : this.map.values())
        {
            collection.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }

    // Views

    public C get(K key)
    {
        return (C) this.map.getIfAbsent(key, this.createCollectionBlock()).asUnmodifiable();
    }

    private C getIfAbsentPutCollection(K key)
    {
        return this.map.getIfAbsentPut(key, this.createCollectionBlock());
    }

    public MutableMap<K, RichIterable<V>> toMap()
    {
        final MutableMap<K, RichIterable<V>> result = (MutableMap<K, RichIterable<V>>) (MutableMap<?, ?>) this.map.newEmpty();
        this.map.forEachKeyValue(new Procedure2<K, C>()
        {
            public void value(K key, C collection)
            {
                MutableCollection<V> mutableCollection = collection.newEmpty();
                mutableCollection.addAll(collection);
                result.put(key, collection);
            }
        });

        return result;
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.map.size());
        this.map.forEachKeyValue(new MultimapKeyValuesSerializingProcedure<K, V>(out));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.readValuesFrom(in);
    }

    void readValuesFrom(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int keyCount = in.readInt();
        this.map = UnifiedMap.newMap(keyCount);
        for (int k = 0; k < keyCount; k++)
        {
            K key = (K) in.readObject();
            int valuesSize = in.readInt();
            C values = this.createCollection();
            for (int v = 0; v < valuesSize; v++)
            {
                values.add((V) in.readObject());
            }
            this.totalSize += valuesSize;
            this.map.put(key, values);
        }
    }
}
