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

import java.util.Map;
import java.util.Map.Entry;

import com.gs.collections.impl.block.factory.Comparators;

public final class ImmutableEntry<K, V> extends AbstractImmutableEntry<K, V>
{
    private static final long serialVersionUID = 1L;

    public ImmutableEntry(K key, V value)
    {
        super(key, value);
    }

    public static <T1, T2> ImmutableEntry<T1, T2> of(T1 key, T2 value)
    {
        return new ImmutableEntry<T1, T2>(key, value);
    }

    /**
     * Indicates whether an object equals this entry, following the behavior specified in {@link Map.Entry#equals(Object)}.
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Entry)
        {
            Entry<?, ?> that = (Entry<?, ?>) object;
            return Comparators.nullSafeEquals(this.key, that.getKey())
                    && Comparators.nullSafeEquals(this.value, that.getValue());
        }
        return false;
    }

    /**
     * Return this entry's hash code, following the behavior specified in {@link Map.Entry#hashCode()}.
     */
    @Override
    public int hashCode()
    {
        K key = this.key;
        V value = this.value;
        return (key == null ? 0 : key.hashCode())
                ^ (value == null ? 0 : value.hashCode());
    }
}
