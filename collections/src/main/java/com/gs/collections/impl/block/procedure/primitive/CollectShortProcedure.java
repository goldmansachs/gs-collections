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

import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.primitive.MutableShortCollection;

/**
 * Applies a shortFunction to an object and adds the result to a target short collection.
 */
public final class CollectShortProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final ShortFunction<? super T> shortFunction;
    private final MutableShortCollection shortCollection;

    public CollectShortProcedure(ShortFunction<? super T> shortFunction, MutableShortCollection targetCollection)
    {
        this.shortFunction = shortFunction;
        this.shortCollection = targetCollection;
    }

    public void value(T object)
    {
        short value = this.shortFunction.shortValueOf(object);
        this.shortCollection.add(value);
    }

    public MutableShortCollection getShortCollection()
    {
        return this.shortCollection;
    }
}
