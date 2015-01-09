/*
 * Copyright 2015 Goldman Sachs.
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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.tuple.Tuples;

public class CaseFunction<T extends Comparable<? super T>, V> implements Function<T, V>
{
    private static final long serialVersionUID = 1L;

    private final MutableList<Pair<Predicate<? super T>, Function<? super T, ? extends V>>> predicateFunctions = Lists.mutable.empty();
    private Function<? super T, ? extends V> defaultFunction;

    public CaseFunction()
    {
    }

    public CaseFunction(Function<? super T, ? extends V> newDefaultFunction)
    {
        this.defaultFunction = newDefaultFunction;
    }

    public CaseFunction<T, V> addCase(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        this.predicateFunctions.add(Tuples.<Predicate<? super T>, Function<? super T, ? extends V>>pair(predicate, function));
        return this;
    }

    public CaseFunction<T, V> setDefault(Function<? super T, ? extends V> function)
    {
        this.defaultFunction = function;
        return this;
    }

    public V valueOf(T argument)
    {
        for (Pair<Predicate<? super T>, Function<? super T, ? extends V>> pair : this.predicateFunctions)
        {
            if (pair.getOne().accept(argument))
            {
                return pair.getTwo().valueOf(argument);
            }
        }

        if (this.defaultFunction != null)
        {
            return this.defaultFunction.valueOf(argument);
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "new CaseFunction(" + this.predicateFunctions + ')';
    }
}
