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

package ponzu.impl.partition.set.sorted;

import java.util.Comparator;

import ponzu.api.block.predicate.Predicate;
import ponzu.api.partition.set.sorted.PartitionImmutableSortedSet;
import ponzu.api.partition.set.sorted.PartitionMutableSortedSet;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.api.set.sorted.SortedSetIterable;
import ponzu.impl.partition.AbstractPartitionMutableCollection;
import ponzu.impl.set.sorted.mutable.TreeSortedSet;

public class PartitionTreeSortedSet<T> extends AbstractPartitionMutableCollection<T> implements PartitionMutableSortedSet<T>
{
    private final MutableSortedSet<T> selected;
    private final MutableSortedSet<T> rejected;

    public PartitionTreeSortedSet(Comparator<? super T> comparator, Predicate<? super T> predicate)
    {
        super(predicate);
        this.selected = TreeSortedSet.newSet(comparator);
        this.rejected = TreeSortedSet.newSet(comparator);
    }

    public static <V> PartitionMutableSortedSet<V> of(SortedSetIterable<V> sortedSetIterable, Predicate<? super V> predicate)
    {
        return partition(sortedSetIterable, new PartitionTreeSortedSet<V>(sortedSetIterable.comparator(), predicate));
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
