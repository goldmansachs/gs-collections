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

package ponzu.impl.lazy;

import java.util.Iterator;

import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.impl.block.predicate.DropIterablePredicate;
import ponzu.impl.block.procedure.IfObjectIntProcedure;
import ponzu.impl.block.procedure.IfProcedure;
import ponzu.impl.block.procedure.IfProcedureWith;
import ponzu.impl.lazy.iterator.DropIterator;
import ponzu.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * Iterates over the elements of the adapted Iterable skipping the first count elements or the full adapted Iterable if
 * the count is non-positive.
 */
@Immutable
public class DropIterable<T> extends AbstractLazyIterable<T>
{
    private final Iterable<T> adapted;
    private final int count;

    public DropIterable(Iterable<T> newAdapted, int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        this.adapted = newAdapted;
        this.count = count;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        Iterate.forEach(this.adapted, new IfProcedure<T>(new DropIterablePredicate<T>(this.count), procedure));
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        Iterate.forEach(this.adapted, new IfObjectIntProcedure<T>(new DropIterablePredicate<T>(this.count), objectIntProcedure));
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.adapted, new IfProcedureWith<T, P>(new DropIterablePredicate<T>(this.count), procedure), parameter);
    }

    public Iterator<T> iterator()
    {
        return new DropIterator<T>(this.adapted, this.count);
    }
}
