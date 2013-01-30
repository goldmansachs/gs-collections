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

package com.gs.collections.impl.lazy.primitive;

import java.util.Iterator;

import com.gs.collections.api.ByteIterable;
import com.gs.collections.api.block.function.primitive.ByteToObjectFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ByteProcedure;
import com.gs.collections.api.iterator.ByteIterator;
import com.gs.collections.impl.lazy.AbstractLazyIterable;

public class CollectByteToObjectIterable<V>
        extends AbstractLazyIterable<V>
{
    private final ByteIterable iterable;
    private final ByteToObjectFunction<? extends V> function;

    public CollectByteToObjectIterable(ByteIterable iterable, ByteToObjectFunction<? extends V> function)
    {
        this.iterable = iterable;
        this.function = function;
    }

    public void forEach(final Procedure<? super V> procedure)
    {
        this.iterable.forEach(new ByteProcedure()
        {
            public void value(byte each)
            {
                procedure.value(CollectByteToObjectIterable.this.function.valueOf(each));
            }
        });
    }

    public void forEachWithIndex(final ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.iterable.forEach(new ByteProcedure()
        {
            private int index;

            public void value(byte each)
            {
                objectIntProcedure.value(CollectByteToObjectIterable.this.function.valueOf(each), this.index++);
            }
        });
    }

    public <P> void forEachWith(final Procedure2<? super V, ? super P> procedure, final P parameter)
    {
        this.iterable.forEach(new ByteProcedure()
        {
            public void value(byte each)
            {
                procedure.value(CollectByteToObjectIterable.this.function.valueOf(each), parameter);
            }
        });
    }

    public Iterator<V> iterator()
    {
        return new Iterator<V>()
        {
            private final ByteIterator iterator = CollectByteToObjectIterable.this.iterable.byteIterator();

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }

            public V next()
            {
                return CollectByteToObjectIterable.this.function.valueOf(this.iterator.next());
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int size()
    {
        return this.iterable.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    @Override
    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
    }
}
