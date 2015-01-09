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

package com.gs.collections.impl.map.fixed;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.FixedSizeMap;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.tuple.Tuples;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
final class TripletonMap<K, V>
        extends AbstractMemoryEfficientMutableMap<K, V>
        implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private K key1;
    private V value1;
    private K key2;
    private V value2;
    private K key3;
    private V value3;

    @SuppressWarnings("UnusedDeclaration")
    public TripletonMap()
    {
        // For Externalizable use only
    }

    TripletonMap(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        this.key1 = key1;
        this.value1 = value1;
        this.key2 = key2;
        this.value2 = value2;
        this.key3 = key3;
        this.value3 = value3;
    }

    K getKey1()
    {
        return this.key1;
    }

    K getKey2()
    {
        return this.key2;
    }

    K getKey3()
    {
        return this.key3;
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
        if (Comparators.nullSafeEquals(this.key2, addKey))
        {
            this.value2 = addValue;
            return this;
        }
        if (Comparators.nullSafeEquals(this.key3, addKey))
        {
            this.value3 = addValue;
            return this;
        }
        return UnifiedMap.newWithKeysValues(
                this.key1,
                this.value1,
                this.key2,
                this.value2,
                this.key3,
                this.value3,
                addKey,
                addValue);
    }

    @Override
    public MutableMap<K, V> withoutKey(K key)
    {
        if (Comparators.nullSafeEquals(key, this.key1))
        {
            return new DoubletonMap<K, V>(this.key2, this.value2, this.key3, this.value3);
        }
        if (Comparators.nullSafeEquals(key, this.key2))
        {
            return new DoubletonMap<K, V>(this.key1, this.value1, this.key3, this.value3);
        }
        if (Comparators.nullSafeEquals(key, this.key3))
        {
            return new DoubletonMap<K, V>(this.key1, this.value1, this.key2, this.value2);
        }
        return this;
    }

    // Weird implementation of clone() is ok on final classes
    @Override
    public TripletonMap<K, V> clone()
    {
        return new TripletonMap<K, V>(this.key1, this.value1, this.key2, this.value2, this.key3, this.value3);
    }

    @Override
    public ImmutableMap<K, V> toImmutable()
    {
        return Maps.immutable.with(this.key1, this.value1, this.key2, this.value2, this.key3, this.value3);
    }

    public int size()
    {
        return 3;
    }

    public boolean containsKey(Object key)
    {
        return Comparators.nullSafeEquals(this.key3, key)
                || Comparators.nullSafeEquals(this.key2, key)
                || Comparators.nullSafeEquals(this.key1, key);
    }

    public boolean containsValue(Object value)
    {
        return Comparators.nullSafeEquals(this.value3, value)
                || Comparators.nullSafeEquals(this.value2, value)
                || Comparators.nullSafeEquals(this.value1, value);
    }

    public V get(Object key)
    {
        if (Comparators.nullSafeEquals(this.key3, key))
        {
            return this.value3;
        }
        if (Comparators.nullSafeEquals(this.key2, key))
        {
            return this.value2;
        }
        if (Comparators.nullSafeEquals(this.key1, key))
        {
            return this.value1;
        }
        return null;
    }

    public Set<K> keySet()
    {
        return Sets.fixedSize.of(this.key1, this.key2, this.key3);
    }

    public Collection<V> values()
    {
        return Lists.fixedSize.of(this.value1, this.value2, this.value3);
    }

    public MutableSet<Entry<K, V>> entrySet()
    {
        return Sets.fixedSize.<Map.Entry<K, V>>of(
                new ImmutableEntry<K, V>(this.key1, this.value1),
                new ImmutableEntry<K, V>(this.key2, this.value2),
                new ImmutableEntry<K, V>(this.key3, this.value3));
    }

    @Override
    public int hashCode()
    {
        return this.keyAndValueHashCode(this.key1, this.value1)
                + this.keyAndValueHashCode(this.key2, this.value2)
                + this.keyAndValueHashCode(this.key3, this.value3);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }
        Map<K, V> that = (Map<K, V>) other;
        return that.size() == this.size()
                && this.keyAndValueEquals(this.key1, this.value1, that)
                && this.keyAndValueEquals(this.key2, this.value2, that)
                && this.keyAndValueEquals(this.key3, this.value3, that);
    }

    @Override
    public String toString()
    {
        return "{"
                + this.key1 + '=' + this.value1 + ", "
                + this.key2 + '=' + this.value2 + ", "
                + this.key3 + '=' + this.value3 + '}';
    }

    @Override
    public MutableMap<V, K> flipUniqueValues()
    {
        if (Comparators.nullSafeEquals(this.value1, this.value2))
        {
            throw new IllegalStateException("Duplicate value: " + this.value1 + " found at key: " + this.key1 + " and key: " + this.key2);
        }
        if (Comparators.nullSafeEquals(this.value2, this.value3))
        {
            throw new IllegalStateException("Duplicate value: " + this.value2 + " found at key: " + this.key2 + " and key: " + this.key3);
        }
        if (Comparators.nullSafeEquals(this.value3, this.value1))
        {
            throw new IllegalStateException("Duplicate value: " + this.value3 + " found at key: " + this.key3 + " and key: " + this.key3);
        }
        return new TripletonMap<V, K>(this.value1, this.key1, this.value2, this.key2, this.value3, this.key3);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        procedure.value(this.key1, this.value1);
        procedure.value(this.key2, this.value2);
        procedure.value(this.key3, this.value3);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        procedure.value(this.key1);
        procedure.value(this.key2);
        procedure.value(this.key3);
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        procedure.value(this.value1);
        procedure.value(this.value2);
        procedure.value(this.value3);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        objectIntProcedure.value(this.value1, 0);
        objectIntProcedure.value(this.value2, 1);
        objectIntProcedure.value(this.value3, 2);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        procedure.value(this.value1, parameter);
        procedure.value(this.value2, parameter);
        procedure.value(this.value3, parameter);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.key1);
        out.writeObject(this.value1);
        out.writeObject(this.key2);
        out.writeObject(this.value2);
        out.writeObject(this.key3);
        out.writeObject(this.value3);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.key1 = (K) in.readObject();
        this.value1 = (V) in.readObject();
        this.key2 = (K) in.readObject();
        this.value2 = (V) in.readObject();
        this.key3 = (K) in.readObject();
        this.value3 = (V) in.readObject();
    }

    @Override
    public FixedSizeMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        return this.filter(predicate);
    }

    @Override
    public <R> FixedSizeMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return Maps.fixedSize.of(this.key1, function.value(this.key1, this.value1), this.key2, function.value(this.key2, this.value2), this.key3, function.value(this.key3, this.value3));
    }

    @Override
    public <K2, V2> FixedSizeMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        Pair<K2, V2> pair1 = function.value(this.key1, this.value1);
        Pair<K2, V2> pair2 = function.value(this.key2, this.value2);
        Pair<K2, V2> pair3 = function.value(this.key3, this.value3);
        return Maps.fixedSize.of(pair1.getOne(), pair1.getTwo(), pair2.getOne(), pair2.getTwo(), pair3.getOne(), pair3.getTwo());
    }

    @Override
    public FixedSizeMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        return this.filter(Predicates2.not(predicate));
    }

    @Override
    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Tuples.pair(this.key1, this.value1);
        }
        if (predicate.accept(this.key2, this.value2))
        {
            return Tuples.pair(this.key2, this.value2);
        }
        if (predicate.accept(this.key3, this.value3))
        {
            return Tuples.pair(this.key3, this.value3);
        }
        return null;
    }

    private FixedSizeMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        int result = 0;

        if (predicate.accept(this.key1, this.value1))
        {
            result |= 1;
        }
        if (predicate.accept(this.key2, this.value2))
        {
            result |= 2;
        }
        if (predicate.accept(this.key3, this.value3))
        {
            result |= 4;
        }

        switch (result)
        {
            case 1:
                return Maps.fixedSize.of(this.key1, this.value1);
            case 2:
                return Maps.fixedSize.of(this.key2, this.value2);
            case 3:
                return Maps.fixedSize.of(this.key1, this.value1, this.key2, this.value2);
            case 4:
                return Maps.fixedSize.of(this.key3, this.value3);
            case 5:
                return Maps.fixedSize.of(this.key1, this.value1, this.key3, this.value3);
            case 6:
                return Maps.fixedSize.of(this.key2, this.value2, this.key3, this.value3);
            case 7:
                return Maps.fixedSize.of(this.key1, this.value1, this.key2, this.value2, this.key3, this.value3);
            default:
                return Maps.fixedSize.of();
        }
    }
}
