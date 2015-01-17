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

package com.gs.collections.test.list.mutable;

import java.util.Iterator;
import java.util.ListIterator;

import com.gs.collections.impl.list.mutable.FastList;

public final class FastListNoIterator<T> extends FastList<T>
{
    @Override
    public Iterator<T> iterator()
    {
        throw new AssertionError("No iteration patterns should delegate to iterator()");
    }

    @Override
    public ListIterator<T> listIterator()
    {
        throw new AssertionError("No iteration patterns should delegate to listIterator()");
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        throw new AssertionError("No iteration patterns should delegate to listIterator()");
    }
}
