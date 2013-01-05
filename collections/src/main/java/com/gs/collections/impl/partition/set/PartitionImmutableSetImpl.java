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

package com.gs.collections.impl.partition.set;

import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.set.ImmutableSet;
import net.jcip.annotations.Immutable;

@Immutable
public class PartitionImmutableSetImpl<T> implements PartitionImmutableSet<T>
{
    private final ImmutableSet<T> selected;
    private final ImmutableSet<T> rejected;

    public PartitionImmutableSetImpl(PartitionMutableSet<T> mutablePartition)
    {
        this.selected = mutablePartition.getSelected().toImmutable();
        this.rejected = mutablePartition.getRejected().toImmutable();
    }

    public ImmutableSet<T> getSelected()
    {
        return this.selected;
    }

    public ImmutableSet<T> getRejected()
    {
        return this.rejected;
    }
}
