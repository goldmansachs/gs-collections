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

package com.gs.collections.api.multimap;

import java.util.Collection;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;

/**
 * This collection is a type of {@code Map} that can associate multiple values for keys.
 * <p/>
 * <p>Unlike {@code Map} however, this interface is read-only so the results of access methods such as {@link
 * #get(Object)} return a view onto the values associated with that key. The {@link MutableMultimap} subinterface
 * provides methods to mutate the collection.
 * <p/>
 * <p>The advantages to using this container over a {@code Map<K, Collection<V>>} is that all of the handling of the
 * value collection can be done automatically.  It also allows implementations to further specialize in how duplicate
 * values will be handled.  Value collections with list semantics would allow duplicate values for a key, while those
 * implementing set semantics would not.
 * <p/>
 * <p>Internal iteration methods for keys and values (singly - {@link #forEachKey(Procedure)}, {@link
 * #forEachValue(Procedure)}, and together - {@link #forEachKeyValue(Procedure2)}) are provided to allow flexible
 * browsing of the collection's contents.  Similarly, views also are provided for keys ({@link #keysView()}), values
 * ({@link #valuesView()}) and the combination thereof ({@link #keyValuePairsView()}, {@link
 * #keyMultiValuePairsView()}).
 * <p/>
 *
 * @param <K> the type of keys used
 * @param <V> the type of mapped values
 * @since 1.0
 */
public interface Multimap<K, V>
{
    /**
     * Creates a new instance of the same implementation type, using the default capacity and growth parameters.
     */
    Multimap<K, V> newEmpty();

    /**
     * Returns {@code true} if there are no entries.
     */
    boolean isEmpty();

    /**
     * Returns {@code true} if there is at least one entry.
     */
    boolean notEmpty();

    /**
     * Calls the procedure with each <em>value</em>.
     * <p/>
     * Given a Multimap with the contents:
     * <p/>
     * <code>{ "key1" : ["val1", "val2", "val2"], "key2" : ["val3"] }</code>
     * <p/>
     * The given procedure would be invoked with the parameters:
     * <p/>
     * <code>[ "val1", "val2", "val2", "val3" ]</code>
     */
    void forEachValue(Procedure<? super V> procedure);

    /**
     * Calls the <code>procedure</code> with each <em>key</em>.
     * <p/>
     * Given a Multimap with the contents:
     * <p/>
     * <code>{ "key1" : ["val1", "val2", "val2"], "key2" : ["val3"] }</code>
     * <p/>
     * The given procedure would be invoked with the parameters:
     * <p/>
     * <code>[ "key1", "key2" ]</code>
     */
    void forEachKey(Procedure<? super K> procedure);

    /**
     * Calls the <code>procedure</code> with each <em>key-value</em> pair.
     * <p/>
     * Given a Multimap with the contents:
     * <p/>
     * <code>{ "key1" : ["val1", "val2", "val2"], "key2" : ["val3"] }</code>
     * <p/>
     * The given procedure would be invoked with the parameters:
     * <p/>
     * <code>[ ["key1", "val1"], ["key1", "val2"], ["key1", "val2"], ["key2", "val3"] ]</code>
     */
    void forEachKeyValue(Procedure2<K, V> procedure);

    /**
     * Returns the number of key-value entry pairs.
     * <p/>
     * This method is implemented with O(1) (constant-time) performance.
     */
    int size();

    /**
     * Returns the number of distinct keys.
     */
    int sizeDistinct();

    /**
     * Returns {@code true} if any values are mapped to the specified key.
     *
     * @param key the key to search for
     */
    boolean containsKey(Object key);

    /**
     * Returns {@code true} if any key is mapped to the specified value.
     *
     * @param value the value to search for
     */
    boolean containsValue(Object value);

    /**
     * Returns {@code true} if the specified key-value pair is mapped.
     *
     * @param key   the key to search for
     * @param value the value to search for
     */
    boolean containsKeyAndValue(Object key, Object value);

    /**
     * Returns a view of all values associated with the given key.
     * <p/>
     * If the given key does not exist, an empty {@link RichIterable} is returned.
     *
     * @param key the key to search for
     */
    RichIterable<V> get(K key);

    /**
     * Returns a lazy view of the unique keys.
     */
    LazyIterable<K> keysView();

    /**
     * Returns a {@link Bag} of keys with the count corresponding to the number of mapped values.
     */
    Bag<K> keyBag();

    /**
     * Returns an unmodifiable view of all of the values mapped to each key.
     */
    RichIterable<RichIterable<V>> multiValuesView();

    /**
     * Returns a lazy flattened view of all the values.
     */
    LazyIterable<V> valuesView();

    /**
     * Returns a lazy view of the pair of a key and and a lazy view of the values mapped to that key.
     */
    LazyIterable<Pair<K, LazyIterable<V>>> keyMultiValuePairsView();

    /**
     * Returns a lazy view of all of the key/value pairs.
     */
    LazyIterable<Pair<K, V>> keyValuePairsView();

    /**
     * Returns a new {@link MutableMap} of keys from this Multimap to the mapped values as a {@link RichIterable}.
     */
    MutableMap<K, RichIterable<V>> toMap();

    /**
     * Returns a new {@link MutableMap} of keys from this Multimap to the mapped values as a {@link RichIterable}.
     *
     * @param collectionFactory used to create the collections that hold the values and affects the return type
     */
    <R extends Collection<V>> MutableMap<K, R> toMap(Function0<R> collectionFactory);

    /**
     * Compares the specified object with this Multimap for equality.
     * <p/>
     * Two Multimaps are equal when their map views (as returned by {@link #toMap}) are also equal.
     * <p/>
     * <p>In general, two Multimaps with identical key-value mappings may or may not be equal, depending on the type of
     * the collections holding the values. If the backing collections are Sets, then two instances with the same
     * key-value mappings are equal, but if the backing collections are Lists, equality depends on the ordering of the
     * values for each key.
     * <p/>
     * Any two empty Multimaps are equal, because they both have empty {@link #toMap} views.
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code for this Multimap.
     * <p/>
     * <p>The hash code of a Multimap is defined as the hash code of the map view, as returned by {@link #toMap}.
     */
    @Override
    int hashCode();

    /**
     * Returns a mutable <em>copy</em> of this Multimap.
     */
    MutableMultimap<K, V> toMutable();

    /**
     * Returns an immutable copy of this Multimap <em>if it is not already immutable</em>. If the Multimap is immutable,
     * it will return itself.
     * <p/>
     * The returned Multimap will be {@code Serializable} if this Multimap is {@code Serializable}.
     */
    ImmutableMultimap<K, V> toImmutable();
}
