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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * Abstract JUnit test for {@link UnmodifiableBag}.
 */
public class UnmodifiableBagTest
        extends UnmodifiableMutableCollectionTestCase<String>
{
    @Override
    protected MutableBag<String> getCollection()
    {
        return Bags.mutable.of("").asUnmodifiable();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addOccurrences()
    {
        this.getCollection().addOccurrences(null, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeOccurrences()
    {
        this.getCollection().removeOccurrences(null, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setOccurrences()
    {
        this.getCollection().setOccurrences(null, 1);
    }

    @Test
    public void asUnmodifiable()
    {
        MutableBag<?> bag = this.getCollection();
        Verify.assertInstanceOf(UnmodifiableBag.class, bag.asUnmodifiable());
    }

    @Test
    public void asSynchronized()
    {
        MutableBag<?> bag = this.getCollection();
        Verify.assertInstanceOf(SynchronizedBag.class, bag.asSynchronized());
    }

    @Test
    public void toImmutable()
    {
        MutableBag<?> bag = this.getCollection();
        Verify.assertInstanceOf(ImmutableBag.class, bag.toImmutable());
    }

    @Test
    public void equalsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(this.getCollection(), Bags.mutable.of(""));
        MutableBag<Number> numbers = UnmodifiableBag.of(HashBag.<Number>newBagWith(1, 1, 2, 2, 2, 3));
        Verify.assertPostSerializedEqualsAndHashCode(numbers);
        Verify.assertInstanceOf(UnmodifiableBag.class, SerializeTestHelper.serializeDeserialize(numbers));
    }

    @Test
    public void forEachWithOccurrences()
    {
        MutableList<Pair<Object, Integer>> list = Lists.mutable.of();
        this.getCollection().forEachWithOccurrences((each, index) -> list.add(Tuples.pair(each, index)));
        Assert.assertEquals(FastList.newListWith(Tuples.pair("", 1)), list);
    }

    @Test
    public void toMapOfItemToCount()
    {
        Assert.assertEquals(UnifiedMap.newWithKeysValues("", 1), this.getCollection().toMapOfItemToCount());
    }

    @Test
    public void selectInstancesOf()
    {
        MutableBag<Number> numbers = UnmodifiableBag.of(HashBag.<Number>newBagWith(1, 2.0, 3, 4.0, 5));
        Assert.assertEquals(iBag(1, 3, 5), numbers.selectInstancesOf(Integer.class));
        Assert.assertEquals(iBag(1, 2.0, 3, 4.0, 5), numbers.selectInstancesOf(Number.class));
    }

    @Test
    public void selectByOccurrences()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4));
        Assert.assertEquals(iBag(1, 1, 1, 1, 3, 3), integers.selectByOccurrences(IntPredicates.isEven()));
    }

    @Override
    @Test
    public void collectBoolean()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(0, 1, 2, 2));
        Assert.assertEquals(BooleanHashBag.newBagWith(false, true, true, true),
                integers.collectBoolean(PrimitiveFunctions.integerIsPositive()));
    }

    @Override
    @Test
    public void collectByte()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 2, 2, 3, 3, 3));
        Assert.assertEquals(ByteHashBag.newBagWith((byte) 1, (byte) 2, (byte) 2, (byte) 3, (byte) 3, (byte) 3),
                integers.collectByte(PrimitiveFunctions.unboxIntegerToByte()));
    }

    @Override
    @Test
    public void collectChar()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 2, 2, 3, 3, 3));
        Assert.assertEquals(CharHashBag.newBagWith('A', 'B', 'B', 'C', 'C', 'C'),
                integers.collectChar(integer -> (char) (integer.intValue() + 64)));
    }

    @Override
    @Test
    public void collectDouble()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 2, 2, 3, 3, 3));
        Assert.assertEquals(DoubleHashBag.newBagWith(1.0d, 2.0d, 2.0d, 3.0d, 3.0d, 3.0d),
                integers.collectDouble(PrimitiveFunctions.unboxIntegerToDouble()));
    }

    @Override
    @Test
    public void collectFloat()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 2, 2, 3, 3, 3));
        Assert.assertEquals(FloatHashBag.newBagWith(1.0f, 2.0f, 2.0f, 3.0f, 3.0f, 3.0f),
                integers.collectFloat(PrimitiveFunctions.unboxIntegerToFloat()));
    }

    @Override
    @Test
    public void collectInt()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 2, 2, 3, 3, 3));
        Assert.assertEquals(IntHashBag.newBagWith(1, 2, 2, 3, 3, 3),
                integers.collectInt(PrimitiveFunctions.unboxIntegerToInt()));
    }

    @Override
    @Test
    public void collectLong()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 2, 2, 3, 3, 3));
        Assert.assertEquals(LongHashBag.newBagWith(1L, 2L, 2L, 3L, 3L, 3L),
                integers.collectLong(PrimitiveFunctions.unboxIntegerToLong()));
    }

    @Override
    @Test
    public void collectShort()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 2, 2, 3, 3, 3));
        Assert.assertEquals(ShortHashBag.newBagWith((short) 1, (short) 2, (short) 2, (short) 3, (short) 3, (short) 3),
                integers.collectShort(PrimitiveFunctions.unboxIntegerToShort()));
    }

    @Test
    public void topOccurrences()
    {
        MutableBag<String> mutable = HashBag.newBag();
        mutable.addOccurrences("one", 1);
        mutable.addOccurrences("two", 2);
        mutable.addOccurrences("three", 3);
        mutable.addOccurrences("four", 4);
        mutable.addOccurrences("five", 5);
        mutable.addOccurrences("six", 6);
        mutable.addOccurrences("seven", 7);
        mutable.addOccurrences("eight", 8);
        mutable.addOccurrences("nine", 9);
        mutable.addOccurrences("ten", 10);
        MutableBag<String> strings = mutable.asUnmodifiable();
        MutableList<ObjectIntPair<String>> top5 = strings.topOccurrences(5);
        Verify.assertSize(5, top5);
        Assert.assertEquals("ten", top5.getFirst().getOne());
        Assert.assertEquals(10, top5.getFirst().getTwo());
        Assert.assertEquals("six", top5.getLast().getOne());
        Assert.assertEquals(6, top5.getLast().getTwo());
    }

    @Test
    public void bottomOccurrences()
    {
        MutableBag<String> mutable = HashBag.newBag();
        mutable.addOccurrences("one", 1);
        mutable.addOccurrences("two", 2);
        mutable.addOccurrences("three", 3);
        mutable.addOccurrences("four", 4);
        mutable.addOccurrences("five", 5);
        mutable.addOccurrences("six", 6);
        mutable.addOccurrences("seven", 7);
        mutable.addOccurrences("eight", 8);
        mutable.addOccurrences("nine", 9);
        mutable.addOccurrences("ten", 10);
        MutableBag<String> strings = mutable.asUnmodifiable();
        MutableList<ObjectIntPair<String>> bottom5 = strings.bottomOccurrences(5);
        Verify.assertSize(5, bottom5);
        Assert.assertEquals("one", bottom5.getFirst().getOne());
        Assert.assertEquals(1, bottom5.getFirst().getTwo());
        Assert.assertEquals("five", bottom5.getLast().getOne());
        Assert.assertEquals(5, bottom5.getLast().getTwo());
    }
}
