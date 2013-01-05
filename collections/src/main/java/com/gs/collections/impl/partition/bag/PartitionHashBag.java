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

package com.gs.collections.impl.partition.bag;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.impl.bag.mutable.HashBag;

public class PartitionHashBag<T> implements PartitionMutableBag<T>
{
    private final MutableBag<T> selected = HashBag.newBag();
    private final MutableBag<T> rejected = HashBag.newBag();

    public MutableBag<T> getSelected()
    {
        return this.selected;
    }

    public MutableBag<T> getRejected()
    {
        return this.rejected;
    }

    public PartitionImmutableBag<T> toImmutable()
    {
        return new PartitionImmutableBagImpl<T>(this);
    }
}
