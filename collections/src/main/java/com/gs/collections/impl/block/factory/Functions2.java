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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;

/**
 * Contains factory methods for creating {@link Function2} instances.
 */
public final class Functions2
{
    private static final Function2<Integer, Integer, Integer> INTEGER_ADDITION = new IntegerAddition();

    private Functions2()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T, V, P> Function2<T, P, V> fromFunction(Function<? super T, ? extends V> function)
    {
        return new FunctionAdapter<T, P, V>(function);
    }

    public static Function2<Integer, Integer, Integer> integerAddition()
    {
        return INTEGER_ADDITION;
    }

    private static final class FunctionAdapter<T, P, V> implements Function2<T, P, V>
    {
        private static final long serialVersionUID = 1L;
        private final Function<? super T, ? extends V> function;

        private FunctionAdapter(Function<? super T, ? extends V> function)
        {
            this.function = function;
        }

        public V value(T each, P parameter)
        {
            return this.function.valueOf(each);
        }
    }

    private static class IntegerAddition implements Function2<Integer, Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer aggregate, Integer value)
        {
            return aggregate + value;
        }
    }
}
