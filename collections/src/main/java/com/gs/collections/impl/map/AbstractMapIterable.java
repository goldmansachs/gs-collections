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

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.impl.AbstractRichIterable;

public abstract class AbstractMapIterable<K, V> extends AbstractRichIterable<V> implements MapIterable<K, V>
{
    protected int keyAndValueHashCode(K key, V value)
    {
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    protected boolean keyAndValueEquals(K key, V value, Map<K, V> map)
    {
        if (value == null && !map.containsKey(key))
        {
            return false;
        }

        V oValue = map.get(key);
        return oValue == value || oValue != null && oValue.equals(value);
    }

    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        V result = this.get(key);
        return this.isAbsent(result, key) ? null : function.valueOf(result);
    }

    protected boolean isAbsent(V result, K key)
    {
        return result == null && !this.containsKey(key);
    }

    public V getIfAbsent(K key, Function0<? extends V> function)
    {
        V result = this.get(key);
        if (!this.isAbsent(result, key))
        {
            return result;
        }
        return function.value();
    }

    public V getIfAbsentValue(K key, V value)
    {
        V result = this.get(key);
        if (!this.isAbsent(result, key))
        {
            return result;
        }
        return value;
    }

    public <P> V getIfAbsentWith(
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.valueOf(parameter);
        }
        return result;
    }

    @Override
    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().anySatisfy(predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().anySatisfyWith(predicate, parameter);
    }

    @Override
    public boolean allSatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().allSatisfy(predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().allSatisfyWith(predicate, parameter);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super V> predicate)
    {
        return this.valuesView().noneSatisfy(predicate);
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().noneSatisfyWith(predicate, parameter);
    }

    @Override
    public LazyIterable<V> asLazy()
    {
        return this.valuesView().asLazy();
    }

    public RichIterable<RichIterable<V>> chunk(int size)
    {
        return this.valuesView().chunk(size);
    }

    public void each(Procedure<? super V> procedure)
    {
        this.forEachValue(procedure);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure2, P parameter)
    {
        this.valuesView().forEachWith(procedure2, parameter);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.valuesView().forEachWithIndex(objectIntProcedure);
    }

    public void forEachKey(Procedure<? super K> procedure)
    {
        this.keysView().forEach(procedure);
    }

    public void forEachValue(Procedure<? super V> procedure)
    {
        this.valuesView().forEach(procedure);
    }

    @Override
    public boolean contains(Object object)
    {
        return this.containsValue(object);
    }

    @Override
    public V detect(Predicate<? super V> predicate)
    {
        return this.valuesView().detect(predicate);
    }

    @Override
    public <P> V detectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.valuesView().detectWith(predicate, parameter);
    }

    @Override
    public V detectIfNone(Predicate<? super V> predicate, Function0<? extends V> function)
    {
        return this.valuesView().detectIfNone(predicate, function);
    }

    @Override
    public <P> V detectWithIfNone(Predicate2<? super V, ? super P> predicate, P parameter, Function0<? extends V> function)
    {
        return this.valuesView().detectWithIfNone(predicate, parameter, function);
    }

    public V getFirst()
    {
        return this.valuesView().getFirst();
    }

    public V getLast()
    {
        return this.valuesView().getLast();
    }

    @Override
    public Object[] toArray()
    {
        return this.valuesView().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return this.valuesView().toArray(a);
    }
}
