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

package com.gs.collections.impl.lazy;

import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.mutable.FastList;
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
        this.undertest = new ChunkIterable<>(FastList.newListWith(1, 2, 3, 4, 5), 2);
    }

    @Test
    public void forEach()
    {
        this.undertest.forEach(Procedures.cast(this.buffer::append));
        Assert.assertEquals("[1, 2][3, 4][5]", this.buffer.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        this.undertest.forEachWithIndex((each, index) -> {
            this.buffer.append('|');
            this.buffer.append(each);
            this.buffer.append(index);
        });

        Assert.assertEquals("|[1, 2]0|[3, 4]1|[5]2", this.buffer.toString());
    }

    @Test
    public void forEachWith()
    {
        this.undertest.forEachWith((argument1, argument2) -> {
            this.buffer.append('|');
            this.buffer.append(argument1);
            this.buffer.append(argument2);
        }, 'A');
        Assert.assertEquals("|[1, 2]A|[3, 4]A|[5]A", this.buffer.toString());
    }
}
