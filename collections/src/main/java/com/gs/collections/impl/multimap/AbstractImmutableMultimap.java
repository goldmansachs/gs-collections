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

package com.gs.collections.impl.multimap;

import java.io.InvalidClassException;
import java.io.ObjectStreamException;
import java.util.Collection;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.utility.Iterate;

public abstract class AbstractImmutableMultimap<K, V, C extends ImmutableCollection<V>>
        extends AbstractMultimap<K, V, C>
        implements ImmutableMultimap<K, V>
{
    protected final ImmutableMap<K, C> map;

    /**
     * Creates a new multimap that clones the provided map into an ImmutableMap.
     *
     * @param map place to store the mapping from each key to its corresponding values
     */
    protected AbstractImmutableMultimap(MutableMap<K, C> map)
    {
        this(map.toImmutable());
    }

    /**
     * Creates a new multimap that uses the provided immutableMap.
     *
     * @param immutableMap place to store the mapping from each key to its corresponding values
     */
    protected AbstractImmutableMultimap(ImmutableMap<K, C> immutableMap)
    {
        this.map = immutableMap;
    }

    @Override
    protected ImmutableMap<K, C> getMap()
    {
        return this.map;
    }

// Query Operations

    public int size()
    {
        class CountProcedure implements Procedure<C>
        {
            private static final long serialVersionUID = 1L;

            private int totalSize;

            public void value(C collection)
            {
                this.totalSize += collection.size();
            }

            public int getTotalSize()
            {
                return this.totalSize;
            }
        }

        CountProcedure procedure = new CountProcedure();
        this.map.forEachValue(procedure);
        return procedure.getTotalSize();
    }

    public int sizeDistinct()
    {
        return this.map.size();
    }

    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    // Views

    public C get(K key)
    {
        return this.map.getIfAbsentWith(key, this.createCollectionBlock(), this);
    }

    public MutableMap<K, RichIterable<V>> toMap()
    {
        return (MutableMap<K, RichIterable<V>>) (MutableMap<?, ?>) this.map.toMap();
    }

    public <R extends Collection<V>> MutableMap<K, R> toMap(final Function0<R> collectionFactory)
    {
        final MutableMap<K, R> result = UnifiedMap.newMap();
        this.map.forEachKeyValue(new Procedure2<K, C>()
        {
            public void value(K key, C iterable)
            {
                R newCollection = collectionFactory.value();
                Iterate.addAllTo(iterable, newCollection);
                result.put(key, newCollection);
            }
        });

        return result;
    }

    public ImmutableMultimap<K, V> newWith(K key, V value)
    {
        MutableMultimap<K, V> mutableMultimap = this.toMutable();
        mutableMultimap.put(key, value);
        return mutableMultimap.toImmutable();
    }

    public ImmutableMultimap<K, V> newWithout(Object key, Object value)
    {
        MutableMultimap<K, V> mutableMultimap = this.toMutable();
        mutableMultimap.remove(key, value);
        return mutableMultimap.toImmutable();
    }

    public ImmutableMultimap<K, V> newWithAll(K key, Iterable<? extends V> values)
    {
        MutableMultimap<K, V> mutableMultimap = this.toMutable();
        mutableMultimap.putAll(key, values);
        return mutableMultimap.toImmutable();
    }

    public ImmutableMultimap<K, V> newWithoutAll(Object key)
    {
        MutableMultimap<K, V> mutableMultimap = this.toMutable();
        mutableMultimap.removeAll(key);
        return mutableMultimap.toImmutable();
    }

    public ImmutableMultimap<K, V> toImmutable()
    {
        return this;
    }

    protected Object readResolve() throws ObjectStreamException
    {
        throw new InvalidClassException("You should be using the proxy for serialization of ImmutableMultimaps");
    }
}
