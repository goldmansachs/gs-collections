/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.map.mutable;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions0;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableMutableMap}.
 */
public class UnmodifiableMutableMapTest extends MutableMapTestCase
{
    @Override
    public <K, V> MutableMap<K, V> newMap()
    {
        return new UnmodifiableMutableMap<K, V>(new UnifiedMap<K, V>());
    }

    @Override
    protected <K, V> MutableMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return new UnmodifiableMutableMap<K, V>(UnifiedMap.newWithKeysValues(key, value));
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new UnmodifiableMutableMap<K, V>(UnifiedMap.<K, V>newWithKeysValues(key1, value1, key2, value2));
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new UnmodifiableMutableMap<K, V>(UnifiedMap.<K, V>newWithKeysValues(
                key1, value1, key2, value2, key3, value3));
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(
            K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return new UnmodifiableMutableMap<K, V>(UnifiedMap.<K, V>newWithKeysValues(
                key1, value1, key2, value2, key3, value3, key4, value4));
    }

    @Override
    @Test
    public void removeObject()
    {
        final MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.remove("One");
            }
        });
    }

    @Override
    @Test
    public void removeKey()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.removeKey(1);
            }
        });
    }

    @Override
    @Test
    public void removeFromEntrySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.entrySet().remove(ImmutableEntry.of(2, "Two"));
            }
        });
    }

    @Override
    @Test
    public void removeAllFromEntrySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.entrySet().removeAll(FastList.newListWith(ImmutableEntry.of(2, "Two")));
            }
        });
    }

    @Override
    @Test
    public void retainAllFromEntrySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.entrySet().retainAll(FastList.newListWith(ImmutableEntry.of(2, "Two")));
            }
        });
    }

    @Override
    @Test
    public void clearEntrySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.entrySet().clear();
            }
        });
    }

    @Override
    @Test
    public void removeFromKeySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.keySet().remove(2);
            }
        });
    }

    @Override
    @Test
    public void removeNullFromKeySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.keySet().remove(null);
            }
        });
    }

    @Override
    @Test
    public void removeAllFromKeySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.keySet().removeAll(FastList.newListWith(1, 2));
            }
        });
    }

    @Override
    @Test
    public void retainAllFromKeySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.keySet().retainAll(Lists.mutable.of());
            }
        });
    }

    @Override
    @Test
    public void clearKeySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.keySet().clear();
            }
        });
    }

    @Override
    @Test
    public void removeFromValues()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.values().remove("Two");
            }
        });
    }

    @Override
    @Test
    public void removeAllFromValues()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.values().removeAll(FastList.newListWith("One", "Two"));
            }
        });
    }

    @Override
    @Test
    public void removeNullFromValues()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.values().remove(null);
            }
        });
    }

    @Override
    @Test
    public void retainAllFromValues()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.values().retainAll(Lists.mutable.of());
            }
        });
    }

    @Override
    @Test
    public void getIfAbsentPut()
    {
        Assert.assertEquals("3", this.newMapWithKeysValues(1, "1", 2, "2", 3, "3").getIfAbsentPut(3, Functions0.value("")));
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableMapTest.this.newMapWithKeysValues(1, "1", 2, "2", 3, "3").getIfAbsentPut(4, Functions0.value(""));
            }
        });
    }

    @Override
    @Test
    public void getIfAbsentPutValue()
    {
        Assert.assertEquals("3", this.newMapWithKeysValues(1, "1", 2, "2", 3, "3").getIfAbsentPut(3, ""));
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableMapTest.this.newMapWithKeysValues(1, "1", 2, "2", 3, "3").getIfAbsentPut(4, "");
            }
        });
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void getIfAbsentPutWithKey()
    {
        this.newMapWithKeysValues(1, 1, 2, 2, 3, 3).getIfAbsentPutWithKey(4, Functions.getIntegerPassThru());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void getIfAbsentPutWith()
    {
        this.newMapWithKeysValues(1, "1", 2, "2", 3, "3").getIfAbsentPutWith(4, Functions.getToString(), 4);
    }

    @Override
    @Test
    public void getIfAbsentPut_block_throws()
    {
        // Not applicable for unmodifiable adapter
    }

    @Override
    @Test
    public void getIfAbsentPutWith_block_throws()
    {
        // Not applicable for unmodifiable adapter
    }

    @Override
    @Test
    public void putAll()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "2");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.putAll(null);
            }
        });
    }

    @Override
    @Test
    public void putAllFromCollection()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "2");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.collectKeysAndValues(null, null, null);
            }
        });
    }

    @Override
    @Test
    public void clear()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "2");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.clear();
            }
        });
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withKeyValue()
    {
        this.newMapWithKeysValues(1, "One", 2, "2").withKeyValue(null, null);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withAllKeyValues()
    {
        this.newMapWithKeysValues("A", 1, "B", 2).withAllKeyValues(
                FastList.<Pair<String, Integer>>newListWith(Tuples.pair("B", 22), Tuples.pair("C", 3)));
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withAllKeyValueArguments()
    {
        this.newMapWithKeysValues("A", 1, "B", 2).withAllKeyValueArguments(Tuples.pair("B", 22), Tuples.pair("C", 3));
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withoutKey()
    {
        this.newMapWithKeysValues("A", 1, "B", 2).withoutKey("B");
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withoutAllKeys()
    {
        this.newMapWithKeysValues("A", 1, "B", 2, "C", 3).withoutAllKeys(FastList.newListWith("A", "C"));
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "2");
        Assert.assertSame(map, map.asUnmodifiable());
    }

    @Test
    public void entrySet()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "2").asUnmodifiable();

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.entrySet().remove(null);
            }
        });

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                Iterate.getFirst(map.entrySet()).setValue("Three");
            }
        });

        Assert.assertEquals(this.newMapWithKeysValues(1, "One", 2, "2"), map);
    }

    @Test
    public void entrySetToArray()
    {
        MutableMap<Integer, String> map = this.newMapWithKeyValue(1, "One").asUnmodifiable();
        Object[] entries = map.entrySet().toArray();
        Assert.assertEquals(ImmutableEntry.of(1, "One"), entries[0]);
    }

    @Test
    public void entrySetToArrayWithTarget()
    {
        MutableMap<Integer, String> map = this.newMapWithKeyValue(1, "One").asUnmodifiable();
        Object[] entries = map.entrySet().toArray(new Object[]{});
        Assert.assertEquals(ImmutableEntry.of(1, "One"), entries[0]);
    }

    @Override
    @Test
    public void put()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableMapTest.super.put();
            }
        });
    }

    @Override
    @Test
    public void testClone()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        MutableMap<Integer, String> clone = map.clone();
        Assert.assertSame(map, clone);
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void updateValue()
    {
        super.updateValue();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void updateValue_collisions()
    {
        super.updateValue_collisions();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void updateValueWith()
    {
        super.updateValueWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void updateValueWith_collisions()
    {
        super.updateValueWith_collisions();
    }

    @Override
    public void retainAllFromKeySet_null_collision()
    {
        // Not applicable for unmodifiable maps
    }

    @Override
    public void rehash_null_collision()
    {
        // Not applicable for unmodifiable maps
    }
}
