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

package com.gs.collections.api.bag;

import java.util.AbstractMap;
import java.util.Map;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.partition.bag.PartitionBag;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;

/**
 * A Bag is a Collection whose elements are unordered and may contain duplicate entries.  It varies from
 * MutableCollection in that it adds a protocol for determining, adding, and removing the number of occurrences for an
 * item.
 *
 * @since 1.0
 */
public interface Bag<T>
        extends RichIterable<T>
{
    /**
     * Two bags<tt>b1</tt> and <tt>b2</tt> are equal if <tt>m1.toMapOfItemToCount().equals(m2.toMapOfItemToCount())</tt>.
     *
     * @see Map#equals(Object)
     */
    @Override
    boolean equals(Object object);

    /**
     * Returns the hash code for this Bag, defined as <tt>this.{@link #toMapOfItemToCount()}.hashCode()</tt>.
     *
     * @see Map#hashCode()
     */
    @Override
    int hashCode();

    Bag<T> tap(Procedure<? super T> procedure);

    Bag<T> select(Predicate<? super T> predicate);

    <P> Bag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    Bag<T> reject(Predicate<? super T> predicate);

    <P> Bag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionBag<T> partition(Predicate<? super T> predicate);

    <P> PartitionBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> Bag<S> selectInstancesOf(Class<S> clazz);

    <V> BagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> BagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    SetIterable<Pair<T, Integer>> zipWithIndex();

    /**
     * For each distinct item, with the number of occurrences, execute the specified procedure.
     */
    void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure);

    /**
     * The occurrences of a distinct item in the bag.
     */
    int occurrencesOf(Object item);

    /**
     * Returns all elements of the bag that have a number of occurrences that satisfy the predicate.
     *
     * @since 3.0
     */
    Bag<T> selectByOccurrences(IntPredicate predicate);

    /**
     * Returns the {@code count} most frequently occurring items.
     *
     * In the event of a tie, all of the items with the number of occurrences that match the occurrences of the last
     * item will be returned.
     *
     * @since 6.0
     */
    ListIterable<ObjectIntPair<T>> topOccurrences(int count);

    /**
     * Returns the {@code count} least frequently occurring items.
     *
     * In the event of a tie, all of the items with the number of occurrences that match the occurrences of the last
     * item will be returned.
     *
     * @since 6.0
     */
    ListIterable<ObjectIntPair<T>> bottomOccurrences(int count);

    /**
     * The size of the Bag when counting only distinct elements.
     */
    int sizeDistinct();

    /**
     * Converts the Bag to a Map of the Item type to its count as an Integer.
     */
    MapIterable<T, Integer> toMapOfItemToCount();

    /**
     * Returns a string representation of this bag. The string representation consists of a list of element-count mappings.
     * The elements each appear once, in an order consistent with other methods like {@link #forEachWithOccurrences(ObjectIntProcedure)}
     * and {@link #iterator()}. The element-count mappings are enclosed in braces (<tt>"{}"</tt>).  Adjacent mappings are
     * separated by the characters <tt>", "</tt> (comma and space).  Each element-count mapping is rendered as the element
     * followed by an equals sign (<tt>"="</tt>) followed by the number of ooccurrences. Elements and are converted to
     * strings as by {@link String#valueOf(Object)}.
     * <p>
     * The string representation is similar to {@link AbstractMap#toString()}, not {@link RichIterable#toString()}.
     *
     * @return a string representation of this bag
     * @since 3.0
     */
    String toStringOfItemToCount();

    ImmutableBagIterable<T> toImmutable();
}
