/*
 * Copyright 2012 Goldman Sachs.
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

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.IfObjectIntProcedure;
import com.gs.collections.impl.block.procedure.IfProcedure;
import com.gs.collections.impl.block.procedure.IfProcedureWith;
import com.gs.collections.impl.lazy.iterator.SelectInstancesOfIterator;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * A SelectIterable is an iterable that filters a source iterable for instances of a Class as it iterates.
 */
@Immutable
public class SelectInstancesOfIterable<T>
        extends AbstractLazyIterable<T>
{
    private final Iterable<?> adapted;
    private final Class<T> clazz;

    public SelectInstancesOfIterable(Iterable<?> newAdapted, Class<T> clazz)
    {
        this.adapted = newAdapted;
        this.clazz = clazz;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.each(procedure);
    }

    public void each(Procedure<? super T> procedure)
    {
        Iterate.forEach((Iterable<T>) this.adapted, new IfProcedure<T>(Predicates.instanceOf(this.clazz), procedure));
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        Iterate.forEach((Iterable<T>) this.adapted, new IfObjectIntProcedure<T>(Predicates.instanceOf(this.clazz), objectIntProcedure));
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith((Iterable<T>) this.adapted, new IfProcedureWith<T, P>(Predicates.instanceOf(this.clazz), procedure), parameter);
    }

    public Iterator<T> iterator()
    {
        return new SelectInstancesOfIterator<T>(this.adapted, this.clazz);
    }
}
