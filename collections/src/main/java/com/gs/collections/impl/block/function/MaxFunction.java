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

/**
 * MaxFunction contains iterator aware implementations of Max() for integers, doubles, and longs.
 */
public final class MaxFunction
{
    public static final Function2<Integer, Integer, Integer> INTEGER = new MaxIntegerFunction();
    public static final Function2<Double, Double, Double> DOUBLE = new MaxDoubleFunction();
    public static final Function2<Long, Long, Long> LONG = new MaxLongFunction();

    private MaxFunction()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    private static class MaxIntegerFunction implements Function2<Integer, Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer argument1, Integer argument2)
        {
            if (argument1 == null)
            {
                return argument2;
            }
            if (argument2 == null)
            {
                return argument1;
            }
            return argument1.intValue() > argument2.intValue() ? argument1 : argument2;
        }
    }

    private static class MaxDoubleFunction implements Function2<Double, Double, Double>
    {
        private static final long serialVersionUID = 1L;

        public Double value(Double argument1, Double argument2)
        {
            if (argument1 == null)
            {
                return argument2;
            }
            if (argument2 == null)
            {
                return argument1;
            }
            return argument1.doubleValue() > argument2.doubleValue() ? argument1 : argument2;
        }
    }

    private static class MaxLongFunction implements Function2<Long, Long, Long>
    {
        private static final long serialVersionUID = 1L;

        public Long value(Long argument1, Long argument2)
        {
            if (argument1 == null)
            {
                return argument2;
            }
            if (argument2 == null)
            {
                return argument1;
            }
            return argument1.longValue() > argument2.longValue() ? argument1 : argument2;
        }
    }
}
