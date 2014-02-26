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
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.IfProcedure;
import com.gs.collections.impl.lazy.parallel.set.UnsortedSetBatch;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;

@Beta
public class SelectListBatch<T> implements ListBatch<T>
{
    private final ListBatch<T> listBatch;
    private final Predicate<? super T> predicate;

    public SelectListBatch(ListBatch<T> listBatch, Predicate<? super T> predicate)
    {
        this.listBatch = listBatch;
        this.predicate = predicate;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.listBatch.forEach(new IfProcedure<T>(this.predicate, procedure));
    }

    /*
    public ListBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectListBatch<T>(this.listBatch, Predicates.and(this.predicate, predicate));
    }
    */

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.listBatch.anySatisfy(Predicates.and(this.predicate, predicate));
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.listBatch.allSatisfy(new SelectListBatchAllSatisfyPredicate<T>(this.predicate, predicate));
    }

    public T detect(Predicate<? super T> predicate)
    {
        return this.listBatch.detect(Predicates.and(this.predicate, predicate));
    }

    public ListBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectListBatch<T>(this, predicate);
    }

    public <V> ListBatch<V> collect(Function<? super T, ? extends V> function)
    {
        return new CollectListBatch<T, V>(this, function);
    }

    public UnsortedSetBatch<T> distinct(ConcurrentHashMap<T, Boolean> distinct)
    {
        return new DistinctBatch<T>(this, distinct);
    }

    private static final class SelectListBatchAllSatisfyPredicate<T> implements Predicate<T>
    {
        private final Predicate<? super T> left;
        private final Predicate<? super T> right;

        private SelectListBatchAllSatisfyPredicate(Predicate<? super T> left, Predicate<? super T> right)
        {
            this.left = left;
            this.right = right;
        }

        public boolean accept(T each)
        {
            boolean leftResult = this.left.accept(each);
            return !leftResult || this.right.accept(each);
        }
    }
}
