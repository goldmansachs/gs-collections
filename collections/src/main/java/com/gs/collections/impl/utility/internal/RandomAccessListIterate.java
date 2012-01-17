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

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;

/**
 * The ListIterate class provides a iteration protocol methods for List instances
 */
public final class RandomAccessListIterate
{
    private RandomAccessListIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> void toArray(List<T> list, T[] target, int startIndex, int sourceSize)
    {
        for (int i = 0; i < sourceSize; i++)
        {
            target[startIndex + i] = list.get(i);
        }
    }

    /**
     * @see Iterate#filter(Iterable, Predicate)
     */
    public static <T> MutableList<T> filter(List<T> list, Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.filter(list, predicate, FastList.<T>newList());
    }

    /**
     * @see Iterate#filterWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> MutableList<T> filterWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return RandomAccessListIterate.filterWith(list, predicate, injectedValue, FastList.<T>newList());
    }

    /**
     * @see Iterate#filter(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R filter(
            List<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            if (predicate.accept(item))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    /**
     * @see Iterate#filterWith(Iterable, Predicate2, Object, Collection)
     */
    public static <T, P, R extends Collection<T>> R filterWith(
            List<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            if (predicate.accept(item, parameter))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <T> int count(
            List<T> list,
            Predicate<? super T> predicate)
    {
        int count = 0;
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(list.get(i)))
            {
                count++;
            }
        }
        return count;
    }

    public static <T, IV> int countWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int count = 0;
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(list.get(i), injectedValue))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * @see Iterate#transformIf(Iterable, Predicate, Function)
     */
    public static <T, A> MutableList<A> tranformIf(
            List<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function)
    {
        return RandomAccessListIterate.tranformIf(list, predicate, function, FastList.<A>newList());
    }

    /**
     * @see Iterate#tranformIf(Iterable, Predicate, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R tranformIf(
            List<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            if (predicate.accept(item))
            {
                targetCollection.add(function.valueOf(item));
            }
        }
        return targetCollection;
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate)
     */
    public static <T> MutableList<T> filterNot(List<T> list, Predicate<? super T> predicate)
    {
        return RandomAccessListIterate.filterNot(list, predicate, FastList.<T>newList());
    }

    /**
     * @see Iterate#filterNotWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> MutableList<T> filterNotWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return RandomAccessListIterate.filterNotWith(list, predicate, injectedValue, FastList.<T>newList());
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R filterNot(
            List<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            if (!predicate.accept(item))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate, Collection)
     */
    public static <T, P, R extends Collection<T>> R filterNotWith(
            List<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            if (!predicate.accept(item, parameter))
            {
                targetCollection.add(item);
            }
        }
        return targetCollection;
    }

    /**
     * @see Iterate#transform(Iterable, Function)
     */
    public static <T, A> MutableList<A> transform(
            List<T> list,
            Function<? super T, ? extends A> function)
    {
        return transform(list, function, FastList.<A>newList(list.size()));
    }

    /**
     * @see Iterate#transform(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R transform(
            List<T> list,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            targetCollection.add(function.valueOf(list.get(i)));
        }
        return targetCollection;
    }

    /**
     * @see Iterate#flatTransform(Iterable, Function)
     */
    public static <T, A> MutableList<A> flatTransform(
            List<T> list,
            Function<? super T, ? extends Iterable<A>> function)
    {
        return flatTransform(list, function, FastList.<A>newList(list.size()));
    }

    /**
     * @see Iterate#flatTransform(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R flatTransform(
            List<T> list,
            Function<? super T, ? extends Iterable<A>> function,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            Iterate.addAllTo(function.valueOf(list.get(i)), targetCollection);
        }
        return targetCollection;
    }

    /**
     * Returns the last element of a list.
     */
    public static <T> T getLast(List<T> collection)
    {
        return Iterate.isEmpty(collection) ? null : collection.get(collection.size() - 1);
    }

    /**
     * @see Iterate#forEach(Iterable, Procedure)
     */
    public static <T> void forEach(List<T> list, Procedure<? super T> procedure)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            procedure.value(list.get(i));
        }
    }

    /**
     * Interates over the section of the list covered by the specified indexes.  The indexes are both inclusive.  If the
     * from is less than the to, the list is iterated in forward order. If the from is greater than the to, then the
     * list is iterated in the reverse order.
     * <p/>
     * <p/>
     * <pre>e.g.
     * MutableList<People> people = FastList.newListWith(ted, mary, bob, sally);
     * ListIterate.forEach(people, 0, 1, new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *          LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     * <p/>
     * This code would output ted and mary's names.
     */
    public static <T> void forEach(List<T> list, int from, int to, Procedure<? super T> procedure)
    {
        if (from < 0 || to < 0)
        {
            throw new IllegalArgumentException("Neither from nor to may be negative.");
        }
        if (from <= to)
        {
            for (int i = from; i <= to; i++)
            {
                procedure.value(list.get(i));
            }
        }
        else
        {
            for (int i = from; i >= to; i--)
            {
                procedure.value(list.get(i));
            }
        }
    }

    /**
     * Interates over the section of the list covered by the specified indexes.  The indexes are both inclusive.  If the
     * from is less than the to, the list is iterated in forward order. If the from is greater than the to, then the
     * list is iterated in the reverse order. The index passed into the ProcedureWithInt is the actual index of the
     * range.
     * <p/>
     * <p/>
     * <pre>e.g.
     * MutableList<People> people = FastList.newListWith(ted, mary, bob, sally);
     * ListIterate.forEachWithIndex(people, 0, 1, new ProcedureWithInt<Person>()
     * {
     *     public void value(Person person, int index)
     *     {
     *          LOGGER.info(person.getName() + " at index: " + index);
     *     }
     * });
     * </pre>
     * <p/>
     * This code would output ted and mary's names.
     */
    public static <T> void forEachWithIndex(List<T> list, int from, int to, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (from < 0 || to < 0)
        {
            throw new IllegalArgumentException("Neither from nor to may be negative.");
        }
        if (from <= to)
        {
            for (int i = from; i <= to; i++)
            {
                objectIntProcedure.value(list.get(i), i);
            }
        }
        else
        {
            for (int i = from; i >= to; i--)
            {
                objectIntProcedure.value(list.get(i), i);
            }
        }
    }

    /**
     * For each element in both of the Lists, operation is evaluated with both elements as parameters.
     */
    public static <T1, T2> void forEachInBoth(List<T1> list1, List<T2> list2, Procedure2<? super T1, ? super T2> procedure)
    {
        if (list1 != null && list2 != null)
        {
            if (list1.size() == list2.size())
            {
                for (int i = 0, size = list1.size(); i < size; i++)
                {
                    procedure.value(list1.get(i), list2.get(i));
                }
            }
            else
            {
                throw new IllegalArgumentException("Attempt to call forEachInBoth with two Lists of different sizes :"
                        + list1.size()
                        + ':'
                        + list2.size());
            }
        }
    }

    /**
     * Iterates over a collection passing each element and the current relative int index to the specified instance of
     * ProcedureWithInt.
     */
    public static <T> void forEachWithIndex(List<T> list, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            objectIntProcedure.value(list.get(i), i);
        }
    }

    public static <T> T find(List<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            if (predicate.accept(item))
            {
                return item;
            }
        }
        return null;
    }

    public static <T, IV> T findWith(List<T> list, Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            if (predicate.accept(item, injectedValue))
            {
                return item;
            }
        }
        return null;
    }

    public static <T, IV> IV foldLeft(IV injectValue, List<T> list, Function2<? super IV, ? super T, ? extends IV> function)
    {
        IV result = injectValue;
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            result = function.value(result, list.get(i));
        }
        return result;
    }

    public static <T> int foldLeft(int injectValue, List<T> list, IntObjectToIntFunction<? super T> function)
    {
        int result = injectValue;
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            result = function.intValueOf(result, list.get(i));
        }
        return result;
    }

    public static <T> long foldLeft(long injectValue, List<T> list, LongObjectToLongFunction<? super T> function)
    {
        long result = injectValue;
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            result = function.longValueOf(result, list.get(i));
        }
        return result;
    }

    public static <T> double foldLeft(double injectValue, List<T> list, DoubleObjectToDoubleFunction<? super T> function)
    {
        double result = injectValue;
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            result = function.doubleValueOf(result, list.get(i));
        }
        return result;
    }

    public static <T> boolean anySatisfy(List<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(list.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    public static <T, IV> boolean anySatisfyWith(List<T> list, Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(list.get(i), injectedValue))
            {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean allSatisfy(List<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (!predicate.accept(list.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    public static <T, IV> boolean allSatisfyWith(List<T> list, Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (!predicate.accept(list.get(i), injectedValue))
            {
                return false;
            }
        }
        return true;
    }

    public static <T, IV> Twin<MutableList<T>> partitionWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        MutableList<T> positiveResult = Lists.mutable.of();
        MutableList<T> negativeResult = Lists.mutable.of();
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            (predicate.accept(item, injectedValue) ? positiveResult : negativeResult).add(item);
        }
        return Tuples.twin(positiveResult, negativeResult);
    }

    public static <T> PartitionMutableList<T> partition(
            List<T> list,
            Predicate<? super T> predicate)
    {
        PartitionFastList<T> partitionFastList = new PartitionFastList<T>(predicate);

        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            partitionFastList.add(list.get(i));
        }

        return partitionFastList;
    }

    public static <T> List<T> removeIf(List<T> list, Predicate<? super T> predicate)
    {
        for (int i = 0; i < list.size(); i++)
        {
            T each = list.get(i);
            if (predicate.accept(each))
            {
                list.remove(i--);
            }
        }
        return list;
    }

    public static <T, P> List<T> removeIfWith(List<T> list, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        for (int i = 0; i < list.size(); i++)
        {
            T each = list.get(i);
            if (predicate.accept(each, parameter))
            {
                list.remove(i--);
            }
        }
        return list;
    }

    public static <T> List<T> removeIf(List<T> list, Predicate<? super T> predicate, Procedure<? super T> procedure)
    {
        for (int i = 0; i < list.size(); i++)
        {
            T each = list.get(i);
            if (predicate.accept(each))
            {
                procedure.value(each);
                list.remove(i--);
            }
        }
        return list;
    }

    /**
     * Searches for the first occurence where the predicate evaluates to true.
     */
    public static <T> int findIndex(List<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(list.get(i)))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches for the first occurence where the predicate evaluates to true.
     */
    public static <T, IV> int findIndexWith(List<T> list, Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(list.get(i), injectedValue))
            {
                return i;
            }
        }
        return -1;
    }

    public static <T, IV, P> IV foldLeftWith(
            IV injectedValue,
            List<T> list,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        IV result = injectedValue;
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            result = function.value(result, list.get(i), parameter);
        }
        return result;
    }

    public static <T, P> void forEachWith(List<T> list, Procedure2<? super T, ? super P> procedure, P parameter)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            procedure.value(list.get(i), parameter);
        }
    }

    public static <T, P, A, R extends Collection<A>> R transformWith(
            List<T> list,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            targetCollection.add(function.value(list.get(i), parameter));
        }
        return targetCollection;
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T> MutableList<T> take(List<T> list, int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        return RandomAccessListIterate.take(list, count, FastList.<T>newList(Math.min(list.size(), count)));
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T, R extends Collection<T>> R take(List<T> list, int count, R targetList)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        int end = Math.min(list.size(), count);
        for (int i = 0; i < end; i++)
        {
            targetList.add(list.get(i));
        }
        return targetList;
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T> MutableList<T> drop(List<T> list, int count)
    {
        return RandomAccessListIterate.drop(list, count, FastList.<T>newList(list.size() - Math.min(list.size(), count)));
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T, R extends Collection<T>> R drop(List<T> list, int count, R targetList)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        int start = Math.min(list.size(), count);
        targetList.addAll(list.subList(start, list.size()));
        return targetList;
    }

    /**
     * @see RichIterable#appendString(Appendable, String, String, String)
     */
    public static <T> void appendString(
            List<T> list,
            Appendable appendable,
            String start,
            String separator,
            String end)
    {
        try
        {
            appendable.append(start);

            if (Iterate.notEmpty(list))
            {
                appendable.append(IterableIterate.stringValueOfItem(list, list.get(0)));

                for (int i = 1, size = list.size(); i < size; i++)
                {
                    appendable.append(separator);
                    appendable.append(IterableIterate.stringValueOfItem(list, list.get(i)));
                }
            }

            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Iterate#groupBy(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupBy(
            List<T> list,
            Function<? super T, ? extends V> function)
    {
        return RandomAccessListIterate.groupBy(list, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupBy(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            List<T> list,
            Function<? super T, ? extends V> function,
            R target)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            target.put(function.valueOf(item), item);
        }
        return target;
    }

    public static <T, V> FastListMultimap<V, T> groupByEach(
            List<T> list,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return RandomAccessListIterate.groupByEach(list, function, FastListMultimap.<V, T>newMultimap());
    }

    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            List<T> list,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            T item = list.get(i);
            Iterable<V> iterable = function.valueOf(item);
            for (V key : iterable)
            {
                target.put(key, item);
            }
        }
        return target;
    }

    public static <T> T min(List<T> list, Comparator<? super T> comparator)
    {
        if (list.isEmpty())
        {
            throw new NoSuchElementException();
        }

        T candidate = list.get(0);
        for (int i = 1, size = list.size(); i < size; i++)
        {
            T next = list.get(i);
            if (comparator.compare(next, candidate) < 0)
            {
                candidate = next;
            }
        }
        return candidate;
    }

    public static <T> T max(List<T> list, Comparator<? super T> comparator)
    {
        if (list.isEmpty())
        {
            throw new NoSuchElementException();
        }

        T candidate = list.get(0);
        for (int i = 1, size = list.size(); i < size; i++)
        {
            T next = list.get(i);
            if (comparator.compare(next, candidate) > 0)
            {
                candidate = next;
            }
        }
        return candidate;
    }

    public static <T> T min(List<T> list)
    {
        if (list.isEmpty())
        {
            throw new NoSuchElementException();
        }

        T candidate = list.get(0);
        for (int i = 1, size = list.size(); i < size; i++)
        {
            T next = list.get(i);
            if (((Comparable<? super T>) next).compareTo(candidate) < 0)
            {
                candidate = next;
            }
        }
        return candidate;
    }

    public static <T> T max(List<T> list)
    {
        if (list.isEmpty())
        {
            throw new NoSuchElementException();
        }

        T candidate = list.get(0);
        for (int i = 1, size = list.size(); i < size; i++)
        {
            T next = list.get(i);
            if (((Comparable<T>) next).compareTo(candidate) > 0)
            {
                candidate = next;
            }
        }
        return candidate;
    }

    public static <X, Y> MutableList<Pair<X, Y>> zip(
            List<X> list,
            Iterable<Y> iterable)
    {
        return RandomAccessListIterate.zip(list, iterable, FastList.<Pair<X, Y>>newList());
    }

    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(
            List<X> list,
            Iterable<Y> iterable,
            R target)
    {
        Iterator<Y> yIterator = iterable.iterator();
        for (int i = 0, size = list.size(); i < size && yIterator.hasNext(); i++)
        {
            target.add(Tuples.pair(list.get(i), yIterator.next()));
        }
        return target;
    }

    public static <T> MutableList<Pair<T, Integer>> zipWithIndex(List<T> list)
    {
        return RandomAccessListIterate.zipWithIndex(list, FastList.<Pair<T, Integer>>newList());
    }

    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(
            List<T> list,
            R target)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            target.add(Tuples.pair(list.get(i), i));
        }
        return target;
    }
}
