/*
 * Copyright 2015 Goldman Sachs.
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

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure;

public class InjectIntoProcedure<IV, T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private IV result;
    private final Function2<? super IV, ? super T, ? extends IV> function;

    public InjectIntoProcedure(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        this.result = injectedValue;
        this.function = function;
    }

    public void value(T each)
    {
        this.result = this.function.value(this.result, each);
    }

    public IV getResult()
    {
        return this.result;
    }
}
