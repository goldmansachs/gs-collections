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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.FixedSizeSet;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.set.mutable.SynchronizedMutableSet;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.mutable.UnmodifiableMutableSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link AbstractMemoryEfficientMutableSet}.
 */
public abstract class AbstractMemoryEfficientMutableSetTestCase
{
    protected abstract MutableSet<String> classUnderTest();

    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableSet.class, this.classUnderTest().asSynchronized());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.remove("1");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAll_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.addAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllIterable_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.addAllIterable(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add_duplicate_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.add("1");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAll_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.removeAll(mList("1", "2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAllIterable_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.removeAllIterable(mList("1", "2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void retainAll_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.retainAll(mList("1", "2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void retainAllIterable_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.retainAllIterable(mList("1", "2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void clear_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIf_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.removeIf(Predicates.equal("1"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIfWith_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.removeIfWith(Object::equals, "1");
    }

    @Test
    public void iterator()
    {
        MutableSet<String> set = this.classUnderTest();
        int size = set.size();

        Iterator<String> iterator = set.iterator();
        for (int i = size; i-- > 0; )
        {
            String integerString = iterator.next();
            Assert.assertEquals(size, Integer.parseInt(integerString) + i);
        }

        Verify.assertThrows(NoSuchElementException.class, (Runnable) iterator::next);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorRemove_throws()
    {
        MutableSet<String> set = this.classUnderTest();
        set.iterator().remove();
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws()
    {
        this.classUnderTestWithNull().min(String::compareTo);
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws()
    {
        this.classUnderTestWithNull().max(String::compareTo);
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

    protected abstract MutableSet<String> classUnderTestWithNull();

    @Test
    public void iterationWithIterator()
    {
        Iterator<String> iterator = this.classUnderTest().iterator();
        while (iterator.hasNext())
        {
            this.classUnderTest().contains(iterator.next());
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorWillGetUpsetIfYouPushItTooFar()
    {
        Iterator<String> iterator = this.classUnderTest().iterator();
        for (int i = 0; i < this.classUnderTest().size() + 1; i++)
        {
            iterator.next();
        }
    }

    @Test
    public void equalsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(UnifiedSet.newSet(this.classUnderTest()), this.classUnderTest());
    }

    @Test
    public void groupBy()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSetMultimap<Boolean, String> multimap =
                set.groupBy(object -> IntegerPredicates.isOdd().accept(Integer.parseInt(object)));

        MutableMap<Boolean, RichIterable<String>> actualMap = multimap.toMap();
        int halfSize = this.classUnderTest().size() / 2;
        boolean odd = this.classUnderTest().size() % 2 != 0;
        Assert.assertEquals(halfSize, Iterate.sizeOf(actualMap.getIfAbsent(false, FastList::new)));
        Assert.assertEquals(halfSize + (odd ? 1 : 0), Iterate.sizeOf(actualMap.get(true)));
    }

    @Test
    public void groupByEach()
    {
        MutableSet<Integer> set = this.classUnderTest().collect(Integer::valueOf);

        MutableMultimap<Integer, Integer> expected = UnifiedSetMultimap.newMultimap();
        set.forEach(Procedures.cast(value -> expected.putAll(-value, Interval.fromTo(value, set.size()))));

        Multimap<Integer, Integer> actual =
                set.groupByEach(new NegativeIntervalFunction());
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                set.groupByEach(new NegativeIntervalFunction(), UnifiedSetMultimap.<Integer, Integer>newMultimap());
        Assert.assertEquals(expected, actualWithTarget);
    }

    @Test
    public void zip()
    {
        MutableSet<String> set = this.classUnderTest();
        List<Object> nulls = Collections.nCopies(set.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(set.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(set.size() - 1, null);

        MutableSet<Pair<String, Object>> pairs = set.zip(nulls);
        Assert.assertEquals(set, pairs.collect((Function<Pair<String, ?>, String>) Pair::getOne));
        Assert.assertEquals(nulls, pairs.collect((Function<Pair<?, Object>, Object>) Pair::getTwo, Lists.mutable.of()));

        MutableSet<Pair<String, Object>> pairsPlusOne = set.zip(nullsPlusOne);
        Assert.assertEquals(set, pairsPlusOne.collect((Function<Pair<String, ?>, String>) Pair::getOne));
        Assert.assertEquals(nulls, pairsPlusOne.collect((Function<Pair<?, Object>, Object>) Pair::getTwo, Lists.mutable.of()));

        MutableSet<Pair<String, Object>> pairsMinusOne = set.zip(nullsMinusOne);
        Assert.assertEquals(set.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(set.containsAll(pairsMinusOne.collect((Function<Pair<String, ?>, String>) Pair::getOne)));

        Assert.assertEquals(
                set.zip(nulls),
                set.zip(nulls, UnifiedSet.<Pair<String, Object>>newSet()));
    }

    @Test
    public void zipWithIndex()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<Pair<String, Integer>> pairs = set.zipWithIndex();

        Assert.assertEquals(
                set,
                pairs.collect((Function<Pair<String, ?>, String>) Pair::getOne));
        Assert.assertEquals(
                Interval.zeroTo(set.size() - 1).toSet(),
                pairs.collect((Function<Pair<?, Integer>, Integer>) Pair::getTwo));

        Assert.assertEquals(
                set.zipWithIndex(),
                set.zipWithIndex(UnifiedSet.<Pair<String, Integer>>newSet()));
    }

    @Test
    public void testClone()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> clone = set.clone();
        Assert.assertNotSame(clone, set);
        Verify.assertEqualsAndHashCode(clone, set);
    }

    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableSet.class, this.classUnderTest().asUnmodifiable());
    }

    @Test
    public void toImmutable()
    {
        Verify.assertInstanceOf(ImmutableSet.class, this.classUnderTest().toImmutable());
    }

    @Test
    public void min()
    {
        Assert.assertEquals("1", this.classUnderTest().min(String::compareTo));
    }

    @Test
    public void max()
    {
        Assert.assertEquals("1", this.classUnderTest().max(Comparators.reverse(String::compareTo)));
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals("1", this.classUnderTest().min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(String.valueOf(this.classUnderTest().size()), this.classUnderTest().max());
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals("1", this.classUnderTest().minBy(String::valueOf));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(String.valueOf(this.classUnderTest().size()), this.classUnderTest().maxBy(String::valueOf));
    }

    @Test
    public void chunk()
    {
        MutableSet<String> set = this.classUnderTest();
        RichIterable<RichIterable<String>> chunks = set.chunk(2);
        MutableList<Integer> sizes = chunks.collect(RichIterable::size, FastList.<Integer>newList());
        MutableBag<Integer> hashBag = Bags.mutable.of();
        hashBag.addOccurrences(2, this.classUnderTest().size() / 2);
        if (this.classUnderTest().size() % 2 != 0)
        {
            hashBag.add(1);
        }
        Assert.assertEquals(hashBag, sizes.toBag());
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.classUnderTest().chunk(0);
    }

    @Test
    public void chunk_large_size()
    {
        MutableSet<String> set = this.classUnderTest();
        Assert.assertEquals(set, set.chunk(10).getFirst());
    }

    @Test
    public void union()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> union = set.union(UnifiedSet.newSetWith("a", "b", "c", "1"));
        Verify.assertSize(set.size() + 3, union);
        Assert.assertTrue(union.containsAllIterable(Interval.oneTo(set.size()).collect(String::valueOf)));
        Verify.assertContainsAll(union, "a", "b", "c");

        Assert.assertEquals(set, set.union(UnifiedSet.newSetWith("1")));
    }

    @Test
    public void unionInto()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> union = set.unionInto(UnifiedSet.newSetWith("a", "b", "c", "1"), UnifiedSet.<String>newSet());
        Verify.assertSize(set.size() + 3, union);
        Assert.assertTrue(union.containsAllIterable(Interval.oneTo(set.size()).collect(String::valueOf)));
        Verify.assertContainsAll(union, "a", "b", "c");

        Assert.assertEquals(set, set.unionInto(UnifiedSet.newSetWith("1"), UnifiedSet.<String>newSet()));
    }

    @Test
    public void intersect()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> intersect = set.intersect(UnifiedSet.newSetWith("a", "b", "c", "1"));
        Verify.assertSize(1, intersect);
        Assert.assertEquals(UnifiedSet.newSetWith("1"), intersect);

        Verify.assertEmpty(set.intersect(UnifiedSet.newSetWith("not present")));
    }

    @Test
    public void intersectInto()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> intersect = set.intersectInto(UnifiedSet.newSetWith("a", "b", "c", "1"), UnifiedSet.<String>newSet());
        Verify.assertSize(1, intersect);
        Assert.assertEquals(UnifiedSet.newSetWith("1"), intersect);

        Verify.assertEmpty(set.intersectInto(UnifiedSet.newSetWith("not present"), UnifiedSet.<String>newSet()));
    }

    @Test
    public void difference()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> difference = set.difference(UnifiedSet.newSetWith("2", "3", "4", "not present"));
        Assert.assertEquals(UnifiedSet.newSetWith("1"), difference);
        Assert.assertEquals(set, set.difference(UnifiedSet.newSetWith("not present")));
    }

    @Test
    public void differenceInto()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> difference = set.differenceInto(UnifiedSet.newSetWith("2", "3", "4", "not present"), UnifiedSet.<String>newSet());
        Assert.assertEquals(UnifiedSet.newSetWith("1"), difference);
        Assert.assertEquals(set, set.differenceInto(UnifiedSet.newSetWith("not present"), UnifiedSet.<String>newSet()));
    }

    @Test
    public void symmetricDifference()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> difference = set.symmetricDifference(UnifiedSet.newSetWith("2", "3", "4", "5", "not present"));
        Verify.assertContains("1", difference);
        Assert.assertTrue(difference.containsAllIterable(Interval.fromTo(set.size() + 1, 5).collect(String::valueOf)));
        for (int i = 2; i <= set.size(); i++)
        {
            Verify.assertNotContains(String.valueOf(i), difference);
        }

        Verify.assertSize(set.size() + 1, set.symmetricDifference(UnifiedSet.newSetWith("not present")));
    }

    @Test
    public void symmetricDifferenceInto()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> difference = set.symmetricDifferenceInto(
                UnifiedSet.newSetWith("2", "3", "4", "5", "not present"),
                UnifiedSet.<String>newSet());
        Verify.assertContains("1", difference);
        Assert.assertTrue(difference.containsAllIterable(Interval.fromTo(set.size() + 1, 5).collect(String::valueOf)));
        for (int i = 2; i <= set.size(); i++)
        {
            Verify.assertNotContains(String.valueOf(i), difference);
        }

        Verify.assertSize(
                set.size() + 1,
                set.symmetricDifferenceInto(UnifiedSet.newSetWith("not present"), UnifiedSet.<String>newSet()));
    }

    @Test
    public void isSubsetOf()
    {
        MutableSet<String> set = this.classUnderTest();
        Assert.assertTrue(set.isSubsetOf(UnifiedSet.newSetWith("1", "2", "3", "4", "5")));
    }

    @Test
    public void isProperSubsetOf()
    {
        MutableSet<String> set = this.classUnderTest();
        Assert.assertTrue(set.isProperSubsetOf(UnifiedSet.newSetWith("1", "2", "3", "4", "5")));
        Assert.assertFalse(set.isProperSubsetOf(set));
    }

    @Test
    public void powerSet()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<UnsortedSetIterable<String>> powerSet = set.powerSet();
        Verify.assertSize((int) StrictMath.pow(2, set.size()), powerSet);
        Verify.assertContains(UnifiedSet.<String>newSet(), powerSet);
        Verify.assertContains(set, powerSet);
    }

    @Test
    public void cartesianProduct()
    {
        MutableSet<String> set = this.classUnderTest();
        LazyIterable<Pair<String, String>> cartesianProduct = set.cartesianProduct(UnifiedSet.newSetWith("One", "Two"));
        Verify.assertIterableSize(set.size() * 2, cartesianProduct);
        Assert.assertEquals(
                set,
                cartesianProduct
                        .select(Predicates.attributeEqual((Function<Pair<?, String>, String>) Pair::getTwo, "One"))
                        .collect((Function<Pair<String, ?>, String>) Pair::getOne).toSet());
    }

    @Test
    public void with()
    {
        MutableSet<String> set = this.classUnderTest();
        Assert.assertFalse(set.contains("11"));
        MutableSet<String> setWith = set.with("11");
        Assert.assertTrue(setWith.containsAll(set));
        Assert.assertTrue(setWith.contains("11"));
        Assert.assertSame(setWith, setWith.with("11"));
        assertSetType(set, setWith);
    }

    @Test
    public void withAll()
    {
        MutableSet<String> set = this.classUnderTest();
        Verify.assertContainsNone(set, "11", "12");
        MutableSet<String> setWith = set.withAll(FastList.newListWith("11", "12"));
        Assert.assertTrue(setWith.containsAll(set));
        Verify.assertContainsAll(setWith, "11", "12");
        assertSetType(set, setWith);
        Assert.assertSame(setWith, setWith.withAll(FastList.<String>newList()));
    }

    @Test
    public void without()
    {
        MutableSet<String> set = this.classUnderTest();
        Assert.assertSame(set, set.without("11"));
        MutableList<String> list = set.toList();
        list.forEach(Procedures.cast(each -> {
            MutableSet<String> setWithout = set.without(each);
            Assert.assertFalse(setWithout.contains(each));
            assertSetType(set, setWithout);
        }));
    }

    @Test
    public void withoutAll()
    {
        MutableSet<String> set = this.classUnderTest().with("11").with("12");
        MutableSet<String> setWithout = set.withoutAll(FastList.newListWith("11", "12"));
        Assert.assertTrue(setWithout.containsAll(this.classUnderTest()));
        Verify.assertContainsNone(setWithout, "11", "12");
        assertSetType(set, setWithout);
        Assert.assertSame(setWithout, setWithout.withoutAll(FastList.<String>newList()));
    }

    protected static void assertSetType(MutableSet<?> original, MutableSet<?> modified)
    {
        if (original instanceof FixedSizeSet && modified.size() < 5)
        {
            Verify.assertInstanceOf(FixedSizeSet.class, modified);
        }
        else
        {
            Verify.assertInstanceOf(UnifiedSet.class, modified);
        }
    }
}
