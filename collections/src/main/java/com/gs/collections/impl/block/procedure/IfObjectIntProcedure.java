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

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.Counter;

/**
 * A conditional ObjectIntProcedure that effectively filters which objects should be used
 */
public final class IfObjectIntProcedure<T>
        implements Procedure<T>
{
    private static final long serialVersionUID = 2L;

    private final Counter index = new Counter();
    private final ObjectIntProcedure<? super T> objectIntProcedure;
    private final Predicate<? super T> predicate;

    public IfObjectIntProcedure(Predicate<? super T> newPredicate, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.predicate = newPredicate;
        this.objectIntProcedure = objectIntProcedure;
    }

    public void value(T object)
    {
        if (this.predicate.accept(object))
        {
            this.objectIntProcedure.value(object, this.index.getCount());
            this.index.increment();
        }
    }
}
