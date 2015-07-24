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

package com.gs.collections.impl.set.mutable;

import com.gs.collections.api.factory.set.MutableSetFactory;
import com.gs.collections.api.set.MutableSet;
import net.jcip.annotations.Immutable;

@Immutable
public final class MutableSetFactoryImpl implements MutableSetFactory
{
    public <T> MutableSet<T> empty()
    {
        return UnifiedSet.newSet();
    }

    public <T> MutableSet<T> of()
    {
        return this.empty();
    }

    public <T> MutableSet<T> with()
    {
        return this.empty();
    }

    public <T> MutableSet<T> ofInitialCapacity(int capacity)
    {
        return this.withInitialCapacity(capacity);
    }

    public <T> MutableSet<T> withInitialCapacity(int capacity)
    {
        return UnifiedSet.newSet(capacity);
    }

    public <T> MutableSet<T> of(T... items)
    {
        return this.with(items);
    }

    public <T> MutableSet<T> with(T... items)
    {
        return UnifiedSet.newSetWith(items);
    }

    public <T> MutableSet<T> ofAll(Iterable<? extends T> items)
    {
        return this.withAll(items);
    }

    public <T> MutableSet<T> withAll(Iterable<? extends T> items)
    {
        return UnifiedSet.newSet(items);
    }
}
