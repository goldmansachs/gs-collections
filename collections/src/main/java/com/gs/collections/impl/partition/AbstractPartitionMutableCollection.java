/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.partition;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.impl.block.procedure.PartitionAddProcedure;

public abstract class AbstractPartitionMutableCollection<T> implements PartitionMutableCollection<T>
{
    protected final Predicate<? super T> predicate;

    protected AbstractPartitionMutableCollection(Predicate<? super T> predicate)
    {
        this.predicate = predicate;
    }

    protected static <T, P extends PartitionMutableCollection<T>> P partition(
            RichIterable<T> iterable,
            P partitionMutableCollection)
    {
        iterable.forEach(new PartitionAddProcedure<T>(partitionMutableCollection));
        return partitionMutableCollection;
    }

    public void add(T t)
    {
        if (this.predicate.accept(t))
        {
            this.getSelected().add(t);
        }
        else
        {
            this.getRejected().add(t);
        }
    }
}
