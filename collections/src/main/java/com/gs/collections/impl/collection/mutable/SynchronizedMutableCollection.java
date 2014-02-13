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
import java.util.Collections;

import com.gs.collections.api.collection.MutableCollection;
import net.jcip.annotations.ThreadSafe;

/**
 * A synchronized view of a {@link MutableCollection}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableCollection#asSynchronized()
 */
@ThreadSafe
public class SynchronizedMutableCollection<T>
        extends AbstractSynchronizedMutableCollection<T>
        implements Serializable
{
    SynchronizedMutableCollection(MutableCollection<T> newCollection)
    {
        this(newCollection, null);
    }

    SynchronizedMutableCollection(MutableCollection<T> newCollection, Object newLock)
    {
        super(newCollection, newLock);
    }

    /**
     * This method will take a MutableCollection and wrap it directly in a SynchronizedMutableCollection.  It will
     * take any other non-GS-collection and first adapt it will a CollectionAdapter, and then return a
     * SynchronizedMutableCollection that wraps the adapter.
     */
    public static <E, C extends Collection<E>> SynchronizedMutableCollection<E> of(C collection)
    {
        return new SynchronizedMutableCollection<E>(CollectionAdapter.adapt(collection));
    }

    /**
     * This method will take a MutableCollection and wrap it directly in a SynchronizedMutableCollection.  It will
     * take any other non-GS-collection and first adapt it will a CollectionAdapter, and then return a
     * SynchronizedMutableCollection that wraps the adapter.  Additionally, a developer specifies which lock to use
     * with the collection.
     */
    public static <E, C extends Collection<E>> SynchronizedMutableCollection<E> of(C collection, Object lock)
    {
        return new SynchronizedMutableCollection<E>(CollectionAdapter.adapt(collection), lock);
    }

    protected Object writeReplace()
    {
        return new SynchronizedCollectionSerializationProxy<T>(this.getCollection());
    }
}
