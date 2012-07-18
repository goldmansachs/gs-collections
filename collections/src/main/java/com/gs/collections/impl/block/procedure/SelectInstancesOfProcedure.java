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

import java.util.Collection;

import com.gs.collections.api.block.procedure.Procedure;

/**
 * Calls {@link Class#isInstance(Object)} on an object to determine if it should be added to a target collection.
 *
 * @since 2.0
 */
public final class SelectInstancesOfProcedure<T> implements Procedure<Object>
{
    private static final long serialVersionUID = 1L;

    private final Class<T> clazz;
    private final Collection<T> collection;

    public SelectInstancesOfProcedure(Class<T> clazz, Collection<T> targetCollection)
    {
        this.clazz = clazz;
        this.collection = targetCollection;
    }

    public void value(Object object)
    {
        if (this.clazz.isInstance(object))
        {
            this.collection.add((T) object);
        }
    }

    public Collection<T> getCollection()
    {
        return this.collection;
    }
}
