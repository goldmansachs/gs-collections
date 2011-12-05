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

package com.gs.collections.impl.list.fixed;

import java.util.ListIterator;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test to check that {@link AbstractMemoryEfficientMutableList}s are Unmodifiable.
 */
public abstract class UnmodifiableMemoryEfficientListTestCase<T> extends UnmodifiableMutableCollectionTestCase<T>
{
    @Override
    protected abstract MutableList<T> getCollection();

    @Test
    public void listIterator()
    {
        final ListIterator<T> it = this.getCollection().listIterator();
        Assert.assertFalse(it.hasPrevious());
        Assert.assertEquals(-1, it.previousIndex());
        Assert.assertEquals(0, it.nextIndex());
        it.next();
        Assert.assertEquals(1, it.nextIndex());

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.remove();
            }
        });

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.add(null);
            }
        });
    }

    @Test
    public void addAllAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMemoryEfficientListTestCase.this.getCollection().addAll(0, new FastList<T>().with((T) null));
            }
        });
    }

    @Test
    public void addAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMemoryEfficientListTestCase.this.getCollection().add(0, null);
            }
        });
    }

    @Test
    public void removeFromIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMemoryEfficientListTestCase.this.getCollection().remove(0);
            }
        });
    }

    @Test
    public void subList()
    {
        final MutableList<T> subList = this.getCollection().subList(0, 1);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.clear();
            }
        });
    }

    @Override
    @Test
    public void newEmpty()
    {
        MutableList<T> list = this.getCollection().newEmpty();
        list.add(null);
        Verify.assertContains(null, list);
    }
}
