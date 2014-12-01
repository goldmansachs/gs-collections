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
 * Implementation of {@link Procedure} that holds on to the maximum element seen so far,
 * determined by the {@link Function}.
 */
public class MaxByProcedure<T, V extends Comparable<? super V>> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    protected final Function<? super T, ? extends V> function;
    protected boolean visitedAtLeastOnce;
    protected T result;
    protected V cachedResultValue;

    public MaxByProcedure(Function<? super T, ? extends V> function)
    {
        this.function = function;
    }

    public boolean isVisitedAtLeastOnce()
    {
        return this.visitedAtLeastOnce;
    }

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
            V eachValue = this.function.valueOf(each);
            if (eachValue.compareTo(this.cachedResultValue) > 0)
            {
                this.result = each;
                this.cachedResultValue = eachValue;
            }
        }
        else
        {
            this.visitedAtLeastOnce = true;
            this.result = each;
            this.cachedResultValue = this.function.valueOf(each);
        }
    }
}
