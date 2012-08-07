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

package ponzu.impl.lazy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.tuple.Pair;
import ponzu.impl.factory.Lists;

public class ZipWithIndexIterableTest
{
    private ZipWithIndexIterable<Integer> iterableUnderTest;
    private final StringBuilder buffer = new StringBuilder();

    @Before
    public void setUp()
    {
        this.iterableUnderTest = new ZipWithIndexIterable<Integer>(Lists.immutable.of(1, 2, 3, 4));
    }

    private void assertBufferContains(String expected)
    {
        Assert.assertEquals(expected, this.buffer.toString());
    }

    @Test
    public void forEachWIthIndex()
    {
        this.iterableUnderTest.forEachWithIndex(new ObjectIntProcedure<Pair<Integer, Integer>>()
        {
            public void value(Pair<Integer, Integer> each, int index)
            {
                ZipWithIndexIterableTest.this.buffer.append("|");
                ZipWithIndexIterableTest.this.buffer.append(each.toString());
                ZipWithIndexIterableTest.this.buffer.append(",");
                ZipWithIndexIterableTest.this.buffer.append(index);
            }
        });
        this.assertBufferContains("|(1 . 0),0|(2 . 1),1|(3 . 2),2|(4 . 3),3");
    }

    @Test
    public void forEachWith()
    {
        this.iterableUnderTest.forEachWith(new Procedure2<Pair<Integer, Integer>, String>()
        {
            public void value(Pair<Integer, Integer> argument1, String argument2)
            {
                ZipWithIndexIterableTest.this.buffer.append("|");
                ZipWithIndexIterableTest.this.buffer.append(argument1.toString());
                ZipWithIndexIterableTest.this.buffer.append(",");
                ZipWithIndexIterableTest.this.buffer.append(argument2);
            }
        }, "A");
        this.assertBufferContains("|(1 . 0),A|(2 . 1),A|(3 . 2),A|(4 . 3),A");
    }
}
