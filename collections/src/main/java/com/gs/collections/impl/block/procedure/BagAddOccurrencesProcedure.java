/*
 * Copyright 2014 Goldman Sachs.
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

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;

/**
 * @since 5.0.
 */
public final class BagAddOccurrencesProcedure<T> implements ObjectIntProcedure<T>
{
    private static final long serialVersionUID = 1L;

    private final MutableBag<T> mutableBag;

    public BagAddOccurrencesProcedure(MutableBag<T> mutableBag)
    {
        this.mutableBag = mutableBag;
    }

    public static <T> BagAddOccurrencesProcedure<T> on(MutableBag<T> mutableBag)
    {
        return new BagAddOccurrencesProcedure<T>(mutableBag);
    }

    public void value(T each, int occurrences)
    {
        this.mutableBag.addOccurrences(each, occurrences);
    }

    public MutableBag<T> getResult()
    {
        return this.mutableBag;
    }

    @Override
    public String toString()
    {
        return "MutableBag.addOccurrences()";
    }
}
