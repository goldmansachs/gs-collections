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

package com.gs.collections.api;

import java.util.Comparator;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;

/**
 * A ParallelIterable is RichIterable which will defer evaluation for certain methods like select, reject, collect, etc.
 * Any methods that do not return a ParallelIterable when called will cause evaluation to be forced. Evaluation occurs
 * in parallel. All code blocks passed in must be stateless or thread-safe.
 *
 * @since 5.0
 */
@Beta
public interface ParallelIterable<T>
{
    ParallelIterable<T> asUnique();

    /**
     * Creates a parallel iterable for selecting elements from the current iterable.
     */
    ParallelIterable<T> select(Predicate<? super T> predicate);

    <P> ParallelIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ParallelIterable<S> selectInstancesOf(Class<S> clazz);

    /**
     * Creates a parallel iterable for rejecting elements from the current iterable.
     */
    ParallelIterable<T> reject(Predicate<? super T> predicate);

    <P> ParallelIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Creates a parallel iterable for collecting elements from the current iterable.
     */
    <V> ParallelIterable<V> collect(Function<? super T, ? extends V> function);

    <P, V> ParallelIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    /**
     * Creates a parallel iterable for selecting and collecting elements from the current iterable.
     */
    <V> ParallelIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    /**
     * Creates a parallel flattening iterable for the current iterable.
     */
    <V> ParallelIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

//    /**
//     * Returns a parallel BooleanIterable which will transform the underlying iterable data to boolean values based on the booleanFunction.
//     */
//    ParallelBooleanIterable collectBoolean(BooleanFunction<? super T> booleanFunction);
//
//    /**
//     * Returns a parallel ByteIterable which will transform the underlying iterable data to byte values based on the byteFunction.
//     */
//    ParallelByteIterable collectByte(ByteFunction<? super T> byteFunction);
//
//    /**
//     * Returns a parallel CharIterable which will transform the underlying iterable data to char values based on the charFunction.
//     */
//    ParallelCharIterable collectChar(CharFunction<? super T> charFunction);
//
//    /**
//     * Returns a parallel DoubleIterable which will transform the underlying iterable data to double values based on the doubleFunction.
//     */
//    ParallelDoubleIterable collectDouble(DoubleFunction<? super T> doubleFunction);
//
//    /**
//     * Returns a parallel FloatIterable which will transform the underlying iterable data to float values based on the floatFunction.
//     */
//    ParallelFloatIterable collectFloat(FloatFunction<? super T> floatFunction);
//
//    /**
//     * Returns a parallel IntIterable which will transform the underlying iterable data to int values based on the intFunction.
//     */
//    ParallelIntIterable collectInt(IntFunction<? super T> intFunction);
//
//    /**
//     * Returns a parallel LongIterable which will transform the underlying iterable data to long values based on the longFunction.
//     */
//    ParallelLongIterable collectLong(LongFunction<? super T> longFunction);
//
//    /**
//     * Returns a parallel ShortIterable which will transform the underlying iterable data to short values based on the shortFunction.
//     */
//    ParallelShortIterable collectShort(ShortFunction<? super T> shortFunction);

    void forEach(Procedure<? super T> procedure);

    <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter);

    T detect(Predicate<? super T> predicate);

    <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function);

    <P> T detectWithIfNone(Predicate2<? super T, ? super P> predicate, P parameter, Function0<? extends T> function);

    int count(Predicate<? super T> predicate);

    <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter);

    boolean anySatisfy(Predicate<? super T> predicate);

    <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter);

    boolean allSatisfy(Predicate<? super T> predicate);

    <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter);

    boolean noneSatisfy(Predicate<? super T> predicate);

    <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableList<T> toList();

    MutableList<T> toSortedList();

    MutableList<T> toSortedList(Comparator<? super T> comparator);

    <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function);

    MutableSet<T> toSet();

    MutableSortedSet<T> toSortedSet();

    MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator);

    <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function);

    MutableBag<T> toBag();

    MutableSortedBag<T> toSortedBag();

    MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator);

    <V extends Comparable<? super V>> MutableSortedBag<T> toSortedBagBy(Function<? super T, ? extends V> function);

    <NK, NV> MutableMap<NK, NV> toMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction);

    <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction);

    <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator, Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction);

    Object[] toArray();

    <T1> T1[] toArray(T1[] target);

    T min(Comparator<? super T> comparator);

    T max(Comparator<? super T> comparator);

    T min();

    T max();

    <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function);

    <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function);

    /**
     * Returns the final long result of evaluating function for each element of the iterable in parallel
     * and adding the results together.
     *
     * @since 6.0
     */
    long sumOfInt(IntFunction<? super T> function);

    /**
     * Returns the final double result of evaluating function for each element of the iterable in parallel
     * and adding the results together. It uses Kahan summation algorithm to reduce numerical error.
     *
     * @since 6.0
     */
    double sumOfFloat(FloatFunction<? super T> function);

    /**
     * Returns the final long result of evaluating function for each element of the iterable in parallel
     * and adding the results together.
     *
     * @since 6.0
     */
    long sumOfLong(LongFunction<? super T> function);

    /**
     * Returns the final double result of evaluating function for each element of the iterable in parallel
     * and adding the results together. It uses Kahan summation algorithm to reduce numerical error.
     *
     * @since 6.0
     */
    double sumOfDouble(DoubleFunction<? super T> function);

    String makeString();

    String makeString(String separator);

    String makeString(String start, String separator, String end);

    void appendString(Appendable appendable);

    void appendString(Appendable appendable, String separator);

    void appendString(Appendable appendable, String start, String separator, String end);

    <V> Multimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> Multimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function);

    <K, V> MapIterable<K, V> aggregateInPlaceBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Procedure2<? super V, ? super T> mutatingAggregator);

    <K, V> MapIterable<K, V> aggregateBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);
}
