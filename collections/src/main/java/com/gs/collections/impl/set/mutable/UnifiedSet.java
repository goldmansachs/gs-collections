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

package com.gs.collections.impl.set.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.Pool;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.api.set.primitive.MutableByteSet;
import com.gs.collections.api.set.primitive.MutableCharSet;
import com.gs.collections.api.set.primitive.MutableDoubleSet;
import com.gs.collections.api.set.primitive.MutableFloatSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.api.set.primitive.MutableLongSet;
import com.gs.collections.api.set.primitive.MutableShortSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.CollectIfProcedure;
import com.gs.collections.impl.block.procedure.CollectProcedure;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.block.procedure.FlatCollectProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.block.procedure.RejectProcedure;
import com.gs.collections.impl.block.procedure.SelectInstancesOfProcedure;
import com.gs.collections.impl.block.procedure.SelectProcedure;
import com.gs.collections.impl.block.procedure.ZipWithIndexProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectBooleanProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectByteProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectCharProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectDoubleProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectFloatProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectIntProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectLongProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectShortProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.partition.set.PartitionUnifiedSet;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.set.mutable.primitive.ByteHashSet;
import com.gs.collections.impl.set.mutable.primitive.CharHashSet;
import com.gs.collections.impl.set.mutable.primitive.DoubleHashSet;
import com.gs.collections.impl.set.mutable.primitive.FloatHashSet;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import com.gs.collections.impl.set.mutable.primitive.LongHashSet;
import com.gs.collections.impl.set.mutable.primitive.ShortHashSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;
import com.gs.collections.impl.utility.internal.MutableCollectionIterate;
import com.gs.collections.impl.utility.internal.SetIterables;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class UnifiedSet<K>
        implements MutableSet<K>, Externalizable, Pool<K>, BatchIterable<K>
{
    protected static final Object NULL_KEY = new Object()
    {
        @Override
        public boolean equals(Object obj)
        {
            throw new AssertionError();
        }

        @Override
        public int hashCode()
        {
            throw new AssertionError();
        }

        @Override
        public String toString()
        {
            return "UnifiedSet.NULL_KEY";
        }
    };

    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected static final int DEFAULT_INITIAL_CAPACITY = 8;

    private static final long serialVersionUID = 1L;

    protected transient Object[] table;

    protected transient int occupied;

    protected float loadFactor = DEFAULT_LOAD_FACTOR;

    protected int maxSize;

    public UnifiedSet()
    {
        this.allocate(DEFAULT_INITIAL_CAPACITY << 1);
    }

    public UnifiedSet(int initialCapacity)
    {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public UnifiedSet(int initialCapacity, float loadFactor)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("initial capacity cannot be less than 0");
        }
        this.loadFactor = loadFactor;
        this.init(this.fastCeil(initialCapacity / loadFactor));
    }

    public UnifiedSet(Collection<? extends K> collection)
    {
        this(Math.max(collection.size(), DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
        this.addAll(collection);
    }

    public UnifiedSet(UnifiedSet<K> set)
    {
        this.maxSize = set.maxSize;
        this.loadFactor = set.loadFactor;
        this.occupied = set.occupied;
        this.allocateTable(set.table.length);

        for (int i = 0; i < set.table.length; i++)
        {
            Object key = set.table[i];
            if (key instanceof ChainedBucket)
            {
                this.table[i] = ((ChainedBucket) key).copy();
            }
            else if (key != null)
            {
                this.table[i] = key;
            }
        }
    }

    public static <K> UnifiedSet<K> newSet()
    {
        return new UnifiedSet<K>();
    }

    public static <K> UnifiedSet<K> newSet(int size)
    {
        return new UnifiedSet<K>(size);
    }

    public static <K> UnifiedSet<K> newSet(Iterable<? extends K> source)
    {
        if (source instanceof UnifiedSet)
        {
            return new UnifiedSet<K>((UnifiedSet<K>) source);
        }
        if (source instanceof Collection)
        {
            return new UnifiedSet<K>((Collection<K>) source);
        }
        if (source == null)
        {
            throw new NullPointerException();
        }
        UnifiedSet<K> result = source instanceof RichIterable
                ? UnifiedSet.<K>newSet(((RichIterable<?>) source).size())
                : UnifiedSet.<K>newSet();
        Iterate.forEachWith(source, Procedures2.<K>addToCollection(), result);
        return result;
    }

    public static <K> UnifiedSet<K> newSet(int size, float loadFactor)
    {
        return new UnifiedSet<K>(size, loadFactor);
    }

    public static <K> UnifiedSet<K> newSetWith(K... elements)
    {
        return UnifiedSet.<K>newSet(elements.length).with(elements);
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
        this.allocateTable(capacity);
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
        this.maxSize = Math.min(capacity - 1, (int) (capacity * this.loadFactor));
    }

    protected final int index(Object key)
    {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        int h = key == null ? 0 : key.hashCode();
        h ^= h >>> 20 ^ h >>> 12;
        h ^= h >>> 7 ^ h >>> 4;
        return h & this.table.length - 1;
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

    public boolean add(K key)
    {
        int index = this.index(key);
        Object cur = this.table[index];
        if (cur == null)
        {
            this.table[index] = toSentinelIfNull(key);
            if (++this.occupied > this.maxSize)
            {
                this.rehash();
            }
            return true;
        }
        if (cur instanceof ChainedBucket || !this.nonNullTableObjectEquals(cur, key))
        {
            return this.chainedAdd(key, index);
        }
        return false;
    }

    private boolean chainedAdd(K key, int index)
    {
        Object realKey = toSentinelIfNull(key);
        if (this.table[index] instanceof ChainedBucket)
        {
            ChainedBucket bucket = (ChainedBucket) this.table[index];
            do
            {
                if (this.nonNullTableObjectEquals(bucket.zero, key))
                {
                    return false;
                }
                if (bucket.one == null)
                {
                    bucket.one = realKey;
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return true;
                }
                if (this.nonNullTableObjectEquals(bucket.one, key))
                {
                    return false;
                }
                if (bucket.two == null)
                {
                    bucket.two = realKey;
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return true;
                }
                if (this.nonNullTableObjectEquals(bucket.two, key))
                {
                    return false;
                }
                if (bucket.three instanceof ChainedBucket)
                {
                    bucket = (ChainedBucket) bucket.three;
                    continue;
                }
                if (bucket.three == null)
                {
                    bucket.three = realKey;
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return true;
                }
                if (this.nonNullTableObjectEquals(bucket.three, key))
                {
                    return false;
                }
                bucket.three = new ChainedBucket(bucket.three, realKey);
                if (++this.occupied > this.maxSize)
                {
                    this.rehash();
                }
                return true;
            }
            while (true);
        }
        ChainedBucket newBucket = new ChainedBucket(this.table[index], realKey);
        this.table[index] = newBucket;
        if (++this.occupied > this.maxSize)
        {
            this.rehash();
        }
        return true;
    }

    protected void rehash()
    {
        this.rehash(this.table.length << 1);
    }

    protected void rehash(int newCapacity)
    {
        int oldLength = this.table.length;
        Object[] old = this.table;
        this.allocate(newCapacity);
        this.occupied = 0;

        for (int i = 0; i < oldLength; i++)
        {
            Object oldKey = old[i];
            if (oldKey instanceof ChainedBucket)
            {
                ChainedBucket bucket = (ChainedBucket) oldKey;
                do
                {
                    if (bucket.zero != null)
                    {
                        this.add(this.nonSentinel(bucket.zero));
                    }
                    if (bucket.one == null)
                    {
                        break;
                    }
                    this.add(this.nonSentinel(bucket.one));
                    if (bucket.two == null)
                    {
                        break;
                    }
                    this.add(this.nonSentinel(bucket.two));
                    if (bucket.three != null)
                    {
                        if (bucket.three instanceof ChainedBucket)
                        {
                            bucket = (ChainedBucket) bucket.three;
                            continue;
                        }
                        this.add(this.nonSentinel(bucket.three));
                    }
                    break;
                }
                while (true);
            }
            else if (oldKey != null)
            {
                this.add(this.nonSentinel(oldKey));
            }
        }
    }

    public boolean contains(Object key)
    {
        int index = this.index(key);
        Object cur = this.table[index];
        if (cur == null)
        {
            return false;
        }
        if (cur instanceof ChainedBucket)
        {
            return this.chainContains((ChainedBucket) cur, (K) key);
        }
        return this.nonNullTableObjectEquals(cur, (K) key);
    }

    private boolean chainContains(ChainedBucket bucket, K key)
    {
        do
        {
            if (this.nonNullTableObjectEquals(bucket.zero, key))
            {
                return true;
            }
            if (bucket.one == null)
            {
                return false;
            }
            if (this.nonNullTableObjectEquals(bucket.one, key))
            {
                return true;
            }
            if (bucket.two == null)
            {
                return false;
            }
            if (this.nonNullTableObjectEquals(bucket.two, key))
            {
                return true;
            }
            if (bucket.three == null)
            {
                return false;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            return this.nonNullTableObjectEquals(bucket.three, key);
        }
        while (true);
    }

    public int getBatchCount(int batchSize)
    {
        return Math.max(1, this.table.length / batchSize);
    }

    public void batchForEach(Procedure<? super K> procedure, int sectionIndex, int sectionCount)
    {
        Object[] set = this.table;
        int sectionSize = set.length / sectionCount;
        int start = sectionSize * sectionIndex;
        int end = sectionIndex == sectionCount - 1 ? set.length : start + sectionSize;
        for (int i = start; i < end; i++)
        {
            Object cur = set[i];
            if (cur != null)
            {
                if (cur instanceof ChainedBucket)
                {
                    this.chainedForEach((ChainedBucket) cur, procedure);
                }
                else
                {
                    procedure.value(this.nonSentinel(cur));
                }
            }
        }
    }

    public void forEach(Procedure<? super K> procedure)
    {
        for (int i = 0; i < this.table.length; i++)
        {
            Object cur = this.table[i];
            if (cur instanceof ChainedBucket)
            {
                this.chainedForEach((ChainedBucket) cur, procedure);
            }
            else if (cur != null)
            {
                procedure.value(this.nonSentinel(cur));
            }
        }
    }

    private void chainedForEach(ChainedBucket bucket, Procedure<? super K> procedure)
    {
        do
        {
            procedure.value(this.nonSentinel(bucket.zero));
            if (bucket.one == null)
            {
                return;
            }
            procedure.value(this.nonSentinel(bucket.one));
            if (bucket.two == null)
            {
                return;
            }
            procedure.value(this.nonSentinel(bucket.two));
            if (bucket.three == null)
            {
                return;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            procedure.value(this.nonSentinel(bucket.three));
            return;
        }
        while (true);
    }

    public <P> void forEachWith(Procedure2<? super K, ? super P> procedure, P parameter)
    {
        for (int i = 0; i < this.table.length; i++)
        {
            Object cur = this.table[i];
            if (cur instanceof ChainedBucket)
            {
                this.chainedForEachWith((ChainedBucket) cur, procedure, parameter);
            }
            else if (cur != null)
            {
                procedure.value(this.nonSentinel(cur), parameter);
            }
        }
    }

    private <P> void chainedForEachWith(
            ChainedBucket bucket,
            Procedure2<? super K, ? super P> procedure,
            P parameter)
    {
        do
        {
            procedure.value(this.nonSentinel(bucket.zero), parameter);
            if (bucket.one == null)
            {
                return;
            }
            procedure.value(this.nonSentinel(bucket.one), parameter);
            if (bucket.two == null)
            {
                return;
            }
            procedure.value(this.nonSentinel(bucket.two), parameter);
            if (bucket.three == null)
            {
                return;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            procedure.value(this.nonSentinel(bucket.three), parameter);
            return;
        }
        while (true);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super K> objectIntProcedure)
    {
        int count = 0;
        for (int i = 0; i < this.table.length; i++)
        {
            Object cur = this.table[i];
            if (cur instanceof ChainedBucket)
            {
                count = this.chainedForEachWithIndex((ChainedBucket) cur, objectIntProcedure, count);
            }
            else if (cur != null)
            {
                objectIntProcedure.value(this.nonSentinel(cur), count++);
            }
        }
    }

    private int chainedForEachWithIndex(ChainedBucket bucket, ObjectIntProcedure<? super K> procedure, int count)
    {
        do
        {
            procedure.value(this.nonSentinel(bucket.zero), count++);
            if (bucket.one == null)
            {
                return count;
            }
            procedure.value(this.nonSentinel(bucket.one), count++);
            if (bucket.two == null)
            {
                return count;
            }
            procedure.value(this.nonSentinel(bucket.two), count++);
            if (bucket.three == null)
            {
                return count;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            procedure.value(this.nonSentinel(bucket.three), count++);
            return count;
        }
        while (true);
    }

    public UnifiedSet<K> newEmpty()
    {
        return UnifiedSet.newSet();
    }

    public K getFirst()
    {
        return this.isEmpty() ? null : this.iterator().next();
    }

    public K getLast()
    {
        return this.getFirst();
    }

    public UnifiedSet<K> select(Predicate<? super K> predicate)
    {
        return this.select(predicate, this.newEmpty());
    }

    public <R extends Collection<K>> R select(Predicate<? super K> predicate, R target)
    {
        this.forEach(new SelectProcedure<K>(predicate, target));
        return target;
    }

    public <P> UnifiedSet<K> selectWith(
            Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        return this.selectWith(predicate, parameter, this.newEmpty());
    }

    public <P, R extends Collection<K>> R selectWith(
            final Predicate2<? super K, ? super P> predicate,
            P parameter,
            final R targetCollection)
    {
        this.forEachWith(new Procedure2<K, P>()
        {
            public void value(K each, P parm)
            {
                if (predicate.accept(each, parm))
                {
                    targetCollection.add(each);
                }
            }
        }, parameter);
        return targetCollection;
    }

    public UnifiedSet<K> reject(Predicate<? super K> predicate)
    {
        return this.reject(predicate, this.newEmpty());
    }

    public <R extends Collection<K>> R reject(Predicate<? super K> predicate, R target)
    {
        this.forEach(new RejectProcedure<K>(predicate, target));
        return target;
    }

    public <P> UnifiedSet<K> rejectWith(
            Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        return this.rejectWith(predicate, parameter, this.newEmpty());
    }

    public <P, R extends Collection<K>> R rejectWith(
            final Predicate2<? super K, ? super P> predicate,
            P parameter,
            final R targetCollection)
    {
        this.forEachWith(new Procedure2<K, P>()
        {
            public void value(K each, P parm)
            {
                if (!predicate.accept(each, parm))
                {
                    targetCollection.add(each);
                }
            }
        }, parameter);
        return targetCollection;
    }

    public <P> Twin<MutableList<K>> selectAndRejectWith(
            final Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        final MutableList<K> positiveResult = Lists.mutable.of();
        final MutableList<K> negativeResult = Lists.mutable.of();
        this.forEachWith(new Procedure2<K, P>()
        {
            public void value(K each, P parm)
            {
                (predicate.accept(each, parm) ? positiveResult : negativeResult).add(each);
            }
        }, parameter);
        return Tuples.twin(positiveResult, negativeResult);
    }

    public PartitionMutableSet<K> partition(Predicate<? super K> predicate)
    {
        PartitionMutableSet<K> partitionUnifiedSet = new PartitionUnifiedSet<K>();
        this.forEach(new PartitionProcedure<K>(predicate, partitionUnifiedSet));
        return partitionUnifiedSet;
    }

    public <S> UnifiedSet<S> selectInstancesOf(Class<S> clazz)
    {
        UnifiedSet<S> result = UnifiedSet.newSet();
        this.forEach(new SelectInstancesOfProcedure<S>(clazz, result));
        return result;
    }

    public void removeIf(Predicate<? super K> predicate)
    {
        IterableIterate.removeIf(this, predicate);
    }

    public <P> void removeIfWith(Predicate2<? super K, ? super P> predicate, P parameter)
    {
        IterableIterate.removeIfWith(this, predicate, parameter);
    }

    public <V> UnifiedSet<V> collect(Function<? super K, ? extends V> function)
    {
        return this.collect(function, UnifiedSet.<V>newSet());
    }

    public MutableBooleanSet collectBoolean(BooleanFunction<? super K> booleanFunction)
    {
        BooleanHashSet result = new BooleanHashSet();
        this.forEach(new CollectBooleanProcedure<K>(booleanFunction, result));
        return result;
    }

    public MutableByteSet collectByte(ByteFunction<? super K> byteFunction)
    {
        ByteHashSet result = new ByteHashSet(this.size());
        this.forEach(new CollectByteProcedure<K>(byteFunction, result));
        return result;
    }

    public MutableCharSet collectChar(CharFunction<? super K> charFunction)
    {
        CharHashSet result = new CharHashSet(this.size());
        this.forEach(new CollectCharProcedure<K>(charFunction, result));
        return result;
    }

    public MutableDoubleSet collectDouble(DoubleFunction<? super K> doubleFunction)
    {
        DoubleHashSet result = new DoubleHashSet(this.size());
        this.forEach(new CollectDoubleProcedure<K>(doubleFunction, result));
        return result;
    }

    public MutableFloatSet collectFloat(FloatFunction<? super K> floatFunction)
    {
        FloatHashSet result = new FloatHashSet(this.size());
        this.forEach(new CollectFloatProcedure<K>(floatFunction, result));
        return result;
    }

    public MutableIntSet collectInt(IntFunction<? super K> intFunction)
    {
        IntHashSet result = new IntHashSet(this.size());
        this.forEach(new CollectIntProcedure<K>(intFunction, result));
        return result;
    }

    public MutableLongSet collectLong(LongFunction<? super K> longFunction)
    {
        LongHashSet result = new LongHashSet(this.size());
        this.forEach(new CollectLongProcedure<K>(longFunction, result));
        return result;
    }

    public MutableShortSet collectShort(ShortFunction<? super K> shortFunction)
    {
        ShortHashSet result = new ShortHashSet(this.size());
        this.forEach(new CollectShortProcedure<K>(shortFunction, result));
        return result;
    }

    public <V, R extends Collection<V>> R collect(Function<? super K, ? extends V> function, R target)
    {
        this.forEach(new CollectProcedure<K, V>(function, target));
        return target;
    }

    public <V> UnifiedSet<V> flatCollect(Function<? super K, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, UnifiedSet.<V>newSet());
    }

    public <V, R extends Collection<V>> R flatCollect(
            Function<? super K, ? extends Iterable<V>> function, R target)
    {
        this.forEach(new FlatCollectProcedure<K, V>(function, target));
        return target;
    }

    public <P, A> UnifiedSet<A> collectWith(Function2<? super K, ? super P, ? extends A> function, P parameter)
    {
        return this.collectWith(function, parameter, UnifiedSet.<A>newSet());
    }

    public <P, A, R extends Collection<A>> R collectWith(
            final Function2<? super K, ? super P, ? extends A> function, P parameter, final R targetCollection)
    {
        this.forEachWith(new Procedure2<K, P>()
        {
            public void value(K each, P parm)
            {
                targetCollection.add(function.value(each, parm));
            }
        }, parameter);
        return targetCollection;
    }

    public <V> UnifiedSet<V> collectIf(
            Predicate<? super K> predicate, Function<? super K, ? extends V> function)
    {
        return this.collectIf(predicate, function, UnifiedSet.<V>newSet());
    }

    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super K> predicate, Function<? super K, ? extends V> function, R target)
    {
        this.forEach(new CollectIfProcedure<K, V>(target, function, predicate));
        return target;
    }

    public K detect(Predicate<? super K> predicate)
    {
        return IterableIterate.detect(this, predicate);
    }

    public K min(Comparator<? super K> comparator)
    {
        return Iterate.min(this, comparator);
    }

    public K max(Comparator<? super K> comparator)
    {
        return Iterate.max(this, comparator);
    }

    public K min()
    {
        return Iterate.min(this);
    }

    public K max()
    {
        return Iterate.max(this);
    }

    public <V extends Comparable<? super V>> K minBy(Function<? super K, ? extends V> function)
    {
        return IterableIterate.minBy(this, function);
    }

    public <V extends Comparable<? super V>> K maxBy(Function<? super K, ? extends V> function)
    {
        return IterableIterate.maxBy(this, function);
    }

    public K detectIfNone(Predicate<? super K> predicate, Function0<? extends K> function)
    {
        K result = this.detect(predicate);
        return result == null ? function.value() : result;
    }

    public <P> K detectWith(Predicate2<? super K, ? super P> predicate, P parameter)
    {
        return IterableIterate.detectWith(this, predicate, parameter);
    }

    public <P> K detectWithIfNone(
            Predicate2<? super K, ? super P> predicate,
            P parameter,
            Function0<? extends K> function)
    {
        K result = this.detectWith(predicate, parameter);
        return result == null ? function.value() : result;
    }

    public int count(Predicate<? super K> predicate)
    {
        CountProcedure<K> procedure = new CountProcedure<K>(predicate);
        this.forEach(procedure);
        return procedure.getCount();
    }

    public <P> int countWith(final Predicate2<? super K, ? super P> predicate, P parameter)
    {
        final Counter count = new Counter();
        this.forEachWith(new Procedure2<K, P>()
        {
            public void value(K each, P parm)
            {
                if (predicate.accept(each, parm))
                {
                    count.increment();
                }
            }
        }, parameter);
        return count.getCount();
    }

    public boolean anySatisfy(Predicate<? super K> predicate)
    {
        return IterableIterate.anySatisfy(this, predicate);
    }

    public <P> boolean anySatisfyWith(
            Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        return IterableIterate.anySatisfyWith(this, predicate, parameter);
    }

    public boolean allSatisfy(Predicate<? super K> predicate)
    {
        return IterableIterate.allSatisfy(this, predicate);
    }

    public <P> boolean allSatisfyWith(
            Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        return IterableIterate.allSatisfyWith(this, predicate, parameter);
    }

    public boolean noneSatisfy(Predicate<? super K> predicate)
    {
        return IterableIterate.noneSatisfy(this, predicate);
    }

    public <P> boolean noneSatisfyWith(
            Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        return IterableIterate.noneSatisfyWith(this, predicate, parameter);
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super K, ? extends IV> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super K> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super K> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super K> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super K> function)
    {
        return IterableIterate.injectInto(injectedValue, this, function);
    }

    public long sumOfInt(IntFunction<? super K> function)
    {
        return IterableIterate.sumOfInt(this, function);
    }

    public double sumOfFloat(FloatFunction<? super K> function)
    {
        return IterableIterate.sumOfFloat(this, function);
    }

    public long sumOfLong(LongFunction<? super K> function)
    {
        return IterableIterate.sumOfLong(this, function);
    }

    public double sumOfDouble(DoubleFunction<? super K> function)
    {
        return IterableIterate.sumOfDouble(this, function);
    }

    public <IV, P> IV injectIntoWith(
            IV injectValue,
            Function3<? super IV, ? super K, ? super P, ? extends IV> function,
            P parameter)
    {
        return IterableIterate.injectIntoWith(injectValue, this, function, parameter);
    }

    public MutableList<K> toList()
    {
        return FastList.newList(this);
    }

    public MutableList<K> toSortedList()
    {
        return FastList.newList(this).sortThis();
    }

    public MutableList<K> toSortedList(Comparator<? super K> comparator)
    {
        return FastList.newList(this).sortThis(comparator);
    }

    public <V extends Comparable<? super V>> MutableList<K> toSortedListBy(
            Function<? super K, ? extends V> function)
    {
        return this.toSortedList(Comparators.byFunction(function));
    }

    public MutableSortedSet<K> toSortedSet()
    {
        return TreeSortedSet.newSet(null, this);
    }

    public MutableSortedSet<K> toSortedSet(Comparator<? super K> comparator)
    {
        return TreeSortedSet.newSet(comparator, this);
    }

    public <V extends Comparable<? super V>> MutableSortedSet<K> toSortedSetBy(Function<? super K, ? extends V> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public UnifiedSet<K> toSet()
    {
        return UnifiedSet.newSet(this);
    }

    public MutableBag<K> toBag()
    {
        return HashBag.newBag(this);
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super K, ? extends NK> keyFunction,
            Function<? super K, ? extends NV> valueFunction)
    {
        return UnifiedMap.<NK, NV>newMap(this.size()).collectKeysAndValues(this, keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super K, ? extends NK> keyFunction,
            Function<? super K, ? extends NV> valueFunction)
    {
        return TreeSortedMap.<NK, NV>newMap().collectKeysAndValues(this, keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super K, ? extends NK> keyFunction,
            Function<? super K, ? extends NV> valueFunction)
    {
        return TreeSortedMap.<NK, NV>newMap(comparator).collectKeysAndValues(this, keyFunction, valueFunction);
    }

    public LazyIterable<K> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public MutableSet<K> asUnmodifiable()
    {
        return UnmodifiableMutableSet.of(this);
    }

    public MutableSet<K> asSynchronized()
    {
        return SynchronizedMutableSet.of(this);
    }

    public ImmutableSet<K> toImmutable()
    {
        return Sets.immutable.ofAll(this);
    }

    public boolean notEmpty()
    {
        return !this.isEmpty();
    }

    public boolean isEmpty()
    {
        return this.occupied == 0;
    }

    public UnifiedSet<K> with(K element)
    {
        this.add(element);
        return this;
    }

    public UnifiedSet<K> with(K element1, K element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public UnifiedSet<K> with(K element1, K element2, K element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public UnifiedSet<K> with(K... elements)
    {
        this.addAll(Arrays.asList(elements));
        return this;
    }

    public UnifiedSet<K> withAll(Iterable<? extends K> iterable)
    {
        this.addAllIterable(iterable);
        return this;
    }

    public UnifiedSet<K> without(K element)
    {
        this.remove(element);
        return this;
    }

    public UnifiedSet<K> withoutAll(Iterable<? extends K> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    public boolean addAll(Collection<? extends K> collection)
    {
        return this.addAllIterable(collection);
    }

    public boolean addAllIterable(Iterable<? extends K> iterable)
    {
        if (iterable instanceof UnifiedSet)
        {
            return this.copySet((UnifiedSet<?>) iterable);
        }
        int size = Iterate.sizeOf(iterable);
        this.ensureCapacity(size);
        int oldSize = this.size();
        Iterate.forEachWith(iterable, Procedures2.<K>addToCollection(), this);
        return this.size() != oldSize;
    }

    private void ensureCapacity(int size)
    {
        if (size > this.maxSize)
        {
            size = (int) (size / this.loadFactor) + 1;
            int capacity = Integer.highestOneBit(size);
            if (size != capacity)
            {
                capacity <<= 1;
            }
            this.rehash(capacity);
        }
    }

    protected boolean copySet(UnifiedSet<?> unifiedset)
    {
        //todo: optimize for current size == 0
        boolean changed = false;
        for (int i = 0; i < unifiedset.table.length; i++)
        {
            Object cur = unifiedset.table[i];
            if (cur instanceof ChainedBucket)
            {
                changed |= this.copyChain((ChainedBucket) cur);
            }
            else if (cur != null)
            {
                changed |= this.add(this.nonSentinel(cur));
            }
        }
        return changed;
    }

    private boolean copyChain(ChainedBucket bucket)
    {
        boolean changed = false;
        do
        {
            changed |= this.add(this.nonSentinel(bucket.zero));
            if (bucket.one == null)
            {
                return changed;
            }
            changed |= this.add(this.nonSentinel(bucket.one));
            if (bucket.two == null)
            {
                return changed;
            }
            changed |= this.add(this.nonSentinel(bucket.two));
            if (bucket.three == null)
            {
                return changed;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            changed |= this.add(this.nonSentinel(bucket.three));
            return changed;
        }
        while (true);
    }

    public boolean remove(Object key)
    {
        int index = this.index(key);

        Object cur = this.table[index];
        if (cur == null)
        {
            return false;
        }
        if (cur instanceof ChainedBucket)
        {
            return this.removeFromChain((ChainedBucket) cur, (K) key, index);
        }
        if (this.nonNullTableObjectEquals(cur, (K) key))
        {
            this.table[index] = null;
            this.occupied--;
            return true;
        }
        return false;
    }

    private boolean removeFromChain(ChainedBucket bucket, K key, int index)
    {
        if (this.nonNullTableObjectEquals(bucket.zero, key))
        {
            bucket.zero = bucket.removeLast(0);
            if (bucket.zero == null)
            {
                this.table[index] = null;
            }
            this.occupied--;
            return true;
        }
        if (bucket.one == null)
        {
            return false;
        }
        if (this.nonNullTableObjectEquals(bucket.one, key))
        {
            bucket.one = bucket.removeLast(1);
            this.occupied--;
            return true;
        }
        if (bucket.two == null)
        {
            return false;
        }
        if (this.nonNullTableObjectEquals(bucket.two, key))
        {
            bucket.two = bucket.removeLast(2);
            this.occupied--;
            return true;
        }
        if (bucket.three == null)
        {
            return false;
        }
        if (bucket.three instanceof ChainedBucket)
        {
            return this.removeDeepChain(bucket, key);
        }
        if (this.nonNullTableObjectEquals(bucket.three, key))
        {
            bucket.three = bucket.removeLast(3);
            this.occupied--;
            return true;
        }
        return false;
    }

    private boolean removeDeepChain(ChainedBucket oldBucket, K key)
    {
        do
        {
            ChainedBucket bucket = (ChainedBucket) oldBucket.three;
            if (this.nonNullTableObjectEquals(bucket.zero, key))
            {
                bucket.zero = bucket.removeLast(0);
                if (bucket.zero == null)
                {
                    oldBucket.three = null;
                }
                this.occupied--;
                return true;
            }
            if (bucket.one == null)
            {
                return false;
            }
            if (this.nonNullTableObjectEquals(bucket.one, key))
            {
                bucket.one = bucket.removeLast(1);
                this.occupied--;
                return true;
            }
            if (bucket.two == null)
            {
                return false;
            }
            if (this.nonNullTableObjectEquals(bucket.two, key))
            {
                bucket.two = bucket.removeLast(2);
                this.occupied--;
                return true;
            }
            if (bucket.three == null)
            {
                return false;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                oldBucket = bucket;
                continue;
            }
            if (this.nonNullTableObjectEquals(bucket.three, key))
            {
                bucket.three = bucket.removeLast(3);
                this.occupied--;
                return true;
            }
            return false;
        }
        while (true);
    }

    public int size()
    {
        return this.occupied;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (!(object instanceof Set))
        {
            return false;
        }

        Set<?> other = (Set<?>) object;
        return this.size() == other.size() && this.containsAll(other);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        for (int i = 0; i < this.table.length; i++)
        {
            Object cur = this.table[i];
            if (cur instanceof ChainedBucket)
            {
                hashCode += this.chainedHashCode((ChainedBucket) cur);
            }
            else if (cur != null)
            {
                hashCode += cur == NULL_KEY ? 0 : cur.hashCode();
            }
        }
        return hashCode;
    }

    private int chainedHashCode(ChainedBucket bucket)
    {
        int hashCode = 0;
        do
        {
            hashCode += bucket.zero == NULL_KEY ? 0 : bucket.zero.hashCode();
            if (bucket.one == null)
            {
                return hashCode;
            }
            hashCode += bucket.one == NULL_KEY ? 0 : bucket.one.hashCode();
            if (bucket.two == null)
            {
                return hashCode;
            }
            hashCode += bucket.two == NULL_KEY ? 0 : bucket.two.hashCode();
            if (bucket.three == null)
            {
                return hashCode;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            hashCode += bucket.three == NULL_KEY ? 0 : bucket.three.hashCode();
            return hashCode;
        }
        while (true);
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int size = in.readInt();
        this.loadFactor = in.readFloat();
        this.init(Math.max((int) (size / this.loadFactor) + 1, DEFAULT_INITIAL_CAPACITY));
        for (int i = 0; i < size; i++)
        {
            this.add((K) in.readObject());
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.size());
        out.writeFloat(this.loadFactor);
        for (int i = 0; i < this.table.length; i++)
        {
            Object o = this.table[i];
            if (o != null)
            {
                if (o instanceof ChainedBucket)
                {
                    this.writeExternalChain(out, (ChainedBucket) o);
                }
                else
                {
                    out.writeObject(this.nonSentinel(o));
                }
            }
        }
    }

    private void writeExternalChain(ObjectOutput out, ChainedBucket bucket) throws IOException
    {
        do
        {
            out.writeObject(this.nonSentinel(bucket.zero));
            if (bucket.one == null)
            {
                return;
            }
            out.writeObject(this.nonSentinel(bucket.one));
            if (bucket.two == null)
            {
                return;
            }
            out.writeObject(this.nonSentinel(bucket.two));
            if (bucket.three == null)
            {
                return;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            out.writeObject(this.nonSentinel(bucket.three));
            return;
        }
        while (true);
    }

    public boolean containsAll(Collection<?> collection)
    {
        return Iterate.allSatisfyWith(collection, Predicates2.in(), this);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.allSatisfyWith(source, Predicates2.in(), this);
    }

    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.allSatisfyWith(elements, Predicates2.in(), this);
    }

    public boolean removeAll(Collection<?> collection)
    {
        return this.removeAllIterable(collection);
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        boolean changed = false;
        for (Object each : iterable)
        {
            changed |= this.remove(each);
        }
        return changed;
    }

    private void addIfFound(K key, UnifiedSet<K> other)
    {
        int index = this.index(key);

        Object cur = this.table[index];
        if (cur == null)
        {
            return;
        }
        if (cur instanceof ChainedBucket)
        {
            this.addIfFoundFromChain((ChainedBucket) cur, key, other);
            return;
        }
        if (this.nonNullTableObjectEquals(cur, key))
        {
            other.add(this.nonSentinel(cur));
        }
    }

    private void addIfFoundFromChain(ChainedBucket bucket, K key, UnifiedSet<K> other)
    {
        do
        {
            if (this.nonNullTableObjectEquals(bucket.zero, key))
            {
                other.add(this.nonSentinel(bucket.zero));
                return;
            }
            if (bucket.one == null)
            {
                return;
            }
            if (this.nonNullTableObjectEquals(bucket.one, key))
            {
                other.add(this.nonSentinel(bucket.one));
                return;
            }
            if (bucket.two == null)
            {
                return;
            }
            if (this.nonNullTableObjectEquals(bucket.two, key))
            {
                other.add(this.nonSentinel(bucket.two));
                return;
            }
            if (bucket.three == null)
            {
                return;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            if (this.nonNullTableObjectEquals(bucket.three, key))
            {
                other.add(this.nonSentinel(bucket.three));
                return;
            }
            return;
        }
        while (true);
    }

    public boolean retainAll(Collection<?> collection)
    {
        return this.retainAllIterable(collection);
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        if (iterable instanceof Set)
        {
            return this.retainAllFromSet((Set<?>) iterable);
        }
        return this.retainAllFromNonSet(iterable);
    }

    private boolean retainAllFromNonSet(Iterable<?> iterable)
    {
        int retainedSize = Iterate.sizeOf(iterable);
        UnifiedSet<K> retainedCopy = new UnifiedSet<K>(retainedSize, this.loadFactor);
        for (Object key : iterable)
        {
            this.addIfFound((K) key, retainedCopy);
        }
        if (retainedCopy.size() < this.size())
        {
            this.maxSize = retainedCopy.maxSize;
            this.occupied = retainedCopy.occupied;
            this.table = retainedCopy.table;
            return true;
        }
        return false;
    }

    private boolean retainAllFromSet(Set<?> collection)
    {
        // TODO: turn iterator into a loop
        boolean result = false;
        Iterator<K> e = this.iterator();
        while (e.hasNext())
        {
            if (!collection.contains(e.next()))
            {
                e.remove();
                result = true;
            }
        }
        return result;
    }

    @Override
    public UnifiedSet<K> clone()
    {
        return new UnifiedSet<K>(this);
    }

    public Object[] toArray()
    {
        Object[] result = new Object[this.occupied];
        this.copyToArray(result);
        return result;
    }

    private void copyToArray(Object[] result)
    {
        Object[] table = this.table;
        int count = 0;
        for (int i = 0; i < table.length; i++)
        {
            Object cur = table[i];
            if (cur != null)
            {
                if (cur instanceof ChainedBucket)
                {
                    ChainedBucket bucket = (ChainedBucket) cur;
                    count = this.copyBucketToArray(result, bucket, count);
                }
                else
                {
                    result[count++] = this.nonSentinel(cur);
                }
            }
        }
    }

    private int copyBucketToArray(Object[] result, ChainedBucket bucket, int count)
    {
        do
        {
            result[count++] = this.nonSentinel(bucket.zero);
            if (bucket.one == null)
            {
                break;
            }
            result[count++] = this.nonSentinel(bucket.one);
            if (bucket.two == null)
            {
                break;
            }
            result[count++] = this.nonSentinel(bucket.two);
            if (bucket.three == null)
            {
                break;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            result[count++] = this.nonSentinel(bucket.three);
            break;
        }
        while (true);
        return count;
    }

    public <T> T[] toArray(T[] array)
    {
        int size = this.size();
        T[] result = array.length < size
                ? (T[]) Array.newInstance(array.getClass().getComponentType(), size)
                : array;

        this.copyToArray(result);
        if (size < result.length)
        {
            result[size] = null;
        }
        return result;
    }

    public Iterator<K> iterator()
    {
        return new PositionalIterator();
    }

    protected class PositionalIterator implements Iterator<K>
    {
        protected int count;
        protected int position;
        protected int chainPosition;
        protected boolean lastReturned;

        public boolean hasNext()
        {
            return this.count < UnifiedSet.this.size();
        }

        public void remove()
        {
            if (!this.lastReturned)
            {
                throw new IllegalStateException("next() must be called as many times as remove()");
            }
            this.count--;
            UnifiedSet.this.occupied--;

            if (this.chainPosition != 0)
            {
                this.removeFromChain();
                return;
            }

            int pos = this.position - 1;
            Object key = UnifiedSet.this.table[pos];
            if (key instanceof ChainedBucket)
            {
                this.removeLastFromChain((ChainedBucket) key, pos);
                return;
            }
            UnifiedSet.this.table[pos] = null;
            this.position = pos;
            this.lastReturned = false;
        }

        protected void removeFromChain()
        {
            ChainedBucket chain = (ChainedBucket) UnifiedSet.this.table[this.position];
            chain.remove(--this.chainPosition);
            this.lastReturned = false;
        }

        protected void removeLastFromChain(ChainedBucket bucket, int tableIndex)
        {
            bucket.removeLast(0);
            if (bucket.zero == null)
            {
                UnifiedSet.this.table[tableIndex] = null;
            }
            this.lastReturned = false;
        }

        protected K nextFromChain()
        {
            ChainedBucket bucket = (ChainedBucket) UnifiedSet.this.table[this.position];
            Object cur = bucket.get(this.chainPosition);
            this.chainPosition++;
            if (bucket.get(this.chainPosition) == null)
            {
                this.chainPosition = 0;
                this.position++;
            }
            this.lastReturned = true;
            return UnifiedSet.this.nonSentinel(cur);
        }

        public K next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException("next() called, but the iterator is exhausted");
            }
            this.count++;
            Object[] table = UnifiedSet.this.table;
            if (this.chainPosition != 0)
            {
                return this.nextFromChain();
            }
            while (table[this.position] == null)
            {
                this.position++;
            }
            Object cur = table[this.position];
            if (cur instanceof ChainedBucket)
            {
                return this.nextFromChain();
            }
            this.position++;
            this.lastReturned = true;
            return UnifiedSet.this.nonSentinel(cur);
        }
    }

    private static final class ChainedBucket
    {
        private Object zero;
        private Object one;
        private Object two;
        private Object three;

        private ChainedBucket()
        {
        }

        private ChainedBucket(Object first, Object second)
        {
            this.zero = first;
            this.one = second;
        }

        public void remove(int i)
        {
            if (i > 3)
            {
                this.removeLongChain(this, i - 3);
            }
            else
            {
                switch (i)
                {
                    case 0:
                        this.zero = this.removeLast(0);
                        return;
                    case 1:
                        this.one = this.removeLast(1);
                        return;
                    case 2:
                        this.two = this.removeLast(2);
                        return;
                    case 3:
                        if (this.three instanceof ChainedBucket)
                        {
                            this.removeLongChain(this, i - 3);
                            return;
                        }
                        this.three = null;
                        return;
                    default:
                        throw new AssertionError();
                }
            }
        }

        private void removeLongChain(ChainedBucket oldBucket, int i)
        {
            do
            {
                ChainedBucket bucket = (ChainedBucket) oldBucket.three;
                switch (i)
                {
                    case 0:
                        bucket.zero = bucket.removeLast(0);
                        return;
                    case 1:
                        bucket.one = bucket.removeLast(1);
                        return;
                    case 2:
                        bucket.two = bucket.removeLast(2);
                        return;
                    case 3:
                        if (bucket.three instanceof ChainedBucket)
                        {
                            i -= 3;
                            oldBucket = bucket;
                            continue;
                        }
                        bucket.three = null;
                        return;
                    default:
                        throw new AssertionError();
                }
            }
            while (true);
        }

        public Object get(int i)
        {
            ChainedBucket bucket = this;
            while (i > 3 && bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                i -= 3;
            }
            do
            {
                switch (i)
                {
                    case 0:
                        return bucket.zero;
                    case 1:
                        return bucket.one;
                    case 2:
                        return bucket.two;
                    case 3:
                        if (bucket.three instanceof ChainedBucket)
                        {
                            i -= 3;
                            bucket = (ChainedBucket) bucket.three;
                            continue;
                        }
                        return bucket.three;
                    case 4:
                        return null; // this happens when a bucket is exactly full and we're iterating
                }
            }
            while (true);
        }

        public Object removeLast(int cur)
        {
            if (this.three instanceof ChainedBucket)
            {
                return this.removeLast(this);
            }
            if (this.three != null)
            {
                Object result = this.three;
                this.three = null;
                return cur == 3 ? null : result;
            }
            if (this.two != null)
            {
                Object result = this.two;
                this.two = null;
                return cur == 2 ? null : result;
            }
            if (this.one != null)
            {
                Object result = this.one;
                this.one = null;
                return cur == 1 ? null : result;
            }
            this.zero = null;
            return null;
        }

        private Object removeLast(ChainedBucket oldBucket)
        {
            do
            {
                ChainedBucket bucket = (ChainedBucket) oldBucket.three;
                if (bucket.three instanceof ChainedBucket)
                {
                    oldBucket = bucket;
                    continue;
                }
                if (bucket.three != null)
                {
                    Object result = bucket.three;
                    bucket.three = null;
                    return result;
                }
                if (bucket.two != null)
                {
                    Object result = bucket.two;
                    bucket.two = null;
                    return result;
                }
                if (bucket.one != null)
                {
                    Object result = bucket.one;
                    bucket.one = null;
                    return result;
                }
                Object result = bucket.zero;
                oldBucket.three = null;
                return result;
            }
            while (true);
        }

        public ChainedBucket copy()
        {
            ChainedBucket result = new ChainedBucket();
            ChainedBucket dest = result;
            ChainedBucket src = this;
            do
            {
                dest.zero = src.zero;
                dest.one = src.one;
                dest.two = src.two;
                if (src.three instanceof ChainedBucket)
                {
                    dest.three = new ChainedBucket();
                    src = (ChainedBucket) src.three;
                    dest = (ChainedBucket) dest.three;
                    continue;
                }
                dest.three = src.three;
                return result;
            }
            while (true);
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
        IterableIterate.appendString(this, appendable, start, separator, end);
    }

    public <V> UnifiedSetMultimap<V, K> groupBy(
            Function<? super K, ? extends V> function)
    {
        return this.groupBy(function, UnifiedSetMultimap.<V, K>newMultimap());
    }

    public <V, R extends MutableMultimap<V, K>> R groupBy(
            Function<? super K, ? extends V> function,
            R target)
    {
        this.forEach(new MultimapPutProcedure<V, K>(target, function));
        return target;
    }

    public <V> UnifiedSetMultimap<V, K> groupByEach(
            Function<? super K, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, UnifiedSetMultimap.<V, K>newMultimap());
    }

    public <V, R extends MutableMultimap<V, K>> R groupByEach(
            Function<? super K, ? extends Iterable<V>> function,
            R target)
    {
        this.forEach(new MultimapEachPutProcedure<V, K>(target, function));
        return target;
    }

    public <S> MutableSet<Pair<K, S>> zip(Iterable<S> that)
    {
        return this.zip(that, UnifiedSet.<Pair<K, S>>newSet());
    }

    public <S, R extends Collection<Pair<K, S>>> R zip(Iterable<S> that, R target)
    {
        return IterableIterate.zip(this, that, target);
    }

    public MutableSet<Pair<K, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(UnifiedSet.<Pair<K, Integer>>newSet());
    }

    public <R extends Collection<Pair<K, Integer>>> R zipWithIndex(R target)
    {
        this.forEach(ZipWithIndexProcedure.create(target));
        return target;
    }

    public RichIterable<RichIterable<K>> chunk(int size)
    {
        return MutableCollectionIterate.chunk(this, size);
    }

    public MutableSet<K> union(SetIterable<? extends K> set)
    {
        return SetIterables.unionInto(this, set, this.newEmpty());
    }

    public <R extends Set<K>> R unionInto(SetIterable<? extends K> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public MutableSet<K> intersect(SetIterable<? extends K> set)
    {
        return SetIterables.intersectInto(this, set, this.newEmpty());
    }

    public <R extends Set<K>> R intersectInto(SetIterable<? extends K> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public MutableSet<K> difference(SetIterable<? extends K> subtrahendSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, this.newEmpty());
    }

    public <R extends Set<K>> R differenceInto(SetIterable<? extends K> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public MutableSet<K> symmetricDifference(SetIterable<? extends K> setB)
    {
        return SetIterables.symmetricDifferenceInto(this, setB, this.newEmpty());
    }

    public <R extends Set<K>> R symmetricDifferenceInto(SetIterable<? extends K> set, R targetSet)
    {
        return SetIterables.symmetricDifferenceInto(this, set, targetSet);
    }

    public boolean isSubsetOf(SetIterable<? extends K> candidateSuperset)
    {
        return SetIterables.isSubsetOf(this, candidateSuperset);
    }

    public boolean isProperSubsetOf(SetIterable<? extends K> candidateSuperset)
    {
        return SetIterables.isProperSubsetOf(this, candidateSuperset);
    }

    public MutableSet<UnsortedSetIterable<K>> powerSet()
    {
        return (MutableSet<UnsortedSetIterable<K>>) (MutableSet<?>) SetIterables.powerSet(this);
    }

    public <B> LazyIterable<Pair<K, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }

    public K get(K key)
    {
        int index = this.index(key);
        Object cur = this.table[index];

        if (cur == null)
        {
            return null;
        }
        if (cur instanceof ChainedBucket)
        {
            return this.chainedGet(key, (ChainedBucket) cur);
        }
        if (this.nonNullTableObjectEquals(cur, key))
        {
            return (K) cur;
        }
        return null;
    }

    private K chainedGet(K key, ChainedBucket bucket)
    {
        do
        {
            if (this.nonNullTableObjectEquals(bucket.zero, key))
            {
                return this.nonSentinel(bucket.zero);
            }
            if (bucket.one == null)
            {
                return null;
            }
            if (this.nonNullTableObjectEquals(bucket.one, key))
            {
                return this.nonSentinel(bucket.one);
            }
            if (bucket.two == null)
            {
                return null;
            }
            if (this.nonNullTableObjectEquals(bucket.two, key))
            {
                return this.nonSentinel(bucket.two);
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            if (bucket.three == null)
            {
                return null;
            }
            if (this.nonNullTableObjectEquals(bucket.three, key))
            {
                return this.nonSentinel(bucket.three);
            }
            return null;
        }
        while (true);
    }

    public K put(K key)
    {
        int index = this.index(key);
        Object cur = this.table[index];

        if (cur == null)
        {
            this.table[index] = toSentinelIfNull(key);
            if (++this.occupied > this.maxSize)
            {
                this.rehash();
            }
            return key;
        }

        if (cur instanceof ChainedBucket || !this.nonNullTableObjectEquals(cur, key))
        {
            return this.chainedPut(key, index);
        }
        return this.nonSentinel(cur);
    }

    private K chainedPut(K key, int index)
    {
        if (this.table[index] instanceof ChainedBucket)
        {
            ChainedBucket bucket = (ChainedBucket) this.table[index];
            do
            {
                if (this.nonNullTableObjectEquals(bucket.zero, key))
                {
                    return this.nonSentinel(bucket.zero);
                }
                if (bucket.one == null)
                {
                    bucket.one = toSentinelIfNull(key);
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return key;
                }
                if (this.nonNullTableObjectEquals(bucket.one, key))
                {
                    return this.nonSentinel(bucket.one);
                }
                if (bucket.two == null)
                {
                    bucket.two = toSentinelIfNull(key);
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return key;
                }
                if (this.nonNullTableObjectEquals(bucket.two, key))
                {
                    return this.nonSentinel(bucket.two);
                }
                if (bucket.three instanceof ChainedBucket)
                {
                    bucket = (ChainedBucket) bucket.three;
                    continue;
                }
                if (bucket.three == null)
                {
                    bucket.three = toSentinelIfNull(key);
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return key;
                }
                if (this.nonNullTableObjectEquals(bucket.three, key))
                {
                    return this.nonSentinel(bucket.three);
                }
                bucket.three = new ChainedBucket(bucket.three, key);
                if (++this.occupied > this.maxSize)
                {
                    this.rehash();
                }
                return key;
            }
            while (true);
        }
        ChainedBucket newBucket = new ChainedBucket(this.table[index], key);
        this.table[index] = newBucket;
        if (++this.occupied > this.maxSize)
        {
            this.rehash();
        }
        return key;
    }

    public K removeFromPool(K key)
    {
        int index = this.index(key);
        Object cur = this.table[index];
        if (cur == null)
        {
            return null;
        }
        if (cur instanceof ChainedBucket)
        {
            return this.removeFromChainForPool((ChainedBucket) cur, key, index);
        }
        if (this.nonNullTableObjectEquals(cur, key))
        {
            this.table[index] = null;
            this.occupied--;
            return this.nonSentinel(cur);
        }
        return null;
    }

    private K removeFromChainForPool(ChainedBucket bucket, K key, int index)
    {
        if (this.nonNullTableObjectEquals(bucket.zero, key))
        {
            Object result = bucket.zero;
            bucket.zero = bucket.removeLast(0);
            if (bucket.zero == null)
            {
                this.table[index] = null;
            }
            this.occupied--;
            return this.nonSentinel(result);
        }
        if (bucket.one == null)
        {
            return null;
        }
        if (this.nonNullTableObjectEquals(bucket.one, key))
        {
            Object result = bucket.one;
            bucket.one = bucket.removeLast(1);
            this.occupied--;
            return this.nonSentinel(result);
        }
        if (bucket.two == null)
        {
            return null;
        }
        if (this.nonNullTableObjectEquals(bucket.two, key))
        {
            Object result = bucket.two;
            bucket.two = bucket.removeLast(2);
            this.occupied--;
            return this.nonSentinel(result);
        }
        if (bucket.three == null)
        {
            return null;
        }
        if (bucket.three instanceof ChainedBucket)
        {
            return this.removeDeepChainForPool(bucket, key);
        }
        if (this.nonNullTableObjectEquals(bucket.three, key))
        {
            Object result = bucket.three;
            bucket.three = bucket.removeLast(3);
            this.occupied--;
            return this.nonSentinel(result);
        }
        return null;
    }

    private K removeDeepChainForPool(ChainedBucket oldBucket, K key)
    {
        do
        {
            ChainedBucket bucket = (ChainedBucket) oldBucket.three;
            if (this.nonNullTableObjectEquals(bucket.zero, key))
            {
                Object result = bucket.zero;
                bucket.zero = bucket.removeLast(0);
                if (bucket.zero == null)
                {
                    oldBucket.three = null;
                }
                this.occupied--;
                return this.nonSentinel(result);
            }
            if (bucket.one == null)
            {
                return null;
            }
            if (this.nonNullTableObjectEquals(bucket.one, key))
            {
                Object result = bucket.one;
                bucket.one = bucket.removeLast(1);
                this.occupied--;
                return this.nonSentinel(result);
            }
            if (bucket.two == null)
            {
                return null;
            }
            if (this.nonNullTableObjectEquals(bucket.two, key))
            {
                Object result = bucket.two;
                bucket.two = bucket.removeLast(2);
                this.occupied--;
                return this.nonSentinel(result);
            }
            if (bucket.three == null)
            {
                return null;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                oldBucket = bucket;
                continue;
            }
            if (this.nonNullTableObjectEquals(bucket.three, key))
            {
                Object result = bucket.three;
                bucket.three = bucket.removeLast(3);
                this.occupied--;
                return this.nonSentinel(result);
            }
            return null;
        }
        while (true);
    }

    private K nonSentinel(Object key)
    {
        return key == NULL_KEY ? null : (K) key;
    }

    private static Object toSentinelIfNull(Object key)
    {
        if (key == null)
        {
            return NULL_KEY;
        }
        return key;
    }

    private boolean nonNullTableObjectEquals(Object cur, K key)
    {
        return cur == key || (cur == NULL_KEY ? key == null : cur.equals(key));
    }

    public <K2, V> MutableMap<K2, V> aggregateInPlaceBy(
            Function<? super K, ? extends K2> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super K> mutatingAggregator)
    {
        MutableMap<K2, V> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<K, K2, V>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K2, V> MutableMap<K2, V> aggregateBy(
            Function<? super K, ? extends K2> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super K, ? extends V> nonMutatingAggregator)
    {
        MutableMap<K2, V> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<K, K2, V>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }
}
