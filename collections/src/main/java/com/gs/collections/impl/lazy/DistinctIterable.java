/*
 * Copyright 2014 Goldman Sachs.
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

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.lazy.iterator.DistinctIterator;
import com.gs.collections.impl.utility.internal.IterableIterate;
import net.jcip.annotations.Immutable;

/**
 * A DistinctIterable is an iterable that eliminates duplicates from a source iterable as it iterates.
 *
 * @since 5.0
 */
@Immutable
public class DistinctIterable<T>
        extends AbstractLazyIterable<T>
{
    private final Iterable<T> adapted;

    public DistinctIterable(Iterable<T> newAdapted)
    {
        this.adapted = newAdapted;
    }

    @Override
    public LazyIterable<T> distinct()
    {
        return this;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        IterableIterate.forEach(this, procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        IterableIterate.forEachWithIndex(this, objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        IterableIterate.forEachWith(this, procedure, parameter);
    }

    public Iterator<T> iterator()
    {
        return new DistinctIterator<T>(this.adapted);
    }
}
