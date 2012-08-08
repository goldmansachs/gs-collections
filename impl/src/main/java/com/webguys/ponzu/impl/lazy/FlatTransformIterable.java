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

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.impl.Counter;
import com.webguys.ponzu.impl.lazy.iterator.FlatTransformIterator;
import com.webguys.ponzu.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

@Immutable
public class FlatTransformIterable<T, V>
        extends AbstractLazyIterable<V>
{
    private final Iterable<T> adapted;
    private final Function<? super T, ? extends Iterable<V>> function;

    public FlatTransformIterable(Iterable<T> newAdapted, Function<? super T, ? extends Iterable<V>> function)
    {
        this.adapted = newAdapted;
        this.function = function;
    }

    public void forEach(final Procedure<? super V> procedure)
    {
        Iterate.forEach(this.adapted, new Procedure<T>()
        {
            public void value(T each)
            {
                Iterate.forEach(FlatTransformIterable.this.function.valueOf(each), procedure);
            }
        });
    }

    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        final Procedure<V> innerProcedure = new AdaptObjectIntProcedureToProcedure<V>(objectIntProcedure);
        Iterate.forEach(this.adapted, new Procedure<T>()
        {
            public void value(T each)
            {
                Iterable<V> iterable = FlatTransformIterable.this.function.valueOf(each);
                Iterate.forEach(iterable, innerProcedure);
            }
        });
    }

    public <P> void forEachWith(final Procedure2<? super V, ? super P> procedure, final P parameter)
    {
        Iterate.forEach(this.adapted, new Procedure<T>()
        {
            public void value(T each)
            {
                Iterate.forEachWith(FlatTransformIterable.this.function.valueOf(each), procedure, parameter);
            }
        });
    }

    public Iterator<V> iterator()
    {
        return new FlatTransformIterator<T, V>(this.adapted, this.function);
    }

    private static final class AdaptObjectIntProcedureToProcedure<V> implements Procedure<V>
    {
        private static final long serialVersionUID = 1L;

        private final Counter index;
        private final ObjectIntProcedure<? super V> objectIntProcedure;

        private AdaptObjectIntProcedureToProcedure(ObjectIntProcedure<? super V> objectIntProcedure)
        {
            this.objectIntProcedure = objectIntProcedure;
            this.index = new Counter();
        }

        public void value(V each)
        {
            this.objectIntProcedure.value(each, this.index.getCount());
            this.index.increment();
        }
    }
}
