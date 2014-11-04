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

package com.gs.collections.impl.map.fixed;

import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.FixedSizeMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.map.mutable.AbstractMutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;

abstract class AbstractMemoryEfficientMutableMap<K, V>
        extends AbstractMutableMap<K, V>
        implements FixedSizeMap<K, V>
{
    public V put(K key, V value)
    {
        throw new UnsupportedOperationException("Cannot call put() on " + this.getClass().getSimpleName());
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        throw new UnsupportedOperationException("Cannot call putAll() on " + this.getClass().getSimpleName());
    }

    public void clear()
    {
        throw new UnsupportedOperationException("Cannot call clear() on " + this.getClass().getSimpleName());
    }

    public V removeKey(K key)
    {
        throw new UnsupportedOperationException("Cannot call removeKey() on " + this.getClass().getSimpleName());
    }

    public <E> MutableMap<K, V> collectKeysAndValues(
            Iterable<E> iterable,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction)
    {
        throw new UnsupportedOperationException("Cannot call collectKeysAndValues() on " + this.getClass().getSimpleName());
    }

    @Override
    public V updateValue(K key, Function0<? extends V> factory, Function<? super V, ? extends V> function)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".updateValue() not implemented yet");
    }

    @Override
    public <P> V updateValueWith(K key, Function0<? extends V> factory, Function2<? super V, ? super P, ? extends V> function, P parameter)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".updateValueWith() not implemented yet");
    }

    public MutableMap<K, V> newEmpty()
    {
        return UnifiedMap.newMap();
    }

    @Override
    public MutableMap<K, V> newEmpty(int capacity)
    {
        return UnifiedMap.newMap(capacity);
    }

    @Override
    public MutableMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        MutableMap<K, V> map = this;
        for (Pair<? extends K, ? extends V> keyVal : keyValues)
        {
            map = map.withKeyValue(keyVal.getOne(), keyVal.getTwo());
        }
        return map;
    }

    @Override
    public MutableMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValues)
    {
        return this.withAllKeyValues(ArrayAdapter.adapt(keyValues));
    }

    @Override
    public MutableMap<K, V> withoutAllKeys(Iterable<? extends K> keys)
    {
        MutableMap<K, V> map = this;
        for (K key : keys)
        {
            map = map.withoutKey(key);
        }
        return map;
    }

    @Override
    public FixedSizeMap<K, V> tap(Procedure<? super V> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public abstract FixedSizeMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    @Override
    public abstract <R> FixedSizeMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    @Override
    public abstract <K2, V2> FixedSizeMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    @Override
    public abstract FixedSizeMap<K, V> reject(Predicate2<? super K, ? super V> predicate);
}
