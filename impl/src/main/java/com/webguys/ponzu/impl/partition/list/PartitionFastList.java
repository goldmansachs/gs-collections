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

package com.webguys.ponzu.impl.partition.list;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.partition.list.PartitionImmutableList;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.partition.AbstractPartitionMutableCollection;

public class PartitionFastList<T> extends AbstractPartitionMutableCollection<T> implements PartitionMutableList<T>
{
    private final MutableList<T> selected = FastList.newList();
    private final MutableList<T> rejected = FastList.newList();

    public PartitionFastList(Predicate<? super T> predicate)
    {
        super(predicate);
    }

    public static <V> PartitionMutableList<V> of(RichIterable<V> iterable, Predicate<? super V> predicate)
    {
        return partition(iterable, new PartitionFastList<V>(predicate));
    }

    public MutableList<T> getSelected()
    {
        return this.selected;
    }

    public MutableList<T> getRejected()
    {
        return this.rejected;
    }

    public PartitionImmutableList<T> toImmutable()
    {
        return new PartitionImmutableListImpl<T>(this);
    }
}
