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

package com.gs.collections.api.map.sorted;

import java.util.SortedMap;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
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
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.MutableMapIterable;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableSortedMap is similar to a JCF Map but adds additional useful internal iterator methods.
 * The MutableSortedMap interface additionally implements some of the methods in the Smalltalk Dictionary protocol.
 */
public interface MutableSortedMap<K, V>
        extends MutableMapIterable<K, V>, SortedMapIterable<K, V>, SortedMap<K, V>, Cloneable
{
    /**
     * Creates a new instance of the same type with the same internal Comparator.
     */
    MutableSortedMap<K, V> newEmpty();

    /**
     * Adds all the entries derived from {@code iterable} to {@code this}.
     * The key and value for each entry is determined by applying the {@code keyFunction} and {@code valueFunction} to each item in {@code collection}.
     * Any entry in {@code map} that has the same key as an entry in {@code this} will have it's value replaced by that in {@code map}.
     */
    <E> MutableSortedMap<K, V> collectKeysAndValues(
            Iterable<E> iterable,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction);

    /**
     * Return the value in the Map that corresponds to the specified key, or if there is no value
     * at the key, return the result of evaluating the specified one argument Function
     * using the specified parameter, and put that value in the map at the specified key.
     */
    <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter);

    MutableSortedMap<K, V> asUnmodifiable();

    MutableSortedMap<K, V> asSynchronized();

    // TODO: Keys could be ordered
    MutableSortedSetMultimap<V, K> flip();

    MutableSortedMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    MutableSortedMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <K2, V2> MutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> MutableSortedMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    <R> MutableList<R> collect(Function<? super V, ? extends R> function);

    MutableBooleanList collectBoolean(BooleanFunction<? super V> booleanFunction);

    MutableByteList collectByte(ByteFunction<? super V> byteFunction);

    MutableCharList collectChar(CharFunction<? super V> charFunction);

    MutableDoubleList collectDouble(DoubleFunction<? super V> doubleFunction);

    MutableFloatList collectFloat(FloatFunction<? super V> floatFunction);

    MutableIntList collectInt(IntFunction<? super V> intFunction);

    MutableLongList collectLong(LongFunction<? super V> longFunction);

    MutableShortList collectShort(ShortFunction<? super V> shortFunction);

    <P, VV> MutableList<VV> collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter);

    <R> MutableList<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function);

    <R> MutableList<R> flatCollect(Function<? super V, ? extends Iterable<R>> function);

    MutableSortedMap<K, V> tap(Procedure<? super V> procedure);

    MutableList<V> select(Predicate<? super V> predicate);

    <P> MutableList<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    MutableList<V> reject(Predicate<? super V> predicate);

    <P> MutableList<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionMutableList<V> partition(Predicate<? super V> predicate);

    <P> PartitionMutableList<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> MutableList<S> selectInstancesOf(Class<S> clazz);

    <S> MutableList<Pair<V, S>> zip(Iterable<S> that);

    MutableList<Pair<V, Integer>> zipWithIndex();

    MutableSortedMap<K, V> toReversed();

    MutableSortedMap<K, V> take(int count);

    MutableSortedMap<K, V> takeWhile(Predicate<? super V> predicate);

    MutableSortedMap<K, V> drop(int count);

    MutableSortedMap<K, V> dropWhile(Predicate<? super V> predicate);

    PartitionMutableList<V> partitionWhile(Predicate<? super V> predicate);

    MutableList<V> distinct();

    MutableSet<Entry<K, V>> entrySet();

    /**
     * The underlying set for the keys is sorted in ascending order according to their natural ordering or a custom comparator.
     * However, Java 5 TreeMap returns a keySet that does not inherit from SortedSet therefore we have decided to
     * return the keySet simply as a MutableSet to maintain Java 5 compatibility.
     */
    //todo: Change return type to MutableSortedSet when we move to Java 6
    MutableSet<K> keySet();

    MutableSortedMap<K, V> headMap(K toKey);

    MutableSortedMap<K, V> tailMap(K fromKey);

    MutableSortedMap<K, V> subMap(K fromKey, K toKey);

    MutableCollection<V> values();

    MutableSortedMap<K, V> clone();

    <VV> MutableListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> MutableListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);

    <VV> MutableMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function);

    <K2, V2> MutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator);

    <K2, V2> MutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator);

    // TODO: When we have implementations of linked hash maps
    // MutableOrderedMap<V, K> flipUniqueValues();

    MutableSortedMap<K, V> withKeyValue(K key, V value);

    MutableSortedMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    MutableSortedMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    /**
     * @deprecated in 6.0 Use {@link #withAllKeyValueArguments(Pair[])} instead. Inlineable.
     */
    @Deprecated
    MutableSortedMap<K, V> with(Pair<K, V>... pairs);

    MutableSortedMap<K, V> withoutKey(K key);

    MutableSortedMap<K, V> withoutAllKeys(Iterable<? extends K> keys);
}
