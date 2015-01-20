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

package com.gs.collections.impl.memory.map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.list.primitive.IntInterval;

public class ImmutableMap0To10MemoryTest extends AbstractImmutableMapMemoryTest
{
    @Override
    protected IntList getData()
    {
        return IntInterval.zeroTo(10);
    }

    @Override
    protected String getTestType()
    {
        return "ImmutableMap_0to10";
    }

    @Override
    protected Function<Integer, ? extends Object> getKeyFactory()
    {
        return Functions.getIntegerPassThru();
    }
}
