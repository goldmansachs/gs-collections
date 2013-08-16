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

import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;

/**
 * Applies a floatFunction to an object and adds the result to a target float collection.
 */
public final class CollectFloatProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final FloatFunction<? super T> floatFunction;
    private final MutableFloatCollection floatCollection;

    public CollectFloatProcedure(FloatFunction<? super T> floatFunction, MutableFloatCollection targetCollection)
    {
        this.floatFunction = floatFunction;
        this.floatCollection = targetCollection;
    }

    public void value(T object)
    {
        float value = this.floatFunction.floatValueOf(object);
        this.floatCollection.add(value);
    }

    public MutableFloatCollection getFloatCollection()
    {
        return this.floatCollection;
    }
}
