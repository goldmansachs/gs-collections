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

package com.gs.collections.impl.lazy;

import java.util.Iterator;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
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

    public void each(Procedure<? super T> procedure)
    {
        Iterate.forEach((Iterable<T>) this.adapted, new IfProcedure<T>(Predicates.instanceOf(this.clazz), procedure));
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        Iterate.forEach((Iterable<T>) this.adapted, new IfObjectIntProcedure<T>(Predicates.instanceOf(this.clazz), objectIntProcedure));
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith((Iterable<T>) this.adapted, new IfProcedureWith<T, P>(Predicates.instanceOf(this.clazz), procedure), parameter);
    }

    @Override
    public T getFirst()
    {
        return this.clazz.cast(Iterate.detect(this.adapted, Predicates.instanceOf(this.clazz)));
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return Iterate.anySatisfy((Iterable<T>) this.adapted, Predicates.and(Predicates.instanceOf(this.clazz), predicate));
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.anySatisfy(Predicates.bind(predicate, parameter));
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return Iterate.allSatisfy((Iterable<T>) this.adapted, new AllSatisfyPredicate<T>(Predicates.instanceOf(this.clazz), predicate));
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.allSatisfy(Predicates.bind(predicate, parameter));
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return Iterate.noneSatisfy((Iterable<T>) this.adapted, new AllSatisfyPredicate<T>(Predicates.instanceOf(this.clazz), predicate));
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.noneSatisfy(Predicates.bind(predicate, parameter));
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return Iterate.detect((Iterable<T>) this.adapted, Predicates.and(Predicates.instanceOf(this.clazz), predicate));
    }

    public Iterator<T> iterator()
    {
        return new SelectInstancesOfIterator<T>(this.adapted, this.clazz);
    }
}
