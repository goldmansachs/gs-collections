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

package com.gs.collections.impl.map.strategy.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.UnsortedMapIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.factory.HashingStrategyMaps;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.AbstractMutableMap;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.NotThreadSafe;

/**
 * The core collections in Java get used all over the place. Unfortunately, most of them are not as good as they could be.
 * HashSet is probably the worst collection, with ArrayList being the best. Even ArrayList is not all that good, as the
 * FastList implementation in fwcommon has shown.
 * <p/>
 * A long time ago we gave up on HashSet and HashMap and started using the Trove collections instead (THashSet, THashMap).
 * HashMap is a real memory hog. For every put, it creates an object that wraps the key/value pair. If you've done some
 * Java memory profiling, you've probably ran into the HashMap$Entry classes that are almost always near the top.
 * THashMap doesn't suffer from the same malady. It instead uses two arrays: one to hold the keys and one to hold values.
 * <p/>
 * The HashMap implementation uses a linked list composed of the Entry objects to resolve hash bucket conflicts. So if
 * two objects hash to the same bucket, the second one is added as a link off the first one.
 * <p/>
 * With Trove, since there are no Entry objects, it's not possible to do the same for conflict resolution. Instead Trove
 * uses something called "open addressing" with double hashing. If there is a conflict, it find another bucket elsewhere
 * to put the entry in. This makes removals a bit complicated, but I'll leave the details as an exercise to the reader.
 * The cost of conflict resolution in trove is generally higher than HashMap. Finally, Trove uses prime number remainder
 * operations to find the hash bucket, which is slower than binary math operations used in HashMap. In general, HashMap
 * can have a speed advantage in get & remove (especially with JDK 1.6), but uses more memory.
 * <p/>
 * I had an idea for creating a faster map that was also lean. It was the culmination of working on sets and maps and
 * having looked at several different implementations. What would happen if we use a single array, where alternate slots
 * were keys and values? This is nicer to the CPU caches as consecutive memory addresses are very cheap to access. If
 * we do that, we won't have Entry objects, but then what about conflict resolution? I tried a couple of Trove like
 * solutions to the conflict scenario, but they turned out to be just as slow (or slower) than trove. So instead of
 * trying to fit the conflict in the main array, I borrowed the idea from HashMap but without the extra cost (that is,
 * no Entry objects). When a conflict happens, we put a special object in the key slot and put a regular Object[] in the
 * value slot. It's sortThis of like the difference between ArrayList and LinkedList. ArrayList is much faster than LinkedList
 * and arrays are even faster than ArrayList. The array contains the key value pairs in consecutive slots, just like the
 * main array, but it's a linear list with no hashing.
 * <p/>
 * The final result is a Map implementation that's faster than HashMap and leaner than Trove. The best of both implementations
 * unified together, aka UnifiedMap.
 */

@NotThreadSafe
public class UnifiedMapWithHashingStrategy<K, V> extends AbstractMutableMap<K, V>
        implements Externalizable, BatchIterable<V>
{
    protected static final Object NULL_KEY = new Object();

    protected static final Object CHAINED_KEY = new Object();

    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected static final int DEFAULT_INITIAL_CAPACITY = 8;

    private static final long serialVersionUID = 1L;

    protected transient Object[] table;

    protected transient int occupied;

    protected float loadFactor = DEFAULT_LOAD_FACTOR;

    protected int maxSize;

    private HashingStrategy<? super K> hashingStrategy;

    /**
     * @deprecated No argument default constructor used for serialization. Instantiating an UnifiedMapWithHashingStrategyMultimap with
     *             this constructor will have a null hashingStrategy and throw NullPointerException when used.
     */
    @Deprecated
    public UnifiedMapWithHashingStrategy()
    {
    }

    public UnifiedMapWithHashingStrategy(HashingStrategy<? super K> hashingStrategy)
    {
        this.hashingStrategy = hashingStrategy;
        this.allocate(DEFAULT_INITIAL_CAPACITY << 1);
    }

    public UnifiedMapWithHashingStrategy(HashingStrategy<? super K> hashingStrategy, int initialCapacity)
    {
        this(hashingStrategy, initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public UnifiedMapWithHashingStrategy(HashingStrategy<? super K> hashingStrategy, int initialCapacity, float loadFactor)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("initial capacity cannot be less than 0");
        }
        this.hashingStrategy = hashingStrategy;
        this.loadFactor = loadFactor;
        this.init(this.fastCeil(initialCapacity / loadFactor));
    }

    public UnifiedMapWithHashingStrategy(HashingStrategy<? super K> hashingStrategy, Map<? extends K, ? extends V> map)
    {
        this(hashingStrategy, Math.max(map.size(), DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);

        this.putAll(map);
    }

    public UnifiedMapWithHashingStrategy(HashingStrategy<? super K> hashingStrategy, Pair<K, V>... pairs)
    {
        this(hashingStrategy, Math.max(pairs.length, DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
        ArrayIterate.forEach(pairs, new MapCollectProcedure<Pair<K, V>, K, V>(
                this,
                Functions.<K>firstOfPair(),
                Functions.<V>secondOfPair()));
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newMap(HashingStrategy<? super K> hashingStrategy)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newMap(UnifiedMapWithHashingStrategy<K, V> map)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(map.hashingStrategy, map);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newMap(
            HashingStrategy<? super K> hashingStrategy,
            int size)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, size);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newMap(
            HashingStrategy<? super K> hashingStrategy,
            int size,
            float loadFactor)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, size, loadFactor);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newMap(
            HashingStrategy<? super K> hashingStrategy,
            Map<? extends K, ? extends V> map)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, map);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newMapWith(
            HashingStrategy<? super K> hashingStrategy,
            Pair<K, V>... pairs)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, pairs);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newWithKeysValues(
            HashingStrategy<? super K> hashingStrategy,
            K key, V value)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, 1).withKeysValues(key, value);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newWithKeysValues(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, 2).withKeysValues(key1, value1, key2, value2);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newWithKeysValues(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, 3).withKeysValues(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> UnifiedMapWithHashingStrategy<K, V> newWithKeysValues(
            HashingStrategy<? super K> hashingStrategy,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        return new UnifiedMapWithHashingStrategy<K, V>(hashingStrategy, 4).withKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public UnifiedMapWithHashingStrategy<K, V> withKeysValues(K key, V value)
    {
        this.put(key, value);
        return this;
    }

    public UnifiedMapWithHashingStrategy<K, V> withKeysValues(K key1, V value1, K key2, V value2)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        return this;
    }

    public UnifiedMapWithHashingStrategy<K, V> withKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        this.put(key3, value3);
        return this;
    }

    public UnifiedMapWithHashingStrategy<K, V> withKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        this.put(key3, value3);
        this.put(key4, value4);
        return this;
    }

    public HashingStrategy<? super K> hashingStrategy()
    {
        return this.hashingStrategy;
    }

    @Override
    public UnifiedMapWithHashingStrategy<K, V> clone()
    {
        return new UnifiedMapWithHashingStrategy<K, V>(this.hashingStrategy, this);
    }

    public MutableMap<K, V> newEmpty()
    {
        return new UnifiedMapWithHashingStrategy<K, V>(this.hashingStrategy);
    }

    @Override
    public MutableMap<K, V> newEmpty(int capacity)
    {
        return UnifiedMapWithHashingStrategy.newMap(this.hashingStrategy, capacity);
    }

    private int fastCeil(float v)
    {
        int possibleResult = (int) v;
        if (v - possibleResult > 0.0F)
        {
            possibleResult++;
        }
        return possibleResult;
    }

    protected int init(int initialCapacity)
    {
        int capacity = 1;
        while (capacity < initialCapacity)
        {
            capacity <<= 1;
        }

        return this.allocate(capacity);
    }

    protected int allocate(int capacity)
    {
        this.allocateTable(capacity << 1); // the table size is twice the capacity to handle both keys and values
        this.computeMaxSize(capacity);

        return capacity;
    }

    protected void allocateTable(int sizeToAllocate)
    {
        this.table = new Object[sizeToAllocate];
    }

    protected void computeMaxSize(int capacity)
    {
        // need at least one free slot for open addressing
        this.maxSize = Math.min(capacity - 1,
                (int) (capacity * this.loadFactor));
    }

    protected final int index(K key)
    {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        int h = this.hashingStrategy.computeHashCode(key);
        h ^= (h >>> 20) ^ (h >>> 12);
        h ^= (h >>> 7) ^ (h >>> 4);
        return (h & (this.table.length >> 1) - 1) << 1;
    }

    public void clear()
    {
        if (this.occupied == 0)
        {
            return;
        }
        this.occupied = 0;
        Object[] set = this.table;

        for (int i = set.length; i-- > 0; )
        {
            set[i] = null;
        }
    }

    public V put(K key, V value)
    {
        int index = this.index(key);
        V result = null;
        Object cur = this.table[index];
        if (cur == null)
        {
            this.table[index] = this.toSentinelIfNull(key);
            this.table[index + 1] = value;
            if (++this.occupied > this.maxSize)
            {
                this.rehash(this.table.length);
            }
        }
        else if (cur != CHAINED_KEY && this.hashingStrategyEquals(this.nonSentinel(cur), key))
        {
            result = (V) this.table[index + 1];
            this.table[index + 1] = value;
        }
        else
        {
            result = this.chainedPut(key, index, value);
        }
        return result;
    }

    private V chainedPut(K key, int index, V value)
    {
        V result = null;
        if (this.table[index] == CHAINED_KEY)
        {
            Object[] chain = (Object[]) this.table[index + 1];
            int i = 0;
            for (; i < chain.length; i += 2)
            {
                if (chain[i] == null)
                {
                    chain[i] = this.toSentinelIfNull(key);
                    chain[i + 1] = value;
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash(this.table.length);
                    }
                    break;
                }
                if (this.hashingStrategyEquals(this.nonSentinel(chain[i]), key))
                {
                    result = (V) chain[i + 1];
                    chain[i + 1] = value;
                    break;
                }
            }
            if (i == chain.length)
            {
                Object[] newChain = new Object[chain.length + 4];
                System.arraycopy(chain, 0, newChain, 0, chain.length);
                this.table[index + 1] = newChain;
                newChain[i] = this.toSentinelIfNull(key);
                newChain[i + 1] = value;
                if (++this.occupied > this.maxSize)
                {
                    this.rehash(this.table.length);
                }
            }
        }
        else
        {
            Object[] newChain = new Object[4];
            newChain[0] = this.table[index];
            newChain[1] = this.table[index + 1];
            newChain[2] = this.toSentinelIfNull(key);
            newChain[3] = value;
            this.table[index] = CHAINED_KEY;
            this.table[index + 1] = newChain;
            if (++this.occupied > this.maxSize)
            {
                this.rehash(this.table.length);
            }
        }
        return result;
    }

    @Override
    public V getIfAbsentPut(K key, Function0<? extends V> function)
    {
        int index = this.index(key);
        Object cur = this.table[index];

        if (cur == null)
        {
            V result = function.value();
            this.table[index] = this.toSentinelIfNull(key);
            this.table[index + 1] = result;
            if (++this.occupied > this.maxSize)
            {
                this.rehash(this.table.length);
            }
            return result;
        }
        if (cur != CHAINED_KEY && this.hashingStrategyEquals(this.nonSentinel(cur), key))
        {
            return (V) this.table[index + 1];
        }
        return this.chainedGetIfAbsentPut(key, index, function);
    }

    private V chainedGetIfAbsentPut(K key, int index, Function0<? extends V> function)
    {
        V result = null;
        if (this.table[index] == CHAINED_KEY)
        {
            Object[] chain = (Object[]) this.table[index + 1];
            int i = 0;
            for (; i < chain.length; i += 2)
            {
                if (chain[i] == null)
                {
                    result = function.value();
                    chain[i] = this.toSentinelIfNull(key);
                    chain[i + 1] = result;
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash(this.table.length);
                    }
                    break;
                }
                if (this.hashingStrategyEquals(this.nonSentinel(chain[i]), key))
                {
                    result = (V) chain[i + 1];
                    break;
                }
            }
            if (i == chain.length)
            {
                result = function.value();
                Object[] newChain = new Object[chain.length + 4];
                System.arraycopy(chain, 0, newChain, 0, chain.length);
                newChain[i] = this.nonSentinel(key);
                newChain[i + 1] = result;
                this.table[index + 1] = newChain;
                if (++this.occupied > this.maxSize)
                {
                    this.rehash(this.table.length);
                }
            }
        }
        else
        {
            result = function.value();
            Object[] newChain = new Object[4];
            newChain[0] = this.table[index];
            newChain[1] = this.table[index + 1];
            newChain[2] = this.nonSentinel(key);
            newChain[3] = result;
            this.table[index] = CHAINED_KEY;
            this.table[index + 1] = newChain;
            if (++this.occupied > this.maxSize)
            {
                this.rehash(this.table.length);
            }
        }
        return result;
    }

    public int getCollidingBuckets()
    {
        int count = 0;
        for (int i = 0; i < this.table.length; i += 2)
        {
            if (this.table[i] == CHAINED_KEY)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of JVM words that is used by this map.  A word is 4 bytes in a 32bit VM and 8 bytes in a 64bit
     * VM. Each array has a 2 word header, thus the formula is:
     * words = (internal table length + 2) + sum (for all chains (chain length + 2))
     *
     * @return the number of JVM words that is used by this map.
     */
    public int getMapMemoryUsedInWords()
    {
        int headerSize = 2;
        int sizeInWords = this.table.length + headerSize;
        for (int i = 0; i < this.table.length; i += 2)
        {
            if (this.table[i] == CHAINED_KEY)
            {
                sizeInWords += headerSize + ((Object[]) this.table[i + 1]).length;
            }
        }
        return sizeInWords;
    }

    protected void rehash(int newCapacity)
    {
        int oldLength = this.table.length;
        Object[] old = this.table;
        this.allocate(newCapacity);
        this.occupied = 0;

        for (int i = 0; i < oldLength; i += 2)
        {
            Object oldKey = old[i];
            if (oldKey == CHAINED_KEY)
            {
                Object[] chain = (Object[]) old[i + 1];
                for (int j = 0; j < chain.length; j += 2)
                {
                    if (chain[j] != null)
                    {
                        this.put(this.nonSentinel(chain[j]), (V) chain[j + 1]);
                    }
                }
            }
            else if (oldKey != null)
            {
                this.put(this.nonSentinel(oldKey), (V) old[i + 1]);
            }
        }
    }

    public V get(Object key)
    {
        int index = this.index((K) key);
        Object cur = this.table[index];
        if (cur != null)
        {
            Object val = this.table[index + 1];
            if (cur == CHAINED_KEY)
            {
                return this.getFromChain((Object[]) val, (K) key);
            }
            if (this.hashingStrategyEquals(this.nonSentinel(cur), (K) key))
            {
                return (V) val;
            }
        }
        return null;
    }

    private V getFromChain(Object[] chain, K key)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object k = chain[i];
            if (k == null)
            {
                return null;
            }
            if (this.hashingStrategyEquals(this.nonSentinel(k), key))
            {
                return (V) chain[i + 1];
            }
        }
        return null;
    }

    public boolean containsKey(Object key)
    {
        int index = this.index((K) key);
        Object cur = this.table[index];
        if (cur == null)
        {
            return false;
        }
        if (cur != CHAINED_KEY && this.hashingStrategyEquals(this.nonSentinel(cur), (K) key))
        {
            return true;
        }
        return cur == CHAINED_KEY && this.chainContainsKey((Object[]) this.table[index + 1], (K) key);
    }

    private boolean chainContainsKey(Object[] chain, K key)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object k = chain[i];
            if (k == null)
            {
                return false;
            }
            if (this.hashingStrategyEquals(this.nonSentinel(k), key))
            {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(Object value)
    {
        for (int i = 0; i < this.table.length; i += 2)
        {
            if (this.table[i] == CHAINED_KEY)
            {
                if (this.chainedContainsValue((Object[]) this.table[i + 1], value))
                {
                    return true;
                }
            }
            else if (this.table[i] != null)
            {
                if (UnifiedMapWithHashingStrategy.nullSafeEquals(value, this.table[i + 1]))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean chainedContainsValue(Object[] chain, Object value)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            if (chain[i] == null)
            {
                return false;
            }
            if (UnifiedMapWithHashingStrategy.nullSafeEquals(value, chain[i + 1]))
            {
                return true;
            }
        }
        return false;
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        for (int i = 0; i < this.table.length; i += 2)
        {
            Object key = this.table[i];
            if (key == CHAINED_KEY)
            {
                this.chainedForEachEntry((Object[]) this.table[i + 1], procedure);
            }
            else if (key != null)
            {
                procedure.value(this.nonSentinel(key), (V) this.table[i + 1]);
            }
        }
    }

    public <E> MutableMap<K, V> collectKeysAndValues(
            Collection<E> collection,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction)
    {
        Iterate.forEach(collection, new MapCollectProcedure<E, K, V>(this, keyFunction, valueFunction));
        return this;
    }

    public V removeKey(K key)
    {
        return this.remove(key);
    }

    private void chainedForEachEntry(Object[] chain, Procedure2<? super K, ? super V> procedure)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object key = chain[i];
            if (key == null)
            {
                return;
            }
            procedure.value(this.nonSentinel(key), (V) chain[i + 1]);
        }
    }

    public int getBatchCount(int batchSize)
    {
        return Math.max(1, this.table.length / 2 / batchSize);
    }

    public void batchForEach(Procedure<? super V> procedure, int sectionIndex, int sectionCount)
    {
        this.mapBatchForEach(this.table, procedure, sectionIndex, sectionCount);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        for (int i = 0; i < this.table.length; i += 2)
        {
            Object key = this.table[i];
            if (key == CHAINED_KEY)
            {
                this.chainedForEachKey((Object[]) this.table[i + 1], procedure);
            }
            else if (key != null)
            {
                procedure.value(this.nonSentinel(key));
            }
        }
    }

    private void chainedForEachKey(Object[] chain, Procedure<? super K> procedure)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object key = chain[i];
            if (key == null)
            {
                return;
            }
            procedure.value(this.nonSentinel(key));
        }
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        for (int i = 0; i < this.table.length; i += 2)
        {
            Object key = this.table[i];
            if (key == CHAINED_KEY)
            {
                this.chainedForEachValue((Object[]) this.table[i + 1], procedure);
            }
            else if (key != null)
            {
                procedure.value((V) this.table[i + 1]);
            }
        }
    }

    @Override
    public boolean isEmpty()
    {
        return this.occupied == 0;
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        if (map instanceof UnifiedMapWithHashingStrategy<?, ?>)
        {
            this.copyMap((UnifiedMapWithHashingStrategy<K, V>) map);
        }
        else if (map instanceof UnsortedMapIterable)
        {
            MapIterable<K, V> mapIterable = (MapIterable<K, V>) map;
            mapIterable.forEachKeyValue(new Procedure2<K, V>()
            {
                public void value(K key, V value)
                {
                    UnifiedMapWithHashingStrategy.this.put(key, value);
                }
            });
        }
        else
        {
            Iterator<? extends Entry<? extends K, ? extends V>> iterator = this.getEntrySetFrom(map).iterator();
            while (iterator.hasNext())
            {
                Entry<? extends K, ? extends V> entry = iterator.next();
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private Set<? extends Entry<? extends K, ? extends V>> getEntrySetFrom(Map<? extends K, ? extends V> map)
    {
        Set<? extends Entry<? extends K, ? extends V>> entries = map.entrySet();
        if (entries != null)
        {
            return entries;
        }
        if (map.size() == 0)
        {
            return Sets.immutable.<Entry<K, V>>of().castToSet();
        }
        throw new IllegalStateException("Entry set was null and size was non-zero");
    }

    protected void copyMap(UnifiedMapWithHashingStrategy<K, V> unifiedMap)
    {
        for (int i = 0; i < unifiedMap.table.length; i += 2)
        {
            Object key = unifiedMap.table[i];
            if (key == CHAINED_KEY)
            {
                this.copyChain((Object[]) unifiedMap.table[i + 1]);
            }
            else if (key != null)
            {
                this.put(this.nonSentinel(key), (V) unifiedMap.table[i + 1]);
            }
        }
    }

    private void copyChain(Object[] chain)
    {
        for (int j = 0; j < chain.length; j += 2)
        {
            Object key = chain[j];
            if (key == null)
            {
                break;
            }
            this.put(this.nonSentinel(key), (V) chain[j + 1]);
        }
    }

    public V remove(Object key)
    {
        int index = this.index((K) key);
        Object cur = this.table[index];
        if (cur != null)
        {
            Object val = this.table[index + 1];
            if (cur == CHAINED_KEY)
            {
                return this.removeFromChain((Object[]) val, (K) key, index);
            }
            if (this.hashingStrategyEquals(this.nonSentinel(cur), (K) key))
            {
                this.table[index] = null;
                this.table[index + 1] = null;
                this.occupied--;
                return (V) val;
            }
        }
        return null;
    }

    private V removeFromChain(Object[] chain, K key, int index)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object k = chain[i];
            if (k == null)
            {
                return null;
            }
            if (this.hashingStrategyEquals(this.nonSentinel(k), key))
            {
                V val = (V) chain[i + 1];
                this.overwriteWithLastElementFromChain(chain, index, i);
                return val;
            }
        }
        return null;
    }

    private void overwriteWithLastElementFromChain(Object[] chain, int index, int i)
    {
        int j = chain.length - 2;
        for (; j > i; j -= 2)
        {
            if (chain[j] != null)
            {
                chain[i] = chain[j];
                chain[i + 1] = chain[j + 1];
                break;
            }
        }
        chain[j] = null;
        chain[j + 1] = null;
        if (j == 0)
        {
            this.table[index] = null;
            this.table[index + 1] = null;
        }
        this.occupied--;
    }

    public int size()
    {
        return this.occupied;
    }

    public Set<Entry<K, V>> entrySet()
    {
        return new EntrySet();
    }

    public Set<K> keySet()
    {
        return new KeySet();
    }

    public Collection<V> values()
    {
        return new ValuesCollection();
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (!(object instanceof Map))
        {
            return false;
        }

        Map<?, ?> other = (Map<?, ?>) object;
        if (this.size() != other.size())
        {
            return false;
        }
        return Iterate.allSatisfy(other.entrySet(), Predicates.in(this.entrySet()));
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        for (int i = 0; i < this.table.length; i += 2)
        {
            Object key = this.table[i];
            if (key == CHAINED_KEY)
            {
                hashCode += this.chainedHashCode((Object[]) this.table[i + 1]);
            }
            else if (key != null)
            {
                Object value = this.table[i + 1];
                hashCode += this.hashingStrategy.computeHashCode(this.nonSentinel(key)) ^ (value == null ? 0 : value.hashCode());
            }
        }
        return hashCode;
    }

    private int chainedHashCode(Object[] chain)
    {
        int hashCode = 0;
        for (int i = 0; i < chain.length; i += 2)
        {
            Object key = chain[i];
            if (key == null)
            {
                return hashCode;
            }
            Object value = chain[i + 1];
            hashCode += this.hashingStrategy.computeHashCode(this.nonSentinel(key)) ^ (value == null ? 0 : value.hashCode());
        }
        return hashCode;
    }

    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append('{');

        Iterator<Entry<K, V>> iterator = this.entrySet().iterator();
        boolean hasNext = iterator.hasNext();
        while (hasNext)
        {
            Entry<K, V> e = iterator.next();
            K key = e.getKey();
            V value = e.getValue();
            buf.append(key == this ? "(this Map)" : key);
            buf.append('=');
            buf.append(value == this ? "(this Map)" : value);
            hasNext = iterator.hasNext();
            if (hasNext)
            {
                buf.append(", ");
            }
        }

        buf.append('}');
        return buf.toString();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.hashingStrategy = (HashingStrategy<? super K>) in.readObject();
        int size = in.readInt();
        this.loadFactor = in.readFloat();
        this.init(Math.max((int) (size / this.loadFactor) + 1,
                DEFAULT_INITIAL_CAPACITY));
        for (int i = 0; i < size; i++)
        {
            this.put((K) in.readObject(), (V) in.readObject());
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.hashingStrategy);
        out.writeInt(this.size());
        out.writeFloat(this.loadFactor);
        for (int i = 0; i < this.table.length; i += 2)
        {
            Object o = this.table[i];
            if (o != null)
            {
                if (o == CHAINED_KEY)
                {
                    this.writeExternalChain(out, (Object[]) this.table[i + 1]);
                }
                else
                {
                    if (o == NULL_KEY)
                    {
                        out.writeObject(null);
                    }
                    else
                    {
                        out.writeObject(o);
                    }
                    out.writeObject(this.table[i + 1]);
                }
            }
        }
    }

    private void writeExternalChain(ObjectOutput out, Object[] chain) throws IOException
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object key = chain[i];
            if (key == null)
            {
                return;
            }
            if (key == NULL_KEY)
            {
                out.writeObject(null);
            }
            else
            {
                out.writeObject(key);
            }
            out.writeObject(chain[i + 1]);
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        int index = 0;
        for (int i = 0; i < this.table.length; i += 2)
        {
            Object key = this.table[i];
            if (key == CHAINED_KEY)
            {
                index = this.chainedForEachValueWithIndex((Object[]) this.table[i + 1], objectIntProcedure, index);
            }
            else if (key != null)
            {
                objectIntProcedure.value((V) this.table[i + 1], index++);
            }
        }
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        for (int i = 0; i < this.table.length; i += 2)
        {
            Object key = this.table[i];
            if (key == CHAINED_KEY)
            {
                this.chainedForEachValueWith((Object[]) this.table[i + 1], procedure, parameter);
            }
            else if (key != null)
            {
                procedure.value((V) this.table[i + 1], parameter);
            }
        }
    }

    protected class KeySet implements Set<K>, Serializable, BatchIterable<K>
    {
        private static final long serialVersionUID = 1L;

        public boolean add(K key)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends K> collection)
        {
            throw new UnsupportedOperationException();
        }

        public void clear()
        {
            UnifiedMapWithHashingStrategy.this.clear();
        }

        public boolean contains(Object o)
        {
            return UnifiedMapWithHashingStrategy.this.containsKey(o);
        }

        public boolean containsAll(Collection<?> collection)
        {
            for (Object aCollection : collection)
            {
                if (!UnifiedMapWithHashingStrategy.this.containsKey(aCollection))
                {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty()
        {
            return UnifiedMapWithHashingStrategy.this.isEmpty();
        }

        public Iterator<K> iterator()
        {
            return new KeySetIterator();
        }

        public boolean remove(Object key)
        {
            int oldSize = UnifiedMapWithHashingStrategy.this.occupied;
            UnifiedMapWithHashingStrategy.this.remove(key);
            return UnifiedMapWithHashingStrategy.this.occupied != oldSize;
        }

        public boolean removeAll(Collection<?> collection)
        {
            int oldSize = UnifiedMapWithHashingStrategy.this.occupied;
            for (Object object : collection)
            {
                UnifiedMapWithHashingStrategy.this.remove(object);
            }
            return oldSize != UnifiedMapWithHashingStrategy.this.occupied;
        }

        public void putIfFound(Object key, Map<K, V> other)
        {
            int index = UnifiedMapWithHashingStrategy.this.index((K) key);
            Object cur = UnifiedMapWithHashingStrategy.this.table[index];
            if (cur != null)
            {
                Object val = UnifiedMapWithHashingStrategy.this.table[index + 1];
                if (cur == CHAINED_KEY)
                {
                    this.putIfFoundFromChain((Object[]) val, (K) key, other);
                    return;
                }
                if (UnifiedMapWithHashingStrategy.this.hashingStrategyEquals(UnifiedMapWithHashingStrategy.this.nonSentinel(cur), (K) key))
                {
                    other.put(UnifiedMapWithHashingStrategy.this.nonSentinel(cur), (V) val);
                    return;
                }
            }
        }

        private void putIfFoundFromChain(Object[] chain, K key, Map<K, V> other)
        {
            for (int i = 0; i < chain.length; i += 2)
            {
                Object k = chain[i];
                if (k == null)
                {
                    return;
                }
                if (UnifiedMapWithHashingStrategy.this.hashingStrategyEquals(UnifiedMapWithHashingStrategy.this.nonSentinel(k), key))
                {
                    other.put(UnifiedMapWithHashingStrategy.this.nonSentinel(k), (V) chain[i + 1]);
                }
            }
        }

        public boolean retainAll(Collection<?> collection)
        {
            int retainedSize = collection.size();
            UnifiedMapWithHashingStrategy<K, V> retainedCopy = new UnifiedMapWithHashingStrategy<K, V>(
                    UnifiedMapWithHashingStrategy.this.hashingStrategy, retainedSize, UnifiedMapWithHashingStrategy.this.loadFactor);
            for (Object key : collection)
            {
                this.putIfFound(key, retainedCopy);
            }
            if (retainedCopy.size() < this.size())
            {
                UnifiedMapWithHashingStrategy.this.maxSize = retainedCopy.maxSize;
                UnifiedMapWithHashingStrategy.this.occupied = retainedCopy.occupied;
                UnifiedMapWithHashingStrategy.this.table = retainedCopy.table;
                return true;
            }
            return false;
        }

        public int size()
        {
            return UnifiedMapWithHashingStrategy.this.size();
        }

        public void forEach(Procedure<? super K> procedure)
        {
            UnifiedMapWithHashingStrategy.this.forEachKey(procedure);
        }

        public int getBatchCount(int batchSize)
        {
            return UnifiedMapWithHashingStrategy.this.getBatchCount(batchSize);
        }

        public void batchForEach(Procedure<? super K> procedure, int sectionIndex, int sectionCount)
        {
            Object[] map = UnifiedMapWithHashingStrategy.this.table;
            int sectionSize = map.length / sectionCount;
            int start = sectionIndex * sectionSize;
            int end = sectionIndex == sectionCount - 1 ? map.length : start + sectionSize;
            if (start % 2 != 0)
            {
                start++;
            }
            for (int i = start; i < end; i += 2)
            {
                Object key = map[i];
                if (key == CHAINED_KEY)
                {
                    UnifiedMapWithHashingStrategy.this.chainedForEachKey((Object[]) map[i + 1], procedure);
                }
                else if (key != null)
                {
                    procedure.value(UnifiedMapWithHashingStrategy.this.nonSentinel(key));
                }
            }
        }

        protected void copyKeys(Object[] result)
        {
            Object[] table = UnifiedMapWithHashingStrategy.this.table;
            int count = 0;
            for (int i = 0; i < table.length; i += 2)
            {
                Object x = table[i];
                if (x != null)
                {
                    if (x == CHAINED_KEY)
                    {
                        Object[] chain = (Object[]) table[i + 1];
                        for (int j = 0; j < chain.length; j += 2)
                        {
                            Object key = chain[j];
                            if (key == null)
                            {
                                break;
                            }
                            result[count++] = UnifiedMapWithHashingStrategy.this.nonSentinel(key);
                        }
                    }
                    else
                    {
                        result[count++] = UnifiedMapWithHashingStrategy.this.nonSentinel(x);
                    }
                }
            }
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Set)
            {
                Set<?> other = (Set<?>) obj;
                if (other.size() == this.size())
                {
                    return this.containsAll(other);
                }
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            int hashCode = 0;
            Object[] table = UnifiedMapWithHashingStrategy.this.table;
            for (int i = 0; i < table.length; i += 2)
            {
                Object x = table[i];
                if (x != null)
                {
                    if (x == CHAINED_KEY)
                    {
                        Object[] chain = (Object[]) table[i + 1];
                        for (int j = 0; j < chain.length; j += 2)
                        {
                            Object key = chain[j];
                            if (key == null)
                            {
                                break;
                            }
                            hashCode += UnifiedMapWithHashingStrategy.this.hashingStrategy.computeHashCode(UnifiedMapWithHashingStrategy.this.nonSentinel(key));
                        }
                    }
                    else
                    {
                        hashCode += UnifiedMapWithHashingStrategy.this.hashingStrategy.computeHashCode(UnifiedMapWithHashingStrategy.this.nonSentinel(x));
                    }
                }
            }
            return hashCode;
        }

        public Object[] toArray()
        {
            int size = UnifiedMapWithHashingStrategy.this.size();
            Object[] result = new Object[size];
            this.copyKeys(result);
            return result;
        }

        public <T> T[] toArray(T[] result)
        {
            int size = UnifiedMapWithHashingStrategy.this.size();
            if (result.length < size)
            {
                result = (T[]) Array.newInstance(result.getClass().getComponentType(), size);
            }
            this.copyKeys(result);
            if (size < result.length)
            {
                result[size] = null;
            }
            return result;
        }

        protected Object writeReplace()
        {
            UnifiedSetWithHashingStrategy<K> replace = UnifiedSetWithHashingStrategy.newSet(
                    UnifiedMapWithHashingStrategy.this.hashingStrategy, UnifiedMapWithHashingStrategy.this.size());
            for (int i = 0; i < UnifiedMapWithHashingStrategy.this.table.length; i += 2)
            {
                Object key = UnifiedMapWithHashingStrategy.this.table[i];
                if (key == CHAINED_KEY)
                {
                    this.chainedAddToSet((Object[]) UnifiedMapWithHashingStrategy.this.table[i + 1], replace);
                }
                else if (key != null)
                {
                    replace.add(UnifiedMapWithHashingStrategy.this.nonSentinel(key));
                }
            }
            return replace;
        }

        private void chainedAddToSet(Object[] chain, UnifiedSetWithHashingStrategy<K> replace)
        {
            for (int i = 0; i < chain.length; i += 2)
            {
                Object key = chain[i];
                if (key == null)
                {
                    return;
                }
                replace.add(UnifiedMapWithHashingStrategy.this.nonSentinel(key));
            }
        }
    }

    protected abstract class PositionalIterator<T> implements Iterator<T>
    {
        protected int count;
        protected int position;
        protected int chainPosition;
        protected K lastReturned;

        public boolean hasNext()
        {
            return this.count < UnifiedMapWithHashingStrategy.this.size();
        }

        public void remove()
        {
            if (this.lastReturned == null)
            {
                throw new IllegalStateException("next() must be called as many times as remove()");
            }
            this.count--;
            UnifiedMapWithHashingStrategy.this.occupied--;

            if (this.chainPosition != 0)
            {
                this.removeFromChain();
                return;
            }

            int pos = this.position - 2;
            Object key = UnifiedMapWithHashingStrategy.this.table[pos];
            if (key == CHAINED_KEY)
            {
                this.removeLastFromChain((Object[]) UnifiedMapWithHashingStrategy.this.table[pos + 1], pos);
                return;
            }
            UnifiedMapWithHashingStrategy.this.table[pos] = null;
            UnifiedMapWithHashingStrategy.this.table[pos + 1] = null;
            this.position = pos;
            this.lastReturned = null;
        }

        protected void removeFromChain()
        {
            Object[] chain = (Object[]) UnifiedMapWithHashingStrategy.this.table[this.position + 1];
            int pos = this.chainPosition - 2;
            int replacePos = this.chainPosition;
            while (replacePos < chain.length - 2 && chain[replacePos + 2] != null)
            {
                replacePos += 2;
            }
            chain[pos] = chain[replacePos];
            chain[pos + 1] = chain[replacePos + 1];
            chain[replacePos] = null;
            chain[replacePos + 1] = null;
            this.chainPosition = pos;
            this.lastReturned = null;
        }

        protected void removeLastFromChain(Object[] chain, int tableIndex)
        {
            int pos = chain.length - 2;
            while (chain[pos] == null)
            {
                pos -= 2;
            }
            if (pos == 0)
            {
                UnifiedMapWithHashingStrategy.this.table[tableIndex] = null;
                UnifiedMapWithHashingStrategy.this.table[tableIndex + 1] = null;
            }
            else
            {
                chain[pos] = null;
                chain[pos + 1] = null;
            }
            this.lastReturned = null;
        }
    }

    protected class KeySetIterator extends PositionalIterator<K>
    {
        protected K nextFromChain()
        {
            Object[] chain = (Object[]) UnifiedMapWithHashingStrategy.this.table[this.position + 1];
            Object key = chain[this.chainPosition];
            this.chainPosition += 2;
            if (this.chainPosition >= chain.length
                    || chain[this.chainPosition] == null)
            {
                this.chainPosition = 0;
                this.position += 2;
            }
            this.lastReturned = (K) key;
            return UnifiedMapWithHashingStrategy.this.nonSentinel(key);
        }

        public K next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException("next() called, but the iterator is exhausted");
            }
            this.count++;
            Object[] table = UnifiedMapWithHashingStrategy.this.table;
            if (this.chainPosition != 0)
            {
                return this.nextFromChain();
            }
            while (table[this.position] == null)
            {
                this.position += 2;
            }
            Object key = table[this.position];
            if (key == CHAINED_KEY)
            {
                return this.nextFromChain();
            }
            this.position += 2;
            this.lastReturned = (K) key;
            return UnifiedMapWithHashingStrategy.this.nonSentinel(key);
        }
    }

    private static boolean nullSafeEquals(Object value, Object other)
    {
        if (value == null)
        {
            if (other == null)
            {
                return true;
            }
        }
        else if (other == value || value.equals(other))
        {
            return true;
        }
        return false;
    }

    protected class EntrySet implements Set<Entry<K, V>>, Serializable, BatchIterable<Entry<K, V>>
    {
        private static final long serialVersionUID = 1L;
        private transient WeakReference<UnifiedMapWithHashingStrategy<K, V>> holder = new WeakReference<UnifiedMapWithHashingStrategy<K, V>>(UnifiedMapWithHashingStrategy.this);

        public boolean add(Entry<K, V> entry)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Entry<K, V>> collection)
        {
            throw new UnsupportedOperationException();
        }

        public void clear()
        {
            UnifiedMapWithHashingStrategy.this.clear();
        }

        public boolean containsEntry(Entry<?, ?> entry)
        {
            Object key = entry.getKey();
            Object value = entry.getValue();
            int index = UnifiedMapWithHashingStrategy.this.index((K) key);

            Object cur = UnifiedMapWithHashingStrategy.this.table[index];
            Object curValue = UnifiedMapWithHashingStrategy.this.table[index + 1];
            if (cur == CHAINED_KEY)
            {
                return this.chainContainsEntry((Object[]) curValue, (K) key, value);
            }
            return cur != null && UnifiedMapWithHashingStrategy.this.hashingStrategyEquals(UnifiedMapWithHashingStrategy.this.nonSentinel(cur), (K) key) && UnifiedMapWithHashingStrategy.nullSafeEquals(value, curValue);
        }

        private boolean chainContainsEntry(Object[] chain, K key, Object value)
        {
            for (int i = 0; i < chain.length; i += 2)
            {
                Object k = chain[i];
                if (k == null)
                {
                    return false;
                }
                if (UnifiedMapWithHashingStrategy.this.hashingStrategyEquals(UnifiedMapWithHashingStrategy.this.nonSentinel(k), key))
                {
                    return value == null ? chain[i + 1] == null : chain[i + 1] == value || value.equals(chain[i + 1]);
                }
            }
            return false;
        }

        public boolean contains(Object o)
        {
            return o instanceof Entry && this.containsEntry((Entry<?, ?>) o);
        }

        public boolean containsAll(Collection<?> collection)
        {
            for (Object obj : collection)
            {
                if (!this.contains(obj))
                {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty()
        {
            return UnifiedMapWithHashingStrategy.this.isEmpty();
        }

        public Iterator<Entry<K, V>> iterator()
        {
            return new EntrySetIterator(this.holder);
        }

        public boolean remove(Object e)
        {
            if (!(e instanceof Entry))
            {
                return false;
            }
            Entry<?, ?> entry = (Entry<?, ?>) e;
            Object key = entry.getKey();
            Object value = entry.getValue();

            int index = UnifiedMapWithHashingStrategy.this.index((K) key);

            Object cur = UnifiedMapWithHashingStrategy.this.table[index];
            if (cur != null)
            {
                Object val = UnifiedMapWithHashingStrategy.this.table[index + 1];
                if (cur == CHAINED_KEY)
                {
                    return this.removeFromChain((Object[]) val, (K) key, value, index);
                }
                if (UnifiedMapWithHashingStrategy.this.hashingStrategyEquals(UnifiedMapWithHashingStrategy.this.nonSentinel(cur), (K) key) && UnifiedMapWithHashingStrategy.nullSafeEquals(value, val))
                {
                    UnifiedMapWithHashingStrategy.this.table[index] = null;
                    UnifiedMapWithHashingStrategy.this.table[index + 1] = null;
                    UnifiedMapWithHashingStrategy.this.occupied--;
                    return true;
                }
            }
            return false;
        }

        private boolean removeFromChain(Object[] chain, K key, Object value, int index)
        {
            for (int i = 0; i < chain.length; i += 2)
            {
                Object k = chain[i];
                if (k == null)
                {
                    return false;
                }
                if (UnifiedMapWithHashingStrategy.this.hashingStrategyEquals(UnifiedMapWithHashingStrategy.this.nonSentinel(k), key))
                {
                    V val = (V) chain[i + 1];
                    if (UnifiedMapWithHashingStrategy.nullSafeEquals(val, value))
                    {
                        UnifiedMapWithHashingStrategy.this.overwriteWithLastElementFromChain(chain, index, i);
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean removeAll(Collection<?> collection)
        {
            boolean changed = false;
            for (Object obj : collection)
            {
                if (this.remove(obj))
                {
                    changed = true;
                }
            }
            return changed;
        }

        public boolean retainAll(Collection<?> collection)
        {
            int retainedSize = collection.size();
            UnifiedMapWithHashingStrategy<K, V> retainedCopy = new UnifiedMapWithHashingStrategy<K, V>(
                    UnifiedMapWithHashingStrategy.this.hashingStrategy, retainedSize, UnifiedMapWithHashingStrategy.this.loadFactor);

            for (Object obj : collection)
            {
                if (obj instanceof Entry)
                {
                    Entry<?, ?> entry = (Entry<?, ?>) obj;
                    if (this.containsEntry(entry))
                    {
                        retainedCopy.put(UnifiedMapWithHashingStrategy.this.nonSentinel(entry.getKey()), (V) entry.getValue());
                    }
                }
            }
            if (retainedCopy.size() < this.size())
            {
                UnifiedMapWithHashingStrategy.this.maxSize = retainedCopy.maxSize;
                UnifiedMapWithHashingStrategy.this.occupied = retainedCopy.occupied;
                UnifiedMapWithHashingStrategy.this.table = retainedCopy.table;
                return true;
            }
            return false;
        }

        public int size()
        {
            return UnifiedMapWithHashingStrategy.this.size();
        }

        public void forEach(Procedure<? super Entry<K, V>> procedure)
        {
            for (int i = 0; i < UnifiedMapWithHashingStrategy.this.table.length; i += 2)
            {
                Object key = UnifiedMapWithHashingStrategy.this.table[i];
                if (key == CHAINED_KEY)
                {
                    this.chainedForEachEntry((Object[]) UnifiedMapWithHashingStrategy.this.table[i + 1], procedure);
                }
                else if (key != null)
                {
                    procedure.value(ImmutableEntry.of(UnifiedMapWithHashingStrategy.this.nonSentinel(key), (V) UnifiedMapWithHashingStrategy.this.table[i + 1]));
                }
            }
        }

        private void chainedForEachEntry(Object[] chain, Procedure<? super Entry<K, V>> procedure)
        {
            for (int i = 0; i < chain.length; i += 2)
            {
                Object key = chain[i];
                if (key == null)
                {
                    return;
                }
                procedure.value(ImmutableEntry.of(UnifiedMapWithHashingStrategy.this.nonSentinel(key), (V) chain[i + 1]));
            }
        }

        public int getBatchCount(int batchSize)
        {
            return UnifiedMapWithHashingStrategy.this.getBatchCount(batchSize);
        }

        public void batchForEach(Procedure<? super Entry<K, V>> procedure, int sectionIndex, int sectionCount)
        {
            Object[] map = UnifiedMapWithHashingStrategy.this.table;
            int sectionSize = map.length / sectionCount;
            int start = sectionIndex * sectionSize;
            int end = sectionIndex == sectionCount - 1 ? map.length : start + sectionSize;
            if (start % 2 != 0)
            {
                start++;
            }
            for (int i = start; i < end; i += 2)
            {
                Object key = map[i];
                if (key == CHAINED_KEY)
                {
                    this.chainedForEachEntry((Object[]) map[i + 1], procedure);
                }
                else if (key != null)
                {
                    procedure.value(ImmutableEntry.of(UnifiedMapWithHashingStrategy.this.nonSentinel(key), (V) map[i + 1]));
                }
            }
        }

        protected void copyEntries(Object[] result)
        {
            Object[] table = UnifiedMapWithHashingStrategy.this.table;
            int count = 0;
            for (int i = 0; i < table.length; i += 2)
            {
                Object x = table[i];
                if (x != null)
                {
                    if (x == CHAINED_KEY)
                    {
                        Object[] chain = (Object[]) table[i + 1];
                        for (int j = 0; j < chain.length; j += 2)
                        {
                            Object key = chain[j];
                            if (key == null)
                            {
                                break;
                            }
                            result[count++] =
                                    new WeakBoundEntry<K, V>(UnifiedMapWithHashingStrategy.this.nonSentinel(key), (V) chain[j + 1], this.holder,
                                            UnifiedMapWithHashingStrategy.this.hashingStrategy);
                        }
                    }
                    else
                    {
                        result[count++] = new WeakBoundEntry<K, V>(UnifiedMapWithHashingStrategy.this.nonSentinel(x), (V) table[i + 1], this.holder,
                                UnifiedMapWithHashingStrategy.this.hashingStrategy);
                    }
                }
            }
        }

        public Object[] toArray()
        {
            Object[] result = new Object[UnifiedMapWithHashingStrategy.this.size()];
            this.copyEntries(result);
            return result;
        }

        public <T> T[] toArray(T[] result)
        {
            int size = UnifiedMapWithHashingStrategy.this.size();
            if (result.length < size)
            {
                result = (T[]) Array.newInstance(result.getClass().getComponentType(), size);
            }
            this.copyEntries(result);
            if (size < result.length)
            {
                result[size] = null;
            }
            return result;
        }

        private void readObject(ObjectInputStream in)
                throws IOException, ClassNotFoundException
        {
            in.defaultReadObject();
            this.holder = new WeakReference<UnifiedMapWithHashingStrategy<K, V>>(UnifiedMapWithHashingStrategy.this);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Set)
            {
                Set<?> other = (Set<?>) obj;
                if (other.size() == this.size())
                {
                    return this.containsAll(other);
                }
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return UnifiedMapWithHashingStrategy.this.hashCode();
        }
    }

    protected class EntrySetIterator extends PositionalIterator<Entry<K, V>>
    {
        private final WeakReference<UnifiedMapWithHashingStrategy<K, V>> holder;

        protected EntrySetIterator(WeakReference<UnifiedMapWithHashingStrategy<K, V>> holder)
        {
            this.holder = holder;
        }

        protected Entry<K, V> nextFromChain()
        {
            Object[] chain = (Object[]) UnifiedMapWithHashingStrategy.this.table[this.position + 1];
            Object key = chain[this.chainPosition];
            Object value = chain[this.chainPosition + 1];
            this.chainPosition += 2;
            if (this.chainPosition >= chain.length
                    || chain[this.chainPosition] == null)
            {
                this.chainPosition = 0;
                this.position += 2;
            }
            this.lastReturned = (K) key;
            return new WeakBoundEntry<K, V>(UnifiedMapWithHashingStrategy.this.nonSentinel(key), (V) value, this.holder,
                    UnifiedMapWithHashingStrategy.this.hashingStrategy);
        }

        public Entry<K, V> next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException("next() called, but the iterator is exhausted");
            }
            this.count++;
            Object[] table = UnifiedMapWithHashingStrategy.this.table;
            if (this.chainPosition != 0)
            {
                return this.nextFromChain();
            }
            while (table[this.position] == null)
            {
                this.position += 2;
            }
            Object key = table[this.position];
            Object value = table[this.position + 1];
            if (key == CHAINED_KEY)
            {
                return this.nextFromChain();
            }
            this.position += 2;
            this.lastReturned = (K) key;
            return new WeakBoundEntry<K, V>(UnifiedMapWithHashingStrategy.this.nonSentinel(key), (V) value, this.holder,
                    UnifiedMapWithHashingStrategy.this.hashingStrategy);
        }
    }

    protected static class WeakBoundEntry<K, V> implements Entry<K, V>
    {
        protected final K key;
        protected V value;
        protected final WeakReference<UnifiedMapWithHashingStrategy<K, V>> holder;
        protected final HashingStrategy<? super K> hashingStrategy;

        protected WeakBoundEntry(K key, V value, WeakReference<UnifiedMapWithHashingStrategy<K, V>> holder,
                HashingStrategy<? super K> hashingStrategy)
        {
            this.key = key;
            this.value = value;
            this.holder = holder;
            this.hashingStrategy = hashingStrategy;
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
            this.value = value;
            UnifiedMapWithHashingStrategy<K, V> map = this.holder.get();
            if (map != null && map.containsKey(this.key))
            {
                return map.put(this.key, value);
            }
            return null;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Entry)
            {
                Entry<?, ?> other = (Entry<?, ?>) obj;
                Object otherValue = other.getValue();
                Object otherValue1 = other.getKey();
                return this.hashingStrategy.equals(this.key, (K) otherValue1)
                        && UnifiedMapWithHashingStrategy.nullSafeEquals(this.value, otherValue);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return this.hashingStrategy.computeHashCode(this.key)
                    ^ (this.value == null ? 0 : this.value.hashCode());
        }

        @Override
        public String toString()
        {
            return this.key + "=" + this.value;
        }
    }

    protected class ValuesCollection extends ValuesCollectionCommon<V>
            implements Serializable, BatchIterable<V>
    {
        private static final long serialVersionUID = 1L;

        public void clear()
        {
            UnifiedMapWithHashingStrategy.this.clear();
        }

        public boolean contains(Object o)
        {
            return UnifiedMapWithHashingStrategy.this.containsValue(o);
        }

        public boolean containsAll(Collection<?> collection)
        {
            // todo: this is N^2. if c is large, we should copy the values to a set.
            return Iterate.allSatisfy((Collection<?>) collection, Predicates.in(this));
        }

        public boolean isEmpty()
        {
            return UnifiedMapWithHashingStrategy.this.isEmpty();
        }

        public Iterator<V> iterator()
        {
            return new ValuesIterator();
        }

        public boolean remove(Object o)
        {
            // this is so slow that the extra overhead of the iterator won't be noticable
            if (o == null)
            {
                for (Iterator<V> it = this.iterator(); it.hasNext(); )
                {
                    if (it.next() == null)
                    {
                        it.remove();
                        return true;
                    }
                }
            }
            else
            {
                for (Iterator<V> it = this.iterator(); it.hasNext(); )
                {
                    V o2 = it.next();
                    if (o == o2 || o2.equals(o))
                    {
                        it.remove();
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean removeAll(Collection<?> collection)
        {
            // todo: this is N^2. if c is large, we should copy the values to a set.
            boolean changed = false;

            for (Object obj : collection)
            {
                if (this.remove(obj))
                {
                    changed = true;
                }
            }
            return changed;
        }

        public boolean retainAll(Collection<?> collection)
        {
            boolean modified = false;
            Iterator<V> e = this.iterator();
            while (e.hasNext())
            {
                if (!collection.contains(e.next()))
                {
                    e.remove();
                    modified = true;
                }
            }
            return modified;
        }

        public int size()
        {
            return UnifiedMapWithHashingStrategy.this.size();
        }

        public void forEach(Procedure<? super V> procedure)
        {
            UnifiedMapWithHashingStrategy.this.forEachValue(procedure);
        }

        public int getBatchCount(int batchSize)
        {
            return UnifiedMapWithHashingStrategy.this.getBatchCount(batchSize);
        }

        public void batchForEach(Procedure<? super V> procedure, int sectionIndex, int sectionCount)
        {
            UnifiedMapWithHashingStrategy.this.batchForEach(procedure, sectionIndex, sectionCount);
        }

        protected void copyValues(Object[] result)
        {
            this.copyValuesFromTable(UnifiedMapWithHashingStrategy.this.table, result, CHAINED_KEY);
        }

        public Object[] toArray()
        {
            int size = UnifiedMapWithHashingStrategy.this.size();
            Object[] result = new Object[size];
            this.copyValues(result);
            return result;
        }

        public <T> T[] toArray(T[] result)
        {
            int size = UnifiedMapWithHashingStrategy.this.size();
            if (result.length < size)
            {
                result = (T[]) Array.newInstance(result.getClass().getComponentType(), size);
            }
            this.copyValues(result);
            if (size < result.length)
            {
                result[size] = null;
            }
            return result;
        }

        protected Object writeReplace()
        {
            FastList<V> replace = FastList.newList(UnifiedMapWithHashingStrategy.this.size());
            for (int i = 0; i < UnifiedMapWithHashingStrategy.this.table.length; i += 2)
            {
                Object key = UnifiedMapWithHashingStrategy.this.table[i];
                if (key == CHAINED_KEY)
                {
                    this.chainedAddToList((Object[]) UnifiedMapWithHashingStrategy.this.table[i + 1], replace);
                }
                else if (key != null)
                {
                    replace.add((V) UnifiedMapWithHashingStrategy.this.table[i + 1]);
                }
            }
            return replace;
        }

        private void chainedAddToList(Object[] chain, FastList<V> replace)
        {
            for (int i = 0; i < chain.length; i += 2)
            {
                Object key = chain[i];
                if (key == null)
                {
                    return;
                }
                replace.add((V) chain[i + 1]);
            }
        }
    }

    protected class ValuesIterator extends PositionalIterator<V>
    {
        protected V nextFromChain()
        {
            Object[] chain = (Object[]) UnifiedMapWithHashingStrategy.this.table[this.position + 1];
            Object key = chain[this.chainPosition];
            Object val = chain[this.chainPosition + 1];
            this.chainPosition += 2;
            if (this.chainPosition >= chain.length
                    || chain[this.chainPosition] == null)
            {
                this.chainPosition = 0;
                this.position += 2;
            }
            this.lastReturned = (K) key;
            return (V) val;
        }

        public V next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException("next() called, but the iterator is exhausted");
            }
            this.count++;
            Object[] table = UnifiedMapWithHashingStrategy.this.table;
            if (this.chainPosition != 0)
            {
                return this.nextFromChain();
            }
            while (table[this.position] == null)
            {
                this.position += 2;
            }
            Object key = table[this.position];
            Object val = table[this.position + 1];
            if (key == CHAINED_KEY)
            {
                return this.nextFromChain();
            }
            this.position += 2;
            this.lastReturned = (K) key;
            return (V) val;
        }
    }

    private boolean hashingStrategyEquals(K key1, K key2)
    {
        return key1 == key2 || this.hashingStrategy.equals(key1, key2);
    }

    private K nonSentinel(Object key)
    {
        return key == NULL_KEY ? null : (K) key;
    }

    private Object toSentinelIfNull(Object key)
    {
        if (key == null)
        {
            return NULL_KEY;
        }
        return key;
    }

    @Override
    public ImmutableMap<K, V> toImmutable()
    {
        return HashingStrategyMaps.immutable.ofMap(this);
    }
}
