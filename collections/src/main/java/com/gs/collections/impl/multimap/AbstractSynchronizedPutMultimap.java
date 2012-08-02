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

import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

public abstract class AbstractSynchronizedPutMultimap<K, V, C extends MutableCollection<V>> extends AbstractMutableMultimap<K, V, C>
{
    private final AtomicInteger atomicTotalSize = new AtomicInteger(0);

    protected AbstractSynchronizedPutMultimap()
    {
    }

    protected AbstractSynchronizedPutMultimap(MutableMap<K, C> newMap)
    {
        super(newMap);
    }

    @Override
    protected MutableMap<K, C> createMap()
    {
        return ConcurrentHashMap.newMap();
    }

    @Override
    protected MutableMap<K, C> createMapWithKeyCount(int keyCount)
    {
        return ConcurrentHashMap.newMap(keyCount);
    }

    @Override
    public int size()
    {
        return this.atomicTotalSize.get();
    }

    @Override
    protected void incrementTotalSize()
    {
        this.atomicTotalSize.incrementAndGet();
    }

    @Override
    protected void decrementTotalSize()
    {
        this.atomicTotalSize.decrementAndGet();
    }

    @Override
    protected void addToTotalSize(int value)
    {
        this.atomicTotalSize.addAndGet(value);
    }

    @Override
    protected void subtractFromTotalSize(int value)
    {
        this.atomicTotalSize.addAndGet(-value);
    }

    @Override
    protected void clearTotalSize()
    {
        this.atomicTotalSize.set(0);
    }

    @Override
    public boolean put(K key, V value)
    {
        MutableCollection<V> collection = this.getIfAbsentPutCollection(key);
        synchronized (collection)
        {
            if (collection.add(value))
            {
                this.incrementTotalSize();
                return true;
            }
            return false;
        }
    }

    private C getIfAbsentPutCollection(K key)
    {
        return this.map.getIfAbsentPutWith(key, this.createCollectionBlock(), this);
    }
}
