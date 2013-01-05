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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.partition.PartitionMutableCollection;

public class PartitionProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Predicate<? super T> predicate;
    private final PartitionMutableCollection<T> partitionMutableCollection;

    public PartitionProcedure(Predicate<? super T> predicate, PartitionMutableCollection<T> partitionMutableCollection)
    {
        this.predicate = predicate;
        this.partitionMutableCollection = partitionMutableCollection;
    }

    public void value(T each)
    {
        MutableCollection<T> bucket = this.predicate.accept(each)
                ? this.partitionMutableCollection.getSelected()
                : this.partitionMutableCollection.getRejected();
        bucket.add(each);
    }
}
