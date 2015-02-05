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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.block.procedure.IfProcedure;
import com.gs.collections.impl.lazy.parallel.AbstractBatch;

@Beta
public class SelectUnsortedBagBatch<T> extends AbstractBatch<T> implements UnsortedBagBatch<T>
{
    private final UnsortedBagBatch<T> unsortedBagBatch;
    private final Predicate<? super T> predicate;

    public SelectUnsortedBagBatch(UnsortedBagBatch<T> unsortedBagBatch, Predicate<? super T> predicate)
    {
        this.unsortedBagBatch = unsortedBagBatch;
        this.predicate = predicate;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.unsortedBagBatch.forEach(new IfProcedure<T>(this.predicate, procedure));
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
        this.unsortedBagBatch.forEachWithOccurrences(new IfProcedureWithOccurrences<T>(this.predicate, procedure));
    }

    public UnsortedBagBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectUnsortedBagBatch<T>(this, predicate);
    }

    public <V> UnsortedBagBatch<V> collect(Function<? super T, ? extends V> function)
    {
        return new CollectUnsortedBagBatch<T, V>(this, function);
    }

    public <V> UnsortedBagBatch<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return new FlatCollectUnsortedBagBatch<T, V>(this, function);
    }

    /*
    public BagBatch<T> select(Predicate<? super T> predicate)
    {
        return new SelectBagBatch<T>(this.bagBatch, Predicates.and(this.predicate, predicate));
    }
    */

    private static final class IfProcedureWithOccurrences<T> implements ObjectIntProcedure<T>
    {
        private final Predicate<? super T> predicate;
        private final ObjectIntProcedure<? super T> procedure;

        private IfProcedureWithOccurrences(Predicate<? super T> predicate, ObjectIntProcedure<? super T> procedure)
        {
            this.predicate = predicate;
            this.procedure = procedure;
        }

        public void value(T each, int parameter)
        {
            if (this.predicate.accept(each))
            {
                this.procedure.value(each, parameter);
            }
        }
    }
}
