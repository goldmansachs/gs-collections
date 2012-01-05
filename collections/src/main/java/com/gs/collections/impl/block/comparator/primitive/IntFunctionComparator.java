/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.block.comparator.primitive;

import com.gs.collections.api.block.SerializableComparator;
import com.gs.collections.api.block.function.primitive.IntFunction;

/**
 * A Comparator which takes an IntegerFunction to compare a primitive int value retrieved from an object.
 */
public class IntFunctionComparator<T>
        implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;

    private final IntFunction<T> function;

    public IntFunctionComparator(IntFunction<T> function)
    {
        this.function = function;
    }

    public int compare(T o1, T o2)
    {
        int one = this.function.intValueOf(o1);
        int two = this.function.intValueOf(o2);
        if (one < two)
        {
            return -1;
        }
        if (one > two)
        {
            return 1;
        }
        return 0;
    }
}
