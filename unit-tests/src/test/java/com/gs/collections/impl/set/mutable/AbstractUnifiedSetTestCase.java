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

package com.gs.collections.impl.set.mutable;

import java.util.SortedSet;

import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.IntegerWithCast;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractUnifiedSetTestCase extends AbstractMutableSetTestCase
{
    @Test
    public void addOnObjectWithCastInEquals()
    {
        if (this.newWith() instanceof SortedSet)
        {
            return;
        }
        MutableSet<IntegerWithCast> mutableSet = this.newWith(new IntegerWithCast(0));
        Assert.assertFalse(mutableSet.add(new IntegerWithCast(0)));
        Assert.assertTrue(mutableSet.add(null));
        Assert.assertFalse(mutableSet.add(null));
    }

    @Test
    public void retainAllFromKeySet_null_collision()
    {
        IntegerWithCast key = new IntegerWithCast(0);
        MutableSet<IntegerWithCast> mutableSet = this.newWith(null, key);

        Assert.assertFalse(mutableSet.retainAll(FastList.newListWith(key, null)));

        Assert.assertEquals(
                this.newWith(null, key),
                mutableSet);
    }

    @Test
    public void rehash_null_collision()
    {
        MutableSet<IntegerWithCast> mutableMap = this.newWith((IntegerWithCast) null);

        for (int i = 0; i < 1000; i++)
        {
            mutableMap.add(new IntegerWithCast(i));
        }
    }
}
