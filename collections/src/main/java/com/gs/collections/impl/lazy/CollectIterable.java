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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.lazy.iterator.CollectIterator;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * A CollectIterable is an iterable that transforms a source iterable using a function as it iterates.
 */
@Immutable
public class CollectIterable<T, V>
        extends AbstractLazyIterable<V>
{
    private final Iterable<T> adapted;
    private final Function<? super T, ? extends V> function;

    public CollectIterable(Iterable<T> newAdapted, Function<? super T, ? extends V> function)
    {
        this.adapted = newAdapted;
        this.function = function;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        Iterate.forEach(this.adapted, Functions.bind(procedure, this.function));
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        Iterate.forEachWithIndex(this.adapted, Functions.bind(objectIntProcedure, this.function));
    }

    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.adapted, Functions.bind(procedure, this.function), parameter);
    }

    public Iterator<V> iterator()
    {
        return new CollectIterator<T, V>(this.adapted, this.function);
    }

    @Override
    public int size()
    {
        return Iterate.sizeOf(this.adapted);
    }

    @Override
    public Object[] toArray()
    {
        Object[] array = Iterate.toArray(this.adapted);
        for (int i = 0; i < array.length; i++)
        {
            array[i] = this.function.valueOf((T) array[i]);
        }
        return array;
    }
}
