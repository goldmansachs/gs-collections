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

package com.gs.collections.impl.utility.internal;

import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.bag.sorted.SortedBag;

public final class SortedBagIterables
{
    private SortedBagIterables()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> int compare(SortedBag<T> bagA, SortedBag<T> bagB)
    {
        Iterator<T> itrA = bagA.iterator();
        Iterator<T> itrB = bagB.iterator();
        if (bagA.comparator() != null)
        {
            Comparator<? super T> comparator = bagA.comparator();
            while (itrA.hasNext())
            {
                if (itrB.hasNext())
                {
                    int val = comparator.compare(itrA.next(), itrB.next());
                    if (val != 0)
                    {
                        return val;
                    }
                }
                else
                {
                    return 1;
                }
            }
            return itrB.hasNext() ? -1 : 0;
        }

        while (itrA.hasNext())
        {
            if (itrB.hasNext())
            {
                int val = ((Comparable<T>) itrA.next()).compareTo(itrB.next());
                if (val != 0)
                {
                    return val;
                }
            }
            else
            {
                return 1;
            }
        }
        return itrB.hasNext() ? -1 : 0;
    }
}
