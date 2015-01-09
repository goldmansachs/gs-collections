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

package com.gs.collections.impl.utility.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.PriorityBlockingQueue;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;

public class DefaultSpeciesNewStrategy
{
    public static final DefaultSpeciesNewStrategy INSTANCE = new DefaultSpeciesNewStrategy();
    private static final Class<?>[] SIZE_CONSTRUCTOR_TYPES = {int.class};

    // todo - Consider behaviour for adapters, eg.:
    //         - SetAdapter.adapt(new TreeSet(orderingComparator))
    //         - SetAdapter.adapt(hibernateCollectionProxy)

    /**
     * Creates a new instance of a collection based on the class type of collection, not on the type of objects the collections contains.
     * e.g.  CollectionFactory.<Integer>speciesNew(hashSetOfString) returns a new HashSet<Integer>();
     * e.g.  CollectionFactory.<Date>speciesNew(linkedListOfWombles) returns a new LinkedList<Date>();
     */
    public <T> Collection<T> speciesNew(Collection<?> collection)
    {
        if (collection instanceof MutableCollection)
        {
            return ((MutableCollection<T>) collection).newEmpty();
        }
        if (collection instanceof Proxy)
        {
            return DefaultSpeciesNewStrategy.createNewInstanceForCollectionType(collection);
        }
        if (ReflectionHelper.hasDefaultConstructor(collection.getClass()))
        {
            return (Collection<T>) ReflectionHelper.newInstance(collection.getClass());
        }
        return DefaultSpeciesNewStrategy.createNewInstanceForCollectionType(collection);
    }

    /**
     * Creates a new instance of a collection based on the class type of collection and specified initial capacity,
     * not on the type of objects the collections contains.
     * e.g.  CollectionFactory.<Integer>speciesNew(hashSetOfString, 20) returns a new HashSet<Integer>(20);
     * e.g.  CollectionFactory.<Date>speciesNew(linkedListOfWombles, 42) returns a new LinkedList<Date>(42);
     */
    public <T> Collection<T> speciesNew(Collection<?> collection, int size)
    {
        if (size < 0)
        {
            throw new IllegalArgumentException("size may not be < 0 but was " + size);
        }

        if (collection instanceof Proxy
                || collection instanceof PriorityQueue
                || collection instanceof PriorityBlockingQueue
                || collection instanceof SortedSet)
        {
            return DefaultSpeciesNewStrategy.createNewInstanceForCollectionType(collection);
        }
        Constructor<?> constructor = ReflectionHelper.getConstructor(collection.getClass(), SIZE_CONSTRUCTOR_TYPES);
        if (constructor != null)
        {
            return (Collection<T>) ReflectionHelper.newInstance(constructor, size);
        }
        return this.speciesNew(collection);
    }

    private static <T> Collection<T> createNewInstanceForCollectionType(Collection<?> collection)
    {
        if (collection instanceof SortedSet
                || collection instanceof PriorityQueue
                || collection instanceof PriorityBlockingQueue)
        {
            return Lists.mutable.empty();
        }
        if (collection instanceof Set)
        {
            return Sets.mutable.empty();
        }
        return Lists.mutable.empty();
    }
}
