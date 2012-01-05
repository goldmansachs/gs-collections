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

import java.io.Serializable;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.FixedSizeMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
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
 * JUnit test for {@link EmptyMap}.
 */
public class EmptyMapTest extends AbstractMemoryEfficientMutableMapTest
{
    @Override
    protected MutableMap<String, String> classUnderTest()
    {
        return new EmptyMap<String, String>();
    }

    @Override
    @Test
    public void containsValue()
    {
        Assert.assertFalse(new EmptyMap<Object, Object>().containsValue("One"));
    }

    @Test
    public void size()
    {
        Verify.assertEmpty(new EmptyMap<Object, Object>());
    }

    @Test
    public void empty()
    {
        Verify.assertEmpty(new EmptyMap<Object, Object>());
        Assert.assertFalse(new EmptyMap<Object, Object>().notEmpty());
        Verify.assertEmpty(new EmptyMap());
        Assert.assertFalse(new EmptyMap().notEmpty());
        Verify.assertEmpty(Maps.fixedSize.of());
        Assert.assertFalse(Maps.fixedSize.of().notEmpty());
    }

    @Test
    public void viewsEmpty()
    {
        Verify.assertEmpty(new EmptyMap<Object, Object>().entrySet());
        Verify.assertEmpty(new EmptyMap<Object, Object>().values());
        Verify.assertEmpty(new EmptyMap<Object, Object>().keySet());
    }

    @Test
    public void testReadResolve()
    {
        Verify.assertInstanceOf(EmptyMap.class, Maps.fixedSize.of());
        Verify.assertPostSerializedIdentity((Serializable) Maps.fixedSize.of());
    }

    @Override
    @Test
    public void testClone()
    {
        MutableMap<String, String> map = this.classUnderTest();
        Assert.assertSame(map, map.clone());
    }

    @Test
    public void iterations()
    {
        StubProcedure<Object> procedure = new StubProcedure<Object>();
        MutableMap<Object, Object> map = new EmptyMap<Object, Object>();

        map.forEach(procedure);
        Assert.assertFalse(procedure.called);

        map.forEachKey(procedure);
        Assert.assertFalse(procedure.called);

        map.forEachValue(procedure);
        Assert.assertFalse(procedure.called);

        map.forEachKeyValue(procedure);
        Assert.assertFalse(procedure.called);

        map.forEachWith(procedure, new Object());
        Assert.assertFalse(procedure.called);

        map.forEachWithIndex(procedure);
        Assert.assertFalse(procedure.called);
    }

    @Override
    @Test
    public void testToString()
    {
        Assert.assertEquals("{}", new EmptyMap<Integer, String>().toString());
    }

    @Override
    @Test
    public void testEqualsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(
                UnifiedMap.<String, String>newMap(),
                this.classUnderTest());
    }

    @Override
    @Test
    public void select()
    {
        MutableMap<String, String> map = this.classUnderTest();
        MutableMap<String, String> actual = map.select(Predicates2.alwaysTrue());
        Verify.assertInstanceOf(EmptyMap.class, actual);
    }

    @Override
    @Test
    public void reject()
    {
        MutableMap<String, String> map = this.classUnderTest();
        MutableMap<String, String> actual = map.reject(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(EmptyMap.class, actual);
    }

    @Override
    @Test
    public void detect()
    {
        MutableMap<String, String> map = this.classUnderTest();
        Assert.assertNull(map.detect(Predicates2.alwaysTrue()));
    }

    @Override
    protected <K, V> FixedSizeMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new EmptyMap<K, V>();
    }

    @Override
    protected <K, V> FixedSizeMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new EmptyMap<K, V>();
    }

    @Override
    @Test
    public void allSatisfy()
    {
        MutableMap<String, String> map = this.classUnderTest();
        Assert.assertTrue(map.allSatisfy(Predicates.alwaysTrue()));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        MutableMap<String, String> map = this.classUnderTest();
        Assert.assertFalse(map.anySatisfy(Predicates.alwaysTrue()));
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        this.classUnderTest().max();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        this.classUnderTest().maxBy(Functions.getStringPassThru());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        this.classUnderTest().min();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        this.classUnderTest().minBy(Functions.getStringPassThru());
    }

    private static class StubProcedure<T>
            implements Procedure<T>, Procedure2<T, T>, ObjectIntProcedure<T>
    {
        private static final long serialVersionUID = 1L;
        private boolean called = false;

        public void value(T each)
        {
            this.called = true;
        }

        public void value(T argument1, T argument2)
        {
            this.called = true;
        }

        public void value(T each, int index)
        {
            this.called = true;
        }
    }

    @Override
    @Test
    public void forEachValue()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        map.forEachValue(CollectionAddProcedure.on(collection));
        Verify.assertEmpty(collection);
    }

    @Override
    @Test
    public void forEach()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        map.forEach(CollectionAddProcedure.on(collection));
        Verify.assertEmpty(collection);
    }

    @Override
    @Test
    public void forEachKey()
    {
        MutableList<Integer> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        map.forEachKey(CollectionAddProcedure.on(collection));
        Verify.assertEmpty(collection);
    }

    @Override
    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MutableMap<Integer, Integer> map = new EmptyMap<Integer, Integer>();
        map.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 10);
        Verify.assertEmpty(result);
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        final MutableList<String> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        map.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                result.add(value);
                result.add(String.valueOf(index));
            }
        });
        Verify.assertEmpty(result);
    }

    @Override
    @Test
    public void forEachKeyValue()
    {
        final MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        map.forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                collection.add(key + value);
            }
        });
        Verify.assertEmpty(collection);
    }

    @Override
    @Test
    public void asLazyKeys()
    {
        Verify.assertIterableEmpty(this.classUnderTest().keysView());
    }

    @Override
    @Test
    public void asLazyValues()
    {
        Verify.assertIterableEmpty(this.classUnderTest().valuesView());
    }

    @Override
    @Test
    public void getIfAbsentPut()
    {
        final MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPut(4, new PassThruFunction0<String>("4"));
            }
        });
    }

    @Override
    @Test
    public void getIfAbsentPutWith()
    {
        final MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPutWith(4, Functions.getToString(), 4);
            }
        });
    }

    @Override
    @Test
    public void getIfAbsent()
    {
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsent(4, new PassThruFunction0<String>("4")));
        Assert.assertNull(map.get(4));
    }

    @Override
    @Test
    public void getIfAbsentWith()
    {
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentWith(4, Functions.getToString(), 4));
        Assert.assertNull(map.get(4));
    }

    @Override
    @Test
    public void ifPresentApply()
    {
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        Assert.assertNull(map.ifPresentApply(4, Functions.<String>getPassThru()));
    }

    @Override
    @Test
    public void notEmpty()
    {
        Assert.assertFalse(new EmptyMap<Integer, String>().notEmpty());
    }

    @Override
    @Test
    public void entrySet()
    {
        Verify.assertEmpty(new EmptyMap<Integer, String>().entrySet());
    }

    @Override
    @Test
    public void values()
    {
        Verify.assertEmpty(new EmptyMap<Integer, String>().values());
    }

    @Override
    @Test
    public void keySet()
    {
        Verify.assertEmpty(new EmptyMap<Integer, String>().keySet());
    }

    @Override
    @Test
    public void nonUniqueWithKeyValue()
    {
        // Not applicable for EmptyMap
    }

    @Override
    public void withKeyValue()
    {
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>().withKeyValue(1, "A");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "A"), map);
        Verify.assertInstanceOf(SingletonMap.class, map);
    }

    @Override
    public void withAllKeyValueArguments()
    {
        MutableMap<Integer, String> map1 = new EmptyMap<Integer, String>().withAllKeyValueArguments(Tuples.pair(1, "A"));
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "A"), map1);
        Verify.assertInstanceOf(SingletonMap.class, map1);

        MutableMap<Integer, String> map2 = new EmptyMap<Integer, String>().withAllKeyValueArguments(Tuples.pair(1, "A"), Tuples.pair(2, "B"));
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "A", 2, "B"), map2);
        Verify.assertInstanceOf(DoubletonMap.class, map2);
    }

    @Override
    public void withoutKey()
    {
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        MutableMap<Integer, String> mapWithout = map.withoutKey(1);
        Assert.assertSame(map, mapWithout);
    }

    @Override
    public void withoutAllKeys()
    {
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        MutableMap<Integer, String> mapWithout = map.withoutAllKeys(FastList.newListWith(1, 2));
        Assert.assertSame(map, mapWithout);
    }

    @Override
    @Test
    public void iterator()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new EmptyMap<Integer, String>();
        for (String eachValue : map)
        {
            collection.add(eachValue);
        }
        Assert.assertEquals(FastList.newListWith(), collection);
    }
}
