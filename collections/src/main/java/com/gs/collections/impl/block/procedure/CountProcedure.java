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
import com.gs.collections.impl.block.factory.Predicates;

/**
 * Applies a predicate to an object and increments a count if it returns true.
 */
public class CountProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Predicate<? super T> predicate;
    private int count;

    public CountProcedure(Predicate<? super T> newPredicate)
    {
        this.predicate = newPredicate;
    }

    public CountProcedure()
    {
        this(Predicates.alwaysTrue());
    }

    public void value(T object)
    {
        if (this.predicate.accept(object))
        {
            this.count++;
        }
    }

    public int getCount()
    {
        return this.count;
    }
}
