/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.list.immutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.partition.list.PartitionImmutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.AddToList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link ImmutableArrayList}.
 */
public class ImmutableArrayListTest
{
    @Test
    public void testNewList()
    {
        ImmutableList<Integer> list = this.newList();
        Assert.assertTrue(list.isEmpty());
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void newWith()
    {
        ImmutableList<Integer> list = this.newList(1, 2, 3);
        ImmutableList<Integer> with = list.newWith(4);
        Assert.assertNotEquals(list, with);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), with);
    }

    @Test
    public void newWithAll()
    {
        ImmutableList<Integer> list = this.newList(1, 2, 3);
        ImmutableList<Integer> withAll = list.newWithAll(FastList.newListWith(4, 5));
        Assert.assertNotEquals(list, withAll);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4, 5), withAll);
    }

    @Test
    public void newWithOut()
    {
        ImmutableList<Integer> list = this.newList(1, 2, 3, 4);
        ImmutableList<Integer> without4 = list.newWithout(4);
        Assert.assertNotEquals(list, without4);
        Assert.assertEquals(FastList.newListWith(1, 2, 3), without4);

        ImmutableList<Integer> without1 = list.newWithout(1);
        Assert.assertNotEquals(list, without1);
        Assert.assertEquals(FastList.newListWith(2, 3, 4), without1);
    }

    @Test
    public void newWithoutAll()
    {
        ImmutableList<Integer> list = this.newList(1, 2, 3, 4, 5);
        ImmutableList<Integer> withoutAll = list.newWithoutAll(FastList.newListWith(4, 5));
        Assert.assertNotEquals(list, withoutAll);
        Assert.assertEquals(FastList.newListWith(1, 2, 3), withoutAll);
        ImmutableList<Integer> largeList = this.newList(Interval.oneTo(20).toArray());
        ImmutableList<Integer> largeWithoutAll = largeList.newWithoutAll(FastList.newList(Interval.oneTo(10)));
        Assert.assertEquals(FastList.newList(Interval.fromTo(11, 20)), largeWithoutAll);
        ImmutableList<Integer> largeWithoutAll2 = largeWithoutAll.newWithoutAll(Interval.fromTo(11, 15));
        Assert.assertEquals(FastList.newList(Interval.fromTo(16, 20)), largeWithoutAll2);
        ImmutableList<Integer> largeWithoutAll3 = largeWithoutAll2.newWithoutAll(UnifiedSet.newSet(Interval.fromTo(16, 19)));
        Assert.assertEquals(FastList.newListWith(20), largeWithoutAll3);
    }

    private ImmutableArrayList<Integer> newList(Integer... elements)
    {
        return ImmutableArrayList.newListWith(elements);
    }

    private ImmutableList<Integer> newListWith(int one, int two)
    {
        return ImmutableArrayList.newListWith(one, two);
    }

    private ImmutableList<Integer> newListWith(int one, int two, int three)
    {
        return ImmutableArrayList.newListWith(one, two, three);
    }

    private ImmutableList<Integer> newListWith(int... littleElements)
    {
        Integer[] bigElements = new Integer[littleElements.length];
        for (int i = 0; i < littleElements.length; i++)
        {
            bigElements[i] = littleElements[i];
        }
        return ImmutableArrayList.newListWith(bigElements);
    }

    @Test
    public void newListWith()
    {
        ImmutableList<Integer> collection = ImmutableArrayList.newListWith(1);
        Assert.assertTrue(collection.notEmpty());
        Assert.assertEquals(1, collection.size());
        Assert.assertTrue(collection.contains(1));
    }

    @Test
    public void newListWithVarArgs()
    {
        ImmutableList<Integer> collection = this.newListWith(1, 2, 3, 4);
        Assert.assertTrue(collection.notEmpty());
        Assert.assertEquals(4, collection.size());
        Assert.assertTrue(collection.containsAllArguments(1, 2, 3, 4));
        Assert.assertTrue(collection.containsAllIterable(Interval.oneTo(4)));
    }

    @Test
    public void forEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> collection = this.newListWith(1, 2, 3, 4);
        collection.forEach(CollectionAddProcedure.on(result));
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void forEachFromTo()
    {
        Collection<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> collection = this.newListWith(1, 2, 3, 4);
        collection.forEach(2, 3, CollectionAddProcedure.on(result));
        Verify.assertSize(2, result);
        Verify.assertContainsAll(result, 3, 4);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> collection = this.newListWith(1, 2, 3, 4);
        collection.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        Verify.assertContainsAll(result, 1, 3, 5, 7);
    }

    @Test
    public void select()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3, 4, 5).select(Predicates.lessThan(3)).containsAllArguments(1, 2));
        Assert.assertFalse(this.newListWith(-1, 2, 3, 4, 5).select(Predicates.lessThan(3)).containsAllArguments(3, 4, 5));
    }

    @Test
    public void reject()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3, 4).reject(Predicates.lessThan(3)).containsAllArguments(3, 4));
    }

    @Test
    public void selectInstancesOf()
    {
        ImmutableList<Number> numbers = ImmutableArrayList.<Number>newListWith(1, 2.0, 3, 4.0, 5);
        ImmutableList<Integer> integers = numbers.selectInstancesOf(Integer.class);
        Assert.assertEquals(iList(1, 3, 5), integers);
    }

    @Test
    public void partition()
    {
        PartitionImmutableList<Integer> partition = this.newListWith(1, 2, 3, 4, 5).partition(Predicates.lessThan(3));
        Assert.assertEquals(iList(1, 2), partition.getSelected());
        Assert.assertEquals(iList(3, 4, 5), partition.getRejected());
    }

    @Test
    public void partitionWith()
    {
        PartitionImmutableList<Integer> partition = this.newListWith(1, 2, 3, 4, 5).partitionWith(Predicates2.<Integer>lessThan(), 3);
        Assert.assertEquals(iList(1, 2), partition.getSelected());
        Assert.assertEquals(iList(3, 4, 5), partition.getRejected());
    }

    @Test
    public void collect()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3, 4).
                collect(Functions.getToString()).containsAllArguments("1", "2", "3", "4"));
    }

    @Test
    public void collectWith()
    {
        Assert.assertEquals(
                this.newListWith(2, 3, 4),
                this.newListWith(1, 2, 3).collectWith(AddFunction.INTEGER, 1));
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newListWith(1, 2, 3, 4, 5).detect(Predicates.equal(3)));
        Assert.assertNull(this.newListWith(1, 2, 3, 4, 5).detect(Predicates.equal(6)));
    }

    @Test
    public void detectIfNone()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(6);
        Assert.assertEquals(Integer.valueOf(3), this.newListWith(1, 2, 3, 4, 5).detectIfNone(Predicates.equal(3), function));
        Assert.assertEquals(Integer.valueOf(6), this.newListWith(1, 2, 3, 4, 5).detectIfNone(Predicates.equal(6), function));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3).allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(this.newListWith(1, 2, 3).allSatisfy(Predicates.equal(1)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.newListWith(1, 2, 3).anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(this.newListWith(1, 2, 3).anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3).noneSatisfy(Predicates.instanceOf(String.class)));
        Assert.assertFalse(this.newListWith(1, 2, 3).noneSatisfy(Predicates.equal(1)));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(3, this.newListWith(1, 2, 3).count(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void collectIf()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3).collectIf(
                Predicates.instanceOf(Integer.class),
                Functions.getToString()).containsAllArguments("1", "2", "3"));
    }

    @Test
    public void getFirst()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newListWith(1, 2, 3).getFirst());
        Assert.assertNotEquals(Integer.valueOf(3), this.newListWith(1, 2, 3).getFirst());
    }

    @Test
    public void getLast()
    {
        Assert.assertNotEquals(Integer.valueOf(1), this.newListWith(1, 2, 3).getLast());
        Assert.assertEquals(Integer.valueOf(3), this.newListWith(1, 2, 3).getLast());
    }

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(this.newList().isEmpty());
        Assert.assertTrue(this.newListWith(1, 2).notEmpty());
    }

    @Test
    public void iterator()
    {
        ImmutableList<Integer> objects = this.newListWith(1, 2, 3);
        Iterator<Integer> iterator = objects.iterator();
        for (int i = objects.size(); i-- > 0; )
        {
            Integer integer = iterator.next();
            Assert.assertEquals(3, integer.intValue() + i);
        }
    }

    @Test
    public void injectInto()
    {
        ImmutableList<Integer> objects = this.newListWith(1, 2, 3);
        Integer result = objects.injectInto(1, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(7), result);
    }

    @Test
    public void toArray()
    {
        ImmutableList<Integer> objects = this.newListWith(1, 2, 3);
        Object[] array = objects.toArray();
        Verify.assertSize(3, array);
        Integer[] array2 = objects.toArray(new Integer[3]);
        Verify.assertSize(3, array2);
    }

    @Test
    public void indexOf()
    {
        ImmutableList<Integer> objects = ImmutableArrayList.newListWith(1, 2, 3);
        Assert.assertEquals(1, objects.indexOf(2));
    }

    @Test
    public void lastIndexOf()
    {
        ImmutableList<Integer> objects = ImmutableArrayList.newListWith(1, 2, 3);
        Assert.assertEquals(1, objects.lastIndexOf(2));
    }

    @Test
    public void equalsAndHashCode()
    {
        ImmutableList<Integer> immutable1 = ImmutableArrayList.newList(FastList.newListWith(1, 2, 3, 4));
        ImmutableList<Integer> immutable2 = ImmutableArrayList.newListWith(1, 2, 3, 4);
        ImmutableList<Integer> immutable3 = ImmutableArrayList.newListWith(1, 2, 3);
        ImmutableList<Integer> immutable4 = ImmutableArrayList.newListWith(0, 1, 2, 3);
        ImmutableList<Integer> immutable5 = ImmutableArrayList.newListWith(1, 2, 3, 4, 5);
        MutableList<Integer> mutable1 = FastList.newList(immutable1);
        ImmutableList<Integer> immutableCopy = mutable1.toImmutable();
        List<Integer> mutable2 = new LinkedList<Integer>(mutable1);
        List<Integer> mutable3 = new ArrayList<Integer>(mutable1);
        Verify.assertEqualsAndHashCode(immutable1, immutable2);
        Assert.assertNotEquals(immutable1, immutable3);
        Assert.assertNotEquals(immutable1, immutable4);
        Assert.assertNotEquals(immutable1, immutable5);
        Verify.assertEqualsAndHashCode(mutable1, immutable1);
        Verify.assertEqualsAndHashCode(immutableCopy, immutable1);
        Verify.assertEqualsAndHashCode(mutable2, immutable1);
        Verify.assertEqualsAndHashCode(mutable3, immutable1);
        Verify.assertPostSerializedEqualsAndHashCode(immutable1);
        Assert.assertNotEquals(immutable1, UnifiedSet.newSet(mutable1));
        mutable1.add(null);
        mutable2.add(null);
        mutable3.add(null);
        Assert.assertNotEquals(mutable1, immutable1);
        Assert.assertNotEquals(mutable2, immutable1);
        Assert.assertNotEquals(mutable3, immutable1);
        mutable1.remove(null);
        mutable2.remove(null);
        mutable3.remove(null);
        Verify.assertEqualsAndHashCode(mutable1, immutable1);
        Verify.assertEqualsAndHashCode(mutable2, immutable1);
        Verify.assertEqualsAndHashCode(mutable3, immutable1);
        mutable1.set(2, null);
        mutable2.set(2, null);
        mutable3.set(2, null);
        Assert.assertNotEquals(mutable1, immutable1);
        Assert.assertNotEquals(mutable2, immutable1);
        Assert.assertNotEquals(mutable3, immutable1);
        mutable1.remove(2);
        mutable2.remove(2);
        mutable3.remove(2);
        Assert.assertNotEquals(mutable1, immutable1);
        Assert.assertNotEquals(mutable2, immutable1);
        Assert.assertNotEquals(mutable3, immutable1);
    }

    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> collection = ImmutableArrayList.newListWith(1, 2, 3, 4);
        collection.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void toList()
    {
        ImmutableArrayList<Integer> integers = ImmutableArrayList.newListWith(1, 2, 3, 4);
        MutableList<Integer> list = integers.toList();
        Assert.assertTrue(list.containsAllArguments(1, 2, 3, 4));
    }

    @Test
    public void toSortedList()
    {
        ImmutableArrayList<Integer> integers = ImmutableArrayList.newListWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(FastList.newListWith(4, 3, 2, 1), list);
    }

    @Test
    public void toSortedListBy()
    {
        ImmutableArrayList<Integer> integers = ImmutableArrayList.newListWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedListBy(Functions.getToString());
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), list);
    }

    @Test
    public void toSet()
    {
        ImmutableArrayList<Integer> integers = ImmutableArrayList.newListWith(1, 2, 3, 4);
        MutableSet<Integer> set = integers.toSet();
        Verify.assertContainsAll(set, 1, 2, 3, 4);
    }

    @Test
    public void toMap()
    {
        ImmutableArrayList<Integer> integers = ImmutableArrayList.newListWith(1, 2, 3, 4);
        MutableMap<String, String> map =
                integers.toMap(Functions.getToString(), Functions.getToString());
        Assert.assertEquals(UnifiedMap.newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"), map);
    }

    @Test
    public void serialization()
    {
        ImmutableList<Integer> collection = ImmutableArrayList.newListWith(1, 2, 3, 4, 5);
        ImmutableList<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Assert.assertEquals(5, deserializedCollection.size());
        Assert.assertTrue(deserializedCollection.containsAllArguments(1, 2, 3, 4, 5));
        Verify.assertEqualsAndHashCode(collection, deserializedCollection);
    }

    @Test
    public void forEachWithIndexWithFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        this.newListWith(1, 2, 3).forEachWithIndex(1, 2, new AddToList(result));
        Assert.assertEquals(FastList.newListWith(2, 3), result);
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[1, 2, 3]", this.newListWith(1, 2, 3).toString());
    }

    @Test
    public void takeWhile()
    {
        Assert.assertEquals(
                iList(1, 2, 3),
                this.newList(1, 2, 3, 4, 5).takeWhile(Predicates.lessThan(4)));

        Assert.assertEquals(
                iList(1, 2, 3, 4, 5),
                this.newList(1, 2, 3, 4, 5).takeWhile(Predicates.lessThan(10)));

        Assert.assertEquals(
                iList(),
                this.newList(1, 2, 3, 4, 5).takeWhile(Predicates.lessThan(0)));
    }

    @Test
    public void dropWhile()
    {
        Assert.assertEquals(
                iList(4, 5),
                this.newList(1, 2, 3, 4, 5).dropWhile(Predicates.lessThan(4)));

        Assert.assertEquals(
                iList(),
                this.newList(1, 2, 3, 4, 5).dropWhile(Predicates.lessThan(10)));

        Assert.assertEquals(
                iList(1, 2, 3, 4, 5),
                this.newList(1, 2, 3, 4, 5).dropWhile(Predicates.lessThan(0)));
    }

    @Test
    public void partitionWhile()
    {
        PartitionImmutableList<Integer> partition1 = this.newList(1, 2, 3, 4, 5).partitionWhile(Predicates.lessThan(4));
        Assert.assertEquals(iList(1, 2, 3), partition1.getSelected());
        Assert.assertEquals(iList(4, 5), partition1.getRejected());

        PartitionImmutableList<Integer> partition2 = this.newList(1, 2, 3, 4, 5).partitionWhile(Predicates.lessThan(0));
        Assert.assertEquals(iList(), partition2.getSelected());
        Assert.assertEquals(iList(1, 2, 3, 4, 5), partition2.getRejected());

        PartitionImmutableList<Integer> partition3 = this.newList(1, 2, 3, 4, 5).partitionWhile(Predicates.lessThan(10));
        Assert.assertEquals(iList(1, 2, 3, 4, 5), partition3.getSelected());
        Assert.assertEquals(iList(), partition3.getRejected());
    }
}
