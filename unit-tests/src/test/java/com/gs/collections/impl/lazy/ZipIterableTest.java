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

import com.gs.collections.impl.factory.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZipIterableTest
{
    private ZipIterable<Character, Integer> zipIterable;

    @Before
    public void setUp()
    {
        this.zipIterable = new ZipIterable<>(
                Lists.immutable.of('a', 'b', 'c'),
                Lists.immutable.of(0, 1, 2));
    }

    @Test
    public void forEachWithIndex()
    {
        StringBuilder sb = new StringBuilder();
        this.zipIterable.forEachWithIndex((each, index) -> {
            sb.append('|');
            sb.append(each.getOne());
            sb.append(each.getTwo());
            sb.append(index);
        });

        Assert.assertEquals("|a00|b11|c22", sb.toString());
    }

    @Test
    public void forEachWith()
    {
        StringBuilder sb = new StringBuilder();
        this.zipIterable.forEachWith((each, argument2) -> {
            sb.append(argument2);
            sb.append(each.getOne());
            sb.append(each.getTwo());
        }, "|");

        Assert.assertEquals("|a0|b1|c2", sb.toString());
    }
}
