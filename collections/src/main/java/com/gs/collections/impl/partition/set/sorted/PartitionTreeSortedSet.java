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

package com.gs.collections.impl.partition.set.sorted;

import java.util.Comparator;

import com.gs.collections.api.partition.set.sorted.PartitionImmutableSortedSet;
import com.gs.collections.api.partition.set.sorted.PartitionMutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;

public class PartitionTreeSortedSet<T> implements PartitionMutableSortedSet<T>
{
    private final MutableSortedSet<T> selected;
    private final MutableSortedSet<T> rejected;

    public PartitionTreeSortedSet(Comparator<? super T> comparator)
    {
        this.selected = TreeSortedSet.newSet(comparator);
        this.rejected = TreeSortedSet.newSet(comparator);
    }

    public MutableSortedSet<T> getSelected()
    {
        return this.selected;
    }

    public MutableSortedSet<T> getRejected()
    {
        return this.rejected;
    }

    public PartitionImmutableSortedSet<T> toImmutable()
    {
        return new PartitionImmutableSortedSetImpl<T>(this);
    }
}
