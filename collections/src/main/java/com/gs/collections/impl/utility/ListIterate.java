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

package com.gs.collections.impl.utility;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
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
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.utility.internal.IterableIterate;
import com.gs.collections.impl.utility.internal.RandomAccessListIterate;

/**
 * The ListIterate utility class can be useful for iterating over lists, especially if there
 * is a desire to return a MutableList from any of the iteration methods.
 *
 * @since 1.0
 */
public final class ListIterate
{
    private ListIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> void toArray(List<T> list, T[] target, int startIndex, int sourceSize)
    {
        if (list instanceof RandomAccess)
        {
            RandomAccessListIterate.toArray(list, target, startIndex, sourceSize);
        }
        else
        {
            for (int i = 0; i < sourceSize; i++)
            {
                target[startIndex + i] = list.get(i);
            }
        }
    }

    /**
     * @see Iterate#select(Iterable, Predicate)
     */
    public static <T> MutableList<T> select(List<T> list, Predicate<? super T> predicate)
    {
        return ListIterate.select(list, predicate, FastList.<T>newList());
    }

    /**
     * @see Iterate#selectWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> MutableList<T> selectWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return ListIterate.selectWith(list, predicate, injectedValue, FastList.<T>newList());
    }

    /**
     * @see Iterate#select(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R select(
            List<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.select(list, predicate, targetCollection);
        }
        return IterableIterate.select(list, predicate, targetCollection);
    }

    /**
     * @see Iterate#selectWith(Iterable, Predicate2, Object, Collection)
     */
    public static <T, P, R extends Collection<T>> R selectWith(
            List<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.selectWith(list, predicate, parameter, targetCollection);
        }
        return IterableIterate.selectWith(list, predicate, parameter, targetCollection);
    }

    /**
     * @see Iterate#selectInstancesOf(Iterable, Class)
     */
    public static <T, S> MutableList<S> selectInstancesOf(
            List<T> list,
            Class<S> clazz)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.selectInstancesOf(list, clazz);
        }
        return IterableIterate.selectInstancesOf(list, clazz);
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <T> int count(List<T> list, Predicate<? super T> predicate)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.count(list, predicate);
        }
        return IterableIterate.count(list, predicate);
    }

    /**
     * @see Iterate#countWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> int countWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.countWith(list, predicate, injectedValue);
        }
        return IterableIterate.countWith(list, predicate, injectedValue);
    }

    /**
     * @see Iterate#collectIf(Iterable, Predicate, Function)
     */
    public static <T, A> MutableList<A> collectIf(
            List<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function)
    {
        return ListIterate.collectIf(list, predicate, function, FastList.<A>newList());
    }

    /**
     * @see Iterate#collectIf(Iterable, Predicate, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R collectIf(
            List<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.collectIf(list, predicate, function, targetCollection);
        }
        return IterableIterate.collectIf(list, predicate, function, targetCollection);
    }

    /**
     * @see Iterate#reject(Iterable, Predicate)
     */
    public static <T> MutableList<T> reject(List<T> list, Predicate<? super T> predicate)
    {
        return ListIterate.reject(list, predicate, FastList.<T>newList());
    }

    /**
     * @see Iterate#rejectWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> MutableList<T> rejectWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return ListIterate.rejectWith(list, predicate, injectedValue, FastList.<T>newList());
    }

    /**
     * @see Iterate#reject(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R reject(
            List<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.reject(list, predicate, targetCollection);
        }
        return IterableIterate.reject(list, predicate, targetCollection);
    }

    /**
     * @see Iterate#reject(Iterable, Predicate, Collection)
     */
    public static <T, P, R extends Collection<T>> R rejectWith(
            List<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.rejectWith(list, predicate, parameter, targetCollection);
        }
        return IterableIterate.rejectWith(list, predicate, parameter, targetCollection);
    }

    /**
     * @see Iterate#collect(Iterable, Function)
     */
    public static <T, A> MutableList<A> collect(
            List<T> list,
            Function<? super T, ? extends A> function)
    {
        return collect(list, function, FastList.<A>newList(list.size()));
    }

    /**
     * @see Iterate#collect(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R collect(
            List<T> list,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.collect(list, function, targetCollection);
        }
        return IterableIterate.collect(list, function, targetCollection);
    }

    /**
     * @see Iterate#flatCollect(Iterable, Function)
     */
    public static <T, A> MutableList<A> flatCollect(
            List<T> list,
            Function<? super T, ? extends Iterable<A>> function)
    {
        return flatCollect(list, function, FastList.<A>newList(list.size()));
    }

    /**
     * @see Iterate#flatCollect(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R flatCollect(
            List<T> list,
            Function<? super T, ? extends Iterable<A>> function,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.flatCollect(list, function, targetCollection);
        }
        return IterableIterate.flatCollect(list, function, targetCollection);
    }

    /**
     * Returns the first element of a list.
     */
    public static <T> T getFirst(List<T> collection)
    {
        return Iterate.isEmpty(collection) ? null : collection.get(0);
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
        if (list instanceof RandomAccess)
        {
            RandomAccessListIterate.forEach(list, procedure);
        }
        else
        {
            IterableIterate.forEach(list, procedure);
        }
    }

    /**
     * Reverses over the List in reverse order executing the Procedure for each element
     */
    public static <T> void reverseForEach(List<T> list, Procedure<? super T> procedure)
    {
        if (!list.isEmpty())
        {
            ListIterate.forEach(list, list.size() - 1, 0, procedure);
        }
    }

    /**
     * Iterates over the section of the list covered by the specified indexes.  The indexes are both inclusive.  If the
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
        ListIterate.rangeCheck(from, to, list.size());

        if (list instanceof RandomAccess)
        {
            RandomAccessListIterate.forEach(list, from, to, procedure);
        }
        else
        {
            if (from <= to)
            {
                ListIterator<T> iterator = list.listIterator(from);
                for (int i = from; i <= to; i++)
                {
                    procedure.value(iterator.next());
                }
            }
            else
            {
                ListIterator<T> iterator = list.listIterator(from + 1);
                for (int i = from; i >= to; i--)
                {
                    procedure.value(iterator.previous());
                }
            }
        }
    }

    /**
     * Iterates over the section of the list covered by the specified indexes.  The indexes are both inclusive.  If the
     * from is less than the to, the list is iterated in forward order. If the from is greater than the to, then the
     * list is iterated in the reverse order. The index passed into the ObjectIntProcedure is the actual index of the
     * range.
     * <p/>
     * <p/>
     * <pre>e.g.
     * MutableList<People> people = FastList.newListWith(ted, mary, bob, sally);
     * ListIterate.forEachWithIndex(people, 0, 1, new ObjectIntProcedure<Person>()
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
        ListIterate.rangeCheck(from, to, list.size());

        if (list instanceof RandomAccess)
        {
            RandomAccessListIterate.forEachWithIndex(list, from, to, objectIntProcedure);
        }
        else
        {
            if (from <= to)
            {
                ListIterator<T> iterator = list.listIterator(from);
                for (int i = from; i <= to; i++)
                {
                    objectIntProcedure.value(iterator.next(), i);
                }
            }
            else
            {
                ListIterator<T> iterator = list.listIterator(from + 1);
                for (int i = from; i >= to; i--)
                {
                    objectIntProcedure.value(iterator.previous(), i);
                }
            }
        }
    }

    public static void rangeCheck(int from, int to, int size)
    {
        if (from < 0)
        {
            throw new IndexOutOfBoundsException("From index: " + from);
        }
        if (to < 0)
        {
            throw new IndexOutOfBoundsException("To index: " + to);
        }
        if (from >= size)
        {
            throw new IndexOutOfBoundsException("From index: " + from + " Size: " + size);
        }
        if (to >= size)
        {
            throw new IndexOutOfBoundsException("To index: " + to + " Size: " + size);
        }
    }

    /**
     * Iterates over both lists together, evaluating Procedure2 with the current element from each list.
     */
    public static <T1, T2> void forEachInBoth(List<T1> list1, List<T2> list2, Procedure2<? super T1, ? super T2> procedure)
    {
        if (list1 != null && list2 != null)
        {
            if (list1.size() == list2.size())
            {
                if (list1 instanceof RandomAccess && list2 instanceof RandomAccess)
                {
                    RandomAccessListIterate.forEachInBoth(list1, list2, procedure);
                }
                else
                {
                    Iterator<T1> iterator1 = list1.iterator();
                    Iterator<T2> iterator2 = list2.iterator();
                    int size = list2.size();
                    for (int i = 0; i < size; i++)
                    {
                        procedure.value(iterator1.next(), iterator2.next());
                    }
                }
            }
            else
            {
                throw new RuntimeException("Attempt to call forEachInBoth with two Lists of different sizes :"
                        + list1.size()
                        + ':'
                        + list2.size());
            }
        }
    }

    /**
     * @see Iterate#forEachWithIndex(Iterable, ObjectIntProcedure)
     */
    public static <T> void forEachWithIndex(List<T> list, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (list instanceof RandomAccess)
        {
            RandomAccessListIterate.forEachWithIndex(list, objectIntProcedure);
        }
        else
        {
            IterableIterate.forEachWithIndex(list, objectIntProcedure);
        }
    }

    /**
     * @see Iterate#detect(Iterable, Predicate)
     */
    public static <T> T detect(List<T> list, Predicate<? super T> predicate)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.detect(list, predicate);
        }
        return IterableIterate.detect(list, predicate);
    }

    /**
     * @see Iterate#detectWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> T detectWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.detectWith(list, predicate, injectedValue);
        }
        return IterableIterate.detectWith(list, predicate, injectedValue);
    }

    /**
     * @see Iterate#detectIfNone(Iterable, Predicate, Object)
     */
    public static <T> T detectIfNone(List<T> list, Predicate<? super T> predicate, T ifNone)
    {
        T result = ListIterate.detect(list, predicate);
        return result == null ? ifNone : result;
    }

    /**
     * @see Iterate#detectWithIfNone(Iterable, Predicate2, Object, Object)
     */
    public static <T, IV> T detectWithIfNone(List<T> list, Predicate2<? super T, ? super IV> predicate, IV injectedValue, T ifNone)
    {
        T result = ListIterate.detectWith(list, predicate, injectedValue);
        return result == null ? ifNone : result;
    }

    /**
     * @see Iterate#injectInto(Object, Iterable, Function2)
     */
    public static <T, IV> IV injectInto(IV injectValue, List<T> list, Function2<? super IV, ? super T, ? extends IV> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, list, function);
        }
        return IterableIterate.injectInto(injectValue, list, function);
    }

    /**
     * @see Iterate#injectInto(int, Iterable, IntObjectToIntFunction)
     */
    public static <T> int injectInto(int injectValue, List<T> list, IntObjectToIntFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, list, function);
        }
        return IterableIterate.injectInto(injectValue, list, function);
    }

    /**
     * @see Iterate#injectInto(long, Iterable, LongObjectToLongFunction)
     */
    public static <T> long injectInto(long injectValue, List<T> list, LongObjectToLongFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, list, function);
        }
        return IterableIterate.injectInto(injectValue, list, function);
    }

    /**
     * @see Iterate#injectInto(double, Iterable, DoubleObjectToDoubleFunction)
     */
    public static <T> double injectInto(double injectValue, List<T> list, DoubleObjectToDoubleFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, list, function);
        }
        return IterableIterate.injectInto(injectValue, list, function);
    }

    /**
     * @see Iterate#injectInto(float, Iterable, FloatObjectToFloatFunction)
     */
    public static <T> float injectInto(float injectValue, List<T> list, FloatObjectToFloatFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, list, function);
        }
        return IterableIterate.injectInto(injectValue, list, function);
    }

    public static <T> int sumOf(List<T> list, IntFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.sumOf(list, function);
        }
        return IterableIterate.sumOf(list, function);
    }

    public static <T> long sumOf(List<T> list, LongFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.sumOf(list, function);
        }
        return IterableIterate.sumOf(list, function);
    }

    public static <T> float sumOf(List<T> list, FloatFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.sumOf(list, function);
        }
        return IterableIterate.sumOf(list, function);
    }

    public static <T> double sumOf(List<T> list, DoubleFunction<? super T> function)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.sumOf(list, function);
        }
        return IterableIterate.sumOf(list, function);
    }

    /**
     * @see Iterate#anySatisfy(Iterable, Predicate)
     */
    public static <T> boolean anySatisfy(List<T> list, Predicate<? super T> predicate)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.anySatisfy(list, predicate);
        }
        return IterableIterate.anySatisfy(list, predicate);
    }

    /**
     * @see Iterate#anySatisfyWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> boolean anySatisfyWith(List<T> list, Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.anySatisfyWith(list, predicate, injectedValue);
        }
        return IterableIterate.anySatisfyWith(list, predicate, injectedValue);
    }

    /**
     * @see Iterate#allSatisfy(Iterable, Predicate)
     */
    public static <T> boolean allSatisfy(List<T> list, Predicate<? super T> predicate)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.allSatisfy(list, predicate);
        }
        return IterableIterate.allSatisfy(list, predicate);
    }

    /**
     * @see Iterate#allSatisfyWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> boolean allSatisfyWith(List<T> list, Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.allSatisfyWith(list, predicate, injectedValue);
        }
        return IterableIterate.allSatisfyWith(list, predicate, injectedValue);
    }

    /**
     * @see Iterate#selectAndRejectWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> Twin<MutableList<T>> selectAndRejectWith(
            List<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.selectAndRejectWith(list, predicate, injectedValue);
        }
        return IterableIterate.selectAndRejectWith(list, predicate, injectedValue);
    }

    /**
     * @see Iterate#partition(Iterable, Predicate)
     */
    public static <T> PartitionMutableList<T> partition(
            List<T> list,
            Predicate<? super T> predicate)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.partition(list, predicate);
        }
        return IterableIterate.partition(list, predicate);
    }

    /**
     * @see Iterate#removeIf(Iterable, Predicate)
     */
    public static <T> List<T> removeIf(List<T> list, Predicate<? super T> predicate)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.removeIf(list, predicate);
        }
        return (List<T>) IterableIterate.removeIf(list, predicate);
    }

    /**
     * @see Iterate#removeIfWith(Iterable, Predicate2, Object)
     */
    public static <T, P> List<T> removeIfWith(List<T> list, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.removeIfWith(list, predicate, parameter);
        }
        return (List<T>) IterableIterate.removeIfWith(list, predicate, parameter);
    }

    public static <T> List<T> removeIf(List<T> list, Predicate<? super T> predicate, Procedure<? super T> procedure)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.removeIf(list, predicate, procedure);
        }
        return (List<T>) IterableIterate.removeIf(list, predicate, procedure);
    }

    /**
     * Searches for the first index where the predicate evaluates to true.
     */
    public static <T> int detectIndex(List<T> list, Predicate<? super T> predicate)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.detectIndex(list, predicate);
        }
        return IterableIterate.detectIndex(list, predicate);
    }

    /**
     * Searches for the first index where the predicate2 and parameter evaluates to true.
     */
    public static <T, P> int detectIndexWith(List<T> list, Predicate2<? super T, ? super P> predicate, P parameter)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.detectIndexWith(list, predicate, parameter);
        }
        return IterableIterate.detectIndexWith(list, predicate, parameter);
    }

    public static <T, IV, P> IV injectIntoWith(IV injectedValue, List<T> list, Function3<? super IV, ? super T, ? super P, ? extends IV> function, P parameter)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectIntoWith(injectedValue, list, function, parameter);
        }
        return IterableIterate.injectIntoWith(injectedValue, list, function, parameter);
    }

    /**
     * @see Iterate#forEachWith(Iterable, Procedure2, Object)
     */
    public static <T, P> void forEachWith(List<T> list, Procedure2<? super T, ? super P> procedure, P parameter)
    {
        if (list instanceof RandomAccess)
        {
            RandomAccessListIterate.forEachWith(list, procedure, parameter);
        }
        else
        {
            IterableIterate.forEachWith(list, procedure, parameter);
        }
    }

    /**
     * @see Iterate#collectWith(Iterable, Function2, Object)
     */
    public static <T, P, A> MutableList<A> collectWith(
            List<T> list,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter)
    {
        return ListIterate.collectWith(list, function, parameter, FastList.<A>newList(list.size()));
    }

    /**
     * @see Iterate#collectWith(Iterable, Function2, Object, Collection)
     */
    public static <T, P, A, R extends Collection<A>> R collectWith(
            List<T> list,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.collectWith(list, function, parameter, targetCollection);
        }
        return IterableIterate.collectWith(list, function, parameter, targetCollection);
    }

    /**
     * Reverses the order of the items in the list.
     * <pre>
     *     List<Integer> integers = Lists.fixedSize.of(1, 3, 2);
     *     Verify.assertListsEqual(FastList.newListWith(2, 3, 1), ListIterate.reverse(integers));
     * </pre>
     *
     * @return the reversed list
     */
    public static <T> List<T> reverseThis(List<T> list)
    {
        Collections.reverse(list);
        return list;
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

        return ListIterate.take(list, count, FastList.<T>newList(count));
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T, R extends Collection<T>> R take(List<T> list, int count, R targetList)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.take(list, count, targetList);
        }
        return IterableIterate.take(list, count, targetList);
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T> MutableList<T> drop(List<T> list, int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }

        return ListIterate.drop(list, count, FastList.<T>newList(list.size() - Math.min(list.size(), count)));
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
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.drop(list, count, targetList);
        }
        return (R) IterableIterate.drop(list, count, targetList);
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
        if (list instanceof RandomAccess)
        {
            RandomAccessListIterate.appendString(list, appendable, start, separator, end);
        }
        else
        {
            IterableIterate.appendString(list, appendable, start, separator, end);
        }
    }

    /**
     * @see Iterate#groupBy(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupBy(
            List<T> list,
            Function<? super T, ? extends V> function)
    {
        return ListIterate.groupBy(list, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupBy(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            List<T> list,
            Function<? super T, ? extends V> function,
            R target)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupBy(list, function, target);
        }
        return IterableIterate.groupBy(list, function, target);
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupByEach(
            List<T> list,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return ListIterate.groupByEach(list, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            List<T> list,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupByEach(list, function, target);
        }
        return IterableIterate.groupByEach(list, function, target);
    }

    /**
     * @see Iterate#min(Iterable, Comparator)
     */
    public static <T> T min(List<T> list, Comparator<? super T> comparator)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.min(list, comparator);
        }
        return IterableIterate.min(list, comparator);
    }

    /**
     * @see Iterate#max(Iterable, Comparator)
     */
    public static <T> T max(List<T> list, Comparator<? super T> comparator)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.max(list, comparator);
        }
        return IterableIterate.max(list, comparator);
    }

    /**
     * @see Iterate#min(Iterable)
     */
    public static <T> T min(List<T> list)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.min(list);
        }
        return IterableIterate.min(list);
    }

    /**
     * @see Iterate#max(Iterable)
     */
    public static <T> T max(List<T> list)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.max(list);
        }
        return IterableIterate.max(list);
    }

    /**
     * @see Iterate#chunk(Iterable, int)
     */
    public static <T> RichIterable<RichIterable<T>> chunk(List<T> list, int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        Iterator<T> iterator = list.iterator();
        MutableList<RichIterable<T>> result = Lists.mutable.of();
        while (iterator.hasNext())
        {
            MutableCollection<T> batch = Lists.mutable.of();
            for (int i = 0; i < size && iterator.hasNext(); i++)
            {
                batch.add(iterator.next());
            }
            result.add(batch);
        }
        return result;
    }

    /**
     * @see Iterate#zip(Iterable, Iterable)
     */
    public static <X, Y> MutableList<Pair<X, Y>> zip(
            List<X> list,
            Iterable<Y> iterable)
    {
        return ListIterate.zip(list, iterable, FastList.<Pair<X, Y>>newList());
    }

    /**
     * @see Iterate#zip(Iterable, Iterable, Collection)
     */
    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(
            List<X> list,
            Iterable<Y> iterable,
            R target)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.zip(list, iterable, target);
        }
        return IterableIterate.zip(list, iterable, target);
    }

    /**
     * @see Iterate#zipWithIndex(Iterable)
     */
    public static <T> MutableList<Pair<T, Integer>> zipWithIndex(List<T> list)
    {
        return ListIterate.zipWithIndex(list, FastList.<Pair<T, Integer>>newList());
    }

    /**
     * @see Iterate#zipWithIndex(Iterable, Collection)
     */
    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(
            List<T> list,
            R target)
    {
        if (list instanceof RandomAccess)
        {
            return RandomAccessListIterate.zipWithIndex(list, target);
        }
        return IterableIterate.zipWithIndex(list, target);
    }
}
