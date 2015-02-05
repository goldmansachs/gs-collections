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
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.lazy.parallel.Batch;

@Beta
public interface UnsortedBagBatch<T> extends Batch<T>
{
    void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure);

    UnsortedBagBatch<T> select(Predicate<? super T> predicate);

    <V> UnsortedBagBatch<V> collect(Function<? super T, ? extends V> function);

    <V> UnsortedBagBatch<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);
}
