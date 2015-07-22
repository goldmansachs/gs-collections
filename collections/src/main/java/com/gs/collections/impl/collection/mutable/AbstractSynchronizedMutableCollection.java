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

package com.gs.collections.impl.collection.mutable;

import java.util.Collection;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.collection.AbstractSynchronizedRichIterable;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public abstract class AbstractSynchronizedMutableCollection<T> extends AbstractSynchronizedRichIterable<T> implements MutableCollection<T>
{
    protected AbstractSynchronizedMutableCollection(MutableCollection<T> delegate)
    {
        this(delegate, null);
    }

    protected AbstractSynchronizedMutableCollection(MutableCollection<T> delegate, Object lock)
    {
        super(delegate, lock);
    }

    @Override
    protected MutableCollection<T> getDelegate()
    {
        return (MutableCollection<T>) super.getDelegate();
    }

    public boolean add(T o)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().add(o);
        }
    }

    public boolean remove(Object o)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().remove(o);
        }
    }

    public boolean addAll(Collection<? extends T> coll)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().addAll(coll);
        }
    }

    public boolean removeAll(Collection<?> coll)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().removeAll(coll);
        }
    }

    public boolean retainAll(Collection<?> coll)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().retainAll(coll);
        }
    }

    public void clear()
    {
        synchronized (this.getLock())
        {
            this.getDelegate().clear();
        }
    }

    public boolean removeIf(Predicate<? super T> predicate)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().removeIf(predicate);
        }
    }

    public <P> boolean removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().removeIfWith(predicate, parameter);
        }
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().addAllIterable(iterable);
        }
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().removeAllIterable(iterable);
        }
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().retainAllIterable(iterable);
        }
    }

    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().selectAndRejectWith(predicate, parameter);
        }
    }

    public <IV, P> IV injectIntoWith(
            IV injectValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().injectIntoWith(injectValue, function, parameter);
        }
    }

    public <K, V> MutableMap<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().aggregateInPlaceBy(groupBy, zeroValueFactory, mutatingAggregator);
        }
    }

    public <K, V> MutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().aggregateBy(groupBy, zeroValueFactory, nonMutatingAggregator);
        }
    }
}
