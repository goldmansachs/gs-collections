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

package ponzu.impl.block.factory;

import ponzu.api.block.HashingStrategy;
import ponzu.api.block.function.Function;

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

    public static <T> HashingStrategy<T> nullSafeHashingStrategy(final HashingStrategy<T> nonNullSafeStrategy)
    {
        return new HashingStrategy<T>()
        {
            public int computeHashCode(T object)
            {
                return object == null ? 0 : nonNullSafeStrategy.computeHashCode(object);
            }

            public boolean equals(T object1, T object2)
            {
                return object1 == null || object2 == null ? object1 == object2 : nonNullSafeStrategy.equals(object1, object2);
            }
        };
    }

    public static <T, V> HashingStrategy<T> fromFunction(final Function<T, V> function)
    {
        return new HashingStrategy<T>()
        {
            public int computeHashCode(T object)
            {
                return function.valueOf(object).hashCode();
            }

            public boolean equals(T object1, T object2)
            {
                return function.valueOf(object1).equals(function.valueOf(object2));
            }
        };
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
}
