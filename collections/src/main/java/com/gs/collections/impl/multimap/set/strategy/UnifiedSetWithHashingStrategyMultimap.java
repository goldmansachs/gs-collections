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

package com.gs.collections.impl.multimap.set.strategy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.set.AbstractMutableSetMultimap;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import com.gs.collections.impl.utility.Iterate;

public final class UnifiedSetWithHashingStrategyMultimap<K, V>
        extends AbstractMutableSetMultimap<K, V> implements Externalizable
{
    private static final long serialVersionUID = 1L;
    private HashingStrategy<? super V> hashingStrategy;

    /**
     * @deprecated Empty default constructor used for serialization. Instantiating an UnifiedSetWithHashingStrategyMultimap with
     * this constructor will have a null hashingStrategy and throw NullPointerException when used.
     */
    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    public UnifiedSetWithHashingStrategyMultimap()
    {
        // For Externalizable use only
    }

    public UnifiedSetWithHashingStrategyMultimap(HashingStrategy<? super V> hashingStrategy)
    {
        this.hashingStrategy = hashingStrategy;
    }

    public UnifiedSetWithHashingStrategyMultimap(UnifiedSetWithHashingStrategyMultimap<K, V> multimap)
    {
        this(multimap.hashingStrategy, multimap);
    }

    public UnifiedSetWithHashingStrategyMultimap(HashingStrategy<? super V> hashingStrategy, Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.sizeDistinct() * 2, 16));
        this.hashingStrategy = hashingStrategy;
        this.putAll(multimap);
    }

    public UnifiedSetWithHashingStrategyMultimap(HashingStrategy<? super V> hashingStrategy, Pair<K, V>... pairs)
    {
        this.hashingStrategy = hashingStrategy;
        this.putAllPairs(pairs);
    }

    public UnifiedSetWithHashingStrategyMultimap(HashingStrategy<? super V> hashingStrategy, Iterable<Pair<K, V>> inputIterable)
    {
        this.hashingStrategy = hashingStrategy;
        for (Pair<K, V> single : inputIterable)
        {
            this.add(single);
        }
    }

    public static <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimap(UnifiedSetWithHashingStrategyMultimap<K, V> multimap)
    {
        return new UnifiedSetWithHashingStrategyMultimap<K, V>(multimap);
    }

    public static <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimap(HashingStrategy<? super V> hashingStrategy,
            Multimap<? extends K, ? extends V> multimap)
    {
        return new UnifiedSetWithHashingStrategyMultimap<K, V>(hashingStrategy, multimap);
    }

    public static <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimap(HashingStrategy<? super V> hashingStrategy)
    {
        return new UnifiedSetWithHashingStrategyMultimap<K, V>(hashingStrategy);
    }

    public static <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimap(HashingStrategy<? super V> hashingStrategy, Pair<K, V>... pairs)
    {
        return new UnifiedSetWithHashingStrategyMultimap<K, V>(hashingStrategy, pairs);
    }

    public static <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimap(HashingStrategy<? super V> hashingStrategy, Iterable<Pair<K, V>> inputIterable)
    {
        return new UnifiedSetWithHashingStrategyMultimap<K, V>(hashingStrategy, inputIterable);
    }

    @Override
    protected MutableMap<K, MutableSet<V>> createMap()
    {
        return UnifiedMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableSet<V>> createMapWithKeyCount(int keyCount)
    {
        return UnifiedMap.newMap(keyCount);
    }

    @Override
    protected UnifiedSetWithHashingStrategy<V> createCollection()
    {
        return UnifiedSetWithHashingStrategy.newSet(this.hashingStrategy);
    }

    public UnifiedSetWithHashingStrategyMultimap<K, V> newEmpty()
    {
        return new UnifiedSetWithHashingStrategyMultimap<K, V>(this.hashingStrategy);
    }

    public HashingStrategy<? super V> getValueHashingStrategy()
    {
        return this.hashingStrategy;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.hashingStrategy);
        super.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.hashingStrategy = (HashingStrategy<? super V>) in.readObject();
        super.readExternal(in);
    }

    public MutableSetMultimap<V, K> flip()
    {
        return Iterate.flip(this);
    }

    public UnifiedSetWithHashingStrategyMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, this.newEmpty());
    }

    public UnifiedSetWithHashingStrategyMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, this.newEmpty());
    }

    public UnifiedSetWithHashingStrategyMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, this.newEmpty());
    }

    public UnifiedSetWithHashingStrategyMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, this.newEmpty());
    }
}
