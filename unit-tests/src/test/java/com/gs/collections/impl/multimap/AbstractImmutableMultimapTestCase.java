/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.multimap;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractImmutableMultimapTestCase
{
    protected abstract ImmutableMultimap<String, String> classUnderTest();

    protected abstract MutableCollection<String> mutableCollection();

    @Test
    public void size()
    {
        Verify.assertEmpty(this.classUnderTest());
        ImmutableMultimap<String, String> one = this.classUnderTest().newWith("1", "1");
        Verify.assertSize(1, one);
        ImmutableMultimap<String, String> two = one.newWith("2", "2");
        Verify.assertSize(2, two);
    }

    @Test
    public void allowDuplicates()
    {
        Verify.assertEmpty(this.classUnderTest());
        ImmutableMultimap<String, String> one = this.classUnderTest().newWith("1", "1");
        Verify.assertSize(1, one);
        ImmutableMultimap<String, String> two = one.newWith("1", "1");
        Verify.assertSize(2, two);
    }

    @Test
    public void noDuplicates()
    {
        Verify.assertEmpty(this.classUnderTest());
        ImmutableMultimap<String, String> one = this.classUnderTest().newWith("1", "1");
        Verify.assertSize(1, one);
        ImmutableMultimap<String, String> two = one.newWith("1", "1");
        Verify.assertSize(1, two);
    }

    @Test
    public void isEmpty()
    {
        ImmutableMultimap<String, String> empty = this.classUnderTest();
        Verify.assertEmpty(empty);
        Assert.assertTrue(empty.isEmpty());
        Assert.assertFalse(empty.notEmpty());

        ImmutableMultimap<String, String> notEmpty = empty.newWith("1", "1");
        Verify.assertNotEmpty(notEmpty);
        Assert.assertTrue(notEmpty.notEmpty());
        Assert.assertFalse(notEmpty.isEmpty());
    }

    @Test
    public void get()
    {
        ImmutableMultimap<String, String> empty = this.classUnderTest();
        RichIterable<String> emptyView = empty.get("");
        Verify.assertIterableEmpty(emptyView);

        ImmutableMultimap<String, String> notEmpty = empty.newWith("1", "1");
        RichIterable<String> notEmptyView = notEmpty.get("1");
        Verify.assertIterableNotEmpty(notEmptyView);
        Assert.assertEquals(FastList.newListWith("1"), notEmptyView.toList());
    }

    @Test
    public void toMap()
    {
        ImmutableMultimap<String, String> empty = this.classUnderTest();
        Assert.assertEquals(UnifiedMap.<String, RichIterable<String>>newMap(), empty.toMap());

        ImmutableMultimap<String, String> notEmpty = empty.newWith("1", "1");
        MutableCollection<String> strings = this.mutableCollection();
        strings.add("1");
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues("1", (RichIterable<String>) strings),
                notEmpty.toMap());
    }

    @Test
    public void newWithout()
    {
        ImmutableMultimap<String, String> empty = this.classUnderTest();
        Verify.assertEmpty(empty.newWithout("1", "1"));

        ImmutableMultimap<String, String> notEmpty = empty.newWith("1", "1");
        Verify.assertNotEmpty(notEmpty);
        Verify.assertEmpty(notEmpty.newWithout("1", "1"));
    }

    @Test
    public void newWithAll_newWithoutAll()
    {
        ImmutableMultimap<String, String> empty = this.classUnderTest();
        Verify.assertEmpty(empty.newWithoutAll("1"));

        ImmutableMultimap<String, String> notEmpty = empty.newWith("1", "1").newWith("1", "2");
        Verify.assertNotEmpty(notEmpty);

        Assert.assertEquals(empty.newWithAll("1", FastList.newListWith("1", "2")), notEmpty);
        Verify.assertEmpty(notEmpty.newWithoutAll("1"));
    }

    @Test
    public void toImmutable()
    {
        ImmutableMultimap<String, String> empty = this.classUnderTest();
        Assert.assertSame(empty, empty.toImmutable());
    }

    @Test
    public void testSerialization()
    {
        Multimap<String, String> original = this.classUnderTest()
                .newWith("A", "A")
                .newWith("A", "B")
                .newWith("A", "B")
                .newWith("B", "A");
        Multimap<String, String> copy = SerializeTestHelper.serializeDeserialize(original);
        Verify.assertEqualsAndHashCode(original, copy);
    }

    @Test
    public void selectKeysValues()
    {
        Multimap<String, String> multimap = this.classUnderTest().newWith("One", "1").newWith("Two", "2");
        Multimap<String, String> selectedMultimap = multimap.selectKeysValues((key, value) -> ("Two".equals(key) && "2".equals(value)));
        Assert.assertEquals(this.classUnderTest().newWith("Two", "2"), selectedMultimap);
    }

    @Test
    public void rejectKeysValues()
    {
        Multimap<String, String> multimap = this.classUnderTest().newWith("One", "1").newWith("Two", "2");
        Multimap<String, String> rejectedMultimap = multimap.rejectKeysValues((key, value) -> ("Two".equals(key) && "2".equals(value)));
        Assert.assertEquals(this.classUnderTest().newWith("One", "1"), rejectedMultimap);
    }

    @Test
    public void selectKeysMultiValues()
    {
        Multimap<String, String> multimap1 = this.classUnderTest().newWith("One", "1").newWith("Two", "2").newWith("Two", "3");
        Multimap<String, String> selectedMultimap1 = multimap1.selectKeysMultiValues((key, values) -> "Two".equals(key) && Iterate.contains(values, "2"));
        Assert.assertEquals(this.classUnderTest().newWith("Two", "2").newWith("Two", "3"), selectedMultimap1);

        Multimap<String, String> multimap2 = this.classUnderTest().newWith("One", "1").newWith("Two", "3");
        Multimap<String, String> selectedMultimap2 = multimap2.selectKeysMultiValues((key, values) -> "Two".equals(key) && Iterate.contains(values, "2"));
        Assert.assertEquals(this.classUnderTest(), selectedMultimap2);
    }

    @Test
    public void rejectKeysMultiValues()
    {
        Multimap<String, String> multimap1 = this.classUnderTest().newWith("One", "1").newWith("Two", "2").newWith("Two", "3");
        Multimap<String, String> rejectedMultimap1 = multimap1.rejectKeysMultiValues((key, values) -> "Two".equals(key) && Iterate.contains(values, "2"));
        Assert.assertEquals(this.classUnderTest().newWith("One", "1"), rejectedMultimap1);

        Multimap<String, String> multimap2 = this.classUnderTest().newWith("One", "1").newWith("Two", "3");
        Multimap<String, String> rejectedMultimap2 = multimap2.rejectKeysMultiValues((key, values) -> "Two".equals(key) && Iterate.contains(values, "2"));
        Assert.assertEquals(this.classUnderTest().newWith("One", "1"), rejectedMultimap2);
    }

    @Test
    public void collectKeysValues()
    {
        Multimap<String, String> multimap = this.classUnderTest().newWith("One", "1").newWith("Two", "2");
        Multimap<String, String> collectedMultimap = multimap.collectKeysValues((argument1, argument2) -> Tuples.pair(argument1 + "Key", argument2 + "Value"));
        Assert.assertEquals(this.classUnderTest().newWith("OneKey", "1Value").newWith("TwoKey", "2Value"), collectedMultimap);
    }

    @Test
    public void collectValues()
    {
        Multimap<String, String> multimap = this.classUnderTest().newWith("One", "1").newWith("Two", "2");
        Multimap<String, String> collectedMultimap = multimap.collectValues(value -> value + "Value");
        Assert.assertEquals(this.classUnderTest().newWith("One", "1Value").newWith("Two", "2Value"), collectedMultimap);
    }
}
