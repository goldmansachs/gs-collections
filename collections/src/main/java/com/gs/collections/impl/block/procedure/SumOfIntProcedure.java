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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.procedure.Procedure;

/**
 * Implementation of {@link Procedure} that holds on to the summation of elements seen so far,
 * determined by the {@link Function}.
 */
public class SumOfIntProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 2L;

    private final IntFunction<? super T> function;
    private int result;

    public SumOfIntProcedure(IntFunction<? super T> function)
    {
        this.function = function;
    }

    public int getResult()
    {
        return this.result;
    }

    public void value(T each)
    {
        this.result += this.function.intValueOf(each);
    }
}

