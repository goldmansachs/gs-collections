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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions2;
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
        Predicate<Object> predicate = null;
        this.getCollection().removeIf(predicate);
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
        Assert.assertEquals(this.getCollection(), this.getCollection().select(ignored -> true));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().select(ignored -> false));
    }

    @Test
    public void selectWith()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().selectWith((ignored1, ignored2) -> true, null));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().selectWith((ignored1, ignored2) -> false, null));
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().reject(ignored1 -> false));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().reject(ignored -> true));
    }

    @Test
    public void rejectWith()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().rejectWith((ignored11, ignored21) -> false, null));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().rejectWith((ignored1, ignored2) -> true, null));
    }

    @Test
    public void partition()
    {
        PartitionMutableCollection<?> partition = this.getCollection().partition(ignored -> true);
        Assert.assertEquals(this.getCollection(), partition.getSelected());
        Assert.assertNotEquals(this.getCollection(), partition.getRejected());
    }

    @Test
    public void partitionWith()
    {
        PartitionMutableCollection<?> partition = this.getCollection().partitionWith((ignored1, ignored2) -> true, null);
        Assert.assertEquals(this.getCollection(), partition.getSelected());
        Assert.assertNotEquals(this.getCollection(), partition.getRejected());
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().collect(Functions.getPassThru()));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().collect(Object::getClass));
    }

    @Test
    public void collectInt()
    {
        IntFunction<T> intFunction = anObject -> anObject == null ? 0 : 1;
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectInt(intFunction));
    }

    @Test
    public void collectBoolean()
    {
        BooleanFunction<T> booleanFunction = anObject -> anObject == null;
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectBoolean(booleanFunction));
    }

    @Test
    public void collectByte()
    {
        ByteFunction<T> byteFunction = anObject -> anObject == null ? (byte) 0 : (byte) 1;
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectByte(byteFunction));
    }

    @Test
    public void collectChar()
    {
        CharFunction<T> charFunction = anObject -> anObject == null ? '0' : '1';
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectChar(charFunction));
    }

    @Test
    public void collectDouble()
    {
        DoubleFunction<T> doubleFunction = anObject -> anObject == null ? 0.0d : 1.0d;
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectDouble(doubleFunction));
    }

    @Test
    public void collectFloat()
    {
        FloatFunction<T> floatFunction = anObject -> anObject == null ? 0.0f : 1.0f;
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectFloat(floatFunction));
    }

    @Test
    public void collectLong()
    {
        LongFunction<T> longFunction = anObject -> anObject == null ? 0L : 1L;
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectLong(longFunction));
    }

    @Test
    public void collectShort()
    {
        ShortFunction<T> shortFunction = anObject -> (short) (anObject == null ? 0 : 1);
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectShort(shortFunction));
    }

    @Test
    public void collectWith()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().collectWith(Functions2.fromFunction(Functions.getPassThru()), null));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().collectWith(Functions2.fromFunction(Object::getClass), null));
    }

    @Test
    public void collectIf()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().collectIf(ignored -> true, Functions.getPassThru()));
        Assert.assertNotEquals(this.getCollection(), this.getCollection().collectIf(ignored -> false, Object::getClass));
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
                pairs.collect((Function<Pair<Object, ?>, Object>) Pair::getOne).toSet());
        Assert.assertEquals(
                nulls,
                pairs.collect((Function<Pair<?, Object>, Object>) Pair::getTwo, Lists.mutable.of()));

        MutableCollection<Pair<Object, Object>> pairsPlusOne = collection.zip(nullsPlusOne);
        Assert.assertEquals(
                collection.toSet(),
                pairsPlusOne.collect((Function<Pair<Object, ?>, Object>) Pair::getOne).toSet());
        Assert.assertEquals(nulls, pairsPlusOne.collect((Function<Pair<?, Object>, Object>) Pair::getTwo, Lists.mutable.of()));

        MutableCollection<Pair<Object, Object>> pairsMinusOne = collection.zip(nullsMinusOne);
        Assert.assertEquals(collection.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(collection.containsAll(pairsMinusOne.collect((Function<Pair<Object, ?>, Object>) Pair::getOne)));

        Assert.assertEquals(
                collection.zip(nulls).toSet(),
                collection.zip(nulls, new UnifiedSet<>()));
    }

    @Test
    public void zipWithIndex()
    {
        MutableCollection<Object> collection = (MutableCollection<Object>) this.getCollection();
        MutableCollection<Pair<Object, Integer>> pairs = collection.zipWithIndex();

        Assert.assertEquals(
                collection.toSet(),
                pairs.collect((Function<Pair<Object, ?>, Object>) Pair::getOne).toSet());
        Assert.assertEquals(
                Interval.zeroTo(collection.size() - 1).toSet(),
                pairs.collect((Function<Pair<?, Integer>, Integer>) Pair::getTwo, UnifiedSet.<Integer>newSet()));

        Assert.assertEquals(
                collection.zipWithIndex().toSet(),
                collection.zipWithIndex(new UnifiedSet<>()));
    }

    @Test
    public void flatCollect()
    {
        MutableCollection<Object> collection = (MutableCollection<Object>) this.getCollection();
        Assert.assertEquals(
                this.getCollection().toBag(),
                collection.flatCollect((Function<Object, Iterable<Object>>) Lists.fixedSize::of).toBag());
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

    @Test
    public void tap()
    {
        MutableList<T> tapResult = Lists.mutable.of();
        MutableCollection<T> collection = this.getCollection();
        Assert.assertSame(collection, collection.tap(tapResult::add));
        Assert.assertEquals(collection.toList(), tapResult);
    }
}
