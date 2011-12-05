/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.parallel;

import java.util.Collection;

import com.gs.collections.impl.block.procedure.FlatCollectProcedure;

/**
 * Combines the results of a Collection of {@link FlatCollectProcedure}s which each hold onto a transformed (flatten) collection
 * of results.
 */
public final class FlatCollectProcedureCombiner<T, V>
        extends AbstractTransformerBasedCombiner<V, T, FlatCollectProcedure<T, V>>
{
    private static final long serialVersionUID = 1L;

    public FlatCollectProcedureCombiner(
            Iterable<T> sourceIterable,
            Collection<V> targetCollection,
            int initialCapacity,
            boolean combineOne)
    {
        super(combineOne, targetCollection, sourceIterable, initialCapacity);
    }

    public void combineOne(FlatCollectProcedure<T, V> procedureCombine)
    {
        this.result.addAll(procedureCombine.getCollection());
    }
}
