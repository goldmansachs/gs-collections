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

package com.gs.collections.impl.collection.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.list.mutable.ArrayListAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.ListAdapter;
import com.gs.collections.impl.list.mutable.RandomAccessListAdapter;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.SetAdapter;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link CollectionAdapter}.
 */
public class CollectionAdapterTest extends AbstractCollectionTestCase
{
    @Override
    protected <T> CollectionAdapter<T> newWith(T... littleElements)
    {
        return new CollectionAdapter<>(new ArrayList<>(FastList.newListWith(littleElements)));
    }

    private <T> CollectionAdapter<T> newSet()
    {
        return new CollectionAdapter<>(UnifiedSet.<T>newSet());
    }

    private <T> CollectionAdapter<T> newList()
    {
        return new CollectionAdapter<>(FastList.<T>newList());
    }

    @Test(expected = NullPointerException.class)
    public void null_throws()
    {
        new CollectionAdapter<>(null);
    }

    @Override
    @Test
    public void asSynchronized()
    {
        MutableCollection<String> collection = CollectionAdapter.adapt(Maps.mutable.of("1", "1").values());
        MutableCollection<String> asSynchronized = collection.asSynchronized();
        Verify.assertInstanceOf(AbstractSynchronizedMutableCollection.class, asSynchronized);
        Verify.assertInstanceOf(SynchronizedMutableCollection.class, asSynchronized);
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableCollection.class, this.newWith().asUnmodifiable());
    }

    @Override
    @Test
    public void toImmutable()
    {
        super.toImmutable();
        Verify.assertInstanceOf(ImmutableList.class, new CollectionAdapter<>(Collections.singletonList("1")).toImmutable());
        Verify.assertInstanceOf(ImmutableSet.class, new CollectionAdapter<>(Collections.singleton("1")).toImmutable());
    }

    @Override
    @Test
    public void select()
    {
        super.select();
        Verify.assertContainsAll(this.<Integer>newSet().with(1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 1, 2);
        Verify.assertContainsAll(this.<Integer>newSet().with(-1, 2, 3, 4, 5).select(
                Predicates.lessThan(3),
                FastList.<Integer>newList()), -1, 2);
    }

    @Override
    @Test
    public void newEmpty()
    {
        Verify.assertInstanceOf(FastList.class, CollectionAdapter.adapt(new LinkedList<>()).newEmpty());
    }

    @Override
    @Test
    public void reject()
    {
        super.reject();
        Verify.assertContainsAll(this.<Integer>newSet().with(1, 2, 3, 4).reject(Predicates.lessThan(3)), 3, 4);
        Verify.assertContainsAll(this.<Integer>newSet().with(1, 2, 3, 4).reject(Predicates.lessThan(3), FastList.<Integer>newList()), 3, 4);
    }

    @Override
    @Test
    public void selectInstancesOf()
    {
        super.selectInstancesOf();

        MutableCollection<Number> numbers = this.<Number>newSet().with(1, 2.0, 3, 4.0, 5);
        MutableCollection<Integer> integers = numbers.selectInstancesOf(Integer.class);
        Assert.assertEquals(HashBag.newBagWith(1, 3, 5), integers.toBag());
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();

        Assert.assertEquals(
                UnifiedSet.newSetWith("1", "2", "3", "4"),
                this.newSet().with(1, 2, 3, 4).collect(String::valueOf));
        Assert.assertEquals(
                UnifiedSet.newSetWith("1", "2", "3", "4"),
                this.newSet().with(1, 2, 3, 4).collect(
                        String::valueOf,
                        UnifiedSet.<String>newSet()));
    }

    @Override
    @Test
    public void flatCollect()
    {
        super.flatCollect();

        Function<Integer, Iterable<Integer>> function = Interval::oneTo;
        Assert.assertEquals(
                FastList.newListWith(1, 1, 2, 1, 2, 3, 1, 2, 3, 4),
                this.<Integer>newList().with(1, 2, 3, 4).flatCollect(function));
        Assert.assertEquals(
                FastList.newListWith(1, 1, 2, 1, 2, 3, 1, 2, 3, 4),
                this.<Integer>newList().with(1, 2, 3, 4).flatCollect(function));
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        MutableCollection<Integer> list1 = this.<Integer>newList().with(1, 2, 3);
        MutableCollection<Integer> list2 = this.<Integer>newList().with(1, 2, 3);
        MutableCollection<Integer> list3 = this.<Integer>newList().with(2, 3, 4);
        Verify.assertEqualsAndHashCode(list1, list2);
        Assert.assertNotEquals(list1, null);
        Assert.assertNotEquals(list2, list3);
    }

    @Test
    public void newListWithSize()
    {
        Collection<Integer> collection = this.<Integer>newList().with(1, 2, 3);
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void serialization()
    {
        MutableCollection<Integer> collection = this.<Integer>newList().with(1, 2, 3, 4, 5);
        MutableCollection<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Verify.assertContainsAll(deserializedCollection, 1, 2, 3, 4, 5);
        Assert.assertEquals(collection, deserializedCollection);
    }

    @Test
    public void adapt()
    {
        Verify.assertInstanceOf(FastList.class, CollectionAdapter.adapt(FastList.newList()));
        Verify.assertInstanceOf(ArrayListAdapter.class, CollectionAdapter.adapt(new ArrayList<>()));
        Verify.assertInstanceOf(SetAdapter.class, CollectionAdapter.adapt(new HashSet<>()));
        Verify.assertInstanceOf(UnifiedSet.class, CollectionAdapter.adapt(UnifiedSet.newSet()));
        Verify.assertInstanceOf(RandomAccessListAdapter.class, CollectionAdapter.adapt(Collections.emptyList()));
        Verify.assertInstanceOf(ListAdapter.class, CollectionAdapter.adapt(new LinkedList<>()));
        Verify.assertInstanceOf(ArrayAdapter.class, CollectionAdapter.adapt(ArrayAdapter.newArray()));
    }

    @Override
    @Test
    public void groupBy()
    {
        RichIterable<Integer> list = this.newWith(1, 2, 3, 4, 5, 6, 7);
        Multimap<Boolean, Integer> multimap = list.groupBy(object -> IntegerPredicates.isOdd().accept(object));

        MutableMap<Boolean, RichIterable<Integer>> expected =
                UnifiedMap.<Boolean, RichIterable<Integer>>newWithKeysValues(
                        Boolean.TRUE, FastList.newListWith(1, 3, 5, 7),
                        Boolean.FALSE, FastList.newListWith(2, 4, 6));

        Assert.assertEquals(expected, multimap.toMap());
    }

    @Override
    @Test
    public void groupByEach()
    {
        RichIterable<Integer> underTest = this.newWith(1, 2, 3, 4, 5, 6, 7);
        Function<Integer, Iterable<Integer>> intervalFunction = new NegativeIntervalFunction();

        MutableMultimap<Integer, Integer> expected = this.<Integer>newWith().groupByEach(intervalFunction);
        for (int i = 1; i < 8; i++)
        {
            expected.putAll(-i, Interval.fromTo(i, 7));
        }

        Multimap<Integer, Integer> actual =
                underTest.groupByEach(intervalFunction);
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                underTest.groupByEach(intervalFunction, this.<Integer>newWith().groupByEach(intervalFunction));
        Assert.assertEquals(expected, actualWithTarget);
    }

    @Test
    public void wrapSet()
    {
        Verify.assertInstanceOf(SetAdapter.class, CollectionAdapter.wrapSet(new HashSet<>()));
        Verify.assertInstanceOf(UnifiedSet.class, CollectionAdapter.wrapSet(new FastList<>()));
    }

    @Test
    public void wrapList()
    {
        Verify.assertInstanceOf(ArrayListAdapter.class, CollectionAdapter.wrapList(new ArrayList<>()));
        Verify.assertInstanceOf(FastList.class, CollectionAdapter.wrapList(new HashSet<>()));
        Verify.assertInstanceOf(FastList.class, CollectionAdapter.wrapList(FastList.newList()));
    }

    @Test
    public void testEquals()
    {
        Assert.assertEquals(new CollectionAdapter<>(FastList.newList()), new CollectionAdapter<>(FastList.newList()));
        Assert.assertNotEquals(new CollectionAdapter<>(FastList.newList()), new CollectionAdapter<>(FastList.newListWith(1)));
        Assert.assertEquals(new CollectionAdapter<>(FastList.newListWith(1)), new CollectionAdapter<>(FastList.newListWith(1)));
        Assert.assertNotEquals(new CollectionAdapter<>(FastList.newListWith(1)), new CollectionAdapter<>(FastList.newListWith(2)));
    }

    @Test
    public void testNewEmpty()
    {
        Verify.assertInstanceOf(UnifiedSet.class, new CollectionAdapter<>(new HashSet<>()).newEmpty());
        Verify.assertInstanceOf(FastList.class, new CollectionAdapter<>(new ArrayList<>()).newEmpty());
    }

    @Override
    @Test
    public void chunk_large_size()
    {
        MutableCollection<String> collection = this.newWith("1", "2", "3", "4", "5", "6", "7");
        Assert.assertEquals(collection.toList(), collection.chunk(10).getFirst());
    }

    @Override
    @Test
    public void partition()
    {
        MutableCollection<Integer> integers = this.newWith(-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        PartitionMutableCollection<Integer> result = integers.partition(IntegerPredicates.isEven());
        Assert.assertEquals(iList(-2, 0, 2, 4, 6, 8), result.getSelected());
        Assert.assertEquals(iList(-3, -1, 1, 3, 5, 7, 9), result.getRejected());
    }

    @Override
    @Test
    public void partitionWith()
    {
        MutableCollection<Integer> integers = this.newWith(-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        PartitionMutableCollection<Integer> result = integers.partitionWith(Predicates2.in(), integers.select(IntegerPredicates.isEven()));
        Assert.assertEquals(iList(-2, 0, 2, 4, 6, 8), result.getSelected());
        Assert.assertEquals(iList(-3, -1, 1, 3, 5, 7, 9), result.getRejected());
    }
}
