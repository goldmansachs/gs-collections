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

package com.gs.collections.impl.bag.mutable;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.collection.mutable.AbstractCollectionTestCase;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link MultiReaderHashBag}.
 */
public class MultiReaderHashBagTest extends AbstractCollectionTestCase
{
    @Override
    protected <T> MultiReaderHashBag<T> classUnderTest()
    {
        return MultiReaderHashBag.newBag();
    }

    @Override
    @Test
    public void newEmpty()
    {
        Verify.assertInstanceOf(MultiReaderHashBag.class, MultiReaderHashBag.newBag().newEmpty());
        Verify.assertEmpty(MultiReaderHashBag.<Integer>newBagWith(null, null).newEmpty());
    }

    @Test
    public void hashBagNewWith()
    {
        Assert.assertEquals(
                HashBag.newBagWith("Alice", "Bob", "Bob", "Bob", "Cooper", "Dio"),
                HashBag.newBagWith("Alice", "Bob", "Cooper", "Dio", "Bob", "Bob"));
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedBag.class, MultiReaderHashBag.newBag().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableBag.class, this.classUnderTest().asUnmodifiable());
    }

    @Override
    @Test
    public void toImmutable()
    {
        Verify.assertInstanceOf(ImmutableBag.class, this.classUnderTest().toImmutable());
    }

    @Test
    public void addOccurrences()
    {
        MultiReaderHashBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 2, 3);
        bag.addOccurrences(1, 2);
        bag.addOccurrences(4, 2);
        bag.addOccurrences(2, 1);
        MutableBagTestCase.assertBagsEqual(HashBag.newBagWith(1, 1, 1, 1, 2, 2, 3, 4, 4), bag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOccurrences_throws()
    {
        this.classUnderTest().addOccurrences(new Object(), -1);
    }

    @Test
    public void removeOccurrences()
    {
        MultiReaderHashBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 1, 1, 2, 2, 3);
        Assert.assertFalse(bag.removeOccurrences(4, 2));
        MutableBagTestCase.assertBagsEqual(HashBag.newBagWith(1, 1, 1, 1, 2, 2, 3), bag);

        bag.removeOccurrences(1, 3);
        bag.removeOccurrences(3, 1);
        MutableBagTestCase.assertBagsEqual(HashBag.newBagWith(1, 2, 2), bag);
    }

    @Test
    public void occurrencesOf()
    {
        MultiReaderHashBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 2);
        Assert.assertEquals(2, bag.occurrencesOf(1));
        Assert.assertEquals(1, bag.occurrencesOf(2));
    }

    @Test
    public void sizeDistinct()
    {
        MultiReaderHashBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 2, 2, 3);
        Assert.assertEquals(3, bag.sizeDistinct());
    }

    @Override
    @Test
    public void collect()
    {
        MutableBag<Boolean> bag = MultiReaderHashBag.newBagWith(Boolean.TRUE, Boolean.FALSE, null);
        MutableBag<String> newCollection = bag.collect(Functions.getToString());
        Assert.assertEquals(HashBag.newBagWith("true", "false", "null"), newCollection);
    }

    @Override
    @Test
    public void flatCollect()
    {
        MutableBag<Integer> collection = MultiReaderHashBag.newBagWith(1, 1, 2, 3, 4);
        Function<Integer, MutableBag<String>> function =
                new Function<Integer, MutableBag<String>>()
                {
                    public MutableBag<String> valueOf(Integer object)
                    {
                        return HashBag.newBagWith(String.valueOf(object));
                    }
                };

        MutableBagTestCase.assertBagsEqual(
                HashBag.newBagWith("1", "1", "2", "3", "4"),
                collection.flatCollect(function));
    }

    @Override
    @Test
    public void collectIf()
    {
        Assert.assertEquals(
                HashBag.newBagWith("1", "1", "2", "3"),
                MultiReaderHashBag.newBagWith(1, 1, 2, 3).collectIf(
                        Predicates.instanceOf(Integer.class),
                        Functions.getToString()));
        Assert.assertEquals(
                HashBag.newBagWith("1", "1"),
                MultiReaderHashBag.newBagWith(1, 1, 2, 3).collectIf(
                        Predicates.lessThan(2),
                        Functions.getToString()));
    }

    @Override
    @Test
    public void collectWith()
    {
        Function2<Integer, Integer, Integer> addZeroFunction =
                new Function2<Integer, Integer, Integer>()
                {
                    public Integer value(Integer each, Integer parameter)
                    {
                        return each + parameter;
                    }
                };
        Verify.assertContainsAll(MultiReaderHashBag.newBagWith(1, 1, 2, 3).collectWith(addZeroFunction, 0), 1, 2, 3);
        Verify.assertContainsAll(
                MultiReaderHashBag.newBagWith(1, 1, 2, 3).collectWith(addZeroFunction,
                        0,
                        HashBag.<Integer>newBag()), 1, 2, 3);
    }

    @Override
    @Test
    public void reject()
    {
        Verify.assertContainsAll(MultiReaderHashBag.newBagWith(1, 1, 2, 3, 4).reject(Predicates.lessThan(3)), 3, 4);
        Verify.assertContainsAll(MultiReaderHashBag.newBagWith(1, 2, 3, 3, 4).reject(
                Predicates.lessThan(3),
                HashBag.<Integer>newBag()), 3, 4);
    }

    @Override
    @Test
    public void rejectWith()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 1);
        MutableBag<Integer> results = bag.rejectWith(Predicates2.instanceOf(), Integer.class);
        Verify.assertEmpty(results);
    }

    @Override
    @Test
    public void select()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 2, 3, 4, 5, 5, 1);
        MutableBag<Integer> results = bag.select(Predicates.equal(1));
        MutableBagTestCase.assertBagsEqual(results, MultiReaderHashBag.newBagWith(1, 1));
    }

    @Override
    @Test
    public void selectWith()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 2, 2);
        MutableBag<Integer> results = bag.selectWith(Predicates2.instanceOf(), Integer.class);
        Verify.assertSize(4, results);
    }

    @Test
    public void selectByOccurrences()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 2, 2, 2, 3);
        MutableBag<Integer> results = bag.selectByOccurrences(IntPredicates.isEven());
        Verify.assertSize(2, results);
        MutableBagTestCase.assertBagsEqual(results, MultiReaderHashBag.newBagWith(1, 1));
    }

    @Override
    @Test
    public void selectInstancesOf()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 2, 2, 2, 3);
        MutableBagTestCase.assertBagsEqual(bag.selectInstancesOf(Integer.class), MultiReaderHashBag.newBagWith(1, 1, 2, 2, 2, 3));
    }

    @Override
    @Test
    public void partition()
    {
        MutableBag<Integer> integers = MultiReaderHashBag.newBagWith(-3, -2, -1, 0, 1, 2, 2, 2, 3, 3, 4, 5);
        PartitionMutableCollection<Integer> result = integers.partition(IntegerPredicates.isEven());
        Assert.assertEquals(MultiReaderHashBag.newBagWith(-2, 0, 2, 2, 2, 4), result.getSelected());
        Assert.assertEquals(MultiReaderHashBag.newBagWith(-3, -1, 1, 3, 3, 5), result.getRejected());
    }

    @Override
    @Test
    public void with()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 3, 3);
        MutableBag<Integer> bagWith = bag.with(3);
        MutableBagTestCase.assertBagsEqual(MultiReaderHashBag.newBagWith(1, 2, 3, 3, 3), bagWith);
    }

    @Override
    @Test
    public void without()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 3, 3);
        MutableBag<Integer> bagWithout = bag.without(3);
        MutableBagTestCase.assertBagsEqual(MultiReaderHashBag.newBagWith(1, 2, 3), bagWithout);
    }

    @Override
    @Test
    public void withAll()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 3, 3);
        MutableBag<Integer> bagWith = bag.withAll(FastList.newListWith(2, 4, 4));
        MutableBagTestCase.assertBagsEqual(MultiReaderHashBag.newBagWith(1, 2, 2, 3, 3, 4, 4), bagWith);
    }

    @Override
    @Test
    public void withoutAll()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 3, 3, 4);
        MutableBag<Integer> bagWithout = bag.withoutAll(FastList.newListWith(3, 4));
        MutableBagTestCase.assertBagsEqual(MultiReaderHashBag.newBagWith(1, 2), bagWithout);
    }

    @Test
    public void toMapOfItemToCount()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 2, 3, 3, 3);
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, 1, 2, 2, 3, 3), bag.toMapOfItemToCount());
    }

    @Test
    public void toStringOfItemToCount()
    {
        Assert.assertEquals("{}", MultiReaderHashBag.newBagWith().toStringOfItemToCount());
        Assert.assertEquals("{1=3}", MultiReaderHashBag.newBagWith(1, 1, 1).toStringOfItemToCount());
        String actual = MultiReaderHashBag.newBagWith(1, 2, 2).toStringOfItemToCount();
        Assert.assertTrue("{1=1, 2=2}".equals(actual) || "{2=2, 1=1}".equals(actual));
    }

    @Test
    public void forEachWithOccurrences()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 2, 2, 3, 3, 3);
        final int[] sum = new int[1];
        bag.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int occurrences)
            {
                if (occurrences > 1)
                {
                    sum[0] += each * occurrences;
                }
            }
        });

        Assert.assertEquals(13, sum[0]);
    }

    @Test
    public void equalsAndHashCose()
    {
        MutableBag<Integer> integers = MultiReaderHashBag.newBagWith(1, 2, 3);
        MutableBag<Integer> integers2 = MultiReaderHashBag.newBagWith(1, 2, 3);
        MutableBag<Integer> integers3 = MultiReaderHashBag.newBagWith(1, null, 3, 4, 5);
        MutableBag<Integer> integers4 = MultiReaderHashBag.newBagWith(1, null, 3, 4, 5);
        MutableBag<Integer> integers5 = MultiReaderHashBag.newBagWith(1, null, 3);
        MutableBag<Integer> randomAccessList = Bags.mutable.of(1, 2, 3);
        MutableBag<Integer> randomAccessList2 = Bags.mutable.of(2, 3, 4);
        Verify.assertEqualsAndHashCode(integers, integers);
        Verify.assertPostSerializedEqualsAndHashCode(integers);
        Verify.assertEqualsAndHashCode(integers, integers2);
        Verify.assertEqualsAndHashCode(integers, randomAccessList);
        Assert.assertNotEquals(integers, integers3);
        Assert.assertNotEquals(integers, integers5);
        Assert.assertNotEquals(integers, randomAccessList2);
        Assert.assertNotEquals(integers, Sets.fixedSize.of());
        Verify.assertEqualsAndHashCode(integers3, integers4);
        Assert.assertEquals(integers, integers2);
        Assert.assertNotEquals(integers, integers3);
    }

    @Override
    @Test
    public void toSet()
    {
        super.toSet();
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(3, 3, 3, 2, 2, 1);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3), bag.toSet());
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 1);
        Assert.assertEquals(FastList.newListWith(1, 1, 1), bag.toList());
    }

    @Override
    @Test
    public void injectInto()
    {
        MutableBag<Integer> bag = MultiReaderHashBag.newBagWith(1, 1, 3);
        Assert.assertEquals(Integer.valueOf(6), bag.injectInto(1, AddFunction.INTEGER));
    }

    @Override
    @Test
    public void forEach()
    {
        MutableBag<Integer> result = HashBag.newBag();
        MutableBag<Integer> collection = MultiReaderHashBag.newBagWith(1, 2, 3, 4, 4);
        collection.forEach(CollectionAddProcedure.<Integer>on(result));
        Assert.assertEquals(HashBag.newBagWith(1, 2, 3, 4, 4), result);
    }

    @Override
    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(MultiReaderHashBag.newBag());
        Verify.assertNotEmpty(MultiReaderHashBag.newBagWith(1, 1));
    }

    @Test
    public void serialization()
    {
        MutableBag<Integer> collection = MultiReaderHashBag.newBagWith(1, 1, 3, 4, 5);
        MutableBag<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Assert.assertEquals(collection, deserializedCollection);
    }

    private void verifyDelegateIsUnmodifiable(final MutableBag<Integer> delegate)
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                delegate.add(2);
            }
        });
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                delegate.remove(0);
            }
        });
    }

    @Test
    public void withReadLockAndDelegate()
    {
        MultiReaderHashBag<Integer> bag = MultiReaderHashBag.newBagWith(1);
        final Object[] result = new Object[1];
        bag.withReadLockAndDelegate(new Procedure<MutableBag<Integer>>()
        {
            public void value(MutableBag<Integer> delegate)
            {
                result[0] = delegate.getFirst();
                MultiReaderHashBagTest.this.verifyDelegateIsUnmodifiable(delegate);
            }
        });
        Assert.assertNotNull(result[0]);
    }

    @Override
    @Test
    public void makeString()
    {
        Assert.assertEquals("[1, 1, 2, 3]", MultiReaderHashBag.newBagWith(1, 1, 2, 3).toString());
    }

    @Override
    @Test
    public void appendString()
    {
        Appendable builder = new StringBuilder();
        MultiReaderHashBag.newBagWith(1, 1, 2, 3).appendString(builder);
        Assert.assertEquals("1, 1, 2, 3", builder.toString());
    }

    @Override
    @Test
    public void testToString()
    {
        Assert.assertEquals("[1, 1, 2, 3]", MultiReaderHashBag.newBagWith(1, 1, 2, 3).toString());
    }

    @Override
    @Test
    public void iterator()
    {
        final MultiReaderHashBag<Integer> integers = MultiReaderHashBag.newBagWith(1, 1, 2, 3, 4);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                integers.iterator();
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listIterator()
    {
        MultiReaderHashBag<Integer> integers = MultiReaderHashBag.newBagWith(1, 1, 2, 3, 4);
        integers.iterator();
    }

    @Test
    public void withWriteLockAndDelegate()
    {
        MultiReaderHashBag<Integer> bag = MultiReaderHashBag.newBagWith(2);
        final AtomicReference<MutableBag<?>> delegateList = new AtomicReference<MutableBag<?>>();
        final AtomicReference<Iterator<?>> iterator = new AtomicReference<Iterator<?>>();
        bag.withWriteLockAndDelegate(new Procedure<MutableBag<Integer>>()
        {
            public void value(MutableBag<Integer> delegate)
            {
                delegate.add(1);
                delegate.add(2);
                delegate.add(3);
                delegate.add(4);
                delegateList.set(delegate);
                iterator.set(delegate.iterator());
            }
        });
        Assert.assertEquals(HashBag.newBagWith(1, 2, 2, 3, 4), bag);

        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                iterator.get().hasNext();
            }
        });

        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                delegateList.get().iterator();
            }
        });
    }

    @Test
    public void concurrentWrite()
    {
        final MultiReaderHashBag<Integer> numbers = this.classUnderTest();
        Interval interval = Interval.oneTo(100);
        ParallelIterate.forEach(interval, new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                numbers.add(each);
                Verify.assertSize(1, numbers.select(Predicates.equal(each)));
                numbers.add(each);
                Assert.assertEquals(2, numbers.count(Predicates.equal(each)));
                numbers.add(each);
                final Integer[] removed = new Integer[1];
                numbers.withWriteLockAndDelegate(new Procedure<MutableBag<Integer>>()
                {
                    public void value(MutableBag<Integer> bag)
                    {
                        Iterator<Integer> iterator = bag.iterator();
                        removed[0] = iterator.next();
                        bag.remove(removed[0]);
                        bag.add(removed[0]);
                    }
                });
                numbers.add(each);
                Assert.assertEquals(4, numbers.count(Predicates.equal(each)));
            }
        }, 1);

        interval.forEach(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                Assert.assertEquals(4, numbers.occurrencesOf(each));
            }
        });
    }

    @Test
    public void parallelCollect()
    {
        MultiReaderHashBag<String> numbers = this.classUnderTest();
        Interval interval = Interval.oneTo(50000);
        ParallelIterate.collect(interval, Functions.getToString(), numbers, true);
        Assert.assertEquals(numbers, interval.collect(Functions.getToString()).toBag());
    }
}
