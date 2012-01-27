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

package ponzu.api.list;

import java.util.Comparator;
import java.util.List;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.collection.MutableCollection;
import ponzu.api.multimap.list.MutableListMultimap;
import ponzu.api.partition.list.PartitionMutableList;
import ponzu.api.tuple.Pair;

/**
 * A MutableList is an implementation of a JCF List which provides methods matching the Smalltalk Collection protocol.
 */
public interface MutableList<T>
        extends MutableCollection<T>, List<T>, Cloneable, ListIterable<T>
{
    MutableList<T> with(T element);

    MutableList<T> without(T element);

    MutableList<T> withAll(Iterable<? extends T> elements);

    MutableList<T> withoutAll(Iterable<? extends T> elements);

    MutableList<T> newEmpty();

    MutableList<T> clone();

    MutableList<T> filter(Predicate<? super T> predicate);

    <P> MutableList<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableList<T> filterNot(Predicate<? super T> predicate);

    <P> MutableList<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableList<T> partition(Predicate<? super T> predicate);

    <V> MutableList<V> transform(Function<? super T, ? extends V> function);

    <P, V> MutableList<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> MutableList<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> MutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Sorts the internal data structure of this list and returns the list itself as a convenience.
     */
    MutableList<T> sortThis(Comparator<? super T> comparator);

    /**
     * Sorts the internal data structure of this list and returns the list itself as a convenience.
     */
    MutableList<T> sortThis();

    /**
     * Sorts the internal data structure of this list based on the natural order of the attribute returned by {@code
     * function}.
     */
    <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function);

    MutableList<T> subList(int fromIndex, int toIndex);

    /**
     * Returns an unmodifable view of the list.
     * The returned list will be <tt>Serializable</tt> if this list is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this list
     */
    MutableList<T> asUnmodifiable();

    MutableList<T> asSynchronized();

    /**
     * Returns an immutable copy of this list. If the list is immutable, it returns itself.
     * The returned list will be <tt>Serializable</tt> if this list is <tt>Serializable</tt>.
     */
    ImmutableList<T> toImmutable();

    <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> MutableList<Pair<T, S>> zip(Iterable<S> that);

    MutableList<Pair<T, Integer>> zipWithIndex();

    /**
     * Returns a new MutableList in reverse order
     */
    MutableList<T> toReversed();

    /**
     * Mutates the current list by reversing its order and returns the current list as a result
     */
    MutableList<T> reverseThis();
}
