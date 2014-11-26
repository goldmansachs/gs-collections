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

import com.gs.collections.api.block.procedure.Procedure;

public class DoNothingProcedure implements Procedure<Object>
{
    public static final DoNothingProcedure DO_NOTHING = new DoNothingProcedure();

    private static final long serialVersionUID = 1L;

    @Override
    public void value(Object each)
    {
        // Do nothing.
    }
}
