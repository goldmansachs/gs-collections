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

package com.gs.collections.impl.block.function.checked;

import com.gs.collections.api.block.function.Function2;

public abstract class CheckedFunction2<T1, T2, R>
        implements Function2<T1, T2, R>
{
    private static final long serialVersionUID = 1L;

    public final R value(T1 argument1, T2 argument2)
    {
        try
        {
            return this.safeValue(argument1, argument2);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Checked exception caught in Function2", e);
        }
    }

    @SuppressWarnings("ProhibitedExceptionDeclared")
    public abstract R safeValue(T1 argument1, T2 argument2) throws Exception;
}
