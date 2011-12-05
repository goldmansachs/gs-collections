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

package com.gs.collections.impl.block.predicate;

import java.util.Map;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;

public abstract class MapEntryPredicate<T1, T2>
        implements Predicate<Map.Entry<T1, T2>>, Predicate2<T1, T2>
{
    private static final long serialVersionUID = 1L;

    public boolean accept(Map.Entry<T1, T2> entry)
    {
        return this.accept(entry.getKey(), entry.getValue());
    }
}
