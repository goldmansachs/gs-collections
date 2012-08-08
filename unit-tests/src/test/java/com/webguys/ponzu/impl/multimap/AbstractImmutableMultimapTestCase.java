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

package com.webguys.ponzu.impl.multimap;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.collection.MutableCollection;
import com.webguys.ponzu.api.multimap.ImmutableMultimap;
import com.webguys.ponzu.api.multimap.Multimap;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.test.SerializeTestHelper;
import com.webguys.ponzu.impl.test.Verify;
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
}
