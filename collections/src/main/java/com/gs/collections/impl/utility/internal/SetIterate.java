/*
 * Copyright 2013 Goldman Sachs.
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @since 1.0
 */
public final class SetIterate
{
    private SetIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * @see Collection#removeAll(Collection)
     * @since 1.0
     */
    public static boolean removeAllIterable(Set<?> collection, Iterable<?> iterable)
    {
        if (iterable instanceof Set<?> && ((Set<?>) iterable).size() > collection.size())
        {
            boolean modified = false;
            Iterator<?> e = collection.iterator();
            while (e.hasNext())
            {
                if (((Set<?>) iterable).contains(e.next()))
                {
                    e.remove();
                    modified = true;
                }
            }
            return modified;
        }

        boolean modified = false;
        for (Object each : iterable)
        {
            if (collection.remove(each))
            {
                modified = true;
            }
        }
        return modified;
    }
}
