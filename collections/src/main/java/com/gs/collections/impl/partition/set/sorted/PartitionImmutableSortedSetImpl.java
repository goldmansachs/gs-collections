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

package com.gs.collections.impl.partition.set.sorted;

import com.gs.collections.api.partition.set.sorted.PartitionImmutableSortedSet;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import net.jcip.annotations.Immutable;

@Immutable
public class PartitionImmutableSortedSetImpl<T> implements PartitionImmutableSortedSet<T>
{
    private final ImmutableSortedSet<T> selected;
    private final ImmutableSortedSet<T> rejected;

    public PartitionImmutableSortedSetImpl(PartitionTreeSortedSet<T> partitionTreeSortedSet)
    {
        this.selected = partitionTreeSortedSet.getSelected().toImmutable();
        this.rejected = partitionTreeSortedSet.getRejected().toImmutable();
    }

    public ImmutableSortedSet<T> getSelected()
    {
        return this.selected;
    }

    public ImmutableSortedSet<T> getRejected()
    {
        return this.rejected;
    }
}
