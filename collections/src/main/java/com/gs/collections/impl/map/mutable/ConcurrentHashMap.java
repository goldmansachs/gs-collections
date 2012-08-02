/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.map.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.ConcurrentMutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.procedure.MapEntryToProcedure2;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.MapIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

@SuppressWarnings("ObjectEquality")
public final class ConcurrentHashMap<K, V>
        extends AbstractMutableMap<K, V>
        implements ConcurrentMutableMap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;

    private static final Object RESIZE_SENTINEL = new Object();
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<ConcurrentHashMap, AtomicReferenceArray> TABLE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(ConcurrentHashMap.class, AtomicReferenceArray.class, "table");
    @SuppressWarnings("rawtypes")
    private static final AtomicIntegerFieldUpdater<ConcurrentHashMap> SIZE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ConcurrentHashMap.class, "size");
    private static final Object RESIZED = new Object();

    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    private volatile AtomicReferenceArray<Object> table;

    @SuppressWarnings("UnusedDeclaration")
    private volatile int size; // updated via atomic field updater

    public ConcurrentHashMap()
    {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ConcurrentHashMap(int initialCapacity)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("Illegal Initial Capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY)
        {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        int threshold = initialCapacity;
        threshold += threshold >> 1; // threshold = length * 0.75

        int capacity = 1;
        while (capacity < threshold)
        {
            capacity <<= 1;
        }
        this.table = new AtomicReferenceArray<Object>(capacity + 1);
    }

    public static <K, V> ConcurrentHashMap<K, V> newMap()
    {
        return new ConcurrentHashMap<K, V>();
    }

    public static <K, V> ConcurrentHashMap<K, V> newMap(int newSize)
    {
        return new ConcurrentHashMap<K, V>(newSize);
    }

    private static int indexFor(int h, int length)
    {
        return h & length - 2;
    }

    public V putIfAbsent(K key, V value)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        while (true)
        {
            int length = currentArray.length();
            int index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    K candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        return e.getValue();
                    }
                    e = e.getNext();
                }
                Entry<K, V> newEntry = new Entry<K, V>(key, value, (Entry<K, V>) o);
                if (currentArray.compareAndSet(index, o, newEntry))
                {
                    this.incrementSizeAndPossiblyResize(currentArray, length);
                    return null; // per the contract of putIfAbsent, we return null when the map didn't have this key before
                }
            }
        }
    }

    private void incrementSizeAndPossiblyResize(AtomicReferenceArray<Object> currentArray, int length)
    {
        int threshold = length >> 1;
        threshold += threshold >> 1; // threshold = length * 0.75
        if (SIZE_UPDATER.incrementAndGet(this) > threshold)
        {
            this.resize(currentArray);
        }
    }

    private int hash(Object key)
    {
        int h = key.hashCode();
        h ^= h >>> 20 ^ h >>> 12;
        h ^= h >>> 7 ^ h >>> 4;
        return h;
    }

    private AtomicReferenceArray<Object> helpWithResizeWhileCurrentIndex(AtomicReferenceArray<Object> currentArray, int index)
    {
        AtomicReferenceArray<Object> newArray = this.helpWithResize(currentArray, index);
        int helpCount = 0;
        while (currentArray.get(index) != RESIZED)
        {
            helpCount++;
            newArray = this.helpWithResize(currentArray, index + helpCount & currentArray.length() - 2);
            if ((helpCount & 7) == 0)
            {
                Thread.yield();
            }
        }
        return newArray;
    }

    private AtomicReferenceArray<Object> helpWithResize(AtomicReferenceArray<Object> currentArray, int index)
    {
        AtomicReferenceArray<Object> newTable = (AtomicReferenceArray<Object>) currentArray.get(currentArray.length() - 1);
        int dir = 1;
        if ((Integer.bitCount(index) & 1) == 0)
        {
            dir = -1;
        }
        int low = index + 1;
        int high = currentArray.length() - 2;
        int start = -1;

        while (low <= high)
        {
            int mid = low + high >>> 1;
            Object o = currentArray.get(mid);
            if (o != RESIZED && !(o instanceof ResizeContainer))
            {
                start = mid;
                break;
            }
            if (dir < 0)
            {
                low = mid + 1;
            }
            else
            {
                high = mid - 1;
            }
        }
        if (start > 0)
        {
            ResizeContainer<K, V> resizeContainer = new ResizeContainer<K, V>(null);
            for (int j = start; j < currentArray.length() - 1; )
            {
                Object o = currentArray.get(j);
                if (o == null)
                {
                    if (currentArray.compareAndSet(j, null, RESIZED))
                    {
                        j++;
                    }
                }
                else if (o == RESIZED || o instanceof ResizeContainer)
                {
                    break;
                }
                else
                {
                    Entry<K, V> e = (Entry<K, V>) o;
                    resizeContainer.setEntryChain(e);
                    if (currentArray.compareAndSet(j, o, resizeContainer))
                    {
                        while (e != null)
                        {
                            this.unconditionalCopy(newTable, e);
                            e = e.getNext();
                        }
                        currentArray.set(j, RESIZED);
                        j++;
                    }
                }
            }
        }
        return newTable;
    }

    private void resize(AtomicReferenceArray<Object> oldTable)
    {
        this.resize(oldTable, (oldTable.length() - 1 << 1) + 1);
    }

    // newSize must be a power of 2 + 1
    @SuppressWarnings("JLM_JSR166_UTILCONCURRENT_MONITORENTER")
    private void resize(AtomicReferenceArray<Object> oldTable, int newSize)
    {
        int oldCapacity = oldTable.length();
        int end = oldCapacity - 1;
        Object last = oldTable.get(end);
        if (this.size < end && last == RESIZE_SENTINEL)
        {
            return;
        }
        if (oldCapacity >= MAXIMUM_CAPACITY)
        {
            throw new RuntimeException("index is too large!");
        }
        boolean ownResize = false;
        AtomicReferenceArray<Object> newTable = null;
        if (last == null || last == RESIZE_SENTINEL)
        {
            synchronized (oldTable) // allocating a new array is too expensive to make this an atomic operation
            {
                if (oldTable.get(end) == null)
                {
                    oldTable.set(end, RESIZE_SENTINEL);
                    newTable = new AtomicReferenceArray(newSize);
                    oldTable.set(end, newTable);
                    ownResize = true;
                }
            }
        }
        if (ownResize)
        {
            this.transfer(oldTable, newTable);
            AtomicReferenceArray<Object> src = this.table;
            while (!TABLE_UPDATER.compareAndSet(this, oldTable, newTable))
            {
                // we're in a double resize situation; we'll have to go help until it's our turn to set the table
                if (src != oldTable)
                {
                    AtomicReferenceArray<Object> next = (AtomicReferenceArray<Object>) src.get(src.length() - 1);
                    if (next != null)
                    {
                        this.transfer(src, next);
                    }
                    src = next;
                    if (src == null)
                    {
                        src = this.table;
                    }
                }
            }
        }
        else
        {
            this.helpWithResize(oldTable, 0);
        }
    }

    /*
     * Transfer all entries from src to dest tables
     */
    private void transfer(AtomicReferenceArray<Object> src, AtomicReferenceArray<Object> dest)
    {
        int rescanLocation = -1;
        int helpers = 0;
        ResizeContainer<K, V> resizeContainer = new ResizeContainer<K, V>(null);
        for (int j = 0; j < src.length() - 1; )
        {
            Object o = src.get(j);
            if (o == null)
            {
                if (src.compareAndSet(j, null, RESIZED))
                {
                    j++;
                }
            }
            else if (o == RESIZED)
            {
                j++;
            }
            else if (o instanceof ResizeContainer)
            {
                if (rescanLocation < 0)
                {
                    rescanLocation = j;
                }
                j++;
                helpers++;
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                resizeContainer.setEntryChain(e);
                if (src.compareAndSet(j, o, resizeContainer))
                {
                    while (e != null)
                    {
                        this.unconditionalCopy(dest, e);
                        e = e.getNext();
                    }
                    src.set(j, RESIZED);
                    j++;
                }
            }
        }
        this.rescanUntilDone(src, rescanLocation, helpers);
    }

    private void rescanUntilDone(AtomicReferenceArray<Object> src, int rescanLocation, int helpers)
    {
        while (rescanLocation >= 0)
        {
            if (src.get(rescanLocation) == RESIZED && helpers == 1)
            {
                return;
            }
            helpers = 0;
            int j = rescanLocation;
            rescanLocation = -1;
            while (j < src.length() - 1)
            {
                Object o = src.get(j);
                if (o instanceof ResizeContainer)
                {
                    while (src.get(j) != RESIZED)
                    {
                        Thread.yield();
                    }
                }
                j++;
            }
        }
    }

    private void unconditionalCopy(AtomicReferenceArray<Object> dest, Entry<K, V> toCopyEntry)
    {
        int hash = this.hash(toCopyEntry.getKey());
        AtomicReferenceArray<Object> currentArray = dest;
        while (true)
        {
            int length = currentArray.length();
            int index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = (AtomicReferenceArray<Object>) currentArray.get(length - 1);
            }
            else
            {
                Entry<K, V> newEntry;
                if (o == null)
                {
                    if (toCopyEntry.getNext() == null)
                    {
                        newEntry = toCopyEntry; // no need to duplicate
                    }
                    else
                    {
                        newEntry = new Entry<K, V>(toCopyEntry.getKey(), toCopyEntry.getValue());
                    }
                }
                else
                {
                    newEntry = new Entry<K, V>(toCopyEntry.getKey(), toCopyEntry.getValue(), (Entry<K, V>) o);
                }
                if (currentArray.compareAndSet(index, o, newEntry))
                {
                    return;
                }
            }
        }
    }

    public V getIfAbsentPut(K key, Function<? super K, ? extends V> factory)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        V newValue = null;
        boolean createdValue = false;
        while (true)
        {
            int length = currentArray.length();
            int index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        return e.getValue();
                    }
                    e = e.getNext();
                }
                if (!createdValue)
                {
                    createdValue = true;
                    newValue = factory.valueOf(key);
                }
                Entry<K, V> newEntry = new Entry<K, V>(key, newValue, (Entry<K, V>) o);
                if (currentArray.compareAndSet(index, o, newEntry))
                {
                    this.incrementSizeAndPossiblyResize(currentArray, length);
                    return newValue;
                }
            }
        }
    }

    @Override
    public V getIfAbsentPut(K key, Function0<? extends V> factory)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        V newValue = null;
        boolean createdValue = false;
        while (true)
        {
            int length = currentArray.length();
            int index = indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        return e.getValue();
                    }
                    e = e.getNext();
                }
                if (!createdValue)
                {
                    createdValue = true;
                    newValue = factory.value();
                }
                Entry<K, V> newEntry = new Entry<K, V>(key, newValue, (Entry<K, V>) o);
                if (currentArray.compareAndSet(index, o, newEntry))
                {
                    this.incrementSizeAndPossiblyResize(currentArray, length);
                    return newValue;
                }
            }
        }
    }

    /**
     * It puts an object into the map based on the key. It uses a copy of the key converted by transformer.
     *
     * @param key            The "mutable" key, which has the same identity/hashcode as the inserted key, only during this call
     * @param keyTransformer If the record is absent, the transformer will transform the "mutable" key into an immutable copy of the key.
     *                       Note that the transformed key must have the same identity/hashcode as the original "mutable" key.
     * @param factory        It creates an object, if it is not present in the map already.
     */
    public <P1, P2> V putIfAbsentGetIfPresent(K key, Function2<K, V, K> keyTransformer, Function3<P1, P2, K, V> factory, P1 param1, P2 param2)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        V newValue = null;
        boolean createdValue = false;
        while (true)
        {
            int length = currentArray.length();
            int index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        return e.getValue();
                    }
                    e = e.getNext();
                }
                if (!createdValue)
                {
                    createdValue = true;
                    newValue = factory.value(param1, param2, key);
                    if (newValue == null)
                    {
                        return null; // null value means no mapping is required
                    }
                    key = keyTransformer.value(key, newValue);
                }
                Entry<K, V> newEntry = new Entry<K, V>(key, newValue, (Entry<K, V>) o);
                if (currentArray.compareAndSet(index, o, newEntry))
                {
                    this.incrementSizeAndPossiblyResize(currentArray, length);
                    return null;
                }
            }
        }
    }

    public boolean remove(Object key, Object value)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        //noinspection LabeledStatement
        outer:
        while (true)
        {
            int length = currentArray.length();
            int index = indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key) && this.nullSafeEquals(e.getValue(), value))
                    {
                        Entry<K, V> replacement = this.createReplacementChainForRemoval((Entry<K, V>) o, e);
                        if (currentArray.compareAndSet(index, o, replacement))
                        {
                            SIZE_UPDATER.decrementAndGet(this);
                            return true;
                        }
                        //noinspection ContinueStatementWithLabel
                        continue outer;
                    }
                    e = e.getNext();
                }
                return false;
            }
        }
    }

    public int size()
    {
        return this.size;
    }

    @Override
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    public boolean containsKey(Object key)
    {
        return this.getEntry(key) != null;
    }

    public boolean containsValue(Object value)
    {
        AtomicReferenceArray<Object> currentArray = this.table;
        boolean followResize;
        int rescanLocation = -1;
        int helpers = 0;
        do
        {
            followResize = false;
            for (int i = 0; i < currentArray.length() - 1; i++)
            {
                Object o = currentArray.get(i);
                if (o == RESIZED)
                {
                    followResize = true;
                }
                else if (o instanceof ResizeContainer)
                {
                    if (rescanLocation < 0)
                    {
                        rescanLocation = i;
                    }
                    helpers++;
                    followResize = true;
                }
                else if (o != null)
                {
                    Entry<K, V> e = (Entry<K, V>) o;
                    while (e != null)
                    {
                        Object v = e.getValue();
                        if (this.nullSafeEquals(v, value))
                        {
                            return true;
                        }
                        e = e.getNext();
                    }
                }
            }
            if (followResize)
            {
                this.helpWithResize(currentArray, 0);
                this.rescanUntilDone(currentArray, rescanLocation, helpers);
                currentArray = (AtomicReferenceArray<Object>) currentArray.get(currentArray.length() - 1);
            }
        }
        while (followResize);
        return false;
    }

    private boolean nullSafeEquals(Object v, Object value)
    {
        return v == value || v != null && v.equals(value);
    }

    public V get(Object key)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        int index = ConcurrentHashMap.indexFor(hash, currentArray.length());
        Object o = currentArray.get(index);
        if (o == RESIZED || o instanceof ResizeContainer)
        {
            return this.slowGet(key, hash, index, currentArray);
        }
        for (Entry<K, V> e = (Entry<K, V>) o; e != null; e = e.getNext())
        {
            Object k;
            if ((k = e.key) == key || key.equals(k))
            {
                return e.value;
            }
        }
        return null;
    }

    private V slowGet(Object key, int hash, int index, AtomicReferenceArray<Object> currentArray)
    {
        while (true)
        {
            int length = currentArray.length();
            index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        return e.getValue();
                    }
                    e = e.getNext();
                }
                return null;
            }
        }
    }

    private Entry<K, V> getEntry(Object key)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        while (true)
        {
            int length = currentArray.length();
            int index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        return e;
                    }
                    e = e.getNext();
                }
                return null;
            }
        }
    }

    public V put(K key, V value)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        int length = currentArray.length();
        int index = ConcurrentHashMap.indexFor(hash, length);
        Object o = currentArray.get(index);
        if (o == RESIZED || o instanceof ResizeContainer)
        {
            return this.slowPut(key, value, hash, currentArray);
        }
        Entry<K, V> e = (Entry<K, V>) o;
        while (e != null)
        {
            Object candidate = e.getKey();
            if (candidate.equals(key))
            {
                V oldValue = e.getValue();
                Entry<K, V> newEntry = new Entry<K, V>(e.getKey(), value, this.createReplacementChainForRemoval((Entry<K, V>) o, e));
                if (!currentArray.compareAndSet(index, o, newEntry))
                {
                    return this.slowPut(key, value, hash, currentArray);
                }
                return oldValue;
            }
            e = e.getNext();
        }
        Entry<K, V> newEntry = new Entry<K, V>(key, value, (Entry<K, V>) o);
        if (currentArray.compareAndSet(index, o, newEntry))
        {
            this.incrementSizeAndPossiblyResize(currentArray, length);
            return null;
        }
        return this.slowPut(key, value, hash, currentArray);
    }

    private V slowPut(K key, V value, int hash, AtomicReferenceArray<Object> currentArray)
    {
        //noinspection LabeledStatement
        outer:
        while (true)
        {
            int length = currentArray.length();
            int index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        V oldValue = e.getValue();
                        Entry<K, V> newEntry = new Entry<K, V>(e.getKey(), value, this.createReplacementChainForRemoval((Entry<K, V>) o, e));
                        if (!currentArray.compareAndSet(index, o, newEntry))
                        {
                            //noinspection ContinueStatementWithLabel
                            continue outer;
                        }
                        return oldValue;
                    }
                    e = e.getNext();
                }
                Entry<K, V> newEntry = new Entry<K, V>(key, value, (Entry<K, V>) o);
                if (currentArray.compareAndSet(index, o, newEntry))
                {
                    this.incrementSizeAndPossiblyResize(currentArray, length);
                    return null;
                }
            }
        }
    }

    public void putAllInParallel(Map<K, V> map, int chunks, Executor executor)
    {
        if (this.size == 0)
        {
            int threshold = map.size();
            threshold += threshold >> 1; // threshold = length * 0.75

            int capacity = 1;
            while (capacity < threshold)
            {
                capacity <<= 1;
            }
            this.resize(this.table, capacity + 1);
        }
        if (map instanceof ConcurrentHashMap<?, ?> && chunks > 1 && map.size() > 50000)
        {
            ConcurrentHashMap<K, V> incoming = (ConcurrentHashMap<K, V>) map;
            final AtomicReferenceArray<Object> currentArray = incoming.table;
            FutureTask<?>[] futures = new FutureTask<?>[chunks];
            int chunkSize = currentArray.length() / chunks;
            if (currentArray.length() % chunks != 0)
            {
                chunkSize++;
            }
            for (int i = 0; i < chunks; i++)
            {
                final int start = i * chunkSize;
                final int end = Math.min((i + 1) * chunkSize, currentArray.length());
                futures[i] = new FutureTask(new Runnable()
                {
                    public void run()
                    {
                        ConcurrentHashMap.this.sequentialPutAll(currentArray, start, end);
                    }
                }, null);
                executor.execute(futures[i]);
            }
            for (int i = 0; i < chunks; i++)
            {
                try
                {
                    futures[i].get();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("parallelForEachKeyValue failed", e);
                }
            }
        }
        else
        {
            this.putAll(map);
        }
    }

    private void sequentialPutAll(AtomicReferenceArray<Object> currentArray, int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            Object o = currentArray.get(i);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                throw new ConcurrentModificationException("can't iterate while resizing!");
            }
            Entry<K, V> e = (Entry<K, V>) o;
            while (e != null)
            {
                Object key = e.getKey();
                Object value = e.getValue();
                this.put((K) key, (V) value);
                e = e.getNext();
            }
        }
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        MapIterate.forEachKeyValue(map, new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                ConcurrentHashMap.this.put(key, value);
            }
        });
    }

    public void clear()
    {
        AtomicReferenceArray<Object> currentArray = this.table;
        boolean followResize;
        int rescanLocation = -1;
        int helpers = 0;
        do
        {
            followResize = false;
            for (int i = 0; i < currentArray.length() - 1; i++)
            {
                Object o = currentArray.get(i);
                if (o == RESIZED)
                {
                    followResize = true;
                }
                else if (o instanceof ResizeContainer)
                {
                    if (rescanLocation < 0)
                    {
                        rescanLocation = i;
                    }
                    helpers++;
                    followResize = true;
                }
                else if (o != null)
                {
                    Entry<K, V> e = (Entry<K, V>) o;
                    if (currentArray.compareAndSet(i, o, null))
                    {
                        int removedEntries = 0;
                        while (e != null)
                        {
                            removedEntries++;
                            e = e.getNext();
                        }
                        SIZE_UPDATER.getAndAdd(this, -removedEntries);
                    }
                }
            }
            if (followResize)
            {
                this.helpWithResize(currentArray, 0);
                this.rescanUntilDone(currentArray, rescanLocation, helpers);
                currentArray = (AtomicReferenceArray<Object>) currentArray.get(currentArray.length() - 1);
            }
        }
        while (followResize);
    }

    public Set<K> keySet()
    {
        return new KeySet();
    }

    public Collection<V> values()
    {
        return new Values();
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
        return new EntrySet();
    }

    public boolean replace(K key, V oldValue, V newValue)
    {
        throw new UnsupportedOperationException("replace not implemented");
    }

    public V replace(K key, V value)
    {
        throw new UnsupportedOperationException("replace not implemented");
    }

    public V remove(Object key)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        int length = currentArray.length();
        int index = indexFor(hash, length);
        Object o = currentArray.get(index);
        if (o == RESIZED || o instanceof ResizeContainer)
        {
            return this.slowRemove(key, hash, currentArray);
        }
        Entry<K, V> e = (Entry<K, V>) o;
        while (e != null)
        {
            Object candidate = e.getKey();
            if (candidate.equals(key))
            {
                Entry<K, V> replacement = this.createReplacementChainForRemoval((Entry<K, V>) o, e);
                if (currentArray.compareAndSet(index, o, replacement))
                {
                    SIZE_UPDATER.decrementAndGet(this);
                    return e.getValue();
                }
                return this.slowRemove(key, hash, currentArray);
            }
            e = e.getNext();
        }
        return null;
    }

    private V slowRemove(Object key, int hash, AtomicReferenceArray<Object> currentArray)
    {
        //noinspection LabeledStatement
        outer:
        while (true)
        {
            int length = currentArray.length();
            int index = ConcurrentHashMap.indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        Entry<K, V> replacement = this.createReplacementChainForRemoval((Entry<K, V>) o, e);
                        if (currentArray.compareAndSet(index, o, replacement))
                        {
                            SIZE_UPDATER.decrementAndGet(this);
                            return e.getValue();
                        }
                        //noinspection ContinueStatementWithLabel
                        continue outer;
                    }
                    e = e.getNext();
                }
                return null;
            }
        }
    }

    private Entry<K, V> createReplacementChainForRemoval(Entry<K, V> original, Entry<K, V> toRemove)
    {
        if (original == toRemove && original.getNext() == null)
        {
            return null;
        }
        Entry<K, V> replacement = null;
        Entry<K, V> e = original;
        while (e != null)
        {
            if (e != toRemove)
            {
                replacement = new Entry<K, V>(e.getKey(), e.getValue(), replacement);
            }
            e = e.getNext();
        }
        return replacement;
    }

    public void parallelForEachKeyValue(List<Procedure2<K, V>> blocks, Executor executor)
    {
        final AtomicReferenceArray<Object> currentArray = this.table;
        int chunks = blocks.size();
        if (chunks > 1)
        {
            FutureTask<?>[] futures = new FutureTask<?>[chunks];
            int chunkSize = currentArray.length() / chunks;
            if (currentArray.length() % chunks != 0)
            {
                chunkSize++;
            }
            for (int i = 0; i < chunks; i++)
            {
                final int start = i * chunkSize;
                final int end = Math.min((i + 1) * chunkSize, currentArray.length());
                final Procedure2<K, V> block = blocks.get(i);
                futures[i] = new FutureTask(new Runnable()
                {
                    public void run()
                    {
                        ConcurrentHashMap.this.sequentialForEachKeyValue(block, currentArray, start, end);
                    }
                }, null);
                executor.execute(futures[i]);
            }
            for (int i = 0; i < chunks; i++)
            {
                try
                {
                    futures[i].get();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("parallelForEachKeyValue failed", e);
                }
            }
        }
        else
        {
            this.sequentialForEachKeyValue(blocks.get(0), currentArray, 0, currentArray.length());
        }
    }

    private void sequentialForEachKeyValue(Procedure2<K, V> block, AtomicReferenceArray<Object> currentArray, int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            Object o = currentArray.get(i);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                throw new ConcurrentModificationException("can't iterate while resizing!");
            }
            Entry<K, V> e = (Entry<K, V>) o;
            while (e != null)
            {
                Object key = e.getKey();
                Object value = e.getValue();
                block.value((K) key, (V) value);
                e = e.getNext();
            }
        }
    }

    public void parallelForEachValue(List<Procedure<V>> blocks, Executor executor)
    {
        final AtomicReferenceArray<Object> currentArray = this.table;
        int chunks = blocks.size();
        if (chunks > 1)
        {
            FutureTask<?>[] futures = new FutureTask<?>[chunks];
            int chunkSize = currentArray.length() / chunks;
            if (currentArray.length() % chunks != 0)
            {
                chunkSize++;
            }
            for (int i = 0; i < chunks; i++)
            {
                final int start = i * chunkSize;
                final int end = Math.min((i + 1) * chunkSize, currentArray.length() - 1);
                final Procedure<V> block = blocks.get(i);
                futures[i] = new FutureTask(new Runnable()
                {
                    public void run()
                    {
                        ConcurrentHashMap.this.sequentialForEachValue(block, currentArray, start, end);
                    }
                }, null);
                executor.execute(futures[i]);
            }
            for (int i = 0; i < chunks; i++)
            {
                try
                {
                    futures[i].get();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("parallelForEachKeyValue failed", e);
                }
            }
        }
        else
        {
            this.sequentialForEachValue(blocks.get(0), currentArray, 0, currentArray.length());
        }
    }

    private void sequentialForEachValue(Procedure<V> block, AtomicReferenceArray<Object> currentArray, int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            Object o = currentArray.get(i);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                throw new ConcurrentModificationException("can't iterate while resizing!");
            }
            Entry<K, V> e = (Entry<K, V>) o;
            while (e != null)
            {
                Object value = e.getValue();
                block.value((V) value);
                e = e.getNext();
            }
        }
    }

    @Override
    public int hashCode()
    {
        int h = 0;
        AtomicReferenceArray<Object> currentArray = this.table;
        for (int i = 0; i < currentArray.length() - 1; i++)
        {
            Object o = currentArray.get(i);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                throw new ConcurrentModificationException("can't compute hashcode while resizing!");
            }
            Entry<K, V> e = (Entry<K, V>) o;
            while (e != null)
            {
                Object key = e.getKey();
                Object value = e.getValue();
                h += (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
                e = e.getNext();
            }
        }
        return h;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (!(o instanceof Map))
        {
            return false;
        }
        Map<K, V> m = (Map<K, V>) o;
        if (m.size() != this.size())
        {
            return false;
        }

        Iterator<Map.Entry<K, V>> i = this.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry<K, V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            if (value == null)
            {
                if (!(m.get(key) == null && m.containsKey(key)))
                {
                    return false;
                }
            }
            else
            {
                if (!value.equals(m.get(key)))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        if (this.isEmpty())
        {
            return "{}";
        }
        Iterator<Map.Entry<K, V>> iterator = this.entrySet().iterator();

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        while (true)
        {
            Map.Entry<K, V> e = iterator.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (!iterator.hasNext())
            {
                return sb.append('}').toString();
            }
            sb.append(", ");
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int size = in.readInt();
        int capacity = 1;
        while (capacity < size)
        {
            capacity <<= 1;
        }
        this.table = new AtomicReferenceArray<Object>(capacity + 1);
        for (int i = 0; i < size; i++)
        {
            this.put((K) in.readObject(), (V) in.readObject());
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        int size = this.size();
        out.writeInt(size);
        int count = 0;
        for (int i = 0; i < this.table.length() - 1; i++)
        {
            Object o = this.table.get(i);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                throw new ConcurrentModificationException("Can't serialize while resizing!");
            }
            Entry<K, V> e = (Entry<K, V>) o;
            while (e != null)
            {
                count++;
                out.writeObject(e.getKey());
                out.writeObject(e.getValue());
                e = e.getNext();
            }
        }
        if (count != size)
        {
            throw new ConcurrentModificationException("Map changed while serializing");
        }
    }

    private abstract class HashIterator<E> implements Iterator<E>
    {
        private Entry<K, V> next;
        private int index = 0;
        private Entry<K, V> current;
        private AtomicReferenceArray<Object> currentTable;

        protected HashIterator()
        {
            if (ConcurrentHashMap.this.size > 0)
            {
                this.currentTable = ConcurrentHashMap.this.table;
                this.findNext();
            }
        }

        private void findNext()
        {
            for (; this.index < this.currentTable.length() - 1; this.index++)
            {
                Object o = this.currentTable.get(this.index);
                if (o == RESIZED || o instanceof ResizeContainer)
                {
                    this.throwConcurrentException();
                }
                if (o != null)
                {
                    this.next = (Entry<K, V>) o;
                    this.index++;
                    break;
                }
            }
        }

        private void throwConcurrentException()
        {
            throw new ConcurrentModificationException("Can't iterate while resizing");
        }

        public final boolean hasNext()
        {
            return this.next != null;
        }

        final Entry<K, V> nextEntry()
        {
            Entry<K, V> e = this.next;
            if (e == null)
            {
                throw new NoSuchElementException();
            }

            if ((this.next = e.getNext()) == null)
            {
                this.findNext();
            }
            this.current = e;
            return e;
        }

        public void remove()
        {
            if (this.current == null)
            {
                throw new IllegalStateException();
            }
            K key = this.current.key;
            this.current = null;
            ConcurrentHashMap.this.remove(key);
        }
    }

    private final class ValueIterator extends HashIterator<V>
    {
        public V next()
        {
            return this.nextEntry().value;
        }
    }

    private final class KeyIterator extends HashIterator<K>
    {
        public K next()
        {
            return this.nextEntry().getKey();
        }
    }

    private final class EntryIterator extends HashIterator<Map.Entry<K, V>>
    {
        public Map.Entry<K, V> next()
        {
            return this.nextEntry();
        }
    }

    private final class KeySet extends AbstractSet<K>
    {
        @Override
        public Iterator<K> iterator()
        {
            return new KeyIterator();
        }

        @Override
        public int size()
        {
            return ConcurrentHashMap.this.size;
        }

        @Override
        public boolean contains(Object o)
        {
            return ConcurrentHashMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o)
        {
            return ConcurrentHashMap.this.remove(o) != null;
        }

        @Override
        public void clear()
        {
            ConcurrentHashMap.this.clear();
        }
    }

    private final class Values extends AbstractCollection<V>
    {
        @Override
        public Iterator<V> iterator()
        {
            return new ValueIterator();
        }

        @Override
        public int size()
        {
            return ConcurrentHashMap.this.size;
        }

        @Override
        public boolean contains(Object o)
        {
            return ConcurrentHashMap.this.containsValue(o);
        }

        @Override
        public void clear()
        {
            ConcurrentHashMap.this.clear();
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>>
    {
        @Override
        public Iterator<Map.Entry<K, V>> iterator()
        {
            return new EntryIterator();
        }

        @Override
        public boolean contains(Object o)
        {
            if (!(o instanceof Map.Entry<?, ?>))
            {
                return false;
            }
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            Entry<K, V> candidate = ConcurrentHashMap.this.getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        @Override
        public boolean remove(Object o)
        {
            if (!(o instanceof Map.Entry<?, ?>))
            {
                return false;
            }
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            return ConcurrentHashMap.this.remove(e.getKey(), e.getValue());
        }

        @Override
        public int size()
        {
            return ConcurrentHashMap.this.size;
        }

        @Override
        public void clear()
        {
            ConcurrentHashMap.this.clear();
        }
    }

    private static final class Entry<K, V> implements Map.Entry<K, V>
    {
        private final K key;
        private final V value;
        private final Entry<K, V> next;

        private Entry(K key, V value)
        {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        private Entry(K key, V value, Entry<K, V> next)
        {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey()
        {
            return this.key;
        }

        public V getValue()
        {
            return this.value;
        }

        public V setValue(V value)
        {
            throw new RuntimeException("not implemented");
        }

        public Entry<K, V> getNext()
        {
            return this.next;
        }

        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof Map.Entry<?, ?>))
            {
                return false;
            }
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            K k1 = this.key;
            Object k2 = e.getKey();
            if (k1 == k2 || k1 != null && k1.equals(k2))
            {
                V v1 = this.value;
                Object v2 = e.getValue();
                if (v1 == v2 || v1 != null && v1.equals(v2))
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        @Override
        public String toString()
        {
            return this.key + "=" + this.value;
        }
    }

    private static final class ResizeContainer<K, V>
    {
        private volatile Entry<K, V> entryChain;

        private ResizeContainer(Entry<K, V> entryChain)
        {
            this.entryChain = entryChain;
        }

        public Entry<K, V> getEntryChain()
        {
            return this.entryChain;
        }

        public void setEntryChain(Entry<K, V> entryChain)
        {
            this.entryChain = entryChain;
        }
    }

    public static <NK, NV> ConcurrentHashMap<NK, NV> newMap(Map<NK, NV> map)
    {
        ConcurrentHashMap<NK, NV> result = new ConcurrentHashMap<NK, NV>(map.size());
        result.putAll(map);
        return result;
    }

    @Override
    public ConcurrentHashMap<K, V> withKeyValue(K key, V value)
    {
        return (ConcurrentHashMap<K, V>) super.withKeyValue(key, value);
    }

    @Override
    public ConcurrentHashMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        return (ConcurrentHashMap<K, V>) super.withAllKeyValues(keyValues);
    }

    @Override
    public ConcurrentHashMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValues)
    {
        return (ConcurrentHashMap<K, V>) super.withAllKeyValueArguments(keyValues);
    }

    @Override
    public ConcurrentHashMap<K, V> withoutKey(K key)
    {
        return (ConcurrentHashMap<K, V>) super.withoutKey(key);
    }

    @Override
    public ConcurrentHashMap<K, V> withoutAllKeys(Iterable<? extends K> keys)
    {
        return (ConcurrentHashMap<K, V>) super.withoutAllKeys(keys);
    }

    @Override
    public MutableMap<K, V> clone()
    {
        return ConcurrentHashMap.newMap(this);
    }

    @Override
    public <K, V> MutableMap<K, V> newEmpty(int capacity)
    {
        return ConcurrentHashMap.newMap();
    }

    @Override
    public boolean notEmpty()
    {
        return !this.isEmpty();
    }

    @Override
    public void forEach(Procedure<? super V> procedure)
    {
        IterableIterate.forEach(this.values(), procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        Iterate.forEachWithIndex(this.values(), objectIntProcedure);
    }

    @Override
    public Iterator<V> iterator()
    {
        return this.values().iterator();
    }

    public MutableMap<K, V> newEmpty()
    {
        return ConcurrentHashMap.newMap();
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        IterableIterate.forEach(this.values(), procedure);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        IterableIterate.forEach(this.keySet(), procedure);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        IterableIterate.forEach(this.entrySet(), new MapEntryToProcedure2<K, V>(procedure));
    }

    public <E> MutableMap<K, V> collectKeysAndValues(Collection<E> collection, Function<? super E, ? extends K> keyFunction, Function<? super E, ? extends V> valueFunction)
    {
        Iterate.addToMap(collection, keyFunction, valueFunction, this);
        return this;
    }

    public V removeKey(K key)
    {
        return this.remove(key);
    }

    @Override
    public <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        int hash = this.hash(key);
        AtomicReferenceArray<Object> currentArray = this.table;
        V newValue = null;
        boolean createdValue = false;
        while (true)
        {
            int length = currentArray.length();
            int index = indexFor(hash, length);
            Object o = currentArray.get(index);
            if (o == RESIZED || o instanceof ResizeContainer)
            {
                currentArray = this.helpWithResizeWhileCurrentIndex(currentArray, index);
            }
            else
            {
                Entry<K, V> e = (Entry<K, V>) o;
                while (e != null)
                {
                    Object candidate = e.getKey();
                    if (candidate.equals(key))
                    {
                        return e.getValue();
                    }
                    e = e.getNext();
                }
                if (!createdValue)
                {
                    createdValue = true;
                    newValue = function.valueOf(parameter);
                }
                Entry<K, V> newEntry = new Entry<K, V>(key, newValue, (Entry<K, V>) o);
                if (currentArray.compareAndSet(index, o, newEntry))
                {
                    this.incrementSizeAndPossiblyResize(currentArray, length);
                    return newValue;
                }
            }
        }
    }

    @Override
    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        V result = this.get(key);
        if (result == null)
        {
            return function.value();
        }
        return result;
    }

    @Override
    public <P> V getIfAbsentWith(K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        V result = this.get(key);
        if (result == null)
        {
            return function.valueOf(parameter);
        }
        return result;
    }

    @Override
    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        V result = this.get(key);
        return result == null ? null : function.valueOf(result);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.values(), procedure, parameter);
    }
}
