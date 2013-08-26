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

package com.gs.collections.impl.map.mutable.primitive;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.BooleanFunction0;
import com.gs.collections.api.block.function.primitive.BooleanToBooleanFunction;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.predicate.primitive.ObjectBooleanPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectBooleanProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.map.primitive.ImmutableObjectBooleanMap;
import com.gs.collections.api.map.primitive.MutableObjectBooleanMap;
import com.gs.collections.api.map.primitive.ObjectBooleanMap;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.collection.mutable.primitive.SynchronizedBooleanCollection;
import com.gs.collections.impl.collection.mutable.primitive.UnmodifiableBooleanCollection;
import com.gs.collections.impl.factory.primitive.BooleanLists;
import com.gs.collections.impl.factory.primitive.ObjectBooleanMaps;
import com.gs.collections.impl.lazy.primitive.LazyBooleanIterableAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;

/**
 * @since 3.0.
 */
public class ObjectBooleanHashMap<K> implements MutableObjectBooleanMap<K>, Externalizable
{
    public static final boolean EMPTY_VALUE = false;
    private static final Object NULL_KEY = new Object()
    {
        @Override
        public boolean equals(Object obj)
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public int hashCode()
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public String toString()
        {
            return "ObjectBooleanHashMap.NULL_KEY";
        }
    };
    private static final Object REMOVED_KEY = new Object()
    {
        @Override
        public boolean equals(Object obj)
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public int hashCode()
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public String toString()
        {
            return "ObjectBooleanHashMap.REMOVED_KEY";
        }
    };

    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private static final long serialVersionUID = 1L;
    private int occupied;
    private int maxSize;

    private Object[] keys;
    private BitSet values;
    private float loadFactor = DEFAULT_LOAD_FACTOR;

    public ObjectBooleanHashMap()
    {
        this.allocate(DEFAULT_INITIAL_CAPACITY << 1);
    }

    public ObjectBooleanHashMap(int initialCapacity)
    {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public ObjectBooleanHashMap(ObjectBooleanMap<? extends K> map)
    {
        this(Math.max(map.size(), DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);

        this.putAll(map);
    }

    public ObjectBooleanHashMap(int initialCapacity, float loadFactor)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("initial capacity cannot be less than 0");
        }
        this.loadFactor = loadFactor;
        this.init(this.fastCeil(initialCapacity / loadFactor));
    }

    public static <K> ObjectBooleanHashMap<K> newMap()
    {
        return new ObjectBooleanHashMap<K>();
    }

    public MutableObjectBooleanMap<K> asUnmodifiable()
    {
        return new UnmodifiableObjectBooleanMap<K>(this);
    }

    public MutableObjectBooleanMap<K> asSynchronized()
    {
        return new SynchronizedObjectBooleanMap<K>(this);
    }

    public ImmutableObjectBooleanMap<K> toImmutable()
    {
        return ObjectBooleanMaps.immutable.withAll(this);
    }

    public static <K> ObjectBooleanHashMap<K> newWithKeysValues(K key1, boolean value1)
    {
        return new ObjectBooleanHashMap<K>(1).withKeyValue(key1, value1);
    }

    public static <K> ObjectBooleanHashMap<K> newWithKeysValues(K key1, boolean value1, K key2, boolean value2)
    {
        return new ObjectBooleanHashMap<K>(2).withKeysValues(key1, value1, key2, value2);
    }

    public static <K> ObjectBooleanHashMap<K> newWithKeysValues(K key1, boolean value1, K key2, boolean value2, K key3, boolean value3)
    {
        return new ObjectBooleanHashMap<K>(3).withKeysValues(key1, value1, key2, value2, key3, value3);
    }

    public static <K> ObjectBooleanHashMap<K> newWithKeysValues(K key1, boolean value1, K key2, boolean value2, K key3, boolean value3, K key4, boolean value4)
    {
        return new ObjectBooleanHashMap<K>(4).withKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public ObjectBooleanHashMap<K> withKeyValue(K key1, boolean value1)
    {
        this.put(key1, value1);
        return this;
    }

    public ObjectBooleanHashMap<K> withKeysValues(K key1, boolean value1, K key2, boolean value2)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        return this;
    }

    public ObjectBooleanHashMap<K> withKeysValues(K key1, boolean value1, K key2, boolean value2, K key3, boolean value3)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        this.put(key3, value3);
        return this;
    }

    public ObjectBooleanHashMap<K> withKeysValues(K key1, boolean value1, K key2, boolean value2, K key3, boolean value3, K key4, boolean value4)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        this.put(key3, value3);
        this.put(key4, value4);
        return this;
    }

    public ObjectBooleanHashMap<K> withoutKey(K key)
    {
        this.removeKey(key);
        return this;
    }

    public ObjectBooleanHashMap<K> withoutAllKeys(Iterable<? extends K> keys)
    {
        for (K key : keys)
        {
            this.removeKey(key);
        }
        return this;
    }

    private int init(int initialCapacity)
    {
        int capacity = 1;
        while (capacity < initialCapacity)
        {
            capacity <<= 1;
        }
        return this.allocate(capacity);
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

    private static boolean isRemovedKey(Object key)
    {
        return key == REMOVED_KEY;
    }

    private int allocate(int capacity)
    {
        this.allocateTable(capacity);
        this.computeMaxSize(capacity);
        return capacity;
    }

    private void allocateTable(int sizeToAllocate)
    {
        this.keys = new Object[sizeToAllocate];
        this.values = new BitSet(sizeToAllocate);
    }

    private void computeMaxSize(int capacity)
    {
        // need at least one free slot for open addressing
        this.maxSize = Math.min(capacity - 1, (int) (capacity * this.loadFactor));
    }

    private static <K> boolean isNonSentinel(K key)
    {
        return key != null && !isRemovedKey(key);
    }

    public int size()
    {
        return this.occupied;
    }

    public boolean isEmpty()
    {
        return this.occupied == 0;
    }

    public boolean notEmpty()
    {
        return this.occupied != 0;
    }

    private void rehash()
    {
        this.rehash(this.keys.length << 1);
    }

    private void rehash(int newCapacity)
    {
        int oldLength = this.keys.length;
        Object[] old = this.keys;
        BitSet oldValues = this.values;
        this.allocate(newCapacity);
        this.occupied = 0;

        for (int i = 0; i < oldLength; i++)
        {
            if (isNonSentinel(old[i]))
            {
                this.put(this.toNonSentinel(old[i]), oldValues.get(i));
            }
        }
    }

    private K toNonSentinel(Object key)
    {
        return key == NULL_KEY ? null : (K) key;
    }

    // exposed for testing
    int probe(Object element)
    {
        int index = this.index(element);

        int removedIndex = -1;
        if (isRemovedKey(this.keys[index]))
        {
            removedIndex = index;
        }

        else if (this.keys[index] == null || nullSafeEquals(this.toNonSentinel(this.keys[index]), element))
        {
            return index;
        }

        int nextIndex = index;
        int probe = 17;

        // loop until an empty slot is reached
        while (true)
        {
            // Probe algorithm: 17*n*(n+1)/2 where n = no. of collisions
            nextIndex += probe;
            probe += 17;
            nextIndex &= this.keys.length - 1;

            if (isRemovedKey(this.keys[nextIndex]))
            {
                if (removedIndex == -1)
                {
                    removedIndex = nextIndex;
                }
            }
            else if (nullSafeEquals(this.toNonSentinel(this.keys[nextIndex]), element))
            {
                return nextIndex;
            }
            else if (this.keys[nextIndex] == null)
            {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
        }
    }

    // exposed for testing
    int index(Object element)
    {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        int h = element == null ? 0 : element.hashCode();
        h ^= h >>> 20 ^ h >>> 12;
        h ^= h >>> 7 ^ h >>> 4;
        return (h & (this.keys.length >> 1) - 1) << 1;
    }

    public void clear()
    {
        this.occupied = 0;
        Arrays.fill(this.keys, null);
        this.values.clear();
    }

    public void put(K key, boolean value)
    {
        int index = this.probe(key);

        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            // key already present in map
            this.values.set(index, value);
            return;
        }

        this.keys[index] = toSentinelIfNull(key);
        this.values.set(index, value);
        ++this.occupied;
        if (this.occupied > this.maxSize)
        {
            this.rehash();
        }
    }

    public void putAll(ObjectBooleanMap<? extends K> map)
    {
        map.forEachKeyValue(new ObjectBooleanProcedure<K>()
        {
            public void value(K each, boolean parameter)
            {
                ObjectBooleanHashMap.this.put(each, parameter);
            }
        });
    }

    private static Object toSentinelIfNull(Object key)
    {
        return key == null ? NULL_KEY : key;
    }

    public boolean get(Object key)
    {
        return this.getIfAbsent(key, EMPTY_VALUE);
    }

    public boolean getIfAbsent(Object key, boolean ifAbsent)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            return this.values.get(index);
        }
        return ifAbsent;
    }

    public boolean getOrThrow(Object key)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]))
        {
            return this.values.get(index);
        }
        throw new IllegalStateException("Key " + key + " not present.");
    }

    public boolean getIfAbsentPut(K key, boolean value)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            return this.values.get(index);
        }
        this.keys[index] = toSentinelIfNull(key);
        this.occupied++;
        this.values.set(index, value);
        return value;
    }

    public boolean getIfAbsentPut(K key, BooleanFunction0 function)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            return this.values.get(index);
        }
        this.keys[index] = toSentinelIfNull(key);
        this.occupied++;
        boolean value = function.value();
        this.values.set(index, value);
        return value;
    }

    public <P> boolean getIfAbsentPutWith(K key, BooleanFunction<? super P> function, P parameter)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            return this.values.get(index);
        }
        this.keys[index] = toSentinelIfNull(key);
        this.occupied++;
        boolean value = function.booleanValueOf(parameter);
        this.values.set(index, value);
        return value;
    }

    public boolean getIfAbsentPutWithKey(K key, BooleanFunction<? super K> function)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            return this.values.get(index);
        }
        this.keys[index] = toSentinelIfNull(key);
        this.occupied++;
        boolean value = function.booleanValueOf(key);
        this.values.set(index, value);
        return value;
    }

    public boolean updateValue(K key, boolean initialValueIfAbsent, BooleanToBooleanFunction function)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            this.values.set(index, function.valueOf(this.values.get(index)));
        }
        else
        {
            this.keys[index] = toSentinelIfNull(key);
            this.occupied++;
            this.values.set(index, function.valueOf(initialValueIfAbsent));
        }
        return this.values.get(index);
    }

    public boolean containsKey(Object key)
    {
        int index = this.probe(key);
        return isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key);
    }

    public boolean containsValue(boolean value)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && this.values.get(i) == value)
            {
                return true;
            }
        }
        return false;
    }

    public void removeKey(K key)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            this.keys[index] = REMOVED_KEY;
            this.occupied--;
            this.values.set(index, EMPTY_VALUE);
        }
    }

    public void remove(K key)
    {
        this.removeKey(key);
    }

    public boolean removeKeyIfAbsent(K key, boolean value)
    {
        int index = this.probe(key);
        if (isNonSentinel(this.keys[index]) && nullSafeEquals(this.toNonSentinel(this.keys[index]), key))
        {
            this.keys[index] = REMOVED_KEY;
            this.occupied--;
            boolean oldValue = this.values.get(index);
            this.values.set(index, EMPTY_VALUE);
            return oldValue;
        }
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof ObjectBooleanMap))
        {
            return false;
        }

        ObjectBooleanMap<K> other = (ObjectBooleanMap<K>) obj;

        if (this.size() != other.size())
        {
            return false;
        }

        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && (!other.containsKey(this.toNonSentinel(this.keys[i])) || this.values.get(i) != other.getOrThrow(this.toNonSentinel(this.keys[i]))))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int result = 0;

        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                result += (this.toNonSentinel(this.keys[i]) == null ? 0 : this.keys[i].hashCode()) ^ (this.values.get(i) ? 1231 : 1237);
            }
        }
        return result;
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public ObjectBooleanHashMap<K> select(ObjectBooleanPredicate<? super K> predicate)
    {
        ObjectBooleanHashMap<K> result = ObjectBooleanHashMap.newMap();
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && predicate.accept(this.toNonSentinel(this.keys[i]), this.values.get(i)))
            {
                result.put(this.toNonSentinel(this.keys[i]), this.values.get(i));
            }
        }
        return result;
    }

    public ObjectBooleanHashMap<K> reject(ObjectBooleanPredicate<? super K> predicate)
    {
        ObjectBooleanHashMap<K> result = ObjectBooleanHashMap.newMap();
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && !predicate.accept(this.toNonSentinel(this.keys[i]), this.values.get(i)))
            {
                result.put(this.toNonSentinel(this.keys[i]), this.values.get(i));
            }
        }
        return result;
    }

    public <V> V injectInto(V injectedValue, ObjectBooleanToObjectFunction<? super V, ? extends V> function)
    {
        V result = injectedValue;

        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                result = function.valueOf(result, this.values.get(i));
            }
        }

        return result;
    }

    public BooleanIterator booleanIterator()
    {
        return new InternalBooleanIterator();
    }

    public void forEach(BooleanProcedure procedure)
    {
        this.forEachValue(procedure);
    }

    public int count(BooleanPredicate predicate)
    {
        int count = 0;
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && predicate.accept(this.values.get(i)))
            {
                count++;
            }
        }
        return count;
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && predicate.accept(this.values.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && !predicate.accept(this.values.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    public boolean noneSatisfy(BooleanPredicate predicate)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && predicate.accept(this.values.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    public MutableBooleanCollection select(BooleanPredicate predicate)
    {
        BooleanArrayList result = new BooleanArrayList();
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && predicate.accept(this.values.get(i)))
            {
                result.add(this.values.get(i));
            }
        }
        return result;
    }

    public MutableBooleanCollection reject(BooleanPredicate predicate)
    {
        BooleanArrayList result = new BooleanArrayList();
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && !predicate.accept(this.values.get(i)))
            {
                result.add(this.values.get(i));
            }
        }
        return result;
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]) && predicate.accept(this.values.get(i)))
            {
                return this.values.get(i);
            }
        }
        return ifNone;
    }

    public <V> MutableCollection<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        MutableList<V> result = FastList.newList(this.size());
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                result.add(function.valueOf(this.values.get(i)));
            }
        }
        return result;
    }

    public boolean[] toArray()
    {
        boolean[] result = new boolean[this.size()];
        int index = 0;
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                result[index] = this.values.get(i);
                index++;
            }
        }
        return result;
    }

    public boolean contains(boolean value)
    {
        return this.containsValue(value);
    }

    public boolean containsAll(boolean... source)
    {
        for (boolean item : source)
        {
            if (!this.containsValue(item))
            {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(BooleanIterable source)
    {
        return this.containsAll(source.toArray());
    }

    public MutableBooleanList toList()
    {
        MutableBooleanList result = new BooleanArrayList(this.size());
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                result.add(this.values.get(i));
            }
        }
        return result;
    }

    public MutableBooleanSet toSet()
    {
        MutableBooleanSet result = new BooleanHashSet();
        for (int i = 0; result.size() < 2 && i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                result.add(this.values.get(i));
            }
        }
        return result;
    }

    public MutableBooleanBag toBag()
    {
        MutableBooleanBag result = new BooleanHashBag();
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                result.add(this.values.get(i));
            }
        }
        return result;
    }

    public LazyBooleanIterable asLazy()
    {
        return new LazyBooleanIterableAdapter(this);
    }

    public void forEachValue(BooleanProcedure procedure)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                procedure.value(this.values.get(i));
            }
        }
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                procedure.value(this.toNonSentinel(this.keys[i]));
            }
        }
    }

    public void forEachKeyValue(ObjectBooleanProcedure<? super K> procedure)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                procedure.value(this.toNonSentinel(this.keys[i]), this.values.get(i));
            }
        }
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);

            boolean first = true;

            for (int i = 0; i < this.keys.length; i++)
            {
                Object key = this.keys[i];
                if (isNonSentinel(key))
                {
                    if (!first)
                    {
                        appendable.append(separator);
                    }
                    appendable.append(String.valueOf(this.toNonSentinel(key))).append("=").append(String.valueOf(this.values.get(i)));
                    first = false;
                }
            }
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
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

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.size());
        out.writeFloat(this.loadFactor);
        for (int i = 0; i < this.keys.length; i++)
        {
            if (isNonSentinel(this.keys[i]))
            {
                out.writeObject(this.toNonSentinel(this.keys[i]));
                out.writeBoolean(this.values.get(i));
            }
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int size = in.readInt();
        this.loadFactor = in.readFloat();
        this.init(Math.max((int) (size / this.loadFactor) + 1,
                DEFAULT_INITIAL_CAPACITY));
        for (int i = 0; i < size; i++)
        {
            this.put((K) in.readObject(), in.readBoolean());
        }
    }

    public Set<K> keySet()
    {
        return new KeySet();
    }

    public MutableBooleanCollection values()
    {
        return new ValuesCollection();
    }

    private class KeySet implements Set<K>
    {
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
            ObjectBooleanHashMap.this.clear();
        }

        public boolean contains(Object o)
        {
            return ObjectBooleanHashMap.this.containsKey(o);
        }

        public boolean containsAll(Collection<?> collection)
        {
            for (Object aCollection : collection)
            {
                if (!ObjectBooleanHashMap.this.containsKey(aCollection))
                {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty()
        {
            return ObjectBooleanHashMap.this.isEmpty();
        }

        public Iterator<K> iterator()
        {
            return new KeySetIterator();
        }

        public boolean remove(Object key)
        {
            int oldSize = ObjectBooleanHashMap.this.size();
            ObjectBooleanHashMap.this.removeKey((K) key);
            return oldSize != ObjectBooleanHashMap.this.size();
        }

        public boolean removeAll(Collection<?> collection)
        {
            int oldSize = ObjectBooleanHashMap.this.size();
            for (Object object : collection)
            {
                ObjectBooleanHashMap.this.removeKey((K) object);
            }
            return oldSize != ObjectBooleanHashMap.this.size();
        }

        public boolean retainAll(Collection<?> collection)
        {
            int oldSize = ObjectBooleanHashMap.this.size();
            Iterator<K> iterator = this.iterator();
            while (iterator.hasNext())
            {
                K next = iterator.next();
                if (!collection.contains(next))
                {
                    this.remove(next);
                }
            }
            return oldSize != ObjectBooleanHashMap.this.size();
        }

        public int size()
        {
            return ObjectBooleanHashMap.this.size();
        }

        private void copyKeys(Object[] result)
        {
            int count = 0;
            for (int i = 0; i < ObjectBooleanHashMap.this.keys.length; i++)
            {
                Object key = ObjectBooleanHashMap.this.keys[i];
                if (ObjectBooleanHashMap.isNonSentinel(key))
                {
                    result[count++] = ObjectBooleanHashMap.this.keys[i];
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
            Object[] table = ObjectBooleanHashMap.this.keys;
            for (int i = 0; i < table.length; i++)
            {
                Object key = table[i];
                if (ObjectBooleanHashMap.isNonSentinel(key))
                {
                    K nonSentinelKey = ObjectBooleanHashMap.this.toNonSentinel(key);
                    hashCode += nonSentinelKey == null ? 0 : nonSentinelKey.hashCode();
                }
            }
            return hashCode;
        }

        public Object[] toArray()
        {
            int size = ObjectBooleanHashMap.this.size();
            Object[] result = new Object[size];
            this.copyKeys(result);
            return result;
        }

        public <T> T[] toArray(T[] result)
        {
            int size = ObjectBooleanHashMap.this.size();
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
    }

    private class KeySetIterator implements Iterator<K>
    {
        private int count;
        private int position;
        private K currentKey;
        private boolean isCurrentKeySet;

        public boolean hasNext()
        {
            return this.count < ObjectBooleanHashMap.this.size();
        }

        public void remove()
        {
            if (!this.isCurrentKeySet)
            {
                throw new IllegalStateException();
            }

            this.isCurrentKeySet = false;
            this.count--;

            if (isNonSentinel(this.currentKey))
            {
                int index = this.position - 1;
                ObjectBooleanHashMap.this.keys[index] = REMOVED_KEY;
                ObjectBooleanHashMap.this.occupied--;
                ObjectBooleanHashMap.this.values.set(index, EMPTY_VALUE);
            }
            else
            {
                ObjectBooleanHashMap.this.removeKey(this.currentKey);
            }
        }

        public K next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            this.count++;
            Object[] keys = ObjectBooleanHashMap.this.keys;
            while (!isNonSentinel(keys[this.position]))
            {
                this.position++;
            }
            this.currentKey = (K) ObjectBooleanHashMap.this.keys[this.position];
            this.isCurrentKeySet = true;
            this.position++;
            return ObjectBooleanHashMap.this.toNonSentinel(this.currentKey);
        }
    }

    private class ValuesCollection implements MutableBooleanCollection
    {
        public void clear()
        {
            ObjectBooleanHashMap.this.clear();
        }

        public MutableBooleanCollection select(BooleanPredicate predicate)
        {
            return ObjectBooleanHashMap.this.select(predicate);
        }

        public MutableBooleanCollection reject(BooleanPredicate predicate)
        {
            return ObjectBooleanHashMap.this.reject(predicate);
        }

        public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
        {
            return ObjectBooleanHashMap.this.detectIfNone(predicate, ifNone);
        }

        public <V> MutableCollection<V> collect(BooleanToObjectFunction<? extends V> function)
        {
            return ObjectBooleanHashMap.this.collect(function);
        }

        public MutableBooleanCollection with(boolean element)
        {
            throw new UnsupportedOperationException();
        }

        public MutableBooleanCollection without(boolean element)
        {
            throw new UnsupportedOperationException();
        }

        public MutableBooleanCollection withAll(BooleanIterable elements)
        {
            throw new UnsupportedOperationException();
        }

        public MutableBooleanCollection withoutAll(BooleanIterable elements)
        {
            throw new UnsupportedOperationException();
        }

        public MutableBooleanCollection asUnmodifiable()
        {
            return UnmodifiableBooleanCollection.of(this);
        }

        public MutableBooleanCollection asSynchronized()
        {
            return SynchronizedBooleanCollection.of(this);
        }

        public ImmutableBooleanCollection toImmutable()
        {
            return BooleanLists.immutable.withAll(this);
        }

        public boolean contains(boolean value)
        {
            return ObjectBooleanHashMap.this.containsValue(value);
        }

        public boolean containsAll(boolean... source)
        {
            return ObjectBooleanHashMap.this.containsAll(source);
        }

        public boolean containsAll(BooleanIterable source)
        {
            return ObjectBooleanHashMap.this.containsAll(source);
        }

        public MutableBooleanList toList()
        {
            return ObjectBooleanHashMap.this.toList();
        }

        public MutableBooleanSet toSet()
        {
            return ObjectBooleanHashMap.this.toSet();
        }

        public MutableBooleanBag toBag()
        {
            return ObjectBooleanHashMap.this.toBag();
        }

        public LazyBooleanIterable asLazy()
        {
            return new LazyBooleanIterableAdapter(this);
        }

        public boolean isEmpty()
        {
            return ObjectBooleanHashMap.this.isEmpty();
        }

        public boolean notEmpty()
        {
            return ObjectBooleanHashMap.this.notEmpty();
        }

        public String makeString()
        {
            return this.makeString(", ");
        }

        public String makeString(String separator)
        {
            return this.makeString("", separator, "");
        }

        public String makeString(String start, String separator, String end)
        {
            Appendable stringBuilder = new StringBuilder();
            this.appendString(stringBuilder, start, separator, end);
            return stringBuilder.toString();
        }

        public void appendString(Appendable appendable)
        {
            this.appendString(appendable, ", ");
        }

        public void appendString(Appendable appendable, String separator)
        {
            this.appendString(appendable, "", separator, "");
        }

        public void appendString(Appendable appendable, String start, String separator, String end)
        {
            try
            {
                appendable.append(start);

                boolean first = true;

                for (int i = 0; i < ObjectBooleanHashMap.this.keys.length; i++)
                {
                    Object key = ObjectBooleanHashMap.this.keys[i];
                    if (isNonSentinel(key))
                    {
                        if (!first)
                        {
                            appendable.append(separator);
                        }
                        appendable.append(String.valueOf(ObjectBooleanHashMap.this.values.get(i)));
                        first = false;
                    }
                }
                appendable.append(end);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        public BooleanIterator booleanIterator()
        {
            return ObjectBooleanHashMap.this.booleanIterator();
        }

        public void forEach(BooleanProcedure procedure)
        {
            ObjectBooleanHashMap.this.forEach(procedure);
        }

        public int count(BooleanPredicate predicate)
        {
            return ObjectBooleanHashMap.this.count(predicate);
        }

        public boolean anySatisfy(BooleanPredicate predicate)
        {
            return ObjectBooleanHashMap.this.anySatisfy(predicate);
        }

        public boolean allSatisfy(BooleanPredicate predicate)
        {
            return ObjectBooleanHashMap.this.allSatisfy(predicate);
        }

        public boolean noneSatisfy(BooleanPredicate predicate)
        {
            return ObjectBooleanHashMap.this.noneSatisfy(predicate);
        }

        public boolean add(boolean element)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(boolean... source)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(BooleanIterable source)
        {
            throw new UnsupportedOperationException();
        }

        public boolean remove(boolean element)
        {
            for (int i = 0; i < ObjectBooleanHashMap.this.values.size(); i++)
            {
                if (ObjectBooleanHashMap.this.values.get(i) == element && ObjectBooleanHashMap.isNonSentinel(ObjectBooleanHashMap.this.keys[i]))
                {
                    ObjectBooleanHashMap.this.removeKey(ObjectBooleanHashMap.this.toNonSentinel((K) ObjectBooleanHashMap.this.keys[i]));
                    return true;
                }
            }
            return false;
        }

        public boolean removeAll(BooleanIterable source)
        {
            int oldSize = ObjectBooleanHashMap.this.size();

            BooleanIterator iterator = source.booleanIterator();
            while (iterator.hasNext())
            {
                this.remove(iterator.next());
            }
            return oldSize != ObjectBooleanHashMap.this.size();
        }

        public boolean removeAll(boolean... source)
        {
            int oldSize = ObjectBooleanHashMap.this.size();

            for (boolean item : source)
            {
                this.remove(item);
            }
            return oldSize != ObjectBooleanHashMap.this.size();
        }

        public int size()
        {
            return ObjectBooleanHashMap.this.size();
        }

        public boolean[] toArray()
        {
            return ObjectBooleanHashMap.this.toArray();
        }
    }

    private class InternalBooleanIterator implements BooleanIterator
    {
        private int count;
        private int position;

        public boolean next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }

            Object[] keys = ObjectBooleanHashMap.this.keys;
            while (!isNonSentinel(keys[this.position]))
            {
                this.position++;
            }
            boolean result = ObjectBooleanHashMap.this.values.get(this.position);
            this.count++;
            this.position++;
            return result;
        }

        public boolean hasNext()
        {
            return this.count != ObjectBooleanHashMap.this.size();
        }
    }
}
