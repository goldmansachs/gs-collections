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

package com.gs.collections.impl.utility.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.block.procedure.FastListCollectIfProcedure;
import com.gs.collections.impl.block.procedure.FastListCollectProcedure;
import com.gs.collections.impl.block.procedure.FastListRejectProcedure;
import com.gs.collections.impl.block.procedure.FastListSelectProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;

public final class InternalArrayIterate
{
    private InternalArrayIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> boolean randomAccessListEquals(T[] array, int size, List<?> otherList)
    {
        if (size != otherList.size())
        {
            return false;
        }
        for (int i = 0; i < size; i++)
        {
            T one = array[i];
            Object two = otherList.get(i);
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean regularListEquals(T[] array, int size, List<?> otherList)
    {
        Iterator<?> iterator = otherList.iterator();
        for (int i = 0; i < size; i++)
        {
            T one = array[i];
            if (!iterator.hasNext())
            {
                return false;
            }
            Object two = iterator.next();
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return !iterator.hasNext();
    }

    public static <T> void forEachWithoutChecks(T[] objectArray, int from, int to, Procedure<? super T> procedure)
    {
        if (from <= to)
        {
            for (int i = from; i <= to; i++)
            {
                procedure.value(objectArray[i]);
            }
        }
        else
        {
            for (int i = from; i >= to; i--)
            {
                procedure.value(objectArray[i]);
            }
        }
    }

    public static <T> void forEachWithIndexWithoutChecks(T[] objectArray, int from, int to, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (from <= to)
        {
            for (int i = from; i <= to; i++)
            {
                objectIntProcedure.value(objectArray[i], i);
            }
        }
        else
        {
            for (int i = from; i >= to; i--)
            {
                objectIntProcedure.value(objectArray[i], i);
            }
        }
    }

    public static <T> void batchForEach(Procedure<? super T> procedure, T[] array, int size, int sectionIndex, int sectionCount)
    {
        int sectionSize = size / sectionCount;
        int start = sectionSize * sectionIndex;
        int end = sectionIndex == sectionCount - 1 ? size : start + sectionSize;
        if (procedure instanceof FastListSelectProcedure)
        {
            InternalArrayIterate.batchFastListSelect(array, start, end, (FastListSelectProcedure<T>) procedure);
        }
        else if (procedure instanceof FastListCollectProcedure)
        {
            InternalArrayIterate.batchFastListCollect(array, start, end, (FastListCollectProcedure<T, ?>) procedure);
        }
        else if (procedure instanceof FastListCollectIfProcedure)
        {
            InternalArrayIterate.batchFastListCollectIf(array, start, end, (FastListCollectIfProcedure<T, ?>) procedure);
        }
        else if (procedure instanceof CountProcedure)
        {
            InternalArrayIterate.batchCount(array, start, end, (CountProcedure<T>) procedure);
        }
        else if (procedure instanceof FastListRejectProcedure)
        {
            InternalArrayIterate.batchReject(array, start, end, (FastListRejectProcedure<T>) procedure);
        }
        else if (procedure instanceof MultimapPutProcedure)
        {
            InternalArrayIterate.batchGroupBy(array, start, end, (MultimapPutProcedure<?, T>) procedure);
        }
        else
        {
            for (int i = start; i < end; i++)
            {
                procedure.value(array[i]);
            }
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure.
     */
    private static <T> void batchGroupBy(T[] array, int start, int end, MultimapPutProcedure<?, T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(array[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure.
     */
    private static <T> void batchReject(T[] array, int start, int end, FastListRejectProcedure<T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(array[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure.
     */
    private static <T> void batchCount(T[] array, int start, int end, CountProcedure<T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(array[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure.
     */
    private static <T> void batchFastListCollectIf(T[] array, int start, int end, FastListCollectIfProcedure<T, ?> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(array[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure.
     */
    private static <T> void batchFastListCollect(T[] array, int start, int end, FastListCollectProcedure<T, ?> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(array[i]);
        }
    }

    /**
     * Implemented to avoid megamorphic call on castProcedure.
     */
    private static <T> void batchFastListSelect(T[] array, int start, int end, FastListSelectProcedure<T> castProcedure)
    {
        for (int i = start; i < end; i++)
        {
            castProcedure.value(array[i]);
        }
    }

    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(T[] array, int size, Function<? super T, ? extends V> function, R target)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            target.put(function.valueOf(item), item);
        }
        return target;
    }

    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            T[] array,
            int size,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            Iterable<V> iterable = function.valueOf(item);
            for (V key : iterable)
            {
                target.put(key, item);
            }
        }
        return target;
    }

    public static <T, K, R extends MutableMap<K, T>> R groupByUniqueKey(
            T[] array,
            int size,
            Function<? super T, ? extends K> function,
            R target)
    {
        for (int i = 0; i < size; i++)
        {
            T value = array[i];
            K key = function.valueOf(value);
            if (target.put(key, value) != null)
            {
                throw new IllegalStateException("Key " + key + " already exists in map!");
            }
        }
        return target;
    }

    public static <T> PartitionFastList<T> partition(T[] array, int size, Predicate<? super T> predicate)
    {
        PartitionFastList<T> partitionFastList = new PartitionFastList<T>();

        for (int i = 0; i < size; i++)
        {
            T each = array[i];
            MutableList<T> bucket = predicate.accept(each) ? partitionFastList.getSelected() : partitionFastList.getRejected();
            bucket.add(each);
        }
        return partitionFastList;
    }

    public static <T, P> PartitionFastList<T> partitionWith(T[] array, int size, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        PartitionFastList<T> partitionFastList = new PartitionFastList<T>();

        for (int i = 0; i < size; i++)
        {
            T each = array[i];
            MutableList<T> bucket = predicate.accept(each, parameter) ? partitionFastList.getSelected() : partitionFastList.getRejected();
            bucket.add(each);
        }
        return partitionFastList;
    }

    /**
     * @see Iterate#selectAndRejectWith(Iterable, Predicate2, Object)
     * @deprecated since 6.0 use {@link RichIterable#partitionWith(Predicate2, Object)} instead.
     */
    @Deprecated
    public static <T, P> Twin<MutableList<T>> selectAndRejectWith(T[] objectArray, int size, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        MutableList<T> positiveResult = Lists.mutable.of();
        MutableList<T> negativeResult = Lists.mutable.of();
        for (int i = 0; i < size; i++)
        {
            T each = objectArray[i];
            (predicate.accept(each, parameter) ? positiveResult : negativeResult).add(each);
        }
        return Tuples.twin(positiveResult, negativeResult);
    }

    public static int indexOf(Object[] array, int size, Object object)
    {
        for (int i = 0; i < size; i++)
        {
            if (Comparators.nullSafeEquals(array[i], object))
            {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(Object[] array, int size, Object object)
    {
        for (int i = size - 1; i >= 0; i--)
        {
            if (Comparators.nullSafeEquals(array[i], object))
            {
                return i;
            }
        }
        return -1;
    }

    public static <T, R extends Collection<T>> R select(T[] array, int size, Predicate<? super T> predicate, R target)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            if (predicate.accept(item))
            {
                target.add(item);
            }
        }
        return target;
    }

    public static <T, P, R extends Collection<T>> R selectWith(
            T[] array,
            int size,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            if (predicate.accept(item, parameter))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    public static <T, R extends Collection<T>> R reject(T[] array, int size, Predicate<? super T> predicate, R target)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            if (!predicate.accept(item))
            {
                target.add(item);
            }
        }
        return target;
    }

    public static <T, P, R extends Collection<T>> R rejectWith(
            T[] array,
            int size,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R target)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            if (!predicate.accept(item, parameter))
            {
                target.add(item);
            }
        }
        return target;
    }

    public static <T> FastList<T> selectInstancesOf(Object[] array, int size, Class<T> clazz)
    {
        FastList<T> results = FastList.newList(size);
        for (int i = 0; i < size; i++)
        {
            Object each = array[i];
            if (clazz.isInstance(each))
            {
                results.add((T) each);
            }
        }
        return results;
    }

    public static <T, V, R extends Collection<V>> R collect(
            T[] array,
            int size,
            Function<? super T, ? extends V> function,
            R target)
    {
        for (int i = 0; i < size; i++)
        {
            target.add(function.valueOf(array[i]));
        }
        return target;
    }

    public static <T, V, R extends Collection<V>> R flatCollect(
            T[] array,
            int size,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        for (int i = 0; i < size; i++)
        {
            Iterate.addAllTo(function.valueOf(array[i]), target);
        }
        return target;
    }

    public static <T, P, V, R extends Collection<V>> R collectWith(
            T[] array,
            int size,
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection)
    {
        for (int i = 0; i < size; i++)
        {
            targetCollection.add(function.value(array[i], parameter));
        }
        return targetCollection;
    }

    public static <T, V, R extends Collection<V>> R collectIf(
            T[] array,
            int size,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            if (predicate.accept(item))
            {
                target.add(function.valueOf(item));
            }
        }
        return target;
    }

    public static <T> T detect(T[] array, int size, Predicate<? super T> predicate)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            if (predicate.accept(item))
            {
                return item;
            }
        }
        return null;
    }

    public static <T, P> T detectWith(T[] array, int size, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < size; i++)
        {
            T item = array[i];
            if (predicate.accept(item, parameter))
            {
                return item;
            }
        }
        return null;
    }

    public static <T> T min(T[] array, int size, Comparator<? super T> comparator)
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }

        T min = array[0];
        for (int i = 1; i < size; i++)
        {
            T item = array[i];
            if (comparator.compare(item, min) < 0)
            {
                min = item;
            }
        }
        return min;
    }

    public static <T> T max(T[] array, int size, Comparator<? super T> comparator)
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }

        T max = array[0];
        for (int i = 1; i < size; i++)
        {
            T item = array[i];
            if (comparator.compare(item, max) > 0)
            {
                max = item;
            }
        }
        return max;
    }

    public static <T> T min(T[] array, int size)
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }

        T min = array[0];
        for (int i = 1; i < size; i++)
        {
            T item = array[i];
            if (((Comparable<? super T>) item).compareTo(min) < 0)
            {
                min = item;
            }
        }
        return min;
    }

    public static <T> T max(T[] array, int size)
    {
        if (size == 0)
        {
            throw new NoSuchElementException();
        }

        T max = array[0];
        for (int i = 1; i < size; i++)
        {
            T item = array[i];
            if (((Comparable<T>) item).compareTo(max) > 0)
            {
                max = item;
            }
        }
        return max;
    }

    public static <T, V extends Comparable<? super V>> T minBy(T[] array, int size, Function<? super T, ? extends V> function)
    {
        if (ArrayIterate.isEmpty(array))
        {
            throw new NoSuchElementException();
        }

        T min = array[0];
        V minValue = function.valueOf(min);
        for (int i = 1; i < size; i++)
        {
            T next = array[i];
            V nextValue = function.valueOf(next);
            if (nextValue.compareTo(minValue) < 0)
            {
                min = next;
                minValue = nextValue;
            }
        }
        return min;
    }

    public static <T, V extends Comparable<? super V>> T maxBy(T[] array, int size, Function<? super T, ? extends V> function)
    {
        if (ArrayIterate.isEmpty(array))
        {
            throw new NoSuchElementException();
        }

        T max = array[0];
        V maxValue = function.valueOf(max);
        for (int i = 1; i < size; i++)
        {
            T next = array[i];
            V nextValue = function.valueOf(next);
            if (nextValue.compareTo(maxValue) > 0)
            {
                max = next;
                maxValue = nextValue;
            }
        }
        return max;
    }

    public static <T> int count(T[] array, int size, Predicate<? super T> predicate)
    {
        int count = 0;
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(array[i]))
            {
                count++;
            }
        }
        return count;
    }

    public static <T, P> int countWith(T[] array, int size, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        int count = 0;
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(array[i], parameter))
            {
                count++;
            }
        }
        return count;
    }

    public static <T> boolean anySatisfy(T[] array, int size, Predicate<? super T> predicate)
    {
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(array[i]))
            {
                return true;
            }
        }
        return false;
    }

    public static <T, P> boolean anySatisfyWith(T[] array, int size, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(array[i], parameter))
            {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean allSatisfy(T[] array, int size, Predicate<? super T> predicate)
    {
        for (int i = 0; i < size; i++)
        {
            if (!predicate.accept(array[i]))
            {
                return false;
            }
        }
        return true;
    }

    public static <T, P> boolean allSatisfyWith(T[] array, int size, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < size; i++)
        {
            if (!predicate.accept(array[i], parameter))
            {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean noneSatisfy(T[] array, int size, Predicate<? super T> predicate)
    {
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(array[i]))
            {
                return false;
            }
        }
        return true;
    }

    public static <T, P> boolean noneSatisfyWith(T[] array, int size, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(array[i], parameter))
            {
                return false;
            }
        }
        return true;
    }

    public static <T> void appendString(
            ListIterable<T> iterable,
            T[] array,
            int size,
            Appendable appendable,
            String start,
            String separator,
            String end)
    {
        try
        {
            appendable.append(start);

            if (size > 0)
            {
                appendable.append(IterableIterate.stringValueOfItem(iterable, array[0]));

                for (int i = 1; i < size; i++)
                {
                    appendable.append(separator);
                    appendable.append(IterableIterate.stringValueOfItem(iterable, array[i]));
                }
            }

            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T> int detectIndex(T[] objectArray, int size, Predicate<? super T> predicate)
    {
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(objectArray[i]))
            {
                return i;
            }
        }
        return -1;
    }

    public static <T> void forEachWithIndex(T[] objectArray, int size, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        for (int i = 0; i < size; i++)
        {
            objectIntProcedure.value(objectArray[i], i);
        }
    }

    public static <T, R extends Collection<T>> R distinct(T[] objectArray, int size, R targetCollection)
    {
        MutableSet<T> seenSoFar = UnifiedSet.newSet();

        for (int i = 0; i < size; i++)
        {
            T each = objectArray[i];
            if (seenSoFar.add(each))
            {
                targetCollection.add(each);
            }
        }
        return targetCollection;
    }
}
