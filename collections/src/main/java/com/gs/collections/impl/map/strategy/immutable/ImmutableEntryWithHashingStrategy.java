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

package com.gs.collections.impl.map.strategy.immutable;

import java.util.Map;
import java.util.Map.Entry;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.tuple.AbstractImmutableEntry;

public final class ImmutableEntryWithHashingStrategy<K, V> extends AbstractImmutableEntry<K, V>
{
    private static final long serialVersionUID = 1L;

    private final HashingStrategy<? super K> hashingStrategy;

    public ImmutableEntryWithHashingStrategy(K key, V value, HashingStrategy<? super K> hashingStrategy)
    {
        super(key, value);
        this.hashingStrategy = hashingStrategy;
    }

    public static <T1, T2> ImmutableEntryWithHashingStrategy<T1, T2> of(T1 key, T2 value, HashingStrategy<? super T1> hashingStrategy)
    {
        return new ImmutableEntryWithHashingStrategy<T1, T2>(key, value, hashingStrategy);
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
            return this.hashingStrategy.equals(this.key, (K) that.getKey())
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
        return this.hashingStrategy.computeHashCode(key)
                ^ (value == null ? 0 : value.hashCode());
    }
}
