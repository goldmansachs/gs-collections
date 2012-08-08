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

package com.gs.collections.api.map;

import java.util.Map;

/**
 * A FixedSizeMap is a map that may be mutated, but cannot grow or shrink in size.
 */
public interface FixedSizeMap<K, V>
        extends MutableMap<K, V>
{
    /**
     * @throws UnsupportedOperationException
     */
    void clear();

    /**
     * @throws UnsupportedOperationException
     */
    V put(K key, V value);

    /**
     * @throws UnsupportedOperationException
     */
    void putAll(Map<? extends K, ? extends V> map);

    /**
     * @throws UnsupportedOperationException
     */
    V remove(Object key);

    /**
     * @throws UnsupportedOperationException
     */
    V removeKey(K key);
}
