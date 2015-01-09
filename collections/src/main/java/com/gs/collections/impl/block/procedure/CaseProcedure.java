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

import java.util.List;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.tuple.Tuples;

/**
 * CaseProcedure allows developers to create an object form of a case statement, which instead of being based on
 * a single switch value is based on a list of predicate / procedure combinations.  For the first predicate
 * that returns true for a given value in the case statement, the corresponding procedure will be executed.
 */
public final class CaseProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final List<Pair<Predicate<? super T>, Procedure<? super T>>> predicateProcedures = Lists.mutable.empty();
    private Procedure<? super T> defaultProcedure;

    public CaseProcedure(Procedure<? super T> defaultProcedure)
    {
        this.defaultProcedure = defaultProcedure;
    }

    public CaseProcedure()
    {
    }

    public CaseProcedure<T> addCase(Predicate<? super T> predicate, Procedure<? super T> procedure)
    {
        this.predicateProcedures.add(Tuples.<Predicate<? super T>, Procedure<? super T>>pair(predicate, procedure));
        return this;
    }

    public CaseProcedure<T> setDefault(Procedure<? super T> procedure)
    {
        this.defaultProcedure = procedure;
        return this;
    }

    public void value(T argument)
    {
        for (Pair<Predicate<? super T>, Procedure<? super T>> pair : this.predicateProcedures)
        {
            if (pair.getOne().accept(argument))
            {
                pair.getTwo().value(argument);
                return;
            }
        }
        if (this.defaultProcedure != null)
        {
            this.defaultProcedure.value(argument);
        }
    }

    @Override
    public String toString()
    {
        return "new CaseProcedure(" + this.predicateProcedures + ')';
    }
}
