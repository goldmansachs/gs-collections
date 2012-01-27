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

package ponzu.impl.utility;

import ponzu.impl.block.factory.Functions;
import ponzu.impl.test.Verify;
import ponzu.impl.utility.internal.IterableIterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for the null handling behavior of {@link Iterate}, {@link ArrayIterate}, {@link ArrayListIterate},
 * {@link ListIterate}, {@link IterableIterate}.
 */
public class IterateNullTest
{
    // Iterate

    @Test
    public void collect()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.transform(null, Functions.getPassThru());
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.transform(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectIf()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.transformIf(null, null, Functions.getPassThru());
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.transformIf(null, null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.transformWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.transformWith(null, null, null);
            }
        });
    }

    @Test
    public void select()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.filter(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.filter(null, null);
            }
        });
    }

    @Test
    public void selectAndRejectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.partitionWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.partitionWith(null, null, null);
            }
        });
    }

    @Test
    public void partition()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.partition(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.partition(null, null);
            }
        });
    }

    @Test
    public void selectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.filterWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.filterWith(null, null, null);
            }
        });
    }

    @Test
    public void detect()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.find(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.find(null, null);
            }
        });
    }

    @Test
    public void detectIfNone()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.findIfNone(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.findIfNone(null, null, null);
            }
        });
    }

    @Test
    public void detectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.findWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.findWith(null, null, null);
            }
        });
    }

    @Test
    public void detectWithIfNone()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.findWithIfNone(null, null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.findWithIfNone(null, null, null, null);
            }
        });
    }

    @Test
    public void detectIndex()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.findIndex(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.findIndex(null, null);
            }
        });
    }

    @Test
    public void detectIndexWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.findIndexWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.findIndexWith(null, null, null);
            }
        });
    }

    @Test
    public void reject()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.filterNot(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.filterNot(null, null);
            }
        });
    }

    @Test
    public void rejectWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.filterNotWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.filterNotWith(null, null, null);
            }
        });
    }

    @Test
    public void injectInto()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.foldLeft(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.foldLeft(null, null, null);
            }
        });
    }

    @Test
    public void injectIntoWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.foldLeftWith(null, null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.foldLeftWith(null, null, null, null);
            }
        });
    }

    @Test
    public void forEach()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.forEach(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void forEachWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.forEachWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.forEachWith(null, null, null);
            }
        });
    }

    @Test
    public void forEachWithIndex()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.forEachWithIndex((Iterable<?>) null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.forEachWithIndex(null, null);
            }
        });
    }

    @Test
    public void anySatisfy()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.anySatisfy(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.anySatisfy(null, null);
            }
        });
    }

    @Test
    public void anySatisfyWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.anySatisfyWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.anySatisfyWith(null, null, null);
            }
        });
    }

    @Test
    public void allSatisfy()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.allSatisfy(null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.allSatisfy(null, null);
            }
        });
    }

    @Test
    public void allSatisfyWith()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                Iterate.allSatisfyWith(null, null, null);
            }
        });

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.allSatisfyWith(null, null, null);
            }
        });
    }

    // Others

    @Test
    public void collectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.transform(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.transform(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void collectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.transform(null, Functions.getPassThru());
            }
        });
    }

    @Test
    public void selectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.filter(null, null);
            }
        });
    }

    @Test
    public void selectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.filter(null, null);
            }
        });
    }

    @Test
    public void selectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.filter(null, null);
            }
        });
    }

    @Test
    public void detectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ArrayListIterate.find(null, null));
            }
        });
    }

    @Test
    public void detectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ListIterate.find(null, null));
            }
        });
    }

    @Test
    public void detectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(IterableIterate.find(null, null));
            }
        });
    }

    @Test
    public void rejectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.filterNot(null, null);
            }
        });
    }

    @Test
    public void rejectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.filterNot(null, null);
            }
        });
    }

    @Test
    public void rejectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.filterNot(null, null);
            }
        });
    }

    @Test
    public void injectArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ArrayListIterate.foldLeft(null, null, null));
            }
        });
    }

    @Test
    public void injectList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(ListIterate.foldLeft(null, null, null));
            }
        });
    }

    @Test
    public void injectIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                Assert.assertNull(IterableIterate.foldLeft(null, null, null));
            }
        });
    }

    @Test
    public void forEachArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void forEachList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void forEachIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.forEach(null, null);
            }
        });
    }

    @Test
    public void takeArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.take(null, 0);
            }
        });
    }

    @Test
    public void takeList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.take(null, 0);
            }
        });
    }

    @Test
    public void takeIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.take(null, 0);
            }
        });
    }

    @Test
    public void dropArrayList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.drop(null, 0);
            }
        });
    }

    @Test
    public void dropList()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.drop(null, 0);
            }
        });
    }

    @Test
    public void dropIterable()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                IterableIterate.drop(null, 0);
            }
        });
    }
}
