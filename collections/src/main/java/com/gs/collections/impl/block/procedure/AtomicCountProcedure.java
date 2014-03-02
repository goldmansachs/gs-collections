/*
 * Copyright 2014 Goldman Sachs.
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

import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;

/**
 * Applies a predicate to an object and increments a count if it returns true.
 */
public class AtomicCountProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Predicate<? super T> predicate;
    private final AtomicInteger count = new AtomicInteger(0);

    public AtomicCountProcedure(Predicate<? super T> predicate)
    {
        this.predicate = predicate;
    }

    public void value(T object)
    {
        if (this.predicate.accept(object))
        {
            this.count.incrementAndGet();
        }
    }

    public int getCount()
    {
        return this.count.get();
    }
}
