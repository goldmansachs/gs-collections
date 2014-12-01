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
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link DoubletonSet}.
 */
public class DoubletonSetTest extends AbstractMemoryEfficientMutableSetTestCase
{
    private DoubletonSet<String> set;

    @Before
    public void setUp()
    {
        this.set = new DoubletonSet<>("1", "2");
    }

    @Override
    protected MutableSet<String> classUnderTest()
    {
        return new DoubletonSet<>("1", "2");
    }

    @Override
    protected MutableSet<String> classUnderTestWithNull()
    {
        return new DoubletonSet<>(null, "2");
    }

    @Test
    public void nonUniqueWith()
    {
        Twin<String> twin1 = Tuples.twin("1", "1");
        Twin<String> twin2 = Tuples.twin("2", "2");
        DoubletonSet<Twin<String>> set = new DoubletonSet<>(twin1, twin2);

        Twin<String> twin3 = Tuples.twin("1", "1");
        set.with(twin3);
        Assert.assertSame(set.getFirst(), twin1);

        Twin<String> twin4 = Tuples.twin("2", "2");
        set.with(twin4);
        Assert.assertSame(set.getLast(), twin2);
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        MutableSet<String> one = Sets.fixedSize.of("1", "2");
        Verify.assertEqualsAndHashCode(one, mSet("1", "2"));
        Verify.assertPostSerializedEqualsAndHashCode(one);
    }

    @Test
    public void remove()
    {
        try
        {
            this.set.remove("1");
            Assert.fail("Cannot remove from DoubletonSet");
        }
        catch (UnsupportedOperationException ignored)
        {
            this.assertUnchanged();
        }
    }

    @Test
    public void addDuplicate()
    {
        try
        {
            this.set.add("1");
            Assert.fail("Cannot add to DoubletonSet");
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
            this.set.add("3");
            Assert.fail("Cannot add to DoubletonSet");
        }
        catch (UnsupportedOperationException ignored)
        {
            this.assertUnchanged();
        }
    }

    @Test
    public void addingAllToOtherSet()
    {
        MutableSet<String> newSet = UnifiedSet.newSet(Sets.fixedSize.of("1", "2"));
        newSet.add("3");
        Verify.assertContainsAll(newSet, "1", "2", "3");
    }

    private void assertUnchanged()
    {
        Verify.assertSize(2, this.set);
        Verify.assertContainsAll(this.set, "1", "2");
        Verify.assertNotContains("3", this.set);
    }

    @Override
    @Test
    public void testClone()
    {
        Verify.assertShallowClone(this.set);
        MutableSet<String> cloneSet = this.set.clone();
        Assert.assertNotSame(cloneSet, this.set);
        Assert.assertEquals(UnifiedSet.newSetWith("1", "2"), cloneSet);
    }

    @Test
    public void newEmpty()
    {
        MutableSet<String> newEmpty = this.set.newEmpty();
        Verify.assertInstanceOf(UnifiedSet.class, newEmpty);
        Verify.assertEmpty(newEmpty);
    }

    @Test
    public void getLast()
    {
        Assert.assertEquals("2", this.set.getLast());
    }

    @Test
    public void forEach()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableSet<String> source = Sets.fixedSize.of("1", "2");
        source.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith("1", "2"), result);
    }

    @Test
    public void forEachWithIndex()
    {
        int[] indexSum = new int[1];
        MutableList<String> result = Lists.mutable.of();
        MutableSet<String> source = Sets.fixedSize.of("1", "2");
        source.forEachWithIndex((each, index) -> {
            result.add(each);
            indexSum[0] += index;
        });
        Assert.assertEquals(FastList.newListWith("1", "2"), result);
        Assert.assertEquals(1, indexSum[0]);
    }

    @Test
    public void forEachWith()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableSet<String> source = Sets.fixedSize.of("1", "2");
        source.forEachWith(Procedures2.fromProcedure(CollectionAddProcedure.on(result)), null);
        Assert.assertEquals(FastList.newListWith("1", "2"), result);
    }

    @Test
    public void getFirstGetLast()
    {
        MutableSet<String> source = Sets.fixedSize.of("1", "2");
        Assert.assertEquals("1", source.getFirst());
        Assert.assertEquals("2", source.getLast());
    }

    @Override
    @Test
    public void groupByEach()
    {
        super.groupByEach();

        MutableSet<Integer> set = Sets.fixedSize.of(1, 2);
        MutableMultimap<Integer, Integer> expected = UnifiedSetMultimap.newMultimap();
        set.forEach(Procedures.cast(value -> expected.putAll(-value, Interval.fromTo(value, set.size()))));
        Multimap<Integer, Integer> actual =
                set.groupByEach(new NegativeIntervalFunction());
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                set.groupByEach(new NegativeIntervalFunction(), UnifiedSetMultimap.<Integer, Integer>newMultimap());
        Assert.assertEquals(expected, actualWithTarget);
    }
}
