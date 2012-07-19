/*
 * Copyright 2012 Goldman Sachs.
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

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.stack.PartitionImmutableStack;
import com.gs.collections.api.partition.stack.PartitionMutableStack;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.stack.mutable.ArrayStack;

public class PartitionArrayStack<T> implements PartitionMutableStack<T>
{
    private final MutableList<T> selected = FastList.newList();
    private final MutableList<T> rejected = FastList.newList();
    private final Predicate<? super T> predicate;

    public PartitionArrayStack(Predicate<? super T> predicate)
    {
        this.predicate = predicate;
    }

    public static <V> PartitionMutableStack<V> of(RichIterable<V> iterable, Predicate<? super V> predicate)
    {
        return PartitionArrayStack.partition(iterable, new PartitionArrayStack<V>(predicate));
    }

    protected static <T, P extends PartitionMutableStack<T>> P partition(
            RichIterable<T> iterable,
            P partitionMutableStack)
    {
        iterable.forEach(new PartitionAddProcedure<T>(partitionMutableStack));
        return partitionMutableStack;
    }

    public MutableStack<T> getSelected()
    {
        return ArrayStack.newStackFromTopToBottom(this.selected);
    }

    public MutableStack<T> getRejected()
    {
        return ArrayStack.newStackFromTopToBottom(this.rejected);
    }

    public PartitionImmutableStack<T> toImmutable()
    {
        return new PartitionImmutableStackImpl<T>(this);
    }

    public void add(T t)
    {
        (this.predicate.accept(t) ? this.selected : this.rejected).add(t);
    }

    private static final class PartitionAddProcedure<T> implements Procedure<T>
    {
        private static final long serialVersionUID = 1L;

        private final PartitionMutableStack<T> partitionMutableStack;

        private PartitionAddProcedure(PartitionMutableStack<T> partitionMutableStack)
        {
            this.partitionMutableStack = partitionMutableStack;
        }

        public void value(T object)
        {
            this.partitionMutableStack.add(object);
        }
    }
}
