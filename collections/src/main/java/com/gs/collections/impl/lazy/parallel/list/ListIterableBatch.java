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

package com.gs.collections.impl.lazy.parallel.list;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.impl.lazy.parallel.AbstractBatch;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

@Beta
public class ListIterableBatch<T> extends AbstractBatch<T> implements RootListBatch<T>
{
    private final ListIterable<T> list;
    private final int chunkStartIndex;
    private final int chunkEndIndex;

    public ListIterableBatch(ListIterable<T> list, int chunkStartIndex, int chunkEndIndex)
    {
        this.list = list;
        this.chunkStartIndex = chunkStartIndex;
        this.chunkEndIndex = chunkEndIndex;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        for (int i = this.chunkStartIndex; i < this.chunkEndIndex; i++)
        {
            procedure.value(this.list.get(i));
        }
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        int count = 0;
        for (int i = this.chunkStartIndex; i < this.chunkEndIndex; i++)
        {
            if (predicate.accept(this.list.get(i)))
            {
                count++;
            }
        }
        return count;
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        for (int i = this.chunkStartIndex; i < this.chunkEndIndex; i++)
        {
            if (predicate.accept(this.list.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        for (int i = this.chunkStartIndex; i < this.chunkEndIndex; i++)
        {
            if (!predicate.accept(this.list.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    public T detect(Predicate<? super T> predicate)
    {
        for (int i = this.chunkStartIndex; i < this.chunkEndIndex; i++)
        {
            if (predicate.accept(this.list.get(i)))
            {
                return this.list.get(i);
            }
        }
        return null;
    }

    public ListBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectListBatch<T>(this, predicate);
    }

    public <V> ListBatch<V> collect(Function<? super T, ? extends V> function)
    {
        return new CollectListBatch<T, V>(this, function);
    }

    public <V> ListBatch<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new FlatCollectListBatch<T, V>(this, function);
    }

    public UnsortedSetBatch<T> distinct(ConcurrentHashMap<T, Boolean> distinct)
    {
        return new DistinctBatch<T>(this, distinct);
    }
}
