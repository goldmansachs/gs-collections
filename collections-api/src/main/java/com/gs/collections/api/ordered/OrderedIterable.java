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

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import com.gs.collections.api.RichIterable;
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
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.multimap.ordered.OrderedIterableMultimap;
import com.gs.collections.api.ordered.primitive.OrderedBooleanIterable;
import com.gs.collections.api.ordered.primitive.OrderedByteIterable;
import com.gs.collections.api.ordered.primitive.OrderedCharIterable;
import com.gs.collections.api.ordered.primitive.OrderedDoubleIterable;
import com.gs.collections.api.ordered.primitive.OrderedFloatIterable;
import com.gs.collections.api.ordered.primitive.OrderedIntIterable;
import com.gs.collections.api.ordered.primitive.OrderedLongIterable;
import com.gs.collections.api.ordered.primitive.OrderedShortIterable;
import com.gs.collections.api.partition.ordered.PartitionOrderedIterable;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;

/**
 * An OrderedIterable is a RichIterable with some meaningful order, such as insertion order, access order, or sorted order.
 *
 * @since 6.0
 */
public interface OrderedIterable<T> extends RichIterable<T>
{
    /**
     * Returns the index of the first occurrence of the specified item
     * in this iterable, or -1 if this iterable does not contain the item.
     *
     * @see List#indexOf(Object)
     */
    int indexOf(Object object);

    /**
     * Returns the first element of an iterable.  In the case of a List it is the element at the first index.  In the
     * case of any other Collection, it is the first element that would be returned during an iteration.  If the
     * iterable is empty, null is returned.  If null is a valid element of the container, then a developer would need to
     * check to see if the iterable is empty to validate that a null result was not due to the container being empty.
     */
    T getFirst();

    /**
     * Returns the last element of an iterable.  In the case of a List it is the element at the last index.  In the case
     * of any other Collection, it is the last element that would be returned during an iteration.  If the iterable is
     * empty, null is returned.  If null is a valid element of the container, then a developer would need to check to
     * see if the iterable is empty to validate that a null result was not due to the container being empty.
     */
    T getLast();

    /**
     * Returns the initial elements that satisfy the Predicate. Short circuits at the first element which does not
     * satisfy the Predicate.
     */
    OrderedIterable<T> takeWhile(Predicate<? super T> predicate);

    /**
     * Returns the final elements that do not satisfy the Predicate. Short circuits at the first element which does
     * satisfy the Predicate.
     */
    OrderedIterable<T> dropWhile(Predicate<? super T> predicate);

    /**
     * Returns a Partition of the initial elements that satisfy the Predicate and the remaining elements. Short circuits at the first element which does
     * satisfy the Predicate.
     */
    PartitionOrderedIterable<T> partitionWhile(Predicate<? super T> predicate);

    /**
     * Returns a new {@code OrderedIterable} containing the distinct elements in this iterable.
     * <p>
     * Conceptually similar to {@link #toSet()}.{@link #toList()} but retains the original order. If an element appears
     * multiple times in this iterable, the first one will be copied into the result.
     *
     * @return {@code OrderedIterable} of distinct elements
     */
    OrderedIterable<T> distinct();

    /**
     * Returns true if both OrderedIterables have the same length
     * and {@code predicate} returns true for all corresponding elements e1 of
     * this {@code OrderedIterable} and e2 of {@code other}.
     * The {@code predicate} is evaluated for each element at the same position of each {@code OrderedIterable} in a forward iteration order.
     * This is a short circuit pattern.
     *
     * @since 6.0
     */
    <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate);

    /**
     * Iterates over the section of the iterable covered by the specified inclusive indexes.  The indexes are
     * both inclusive.
     * <p>
     * <p>
     * <pre>e.g.
     * OrderedIterable<People> people = FastList.newListWith(ted, mary, bob, sally)
     * people.forEach(0, 1, new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *          LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     * <p>
     * This code would output ted and mary's names.
     */
    void forEach(int startIndex, int endIndex, Procedure<? super T> procedure);

    /**
     * Iterates over the iterable passing each element and the current relative int index to the specified instance of
     * ObjectIntProcedure
     * <pre>e.g.
     * people.forEachWithIndex(new ObjectIntProcedure<Person>()
     * {
     *     public void value(Person person, int index)
     *     {
     *         LOGGER.info("Index: " + index + " person: " + person.getName());
     *     }
     * });
     * </pre>
     */
    void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure);

    /**
     * Iterates over the section of the iterable covered by the specified inclusive indexes.  The indexes are
     * both inclusive.
     * <p>
     * <p>
     * <pre>e.g.
     * OrderedIterable<People> people = FastList.newListWith(ted, mary, bob, sally)
     * people.forEachWithIndex(0, 1, new ObjectIntProcedure<Person>()
     * {
     *     public void value(Person person, int index)
     *     {
     *          LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     * <p>
     * This code would output ted and mary's names.
     */
    void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure);

    /**
     * Converts the OrderedIterable to a mutable MutableStack implementation.
     */
    MutableStack<T> toStack();

    /**
     * Returns the minimum element out of this container based on the natural order, not the order of this container.
     * If you want the minimum element based on the order of this container, use {@link #getFirst()}.
     *
     * @throws ClassCastException     if the elements are not {@link Comparable}
     * @throws NoSuchElementException if the OrderedIterable is empty
     */
    T min();

    /**
     * Returns the maximum element out of this container based on the natural order, not the order of this container.
     * If you want the maximum element based on the order of this container, use {@link #getLast()}.
     *
     * @throws ClassCastException     if the elements are not {@link Comparable}
     * @throws NoSuchElementException if the OrderedIterable is empty
     */
    T max();

    OrderedIterable<T> select(Predicate<? super T> predicate);

    <P> OrderedIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    OrderedIterable<T> reject(Predicate<? super T> predicate);

    <P> OrderedIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionOrderedIterable<T> partition(Predicate<? super T> predicate);

    <P> PartitionOrderedIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> OrderedIterable<S> selectInstancesOf(Class<S> clazz);

    <V> OrderedIterable<V> collect(Function<? super T, ? extends V> function);

    <P, V> OrderedIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> OrderedIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> OrderedIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    OrderedBooleanIterable collectBoolean(BooleanFunction<? super T> booleanFunction);

    OrderedByteIterable collectByte(ByteFunction<? super T> byteFunction);

    OrderedCharIterable collectChar(CharFunction<? super T> charFunction);

    OrderedDoubleIterable collectDouble(DoubleFunction<? super T> doubleFunction);

    OrderedFloatIterable collectFloat(FloatFunction<? super T> floatFunction);

    OrderedIntIterable collectInt(IntFunction<? super T> intFunction);

    OrderedLongIterable collectLong(LongFunction<? super T> longFunction);

    OrderedShortIterable collectShort(ShortFunction<? super T> shortFunction);

    /**
     * Returns the index of the first element of the {@code OrderedIterable} for which the {@code predicate} evaluates to true.
     * Returns -1 if no element evaluates true for the {@code predicate}.
     *
     * @since 6.0
     */
    int detectIndex(Predicate<? super T> predicate);

    <V> OrderedIterableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> OrderedIterableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Returns a {@code OrderedIterable} formed from this {@code OrderedIterable} and another {@code Iterable} by
     * combining corresponding elements in pairs. The second {@code Iterable} should also be ordered.
     * If one of the two {@code Iterable}s is longer than the other, its
     * remaining elements are ignored.
     *
     * @param that The {@code Iterable} providing the second half of each result pair
     * @param <S>  the type of the second half of the returned pairs
     * @return A new {@code OrderedIterable} containing pairs consisting of corresponding elements of this {@code
     * OrderedIterable} and that. The length of the returned {@code OrderedIterable} is the minimum of the lengths of
     * this {@code OrderedIterable} and that.
     */
    <S> OrderedIterable<Pair<T, S>> zip(Iterable<S> that);

    /**
     * Same as {@link #zip(Iterable)} but uses {@code target} for output.
     */
    <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target);

    OrderedIterable<Pair<T, Integer>> zipWithIndex();

    /**
     * Same as {@link #zipWithIndex()} but uses {@code target} for output.
     */
    <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target);
}
