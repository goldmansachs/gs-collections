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

import com.gs.collections.impl.collection.immutable.AbstractImmutableCollectionTestCase;
import org.junit.Assert;
import org.junit.Test;
import ponzu.api.block.function.Function;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.collection.ImmutableCollection;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.ObjectIntProcedures;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.Interval;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.test.Verify;
import ponzu.impl.utility.ListIterate;

public abstract class AbstractImmutableListTestCase extends AbstractImmutableCollectionTestCase
{
    @Override
    protected abstract ImmutableList<Integer> classUnderTest();

    @Override
    protected <T> MutableList<T> newMutable()
    {
        return FastList.newList();
    }

    @Test
    public void testEqualsAndHashCode()
    {
        ImmutableList<Integer> immutable = this.classUnderTest();
        MutableList<Integer> mutable = FastList.newList(immutable);
        Verify.assertEqualsAndHashCode(immutable, mutable);
        Verify.assertPostSerializedEqualsAndHashCode(immutable);
        Verify.assertNotEquals(immutable, UnifiedSet.newSet(mutable));
    }

    @Test
    public void testContains()
    {
        ImmutableList<Integer> list = this.classUnderTest();
        for (int i = 1; i <= list.size(); i++)
        {
            Assert.assertTrue(list.contains(i));
        }
        Assert.assertFalse(list.contains(list.size() + 1));
    }

    @Test
    public void testContainsAllArray()
    {
        Assert.assertTrue(this.classUnderTest().containsAllArguments(this.classUnderTest().toArray()));
    }

    @Test
    public void testContainsAllIterable()
    {
        Assert.assertTrue(this.classUnderTest().containsAllIterable(this.classUnderTest()));
    }

    @Test
    public void testIndexOf()
    {
        Assert.assertEquals(0, this.classUnderTest().indexOf(1));
        Assert.assertEquals(-1, this.classUnderTest().indexOf(null));
        ImmutableList<Integer> immutableList = this.classUnderTest().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.indexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().indexOf(Integer.MAX_VALUE));
    }

    @Test
    public void testLastIndexOf()
    {
        Assert.assertEquals(0, this.classUnderTest().lastIndexOf(1));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(null));
        ImmutableList<Integer> immutableList = this.classUnderTest().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.lastIndexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(Integer.MAX_VALUE));
    }

    @Test
    public void testGet()
    {
        final ImmutableList<Integer> list = this.classUnderTest();
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            @Override
            public void run()
            {
                list.get(list.size() + 1);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            @Override
            public void run()
            {
                list.get(-1);
            }
        });
    }

    @Test
    public void testForEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> collection = this.classUnderTest();
        collection.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(collection, result);
    }

    @Test
    public void testReverseForEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> list = this.classUnderTest();
        list.reverseForEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), result);
    }

    @Test
    public void testForEachFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> reverseResult = Lists.mutable.of();
        ImmutableList<Integer> list = this.classUnderTest();
        list.forEach(0, list.size() - 1, CollectionAddProcedure.on(result));
        Assert.assertEquals(list, result);
        list.forEach(list.size() - 1, 0, CollectionAddProcedure.on(reverseResult));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), reverseResult);
    }

    @Test
    public void testForEachWithIndexFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> reverseResult = Lists.mutable.of();
        ImmutableList<Integer> list = this.classUnderTest();
        list.forEachWithIndex(0, list.size() - 1, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(result)));
        Assert.assertEquals(list, result);
        list.forEachWithIndex(list.size() - 1, 0, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResult)));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), reverseResult);
    }

    @Test
    public void testForEachWith()
    {
        final MutableCollection<Integer> result = Lists.mutable.of();
        this.classUnderTest().forEachWith(new Procedure2<Integer, Integer>()
        {
            @Override
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Assert.assertEquals(this.classUnderTest(), result);
    }

    @Test
    public void testForEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        this.classUnderTest().forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            @Override
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        result.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            @Override
            public void value(Integer object, int index)
            {
                Assert.assertEquals(object, result.set(index, object - index));
            }
        });
        Assert.assertEquals(this.classUnderTest(), result);
    }

    @Test
    public void testSelectWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.filter(Predicates.lessThan(integers.size() + 1), FastList.<Integer>newList()));
        Verify.assertEmpty(integers.filter(Predicates.greaterThan(integers.size()), FastList.<Integer>newList()));
    }

    @Test
    public void testRejectWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Verify.assertEmpty(integers.filterNot(Predicates.lessThan(integers.size() + 1), FastList.<Integer>newList()));
        Assert.assertEquals(integers, integers.filterNot(Predicates.greaterThan(integers.size()), FastList.<Integer>newList()));
    }

    @Test
    public void testCollectWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.transform(Functions.getIntegerPassThru(), FastList.<Integer>newList()));
    }

    @Test
    public void flatCollectWithTarget()
    {
        MutableCollection<String> actual = this.classUnderTest().flatTransform(new Function<Integer, MutableList<String>>()
        {
            @Override
            public MutableList<String> valueOf(Integer integer)
            {
                return Lists.fixedSize.of(String.valueOf(integer));
            }
        }, FastList.<String>newList());

        ImmutableCollection<String> expected = this.classUnderTest().transform(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void zip()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        List<Object> nulls = Collections.nCopies(immutableCollection.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableCollection.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(immutableCollection.size() - 1, null);

        ImmutableCollection<Pair<Integer, Object>> pairs = immutableCollection.zip(nulls);
        Assert.assertEquals(immutableCollection, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairs.transform(Functions.<Object>secondOfPair()));

        ImmutableCollection<Pair<Integer, Object>> pairsPlusOne = immutableCollection.zip(nullsPlusOne);
        Assert.assertEquals(immutableCollection, pairsPlusOne.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairsPlusOne.transform(Functions.<Object>secondOfPair()));

        ImmutableCollection<Pair<Integer, Object>> pairsMinusOne = immutableCollection.zip(nullsMinusOne);
        Assert.assertEquals(immutableCollection.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(immutableCollection.containsAllIterable(pairsMinusOne.transform(Functions.<Integer>firstOfPair())));

        Assert.assertEquals(immutableCollection.zip(nulls), immutableCollection.zip(nulls, FastList.<Pair<Integer, Object>>newList()));
    }

    @Test
    public void zipWithIndex()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        ImmutableCollection<Pair<Integer, Integer>> pairs = immutableCollection.zipWithIndex();

        Assert.assertEquals(immutableCollection, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(Interval.zeroTo(immutableCollection.size() - 1), pairs.transform(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableCollection.zipWithIndex(), immutableCollection.zipWithIndex(FastList.<Pair<Integer, Integer>>newList()));
    }

    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.classUnderTest(), this.classUnderTest().chunk(10).getFirst());
        Verify.assertInstanceOf(ImmutableList.class, this.classUnderTest().chunk(10).getFirst());
    }

    @Test
    public void testCollectIfWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.transformIf(Predicates.instanceOf(Integer.class),
                Functions.getIntegerPassThru(), FastList.<Integer>newList()));
    }

    @Test
    public void toList()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableList<Integer> list = integers.toList();
        Verify.assertEqualsAndHashCode(integers, list);
        Assert.assertNotSame(integers, list);
    }

    @Test
    public void toSortedListBy()
    {
        MutableList<Integer> mutableList = this.classUnderTest().toList();
        Collections.shuffle(mutableList);
        ImmutableList<Integer> immutableList = mutableList.toImmutable();
        MutableList<Integer> sortedList = immutableList.toSortedListBy(Functions.getIntegerPassThru());
        Assert.assertEquals(this.classUnderTest(), sortedList);
    }

    @Test
    public void testRemoveAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().remove(1);
            }
        });
    }

    @Test
    public void testSet()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().set(0, 1);
            }
        });
    }

    @Test
    public void testAddAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().add(0, 1);
            }
        });
    }

    @Test
    public void testAddAllAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().addAll(0, Lists.fixedSize.<Integer>of());
            }
        });
    }

    @Test
    public void testSubList()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().subList(0, 1);
            }
        });
    }

    @Test
    public void testListIteratorHasPrevious()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().listIterator().hasPrevious();
            }
        });
    }

    @Test
    public void testListIteratorPrevious()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().listIterator().previous();
            }
        });
    }

    @Test
    public void testListIteratorPreviousIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().listIterator().previousIndex();
            }
        });
    }

    @Test
    public void testListIteratorNextIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().listIterator().previousIndex();
            }
        });
    }

    @Test
    public void testListIteratorRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().listIterator().remove();
            }
        });
    }

    @Test
    public void testListIteratorWithIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            @Override
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().listIterator(0);
            }
        });
    }
}
