/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.block.procedure.primitive;

import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.primitive.MutableLongCollection;

/**
 * Applies a longFunction to an object and adds the result to a target long collection.
 */
public final class CollectLongProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final LongFunction<? super T> longFunction;
    private final MutableLongCollection longCollection;

    public CollectLongProcedure(LongFunction<? super T> longFunction, MutableLongCollection targetCollection)
    {
        this.longFunction = longFunction;
        this.longCollection = targetCollection;
    }

    public void value(T object)
    {
        long value = this.longFunction.longValueOf(object);
        this.longCollection.add(value);
    }

    public MutableLongCollection getLongCollection()
    {
        return this.longCollection;
    }
}
