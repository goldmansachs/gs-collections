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

package com.gs.collections.impl.set.mutable;

import java.util.NoSuchElementException;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.collection.mutable.AbstractCollectionTestCase;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableMutableSet}.
 */
public class UnmodifiableMutableSetTest extends AbstractCollectionTestCase
{
    private static final String LED_ZEPPELIN = "Led Zeppelin";
    private static final String METALLICA = "Metallica";

    private MutableSet<String> mutableSet;
    private MutableSet<String> unmodifiableSet;

    @Before
    public void setUp()
    {
        this.mutableSet = UnifiedSet.newSetWith(METALLICA, "Bon Jovi", "Europe", "Scorpions");
        this.unmodifiableSet = this.mutableSet.asUnmodifiable();
    }

    @Override
    protected <T> MutableSet<T> classUnderTest()
    {
        return UnifiedSet.<T>newSet().asUnmodifiable();
    }

    @Override
    protected <T> MutableSet<T> newWith(T one)
    {
        return UnifiedSet.newSetWith(one).asUnmodifiable();
    }

    @Override
    protected <T> MutableSet<T> newWith(T one, T two)
    {
        return UnifiedSet.newSetWith(one, two).asUnmodifiable();
    }

    @Override
    protected <T> MutableSet<T> newWith(T one, T two, T three)
    {
        return UnifiedSet.newSetWith(one, two, three).asUnmodifiable();
    }

    @Override
    protected <T> MutableSet<T> newWith(T... elements)
    {
        return UnifiedSet.newSetWith(elements).asUnmodifiable();
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
    public void remove()
    {
        super.remove();
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
        MutableCollection<Object> collection = this.<Object>newWith(1, 2);
        String string = collection.toString();
        Assert.assertTrue("[1, 2]".equals(string) || "[2, 1]".equals(string));
    }

    @Override
    @Test
    public void makeString()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        Assert.assertEquals(collection.toString(), '[' + collection.makeString() + ']');
    }

    @Override
    @Test
    public void appendString()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        Appendable builder = new StringBuilder();
        collection.appendString(builder);
        Assert.assertEquals(collection.toString(), '[' + builder.toString() + ']');
    }

    @Override
    @Test
    public void getFirst()
    {
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

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableSet.class, this.classUnderTest().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableSet.class, this.classUnderTest().asUnmodifiable());
    }

    @Test
    public void testEqualsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(this.mutableSet, this.unmodifiableSet);
        Verify.assertPostSerializedEqualsAndHashCode(this.unmodifiableSet);
    }

    @Test
    public void testNewEmpty()
    {
        MutableSet<String> set = this.unmodifiableSet.newEmpty();
        set.add(LED_ZEPPELIN);
        Verify.assertContains(LED_ZEPPELIN, set);
    }

    @Test
    public void testClone()
    {
        MutableSet<String> set = this.classUnderTest();
        MutableSet<String> clone = set.clone();
        Assert.assertSame(clone, set);
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
    @Test(expected = UnsupportedOperationException.class)
    public void with()
    {
        this.newWith(1, 2, 3).with(4);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withAll()
    {
        this.newWith(1, 2, 3).withAll(FastList.newListWith(4, 5));
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void without()
    {
        this.newWith(1, 2, 3).without(2);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withoutAll()
    {
        this.newWith(1, 2, 3, 4, 5).withoutAll(FastList.newListWith(2, 4));
    }
}
