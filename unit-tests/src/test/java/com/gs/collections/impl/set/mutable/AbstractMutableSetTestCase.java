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

package com.gs.collections.impl.set.mutable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.IntegerWithCast;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.collection.mutable.AbstractCollectionTestCase;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link AbstractMutableSet}.
 */
public abstract class AbstractMutableSetTestCase extends AbstractCollectionTestCase
{
    protected static final Integer COLLISION_1 = 0;
    protected static final Integer COLLISION_2 = 17;
    protected static final Integer COLLISION_3 = 34;
    protected static final Integer COLLISION_4 = 51;
    protected static final Integer COLLISION_5 = 68;
    protected static final Integer COLLISION_6 = 85;
    protected static final Integer COLLISION_7 = 102;
    protected static final Integer COLLISION_8 = 119;
    protected static final Integer COLLISION_9 = 136;
    protected static final Integer COLLISION_10 = 152;
    protected static final MutableList<Integer> COLLISIONS =
            FastList.newListWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_4, COLLISION_5);
    protected static final MutableList<Integer> MORE_COLLISIONS = FastList.newList(COLLISIONS)
            .with(COLLISION_6, COLLISION_7, COLLISION_8, COLLISION_9);
    protected static final int SIZE = 8;

    @Override
    protected abstract <T> MutableSet<T> classUnderTest();

    @Override
    protected <T> MutableSet<T> newWith(T one)
    {
        return (MutableSet<T>) super.newWith(one);
    }

    @Override
    protected <T> MutableSet<T> newWith(T one, T two)
    {
        return (MutableSet<T>) super.newWith(one, two);
    }

    @Override
    protected <T> MutableSet<T> newWith(T one, T two, T three)
    {
        return (MutableSet<T>) super.newWith(one, two, three);
    }

    @Override
    protected <T> MutableSet<T> newWith(T... littleElements)
    {
        return (MutableSet<T>) super.newWith(littleElements);
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableSet.class, this.<Object>classUnderTest().asSynchronized());
    }

    @Override
    @Test
    public void addAll()
    {
        super.addAll();

        UnifiedSet<Integer> expected = UnifiedSet.newSetWith(1, 2, 3);
        MutableSet<Integer> collection = this.classUnderTest();

        Assert.assertTrue(collection.addAll(FastList.newListWith(1, 2, 3)));
        Assert.assertEquals(expected, collection);

        Assert.assertFalse(collection.addAll(FastList.newListWith(1, 2, 3)));
        Assert.assertEquals(expected, collection);
    }

    @Override
    @Test
    public void addAllIterable()
    {
        super.addAllIterable();

        UnifiedSet<Integer> expected = UnifiedSet.newSetWith(1, 2, 3);
        MutableSet<Integer> collection = this.classUnderTest();

        Assert.assertTrue(collection.addAllIterable(FastList.newListWith(1, 2, 3)));
        Assert.assertEquals(expected, collection);

        Assert.assertFalse(collection.addAllIterable(FastList.newListWith(1, 2, 3)));
        Assert.assertEquals(expected, collection);
    }

    @Test
    public void union()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> union = set.union(UnifiedSet.newSetWith("a", "b", "c", "1"));
        Verify.assertSize(set.size() + 3, union);
        Assert.assertTrue(union.containsAllIterable(Interval.oneTo(set.size()).collect(Functions.getToString())));
        Verify.assertContainsAll(union, "a", "b", "c");

        Assert.assertEquals(set, set.union(UnifiedSet.newSetWith("1")));
    }

    @Test
    public void unionInto()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> union = set.unionInto(UnifiedSet.newSetWith("a", "b", "c", "1"), UnifiedSet.<String>newSet());
        Verify.assertSize(set.size() + 3, union);
        Assert.assertTrue(union.containsAllIterable(Interval.oneTo(set.size()).collect(Functions.getToString())));
        Verify.assertContainsAll(union, "a", "b", "c");

        Assert.assertEquals(set, set.unionInto(UnifiedSet.newSetWith("1"), UnifiedSet.<String>newSet()));
    }

    @Test
    public void intersect()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> intersect = set.intersect(UnifiedSet.newSetWith("a", "b", "c", "1"));
        Verify.assertSize(1, intersect);
        Assert.assertEquals(UnifiedSet.newSetWith("1"), intersect);

        Verify.assertEmpty(set.intersect(UnifiedSet.newSetWith("not present")));
    }

    @Test
    public void intersectInto()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> intersect = set.intersectInto(UnifiedSet.newSetWith("a", "b", "c", "1"), UnifiedSet.<String>newSet());
        Verify.assertSize(1, intersect);
        Assert.assertEquals(UnifiedSet.newSetWith("1"), intersect);

        Verify.assertEmpty(set.intersectInto(UnifiedSet.newSetWith("not present"), UnifiedSet.<String>newSet()));
    }

    @Test
    public void difference()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> difference = set.difference(UnifiedSet.newSetWith("2", "3", "4", "not present"));
        Assert.assertEquals(UnifiedSet.newSetWith("1"), difference);
        Assert.assertEquals(set, set.difference(UnifiedSet.newSetWith("not present")));
    }

    @Test
    public void differenceInto()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> difference = set.differenceInto(UnifiedSet.newSetWith("2", "3", "4", "not present"), UnifiedSet.<String>newSet());
        Assert.assertEquals(UnifiedSet.newSetWith("1"), difference);
        Assert.assertEquals(set, set.differenceInto(UnifiedSet.newSetWith("not present"), UnifiedSet.<String>newSet()));
    }

    @Test
    public void symmetricDifference()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> difference = set.symmetricDifference(UnifiedSet.newSetWith("2", "3", "4", "5", "not present"));
        Verify.assertContains("1", difference);
        Assert.assertTrue(difference.containsAllIterable(Interval.fromTo(set.size() + 1, 5).collect(Functions.getToString())));
        for (int i = 2; i <= set.size(); i++)
        {
            Verify.assertNotContains(String.valueOf(i), difference);
        }

        Verify.assertSize(set.size() + 1, set.symmetricDifference(UnifiedSet.newSetWith("not present")));
    }

    @Test
    public void symmetricDifferenceInto()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<String> difference = set.symmetricDifferenceInto(
                UnifiedSet.newSetWith("2", "3", "4", "5", "not present"),
                UnifiedSet.<String>newSet());
        Verify.assertContains("1", difference);
        Assert.assertTrue(difference.containsAllIterable(Interval.fromTo(set.size() + 1, 5).collect(Functions.getToString())));
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
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        Assert.assertTrue(set.isSubsetOf(UnifiedSet.newSetWith("1", "2", "3", "4", "5")));
    }

    @Test
    public void isProperSubsetOf()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        Assert.assertTrue(set.isProperSubsetOf(UnifiedSet.newSetWith("1", "2", "3", "4", "5")));
        Assert.assertFalse(set.isProperSubsetOf(set));
    }

    @Test
    public void powerSet()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        MutableSet<UnsortedSetIterable<String>> powerSet = set.powerSet();
        Verify.assertSize((int) StrictMath.pow(2, set.size()), powerSet);
        Verify.assertContains(UnifiedSet.<String>newSet(), powerSet);
        Verify.assertContains(set, powerSet);
    }

    @Test
    public void cartesianProduct()
    {
        MutableSet<String> set = this.newWith("1", "2", "3", "4");
        LazyIterable<Pair<String, String>> cartesianProduct = set.cartesianProduct(UnifiedSet.newSetWith("One", "Two"));
        Verify.assertIterableSize(set.size() * 2, cartesianProduct);
        Assert.assertEquals(
                set,
                cartesianProduct
                        .select(Predicates.attributeEqual(Functions.<String>secondOfPair(), "One"))
                        .collect(Functions.<String>firstOfPair()).toSet());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableSet.class, this.<Object>classUnderTest().asUnmodifiable());
    }

    @Override
    @Test
    public void select()
    {
        super.select();
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 1, 2);
        Verify.assertContainsAll(
                this.newWith(-1, 2, 3, 4, 5).select(Predicates.lessThan(3),
                        FastList.<Integer>newList()), -1, 2);
    }

    @Override
    @Test
    public void reject()
    {
        super.reject();
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4).reject(Predicates.lessThan(3)), 3, 4);
        Verify.assertContainsAll(
                this.newWith(1, 2, 3, 4).reject(Predicates.lessThan(3),
                        FastList.<Integer>newList()), 3, 4);
    }

    @Override
    @Test
    public void getFirst()
    {
        super.getFirst();

        Assert.assertNotNull(this.newWith(1, 2, 3).getFirst());
        Assert.assertNull(this.classUnderTest().getFirst());
    }

    @Override
    @Test
    public void getLast()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getLast());
        Assert.assertNull(this.classUnderTest().getLast());
    }

    @Test
    public void unifiedSetKeySetToArrayDest()
    {
        MutableSet<Integer> set = this.newWith(1, 2, 3, 4);
        // deliberately to small to force the method to allocate one of the correct size
        Integer[] dest = new Integer[2];
        Integer[] result = set.toArray(dest);
        Verify.assertSize(4, result);
        Arrays.sort(result);
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4}, result);
    }

    @Test
    public void unifiedSetToString()
    {
        MutableSet<Integer> set = this.newWith(1, 2);
        String s = set.toString();
        Assert.assertTrue("[1, 2]".equals(s) || "[2, 1]".equals(s));
    }

    @Test
    public void testClone()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> clone = set.clone();
        Assert.assertNotSame(clone, set);
        Verify.assertEqualsAndHashCode(clone, set);
    }

    @Override
    @Test
    public void isEmpty()
    {
        super.isEmpty();

        MutableSet<String> set = this.classUnderTest();
        this.assertIsEmpty(true, set);

        set.add("stuff");
        this.assertIsEmpty(false, set);

        set.remove("stuff");
        this.assertIsEmpty(true, set);

        set.add("Bon");
        set.add("Jovi");
        this.assertIsEmpty(false, set);
        set.remove("Jovi");
        this.assertIsEmpty(false, set);
        set.clear();
        this.assertIsEmpty(true, set);
    }

    private void assertIsEmpty(boolean isEmpty, MutableSet<?> set)
    {
        Assert.assertEquals(isEmpty, set.isEmpty());
        Assert.assertEquals(!isEmpty, set.notEmpty());
    }

    @Test
    public void add()
    {
        MutableSet<IntegerWithCast> set = this.classUnderTest();
        MutableList<IntegerWithCast> collisions = COLLISIONS.collect(IntegerWithCast.CONSTRUCT);
        set.addAll(collisions);
        set.removeAll(collisions);
        for (Integer integer : COLLISIONS)
        {
            Assert.assertTrue(set.add(new IntegerWithCast(integer)));
            Assert.assertFalse(set.add(new IntegerWithCast(integer)));
        }
        Assert.assertEquals(collisions.toSet(), set);
    }

    @Override
    @Test
    public void remove()
    {
        super.remove();

        final MutableSet<IntegerWithCast> set = this.classUnderTest();
        final MutableList<IntegerWithCast> collisions = COLLISIONS.collect(IntegerWithCast.CONSTRUCT);
        set.addAll(collisions);
        collisions.reverseForEach(new Procedure<IntegerWithCast>()
        {
            public void value(IntegerWithCast each)
            {
                Assert.assertFalse(set.remove(null));
                Assert.assertTrue(set.remove(each));
                Assert.assertFalse(set.remove(each));
                Assert.assertFalse(set.remove(null));
                Assert.assertFalse(set.remove(new IntegerWithCast(COLLISION_10)));
            }
        });

        Assert.assertEquals(UnifiedSet.<IntegerWithCast>newSet(), set);

        collisions.forEach(new Procedure<IntegerWithCast>()
        {
            public void value(IntegerWithCast each)
            {
                MutableSet<IntegerWithCast> set2 = AbstractMutableSetTestCase.this.classUnderTest();
                set2.addAll(collisions);

                Assert.assertFalse(set2.remove(null));
                Assert.assertTrue(set2.remove(each));
                Assert.assertFalse(set2.remove(each));
                Assert.assertFalse(set2.remove(null));
                Assert.assertFalse(set2.remove(new IntegerWithCast(COLLISION_10)));
            }
        });

        // remove the second-to-last item in a fully populated single chain to cause the last item to move
        MutableSet<Integer> set3 = this.newWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_4);
        Assert.assertTrue(set3.remove(COLLISION_3));
        Assert.assertEquals(UnifiedSet.newSetWith(COLLISION_1, COLLISION_2, COLLISION_4), set3);

        Assert.assertTrue(set3.remove(COLLISION_2));
        Assert.assertEquals(UnifiedSet.newSetWith(COLLISION_1, COLLISION_4), set3);

        // search a chain for a non-existent element
        MutableSet<Integer> chain = this.newWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_4);
        Assert.assertFalse(chain.remove(COLLISION_5));

        // search a deep chain for a non-existent element
        MutableSet<Integer> deepChain = this.newWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_4, COLLISION_5, COLLISION_6, COLLISION_7);
        Assert.assertFalse(deepChain.remove(COLLISION_8));

        // search for a non-existent element
        MutableSet<Integer> empty = this.classUnderTest();
        Assert.assertFalse(empty.remove(COLLISION_1));
    }

    @Override
    @Test
    public void retainAll()
    {
        super.retainAll();

        MutableList<Integer> collisions = MORE_COLLISIONS.clone();
        collisions.add(COLLISION_10);

        int size = MORE_COLLISIONS.size();
        for (int i = 0; i < size; i++)
        {
            MutableList<Integer> list = MORE_COLLISIONS.subList(0, i);
            MutableSet<Integer> set = this.<Integer>classUnderTest().withAll(list);
            Assert.assertFalse(set.retainAll(collisions));
            Assert.assertEquals(list.toSet(), set);
        }

        for (Integer item : MORE_COLLISIONS)
        {
            MutableSet<Integer> integers = this.<Integer>classUnderTest().withAll(MORE_COLLISIONS);
            @SuppressWarnings("BoxingBoxedValue")
            Integer keyCopy = new Integer(item);
            Assert.assertTrue(integers.retainAll(mList(keyCopy)));
            Assert.assertEquals(iSet(keyCopy), integers);
            Assert.assertNotSame(keyCopy, Iterate.getOnly(integers));
        }

        // retain all on a bucket with a single element
        MutableSet<Integer> singleCollisionBucket = this.newWith(COLLISION_1, COLLISION_2);
        singleCollisionBucket.remove(COLLISION_2);
        Assert.assertTrue(singleCollisionBucket.retainAll(FastList.newListWith(COLLISION_2)));
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();

        UnifiedSet<Integer> expected = UnifiedSet.newSetWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_4);
        Assert.assertNotEquals(expected, this.newWith(COLLISION_2, COLLISION_3, COLLISION_4, COLLISION_5));
        Assert.assertNotEquals(expected, this.newWith(COLLISION_1, COLLISION_3, COLLISION_4, COLLISION_5));
        Assert.assertNotEquals(expected, this.newWith(COLLISION_1, COLLISION_2, COLLISION_4, COLLISION_5));
        Assert.assertNotEquals(expected, this.newWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_5));

        Assert.assertEquals(expected, this.newWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_4));
    }

    @Override
    @Test
    public void forEach()
    {
        super.forEach();

        int size = MORE_COLLISIONS.size();
        for (int i = 1; i < size; i++)
        {
            MutableSet<Integer> set = this.classUnderTest();
            set.addAll(MORE_COLLISIONS.subList(0, i));
            MutableSet<Integer> result = UnifiedSet.newSet();
            set.forEach(CollectionAddProcedure.on(result));
            Assert.assertEquals(set, result);
        }

        // test iterating on a bucket with only one element
        MutableSet<Integer> set = this.newWith(COLLISION_1, COLLISION_2);
        set.remove(COLLISION_2);
        final Counter counter = new Counter();
        set.forEach(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                counter.increment();
            }
        });
        Assert.assertEquals(1, counter.getCount());
    }

    @Override
    @Test
    public void forEachWith()
    {
        super.forEachWith();

        final Object sentinel = new Object();
        int size = MORE_COLLISIONS.size();
        for (int i = 1; i < size; i++)
        {
            MutableSet<Integer> set = this.classUnderTest();
            set.addAll(MORE_COLLISIONS.subList(0, i));
            final MutableSet<Integer> result = UnifiedSet.newSet();

            set.forEachWith(new Procedure2<Integer, Object>()
            {
                public void value(Integer argument1, Object argument2)
                {
                    Assert.assertSame(sentinel, argument2);
                    result.add(argument1);
                }
            }, sentinel);
            Assert.assertEquals(set, result);
        }

        // test iterating on a bucket with only one element
        MutableSet<Integer> set = this.newWith(COLLISION_1, COLLISION_2);
        set.remove(COLLISION_2);
        Counter counter = new Counter();
        set.forEachWith(new Procedure2<Integer, Counter>()
        {
            public void value(Integer argument1, Counter argument2)
            {
                argument2.increment();
            }
        }, counter);
        Assert.assertEquals(1, counter.getCount());
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        super.forEachWithIndex();

        int size = MORE_COLLISIONS.size();
        for (int i = 1; i < size; i++)
        {
            MutableSet<Integer> set = this.classUnderTest();
            set.addAll(MORE_COLLISIONS.subList(0, i));
            final MutableSet<Integer> result = UnifiedSet.newSet();
            final MutableList<Integer> indexes = Lists.mutable.of();
            set.forEachWithIndex(new ObjectIntProcedure<Integer>()
            {
                public void value(Integer each, int index)
                {
                    result.add(each);
                    indexes.add(index);
                }
            });
            Assert.assertEquals(set, result);
            Assert.assertEquals(Interval.zeroTo(i - 1), indexes);
        }

        // test iterating on a bucket with only one element
        UnifiedSet<Integer> set = UnifiedSet.newSetWith(COLLISION_1, COLLISION_2);
        set.remove(COLLISION_2);
        final Counter counter = new Counter();
        set.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                counter.increment();
            }
        });
        Assert.assertEquals(1, counter.getCount());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_increment_past_end()
    {
        MutableSet<Integer> set = this.classUnderTest();
        Iterator<Integer> iterator = set.iterator();
        iterator.next();
        iterator.next();
    }

    @Test(expected = IllegalStateException.class)
    public void iterator_remove_without_next()
    {
        Iterator<Integer> iterator = this.<Integer>classUnderTest().iterator();
        iterator.remove();
    }

    @Override
    @Test
    public void toArray()
    {
        super.toArray();

        MutableSet<Integer> integers = this.newWith(1);
        Integer[] target = new Integer[3];
        target[0] = 2;
        target[1] = 2;
        target[2] = 2;
        integers.toArray(target);
        Assert.assertArrayEquals(new Integer[]{1, null, 2}, target);
    }
}
