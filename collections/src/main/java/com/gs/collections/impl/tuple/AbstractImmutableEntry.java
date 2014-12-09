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

package com.gs.collections.impl.tuple;

import java.io.Serializable;
import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.tuple.Pair;

public class AbstractImmutableEntry<K, V> implements Map.Entry<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final KeyFunction<?> TO_KEY = new KeyFunction<Object>();
    private static final ValueFunction<?> TO_VALUE = new ValueFunction<Object>();
    private static final PairFunction<?, ?> TO_PAIR = new PairFunction<Object, Object>();

    protected final K key;
    protected final V value;

    public AbstractImmutableEntry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public static <K> Function<Map.Entry<K, ?>, K> getKeyFunction()
    {
        return (Function<Map.Entry<K, ?>, K>) (Function<?, ?>) TO_KEY;
    }

    public static <V> Function<Map.Entry<?, V>, V> getValueFunction()
    {
        return (Function<Map.Entry<?, V>, V>) (Function<?, ?>) TO_VALUE;
    }

    public static <K, V> Function<Map.Entry<K, V>, Pair<K, V>> getPairFunction()
    {
        return (Function<Map.Entry<K, V>, Pair<K, V>>) (Function<?, ?>) TO_PAIR;
    }

    public K getKey()
    {
        return this.key;
    }

    public V getValue()
    {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation throws an {@link UnsupportedOperationException}. Override this method to support mutable
     * map entries.
     */
    public V setValue(V value)
    {
        throw new UnsupportedOperationException("Cannot call setValue() on " + this.getClass().getSimpleName());
    }

    /**
     * Returns a string representation of the form {@code {key}={value}}.
     */
    @Override
    public String toString()
    {
        return this.key + "=" + this.value;
    }

    private static class KeyFunction<K> implements Function<Map.Entry<K, ?>, K>
    {
        private static final long serialVersionUID = 1L;

        public K valueOf(Map.Entry<K, ?> entry)
        {
            return entry.getKey();
        }
    }

    private static class ValueFunction<V> implements Function<Map.Entry<?, V>, V>
    {
        private static final long serialVersionUID = 1L;

        public V valueOf(Map.Entry<?, V> entry)
        {
            return entry.getValue();
        }
    }

    private static class PairFunction<K, V> implements Function<Map.Entry<K, V>, Pair<K, V>>
    {
        private static final long serialVersionUID = 1L;

        public Pair<K, V> valueOf(Map.Entry<K, V> entry)
        {
            return Tuples.pairFrom(entry);
        }
    }
}
