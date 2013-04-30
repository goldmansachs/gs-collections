/*
 * Copyright 2013 Goldman Sachs.
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

import java.util.Map;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.partition.bag.PartitionBag;
import com.gs.collections.api.tuple.Pair;

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
     * The size of the Bag when counting only distinct elements.
     */
    int sizeDistinct();

    /**
     * The occurrences of a distinct item in the bag.
     */
    int occurrencesOf(Object item);

    /**
     * For each distinct item, with the number of occurrences, execute the specified procedure.
     */
    void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure);

    /**
     * Converts the Bag to a Map of the Item type to its count as an Integer.
     */
    MutableMap<T, Integer> toMapOfItemToCount();

    /**
     * Returns a string representation of this bag. The string representation consists of a list of element-count mappings.
     * The elements each appear once, in an order consistent with other methods like {@link #forEachWithOccurrences(ObjectIntProcedure)}
     * and {@link #iterator()}. The element-count mappings are enclosed in braces (<tt>"{}"</tt>).  Adjacent mappings are
     * separated by the characters <tt>", "</tt> (comma and space).  Each element-count mapping is rendered as the element
     * followed by an equals sign (<tt>"="</tt>) followed by the number of ooccurrences. Elements and are converted to
     * strings as by {@link String#valueOf(Object)}.
     * <p/>
     * The string representation is similar to {@link java.util.AbstractMap#toString()}, not {@link RichIterable#toString()}.
     *
     * @return a string representation of this bag
     * @since 3.0
     */
    String toStringOfItemToCount();

    /**
     * Returns all elements of the bag that have a number of occurrences that satisfy the predicate.
     *
     * @since 3.0
     */
    Bag<T> selectByOccurrences(IntPredicate predicate);

    /**
     * Convert the Bag to an ImmutableBag.  If the bag is immutable, it returns itself.
     */
    ImmutableBag<T> toImmutable();

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

    Bag<T> select(Predicate<? super T> predicate);

    Bag<T> reject(Predicate<? super T> predicate);

    PartitionBag<T> partition(Predicate<? super T> predicate);

    <S> Bag<S> selectInstancesOf(Class<S> clazz);

    <V> Bag<V> collect(Function<? super T, ? extends V> function);

    <V> Bag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> Bag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> BagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> BagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> Bag<Pair<T, S>> zip(Iterable<S> that);

    Bag<Pair<T, Integer>> zipWithIndex();
}
