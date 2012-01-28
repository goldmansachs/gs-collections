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

package ponzu.impl.map.strategy.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.Immutable;
import ponzu.api.RichIterable;
import ponzu.api.block.HashingStrategy;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Generator;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.map.ImmutableMap;
import ponzu.api.tuple.Pair;
import ponzu.impl.factory.Lists;
import ponzu.impl.factory.Maps;
import ponzu.impl.factory.Sets;
import ponzu.impl.map.immutable.AbstractImmutableMap;
import ponzu.impl.utility.LazyIterate;

/**
 * This is a zero element {@link ImmutableUnifiedMapWithHashingStrategy} which is created by calling
 * the HashingStrategyMaps.immutable.of() method.
 */
@Immutable
final class ImmutableEmptyMapWithHashingStrategy<K, V>
        extends AbstractImmutableMap<K, V>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final HashingStrategy<? super K> hashingStrategy;

    ImmutableEmptyMapWithHashingStrategy(HashingStrategy<? super K> hashingStrategy)
    {
        this.hashingStrategy = hashingStrategy;
    }

    public int size()
    {
        return 0;
    }

    public RichIterable<K> keysView()
    {
        return LazyIterate.empty();
    }

    public RichIterable<V> valuesView()
    {
        return LazyIterate.empty();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return LazyIterate.empty();
    }

    public boolean containsKey(Object key)
    {
        return false;
    }

    public boolean containsValue(Object value)
    {
        return false;
    }

    public V get(Object key)
    {
        return null;
    }

    public Set<K> keySet()
    {
        return Sets.immutable.<K>of().castToSet();
    }

    public Collection<V> values()
    {
        return Lists.immutable.<V>of().castToList();
    }

    @Override
    public String toString()
    {
        return "{}";
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }

        return ((Map<K, V>) other).isEmpty();
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public boolean notEmpty()
    {
        return false;
    }

    @Override
    public void forEach(Procedure<? super V> procedure)
    {
    }

    @Override
    public <A> A ifPresentApply(K key, Function<? super V, ? extends A> function)
    {
        return null;
    }

    @Override
    public V getIfAbsent(K key, Generator<? extends V> function)
    {
        return function.value();
    }

    @Override
    public <P> V getIfAbsentWith(
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        return function.valueOf(parameter);
    }

    @Override
    public <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return Maps.immutable.of();
    }

    @Override
    public <R> ImmutableMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return new ImmutableEmptyMapWithHashingStrategy<K, R>(this.hashingStrategy);
    }

    @Override
    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        return null;
    }

    @Override
    public ImmutableMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        return this;
    }

    @Override
    public ImmutableMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        return this;
    }
}
