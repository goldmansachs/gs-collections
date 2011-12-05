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

/**
 * IfProcedure allows developers to evaluate the specified procedure only when either predicate returns true.
 * If the result of evaluating the predicate is false, and the developer has specified that there
 * is an elseProcedure, then the elseProcedure is evaluated.
 */
public final class IfProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Predicate<? super T> predicate;
    private final Procedure<? super T> procedure;
    private final Procedure<? super T> elseProcedure;

    public IfProcedure(
            Predicate<? super T> predicate,
            Procedure<? super T> procedure,
            Procedure<? super T> elseProcedure)
    {
        this.predicate = predicate;
        this.procedure = procedure;
        this.elseProcedure = elseProcedure;
    }

    public IfProcedure(Predicate<? super T> predicate, Procedure<? super T> procedure)
    {
        this(predicate, procedure, null);
    }

    public void value(T object)
    {
        if (this.predicate.accept(object))
        {
            this.procedure.value(object);
        }
        else
        {
            if (this.elseProcedure != null)
            {
                this.elseProcedure.value(object);
            }
        }
    }

    @Override
    public String toString()
    {
        return "new IfProcedure(" + this.predicate + ", " + this.procedure + ", " + this.elseProcedure + ')';
    }
}
