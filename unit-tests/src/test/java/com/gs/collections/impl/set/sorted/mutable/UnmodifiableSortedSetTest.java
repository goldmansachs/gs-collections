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

package com.gs.collections.impl.set.sorted.mutable;

import java.util.Comparator;
import java.util.NoSuchElementException;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableSortedSet}.
 */
public class UnmodifiableSortedSetTest extends AbstractSortedSetTestCase
{
    private static final String LED_ZEPPELIN = "Led Zeppelin";
    private static final String METALLICA = "Metallica";

    private MutableSortedSet<String> mutableSet;
    private MutableSortedSet<String> unmodifiableSet;

    @Before
    public void setUp()
    {
        this.mutableSet = TreeSortedSet.newSetWith(METALLICA, "Bon Jovi", "Europe", "Scorpions");
        this.unmodifiableSet = this.mutableSet.asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedSet<T> newWith(T... elements)
    {
        return TreeSortedSet.newSetWith(elements).asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedSet<T> newWith(Comparator<? super T> comparator, T... elements)
    {
        return TreeSortedSet.newSetWith(comparator, elements).asUnmodifiable();
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedSortedSet.class, this.newWith().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableSortedSet.class, this.newWith());
    }

    @Test
    public void testAsUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableSortedSet.class, this.newWith().asUnmodifiable());
        MutableSortedSet<Object> set = this.newWith();
        Assert.assertSame(set, set.asUnmodifiable());
    }

    @Test
    public void testEqualsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(this.mutableSet, this.unmodifiableSet);
        Verify.assertPostSerializedEqualsAndHashCode(this.unmodifiableSet);
        Verify.assertInstanceOf(UnmodifiableSortedSet.class, SerializeTestHelper.serializeDeserialize(this.unmodifiableSet));
    }

    @Test
    public void testNewEmpty()
    {
        MutableSortedSet<String> set = this.unmodifiableSet.newEmpty();
        set.add(LED_ZEPPELIN);
        Verify.assertContains(LED_ZEPPELIN, set);
    }

    @Override
    @Test
    public void testClone()
    {
        MutableSortedSet<String> set = this.newWith();
        MutableSortedSet<String> clone = set.clone();
        Assert.assertSame(clone, set);
    }

    @Override
    @Test
    public void min()
    {
        super.min();
        Assert.assertEquals("1", this.newWith("1", "3", "2").min(String::compareTo));
    }

    @Override
    @Test
    public void max()
    {
        super.max();
        Assert.assertEquals("3", this.newWith("1", "3", "2").max(String::compareTo));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws_without_comparator()
    {
        this.newWith().min();
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws_without_comparator()
    {
        this.newWith().max();
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void min_null_throws_without_comparator()
    {
        super.min_null_throws_without_comparator();
        this.newWith("1", null, "2").min();
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void max_null_throws_without_comparator()
    {
        super.max_null_throws_without_comparator();
        this.newWith("1", null, "2").max();
    }

    @Override
    @Test
    public void min_without_comparator()
    {
        super.min_without_comparator();
        Assert.assertEquals("1", this.newWith("1", "3", "2").min());
    }

    @Override
    @Test
    public void max_without_comparator()
    {
        super.max_without_comparator();
        Assert.assertEquals("3", this.newWith("1", "3", "2").max());
    }

    @Override
    @Test
    public void minBy()
    {
        super.minBy();
        Assert.assertEquals("1", this.newWith("1", "3", "2").minBy(Functions.getStringToInteger()));
    }

    @Override
    @Test
    public void maxBy()
    {
        super.maxBy();
        Assert.assertEquals("3", this.newWith("1", "3", "2").maxBy(Functions.getStringToInteger()));
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeObject()
    {
        super.removeObject();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeIfWith()
    {
        super.removeIfWith();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void clear()
    {
        super.clear();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addAll()
    {
        super.addAll();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addAllIterable()
    {
        super.addAllIterable();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeIf()
    {
        super.removeIf();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeAll()
    {
        super.removeAll();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeAllIterable()
    {
        super.removeAllIterable();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void retainAll()
    {
        super.retainAll();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void retainAllIterable()
    {
        super.retainAllIterable();
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();
        MutableCollection<Object> collection = this.newWith(1, 2);
        String toString = collection.toString();
        Assert.assertTrue("[1, 2]".equals(toString) || "[2, 1]".equals(toString));
    }

    @Override
    @Test
    public void makeString()
    {
        super.makeString();
        MutableCollection<Object> collection = this.newWith(1, 2, 3);
        Assert.assertEquals(collection.toString(), '[' + collection.makeString() + ']');
    }

    @Override
    @Test
    public void appendString()
    {
        super.appendString();
        MutableCollection<Object> collection = this.newWith(1, 2, 3);
        Appendable builder = new StringBuilder();
        collection.appendString(builder);
        Assert.assertEquals(collection.toString(), '[' + builder.toString() + ']');
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void getFirst()
    {
        super.getFirst();
        Assert.assertNotNull(this.newWith(1, 2, 3).getFirst());
        Assert.assertNull(this.newWith().getFirst());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void getLast()
    {
        super.getLast();
        Assert.assertNotNull(this.newWith(1, 2, 3).getLast());
        Assert.assertNull(this.newWith().getLast());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void subSet()
    {
        this.newWith(1, 2, 3).subSet(1, 3).clear();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void headSet()
    {
        this.newWith(1, 2, 3, 4).headSet(3).add(4);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void tailSet()
    {
        this.newWith(1, 2, 3, 4).tailSet(3).remove(1);
    }

    @Test
    public void serialization()
    {
        Verify.assertPostSerializedEqualsAndHashCode(this.newWith(1, 2, 3, 4, 5));
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void with()
    {
        this.newWith().with(1);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withAll()
    {
        this.newWith().withAll(FastList.newListWith(1, 2));
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void without()
    {
        this.newWith().without(2);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withoutAll()
    {
        this.newWith().withoutAll(FastList.newListWith(1, 2));
    }
}
