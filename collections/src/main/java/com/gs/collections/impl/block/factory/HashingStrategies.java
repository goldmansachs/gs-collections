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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function;

public final class HashingStrategies
{
    private static final HashingStrategy<?> DEFAULT_HASHING_STRATEGY = new DefaultStrategy();

    private HashingStrategies()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> HashingStrategy<T> defaultStrategy()
    {
        return (HashingStrategy<T>) DEFAULT_HASHING_STRATEGY;
    }

    public static <T> HashingStrategy<T> nullSafeHashingStrategy(HashingStrategy<T> nonNullSafeStrategy)
    {
        return new NullSafeHashingStrategy<T>(nonNullSafeStrategy);
    }

    public static <T, V> HashingStrategy<T> fromFunction(Function<T, V> function)
    {
        return new FunctionHashingStrategy<T, V>(function);
    }

    private static class DefaultStrategy<T> implements HashingStrategy<T>
    {
        private static final long serialVersionUID = 1L;

        public int computeHashCode(T object)
        {
            return object.hashCode();
        }

        public boolean equals(T object1, T object2)
        {
            return object1.equals(object2);
        }
    }

    private static final class NullSafeHashingStrategy<T> implements HashingStrategy<T>
    {
        private static final long serialVersionUID = 1L;

        private final HashingStrategy<T> nonNullSafeStrategy;

        private NullSafeHashingStrategy(HashingStrategy<T> nonNullSafeStrategy)
        {
            this.nonNullSafeStrategy = nonNullSafeStrategy;
        }

        public int computeHashCode(T object)
        {
            return object == null ? 0 : this.nonNullSafeStrategy.computeHashCode(object);
        }

        public boolean equals(T object1, T object2)
        {
            return object1 == null || object2 == null ? object1 == object2 : this.nonNullSafeStrategy.equals(object1, object2);
        }
    }

    private static final class FunctionHashingStrategy<T, V> implements HashingStrategy<T>
    {
        private static final long serialVersionUID = 1L;

        private final Function<T, V> function;

        private FunctionHashingStrategy(Function<T, V> function)
        {
            this.function = function;
        }

        public int computeHashCode(T object)
        {
            return this.function.valueOf(object).hashCode();
        }

        public boolean equals(T object1, T object2)
        {
            return this.function.valueOf(object1).equals(this.function.valueOf(object2));
        }
    }
}
