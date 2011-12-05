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

package com.gs.collections.api.set;

public interface Pool<V>
{
    /**
     * Locates an object in the pool which is equal to {@code key}.
     *
     * @param key the value to look for
     * @return The object reference in the pool equal to key or null.
     */
    V get(V key);

    void clear();

    /**
     * Puts {@code key} into the pool. If there is no existing object that is equal
     * to key, key will be added to the pool and the return value will be the same instance.
     * If there is an existing object in the pool that is equal to {@code key}, the pool will remain unchanged
     * and the pooled instance will be is returned.
     *
     * @param key the value to add if not in the pool
     * @return the object reference in the pool equal to key (either key itself or the existing reference)
     */
    V put(V key);

    int size();

    /**
     * Locates an object in the pool which is equal to {@code key} and removes it.
     *
     * @param key object to remove
     * @return The object reference in the pool equal to key or null.
     */
    V removeFromPool(V key);
}
