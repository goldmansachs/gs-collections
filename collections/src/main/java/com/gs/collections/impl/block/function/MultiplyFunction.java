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

package com.gs.collections.impl.block.function;

import com.gs.collections.api.block.function.Function2;

public final class MultiplyFunction
{
    public static final Function2<Integer, Integer, Integer> INTEGER = new MultiplyIntegerFunction();
    public static final Function2<Double, Double, Double> DOUBLE = new MultiplyDoubleFunction();
    public static final Function2<Long, Long, Long> LONG = new MultiplyLongFunction();

    private MultiplyFunction()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    private static class MultiplyIntegerFunction implements Function2<Integer, Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer argument1, Integer argument2)
        {
            return argument1 * argument2;
        }
    }

    private static class MultiplyDoubleFunction implements Function2<Double, Double, Double>
    {
        private static final long serialVersionUID = 1L;

        public Double value(Double argument1, Double argument2)
        {
            return argument1 * argument2;
        }
    }

    private static class MultiplyLongFunction implements Function2<Long, Long, Long>
    {
        private static final long serialVersionUID = 1L;

        public Long value(Long argument1, Long argument2)
        {
            return argument1 * argument2;
        }
    }
}
