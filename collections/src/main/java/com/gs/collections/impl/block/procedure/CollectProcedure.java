/*
 * Copyright 2011 Goldman Sachs & Co.
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

import java.util.Collection;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;

/**
 * Applies a function to an object and adds the result to a target collection.
 */
public final class CollectProcedure<T, V> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Function<? super T, ? extends V> function;
    private final Collection<V> collection;

    public CollectProcedure(Function<? super T, ? extends V> function, Collection<V> targetCollection)
    {
        this.function = function;
        this.collection = targetCollection;
    }

    public void value(T object)
    {
        this.collection.add(this.function.valueOf(object));
    }

    public Collection<V> getCollection()
    {
        return this.collection;
    }
}
