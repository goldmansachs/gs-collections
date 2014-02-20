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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.IfProcedure;

@Beta
class ParallelSelectUnsortedBag<T> extends AbstractParallelUnsortedBag<T>
{
    private final AbstractParallelUnsortedBag<T> parallelUnsortedBag;
    private final Predicate<? super T> predicate;

    ParallelSelectUnsortedBag(AbstractParallelUnsortedBag<T> parallelUnsortedBag, Predicate<? super T> predicate)
    {
        this.parallelUnsortedBag = parallelUnsortedBag;
        this.predicate = predicate;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.parallelUnsortedBag.forEach(new IfProcedure<T>(this.predicate, procedure));
    }

    public void forEachWithOccurrences(final ObjectIntProcedure<? super T> procedure)
    {
        this.parallelUnsortedBag.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int parameter)
            {
                if (ParallelSelectUnsortedBag.this.predicate.accept(each))
                {
                    procedure.value(each, parameter);
                }
            }
        });
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.parallelUnsortedBag.anySatisfy(Predicates.and(this.predicate, predicate));
    }
}
