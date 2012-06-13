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

package ponzu.impl.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Function3;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.MutableList;
import ponzu.api.multimap.MutableMultimap;
import ponzu.api.partition.list.PartitionMutableList;
import ponzu.api.tuple.Pair;
import ponzu.api.tuple.Twin;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.multimap.list.FastListMultimap;
import ponzu.impl.partition.list.PartitionFastList;
import ponzu.impl.tuple.Tuples;
import ponzu.impl.utility.internal.InternalArrayIterate;
import ponzu.impl.utility.internal.RandomAccessListIterate;

/**
 * This utility class provides optimized iteration pattern implementations that work with java.util.ArrayList.
 */
public final class ArrayListIterate
{
    private static final Field ELEMENT_DATA_FIELD;
    private static final Field SIZE_FIELD;
    private static final int MIN_DIRECT_ARRAY_ACCESS_SIZE = 100;

    static
    {
        Field data = null;
        Field size = null;
        try
        {
            data = ArrayList.class.getDeclaredField("elementData");
            size = ArrayList.class.getDeclaredField("size");
            try
            {
                data.setAccessible(true);
                size.setAccessible(true);
            }
            catch (SecurityException ignored)
            {
                data = null;
                size = null;
            }
        }
        catch (NoSuchFieldException ignored)
        {
        }
        ELEMENT_DATA_FIELD = data;
        SIZE_FIELD = size;
    }

    private ArrayListIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * @see Iterate#filter(Iterable, Predicate)
     */
    public static <T> ArrayList<T> filter(ArrayList<T> list, Predicate<? super T> predicate)
    {
        return ArrayListIterate.filter(list, predicate, new ArrayList<T>());
    }

    /**
     * @see Iterate#filterWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> ArrayList<T> filterWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return ArrayListIterate.filterWith(list, predicate, injectedValue, new ArrayList<T>());
    }

    /**
     * @see Iterate#filter(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R filter(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.filter(list, predicate, targetCollection);
    }

    /**
     * @see Iterate#filterWith(Iterable, Predicate2, Object, Collection)
     */
    public static <T, P, R extends Collection<T>> R filterWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], parameter))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.filterWith(list, predicate, parameter, targetCollection);
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <T> int count(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            int count = 0;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    count++;
                }
            }
            return count;
        }
        return RandomAccessListIterate.count(list, predicate);
    }

    /**
     * @see Iterate#countWith(Iterable, Predicate2, Object)
     */
    public static <T, P> int countWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            int count = 0;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], parameter))
                {
                    count++;
                }
            }
            return count;
        }
        return RandomAccessListIterate.countWith(list, predicate, parameter);
    }

    /**
     * @see Iterate#transformIf(Iterable, Predicate, Function)
     */
    public static <T, A> ArrayList<A> transformIf(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function)
    {
        return ArrayListIterate.tranformIf(list, predicate, function, new ArrayList<A>());
    }

    /**
     * @see Iterate#tranformIf(Iterable, Predicate, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R tranformIf(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    targetCollection.add(function.valueOf(elements[i]));
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.tranformIf(list, predicate, function, targetCollection);
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate)
     */
    public static <T> ArrayList<T> filterNot(ArrayList<T> list, Predicate<? super T> predicate)
    {
        return ArrayListIterate.filterNot(list, predicate, new ArrayList<T>());
    }

    /**
     * @see Iterate#filterNotWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> ArrayList<T> filterNotWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return ArrayListIterate.filterNotWith(list, predicate, injectedValue, new ArrayList<T>());
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R filterNot(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i]))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.filterNot(list, predicate, targetCollection);
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate, Collection)
     */
    public static <T, P, R extends Collection<T>> R filterNotWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P injectedValue,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i], injectedValue))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.filterNotWith(list, predicate, injectedValue, targetCollection);
    }

    /**
     * @see Iterate#transform(Iterable, Function)
     */
    public static <T, A> ArrayList<A> transform(
            ArrayList<T> list,
            Function<? super T, ? extends A> function)
    {
        return ArrayListIterate.transform(list, function, new ArrayList<A>(list.size()));
    }

    /**
     * @see Iterate#transform(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R transform(
            ArrayList<T> list,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                targetCollection.add(function.valueOf(elements[i]));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.transform(list, function, targetCollection);
    }

    /**
     * @see Iterate#flatTransform(Iterable, Function)
     */
    public static <T, A> ArrayList<A> flatTransform(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<A>> function)
    {
        return ArrayListIterate.flatTransform(list, function, new ArrayList<A>(list.size()));
    }

    /**
     * @see Iterate#flatTransform(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R flatTransform(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<A>> function,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                Iterate.addAllTo(function.valueOf(elements[i]), targetCollection);
            }
            return targetCollection;
        }
        return RandomAccessListIterate.flatTransform(list, function, targetCollection);
    }

    /**
     * @see Iterate#forEach(Iterable, Procedure)
     */
    public static <T> void forEach(ArrayList<T> list, Procedure<? super T> procedure)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                procedure.value(elements[i]);
            }
        }
        else
        {
            RandomAccessListIterate.forEach(list, procedure);
        }
    }

    /**
     * Reverses over the List in reverse order executing the Procedure for each element
     */
    public static <T> void reverseForEach(ArrayList<T> list, Procedure<? super T> procedure)
    {
        if (!list.isEmpty())
        {
            ArrayListIterate.forEach(list, list.size() - 1, 0, procedure);
        }
    }

    /**
     * Iterates over the section of the list covered by the specified indexes.  The indexes are both inclusive.  If the
     * from is less than the to, the list is iterated in forward order. If the from is greater than the to, then the
     * list is iterated in the reverse order.
     * <p/>
     * <p/>
     * <pre>e.g.
     * ArrayList<People> people = new ArrayList<People>(FastList.newListWith(ted, mary, bob, sally));
     * ArrayListIterate.forEach(people, 0, 1, new Procedure<Person>()
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
    public static <T> void forEach(ArrayList<T> list, int from, int to, Procedure<? super T> procedure)
    {
        ListIterate.rangeCheck(from, to, list.size());
        if (ArrayListIterate.isOptimizableArrayList(list, to - from + 1))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);

            InternalArrayIterate.forEachWithoutChecks(elements, from, to, procedure);
        }
        else
        {
            RandomAccessListIterate.forEach(list, from, to, procedure);
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
     * ArrayList<People> people = new ArrayList<People>(FastList.newListWith(ted, mary, bob, sally));
     * ArrayListIterate.forEachWithIndex(people, 0, 1, new ObjectIntProcedure<Person>()
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
    public static <T> void forEachWithIndex(
            ArrayList<T> list,
            int from,
            int to,
            ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.rangeCheck(from, to, list.size());
        if (ArrayListIterate.isOptimizableArrayList(list, to - from + 1))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);

            InternalArrayIterate.forEachWithIndexWithoutChecks(elements, from, to, objectIntProcedure);
        }
        else
        {
            RandomAccessListIterate.forEachWithIndex(list, from, to, objectIntProcedure);
        }
    }

    /**
     * @see ListIterate#forEachInBoth(List, List, Procedure2)
     */
    public static <T1, T2> void forEachInBoth(
            ArrayList<T1> list1,
            ArrayList<T2> list2,
            Procedure2<? super T1, ? super T2> procedure)
    {
        RandomAccessListIterate.forEachInBoth(list1, list2, procedure);
    }

    /**
     * @see Iterate#forEachWithIndex(Iterable, ObjectIntProcedure)
     */
    public static <T> void forEachWithIndex(ArrayList<T> list, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                objectIntProcedure.value(elements[i], i);
            }
        }
        else
        {
            RandomAccessListIterate.forEachWithIndex(list, objectIntProcedure);
        }
    }

    /**
     * @see Iterate#find(Iterable, Predicate)
     */
    public static <T> T find(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                T item = elements[i];
                if (predicate.accept(item))
                {
                    return item;
                }
            }
            return null;
        }
        return RandomAccessListIterate.find(list, predicate);
    }

    /**
     * @see Iterate#findWith(Iterable, Predicate2, Object)
     */
    public static <T, P> T findWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                T item = elements[i];
                if (predicate.accept(item, parameter))
                {
                    return item;
                }
            }
            return null;
        }
        return RandomAccessListIterate.findWith(list, predicate, parameter);
    }

    /**
     * @see Iterate#findIfNone(Iterable, Predicate, Object)
     */
    public static <T> T findIfNone(ArrayList<T> list, Predicate<? super T> predicate, T ifNone)
    {
        T result = ArrayListIterate.find(list, predicate);
        return result == null ? ifNone : result;
    }

    /**
     * @see Iterate#findWithIfNone(Iterable, Predicate2, Object, Object)
     */
    public static <T, IV> T findIfWithNone(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue,
            T ifNone)
    {
        T result = ArrayListIterate.findWith(list, predicate, injectedValue);
        return result == null ? ifNone : result;
    }

    /**
     * @see Iterate#foldLeft(Object, Iterable, Function2)
     */
    public static <T, IV> IV foldLeft(
            IV injectValue,
            ArrayList<T> list,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            IV result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.value(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.foldLeft(injectValue, list, function);
    }

    /**
     * @see Iterate#foldLeft(int, Iterable, IntObjectToIntFunction)
     */
    public static <T> int foldLeft(
            int injectValue,
            ArrayList<T> list,
            IntObjectToIntFunction<? super T> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            int result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.intValueOf(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.foldLeft(injectValue, list, function);
    }

    /**
     * @see Iterate#foldLeft(long, Iterable, LongObjectToLongFunction)
     */
    public static <T> long foldLeft(
            long injectValue,
            ArrayList<T> list,
            LongObjectToLongFunction<? super T> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            long result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.longValueOf(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.foldLeft(injectValue, list, function);
    }

    /**
     * @see Iterate#foldLeft(double, Iterable, DoubleObjectToDoubleFunction)
     */
    public static <T> double foldLeft(
            double injectValue,
            ArrayList<T> list,
            DoubleObjectToDoubleFunction<? super T> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            double result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.doubleValueOf(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.foldLeft(injectValue, list, function);
    }

    /**
     * @see Iterate#anySatisfy(Iterable, Predicate)
     */
    public static <T> boolean anySatisfy(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    return true;
                }
            }
            return false;
        }
        return RandomAccessListIterate.anySatisfy(list, predicate);
    }

    /**
     * @see Iterate#anySatisfyWith(Iterable, Predicate2, Object)
     */
    public static <T, P> boolean anySatisfyWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], parameter))
                {
                    return true;
                }
            }
            return false;
        }
        return RandomAccessListIterate.anySatisfyWith(list, predicate, parameter);
    }

    /**
     * @see Iterate#allSatisfy(Iterable, Predicate)
     */
    public static <T> boolean allSatisfy(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i]))
                {
                    return false;
                }
            }
            return true;
        }
        return RandomAccessListIterate.allSatisfy(list, predicate);
    }

    /**
     * @see Iterate#allSatisfyWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> boolean allSatisfyWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i], injectedValue))
                {
                    return false;
                }
            }
            return true;
        }
        return RandomAccessListIterate.allSatisfyWith(list, predicate, injectedValue);
    }

    /**
     * @see Iterate#partitionWith(Iterable, Predicate2, Object)
     */
    public static <T, P> Twin<MutableList<T>> partitionWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            MutableList<T> positiveResult = Lists.mutable.of();
            MutableList<T> negativeResult = Lists.mutable.of();
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                (predicate.accept(elements[i], parameter) ? positiveResult : negativeResult).add(elements[i]);
            }
            return Tuples.twin(positiveResult, negativeResult);
        }
        return RandomAccessListIterate.partitionWith(list, predicate, parameter);
    }

    /**
     * @see Iterate#partition(Iterable, Predicate)
     */
    public static <T> PartitionMutableList<T> partition(
            ArrayList<T> list,
            Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            PartitionFastList<T> partitionFastList = new PartitionFastList<T>(predicate);
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                partitionFastList.add(elements[i]);
            }
            return partitionFastList;
        }
        return RandomAccessListIterate.partition(list, predicate);
    }

    /**
     * @see Iterate#findIndex(Iterable, Predicate)
     */
    public static <T> int findIndex(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    return i;
                }
            }
            return -1;
        }
        return RandomAccessListIterate.findIndex(list, predicate);
    }

    /**
     * @see Iterate#findIndexWith(Iterable, Predicate2, Object)
     */
    public static <T, P> int findIndexWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], parameter))
                {
                    return i;
                }
            }
            return -1;
        }
        return RandomAccessListIterate.findIndexWith(list, predicate, parameter);
    }

    /**
     * @see Iterate#foldLeftWith(Object, Iterable, Function3, Object)
     */
    public static <T, IV, P> IV foldLeftWith(
            IV injectedValue,
            ArrayList<T> list,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            IV result = injectedValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.value(result, elements[i], parameter);
            }
            return result;
        }
        return RandomAccessListIterate.foldLeftWith(injectedValue, list, function, parameter);
    }

    /**
     * @see Iterate#forEachWith(Iterable, Procedure2, Object)
     */
    public static <T, P> void forEachWith(
            ArrayList<T> list,
            Procedure2<? super T, ? super P> procedure,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                procedure.value(elements[i], parameter);
            }
        }
        else
        {
            RandomAccessListIterate.forEachWith(list, procedure, parameter);
        }
    }

    /**
     * @see Iterate#transformWith(Iterable, Function2, Object)
     */
    public static <T, P, A> ArrayList<A> transformWith(
            ArrayList<T> list,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter)
    {
        return ArrayListIterate.transformWith(list, function, parameter, new ArrayList<A>(list.size()));
    }

    /**
     * @see Iterate#transformWith(Iterable, Function2, Object, Collection)
     */
    public static <T, P, A, R extends Collection<A>> R transformWith(
            ArrayList<T> list,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                targetCollection.add(function.value(elements[i], parameter));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.transformWith(list, function, parameter, targetCollection);
    }

    /**
     * @see Iterate#removeIf(Iterable, Predicate)
     */
    public static <T> ArrayList<T> removeIf(ArrayList<T> list, Predicate<? super T> predicate)
    {
        if (list.getClass() == ArrayList.class && ArrayListIterate.SIZE_FIELD != null)
        {
            int currentFilledIndex = 0;
            int size = list.size();
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i]))
                {
                    // keep it
                    if (currentFilledIndex != i)
                    {
                        elements[currentFilledIndex] = elements[i];
                    }
                    currentFilledIndex++;
                }
            }
            wipeAndResetTheEnd(currentFilledIndex, size, elements, list);
        }
        else
        {
            RandomAccessListIterate.removeIf(list, predicate);
        }
        return list;
    }

    /**
     * @see Iterate#removeIfWith(Iterable, Predicate2, Object)
     */
    public static <T, P> ArrayList<T> removeIfWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (list.getClass() == ArrayList.class && ArrayListIterate.SIZE_FIELD != null)
        {
            int currentFilledIndex = 0;
            int size = list.size();
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i], parameter))
                {
                    // keep it
                    if (currentFilledIndex != i)
                    {
                        elements[currentFilledIndex] = elements[i];
                    }
                    currentFilledIndex++;
                }
            }
            wipeAndResetTheEnd(currentFilledIndex, size, elements, list);
        }
        else
        {
            RandomAccessListIterate.removeIfWith(list, predicate, parameter);
        }
        return list;
    }

    private static <T> void wipeAndResetTheEnd(
            int newCurrentFilledIndex,
            int newSize,
            T[] newElements,
            ArrayList<T> list)
    {
        for (int i = newCurrentFilledIndex; i < newSize; i++)
        {
            newElements[i] = null;
        }
        try
        {
            ArrayListIterate.SIZE_FIELD.setInt(list, newCurrentFilledIndex);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(
                    "Something really bad happened on the way to pounding size into the ArrayList reflectively",
                    e);
        }
    }

    /**
     * Mutates the internal array of the ArrayList by sorting it and then returns the same ArrayList.
     */
    public static <T extends Comparable<? super T>> ArrayList<T> sortThis(ArrayList<T> list)
    {
        return ArrayListIterate.sortThis(list, Comparators.naturalOrder());
    }

    /**
     * Mutates the internal array of the ArrayList by sorting it and then returns the same ArrayList.
     */
    public static <T> ArrayList<T> sortThis(ArrayList<T> list, Comparator<? super T> comparator)
    {
        int size = list.size();
        if (size > 1)
        {
            if (ArrayListIterate.canAccessInternalArray(list))
            {
                ArrayIterate.sort(ArrayListIterate.getInternalArray(list), size, comparator);
            }
            else
            {
                Collections.sort(list, comparator);
            }
        }
        return list;
    }

    public static <T> void toArray(ArrayList<T> list, T[] target, int startIndex, int sourceSize)
    {
        if (ArrayListIterate.canAccessInternalArray(list))
        {
            System.arraycopy(ArrayListIterate.getInternalArray(list), 0, target, startIndex, sourceSize);
        }
        else
        {
            RandomAccessListIterate.toArray(list, target, startIndex, sourceSize);
        }
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T> ArrayList<T> take(ArrayList<T> list, int count)
    {
        return ArrayListIterate.take(list, count, new ArrayList<T>(count));
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T, R extends Collection<T>> R take(ArrayList<T> list, int count, R targetList)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);

            int end = Math.min(size, count);
            for (int i = 0; i < end; i++)
            {
                targetList.add(elements[i]);
            }
            return targetList;
        }
        return RandomAccessListIterate.take(list, count, targetList);
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T> ArrayList<T> drop(ArrayList<T> list, int count)
    {
        return ArrayListIterate.drop(list, count, new ArrayList<T>(list.size() - Math.min(list.size(), count)));
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T, R extends Collection<T>> R drop(ArrayList<T> list, int count, R targetList)
    {
        return RandomAccessListIterate.drop(list, count, targetList);
    }

    /**
     * @see Iterate#groupBy(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupBy(
            ArrayList<T> list,
            Function<? super T, ? extends V> function)
    {
        return ArrayListIterate.groupBy(list, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupBy(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            ArrayList<T> list,
            Function<? super T, ? extends V> function,
            R target)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                target.put(function.valueOf(elements[i]), elements[i]);
            }
            return target;
        }
        return RandomAccessListIterate.groupBy(list, function, target);
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupByEach(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return ArrayListIterate.groupByEach(list, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                Iterable<V> iterable = function.valueOf(elements[i]);
                for (V key : iterable)
                {
                    target.put(key, elements[i]);
                }
            }
            return target;
        }
        return RandomAccessListIterate.groupByEach(list, function, target);
    }

    /**
     * @see Iterate#zip(Iterable, Iterable)
     */
    public static <X, Y> MutableList<Pair<X, Y>> zip(ArrayList<X> xs, Iterable<Y> ys)
    {
        return ArrayListIterate.zip(xs, ys, FastList.<Pair<X, Y>>newList());
    }

    /**
     * @see Iterate#zip(Iterable, Iterable, Collection)
     */
    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(ArrayList<X> xs, Iterable<Y> ys, R targetCollection)
    {
        int size = xs.size();
        if (ArrayListIterate.isOptimizableArrayList(xs, size))
        {
            Iterator<Y> yIterator = ys.iterator();
            X[] elements = ArrayListIterate.getInternalArray(xs);
            for (int i = 0; i < size && yIterator.hasNext(); i++)
            {
                targetCollection.add(Tuples.pair(elements[i], yIterator.next()));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.zip(xs, ys, targetCollection);
    }

    /**
     * @see Iterate#zipWithIndex(Iterable)
     */
    public static <T> MutableList<Pair<T, Integer>> zipWithIndex(ArrayList<T> list)
    {
        return ArrayListIterate.zipWithIndex(list, FastList.<Pair<T, Integer>>newList());
    }

    /**
     * @see Iterate#zipWithIndex(Iterable, Collection)
     */
    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(ArrayList<T> list, R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                targetCollection.add(Tuples.pair(elements[i], i));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.zipWithIndex(list, targetCollection);
    }

    private static boolean canAccessInternalArray(ArrayList<?> list)
    {
        return ArrayListIterate.ELEMENT_DATA_FIELD != null && list.getClass() == ArrayList.class;
    }

    private static boolean isOptimizableArrayList(ArrayList<?> list, int newSize)
    {
        return newSize > MIN_DIRECT_ARRAY_ACCESS_SIZE
                && ArrayListIterate.ELEMENT_DATA_FIELD != null
                && list.getClass() == ArrayList.class;
    }

    private static <T> T[] getInternalArray(ArrayList<T> list)
    {
        try
        {
            return (T[]) ArrayListIterate.ELEMENT_DATA_FIELD.get(list);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
