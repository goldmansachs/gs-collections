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

package com.gs.collections.impl.lazy.parallel.set;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;

@Beta
class ParallelCollectUnsortedSetIterable<T, V> extends AbstractParallelUnsortedSetIterable<V>
{
    private final AbstractParallelUnsortedSetIterable<T> parallelSetIterable;
    private final Function<? super T, ? extends V> function;

    ParallelCollectUnsortedSetIterable(AbstractParallelUnsortedSetIterable<T> parallelSetIterable, Function<? super T, ? extends V> function)
    {
        this.parallelSetIterable = parallelSetIterable;
        this.function = function;
    }

    public void forEach(Procedure<? super V> procedure)
    {
        this.parallelSetIterable.forEach(Functions.bind(procedure, this.function));
    }

    @Override
    public boolean anySatisfy(Predicate<? super V> predicate)
    {
        return this.parallelSetIterable.anySatisfy(Predicates.attributePredicate(this.function, predicate));
    }
}
