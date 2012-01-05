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

package com.gs.collections.impl.block.function.primitive;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.DoubleFunction;

/**
 * DoubleFunctionImpl is an abstract implementation of the DoubleFunction interface which can be subclassed
 * explicitly or as an anonymous inner class, without needing to override the valueOf method defined in
 * Function.
 */
public abstract class DoubleFunctionImpl<T>
        implements Function<T, Double>, DoubleFunction<T>
{
    private static final long serialVersionUID = 1L;

    private static final Double DOUBLE_ZERO = 0.0;

    public Double valueOf(T anObject)
    {
        double value = this.doubleValueOf(anObject);
        return value == 0.0 ? DOUBLE_ZERO : Double.valueOf(value);
    }
}
