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
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import ponzu.api.block.function.Generator;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.partition.list.PartitionImmutableList;
import ponzu.api.set.MutableSet;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.function.AddFunction;
import ponzu.impl.block.function.Constant;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.Interval;
import ponzu.impl.list.mutable.AddToList;
import ponzu.impl.list.mutable.ArrayListAdapter;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.test.SerializeTestHelper;
import ponzu.impl.test.Verify;

import static ponzu.impl.factory.Iterables.*;

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
        Verify.assertNotEquals(list, with);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), with);
    }

    @Test
    public void newWithAll()
    {
        ImmutableList<Integer> list = this.newList(1, 2, 3);
        ImmutableList<Integer> withAll = list.newWithAll(FastList.newListWith(4, 5));
        Verify.assertNotEquals(list, withAll);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4, 5), withAll);
    }

    @Test
    public void newWithOut()
    {
        ImmutableList<Integer> list = this.newList(1, 2, 3, 4);
        ImmutableList<Integer> without = list.newWithout(4);
        Verify.assertNotEquals(list, without);
        Assert.assertEquals(FastList.newListWith(1, 2, 3), without);
    }

    @Test
    public void newWithoutAll()
    {
        ImmutableList<Integer> list = this.newList(1, 2, 3, 4, 5);
        ImmutableList<Integer> withoutAll = list.newWithoutAll(FastList.newListWith(4, 5));
        Verify.assertNotEquals(list, withoutAll);
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
        collection.forEach(CollectionAddProcedure.<Integer>on(result));
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void forEachFromTo()
    {
        Collection<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> collection = this.newListWith(1, 2, 3, 4);
        collection.forEach(2, 3, CollectionAddProcedure.<Integer>on(result));
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
        Assert.assertTrue(this.newListWith(1, 2, 3, 4, 5).filter(Predicates.lessThan(3)).containsAllArguments(1, 2));
        Assert.assertFalse(this.newListWith(-1, 2, 3, 4, 5).filter(Predicates.lessThan(3)).containsAllArguments(3, 4, 5));
    }

    @Test
    public void reject()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3, 4).filterNot(Predicates.lessThan(3)).containsAllArguments(3, 4));
    }

    @Test
    public void partition()
    {
        PartitionImmutableList<Integer> partition = this.newListWith(1, 2, 3, 4, 5).partition(Predicates.lessThan(3));
        Assert.assertEquals(iList(1, 2), partition.getSelected());
        Assert.assertEquals(iList(3, 4, 5), partition.getRejected());
    }

    @Test
    public void collect()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3, 4).
                transform(Functions.getToString()).containsAllArguments("1", "2", "3", "4"));
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newListWith(1, 2, 3, 4, 5).find(Predicates.equal(3)));
        Assert.assertNull(this.newListWith(1, 2, 3, 4, 5).find(Predicates.equal(6)));
    }

    @Test
    public void detectIfNoneWithBlock()
    {
        Generator<Integer> function = new Constant<Integer>(6);
        Assert.assertEquals(Integer.valueOf(3), this.newListWith(1, 2, 3, 4, 5).findIfNone(Predicates.equal(3), function));
        Assert.assertEquals(Integer.valueOf(6), this.newListWith(1, 2, 3, 4, 5).findIfNone(Predicates.equal(6), function));
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
    public void count()
    {
        Assert.assertEquals(3, this.newListWith(1, 2, 3).count(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void collectIf()
    {
        Assert.assertTrue(this.newListWith(1, 2, 3).transformIf(
                Predicates.instanceOf(Integer.class),
                Functions.getToString()).containsAllArguments("1", "2", "3"));
    }

    @Test
    public void getFirst()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newListWith(1, 2, 3).getFirst());
        Verify.assertNotEquals(Integer.valueOf(3), this.newListWith(1, 2, 3).getFirst());
    }

    @Test
    public void getLast()
    {
        Verify.assertNotEquals(Integer.valueOf(1), this.newListWith(1, 2, 3).getLast());
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
        Integer result = objects.foldLeft(1, AddFunction.INTEGER);
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
        ImmutableArrayList<Integer> array1 = ImmutableArrayList.newListWith(1, 2, 3, 4);
        ImmutableArrayList<Integer> array2 = ImmutableArrayList.newListWith(1, 2, 3, 4);
        ImmutableArrayList<Integer> array3 = ImmutableArrayList.newListWith(2, 3, 4);
        Verify.assertNotEquals(array1, null);
        Verify.assertEqualsAndHashCode(array1, array1);
        Verify.assertEqualsAndHashCode(array1, array2);
        Verify.assertNotEquals(array2, array3);
        FastList<Integer> fastList = FastList.newList(array1);
        Verify.assertEqualsAndHashCode(array1, fastList);
        Assert.assertEquals(array1, new LinkedList<Integer>(fastList));
        Assert.assertEquals(array1, ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4));
        Verify.assertEqualsAndHashCode(array1, FastList.<Integer>newList().with(1, 2, 3, 4));
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
        Assert.assertEquals(UnifiedMap.<String, String>newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"), map);
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
}
