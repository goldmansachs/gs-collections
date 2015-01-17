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

package com.gs.collections.impl.lazy;

import java.util.Iterator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.lazy.iterator.ChunkIterator;
import com.gs.collections.impl.utility.internal.IterableIterate;
import net.jcip.annotations.Immutable;

/**
 * A ChunkIterable is an iterable that partitions a source iterable into fixed size chunks as it iterates.
 */
@Immutable
public class ChunkIterable<T>
        extends AbstractLazyIterable<RichIterable<T>>
{
    private final Iterable<T> iterable;
    private final int size;

    public ChunkIterable(Iterable<T> iterable, int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        this.iterable = iterable;
        this.size = size;
    }

    public Iterator<RichIterable<T>> iterator()
    {
        return new ChunkIterator<T>(this.iterable, this.size);
    }

    public void each(Procedure<? super RichIterable<T>> procedure)
    {
        IterableIterate.forEach(this, procedure);
    }
}
