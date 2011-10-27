/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.StringPredicates;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.StringIterate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableRichIterable}.
 */
public class UnmodifiableRichIterableTest
{
    private static final String METALLICA = "Metallica";

    private RichIterable<String> mutableCollection;
    private RichIterable<String> unmodifiableCollection;

    private RichIterable<Integer> newWith(Integer... elements)
    {
        return UnmodifiableRichIterable.of(Lists.mutable.of(elements));
    }

    @Before
    public void setUp()
    {
        this.mutableCollection = Lists.mutable.of(METALLICA, "Bon Jovi", "Europe", "Scorpions");
        this.unmodifiableCollection = UnmodifiableRichIterable.of(this.mutableCollection);
    }

    @Test
    public void testDelegatingMethods()
    {
        Assert.assertEquals(this.mutableCollection.notEmpty(), this.unmodifiableCollection.notEmpty());
        Assert.assertEquals(this.mutableCollection.isEmpty(), this.unmodifiableCollection.isEmpty());
        Assert.assertEquals(this.mutableCollection.size(), this.unmodifiableCollection.size());
        Assert.assertEquals(this.mutableCollection.getFirst(), this.unmodifiableCollection.getFirst());
        Assert.assertEquals(this.mutableCollection.getLast(), this.unmodifiableCollection.getLast());
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.unmodifiableCollection.allSatisfy(Predicates.notNull()));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.unmodifiableCollection.anySatisfy(StringPredicates.contains("allic")));
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(METALLICA, this.unmodifiableCollection.detect(StringPredicates.contains("allic")));
        Assert.assertEquals("Not found", this.unmodifiableCollection.detectIfNone(StringPredicates.contains("donna"),
                new PassThruFunction0<String>("Not found")));
    }

    @Test
    public void chunk()
    {
        Assert.assertEquals(
                this.mutableCollection.chunk(1).toList(),
                this.unmodifiableCollection.chunk(1).toList());
    }

    @Test
    public void converters()
    {
        Assert.assertEquals(
                this.mutableCollection.toBag(),
                this.unmodifiableCollection.toBag());
        Assert.assertEquals(
                this.mutableCollection.asLazy().toBag(),
                this.unmodifiableCollection.asLazy().toBag());
        Assert.assertArrayEquals(
                this.mutableCollection.toArray(),
                this.unmodifiableCollection.toArray());
        Assert.assertArrayEquals(
                this.mutableCollection.toArray(new String[0]),
                this.unmodifiableCollection.toArray(new String[0]));
        Assert.assertEquals(this.mutableCollection.toList(), this.unmodifiableCollection.toList());
        Verify.assertListsEqual(Lists.mutable.of("Bon Jovi", "Europe", METALLICA, "Scorpions"),
                this.unmodifiableCollection
                        .toSortedList());
        Verify.assertListsEqual(Lists.mutable.of("Scorpions", METALLICA, "Europe", "Bon Jovi"),
                this.unmodifiableCollection
                        .toSortedList(Collections.reverseOrder()));
        Verify.assertListsEqual(Lists.mutable.of("Bon Jovi", "Europe", METALLICA, "Scorpions"),
                this.unmodifiableCollection
                        .toSortedListBy(Functions.getStringPassThru()));
        Verify.assertSize(4, this.unmodifiableCollection.toSet());
        Verify.assertSize(4, this.unmodifiableCollection.toMap(Functions.getStringPassThru(), Functions.getStringPassThru()));
    }

    @Test
    public void groupByEach()
    {
        Function<String, Set<Character>> lowerCaseSetFunction = new Function<String, Set<Character>>()
        {
            public Set<Character> valueOf(String name)
            {
                return StringIterate.asLowercaseSet(name);
            }
        };

        Assert.assertEquals(
                this.mutableCollection.groupByEach(lowerCaseSetFunction),
                this.unmodifiableCollection.groupByEach(lowerCaseSetFunction));
        Assert.assertEquals(
                this.mutableCollection.groupByEach(lowerCaseSetFunction, FastListMultimap.<Character, String>newMultimap()),
                this.unmodifiableCollection.groupByEach(lowerCaseSetFunction, FastListMultimap.<Character, String>newMultimap()));
    }

    @Test
    public void groupBy()
    {
        Assert.assertEquals(
                this.mutableCollection.groupBy(Functions.getStringPassThru()),
                this.unmodifiableCollection.groupBy(Functions.getStringPassThru()));
        Assert.assertEquals(
                this.mutableCollection.groupBy(Functions.getStringPassThru(), FastListMultimap.<String, String>newMultimap()),
                this.unmodifiableCollection.groupBy(Functions.getStringPassThru(), FastListMultimap.<String, String>newMultimap()));
    }

    @Test
    public void collectIf()
    {
        Assert.assertEquals(
                this.mutableCollection.collectIf(Predicates.alwaysTrue(), Functions.getStringPassThru()),
                this.unmodifiableCollection.collectIf(Predicates.alwaysTrue(), Functions.getStringPassThru()));
        Assert.assertEquals(
                this.mutableCollection.collectIf(Predicates.alwaysTrue(), Functions.getStringPassThru(), Lists.mutable.<String>of()),
                this.unmodifiableCollection.collectIf(Predicates.alwaysTrue(), Functions.getStringPassThru(), Lists.mutable.<String>of()));
    }

    @Test
    public void collectWith()
    {
        Function2<String, Object, String> function = new Function2<String, Object, String>()
        {
            public String value(String each, Object parm)
            {
                return each;
            }
        };
        Assert.assertEquals(
                this.mutableCollection.collectWith(function, null, Lists.mutable.<String>of()),
                this.unmodifiableCollection.collectWith(function, null, Lists.mutable.<String>of()));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(
                this.mutableCollection.collect(Functions.getStringPassThru()),
                this.unmodifiableCollection.collect(Functions.getStringPassThru()));
        Assert.assertEquals(
                this.mutableCollection.collect(Functions.getStringPassThru(), Lists.mutable.<String>of()),
                this.unmodifiableCollection.collect(Functions.getStringPassThru(), Lists.mutable.<String>of()));
    }

    @Test
    public void rejectWith()
    {
        Assert.assertEquals(
                this.mutableCollection.rejectWith(Predicates2.alwaysFalse(), null, Lists.mutable.<String>of()),
                this.unmodifiableCollection.rejectWith(Predicates2.alwaysFalse(), null, Lists.mutable.<String>of()));
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(
                this.mutableCollection.reject(Predicates.alwaysFalse()),
                this.unmodifiableCollection.reject(Predicates.alwaysFalse()));
        Assert.assertEquals(
                this.mutableCollection.reject(Predicates.alwaysFalse(), Lists.mutable.<String>of()),
                this.unmodifiableCollection.reject(Predicates.alwaysFalse(), Lists.mutable.<String>of()));
    }

    @Test
    public void selectWith()
    {
        Assert.assertEquals(
                this.mutableCollection.selectWith(Predicates2.alwaysTrue(), null, Lists.mutable.<String>of()),
                this.unmodifiableCollection.selectWith(Predicates2.alwaysTrue(), null, Lists.mutable.<String>of()));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(
                this.mutableCollection.select(Predicates.alwaysTrue()),
                this.unmodifiableCollection.select(Predicates.alwaysTrue()));
        Assert.assertEquals(
                this.mutableCollection.select(Predicates.alwaysTrue(), Lists.mutable.<String>of()),
                this.unmodifiableCollection.select(Predicates.alwaysTrue(), Lists.mutable.<String>of()));
    }

    @Test
    public void partition()
    {
        PartitionIterable<String> partition = this.mutableCollection.partition(Predicates.alwaysTrue());
        PartitionIterable<String> unmodifiablePartition = this.unmodifiableCollection.partition(Predicates.alwaysTrue());
        Assert.assertEquals(
                partition.getSelected(),
                unmodifiablePartition.getSelected());
        Assert.assertEquals(
                partition.getRejected(),
                unmodifiablePartition.getRejected());
    }

    @Test
    public void count()
    {
        Assert.assertEquals(this.mutableCollection.count(Predicates.alwaysTrue()),
                this.unmodifiableCollection.count(Predicates.alwaysTrue()));
    }

    @Test
    public void testIntejectInto()
    {
        Function2<String, String, String> function = new Function2<String, String, String>()
        {
            public String value(String injectValue, String band)
            {
                return injectValue + band.charAt(0);
            }
        };
        Assert.assertEquals(">MBES", this.unmodifiableCollection.injectInto(">", function));
    }

    @Test
    public void testIterator()
    {
        Counter counter = new Counter();
        for (String each : this.unmodifiableCollection)
        {
            counter.increment();
        }
        Assert.assertEquals(4, counter.getCount());
    }

    @Test
    public void testForEach()
    {
        final Counter counter = new Counter();
        this.unmodifiableCollection.forEach(new Procedure<String>()
        {
            public void value(String band)
            {
                counter.increment();
            }
        });
        Assert.assertEquals(4, counter.getCount());
    }

    @Test
    public void testForEachWith()
    {
        final StringBuilder buf = new StringBuilder();
        this.unmodifiableCollection.forEachWith(new Procedure2<String, String>()
        {
            public void value(String band, String param)
            {
                buf.append(param).append('<').append(band).append('>');
            }
        }, "GreatBand");
        Assert.assertEquals("GreatBand<Metallica>GreatBand<Bon Jovi>GreatBand<Europe>GreatBand<Scorpions>", buf.toString());
    }

    @Test
    public void testForEachWithIndex()
    {
        final Counter counter = new Counter();
        this.unmodifiableCollection.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String band, int index)
            {
                counter.add(index);
            }
        });
        Assert.assertEquals(6, counter.getCount());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals(this.mutableCollection.toString(), this.unmodifiableCollection.toString());
    }

    @Test
    public void testMakeString()
    {
        Assert.assertEquals(this.mutableCollection.makeString(), this.unmodifiableCollection.makeString());
    }

    @Test
    public void testAppendString()
    {
        Appendable mutableBuilder = new StringBuilder();
        this.mutableCollection.appendString(mutableBuilder);

        Appendable unmodifiableBuilder = new StringBuilder();
        this.unmodifiableCollection.appendString(unmodifiableBuilder);

        Assert.assertEquals(mutableBuilder.toString(), unmodifiableBuilder.toString());
    }

    @Test
    public void zip()
    {
        List<Object> nulls = Collections.nCopies(this.unmodifiableCollection.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(this.unmodifiableCollection.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(this.unmodifiableCollection.size() - 1, null);

        RichIterable<Pair<String, Object>> pairs = this.unmodifiableCollection.zip(nulls);
        Assert.assertEquals(
                this.unmodifiableCollection.toSet(),
                pairs.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(
                nulls,
                pairs.collect(Functions.secondOfPair(), Lists.mutable.of()));

        RichIterable<Pair<String, Object>> pairsPlusOne = this.unmodifiableCollection.zip(nullsPlusOne);
        Assert.assertEquals(
                this.unmodifiableCollection.toSet(),
                pairsPlusOne.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(nulls, pairsPlusOne.collect(Functions.secondOfPair(), Lists.mutable.of()));

        RichIterable<Pair<String, Object>> pairsMinusOne = this.unmodifiableCollection.zip(nullsMinusOne);
        Assert.assertEquals(this.unmodifiableCollection.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(this.unmodifiableCollection.containsAllIterable(pairsMinusOne.collect(Functions.<String>firstOfPair())));

        Assert.assertEquals(
                this.unmodifiableCollection.zip(nulls).toSet(),
                this.unmodifiableCollection.zip(nulls, Sets.mutable.<Pair<String, Object>>of()));
    }

    @Test
    public void zipWithIndex()
    {
        RichIterable<Pair<String, Integer>> pairs = this.unmodifiableCollection.zipWithIndex();

        Assert.assertEquals(
                this.unmodifiableCollection.toSet(),
                pairs.collect(Functions.<String>firstOfPair()).toSet());
        Assert.assertEquals(
                Interval.zeroTo(this.unmodifiableCollection.size() - 1).toSet(),
                pairs.collect(Functions.<Integer>secondOfPair(), Sets.mutable.<Integer>of()));

        Assert.assertEquals(
                this.unmodifiableCollection.zipWithIndex().toSet(),
                this.unmodifiableCollection.zipWithIndex(Sets.mutable.<Pair<String, Integer>>of()));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws()
    {
        this.newWith().min(Comparators.naturalOrder());
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws()
    {
        this.newWith().max(Comparators.naturalOrder());
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws()
    {
        this.newWith(1, null, 2).min(Comparators.naturalOrder());
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws()
    {
        this.newWith(1, null, 2).max(Comparators.naturalOrder());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1, 3, 2).min(Comparators.naturalOrder()));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 3, 2).max(Comparators.naturalOrder()));
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1, 3, 2).minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 3, 2).maxBy(Functions.getToString()));
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws_without_comparator()
    {
        this.newWith(1, null, 2).min();
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws_without_comparator()
    {
        this.newWith(1, null, 2).max();
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(3, 1, 2).min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newWith(1, 3, 2).max());
    }

    @Test
    public void toSortedSet()
    {
        RichIterable<Integer> integers = this.newWith(4, 3, 1, 2);
        MutableSortedSet<Integer> set = integers.toSortedSet();
        Verify.assertSortedSetsEqual(SortedSets.mutable.of(1, 2, 3, 4), set);
    }

    @Test
    public void toSortedSet_with_comparator()
    {
        RichIterable<Integer> integers = this.newWith(2, 4, 4, 2, 1, 4, 1, 3);
        MutableSortedSet<Integer> set = integers.toSortedSet(Collections.<Integer>reverseOrder());
        Verify.assertSortedSetsEqual(SortedSets.mutable.of(Collections.<Integer>reverseOrder(), 1, 2, 3, 4), set);
    }

    @Test
    public void toSortedSetBy()
    {
        RichIterable<Integer> integers = this.newWith(2, 4, 1, 3);
        MutableSortedSet<Integer> set = integers.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(SortedSets.mutable.of(1, 2, 3, 4), set);
    }
}
