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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.BooleanObjectPredicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.BooleanObjectProcedure;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.primitive.BooleanObjectMap;
import com.gs.collections.api.map.primitive.MutableBooleanObjectMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.block.procedure.SelectInstancesOfProcedure;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

public class BooleanObjectHashMap<V> implements MutableBooleanObjectMap<V>
{
    private boolean containsTrueKey;
    private boolean containsFalseKey;

    private V trueValue;
    private V falseValue;

    public static <V> BooleanObjectHashMap<V> newMap()
    {
        return new BooleanObjectHashMap<V>();
    }

    public static <V> BooleanObjectHashMap<V> newWithKeysValues(boolean key, V value)
    {
        return new BooleanObjectHashMap<V>().withKeyValue(key, value);
    }

    public static <V> BooleanObjectHashMap<V> newWithKeysValues(boolean key1, V value1, boolean key2, V value2)
    {
        return new BooleanObjectHashMap<V>().withKeysValues(key1, value1, key2, value2);
    }

    public V put(boolean key, V value)
    {
        if (key)
        {
            V oldValue = this.trueValue;
            this.containsTrueKey = true;
            this.trueValue = value;
            return oldValue;
        }
        V oldValue = this.falseValue;
        this.containsFalseKey = true;
        this.falseValue = value;
        return oldValue;
    }

    public V get(boolean key)
    {
        return key ? this.trueValue : this.falseValue;
    }

    public V getIfAbsentPut(boolean key, Function0<? extends V> function)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                return this.trueValue;
            }
            this.containsTrueKey = true;
            this.trueValue = function.value();
            return this.trueValue;
        }
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        this.containsFalseKey = true;
        this.falseValue = function.value();
        return this.falseValue;
    }

    public <P> V getIfAbsentPutWith(boolean key, Function<? super P, ? extends V> function, P parameter)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                return this.trueValue;
            }
            this.containsTrueKey = true;
            this.trueValue = function.valueOf(parameter);
            return this.trueValue;
        }
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        this.containsFalseKey = true;
        this.falseValue = function.valueOf(parameter);
        return this.falseValue;
    }

    public V getIfAbsentPutWithKey(boolean key, BooleanToObjectFunction<? extends V> function)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                return this.trueValue;
            }
            this.containsTrueKey = true;
            this.trueValue = function.valueOf(true);
            return this.trueValue;
        }
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        this.containsFalseKey = true;
        this.falseValue = function.valueOf(false);
        return this.falseValue;
    }

    public void clear()
    {
        this.containsFalseKey = false;
        this.containsTrueKey = false;
        this.falseValue = null;
        this.trueValue = null;
    }

    public V removeKey(boolean key)
    {
        if (key)
        {
            V oldValue = this.trueValue;
            this.containsTrueKey = false;
            this.trueValue = null;
            return oldValue;
        }
        V oldValue = this.falseValue;
        this.containsFalseKey = false;
        this.falseValue = null;
        return oldValue;
    }

    public boolean containsKey(boolean key)
    {
        return key ? this.containsTrueKey : this.containsFalseKey;
    }

    public boolean containsValue(Object value)
    {
        return (this.containsFalseKey && nullSafeEquals(this.falseValue, value))
                || (this.containsTrueKey && nullSafeEquals(this.trueValue, value));
    }

    public V updateValue(boolean key, Function0<? extends V> factory, Function<? super V, ? extends V> function)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                this.trueValue = function.valueOf(this.trueValue);
            }
            else
            {
                this.containsTrueKey = true;
                this.trueValue = function.valueOf(factory.value());
            }
            return this.trueValue;
        }

        if (this.containsFalseKey)
        {
            this.falseValue = function.valueOf(this.falseValue);
        }
        else
        {
            this.containsFalseKey = true;
            this.falseValue = function.valueOf(factory.value());
        }
        return this.falseValue;
    }

    public <P> V updateValueWith(boolean key, Function0<? extends V> factory, Function2<? super V, ? super P, ? extends V> function, P parameter)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                this.trueValue = function.value(this.trueValue, parameter);
            }
            else
            {
                this.containsTrueKey = true;
                this.trueValue = function.value(factory.value(), parameter);
            }
            return this.trueValue;
        }

        if (this.containsFalseKey)
        {
            this.falseValue = function.value(this.falseValue, parameter);
        }
        else
        {
            this.containsFalseKey = true;
            this.falseValue = function.value(factory.value(), parameter);
        }
        return this.falseValue;
    }

    public BooleanObjectHashMap<V> select(BooleanObjectPredicate<? super V> predicate)
    {
        BooleanObjectHashMap<V> result = BooleanObjectHashMap.newMap();
        if (this.containsFalseKey && predicate.accept(false, this.falseValue))
        {
            result.put(false, this.falseValue);
        }
        if (this.containsTrueKey && predicate.accept(true, this.trueValue))
        {
            result.put(true, this.trueValue);
        }
        return result;
    }

    public BooleanObjectHashMap<V> reject(BooleanObjectPredicate<? super V> predicate)
    {
        BooleanObjectHashMap<V> result = BooleanObjectHashMap.newMap();
        if (this.containsFalseKey && !predicate.accept(false, this.falseValue))
        {
            result.put(false, this.falseValue);
        }
        if (this.containsTrueKey && !predicate.accept(true, this.trueValue))
        {
            result.put(true, this.trueValue);
        }
        return result;
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        if (this.containsFalseKey)
        {
            procedure.value(this.falseValue);
        }
        if (this.containsTrueKey)
        {
            procedure.value(this.trueValue);
        }
    }

    public void forEachKey(BooleanProcedure procedure)
    {
        if (this.containsFalseKey)
        {
            procedure.value(false);
        }
        if (this.containsTrueKey)
        {
            procedure.value(true);
        }
    }

    public void forEachKeyValue(BooleanObjectProcedure<? super V> procedure)
    {
        if (this.containsFalseKey)
        {
            procedure.value(false, this.falseValue);
        }
        if (this.containsTrueKey)
        {
            procedure.value(true, this.trueValue);
        }
    }

    public int size()
    {
        return (this.containsTrueKey ? 1 : 0) + (this.containsFalseKey ? 1 : 0);
    }

    public boolean isEmpty()
    {
        return !this.containsTrueKey && !this.containsFalseKey;
    }

    public boolean notEmpty()
    {
        return this.containsTrueKey || this.containsFalseKey;
    }

    public V getFirst()
    {
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        if (this.containsTrueKey)
        {
            return this.trueValue;
        }
        return null;
    }

    public V getLast()
    {
        if (this.containsTrueKey)
        {
            return this.trueValue;
        }
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        return null;
    }

    public boolean contains(Object object)
    {
        return this.containsValue(object);
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        for (Object item : source)
        {
            if (!this.contains(item))
            {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(Collection<?> source)
    {
        return this.containsAllIterable(source);
    }

    public boolean containsAllArguments(Object... elements)
    {
        for (Object item : elements)
        {
            if (!this.contains(item))
            {
                return false;
            }
        }
        return true;
    }

    public MutableCollection<V> select(Predicate<? super V> predicate)
    {
        MutableList<V> result = FastList.newList();
        if (this.containsFalseKey && predicate.accept(this.falseValue))
        {
            result.add(this.falseValue);
        }
        if (this.containsTrueKey && predicate.accept(this.trueValue))
        {
            result.add(this.trueValue);
        }
        return result;
    }

    public <R extends Collection<V>> R select(Predicate<? super V> predicate, R target)
    {
        if (this.containsFalseKey && predicate.accept(this.falseValue))
        {
            target.add(this.falseValue);
        }
        if (this.containsTrueKey && predicate.accept(this.trueValue))
        {
            target.add(this.trueValue);
        }
        return target;
    }

    public <P, R extends Collection<V>> R selectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        if (this.containsFalseKey && predicate.accept(this.falseValue, parameter))
        {
            targetCollection.add(this.falseValue);
        }
        if (this.containsTrueKey && predicate.accept(this.trueValue, parameter))
        {
            targetCollection.add(this.trueValue);
        }
        return targetCollection;
    }

    public MutableCollection<V> reject(Predicate<? super V> predicate)
    {
        MutableList<V> result = FastList.newList();
        if (this.containsFalseKey && !predicate.accept(this.falseValue))
        {
            result.add(this.falseValue);
        }
        if (this.containsTrueKey && !predicate.accept(this.trueValue))
        {
            result.add(this.trueValue);
        }
        return result;
    }

    public <R extends Collection<V>> R reject(Predicate<? super V> predicate, R target)
    {
        if (this.containsFalseKey && !predicate.accept(this.falseValue))
        {
            target.add(this.falseValue);
        }
        if (this.containsTrueKey && !predicate.accept(this.trueValue))
        {
            target.add(this.trueValue);
        }
        return target;
    }

    public <P, R extends Collection<V>> R rejectWith(Predicate2<? super V, ? super P> predicate, P parameter, R targetCollection)
    {
        if (this.containsFalseKey && !predicate.accept(this.falseValue, parameter))
        {
            targetCollection.add(this.falseValue);
        }
        if (this.containsTrueKey && !predicate.accept(this.trueValue, parameter))
        {
            targetCollection.add(this.trueValue);
        }
        return targetCollection;
    }

    public PartitionMutableCollection<V> partition(Predicate<? super V> predicate)
    {
        PartitionMutableList<V> partitionMutableList = new PartitionFastList<V>();
        this.forEach(new PartitionProcedure<V>(predicate, partitionMutableList));
        return partitionMutableList;
    }

    public <S> MutableCollection<S> selectInstancesOf(Class<S> clazz)
    {
        FastList<S> result = FastList.newList(this.size());
        this.forEach(new SelectInstancesOfProcedure<S>(clazz, result));
        result.trimToSize();
        return result;
    }

    public <V1> MutableList<V1> collect(Function<? super V, ? extends V1> function)
    {
        return this.collect(function, FastList.<V1>newList(this.size()));
    }

    public <V1> MutableList<V1> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends V1> function)
    {
        return this.collectIf(predicate, function, FastList.<V1>newList());
    }

    public <V1> MutableList<V1> flatCollect(Function<? super V, ? extends Iterable<V1>> function)
    {
        return this.flatCollect(function, FastList.<V1>newList());
    }

    public <V1, R extends Collection<V1>> R flatCollect(Function<? super V, ? extends Iterable<V1>> function, R target)
    {
        if (this.containsFalseKey)
        {
            Iterate.addAllTo(function.valueOf(this.falseValue), target);
        }
        if (this.containsTrueKey)
        {
            Iterate.addAllTo(function.valueOf(this.trueValue), target);
        }
        return target;
    }

    public V detect(Predicate<? super V> predicate)
    {
        if (this.containsFalseKey && predicate.accept(this.falseValue))
        {
            return this.falseValue;
        }
        if (this.containsTrueKey && predicate.accept(this.trueValue))
        {
            return this.trueValue;
        }
        return null;
    }

    public V detectIfNone(Predicate<? super V> predicate, Function0<? extends V> function)
    {
        if (this.containsFalseKey && predicate.accept(this.falseValue))
        {
            return this.falseValue;
        }
        if (this.containsTrueKey && predicate.accept(this.trueValue))
        {
            return this.trueValue;
        }
        return function.value();
    }

    public int count(Predicate<? super V> predicate)
    {
        int count = 0;
        if (this.containsFalseKey && predicate.accept(this.falseValue))
        {
            count++;
        }
        if (this.containsTrueKey && predicate.accept(this.trueValue))
        {
            count++;
        }
        return count;
    }

    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        if (this.containsFalseKey && predicate.accept(this.falseValue))
        {
            return true;
        }
        return this.containsTrueKey && predicate.accept(this.trueValue);
    }

    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        if (this.containsFalseKey && !predicate.accept(this.falseValue))
        {
            return false;
        }
        return !(this.containsTrueKey && !predicate.accept(this.trueValue));
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super V, ? extends IV> function)
    {
        IV result = injectedValue;
        if (this.containsFalseKey)
        {
            result = function.value(result, this.falseValue);
        }
        if (this.containsTrueKey)
        {
            result = function.value(result, this.trueValue);
        }
        return result;
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super V> function)
    {
        int result = injectedValue;
        if (this.containsFalseKey)
        {
            result = function.intValueOf(result, this.falseValue);
        }
        if (this.containsTrueKey)
        {
            result = function.intValueOf(result, this.trueValue);
        }
        return result;
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super V> function)
    {
        long result = injectedValue;
        if (this.containsFalseKey)
        {
            result = function.longValueOf(result, this.falseValue);
        }
        if (this.containsTrueKey)
        {
            result = function.longValueOf(result, this.trueValue);
        }
        return result;
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super V> function)
    {
        float result = injectedValue;
        if (this.containsFalseKey)
        {
            result = function.floatValueOf(result, this.falseValue);
        }
        if (this.containsTrueKey)
        {
            result = function.floatValueOf(result, this.trueValue);
        }
        return result;
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super V> function)
    {
        double result = injectedValue;
        if (this.containsFalseKey)
        {
            result = function.doubleValueOf(result, this.falseValue);
        }
        if (this.containsTrueKey)
        {
            result = function.doubleValueOf(result, this.trueValue);
        }
        return result;
    }

    public MutableList<V> toList()
    {
        MutableList<V> list = Lists.mutable.of();
        this.forEachWith(Procedures2.<V>addToCollection(), list);
        return list;
    }

    public MutableList<V> toSortedList()
    {
        return this.toList().sortThis();
    }

    public MutableList<V> toSortedList(Comparator<? super V> comparator)
    {
        return this.toList().sortThis(comparator);
    }

    public MutableSet<V> toSet()
    {
        MutableSet<V> set = UnifiedSet.newSet();
        this.forEachWith(Procedures2.<V>addToCollection(), set);
        return set;
    }

    public MutableSortedSet<V> toSortedSet()
    {
        MutableSortedSet<V> set = TreeSortedSet.newSet();
        this.forEachWith(Procedures2.<V>addToCollection(), set);
        return set;
    }

    public MutableSortedSet<V> toSortedSet(Comparator<? super V> comparator)
    {
        MutableSortedSet<V> set = TreeSortedSet.newSet(comparator);
        this.forEachWith(Procedures2.<V>addToCollection(), set);
        return set;
    }

    public <V1 extends Comparable<? super V1>> MutableSortedSet<V> toSortedSetBy(Function<? super V, ? extends V1> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public MutableBag<V> toBag()
    {
        MutableBag<V> bag = Bags.mutable.of();
        this.forEachWith(Procedures2.<V>addToCollection(), bag);
        return bag;
    }

    public <NK, NV> MutableMap<NK, NV> toMap(Function<? super V, ? extends NK> keyFunction, Function<? super V, ? extends NV> valueFunction)
    {
        UnifiedMap<NK, NV> map = UnifiedMap.newMap();
        this.forEach(new MapCollectProcedure<V, NK, NV>(map, keyFunction, valueFunction));
        return map;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Function<? super V, ? extends NK> keyFunction, Function<? super V, ? extends NV> valueFunction)
    {
        TreeSortedMap<NK, NV> sortedMap = TreeSortedMap.newMap();
        this.forEach(new MapCollectProcedure<V, NK, NV>(sortedMap, keyFunction, valueFunction));
        return sortedMap;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator, Function<? super V, ? extends NK> keyFunction, Function<? super V, ? extends NV> valueFunction)
    {
        TreeSortedMap<NK, NV> sortedMap = TreeSortedMap.newMap(comparator);
        this.forEach(new MapCollectProcedure<V, NK, NV>(sortedMap, keyFunction, valueFunction));
        return sortedMap;
    }

    public LazyIterable<V> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public Object[] toArray()
    {
        Object[] result = new Object[this.size()];
        int index = 0;

        if (this.containsFalseKey)
        {
            result[index] = this.falseValue;
            index++;
        }
        if (this.containsTrueKey)
        {
            result[index] = this.trueValue;
        }
        return result;
    }

    public <T> T[] toArray(T[] a)
    {
        int size = this.size();

        T[] result = a.length < size
                ? (T[]) Array.newInstance(a.getClass().getComponentType(), size)
                : a;

        int index = 0;

        if (this.containsFalseKey)
        {
            result[index] = (T) this.falseValue;
            index++;
        }
        if (this.containsTrueKey)
        {
            result[index] = (T) this.trueValue;
        }

        if (result.length > size)
        {
            result[size] = null;
        }
        return result;
    }

    public V min(Comparator<? super V> comparator)
    {
        if (this.isEmpty())
        {
            throw new NoSuchElementException();
        }

        if (this.containsFalseKey)
        {
            if (this.containsTrueKey)
            {
                return comparator.compare(this.falseValue, this.trueValue) <= 0 ? this.falseValue : this.trueValue;
            }
            return this.falseValue;
        }
        return this.trueValue;
    }

    public V max(Comparator<? super V> comparator)
    {
        if (this.isEmpty())
        {
            throw new NoSuchElementException();
        }

        if (this.containsFalseKey)
        {
            if (this.containsTrueKey)
            {
                return comparator.compare(this.falseValue, this.trueValue) >= 0 ? this.falseValue : this.trueValue;
            }
            return this.falseValue;
        }
        return this.trueValue;
    }

    public V min()
    {
        return this.min(Comparators.naturalOrder());
    }

    public V max()
    {
        return this.max(Comparators.naturalOrder());
    }

    public long sumOfInt(IntFunction<? super V> function)
    {
        long sum = 0L;

        if (this.containsFalseKey)
        {
            sum += function.intValueOf(this.falseValue);
        }
        if (this.containsTrueKey)
        {
            sum += function.intValueOf(this.trueValue);
        }
        return sum;
    }

    public double sumOfFloat(FloatFunction<? super V> function)
    {
        double sum = 0L;

        if (this.containsFalseKey)
        {
            sum += function.floatValueOf(this.falseValue);
        }
        if (this.containsTrueKey)
        {
            sum += function.floatValueOf(this.trueValue);
        }
        return sum;
    }

    public long sumOfLong(LongFunction<? super V> function)
    {
        long sum = 0L;

        if (this.containsFalseKey)
        {
            sum += function.longValueOf(this.falseValue);
        }
        if (this.containsTrueKey)
        {
            sum += function.longValueOf(this.trueValue);
        }
        return sum;
    }

    public double sumOfDouble(DoubleFunction<? super V> function)
    {
        double sum = 0L;

        if (this.containsFalseKey)
        {
            sum += function.doubleValueOf(this.falseValue);
        }
        if (this.containsTrueKey)
        {
            sum += function.doubleValueOf(this.trueValue);
        }
        return sum;
    }

    public <V1 extends Comparable<? super V1>> V maxBy(Function<? super V, ? extends V1> function)
    {
        if (this.isEmpty())
        {
            throw new NoSuchElementException();
        }

        if (this.containsFalseKey)
        {
            if (this.containsTrueKey)
            {
                return function.valueOf(this.falseValue).compareTo(function.valueOf(this.trueValue)) >= 0 ? this.falseValue : this.trueValue;
            }
            return this.falseValue;
        }
        return this.trueValue;
    }

    public <V1 extends Comparable<? super V1>> V minBy(Function<? super V, ? extends V1> function)
    {
        if (this.isEmpty())
        {
            throw new NoSuchElementException();
        }

        if (this.containsFalseKey)
        {
            if (this.containsTrueKey)
            {
                return function.valueOf(this.falseValue).compareTo(function.valueOf(this.trueValue)) <= 0 ? this.falseValue : this.trueValue;
            }
            return this.falseValue;
        }
        return this.trueValue;
    }

    public <V1 extends Comparable<? super V1>> MutableList<V> toSortedListBy(Function<? super V, ? extends V1> function)
    {
        return this.toList().sortThis(Comparators.byFunction(function));
    }

    public <V1, R extends Collection<V1>> R collectIf(Predicate<? super V> predicate, Function<? super V, ? extends V1> function, R target)
    {
        if (this.containsFalseKey && predicate.accept(this.falseValue))
        {
            target.add(function.valueOf(this.falseValue));
        }
        if (this.containsTrueKey && predicate.accept(this.trueValue))
        {
            target.add(function.valueOf(this.trueValue));
        }
        return target;
    }

    public <P, V1, R extends Collection<V1>> R collectWith(Function2<? super V, ? super P, ? extends V1> function, P parameter, R targetCollection)
    {
        if (this.containsFalseKey)
        {
            targetCollection.add(function.value(this.falseValue, parameter));
        }
        if (this.containsTrueKey)
        {
            targetCollection.add(function.value(this.trueValue, parameter));
        }
        return targetCollection;
    }

    public <V1, R extends Collection<V1>> R collect(Function<? super V, ? extends V1> function, R target)
    {
        if (this.containsFalseKey)
        {
            target.add(function.valueOf(this.falseValue));
        }
        if (this.containsTrueKey)
        {
            target.add(function.valueOf(this.trueValue));
        }
        return target;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof BooleanObjectMap))
        {
            return false;
        }

        BooleanObjectMap<V> other = (BooleanObjectMap<V>) obj;

        if (this.size() != other.size())
        {
            return false;
        }

        if (this.containsTrueKey && (!other.containsKey(true) || !nullSafeEquals(this.trueValue, other.get(true))))
        {
            return false;
        }

        if (this.containsFalseKey && (!other.containsKey(false) || !nullSafeEquals(this.falseValue, other.get(false))))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = 0;

        if (this.containsTrueKey)
        {
            result += 1231 ^ (this.trueValue == null ? 0 : this.trueValue.hashCode());
        }

        if (this.containsFalseKey)
        {
            result += 1237 ^ (this.falseValue == null ? 0 : this.falseValue.hashCode());
        }

        return result;
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public <S> MutableList<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<V, S>>newList());
    }

    public <S, R extends Collection<Pair<V, S>>> R zip(Iterable<S> that, R target)
    {
        return IterableIterate.zip(this, that, target);
    }

    public MutableList<Pair<V, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(FastList.<Pair<V, Integer>>newList());
    }

    public <R extends Collection<Pair<V, Integer>>> R zipWithIndex(R target)
    {
        return IterableIterate.zipWithIndex(this, target);
    }

    public MutableList<RichIterable<V>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        Iterator<V> iterator = this.iterator();
        MutableList<RichIterable<V>> result = Lists.mutable.of();
        while (iterator.hasNext())
        {
            MutableList<V> batch = FastList.newList();
            for (int i = 0; i < size && iterator.hasNext(); i++)
            {
                batch.add(iterator.next());
            }
            result.add(batch);
        }
        return result;
    }

    public <K, V1> MutableMap<K, V1> aggregateInPlaceBy(Function<? super V, ? extends K> groupBy, Function0<? extends V1> zeroValueFactory, Procedure2<? super V1, ? super V> mutatingAggregator)
    {
        MutableMap<K, V1> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<V, K, V1>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K, V1> MutableMap<K, V1> aggregateBy(Function<? super V, ? extends K> groupBy, Function0<? extends V1> zeroValueFactory, Function2<? super V1, ? super V, ? extends V1> nonMutatingAggregator)
    {
        MutableMap<K, V1> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<V, K, V1>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
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

            if (this.containsTrueKey)
            {
                appendable.append("true=").append(String.valueOf(this.trueValue));
            }
            if (this.containsFalseKey)
            {
                if (this.containsTrueKey)
                {
                    appendable.append(separator);
                }
                appendable.append("false=").append(String.valueOf(this.falseValue.toString()));
            }
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public <V1> MutableMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function)
    {
        return this.groupBy(function, FastListMultimap.<V1, V>newMultimap());
    }

    public <V1> MutableMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function)
    {
        return this.groupByEach(function, FastListMultimap.<V1, V>newMultimap());
    }

    public <V1, R extends MutableMultimap<V1, V>> R groupByEach(Function<? super V, ? extends Iterable<V1>> function, R target)
    {
        return IterableIterate.groupByEach(this, function, target);
    }

    public <V1, R extends MutableMultimap<V1, V>> R groupBy(Function<? super V, ? extends V1> function, R target)
    {
        if (this.containsFalseKey)
        {
            target.put(function.valueOf(this.falseValue), this.falseValue);
        }
        if (this.containsTrueKey)
        {
            target.put(function.valueOf(this.trueValue), this.trueValue);
        }
        return target;
    }

    public BooleanObjectHashMap<V> withKeyValue(boolean key, V value)
    {
        this.put(key, value);
        return this;
    }

    public MutableBooleanObjectMap<V> withoutKey(boolean key)
    {
        this.removeKey(key);
        return this;
    }

    public MutableBooleanObjectMap<V> withoutAllKeys(BooleanIterable keys)
    {
        BooleanIterator iterator = keys.booleanIterator();
        while (iterator.hasNext())
        {
            boolean item = iterator.next();
            this.removeKey(item);
        }
        return this;
    }

    public BooleanObjectHashMap<V> withKeysValues(boolean key1, V value1, boolean key2, V value2)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        return this;
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

    public void forEach(Procedure<? super V> procedure)
    {
        this.forEachValue(procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        int index = 0;
        if (this.containsFalseKey)
        {
            objectIntProcedure.value(this.falseValue, index++);
        }
        if (this.containsTrueKey)
        {
            objectIntProcedure.value(this.trueValue, index);
        }
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        if (this.containsFalseKey)
        {
            procedure.value(this.falseValue, parameter);
        }
        if (this.containsTrueKey)
        {
            procedure.value(this.trueValue, parameter);
        }
    }

    public Iterator<V> iterator()
    {
        return new InternalIterator();
    }

    private class InternalIterator implements Iterator<V>
    {
        private boolean currentKey;
        private boolean isCurrentKeySet;
        private boolean handledFalse;
        private boolean handledTrue;

        public boolean hasNext()
        {
            return !this.handledFalse && BooleanObjectHashMap.this.containsFalseKey || !this.handledTrue && BooleanObjectHashMap.this.containsTrueKey;
        }

        public V next()
        {
            if (!this.handledFalse && BooleanObjectHashMap.this.containsFalseKey)
            {
                this.currentKey = false;
                this.isCurrentKeySet = true;
                this.handledFalse = true;
                return BooleanObjectHashMap.this.falseValue;
            }
            if (!this.handledTrue && BooleanObjectHashMap.this.containsTrueKey)
            {
                this.currentKey = true;
                this.isCurrentKeySet = true;
                this.handledTrue = true;
                return BooleanObjectHashMap.this.trueValue;
            }
            throw new NoSuchElementException();
        }

        public void remove()
        {
            if (BooleanObjectHashMap.this.size() == 0)
            {
                throw new NoSuchElementException();
            }
            if (!this.isCurrentKeySet)
            {
                throw new IllegalStateException();
            }

            BooleanObjectHashMap.this.removeKey(this.currentKey);
            this.isCurrentKeySet = false;
        }
    }
}
