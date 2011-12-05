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

package com.gs.collections.impl.list.mutable;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

/**
 * JUnit test for {@link MultiReaderFastList}.
 */
public class MultiReaderFastListAcceptanceTest
{
    @Test
    public void sortThisOnListWithMoreThan9Elements()
    {
        MutableList<Integer> integers = MultiReaderFastList.newList(Interval.toReverseList(1, 10000));
        Verify.assertStartsWith(integers.sortThis(), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Verify.assertEndsWith(integers, 9997, 9998, 9999, 10000);
    }
}
