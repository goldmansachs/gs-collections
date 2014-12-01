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

package com.gs.collections.impl.set.mutable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SetAdapter}.
 */
public class SetAdapterTest extends AbstractMutableSetTestCase
{
    @Override
    protected <T> SetAdapter<T> newWith(T... littleElements)
    {
        return new SetAdapter<>(new HashSet<>(UnifiedSet.newSetWith(littleElements)));
    }

    @Override
    @Test
    public void testToString()
    {
        MutableCollection<Object> collection = this.newWith(1);
        collection.add(collection);
        String simpleName = collection.getClass().getSimpleName();
        String string = collection.toString();
        Assert.assertTrue(
                ("[1, (this " + simpleName + ")]").equals(string)
                        || ("[(this " + simpleName + "), 1]").equals(string));
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableSet.class, SetAdapter.adapt(new HashSet<>()).asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableSet.class, this.newWith().asUnmodifiable());
    }

    @Test
    public void adapt()
    {
        MutableSet<Integer> adapter1 = SetAdapter.adapt(Sets.fixedSize.of(1, 2, 3, 4));
        MutableSet<Integer> adapter2 = new SetAdapter<Integer>(new HashSet<>()).with(1, 2, 3, 4);
        Verify.assertEqualsAndHashCode(adapter1, adapter2);
    }

    @Override
    @Test
    public void select()
    {
        super.select();
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 1, 2);
        Verify.assertContainsAll(this.newWith(-1, 2, 3, 4, 5).select(Predicates.lessThan(3),
                FastList.<Integer>newList()), -1, 2);
    }

    @Override
    @Test
    public void reject()
    {
        super.reject();
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4).reject(Predicates.lessThan(3)), 3, 4);
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4).reject(Predicates.lessThan(3),
                FastList.<Integer>newList()), 3, 4);
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4).collect(String::valueOf),
                "1",
                "2",
                "3",
                "4");
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4).collect(
                String::valueOf,
                FastList.<String>newList()), "1", "2", "3", "4");
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        MutableCollection<Integer> set1 = this.newWith(1, 2, 3);
        MutableCollection<Integer> set2 = this.newWith(1, 2, 3);
        MutableCollection<Integer> set3 = this.newWith(2, 3, 4);
        MutableSet<Integer> set4 = UnifiedSet.newSet();
        set4.add(2);
        set4.add(3);
        set4.add(4);
        Assert.assertNotEquals(set1, null);
        Verify.assertEqualsAndHashCode(set1, set1);
        Verify.assertEqualsAndHashCode(set1, set2);
        Assert.assertNotEquals(set2, set3);
        Verify.assertEqualsAndHashCode(set3, set4);
    }

    @Test
    public void newListWithSize()
    {
        Collection<Integer> collection = this.newWith(1, 2, 3);
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void serialization()
    {
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4, 5);
        MutableCollection<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Verify.assertContainsAll(deserializedCollection, 1, 2, 3, 4, 5);
        Assert.assertEquals(collection, deserializedCollection);
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableCollection<Integer> collection = this.newWith(1, 2, 3, 4);
        collection.forEachWithIndex((object, index) -> result.add(object));
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Override
    @Test
    public void getFirst()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getFirst());
        Assert.assertNull(this.newWith().getFirst());
    }

    @Override
    @Test
    public void getLast()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getLast());
        Assert.assertNull(this.newWith().getLast());
    }

    @Override
    @Test
    public void iterator()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3);
        MutableList<Integer> result = Lists.mutable.of();
        Iterator<Integer> iterator = objects.iterator();
        for (int i = objects.size(); i-- > 0; )
        {
            Integer integer = iterator.next();
            result.add(integer);
        }
        Verify.assertStartsWith(result.sortThis(Collections.<Integer>reverseOrder()), 3, 2, 1);
    }

    @Test
    public void withMethods()
    {
        Verify.assertContainsAll(this.newWith().with(1), 1);
        Verify.assertContainsAll(this.newWith().with(1, 2), 1, 2);
        Verify.assertContainsAll(this.newWith().with(1, 2, 3), 1, 2, 3);
        Verify.assertContainsAll(this.newWith().with(1, 2, 3, 4), 1, 2, 3, 4);
    }

    @Test
    public void returnType()
    {
        //Type HashSet is important here because it's not a MutableSet
        Set<Integer> set = new HashSet<>();
        MutableSet<Integer> integerSetAdapter = SetAdapter.adapt(set);
        Verify.assertInstanceOf(MutableSet.class, integerSetAdapter.select(ignored -> true));
    }

    @Test
    public void adaptNull()
    {
        Verify.assertThrows(NullPointerException.class, () -> new SetAdapter<>(null));

        Verify.assertThrows(NullPointerException.class, () -> SetAdapter.adapt(null));
    }
}
