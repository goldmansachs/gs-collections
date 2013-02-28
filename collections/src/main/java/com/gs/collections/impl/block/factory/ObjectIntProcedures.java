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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;

/**
 * Contains factory methods for creating {@link ObjectIntProcedure} instances.
 */
public final class ObjectIntProcedures
{
    private ObjectIntProcedures()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> ObjectIntProcedure<T> fromProcedure(Procedure<? super T> procedure)
    {
        return new ProcedureAdapter<T>(procedure);
    }

    private static final class ProcedureAdapter<T> implements ObjectIntProcedure<T>
    {
        private static final long serialVersionUID = 1L;
        private final Procedure<? super T> procedure;

        private ProcedureAdapter(Procedure<? super T> procedure)
        {
            this.procedure = procedure;
        }

        public void value(T each, int count)
        {
            this.procedure.value(each);
        }
    }
}
