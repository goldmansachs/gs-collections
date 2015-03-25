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

package com.gs.collections.impl.memory;

import java.util.Random;

import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.set.primitive.IntSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;

public final class TestDataFactory
{
    public static final IntToObjectFunction<Integer> INTEGER_VALUE_OF = new IntToObjectFunction<Integer>()
    {
        public Integer valueOf(int each)
        {
            return Integer.valueOf(each);
        }
    };

    private TestDataFactory()
    {
    }

    public static IntList create(int size)
    {
        if (size > 0)
        {
            return IntInterval.from(-(size / 2) + 1).to(size / 2);
        }
        return new IntArrayList();
    }

    public static IntSet createRandomSet(int size)
    {
        if (size > 0)
        {
            MutableIntSet set = new IntHashSet();
            Random random = new Random((long) size);
            while (set.size() < size)
            {
                set.add(random.nextInt());
            }
            return set;
        }
        return new IntHashSet();
    }

    public static ImmutableList<Integer> createImmutableList(int size)
    {
        return TestDataFactory.create(size)
                .collect(INTEGER_VALUE_OF)
                .toList()
                .toImmutable();
    }

    public static ImmutableList<Integer> createRandomImmutableList(int size)
    {
        return TestDataFactory.createRandomSet(size)
                .collect(INTEGER_VALUE_OF)
                .toList()
                .toImmutable();
    }
}
