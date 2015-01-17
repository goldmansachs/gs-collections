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

package com.gs.collections.impl.utility;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.impl.utility.internal.RandomAccessListIterate;

/**
 * @since 6.0
 */
public final class OrderedIterate
{
    private OrderedIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <S, T> boolean corresponds(OrderedIterable<T> o1, OrderedIterable<S> o2, Predicate2<? super T, ? super S> predicate)
    {
        if (o1.size() != o2.size())
        {
            return false;
        }
        if (o1 instanceof RandomAccess)
        {
            return RandomAccessListIterate.corresponds((List<T>) o1, o2, predicate);
        }
        if (o2 instanceof RandomAccess)
        {
            List<S> otherList = (List<S>) o2;
            Iterator<T> iterator = o1.iterator();
            for (int index = 0; index < otherList.size(); index++)
            {
                if (!predicate.accept(iterator.next(), otherList.get(index)))
                {
                    return false;
                }
            }
            return true;
        }

        Iterator<T> iterator1 = o1.iterator();
        Iterator<S> iterator2 = o2.iterator();
        while (iterator1.hasNext())
        {
            if (!predicate.accept(iterator1.next(), iterator2.next()))
            {
                return false;
            }
        }
        return true;
    }
}
