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
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
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
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.procedure.CollectIfProcedure;
import com.gs.collections.impl.block.procedure.CollectProcedure;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.block.procedure.FlatCollectProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.block.procedure.FilterNotProcedure;
import com.gs.collections.impl.block.procedure.SelectProcedure;
import com.gs.collections.impl.block.procedure.ZipWithIndexProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.partition.set.PartitionUnifiedSet;
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
        public int hashCode()
        {
            return 0;
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
        this.table = new Object[set.table.length];

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
        Iterate.forEach(source, CollectionAddProcedure.on(result));
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
        this.table = new Object[capacity];
        this.computeMaxSize(capacity);

        return capacity;
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
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        h = h ^ (h >>> 7) ^ (h >>> 4);
        return h & (this.table.length - 1);
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
        Object realKey = toSentinelIfNull(key);
        int index = this.index(realKey);
        Object cur = this.table[index];

        if (cur == null)
        {
            this.table[index] = realKey;
            if (++this.occupied > this.maxSize)
            {
                this.rehash();
            }
            return true;
        }
        return cur != realKey && !cur.equals(realKey) && this.chainedAdd(realKey, index);
    }

    private boolean chainedAdd(Object realKey, int index)
    {
        if (this.table[index] instanceof ChainedBucket)
        {
            ChainedBucket bucket = (ChainedBucket) this.table[index];
            do
            {
                if (eq(bucket.zero, realKey))
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
                if (eq(bucket.one, realKey))
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
                if (eq(bucket.two, realKey))
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
                if (eq(bucket.three, realKey))
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
                        this.add((K) bucket.zero);
                    }
                    if (bucket.one != null)
                    {
                        this.add((K) bucket.one);
                    }
                    else
                    {
                        break;
                    }
                    if (bucket.two != null)
                    {
                        this.add((K) bucket.two);
                    }
                    else
                    {
                        break;
                    }
                    if (bucket.three != null)
                    {
                        if (bucket.three instanceof ChainedBucket)
                        {
                            bucket = (ChainedBucket) bucket.three;
                            continue;
                        }
                        this.add((K) bucket.three);
                    }
                    break;
                }
                while (true);
            }
            else if (oldKey != null)
            {
                this.add((K) oldKey);
            }
        }
    }

    public boolean contains(Object key)
    {
        key = toSentinelIfNull(key);
        int index = this.index(key);

        Object cur = this.table[index];
        return cur != null && (eq(cur, key)
                || cur instanceof ChainedBucket && this.chainContains((ChainedBucket) cur, key));
    }

    private boolean chainContains(ChainedBucket bucket, Object key)
    {
        do
        {
            if (eq(bucket.zero, key))
            {
                return true;
            }
            if (bucket.one == null)
            {
                return false;
            }
            if (eq(bucket.one, key))
            {
                return true;
            }
            if (bucket.two == null)
            {
                return false;
            }
            if (eq(bucket.two, key))
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
            return eq(bucket.three, key);
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
            Object key = this.table[i];
            if (key instanceof ChainedBucket)
            {
                this.chainedForEach((ChainedBucket) key, procedure);
            }
            else if (key != null)
            {
                procedure.value(this.nonSentinel(key));
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
            Object key = this.table[i];
            if (key instanceof ChainedBucket)
            {
                this.chainedForEachWith((ChainedBucket) key, procedure, parameter);
            }
            else if (key != null)
            {
                procedure.value(this.nonSentinel(key), parameter);
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
            Object key = this.table[i];
            if (key instanceof ChainedBucket)
            {
                count = this.chainedForEachWithIndex((ChainedBucket) key, objectIntProcedure, count);
            }
            else if (key != null)
            {
                objectIntProcedure.value(this.nonSentinel(key), count++);
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

    public UnifiedSet<K> filter(Predicate<? super K> predicate)
    {
        return this.filter(predicate, UnifiedSet.<K>newSet());
    }

    public <R extends Collection<K>> R filter(Predicate<? super K> predicate, R target)
    {
        this.forEach(new SelectProcedure<K>(predicate, target));
        return target;
    }

    public <P> UnifiedSet<K> filterWith(
            Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        return this.filterWith(predicate, parameter, UnifiedSet.<K>newSet());
    }

    public <P, R extends Collection<K>> R filterWith(
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

    public UnifiedSet<K> filterNot(Predicate<? super K> predicate)
    {
        return this.filterNot(predicate, UnifiedSet.<K>newSet());
    }

    public <R extends Collection<K>> R filterNot(Predicate<? super K> predicate, R target)
    {
        this.forEach(new FilterNotProcedure<K>(predicate, target));
        return target;
    }

    public <P> UnifiedSet<K> filterNotWith(
            Predicate2<? super K, ? super P> predicate,
            P parameter)
    {
        return this.filterNotWith(predicate, parameter, UnifiedSet.<K>newSet());
    }

    public <P, R extends Collection<K>> R filterNotWith(
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

    public <P> Twin<MutableList<K>> partitionWith(
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
        return PartitionUnifiedSet.of(this, predicate);
    }

    public void removeIf(Predicate<? super K> predicate)
    {
        IterableIterate.removeIf(this, predicate);
    }

    public <P> void removeIfWith(Predicate2<? super K, ? super P> predicate, P parameter)
    {
        IterableIterate.removeIfWith(this, predicate, parameter);
    }

    public <V> UnifiedSet<V> transform(Function<? super K, ? extends V> function)
    {
        return this.transform(function, UnifiedSet.<V>newSet());
    }

    public <V, R extends Collection<V>> R transform(Function<? super K, ? extends V> function, R target)
    {
        this.forEach(new CollectProcedure<K, V>(function, target));
        return target;
    }

    public <V> UnifiedSet<V> flatTransform(Function<? super K, ? extends Iterable<V>> function)
    {
        return this.flatTransform(function, UnifiedSet.<V>newSet());
    }

    public <V, R extends Collection<V>> R flatTransform(
            Function<? super K, ? extends Iterable<V>> function, R target)
    {
        this.forEach(new FlatCollectProcedure<K, V>(function, target));
        return target;
    }

    public <P, A> UnifiedSet<A> transformWith(Function2<? super K, ? super P, ? extends A> function, P parameter)
    {
        return this.transformWith(function, parameter, UnifiedSet.<A>newSet());
    }

    public <P, A, R extends Collection<A>> R transformWith(
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

    public <V> UnifiedSet<V> transformIf(
            Predicate<? super K> predicate, Function<? super K, ? extends V> function)
    {
        return this.transformIf(predicate, function, UnifiedSet.<V>newSet());
    }

    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super K> predicate, Function<? super K, ? extends V> function, R target)
    {
        this.forEach(new CollectIfProcedure<K, V>(target, function, predicate));
        return target;
    }

    public K find(Predicate<? super K> predicate)
    {
        return IterableIterate.find(this, predicate);
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
        return Iterate.min(this, Comparators.byFunction(function));
    }

    public <V extends Comparable<? super V>> K maxBy(Function<? super K, ? extends V> function)
    {
        return Iterate.max(this, Comparators.byFunction(function));
    }

    public K findIfNone(Predicate<? super K> predicate, Function0<? extends K> function)
    {
        K result = this.find(predicate);
        return result == null ? function.value() : result;
    }

    public <P> K findWith(Predicate2<? super K, ? super P> predicate, P parameter)
    {
        return IterableIterate.findWith(this, predicate, parameter);
    }

    public <P> K findWithIfNone(
            Predicate2<? super K, ? super P> predicate,
            P parameter,
            Function0<? extends K> function)
    {
        K result = this.findWith(predicate, parameter);
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

    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super K, ? extends IV> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super K> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super K> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super K> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public <IV, P> IV foldLeftWith(
            IV initialValue,
            Function3<? super IV, ? super K, ? super P, ? extends IV> function,
            P parameter)
    {
        return IterableIterate.foldLeftWith(initialValue, this, function, parameter);
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
        return newSet(this);
    }

    public MutableBag<K> toBag()
    {
        return HashBag.newBag(this);
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super K, ? extends NK> keyFunction,
            Function<? super K, ? extends NV> valueFunction)
    {
        return UnifiedMap.<NK, NV>newMap(this.size()).transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super K, ? extends NK> keyFunction,
            Function<? super K, ? extends NV> valueFunction)
    {
        return TreeSortedMap.<NK, NV>newMap().transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super K, ? extends NK> keyFunction,
            Function<? super K, ? extends NV> valueFunction)
    {
        return TreeSortedMap.<NK, NV>newMap(comparator).transformKeysAndValues(this, keyFunction, valueFunction);
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
        Iterate.forEach(iterable, CollectionAddProcedure.on(this));
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
            Object key = unifiedset.table[i];
            if (key instanceof ChainedBucket)
            {
                changed |= this.copyChain((ChainedBucket) key);
            }
            else if (key != null)
            {
                changed |= this.add((K) key);
            }
        }
        return changed;
    }

    private boolean copyChain(ChainedBucket bucket)
    {
        boolean changed = false;
        do
        {
            changed |= this.add((K) bucket.zero);
            if (bucket.one == null)
            {
                return changed;
            }
            changed |= this.add((K) bucket.one);
            if (bucket.two == null)
            {
                return changed;
            }
            changed |= this.add((K) bucket.two);
            if (bucket.three == null)
            {
                return changed;
            }
            if (bucket.three instanceof ChainedBucket)
            {
                bucket = (ChainedBucket) bucket.three;
                continue;
            }
            changed |= this.add((K) bucket.three);
            return changed;
        }
        while (true);
    }

    public boolean remove(Object key)
    {
        key = toSentinelIfNull(key);
        int index = this.index(key);

        Object cur = this.table[index];
        if (cur != null)
        {
            if (eq(cur, key))
            {
                this.table[index] = null;
                this.occupied--;
                return true;
            }
            if (cur instanceof ChainedBucket)
            {
                return this.removeFromChain((ChainedBucket) cur, key, index);
            }
        }
        return false;
    }

    private boolean removeFromChain(ChainedBucket bucket, Object key, int index)
    {
        if (eq(bucket.zero, key))
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
        if (eq(bucket.one, key))
        {
            bucket.one = bucket.removeLast(1);
            this.occupied--;
            return true;
        }
        if (bucket.two == null)
        {
            return false;
        }
        if (eq(bucket.two, key))
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
        if (eq(bucket.three, key))
        {
            bucket.three = bucket.removeLast(3);
            this.occupied--;
            return true;
        }
        return false;
    }

    private boolean removeDeepChain(ChainedBucket oldBucket, Object key)
    {
        do
        {
            ChainedBucket bucket = (ChainedBucket) oldBucket.three;
            if (eq(bucket.zero, key))
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
            if (eq(bucket.one, key))
            {
                bucket.one = bucket.removeLast(1);
                this.occupied--;
                return true;
            }
            if (bucket.two == null)
            {
                return false;
            }
            if (eq(bucket.two, key))
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
            if (eq(bucket.three, key))
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
        if (this.size() != other.size())
        {
            return false;
        }

        return Iterate.allSatisfy(other, Predicates.in(this));
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        for (int i = 0; i < this.table.length; i++)
        {
            Object key = this.table[i];
            if (key instanceof ChainedBucket)
            {
                hashCode += this.chainedHashCode((ChainedBucket) key);
            }
            else if (key != null)
            {
                hashCode += key == NULL_KEY ? 0 : key.hashCode();
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
                    if (o == NULL_KEY)
                    {
                        out.writeObject(null);
                    }
                    else
                    {
                        out.writeObject(o);
                    }
                }
            }
        }
    }

    private void writeExternalChain(ObjectOutput out, ChainedBucket bucket) throws IOException
    {
        do
        {
            if (bucket.zero == NULL_KEY)
            {
                out.writeObject(null);
            }
            else
            {
                out.writeObject(bucket.zero);
            }
            if (bucket.one == null)
            {
                return;
            }
            if (bucket.one == NULL_KEY)
            {
                out.writeObject(null);
            }
            else
            {
                out.writeObject(bucket.one);
            }
            if (bucket.two == null)
            {
                return;
            }
            if (bucket.two == NULL_KEY)
            {
                out.writeObject(null);
            }
            else
            {
                out.writeObject(bucket.two);
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
            if (bucket.three == NULL_KEY)
            {
                out.writeObject(null);
            }
            else
            {
                out.writeObject(bucket.three);
            }
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

    private void addIfFound(Object key, UnifiedSet<K> other)
    {
        key = toSentinelIfNull(key);
        int index = this.index(key);

        Object cur = this.table[index];
        if (cur != null)
        {
            if (eq(cur, key))
            {
                other.add(this.nonSentinel(cur));
                return;
            }
            if (cur instanceof ChainedBucket)
            {
                this.addIfFoundFromChain((ChainedBucket) cur, key, other);
            }
        }
    }

    private void addIfFoundFromChain(ChainedBucket bucket, Object key, UnifiedSet<K> other)
    {
        do
        {
            if (eq(bucket.zero, key))
            {
                other.add(this.nonSentinel(key));
                return;
            }
            if (bucket.one == null)
            {
                return;
            }
            if (eq(bucket.one, key))
            {
                other.add(this.nonSentinel(key));
                return;
            }
            if (bucket.two == null)
            {
                return;
            }
            if (eq(bucket.two, key))
            {
                other.add(this.nonSentinel(key));
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
            if (eq(bucket.three, key))
            {
                other.add(this.nonSentinel(key));
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
            this.addIfFound(key, retainedCopy);
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
        //todo: rezaem: turn iterator into a loop
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
            Object x = table[i];
            if (x != null)
            {
                if (x instanceof ChainedBucket)
                {
                    ChainedBucket bucket = (ChainedBucket) x;
                    count = this.copyBucketToArray(result, bucket, count);
                }
                else
                {
                    result[count++] = this.nonSentinel(x);
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
        protected K lastReturned;

        public boolean hasNext()
        {
            return this.count < UnifiedSet.this.size();
        }

        public void remove()
        {
            if (this.lastReturned == null)
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
            this.lastReturned = null;
        }

        protected void removeFromChain()
        {
            ChainedBucket chain = (ChainedBucket) UnifiedSet.this.table[this.position];
            chain.remove(--this.chainPosition);
            this.lastReturned = null;
        }

        protected void removeLastFromChain(ChainedBucket bucket, int tableIndex)
        {
            bucket.removeLast(0);
            if (bucket.zero == null)
            {
                UnifiedSet.this.table[tableIndex] = null;
            }
            this.lastReturned = null;
        }

        protected K nextFromChain()
        {
            ChainedBucket bucket = (ChainedBucket) UnifiedSet.this.table[this.position];
            Object key = bucket.get(this.chainPosition);
            this.chainPosition++;
            if (bucket.get(this.chainPosition) == null)
            {
                this.chainPosition = 0;
                this.position++;
            }
            this.lastReturned = (K) key;
            return UnifiedSet.this.nonSentinel(key);
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
            Object key = table[this.position];
            if (key instanceof ChainedBucket)
            {
                return this.nextFromChain();
            }
            this.position++;
            this.lastReturned = (K) key;
            return UnifiedSet.this.nonSentinel(key);
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
        return SetIterables.union(this, set);
    }

    public <R extends Set<K>> R unionInto(SetIterable<? extends K> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public MutableSet<K> intersect(SetIterable<? extends K> set)
    {
        return SetIterables.intersect(this, set);
    }

    public <R extends Set<K>> R intersectInto(SetIterable<? extends K> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public MutableSet<K> difference(SetIterable<? extends K> subtrahendSet)
    {
        return SetIterables.difference(this, subtrahendSet);
    }

    public <R extends Set<K>> R differenceInto(SetIterable<? extends K> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public MutableSet<K> symmetricDifference(SetIterable<? extends K> setB)
    {
        return SetIterables.symmetricDifference(this, setB);
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
        Object realKey = toSentinelIfNull(key);
        int index = this.index(realKey);
        Object cur = this.table[index];

        Object result = null;
        if (cur != null)
        {
            if (eq(cur, realKey))
            {
                result = cur;
            }
            else if (cur instanceof ChainedBucket)
            {
                result = this.chainedGet(realKey, (ChainedBucket) cur);
            }
        }
        return this.nonSentinel(result);
    }

    private Object chainedGet(Object realKey, ChainedBucket bucket)
    {
        do
        {
            if (eq(bucket.zero, realKey))
            {
                return bucket.zero;
            }
            if (bucket.one == null)
            {
                return null;
            }
            else if (eq(bucket.one, realKey))
            {
                return bucket.one;
            }
            if (bucket.two == null)
            {
                return null;
            }
            else if (eq(bucket.two, realKey))
            {
                return bucket.two;
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
            else if (eq(bucket.three, realKey))
            {
                return bucket.three;
            }
            return null;
        }
        while (true);
    }

    public K put(K key)
    {
        Object realKey = toSentinelIfNull(key);
        int index = this.index(realKey);
        Object cur = this.table[index];

        Object result;
        if (cur == null)
        {
            this.table[index] = realKey;
            if (++this.occupied > this.maxSize)
            {
                this.rehash();
            }
            result = key;
        }
        else
        {
            result = eq(cur, realKey) ? cur : this.chainedPut(realKey, index);
        }
        return this.nonSentinel(result);
    }

    private Object chainedPut(Object realKey, int index)
    {
        if (this.table[index] instanceof ChainedBucket)
        {
            ChainedBucket bucket = (ChainedBucket) this.table[index];
            do
            {
                if (eq(bucket.zero, realKey))
                {
                    return bucket.zero;
                }
                if (bucket.one == null)
                {
                    bucket.one = realKey;
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return realKey;
                }
                if (eq(bucket.one, realKey))
                {
                    return bucket.one;
                }
                if (bucket.two == null)
                {
                    bucket.two = realKey;
                    if (++this.occupied > this.maxSize)
                    {
                        this.rehash();
                    }
                    return realKey;
                }
                if (eq(bucket.two, realKey))
                {
                    return bucket.two;
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
                    return realKey;
                }
                if (eq(bucket.three, realKey))
                {
                    return bucket.three;
                }
                bucket.three = new ChainedBucket(bucket.three, realKey);
                if (++this.occupied > this.maxSize)
                {
                    this.rehash();
                }
                return realKey;
            }
            while (true);
        }
        ChainedBucket newBucket = new ChainedBucket(this.table[index], realKey);
        this.table[index] = newBucket;
        if (++this.occupied > this.maxSize)
        {
            this.rehash();
        }
        return realKey;
    }

    public K removeFromPool(K key)
    {
        Object realKey = toSentinelIfNull(key);
        int index = this.index(realKey);

        Object cur = this.table[index];
        if (cur != null)
        {
            if (eq(cur, realKey))
            {
                this.table[index] = null;
                this.occupied--;
                return this.nonSentinel(cur);
            }
            if (cur instanceof ChainedBucket)
            {
                return (K) this.removeFromChainForPool((ChainedBucket) cur, realKey, index);
            }
        }
        return null;
    }

    private Object removeFromChainForPool(ChainedBucket bucket, Object key, int index)
    {
        if (eq(bucket.zero, key))
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
        if (eq(bucket.one, key))
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
        if (eq(bucket.two, key))
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
        if (eq(bucket.three, key))
        {
            Object result = bucket.three;
            bucket.three = bucket.removeLast(3);
            this.occupied--;
            return this.nonSentinel(result);
        }
        return null;
    }

    private Object removeDeepChainForPool(ChainedBucket oldBucket, Object key)
    {
        do
        {
            ChainedBucket bucket = (ChainedBucket) oldBucket.three;
            if (eq(bucket.zero, key))
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
            if (eq(bucket.one, key))
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
            if (eq(bucket.two, key))
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
            if (eq(bucket.three, key))
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

    private static boolean eq(Object object1, Object object2)
    {
        return object1 == object2 || object1.equals(object2);
    }

    private K nonSentinel(Object element)
    {
        return element == NULL_KEY ? null : (K) element;
    }

    private static Object toSentinelIfNull(Object key)
    {
        if (key == null)
        {
            return NULL_KEY;
        }
        return key;
    }
}
