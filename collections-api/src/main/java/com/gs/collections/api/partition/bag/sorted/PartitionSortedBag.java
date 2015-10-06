/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.api.partition.bag.sorted;

import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.partition.bag.PartitionBag;
import com.gs.collections.api.partition.ordered.PartitionReversibleIterable;
import com.gs.collections.api.partition.ordered.PartitionSortedIterable;

/**
 * @since 4.2
 */
public interface PartitionSortedBag<T> extends PartitionBag<T>, PartitionSortedIterable<T>, PartitionReversibleIterable<T>
{
    SortedBag<T> getSelected();

    SortedBag<T> getRejected();
}
