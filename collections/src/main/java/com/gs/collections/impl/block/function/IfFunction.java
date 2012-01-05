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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;

public class IfFunction<T, V> implements Function<T, V>
{
    private static final long serialVersionUID = 1L;
    private final Predicate<? super T> predicate;
    private final Function<? super T, ? extends V> function;
    private final Function<? super T, ? extends V> elseFunction;

    public IfFunction(
            Predicate<? super T> newPredicate,
            Function<? super T, ? extends V> function)
    {
        this(newPredicate, function, null);
    }

    public IfFunction(Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            Function<? super T, ? extends V> elseFunction)
    {
        this.predicate = predicate;
        this.function = function;
        this.elseFunction = elseFunction;
    }

    public V valueOf(T object)
    {
        if (this.predicate.accept(object))
        {
            return this.function.valueOf(object);
        }
        if (this.elseFunction != null)
        {
            return this.elseFunction.valueOf(object);
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "new IfFunction(" + this.predicate + ", " + this.function + ", " + this.elseFunction + ')';
    }
}
