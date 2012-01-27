/*
 * Copyright 2011 Goldman Sachs.
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

package ponzu.impl.map.fixed;

import ponzu.api.factory.map.FixedSizeMapFactory;
import ponzu.api.map.FixedSizeMap;
import ponzu.impl.block.factory.Comparators;

public class FixedSizeMapFactoryImpl implements FixedSizeMapFactory
{
    private static final FixedSizeMap<?, ?> EMPTY_MAP = new EmptyMap();

    public <K, V> FixedSizeMap<K, V> of()
    {
        return (FixedSizeMap<K, V>) EMPTY_MAP;
    }

    public <K, V> FixedSizeMap<K, V> of(K key, V value)
    {
        return new SingletonMap<K, V>(key, value);
    }

    public <K, V> FixedSizeMap<K, V> of(K key1, V value1, K key2, V value2)
    {
        if (Comparators.nullSafeEquals(key1, key2))
        {
            return this.of(key1, value2);
        }
        return new DoubletonMap<K, V>(key1, value1, key2, value2);
    }

    public <K, V> FixedSizeMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3)
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
        return new TripletonMap<K, V>(key1, value1, key2, value2, key3, value3);
    }
}
