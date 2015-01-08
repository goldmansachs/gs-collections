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

package com.gs.collections.impl.multimap.set;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;

public abstract class AbstractMutableSetMultimap<K, V> extends AbstractMutableMultimap<K, V, MutableSet<V>> implements MutableSetMultimap<K, V>
{
    protected AbstractMutableSetMultimap()
    {
    }

    protected AbstractMutableSetMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    protected AbstractMutableSetMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    protected AbstractMutableSetMultimap(int size)
    {
        super(size);
    }

    public MutableSetMultimap<K, V> toMutable()
    {
        return new UnifiedSetMultimap<K, V>(this);
    }

    public ImmutableSetMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableSet<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableSet<V>>()
        {
            public void value(K key, MutableSet<V> set)
            {
                map.put(key, set.toImmutable());
            }
        });

        return new ImmutableSetMultimapImpl<K, V>(map);
    }

    public <K2, V2> MutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.<K2, V2>newMultimap());
    }

    public <V2> MutableBagMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, HashBagMultimap.<K, V2>newMultimap());
    }
}
