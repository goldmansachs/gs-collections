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

package com.gs.collections.test;

import org.junit.Test;

public interface RichIterableWithDuplicatesTestCase extends RichIterableTestCase
{
    @Override
    default boolean allowsDuplicates()
    {
        return true;
    }

    @Test
    default void Iterable_sanity_check()
    {
        String s = "";
        Iterable<String> oneCopy = this.newWith(s);
        Iterable<String> twoCopies = this.newWith(s, s);
        IterableTestCase.assertEquals(!this.allowsDuplicates(), twoCopies.equals(oneCopy));
    }

    @Test
    default void RichIterable_size()
    {
        IterableTestCase.assertEquals(6, this.newWith(3, 3, 3, 2, 2, 1).size());
    }
}
