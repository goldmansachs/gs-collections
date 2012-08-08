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

package com.webguys.ponzu.api.partition.bag;

import com.webguys.ponzu.api.bag.Bag;
import com.webguys.ponzu.api.partition.PartitionIterable;

public interface PartitionBag<T> extends PartitionIterable<T>
{
    Bag<T> getSelected();

    Bag<T> getRejected();
}
