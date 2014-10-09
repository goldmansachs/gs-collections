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

package com.gs.collections.impl.bimap.immutable;

import java.io.Serializable;
import java.util.Map;

import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.factory.Maps;
import net.jcip.annotations.Immutable;

@Immutable
final class ImmutableHashBiMap<K, V> extends AbstractImmutableBiMap<K, V> implements Serializable
{
    private static final long serialVersionUID = 1L;

    ImmutableHashBiMap()
    {
        super(Maps.immutable.<K, V>of());
    }

    ImmutableHashBiMap(Map<K, V> map)
    {
        super(Maps.immutable.ofAll(map));
    }

    ImmutableHashBiMap(MutableBiMap<K, V> biMap)
    {
        super(biMap);
    }

    ImmutableHashBiMap(ImmutableMap<K, V> immutableMap)
    {
        super(immutableMap);
    }

    private Object writeReplace()
    {
        return new ImmutableBiMapSerializationProxy<K, V>(this);
    }
}
