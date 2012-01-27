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

package ponzu.impl.set.immutable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import ponzu.api.block.function.Function0;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.set.ImmutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.set.UnsortedSetIterable;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.function.AddFunction;
import ponzu.impl.block.function.PassThruFunction0;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.factory.Sets;
import ponzu.impl.list.Interval;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.set.sorted.mutable.TreeSortedSet;
import ponzu.impl.test.SerializeTestHelper;
import ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractImmutableUnifiedSetTestCase
{
    public abstract ImmutableSet<Integer> newSet(Integer... elements);

    public abstract ImmutableSet<Integer> newSetWith(int one, int two);

    public abstract ImmutableSet<Integer> newSetWith(int one, int two, int three);

    public abstract ImmutableSet<Integer> newSetWith(int... littleElements);

    @Test
    public void newList()
    {
        ImmutableSet<Integer> set = this.newSet();
        Assert.assertTrue(set.isEmpty());
        Assert.assertEquals(0, set.size());
    }

    @Test
    public void newWith()
    {
        ImmutableSet<Integer> set = this.newSet(1, 2, 3);
        ImmutableSet<Integer> with = set.newWith(4);
        Verify.assertNotEquals(set, with);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), with);
        Assert.assertSame(set, set.newWith(3));
    }

    @Test
    public void newWithAll()
    {
        ImmutableSet<Integer> set = this.newSet(1, 2, 3);
        ImmutableSet<Integer> withAll = set.newWithAll(UnifiedSet.newSetWith(4, 5));
        Verify.assertNotEquals(set, withAll);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4, 5), withAll);
    }

    @Test
    public void newWithOut()
    {
        ImmutableSet<Integer> set = this.newSet(1, 2, 3, 4);
        ImmutableSet<Integer> without = set.newWithout(4);
        Verify.assertNotEquals(set, without);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3), without);
        Assert.assertSame(set, set.newWithout(5));
    }

    @Test
    public void newWithoutAll()
    {
        ImmutableSet<Integer> set = this.newSet(1, 2, 3, 4, 5);
        ImmutableSet<Integer> withoutAll = set.newWithoutAll(UnifiedSet.newSetWith(4, 5));
        Verify.assertNotEquals(set, withoutAll);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3), withoutAll);
        ImmutableSet<Integer> largeList = this.newSet(Interval.oneTo(200).toArray());
        ImmutableSet<Integer> largeWithoutAll = largeList.newWithoutAll(FastList.newList(Interval.oneTo(100)));
        Assert.assertEquals(UnifiedSet.newSet(Interval.fromTo(101, 200)), largeWithoutAll);
        ImmutableSet<Integer> largeWithoutAll2 = largeWithoutAll.newWithoutAll(Interval.fromTo(101, 150));
        Assert.assertEquals(UnifiedSet.newSet(Interval.fromTo(151, 200)), largeWithoutAll2);
        ImmutableSet<Integer> largeWithoutAll3 = largeWithoutAll2.newWithoutAll(UnifiedSet.newSet(Interval.fromTo(151, 199)));
        Assert.assertEquals(UnifiedSet.newSetWith(200), largeWithoutAll3);
    }

    @Test
    public void newSetWith()
    {
        ImmutableSet<Integer> set = this.newSetWith(1);
        Assert.assertTrue(set.notEmpty());
        Assert.assertEquals(1, set.size());
        Assert.assertTrue(set.contains(1));
    }

    @Test
    public void newListWithVarArgs()
    {
        ImmutableSet<Integer> set = this.newSetWith(1, 2, 3, 4);
        Assert.assertTrue(set.notEmpty());
        Assert.assertEquals(4, set.size());
        Assert.assertTrue(set.containsAllArguments(1, 2, 3, 4));
        Assert.assertTrue(set.containsAllIterable(Interval.oneTo(4)));
    }

    @Test
    public void forEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableSet<Integer> set = this.newSetWith(1, 2, 3, 4);
        set.forEach(CollectionAddProcedure.<Integer>on(result));
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        ImmutableSet<Integer> set = this.newSetWith(1, 2, 3, 4);
        set.forEachWithIndex(new ObjectIntProcedure<Integer>()
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
        Assert.assertTrue(this.newSetWith(1, 2, 3, 4, 5).filter(Predicates.lessThan(3)).containsAllArguments(1, 2));
        Assert.assertFalse(this.newSetWith(-1, 2, 3, 4, 5).filter(Predicates.lessThan(3)).containsAllArguments(3, 4, 5));
    }

    @Test
    public void reject()
    {
        Assert.assertTrue(this.newSetWith(1, 2, 3, 4).filterNot(Predicates.lessThan(3)).containsAllArguments(3, 4));
    }

    @Test
    public void collect()
    {
        Assert.assertTrue(
                this.newSetWith(1, 2, 3, 4).transform(Functions.getToString()).containsAllArguments("1", "2", "3", "4"));
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newSetWith(1, 2, 3, 4, 5).find(Predicates.equal(3)));
        Assert.assertNull(this.newSetWith(1, 2, 3, 4, 5).find(Predicates.equal(6)));
    }

    @Test
    public void detectIfNoneWithBlock()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(6);
        Assert.assertEquals(Integer.valueOf(3), this.newSetWith(1, 2, 3, 4, 5).findIfNone(Predicates.equal(3), function));
        Assert.assertEquals(Integer.valueOf(6), this.newSetWith(1, 2, 3, 4, 5).findIfNone(Predicates.equal(6), function));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.newSetWith(1, 2, 3).allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(this.newSetWith(1, 2, 3).allSatisfy(Predicates.equal(1)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.newSetWith(1, 2, 3).anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(this.newSetWith(1, 2, 3).anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(3, this.newSetWith(1, 2, 3).count(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void collectIf()
    {
        Assert.assertTrue(this.newSetWith(1, 2, 3).transformIf(
                Predicates.instanceOf(Integer.class),
                Functions.getToString()).containsAllArguments("1", "2", "3"));
    }

    @Test
    public void getFirst()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newSetWith(1, 2, 3).getFirst());
        Verify.assertNotEquals(Integer.valueOf(3), this.newSetWith(1, 2, 3).getFirst());
    }

    @Test
    public void getLast()
    {
        Assert.assertNotNull(this.newSetWith(1, 2, 3).getLast());
    }

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(this.newSet().isEmpty());
        Assert.assertTrue(this.newSetWith(1, 2).notEmpty());
    }

    @Test
    public void iterator()
    {
        ImmutableSet<Integer> objects = this.newSetWith(1, 2, 3);
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
        ImmutableSet<Integer> objects = this.newSetWith(1, 2, 3);
        Integer result = objects.foldLeft(1, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(7), result);
    }

    @Test
    public void injectIntoInt()
    {
        ImmutableSet<Integer> objects = this.newSetWith(1, 2, 3);
        int result = objects.foldLeft(1, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(7, result);
    }

    @Test
    public void injectIntoLong()
    {
        ImmutableSet<Integer> objects = this.newSetWith(1, 2, 3);
        long result = objects.foldLeft(1, AddFunction.INTEGER_TO_LONG);
        Assert.assertEquals(7, result);
    }

    @Test
    public void injectIntoDouble()
    {
        ImmutableSet<Integer> objects = this.newSetWith(1, 2, 3);
        double result = objects.foldLeft(1, AddFunction.INTEGER_TO_DOUBLE);
        Assert.assertEquals(7.0d, result, 0.001);
    }

    @Test
    public void toArray()
    {
        ImmutableSet<Integer> objects = this.newSetWith(1, 2, 3);
        Object[] array = objects.toArray();
        Verify.assertSize(3, array);
        Integer[] array2 = objects.toArray(new Integer[3]);
        Verify.assertSize(3, array2);
    }

    @Test
    public void equalsAndHashCode()
    {
        ImmutableSet<Integer> set1 = this.newSetWith(1, 2, 3, 4);
        ImmutableSet<Integer> set2 = this.newSetWith(1, 2, 3, 4);
        ImmutableSet<Integer> set3 = this.newSetWith(2, 3, 4);
        Verify.assertNotEquals(set1, null);
        Verify.assertEqualsAndHashCode(set1, set1);
        Verify.assertEqualsAndHashCode(set1, set2);
        Verify.assertNotEquals(set2, set3);
        UnifiedSet<Integer> fastSet = UnifiedSet.newSet(set1);
        Verify.assertEqualsAndHashCode(set1, fastSet);
        Assert.assertEquals(set1, new HashSet<Integer>(fastSet));
        Verify.assertEqualsAndHashCode(set1, UnifiedSet.<Integer>newSetWith(1, 2, 3, 4));
    }

    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        ImmutableSet<Integer> collection = this.newSetWith(1, 2, 3, 4);
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
        ImmutableSet<Integer> integers = this.newSetWith(1, 2, 3, 4);
        MutableList<Integer> list = integers.toList();
        Assert.assertTrue(list.containsAllArguments(1, 2, 3, 4));
    }

    @Test
    public void toSortedList()
    {
        ImmutableSet<Integer> integers = this.newSetWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(FastList.newListWith(4, 3, 2, 1), list);
    }

    @Test
    public void toSortedListBy()
    {
        ImmutableSet<Integer> integers = this.newSetWith(2, 4, 1, 3);
        MutableList<Integer> list = integers.toSortedListBy(Functions.getToString());
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), list);
    }

    @Test
    public void asSortedSet()
    {
        ImmutableSet<Integer> integers = this.newSetWith(2, 3, 1, 4);
        MutableSortedSet<Integer> set = integers.toSortedSet();
        Verify.assertSortedSetsEqual(TreeSortedSet.newSet(integers), set);
    }

    @Test
    public void toSortedSet_with_comparator()
    {
        ImmutableSet<Integer> integers = this.newSetWith(2, 4, 1, 3);
        MutableSortedSet<Integer> set = integers.toSortedSet(Collections.<Integer>reverseOrder());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<Integer>reverseOrder(), 1, 2, 3, 4), set);
    }

    @Test
    public void toSortedSetBy()
    {
        ImmutableSet<Integer> integers = this.newSetWith(4, 1, 3, 2);
        MutableSortedSet<Integer> set = integers.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 3, 4), set);
    }

    @Test
    public void toSet()
    {
        ImmutableSet<Integer> integers = this.newSetWith(1, 2, 3, 4);
        MutableSet<Integer> set = integers.toSet();
        Verify.assertContainsAll(set, 1, 2, 3, 4);
    }

    @Test
    public void toMap()
    {
        ImmutableSet<Integer> integers = this.newSetWith(1, 2, 3, 4);
        MutableMap<String, String> map =
                integers.toMap(Functions.getToString(), Functions.getToString());
        Assert.assertEquals(UnifiedMap.<String, String>newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"), map);
    }

    @Test
    public void serialization()
    {
        ImmutableSet<Integer> set = this.newSetWith(1, 2, 3, 4, 5);
        ImmutableSet<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(set);
        Assert.assertEquals(5, deserializedCollection.size());
        Assert.assertTrue(deserializedCollection.containsAllArguments(1, 2, 3, 4, 5));
        Verify.assertEqualsAndHashCode(set, deserializedCollection);
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[1]", this.newSetWith(1).toString());
    }

    @Test
    public void powerSet()
    {
        ImmutableSet<Integer> set = this.newSetWith(1, 2, 3);
        ImmutableSet<UnsortedSetIterable<Integer>> powerSet = set.powerSet();
        Verify.assertSize(8, powerSet);
        Verify.assertContains(set, powerSet);
        Verify.assertContains(Sets.immutable.<Integer>of(), powerSet);
        Verify.assertInstanceOf(ImmutableSet.class, powerSet.getFirst());
        Verify.assertInstanceOf(ImmutableSet.class, powerSet.getLast());
    }
}
