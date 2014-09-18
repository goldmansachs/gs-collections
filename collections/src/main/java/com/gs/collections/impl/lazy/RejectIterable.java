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

package com.gs.collections.impl.lazy;

import java.util.Iterator;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.IfObjectIntProcedure;
import com.gs.collections.impl.block.procedure.IfProcedure;
import com.gs.collections.impl.block.procedure.IfProcedureWith;
import com.gs.collections.impl.lazy.iterator.SelectIterator;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * A RejectIterable is an iterable that filters a source iterable on a negative condition as it iterates.
 */
@Immutable
public class RejectIterable<T>
        extends AbstractLazyIterable<T>
{
    private final Iterable<T> adapted;
    private final Predicate<? super T> predicate;

    public RejectIterable(Iterable<T> newAdapted, Predicate<? super T> newPredicate)
    {
        this.adapted = newAdapted;
        this.predicate = Predicates.not(newPredicate);
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.each(procedure);
    }

    public void each(Procedure<? super T> procedure)
    {
        Iterate.forEach(this.adapted, new IfProcedure<T>(this.predicate, procedure));
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        Iterate.forEach(this.adapted, new IfObjectIntProcedure<T>(this.predicate, objectIntProcedure));
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.adapted, new IfProcedureWith<T, P>(this.predicate, procedure), parameter);
    }

    /**
     * We use a SelectIterator, since we have already negated the predicate
     */
    public Iterator<T> iterator()
    {
        return new SelectIterator<T>(this.adapted, this.predicate);
    }
}
