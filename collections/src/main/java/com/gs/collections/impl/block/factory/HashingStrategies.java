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
    private static final HashingStrategy<Object> DEFAULT_HASHING_STRATEGY = new DefaultStrategy();
    private static final HashingStrategy<Object> IDENTITY_HASHING_STRATEGY = new IdentityHashingStrategy();

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

    public static <T, V> HashingStrategy<T> fromFunction(Function<? super T, ? extends V> function)
    {
        return new FunctionHashingStrategy<T, V>(function);
    }

    public static HashingStrategy<Object> identityStrategy()
    {
        return IDENTITY_HASHING_STRATEGY;
    }

    public static <T> HashingStrategy<T> chain(HashingStrategy<T>... hashingStrategies)
    {
        if (hashingStrategies.length == 0)
        {
            throw new IllegalArgumentException("Nothing to chain");
        }

        return new ChainedHashingStrategy<T>(hashingStrategies);
    }

    public static <T, V1, V2> HashingStrategy<T> fromFunctions(Function<? super T, ? extends V1> one, Function<? super T, ? extends V2> two)
    {
        return HashingStrategies.chain(
                HashingStrategies.fromFunction(one),
                HashingStrategies.fromFunction(two));
    }

    public static <T, V1, V2, V3> HashingStrategy<T> fromFunctions(Function<? super T, ? extends V1> one, Function<? super T, ? extends V2> two, Function<? super T, ? extends V3> three)
    {
        return HashingStrategies.chain(
                HashingStrategies.fromFunction(one),
                HashingStrategies.fromFunction(two),
                HashingStrategies.fromFunction(three));
    }

    private static class DefaultStrategy implements HashingStrategy<Object>
    {
        private static final long serialVersionUID = 1L;

        public int computeHashCode(Object object)
        {
            return object.hashCode();
        }

        public boolean equals(Object object1, Object object2)
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

        private final Function<? super T, ? extends V> function;

        private FunctionHashingStrategy(Function<? super T, ? extends V> function)
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

    private static final class IdentityHashingStrategy implements HashingStrategy<Object>
    {
        private static final long serialVersionUID = 1L;

        public int computeHashCode(Object object)
        {
            return System.identityHashCode(object);
        }

        public boolean equals(Object object1, Object object2)
        {
            return object1 == object2;
        }
    }

    private static final class ChainedHashingStrategy<T> implements HashingStrategy<T>
    {
        private static final long serialVersionUID = 1L;
        private final HashingStrategy<T>[] hashingStrategies;

        private ChainedHashingStrategy(HashingStrategy<T>... hashingStrategies)
        {
            this.hashingStrategies = hashingStrategies;
        }

        public int computeHashCode(T object)
        {
            int hashCode = this.hashingStrategies[0].computeHashCode(object);
            for (int i = 1; i < this.hashingStrategies.length; i++)
            {
                hashCode = hashCode * 31 + this.hashingStrategies[i].computeHashCode(object);
            }
            return hashCode;
        }

        public boolean equals(T object1, T object2)
        {
            for (HashingStrategy<T> hashingStrategy : this.hashingStrategies)
            {
                if (!hashingStrategy.equals(object1, object2))
                {
                    return false;
                }
            }
            return true;
        }
    }
}
