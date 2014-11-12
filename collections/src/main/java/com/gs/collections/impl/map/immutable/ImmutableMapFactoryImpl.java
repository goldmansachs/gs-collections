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

package com.gs.collections.impl.map.immutable;

import java.util.Map;

import com.gs.collections.api.factory.map.ImmutableMapFactory;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.block.factory.Comparators;
import net.jcip.annotations.Immutable;

@Immutable
public final class ImmutableMapFactoryImpl implements ImmutableMapFactory
{
    public <K, V> ImmutableMap<K, V> empty()
    {
        return (ImmutableMap<K, V>) ImmutableEmptyMap.INSTANCE;
    }

    public <K, V> ImmutableMap<K, V> of()
    {
        return this.empty();
    }

    public <K, V> ImmutableMap<K, V> with()
    {
        return this.empty();
    }

    public <K, V> ImmutableMap<K, V> of(K key, V value)
    {
        return this.with(key, value);
    }

    public <K, V> ImmutableMap<K, V> with(K key, V value)
    {
        return new ImmutableSingletonMap<K, V>(key, value);
    }

    public <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2)
    {
        return this.with(key1, value1, key2, value2);
    }

    public <K, V> ImmutableMap<K, V> with(K key1, V value1, K key2, V value2)
    {
        if (Comparators.nullSafeEquals(key1, key2))
        {
            return this.of(key1, value2);
        }
        return new ImmutableDoubletonMap<K, V>(key1, value1, key2, value2);
    }

    public <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return this.with(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> ImmutableMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        if (Comparators.nullSafeEquals(key1, key2) && Comparators.nullSafeEquals(key2, key3))
        {
            return this.of(key1, value3);
        }
        if (Comparators.nullSafeEquals(key1, key2))
        {
            return this.of(key1, value2, key3, value3);
        }
        if (Comparators.nullSafeEquals(key1, key3))
        {
            return this.of(key2, value2, key1, value3);
        }
        if (Comparators.nullSafeEquals(key2, key3))
        {
            return this.of(key1, value1, key2, value3);
        }

        return new ImmutableTripletonMap<K, V>(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return this.with(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public <K, V> ImmutableMap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        if (Comparators.nullSafeEquals(key1, key2))
        {
            return this.of(key1, value2, key3, value3, key4, value4);
        }
        if (Comparators.nullSafeEquals(key1, key3))
        {
            return this.of(key2, value2, key1, value3, key4, value4);
        }
        if (Comparators.nullSafeEquals(key1, key4))
        {
            return this.of(key2, value2, key3, value3, key1, value4);
        }
        if (Comparators.nullSafeEquals(key2, key3))
        {
            return this.of(key1, value1, key2, value3, key4, value4);
        }
        if (Comparators.nullSafeEquals(key2, key4))
        {
            return this.of(key1, value1, key3, value3, key2, value4);
        }
        if (Comparators.nullSafeEquals(key3, key4))
        {
            return this.of(key1, value1, key2, value2, key3, value4);
        }
        return new ImmutableQuadrupletonMap<K, V>(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    /**
     * @deprecated use {@link #ofAll(Map)} instead (inlineable)
     */
    @Deprecated
    public <K, V> ImmutableMap<K, V> ofMap(Map<K, V> map)
    {
        return this.ofAll(map);
    }

    public <K, V> ImmutableMap<K, V> ofAll(Map<K, V> map)
    {
        return this.withAll(map);
    }

    public <K, V> ImmutableMap<K, V> withAll(Map<K, V> map)
    {
        if (map.isEmpty())
        {
            return this.of();
        }

        if (map.size() > 4)
        {
            return new ImmutableUnifiedMap<K, V>(map);
        }

        Map.Entry<K, V>[] entries = map.entrySet().toArray(new Map.Entry[map.entrySet().size()]);
        switch (entries.length)
        {
            case 1:
                return this.of(entries[0].getKey(), entries[0].getValue());
            case 2:
                return this.of(
                        entries[0].getKey(), entries[0].getValue(),
                        entries[1].getKey(), entries[1].getValue());
            case 3:
                return this.of(
                        entries[0].getKey(), entries[0].getValue(),
                        entries[1].getKey(), entries[1].getValue(),
                        entries[2].getKey(), entries[2].getValue());
            case 4:
                return this.of(
                        entries[0].getKey(), entries[0].getValue(),
                        entries[1].getKey(), entries[1].getValue(),
                        entries[2].getKey(), entries[2].getValue(),
                        entries[3].getKey(), entries[3].getValue());
            default:
                throw new AssertionError();
        }
    }
}
