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

package ponzu.impl.bag.immutable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import ponzu.api.LazyIterable;
import ponzu.api.bag.ImmutableBag;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Generator;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.map.sorted.MutableSortedMap;
import ponzu.api.multimap.bag.ImmutableBagMultimap;
import ponzu.api.partition.bag.PartitionImmutableBag;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.bag.mutable.HashBag;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.IntegerPredicates;
import ponzu.impl.block.factory.ObjectIntProcedures;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.factory.StringFunctions;
import ponzu.impl.block.function.AddFunction;
import ponzu.impl.block.function.Constant;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Bags;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.Interval;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.multimap.bag.HashBagMultimap;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.test.Verify;
import ponzu.impl.utility.StringIterate;

public abstract class ImmutableBagTestCase
{
    /**
     * @return A bag containing "1", "2", "2", "3", "3", "3", etc.
     */
    protected abstract ImmutableBag<String> newBag();

    /**
     * @return The number of unique keys.
     */
    protected abstract int numKeys();

    @Test
    public abstract void testSize();

    @Test
    public void equalsAndHashCode()
    {
        ImmutableBag<String> immutable = this.newBag();
        MutableBag<String> mutable = HashBag.newBag(immutable);
        Verify.assertEqualsAndHashCode(immutable, mutable);
        Verify.assertNotEquals(immutable, FastList.newList(mutable));
    }

    @Test
    public void testNewWith()
    {
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> newBag = bag.newWith("1");
        Verify.assertNotEquals(bag, newBag);
        Assert.assertEquals(bag.size() + 1, newBag.size());
        Assert.assertEquals(bag.sizeDistinct(), newBag.sizeDistinct());
        ImmutableBag<String> newBag2 = bag.newWith("0");
        Verify.assertNotEquals(bag, newBag2);
        Assert.assertEquals(bag.size() + 1, newBag2.size());
        Assert.assertEquals(newBag.sizeDistinct() + 1, newBag2.sizeDistinct());
    }

    @Test
    public void testNewWithout()
    {
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> newBag = bag.newWithout("1");
        Verify.assertNotEquals(bag, newBag);
        Assert.assertEquals(bag.size() - 1, newBag.size());
        Assert.assertEquals(bag.sizeDistinct() - 1, newBag.sizeDistinct());
        ImmutableBag<String> newBag2 = bag.newWithout("0");
        Assert.assertEquals(bag, newBag2);
        Assert.assertEquals(bag.size(), newBag2.size());
        Assert.assertEquals(bag.sizeDistinct(), newBag2.sizeDistinct());
    }

    @Test
    public void testNewWithAll()
    {
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> newBag = bag.newWithAll(Bags.mutable.of("0"));
        Verify.assertNotEquals(bag, newBag);
        Assert.assertEquals(HashBag.newBag(bag).with("0"), newBag);
        Assert.assertEquals(newBag.size(), bag.size() + 1);
    }

    @Test
    public void testNewWithoutAll()
    {
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> withoutAll = bag.newWithoutAll(UnifiedSet.newSet(this.newBag()));
        Assert.assertEquals(Bags.immutable.of(), withoutAll);

        ImmutableBag<String> newBag =
                bag.newWithAll(Lists.fixedSize.of("0", "0", "0"))
                        .newWithoutAll(Lists.fixedSize.of("0"));

        Assert.assertEquals(0, newBag.occurrencesOf("0"));
    }

    @Test
    public void testContains()
    {
        ImmutableBag<String> bag = this.newBag();
        for (int i = 1; i <= this.numKeys(); i++)
        {
            String key = String.valueOf(i);
            Assert.assertTrue(bag.contains(key));
            Assert.assertEquals(i, bag.occurrencesOf(key));
        }
        String missingKey = "0";
        Assert.assertFalse(bag.contains(missingKey));
        Assert.assertEquals(0, bag.occurrencesOf(missingKey));
    }

    @Test
    public void testContainsAllArray()
    {
        Assert.assertTrue(this.newBag().containsAllArguments(this.newBag().toArray()));
    }

    @Test
    public void testContainsAllIterable()
    {
        Assert.assertTrue(this.newBag().containsAllIterable(this.newBag()));
    }

    @Test
    public void testForEach()
    {
        MutableBag<String> result = Bags.mutable.of();
        ImmutableBag<String> collection = this.newBag();
        collection.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(collection, result);
    }

    @Test
    public void testForEachWith()
    {
        final MutableBag<String> result = Bags.mutable.of();
        ImmutableBag<String> bag = this.newBag();
        bag.forEachWith(new Procedure2<String, String>()
        {
            public void value(String argument1, String argument2)
            {
                result.add(argument1 + argument2);
            }
        }, "");
        Assert.assertEquals(bag, result);
    }

    @Test
    public void testForEachWithIndex()
    {
        MutableBag<String> result = Bags.mutable.of();
        ImmutableBag<String> strings = this.newBag();
        strings.forEachWithIndex(ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(result)));
        Assert.assertEquals(strings, result);
    }

    @Test
    public void testSelect()
    {
        ImmutableBag<String> strings = this.newBag();
        Verify.assertContainsAll(
                FastList.newList(strings.filter(Predicates.greaterThan("0"))),
                strings.toArray());
        Verify.assertIterableEmpty(strings.filter(Predicates.lessThan("0")));
        Verify.assertIterableSize(strings.size() - 1, strings.filter(Predicates.greaterThan("1")));
    }

    @Test
    public void testSelectWith()
    {
        ImmutableBag<String> strings = this.newBag();

        Verify.assertIterableEmpty(strings.filter(Predicates.lessThan("0")));

        String argument = "thing";
        Assert.assertEquals(
                strings,
                strings.filterWith(new GreaterThan0Predicate2(argument), argument, FastList.<String>newList()).toBag());
    }

    @Test
    public void testSelectWithTarget()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals(strings, strings.filter(Predicates.greaterThan("0"), FastList.<String>newList()).toBag());
        Verify.assertEmpty(strings.filter(Predicates.lessThan("0"), FastList.<String>newList()));
    }

    @Test
    public void testReject()
    {
        ImmutableBag<String> strings = this.newBag();
        Verify.assertIterableEmpty(strings.filterNot(Predicates.greaterThan("0")));
        Assert.assertEquals(strings, strings.filterNot(Predicates.lessThan("0")));
        Verify.assertIterableSize(strings.size() - 1, strings.filterNot(Predicates.lessThan("2")));
    }

    @Test
    public void testRejectWith()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals(strings, strings.filterNot(Predicates.lessThan("0")));

        String argument = "thing";
        Verify.assertEmpty(strings.filterNotWith(new GreaterThan0Predicate2(argument), argument, FastList.<String>newList()));
    }

    @Test
    public void testRejectWithTarget()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals(strings, strings.filterNot(Predicates.lessThan("0"), FastList.<String>newList()).toBag());
        Verify.assertEmpty(strings.filterNot(Predicates.greaterThan("0"), FastList.<String>newList()));
    }

    @Test
    public void partition()
    {
        ImmutableBag<String> strings = this.newBag();
        PartitionImmutableBag<String> partition = strings.partition(Predicates.greaterThan("0"));
        Assert.assertEquals(strings, partition.getSelected());
        Verify.assertIterableEmpty(partition.getRejected());

        Verify.assertIterableSize(strings.size() - 1, strings.partition(Predicates.greaterThan("1")).getSelected());
    }

    @Test
    public void testCollect()
    {
        Assert.assertEquals(this.newBag(), this.newBag().transform(Functions.getStringPassThru()));
    }

    @Test
    public void testCollectWith()
    {
        ImmutableBag<String> strings = this.newBag();

        final String argument = "thing";
        Assert.assertEquals(strings, strings.transformWith(new Function2<String, String, String>()
        {
            public String value(String argument1, String argument2)
            {
                Assert.assertEquals(argument, argument2);
                return argument1;
            }
        }, argument, HashBag.<String>newBag()));
    }

    @Test
    public void testCollectWithTarget()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals(strings, strings.transform(Functions.getStringPassThru(), HashBag.<String>newBag()));
        Assert.assertEquals(strings, strings.transform(Functions.getStringPassThru(), FastList.<String>newList()).toBag());
    }

    @Test
    public void flatCollect()
    {
        ImmutableBag<String> actual = this.newBag().flatTransform(new Function<String, MutableList<String>>()
        {
            public MutableList<String> valueOf(String string)
            {
                return Lists.fixedSize.of(string);
            }
        });

        ImmutableBag<String> expected = this.newBag().transform(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void flatCollectWithTarget()
    {
        MutableBag<String> actual = this.newBag().flatTransform(new Function<String, MutableList<String>>()
        {
            public MutableList<String> valueOf(String string)
            {
                return Lists.fixedSize.of(string);
            }
        }, HashBag.<String>newBag());

        ImmutableBag<String> expected = this.newBag().transform(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDetect()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals("1", strings.find(Predicates.equal("1")));
        Assert.assertNull(strings.find(Predicates.equal(String.valueOf(this.numKeys() + 1))));
    }

    @Test
    public void zip()
    {
        ImmutableBag<String> immutableBag = this.newBag();
        List<Object> nulls = Collections.nCopies(immutableBag.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableBag.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(immutableBag.size() - 1, null);

        ImmutableBag<Pair<String, Object>> pairs = immutableBag.zip(nulls);
        Assert.assertEquals(
                immutableBag,
                pairs.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                HashBag.newBag(nulls),
                pairs.transform(Functions.<Object>secondOfPair()));

        ImmutableBag<Pair<String, Object>> pairsPlusOne = immutableBag.zip(nullsPlusOne);
        Assert.assertEquals(
                immutableBag,
                pairsPlusOne.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                HashBag.newBag(nulls),
                pairsPlusOne.transform(Functions.<Object>secondOfPair()));

        ImmutableBag<Pair<String, Object>> pairsMinusOne = immutableBag.zip(nullsMinusOne);
        Assert.assertEquals(immutableBag.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(immutableBag.containsAllIterable(pairsMinusOne.transform(Functions.<String>firstOfPair())));

        Assert.assertEquals(immutableBag.zip(nulls), immutableBag.zip(nulls, HashBag.<Pair<String, Object>>newBag()));
    }

    @Test
    public void zipWithIndex()
    {
        ImmutableBag<String> immutableBag = this.newBag();
        ImmutableBag<Pair<String, Integer>> pairs = immutableBag.zipWithIndex();

        Assert.assertEquals(immutableBag, pairs.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(Interval.zeroTo(immutableBag.size() - 1).toBag(), pairs.transform(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableBag.zipWithIndex(), immutableBag.zipWithIndex(HashBag.<Pair<String, Integer>>newBag()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.newBag().chunk(0);
    }

    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.newBag(), this.newBag().chunk(10).getFirst());
        Verify.assertInstanceOf(ImmutableBag.class, this.newBag().chunk(10).getFirst());
    }

    private ImmutableBag<String> classUnderTestWithNull()
    {
        return this.newBag().newWith(null);
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
        Assert.assertEquals("1", this.newBag().min(Comparators.naturalOrder()));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(String.valueOf(this.numKeys()), this.newBag().max(Comparators.naturalOrder()));
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
        Assert.assertEquals("1", this.newBag().min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(String.valueOf(this.numKeys()), this.newBag().max());
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals("1", this.newBag().minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(String.valueOf(this.numKeys()), this.newBag().maxBy(Functions.getToString()));
    }

    @Test
    public void testDetectIfNoneWithBlock()
    {
        ImmutableBag<String> strings = this.newBag();
        Generator<String> function = new Constant<String>(String.valueOf(this.numKeys() + 1));
        Assert.assertEquals("1", strings.findIfNone(Predicates.equal("1"), function));
        Assert.assertEquals(String.valueOf(this.numKeys() + 1), strings.findIfNone(Predicates.equal(String.valueOf(this.numKeys() + 1)), function));
    }

    @Test
    public void allSatisfy()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertTrue(strings.allSatisfy(Predicates.instanceOf(String.class)));
        Assert.assertFalse(strings.allSatisfy(Predicates.equal("0")));
    }

    @Test
    public void testAnySatisfy()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertFalse(strings.anySatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertTrue(strings.anySatisfy(Predicates.instanceOf(String.class)));
    }

    @Test
    public void testCount()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals(strings.size(), strings.count(Predicates.instanceOf(String.class)));
        Assert.assertEquals(0, strings.count(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void testCollectIf()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals(
                strings,
                strings.transformIf(
                        Predicates.instanceOf(String.class),
                        Functions.getStringPassThru()));
    }

    @Test
    public void testCollectIfWithTarget()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertEquals(
                strings,
                strings.transformIf(
                        Predicates.instanceOf(String.class),
                        Functions.getStringPassThru(),
                        HashBag.<String>newBag()));
    }

    @Test
    public void testGetFirst()
    {
        // Cannot assert much here since there's no order.
        ImmutableBag<String> bag = this.newBag();
        Assert.assertTrue(bag.contains(bag.getFirst()));
    }

    @Test
    public void testGetLast()
    {
        // Cannot assert much here since there's no order.
        ImmutableBag<String> bag = this.newBag();
        Assert.assertTrue(bag.contains(bag.getLast()));
    }

    @Test
    public void testIsEmpty()
    {
        ImmutableBag<String> bag = this.newBag();
        Assert.assertFalse(bag.isEmpty());
        Assert.assertTrue(bag.notEmpty());
    }

    @Test
    public void testIterator()
    {
        ImmutableBag<String> strings = this.newBag();
        MutableBag<String> result = Bags.mutable.of();
        final Iterator<String> iterator = strings.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            String string = iterator.next();
            result.add(string);
        }
        Assert.assertEquals(strings, result);

        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator.next();
            }
        });
    }

    @Test
    public void injectInto()
    {
        ImmutableBag<Integer> integers = this.newBag().transform(StringFunctions.toInteger());
        Integer result = integers.foldLeft(0, AddFunction.INTEGER);
        Assert.assertEquals(FastList.newList(integers).foldLeft(0, AddFunction.INTEGER_TO_INT), result.intValue());
    }

    @Test
    public void injectIntoInt()
    {
        ImmutableBag<Integer> integers = this.newBag().transform(new Function<String, Integer>()
        {
            public Integer valueOf(String string)
            {
                return Integer.valueOf(string);
            }
        });
        int result = integers.foldLeft(0, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(FastList.newList(integers).foldLeft(0, AddFunction.INTEGER_TO_INT), result);
    }

    @Test
    public void injectIntoLong()
    {
        ImmutableBag<Integer> integers = this.newBag().transform(StringFunctions.toInteger());
        long result = integers.foldLeft(0, AddFunction.INTEGER_TO_LONG);
        Assert.assertEquals(FastList.newList(integers).foldLeft(0, AddFunction.INTEGER_TO_INT), result);
    }

    @Test
    public void injectIntoDouble()
    {
        ImmutableBag<Integer> integers = this.newBag().transform(StringFunctions.toInteger());
        double result = integers.foldLeft(0, AddFunction.INTEGER_TO_DOUBLE);
        double expected = FastList.newList(integers).foldLeft(0, AddFunction.INTEGER_TO_DOUBLE);
        Assert.assertEquals(expected, result, 0.001);
    }

    @Test
    public void testToArray()
    {
        ImmutableBag<String> strings = this.newBag();
        MutableList<String> copy = FastList.newList(strings);
        Assert.assertArrayEquals(strings.toArray(), copy.toArray());
        Assert.assertArrayEquals(strings.toArray(new String[strings.size()]), copy.toArray(new String[strings.size()]));
    }

    @Test
    public void testToString()
    {
        String string = this.newBag().toString();
        for (int i = 1; i < this.numKeys(); i++)
        {
            Assert.assertEquals(i, StringIterate.occurrencesOf(string, String.valueOf(i)));
        }
    }

    @Test
    public void toList()
    {
        ImmutableBag<String> strings = this.newBag();
        MutableList<String> list = strings.toList();
        Verify.assertEqualsAndHashCode(FastList.newList(strings), list);
    }

    @Test
    public void toSortedList()
    {
        ImmutableBag<String> strings = this.newBag();
        MutableList<String> copy = FastList.newList(strings);
        MutableList<String> list = strings.toSortedList(Collections.<String>reverseOrder());
        Assert.assertEquals(copy.sortThis(Collections.<String>reverseOrder()), list);
        MutableList<String> list2 = strings.toSortedList();
        Assert.assertEquals(copy.sortThis(), list2);
    }

    @Test
    public void toSortedListBy()
    {
        MutableList<String> expected = this.newBag().toList();
        Collections.sort(expected);
        ImmutableBag<String> immutableBag = this.newBag();
        MutableList<String> sortedList = immutableBag.toSortedListBy(Functions.getToString());
        Assert.assertEquals(expected, sortedList);
    }

    @Test
    public void testForLoop()
    {
        ImmutableBag<String> bag = this.newBag();
        for (String each : bag)
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
                ImmutableBagTestCase.this.newBag().iterator().remove();
            }
        });
    }

    @Test
    public void toMapOfItemToCount()
    {
        MutableMap<String, Integer> mapOfItemToCount = this.newBag().toMapOfItemToCount();

        for (int i = 1; i <= this.numKeys(); i++)
        {
            String key = String.valueOf(i);
            Assert.assertTrue(mapOfItemToCount.containsKey(key));
            Assert.assertEquals(Integer.valueOf(i), mapOfItemToCount.get(key));
        }

        String missingKey = "0";
        Assert.assertFalse(mapOfItemToCount.containsKey(missingKey));
        Assert.assertNull(mapOfItemToCount.get(missingKey));
    }

    @Test
    public void toImmutable()
    {
        ImmutableBag<String> bag = this.newBag();
        Assert.assertSame(bag, bag.toImmutable());
    }

    @Test
    public void groupBy()
    {
        ImmutableBagMultimap<Boolean, String> multimap = this.newBag().groupBy(new Function<String, Boolean>()
        {
            public Boolean valueOf(String string)
            {
                return IntegerPredicates.isOdd().accept(Integer.valueOf(string));
            }
        });

        this.groupByAssertions(multimap);
    }

    @Test
    public void groupBy_with_target()
    {
        ImmutableBagMultimap<Boolean, String> multimap = this.newBag().groupBy(new Function<String, Boolean>()
        {
            public Boolean valueOf(String string)
            {
                return IntegerPredicates.isOdd().accept(Integer.valueOf(string));
            }
        }, new HashBagMultimap<Boolean, String>()).toImmutable();

        this.groupByAssertions(multimap);
    }

    private void groupByAssertions(ImmutableBagMultimap<Boolean, String> multimap)
    {
        Verify.assertIterableEmpty(multimap.get(null));

        ImmutableBag<String> odds = multimap.get(true);
        ImmutableBag<String> evens = multimap.get(false);
        for (int i = 1; i <= this.numKeys(); i++)
        {
            String key = String.valueOf(i);
            ImmutableBag<String> containingBag = IntegerPredicates.isOdd().accept(i) ? odds : evens;
            ImmutableBag<String> nonContainingBag = IntegerPredicates.isOdd().accept(i) ? evens : odds;
            Assert.assertTrue(containingBag.contains(key));
            Assert.assertFalse(nonContainingBag.contains(key));

            Assert.assertEquals(i, containingBag.occurrencesOf(key));
        }
    }

    @Test
    public void toSet()
    {
        MutableSet<String> expectedSet = this.numKeys() == 0
                ? UnifiedSet.<String>newSet()
                : Interval.oneTo(this.numKeys()).transform(Functions.getToString()).toSet();
        Assert.assertEquals(expectedSet, this.newBag().toSet());
    }

    @Test
    public void toBag()
    {
        ImmutableBag<String> immutableBag = this.newBag();
        MutableBag<String> mutableBag = immutableBag.toBag();
        Assert.assertEquals(immutableBag, mutableBag);
    }

    @Test
    public void toMap()
    {
        MutableMap<String, String> map = this.newBag().toMap(Functions.<String>getPassThru(), Functions.<String>getPassThru());

        for (int i = 1; i <= this.numKeys(); i++)
        {
            String key = String.valueOf(i);
            Assert.assertTrue(map.containsKey(key));
            Assert.assertEquals(key, map.get(key));
        }

        String missingKey = "0";
        Assert.assertFalse(map.containsKey(missingKey));
        Assert.assertNull(map.get(missingKey));
    }

    @Test
    public void toSortedMap()
    {
        MutableSortedMap<Integer, String> map = this.newBag().toSortedMap(Functions.getStringToInteger(), Functions.<String>getPassThru());

        Verify.assertMapsEqual(this.newBag().toMap(Functions.getStringToInteger(), Functions.<String>getPassThru()), map);
        Verify.assertListsEqual(Interval.oneTo(this.numKeys()), map.keySet().toList());
    }

    @Test
    public void toSortedMap_with_comparator()
    {
        MutableSortedMap<Integer, String> map = this.newBag().toSortedMap(Comparators.<Integer>reverseNaturalOrder(),
                Functions.getStringToInteger(), Functions.<String>getPassThru());

        Verify.assertMapsEqual(this.newBag().toMap(Functions.getStringToInteger(), Functions.<String>getPassThru()), map);
        Verify.assertListsEqual(Interval.fromTo(this.numKeys(), 1), map.keySet().toList());
    }

    @Test
    public void asLazy()
    {
        ImmutableBag<String> bag = this.newBag();
        LazyIterable<String> lazyIterable = bag.asLazy();
        Verify.assertInstanceOf(LazyIterable.class, lazyIterable);
        Assert.assertEquals(bag, lazyIterable.toBag());
    }

    @Test
    public void toArray()
    {
        ImmutableBag<String> bag = this.newBag();
        Object[] array = bag.toArray();
        Verify.assertSize(bag.size(), array);

        String[] array2 = bag.toArray(new String[bag.size() + 1]);
        Verify.assertSize(bag.size() + 1, array2);
        Assert.assertNull(array2[bag.size()]);
    }

    @Test
    public void makeString()
    {
        ImmutableBag<String> bag = this.newBag();
        Assert.assertEquals(FastList.newList(bag).makeString(), bag.makeString());
        Assert.assertEquals(bag.toString(), '[' + bag.makeString() + ']');
        Assert.assertEquals(bag.toString(), '[' + bag.makeString(", ") + ']');
        Assert.assertEquals(bag.toString(), bag.makeString("[", ", ", "]"));
    }

    @Test
    public void appendString()
    {
        ImmutableBag<String> bag = this.newBag();

        Appendable builder = new StringBuilder();
        bag.appendString(builder);
        Assert.assertEquals(FastList.newList(bag).makeString(), builder.toString());
    }

    @Test
    public void appendString_with_separator()
    {
        ImmutableBag<String> bag = this.newBag();

        Appendable builder = new StringBuilder();
        bag.appendString(builder, ", ");
        Assert.assertEquals(bag.toString(), '[' + builder.toString() + ']');
    }

    @Test
    public void appendString_with_start_separator_end()
    {
        ImmutableBag<String> bag = this.newBag();

        Appendable builder = new StringBuilder();
        bag.appendString(builder, "[", ", ", "]");
        Assert.assertEquals(bag.toString(), builder.toString());
    }

    @Test
    public void serialization()
    {
        ImmutableBag<String> bag = this.newBag();
        Verify.assertPostSerializedEqualsAndHashCode(bag);
    }

    private static final class GreaterThan0Predicate2 implements Predicate2<String, String>
    {
        private static final long serialVersionUID = 1L;
        private final String argument;

        private GreaterThan0Predicate2(String argument)
        {
            this.argument = argument;
        }

        public boolean accept(String argument1, String argument2)
        {
            Assert.assertEquals(this.argument, argument2);
            return argument1.compareTo("0") > 0;
        }
    }
}
