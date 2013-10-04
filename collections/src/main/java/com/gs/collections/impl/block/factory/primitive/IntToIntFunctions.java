/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.block.factory.primitive;

import com.gs.collections.api.block.function.primitive.IntToIntFunction;

public final class IntToIntFunctions
{
    private static final IntToIntFunction INCREMENT = new IncrementIntToIntFunction();

    private static final IntToIntFunction DECREMENT = new DecrementIntToIntFunction();

    private IntToIntFunctions()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static IntToIntFunction increment()
    {
        return INCREMENT;
    }

    public static IntToIntFunction decrement()
    {
        return DECREMENT;
    }

    public static IntToIntFunction add(int intToAdd)
    {
        return new AddIntToIntFunction(intToAdd);
    }

    public static IntToIntFunction subtract(int intToSubtract)
    {
        return new SubtractIntToIntFunction(intToSubtract);
    }

    private static class IncrementIntToIntFunction implements IntToIntFunction
    {
        private static final long serialVersionUID = 1L;

        public int valueOf(int intParameter)
        {
            return intParameter + 1;
        }
    }

    private static class DecrementIntToIntFunction implements IntToIntFunction
    {
        private static final long serialVersionUID = 1L;

        public int valueOf(int intParameter)
        {
            return intParameter - 1;
        }
    }

    private static final class AddIntToIntFunction implements IntToIntFunction
    {
        private static final long serialVersionUID = 1L;
        private final int intToAdd;

        private AddIntToIntFunction(int intToAdd)
        {
            this.intToAdd = intToAdd;
        }

        public int valueOf(int intParameter)
        {
            return intParameter + this.intToAdd;
        }
    }

    private static final class SubtractIntToIntFunction implements IntToIntFunction
    {
        private static final long serialVersionUID = 1L;
        private final int intToSubtract;

        private SubtractIntToIntFunction(int intToSubtract)
        {
            this.intToSubtract = intToSubtract;
        }

        public int valueOf(int intParameter)
        {
            return intParameter - this.intToSubtract;
        }
    }
}
