/*
 * Copyright 2012 Goldman Sachs.
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

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.partition.stack.PartitionStack;
import com.gs.collections.api.tuple.Pair;

/**
 * StackIterable is a last-in-first-out data structure. All iteration methods iterate from the "top" of the stack to the
 * "bottom". In other words, it processes the most recently added elements first.
 * <p/>
 * For example:
 * <p/>
 * {@link #forEach(Procedure)} iterates over every element, starting with the most recently added
 * <p/>
 * {@link #getFirst()} returns the most recently added element, not the element that was added first
 * <p/>
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
     *
     * @throws UnsupportedOperationException
     */
    T getLast();

    /**
     * Follows the same rules as {@link AbstractCollection#toString()} except it processes the elements
     * in the same order as {@code forEach()}.
     */
    String toString();

    /**
     * Compares the specified object with this stack for equality.  Returns
     * <tt>true</tt> if and only if the specified object is also a stack, both
     * stacks have the same size, and all corresponding pairs of elements in
     * the two stacks are <i>equal</i>.  (Two elements <tt>e1</tt> and
     * <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ? e2==null :
     * e1.equals(e2))</tt>.)  In other words, two stacks are defined to be
     * equal if they contain the same elements in the same order.  This
     * definition ensures that the equals method works properly across
     * different implementations of the <tt>StackIterable</tt> interface.
     *
     * @param o the object to be compared for equality with this stack
     * @return <tt>true</tt> if the specified object is equal to this stack
     */
    @Override
    boolean equals(Object o);

    /**
     * Returns the hash code value for this stack.  The hash code of a stack
     * is defined to be the result of the following calculation:
     * <pre>
     * int hashCode = 1;
     * for (T each : stack)
     * {
     *     hashCode = 31 * hashCode + (each == null ? 0 : each.hashCode());
     * }
     *
     * </pre>
     * This ensures that <tt>stack1.equals(stack2)</tt> implies that
     * <tt>stack1.hashCode()==stack2.hashCode()</tt> for any two stacks,
     * <tt>stack1</tt> and <tt>stack2</tt>, as required by the general
     * contract of {@link Object#hashCode}.
     *
     * @return the hash code value for this stack
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    @Override
    int hashCode();

    /**
     * Converts the list to a MutableStack implementation.
     *
     * @since 2.0
     */
    MutableStack<T> toStack();

    StackIterable<T> select(Predicate<? super T> predicate);

    StackIterable<T> reject(Predicate<? super T> predicate);

    PartitionStack<T> partition(Predicate<? super T> predicate);

    <V> StackIterable<V> collect(Function<? super T, ? extends V> function);

    <V> StackIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> StackIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> StackIterable<Pair<T, S>> zip(Iterable<S> that);

    StackIterable<Pair<T, Integer>> zipWithIndex();
}
