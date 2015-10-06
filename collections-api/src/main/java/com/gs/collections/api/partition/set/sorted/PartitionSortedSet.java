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

package com.gs.collections.api.partition.set.sorted;

import com.gs.collections.api.partition.ordered.PartitionReversibleIterable;
import com.gs.collections.api.partition.ordered.PartitionSortedIterable;
import com.gs.collections.api.partition.set.PartitionSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;

/**
 * A PartitionSortedSet is the result of splitting a SortedSetIterable into two SortedSetIterables based on a Predicate.
 * The results that answer true for the Predicate will be returned from the getSelected() method and the results that answer
 * false for the predicate will be returned from the getRejected() method.
 */
public interface PartitionSortedSet<T> extends PartitionSet<T>, PartitionSortedIterable<T>, PartitionReversibleIterable<T>
{
    SortedSetIterable<T> getSelected();

    SortedSetIterable<T> getRejected();
}
