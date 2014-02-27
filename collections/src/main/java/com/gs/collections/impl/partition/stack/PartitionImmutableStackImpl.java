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

package com.gs.collections.impl.partition.stack;

import com.gs.collections.api.partition.stack.PartitionImmutableStack;
import com.gs.collections.api.stack.ImmutableStack;
import net.jcip.annotations.Immutable;

@Immutable
public class PartitionImmutableStackImpl<T> implements PartitionImmutableStack<T>
{
    private final ImmutableStack<T> selected;
    private final ImmutableStack<T> rejected;

    public PartitionImmutableStackImpl(PartitionArrayStack<T> partitionArrayStack)
    {
        this.selected = partitionArrayStack.getSelected().toImmutable();
        this.rejected = partitionArrayStack.getRejected().toImmutable();
    }

    public ImmutableStack<T> getSelected()
    {
        return this.selected;
    }

    public ImmutableStack<T> getRejected()
    {
        return this.rejected;
    }
}
