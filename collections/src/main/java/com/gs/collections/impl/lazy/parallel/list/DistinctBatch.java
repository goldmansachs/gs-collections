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

package com.gs.collections.impl.lazy.parallel.list;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.lazy.parallel.set.AbstractUnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

@Beta
class DistinctBatch<T> extends AbstractUnsortedSetBatch<T>
{
    private final ListBatch<T> listBatch;
    private final ConcurrentHashMap<T, Boolean> distinct;

    DistinctBatch(ListBatch<T> listBatch, ConcurrentHashMap<T, Boolean> distinct)
    {
        this.listBatch = listBatch;
        this.distinct = distinct;
    }

    public void forEach(final Procedure<? super T> procedure)
    {
        this.listBatch.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                if (DistinctBatch.this.distinct.put(each, true) == null)
                {
                    procedure.value(each);
                }
            }
        });
    }

    public boolean anySatisfy(final Predicate<? super T> predicate)
    {
        return this.listBatch.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return DistinctBatch.this.distinct.put(each, true) == null && predicate.accept(each);
            }
        });
    }

    public boolean allSatisfy(final Predicate<? super T> predicate)
    {
        return this.listBatch.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                boolean leftResult = DistinctBatch.this.distinct.put(each, true) == null;
                return !leftResult || predicate.accept(each);
            }
        });
    }
}
