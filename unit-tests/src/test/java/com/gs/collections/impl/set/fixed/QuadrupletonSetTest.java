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

package com.gs.collections.impl.set.fixed;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuadrupletonSetTest extends AbstractMemoryEfficientMutableSetTestCase
{
    private QuadrupletonSet<String> set;

    @Before
    public void setUp()
    {
        this.set = new QuadrupletonSet<>("1", "2", "3", "4");
    }

    @Override
    protected MutableSet<String> classUnderTest()
    {
        return new QuadrupletonSet<>("1", "2", "3", "4");
    }

    @Override
    protected MutableSet<String> classUnderTestWithNull()
    {
        return new QuadrupletonSet<>(null, "2", "3", "4");
    }

    @Test
    public void nonUniqueWith()
    {
        Twin<String> twin1 = Tuples.twin("1", "1");
        Twin<String> twin2 = Tuples.twin("2", "2");
        Twin<String> twin3 = Tuples.twin("3", "3");
        Twin<String> twin4 = Tuples.twin("4", "4");
        QuadrupletonSet<Twin<String>> set = new QuadrupletonSet<>(twin1, twin2, twin3, twin4);

        set.with(Tuples.twin("1", "1"));
        set.with(Tuples.twin("2", "2"));
        set.with(Tuples.twin("3", "3"));
        set.with(Tuples.twin("4", "4"));

        Assert.assertSame(set.getFirst(), twin1);
        Assert.assertSame(set.getSecond(), twin2);
        Assert.assertSame(set.getThird(), twin3);
        Assert.assertSame(set.getLast(), twin4);
    }

    @Test
    public void contains()
    {
        Verify.assertContains("1", this.set);
        Verify.assertContainsAll(this.set, "2", "3", "4");
        Verify.assertNotContains("5", this.set);
    }

    @Test
    public void remove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> this.set.remove("1"));
    }

    @Test
    public void addDuplicate()
    {
        try
        {
            this.set.add("1");
            Assert.fail("Cannot add to TripletonSet");
        }
        catch (UnsupportedOperationException ignored)
        {
            this.assertUnchanged();
        }
    }

    @Test
    public void add()
    {
        try
        {
            this.set.add("4");
            Assert.fail("Cannot add to TripletonSet");
        }
        catch (UnsupportedOperationException ignored)
        {
            this.assertUnchanged();
        }
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        MutableSet<String> quadrupletonSet = Sets.fixedSize.of("1", "2", "3", "4");
        MutableSet<String> set = UnifiedSet.newSetWith("1", "2", "3", "4");
        Verify.assertEqualsAndHashCode(quadrupletonSet, set);
        Verify.assertPostSerializedEqualsAndHashCode(quadrupletonSet);
    }

    @Test
    public void addingAllToOtherSet()
    {
        MutableSet<String> newSet = UnifiedSet.newSet(Sets.fixedSize.of("1", "2", "3", "4"));
        newSet.add("5");
        Verify.assertContainsAll(newSet, "1", "2", "3", "4", "5");
    }

    private void assertUnchanged()
    {
        Verify.assertSize(4, this.set);
        Verify.assertContainsAll(this.set, "1", "2", "3", "4");
        Verify.assertNotContains("5", this.set);
    }

    @Test
    public void serializable()
    {
        MutableSet<String> copyOfSet = SerializeTestHelper.serializeDeserialize(this.set);
        Verify.assertSetsEqual(this.set, copyOfSet);
        Assert.assertNotSame(this.set, copyOfSet);
    }

    @Override
    @Test
    public void testClone()
    {
        Verify.assertShallowClone(this.set);
        MutableSet<String> cloneSet = this.set.clone();
        Assert.assertNotSame(cloneSet, this.set);
        Verify.assertEqualsAndHashCode(UnifiedSet.newSetWith("1", "2", "3", "4"), cloneSet);
    }

    @Test
    public void newEmpty()
    {
        MutableSet<String> newEmpty = this.set.newEmpty();
        Verify.assertInstanceOf(UnifiedSet.class, newEmpty);
        Verify.assertEmpty(newEmpty);
    }

    @Test
    public void getFirstGetLast()
    {
        MutableSet<String> list5 = Sets.fixedSize.of("1", "2", "3", "4");
        Assert.assertEquals("1", list5.getFirst());
        Assert.assertEquals("4", list5.getLast());
    }

    @Test
    public void forEach()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableSet<String> source = Sets.fixedSize.of("1", "2", "3", "4");
        source.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith("1", "2", "3", "4"), result);
    }

    @Test
    public void forEachWithIndex()
    {
        int[] indexSum = new int[1];
        MutableList<String> result = Lists.mutable.of();
        MutableSet<String> source = Sets.fixedSize.of("1", "2", "3", "4");
        source.forEachWithIndex((each, index) -> {
            result.add(each);
            indexSum[0] += index;
        });
        Assert.assertEquals(FastList.newListWith("1", "2", "3", "4"), result);
        Assert.assertEquals(6, indexSum[0]);
    }

    @Test
    public void forEachWith()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableSet<String> source = Sets.fixedSize.of("1", "2", "3", "4");
        source.forEachWith(Procedures2.fromProcedure(CollectionAddProcedure.on(result)), null);
        Assert.assertEquals(FastList.newListWith("1", "2", "3", "4"), result);
    }
}
