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

package com.gs.collections.impl.block.comparator;

import java.util.Comparator;

import com.gs.collections.api.block.SerializableComparator;
import com.gs.collections.api.block.function.Function;

/**
 * Simple {@link Comparator} that uses a {@link Function}
 * to select a value from the underlying object and compare it against a known value to determine ordering.
 */
public class FunctionComparator<T, V>
        implements SerializableComparator<T>
{
    private static final long serialVersionUID = 1L;
    private final Function<? super T, ? extends V> function;
    private final Comparator<V> comparator;

    public FunctionComparator(Function<? super T, ? extends V> function, Comparator<V> comparator)
    {
        this.function = function;
        this.comparator = comparator;
    }

    public int compare(T o1, T o2)
    {
        V attrValue1 = this.function.valueOf(o1);
        V attrValue2 = this.function.valueOf(o2);
        return this.comparator.compare(attrValue1, attrValue2);
    }
}
