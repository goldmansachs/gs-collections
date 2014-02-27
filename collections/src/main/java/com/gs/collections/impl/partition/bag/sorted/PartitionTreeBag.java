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

import java.util.Comparator;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;

/**
 * @since 4.2
 */
public class PartitionTreeBag<T> implements PartitionMutableSortedBag<T>
{
    private final MutableSortedBag<T> selected;
    private final MutableSortedBag<T> rejected;

    public PartitionTreeBag(Comparator<? super T> comparator)
    {
        this.selected = TreeBag.newBag(comparator);
        this.rejected = TreeBag.newBag(comparator);
    }

    public MutableSortedBag<T> getSelected()
    {
        return this.selected;
    }

    public MutableSortedBag<T> getRejected()
    {
        return this.rejected;
    }

    public PartitionImmutableSortedBag<T> toImmutable()
    {
        return new PartitionImmutableSortedBagImpl<T>(this);
    }
}
