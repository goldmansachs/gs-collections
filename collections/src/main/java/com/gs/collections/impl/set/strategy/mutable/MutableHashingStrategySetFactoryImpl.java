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

package com.gs.collections.impl.set.strategy.mutable;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.factory.set.strategy.MutableHashingStrategySetFactory;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

@Immutable
public final class MutableHashingStrategySetFactoryImpl implements MutableHashingStrategySetFactory
{
    public <T> MutableSet<T> of(HashingStrategy<? super T> hashingStrategy)
    {
        return this.with(hashingStrategy);
    }

    public <T> MutableSet<T> with(HashingStrategy<? super T> hashingStrategy)
    {
        return UnifiedSetWithHashingStrategy.newSetWith(hashingStrategy);
    }

    public <T> MutableSet<T> of(HashingStrategy<? super T> hashingStrategy, T... items)
    {
        return this.with(hashingStrategy, items);
    }

    public <T> MutableSet<T> with(HashingStrategy<? super T> hashingStrategy, T... items)
    {
        return UnifiedSetWithHashingStrategy.newSetWith(hashingStrategy, items);
    }

    public <T> MutableSet<T> ofAll(HashingStrategy<? super T> hashingStrategy, Iterable<? extends T> items)
    {
        return this.withAll(hashingStrategy, items);
    }

    public <T> MutableSet<T> withAll(HashingStrategy<? super T> hashingStrategy, Iterable<? extends T> items)
    {
        if (Iterate.isEmpty(items))
        {
            return this.with(hashingStrategy);
        }
        return UnifiedSetWithHashingStrategy.newSet(hashingStrategy, items);
    }
}
