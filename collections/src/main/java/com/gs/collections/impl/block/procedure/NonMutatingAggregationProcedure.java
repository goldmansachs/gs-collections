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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.MutableMap;

/**
 * This procedure is used to apply an aggregate function like sum on a grouped set of data.  The values in the
 * grouping must be immutable using this procedure.  An example would be using an Integer which is immutable
 * vs. an AtomicInteger which is not.
 */
public final class NonMutatingAggregationProcedure<T, K, V> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;
    private final MutableMap<K, V> map;
    private final Function<? super T, ? extends K> groupBy;
    private final Function0<? extends V> zeroValueFactory;
    private final Function2<? super V, ? super T, ? extends V> nonMutatingAggregator;

    public NonMutatingAggregationProcedure(MutableMap<K, V> map, Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        this.map = map;
        this.groupBy = groupBy;
        this.zeroValueFactory = zeroValueFactory;
        this.nonMutatingAggregator = nonMutatingAggregator;
    }

    public void value(T each)
    {
        K key = this.groupBy.valueOf(each);
        this.map.updateValueWith(key, this.zeroValueFactory, this.nonMutatingAggregator, each);
    }
}
