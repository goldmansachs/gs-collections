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

package com.gs.collections.impl.map.fixed;

import java.util.Map;

import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.FixedSizeMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SingletonMap}.
 */
public class SingletonMapTest extends AbstractMemoryEfficientMutableMapTest
{
    @Override
    protected MutableMap<String, String> classUnderTest()
    {
        return new SingletonMap<String, String>("1", "One");
    }

    @Override
    @Test
    public void containsValue()
    {
        Assert.assertTrue(classUnderTest().containsValue("One"));
    }

    @Override
    @Test
    public void forEachKeyValue()
    {
        final MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "One");
        map.forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                collection.add(key + value);
            }
        });
        Assert.assertEquals(FastList.newListWith("1One"), collection);
    }

    @Override
    @Test
    public void nonUniqueWithKeyValue()
    {
        Twin<String> twin1 = Tuples.twin("1", "1");
        Twin<String> twin2 = Tuples.twin("1", "1");
        SingletonMap<Twin<String>, Twin<String>> map = new SingletonMap<Twin<String>, Twin<String>>(twin1, twin1);
        map.withKeyValue(twin2, twin2);
        Assert.assertSame(map.getKey1(), twin1);
        Assert.assertSame(map.get(twin1), twin2);
    }

    @Override
    public void withKeyValue()
    {
        MutableMap<Integer, String> map1 = new SingletonMap<Integer, String>(1, "A").withKeyValue(2, "B");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "A", 2, "B"), map1);
        Verify.assertInstanceOf(DoubletonMap.class, map1);

        MutableMap<Integer, String> map2 = new SingletonMap<Integer, String>(1, "A");
        MutableMap<Integer, String> map2with = map2.withKeyValue(1, "AA");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "AA"), map2with);
        Assert.assertSame(map2, map2with);
    }

    @Override
    public void withAllKeyValueArguments()
    {
        MutableMap<Integer, String> map1 = new SingletonMap<Integer, String>(1, "A").withAllKeyValueArguments(
                Tuples.pair(1, "AA"), Tuples.pair(2, "B"));
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "AA", 2, "B"), map1);
        Verify.assertInstanceOf(DoubletonMap.class, map1);

        MutableMap<Integer, String> map2 = new SingletonMap<Integer, String>(1, "A");
        MutableMap<Integer, String> map2with = map2.withAllKeyValueArguments(Tuples.pair(1, "AA"));
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "AA"), map2with);
        Assert.assertSame(map2, map2with);
    }

    @Override
    public void withoutKey()
    {
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "A");
        MutableMap<Integer, String> mapWithout = map.withoutKey(2);
        Assert.assertSame(map, mapWithout);
        mapWithout = map.withoutKey(1);
        Verify.assertMapsEqual(UnifiedMap.newMap(), mapWithout);
        Verify.assertInstanceOf(EmptyMap.class, mapWithout);
    }

    @Override
    public void withoutAllKeys()
    {
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "A");
        MutableMap<Integer, String> mapWithout = map.withoutAllKeys(FastList.newListWith(2, 3));
        Assert.assertSame(map, mapWithout);
        mapWithout = map.withoutAllKeys(FastList.newListWith(1, 2));
        Verify.assertMapsEqual(UnifiedMap.newMap(), mapWithout);
        Verify.assertInstanceOf(EmptyMap.class, mapWithout);
    }

    @Override
    @Test
    public void forEachValue()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        map.forEachValue(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith("1"), collection);
    }

    @Override
    @Test
    public void forEach()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        map.forEach(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith("1"), collection);
    }

    @Override
    @Test
    public void iterator()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        for (String eachValue : map)
        {
            collection.add(eachValue);
        }
        Assert.assertEquals(FastList.newListWith("1"), collection);
    }

    @Override
    @Test
    public void forEachKey()
    {
        MutableList<Integer> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        map.forEachKey(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith(1), collection);
    }

    @Override
    @Test
    public void getIfAbsentPut()
    {
        final MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPut(4, new PassThruFunction0<String>("4"));
            }
        });
        Assert.assertEquals("1", map.getIfAbsentPut(1, new PassThruFunction0<String>("1")));
    }

    @Override
    @Test
    public void getIfAbsentPutWith()
    {
        final MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPutWith(4, Functions.getToString(), 4);
            }
        });
        Assert.assertEquals("1", map.getIfAbsentPutWith(1, Functions.getToString(), 1));
    }

    @Override
    @Test
    public void getIfAbsent()
    {
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsent(4, new PassThruFunction0<String>("4")));
        Assert.assertEquals("1", map.getIfAbsent(1, new PassThruFunction0<String>("1")));
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "1"), map);
    }

    @Override
    @Test
    public void getIfAbsentWith()
    {
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentWith(4, Functions.getToString(), 4));
        Assert.assertEquals("1", map.getIfAbsentWith(1, Functions.getToString(), 1));
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "1"), map);
    }

    @Override
    @Test
    public void ifPresentApply()
    {
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "1");
        Assert.assertNull(map.ifPresentApply(4, Functions.<String>getPassThru()));
        Assert.assertEquals("1", map.ifPresentApply(1, Functions.<String>getPassThru()));
    }

    @Override
    @Test
    public void notEmpty()
    {
        Assert.assertTrue(new SingletonMap<Integer, String>(1, "1").notEmpty());
    }

    @Override
    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MutableMap<Integer, Integer> map = new SingletonMap<Integer, Integer>(1, 1);
        map.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 10);
        Assert.assertEquals(FastList.newListWith(11), result);
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        final MutableList<String> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "One");
        map.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                result.add(value);
                result.add(String.valueOf(index));
            }
        });
        Assert.assertEquals(FastList.newListWith("One", "0"), result);
    }

    @Override
    @Test
    public void entrySet()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "One");
        for (Map.Entry<Integer, String> entry : map.entrySet())
        {
            result.add(entry.getValue());
        }
        Assert.assertEquals(FastList.newListWith("One"), result);
    }

    @Override
    @Test
    public void values()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "One");
        for (String value : map.values())
        {
            result.add(value);
        }
        Assert.assertEquals(FastList.newListWith("One"), result);
    }

    @Override
    @Test
    public void keySet()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new SingletonMap<Integer, String>(1, "One");
        for (Integer key : map.keySet())
        {
            result.add(key);
        }
        Assert.assertEquals(FastList.newListWith(1), result);
    }

    @Override
    @Test
    public void testToString()
    {
        Assert.assertEquals("{1=One}", new SingletonMap<Integer, String>(1, "One").toString());
        Assert.assertEquals("{1=null}", new SingletonMap<Integer, String>(1, null).toString());
        Assert.assertEquals("{null=One}", new SingletonMap<Integer, String>(null, "One").toString());
    }

    @Override
    @Test
    public void testEqualsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(
                UnifiedMap.newWithKeysValues("1", "One"),
                this.classUnderTest());
    }

    @Override
    @Test
    public void asLazyKeys()
    {
        MutableList<Integer> keys = Maps.fixedSize.of(1, 1).keysView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1), keys);
    }

    @Override
    @Test
    public void asLazyValues()
    {
        MutableList<Integer> values = Maps.fixedSize.of(1, 1).valuesView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1), values);
    }

    @Override
    @Test
    public void select()
    {
        MutableMap<String, String> map = this.classUnderTest();

        MutableMap<String, String> empty = map.filter(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(EmptyMap.class, empty);

        MutableMap<String, String> full = map.filter(Predicates2.alwaysTrue());
        Verify.assertInstanceOf(SingletonMap.class, full);
        Assert.assertEquals(map, full);
    }

    @Override
    @Test
    public void reject()
    {
        MutableMap<String, String> map = this.classUnderTest();

        MutableMap<String, String> empty = map.filterNot(Predicates2.alwaysTrue());
        Verify.assertInstanceOf(EmptyMap.class, empty);

        MutableMap<String, String> full = map.filterNot(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(SingletonMap.class, full);
        Assert.assertEquals(map, full);
    }

    @Override
    @Test
    public void detect()
    {
        MutableMap<String, String> map = this.classUnderTest();

        Pair<String, String> actual = map.find(Predicates2.alwaysTrue());
        Assert.assertEquals(Tuples.pair("1", "One"), actual);

        Assert.assertNull(map.find(Predicates2.alwaysFalse()));
    }

    @Override
    protected <K, V> FixedSizeMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new SingletonMap<K, V>(key1, value1);
    }

    @Override
    protected <K, V> FixedSizeMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new SingletonMap<K, V>(key1, value1);
    }
}
