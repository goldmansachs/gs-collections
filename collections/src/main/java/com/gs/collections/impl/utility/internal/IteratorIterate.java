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

package com.gs.collections.impl.utility.internal;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
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
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;

/**
 * The IteratorIterate class provides a few of the methods from the Smalltalk Collection Protocol for use with general
 * Collection classes. This includes do:, select:, reject:, collect:, inject:into:, detect:, detect:ifNone:, anySatisfy:
 * and allSatisfy:
 */
public final class IteratorIterate
{
    private IteratorIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T, IV> Twin<MutableList<T>> selectAndRejectWith(
            Iterator<T> iterator,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        MutableList<T> positiveResult = Lists.mutable.of();
        MutableList<T> negativeResult = Lists.mutable.of();
        while (iterator.hasNext())
        {
            T item = iterator.next();
            (predicate.accept(item, injectedValue) ? positiveResult : negativeResult).add(item);
        }
        return Tuples.twin(positiveResult, negativeResult);
    }

    public static <T> PartitionMutableList<T> partition(
            Iterator<T> iterator,
            Predicate<? super T> predicate)
    {
        PartitionMutableList<T> partitionFastList = new PartitionFastList<T>(predicate);
        while (iterator.hasNext())
        {
            partitionFastList.add(iterator.next());
        }
        return partitionFastList;
    }

    public static <T> int count(Iterator<T> iterator, Predicate<? super T> predicate)
    {
        int count = 0;
        while (iterator.hasNext())
        {
            if (predicate.accept(iterator.next()))
            {
                count++;
            }
        }
        return count;
    }

    public static <T, IV> int countWith(
            Iterator<T> iterator,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int count = 0;
        while (iterator.hasNext())
        {
            if (predicate.accept(iterator.next(), injectedValue))
            {
                count++;
            }
        }
        return count;
    }

    public static <T, R extends Collection<T>> R select(
            Iterator<T> iterator,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (predicate.accept(item))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    public static <T, P, R extends Collection<T>> R selectWith(
            Iterator<T> iterator,
            Predicate2<? super T, ? super P> predicate,
            P injectedValue,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (predicate.accept(item, injectedValue))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    public static <T, V, R extends Collection<V>> R collectIf(
            Iterator<T> iterator,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (predicate.accept(item))
            {
                targetCollection.add(function.valueOf(item));
            }
        }
        return targetCollection;
    }

    public static <T, R extends Collection<T>> R reject(
            Iterator<T> iterator,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (!predicate.accept(item))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    public static <T, P, R extends Collection<T>> R rejectWith(
            Iterator<T> iterator,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (!predicate.accept(item, parameter))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    public static <T, V, R extends Collection<V>> R collect(
            Iterator<T> iterator,
            Function<? super T, ? extends V> function,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            targetCollection.add(function.valueOf(iterator.next()));
        }
        return targetCollection;
    }

    public static <T, V, R extends Collection<V>> R flatCollect(
            Iterator<T> iterator,
            Function<? super T, ? extends Iterable<V>> function,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            Iterate.addAllTo(function.valueOf(iterator.next()), targetCollection);
        }
        return targetCollection;
    }

    public static <T> void forEach(Iterator<T> iterator, Procedure<? super T> procedure)
    {
        while (iterator.hasNext())
        {
            procedure.value(iterator.next());
        }
    }

    public static <T> void forEachWithIndex(Iterator<T> iterator, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int i = 0;
        while (iterator.hasNext())
        {
            objectIntProcedure.value(iterator.next(), i++);
        }
    }

    public static <T> T detect(Iterator<T> iterator, Predicate<? super T> predicate)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (predicate.accept(item))
            {
                return item;
            }
        }
        return null;
    }

    public static <T, IV> T detectWith(
            Iterator<T> iterator,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (predicate.accept(item, injectedValue))
            {
                return item;
            }
        }
        return null;
    }

    public static <T, IV> IV injectInto(
            IV injectValue,
            Iterator<T> iterator,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        IV result = injectValue;
        while (iterator.hasNext())
        {
            result = function.value(result, iterator.next());
        }
        return result;
    }

    public static <T> int injectInto(
            int injectValue,
            Iterator<T> iterator,
            IntObjectToIntFunction<? super T> function)
    {
        int result = injectValue;
        while (iterator.hasNext())
        {
            result = function.intValueOf(result, iterator.next());
        }
        return result;
    }

    public static <T> long injectInto(
            long injectValue,
            Iterator<T> iterator,
            LongObjectToLongFunction<? super T> function)
    {
        long result = injectValue;
        while (iterator.hasNext())
        {
            result = function.longValueOf(result, iterator.next());
        }
        return result;
    }

    public static <T> double injectInto(
            double injectValue,
            Iterator<T> iterator,
            DoubleObjectToDoubleFunction<? super T> function)
    {
        double result = injectValue;
        while (iterator.hasNext())
        {
            result = function.doubleValueOf(result, iterator.next());
        }
        return result;
    }

    public static <T, IV, P> IV injectIntoWith(IV injectValue, Iterator<T> iterator, Function3<? super IV, ? super T, ? super P, ? extends IV> function, P parameter)
    {
        IV result = injectValue;
        while (iterator.hasNext())
        {
            result = function.value(result, iterator.next(), parameter);
        }
        return result;
    }

    public static <T> boolean anySatisfy(Iterator<T> iterator, Predicate<? super T> predicate)
    {
        while (iterator.hasNext())
        {
            if (predicate.accept(iterator.next()))
            {
                return true;
            }
        }
        return false;
    }

    public static <T, IV> boolean anySatisfyWith(
            Iterator<T> iterator,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        while (iterator.hasNext())
        {
            if (predicate.accept(iterator.next(), injectedValue))
            {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean allSatisfy(Iterator<T> iterator, Predicate<? super T> predicate)
    {
        while (iterator.hasNext())
        {
            if (!predicate.accept(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    public static <T, IV> boolean allSatisfyWith(
            Iterator<T> iterator,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        while (iterator.hasNext())
        {
            if (!predicate.accept(iterator.next(), injectedValue))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true is removed from the source iterator
     */
    public static <T> Iterator<T> removeIf(Iterator<T> iterator, Predicate<? super T> predicate)
    {
        while (iterator.hasNext())
        {
            T each = iterator.next();
            if (predicate.accept(each))
            {
                iterator.remove();
            }
        }
        return iterator;
    }

    public static <T, P> Iterator<T> removeIfWith(Iterator<T> iterator, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        while (iterator.hasNext())
        {
            T each = iterator.next();
            if (predicate.accept(each, parameter))
            {
                iterator.remove();
            }
        }
        return iterator;
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true is removed from the source iterator
     */
    public static <T> Iterator<T> removeIf(Iterator<T> iterator, Predicate<? super T> predicate, Procedure<? super T> procedure)
    {
        while (iterator.hasNext())
        {
            T each = iterator.next();
            if (predicate.accept(each))
            {
                procedure.value(each);
                iterator.remove();
            }
        }
        return iterator;
    }

    public static <T> int detectIndex(Iterator<T> iterator, Predicate<? super T> predicate)
    {
        int i = 0;
        while (iterator.hasNext())
        {
            if (predicate.accept(iterator.next()))
            {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <T, IV> int detectIndexWith(Iterator<T> iterator, Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        int i = 0;
        while (iterator.hasNext())
        {
            if (predicate.accept(iterator.next(), injectedValue))
            {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <T, P> void forEachWith(Iterator<T> iterator, Procedure2<? super T, ? super P> procedure, P parameter)
    {
        while (iterator.hasNext())
        {
            procedure.value(iterator.next(), parameter);
        }
    }

    public static <T, P, A, R extends Collection<A>> R collectWith(
            Iterator<T> iterator,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        while (iterator.hasNext())
        {
            targetCollection.add(function.value(iterator.next(), parameter));
        }
        return targetCollection;
    }

    public static <T, V> ImmutableMultimap<V, T> groupBy(
            Iterator<T> iterator,
            Function<? super T, ? extends V> function)
    {
        return IteratorIterate.groupBy(iterator, function, new FastListMultimap<V, T>()).toImmutable();
    }

    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            Iterator<T> iterator,
            Function<? super T, ? extends V> function,
            R target)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            target.put(function.valueOf(item), item);
        }
        return target;
    }

    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            Iterator<T> iterator,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            Iterable<V> iterable = function.valueOf(item);
            for (V key : iterable)
            {
                target.put(key, item);
            }
        }
        return target;
    }

    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(
            Iterator<X> xs,
            Iterator<Y> ys,
            R target)
    {
        while (xs.hasNext() && ys.hasNext())
        {
            target.add(Tuples.pair(xs.next(), ys.next()));
        }
        return target;
    }

    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(Iterator<T> iterator, R target)
    {
        int index = 0;
        while (iterator.hasNext())
        {
            target.add(Tuples.pair(iterator.next(), index));
            index += 1;
        }
        return target;
    }

    public static <T> RichIterable<RichIterable<T>> chunk(Iterator<T> iterator, int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        MutableList<RichIterable<T>> result = Lists.mutable.of();
        while (iterator.hasNext())
        {
            MutableList<T> batch = Lists.mutable.of();
            for (int i = 0; i < size && iterator.hasNext(); i++)
            {
                batch.add(iterator.next());
            }
            result.add(batch);
        }
        return result;
    }

    public static <T> T min(Iterator<T> iterator, Comparator<? super T> comparator)
    {
        T min = iterator.next();
        while (iterator.hasNext())
        {
            T next = iterator.next();
            if (comparator.compare(next, min) < 0)
            {
                min = next;
            }
        }
        return min;
    }

    public static <T> T max(Iterator<T> iterator, Comparator<? super T> comparator)
    {
        T max = iterator.next();
        while (iterator.hasNext())
        {
            T next = iterator.next();
            if (comparator.compare(next, max) > 0)
            {
                max = next;
            }
        }
        return max;
    }

    public static <T> Iterator<T> advanceIteratorTo(Iterator<T> iterator, int from)
    {
        for (int i = 0; i < from; i++)
        {
            iterator.next();
        }
        return iterator;
    }
}
