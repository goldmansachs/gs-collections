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

package ponzu.impl.multimap.set.strategy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import ponzu.api.block.HashingStrategy;
import ponzu.api.block.function.Function2;
import ponzu.api.map.MutableMap;
import ponzu.api.multimap.Multimap;
import ponzu.api.multimap.set.ImmutableSetMultimap;
import ponzu.api.multimap.set.MutableSetMultimap;
import ponzu.api.set.ImmutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.multimap.AbstractMutableMultimap;
import ponzu.impl.multimap.set.ImmutableSetMultimapImpl;
import ponzu.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;

public final class UnifiedSetWithHashingStrategyMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableSet<V>>
        implements MutableSetMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;
    private HashingStrategy<? super V> hashingStrategy;

    /**
     * @deprecated Empty default constructor used for serialization. Instantiating an UnifiedSetWithHashingStrategyMultimap with
     *             this constructor will have a null hashingStrategy and throw NullPointerException when used.
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

    public MutableSetMultimap<K, V> toMutable()
    {
        return new UnifiedSetWithHashingStrategyMultimap<K, V>(this);
    }

    public ImmutableSetMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableSet<V>> map = UnifiedMap.newMap();

        this.map.transformValues(new Function2<K, MutableSet<V>, Object>()
        {
            public Object value(K key, MutableSet<V> set)
            {
                return map.put(key, set.toImmutable());
            }
        });
        return new ImmutableSetMultimapImpl<K, V>(map);
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
}
