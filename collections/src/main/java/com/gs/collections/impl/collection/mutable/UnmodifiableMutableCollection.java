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

package com.gs.collections.impl.collection.mutable;

import java.io.Serializable;
import java.util.Collection;

import com.gs.collections.api.collection.MutableCollection;

/**
 * An unmodifiable view of a collection.
 *
 * @see MutableCollection#asUnmodifiable()
 */
public class UnmodifiableMutableCollection<T>
        extends AbstractUnmodifiableMutableCollection<T>
        implements Serializable
{
    UnmodifiableMutableCollection(MutableCollection<? extends T> collection)
    {
        super(collection);
    }

    /**
     * This method will take a MutableCollection and wrap it directly in a UnmodifiableMutableCollection.  It will
     * take any other non-GS-collection and first adapt it will a CollectionAdapter, and then return a
     * UnmodifiableMutableCollection that wraps the adapter.
     */
    public static <E, C extends Collection<E>> UnmodifiableMutableCollection<E> of(C collection)
    {
        if (collection == null)
        {
            throw new IllegalArgumentException("cannot create a UnmodifiableMutableCollection for null");
        }
        return new UnmodifiableMutableCollection<E>(CollectionAdapter.adapt(collection));
    }

    protected Object writeReplace()
    {
        return new UnmodifiableCollectionSerializationProxy<T>(this.getMutableCollection());
    }
}
