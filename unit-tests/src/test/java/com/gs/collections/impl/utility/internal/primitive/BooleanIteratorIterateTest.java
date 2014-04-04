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
import org.junit.Test;

public class BooleanIteratorIterateTest
{
    private final BooleanIterable iterable = BooleanArrayList.newListWith(true, false, true);

    @Test
    public void select_target()
    {
        Verify.assertSize(1, BooleanIteratorIterate.select(this.iterable.booleanIterator(), BooleanPredicates.equal(false), new BooleanArrayList(2)));
        Verify.assertSize(2, BooleanIteratorIterate.select(this.iterable.booleanIterator(), BooleanPredicates.equal(true), new BooleanArrayList(3)));
    }

    @Test
    public void reject_target()
    {
        Verify.assertSize(1, BooleanIteratorIterate.reject(this.iterable.booleanIterator(), BooleanPredicates.equal(true), new BooleanArrayList(1)));
        Verify.assertSize(2, BooleanIteratorIterate.reject(this.iterable.booleanIterator(), BooleanPredicates.equal(false), new BooleanArrayList(0)));
    }

    @Test
    public void collect_target()
    {
        Verify.assertIterableSize(3, BooleanIteratorIterate.collect(this.iterable.booleanIterator(), String::valueOf, FastList.<String>newList()));
    }
}
