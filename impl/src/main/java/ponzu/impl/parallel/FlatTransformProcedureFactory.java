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

package ponzu.impl.parallel;

import java.util.Collection;

import ponzu.api.block.function.Function;
import ponzu.impl.block.procedure.FlatTransformProcedure;
import ponzu.impl.list.mutable.FastList;

public final class FlatTransformProcedureFactory<T, V> implements ProcedureFactory<FlatTransformProcedure<T, V>>
{
    private final int collectionSize;
    private final Function<? super T, Collection<V>> function;

    public FlatTransformProcedureFactory(Function<? super T, Collection<V>> function, int newTaskSize)
    {
        this.collectionSize = newTaskSize;
        this.function = function;
    }

    public FlatTransformProcedure<T, V> create()
    {
        return new FlatTransformProcedure<T, V>(this.function, new FastList<V>(this.collectionSize));
    }
}
