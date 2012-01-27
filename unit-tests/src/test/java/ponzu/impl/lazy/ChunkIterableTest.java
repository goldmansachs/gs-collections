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

import ponzu.api.RichIterable;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChunkIterableTest
{
    private final StringBuffer buffer = new StringBuffer();
    private ChunkIterable<Integer> undertest;

    @Before
    public void setUp()
    {
        this.undertest = new ChunkIterable<Integer>(FastList.newListWith(1, 2, 3, 4, 5), 2);
    }

    @Test
    public void forEachWithIndex()
    {
        this.undertest.forEachWithIndex(new ObjectIntProcedure<RichIterable<Integer>>()
        {
            public void value(RichIterable<Integer> each, int index)
            {
                ChunkIterableTest.this.buffer.append('|');
                ChunkIterableTest.this.buffer.append(each);
                ChunkIterableTest.this.buffer.append(index);
            }
        });

        Assert.assertEquals("|[1, 2]0|[3, 4]1|[5]2", this.buffer.toString());
    }

    @Test
    public void forEachWith()
    {
        this.undertest.forEachWith(new Procedure2<RichIterable<Integer>, Character>()
        {
            public void value(RichIterable<Integer> argument1, Character argument2)
            {
                ChunkIterableTest.this.buffer.append('|');
                ChunkIterableTest.this.buffer.append(argument1);
                ChunkIterableTest.this.buffer.append(argument2);
            }
        }, 'A');
        Assert.assertEquals("|[1, 2]A|[3, 4]A|[5]A", this.buffer.toString());
    }
}
