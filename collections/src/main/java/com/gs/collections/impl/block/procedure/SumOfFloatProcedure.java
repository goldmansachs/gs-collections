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
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.procedure.Procedure;

/**
 * Implementation of {@link Procedure} that holds on to the summation of elements seen so far,
 * determined by the {@link Function}.
 */
public class SumOfFloatProcedure<T> implements Procedure<T>, DoubleSumResultHolder
{
    private static final long serialVersionUID = 2L;

    private final FloatFunction<? super T> function;
    private double result;
    private double compensation;

    public SumOfFloatProcedure(FloatFunction<? super T> function)
    {
        this.function = function;
    }

    public double getResult()
    {
        return this.result;
    }

    public double getCompensation()
    {
        return this.compensation;
    }

    public void value(T each)
    {
        double adjustedValue = (double) this.function.floatValueOf(each) - this.compensation;
        double nextSum = this.result + adjustedValue;
        this.compensation = nextSum - this.result - adjustedValue;
        this.result = nextSum;
    }
}

