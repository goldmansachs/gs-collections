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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class BooleanObjectHashMapTest
{
    private final BooleanObjectHashMap<String> map0 = new BooleanObjectHashMap<String>();
    private final BooleanObjectHashMap<String> map1 = BooleanObjectHashMap.newWithKeysValues(false, "0");
    private final BooleanObjectHashMap<String> map2 = BooleanObjectHashMap.newWithKeysValues(true, "1");
    private final BooleanObjectHashMap<String> map3 = BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0");

    @Test
    public void removeKey()
    {
        Assert.assertNull(this.map0.removeKey(true));
        Assert.assertNull(this.map0.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map0);

        Assert.assertNull(this.map1.removeKey(true));
        Assert.assertEquals("0", this.map1.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map1);

        Assert.assertNull(this.map2.removeKey(false));
        Assert.assertEquals("1", this.map2.removeKey(true));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map2);

        Assert.assertEquals("1", this.map3.removeKey(true));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map3);
        Assert.assertEquals("0", this.map3.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map3);
        Assert.assertTrue(this.map3.isEmpty());

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0");
        Assert.assertEquals("0", map.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), map);
    }

    @Test
    public void put()
    {
        Assert.assertNull(this.map0.put(true, "1"));
        Assert.assertFalse(this.map0.isEmpty());
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map0);
        Assert.assertNull(this.map0.put(false, "0"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertNull(map.put(false, "0"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), map);

        Assert.assertNull(this.map1.put(true, "1"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map1);
        Assert.assertEquals("0", this.map1.put(false, "2"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "2"), this.map1);

        Assert.assertNull(this.map2.put(false, "0"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map2);
        Assert.assertEquals("1", this.map2.put(true, "2"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "2", false, "0"), this.map2);

        Assert.assertEquals("1", this.map3.put(true, "2"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "2", false, "0"), this.map3);
        Assert.assertEquals("0", this.map3.put(false, "3"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "2", false, "3"), this.map3);
    }

    @Test
    public void getIfAbsentPut()
    {
        Function0<String> trueString = new Function0<String>()
        {
            public String value()
            {
                return "true";
            }
        };

        Assert.assertEquals("true", this.map0.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "true"), this.map0);

        Function0<String> falseString = new Function0<String>()
        {
            public String value()
            {
                return "false";
            }
        };
        Assert.assertEquals("false", this.map0.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "true", false, "false"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertEquals("false", map.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "false"), map);

        Assert.assertEquals("0", this.map1.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1);
        Assert.assertEquals("true", this.map1.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "true", false, "0"), this.map1);

        Assert.assertEquals("1", this.map2.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2);
        Assert.assertEquals("false", this.map2.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "false"), this.map2);

        Assert.assertEquals("0", this.map3.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);
        Assert.assertEquals("1", this.map3.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);

    }

    @Test
    public void getIfAbsentPutWith()
    {
        Function<String, String> toUpperCase = new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.toUpperCase();
            }
        };

        Assert.assertEquals("TRUE", this.map0.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE"), this.map0);
        Assert.assertEquals("FALSE", this.map0.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "FALSE"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertEquals("FALSE", map.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "FALSE"), map);

        Assert.assertEquals("0", this.map1.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1);
        Assert.assertEquals("TRUE", this.map1.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "0"), this.map1);

        Assert.assertEquals("1", this.map2.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2);

        Assert.assertEquals("FALSE", this.map2.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "FALSE"), this.map2);

        Assert.assertEquals("0", this.map3.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);

        Assert.assertEquals("1", this.map3.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);
    }

    @Test
    public void getIfAbsentPutWithKey()
    {
        BooleanToObjectFunction<String> toUpperCase = new BooleanToObjectFunction<String>()
        {
            public String valueOf(boolean booleanParameter)
            {
                return String.valueOf(booleanParameter).toUpperCase();
            }
        };

        Assert.assertEquals("TRUE", this.map0.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE"), this.map0);
        Assert.assertEquals("FALSE", this.map0.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "FALSE"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertEquals("FALSE", map.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "FALSE"), map);

        Assert.assertEquals("0", this.map1.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1);
        Assert.assertEquals("TRUE", this.map1.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "0"), this.map1);

        Assert.assertEquals("1", this.map2.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2);

        Assert.assertEquals("FALSE", this.map2.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "FALSE"), this.map2);

        Assert.assertEquals("0", this.map3.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);

        Assert.assertEquals("1", this.map3.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);
    }

    @Test
    public void get()
    {
        Assert.assertNull(this.map0.get(true));
        Assert.assertNull(this.map0.get(false));

        Assert.assertNull(this.map1.get(true));
        Assert.assertEquals("0", this.map1.get(false));

        Assert.assertNull(this.map2.get(false));
        Assert.assertEquals("1", this.map2.get(true));

        Assert.assertEquals("0", this.map3.get(false));
        Assert.assertEquals("1", this.map3.get(true));
    }

    @Test
    public void containsKey()
    {
        Assert.assertFalse(this.map0.containsKey(true));
        Assert.assertFalse(this.map0.containsKey(false));

        Assert.assertNull(this.map0.put(true, ""));
        Assert.assertTrue(this.map0.containsKey(true));

        Assert.assertNull(this.map0.put(false, ""));
        Assert.assertTrue(this.map0.containsKey(true));

        Assert.assertFalse(this.map1.containsKey(true));
        Assert.assertTrue(this.map1.containsKey(false));

        Assert.assertFalse(this.map2.containsKey(false));
        Assert.assertTrue(this.map2.containsKey(true));

        Assert.assertTrue(this.map3.containsKey(true));
        Assert.assertTrue(this.map3.containsKey(false));
    }

    @Test
    public void containsValue()
    {
        Assert.assertFalse(this.map3.containsValue(null));
        Assert.assertTrue(this.map3.containsValue("0"));
        Assert.assertTrue(this.map3.containsValue("1"));
        Assert.assertFalse(this.map3.containsValue("2"));

        Assert.assertEquals("1", this.map3.put(true, null));
        Assert.assertTrue(this.map3.containsValue(null));
        Assert.assertNull(this.map3.removeKey(true));

        Assert.assertEquals("0", this.map3.put(false, null));
        Assert.assertTrue(this.map3.containsValue(null));
    }

    @Test
    public void size()
    {
        Assert.assertEquals(0, this.map0.size());
        Assert.assertEquals(1, this.map1.size());
        Assert.assertEquals(1, this.map2.size());
        Assert.assertEquals(2, this.map3.size());
    }

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(this.map0.isEmpty());
        Assert.assertFalse(this.map1.isEmpty());
        Assert.assertFalse(this.map2.isEmpty());
        Assert.assertFalse(this.map3.isEmpty());
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.map0.notEmpty());
        Assert.assertTrue(this.map1.notEmpty());
        Assert.assertTrue(this.map2.notEmpty());
        Assert.assertTrue(this.map3.notEmpty());
    }

    @Test
    public void testEquals()
    {
        Assert.assertNotEquals(this.map0, this.map1);
        Assert.assertNotEquals(this.map0, this.map2);
        Assert.assertNotEquals(this.map0, this.map3);
        Assert.assertNotEquals(this.map1, this.map2);
        Assert.assertNotEquals(this.map1, this.map3);
        Assert.assertNotEquals(this.map2, this.map3);
        Verify.assertEqualsAndHashCode(BooleanObjectHashMap.newWithKeysValues(false, "0", true, "1"), this.map3);
        Assert.assertNotEquals(BooleanObjectHashMap.newWithKeysValues(false, "1", true, "0"), this.map3);
        Assert.assertNotEquals(BooleanObjectHashMap.newWithKeysValues(false, "1"), this.map1);
        Assert.assertNotEquals(BooleanObjectHashMap.newWithKeysValues(true, "0"), this.map2);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(UnifiedMap.newMap().hashCode(), this.map0.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, "0").hashCode(), this.map1.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(true, "1").hashCode(), this.map2.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, "0", true, "1").hashCode(), this.map3.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, null, true, null).hashCode(), BooleanObjectHashMap.newWithKeysValues(false, null, true, null).hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", this.map0.toString());
        Assert.assertEquals("[false=0]", this.map1.toString());
        Assert.assertEquals("[true=1]", this.map2.toString());
        Assert.assertTrue(
                this.map3.toString(),
                "[false=0, true=1]".equals(this.map3.toString())
                        || "[true=1, false=0]".equals(this.map3.toString()));
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("", this.map0.makeString());
        Assert.assertEquals("false=0", this.map1.makeString());
        Assert.assertEquals("true=1", this.map2.makeString());
        Assert.assertTrue(
                this.map3.makeString(),
                "false=0, true=1".equals(this.map3.makeString())
                        || "true=1, false=0".equals(this.map3.makeString()));
        Assert.assertTrue(
                this.map3.makeString("/"),
                "false=0/true=1".equals(this.map3.makeString("/"))
                        || "true=1/false=0".equals(this.map3.makeString("/")));

        Assert.assertTrue(
                this.map3.makeString("{", ". ", "}"),
                "{false=0. true=1}".equals(this.map3.makeString("{", ". ", "}"))
                        || "{true=1. false=0}".equals(this.map3.makeString("{", ". ", "}")));
    }

    @Test
    public void appendString()
    {
        Appendable appendable = new StringBuilder();
        this.map0.appendString(appendable);
        Assert.assertEquals("", appendable.toString());

        Appendable appendable0 = new StringBuilder();
        this.map1.appendString(appendable0);
        Assert.assertEquals("false=0", appendable0.toString());

        Appendable appendable1 = new StringBuilder();
        this.map2.appendString(appendable1);
        Assert.assertEquals("true=1", appendable1.toString());

        Appendable appendable3 = new StringBuilder();
        this.map3.appendString(appendable3);
        Assert.assertTrue(
                appendable3.toString(),
                "false=0, true=1".equals(appendable3.toString())
                        || "true=1, false=0".equals(appendable3.toString()));

        Appendable appendable4 = new StringBuilder();
        this.map3.appendString(appendable4, "[", "/", "]");
        Assert.assertTrue(
                appendable4.toString(),
                "[false=0/true=1]".equals(appendable4.toString())
                        || "[true=1/false=0]".equals(appendable4.toString()));
    }

    @Test
    public void withKeyValue()
    {
        BooleanObjectHashMap<String> hashMap = new BooleanObjectHashMap().withKeyValue(true, "one");
        BooleanObjectHashMap<String> hashMap0 = new BooleanObjectHashMap().withKeysValues(true, "one", false, "two");
        BooleanObjectHashMap<String> hashMap1 = this.map3.withKeyValue(true, "one");
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one"), hashMap);
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one", false, "two"), hashMap0);
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one", false, "0"), hashMap1);
    }
}
