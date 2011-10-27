/*
 * Copyright 2011 Goldman Sachs & Co.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.block.procedure.MaxComparatorProcedure;
import com.gs.collections.impl.block.procedure.MinComparatorProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.internal.InternalArrayIterate;

/**
 * The ArrayIterate class provides a few of the methods from the Smalltalk Collection Protocol. This includes do:,
 * select:, reject:, collect:, inject:into:, detect:, detect:ifNone:, anySatisfy: and allSatisfy:
 */
public final class ArrayIterate
{
    private static final int INSERTIONSORT_THRESHOLD = 10;

    private ArrayIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> void reverse(T[] array, int size)
    {
        int mid = size / 2;
        int j = size - 1;
        for (int i = 0; i < mid; i++, j--)
        {
            T one = array[i];
            T two = array[j];
            array[i] = two;
            array[j] = one;
        }
    }

    public static <T> void sort(T[] array, int size, Comparator<? super T> comparator)
    {
        if (size < INSERTIONSORT_THRESHOLD)
        {
            if (comparator == null)
            {
                ArrayIterate.insertionSort(array, size);
            }
            else
            {
                ArrayIterate.insertionSort(array, size, comparator);
            }
        }
        else
        {
            if (comparator == null)
            {
                Arrays.sort(array, 0, size);
            }
            else
            {
                Arrays.sort(array, 0, size, comparator);
            }
        }
    }

    private static <T> void insertionSort(T[] array, int size, Comparator<? super T> comparator)
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = i; j > 0 && comparator.compare(array[j - 1], array[j]) > 0; j--)
            {
                swapWithPrevious(array, j);
            }
        }
    }

    private static <T> void insertionSort(T[] array, int size)
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = i; j > 0 && ((Comparable<T>) array[j - 1]).compareTo(array[j]) > 0; j--)
            {
                swapWithPrevious(array, j);
            }
        }
    }

    private static <T> void swapWithPrevious(T[] array, int index)
    {
        T t = array[index];
        array[index] = array[(index - 1)];
        array[(index - 1)] = t;
    }

    public static <T> T min(T[] array, Comparator<? super T> comparator)
    {
        MinComparatorProcedure<T> procedure = new MinComparatorProcedure<T>(comparator);
        ArrayIterate.forEach(array, procedure);
        return procedure.getResult();
    }

    public static <T> T max(T[] array, Comparator<? super T> comparator)
    {
        MaxComparatorProcedure<T> procedure = new MaxComparatorProcedure<T>(comparator);
        ArrayIterate.forEach(array, procedure);
        return procedure.getResult();
    }

    public static <T> T min(T[] array)
    {
        return ArrayIterate.min(array, Comparators.naturalOrder());
    }

    public static <T> T max(T[] array)
    {
        return ArrayIterate.max(array, Comparators.naturalOrder());
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true is included in the new collection.  Unless specifically refined,
     * this message is defined to answer an object conforming to the same protocol as the receiver. If both the receiver
     * and the result maintain an ordering of their elements, the elements of the result will be in the same relative
     * order as the elements of the receiver.
     */
    public static <T> MutableList<T> select(
            T[] objectArray,
            Predicate<? super T> predicate)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a select on null");
        }

        return ArrayIterate.select(objectArray, predicate, FastList.<T>newList());
    }

    public static <T, IV> MutableList<T> selectWith(
            T[] objectArray,
            Predicate2<? super T, IV> predicate,
            IV injectedValue)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a selectWith on null");
        }

        return ArrayIterate.selectWith(
                objectArray,
                predicate,
                injectedValue,
                FastList.<T>newList());
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <T> int count(T[] objectArray, Predicate<? super T> predicate)
    {
        int count = 0;
        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                if (predicate.accept(each))
                {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <T, IV> int countWith(
            T[] objectArray,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int count = 0;
        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                if (predicate.accept(each, injectedValue))
                {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Filters an array into a positive and negative collection based on the result of applying the specified
     * Predicate
     */
    public static <T, IV> Twin<MutableList<T>> selectAndRejectWith(
            T[] objectArray,
            Predicate2<? super T, ? super IV> predicate, IV injectedValue)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a selectAndRejectWith on null");
        }

        MutableList<T> positiveResult = Lists.mutable.of();
        MutableList<T> negativeResult = Lists.mutable.of();
        for (T each : objectArray)
        {
            (predicate.accept(each, injectedValue) ? positiveResult : negativeResult).add(each);
        }
        return Tuples.twin(positiveResult, negativeResult);
    }

    /**
     * Filters an array into a positive and negative collection based on the result of applying the specified
     * Predicate
     */
    public static <T> PartitionMutableList<T> partition(
            T[] objectArray,
            Predicate<? super T> predicate)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a partition on null");
        }

        PartitionMutableList<T> partition = new PartitionFastList<T>(predicate);

        for (T each : objectArray)
        {
            partition.add(each);
        }
        return partition;
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true has the specified Function applied it and the result
     * is added to the target collection. Unless specifically refined, this message is defined to answer an object
     * conforming to the same protocol as the receiver. If both the receiver and the result maintain an ordering of
     * their elements, the elements of the result will be in the same relative order as the elements of the receiver.
     */
    public static <T, A> MutableList<A> collectIf(
            T[] objectArray,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a collectIf on null");
        }

        return ArrayIterate.<T, A, FastList<A>>collectIf(
                objectArray,
                predicate,
                function,
                FastList.<A>newList(objectArray.length));
    }

    /**
     * Same as the select method with two parameters but uses the specified target collection
     */
    public static <T, R extends Collection<T>> R select(
            T[] objectArray,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a select on null");
        }

        for (T each : objectArray)
        {
            if (predicate.accept(each))
            {
                targetCollection.add(each);
            }
        }
        return targetCollection;
    }

    public static <T, IV, R extends Collection<T>> R selectWith(
            T[] objectArray,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue,
            R targetCollection)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a selectWith on null");
        }

        for (T each : objectArray)
        {
            if (predicate.accept(each, injectedValue))
            {
                targetCollection.add(each);
            }
        }
        return targetCollection;
    }

    /**
     * Same as the select method with three parameters but uses the specified target collection
     */
    public static <T, A, R extends Collection<A>> R collectIf(
            T[] objectArray,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a collectIf on null");
        }

        for (T each : objectArray)
        {
            if (predicate.accept(each))
            {
                targetCollection.add(function.valueOf(each));
            }
        }
        return targetCollection;
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to false is included in the new collection. Unless specifically refined,
     * this message is defined to answer an object conforming to the same protocol as the receiver. If both the receiver
     * and the result maintain an ordering of their elements, the elements of the result will be in the same relative
     * order as the elements of the receiver.
     */
    public static <T> MutableList<T> reject(T[] objectArray, Predicate<? super T> predicate)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a reject on null");
        }

        return ArrayIterate.reject(objectArray, predicate, FastList.<T>newList());
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to false is included in the new collection. Unless specifically refined,
     * this message is defined to answer an object conforming to the same protocol as the receiver. If both the receiver
     * and the result maintain an ordering of their elements, the elements of the result will be in the same relative
     * order as the elements of the receiver.
     */
    public static <T, IV> MutableList<T> rejectWith(
            T[] objectArray,
            Predicate2<? super T, IV> predicate,
            IV injectedValue)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a rejectWith on null");
        }

        return ArrayIterate.rejectWith(
                objectArray,
                predicate,
                injectedValue,
                FastList.<T>newList());
    }

    /**
     * Same as the reject method with two parameters but uses the specified target collection
     */
    public static <T, R extends Collection<T>> R reject(
            T[] objectArray,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a reject on null");
        }

        for (T each : objectArray)
        {
            if (!predicate.accept(each))
            {
                targetCollection.add(each);
            }
        }
        return targetCollection;
    }

    /**
     * Same as the reject method with two parameters but uses the specified target collection
     */
    public static <T, IV, R extends Collection<T>> R rejectWith(
            T[] objectArray,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue,
            R targetCollection)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a rejectWith on null");
        }

        for (T each : objectArray)
        {
            if (!predicate.accept(each, injectedValue))
            {
                targetCollection.add(each);
            }
        }
        return targetCollection;
    }

    public static <T, R extends Collection<T>> R addAllTo(T[] objectArray, R targetCollection)
    {
        ArrayIterate.forEach(objectArray, CollectionAddProcedure.<T>on(targetCollection));
        return targetCollection;
    }

    /**
     * For each element of the receiver, transformer is evaluated with the element as the parameter. The results of
     * these evaluations are collected into a new collection. Unless specifically refined, this message is defined to
     * answer an object conforming to the same protocol as the receiver
     */
    public static <T, A> MutableList<A> collect(
            T[] objectArray,
            Function<? super T, A> function)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a collect on null");
        }

        return ArrayIterate.collect(objectArray, function, FastList.<A>newList(objectArray.length));
    }

    /**
     * Same as the collect method with two parameters, except that the results are gathered into the specified
     * targetCollection
     */
    public static <T, A, R extends Collection<A>> R collect(
            T[] objectArray,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a collect on null");
        }

        for (T each : objectArray)
        {
            targetCollection.add(function.valueOf(each));
        }
        return targetCollection;
    }

    /**
     * Returns the first element of an array.  This method is null safe.
     */
    public static <T> T getFirst(T[] objectArray)
    {
        if (ArrayIterate.notEmpty(objectArray))
        {
            return objectArray[0];
        }
        return null;
    }

    /**
     * Returns the last element of an Array.  This method is null safe.
     */
    public static <T> T getLast(T[] objectArray)
    {
        if (ArrayIterate.notEmpty(objectArray))
        {
            return objectArray[objectArray.length - 1];
        }
        return null;
    }

    /**
     * For each element of the receiver, operation is evaluated with the element as the parameter. Unless specifically
     * refined, the elements are not traversed in a particular order. Each element is visited exactly once.
     */
    public static <T> void forEach(T[] objectArray, Procedure<? super T> procedure)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEach on null");
        }

        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                procedure.value(each);
            }
        }
    }

    /**
     * Iterates over the section of the list covered by the specified inclusive indexes.  The indexes are
     * both inclusive.
     */
    public static <T> void forEach(T[] objectArray, int from, int to, Procedure<? super T> procedure)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEach on null");
        }

        ListIterate.rangeCheck(from, to, objectArray.length);
        InternalArrayIterate.forEachWithoutChecks(objectArray, from, to, procedure);
    }

    /**
     * For each element in both of the arrays, operation is evaluated with both elements as parameters.
     */
    public static <T1, T2> void forEachInBoth(
            T1[] objectArray1,
            T2[] objectArray2,
            Procedure2<T1, T2> procedure)
    {
        if (objectArray1 != null && objectArray2 != null)
        {
            if (objectArray1.length == objectArray2.length)
            {
                int size = objectArray1.length;
                for (int i = 0; i < size; i++)
                {
                    procedure.value(objectArray1[i], objectArray2[i]);
                }
            }
            else
            {
                throw new RuntimeException("Attempt to call forEachInBoth with two arrays of different sizes :"
                        + objectArray1.length + ':' + objectArray2.length);
            }
        }
    }

    /**
     * For each element of the receiver, operation is evaluated with the element and current index as parameters. Unless
     * specifically refined, the elements are not traversed in a particular order. Each element is visited exactly once.
     */
    public static <T> void forEachWithIndex(T[] objectArray, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEachWithIndex on null");
        }

        int size = objectArray.length;
        for (int i = 0; i < size; i++)
        {
            objectIntProcedure.value(objectArray[i], i);
        }
    }

    /**
     * Iterates over the section of the list covered by the specified inclusive indexes.  The indexes are
     * both inclusive.
     */
    public static <T> void forEachWithIndex(
            T[] objectArray,
            int from, int to,
            ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEachWithIndex on null");
        }

        ListIterate.rangeCheck(from, to, objectArray.length);
        InternalArrayIterate.forEachWithIndexWithoutChecks(objectArray, from, to, objectIntProcedure);
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the array have been used as arguments. That is, there may be elements of the receiver that are never used as
     * arguments to the predicate. The result is null if predicate does not evaluate to true for any element.
     */
    public static <T> T detect(T[] objectArray, Predicate<? super T> predicate)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a detect on null");
        }

        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                if (predicate.accept(each))
                {
                    return each;
                }
            }
        }
        return null;
    }

    public static <T, V> T detectWith(
            T[] objectArray,
            Predicate2<? super T, ? super V> predicate,
            V value)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a detectWith on null");
        }

        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                if (predicate.accept(each, value))
                {
                    return each;
                }
            }
        }
        return null;
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the array have been used as arguments. That is, there may be elements of the receiver that are never used as
     * arguments to the predicate. If no element causes predicate to evaluate to true, answer the result of
     * exceptionHandler value.
     */
    public static <T> T detectIfNone(
            T[] objectArray,
            Predicate<? super T> predicate,
            T ifNone)
    {
        T result = ArrayIterate.detect(objectArray, predicate);
        return result == null ? ifNone : result;
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the array have been used as arguments. That is, there may be elements of the receiver that are never used as
     * arguments to the predicate. If no element causes predicate to evaluate to true, answer the result of
     * exceptionHandler value.
     */
    public static <T, IV> T detectWithIfNone(
            T[] objectArray,
            Predicate2<? super T, IV> predicate,
            IV injectedValue,
            T ifNone)
    {
        T result = ArrayIterate.detectWith(objectArray, predicate, injectedValue);
        return result == null ? ifNone : result;
    }

    /**
     * Returns the final result of evaluating function using each element of the receiver and the previous evaluation
     * result as the parameters. The first evaluation of function is performed with initialValue as the first
     * parameter, and the first element of the receiver as the second parameter. Subsequent evaluations are done with
     * the result of the previous evaluation as the first parameter, and the next element as the second parameter. The
     * result of the last evaluation is answered.
     */
    public static <T, IV> IV injectInto(
            IV injectValue,
            T[] objectArray,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform an injectInto on null");
        }

        IV result = injectValue;
        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                result = function.value(result, each);
            }
        }
        return result;
    }

    /**
     * Returns the final result of evaluating function using each element of the receiver and the previous evaluation
     * result as the parameters. The first evaluation of function is performed with initialValue as the first
     * parameter, and the first element of the receiver as the second parameter. Subsequent evaluations are done with
     * the result of the previous evaluation as the first parameter, and the next element as the second parameter. The
     * result of the last evaluation is answered.
     */
    public static <T> int injectInto(
            int injectValue,
            T[] objectArray,
            IntObjectToIntFunction<? super T> function)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform an injectInto on null");
        }

        int result = injectValue;
        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                result = function.intValueOf(result, each);
            }
        }
        return result;
    }

    /**
     * Returns the final result of evaluating function using each element of the receiver and the previous evaluation
     * result as the parameters. The first evaluation of function is performed with initialValue as the first
     * parameter, and the first element of the receiver as the second parameter. Subsequent evaluations are done with
     * the result of the previous evaluation as the first parameter, and the next element as the second parameter. The
     * result of the last evaluation is answered.
     */
    public static <T> long injectInto(
            long injectValue,
            T[] objectArray,
            LongObjectToLongFunction<? super T> function)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform an injectInto on null");
        }

        long result = injectValue;
        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                result = function.longValueOf(result, each);
            }
        }
        return result;
    }

    /**
     * Returns <tt>true</tt> if the specified array contains the specified element.
     */
    public static <T> boolean contains(T[] objectArray, T value)
    {
        return ArrayIterate.anySatisfyWith(objectArray, Predicates2.equal(), value);
    }

    /**
     * Returns <tt>true</tt> if the specified array contains the specified element.
     */
    public static boolean contains(int[] intArray, int value)
    {
        for (int each : intArray)
        {
            if (each == value)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <tt>true</tt> if the specified array contains the specified element.
     */
    public static boolean contains(double[] doubleArray, double value)
    {
        for (double each : doubleArray)
        {
            if (Double.doubleToLongBits(each) == Double.doubleToLongBits(value))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <tt>true</tt> if the specified array contains the specified element.
     */
    public static boolean contains(long[] longArray, long value)
    {
        for (long each : longArray)
        {
            if (each == value)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for the first occurrence of the given argument, testing
     * for equality using the <tt>equals</tt> method.
     */
    public static <T> int indexOf(T[] objectArray, T elem)
    {
        return ArrayIterate.detectIndex(objectArray, Predicates.equal(elem));
    }

    /**
     * Searches for the first occurrence where the predicate evaluates to true.
     */
    public static <T> int detectIndex(T[] objectArray, Predicate<? super T> predicate)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a detectIndex on null");
        }

        for (int i = 0; i < objectArray.length; i++)
        {
            if (predicate.accept(objectArray[i]))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches for the first occurrence where the predicate evaluates to true.
     */
    public static <T, IV> int detectIndexWith(
            T[] objectArray,
            Predicate2<? super T, IV> predicate,
            IV injectedValue)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a detectIndexWith on null");
        }

        for (int i = 0; i < objectArray.length; i++)
        {
            if (predicate.accept(objectArray[i], injectedValue))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * A null safe form of isEmpty.  Null or length of zero returns true.
     */
    public static boolean isEmpty(Object[] array)
    {
        return array == null || array.length == 0;
    }

    /**
     * A null safe form of isEmpty.  The opposite of isEmpty is returned.
     */
    public static boolean notEmpty(Object[] array)
    {
        return array != null && array.length > 0;
    }

    /**
     * Return the size of the array
     */
    public static int size(Object[] array)
    {
        return array == null ? 0 : array.length;
    }

    /**
     * Return true if the predicate evaluates to true for any element of the receiver. Otherwise return false.
     * Return false if the receiver is empty.
     */
    public static <T> boolean anySatisfy(T[] objectArray, Predicate<? super T> predicate)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a anySatisfy on null");
        }

        for (T each : objectArray)
        {
            if (predicate.accept(each))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if the predicate evaluates to true for any element of the receiver. Otherwise return false.
     * Return false if the receiver is empty.
     */
    public static <T, IV> boolean anySatisfyWith(
            T[] objectArray,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a anySatisfyWith on null");
        }

        for (T each : objectArray)
        {
            if (predicate.accept(each, injectedValue))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if the predicate evaluates to true for every element of the receiver. Otherwise return false.
     */
    public static <T> boolean allSatisfy(T[] objectArray, Predicate<? super T> predicate)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a allSatisfy on null");
        }

        for (T each : objectArray)
        {
            if (!predicate.accept(each))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true if the predicate evaluates to true for every element of the receiver. Otherwise return false.
     */
    public static <T, IV> boolean allSatisfyWith(
            T[] objectArray,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a allSatisfyWith on null");
        }

        for (T each : objectArray)
        {
            if (!predicate.accept(each, injectedValue))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Iterate over the specified array applying the specified Function to each element to calculate a key
     * and return the results as a HashMap.
     */
    public static <K, V> MutableMap<K, V> toMap(
            V[] objectArray,
            Function<? super V, K> keyFunction)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        Procedure<V> procedure = new MapCollectProcedure<V, K, V>(map, keyFunction);
        ArrayIterate.forEach(objectArray, procedure);
        return map;
    }

    public static <T, P> void forEachWith(
            T[] objectArray,
            Procedure2<? super T, ? super P> procedure,
            P parameter)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEachWith on null");
        }

        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                procedure.value(each, parameter);
            }
        }
    }

    public static <T, P, A> MutableList<A> collectWith(
            T[] objectArray,
            Function2<? super T, P, A> function,
            P parameter)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a collectWith on null");
        }

        return ArrayIterate.collectWith(objectArray, function, parameter, FastList.<A>newList(objectArray.length));
    }

    public static <T, P, A, R extends Collection<A>> R collectWith(
            T[] objectArray,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform a collectWith on null");
        }

        for (T each : objectArray)
        {
            targetCollection.add(function.value(each, parameter));
        }
        return targetCollection;
    }

    public static <T, IV, P> IV injectIntoWith(
            IV injectValue,
            T[] objectArray,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        if (objectArray == null)
        {
            throw new IllegalArgumentException("Cannot perform an injectIntoWith on null");
        }

        IV result = injectValue;
        if (ArrayIterate.notEmpty(objectArray))
        {
            for (T each : objectArray)
            {
                result = function.value(result, each, parameter);
            }
        }
        return result;
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T> MutableList<T> take(T[] array, int count)
    {
        return ArrayIterate.take(array, count, FastList.<T>newList(count));
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T, R extends Collection<T>> R take(T[] array, int count, R target)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }

        int end = Math.min(array.length, count);
        for (int i = 0; i < end; i++)
        {
            target.add(array[i]);
        }
        return target;
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T> MutableList<T> drop(T[] array, int count)
    {
        return ArrayIterate.drop(array, count, FastList.<T>newList(array.length - Math.min(array.length, count)));
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T, R extends Collection<T>> R drop(T[] array, int count, R target)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }

        for (int i = count; i < array.length; i++)
        {
            target.add(array[i]);
        }
        return target;
    }

    /**
     * @see Iterate#groupBy(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupBy(
            T[] array,
            Function<? super T, ? extends V> function)
    {
        return ArrayIterate.groupBy(array, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupBy(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            T[] array,
            Function<? super T, ? extends V> function,
            R target)
    {
        for (int i = 0; i < array.length; i++)
        {
            target.put(function.valueOf(array[i]), array[i]);
        }
        return target;
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupByEach(
            T[] array,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return ArrayIterate.groupByEach(array, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            T[] array,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        for (int i = 0; i < array.length; i++)
        {
            Iterable<V> iterable = function.valueOf(array[i]);
            for (V key : iterable)
            {
                target.put(key, array[i]);
            }
        }
        return target;
    }

    public static <T> RichIterable<RichIterable<T>> chunk(T[] array, int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        int index = 0;
        MutableList<RichIterable<T>> result = Lists.mutable.of();
        while (index < array.length)
        {
            MutableList<T> batch = Lists.mutable.of();
            for (int i = 0; i < size && index < array.length; i++)
            {
                batch.add(array[i]);
                index++;
            }
            result.add(batch);
        }
        return result;
    }

    public static <X, Y> MutableList<Pair<X, Y>> zip(X[] xs, Y[] ys)
    {
        return ArrayIterate.zip(xs, ys, FastList.<Pair<X, Y>>newList());
    }

    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(X[] xs, Y[] ys, R targetCollection)
    {
        int size = Math.min(xs.length, ys.length);
        for (int i = 0; i < size; i++)
        {
            targetCollection.add(Tuples.pair(xs[i], ys[i]));
        }
        return targetCollection;
    }

    public static <T> MutableList<Pair<T, Integer>> zipWithIndex(T[] array)
    {
        return ArrayIterate.zipWithIndex(array, FastList.<Pair<T, Integer>>newList());
    }

    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(T[] array, R targetCollection)
    {
        for (int i = 0; i < array.length; i++)
        {
            targetCollection.add(Tuples.pair(array[i], i));
        }
        return targetCollection;
    }
}
