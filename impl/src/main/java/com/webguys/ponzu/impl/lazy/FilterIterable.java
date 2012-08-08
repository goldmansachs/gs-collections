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

package com.webguys.ponzu.impl.lazy;

import java.util.Iterator;

import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.impl.block.procedure.IfObjectIntProcedure;
import com.webguys.ponzu.impl.block.procedure.IfProcedure;
import com.webguys.ponzu.impl.block.procedure.IfProcedureWith;
import com.webguys.ponzu.impl.lazy.iterator.FilterIterator;
import com.webguys.ponzu.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * A SelectIterable is an iterable that filters a source iterable on a condition as it iterates.
 */
@Immutable
public class FilterIterable<T>
        extends AbstractLazyIterable<T>
{
    private final Iterable<T> adapted;
    private final Predicate<? super T> predicate;

    public FilterIterable(Iterable<T> newAdapted, Predicate<? super T> newPredicate)
    {
        this.adapted = newAdapted;
        this.predicate = newPredicate;
    }

    public void forEach(Procedure<? super T> procedure)
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

    public Iterator<T> iterator()
    {
        return new FilterIterator<T>(this.adapted.iterator(), this.predicate);
    }
}
