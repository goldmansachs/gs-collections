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

package ponzu.impl.map.fixed;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import ponzu.api.block.function.Function2;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.map.FixedSizeMap;
import ponzu.api.map.ImmutableMap;
import ponzu.api.map.MutableMap;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.factory.Lists;
import ponzu.impl.factory.Maps;
import ponzu.impl.factory.Sets;
import ponzu.impl.tuple.ImmutableEntry;
import ponzu.impl.tuple.Tuples;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
final class SingletonMap<K, V>
        extends AbstractMemoryEfficientMutableMap<K, V>
        implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private K key1;
    private V value1;

    @SuppressWarnings("UnusedDeclaration")
    public SingletonMap()
    {
        // For Externalizable use only
    }

    SingletonMap(K key1, V value1)
    {
        this.key1 = key1;
        this.value1 = value1;
    }

    public int size()
    {
        return 1;
    }

    K getKey1()
    {
        return this.key1;
    }

    @Override
    public MutableMap<K, V> withKeyValue(K addKey, V addValue)
    {
        // Map behavior specifies that if you put in a duplicate key, you replace the value
        if (Comparators.nullSafeEquals(this.key1, addKey))
        {
            this.value1 = addValue;
            return this;
        }
        return new DoubletonMap<K, V>(this.key1, this.value1, addKey, addValue);
    }

    @Override
    public MutableMap<K, V> withoutKey(K key)
    {
        if (Comparators.nullSafeEquals(key, this.key1))
        {
            return new EmptyMap<K, V>();
        }
        return this;
    }

    // Weird implementation of clone() is ok on final classes
    @Override
    public SingletonMap<K, V> clone()
    {
        return new SingletonMap<K, V>(this.key1, this.value1);
    }

    @Override
    public ImmutableMap<K, V> toImmutable()
    {
        return Maps.immutable.of(this.key1, this.value1);
    }

    public boolean containsKey(Object key)
    {
        return Comparators.nullSafeEquals(this.key1, key);
    }

    public boolean containsValue(Object value)
    {
        return Comparators.nullSafeEquals(this.value1, value);
    }

    public V get(Object key)
    {
        if (Comparators.nullSafeEquals(this.key1, key))
        {
            return this.value1;
        }

        return null;
    }

    public Set<K> keySet()
    {
        return Sets.fixedSize.of(this.key1);
    }

    public Collection<V> values()
    {
        return Lists.fixedSize.of(this.value1);
    }

    public MutableSet<Entry<K, V>> entrySet()
    {
        return Sets.fixedSize.<Map.Entry<K, V>>of(new ImmutableEntry<K, V>(this.key1, this.value1));
    }

    @Override
    public String toString()
    {
        return '{' + String.valueOf(this.key1) + '=' + this.value1 + '}';
    }

    @Override
    public int hashCode()
    {
        return this.keyAndValueHashCode(this.key1, this.value1);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }
        Map<K, V> that = (Map<K, V>) other;
        return that.size() == this.size() && this.keyAndValueEquals(this.key1, this.value1, that);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        procedure.value(this.key1, this.value1);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        procedure.value(this.key1);
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        procedure.value(this.value1);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        objectIntProcedure.value(this.value1, 0);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        procedure.value(this.value1, parameter);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.key1);
        out.writeObject(this.value1);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.key1 = (K) in.readObject();
        this.value1 = (V) in.readObject();
    }

    @Override
    public FixedSizeMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return this.clone();
        }
        return Maps.fixedSize.of();
    }

    @Override
    public <R> FixedSizeMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return Maps.fixedSize.of(this.key1, function.value(this.key1, this.value1));
    }

    @Override
    public <K2, V2> FixedSizeMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        Pair<K2, V2> pair1 = function.value(this.key1, this.value1);
        return Maps.fixedSize.of(pair1.getOne(), pair1.getTwo());
    }

    @Override
    public FixedSizeMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Maps.fixedSize.of();
        }
        return this.clone();
    }

    @Override
    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Tuples.pair(this.key1, this.value1);
        }
        return null;
    }
}
