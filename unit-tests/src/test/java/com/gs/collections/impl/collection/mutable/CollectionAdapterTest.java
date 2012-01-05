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
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.factory.Lists;
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
    protected <T> AbstractCollectionAdapter<T> classUnderTest()
    {
        return new CollectionAdapter<T>(new ArrayList<T>());
    }

    private CollectionAdapter<Integer> newSet()
    {
        return new CollectionAdapter<Integer>(UnifiedSet.<Integer>newSet());
    }

    private CollectionAdapter<Integer> newList()
    {
        return new CollectionAdapter<Integer>(FastList.<Integer>newList());
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableCollection.class, CollectionAdapter.adapt(Lists.fixedSize.of("1")).asSynchronized());

        Verify.assertInstanceOf(SynchronizedMutableCollection.class, new CollectionAdapter<Object>(null).asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableCollection.class, this.classUnderTest().asUnmodifiable());
    }

    @Override
    @Test
    public void toImmutable()
    {
        super.toImmutable();
        Verify.assertInstanceOf(ImmutableList.class, new CollectionAdapter<String>(Collections.singletonList("1")).toImmutable());
        Verify.assertInstanceOf(ImmutableSet.class, new CollectionAdapter<String>(Collections.singleton("1")).toImmutable());
    }

    @Override
    @Test
    public void select()
    {
        super.select();
        Verify.assertContainsAll(this.newSet().with(1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 1, 2);
        Verify.assertContainsAll(this.newSet().with(-1, 2, 3, 4, 5).select(
                Predicates.lessThan(3),
                FastList.<Integer>newList()), -1, 2);
    }

    @Override
    @Test
    public void newEmpty()
    {
        Verify.assertInstanceOf(FastList.class, CollectionAdapter.<Object>adapt(new LinkedList<Object>()).newEmpty());
    }

    @Override
    @Test
    public void reject()
    {
        super.reject();
        Verify.assertContainsAll(this.newSet().with(1, 2, 3, 4).reject(Predicates.lessThan(3)), 3, 4);
        Verify.assertContainsAll(this.newSet().with(1, 2, 3, 4).reject(Predicates.lessThan(3), FastList.<Integer>newList()), 3, 4);
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();

        Assert.assertEquals(
                UnifiedSet.newSetWith("1", "2", "3", "4"),
                this.newSet().with(1, 2, 3, 4).collect(Functions.getToString()));
        Assert.assertEquals(
                UnifiedSet.newSetWith("1", "2", "3", "4"),
                this.newSet().with(1, 2, 3, 4).collect(
                        Functions.getToString(),
                        UnifiedSet.<String>newSet()));
    }

    @Override
    @Test
    public void flatCollect()
    {
        super.flatCollect();

        Function<Integer, Collection<Integer>> function =
                new Function<Integer, Collection<Integer>>()
                {
                    public Collection<Integer> valueOf(Integer object)
                    {
                        return Interval.oneTo(object);
                    }
                };

        Assert.assertEquals(
                FastList.newListWith(1, 1, 2, 1, 2, 3, 1, 2, 3, 4),
                this.newList().with(1, 2, 3, 4).flatCollect(function));
        Assert.assertEquals(
                FastList.newListWith(1, 1, 2, 1, 2, 3, 1, 2, 3, 4),
                this.newList().with(1, 2, 3, 4).flatCollect(function));
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        MutableCollection<Integer> list1 = this.newList().with(1, 2, 3);
        MutableCollection<Integer> list2 = this.newList().with(1, 2, 3);
        MutableCollection<Integer> list3 = this.newList().with(2, 3, 4);
        Verify.assertEqualsAndHashCode(list1, list2);
        Verify.assertNotEquals(list1, null);
        Verify.assertNotEquals(list2, list3);
    }

    @Test
    public void newListWithSize()
    {
        Collection<Integer> collection = this.newList().with(1, 2, 3);
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void serialization()
    {
        MutableCollection<Integer> collection = this.newList().with(1, 2, 3, 4, 5);
        MutableCollection<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Verify.assertContainsAll(deserializedCollection, 1, 2, 3, 4, 5);
        Assert.assertEquals(collection, deserializedCollection);
    }

    @Test
    public void adapt()
    {
        Verify.assertInstanceOf(FastList.class, CollectionAdapter.adapt(FastList.newList()));
        Verify.assertInstanceOf(ArrayListAdapter.class, CollectionAdapter.adapt(new ArrayList<Object>()));
        Verify.assertInstanceOf(SetAdapter.class, CollectionAdapter.adapt(new HashSet<Object>()));
        Verify.assertInstanceOf(UnifiedSet.class, CollectionAdapter.adapt(UnifiedSet.newSet()));
        Verify.assertInstanceOf(RandomAccessListAdapter.class, CollectionAdapter.adapt(Collections.emptyList()));
        Verify.assertInstanceOf(ListAdapter.class, CollectionAdapter.adapt(new LinkedList<Object>()));
        Verify.assertInstanceOf(ArrayAdapter.class, CollectionAdapter.adapt(ArrayAdapter.newArray()));
    }

    @Override
    @Test
    public void groupBy()
    {
        RichIterable<Integer> list = this.newWith(1, 2, 3, 4, 5, 6, 7);
        Multimap<Boolean, Integer> multimap =
                list.groupBy(new Function<Integer, Boolean>()
                {
                    public Boolean valueOf(Integer object)
                    {
                        return IntegerPredicates.isOdd().accept(object);
                    }
                });

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

        MutableMultimap<Integer, Integer> expected = this.<Integer>classUnderTest().groupByEach(intervalFunction);
        for (int i = 1; i < 8; i++)
        {
            expected.putAll(-i, Interval.fromTo(i, 7));
        }

        Multimap<Integer, Integer> actual =
                underTest.groupByEach(intervalFunction);
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                underTest.groupByEach(intervalFunction, this.<Integer>classUnderTest().groupByEach(intervalFunction));
        Assert.assertEquals(expected, actualWithTarget);
    }

    @Test
    public void wrapSet()
    {
        Verify.assertInstanceOf(SetAdapter.class, CollectionAdapter.wrapSet(new HashSet<Object>()));
        Verify.assertInstanceOf(UnifiedSet.class, CollectionAdapter.wrapSet(new FastList<Object>()));
    }

    @Test
    public void wrapList()
    {
        Verify.assertInstanceOf(ArrayListAdapter.class, CollectionAdapter.wrapList(new ArrayList<Object>()));
        Verify.assertInstanceOf(FastList.class, CollectionAdapter.wrapList(new HashSet<Object>()));
    }

    @Test
    public void testEquals()
    {
        Assert.assertEquals(new CollectionAdapter<Object>(null), new CollectionAdapter<Object>(null));
        FastList<Integer> match = FastList.newListWith(1);
        Verify.assertNotEquals(new CollectionAdapter<Object>(null), new CollectionAdapter<Integer>(match));
        Assert.assertEquals(new CollectionAdapter<Integer>(match), new CollectionAdapter<Integer>(match));
        FastList<Integer> notMatch = FastList.newListWith(2);
        Verify.assertNotEquals(new CollectionAdapter<Integer>(match), new CollectionAdapter<Integer>(notMatch));
    }

    @Test
    public void testNewEmpty()
    {
        Verify.assertInstanceOf(UnifiedSet.class, new CollectionAdapter<Object>(new HashSet<Object>()).newEmpty());
        Verify.assertInstanceOf(FastList.class, new CollectionAdapter<Object>(null).newEmpty());
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
}
