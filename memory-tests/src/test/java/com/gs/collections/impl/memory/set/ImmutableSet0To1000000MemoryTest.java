/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.memory.set;

import java.util.Random;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.tuple.primitive.PrimitiveTuples;

public class ImmutableSet0To1000000MemoryTest extends AbstractImmutableSetMemoryTest
{
    private final Random random = new Random();

    @Override
    protected IntList getData()
    {
        return IntInterval.fromToBy(0, 1000000, 25000);
    }

    @Override
    protected String getTestType()
    {
        return "ImmutableSet_0to1000000";
    }

    @Override
    protected Function<Integer, ? extends Object> getKeyFactory()
    {
        return new Function<Integer, Object>()
        {
            @Override
            public Object valueOf(Integer integer)
            {
                return PrimitiveTuples.pair(
                        ImmutableSet0To1000000MemoryTest.this.random.nextInt(),
                        ImmutableSet0To1000000MemoryTest.this.random.nextInt());
            }
        };
    }
}
