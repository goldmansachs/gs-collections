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

package com.gs.collections.impl.multimap;

import java.util.Collection;
import java.util.Map;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.UnmodifiableRichIterable;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import com.gs.collections.impl.utility.MapIterate;

public abstract class AbstractMultimap<K, V, C extends RichIterable<V>>
        implements Multimap<K, V>
{
    protected abstract MutableMap<K, C> getMap();

    /**
     * Creates the collection of values for a single key.
     * <p/>
     * Collections with weak, soft, or phantom references are not supported.
     * Each call to {@code createCollection} should create a new instance.
     * <p/>
     * The returned collection class determines whether duplicate key-value
     * pairs are allowed.
     *
     * @return an empty collection of values
     */
    protected abstract C createCollection();

    protected Function0<C> createCollectionBlock()
    {
        return new Function0<C>()
        {
            public C value()
            {
                return AbstractMultimap.this.createCollection();
            }
        };
    }

    // Query Operations

    public boolean containsKey(Object key)
    {
        return this.getMap().containsKey(key);
    }

    public boolean containsValue(final Object value)
    {
        return MapIterate.anySatisfy(this.getMap(), new Predicate<C>()
        {
            public boolean accept(C collection)
            {
                return collection.contains(value);
            }
        });
    }

    public boolean containsKeyAndValue(Object key, Object value)
    {
        C collection = this.getMap().get(key);
        return collection != null && collection.contains(value);
    }

    // Views

    public LazyIterable<K> keysView()
    {
        return LazyIterate.adapt(this.getMap().keySet());
    }

    public RichIterable<RichIterable<V>> multiValuesView()
    {
        return UnmodifiableRichIterable.of(this.getMap().collect(new Function<C, RichIterable<V>>()
        {
            public RichIterable<V> valueOf(C multiValue)
            {
                return UnmodifiableRichIterable.of(multiValue);
            }
        }));
    }

    public Bag<K> keyBag()
    {
        final MutableBag<K> bag = Bags.mutable.of();
        this.getMap().forEachKeyValue(new Procedure2<K, C>()
        {
            public void value(K key, C value)
            {
                bag.addOccurrences(key, value.size());
            }
        });
        return bag;
    }

    public LazyIterable<V> valuesView()
    {
        return LazyIterate.adapt(this.getMap().values()).flatCollect(Functions.<Iterable<V>>getPassThru());
    }

    public LazyIterable<Pair<K, LazyIterable<V>>> keyMultiValuePairsView()
    {
        return LazyIterate.adapt(this.getMap().entrySet()).collect(new Function<Map.Entry<K, C>, Pair<K, LazyIterable<V>>>()
        {
            public Pair<K, LazyIterable<V>> valueOf(Map.Entry<K, C> entry)
            {
                return Tuples.pair(entry.getKey(), LazyIterate.adapt(entry.getValue()));
            }
        });
    }

    public LazyIterable<Pair<K, V>> keyValuePairsView()
    {
        return this.keyMultiValuePairsView().flatCollect(new Function<Pair<K, LazyIterable<V>>, Iterable<Pair<K, V>>>()
        {
            public Iterable<Pair<K, V>> valueOf(Pair<K, LazyIterable<V>> pair)
            {
                return pair.getTwo().collect(new KeyValuePairFunction<V, K>(pair.getOne()));
            }
        });
    }

    // Comparison and hashing

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (object instanceof Multimap)
        {
            Multimap<?, ?> that = (Multimap<?, ?>) object;
            return this.getMap().equals(that.toMap());
        }
        return false;
    }

    /**
     * Returns the hash code for this multimap.
     * <p/>
     * The hash code of a multimap is defined as the hash code of the map view,
     * as returned by {@link Multimap#toMap()}.
     *
     * @see Map#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.getMap().hashCode();
    }

    /**
     * Returns a string representation of the multimap, generated by calling
     * {@code toString} on the map returned by {@link Multimap#toMap()}.
     *
     * @return a string representation of the multimap
     */
    @Override
    public String toString()
    {
        return this.getMap().toString();
    }

    public boolean notEmpty()
    {
        return !this.isEmpty();
    }

    public void forEachValue(final Procedure<? super V> procedure)
    {
        this.getMap().forEachValue(new Procedure<C>()
        {
            public void value(C collection)
            {
                collection.forEach(procedure);
            }
        });
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        this.getMap().forEachKey(procedure);
    }

    public void forEachKeyValue(final Procedure2<K, V> procedure)
    {
        final Procedure2<V, K> innerProcedure = new Procedure2<V, K>()
        {
            public void value(V value, K key)
            {
                procedure.value(key, value);
            }
        };

        this.getMap().forEachKeyValue(new Procedure2<K, C>()
        {
            public void value(K key, C collection)
            {
                collection.forEachWith(innerProcedure, key);
            }
        });
    }

    public <R extends Collection<V>> MutableMap<K, R> toMap(final Function0<R> collectionFactory)
    {
        final MutableMap<K, R> result = UnifiedMap.newMap();
        this.getMap().forEachKeyValue(new Procedure2<K, C>()
        {
            public void value(K key, C iterable)
            {
                R newCollection = collectionFactory.value();
                Iterate.addAllTo(iterable, newCollection);
                result.put(key, newCollection);
            }
        });

        return result;
    }

    private static final class KeyValuePairFunction<V, K> implements Function<V, Pair<K, V>>
    {
        private static final long serialVersionUID = 1L;

        private final K key;

        private KeyValuePairFunction(K key)
        {
            this.key = key;
        }

        public Pair<K, V> valueOf(V value)
        {
            return Tuples.pair(this.key, value);
        }
    }
}
