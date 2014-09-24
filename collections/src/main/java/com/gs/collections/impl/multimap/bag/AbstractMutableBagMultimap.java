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

package com.gs.collections.impl.multimap.bag;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;

public abstract class AbstractMutableBagMultimap<K, V> extends AbstractMutableMultimap<K, V, MutableBag<V>> implements MutableBagMultimap<K, V>
{
    protected AbstractMutableBagMultimap()
    {
    }

    protected AbstractMutableBagMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    protected AbstractMutableBagMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    protected AbstractMutableBagMultimap(int size)
    {
        super(size);
    }

    public MutableBagMultimap<K, V> toMutable()
    {
        return new HashBagMultimap<K, V>(this);
    }

    public ImmutableBagMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableBag<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableBag<V>>()
        {
            public void value(K key, MutableBag<V> bag)
            {
                map.put(key, bag.toImmutable());
            }
        });

        return new ImmutableBagMultimapImpl<K, V>(map);
    }

    public <K2, V2> HashBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.<K2, V2>newMultimap());
    }

    public <V2> HashBagMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, HashBagMultimap.<K, V2>newMultimap());
    }
}
