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

package ponzu.impl.map.immutable;

import ponzu.api.map.ImmutableMap;
import ponzu.api.map.MapIterable;
import ponzu.api.partition.PartitionIterable;
import ponzu.impl.block.factory.IntegerPredicates;
import ponzu.impl.map.MapIterableTestCase;
import ponzu.impl.map.mutable.UnifiedMap;
import org.junit.Assert;
import org.junit.Test;

import static ponzu.impl.factory.Iterables.*;

public class ImmutableUnifiedMap2Test extends MapIterableTestCase
{
    @Override
    protected <K, V> ImmutableMap<K, V> newMap()
    {
        return new ImmutableUnifiedMap<K, V>(UnifiedMap.<K, V>newMap());
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new ImmutableUnifiedMap<K, V>(UnifiedMap.<K, V>newWithKeysValues(key1, value1, key2, value2));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new ImmutableUnifiedMap<K, V>(UnifiedMap.<K, V>newWithKeysValues(key1, value1, key2, value2, key3, value3));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return new ImmutableUnifiedMap<K, V>(UnifiedMap.<K, V>newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4));
    }

    @Override
    @Test
    public void partition_value()
    {
        MapIterable<String, Integer> map = UnifiedMap.newWithKeysValues(
                "A", 1,
                "B", 2,
                "C", 3,
                "D", 4);
        PartitionIterable<Integer> partition = map.partition(IntegerPredicates.isEven());
        Assert.assertEquals(iSet(2, 4), partition.getSelected().toSet());
        Assert.assertEquals(iSet(1, 3), partition.getRejected().toSet());
    }
}
