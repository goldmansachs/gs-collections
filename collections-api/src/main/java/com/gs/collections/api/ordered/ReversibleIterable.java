/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.api.ordered;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.multimap.ordered.ReversibleIterableMultimap;
import com.gs.collections.api.ordered.primitive.ReversibleBooleanIterable;
import com.gs.collections.api.ordered.primitive.ReversibleByteIterable;
import com.gs.collections.api.ordered.primitive.ReversibleCharIterable;
import com.gs.collections.api.ordered.primitive.ReversibleDoubleIterable;
import com.gs.collections.api.ordered.primitive.ReversibleFloatIterable;
import com.gs.collections.api.ordered.primitive.ReversibleIntIterable;
import com.gs.collections.api.ordered.primitive.ReversibleLongIterable;
import com.gs.collections.api.ordered.primitive.ReversibleShortIterable;
import com.gs.collections.api.partition.ordered.PartitionReversibleIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * A ReversibleIterable is an ordered iterable that you can iterate over forwards or backwards. Besides being ordered,
 * it has methods that support efficient iteration from the end, including {@link #asReversed()} and
 * {@link #reverseForEach(Procedure)}.
 *
 * @since 5.0
 */
public interface ReversibleIterable<T> extends OrderedIterable<T>
{
    /**
     * Evaluates the procedure for each element of the list iterating in reverse order.
     * <p>
     * <pre>e.g.
     * people.reverseForEach(new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *         LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     */
    void reverseForEach(Procedure<? super T> procedure);

    /**
     * Returns a reversed view of this ReversibleIterable.
     */
    LazyIterable<T> asReversed();

    /**
     * Returns a new ReversibleIterable in reverse order.
     *
     * @since 6.0.0
     */
    ReversibleIterable<T> toReversed();

    /**
     * Returns the index of the last element of the {@code ReversibleIterable} for which the {@code predicate} evaluates to true.
     * Returns -1 if no element evaluates true for the {@code predicate}.
     *
     * @since 6.0
     */
    int detectLastIndex(Predicate<? super T> predicate);

    /**
     * Returns the first {@code count} elements of the iterable
     * or all the elements in the iterable if {@code count} is greater than the length of
     * the iterable.
     *
     * @param count the number of items to take.
     * @throws IllegalArgumentException if {@code count} is less than zero
     * @since 6.0
     */
    ReversibleIterable<T> take(int count);

    /**
     * Returns the initial elements that satisfy the Predicate. Short circuits at the first element which does not
     * satisfy the Predicate.
     */
    ReversibleIterable<T> takeWhile(Predicate<? super T> predicate);

    /**
     * Returns an iterable after skipping the first {@code count} elements
     * or an empty iterable if the {@code count} is greater than the length of the iterable.
     *
     * @param count the number of items to drop.
     * @throws IllegalArgumentException if {@code count} is less than zero
     * @since 6.0
     */
    ReversibleIterable<T> drop(int count);

    /**
     * Returns the final elements that do not satisfy the Predicate. Short circuits at the first element which does
     * satisfy the Predicate.
     */
    ReversibleIterable<T> dropWhile(Predicate<? super T> predicate);

    PartitionReversibleIterable<T> partitionWhile(Predicate<? super T> predicate);

    ReversibleIterable<T> distinct();

    ReversibleIterable<T> select(Predicate<? super T> predicate);

    <P> ReversibleIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ReversibleIterable<T> reject(Predicate<? super T> predicate);

    <P> ReversibleIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionReversibleIterable<T> partition(Predicate<? super T> predicate);

    <P> PartitionReversibleIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ReversibleIterable<S> selectInstancesOf(Class<S> clazz);

    <V> ReversibleIterable<V> collect(Function<? super T, ? extends V> function);

    <P, V> ReversibleIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ReversibleIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ReversibleIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    ReversibleBooleanIterable collectBoolean(BooleanFunction<? super T> booleanFunction);

    ReversibleByteIterable collectByte(ByteFunction<? super T> byteFunction);

    ReversibleCharIterable collectChar(CharFunction<? super T> charFunction);

    ReversibleDoubleIterable collectDouble(DoubleFunction<? super T> doubleFunction);

    ReversibleFloatIterable collectFloat(FloatFunction<? super T> floatFunction);

    ReversibleIntIterable collectInt(IntFunction<? super T> intFunction);

    ReversibleLongIterable collectLong(LongFunction<? super T> longFunction);

    ReversibleShortIterable collectShort(ShortFunction<? super T> shortFunction);

    <V> ReversibleIterableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ReversibleIterableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ReversibleIterable<Pair<T, S>> zip(Iterable<S> that);

    ReversibleIterable<Pair<T, Integer>> zipWithIndex();
}
