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
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.tuple.Tuples;

/**
 * Creates a PairImpl of objects and their indexes and adds the result to a target collection.
 */
public final class ZipWithIndexProcedure<T, R extends Collection<Pair<T, Integer>>>
        implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private int index = 0;
    private final R target;

    public ZipWithIndexProcedure(R target)
    {
        this.target = target;
    }

    public static <TT, RR extends Collection<Pair<TT, Integer>>> ZipWithIndexProcedure<TT, RR> create(RR target)
    {
        return new ZipWithIndexProcedure<TT, RR>(target);
    }

    public void value(T each)
    {
        this.target.add(Tuples.pair(each, Integer.valueOf(this.index)));
        this.index += 1;
    }
}
