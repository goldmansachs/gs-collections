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

package com.gs.collections.api.set;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.set.UnsortedSetMultimap;

/**
 * A ParallelIterable is RichIterable which will defer evaluation for certain methods like select, reject, collect, etc.
 * Any methods that do not return a ParallelIterable when called will cause evaluation to be forced. Evaluation occurs
 * in parallel. All code blocks passed in must be stateless or thread-safe.
 *
 * @since 5.0
 */
@Beta
public interface ParallelUnsortedSetIterable<T> extends ParallelSetIterable<T>
{
    ParallelUnsortedSetIterable<T> asUnique();

    /**
     * Creates a parallel iterable for selecting elements from the current iterable.
     */
    ParallelUnsortedSetIterable<T> select(Predicate<? super T> predicate);

    <P> ParallelUnsortedSetIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Creates a parallel iterable for rejecting elements from the current iterable.
     */
    ParallelUnsortedSetIterable<T> reject(Predicate<? super T> predicate);

    <P> ParallelUnsortedSetIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ParallelUnsortedSetIterable<S> selectInstancesOf(Class<S> clazz);

    <V> UnsortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> UnsortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

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
}
