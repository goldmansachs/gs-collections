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

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import ponzu.api.map.ImmutableMap;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.factory.Predicates2;
import ponzu.impl.block.function.Constant;
import ponzu.impl.test.Verify;

/**
 * JUnit test for {@link ImmutableEmptyMap}.
 */
public class ImmutableEmptyMapTest extends ImmutableMemoryEfficientMapTestCase
{
    @Override
    protected ImmutableMap<Integer, String> classUnderTest()
    {
        return new ImmutableEmptyMap<Integer, String>();
    }

    @Override
    protected int size()
    {
        return 0;
    }

    @Override
    @Test
    public void testToString()
    {
        ImmutableMap<Integer, String> map = this.classUnderTest();
        Assert.assertEquals("{}", map.toString());
    }

    @Override
    @Test
    public void get()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertNull(classUnderTest.get(absentKey));
        Assert.assertFalse(classUnderTest.containsValue(absentValue));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Override
    @Test
    public void getIfAbsent()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertEquals(absentValue, classUnderTest.getIfAbsent(absentKey, new Constant<String>(absentValue)));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Override
    @Test
    public void getIfAbsentWith()
    {
        Integer absentKey = this.size() + 1;
        String absentValue = String.valueOf(absentKey);

        // Absent key behavior
        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertEquals(absentValue, classUnderTest.getIfAbsentWith(absentKey, Functions.getToString(), absentValue));

        // Still unchanged
        Assert.assertEquals(this.equalUnifiedMap(), classUnderTest);
    }

    @Override
    @Test
    public void ifPresentApply()
    {
        Integer absentKey = this.size() + 1;

        ImmutableMap<Integer, String> classUnderTest = this.classUnderTest();
        Assert.assertNull(classUnderTest.ifPresentApply(absentKey, Functions.<String>getPassThru()));
    }

    @Override
    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.classUnderTest().notEmpty());
    }

    @Override
    @Test
    public void allSatisfy()
    {
        ImmutableMap<String, String> map = new ImmutableEmptyMap<String, String>();

        Assert.assertTrue(map.allSatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(map.allSatisfy(Predicates.equal("Monkey")));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        ImmutableMap<String, String> map = new ImmutableEmptyMap<String, String>();

        Assert.assertFalse(map.anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertFalse(map.anySatisfy(Predicates.equal("Monkey")));
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        ImmutableMap<String, String> map = new ImmutableEmptyMap<String, String>();

        map.max();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        ImmutableMap<String, String> map = new ImmutableEmptyMap<String, String>();

        map.maxBy(Functions.getStringPassThru());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        ImmutableMap<String, String> map = new ImmutableEmptyMap<String, String>();

        map.min();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        ImmutableMap<String, String> map = new ImmutableEmptyMap<String, String>();

        map.minBy(Functions.getStringPassThru());
    }

    @Override
    public void select()
    {
        ImmutableMap<Integer, String> map = this.classUnderTest();
        ImmutableMap<Integer, String> actual = map.filter(Predicates2.alwaysTrue());
        Verify.assertInstanceOf(ImmutableEmptyMap.class, actual);
    }

    @Override
    public void reject()
    {
        ImmutableMap<Integer, String> map = this.classUnderTest();
        ImmutableMap<Integer, String> actual = map.filterNot(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(ImmutableEmptyMap.class, actual);
    }

    @Override
    public void detect()
    {
        ImmutableMap<Integer, String> map = this.classUnderTest();
        Assert.assertNull(map.find(Predicates2.alwaysTrue()));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new ImmutableEmptyMap<K, V>();
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new ImmutableEmptyMap<K, V>();
    }
}
