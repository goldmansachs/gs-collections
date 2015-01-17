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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.lazy.iterator.TapIterator;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

/**
 * A TapIterable is an iterable that executes a procedure for each element before each iteration.
 */
@Immutable
public class TapIterable<T>
        extends AbstractLazyIterable<T>
{
    private final Iterable<T> adapted;
    private final Procedure<? super T> procedure;

    public TapIterable(Iterable<T> newAdapted, Procedure<? super T> procedure)
    {
        this.adapted = newAdapted;
        this.procedure = procedure;
    }

    public void each(final Procedure<? super T> procedure)
    {
        Iterate.forEach(this.adapted, new Procedure<T>()
        {
            public void value(T each)
            {
                TapIterable.this.procedure.value(each);
                procedure.value(each);
            }
        });
    }

    @Override
    public void forEachWithIndex(final ObjectIntProcedure<? super T> objectIntProcedure)
    {
        Iterate.forEachWithIndex(this.adapted, new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                TapIterable.this.procedure.value(each);
                objectIntProcedure.value(each, index);
            }
        });
    }

    @Override
    public <P> void forEachWith(final Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.adapted, new Procedure2<T, P>()
        {
            public void value(T each, P aParameter)
            {
                TapIterable.this.procedure.value(each);
                procedure.value(each, aParameter);
            }
        }, parameter);
    }

    @Override
    public boolean anySatisfy(final Predicate<? super T> predicate)
    {
        return Iterate.anySatisfy(this.adapted, new Predicate<T>()
        {
            public boolean accept(T each)
            {
                TapIterable.this.procedure.value(each);
                return predicate.accept(each);
            }
        });
    }

    @Override
    public boolean allSatisfy(final Predicate<? super T> predicate)
    {
        return Iterate.allSatisfy(this.adapted, new Predicate<T>()
        {
            public boolean accept(T each)
            {
                TapIterable.this.procedure.value(each);
                return predicate.accept(each);
            }
        });
    }

    @Override
    public boolean noneSatisfy(final Predicate<? super T> predicate)
    {
        return Iterate.noneSatisfy(this.adapted, new Predicate<T>()
        {
            public boolean accept(T each)
            {
                TapIterable.this.procedure.value(each);
                return predicate.accept(each);
            }
        });
    }

    @Override
    public T getFirst()
    {
        return Iterate.detect(this.adapted, new Predicate<T>()
        {
            public boolean accept(T each)
            {
                TapIterable.this.procedure.value(each);
                return true;
            }
        });
    }

    @Override
    public T detect(final Predicate<? super T> predicate)
    {
        return Iterate.detect(this.adapted, new Predicate<T>()
        {
            public boolean accept(T each)
            {
                TapIterable.this.procedure.value(each);
                return predicate.accept(each);
            }
        });
    }


    public Iterator<T> iterator()
    {
        return new TapIterator<T>(this.adapted, this.procedure);
    }
}
