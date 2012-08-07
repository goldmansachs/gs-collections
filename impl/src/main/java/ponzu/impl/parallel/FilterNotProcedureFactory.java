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

import ponzu.api.block.predicate.Predicate;
import ponzu.impl.block.procedure.FilterNotProcedure;
import ponzu.impl.list.mutable.FastList;

public final class FilterNotProcedureFactory<T> implements ProcedureFactory<FilterNotProcedure<T>>
{
    private final Predicate<? super T> predicate;
    private final int collectionSize;

    public FilterNotProcedureFactory(Predicate<? super T> newPredicate, int newInitialCapacity)
    {
        this.predicate = newPredicate;
        this.collectionSize = newInitialCapacity;
    }

    public FilterNotProcedure<T> create()
    {
        return new FilterNotProcedure<T>(this.predicate, new FastList<T>(this.collectionSize));
    }
}
