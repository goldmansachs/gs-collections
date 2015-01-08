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
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.utility.internal.DefaultSpeciesNewStrategy;

public abstract class AbstractPredicateBasedCombiner<T, BT extends Procedure<T>>
        extends AbstractProcedureCombiner<BT>
{
    private static final long serialVersionUID = 1L;

    protected final Collection<T> result;

    protected AbstractPredicateBasedCombiner(
            boolean useCombineOne,
            Iterable<?> sourceCollection,
            int initialCapacity,
            Collection<T> targetCollection)
    {
        super(useCombineOne);
        this.result = this.initializeResult(sourceCollection, targetCollection, initialCapacity);
    }

    protected final Collection<T> initializeResult(
            Iterable<?> sourceCollection,
            Collection<T> targetCollection,
            int initialCapacity)
    {
        if (targetCollection != null)
        {
            return targetCollection;
        }
        if (sourceCollection instanceof ListIterable)
        {
            return new CompositeFastList<T>();
        }
        if (sourceCollection instanceof SortedSetIterable)
        {
            return TreeSortedSet.newSet(((SortedSetIterable<T>) sourceCollection).comparator());
        }
        if (sourceCollection instanceof SetIterable)
        {
            this.setCombineOne(true);
            return UnifiedSet.newSet(initialCapacity);
        }
        if (sourceCollection instanceof Bag || sourceCollection instanceof MapIterable)
        {
            return HashBag.newBag();
        }
        return this.createResultForCollection(sourceCollection, initialCapacity);
    }

    private Collection<T> createResultForCollection(Iterable<?> sourceCollection, int initialCapacity)
    {
        if (sourceCollection instanceof Collection)
        {
            return DefaultSpeciesNewStrategy.INSTANCE.speciesNew((Collection<?>) sourceCollection, initialCapacity);
        }
        return FastList.newList(initialCapacity);
    }

    public Collection<T> getResult()
    {
        return this.result;
    }
}
