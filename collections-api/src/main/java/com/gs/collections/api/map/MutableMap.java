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

package com.gs.collections.api.map;

import java.util.Map;

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
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableByteCollection;
import com.gs.collections.api.collection.primitive.MutableCharCollection;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;
import com.gs.collections.api.collection.primitive.MutableIntCollection;
import com.gs.collections.api.collection.primitive.MutableLongCollection;
import com.gs.collections.api.collection.primitive.MutableShortCollection;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableMap is similar to a JCF Map but adds additional useful internal iterator methods. The MutableMap interface
 * additionally implements some of the methods in the Smalltalk Dictionary protocol.
 */
public interface MutableMap<K, V>
        extends UnsortedMapIterable<K, V>, Map<K, V>, Cloneable
{
    /**
     * Creates a new instance of the same type, using the default capacity and growth parameters.
     */
    MutableMap<K, V> newEmpty();

    /**
     * Adds all the entries derived from {@code collection} to {@code this}. The key and value for each entry
     * is determined by applying the {@code keyFunction} and {@code valueFunction} to each item in
     * {@code collection}. Any entry in {@code map} that has the same key as an entry in {@code this}
     * will have it's value replaced by that in {@code map}.
     */
    <E> MutableMap<K, V> collectKeysAndValues(
            Iterable<E> iterable,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction);

    /**
     * Remove an entry from the map at the specified {@code key}.
     *
     * @return The value removed from entry at key, or null if not found.
     * @see #remove(Object)
     */
    V removeKey(K key);

    /**
     * Get and return the value in the Map at the specified key.  Alternatively, if there is no value in the map at the key,
     * return the result of evaluating the specified Function0, and put that value in the map at the specified key.
     */
    V getIfAbsentPut(K key, Function0<? extends V> function);

    /**
     * Get and return the value in the Map at the specified key.  Alternatively, if there is no value in the map at the key,
     * return the specified value, and put that value in the map at the specified key.
     *
     * @since 5.0
     */
    V getIfAbsentPut(K key, V value);

    /**
     * Get and return the value in the Map at the specified key.  Alternatively, if there is no value in the map for that key
     * return the result of evaluating the specified Function using the specified key, and put that value in the
     * map at the specified key.
     */
    V getIfAbsentPutWithKey(K key, Function<? super K, ? extends V> function);

    /**
     * Get and return the value in the Map at the specified key.  Alternatively, if there is no value in the map for that key
     * return the result of evaluating the specified Function using the specified parameter, and put that value in the
     * map at the specified key.
     */
    <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter);

    MutableMap<K, V> clone();

    /**
     * Returns an unmodifiable view of this map. This method allows modules to provide users with "read-only" access to
     * internal maps. Any query operations on the returned map that "read through" to this map and attempt to modify the
     * returned map, whether direct or via its iterator, result in an {@link UnsupportedOperationException}.
     * The returned map will be <tt>Serializable</tt> if this map is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this map.
     */
    MutableMap<K, V> asUnmodifiable();

    /**
     * Returns an immutable copy of this map. If the map is immutable, it returns itself.
     * <p/>
     * The returned map will be <tt>Serializable</tt> if this map is <tt>Serializable</tt>.
     */
    ImmutableMap<K, V> toImmutable();

    /**
     * Returns a synchronized (thread-safe) map backed by the specified map.  In order to guarantee serial access, it is
     * critical that <strong>all</strong> access to the backing map is accomplished through the returned map.<p>
     * <p/>
     * It is imperative that the user manually synchronize on the returned map when iterating over any of its collection
     * views:
     * <pre>
     *  MutableMap map = myMutableMap.asSynchronized();
     *      ...
     *  Set set = map.keySet();  // Needn't be in synchronized block
     *      ...
     *  synchronized(map)
     *  {  // Synchronizing on map, not set!
     *      Iterator i = s.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     * <p/>
     * The preferred way of iterating over a synchronized collection is to use the collection.forEach() method which is
     * properly synchronized internally.
     * <pre>
     *  MutableMap map = myMutableMap.asSynchronized();
     *      ...
     *  Set set = map.keySet();  // Needn't be in synchronized block
     *     ...
     *  Iterate.forEach(set, new Procedure()
     *  {
     *      public void value(Object each)
     *      {
     *          ...
     *      }
     *  });
     * </pre>
     * <p/>
     * <p>The returned map will be serializable if the specified map is serializable.
     */
    MutableMap<K, V> asSynchronized();

    MutableSetMultimap<V, K> flip();

    MutableMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    <R> MutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    <K2, V2> MutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    MutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <R> MutableCollection<R> collect(Function<? super V, ? extends R> function);

    MutableBooleanCollection collectBoolean(BooleanFunction<? super V> booleanFunction);

    MutableByteCollection collectByte(ByteFunction<? super V> byteFunction);

    MutableCharCollection collectChar(CharFunction<? super V> charFunction);

    MutableDoubleCollection collectDouble(DoubleFunction<? super V> doubleFunction);

    MutableFloatCollection collectFloat(FloatFunction<? super V> floatFunction);

    MutableIntCollection collectInt(IntFunction<? super V> intFunction);

    MutableLongCollection collectLong(LongFunction<? super V> longFunction);

    MutableShortCollection collectShort(ShortFunction<? super V> shortFunction);

    <R> MutableCollection<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function);

    <R> MutableCollection<R> flatCollect(Function<? super V, ? extends Iterable<R>> function);

    MutableCollection<V> reject(Predicate<? super V> predicate);

    MutableCollection<V> select(Predicate<? super V> predicate);

    PartitionMutableCollection<V> partition(Predicate<? super V> predicate);

    <P> PartitionMutableCollection<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> MutableCollection<S> selectInstancesOf(Class<S> clazz);

    <S> MutableCollection<Pair<V, S>> zip(Iterable<S> that);

    MutableCollection<Pair<V, Integer>> zipWithIndex();

    MutableMap<V, K> flipUniqueValues();

    /**
     * This method allows mutable map the ability to add an element in the form of Pair<K,V>.
     *
     * @see #put(Object, Object)
     */
    V add(Pair<K, V> keyValuePair);

    /**
     * This method allows mutable, fixed size, and immutable maps the ability to add elements to their existing
     * elements.  In order to support fixed size maps, a new instance of a map would have to be returned including the
     * keys and values of the original plus the additional key and value.  In the case of mutable maps, the original map
     * is modified and then returned.  In order to use this method properly with mutable and fixed size maps the
     * following approach must be taken:
     * <p/>
     * <pre>
     * map = map.withKeyValue("new key", "new value");
     * </pre>
     * In the case of FixedSizeMap, a new instance will be returned by withKeyValue, and any variables that
     * previously referenced the original map will need to be redirected to reference the new instance.  In the case
     * of a FastMap or UnifiedMap, you will be replacing the reference to map with map, since FastMap and UnifiedMap
     * will both return "this" after calling put on themselves.
     *
     * @see #put(Object, Object)
     */
    MutableMap<K, V> withKeyValue(K key, V value);

    /**
     * This method allows mutable, fixed size, and immutable maps the ability to add elements to their existing
     * elements.  In order to support fixed size maps, a new instance of a map would have to be returned including the
     * keys and values of the original plus all of the additional keys and values.  In the case of mutable maps, the
     * original map is modified and then returned.  In order to use this method properly with mutable and fixed size
     * maps the following approach must be taken:
     * <p/>
     * <pre>
     * map = map.withAllKeyValues(FastList.newListWith(PairImpl.of("new key", "new value")));
     * </pre>
     * In the case of FixedSizeMap, a new instance will be returned by withAllKeyValues, and any variables that
     * previously referenced the original map will need to be redirected to reference the new instance.  In the case
     * of a FastMap or UnifiedMap, you will be replacing the reference to map with map, since FastMap and UnifiedMap
     * will both return "this" after calling put on themselves.
     *
     * @see #put(Object, Object)
     */
    MutableMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    /**
     * Convenience var-args version of withAllKeyValues
     *
     * @see #withAllKeyValues(Iterable)
     */
    MutableMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    /**
     * This method allows mutable, fixed size, and immutable maps the ability to remove elements from their existing
     * elements.  In order to support fixed size maps, a new instance of a map would have to be returned including the
     * keys and values of the original minus the key and value to be removed.  In the case of mutable maps, the original
     * map is modified and then returned.  In order to use this method properly with mutable and fixed size maps the
     * following approach must be taken:
     * <p/>
     * <pre>
     * map = map.withoutKey("key");
     * </pre>
     * In the case of FixedSizeMap, a new instance will be returned by withoutKey, and any variables that previously
     * referenced the original map will need to be redirected to reference the new instance.  In the case of a FastMap
     * or UnifiedMap, you will be replacing the reference to map with map, since FastMap and UnifiedMap will both return
     * "this" after calling remove on themselves.
     *
     * @see #remove(Object)
     */
    MutableMap<K, V> withoutKey(K key);

    /**
     * This method allows mutable, fixed size, and immutable maps the ability to remove elements from their existing
     * elements.  In order to support fixed size maps, a new instance of a map would have to be returned including the
     * keys and values of the original minus all of the keys and values to be removed.  In the case of mutable maps, the
     * original map is modified and then returned.  In order to use this method properly with mutable and fixed size
     * maps the following approach must be taken:
     * <p/>
     * <pre>
     * map = map.withoutAllKeys(FastList.newListWith("key1", "key2"));
     * </pre>
     * In the case of FixedSizeMap, a new instance will be returned by withoutAllKeys, and any variables that previously
     * referenced the original map will need to be redirected to reference the new instance.  In the case of a FastMap
     * or UnifiedMap, you will be replacing the reference to map with map, since FastMap and UnifiedMap will both return
     * "this" after calling remove on themselves.
     *
     * @see #remove(Object)
     */
    MutableMap<K, V> withoutAllKeys(Iterable<? extends K> keys);

    <VV> MutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> MutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);

    <K2, V2> MutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator);

    <K2, V2> MutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator);

    /**
     * Looks up the value associated with {@code key}, applies the {@code function} to it, and replaces the value. If there
     * is no value associated with {@code key}, starts it off with a value supplied by {@code factory}.
     */
    V updateValue(K key, Function0<? extends V> factory, Function<? super V, ? extends V> function);

    /**
     * Same as {@link #updateValue(Object, Function0, Function)} with a Function2 and specified parameter which is
     * passed to the function.
     */
    <P> V updateValueWith(K key, Function0<? extends V> factory, Function2<? super V, ? super P, ? extends V> function, P parameter);
}
