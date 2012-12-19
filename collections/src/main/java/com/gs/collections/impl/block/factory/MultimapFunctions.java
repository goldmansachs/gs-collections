/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.multimap.Multimap;

public final class MultimapFunctions
{
    private MultimapFunctions()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * @return Function delegating to {@link Multimap#get(Object)}
     */
    public static <K, V> Function<K, RichIterable<V>> get(Multimap<K, V> multimap)
    {
        return new MultimapGetFunction<K, V>(multimap);
    }

    private static final class MultimapGetFunction<K, V> implements Function<K, RichIterable<V>>
    {
        private static final long serialVersionUID = 1L;

        private final Multimap<K, V> multimap;

        private MultimapGetFunction(Multimap<K, V> multimap)
        {
            this.multimap = multimap;
        }

        public RichIterable<V> valueOf(K subject)
        {
            return this.multimap.get(subject);
        }
    }
}
