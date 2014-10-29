/*
 * Copyright 2014 Goldman Sachs.
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

/**
 * This package contains interfaces for {@link com.gs.collections.api.partition.stack.PartitionStack}.
 * <p>
 *     A PartitionStack is the result of splitting a stack into two stacks based on a Predicate.
 * <p>
 * This package contains 3 interfaces:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.api.partition.stack.PartitionStack} - a read-only PartitionStack API.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.partition.stack.PartitionMutableStack} - a modifiable PartitionStack.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.partition.stack.PartitionImmutableStack} - the non-modifiable equivalent interface to {@link com.gs.collections.api.partition.stack.PartitionMutableStack}.
 *     </li>
 * </ul>
 */
package com.gs.collections.api.partition.stack;
