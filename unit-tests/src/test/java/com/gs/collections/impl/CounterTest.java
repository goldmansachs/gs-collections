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

package com.gs.collections.impl;

import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CounterTest
{
    @Test
    public void basicLifecycle()
    {
        Counter counter = new Counter();

        Assert.assertEquals(0, counter.getCount());
        counter.increment();
        Assert.assertEquals(1, counter.getCount());
        counter.increment();
        Assert.assertEquals(2, counter.getCount());
        counter.add(16);
        Assert.assertEquals(18, counter.getCount());
        Interval.oneTo(1000).forEach(Procedures.cast(each -> counter.increment()));
        Assert.assertEquals(1018, counter.getCount());
        Assert.assertEquals("1018", counter.toString());

        counter.reset();
        Assert.assertEquals(0, counter.getCount());
        counter.add(4);
        Assert.assertEquals(4, counter.getCount());
        counter.increment();
        Assert.assertEquals(5, counter.getCount());

        Assert.assertEquals("5", counter.toString());
    }

    @Test
    public void equalsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(new Counter(1), new Counter(1));
        Assert.assertNotEquals(new Counter(1), new Counter(2));
    }

    @Test
    public void serialization()
    {
        Verify.assertPostSerializedEqualsAndHashCode(new Counter());
    }
}
