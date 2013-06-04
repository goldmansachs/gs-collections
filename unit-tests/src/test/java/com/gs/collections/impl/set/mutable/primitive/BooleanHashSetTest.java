/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.set.mutable.primitive;

import java.lang.reflect.Field;

import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import org.junit.Assert;
import org.junit.Test;

public class BooleanHashSetTest extends AbstractBooleanSetTestCase
{
    @Override
    protected BooleanHashSet classUnderTest()
    {
        return BooleanHashSet.newSetWith(true, false, true);
    }

    @Override
    protected BooleanHashSet newWith(boolean... elements)
    {
        return BooleanHashSet.newSetWith(elements);
    }

    @Test
    public void construction() throws Exception
    {
        BooleanHashSet set = new BooleanHashSet();
        Field table = BooleanHashSet.class.getDeclaredField("state");
        table.setAccessible(true);
        Assert.assertEquals(0, table.get(set));
    }

    @Override
    @Test
    public void newCollection()
    {
        super.newCollection();
        BooleanHashSet set0 = this.newWith();
        BooleanHashSet set1 = this.newWith(false);
        BooleanHashSet set2 = this.newWith(true);
        BooleanHashSet set3 = this.newWith(true, false);

        BooleanHashSet setFromList = BooleanHashSet.newSet(BooleanArrayList.newListWith(true, true, false));
        BooleanHashSet setFromSet0 = BooleanHashSet.newSet(set0);
        BooleanHashSet setFromSet1 = BooleanHashSet.newSet(set1);
        BooleanHashSet setFromSet2 = BooleanHashSet.newSet(set2);
        BooleanHashSet setFromSet3 = BooleanHashSet.newSet(set3);
        Assert.assertEquals(set3, setFromList);
        Assert.assertEquals(set0, setFromSet0);
        Assert.assertEquals(set1, setFromSet1);
        Assert.assertEquals(set2, setFromSet2);
        Assert.assertEquals(set3, setFromSet3);
    }

}
