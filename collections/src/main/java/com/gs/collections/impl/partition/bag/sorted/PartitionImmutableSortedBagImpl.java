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

package com.gs.collections.impl.partition.bag.sorted;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import com.gs.collections.api.partition.bag.sorted.PartitionSortedBag;
import net.jcip.annotations.Immutable;

/**
 * @since 4.2
 */
@Immutable
public class PartitionImmutableSortedBagImpl<T> implements PartitionImmutableSortedBag<T>
{
    private final ImmutableSortedBag<T> selected;
    private final ImmutableSortedBag<T> rejected;

    public PartitionImmutableSortedBagImpl(PartitionSortedBag<T> partitionImmutableSortedBag)
    {
        this.selected = partitionImmutableSortedBag.getSelected().toImmutable();
        this.rejected = partitionImmutableSortedBag.getRejected().toImmutable();
    }

    public ImmutableSortedBag<T> getSelected()
    {
        return this.selected;
    }

    public ImmutableSortedBag<T> getRejected()
    {
        return this.rejected;
    }
}
