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

package com.gs.collections.impl.parallel;

import java.util.Collection;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.list.mutable.CompositeFastList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.internal.DefaultSpeciesNewStrategy;

public abstract class AbstractTransformerBasedCombiner<V, T, BT extends Procedure<T>>
        extends AbstractProcedureCombiner<BT>
{
    private static final long serialVersionUID = 1L;

    protected final Collection<V> result;

    protected AbstractTransformerBasedCombiner(boolean useCombineOne, Collection<V> targetCollection, Iterable<T> iterable, int initialCapacity)
    {
        super(useCombineOne);
        this.result = this.initializeResult(iterable, targetCollection, initialCapacity);
    }

    protected Collection<V> initializeResult(Iterable<T> sourceIterable, Collection<V> targetCollection, int initialCapacity)
    {
        if (targetCollection != null)
        {
            return targetCollection;
        }
        if (sourceIterable instanceof ListIterable)
        {
            return new CompositeFastList<V>();
        }
        if (sourceIterable instanceof SortedSetIterable)
        {
            return FastList.newList();
        }
        if (sourceIterable instanceof SetIterable)
        {
            this.setCombineOne(true);
            return UnifiedSet.newSet(initialCapacity);
        }
        if (sourceIterable instanceof Bag || sourceIterable instanceof MapIterable)
        {
            return HashBag.newBag(initialCapacity);
        }
        return this.createResultForCollection(sourceIterable, initialCapacity);
    }

    private Collection<V> createResultForCollection(Iterable<T> sourceCollection, int initialCapacity)
    {
        if (sourceCollection instanceof Collection)
        {
            return DefaultSpeciesNewStrategy.INSTANCE.speciesNew((Collection<?>) sourceCollection, initialCapacity);
        }
        return FastList.newList(initialCapacity);
    }

    public Collection<V> getResult()
    {
        return this.result;
    }
}
