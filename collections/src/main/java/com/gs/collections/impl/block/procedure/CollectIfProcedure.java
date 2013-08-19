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

package com.gs.collections.impl.block.procedure;

import java.util.Collection;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.list.mutable.FastList;

public final class CollectIfProcedure<T, V>
        implements Procedure<T>
{
    private static final long serialVersionUID = 1L;
    private final Function<? super T, ? extends V> function;
    private final Predicate<? super T> predicate;
    private final Collection<V> collection;

    public CollectIfProcedure(int taskSize, Function<? super T, ? extends V> function,
            Predicate<? super T> predicate)
    {
        this(FastList.<V>newList(taskSize), function, predicate);
    }

    public CollectIfProcedure(Collection<V> targetCollection, Function<? super T, ? extends V> function,
            Predicate<? super T> predicate)
    {
        this.function = function;
        this.predicate = predicate;
        this.collection = targetCollection;
    }

    public void value(T object)
    {
        if (this.predicate.accept(object))
        {
            this.collection.add(this.function.valueOf(object));
        }
    }

    public Collection<V> getCollection()
    {
        return this.collection;
    }
}
