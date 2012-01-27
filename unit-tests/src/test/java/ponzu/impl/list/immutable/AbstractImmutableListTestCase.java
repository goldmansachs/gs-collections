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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function0;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.ObjectIntProcedures;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.function.AddFunction;
import ponzu.impl.block.function.PassThruFunction0;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.Interval;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.set.sorted.mutable.TreeSortedSet;
import ponzu.impl.test.Verify;
import ponzu.impl.utility.ListIterate;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractImmutableListTestCase
{
    protected abstract ImmutableList<Integer> newList();

    @Test
    public void testEqualsAndHashCode()
    {
        ImmutableList<Integer> immutable = this.newList();
        MutableList<Integer> mutable = FastList.newList(immutable);
        Verify.assertEqualsAndHashCode(immutable, mutable);
        Verify.assertPostSerializedEqualsAndHashCode(immutable);
        Verify.assertNotEquals(immutable, UnifiedSet.newSet(mutable));
    }

    @Test
    public void testContains()
    {
        ImmutableList<Integer> list = this.newList();
        for (int i = 1; i <= list.size(); i++)
        {
            Assert.assertTrue(list.contains(i));
        }
        Assert.assertFalse(list.contains(list.size() + 1));
    }

    @Test
    public void testContainsAllArray()
    {
        Assert.assertTrue(this.newList().containsAllArguments(this.newList().toArray()));
    }

    @Test
    public void testContainsAllIterable()
    {
        Assert.assertTrue(this.newList().containsAllIterable(this.newList()));
    }

    @Test
    public void testIndexOf()
    {
        Assert.assertEquals(0, this.newList().indexOf(1));
        Assert.assertEquals(-1, this.newList().indexOf(null));
        ImmutableList<Integer> immutableList = this.newList().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.indexOf(null));
        Assert.assertEquals(-1, this.newList().indexOf(Integer.MAX_VALUE));
    }

    @Test
    public void testLastIndexOf()
    {
        Assert.assertEquals(0, this.newList().lastIndexOf(1));
        Assert.assertEquals(-1, this.newList().lastIndexOf(null));
        Assert.assertEquals(-1, this.newList().lastIndexOf(null));
        ImmutableList<Integer> immutableList = this.newList().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.lastIndexOf(null));
        Assert.assertEquals(-1, this.newList().lastIndexOf(Integer.MAX_VALUE));
    }

    @Test
    public void testGet()
    {
        final ImmutableList<Integer> list = this.newList();
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                list.get(list.size() + 1);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
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
        ImmutableList<Integer> collection = this.newList();
        collection.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(collection, result);
    }

    @Test
    public void testReverseForEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> list = this.newList();
        list.reverseForEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), result);
    }

    @Test
    public void testForEachFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> reverseResult = Lists.mutable.of();
        ImmutableList<Integer> list = this.newList();
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
        ImmutableList<Integer> list = this.newList();
        list.forEachWithIndex(0, list.size() - 1, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(result)));
        Assert.assertEquals(list, result);
        list.forEachWithIndex(list.size() - 1, 0, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResult)));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), reverseResult);
    }

    @Test
    public void testForEachWith()
    {
        final Collection<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> list = this.newList();
        list.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Assert.assertEquals(list, result);
    }

    @Test
    public void testForEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> integers = this.newList();
        integers.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        result.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                Assert.assertEquals(object, result.set(index, object - index));
            }
        });
        Assert.assertEquals(integers, result);
    }

    @Test
    public void testSelect()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(integers, integers.filter(Predicates.lessThan(integers.size() + 1)));
        Verify.assertIterableEmpty(integers.filter(Predicates.greaterThan(integers.size())));
    }

    @Test
    public void testSelectWithTarget()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(integers, integers.filter(Predicates.lessThan(integers.size() + 1), FastList.<Integer>newList()));
        Verify.assertEmpty(integers.filter(Predicates.greaterThan(integers.size()), FastList.<Integer>newList()));
    }

    @Test
    public void testReject()
    {
        ImmutableList<Integer> integers = this.newList();
        Verify.assertIterableEmpty(integers.filterNot(Predicates.lessThan(integers.size() + 1)));
        Assert.assertEquals(integers, integers.filterNot(Predicates.greaterThan(integers.size())));
    }

    @Test
    public void testRejectWithTarget()
    {
        ImmutableList<Integer> integers = this.newList();
        Verify.assertEmpty(integers.filterNot(Predicates.lessThan(integers.size() + 1), FastList.<Integer>newList()));
        Assert.assertEquals(integers, integers.filterNot(Predicates.greaterThan(integers.size()), FastList.<Integer>newList()));
    }

    @Test
    public void testCollect()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(integers, integers.transform(Functions.getIntegerPassThru()));
    }

    @Test
    public void testCollectWithTarget()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(integers, integers.transform(Functions.getIntegerPassThru(), FastList.<Integer>newList()));
    }

    @Test
    public void flatCollect()
    {
        ImmutableList<String> actual = this.newList().flatTransform(new Function<Integer, MutableList<String>>()
        {
            public MutableList<String> valueOf(Integer integer)
            {
                return Lists.fixedSize.of(String.valueOf(integer));
            }
        });

        ImmutableList<String> expected = this.newList().transform(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void flatCollectWithTarget()
    {
        MutableList<String> actual = this.newList().flatTransform(new Function<Integer, MutableList<String>>()
        {
            public MutableList<String> valueOf(Integer integer)
            {
                return Lists.fixedSize.of(String.valueOf(integer));
            }
        }, FastList.<String>newList());

        ImmutableList<String> expected = this.newList().transform(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void zip()
    {
        ImmutableList<Integer> immutableList = this.newList();
        List<Object> nulls = Collections.nCopies(immutableList.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableList.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(immutableList.size() - 1, null);

        ImmutableList<Pair<Integer, Object>> pairs = immutableList.zip(nulls);
        Assert.assertEquals(immutableList, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairs.transform(Functions.<Object>secondOfPair()));

        ImmutableList<Pair<Integer, Object>> pairsPlusOne = immutableList.zip(nullsPlusOne);
        Assert.assertEquals(immutableList, pairsPlusOne.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairsPlusOne.transform(Functions.<Object>secondOfPair()));

        ImmutableList<Pair<Integer, Object>> pairsMinusOne = immutableList.zip(nullsMinusOne);
        Assert.assertEquals(immutableList.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(immutableList.containsAllIterable(pairsMinusOne.transform(Functions.<Integer>firstOfPair())));

        Assert.assertEquals(immutableList.zip(nulls), immutableList.zip(nulls, FastList.<Pair<Integer, Object>>newList()));
    }

    @Test
    public void zipWithIndex()
    {
        ImmutableList<Integer> immutableList = this.newList();
        ImmutableList<Pair<Integer, Integer>> pairs = immutableList.zipWithIndex();

        Assert.assertEquals(immutableList, pairs.transform(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(Interval.zeroTo(immutableList.size() - 1), pairs.transform(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableList.zipWithIndex(), immutableList.zipWithIndex(FastList.<Pair<Integer, Integer>>newList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.newList().chunk(0);
    }

    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.newList(), this.newList().chunk(10).getFirst());
        Verify.assertInstanceOf(ImmutableList.class, this.newList().chunk(10).getFirst());
    }

    @Test
    public void testDetect()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(Integer.valueOf(1), integers.find(Predicates.equal(1)));
        Assert.assertNull(integers.find(Predicates.equal(integers.size() + 1)));
    }

    private ImmutableList<Integer> classUnderTestWithNull()
    {
        return this.newList().filterNot(Predicates.equal(1)).newWith(null);
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws()
    {
        this.classUnderTestWithNull().min(Comparators.naturalOrder());
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws()
    {
        this.classUnderTestWithNull().max(Comparators.naturalOrder());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().min(Comparators.naturalOrder()));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().max(Comparators.reverse(Comparators.naturalOrder())));
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws_without_comparator()
    {
        this.classUnderTestWithNull().min();
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws_without_comparator()
    {
        this.classUnderTestWithNull().max();
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(this.newList().size()), this.newList().max());
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(this.newList().size()), this.newList().maxBy(Functions.getIntegerPassThru()));
    }

    @Test
    public void testDetectIfNoneWithBlock()
    {
        ImmutableList<Integer> integers = this.newList();
        Function0<Integer> function = new PassThruFunction0<Integer>(integers.size() + 1);
        Assert.assertEquals(Integer.valueOf(1), integers.findIfNone(Predicates.equal(1), function));
        Assert.assertEquals(Integer.valueOf(integers.size() + 1), integers.findIfNone(Predicates.equal(integers.size() + 1), function));
    }

    @Test
    public void testAllSatisfy()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertTrue(integers.allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(integers.allSatisfy(Predicates.equal(0)));
    }

    @Test
    public void testAnySatisfy()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertFalse(integers.anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(integers.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void testCount()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(integers.size(), integers.count(Predicates.instanceOf(Integer.class)));
        Assert.assertEquals(0, integers.count(Predicates.instanceOf(String.class)));
    }

    @Test
    public void testCollectIf()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(integers, integers.transformIf(Predicates.instanceOf(Integer.class),
                Functions.getIntegerPassThru()));
    }

    @Test
    public void testCollectIfWithTarget()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(integers, integers.transformIf(Predicates.instanceOf(Integer.class),
                Functions.getIntegerPassThru(), FastList.<Integer>newList()));
    }

    @Test
    public void testGetFirst()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(Integer.valueOf(1), integers.getFirst());
    }

    @Test
    public void testGetLast()
    {
        ImmutableList<Integer> integers = this.newList();
        Assert.assertEquals(Integer.valueOf(integers.size()), integers.getLast());
    }

    @Test
    public void testIsEmpty()
    {
        ImmutableList<Integer> list = this.newList();
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.notEmpty());
    }

    @Test
    public void testIterator()
    {
        ImmutableList<Integer> integers = this.newList();
        final Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            Integer integer = iterator.next();
            Assert.assertEquals(i + 1, integer.intValue());
        }

        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator.next();
            }
        });
    }

    @Test
    public void testInjectInto()
    {
        ImmutableList<Integer> integers = this.newList();
        Integer result = integers.foldLeft(0, AddFunction.INTEGER);
        Assert.assertEquals(FastList.newList(integers).foldLeft(0, AddFunction.INTEGER_TO_INT), result.intValue());
    }

    @Test
    public void testToArray()
    {
        ImmutableList<Integer> integers = this.newList();
        MutableList<Integer> copy = FastList.newList(integers);
        Assert.assertArrayEquals(integers.toArray(), copy.toArray());
        Assert.assertArrayEquals(integers.toArray(new Integer[integers.size()]), copy.toArray(new Integer[integers.size()]));
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals(FastList.newList(this.newList()).toString(), this.newList().toString());
    }

    @Test
    public void testMakeString()
    {
        Assert.assertEquals(FastList.newList(this.newList()).toString(), '[' + this.newList().makeString() + ']');
    }

    @Test
    public void testAppendString()
    {
        Appendable builder = new StringBuilder();
        this.newList().appendString(builder);
        Assert.assertEquals(FastList.newList(this.newList()).toString(), '[' + builder.toString() + ']');
    }

    @Test
    public void toList()
    {
        ImmutableList<Integer> integers = this.newList();
        MutableList<Integer> list = integers.toList();
        Verify.assertEqualsAndHashCode(integers, list);
        Assert.assertNotSame(integers, list);
    }

    @Test
    public void toSortedList()
    {
        ImmutableList<Integer> integers = this.newList();
        MutableList<Integer> copy = FastList.newList(integers);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(copy.sortThis(Collections.<Integer>reverseOrder()), list);
        MutableList<Integer> list2 = integers.toSortedList();
        Assert.assertEquals(copy.sortThis(), list2);
    }

    @Test
    public void toSortedListBy()
    {
        MutableList<Integer> mutableList = this.newList().toList();
        Collections.shuffle(mutableList);
        ImmutableList<Integer> immutableList = mutableList.toImmutable();
        MutableList<Integer> sortedList = immutableList.toSortedListBy(Functions.getIntegerPassThru());
        Assert.assertEquals(this.newList(), sortedList);
    }

    @Test
    public void toSortedSet()
    {
        ImmutableList<Integer> integers = this.newList();
        MutableSortedSet<Integer> set = integers.toSortedSet();
        Verify.assertListsEqual(integers.toSortedList(), set.toList());
    }

    @Test
    public void toSortedSetWithComparator()
    {
        ImmutableList<Integer> integers = this.newList();
        MutableSortedSet<Integer> set = integers.toSortedSet(Comparators.<Integer>reverseNaturalOrder());
        Assert.assertEquals(integers.toSet(), set);
        Assert.assertEquals(integers.toSortedList(Comparators.<Integer>reverseNaturalOrder()), set.toList());
    }

    @Test
    public void toSortedSetBy()
    {
        ImmutableList<Integer> integers = this.newList();
        MutableSortedSet<Integer> set = integers.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSet(integers), set);
    }

    @Test
    public void testForLoop()
    {
        ImmutableList<Integer> list = this.newList();
        for (Integer each : list)
        {
            Assert.assertNotNull(each);
        }
    }

    @Test
    public void testIteratorRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().iterator().remove();
            }
        });
    }

    @Test
    public void testAdd()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().add(1);
            }
        });
    }

    @Test
    public void testRemoveAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().remove(1);
            }
        });
    }

    @Test
    public void testRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().remove(Integer.valueOf(1));
            }
        });
    }

    @Test
    public void testClear()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().clear();
            }
        });
    }

    @Test
    public void testRemoveAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().removeAll(Lists.fixedSize.of());
            }
        });
    }

    @Test
    public void testRetainAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().retainAll(Lists.fixedSize.of());
            }
        });
    }

    @Test
    public void testSet()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().set(0, 1);
            }
        });
    }

    @Test
    public void testAddAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().add(0, 1);
            }
        });
    }

    @Test
    public void testAddAllAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().addAll(0, Lists.fixedSize.<Integer>of());
            }
        });
    }

    @Test
    public void testAddAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().addAll(Lists.fixedSize.<Integer>of());
            }
        });
    }

    @Test
    public void testSubList()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().subList(0, 1);
            }
        });
    }

    @Test
    public void testListIteratorHasPrevious()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().listIterator().hasPrevious();
            }
        });
    }

    @Test
    public void testListIteratorPrevious()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().listIterator().previous();
            }
        });
    }

    @Test
    public void testListIteratorPreviousIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().listIterator().previousIndex();
            }
        });
    }

    @Test
    public void testListIteratorNextIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().listIterator().previousIndex();
            }
        });
    }

    @Test
    public void testListIteratorRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().listIterator().remove();
            }
        });
    }

    @Test
    public void testListIteratorWithIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.newList().castToList().listIterator(0);
            }
        });
    }
}
