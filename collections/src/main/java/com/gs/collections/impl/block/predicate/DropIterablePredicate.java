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

import com.gs.collections.api.block.predicate.Predicate;

public class DropIterablePredicate<T> implements Predicate<T>
{
    private static final long serialVersionUID = 1L;

    private final int count;
    private int index;

    public DropIterablePredicate(int count)
    {
        this.count = count;
        this.index = 0;
    }

    public boolean accept(T each)
    {
        return this.index++ >= this.count;
    }
}
