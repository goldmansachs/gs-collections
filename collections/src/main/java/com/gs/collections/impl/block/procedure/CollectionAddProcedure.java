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

package com.gs.collections.impl.block.procedure;

import java.util.Collection;

import com.gs.collections.api.block.procedure.Procedure;

/**
 * CollectionAddProcedure adds elements to the specified collection when one of the block methods are called.
 */
public final class CollectionAddProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Collection<T> collection;

    public CollectionAddProcedure(Collection<T> newCollection)
    {
        this.collection = newCollection;
    }

    public static <T> CollectionAddProcedure<T> on(Collection<T> newCollection)
    {
        return new CollectionAddProcedure<T>(newCollection);
    }

    public void value(T object)
    {
        this.collection.add(object);
    }

    public Collection<T> getResult()
    {
        return this.collection;
    }

    @Override
    public String toString()
    {
        return "Collection.add()";
    }
}
