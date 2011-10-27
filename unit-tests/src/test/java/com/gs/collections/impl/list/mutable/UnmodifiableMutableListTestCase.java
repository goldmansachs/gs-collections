/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.list.mutable;

import java.util.ListIterator;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.list.fixed.UnmodifiableMemoryEfficientListTestCase;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link UnmodifiableMutableList}.
 */
public abstract class UnmodifiableMutableListTestCase extends UnmodifiableMemoryEfficientListTestCase<Integer>
{
    @Test
    public void testClone()
    {
        Assert.assertEquals(this.getCollection(), this.getCollection().clone());
        Assert.assertNotSame(this.getCollection(), this.getCollection().clone());
    }

    @Test
    public void serialization()
    {
        Verify.assertPostSerializedEqualsAndHashCode(this.getCollection());
    }

    @Override
    @Test
    public void subList()
    {
        super.subList();
        final MutableList<Integer> subList = this.getCollection().subList(0, 1);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.clear();
            }
        });
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.set(0, null);
            }
        });
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.add(0, null);
            }
        });
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.add(null);
            }
        });
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.remove(0);
            }
        });
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.remove(null);
            }
        });
    }

    @Override
    @Test
    public void listIterator()
    {
        final ListIterator<Integer> it = this.getCollection().listIterator();
        Assert.assertFalse(it.hasPrevious());
        Assert.assertEquals(-1, it.previousIndex());
        Assert.assertEquals(0, it.nextIndex());
        it.next();
        Assert.assertEquals(1, it.nextIndex());

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.set(null);
            }
        });

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
    public void subListListIterator()
    {
        final ListIterator<Integer> it = this.getCollection().subList(0, 1).listIterator();
        Assert.assertFalse(it.hasPrevious());
        Assert.assertEquals(-1, it.previousIndex());
        Assert.assertEquals(0, it.nextIndex());
        it.next();
        Assert.assertEquals(1, it.nextIndex());

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.set(null);
            }
        });

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
    public void set()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().set(0, null);
            }
        });
    }

    @Override
    @Test
    public void addAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().add(0, null);
            }
        });
    }

    @Override
    @Test
    public void addAllAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().addAll(0, null);
            }
        });
    }

    @Test
    public void removeAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().remove(0);
            }
        });
    }

    @Test
    public void setAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().set(0, null);
            }
        });
    }

    @Test
    public void sortThis()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().sortThis();
            }
        });
    }

    @Test
    public void sortThisWithComparator()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().sortThis(Comparators.naturalOrder());
            }
        });
    }

    @Test
    public void sortThisBy()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().sortThisBy(Functions.getToString());
            }
        });
    }

    @Test
    public void reverseThis()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTestCase.this.getCollection().reverseThis();
            }
        });
    }
}
