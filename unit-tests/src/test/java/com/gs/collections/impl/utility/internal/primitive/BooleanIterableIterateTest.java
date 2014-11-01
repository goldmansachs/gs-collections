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

package com.gs.collections.impl.utility.internal.primitive;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class BooleanIterableIterateTest
{
    private final BooleanIterable iterable = BooleanArrayList.newListWith(true, false, true);

    @Test
    public void forEach()
    {
        String[] sum = new String[2];
        sum[0] = "";
        sum[1] = "";
        BooleanIterableIterate.forEach(this.iterable, each -> sum[0] += each + " ");
        Assert.assertEquals("true false true ", sum[0]);
    }

    @Test
    public void select_target()
    {
        Verify.assertSize(2, BooleanIterableIterate.select(this.iterable, BooleanPredicates.equal(true), new BooleanArrayList(2)));
        Verify.assertSize(1, BooleanIterableIterate.select(this.iterable, BooleanPredicates.equal(false), new BooleanArrayList(3)));
    }

    @Test
    public void reject_target()
    {
        Verify.assertSize(1, BooleanIterableIterate.reject(this.iterable, BooleanPredicates.equal(true), new BooleanArrayList(1)));
        Verify.assertSize(2, BooleanIterableIterate.reject(this.iterable, BooleanPredicates.equal(false), new BooleanArrayList(1)));
    }

    @Test
    public void collect_target()
    {
        Verify.assertIterableSize(3, BooleanIterableIterate.collect(this.iterable, booleanParameter -> !booleanParameter, FastList.newList()));
    }

    @Test
    public void isEmpty()
    {
        Assert.assertFalse(BooleanIterableIterate.isEmpty(this.iterable));
    }

    @Test
    public void notEmpty()
    {
        Assert.assertTrue(BooleanIterableIterate.notEmpty(this.iterable));
    }
}
