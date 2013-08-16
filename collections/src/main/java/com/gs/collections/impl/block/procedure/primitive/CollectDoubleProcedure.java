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

import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;

/**
 * Applies a doubleFunction to an object and adds the result to a target double collection.
 */
public final class CollectDoubleProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final DoubleFunction<? super T> doubleFunction;
    private final MutableDoubleCollection doubleCollection;

    public CollectDoubleProcedure(DoubleFunction<? super T> doubleFunction, MutableDoubleCollection targetCollection)
    {
        this.doubleFunction = doubleFunction;
        this.doubleCollection = targetCollection;
    }

    public void value(T object)
    {
        double value = this.doubleFunction.doubleValueOf(object);
        this.doubleCollection.add(value);
    }

    public MutableDoubleCollection getDoubleCollection()
    {
        return this.doubleCollection;
    }
}
