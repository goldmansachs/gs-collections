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

package com.gs.collections.impl;

import java.util.Iterator;
import java.util.Map;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.set.mutable.SetAdapter;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.ImmutableEntry;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link UnmodifiableMap#entrySet()} .
 */
public class UnmodifiableMapEntrySetTest extends UnmodifiableMutableCollectionTestCase<Map.Entry<String, String>>
{
    @Override
    protected MutableSet<Map.Entry<String, String>> getCollection()
    {
        return SetAdapter.adapt(new UnmodifiableMap<String, String>(Maps.mutable.<String, String>of("1", "1", "2", "2")).entrySet());
    }

    private MutableSet<Map.Entry<String, String>> newCollection()
    {
        return SetAdapter.adapt(new UnmodifiableMap<String, String>(Maps.mutable.<String, String>of()).entrySet());
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void removeIfWith()
    {
        this.getCollection().removeIfWith(null, null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void removeIf()
    {
        Predicate<Object> predicate = null;
        this.getCollection().removeIf(predicate);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void addAll()
    {
        this.getCollection().addAll(null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void addAllIterable()
    {
        this.getCollection().addAllIterable(null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void removeAll()
    {
        this.getCollection().removeAll(null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void removeAllIterable()
    {
        this.getCollection().removeAllIterable(null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void retainAll()
    {
        this.getCollection().retainAll(null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void retainAllIterable()
    {
        this.getCollection().retainAllIterable(null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void withAll()
    {
        this.getCollection().withAll(null);
    }

    @Override
    @Test(expected = NullPointerException.class)
    public void withoutAll()
    {
        this.getCollection().withAll(null);
    }

    @Test
    public void testNewCollection()
    {
        MutableSet<Map.Entry<String, String>> collection = this.newCollection();
        Verify.assertEmpty(collection);
        Verify.assertSize(0, collection);
    }

    @Test
    public void equalsAndHashCode()
    {
        Object same = this.newWith(1, 2, 3);
        Verify.assertEqualsAndHashCode(this.newWith(1, 2, 3), this.newWith(1, 2, 3));
        Assert.assertNotEquals(this.newWith(1, 2, 3), this.newWith(1, 2));
    }

    @Override
    @Test
    public void newEmpty()
    {
        MutableSet<Map.Entry<String, String>> collection = this.newCollection().newEmpty();
        Verify.assertEmpty(collection);
        Verify.assertSize(0, collection);
        Assert.assertFalse(collection.notEmpty());
    }

    @Test
    public void toImmutable()
    {
        Verify.assertInstanceOf(ImmutableCollection.class, this.newCollection().toImmutable());
    }

    private <T> MutableSet<Map.Entry<T, T>> newWith(T one)
    {
        MutableMap<T, T> map = Maps.mutable.of(one, one);
        return SetAdapter.adapt(new UnmodifiableMap<T, T>(map).entrySet());
    }

    private <T> MutableSet<Map.Entry<T, T>> newWith(T one, T two)
    {
        MutableMap<T, T> map = Maps.mutable.of(one, one, two, two);
        return SetAdapter.adapt(new UnmodifiableMap<T, T>(map).entrySet());
    }

    private <T> MutableSet<Map.Entry<T, T>> newWith(T one, T two, T three)
    {
        MutableMap<T, T> map = Maps.mutable.of(one, one, two, two, three, three);
        return SetAdapter.adapt(new UnmodifiableMap<T, T>(map).entrySet());
    }

    private <T> MutableSet<Map.Entry<T, T>> newWith(T... littleElements)
    {
        MutableMap<T, T> map = Maps.mutable.of();
        for (int i = 0; i < littleElements.length; i++)
        {
            map.put(littleElements[i], littleElements[i]);
        }
        return SetAdapter.adapt(new UnmodifiableMap<T, T>(map).entrySet());
    }

    @Test
    public void testNewWith()
    {
        MutableSet<Map.Entry<Integer, Integer>> collection = this.newWith(1);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(1, collection);
        Verify.assertContains(this.entry(1), collection);
    }

    @Test
    public void testNewWithWith()
    {
        MutableSet<Map.Entry<Integer, Integer>> collection = this.newWith(1, 2);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(2, collection);
        Verify.assertContainsAll(collection, this.entry(1), this.entry(2));
    }

    @Test
    public void testNewWithWithWith()
    {
        MutableSet<Map.Entry<Integer, Integer>> collection = this.newWith(1, 2, 3);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(3, collection);
        Verify.assertContainsAll(collection, this.entry(1), this.entry(2), this.entry(3));
    }

    @Test
    public void testNewWithVarArgs()
    {
        MutableSet<Map.Entry<Integer, Integer>> collection = this.newWith(1, 2, 3, 4);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(4, collection);
        Verify.assertContainsAll(collection, this.entry(1), this.entry(2), this.entry(3), this.entry(4));
    }

    @Test
    public void containsAllIterable()
    {
        MutableSet<Map.Entry<Integer, Integer>> collection = this.newWith(1, 2, 3, 4);
        Assert.assertTrue(collection.containsAllIterable(Lists.immutable.of(this.entry(1), this.entry(2))));
        Assert.assertFalse(collection.containsAllIterable(Lists.immutable.of(this.entry(1), this.entry(5))));
    }

    @Test
    public void containsAllArray()
    {
        MutableSet<Map.Entry<Integer, Integer>> collection = this.newWith(1, 2, 3, 4);
        Assert.assertTrue(collection.containsAllArguments(this.entry(1), this.entry(2)));
        Assert.assertFalse(collection.containsAllArguments(this.entry(1), this.entry(5)));
    }

    @Test
    public void forEach()
    {
        MutableList<Map.Entry<Integer, Integer>> result = Lists.mutable.of();
        MutableSet<Map.Entry<Integer, Integer>> collection = this.newWith(1, 2, 3, 4);
        collection.forEach(CollectionAddProcedure.on(result));
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, this.entry(1), this.entry(2), this.entry(3), this.entry(4));
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.newCollection());
        Verify.assertNotEmpty(this.newWith(1, 2));
        Assert.assertTrue(this.newWith(1, 2).notEmpty());
    }

    @Test
    public void iterator()
    {
        MutableSet<Map.Entry<Integer, Integer>> objects = this.newWith(1, 2, 3);
        Iterator<Map.Entry<Integer, Integer>> iterator = objects.iterator();
        for (int i = objects.size(); i-- > 0; )
        {
            final Map.Entry<Integer, Integer> entry = iterator.next();
            Assert.assertEquals(ImmutableEntry.of(3 - i, 3 - i), entry);
            Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
            {
                public void run()
                {
                    entry.setValue(0);
                }
            });
        }
    }

    @Test
    public void toArray()
    {
        MutableSet<Map.Entry<Integer, Integer>> objects = this.newWith(1, 2, 3);
        Object[] array = objects.toArray();
        Verify.assertSize(3, array);
        Map.Entry<Integer, Integer>[] array2 = objects.toArray(new Map.Entry[3]);
        Verify.assertSize(3, array2);
    }

    private ImmutableEntry<Integer, Integer> entry(int i)
    {
        return ImmutableEntry.of(i, i);
    }
}
