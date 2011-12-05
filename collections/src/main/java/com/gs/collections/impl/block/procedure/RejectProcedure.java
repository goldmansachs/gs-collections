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

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;

/**
 * Applies a predicate to an object to determine if it should be added to a target collection.
 */
public final class RejectProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final Predicate<? super T> predicate;
    private final Collection<T> collection;

    public RejectProcedure(Predicate<? super T> newPredicate, Collection<T> targetCollection)
    {
        this.predicate = newPredicate;
        this.collection = targetCollection;
    }

    public void value(T object)
    {
        if (!this.predicate.accept(object))
        {
            this.collection.add(object);
        }
    }

    public Collection<T> getCollection()
    {
        return this.collection;
    }
}
