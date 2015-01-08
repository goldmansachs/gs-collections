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

package com.gs.collections.impl.map;

import java.util.Map;
import java.util.Set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMapIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.AbstractSynchronizedRichIterable;
import com.gs.collections.impl.tuple.AbstractImmutableEntry;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;

/**
 * A synchronized view of a map.
 */
public abstract class AbstractSynchronizedMapIterable<K, V>
        extends AbstractSynchronizedRichIterable<V>
        implements MutableMapIterable<K, V>
{
    protected AbstractSynchronizedMapIterable(MutableMapIterable<K, V> delegate)
    {
        super(delegate, null);
    }

    protected AbstractSynchronizedMapIterable(MutableMapIterable<K, V> delegate, Object lock)
    {
        super(delegate, lock);
    }

    @Override
    protected MutableMapIterable<K, V> getDelegate()
    {
        return (MutableMapIterable<K, V>) super.getDelegate();
    }

    public V get(Object key)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().get(key);
        }
    }

    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().getIfAbsent(key, function);
        }
    }

    public V getIfAbsentValue(K key, V value)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().getIfAbsentValue(key, value);
        }
    }

    public <P> V getIfAbsentWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().getIfAbsentWith(key, function, parameter);
        }
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().ifPresentApply(key, function);
        }
    }

    public boolean containsKey(Object key)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().containsKey(key);
        }
    }

    public boolean containsValue(Object value)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().containsValue(value);
        }
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        synchronized (this.lock)
        {
            this.getDelegate().forEachValue(procedure);
        }
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        synchronized (this.lock)
        {
            this.getDelegate().forEachKey(procedure);
        }
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure2)
    {
        synchronized (this.lock)
        {
            this.getDelegate().forEachKeyValue(procedure2);
        }
    }

    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().detect(predicate);
        }
    }

    public V getIfAbsentPut(K key, Function0<? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().getIfAbsentPut(key, function);
        }
    }

    public V getIfAbsentPut(K key, V value)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().getIfAbsentPut(key, value);
        }
    }

    public V getIfAbsentPutWithKey(K key, Function<? super K, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().getIfAbsentPutWithKey(key, function);
        }
    }

    public <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().getIfAbsentPutWith(key, function, parameter);
        }
    }

    public V put(K key, V value)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().put(key, value);
        }
    }

    public V remove(Object key)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().remove(key);
        }
    }

    public V removeKey(K key)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().removeKey(key);
        }
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        synchronized (this.lock)
        {
            this.getDelegate().putAll(map);
        }
    }

    public void clear()
    {
        synchronized (this.lock)
        {
            this.getDelegate().clear();
        }
    }

    public V add(Pair<K, V> keyValuePair)
    {
        synchronized (this.lock)
        {
            return this.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
    }

    public V updateValue(K key, Function0<? extends V> factory, Function<? super V, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().updateValue(key, factory, function);
        }
    }

    public <P> V updateValueWith(
            K key,
            Function0<? extends V> factory,
            Function2<? super V, ? super P, ? extends V> function,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.getDelegate().updateValueWith(key, factory, function, parameter);
        }
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        synchronized (this.lock)
        {
            Set<Entry<K, V>> entries = this.getDelegate().entrySet();
            Iterable<Pair<K, V>> pairs = Iterate.collect(entries, AbstractImmutableEntry.<K, V>getPairFunction());
            return LazyIterate.adapt(pairs);
        }
    }
}

