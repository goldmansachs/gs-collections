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

import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;

/**
 * Implementation of {@link Procedure} that holds on to the minimum element seen so far,
 * determined by the {@link Function}.
 */
public class MaxProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    protected boolean visitedAtLeastOnce;
    protected T result;

    public T getResult()
    {
        if (!this.visitedAtLeastOnce)
        {
            throw new NoSuchElementException();
        }
        return this.result;
    }

    public void value(T each)
    {
        if (this.visitedAtLeastOnce)
        {
            if (((Comparable<T>) each).compareTo(this.result) > 0)
            {
                this.result = each;
            }
        }
        else
        {
            this.visitedAtLeastOnce = true;
            this.result = each;
        }
    }
}
