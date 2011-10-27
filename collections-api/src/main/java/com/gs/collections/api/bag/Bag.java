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

package com.gs.collections.api.bag;

import java.util.Map;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
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
     * The size of the Bag when counting only distinct elements
     */
    int sizeDistinct();

    /**
     * The occurrences of a distinct item in the collection
     */
    int occurrencesOf(Object item);

    /**
     * For each distinct item, with the number of occurrences, execute the specified procedure
     */
    void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure);

    /**
     * Converts the Bag to a Map of the Item type to its count as an Integer
     */
    MutableMap<T, Integer> toMapOfItemToCount();

    /**
     * Convert the Bag to an ImmutableBag.  If the bag is immutable, it returns itself.
     */
    ImmutableBag<T> toImmutable();

    /**
     * Compares the specified object with this bag for equality.  Returns <tt>true</tt> if the given object is also a
     * bag and the occurrences of all of the distinct items are the same between the two bags.  More formally, two
     * bags<tt>b1</tt> and <tt>b2</tt> have the same contents if <tt>m1.toMapOfItemToCount().equals(m2.toMapOfItemToCount())</tt>.
     * This ensures that the <tt>equals</tt> method works properly across different implementations of the <tt>Bag</tt>
     * interface.
     *
     * @param object object to be compared for equality with this bag
     * @return <tt>true</tt> if the specified object is equal to this bag
     * @see Map#equals(Object)
     */
    boolean equals(Object object);

    /**
     * Returns the hash code for this Bag. This is defined as follows:
     * <p/>
     * <pre>
     * final IntegerSum sum = new IntegerSum(0);
     * this.forEachWithOccurrences(new ProcedureWithInt<T>()
     * {
     *     public void value(T each, int count)
     *     {
     *         sum.add((each == null ? 0 : each.hashCode()) ^ count);
     *     }
     * });
     * return sum.getIntSum();
     * </pre>
     * This ensures that <tt>b1.equals(b2)</tt> implies that <tt>b1.hashCode()==b2.hashCode()</tt> for any two bags
     * <tt>b1</tt> and <tt>b2</tt>, as required by the general contract of {@link Object#hashCode()}.
     *
     * @return the hash code value for this map
     * @see Map#hashCode()
     * @see #equals(Object)
     * @see #equals(Object)
     */
    int hashCode();

    Bag<T> select(Predicate<? super T> predicate);

    Bag<T> reject(Predicate<? super T> predicate);

    PartitionBag<T> partition(Predicate<? super T> predicate);

    <V> Bag<V> collect(Function<? super T, ? extends V> function);

    <V> Bag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> Bag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> BagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> BagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> Bag<Pair<T, S>> zip(Iterable<S> that);

    Bag<Pair<T, Integer>> zipWithIndex();
}
