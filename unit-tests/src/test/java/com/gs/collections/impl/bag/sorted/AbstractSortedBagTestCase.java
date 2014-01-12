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

package com.gs.collections.impl.bag.sorted;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.bag.mutable.sorted.TreeBag;
import com.gs.collections.impl.bag.mutable.sorted.UnmodifiableSortedBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.StringFunctions;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.collection.mutable.AbstractCollectionTestCase;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.merge.Person;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link MutableSortedBag}s.
 *
 * @since 4.2
 */
public abstract class AbstractSortedBagTestCase extends AbstractCollectionTestCase
{
    @Override
    protected abstract <T> MutableSortedBag<T> classUnderTest();

    protected abstract <T> MutableSortedBag<T> classUnderTest(T... elements);

    protected abstract <T> MutableSortedBag<T> classUnderTest(Comparator<? super T> comparator);

    protected abstract <T> MutableSortedBag<T> classUnderTest(Comparator<? super T> comparator, T... elements);

    @Override
    protected <T> MutableSortedBag<T> newWith(T one)
    {
        return (MutableSortedBag<T>) super.newWith(one);
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(T one, T two)
    {
        return (MutableSortedBag<T>) super.newWith(one, two);
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(T one, T two, T three)
    {
        return (MutableSortedBag<T>) super.newWith(one, two, three);
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(T... littleElements)
    {
        return (MutableSortedBag<T>) super.newWith(littleElements);
    }

    protected <T> MutableSortedBag<T> newWith(Comparator<? super T> comparator, T... littleElements)
    {
        return this.classUnderTest(comparator, littleElements);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void toImmutable()
    {
        //not yet supported
        this.classUnderTest().toImmutable();
    }

    @Test(expected = ClassCastException.class)
    public void testToString_with_collection_containing_self()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1);
        collection.add(collection);
        String simpleName = collection.getClass().getSimpleName();
        String string = collection.toString();
        Assert.assertTrue(
                ("[1, (this " + simpleName + ")]").equals(string)
                        || ("[(this " + simpleName + "), 1]").equals(string));
    }

    @Test(expected = ClassCastException.class)
    public void makeString_with_collection_containing_self()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        collection.add(collection);
        Assert.assertEquals(collection.toString(), '[' + collection.makeString() + ']');
    }

    @Test(expected = ClassCastException.class)
    public void appendString_with_collection_containing_self()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        collection.add(collection);
        Appendable builder = new StringBuilder();
        collection.appendString(builder);
        Assert.assertEquals(collection.toString(), '[' + builder.toString() + ']');
    }

    @Override
    @Test
    public void addAll()
    {
        super.addAll();

        TreeBag<Integer> expected = TreeBag.newBag(Comparators.reverseNaturalOrder(), FastList.newListWith(1, 1, 2, 3));
        MutableSortedBag<Integer> collection = this.classUnderTest(Comparators.reverseNaturalOrder());

        Assert.assertTrue(collection.addAll(FastList.newListWith(3, 2, 1, 1)));
        Verify.assertSortedBagsEqual(expected, collection);
    }

    @Override
    @Test
    public void addAllIterable()
    {
        super.addAllIterable();

        TreeBag<Integer> expected = TreeBag.newBag(Collections.reverseOrder(), FastList.<Integer>newListWith(2, 1, 2, 3));
        MutableSortedBag<Integer> collection = this.classUnderTest(Collections.reverseOrder());

        Assert.assertTrue(collection.addAllIterable(FastList.newListWith(3, 2, 1, 2)));
        Verify.assertSortedBagsEqual(expected, collection);
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();

        Assert.assertEquals("[2, 1, 1]", this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2).toString());
        Assert.assertEquals("[3, 2, 1, 1]", this.newWith(Collections.reverseOrder(), 3, 1, 1, 2).toString());
        Assert.assertEquals("[-1, 2, 3]", this.newWith(3, -1, 2).toString());
    }

    @Override
    @Test
    public void makeString()
    {
        super.makeString();

        Assert.assertEquals("3, 3, 2", this.newWith(Comparators.reverseNaturalOrder(), 3, 2, 3).makeString());
    }

    @Override
    public void makeStringWithSeparator()
    {
        super.makeStringWithSeparator();

        Assert.assertEquals("3!2!-3", this.newWith(Comparators.reverseNaturalOrder(), 3, 2, -3).makeString("!"));
    }

    @Override
    public void makeStringWithSeparatorAndStartAndEnd()
    {
        super.makeStringWithSeparatorAndStartAndEnd();

        Assert.assertEquals("<1,2,3>", this.newWith(1, 2, 3).makeString("<", ",", ">"));
    }

    @Override
    @Test
    public void appendString()
    {
        super.appendString();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 5, 5, 1, 2, 3);
        Appendable builder = new StringBuilder();
        bag.appendString(builder);
        Assert.assertEquals(bag.toString(), "[" + builder.toString() + "]");
        Assert.assertEquals("5, 5, 3, 2, 1", builder.toString());
    }

    @Override
    public void appendStringWithSeparator()
    {
        super.appendStringWithSeparator();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 5, 5, 1, 2, 3);
        Appendable builder = new StringBuilder();
        bag.appendString(builder, ", ");
        Assert.assertEquals(bag.toString(), "[" + builder.toString() + "]");
        Assert.assertEquals("5, 5, 3, 2, 1", builder.toString());
    }

    @Override
    public void appendStringWithSeparatorAndStartAndEnd()
    {
        super.appendStringWithSeparatorAndStartAndEnd();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 5, 5, 1, 2, 3);
        Appendable builder = new StringBuilder();
        bag.appendString(builder, "[", ", ", "]");
        Assert.assertEquals(bag.toString(), builder.toString());
        Assert.assertEquals("[5, 5, 3, 2, 1]", builder.toString());
    }

    @Override
    @Test
    public void remove()
    {
        MutableSortedBag<String> bag = this.newWith(Collections.reverseOrder(), "5", "1", "2", "2", "3");
        Assert.assertFalse(bag.remove("7"));
        Assert.assertTrue(bag.remove("1"));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.reverseOrder(), "5", "3", "2", "2"), bag);
        Assert.assertTrue(bag.remove("2"));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.reverseOrder(), "5", "3", "2"), bag);
    }

    @Test
    public void removeIf()
    {
        MutableSortedBag<Integer> objects = this.newWith(Comparators.reverseNaturalOrder(), 4, 1, 3, 3, 2);
        objects.removeIf(Predicates.equal(2));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 3, 3, 4), objects);
        objects.removeIf(Predicates.equal(3));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 4), objects);
    }

    @Override
    @Test
    public void removeIfWith()
    {
        super.removeIfWith();

        MutableSortedBag<Integer> objects = this.newWith(Comparators.reverseNaturalOrder(), 4, 1, 3, 3, 2);
        objects.removeIfWith(Predicates2.equal(), 2);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 3, 3, 4), objects);
        objects.removeIfWith(Predicates2.equal(), 3);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 4), objects);
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        MutableSortedBag<Integer> sortedBag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4);
        Verify.assertPostSerializedEqualsAndHashCode(sortedBag);

        Verify.assertEqualsAndHashCode(HashBag.newBag(sortedBag), sortedBag);

        Assert.assertNotEquals(HashBag.newBagWith(1, 1, 1, 2, 3, 4), sortedBag);
        Assert.assertNotEquals(HashBag.newBagWith(1, 1, 2, 3), sortedBag);
        Assert.assertNotEquals(HashBag.newBagWith(1, 2, 3, 4), sortedBag);

        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4), sortedBag);
    }

    @Override
    @Test
    public void select()
    {
        super.select();
        MutableSortedBag<Integer> integers = this.newWith(3, 3, 2, 1, 4, 5);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(3, 3, 2, 1), integers.select(Predicates.lessThanOrEqualTo(3)));
        Verify.assertEmpty(integers.select(Predicates.greaterThan(6)));
        MutableSortedBag<Integer> revInt = this.classUnderTest(Collections.<Integer>reverseOrder(), 1, 2, 4, 3, 3, 5);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.<Integer>reverseOrder(), 3, 3, 2, 1), revInt.select(Predicates.lessThan(4)));
    }

    @Override
    @Test
    public void selectWith()
    {
        super.selectWith();
        Verify.assertSortedBagsEqual(
                TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 3),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 3, 1, 2, 5, 3, 6, 6).selectWith(Predicates2.<Integer>lessThan(), 4));
    }

    @Override
    @Test
    public void selectWithTarget()
    {
        super.selectWithTarget();
        Verify.assertSortedBagsEqual(
                TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 3),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 3, 1, 2, 5, 3, 6, 6)
                        .selectWith(
                                Predicates2.<Integer>lessThan(),
                                4,
                                TreeBag.newBag(Comparators.<Integer>reverseNaturalOrder())));
    }

    @Override
    @Test
    public void reject()
    {
        super.reject();
        MutableSortedBag<Integer> integers = this.newWith(4, 4, 2, 1, 3, 5, 6, 6);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(1, 2, 3, 4, 4), integers.reject(Predicates.greaterThan(4)));
        Verify.assertEmpty(integers.reject(Predicates.greaterThan(0)));
        MutableSortedBag<Integer> revInt = this.classUnderTest(Collections.<Integer>reverseOrder(), 1, 2, 2, 4, 3, 5);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.<Integer>reverseOrder(), 3, 2, 2, 1), revInt.reject(Predicates.greaterThan(3)));
    }

    @Override
    @Test
    public void rejectWith()
    {
        super.rejectWith();
        Verify.assertSortedBagsEqual(
                TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 5, 4, 5)
                        .rejectWith(Predicates2.<Integer>greaterThan(), 3));
    }

    @Override
    @Test
    public void rejectWithTarget()
    {
        super.rejectWithTarget();
        Verify.assertSortedBagsEqual(
                TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 5, 4, 5)
                        .rejectWith(Predicates2.<Integer>greaterThan(), 3, TreeBag.newBag(Comparators.<Integer>reverseNaturalOrder())));
    }

    @Override
    @Test
    public void partition()
    {
        super.partition();

        MutableSortedBag<Integer> integers = this.newWith(Comparators.reverseNaturalOrder(), 1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        PartitionMutableSortedBag<Integer> result = integers.partition(IntegerPredicates.isEven());
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 2, 2, 4, 4, 4, 4), result.getSelected());
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 3, 3, 3), result.getRejected());
    }

    @Override
    @Test
    public void selectAndRejectWith()
    {
        super.selectAndRejectWith();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 1, 1, 1, 1, 2);
        Twin<MutableList<Integer>> result = bag.selectAndRejectWith(Predicates2.equal(), 1);
        Verify.assertSize(4, result.getOne());
        Verify.assertSize(1, result.getTwo());
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();

        MutableSortedBag<Integer> integers = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 4, 3, 1, 1);
        MutableBag<Holder> holders = integers.collect(Holder.FROM_INT);
        Assert.assertEquals(HashBag.<Holder>newBagWith(new Holder(4), new Holder(3), new Holder(1), new Holder(1)), holders);
    }

    @Override
    @Test
    public void flatCollect()
    {
        super.flatCollect();
        MutableSortedBag<Integer> integers = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 4, 3, 1, 1);
        MutableBag<Holder> holders = integers.flatCollect(Holder.FROM_LIST);
        Assert.assertEquals(HashBag.newBagWith(new Holder(4), new Holder(4), new Holder(3), new Holder(3), new Holder(1), new Holder(1), new Holder(1), new Holder(1)), holders);
    }

    @Override
    @Test
    public void collectIf()
    {
        super.collectIf();

        Assert.assertEquals(
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3, 4, 5, 5).collectIf(
                        Predicates.lessThan(4),
                        Holder.FROM_INT),
                HashBag.newBagWith(new Holder(1), new Holder(1), new Holder(1), new Holder(2), new Holder(3)));
    }

    @Override
    @Test
    public void collectWith()
    {
        super.collectWith();
        MutableSortedBag<Integer> integers = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 4, 3, 1, 1);
        MutableBag<Holder> holders = integers.collectWith(Holder.FROM_INT_INT, 10);
        Assert.assertEquals(HashBag.<Holder>newBagWith(new Holder(14), new Holder(13), new Holder(11), new Holder(11)), holders);
    }

    @Override
    @Test
    public void collectWithTarget()
    {
        super.collectWith();
        MutableSortedBag<Integer> integers = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 4, 3, 1, 1);
        MutableBag<Holder> holders = integers.collectWith(Holder.FROM_INT_INT, 10, TreeBag.<Holder>newBag(Functions.toIntComparator(Holder.TO_NUMBER)));
        Assert.assertEquals(TreeBag.<Holder>newBagWith(Functions.toIntComparator(Holder.TO_NUMBER), new Holder(14), new Holder(13), new Holder(11), new Holder(11)), holders);
    }

    @Override
    @Test
    public void groupBy()
    {
        super.groupBy();
        MutableSortedBag<Integer> integers = this.classUnderTest(Collections.<Integer>reverseOrder(), 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };
        MutableSortedBagMultimap<Boolean, Integer> map = integers.groupBy(isOddFunction);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.<Integer>reverseOrder(), 9, 7, 5, 3, 1, 1, 1), map.get(Boolean.TRUE));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.<Integer>reverseOrder(), 8, 6, 4, 2), map.get(Boolean.FALSE));
        Verify.assertSize(2, map.keysView().toList());
    }

    @Override
    @Test
    public void groupByEach()
    {
        super.groupByEach();
        MutableSortedBag<Integer> bag = this.classUnderTest(Collections.<Integer>reverseOrder(), 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        NegativeIntervalFunction function = new NegativeIntervalFunction();
        MutableSortedBagMultimap<Integer, Integer> expected =
                this.<Integer>classUnderTest(Collections.<Integer>reverseOrder()).groupByEach(function);

        for (int i = 1; i < 10; i++)
        {
            expected.putAll(-i, Interval.fromTo(i, 9));
        }

        expected.put(-1, 1);
        expected.put(-1, 1);

        MutableSortedBagMultimap<Integer, Integer> actual = bag.groupByEach(function);
        Assert.assertEquals(expected, actual);
        MutableSortedBagMultimap<Integer, Integer> actualWithTarget =
                bag.groupByEach(function, this.<Integer>classUnderTest().groupByEach(function));
        Assert.assertEquals(expected, actualWithTarget);
        for (int i = 1; i < 10; ++i)
        {
            Verify.assertSortedBagsEqual(expected.get(-i), actual.get(-i));
        }
        Verify.assertSize(9, actual.keysView().toList());
        Verify.assertSize(9, actualWithTarget.keysView().toList());
    }

    @Override
    @Test
    public void zip()
    {
        super.zip();
        MutableSortedBag<Integer> revInt = this.classUnderTest(Collections.<Integer>reverseOrder(), 2, 2, 3, 5, 1, 4);
        MutableSortedBag<Integer> integers = this.newWith(1, 3, 2, 2, 4, 5);
        MutableBag<Pair<Integer, Integer>> zip = integers.zip(revInt);
        Verify.assertSize(6, zip);

        Assert.assertEquals(
                HashBag.<Pair<Integer, Integer>>newBagWith(
                        Tuples.pair(1, 5), Tuples.pair(2, 4), Tuples.pair(2, 3), Tuples.pair(3, 2), Tuples.pair(4, 2), Tuples.pair(5, 1)),
                zip);

        MutableBag<Pair<Integer, Integer>> revZip = revInt.zip(integers);
        Verify.assertSize(6, revZip);

        Assert.assertEquals(
                HashBag.<Pair<Integer, Integer>>newBagWith(
                        Tuples.pair(5, 1), Tuples.pair(4, 2), Tuples.pair(3, 2), Tuples.pair(2, 3), Tuples.pair(2, 4), Tuples.pair(1, 5)),
                revZip);

        Person john = new Person("John", "Smith");
        Person johnDoe = new Person("John", "Doe");
        MutableSortedBag<Person> people = this.newWith(john, johnDoe);
        MutableList<Holder> list = FastList.newListWith(new Holder(1), new Holder(2), new Holder(3));
        MutableBag<Pair<Person, Holder>> pairs = people.zip(list);
        Assert.assertEquals(
                HashBag.newBagWith(Tuples.pair(johnDoe, new Holder(1)), Tuples.pair(john, new Holder(2))),
                pairs);
        Assert.assertTrue(pairs.add(Tuples.pair(new Person("Jack", "Baker"), new Holder(3))));
        Assert.assertEquals(Tuples.pair(new Person("Jack", "Baker"), new Holder(3)), pairs.getFirst());
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        super.zipWithIndex();
        MutableSortedBag<Integer> integers = this.classUnderTest(Collections.<Integer>reverseOrder(), 1, 3, 5, 5, 5, 2, 4);
        Assert.assertEquals(HashBag.newBagWith(
                Tuples.pair(5, 0),
                Tuples.pair(5, 1),
                Tuples.pair(5, 2),
                Tuples.pair(4, 3),
                Tuples.pair(3, 4),
                Tuples.pair(2, 5),
                Tuples.pair(1, 6)),
                integers.zipWithIndex());
    }

    @Override
    public void selectInstancesOf()
    {
        MutableSortedBag<Number> numbers = this.<Number>classUnderTest(new Comparator<Number>()
        {
            public int compare(Number o1, Number o2)
            {
                return Double.compare(o2.doubleValue(), o1.doubleValue());
            }
        }, 5, 4.0, 3, 2.0, 1, 1);
        MutableSortedBag<Integer> integers = numbers.selectInstancesOf(Integer.class);
        Verify.assertSortedBagsEqual(TreeBag.<Integer>newBagWith(Comparators.<Integer>reverseNaturalOrder(), 5, 3, 1, 1), integers);
    }

    @Test
    public void toStringOfItemToCount()
    {
        Assert.assertEquals("{}", this.newWith().toStringOfItemToCount());
        Assert.assertEquals("{}", this.classUnderTest(Comparators.reverseNaturalOrder()).toStringOfItemToCount());

        Assert.assertEquals("{1=1, 2=2}", this.newWith(1, 2, 2).toStringOfItemToCount());
        Assert.assertEquals("{2=2, 1=1}", this.newWith(Comparators.reverseNaturalOrder(), 1, 2, 2).toStringOfItemToCount());
    }

    @Test
    public void add()
    {
        MutableSortedBag<Integer> bag = this.classUnderTest(Collections.<Integer>reverseOrder());
        Assert.assertTrue(bag.add(1));
        Verify.assertSortedBagsEqual(this.newWith(1), bag);
        Assert.assertTrue(bag.add(3));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.<Integer>reverseOrder(), 3, 1), bag);
        Verify.assertSize(2, bag);
        Assert.assertTrue(bag.add(2));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.<Integer>reverseOrder(), 3, 2, 1), bag);
        Verify.assertSize(3, bag);
        Assert.assertTrue(bag.add(2));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.<Integer>reverseOrder(), 3, 2, 2, 1), bag);
        Verify.assertSize(4, bag);
    }

    @Override
    @Test
    public void iterator()
    {
        MutableSortedBag<Integer> bag = this.newWith(-1, 0, 1, 1, 2);
        Iterator<Integer> iterator = bag.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(-1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(0), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(2), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        MutableSortedBag<Integer> revBag = this.newWith(Comparators.reverseNaturalOrder(), -1, 0, 1, 1, 2);
        Iterator<Integer> revIterator = revBag.iterator();
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(2), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(0), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(-1), revIterator.next());
        Assert.assertFalse(revIterator.hasNext());

        MutableSortedBag<Integer> sortedBag = this.newWith(Collections.reverseOrder(), 1, 1, 1, 1, 2);
        MutableList<Integer> validate = Lists.mutable.of();
        for (Integer each : sortedBag)
        {
            validate.add(each);
        }
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.reverseOrder(), 1, 1, 1, 1, 2), TreeBag.newBag(Collections.reverseOrder(), validate));

        final Iterator<Integer> sortedBagIterator = sortedBag.iterator();
        MutableSortedBag<Integer> expected = this.newWith(Collections.reverseOrder(), 1, 1, 1, 1, 2);
        Verify.assertThrows(IllegalStateException.class, new Runnable()
        {
            public void run()
            {
                sortedBagIterator.remove();
            }
        });

        this.assertIteratorRemove(sortedBag, sortedBagIterator, expected);
        this.assertIteratorRemove(sortedBag, sortedBagIterator, expected);
        this.assertIteratorRemove(sortedBag, sortedBagIterator, expected);
        this.assertIteratorRemove(sortedBag, sortedBagIterator, expected);
        this.assertIteratorRemove(sortedBag, sortedBagIterator, expected);
        Verify.assertEmpty(sortedBag);
        Assert.assertFalse(sortedBagIterator.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                sortedBagIterator.next();
            }
        });
    }

    private void assertIteratorRemove(MutableSortedBag<Integer> bag, final Iterator<Integer> iterator, MutableSortedBag<Integer> expected)
    {
        Assert.assertTrue(iterator.hasNext());
        Integer first = iterator.next();
        iterator.remove();
        expected.remove(first);
        Verify.assertSortedBagsEqual(expected, bag);
        Verify.assertThrows(IllegalStateException.class, new Runnable()
        {
            public void run()
            {
                iterator.remove();
            }
        });
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void iterator_throws()
    {
        super.iterator_throws();

        MutableSortedBag<Integer> revBag = this.newWith(Comparators.reverseNaturalOrder(), -1, 0, 1, 1, 2);
        Iterator<Integer> revIterator = revBag.iterator();
        Assert.assertTrue(revIterator.hasNext());
        revIterator.next();
        Assert.assertTrue(revIterator.hasNext());
        revIterator.next();
        Assert.assertTrue(revIterator.hasNext());
        revIterator.next();
        Assert.assertTrue(revIterator.hasNext());
        revIterator.next();
        Assert.assertTrue(revIterator.hasNext());
        revIterator.next();
        Assert.assertFalse(revIterator.hasNext());
        revIterator.next();
    }

    @Override
    @Test
    public void forEach()
    {
        super.forEach();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 1, 1, 2);
        final MutableList<Integer> actual = FastList.newList();
        bag.forEach(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                actual.add(each);
            }
        });
        Assert.assertEquals(FastList.newListWith(2, 1, 1), actual);
    }

    @Test
    public void forEachWithOccurrences()
    {
        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 3, 3, 3, 2, 2, 1);
        final MutableList<Integer> actualItems = FastList.newList();
        final MutableList<Integer> actualIndexes = FastList.newList();
        bag.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                actualItems.add(each);
                actualIndexes.add(index);
            }
        });
        Assert.assertEquals(FastList.newListWith(3, 2, 1), actualItems);
        Assert.assertEquals(FastList.newListWith(3, 2, 1), actualIndexes);

        MutableSortedBag<Integer> bag2 = this.classUnderTest();
        bag2.addOccurrences(1, 10);
        bag2.addOccurrences(2, 10);
        bag2.addOccurrences(3, 10);
        final IntegerSum sum = new IntegerSum(0);
        final Counter counter = new Counter();
        bag2.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int occurrences)
            {
                counter.increment();
                sum.add(each * occurrences * counter.getCount());
            }
        });
        Assert.assertEquals(140, sum.getIntSum());
        bag2.removeOccurrences(2, 1);
        final IntegerSum sum2 = new IntegerSum(0);
        bag2.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int occurrences)
            {
                sum2.add(each * occurrences);
            }
        });
        Assert.assertEquals(58, sum2.getIntSum());
        bag2.removeOccurrences(1, 3);
        final IntegerSum sum3 = new IntegerSum(0);
        bag2.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int occurrences)
            {
                sum3.add(each * occurrences);
            }
        });
        Assert.assertEquals(55, sum3.getIntSum());
    }

    @Test
    @Override
    public void getFirst()
    {
        super.getFirst();

        Assert.assertEquals(Integer.valueOf(0), this.newWith(0, 0, 1, 1).getFirst());
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1, 1, 2, 3).getFirst());
        Assert.assertEquals(Integer.valueOf(1), this.newWith(2, 1, 3, 2, 3).getFirst());
        Assert.assertEquals(Integer.valueOf(3), this.newWith(Collections.reverseOrder(), 2, 2, 1, 3).getFirst());
    }

    @Test
    @Override
    public void getLast()
    {
        super.getLast();

        Assert.assertEquals(Integer.valueOf(1), this.newWith(0, 0, 1, 1).getLast());
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 1, 2, 3).getLast());
        Assert.assertEquals(Integer.valueOf(3), this.newWith(3, 2, 3, 2, 3).getLast());
        Assert.assertEquals(Integer.valueOf(1), this.newWith(Collections.reverseOrder(), 2, 2, 1, 3).getLast());
    }

    @Test
    public void occurrencesOf()
    {
        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 1, 1, 2);
        Assert.assertEquals(2, bag.occurrencesOf(1));
        Assert.assertEquals(1, bag.occurrencesOf(2));
    }

    @Test
    public void addOccurrences()
    {
        MutableSortedBag<Integer> bag = this.classUnderTest();
        bag.addOccurrences(0, 3);
        bag.addOccurrences(2, 0);
        bag.addOccurrences(1, 2);
        Verify.assertSortedBagsEqual(TreeBag.<Integer>newBagWith(0, 0, 0, 1, 1), bag);
        bag.addOccurrences(0, 3);
        bag.addOccurrences(1, 2);
        Verify.assertSortedBagsEqual(TreeBag.<Integer>newBagWith(0, 0, 0, 0, 0, 0, 1, 1, 1, 1), bag);

        MutableSortedBag<Integer> revBag = this.classUnderTest(Collections.reverseOrder());
        revBag.addOccurrences(2, 3);
        revBag.addOccurrences(3, 2);
        Verify.assertSortedBagsEqual(TreeBag.<Integer>newBagWith(Collections.reverseOrder(), 3, 3, 2, 2, 2), revBag);
        revBag.addOccurrences(2, 3);
        revBag.addOccurrences(3, 2);
        Verify.assertSortedBagsEqual(TreeBag.<Integer>newBagWith(Collections.reverseOrder(), 3, 3, 3, 3, 2, 2, 2, 2, 2, 2), revBag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOccurrences_throws()
    {
        this.classUnderTest().addOccurrences(new Object(), -1);
    }

    @Test
    public void removeOccurrences()
    {
        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 2, 2, 1);
        MutableSortedBag<Integer> expected = TreeBag.newBag(bag);

        Assert.assertFalse(bag.removeOccurrences(4, 2));
        Assert.assertFalse(bag.removeOccurrences(4, 0));
        Assert.assertFalse(bag.removeOccurrences(2, 0));
        Verify.assertSortedBagsEqual(expected, bag);

        Assert.assertTrue(bag.removeOccurrences(2, 2));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(1), bag);

        Assert.assertTrue(bag.removeOccurrences(1, 100));
        Verify.assertSortedBagsEqual(TreeBag.<String>newBag(), bag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeOccurrences_throws()
    {
        this.classUnderTest().removeOccurrences(new Object(), -1);
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 3, 3, 2);
        Assert.assertEquals(FastList.newListWith(3, 3, 2, 1), bag.toList());
    }

    @Override
    @Test
    public void toSet()
    {
        super.toSet();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 3, 3, 3, 2, 2, 1);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3), bag.toSet());
    }

    @Override
    @Test
    public void toBag()
    {
        super.toBag();

        Assert.assertEquals(Bags.mutable.of("C", "C", "B", "A"),
                this.newWith(Comparators.reverseNaturalOrder(), "C", "C", "B", "A").toBag());
    }

    @Override
    @Test
    public void toSortedList_natural_ordering()
    {
        super.toSortedList_natural_ordering();

        Assert.assertEquals(
                FastList.newListWith(1, 2, 3, 4, 4),
                this.newWith(Comparators.reverseNaturalOrder(), 4, 4, 3, 1, 2).toSortedList());
    }

    @Override
    @Test
    public void toSortedList_with_comparator()
    {
        super.toSortedList_with_comparator();

        Assert.assertEquals(
                FastList.newListWith(4, 4, 3, 2, 1),
                this.newWith(Comparators.reverseNaturalOrder(), 4, 4, 3, 1, 2).toSortedList(Comparators.reverseNaturalOrder()));
    }

    @Override
    @Test
    public void toSortedListBy()
    {
        super.toSortedListBy();

        MutableSortedBag<Integer> sortedBag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 10, 2, 3);
        MutableList<Integer> sortedListBy = sortedBag.toSortedListBy(Functions.getToString());
        Assert.assertEquals(FastList.newListWith(1, 1, 1, 10, 2, 3), sortedListBy);
    }

    @Override
    @Test
    public void toSortedSet_natural_ordering()
    {
        super.toSortedSet_natural_ordering();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 2, 2, 1, 5, 4);
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(1, 2, 4, 5), bag.toSortedSet());
    }

    @Override
    @Test
    public void toSortedSet_with_comparator()
    {
        super.toSortedSet_with_comparator();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 2, 2, 1, 5, 4);
        Verify.assertSortedSetsEqual(
                TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder(), 5, 4, 2, 1),
                bag.toSortedSet(Comparators.reverseNaturalOrder()));
    }

    @Override
    @Test
    public void toSortedSetBy()
    {
        super.toSortedSetBy();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 2, 5, 2, 4, 3, 1, 6, 7, 8, 9, 10);
        Assert.assertEquals(
                UnifiedSet.newSetWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                bag.toSortedSetBy(Functions.getToString()));
        Assert.assertEquals(
                FastList.newListWith(1, 10, 2, 3, 4, 5, 6, 7, 8, 9),
                bag.toSortedSetBy(Functions.getToString()).toList());
    }

    @Override
    @Test
    public void toMap()
    {
        super.toMap();

        Assert.assertEquals(
                UnifiedMap.<String, String>newWithKeysValues("4", "4", "3", "3", "2", "2", "1", "1"),
                this.newWith(Comparators.reverseNaturalOrder(), 4, 3, 2, 1).toMap(Functions.getToString(), Functions.getToString()));
    }

    @Override
    @Test
    public void toSortedMap()
    {
        super.toSortedMap();

        Verify.assertSortedMapsEqual(
                TreeSortedMap.newMapWith(3, "3", 2, "2", 1, "1"),
                this.newWith(3, 2, 1).toSortedMap(Functions.getIntegerPassThru(), Functions.getToString()));
    }

    @Override
    @Test
    public void toSortedMap_with_comparator()
    {
        super.toSortedMap_with_comparator();

        Verify.assertSortedMapsEqual(
                TreeSortedMap.newMapWith(Comparators.<Integer>reverseNaturalOrder(), 3, "3", 2, "2", 1, "1"),
                this.newWith(3, 2, 1, 1).toSortedMap(
                        Comparators.<Integer>reverseNaturalOrder(),
                        Functions.getIntegerPassThru(),
                        Functions.getToString()));
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableSortedBag.class, this.classUnderTest().asUnmodifiable());
        Verify.assertSortedBagsEqual(this.classUnderTest(), this.classUnderTest().asUnmodifiable());
    }

    @Test
    public void serialization()
    {
        MutableSortedBag<String> bag = this.newWith(Comparators.reverseNaturalOrder(), "One", "Two", "Two", "Three", "Three", "Three");
        Verify.assertPostSerializedEqualsAndHashCode(bag);
    }

    @Test
    public void selectByOccurrences()
    {
        MutableSortedBag<Integer> integers = this.newWith(Collections.reverseOrder(), 4, 3, 3, 2, 2, 2, 1, 1, 1, 1);
        Verify.assertSortedBagsEqual(
                TreeBag.newBagWith(Collections.reverseOrder(), 3, 3, 1, 1, 1, 1),
                integers.selectByOccurrences(IntPredicates.isEven()));
    }

    @Test
    public void toMapOfItemToCount()
    {
        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 1, 2, 2, 3, 3, 3);
        Assert.assertEquals(TreeSortedMap.newMapWith(Collections.reverseOrder(), 1, 1, 2, 2, 3, 3), bag.toMapOfItemToCount());
    }

    @Test
    public void compareTo()
    {
        Assert.assertEquals(-1, this.newWith(1, 1, 2, 2).compareTo(this.newWith(1, 1, 2, 2, 2)));
        Assert.assertEquals(0, this.newWith(1, 1, 2, 2).compareTo(this.newWith(1, 1, 2, 2)));
        Assert.assertEquals(1, this.newWith(1, 1, 2, 2, 2).compareTo(this.newWith(1, 1, 2, 2)));

        Assert.assertEquals(-1, this.newWith(1, 1, 2, 2).compareTo(this.newWith(1, 1, 3, 3)));
        Assert.assertEquals(1, this.newWith(1, 1, 3, 3).compareTo(this.newWith(1, 1, 2, 2)));

        Assert.assertEquals(1, this.newWith(Comparators.reverseNaturalOrder(), 2, 2, 1, 1, 1).compareTo(this.newWith(Comparators.reverseNaturalOrder(), 2, 2, 1, 1)));
        Assert.assertEquals(1, this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2).compareTo(this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2, 2)));
        Assert.assertEquals(0, this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2).compareTo(this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2)));
        Assert.assertEquals(-1, this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2, 2).compareTo(this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2)));

        Assert.assertEquals(1, this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2).compareTo(this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 3, 3)));
        Assert.assertEquals(-1, this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 3, 3).compareTo(this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 2)));
    }

    @Override
    @Test
    public void containsAllIterable()
    {
        super.containsAllIterable();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4);
        Assert.assertTrue(bag.containsAllIterable(FastList.newListWith(1, 2)));
        Assert.assertFalse(bag.containsAllIterable(FastList.newListWith(1, 5)));
        Assert.assertTrue(bag.containsAllIterable(FastList.newListWith()));
    }

    @Override
    @Test
    public void containsAllArray()
    {
        super.containsAllArray();
        MutableSortedBag<Integer> collection = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3, 4);
        Assert.assertTrue(collection.containsAllArguments(1, 2));
        Assert.assertFalse(collection.containsAllArguments(1, 5));
        Assert.assertTrue(collection.containsAllArguments());
    }

    @Override
    @Test
    public void forEachWith()
    {
        super.forEachWith();

        MutableSortedBag<String> bag = this.newWith(Collections.reverseOrder(), "1", "2", "2", "3", "4");
        final StringBuilder builder = new StringBuilder();
        bag.forEachWith(new Procedure2<String, Integer>()
        {
            public void value(String argument1, Integer argument2)
            {
                builder.append(argument1).append(argument2);
            }
        }, 0);
        Assert.assertEquals("4030202010", builder.toString());
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        super.forEachWithIndex();

        MutableSortedBag<String> bag = this.newWith(Collections.reverseOrder(), "1", "2", "2", "3", "4");
        final StringBuilder builder = new StringBuilder();
        bag.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String each, int index)
            {
                builder.append(each).append(index);
            }
        });
        Assert.assertEquals("4031222314", builder.toString());
    }

    @Override
    @Test
    public void collectBoolean()
    {
        super.collectBoolean();

        MutableSortedBag<String> bag = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), "true", "nah", "TrUe");
        Assert.assertEquals(
                BooleanHashBag.newBagWith(true, false, true),
                bag.collectBoolean(StringFunctions.toPrimitiveBoolean()));
    }

    @Override
    @Test
    public void collectByte()
    {
        super.collectByte();
        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(
                ByteHashBag.newBagWith((byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 3),
                bag.collectByte(PrimitiveFunctions.unboxIntegerToByte()));
    }

    @Override
    @Test
    public void collectChar()
    {
        super.collectChar();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(
                CharHashBag.newBagWith((char) 1, (char) 1, (char) 1, (char) 2, (char) 3),
                bag.collectChar(PrimitiveFunctions.unboxIntegerToChar()));
    }

    @Override
    @Test
    public void collectDouble()
    {
        super.collectDouble();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(
                DoubleHashBag.newBagWith(1, 1, 1, 2, 3),
                bag.collectDouble(PrimitiveFunctions.unboxIntegerToDouble()));
    }

    @Override
    @Test
    public void collectFloat()
    {
        super.collectFloat();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(
                FloatHashBag.newBagWith(1, 1, 1, 2, 3),
                bag.collectFloat(PrimitiveFunctions.unboxIntegerToFloat()));
    }

    @Override
    @Test
    public void collectInt()
    {
        super.collectInt();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(
                IntHashBag.newBagWith(1, 1, 1, 2, 3),
                bag.collectInt(PrimitiveFunctions.unboxIntegerToInt()));
    }

    @Override
    @Test
    public void collectLong()
    {
        super.collectLong();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(
                LongHashBag.newBagWith(1, 1, 1, 2, 3),
                bag.collectLong(PrimitiveFunctions.unboxIntegerToLong()));
    }

    @Override
    @Test
    public void collectShort()
    {
        super.collectShort();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(
                ShortHashBag.newBagWith((short) 1, (short) 1, (short) 1, (short) 2, (short) 3),
                bag.collectShort(PrimitiveFunctions.unboxIntegerToShort()));
    }

    @Override
    @Test
    public void detect()
    {
        super.detect();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3);
        Assert.assertEquals(Integer.valueOf(2), bag.detect(Predicates.lessThan(3)));
        Assert.assertNull(bag.detect(Predicates.equal(4)));
    }

    @Override
    @Test
    public void min()
    {
        super.min();

        Assert.assertEquals(
                Integer.valueOf(1),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4).min());
        Assert.assertEquals(
                Integer.valueOf(4),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 2, 3, 4).min(Comparators.<Integer>reverseNaturalOrder()));
    }

    @Override
    @Test
    public void max()
    {
        super.max();

        Assert.assertEquals(
                Integer.valueOf(1),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4).min());
        Assert.assertEquals(
                Integer.valueOf(4),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3, 4).min(Comparators.<Integer>reverseNaturalOrder()));
    }

    @Override
    @Test
    public void minBy()
    {
        super.minBy();

        Assert.assertEquals(
                Integer.valueOf(1),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 2, 3, 3).minBy(Functions.getToString()));
    }

    @Override
    @Test
    public void maxBy()
    {
        super.maxBy();

        Assert.assertEquals(
                Integer.valueOf(3),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3).maxBy(Functions.getToString()));
    }

    @Override
    @Test
    public void detectWith()
    {
        super.detectWith();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 2, 3, 4, 4, 5);
        Assert.assertEquals(Integer.valueOf(5), bag.detectWith(Predicates2.<Integer>greaterThan(), 3));
        Assert.assertEquals(Integer.valueOf(2), bag.detectWith(Predicates2.<Integer>lessThan(), 3));
        Assert.assertNull(this.newWith(1, 2, 3, 4, 5).detectWith(Predicates2.equal(), 6));
    }

    @Override
    @Test
    public void detectIfNone()
    {
        super.detectIfNone();

        Function0<Integer> function = new PassThruFunction0<Integer>(6);
        Assert.assertEquals(Integer.valueOf(3), this.newWith(Comparators.reverseNaturalOrder(), 2, 3, 4, 5).detectIfNone(Predicates.equal(3), function));
        Assert.assertEquals(Integer.valueOf(3), this.newWith(Comparators.reverseNaturalOrder(), 2, 3, 4, 5).detectIfNone(Predicates.equal(3), null));
        Assert.assertEquals(Integer.valueOf(6), this.newWith(Comparators.reverseNaturalOrder(), 1, 2, 3, 4, 5).detectIfNone(Predicates.equal(6), function));
    }

    @Override
    @Test
    public void detectWithIfNoneBlock()
    {
        super.detectWithIfNoneBlock();

        Function0<Integer> function = new PassThruFunction0<Integer>(-42);
        Assert.assertEquals(
                Integer.valueOf(5),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 1, 2, 3, 4, 5).detectWithIfNone(
                        Predicates2.<Integer>greaterThan(),
                        4,
                        function));
        Assert.assertEquals(
                Integer.valueOf(-42),
                this.newWith(Comparators.reverseNaturalOrder(), 1, 2, 2, 2, 3, 4, 5).detectWithIfNone(
                        Predicates2.<Integer>lessThan(),
                        0,
                        function));
    }

    @Override
    @Test
    public void allSatisfy()
    {
        super.allSatisfy();

        MutableSortedBag<Integer> bag = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 3, 3, 3, 2, 2, 1);
        Assert.assertTrue(bag.allSatisfy(Predicates.lessThan(4)));
        Assert.assertFalse(bag.allSatisfy(Predicates.equal(2)));
        Assert.assertFalse(bag.allSatisfy(Predicates.greaterThan(4)));
    }

    @Override
    @Test
    public void allSatisfyWith()
    {
        super.allSatisfyWith();

        MutableSortedBag<Integer> bag = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 3, 3, 3, 2, 2, 1);
        Assert.assertTrue(bag.allSatisfyWith(Predicates2.<Integer>lessThan(), 4));
        Assert.assertFalse(bag.allSatisfyWith(Predicates2.<Integer>equal(), 2));
        Assert.assertFalse(bag.allSatisfyWith(Predicates2.<Integer>greaterThan(), 4));
    }

    @Override
    @Test
    public void noneSatisfy()
    {
        super.noneSatisfy();

        MutableSortedBag<Integer> bag = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 3, 3, 3, 2, 2, 1);

        Assert.assertFalse(bag.noneSatisfy(Predicates.lessThan(4)));
        Assert.assertFalse(bag.noneSatisfy(Predicates.equal(2)));
        Assert.assertTrue(bag.noneSatisfy(Predicates.greaterThan(4)));
    }

    @Override
    @Test
    public void noneSatisfyWith()
    {
        super.noneSatisfyWith();

        MutableSortedBag<Integer> bag = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 3, 3, 3, 2, 2, 1);

        Assert.assertFalse(bag.noneSatisfyWith(Predicates2.<Integer>lessThan(), 4));
        Assert.assertFalse(bag.noneSatisfyWith(Predicates2.equal(), 2));
        Assert.assertTrue(bag.noneSatisfyWith(Predicates2.<Integer>greaterThan(), 4));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        super.anySatisfy();

        MutableSortedBag<Integer> bag = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 3, 3, 3, 2, 2, 1);
        Assert.assertTrue(bag.anySatisfy(Predicates.lessThan(4)));
        Assert.assertTrue(bag.anySatisfy(Predicates.equal(2)));
        Assert.assertFalse(bag.anySatisfy(Predicates.greaterThan(4)));
    }

    @Override
    @Test
    public void anySatisfyWith()
    {
        super.anySatisfyWith();

        MutableSortedBag<Integer> bag = TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 3, 3, 3, 2, 2, 1);
        Assert.assertTrue(bag.anySatisfyWith(Predicates2.<Integer>lessThan(), 4));
        Assert.assertTrue(bag.anySatisfyWith(Predicates2.equal(), 2));
        Assert.assertFalse(bag.anySatisfyWith(Predicates2.<Integer>greaterThan(), 4));
    }

    @Override
    @Test
    public void count()
    {
        super.count();

        MutableSortedBag<Integer> sortedBag = this.newWith(Collections.reverseOrder(), 3, 2, 2, 2, 1);
        Assert.assertEquals(1, sortedBag.count(Predicates.greaterThan(2)));
        Assert.assertEquals(4, sortedBag.count(Predicates.greaterThan(1)));
        Assert.assertEquals(0, sortedBag.count(Predicates.greaterThan(3)));
    }

    @Override
    @Test
    public void countWith()
    {
        super.countWith();

        MutableSortedBag<Integer> sortedBag = this.newWith(Collections.reverseOrder(), 3, 2, 2, 2, 1);
        Assert.assertEquals(1, sortedBag.countWith(Predicates2.<Integer>greaterThan(), 2));
        Assert.assertEquals(4, sortedBag.countWith(Predicates2.<Integer>greaterThan(), 1));
        Assert.assertEquals(0, sortedBag.countWith(Predicates2.<Integer>greaterThan(), 3));
    }

    @Override
    @Test
    public void removeAll()
    {
        super.removeAll();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 5, 5, 3, 2, 2, 2, 1);
        Assert.assertTrue(bag.removeAll(FastList.newListWith(1, 2, 4)));
        Assert.assertFalse(bag.removeAll(FastList.newListWith(1, 2, 4)));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 5, 5, 3), bag);
    }

    @Override
    @Test
    public void removeAllIterable()
    {
        super.removeAllIterable();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 5, 5, 3, 2, 2, 2, 1);
        Assert.assertTrue(bag.removeAllIterable(FastList.newListWith(1, 2, 4)));
        Assert.assertFalse(bag.removeAllIterable(FastList.newListWith(1, 2, 4)));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 5, 5, 3), bag);
    }

    @Override
    @Test
    public void retainAll()
    {
        super.retainAll();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 5, 5, 3, 2, 1, 1, 1);
        Assert.assertTrue(bag.retainAll(FastList.newListWith(1, 2)));
        Assert.assertFalse(bag.retainAll(FastList.newListWith(1, 2)));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.reverseOrder(), 2, 1, 1, 1), bag);
    }

    @Override
    @Test
    public void retainAllIterable()
    {
        super.retainAllIterable();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 5, 5, 3, 2, 1, 1, 1);
        Assert.assertTrue(bag.retainAllIterable(FastList.newListWith(1, 2)));
        Assert.assertFalse(bag.retainAllIterable(FastList.newListWith(1, 2)));
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Collections.reverseOrder(), 2, 1, 1, 1), bag);
    }

    @Override
    @Test
    public void injectInto()
    {
        super.injectInto();

        Assert.assertEquals(
                Integer.valueOf(11),
                this.newWith(Collections.reverseOrder(), 1, 1, 2, 3, 4).injectInto(Integer.valueOf(0), AddFunction.INTEGER));
    }

    @Override
    @Test
    public void injectIntoWith()
    {
        super.injectIntoWith();

        MutableSortedBag<Integer> bag = this.newWith(Collections.reverseOrder(), 1, 1, 2, 3);
        Integer result = bag.injectIntoWith(1, new Function3<Integer, Integer, Integer, Integer>()
        {
            public Integer value(Integer injectedValued, Integer item, Integer parameter)
            {
                return injectedValued + item + parameter;
            }
        }, 0);
        Assert.assertEquals(Integer.valueOf(8), result);
    }

    @Override
    @Test
    public void injectIntoInt()
    {
        super.injectIntoInt();

        Assert.assertEquals(
                11,
                this.newWith(Collections.reverseOrder(), 1, 1, 2, 3, 4).injectInto(0, AddFunction.INTEGER_TO_INT));
    }

    @Override
    @Test
    public void injectIntoLong()
    {
        super.injectIntoLong();

        Assert.assertEquals(
                8,
                this.newWith(Collections.reverseOrder(), 1, 1, 2, 3).injectInto(1L, AddFunction.INTEGER_TO_LONG));
    }

    @Override
    @Test
    public void injectIntoDouble()
    {
        super.injectIntoDouble();

        Assert.assertEquals(
                8.0,
                this.newWith(Collections.reverseOrder(), 1.0, 1.0, 2.0, 3.0).injectInto(1.0d, AddFunction.DOUBLE_TO_DOUBLE), 0.001);
    }

    @Override
    @Test
    public void injectIntoFloat()
    {
        super.injectIntoFloat();

        Assert.assertEquals(
                8.0,
                this.newWith(Collections.reverseOrder(), 1, 1, 2, 3).injectInto(1.0f, AddFunction.INTEGER_TO_FLOAT), 0.001);
    }

    @Override
    @Test
    public void sumFloat()
    {
        super.sumFloat();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4, 5);
        Assert.assertEquals(16.0f, bag.sumOfFloat(new FloatFunction<Integer>()
        {
            public float floatValueOf(Integer integer)
            {
                return integer.floatValue();
            }
        }), 0.001);
    }

    @Override
    @Test
    public void sumDouble()
    {
        super.sumDouble();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4, 5);
        Assert.assertEquals(16.0d, bag.sumOfDouble(new DoubleFunction<Integer>()
        {
            public double doubleValueOf(Integer integer)
            {
                return integer.doubleValue();
            }
        }), 0.001);
    }

    @Override
    @Test
    public void sumInteger()
    {
        super.sumInteger();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4, 5);
        Assert.assertEquals(16, bag.sumOfLong(new LongFunction<Integer>()
        {
            public long longValueOf(Integer integer)
            {
                return integer.longValue();
            }
        }));
    }

    @Override
    @Test
    public void sumLong()
    {
        super.sumLong();

        MutableSortedBag<Integer> bag = this.newWith(Comparators.reverseNaturalOrder(), 1, 1, 2, 3, 4, 5);
        Assert.assertEquals(16, bag.sumOfLong(new LongFunction<Integer>()
        {
            public long longValueOf(Integer integer)
            {
                return integer.longValue();
            }
        }));
    }

    @Override
    @Test
    public void toArray()
    {
        super.toArray();

        Assert.assertArrayEquals(new Object[]{4, 4, 3, 2, 1}, this.newWith(Collections.reverseOrder(), 4, 4, 3, 2, 1).toArray());
        Assert.assertArrayEquals(new Integer[]{4, 4, 3, 2, 1}, this.newWith(Collections.reverseOrder(), 4, 4, 3, 2, 1).toArray(new Integer[0]));
        Assert.assertArrayEquals(new Integer[]{4, 4, 3, 2, 1, null, null}, this.newWith(Collections.reverseOrder(), 4, 4, 3, 2, 1).toArray(new Integer[7]));
    }

    @Override
    @Test
    public void chunk()
    {
        super.chunk();

        MutableSortedBag<String> bag = this.newWith(Comparators.reverseNaturalOrder(), "6", "5", "4", "3", "2", "1", "1");
        RichIterable<RichIterable<String>> groups = bag.chunk(2);
        Assert.assertEquals(
                FastList.newListWith(
                        TreeBag.newBagWith(Comparators.reverseNaturalOrder(), "6", "5"),
                        TreeBag.newBagWith(Comparators.reverseNaturalOrder(), "4", "3"),
                        TreeBag.newBagWith(Comparators.reverseNaturalOrder(), "2", "1"),
                        TreeBag.newBagWith(Comparators.reverseNaturalOrder(), "1")
                ),
                groups);
    }

    @Override
    @Test
    public void aggregateByMutating()
    {
        super.aggregateByMutating();

        Function0<AtomicInteger> zeroValueFactory = new Function0<AtomicInteger>()
        {
            public AtomicInteger value()
            {
                return new AtomicInteger(0);
            }
        };
        Procedure2<AtomicInteger, Integer> sumAggregator = new

                Procedure2<AtomicInteger, Integer>()
                {
                    public void value(AtomicInteger aggregate, Integer value)
                    {
                        aggregate.addAndGet(value);
                    }
                };
        MutableSortedBag<Integer> sortedBag = this.newWith(Comparators.reverseNaturalOrder(), 3, 2, 2, 1, 1, 1);
        MapIterable<String, AtomicInteger> aggregation = sortedBag.aggregateInPlaceBy(Functions.getToString(), zeroValueFactory, sumAggregator);
        Assert.assertEquals(3, aggregation.get("1").intValue());
        Assert.assertEquals(4, aggregation.get("2").intValue());
        Assert.assertEquals(3, aggregation.get("3").intValue());
    }

    @Override
    @Test
    public void aggregateByNonMutating()
    {
        super.aggregateByNonMutating();

        Function0<Integer> zeroValueFactory = new Function0<Integer>()
        {
            public Integer value()
            {
                return Integer.valueOf(0);
            }
        };
        Function2<Integer, Integer, Integer> sumAggregator = new

                Function2<Integer, Integer, Integer>()
                {
                    public Integer value(Integer aggregate, Integer value)
                    {
                        return aggregate + value;
                    }
                };
        MutableSortedBag<Integer> sortedBag = this.newWith(Comparators.reverseNaturalOrder(), 3, 2, 2, 1, 1, 1);
        MapIterable<String, Integer> aggregation = sortedBag.aggregateBy(Functions.getToString(), zeroValueFactory, sumAggregator);
        Assert.assertEquals(3, aggregation.get("1").intValue());
        Assert.assertEquals(4, aggregation.get("2").intValue());
        Assert.assertEquals(3, aggregation.get("3").intValue());
    }

    // Like Integer, but not Comparable
    public static final class Holder
    {
        private static final Function<Integer, Holder> FROM_INT = new Function<Integer, Holder>()
        {
            public Holder valueOf(Integer object)
            {
                return new Holder(object);
            }
        };
        private static final Function2<Integer, Integer, Holder> FROM_INT_INT = new Function2<Integer, Integer, Holder>()
        {
            public Holder value(Integer each, Integer each2)
            {
                return new Holder(each + each2);
            }
        };
        private static final Function<Integer, MutableList<Holder>> FROM_LIST = new Function<Integer, MutableList<Holder>>()
        {
            public MutableList<Holder> valueOf(Integer object)
            {
                return FastList.newListWith(new Holder(object), new Holder(object));
            }
        };
        private static final IntFunction<Holder> TO_NUMBER = new IntFunction<Holder>()
        {
            public int intValueOf(Holder holder)
            {
                return holder.number;
            }
        };
        private final int number;

        private Holder(int i)
        {
            this.number = i;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || this.getClass() != o.getClass())
            {
                return false;
            }

            Holder holder = (Holder) o;

            return this.number == holder.number;
        }

        @Override
        public int hashCode()
        {
            return this.number;
        }

        @Override
        public String toString()
        {
            return String.valueOf(this.number);
        }
    }
}
