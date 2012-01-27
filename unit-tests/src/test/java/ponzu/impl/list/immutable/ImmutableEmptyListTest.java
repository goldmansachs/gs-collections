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

package ponzu.impl.list.immutable;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.ObjectIntProcedures;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.Interval;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableEmptyListTest extends AbstractImmutableListTestCase
{
    @Override
    protected ImmutableList<Integer> newList()
    {
        return Lists.immutable.of();
    }

    @Override
    @Test
    public void testIndexOf()
    {
        Assert.assertEquals(-1, this.newList().indexOf(1));
        Assert.assertEquals(-1, this.newList().indexOf(null));
        ImmutableList<Integer> immutableList = this.newList().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.indexOf(null));
        Assert.assertEquals(-1, this.newList().indexOf(Integer.MAX_VALUE));
    }

    @Override
    @Test
    public void testLastIndexOf()
    {
        Assert.assertEquals(-1, this.newList().lastIndexOf(1));
        Assert.assertEquals(-1, this.newList().lastIndexOf(null));
        Assert.assertEquals(-1, this.newList().lastIndexOf(null));
        ImmutableList<Integer> immutableList = this.newList().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.lastIndexOf(null));
        Assert.assertEquals(-1, this.newList().lastIndexOf(Integer.MAX_VALUE));
    }

    @Test
    public void testNewWithout()
    {
        Assert.assertSame(Lists.immutable.of(), Lists.immutable.of().newWithout(1));
        Assert.assertSame(Lists.immutable.of(), Lists.immutable.of().newWithoutAll(Interval.oneTo(3)));
    }

    @Override
    @Test
    public void testReverseForEach()
    {
        ImmutableList<Integer> list = Lists.immutable.of();
        MutableList<Integer> result = Lists.mutable.of();
        list.reverseForEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(list, result);
    }

    @Override
    @Test
    public void testForEachFromTo()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        final MutableList<Integer> reverseResult = Lists.mutable.of();
        final ImmutableList<Integer> list = this.newList();
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEach(0, list.size() - 1, CollectionAddProcedure.<Integer>on(result));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEach(list.size() - 1, 0, CollectionAddProcedure.<Integer>on(reverseResult));
            }
        });
    }

    @Override
    @Test
    public void testForEachWithIndexFromTo()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        final MutableList<Integer> reverseResult = Lists.mutable.of();
        final ImmutableList<Integer> list = this.newList();
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEachWithIndex(0, list.size() - 1, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.<Integer>on(result)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEachWithIndex(list.size() - 1, 0, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.<Integer>on(reverseResult)));
            }
        });
    }

    @Override
    @Test
    public void testDetect()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertNull(integers.find(Predicates.equal(1)));
    }

    @Override
    @Test
    public void testAllSatisfy()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertTrue(integers.allSatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    @Test
    public void testAnySatisfy()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertFalse(integers.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    @Test
    public void testGetFirst()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertNull(integers.getFirst());
    }

    @Override
    @Test
    public void testGetLast()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertNull(integers.getLast());
    }

    @Override
    @Test
    public void testIsEmpty()
    {
        ImmutableList<Integer> list = this.newList();
        Assert.assertTrue(list.isEmpty());
        Assert.assertFalse(list.notEmpty());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        this.newList().min(Comparators.naturalOrder());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        this.newList().max(Comparators.naturalOrder());
    }

    @Test
    @Override
    public void min_null_throws()
    {
        // Not applicable for empty collections
        super.min_null_throws();
    }

    @Test
    @Override
    public void max_null_throws()
    {
        // Not applicable for empty collections
        super.max_null_throws();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min_without_comparator()
    {
        this.newList().min();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max_without_comparator()
    {
        this.newList().max();
    }

    @Test
    @Override
    public void min_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.min_null_throws_without_comparator();
    }

    @Test
    @Override
    public void max_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.max_null_throws_without_comparator();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        this.newList().minBy(Functions.getToString());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        this.newList().maxBy(Functions.getToString());
    }

    @Override
    @Test
    public void zip()
    {
        ImmutableList<Integer> immutableList = this.newList();
        List<Object> nulls = Collections.nCopies(immutableList.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableList.size() + 1, null);

        ImmutableList<Pair<Integer, Object>> pairs = immutableList.zip(nulls);
        Assert.assertEquals(immutableList, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairs.transform(Functions.<Object>secondOfPair()));

        ImmutableList<Pair<Integer, Object>> pairsPlusOne = immutableList.zip(nullsPlusOne);
        Assert.assertEquals(immutableList, pairsPlusOne.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairsPlusOne.transform(Functions.<Object>secondOfPair()));

        Assert.assertEquals(immutableList.zip(nulls), immutableList.zip(nulls, FastList.<Pair<Integer, Object>>newList()));
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        ImmutableList<Integer> immutableList = this.newList();
        ImmutableList<Pair<Integer, Integer>> pairs = immutableList.zipWithIndex();

        Assert.assertEquals(immutableList, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(FastList.<Integer>newList(), pairs.transform(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableList.zipWithIndex(), immutableList.zipWithIndex(FastList.<Pair<Integer, Integer>>newList()));
    }

    @Test
    public void chunk()
    {
        Assert.assertEquals(Lists.mutable.of(), this.newList().chunk(2));
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.newList().chunk(0);
    }

    @Override
    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.newList(), this.newList().chunk(10));
        Verify.assertInstanceOf(ImmutableList.class, this.newList().chunk(10));
    }

    @Override
    @Test
    public void testEqualsAndHashCode()
    {
        ImmutableList<Integer> immutable = this.newList();
        MutableList<Integer> mutable = FastList.newList(immutable);
        Verify.assertEqualsAndHashCode(immutable, mutable);
        Verify.assertPostSerializedIdentity(immutable);
        Verify.assertNotEquals(immutable, UnifiedSet.newSet(mutable));
    }
}
