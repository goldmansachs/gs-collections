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

package com.gs.collections.impl.partition.bag;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import net.jcip.annotations.Immutable;

@Immutable
public class PartitionImmutableBagImpl<T> implements PartitionImmutableBag<T>
{
    private final ImmutableBag<T> selected;
    private final ImmutableBag<T> rejected;

    public PartitionImmutableBagImpl(PartitionHashBag<T> partitionHashBag)
    {
        this.selected = partitionHashBag.getSelected().toImmutable();
        this.rejected = partitionHashBag.getRejected().toImmutable();
    }

    public ImmutableBag<T> getSelected()
    {
        return this.selected;
    }

    public ImmutableBag<T> getRejected()
    {
        return this.rejected;
    }
}
