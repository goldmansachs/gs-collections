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

package com.gs.collections.impl.map.immutable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableMap}.
 */
public abstract class ImmutableMapTestCase
{
    /**
     * @return A map containing 1 => "1", 2 => "2", etc.
     */
    protected abstract ImmutableMap<Integer, String> classUnderTest();

    /**
     * @return Size (and max key) of {@link #classUnderTest()}.
     */
    protected abstract int size();

    @Test
    public void castToMap()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        Map<Integer, String> map = immutable.castToMap();
        Assert.assertSame(immutable, map);
        Assert.assertEquals(immutable, new HashMap<Integer, String>(map));
    }

    @Test
    public void toMap()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        MutableMap<Integer, String> map = immutable.toMap();
        Assert.assertNotSame(immutable, map);
        Assert.assertEquals(immutable, map);
    }

    @Test
    public void equalsAndHashCode()
    {
        MutableMap<Integer, String> expected = this.equalUnifiedMap();
        Verify.assertEqualsAndHashCode(expected, this.classUnderTest());
    }

    @Test
    public void forEachKeyValue()
    {
        final MutableSet<Integer> actualKeys = UnifiedSet.newSet();
        final MutableSet<String> actualValues = UnifiedSet.newSet();

        this.classUnderTest().forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                actualKeys.add(key);
                actualValues.add(value);
            }
        });

        MutableSet<Integer> expectedKeys = this.expectedKeys();
        Assert.assertEquals(expectedKeys, actualKeys);

        MutableSet<String> expectedValues = expectedKeys.transform(Functions.getToString());
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void forEachValue()
    {
        MutableSet<String> actualValues = UnifiedSet.newSet();
        this.classUnderTest().forEachValue(CollectionAddProcedure.on(actualValues));
        Assert.assertEquals(this.expectedValues(), actualValues);
    }

    @Test
    public void forEach()
    {
        MutableSet<String> actualValues = UnifiedSet.newSet();
        this.classUnderTest().forEach(CollectionAddProcedure.on(actualValues));
        Assert.assertEquals(this.expectedValues(), actualValues);
    }

    @Test
    public void iterator()
    {
        MutableSet<String> actualValues = UnifiedSet.newSet();
        for (String eachValue : this.classUnderTest())
        {
            actualValues.add(eachValue);
        }
        Assert.assertEquals(this.expectedValues(), actualValues);
    }

    @Test
    public void iteratorThrows()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                Iterator<String> iterator = ImmutableMapTestCase.this.classUnderTest().iterator();
                iterator.remove();
            }
        });
    }

    @Test
    public void forEachKey()
    {
        MutableSet<Integer> actualKeys = UnifiedSet.newSet();
        this.classUnderTest().forEachKey(CollectionAddProcedure.on(actualKeys));
        Assert.assertEquals(this.expectedKeys(), actualKeys);
    }

    @Test
    public void get()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertNull(classUnderTest.get(absentKey));
        Assert.assertFalse(classUnderTest.containsValue(absentValue));

        // Present key behavior
        Assert.assertEquals("1", classUnderTest.get(1));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Test
    public void getIfAbsent()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertEquals(absentValue, classUnderTest.getIfAbsent(absentKey, new PassThruFunction0<String>(absentValue)));

        // Present key behavior
        Assert.assertEquals("1", classUnderTest.getIfAbsent(1, new PassThruFunction0<String>(absentValue)));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Test
    public void getIfAbsentWith()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertEquals(absentValue, classUnderTest.getIfAbsentWith(absentKey, Functions.getToString(), absentValue));

        // Present key behavior
        Assert.assertEquals("1", classUnderTest.getIfAbsentWith(1, Functions.getToString(), absentValue));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Test
    public void ifPresentApply()
    {
        Integer absentKey = this.size() + 1;

        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertNull(classUnderTest.ifPresentApply(absentKey, Functions.<String>getPassThru()));
        Assert.assertEquals("1", classUnderTest.ifPresentApply(1, Functions.<String>getPassThru()));
    }

    @Test
    public void notEmpty()
    {
        Assert.assertTrue(this.classUnderTest().notEmpty());
    }

    @Test
    public void forEachWith()
    {
        Object actualParameter = new Object();

        final MutableSet<String> actualValues = UnifiedSet.newSet();
        final MutableList<Object> actualParameters = Lists.mutable.of();

        this.classUnderTest().forEachWith(new Procedure2<String, Object>()
        {
            public void value(String eachValue, Object parameter)
            {
                actualValues.add(eachValue);
                actualParameters.add(parameter);
            }
        }, actualParameter);

        Assert.assertEquals(this.expectedKeys().transform(Functions.getToString()), actualValues);
        Assert.assertEquals(Collections.nCopies(this.size(), actualParameter), actualParameters);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableSet<String> actualValues = UnifiedSet.newSet();
        final MutableList<Integer> actualIndices = Lists.mutable.of();

        this.classUnderTest().forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String eachValue, int index)
            {
                actualValues.add(eachValue);
                actualIndices.add(index);
            }
        });

        Assert.assertEquals(this.expectedKeys().transform(Functions.getToString()), actualValues);
        Assert.assertEquals(this.expectedIndices(), actualIndices);
    }

    @Test
    public void keyValuesView()
    {
        MutableSet<Integer> actualKeys = UnifiedSet.newSet();
        MutableSet<String> actualValues = UnifiedSet.newSet();

        for (Pair<Integer, String> entry : this.classUnderTest().keyValuesView())
        {
            actualKeys.add(entry.getOne());
            actualValues.add(entry.getTwo());
        }

        MutableSet<Integer> expectedKeys = this.expectedKeys();
        Assert.assertEquals(expectedKeys, actualKeys);

        MutableSet<String> expectedValues = expectedKeys.transform(Functions.getToString());
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void valuesView()
    {
        MutableSet<String> actualValues = UnifiedSet.newSet();
        for (String eachValue : this.classUnderTest().valuesView())
        {
            actualValues.add(eachValue);
        }
        MutableSet<String> expectedValues = this.expectedValues();
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void keysView()
    {
        MutableSet<Integer> actualKeys = UnifiedSet.newSet();
        for (Integer eachKey : this.classUnderTest().keysView())
        {
            actualKeys.add(eachKey);
        }
        Assert.assertEquals(this.expectedKeys(), actualKeys);
    }

    @Test
    public void putAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Map<Integer, String>) ImmutableMapTestCase.this.classUnderTest()).putAll(null);
            }
        });
    }

    @Test
    public void clear()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Map<Integer, String>) ImmutableMapTestCase.this.classUnderTest()).clear();
            }
        });
    }

    @Test
    public void entrySet()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        Map<Integer, String> map = new HashMap<Integer, String>(immutable.castToMap());
        Assert.assertEquals(immutable.size(), immutable.castToMap().entrySet().size());
        Assert.assertEquals(map.entrySet(), immutable.castToMap().entrySet());
    }

    @Test
    public void newWithKeyValue()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        ImmutableMap<Integer, String> immutable2 = immutable.newWithKeyValue(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE));
        Verify.assertSize(immutable.size() + 1, immutable2);
    }

    @Test
    public void newWithAllKeyValuePairs()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        ImmutableMap<Integer, String> immutable2 = immutable.newWithAllKeyValueArguments(
                Tuples.pair(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE)),
                Tuples.pair(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE)));
        Verify.assertSize(immutable.size() + 2, immutable2);
    }

    @Test
    public void newWithAllKeyValues()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        ImmutableMap<Integer, String> immutable2 = immutable.newWithAllKeyValues(ArrayAdapter.newArrayWith(
                Tuples.pair(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE)),
                Tuples.pair(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE))));
        Verify.assertSize(immutable.size() + 2, immutable2);
    }

    @Test
    public void newWithoutKey()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        ImmutableMap<Integer, String> immutable3 = immutable.newWithoutKey(Integer.MAX_VALUE);
        Verify.assertSize(immutable.size(), immutable3);
    }

    @Test
    public void newWithoutKeys()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        ImmutableMap<Integer, String> immutable2 = immutable.newWithoutAllKeys(immutable.keysView());
        ImmutableMap<Integer, String> immutable3 = immutable.newWithoutAllKeys(Lists.immutable.<Integer>of());
        Assert.assertEquals(immutable, immutable3);
        Assert.assertEquals(Maps.immutable.of(), immutable2);
    }

    @Test
    public void put()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Map<Integer, String>) ImmutableMapTestCase.this.classUnderTest()).put(null, null);
            }
        });
    }

    @Test
    public void remove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Map<Integer, String>) ImmutableMapTestCase.this.classUnderTest()).remove(null);
            }
        });
    }

    @Test
    public abstract void testToString();

    protected MutableMap<Integer, String> equalUnifiedMap()
    {
        MutableMap<Integer, String> expected = UnifiedMap.newMap();
        for (int i = 1; i <= this.size(); i++)
        {
            expected.put(i, String.valueOf(i));
        }
        return expected;
    }

    private MutableSet<String> expectedValues()
    {
        return this.expectedKeys().transform(Functions.getToString());
    }

    private MutableSet<Integer> expectedKeys()
    {
        if (this.size() == 0)
        {
            return UnifiedSet.newSet();
        }
        return Interval.oneTo(this.size()).toSet();
    }

    private List<Integer> expectedIndices()
    {
        if (this.size() == 0)
        {
            return Lists.mutable.of();
        }
        return Interval.zeroTo(this.size() - 1);
    }
}
