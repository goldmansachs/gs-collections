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

package com.gs.collections.api.stack;

import java.util.AbstractCollection;
import java.util.List;

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
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.partition.stack.PartitionStack;
import com.gs.collections.api.stack.primitive.BooleanStack;
import com.gs.collections.api.stack.primitive.ByteStack;
import com.gs.collections.api.stack.primitive.CharStack;
import com.gs.collections.api.stack.primitive.DoubleStack;
import com.gs.collections.api.stack.primitive.FloatStack;
import com.gs.collections.api.stack.primitive.IntStack;
import com.gs.collections.api.stack.primitive.LongStack;
import com.gs.collections.api.stack.primitive.ShortStack;
import com.gs.collections.api.tuple.Pair;

/**
 * StackIterable is a last-in-first-out data structure. All iteration methods iterate from the "top" of the stack to the
 * "bottom". In other words, it processes the most recently added elements first.
 * <p>
 * For example:
 * <p>
 * {@link #forEach(Procedure)} iterates over every element, starting with the most recently added
 * <p>
 * {@link #getFirst()} returns the most recently added element, not the element that was added first
 * <p>
 * {@link #toString()} follows the same rules as {@link AbstractCollection#toString()} except it processes the elements
 * in the same order as {@code forEach()}.
 */
public interface StackIterable<T> extends RichIterable<T>
{
    /**
     * @return the top of the stack.
     */
    T peek();

    /**
     * @return a ListIterable of the number of elements specified by the count, beginning with the top of the stack.
     */
    ListIterable<T> peek(int count);

    /**
     * @param index the location to peek into
     * @return the element at the specified index
     */
    T peekAt(int index);

    /**
     * Should return the same value as peek().
     */
    T getFirst();

    /**
     * Should not work as it violates the contract of a Stack.
     */
    T getLast();

    /**
     * Follows the same rules as {@link AbstractCollection#toString()} except it processes the elements
     * in the same order as {@code forEach()}.
     */
    String toString();

    /**
     * Follows the same general contract as {@link List#equals(Object)}, but for Stacks.
     */
    boolean equals(Object o);

    /**
     * Follows the same general contract as {@link List#hashCode()}, but for Stacks.
     */
    int hashCode();

    /**
     * Converts the stack to a MutableStack implementation.
     *
     * @since 2.0
     */
    MutableStack<T> toStack();

    StackIterable<T> tap(Procedure<? super T> procedure);

    StackIterable<T> select(Predicate<? super T> predicate);

    <P> StackIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    StackIterable<T> reject(Predicate<? super T> predicate);

    <P> StackIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionStack<T> partition(Predicate<? super T> predicate);

    <P> PartitionStack<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <V> StackIterable<V> collect(Function<? super T, ? extends V> function);

    BooleanStack collectBoolean(BooleanFunction<? super T> booleanFunction);

    ByteStack collectByte(ByteFunction<? super T> byteFunction);

    CharStack collectChar(CharFunction<? super T> charFunction);

    DoubleStack collectDouble(DoubleFunction<? super T> doubleFunction);

    FloatStack collectFloat(FloatFunction<? super T> floatFunction);

    IntStack collectInt(IntFunction<? super T> intFunction);

    LongStack collectLong(LongFunction<? super T> longFunction);

    ShortStack collectShort(ShortFunction<? super T> shortFunction);

    <P, V> StackIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> StackIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> StackIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> StackIterable<Pair<T, S>> zip(Iterable<S> that);

    StackIterable<Pair<T, Integer>> zipWithIndex();

    /**
     * Converts the StackIterable to an immutable implementation. Returns this for immutable stacks.
     *
     * @since 5.0
     */
    ImmutableStack<T> toImmutable();
}
