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

package com.gs.collections.impl.collection.mutable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions2;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link UnmodifiableMutableCollection}.
 */
public abstract class UnmodifiableMutableCollectionTestCase<T>
{
    protected abstract MutableCollection<T> getCollection();

    @Test(expected = UnsupportedOperationException.class)
    public void removeIfWith()
    {
        this.getCollection().removeIfWith(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIf()
    {
        this.getCollection().removeIf(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove()
    {
        this.getCollection().remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorRemove()
    {
        Iterator<?> iterator = this.getCollection().iterator();
        iterator.next();
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add()
    {
        this.getCollection().add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAll()
    {
        this.getCollection().addAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllIterable()
    {
        this.getCollection().addAllIterable(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAll()
    {
        this.getCollection().removeAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAllIterable()
    {
        this.getCollection().removeAllIterable(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void retainAll()
    {
        this.getCollection().retainAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void retainAllIterable()
    {
        this.getCollection().retainAllIterable(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void clear()
    {
        this.getCollection().clear();
    }

    @Test
    public void testMakeString()
    {
        Assert.assertEquals(this.getCollection().toString(), '[' + this.getCollection().makeString() + ']');
    }

    @Test
    public void testAppendString()
    {
        Appendable builder = new StringBuilder();
        this.getCollection().appendString(builder);
        Assert.assertEquals(this.getCollection().toString(), '[' + builder.toString() + ']');
    }

    @Test
    public void select()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().select(Predicates.alwaysTrue()));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().select(Predicates.alwaysFalse()));
    }

    @Test
    public void selectWith()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().selectWith(Predicates2.alwaysTrue(), null));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().selectWith(Predicates2.alwaysFalse(), null));
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().reject(Predicates.alwaysFalse()));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().reject(Predicates.alwaysTrue()));
    }

    @Test
    public void rejectWith()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().rejectWith(Predicates2.alwaysFalse(), null));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().rejectWith(Predicates2.alwaysTrue(), null));
    }

    @Test
    public void partition()
    {
        PartitionMutableCollection<?> partition = this.getCollection().partition(Predicates.alwaysTrue());
        Assert.assertEquals(this.getCollection(), partition.getSelected());
        Assert.assertNotEquals(this.getCollection(), partition.getRejected());
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().collect(Functions.<Object>getPassThru()));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().collect(Functions.getToClass()));
    }

    @Test
    public void collectWith()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().collectWith(Functions2.fromFunction(Functions.getPassThru()), null));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().collectWith(Functions2.fromFunction(Functions.getToClass()), null));
    }

    @Test
    public void collectIf()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().collectIf(Predicates.alwaysTrue(), Functions.<Object>getPassThru()));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().collectIf(Predicates.alwaysFalse(), Functions.getToClass()));
    }

    @Test
    public void newEmpty()
    {
        MutableCollection<Object> collection = (MutableCollection<Object>) this.getCollection().newEmpty();
        Verify.assertEmpty(collection);
        collection.add("test");
        Verify.assertNotEmpty(collection);
    }

    @Test
    public void groupBy()
    {
        Assert.assertEquals(this.getCollection().size(), this.getCollection().groupBy(Functions.getPassThru()).size());
    }

    @Test
    public void zip()
    {
        MutableCollection<Object> collection = (MutableCollection<Object>) this.getCollection();
        List<Object> nulls = Collections.nCopies(collection.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(collection.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(collection.size() - 1, null);

        MutableCollection<Pair<Object, Object>> pairs = collection.zip(nulls);
        Assert.assertEquals(
                collection.toSet(),
                pairs.collect(Functions.firstOfPair()).toSet());
        Assert.assertEquals(
                nulls,
                pairs.collect(Functions.secondOfPair(), Lists.mutable.of()));

        MutableCollection<Pair<Object, Object>> pairsPlusOne = collection.zip(nullsPlusOne);
        Assert.assertEquals(
                collection.toSet(),
                pairsPlusOne.collect(Functions.firstOfPair()).toSet());
        Assert.assertEquals(nulls, pairsPlusOne.collect(Functions.secondOfPair(), Lists.mutable.of()));

        MutableCollection<Pair<Object, Object>> pairsMinusOne = collection.zip(nullsMinusOne);
        Assert.assertEquals(collection.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(collection.containsAll(pairsMinusOne.collect(Functions.firstOfPair())));

        Assert.assertEquals(
                collection.zip(nulls).toSet(),
                collection.zip(nulls, new UnifiedSet<Pair<Object, Object>>()));
    }

    @Test
    public void zipWithIndex()
    {
        MutableCollection<Object> collection = (MutableCollection<Object>) this.getCollection();
        MutableCollection<Pair<Object, Integer>> pairs = collection.zipWithIndex();

        Assert.assertEquals(
                collection.toSet(),
                pairs.collect(Functions.firstOfPair()).toSet());
        Assert.assertEquals(
                Interval.zeroTo(collection.size() - 1).toSet(),
                pairs.collect(Functions.<Integer>secondOfPair(), UnifiedSet.<Integer>newSet()));

        Assert.assertEquals(
                collection.zipWithIndex().toSet(),
                collection.zipWithIndex(new UnifiedSet<Pair<Object, Integer>>()));
    }

    @Test
    public void flatCollect()
    {
        MutableCollection<Object> collection = (MutableCollection<Object>) this.getCollection();
        Assert.assertEquals(
                this.getCollection().toBag(),
                collection.flatCollect(new Function<Object, Iterable<Object>>()
                {
                    public Iterable<Object> valueOf(Object object)
                    {
                        return Lists.fixedSize.of(object);
                    }
                }).toBag());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void with()
    {
        this.getCollection().with(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void withAll()
    {
        this.getCollection().withAll(FastList.<T>newList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void without()
    {
        this.getCollection().without(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void withoutAll()
    {
        this.getCollection().withoutAll(FastList.<T>newList());
    }
}
