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

package com.gs.collections.impl.map.sorted;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableSortedMap}.
 */
public abstract class ImmutableSortedMapTestCase
{
    private static final Comparator<? super Integer> REV_INT_COMPARATOR = Comparators.reverseNaturalOrder();

    /**
     * @return A map containing 1 => "1", 2 => "2", etc.
     */
    protected abstract ImmutableSortedMap<Integer, String> classUnderTest();

    protected abstract ImmutableSortedMap<Integer, String> classUnderTest(Comparator<? super Integer> comparator);

    /**
     * @return Size (and max key) of {@link #classUnderTest()}.
     */
    protected abstract int size();

    @Test
    public void castToSortedMap()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        SortedMap<Integer, String> map = immutable.castToSortedMap();
        Assert.assertSame(immutable, map);
        Assert.assertEquals(immutable, new HashMap<Integer, String>(map));

        ImmutableSortedMap<Integer, String> revImmutable = this.classUnderTest(REV_INT_COMPARATOR);
        SortedMap<Integer, String> revMap = revImmutable.castToSortedMap();
        Assert.assertSame(revImmutable, revMap);
        Assert.assertEquals(revImmutable, new HashMap<Integer, String>(revMap));
    }

    @Test
    public void toSortedMap()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        MutableSortedMap<Integer, String> map = immutable.toSortedMap();
        Assert.assertNotSame(immutable, map);
        Assert.assertEquals(immutable, map);

        ImmutableSortedMap<Integer, String> revImmutable = this.classUnderTest(REV_INT_COMPARATOR);
        MutableSortedMap<Integer, String> revMap = revImmutable.toSortedMap();
        Assert.assertNotSame(revImmutable, revMap);
        Assert.assertEquals(revImmutable, revMap);
    }

    @Test
    public void equalsAndHashCode()
    {
        MutableMap<Integer, String> expected = this.equalUnifiedMap();
        MutableSortedMap<Integer, String> sortedMap = this.equalSortedMap();
        Verify.assertEqualsAndHashCode(expected, this.classUnderTest());
        Verify.assertEqualsAndHashCode(sortedMap, this.classUnderTest());
        Verify.assertEqualsAndHashCode(expected, this.classUnderTest(REV_INT_COMPARATOR));
        Verify.assertEqualsAndHashCode(sortedMap, this.classUnderTest(REV_INT_COMPARATOR));
    }

    @Test
    public void forEachKeyValue()
    {
        final MutableList<Integer> actualKeys = Lists.mutable.of();
        final MutableList<String> actualValues = Lists.mutable.of();

        this.classUnderTest().forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                actualKeys.add(key);
                actualValues.add(value);
            }
        });

        MutableList<Integer> expectedKeys = this.expectedKeys();
        Verify.assertListsEqual(expectedKeys, actualKeys);

        MutableList<String> expectedValues = expectedKeys.collect(Functions.getToString());
        Verify.assertListsEqual(expectedValues, actualValues);

        final MutableList<Integer> revActualKeys = Lists.mutable.of();
        final MutableList<String> revActualValues = Lists.mutable.of();

        this.classUnderTest(REV_INT_COMPARATOR).forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                revActualKeys.add(key);
                revActualValues.add(value);
            }
        });

        MutableList<Integer> reverseKeys = expectedKeys.reverseThis();
        Verify.assertListsEqual(reverseKeys, revActualKeys);

        MutableList<String> reverseValues = expectedValues.reverseThis();
        Verify.assertListsEqual(reverseValues, revActualValues);
    }

    @Test
    public void forEachValue()
    {
        MutableList<String> actualValues = Lists.mutable.of();
        this.classUnderTest().forEachValue(CollectionAddProcedure.on(actualValues));
        Verify.assertListsEqual(this.expectedValues(), actualValues);

        MutableList<String> revActualValues = Lists.mutable.of();
        this.classUnderTest(REV_INT_COMPARATOR).forEachValue(CollectionAddProcedure.on(revActualValues));
        Verify.assertListsEqual(this.expectedValues().reverseThis(), revActualValues);
    }

    @Test
    public void forEach()
    {
        MutableList<String> actualValues = Lists.mutable.of();
        this.classUnderTest().forEach(CollectionAddProcedure.on(actualValues));
        Verify.assertListsEqual(this.expectedValues(), actualValues);

        MutableList<String> revActualValues = Lists.mutable.of();
        this.classUnderTest(REV_INT_COMPARATOR).forEach(CollectionAddProcedure.on(revActualValues));
        Verify.assertListsEqual(this.expectedValues().reverseThis(), revActualValues);
    }

    @Test
    public void iterator()
    {
        MutableList<String> actualValues = Lists.mutable.of();
        for (String eachValue : this.classUnderTest())
        {
            actualValues.add(eachValue);
        }
        Verify.assertListsEqual(this.expectedValues(), actualValues);

        MutableList<String> revActualValues = Lists.mutable.of();
        for (String eachValue : this.classUnderTest(REV_INT_COMPARATOR))
        {
            revActualValues.add(eachValue);
        }
        Verify.assertListsEqual(this.expectedValues().reverseThis(), revActualValues);
    }

    @Test
    public void iteratorThrows()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                Iterator<String> iterator = ImmutableSortedMapTestCase.this.classUnderTest().iterator();
                iterator.remove();
            }
        });
    }

    @Test
    public void forEachKey()
    {
        MutableList<Integer> actualKeys = Lists.mutable.of();
        this.classUnderTest().forEachKey(CollectionAddProcedure.on(actualKeys));
        Verify.assertListsEqual(this.expectedKeys(), actualKeys);

        MutableList<Integer> revActualKeys = Lists.mutable.of();
        this.classUnderTest(REV_INT_COMPARATOR).forEachKey(CollectionAddProcedure.on(revActualKeys));
        Verify.assertListsEqual(this.expectedKeys().reverseThis(), revActualKeys);
    }

    @Test
    public void get()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableSortedMap<Integer, String> classUnderTest = this.classUnderTest();
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
        ImmutableSortedMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertEquals(absentValue, classUnderTest.getIfAbsent(absentKey, new PassThruFunction0<String>(absentValue)));

        // Present key behavior
        Assert.assertEquals("1", classUnderTest.getIfAbsent(1, new PassThruFunction0<String>(absentValue)));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Test
    public void getIfAbsentValue()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableSortedMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertEquals(absentValue, classUnderTest.getIfAbsentValue(absentKey, absentValue));

        // Present key behavior
        Assert.assertEquals("1", classUnderTest.getIfAbsentValue(1, absentValue));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Test
    public void getIfAbsentWith()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableSortedMap<Integer, String> classUnderTest = this.classUnderTest();
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

        ImmutableSortedMap<Integer, String> classUnderTest = this.classUnderTest();
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

        final MutableList<String> actualValues = Lists.mutable.of();
        final MutableList<Object> actualParameters = Lists.mutable.of();

        this.classUnderTest().forEachWith(new Procedure2<String, Object>()
        {
            public void value(String eachValue, Object parameter)
            {
                actualValues.add(eachValue);
                actualParameters.add(parameter);
            }
        }, actualParameter);

        Verify.assertListsEqual(this.expectedKeys().collect(Functions.getToString()), actualValues);
        Verify.assertListsEqual(Collections.nCopies(this.size(), actualParameter), actualParameters);

        final MutableList<String> revActualValues = Lists.mutable.of();
        final MutableList<Object> revActualParameters = Lists.mutable.of();

        this.classUnderTest(REV_INT_COMPARATOR).forEachWith(new Procedure2<String, Object>()
        {
            public void value(String eachValue, Object parameter)
            {
                revActualValues.add(eachValue);
                revActualParameters.add(parameter);
            }
        }, actualParameter);

        Verify.assertListsEqual(this.expectedKeys().collect(Functions.getToString()).reverseThis(), revActualValues);
        Verify.assertListsEqual(Collections.nCopies(this.size(), actualParameter), revActualParameters);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<String> actualValues = Lists.mutable.of();
        final MutableList<Integer> actualIndices = Lists.mutable.of();

        this.classUnderTest().forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String eachValue, int index)
            {
                actualValues.add(eachValue);
                actualIndices.add(index);
            }
        });

        Verify.assertListsEqual(this.expectedKeys().collect(Functions.getToString()), actualValues);
        Verify.assertListsEqual(this.expectedIndices(), actualIndices);

        final MutableList<String> revActualValues = Lists.mutable.of();
        final MutableList<Integer> revActualIndices = Lists.mutable.of();

        this.classUnderTest(REV_INT_COMPARATOR).forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String eachValue, int index)
            {
                revActualValues.add(eachValue);
                revActualIndices.add(index);
            }
        });

        Verify.assertListsEqual(this.expectedKeys().collect(Functions.getToString()).reverseThis(), revActualValues);
        Verify.assertListsEqual(this.expectedIndices(), revActualIndices);
    }

    @Test
    public void valuesView()
    {
        MutableList<String> actualValues = Lists.mutable.of();
        for (String eachValue : this.classUnderTest().valuesView())
        {
            actualValues.add(eachValue);
        }
        MutableList<String> expectedValues = this.expectedValues();
        Verify.assertListsEqual(expectedValues, actualValues);

        MutableList<String> revActualValues = Lists.mutable.of();
        for (String eachValue : this.classUnderTest(REV_INT_COMPARATOR).valuesView())
        {
            revActualValues.add(eachValue);
        }
        Verify.assertListsEqual(this.expectedValues().reverseThis(), revActualValues);
    }

    @Test
    public void keysView()
    {
        MutableList<Integer> actualKeys = Lists.mutable.of();
        for (Integer eachKey : this.classUnderTest().keysView())
        {
            actualKeys.add(eachKey);
        }
        Verify.assertListsEqual(this.expectedKeys(), actualKeys);

        MutableList<Integer> revActualKeys = Lists.mutable.of();
        for (Integer eachKey : this.classUnderTest(REV_INT_COMPARATOR).keysView())
        {
            revActualKeys.add(eachKey);
        }
        Verify.assertListsEqual(this.expectedKeys().reverseThis(), revActualKeys);
    }

    @Test
    public void putAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Map<Integer, String>) ImmutableSortedMapTestCase.this.classUnderTest()).putAll(null);
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
                ((Map<Integer, String>) ImmutableSortedMapTestCase.this.classUnderTest()).clear();
            }
        });
    }

    @Test
    public void entrySet()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        Map<Integer, String> map = new HashMap<Integer, String>(immutable.castToSortedMap());
        Assert.assertEquals(map.entrySet(), immutable.castToSortedMap().entrySet());
    }

    @Test
    public void select()
    {
        ImmutableSortedMap<Integer, String> map = this.classUnderTest();
        ImmutableSortedMap<Integer, String> select = map.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return argument1 < ImmutableSortedMapTestCase.this.size();
            }
        });
        Verify.assertListsEqual(Interval.oneTo(this.size() - 1), select.keysView().toList());
        Verify.assertListsEqual(Interval.oneTo(this.size() - 1).collect(Functions.getToString()).toList(),
                select.valuesView().toList());

        ImmutableSortedMap<Integer, String> revMap = this.classUnderTest(REV_INT_COMPARATOR);
        ImmutableSortedMap<Integer, String> revSelect = revMap.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return argument1 < ImmutableSortedMapTestCase.this.size();
            }
        });
        Verify.assertListsEqual(Interval.oneTo(this.size() - 1).reverseThis(), revSelect.keysView().toList());
        Verify.assertListsEqual(Interval.oneTo(this.size() - 1).collect(Functions.getToString()).toList().reverseThis(),
                revSelect.valuesView().toList());
    }

    @Test
    public void reject()
    {
        ImmutableSortedMap<Integer, String> map = this.classUnderTest();
        ImmutableSortedMap<Integer, String> reject = map.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return argument1 == 1;
            }
        });
        Verify.assertListsEqual(Interval.fromTo(2, this.size()), reject.keysView().toList());
        Verify.assertListsEqual(Interval.fromTo(2, this.size()).collect(Functions.getToString()).toList(),
                reject.valuesView().toList());

        ImmutableSortedMap<Integer, String> revMap = this.classUnderTest(REV_INT_COMPARATOR);
        ImmutableSortedMap<Integer, String> revReject = revMap.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return argument1 == 1;
            }
        });
        Verify.assertListsEqual(Interval.fromTo(2, this.size()).reverseThis(), revReject.keysView().toList());
        Verify.assertListsEqual(Interval.fromTo(2, this.size()).collect(Functions.getToString()).toList().reverseThis(),
                revReject.valuesView().toList());
    }

    @Test
    public void collect()
    {
        ImmutableSortedMap<Integer, String> map = this.classUnderTest();
        ImmutableMap<String, Integer> collect = map.collect(new Function2<Integer, String, Pair<String, Integer>>()
        {
            public Pair<String, Integer> value(Integer argument1, String argument2)
            {
                return Tuples.pair(argument2, argument1);
            }
        });
        Verify.assertSetsEqual(Interval.oneTo(this.size()).collect(Functions.getToString()).toSet(), collect.keysView().toSet());
        Verify.assertSetsEqual(Interval.oneTo(this.size()).toSet(), collect.valuesView().toSet());

        ImmutableSortedMap<Integer, String> revMap = this.classUnderTest(REV_INT_COMPARATOR);
        ImmutableMap<String, Integer> revCollect = revMap.collect(new Function2<Integer, String, Pair<String, Integer>>()
        {
            public Pair<String, Integer> value(Integer argument1, String argument2)
            {
                return Tuples.pair(argument2, argument1);
            }
        });
        Verify.assertSetsEqual(Interval.oneTo(this.size()).collect(Functions.getToString()).toSet(), revCollect.keysView().toSet());
        Verify.assertSetsEqual(Interval.oneTo(this.size()).toSet(), revCollect.valuesView().toSet());
    }

    @Test
    public void collectValues()
    {
        ImmutableSortedMap<Integer, String> map = this.classUnderTest();
        ImmutableSortedMap<Integer, Integer> result = map.collectValues(new Function2<Integer, String, Integer>()
        {
            public Integer value(Integer argument1, String argument2)
            {
                return argument1;
            }
        });
        Verify.assertListsEqual(result.keysView().toList(), result.valuesView().toList());

        ImmutableSortedMap<Integer, String> revMap = this.classUnderTest(REV_INT_COMPARATOR);
        ImmutableSortedMap<Integer, Integer> revResult = revMap.collectValues(new Function2<Integer, String, Integer>()
        {
            public Integer value(Integer argument1, String argument2)
            {
                return argument1;
            }
        });

        Verify.assertListsEqual(revResult.keysView().toList(), revResult.valuesView().toList());
    }

    @Test
    public void newWithKeyValue()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        ImmutableSortedMap<Integer, String> immutable2 = immutable.newWithKeyValue(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE));
        Verify.assertSize(immutable.size() + 1, immutable2.castToSortedMap());
    }

    @Test
    public void newWithAllKeyValuePairs()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        ImmutableSortedMap<Integer, String> immutable2 = immutable.newWithAllKeyValueArguments(
                Tuples.pair(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE)),
                Tuples.pair(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE)));
        Verify.assertSize(immutable.size() + 2, immutable2.castToSortedMap());
    }

    @Test
    public void newWithAllKeyValues()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        ImmutableSortedMap<Integer, String> immutable2 = immutable.newWithAllKeyValues(ArrayAdapter.newArrayWith(
                Tuples.pair(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE)),
                Tuples.pair(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE))));
        Verify.assertSize(immutable.size() + 2, immutable2.castToSortedMap());
    }

    @Test
    public void newWithoutKey()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        ImmutableSortedMap<Integer, String> immutable3 = immutable.newWithoutKey(Integer.MAX_VALUE);
        Verify.assertSize(immutable.size(), immutable3.castToSortedMap());
    }

    @Test
    public void newWithoutKeys()
    {
        ImmutableSortedMap<Integer, String> immutable = this.classUnderTest();
        ImmutableSortedMap<Integer, String> immutable2 = immutable.newWithoutAllKeys(immutable.keysView());
        ImmutableSortedMap<Integer, String> immutable3 = immutable.newWithoutAllKeys(Lists.immutable.<Integer>of());
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
                ((Map<Integer, String>) ImmutableSortedMapTestCase.this.classUnderTest()).put(null, null);
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
                ((Map<Integer, String>) ImmutableSortedMapTestCase.this.classUnderTest()).remove(null);
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

    protected MutableSortedMap<Integer, String> equalSortedMap()
    {
        MutableSortedMap<Integer, String> expected = TreeSortedMap.newMap();
        for (int i = 1; i <= this.size(); i++)
        {
            expected.put(i, String.valueOf(i));
        }
        return expected;
    }

    private MutableList<String> expectedValues()
    {
        return this.expectedKeys().collect(Functions.getToString());
    }

    private MutableList<Integer> expectedKeys()
    {
        if (this.size() == 0)
        {
            return Lists.mutable.of();
        }
        return Interval.oneTo(this.size()).toList();
    }

    private MutableList<Integer> expectedIndices()
    {
        if (this.size() == 0)
        {
            return Lists.mutable.of();
        }
        return Interval.zeroTo(this.size() - 1).toList();
    }
}
