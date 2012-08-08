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

package com.gs.collections.impl.map.mutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.CollidingInt;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnifiedMapTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiedMapTest.class);

    private static final Comparator<Map.Entry<CollidingInt, String>> ENTRY_COMPARATOR = new Comparator<Map.Entry<CollidingInt, String>>()
    {
        public int compare(Map.Entry<CollidingInt, String> o1, Map.Entry<CollidingInt, String> o2)
        {
            return o1.getKey().compareTo(o2.getKey());
        }
    };

    private static final Comparator<String> VALUE_COMPARATOR = new Comparator<String>()
    {
        public int compare(String o1, String o2)
        {
            return Integer.parseInt(o1.substring(1)) - Integer.parseInt(o2.substring(1));
        }
    };

    @Test
    public void forEachWithIndexWithChainedValues()
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, 3), UnifiedMapTest.createVal(i));
        }
        final int[] intArray = new int[1];
        intArray[0] = -1;
        map.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                Assert.assertEquals(index, intArray[0] + 1);
                intArray[0] = index;
            }
        });
    }

    @Test
    public void getMapMemoryUsedInWords()
    {
        UnifiedMap<String, String> map = UnifiedMap.newMap();
        Assert.assertEquals(34, map.getMapMemoryUsedInWords());
        map.put("1", "1");
        Assert.assertEquals(34, map.getMapMemoryUsedInWords());
    }

    @Test
    public void getCollidingBuckets()
    {
        UnifiedMap<Object, Object> map = UnifiedMap.newMap();
        Assert.assertEquals(0, map.getCollidingBuckets());
    }

    private static String createVal(int i)
    {
        return "X" + i;
    }

    //todo: tests with null values
    //todo: keyset.removeAll(some collection where one of the keys is associated with null in the map) == true
    //todo: entryset.add(key associated with null) == true
    //todo: entryset.contains(entry with null value) == true

    @Test
    public void unifiedMapWithCollisions()
    {
        UnifiedMapTest.assertUnifiedMapWithCollisions(0, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisions(1, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisions(2, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisions(3, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisions(4, 2);

        UnifiedMapTest.assertUnifiedMapWithCollisions(0, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisions(1, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisions(2, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisions(3, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisions(4, 4);

        UnifiedMapTest.assertUnifiedMapWithCollisions(0, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisions(1, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisions(2, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisions(3, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisions(4, 8);
    }

    private static void assertUnifiedMapWithCollisions(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }

        for (int i = 0; i < size; i += removeStride)
        {
            Assert.assertEquals(UnifiedMapTest.createVal(i), map.remove(new CollidingInt(i, shift)));
        }
        Verify.assertSize(size - size / removeStride, map);
        for (int i = 0; i < size; i++)
        {
            if (i % removeStride == 0)
            {
                Verify.assertNotContainsKey(new CollidingInt(i, shift), map);
                Assert.assertNull(map.get(new CollidingInt(i, shift)));
            }
            else
            {
                Verify.assertContainsKey(new CollidingInt(i, shift), map);
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
        for (int i = 0; i < size; i++)
        {
            map.remove(new CollidingInt(i, shift));
        }
        Verify.assertSize(0, map);
    }

    @Test
    public void unifiedMapWithCollisionsAndNullKey()
    {
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(0, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(1, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(2, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(3, 2);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(4, 2);

        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(0, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(1, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(2, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(3, 4);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(4, 4);

        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(0, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(1, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(2, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(3, 8);
        UnifiedMapTest.assertUnifiedMapWithCollisionsAndNullKey(4, 8);
    }

    private static void assertUnifiedMapWithCollisionsAndNullKey(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();
        Assert.assertTrue(map.isEmpty());

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        map.put(null, "Y");
        Verify.assertSize(size + 1, map);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
        Assert.assertTrue(map.containsKey(null));
        Assert.assertEquals("Y", map.get(null));

        for (int i = 0; i < size; i += removeStride)
        {
            Assert.assertEquals(UnifiedMapTest.createVal(i), map.remove(new CollidingInt(i, shift)));
        }
        Verify.assertSize(size - size / removeStride + 1, map);
        for (int i = 0; i < size; i++)
        {
            if (i % removeStride != 0)
            {
                Verify.assertContainsKey(new CollidingInt(i, shift), map);
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
        Assert.assertTrue(map.containsKey(null));
        Assert.assertEquals("Y", map.get(null));

        map.remove(null);
        Assert.assertFalse(map.containsKey(null));
        Assert.assertNull(map.get(null));
    }

    @Test
    public void unifiedMap()
    {
        UnifiedMap<Integer, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(i, UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        for (int i = 0; i < size; i++)
        {
            Assert.assertTrue(map.containsKey(i));
            Assert.assertEquals(UnifiedMapTest.createVal(i), map.get(i));
        }

        for (int i = 0; i < size; i += 2)
        {
            Assert.assertEquals(UnifiedMapTest.createVal(i), map.remove(i));
        }
        Verify.assertSize(size / 2, map);
        for (int i = 1; i < size; i += 2)
        {
            Assert.assertTrue(map.containsKey(i));
            Assert.assertEquals(UnifiedMapTest.createVal(i), map.get(i));
        }
    }

    @Test
    public void unifiedMapClear()
    {
        UnifiedMapTest.assertUnifiedMapClear(0);
        UnifiedMapTest.assertUnifiedMapClear(1);
        UnifiedMapTest.assertUnifiedMapClear(2);
        UnifiedMapTest.assertUnifiedMapClear(3);
    }

    private static void assertUnifiedMapClear(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        map.clear();
        Verify.assertSize(0, map);
        for (int i = 0; i < size; i++)
        {
            Verify.assertNotContainsKey(new CollidingInt(i, shift), map);
            Assert.assertNull(map.get(new CollidingInt(i, shift)));
        }
    }

    @Test
    public void unifiedMapForEachEntry()
    {
        UnifiedMapTest.assertUnifiedMapForEachEntry(0);
        UnifiedMapTest.assertUnifiedMapForEachEntry(1);
        UnifiedMapTest.assertUnifiedMapForEachEntry(2);
        UnifiedMapTest.assertUnifiedMapForEachEntry(3);
    }

    private static void assertUnifiedMapForEachEntry(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        final int[] count = new int[1];
        map.forEachKeyValue(new Procedure2<CollidingInt, String>()
        {
            public void value(CollidingInt key, String value)
            {
                Assert.assertEquals(UnifiedMapTest.createVal(key.getValue()), value);
                count[0]++;
            }
        });
        Assert.assertEquals(size, count[0]);
    }

    @Test
    public void unifiedMapForEachKey()
    {
        UnifiedMapTest.assertUnifiedMapForEachKey(0);
        UnifiedMapTest.assertUnifiedMapForEachKey(1);
        UnifiedMapTest.assertUnifiedMapForEachKey(2);
        UnifiedMapTest.assertUnifiedMapForEachKey(3);
    }

    private static void assertUnifiedMapForEachKey(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        final List<CollidingInt> keys = new ArrayList<CollidingInt>(size);
        map.forEachKey(new Procedure<CollidingInt>()
        {
            public void value(CollidingInt key)
            {
                keys.add(key);
            }
        });
        Verify.assertSize(size, keys);
        Collections.sort(keys);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(new CollidingInt(i, shift), keys.get(i));
        }
    }

    @Test
    public void unifiedMapForEachValue()
    {
        UnifiedMapTest.assertUnifiedMapForEachValue(0);
        UnifiedMapTest.assertUnifiedMapForEachValue(1);
        UnifiedMapTest.assertUnifiedMapForEachValue(2);
        UnifiedMapTest.assertUnifiedMapForEachValue(3);
    }

    private static void assertUnifiedMapForEachValue(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        final List<String> values = new ArrayList<String>(size);
        map.forEachValue(new Procedure<String>()
        {
            public void value(String key)
            {
                values.add(key);
            }
        });
        Verify.assertSize(size, values);
        Collections.sort(values, UnifiedMapTest.VALUE_COMPARATOR);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(UnifiedMapTest.createVal(i), values.get(i));
        }
    }

    @Test
    public void equalsWithNullValue()
    {
        MutableMap<Integer, Integer> map1 = UnifiedMap.newWithKeysValues(1, null, 2, 2);
        MutableMap<Integer, Integer> map2 = UnifiedMap.newWithKeysValues(2, 2, 3, 3);
        Verify.assertNotEquals(map1, map2);
    }

    @Test
    public void unifiedMapEqualsAndHashCode()
    {
        UnifiedMapTest.assertUnifiedMapEqualsAndHashCode(0);
        UnifiedMapTest.assertUnifiedMapEqualsAndHashCode(1);
        UnifiedMapTest.assertUnifiedMapEqualsAndHashCode(2);
        UnifiedMapTest.assertUnifiedMapEqualsAndHashCode(3);
    }

    private static void assertUnifiedMapEqualsAndHashCode(int shift)
    {
        MutableMap<CollidingInt, String> map1 = UnifiedMap.newMap();
        Map<CollidingInt, String> map2 = new HashMap<CollidingInt, String>();
        MutableMap<CollidingInt, String> map3 = UnifiedMap.newMap();
        MutableMap<CollidingInt, String> map4 = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map1.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            map2.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            map3.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            map4.put(new CollidingInt(size - i - 1, shift), UnifiedMapTest.createVal(size - i - 1));
        }

        Assert.assertEquals(map2, map1);
        Assert.assertEquals(map1, map2);
        Assert.assertEquals(map2.hashCode(), map1.hashCode());
        Assert.assertEquals(map1, map3);
        Assert.assertEquals(map1.hashCode(), map3.hashCode());
        Assert.assertEquals(map2, map4);
        Assert.assertEquals(map4, map2);
        Assert.assertEquals(map2.hashCode(), map4.hashCode());

        Verify.assertSetsEqual(map2.entrySet(), map1.entrySet());
        Verify.assertSetsEqual(map1.entrySet(), map2.entrySet());
        Assert.assertEquals(map2.entrySet().hashCode(), map1.entrySet().hashCode());
        Verify.assertSetsEqual(map1.entrySet(), map3.entrySet());
        Assert.assertEquals(map1.entrySet().hashCode(), map3.entrySet().hashCode());
        Verify.assertSetsEqual(map2.entrySet(), map4.entrySet());
        Verify.assertSetsEqual(map4.entrySet(), map2.entrySet());
        Assert.assertEquals(map2.entrySet().hashCode(), map4.entrySet().hashCode());

        Verify.assertSetsEqual(map2.keySet(), map1.keySet());
        Verify.assertSetsEqual(map1.keySet(), map2.keySet());
        Assert.assertEquals(map2.keySet().hashCode(), map1.keySet().hashCode());
        Verify.assertSetsEqual(map1.keySet(), map3.keySet());
        Assert.assertEquals(map1.keySet().hashCode(), map3.keySet().hashCode());
        Verify.assertSetsEqual(map2.keySet(), map4.keySet());
        Verify.assertSetsEqual(map4.keySet(), map2.keySet());
        Assert.assertEquals(map2.keySet().hashCode(), map4.keySet().hashCode());
    }

    @Test
    public void unifiedMapPutAll()
    {
        UnifiedMapTest.assertUnifiedMapPutAll(0);
        UnifiedMapTest.assertUnifiedMapPutAll(1);
        UnifiedMapTest.assertUnifiedMapPutAll(2);
        UnifiedMapTest.assertUnifiedMapPutAll(3);
    }

    private static void assertUnifiedMapPutAll(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        UnifiedMap<CollidingInt, String> newMap = UnifiedMap.newMap(size);
        newMap.putAll(map);

        Verify.assertSize(size, newMap);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), newMap);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), newMap);
        }
    }

    @Test
    public void unifiedMapPutAllWithHashMap()
    {
        UnifiedMapTest.assertUnifiedMapPutAllWithHashMap(0);
        UnifiedMapTest.assertUnifiedMapPutAllWithHashMap(1);
        UnifiedMapTest.assertUnifiedMapPutAllWithHashMap(2);
        UnifiedMapTest.assertUnifiedMapPutAllWithHashMap(3);
    }

    private static void assertUnifiedMapPutAllWithHashMap(int shift)
    {
        Map<CollidingInt, String> map = new HashMap<CollidingInt, String>();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        UnifiedMap<CollidingInt, String> newMap = UnifiedMap.newMap(size);
        newMap.putAll(map);

        Verify.assertSize(size, newMap);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), newMap);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), newMap);
        }
    }

    @Test
    public void unifiedMapReplace()
    {
        UnifiedMapTest.assertUnifiedMapReplace(0);
        UnifiedMapTest.assertUnifiedMapReplace(1);
        UnifiedMapTest.assertUnifiedMapReplace(2);
        UnifiedMapTest.assertUnifiedMapReplace(3);
    }

    private static void assertUnifiedMapReplace(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), "Y" + i);
        }
        Verify.assertSize(size, map);
        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals("Y" + i, map.get(new CollidingInt(i, shift)));
        }
    }

    @Test
    public void unifiedMapContainsValue()
    {
        UnifiedMapTest.runUnifiedMapContainsValue(0);
        UnifiedMapTest.runUnifiedMapContainsValue(1);
        UnifiedMapTest.runUnifiedMapContainsValue(2);
        UnifiedMapTest.runUnifiedMapContainsValue(3);
    }

    private static void runUnifiedMapContainsValue(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 1000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        for (int i = 0; i < size; i++)
        {
            Assert.assertTrue(map.containsValue(UnifiedMapTest.createVal(i)));
        }
    }

    @Test
    public void unifiedMapKeySet()
    {
        UnifiedMapTest.runUnifiedMapKeySet(0);
        UnifiedMapTest.runUnifiedMapKeySet(1);
        UnifiedMapTest.runUnifiedMapKeySet(2);
        UnifiedMapTest.runUnifiedMapKeySet(3);
    }

    private static void runUnifiedMapKeySet(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<CollidingInt> keySet = map.keySet();
        Verify.assertSize(size, keySet);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContains(new CollidingInt(i, shift), keySet);
        }

        for (int i = 0; i < size; i += 2)
        {
            Assert.assertTrue(keySet.remove(new CollidingInt(i, shift)));
        }
        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, keySet);

        for (int i = 1; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(new CollidingInt(i, shift), keySet);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapKeySetRetainAll()
    {
        UnifiedMapTest.runUnifiedMapKeySetRetainAll(0);
        UnifiedMapTest.runUnifiedMapKeySetRetainAll(1);
        UnifiedMapTest.runUnifiedMapKeySetRetainAll(2);
        UnifiedMapTest.runUnifiedMapKeySetRetainAll(3);
    }

    private static void runUnifiedMapKeySetRetainAll(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        List<CollidingInt> toRetain = new ArrayList<CollidingInt>();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            if (i % 2 == 0)
            {
                toRetain.add(new CollidingInt(i, shift));
            }
        }
        Verify.assertSize(size, map);
        Set<CollidingInt> keySet = map.keySet();
        Assert.assertTrue(keySet.containsAll(toRetain));

        Assert.assertTrue(keySet.retainAll(toRetain));
        Assert.assertTrue(keySet.containsAll(toRetain));

        Assert.assertFalse(keySet.retainAll(toRetain)); // a second call should not modify the set

        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, keySet);

        for (int i = 0; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(new CollidingInt(i, shift), keySet);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapKeySetRemoveAll()
    {
        UnifiedMapTest.runUnifiedMapKeySetRemoveAll(0);
        UnifiedMapTest.runUnifiedMapKeySetRemoveAll(1);
        UnifiedMapTest.runUnifiedMapKeySetRemoveAll(2);
        UnifiedMapTest.runUnifiedMapKeySetRemoveAll(3);
    }

    private static void runUnifiedMapKeySetRemoveAll(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        List<CollidingInt> toRemove = new ArrayList<CollidingInt>();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            if (i % 2 == 0)
            {
                toRemove.add(new CollidingInt(i, shift));
            }
        }
        Verify.assertSize(size, map);
        Set<CollidingInt> keySet = map.keySet();

        Assert.assertTrue(keySet.removeAll(toRemove));

        Assert.assertFalse(keySet.removeAll(toRemove)); // a second call should not modify the set

        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, keySet);

        for (int i = 1; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(new CollidingInt(i, shift), keySet);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapKeySetToArray()
    {
        UnifiedMapTest.runUnifiedMapKeySetToArray(0);
        UnifiedMapTest.runUnifiedMapKeySetToArray(1);
        UnifiedMapTest.runUnifiedMapKeySetToArray(2);
        UnifiedMapTest.runUnifiedMapKeySetToArray(3);
    }

    private static void runUnifiedMapKeySetToArray(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<CollidingInt> keySet = map.keySet();

        Object[] keys = keySet.toArray();
        Arrays.sort(keys);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(new CollidingInt(i, shift), keys[i]);
        }
    }

    @Test
    public void unifiedMapKeySetIterator()
    {
        UnifiedMapTest.runUnifiedMapKeySetIterator(0);
        UnifiedMapTest.runUnifiedMapKeySetIterator(1);
        UnifiedMapTest.runUnifiedMapKeySetIterator(2);
        UnifiedMapTest.runUnifiedMapKeySetIterator(3);
    }

    private static void runUnifiedMapKeySetIterator(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<CollidingInt> keySet = map.keySet();

        CollidingInt[] keys = new CollidingInt[size];
        int count = 0;
        for (Iterator<CollidingInt> it = keySet.iterator(); it.hasNext(); )
        {
            keys[count++] = it.next();
        }
        Arrays.sort(keys);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(new CollidingInt(i, shift), keys[i]);
        }
    }

    @Test
    public void unifiedMapKeySetIteratorRemove()
    {
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(0, 2);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(1, 2);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(2, 2);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(3, 2);

        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(0, 3);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(1, 3);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(2, 3);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(3, 3);

        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(0, 4);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(1, 4);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(2, 4);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemove(3, 4);
    }

    private static void runUnifiedMapKeySetIteratorRemove(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<CollidingInt> keySet = map.keySet();

        int count = 0;
        for (Iterator<CollidingInt> it = keySet.iterator(); it.hasNext(); )
        {
            CollidingInt key = it.next();
            count++;
            if (key.getValue() % removeStride == 0)
            {
                it.remove();
            }
        }
        Assert.assertEquals(size, count);

        for (int i = 0; i < size; i++)
        {
            if (i % removeStride != 0)
            {
                Assert.assertTrue("map contains " + i + "for shift " + shift + " and remove stride " + removeStride, map
                        .containsKey(new CollidingInt(i, shift)));
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
    }

    @Test
    public void unifiedMapKeySetIteratorRemoveFlip()
    {
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(0, 2);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(1, 2);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(2, 2);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(3, 2);

        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(0, 3);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(1, 3);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(2, 3);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(3, 3);

        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(0, 4);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(1, 4);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(2, 4);
        UnifiedMapTest.runUnifiedMapKeySetIteratorRemoveFlip(3, 4);
    }

    private static void runUnifiedMapKeySetIteratorRemoveFlip(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<CollidingInt> keySet = map.keySet();

        int count = 0;
        for (Iterator<CollidingInt> it = keySet.iterator(); it.hasNext(); )
        {
            CollidingInt key = it.next();
            count++;
            if (key.getValue() % removeStride != 0)
            {
                it.remove();
            }
        }
        Assert.assertEquals(size, count);

        for (int i = 0; i < size; i++)
        {
            if (i % removeStride == 0)
            {
                Assert.assertTrue("map contains " + i + "for shift " + shift + " and remove stride " + removeStride, map
                        .containsKey(new CollidingInt(i, shift)));
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
    }

    //entry set tests

    @Test
    public void unifiedMapEntrySet()
    {
        UnifiedMapTest.runUnifiedMapEntrySet(0);
        UnifiedMapTest.runUnifiedMapEntrySet(1);
        UnifiedMapTest.runUnifiedMapEntrySet(2);
        UnifiedMapTest.runUnifiedMapEntrySet(3);
    }

    private static void runUnifiedMapEntrySet(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();
        Verify.assertSize(size, entrySet);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContains(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)), entrySet);
        }

        for (int i = 0; i < size; i += 2)
        {
            Assert.assertTrue(entrySet.remove(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i))));
        }
        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, entrySet);

        for (int i = 1; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)), entrySet);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapEntrySetRetainAll()
    {
        UnifiedMapTest.runUnifiedMapEntrySetRetainAll(0);
        UnifiedMapTest.runUnifiedMapEntrySetRetainAll(1);
        UnifiedMapTest.runUnifiedMapEntrySetRetainAll(2);
        UnifiedMapTest.runUnifiedMapEntrySetRetainAll(3);
    }

    private static void runUnifiedMapEntrySetRetainAll(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        List<Entry> toRetain = new ArrayList<Entry>();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            if (i % 2 == 0)
            {
                toRetain.add(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)));
            }
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();
        Assert.assertTrue(entrySet.containsAll(toRetain));

        Assert.assertTrue(entrySet.retainAll(toRetain));
        Assert.assertTrue(entrySet.containsAll(toRetain));

        Assert.assertFalse(entrySet.retainAll(toRetain)); // a second call should not modify the set

        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, entrySet);

        for (int i = 0; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)), entrySet);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapEntrySetRemoveAll()
    {
        UnifiedMapTest.runUnifiedMapEntrySetRemoveAll(0);
        UnifiedMapTest.runUnifiedMapEntrySetRemoveAll(1);
        UnifiedMapTest.runUnifiedMapEntrySetRemoveAll(2);
        UnifiedMapTest.runUnifiedMapEntrySetRemoveAll(3);
    }

    private static void runUnifiedMapEntrySetRemoveAll(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        List<Entry> toRemove = new ArrayList<Entry>();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            if (i % 2 == 0)
            {
                toRemove.add(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)));
            }
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();

        Assert.assertTrue(entrySet.removeAll(toRemove));

        Assert.assertFalse(entrySet.removeAll(toRemove)); // a second call should not modify the set

        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, entrySet);

        for (int i = 1; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)), entrySet);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapEntrySetToArray()
    {
        UnifiedMapTest.runUnifiedMapEntrySetToArray(0);
        UnifiedMapTest.runUnifiedMapEntrySetToArray(1);
        UnifiedMapTest.runUnifiedMapEntrySetToArray(2);
        UnifiedMapTest.runUnifiedMapEntrySetToArray(3);
    }

    private static void runUnifiedMapEntrySetToArray(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();

        Map.Entry<CollidingInt, String>[] entries = entrySet.toArray(new Map.Entry[0]);
        Arrays.sort(entries, UnifiedMapTest.ENTRY_COMPARATOR);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)), entries[i]);
        }
    }

    @Test
    public void unifiedMapEntrySetIterator()
    {
        UnifiedMapTest.runUnifiedMapEntrySetIterator(0);
        UnifiedMapTest.runUnifiedMapEntrySetIterator(1);
        UnifiedMapTest.runUnifiedMapEntrySetIterator(2);
        UnifiedMapTest.runUnifiedMapEntrySetIterator(3);
    }

    private static void runUnifiedMapEntrySetIterator(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();

        Map.Entry<CollidingInt, String>[] entries = new Map.Entry[size];
        int count = 0;
        for (Iterator<Map.Entry<CollidingInt, String>> it = entrySet.iterator(); it.hasNext(); )
        {
            entries[count++] = it.next();
        }
        Arrays.sort(entries, UnifiedMapTest.ENTRY_COMPARATOR);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)), entries[i]);
        }
    }

    @Test
    public void unifiedMapEntrySetIteratorSetValue()
    {
        UnifiedMapTest.runUnifiedMapEntrySetIteratorSetValue(0);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorSetValue(1);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorSetValue(2);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorSetValue(3);
    }

    private static void runUnifiedMapEntrySetIteratorSetValue(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();
        for (Map.Entry<CollidingInt, String> entry : entrySet)
        {
            CollidingInt key = entry.getKey();
            entry.setValue("Y" + key.getValue());
        }

        Map.Entry<CollidingInt, String>[] entries = new Map.Entry[size];
        int count = 0;
        for (Iterator<Map.Entry<CollidingInt, String>> it = entrySet.iterator(); it.hasNext(); )
        {
            entries[count++] = it.next();
        }
        Arrays.sort(entries, UnifiedMapTest.ENTRY_COMPARATOR);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(new Entry(new CollidingInt(i, shift), "Y" + i), entries[i]);
        }
    }

    @Test
    public void unifiedMapEntrySetIteratorRemove()
    {
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(0, 2);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(1, 2);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(2, 2);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(3, 2);

        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(0, 3);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(1, 3);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(2, 3);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(3, 3);

        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(0, 4);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(1, 4);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(2, 4);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemove(3, 4);
    }

    private static void runUnifiedMapEntrySetIteratorRemove(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();

        int count = 0;
        for (Iterator<Map.Entry<CollidingInt, String>> it = entrySet.iterator(); it.hasNext(); )
        {
            CollidingInt entry = it.next().getKey();
            count++;
            if (entry.getValue() % removeStride == 0)
            {
                it.remove();
            }
        }
        Assert.assertEquals(size, count);

        for (int i = 0; i < size; i++)
        {
            if (i % removeStride != 0)
            {
                Assert.assertTrue("map contains " + i + "for shift " + shift + " and remove stride " + removeStride, map
                        .containsKey(new CollidingInt(i, shift)));
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
    }

    @Test
    public void unifiedMapEntrySetIteratorRemoveFlip()
    {
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(0, 2);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(1, 2);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(2, 2);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(3, 2);

        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(0, 3);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(1, 3);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(2, 3);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(3, 3);

        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(0, 4);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(1, 4);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(2, 4);
        UnifiedMapTest.runUnifiedMapEntrySetIteratorRemoveFlip(3, 4);
    }

    private static void runUnifiedMapEntrySetIteratorRemoveFlip(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Set<Map.Entry<CollidingInt, String>> entrySet = map.entrySet();

        int count = 0;
        for (Iterator<Map.Entry<CollidingInt, String>> it = entrySet.iterator(); it.hasNext(); )
        {
            CollidingInt entry = it.next().getKey();
            count++;
            if (entry.getValue() % removeStride != 0)
            {
                it.remove();
            }
        }
        Assert.assertEquals(size, count);

        for (int i = 0; i < size; i++)
        {
            if (i % removeStride == 0)
            {
                Assert.assertTrue("map contains " + i + "for shift " + shift + " and remove stride " + removeStride, map
                        .containsKey(new CollidingInt(i, shift)));
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
    }

    // values collection

    @Test
    public void unifiedMapValues()
    {
        UnifiedMapTest.runUnifiedMapValues(0);
        UnifiedMapTest.runUnifiedMapValues(1);
        UnifiedMapTest.runUnifiedMapValues(2);
        UnifiedMapTest.runUnifiedMapValues(3);
    }

    private static void runUnifiedMapValues(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 1000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Collection<String> values = map.values();
        Assert.assertEquals(size, values.size());
        for (int i = 0; i < size; i++)
        {
            Verify.assertContains(UnifiedMapTest.createVal(i), values);
        }

        for (int i = 0; i < size; i += 2)
        {
            Assert.assertTrue(values.remove(UnifiedMapTest.createVal(i)));
        }
        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, values);

        for (int i = 1; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(UnifiedMapTest.createVal(i), values);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapValuesRetainAll()
    {
        UnifiedMapTest.runUnifiedMapValuesRetainAll(0);
        UnifiedMapTest.runUnifiedMapValuesRetainAll(1);
        UnifiedMapTest.runUnifiedMapValuesRetainAll(2);
        UnifiedMapTest.runUnifiedMapValuesRetainAll(3);
    }

    private static void runUnifiedMapValuesRetainAll(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        List<String> toRetain = new ArrayList<String>();

        int size = 1000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            if (i % 2 == 0)
            {
                toRetain.add(UnifiedMapTest.createVal(i));
            }
        }
        Verify.assertSize(size, map);
        Collection<String> values = map.values();
        Assert.assertTrue(values.containsAll(toRetain));

        Assert.assertTrue(values.retainAll(toRetain));
        Assert.assertTrue(values.containsAll(toRetain));

        Assert.assertFalse(values.retainAll(toRetain)); // a second call should not modify the set

        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, values);

        for (int i = 0; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(UnifiedMapTest.createVal(i), values);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapValuesRemoveAll()
    {
        UnifiedMapTest.runUnifiedMapValuesRemoveAll(0);
        UnifiedMapTest.runUnifiedMapValuesRemoveAll(1);
        UnifiedMapTest.runUnifiedMapValuesRemoveAll(2);
        UnifiedMapTest.runUnifiedMapValuesRemoveAll(3);
    }

    private static void runUnifiedMapValuesRemoveAll(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        List<String> toRemove = new ArrayList<String>();

        int size = 1000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
            if (i % 2 == 0)
            {
                toRemove.add(UnifiedMapTest.createVal(i));
            }
        }
        Verify.assertSize(size, map);
        Collection<String> values = map.values();

        Assert.assertTrue(values.removeAll(toRemove));

        Assert.assertFalse(values.removeAll(toRemove)); // a second call should not modify the set

        Verify.assertSize(size / 2, map);
        Verify.assertSize(size / 2, values);

        for (int i = 1; i < size; i += 2)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContains(UnifiedMapTest.createVal(i), values);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
    }

    @Test
    public void unifiedMapValuesToArray()
    {
        UnifiedMapTest.runUnifiedMapValuesToArray(0);
        UnifiedMapTest.runUnifiedMapValuesToArray(1);
        UnifiedMapTest.runUnifiedMapValuesToArray(2);
        UnifiedMapTest.runUnifiedMapValuesToArray(3);
    }

    private static void runUnifiedMapValuesToArray(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 1000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Collection<String> values = map.values();

        String[] entries = values.toArray(new String[0]);
        Arrays.sort(entries, UnifiedMapTest.VALUE_COMPARATOR);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(UnifiedMapTest.createVal(i), entries[i]);
        }
    }

    @Test
    public void unifiedMapValuesIterator()
    {
        UnifiedMapTest.runUnifiedMapValuesIterator(0);
        UnifiedMapTest.runUnifiedMapValuesIterator(1);
        UnifiedMapTest.runUnifiedMapValuesIterator(2);
        UnifiedMapTest.runUnifiedMapValuesIterator(3);
    }

    private static void runUnifiedMapValuesIterator(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 1000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Collection<String> values = map.values();

        String[] valuesArray = new String[size];
        int count = 0;
        for (Iterator<String> it = values.iterator(); it.hasNext(); )
        {
            valuesArray[count++] = it.next();
        }
        Arrays.sort(valuesArray, UnifiedMapTest.VALUE_COMPARATOR);

        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(UnifiedMapTest.createVal(i), valuesArray[i]);
        }
    }

    @Test
    public void unifiedMapValuesIteratorRemove()
    {
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(0, 2);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(1, 2);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(2, 2);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(3, 2);

        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(0, 3);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(1, 3);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(2, 3);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(3, 3);

        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(0, 4);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(1, 4);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(2, 4);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemove(3, 4);
    }

    private static void runUnifiedMapValuesIteratorRemove(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Collection<String> values = map.values();

        int count = 0;
        for (Iterator<String> it = values.iterator(); it.hasNext(); )
        {
            String value = it.next();
            int x = Integer.parseInt(value.substring(1));
            count++;
            if (x % removeStride == 0)
            {
                it.remove();
            }
        }
        Assert.assertEquals(size, count);

        for (int i = 0; i < size; i++)
        {
            if (i % removeStride != 0)
            {
                Assert.assertTrue("map contains " + i + "for shift " + shift + " and remove stride " + removeStride, map
                        .containsKey(new CollidingInt(i, shift)));
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
    }

    @Test
    public void unifiedMapValuesIteratorRemoveFlip()
    {
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(0, 2);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(1, 2);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(2, 2);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(3, 2);

        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(0, 3);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(1, 3);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(2, 3);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(3, 3);

        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(0, 4);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(1, 4);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(2, 4);
        UnifiedMapTest.runUnifiedMapValuesIteratorRemoveFlip(3, 4);
    }

    private static void runUnifiedMapValuesIteratorRemoveFlip(int shift, int removeStride)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        Collection<String> values = map.values();

        int count = 0;
        for (Iterator<String> it = values.iterator(); it.hasNext(); )
        {
            String value = it.next();
            int x = Integer.parseInt(value.substring(1));
            count++;
            if (x % removeStride != 0)
            {
                it.remove();
            }
        }
        Assert.assertEquals(size, count);

        for (int i = 0; i < size; i++)
        {
            if (i % removeStride == 0)
            {
                Assert.assertTrue("map contains " + i + "for shift " + shift + " and remove stride " + removeStride, map
                        .containsKey(new CollidingInt(i, shift)));
                Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
            }
        }
    }

    @Test
    public void unifiedMapSerialize()
    {
        UnifiedMapTest.runUnifiedMapSerialize(0);
        UnifiedMapTest.runUnifiedMapSerialize(1);
        UnifiedMapTest.runUnifiedMapSerialize(2);
        UnifiedMapTest.runUnifiedMapSerialize(3);
    }

    private static void runUnifiedMapSerialize(int shift)
    {
        UnifiedMap<CollidingInt, String> map = UnifiedMap.newMap();

        int size = 100000;
        for (int i = 0; i < size; i++)
        {
            map.put(new CollidingInt(i, shift), UnifiedMapTest.createVal(i));
        }
        String nullVal = "Y99999999";
        map.put(null, nullVal);
        map = SerializeTestHelper.serializeDeserialize(map);

        Verify.assertSize(size + 1, map);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContainsKey(new CollidingInt(i, shift), map);
            Verify.assertContainsKeyValue(new CollidingInt(i, shift), UnifiedMapTest.createVal(i), map);
        }
        Assert.assertTrue(map.containsKey(null));
        Assert.assertEquals(nullVal, map.get(null));

        Set<CollidingInt> keySet = SerializeTestHelper.serializeDeserialize(map.keySet());
        Verify.assertSize(size + 1, keySet);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContains(new CollidingInt(i, shift), keySet);
        }
        Verify.assertContains(null, keySet);

        Set<Map.Entry<CollidingInt, String>> entrySet = SerializeTestHelper.serializeDeserialize(map.entrySet());
        Verify.assertSize(size + 1, entrySet);
        for (int i = 0; i < size; i++)
        {
            Verify.assertContains(new Entry(new CollidingInt(i, shift), UnifiedMapTest.createVal(i)), entrySet);
        }
        Verify.assertContains(new Entry(null, nullVal), entrySet);

        for (Map.Entry<CollidingInt, String> e : entrySet)
        {
            CollidingInt key = e.getKey();
            if (key == null)
            {
                Assert.assertEquals(nullVal, e.getValue());
            }
            else
            {
                Assert.assertEquals(UnifiedMapTest.createVal(key.getValue()), e.getValue());
            }
        }

        List<String> values = new ArrayList<String>(SerializeTestHelper.serializeDeserialize(map.values()));
        Collections.sort(values, UnifiedMapTest.VALUE_COMPARATOR);
        Verify.assertSize(size + 1, values);
        for (int i = 0; i < size; i++)
        {
            Assert.assertEquals(UnifiedMapTest.createVal(i), values.get(i));
        }
        Assert.assertEquals(nullVal, values.get(values.size() - 1));
    }

    public void perfTestUnifiedMapGet()
    {
        for (int i = 1000000; i > 10; i /= 10)
        {
            this.runGetTest(new UnifiedMap<CollidingInt, String>(), "Unified Map", i);
        }
    }

    public void perfTestJdkHashMapGet()
    {
        for (int i = 1000000; i > 10; i /= 10)
        {
            this.runGetTest(new HashMap<CollidingInt, String>(), "JDK HashMap", i);
        }
    }

    public void perfTestUnifiedMapCollidingGet()
    {
        this.runCollidingGetTest(new UnifiedMap<CollidingInt, String>(), "Unified Map", 1);
        this.runCollidingGetTest(new UnifiedMap<CollidingInt, String>(), "Unified Map", 2);
        this.runCollidingGetTest(new UnifiedMap<CollidingInt, String>(), "Unified Map", 3);
    }

    public void perfTestJdkHashMapCollidingGet()
    {
        this.runCollidingGetTest(new HashMap<CollidingInt, String>(), "JDK HashMap", 1);
        this.runCollidingGetTest(new HashMap<CollidingInt, String>(), "JDK HashMap", 2);
        this.runCollidingGetTest(new HashMap<CollidingInt, String>(), "JDK HashMap", 3);
    }

    private void runGetTest(Map<CollidingInt, String> map, String mapName, int size)
    {
        Integer[] keys = UnifiedMapTest.createMap((Map<Object, String>) (Map<?, ?>) map, size);
        UnifiedMapTest.sleep(100L);
        int n = 10000000 / size;
        int max = 4;
        for (int i = 0; i < max; i++)
        {
            long startTime = System.nanoTime();
            UnifiedMapTest.runMapGet(map, keys, n);
            long runTimes = System.nanoTime() - startTime;
            LOGGER.info(mapName + " get: " + (double) runTimes / (double) n / size + " ns per get on map size " + size);
        }
        map = null;
        System.gc();
        Thread.yield();
        System.gc();
        Thread.yield();
    }

    private static Integer[] createMap(Map<Object, String> map, int size)
    {
        Integer[] keys = new Integer[size];
        for (int i = 0; i < size; i++)
        {
            keys[i] = i;
            map.put(i, UnifiedMapTest.createVal(i));
        }
        Verify.assertSize(size, map);
        return keys;
    }

    private static void runMapGet(Map<CollidingInt, String> map, Object[] keys, int n)
    {
        for (int i = 0; i < n; i++)
        {
            for (Object key : keys)
            {
                map.get(key);
            }
        }
    }

    private void runCollidingGetTest(Map<CollidingInt, String> map, String mapName, int shift)
    {
        int size = 100000;
        Object[] keys = UnifiedMapTest.createCollidingMap(map, size, shift);
        UnifiedMapTest.sleep(100L);
        int n = 100;
        int max = 5;
        for (int i = 0; i < max; i++)
        {
            long startTime = System.nanoTime();
            UnifiedMapTest.runMapGet(map, keys, n);
            long runTimes = System.nanoTime() - startTime;
            LOGGER.info(mapName + " with " + (1 << shift) + " collisions. get: " + (double) runTimes / (double) n / size
                    + " ns per get");
        }
    }

    private static CollidingInt[] createCollidingMap(Map<CollidingInt, String> map, int size, int shift)
    {
        CollidingInt[] keys = new CollidingInt[size];
        for (int i = 0; i < size; i++)
        {
            keys[i] = new CollidingInt(i, shift);
            map.put(keys[i], UnifiedMapTest.createVal(i));
        }
        Assert.assertEquals(size, map.size());
        return keys;
    }

    private static void sleep(long millis)
    {
        long now = System.currentTimeMillis();
        long target = now + millis;
        while (now < target)
        {
            try
            {
                Thread.sleep(target - now);
            }
            catch (InterruptedException ignored)
            {
                Assert.fail("why were we interrupted?");
            }
            now = System.currentTimeMillis();
        }
    }

    public static final class Entry implements Map.Entry<CollidingInt, String>
    {
        private final CollidingInt key;
        private String value;

        private Entry(CollidingInt key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public CollidingInt getKey()
        {
            return this.key;
        }

        public String getValue()
        {
            return this.value;
        }

        public String setValue(String value)
        {
            String ret = this.value;
            this.value = value;
            return ret;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (!(o instanceof Map.Entry))
            {
                return false;
            }

            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;

            if (!Comparators.nullSafeEquals(this.key, entry.getKey()))
            {
                return false;
            }
            return Comparators.nullSafeEquals(this.value, entry.getValue());
        }

        @Override
        public int hashCode()
        {
            return this.key == null ? 0 : this.key.hashCode();
        }
    }

    @Test
    public void unifiedMapToString()
    {
        UnifiedMap<Object, Object> map = UnifiedMap.<Object, Object>newWithKeysValues(1, "One", 2, "Two");
        Verify.assertContains("1=One", map.toString());
        Verify.assertContains("2=Two", map.toString());
        map.put("value is 'self'", map);
        Verify.assertContains("value is 'self'=(this Map)", map.toString());
    }
}
