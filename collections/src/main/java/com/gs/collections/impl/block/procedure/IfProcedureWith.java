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
import com.gs.collections.api.block.procedure.Procedure2;

/**
 * A conditional parameterized two argument procedure that effectively filters which objects should be used
 */
public final class IfProcedureWith<T, P>
        implements Procedure2<T, P>
{
    private static final long serialVersionUID = 1L;
    private final Procedure2<? super T, ? super P> procedure;
    private final Predicate<? super T> predicate;

    public IfProcedureWith(Predicate<? super T> newPredicate, Procedure2<? super T, ? super P> procedure)
    {
        this.predicate = newPredicate;
        this.procedure = procedure;
    }

    public void value(T each, P parm)
    {
        if (this.predicate.accept(each))
        {
            this.procedure.value(each, parm);
        }
    }
}
