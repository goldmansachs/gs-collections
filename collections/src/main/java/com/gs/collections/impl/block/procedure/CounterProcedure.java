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

import com.gs.collections.api.block.procedure.Procedure;

/**
 * CounterProcedure wraps a specified procedure and keeps track of the number of times it is executed.
 */
public final class CounterProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private int count = 0;
    private final Procedure<T> procedure;

    public CounterProcedure(Procedure<T> procedure)
    {
        this.procedure = procedure;
    }

    public void value(T object)
    {
        this.incrementCounter();
        this.procedure.value(object);
    }

    private void incrementCounter()
    {
        this.count++;
    }

    public int getCount()
    {
        return this.count;
    }

    @Override
    public String toString()
    {
        return "counter: " + this.count + " procedure: " + this.procedure;
    }
}
