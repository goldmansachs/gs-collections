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

package ponzu.impl.list.mutable;

import java.util.ListIterator;

import ponzu.api.block.procedure.Procedure;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.impl.Counter;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.factory.Lists;
import ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableMutableList}.
 */
public class UnmodifiableMutableListTest
{
    private static final String LED_ZEPPELIN = "Led Zeppelin";
    private static final String METALLICA = "Metallica";

    private MutableList<String> mutableList;
    private MutableList<String> unmodifiableList;

    @Before
    public void setUp()
    {
        this.mutableList = Lists.mutable.<String>of(METALLICA, "Bon Jovi", "Europe", "Scorpions");
        this.unmodifiableList = UnmodifiableMutableList.of(this.mutableList);
    }

    @Test
    public void equalsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(this.mutableList, this.unmodifiableList);
        Verify.assertPostSerializedEqualsAndHashCode(this.unmodifiableList);
    }

    @Test
    public void delegatingMethods()
    {
        Verify.assertItemAtIndex("Europe", 2, this.unmodifiableList);
        Assert.assertEquals(2, this.unmodifiableList.indexOf("Europe"));
        Assert.assertEquals(0, this.unmodifiableList.lastIndexOf(METALLICA));
    }

    @Test
    public void forEachFromTo()
    {
        final Counter counter = new Counter();
        this.unmodifiableList.forEach(1, 2, new Procedure<String>()
        {
            public void value(String band)
            {
                counter.increment();
            }
        });
        Assert.assertEquals(2, counter.getCount());
    }

    @Test
    public void listIterator()
    {
        final ListIterator<String> it = this.unmodifiableList.listIterator();
        Assert.assertFalse(it.hasPrevious());
        Assert.assertEquals(-1, it.previousIndex());
        Assert.assertEquals(METALLICA, it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(1, it.nextIndex());

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.set("Rick Astley");
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
                it.add("Gloria Gaynor");
            }
        });

        Assert.assertEquals(METALLICA, it.previous());
    }

    @Test
    public void sortThis()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableListTest.this.unmodifiableList.sortThis();
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
                UnmodifiableMutableListTest.this.unmodifiableList.sortThis(Comparators.naturalOrder());
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
                UnmodifiableMutableListTest.this.unmodifiableList.sortThisBy(Functions.getStringToInteger());
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
                UnmodifiableMutableListTest.this.unmodifiableList.reverseThis();
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
                UnmodifiableMutableListTest.this.unmodifiableList.addAll(0, Lists.mutable.<String>of("Madonna"));
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
                UnmodifiableMutableListTest.this.unmodifiableList.set(0, "Madonna");
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
                UnmodifiableMutableListTest.this.unmodifiableList.add(0, "Madonna");
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
                UnmodifiableMutableListTest.this.unmodifiableList.remove(0);
            }
        });
    }

    @Test
    public void subList()
    {
        final MutableList<String> subList = this.unmodifiableList.subList(1, 3);
        Assert.assertEquals(Lists.immutable.of("Bon Jovi", "Europe"), subList);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                subList.clear();
            }
        });
    }

    @Test
    public void newEmpty()
    {
        MutableList<String> list = this.unmodifiableList.newEmpty();
        list.add(LED_ZEPPELIN);
        Verify.assertContains(LED_ZEPPELIN, list);
    }

    @Test
    public void toImmutable()
    {
        Verify.assertInstanceOf(ImmutableList.class, this.unmodifiableList.toImmutable());
        Assert.assertEquals(this.unmodifiableList, this.unmodifiableList.toImmutable());
    }

    @Test
    public void asUnmodifiable()
    {
        Assert.assertSame(this.unmodifiableList, this.unmodifiableList.asUnmodifiable());
    }

    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, this.unmodifiableList.asSynchronized());
    }

    @Test
    public void toReversed()
    {
        Assert.assertEquals(Lists.mutable.ofAll(this.unmodifiableList).toReversed(), this.unmodifiableList.toReversed());
    }
}
