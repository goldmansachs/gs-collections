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

import java.util.Collection;
import java.util.Iterator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.UnmodifiableIteratorAdapter;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;
import net.jcip.annotations.Immutable;

/**
 * A LazyIterableAdapter wraps any iterable with the LazyIterable interface.
 */
@Immutable
public class LazyIterableAdapter<T>
        extends AbstractLazyIterable<T>
{
    private final Iterable<T> adapted;

    public LazyIterableAdapter(Iterable<T> newAdapted)
    {
        this.adapted = newAdapted;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.each(procedure);
    }

    public void each(Procedure<? super T> procedure)
    {
        Iterate.forEach(this.adapted, procedure);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        Iterate.forEachWithIndex(this.adapted, objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Iterate.forEachWith(this.adapted, procedure, parameter);
    }

    public Iterator<T> iterator()
    {
        return new UnmodifiableIteratorAdapter<T>(this.adapted.iterator());
    }

    @Override
    public <R extends Collection<T>> R into(R target)
    {
        Iterate.addAllIterable(this.adapted, target);
        return target;
    }

    @Override
    public LazyIterable<T> select(Predicate<? super T> predicate)
    {
        return LazyIterate.select(this.adapted, predicate);
    }

    @Override
    public LazyIterable<T> reject(Predicate<? super T> predicate)
    {
        return LazyIterate.reject(this.adapted, predicate);
    }

    @Override
    public <V> LazyIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return LazyIterate.collect(this.adapted, function);
    }

    @Override
    public <V> LazyIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return LazyIterate.flatCollect(this.adapted, function);
    }

    @Override
    public <V> LazyIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return LazyIterate.collectIf(this.adapted, predicate, function);
    }

    @Override
    public LazyIterable<T> take(int count)
    {
        return LazyIterate.take(this.adapted, count);
    }

    @Override
    public LazyIterable<T> drop(int count)
    {
        return LazyIterate.drop(this.adapted, count);
    }

    @Override
    public LazyIterable<T> distinct()
    {
        return LazyIterate.distinct(this.adapted);
    }

    @Override
    public Object[] toArray()
    {
        return Iterate.toArray(this.adapted);
    }

    @Override
    public int size()
    {
        return Iterate.sizeOf(this.adapted);
    }

    @Override
    public boolean isEmpty()
    {
        return Iterate.isEmpty(this.adapted);
    }
}
