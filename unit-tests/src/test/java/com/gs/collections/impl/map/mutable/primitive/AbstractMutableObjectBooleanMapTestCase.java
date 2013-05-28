/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.map.mutable.primitive;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.BooleanFunction0;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.ObjectBooleanPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectBooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.primitive.MutableObjectBooleanMap;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractMutableObjectBooleanMapTestCase
{
    protected abstract MutableObjectBooleanMap<String> classUnderTest();

    protected abstract <T> MutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1);

    protected abstract <T> MutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2);

    protected abstract <T> MutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2, T key3, boolean value3);

    protected abstract <T> MutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2, T key3, boolean value3, T key4, boolean value4);

    protected abstract <T> MutableObjectBooleanMap<T> getEmptyMap();

    private final MutableObjectBooleanMap<String> map = this.classUnderTest();

    protected static MutableList<String> generateCollisions()
    {
        MutableList<String> collisions = FastList.newList();
        ObjectBooleanHashMap<String> hashMap = new ObjectBooleanHashMap<String>();
        for (int each = 3; collisions.size() <= 10; each++)
        {
            if (hashMap.index(String.valueOf(each)) == hashMap.index(String.valueOf(3)))
            {
                collisions.add(String.valueOf(each));
            }
        }
        return collisions;
    }

    @Test
    public void clear()
    {
        MutableObjectBooleanMap<String> hashMap = this.getEmptyMap();
        hashMap.put("0", true);
        hashMap.clear();
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), hashMap);

        hashMap.put("1", false);
        hashMap.clear();
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), hashMap);

        hashMap.put(null, true);
        hashMap.clear();
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), hashMap);
    }

    @Test
    public void removeKey()
    {
        MutableObjectBooleanMap<String> map0 = this.newWithKeysValues("0", true, "1", false);
        map0.removeKey("1");
        Assert.assertEquals(this.newWithKeysValues("0", true), map0);
        map0.removeKey("0");
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), map0);

        MutableObjectBooleanMap<String> map1 = this.newWithKeysValues("0", false, "1", true);
        map1.removeKey("0");
        Assert.assertEquals(this.newWithKeysValues("1", true), map1);
        map1.removeKey("1");
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), map1);

        this.map.removeKey("5");
        Assert.assertEquals(this.newWithKeysValues("0", true, "1", true, "2", false), this.map);
        this.map.removeKey("0");
        Assert.assertEquals(this.newWithKeysValues("1", true, "2", false), this.map);
        this.map.removeKey("1");
        Assert.assertEquals(this.newWithKeysValues("2", false), this.map);
        this.map.removeKey("2");
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), this.map);
        this.map.removeKey("0");
        this.map.removeKey("1");
        this.map.removeKey("2");
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), this.map);
        Verify.assertEmpty(this.map);

        this.map.put(null, true);
        Assert.assertTrue(this.map.get(null));
        this.map.removeKey(null);
        Assert.assertFalse(this.map.get(null));
    }

    @Test
    public void put()
    {
        this.map.put("0", false);
        this.map.put("1", false);
        this.map.put("2", true);
        ObjectBooleanHashMap<String> expected = ObjectBooleanHashMap.newWithKeysValues("0", false, "1", false, "2", true);
        Assert.assertEquals(expected, this.map);

        this.map.put("5", true);
        expected.put("5", true);
        Assert.assertEquals(expected, this.map);

        this.map.put(null, false);
        expected.put(null, false);
        Assert.assertEquals(expected, this.map);
    }

    @Test
    public void putDuplicateWithRemovedSlot()
    {
        String collision1 = generateCollisions().getFirst();
        String collision2 = generateCollisions().get(1);
        String collision3 = generateCollisions().get(2);
        String collision4 = generateCollisions().get(3);

        MutableObjectBooleanMap<String> hashMap = this.getEmptyMap();
        hashMap.put(collision1, true);
        hashMap.put(collision2, false);
        hashMap.put(collision3, true);
        Assert.assertFalse(hashMap.get(collision2));
        hashMap.removeKey(collision2);
        hashMap.put(collision4, false);
        Assert.assertEquals(this.newWithKeysValues(collision1, true, collision3, true, collision4, false), hashMap);

        MutableObjectBooleanMap<String> hashMap1 = this.getEmptyMap();
        hashMap1.put(collision1, false);
        hashMap1.put(collision2, false);
        hashMap1.put(collision3, true);
        Assert.assertFalse(hashMap1.get(collision1));
        hashMap1.removeKey(collision1);
        hashMap1.put(collision4, true);
        Assert.assertEquals(this.newWithKeysValues(collision2, false, collision3, true, collision4, true), hashMap1);

        MutableObjectBooleanMap<String> hashMap2 = this.getEmptyMap();
        hashMap2.put(collision1, true);
        hashMap2.put(collision2, true);
        hashMap2.put(collision3, false);
        Assert.assertFalse(hashMap2.get(collision3));
        hashMap2.removeKey(collision3);
        hashMap2.put(collision4, false);
        Assert.assertEquals(this.newWithKeysValues(collision1, true, collision2, true, collision4, false), hashMap2);

        MutableObjectBooleanMap<String> hashMap3 = this.getEmptyMap();
        hashMap3.put(collision1, true);
        hashMap3.put(collision2, true);
        hashMap3.put(collision3, false);
        Assert.assertTrue(hashMap3.get(collision2));
        Assert.assertFalse(hashMap3.get(collision3));
        hashMap3.removeKey(collision2);
        hashMap3.removeKey(collision3);
        hashMap3.put(collision4, false);
        Assert.assertEquals(this.newWithKeysValues(collision1, true, collision4, false), hashMap3);

        MutableObjectBooleanMap<String> hashMap4 = this.getEmptyMap();
        hashMap4.put(null, false);
        Assert.assertEquals(this.newWithKeysValues(null, false), hashMap4);
        hashMap4.put(null, true);
        Assert.assertEquals(this.newWithKeysValues(null, true), hashMap4);
    }

    @Test
    public void get()
    {
        Assert.assertTrue(this.map.get("0"));
        Assert.assertTrue(this.map.get("1"));
        Assert.assertFalse(this.map.get("2"));

        Assert.assertFalse(this.map.get("5"));

        this.map.put("0", false);
        Assert.assertFalse(this.map.get("0"));

        this.map.put("5", true);
        Assert.assertTrue(this.map.get("5"));

        this.map.put(null, true);
        Assert.assertTrue(this.map.get(null));
    }

    @Test
    public void getIfAbsent()
    {
        Assert.assertTrue(this.map.getIfAbsent("0", false));
        Assert.assertTrue(this.map.getIfAbsent("1", false));
        Assert.assertFalse(this.map.getIfAbsent("2", true));

        this.map.removeKey("0");
        Assert.assertTrue(this.map.getIfAbsent("0", true));
        Assert.assertFalse(this.map.getIfAbsent("0", false));

        Assert.assertTrue(this.map.getIfAbsent("5", true));
        Assert.assertFalse(this.map.getIfAbsent("5", false));

        Assert.assertTrue(this.map.getIfAbsent(null, true));
        Assert.assertFalse(this.map.getIfAbsent(null, false));

        this.map.put("0", false);
        Assert.assertFalse(this.map.getIfAbsent("0", true));

        this.map.put("5", true);
        Assert.assertTrue(this.map.getIfAbsent("5", false));

        this.map.put(null, false);
        Assert.assertFalse(this.map.getIfAbsent(null, true));
    }

    @Test
    public void getOrThrow()
    {
        Assert.assertTrue(this.map.getOrThrow("0"));
        Assert.assertTrue(this.map.getOrThrow("1"));
        Assert.assertFalse(this.map.getOrThrow("2"));

        this.map.removeKey("0");
        Verify.assertThrows(IllegalStateException.class, new Runnable()
        {
            public void run()
            {
                AbstractMutableObjectBooleanMapTestCase.this.map.getOrThrow("0");
            }
        });
        Verify.assertThrows(IllegalStateException.class, new Runnable()
        {
            public void run()
            {
                AbstractMutableObjectBooleanMapTestCase.this.map.getOrThrow("5");
            }
        });
        Verify.assertThrows(IllegalStateException.class, new Runnable()
        {
            public void run()
            {
                AbstractMutableObjectBooleanMapTestCase.this.map.getOrThrow(null);
            }
        });

        this.map.put("0", false);
        Assert.assertFalse(this.map.getOrThrow("0"));

        this.map.put("5", true);
        Assert.assertTrue(this.map.getOrThrow("5"));

        this.map.put(null, false);
        Assert.assertFalse(this.map.getOrThrow(null));
    }

    @Test
    public void getIfAbsentPut_Function()
    {
        BooleanFunction0 factory = new BooleanFunction0()
        {
            public boolean value()
            {
                return true;
            }
        };

        MutableObjectBooleanMap<Integer> map1 = this.getEmptyMap();
        Assert.assertTrue(map1.getIfAbsentPut(0, factory));
        BooleanFunction0 factoryThrows = new BooleanFunction0()
        {
            public boolean value()
            {
                throw new AssertionError();
            }
        };
        Assert.assertTrue(map1.getIfAbsentPut(0, factoryThrows));
        Assert.assertEquals(this.newWithKeysValues(0, true), map1);
        Assert.assertTrue(map1.getIfAbsentPut(1, factory));
        Assert.assertTrue(map1.getIfAbsentPut(1, factoryThrows));
        Assert.assertEquals(this.newWithKeysValues(0, true, 1, true), map1);

        MutableObjectBooleanMap<Integer> map2 = this.getEmptyMap();
        BooleanFunction0 factoryFalse = new BooleanFunction0()
        {
            public boolean value()
            {
                return false;
            }
        };
        Assert.assertFalse(map2.getIfAbsentPut(1, factoryFalse));
        Assert.assertFalse(map2.getIfAbsentPut(1, factoryThrows));
        Assert.assertEquals(this.newWithKeysValues(1, false), map2);
        Assert.assertFalse(map2.getIfAbsentPut(0, factoryFalse));
        Assert.assertFalse(map2.getIfAbsentPut(0, factoryThrows));
        Assert.assertEquals(this.newWithKeysValues(0, false, 1, false), map2);

        MutableObjectBooleanMap<Integer> map3 = this.getEmptyMap();
        Assert.assertTrue(map3.getIfAbsentPut(null, factory));
        Assert.assertTrue(map3.getIfAbsentPut(null, factoryThrows));
        Assert.assertEquals(this.newWithKeysValues(null, true), map3);
    }

    @Test
    public void getIfAbsentPutWith()
    {
        BooleanFunction<String> functionLengthEven = new BooleanFunction<String>()
        {
            public boolean booleanValueOf(String string)
            {
                return (string.length() & 1) == 0;
            }
        };

        MutableObjectBooleanMap<Integer> map1 = this.getEmptyMap();
        Assert.assertFalse(map1.getIfAbsentPutWith(0, functionLengthEven, "123456789"));
        BooleanFunction<String> functionThrows = new BooleanFunction<String>()
        {
            public boolean booleanValueOf(String string)
            {
                throw new AssertionError();
            }
        };
        Assert.assertFalse(map1.getIfAbsentPutWith(0, functionThrows, "unused"));
        Assert.assertEquals(this.newWithKeysValues(0, false), map1);
        Assert.assertFalse(map1.getIfAbsentPutWith(1, functionLengthEven, "123456789"));
        Assert.assertFalse(map1.getIfAbsentPutWith(1, functionThrows, "unused"));
        Assert.assertEquals(this.newWithKeysValues(0, false, 1, false), map1);

        MutableObjectBooleanMap<Integer> map2 = this.getEmptyMap();
        Assert.assertTrue(map2.getIfAbsentPutWith(1, functionLengthEven, "1234567890"));
        Assert.assertTrue(map2.getIfAbsentPutWith(1, functionThrows, "unused0"));
        Assert.assertEquals(this.newWithKeysValues(1, true), map2);
        Assert.assertTrue(map2.getIfAbsentPutWith(0, functionLengthEven, "1234567890"));
        Assert.assertTrue(map2.getIfAbsentPutWith(0, functionThrows, "unused0"));
        Assert.assertEquals(this.newWithKeysValues(0, true, 1, true), map2);

        MutableObjectBooleanMap<Integer> map3 = this.getEmptyMap();
        Assert.assertFalse(map3.getIfAbsentPutWith(null, functionLengthEven, "123456789"));
        Assert.assertFalse(map3.getIfAbsentPutWith(null, functionThrows, "unused"));
        Assert.assertEquals(this.newWithKeysValues(null, false), map3);
    }

    @Test
    public void getIfAbsentPutWithKey()
    {
        BooleanFunction<Integer> function = new BooleanFunction<Integer>()
        {
            public boolean booleanValueOf(Integer anObject)
            {
                return anObject == null || (anObject & 1) == 0;
            }
        };

        MutableObjectBooleanMap<Integer> map1 = this.getEmptyMap();
        Assert.assertTrue(map1.getIfAbsentPutWithKey(0, function));
        BooleanFunction<Integer> functionThrows = new BooleanFunction<Integer>()
        {
            public boolean booleanValueOf(Integer anObject)
            {
                throw new AssertionError();
            }
        };
        Assert.assertTrue(map1.getIfAbsentPutWithKey(0, functionThrows));
        Assert.assertEquals(this.newWithKeysValues(0, true), map1);
        Assert.assertFalse(map1.getIfAbsentPutWithKey(1, function));
        Assert.assertFalse(map1.getIfAbsentPutWithKey(1, functionThrows));
        Assert.assertEquals(this.newWithKeysValues(0, true, 1, false), map1);

        MutableObjectBooleanMap<Integer> map2 = this.getEmptyMap();
        Assert.assertFalse(map2.getIfAbsentPutWithKey(1, function));
        Assert.assertFalse(map2.getIfAbsentPutWithKey(1, functionThrows));
        Assert.assertEquals(this.newWithKeysValues(1, false), map2);
        Assert.assertTrue(map2.getIfAbsentPutWithKey(0, function));
        Assert.assertTrue(map2.getIfAbsentPutWithKey(0, functionThrows));
        Assert.assertEquals(this.newWithKeysValues(0, true, 1, false), map2);

        MutableObjectBooleanMap<Integer> map3 = this.getEmptyMap();
        Assert.assertTrue(map3.getIfAbsentPutWithKey(null, function));
        Assert.assertTrue(map3.getIfAbsentPutWithKey(null, functionThrows));
        Assert.assertEquals(this.newWithKeysValues(null, true), map3);
    }

    @Test
    public void containsKey()
    {
        Assert.assertTrue(this.map.containsKey("0"));
        Assert.assertTrue(this.map.containsKey("1"));
        Assert.assertTrue(this.map.containsKey("2"));
        Assert.assertFalse(this.map.containsKey("3"));
        Assert.assertFalse(this.map.containsKey(null));

        this.map.removeKey("0");
        Assert.assertFalse(this.map.containsKey("0"));
        Assert.assertFalse(this.map.get("0"));
        this.map.removeKey("0");
        Assert.assertFalse(this.map.containsKey("0"));
        Assert.assertFalse(this.map.get("0"));

        this.map.removeKey("1");
        Assert.assertFalse(this.map.containsKey("1"));
        Assert.assertFalse(this.map.get("1"));

        this.map.removeKey("2");
        Assert.assertFalse(this.map.containsKey("2"));
        Assert.assertFalse(this.map.get("2"));

        this.map.removeKey("3");
        Assert.assertFalse(this.map.containsKey("3"));
        Assert.assertFalse(this.map.get("3"));

        this.map.put(null, true);
        Assert.assertTrue(this.map.containsKey(null));
        this.map.removeKey(null);
        Assert.assertFalse(this.map.containsKey(null));
    }

    @Test
    public void containsValue()
    {
        Assert.assertTrue(this.map.containsValue(true));
        Assert.assertTrue(this.map.containsValue(false));
        this.map.clear();

        this.map.put("5", true);
        Assert.assertTrue(this.map.containsValue(true));

        this.map.put(null, false);
        Assert.assertTrue(this.map.containsValue(false));

        this.map.removeKey("5");
        Assert.assertFalse(this.map.containsValue(true));

        this.map.removeKey(null);
        Assert.assertFalse(this.map.containsValue(false));
    }

    @Test
    public void size()
    {
        Verify.assertSize(0, this.getEmptyMap());
        Verify.assertSize(1, this.getEmptyMap().withKeyValue(0, false));
        Verify.assertSize(1, this.getEmptyMap().withKeyValue(1, true));
        Verify.assertSize(1, this.getEmptyMap().withKeyValue(null, false));

        MutableObjectBooleanMap<Integer> hashMap1 = this.newWithKeysValues(1, true, 0, false);
        Verify.assertSize(2, hashMap1);
        hashMap1.removeKey(1);
        Verify.assertSize(1, hashMap1);
        hashMap1.removeKey(0);
        Verify.assertSize(0, hashMap1);

        Verify.assertSize(2, this.newWithKeysValues(1, false, 5, false));
        Verify.assertSize(2, this.newWithKeysValues(0, true, 5, true));
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.getEmptyMap());
        Assert.assertFalse(this.map.isEmpty());
        Assert.assertFalse(this.newWithKeysValues(null, false).isEmpty());
        Assert.assertFalse(this.newWithKeysValues(1, true).isEmpty());
        Assert.assertFalse(this.newWithKeysValues(0, false).isEmpty());
        Assert.assertFalse(this.newWithKeysValues(50, true).isEmpty());
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.getEmptyMap().notEmpty());
        Assert.assertTrue(this.map.notEmpty());
        Assert.assertTrue(this.newWithKeysValues(1, true).notEmpty());
        Assert.assertTrue(this.newWithKeysValues(null, false).notEmpty());
        Assert.assertTrue(this.newWithKeysValues(0, true).notEmpty());
        Assert.assertTrue(this.newWithKeysValues(50, false).notEmpty());
    }

    @Test
    public void testEquals()
    {
        MutableObjectBooleanMap<Integer> map1 = this.newWithKeysValues(0, true, 1, false, null, false);
        MutableObjectBooleanMap<Integer> map2 = this.newWithKeysValues(null, false, 0, true, 1, false);
        MutableObjectBooleanMap<Integer> map3 = this.newWithKeysValues(0, true, 1, true, null, false);
        MutableObjectBooleanMap<Integer> map4 = this.newWithKeysValues(0, false, 1, false, null, false);
        MutableObjectBooleanMap<Integer> map5 = this.newWithKeysValues(0, true, 1, false, null, true);
        MutableObjectBooleanMap<Integer> map6 = this.newWithKeysValues(null, true, 60, false, 70, true);
        MutableObjectBooleanMap<Integer> map7 = this.newWithKeysValues(null, true, 60, false);
        MutableObjectBooleanMap<Integer> map8 = this.newWithKeysValues(0, true, 1, false);

        Verify.assertEqualsAndHashCode(map1, map2);
        Verify.assertPostSerializedEqualsAndHashCode(map1);
        Verify.assertPostSerializedEqualsAndHashCode(this.getEmptyMap());
        Assert.assertNotEquals(map1, map3);
        Assert.assertNotEquals(map1, map4);
        Assert.assertNotEquals(map1, map5);
        Assert.assertNotEquals(map7, map6);
        Assert.assertNotEquals(map7, map8);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues(0, false, 1, true, 32, true).hashCode(),
                this.newWithKeysValues(32, true, 0, false, 1, true).hashCode());
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues(50, true, 60, true, null, false).hashCode(),
                this.newWithKeysValues(50, true, 60, true, null, false).hashCode());
        Assert.assertEquals(UnifiedMap.newMap().hashCode(), this.getEmptyMap().hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", this.getEmptyMap().toString());
        Assert.assertEquals("[0=false]", this.newWithKeysValues(0, false).toString());
        Assert.assertEquals("[1=true]", this.newWithKeysValues(1, true).toString());
        Assert.assertEquals("[5=true]", this.newWithKeysValues(5, true).toString());

        MutableObjectBooleanMap<Integer> map1 = this.newWithKeysValues(0, true, 1, false);
        Assert.assertTrue(
                map1.toString(),
                "[0=true, 1=false]".equals(map1.toString())
                        || "[1=false, 0=true]".equals(map1.toString()));

        MutableObjectBooleanMap<Integer> map2 = this.newWithKeysValues(1, false, null, true);
        Assert.assertTrue(
                map2.toString(),
                "[1=false, null=true]".equals(map2.toString())
                        || "[null=true, 1=false]".equals(map2.toString()));

        MutableObjectBooleanMap<Integer> map3 = this.newWithKeysValues(1, true, null, true);
        Assert.assertTrue(
                map3.toString(),
                "[1=true, null=true]".equals(map3.toString())
                        || "[null=true, 1=true]".equals(map3.toString()));
    }

    @Test
    public void forEachValue()
    {
        MutableObjectBooleanMap<Integer> map01 = this.newWithKeysValues(0, true, 1, false);
        final String[] sum01 = new String[1];
        sum01[0] = "";
        map01.forEachValue(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum01[0] += String.valueOf(each);
            }
        });
        Assert.assertTrue("truefalse".equals(sum01[0]) || "falsetrue".equals(sum01[0]));

        MutableObjectBooleanMap<Integer> map = this.newWithKeysValues(3, true, 4, true);
        final String[] sum = new String[1];
        sum[0] = "";
        map.forEachValue(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += String.valueOf(each);
            }
        });
        Assert.assertEquals("truetrue", sum[0]);

        MutableObjectBooleanMap<Integer> map1 = this.newWithKeysValues(3, false, null, true);
        final String[] sum1 = new String[1];
        sum1[0] = "";
        map1.forEachValue(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum1[0] += String.valueOf(each);
            }
        });
        Assert.assertTrue("truefalse".equals(sum1[0]) || "falsetrue".equals(sum1[0]));
    }

    @Test
    public void forEach()
    {
        MutableObjectBooleanMap<Integer> map01 = this.newWithKeysValues(0, true, 1, false);
        final String[] sum01 = new String[1];
        sum01[0] = "";
        map01.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum01[0] += String.valueOf(each);
            }
        });
        Assert.assertTrue("truefalse".equals(sum01[0]) || "falsetrue".equals(sum01[0]));

        MutableObjectBooleanMap<Integer> map = this.newWithKeysValues(3, true, 4, true);
        final String[] sum = new String[1];
        sum[0] = "";
        map.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += String.valueOf(each);
            }
        });
        Assert.assertEquals("truetrue", sum[0]);

        MutableObjectBooleanMap<Integer> map1 = this.newWithKeysValues(3, false, null, true);
        final String[] sum1 = new String[1];
        sum1[0] = "";
        map1.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum1[0] += String.valueOf(each);
            }
        });
        Assert.assertTrue("truefalse".equals(sum1[0]) || "falsetrue".equals(sum1[0]));
    }

    @Test
    public void forEachKey()
    {
        MutableObjectBooleanMap<Integer> map01 = this.newWithKeysValues(0, true, 1, false);
        final int[] sum01 = new int[1];
        map01.forEachKey(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                sum01[0] += each;
            }
        });
        Assert.assertEquals(1, sum01[0]);

        MutableObjectBooleanMap<Integer> map = this.newWithKeysValues(3, false, null, true);
        final String[] sum = new String[1];
        sum[0] = "";
        map.forEachKey(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                sum[0] += String.valueOf(each);
            }
        });
        Assert.assertTrue("3null".equals(sum[0]) || "null3".equals(sum[0]));
    }

    @Test
    public void forEachKeyValue()
    {
        MutableObjectBooleanMap<Integer> map01 = this.newWithKeysValues(0, true, 1, false);
        final String[] sumValue01 = new String[1];
        sumValue01[0] = "";
        final int[] sumKey01 = new int[1];
        map01.forEachKeyValue(new ObjectBooleanProcedure<Integer>()
        {
            public void value(Integer eachKey, boolean eachValue)
            {
                sumKey01[0] += eachKey;
                sumValue01[0] += eachValue;
            }
        });
        Assert.assertEquals(1, sumKey01[0]);
        Assert.assertTrue("truefalse".equals(sumValue01[0]) || "falsetrue".equals(sumValue01[0]));

        MutableObjectBooleanMap<Integer> map = this.newWithKeysValues(3, true, null, false);
        final String[] sumKey = new String[1];
        sumKey[0] = "";
        final String[] sumValue = new String[1];
        sumValue[0] = "";
        map.forEachKeyValue(new ObjectBooleanProcedure<Integer>()
        {
            public void value(Integer eachKey, boolean eachValue)
            {
                sumKey[0] += String.valueOf(eachKey);
                sumValue[0] += eachValue;
            }
        });
        Assert.assertTrue(sumKey[0], "3null".equals(sumKey[0]) || "null3".equals(sumKey[0]));
        Assert.assertTrue("truefalse".equals(sumValue[0]) || "falsetrue".equals(sumValue[0]));
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("", this.<String>getEmptyMap().makeString());
        Assert.assertEquals("0=true", this.newWithKeysValues(0, true).makeString());
        Assert.assertEquals("1=false", this.newWithKeysValues(1, false).makeString());
        Assert.assertEquals("null=true", this.newWithKeysValues(null, true).makeString());

        MutableObjectBooleanMap<Integer> map2 = this.newWithKeysValues(1, true, 32, false);
        Assert.assertTrue(
                map2.makeString("[", "/", "]"),
                "[1=true/32=false]".equals(map2.makeString("[", "/", "]"))
                        || "[32=false/1=true]".equals(map2.makeString("[", "/", "]")));

        Assert.assertTrue(
                map2.makeString("/"),
                "1=true/32=false".equals(map2.makeString("/"))
                        || "32=false/1=true".equals(map2.makeString("/")));
    }

    @Test
    public void appendString()
    {
        Appendable appendable = new StringBuilder();
        this.getEmptyMap().appendString(appendable);
        Assert.assertEquals("", appendable.toString());

        Appendable appendable0 = new StringBuilder();
        this.newWithKeysValues(0, true).appendString(appendable0);
        Assert.assertEquals("0=true", appendable0.toString());

        Appendable appendable1 = new StringBuilder();
        this.newWithKeysValues(1, false).appendString(appendable1);
        Assert.assertEquals("1=false", appendable1.toString());

        Appendable appendable2 = new StringBuilder();
        this.newWithKeysValues(null, false).appendString(appendable2);
        Assert.assertEquals("null=false", appendable2.toString());

        Appendable appendable3 = new StringBuilder();
        MutableObjectBooleanMap<Integer> map1 = this.newWithKeysValues(0, true, 1, false);
        map1.appendString(appendable3);
        Assert.assertTrue(
                appendable3.toString(),
                "0=true, 1=false".equals(appendable3.toString())
                        || "1=false, 0=true".equals(appendable3.toString()));

        Appendable appendable4 = new StringBuilder();
        map1.appendString(appendable4, "/");
        Assert.assertTrue(
                appendable4.toString(),
                "0=true/1=false".equals(appendable4.toString())
                        || "1=false/0=true".equals(appendable4.toString()));

        Appendable appendable5 = new StringBuilder();
        map1.appendString(appendable5, "[", "/", "]");
        Assert.assertTrue(
                appendable5.toString(),
                "[0=true/1=false]".equals(appendable5.toString())
                        || "[1=false/0=true]".equals(appendable5.toString()));
    }

    @Test
    public void withKeysValues()
    {
        MutableObjectBooleanMap<Integer> emptyMap = this.getEmptyMap();
        MutableObjectBooleanMap<Integer> hashMap = emptyMap.withKeyValue(1, true);
        Assert.assertEquals(this.newWithKeysValues(1, true), hashMap);
        Assert.assertSame(emptyMap, hashMap);
    }

    @Test
    public void withoutKey()
    {
        MutableObjectBooleanMap<Integer> hashMap = this.newWithKeysValues(1, true, 2, true, 3, false, 4, false);
        MutableObjectBooleanMap<Integer> actual = hashMap.withoutKey(5);
        Assert.assertSame(hashMap, actual);
        Assert.assertEquals(this.newWithKeysValues(1, true, 2, true, 3, false, 4, false), actual);
        Assert.assertEquals(this.newWithKeysValues(1, true, 2, true, 3, false), hashMap.withoutKey(4));
        Assert.assertEquals(this.newWithKeysValues(1, true, 2, true), hashMap.withoutKey(3));
        Assert.assertEquals(this.newWithKeysValues(1, true), hashMap.withoutKey(2));
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), hashMap.withoutKey(1));
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), hashMap.withoutKey(1));
    }

    @Test
    public void withoutAllKeys()
    {
        MutableObjectBooleanMap<Integer> hashMap = this.newWithKeysValues(1, true, 2, true, 3, false, 4, false);
        MutableObjectBooleanMap<Integer> actual = hashMap.withoutAllKeys(FastList.newListWith(5, 6, 7));
        Assert.assertSame(hashMap, actual);
        Assert.assertEquals(this.newWithKeysValues(1, true, 2, true, 3, false, 4, false), actual);
        Assert.assertEquals(this.newWithKeysValues(1, true, 2, true), hashMap.withoutAllKeys(FastList.newListWith(5, 4, 3)));
        Assert.assertEquals(this.newWithKeysValues(1, true), hashMap.withoutAllKeys(FastList.newListWith(2)));
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), hashMap.withoutAllKeys(FastList.newListWith(1)));
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), hashMap.withoutAllKeys(FastList.newListWith(5, 6)));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true), this.map.select(BooleanPredicates.isTrue()).toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false), this.map.select(BooleanPredicates.isFalse()).toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, false), this.map.select(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())).toBag());
        Assert.assertEquals(new BooleanHashBag(), this.map.select(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())).toBag());

        Assert.assertEquals(this.newWithKeysValues("0", true), this.map.select(new ObjectBooleanPredicate<String>()
        {
            public boolean accept(String object, boolean value)
            {
                return (Integer.parseInt(object) & 1) == 0 && value;
            }
        }));
        Assert.assertEquals(this.newWithKeysValues("2", false), this.map.select(new ObjectBooleanPredicate<String>()
        {
            public boolean accept(String object, boolean value)
            {
                return (Integer.parseInt(object) & 1) == 0 && !value;
            }
        }));
        Assert.assertEquals(ObjectBooleanHashMap.newMap(), this.map.select(new ObjectBooleanPredicate<String>()
        {
            public boolean accept(String object, boolean value)
            {
                return (Integer.parseInt(object) & 1) != 0 && !value;
            }
        }));
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(false), this.map.reject(BooleanPredicates.isTrue()).toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true), this.map.reject(BooleanPredicates.isFalse()).toBag());
        Assert.assertEquals(new BooleanHashBag(), this.map.reject(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())).toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, false), this.map.reject(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())).toBag());

        Assert.assertEquals(this.newWithKeysValues("1", true, "2", false), this.map.reject(new ObjectBooleanPredicate<String>()
        {
            public boolean accept(String object, boolean value)
            {
                return (Integer.parseInt(object) & 1) == 0 && value;
            }
        }));
        Assert.assertEquals(this.newWithKeysValues("0", true, "1", true), this.map.reject(new ObjectBooleanPredicate<String>()
        {
            public boolean accept(String object, boolean value)
            {
                return (Integer.parseInt(object) & 1) == 0 && !value;
            }
        }));
        Assert.assertEquals(this.newWithKeysValues("0", true, "1", true, "2", false), this.map.reject(new ObjectBooleanPredicate<String>()
        {
            public boolean accept(String object, boolean value)
            {
                return (Integer.parseInt(object) & 1) != 0 && !value;
            }
        }));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, this.map.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(1L, this.map.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(3L, this.map.count(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertEquals(0L, this.map.count(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.map.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.map.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.map.anySatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.map.anySatisfy(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(this.map.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.map.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.map.allSatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.map.allSatisfy(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertFalse(this.map.noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.map.noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.map.noneSatisfy(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.map.noneSatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertTrue(this.map.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(this.map.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(this.newWithKeysValues("0", true, "1", true).detectIfNone(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), false));
    }

    @Test
    public void collect()
    {
        MutableObjectBooleanMap<String> map1 = this.newWithKeysValues("0", true, "1", false);
        MutableObjectBooleanMap<String> map2 = this.newWithKeysValues("0", true);
        MutableObjectBooleanMap<String> map3 = this.newWithKeysValues("0", false);
        BooleanToObjectFunction<String> stringValueOf = new BooleanToObjectFunction<String>()
        {
            public String valueOf(boolean booleanParameter)
            {
                return String.valueOf(booleanParameter);
            }
        };
        Assert.assertTrue(FastList.newListWith("true", "false").equals(map1.collect(stringValueOf)) || FastList.newListWith("false", "true").equals(map1.collect(stringValueOf)));
        Assert.assertEquals(FastList.newListWith("true"), map2.collect(stringValueOf));
        Assert.assertEquals(FastList.newListWith("false"), map3.collect(stringValueOf));
    }

    @Test
    public void toArray()
    {
        MutableObjectBooleanMap<String> map1 = this.newWithKeysValues(null, true, "1", false);
        MutableObjectBooleanMap<String> map2 = this.newWithKeysValues("0", true);
        MutableObjectBooleanMap<String> map3 = this.newWithKeysValues("0", false);

        Assert.assertTrue(Arrays.equals(new boolean[]{true, false}, map1.toArray())
                || Arrays.equals(new boolean[]{false, true}, map1.toArray()));
        Assert.assertTrue(Arrays.equals(new boolean[]{true}, map2.toArray()));
        Assert.assertTrue(Arrays.equals(new boolean[]{false}, map3.toArray()));
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(this.map.contains(true));
        Assert.assertTrue(this.map.contains(false));
        this.map.clear();

        this.map.put("5", true);
        Assert.assertTrue(this.map.contains(true));

        this.map.put(null, false);
        Assert.assertTrue(this.map.contains(false));

        this.map.removeKey("5");
        Assert.assertFalse(this.map.contains(true));
        Assert.assertTrue(this.map.contains(false));

        this.map.removeKey(null);
        Assert.assertFalse(this.map.contains(false));
    }

    @Test
    public void containsAll()
    {
        Assert.assertTrue(this.map.containsAll(true, false));
        Assert.assertTrue(this.map.containsAll(true, true));
        Assert.assertTrue(this.map.containsAll(false, false));
        this.map.clear();

        this.map.put("5", true);
        Assert.assertTrue(this.map.containsAll(true));
        Assert.assertFalse(this.map.containsAll(true, false));
        Assert.assertFalse(this.map.containsAll(false, false));

        this.map.put(null, false);
        Assert.assertTrue(this.map.containsAll(false));
        Assert.assertTrue(this.map.containsAll(true, false));

        this.map.removeKey("5");
        Assert.assertFalse(this.map.containsAll(true));
        Assert.assertFalse(this.map.containsAll(true, false));
        Assert.assertTrue(this.map.containsAll(false, false));

        this.map.removeKey(null);
        Assert.assertFalse(this.map.containsAll(false, true));
    }

    @Test
    public void containsAllIterable()
    {
        Assert.assertTrue(this.map.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(this.map.containsAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertTrue(this.map.containsAll(BooleanArrayList.newListWith(false, false)));
        this.map.clear();

        this.map.put("5", true);
        Assert.assertTrue(this.map.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertFalse(this.map.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertFalse(this.map.containsAll(BooleanArrayList.newListWith(false, false)));

        this.map.put(null, false);
        Assert.assertTrue(this.map.containsAll(BooleanArrayList.newListWith(false)));
        Assert.assertTrue(this.map.containsAll(BooleanArrayList.newListWith(true, false)));

        this.map.removeKey("5");
        Assert.assertFalse(this.map.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertFalse(this.map.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(this.map.containsAll(BooleanArrayList.newListWith(false, false)));

        this.map.removeKey(null);
        Assert.assertFalse(this.map.containsAll(BooleanArrayList.newListWith(false, true)));
    }

    @Test
    public void toList()
    {
        MutableObjectBooleanMap<String> map1 = this.newWithKeysValues(null, true, "1", false);
        MutableObjectBooleanMap<String> map2 = this.newWithKeysValues("0", true);
        MutableObjectBooleanMap<String> map3 = this.newWithKeysValues("0", false);

        Assert.assertTrue(map1.toList().toString(), BooleanArrayList.newListWith(true, false).equals(map1.toList())
                || BooleanArrayList.newListWith(false, true).equals(map1.toList()));
        Assert.assertEquals(BooleanArrayList.newListWith(true), map2.toList());
        Assert.assertEquals(BooleanArrayList.newListWith(false), map3.toList());
    }

    @Test
    public void toSet()
    {
        MutableObjectBooleanMap<String> map1 = this.newWithKeysValues("1", false, null, true, "2", false);
        MutableObjectBooleanMap<String> map0 = this.newWithKeysValues("1", false, null, true, "2", true);
        MutableObjectBooleanMap<String> map2 = this.newWithKeysValues("0", true);
        MutableObjectBooleanMap<String> map3 = this.newWithKeysValues("0", false);

        Assert.assertEquals(BooleanHashSet.newSetWith(false, true), map1.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(false, true), map0.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(true), map2.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(false), map3.toSet());
    }

    @Test
    public void toBag()
    {
        MutableObjectBooleanMap<String> map1 = this.newWithKeysValues("1", false, null, true, "2", false);
        MutableObjectBooleanMap<String> map0 = this.newWithKeysValues("1", false, null, true, "2", true);
        MutableObjectBooleanMap<String> map2 = this.newWithKeysValues("0", true);
        MutableObjectBooleanMap<String> map3 = this.newWithKeysValues("0", false);

        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, true), map1.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false, true, true), map0.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(true), map2.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false), map3.toBag());
    }

    @Test
    public void asLazy()
    {
        Verify.assertSize(this.map.toList().size(), this.map.asLazy().toList());
        Assert.assertTrue(this.map.asLazy().toList().containsAll(this.map.toList()));
    }

    @Test
    public void iterator()
    {
        MutableObjectBooleanMap<String> map1 = this.newWithKeysValues(null, true, "GSCollections", false);
        MutableObjectBooleanMap<String> map2 = this.newWithKeysValues("0", true);
        MutableObjectBooleanMap<String> map3 = this.newWithKeysValues("0", false);

        final BooleanIterator iterator1 = map1.booleanIterator();
        Assert.assertTrue(iterator1.hasNext());
        boolean first = iterator1.next();
        Assert.assertTrue(iterator1.hasNext());
        boolean second = iterator1.next();
        Assert.assertEquals(first, !second);
        Assert.assertFalse(iterator1.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator1.next();
            }
        });

        final BooleanIterator iterator2 = map2.booleanIterator();
        Assert.assertTrue(iterator2.hasNext());
        Assert.assertTrue(iterator2.next());
        Assert.assertFalse(iterator2.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator2.next();
            }
        });

        final BooleanIterator iterator3 = map3.booleanIterator();
        Assert.assertTrue(iterator3.hasNext());
        Assert.assertFalse(iterator3.next());
        Assert.assertFalse(iterator3.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator3.next();
            }
        });
    }

    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableObjectBooleanMap.class, this.map.asUnmodifiable());
        Assert.assertEquals(new UnmodifiableObjectBooleanMap<String>(this.map), this.map.asUnmodifiable());
    }

    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedObjectBooleanMap.class, this.map.asSynchronized());
        Assert.assertEquals(new SynchronizedObjectBooleanMap<String>(this.map), this.map.asSynchronized());
    }
}
