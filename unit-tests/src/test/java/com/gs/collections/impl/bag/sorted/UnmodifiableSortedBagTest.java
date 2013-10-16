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

package com.gs.collections.impl.bag.sorted;

import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.bag.mutable.sorted.TreeBag;
import com.gs.collections.impl.bag.mutable.sorted.UnmodifiableSortedBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableSortedBag}.
 *
 * @since 4.2
 */
public class UnmodifiableSortedBagTest extends AbstractSortedBagTestCase
{
    private static final String LED_ZEPPELIN = "Led Zeppelin";
    private static final String METALLICA = "Metallica";

    private MutableSortedBag<String> mutableSortedBag;
    private MutableSortedBag<String> unmodifiableSortedBag;

    @Before
    public void setUp()
    {
        this.mutableSortedBag = TreeBag.newBagWith(METALLICA, "Bon Jovi", "Europe", "Scorpions");
        this.unmodifiableSortedBag = this.mutableSortedBag.asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedBag<T> classUnderTest()
    {
        return TreeBag.<T>newBag().asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedBag<T> classUnderTest(T... elements)
    {
        return TreeBag.<T>newBagWith(elements).asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedBag<T> classUnderTest(Comparator<? super T> comparator)
    {
        return TreeBag.<T>newBagWith(comparator).asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedBag<T> classUnderTest(Comparator<? super T> comparator, T... elements)
    {
        return TreeBag.<T>newBagWith(comparator, elements).asUnmodifiable();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToString_with_collection_containing_self()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1);
        collection.add(collection);
        String simpleName = collection.getClass().getSimpleName();
        String string = collection.toString();
        Assert.assertTrue(
                ("[1, (this " + simpleName + ")]").equals(string)
                        || ("[(this " + simpleName + "), 1]").equals(string));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void makeString_with_collection_containing_self()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        collection.add(collection);
        Assert.assertEquals(collection.toString(), '[' + collection.makeString() + ']');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void appendString_with_collection_containing_self()
    {
        MutableCollection<Object> collection = this.<Object>newWith(1, 2, 3);
        collection.add(collection);
        Appendable builder = new StringBuilder();
        collection.appendString(builder);
        Assert.assertEquals(collection.toString(), '[' + builder.toString() + ']');
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void asSynchronized()
    {
        this.classUnderTest().asSynchronized();
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableSortedBag.class, this.classUnderTest());
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(T one)
    {
        return TreeBag.newBagWith(one).asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(T one, T two)
    {
        return TreeBag.newBagWith(one, two).asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(T one, T two, T three)
    {
        return TreeBag.newBagWith(one, two, three).asUnmodifiable();
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(T... elements)
    {
        return TreeBag.newBagWith(elements).asUnmodifiable();
    }

    @Override
    @Test
    public void iterator()
    {
        MutableSortedBag<Integer> bag = this.newWith(-1, 0, 1, 1, 2);
        Iterator<Integer> iterator = bag.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(-1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(0), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(2), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        MutableSortedBag<Integer> revBag = this.newWith(Comparators.reverseNaturalOrder(), -1, 0, 1, 1, 2);
        Iterator<Integer> revIterator = revBag.iterator();
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(2), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(0), revIterator.next());
        Assert.assertTrue(revIterator.hasNext());
        Assert.assertEquals(Integer.valueOf(-1), revIterator.next());
        Assert.assertFalse(revIterator.hasNext());

        final Iterator<Integer> iterator3 = this.newWith(Comparators.reverseNaturalOrder(), 2, 1, 1, 0, -1).iterator();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                iterator3.remove();
            }
        });
        Assert.assertEquals(Integer.valueOf(2), iterator3.next());
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                iterator3.remove();
            }
        });
    }

    @Test
    public void testAsUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableSortedBag.class, this.<Object>classUnderTest().asUnmodifiable());
        MutableSortedBag<Object> bag = this.classUnderTest();
        Assert.assertSame(bag, bag.asUnmodifiable());
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
    @Test(expected = UnsupportedOperationException.class)
    public void add()
    {
        super.add();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addOccurrences()
    {
        super.addOccurrences();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addOccurrences_throws()
    {
        super.addOccurrences_throws();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeIf()
    {
        super.removeIf();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeOccurrences()
    {
        super.removeOccurrences();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeObject()
    {
        super.removeObject();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeOccurrences_throws()
    {
        super.removeOccurrences_throws();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void forEachWithOccurrences()
    {
        super.forEachWithOccurrences();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void with()
    {
        super.with();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withAll()
    {
        super.withAll();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void without()
    {
        super.without();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void withoutAll()
    {
        super.withoutAll();
    }
}
