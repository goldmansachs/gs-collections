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

package ponzu.impl.multimap.set;

import java.io.Externalizable;
import java.util.concurrent.atomic.AtomicInteger;

import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.map.MutableMap;
import ponzu.api.multimap.Multimap;
import ponzu.api.multimap.set.ImmutableSetMultimap;
import ponzu.api.multimap.set.MutableSetMultimap;
import ponzu.api.set.ImmutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.map.mutable.ConcurrentMutableHashMap;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.multimap.AbstractSynchronizedPutMultimap;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.utility.ArrayIterate;

/**
 * A Multimap that is optimized for parallel writes, but is not protected for concurrent reads.
 */
public final class SynchronizedPutUnifiedSetMultimap<K, V>
        extends AbstractSynchronizedPutMultimap<K, V, MutableSet<V>> implements MutableSetMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 42L;
    private final AtomicInteger atomicTotalSize = new AtomicInteger(0);

    public SynchronizedPutUnifiedSetMultimap()
    {
    }

    public SynchronizedPutUnifiedSetMultimap(int initialCapacity, float loadFactor, int concurrencyLevel)
    {
        super(ConcurrentMutableHashMap.<K, MutableSet<V>>newMap(initialCapacity, loadFactor, concurrencyLevel));
    }

    public SynchronizedPutUnifiedSetMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        this.putAll(multimap);
    }

    public SynchronizedPutUnifiedSetMultimap(Pair<K, V>... pairs)
    {
        this();
        ArrayIterate.forEach(pairs, new Procedure<Pair<K, V>>()
        {
            public void value(Pair<K, V> pair)
            {
                put(pair.getOne(), pair.getTwo());
            }
        });
    }

    @Override
    protected MutableSet<V> createCollection()
    {
        return UnifiedSet.newSet(1);
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap()
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>();
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap(int initiaCapacity, float loadFactor, int concurrencyLevel)
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(initiaCapacity, loadFactor, concurrencyLevel);
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(multimap);
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(pairs);
    }

    public SynchronizedPutUnifiedSetMultimap<K, V> newEmpty()
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>();
    }

    public MutableSetMultimap<K, V> toMutable()
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(this);
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
}
