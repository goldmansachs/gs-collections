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

import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.partition.PartitionMutableCollection;

public class PartitionPredicate2Procedure<T, P> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Predicate2<? super T, ? super P> predicate;
    private final P parameter;
    private final PartitionMutableCollection<T> partitionMutableCollection;

    public PartitionPredicate2Procedure(Predicate2<? super T, ? super P> predicate, P parameter, PartitionMutableCollection<T> partitionMutableCollection)
    {
        this.predicate = predicate;
        this.parameter = parameter;
        this.partitionMutableCollection = partitionMutableCollection;
    }

    public void value(T each)
    {
        MutableCollection<T> bucket = this.predicate.accept(each, this.parameter)
                ? this.partitionMutableCollection.getSelected()
                : this.partitionMutableCollection.getRejected();
        bucket.add(each);
    }
}
