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

package com.webguys.ponzu.impl.map.fixed;

import java.util.Collection;
import java.util.Map;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.map.FixedSizeMap;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.list.fixed.ArrayAdapter;
import com.webguys.ponzu.impl.map.mutable.AbstractMutableMap;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;

abstract class AbstractMemoryEfficientMutableMap<K, V>
        extends AbstractMutableMap<K, V>
        implements FixedSizeMap<K, V>
{
    @Override
    public abstract MutableMap<K, V> clone();

    public V put(K key, V value)
    {
        throw new UnsupportedOperationException();
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        throw new UnsupportedOperationException();
    }

    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    public V removeKey(K key)
    {
        throw new UnsupportedOperationException();
    }

    public <E> MutableMap<K, V> transformKeysAndValues(
            Collection<E> collection,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction)
    {
        throw new UnsupportedOperationException();
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
        MutableMap map = this;
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
        MutableMap map = this;
        for (K key : keys)
        {
            map = map.withoutKey(key);
        }
        return map;
    }

    @Override
    public abstract FixedSizeMap<K, V> filter(Predicate2<? super K, ? super V> predicate);

    @Override
    public abstract <R> FixedSizeMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function);

    @Override
    public abstract <K2, V2> FixedSizeMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function);

    @Override
    public abstract FixedSizeMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate);
}
