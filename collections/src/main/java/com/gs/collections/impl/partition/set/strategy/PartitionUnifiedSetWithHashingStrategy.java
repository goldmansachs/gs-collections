/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.partition.set.strategy;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.partition.set.PartitionImmutableSetImpl;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;

public class PartitionUnifiedSetWithHashingStrategy<T>
        implements PartitionMutableSet<T>
{
    private final MutableSet<T> selected;
    private final MutableSet<T> rejected;

    public PartitionUnifiedSetWithHashingStrategy(HashingStrategy<? super T> hashingStrategy)
    {
        this.selected = UnifiedSetWithHashingStrategy.newSet(hashingStrategy);
        this.rejected = UnifiedSetWithHashingStrategy.newSet(hashingStrategy);
    }

    public MutableSet<T> getSelected()
    {
        return this.selected;
    }

    public MutableSet<T> getRejected()
    {
        return this.rejected;
    }

    public PartitionImmutableSet<T> toImmutable()
    {
        return new PartitionImmutableSetImpl<T>(this);
    }
}
