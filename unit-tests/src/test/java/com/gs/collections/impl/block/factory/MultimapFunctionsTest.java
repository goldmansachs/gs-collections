/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class MultimapFunctionsTest
{
    @Test
    public void get()
    {
        MutableListMultimap<String, String> multimap = FastListMultimap.newMultimap();
        multimap.putAll("One", FastList.newListWith("O", "N", "E"));
        multimap.putAll("Two", FastList.newListWith("T", "W", "O"));
        multimap.putAll("Three", FastList.newListWith("T", "H", "R", "E", "E"));

        Function<String, RichIterable<String>> getFunction = MultimapFunctions.get(multimap);

        Assert.assertEquals(
                FastList.newListWith(
                        FastList.newListWith("O", "N", "E"),
                        FastList.newListWith("T", "W", "O"),
                        FastList.newListWith("T", "H", "R", "E", "E")),
                FastList.newListWith("One", "Two", "Three").collect(getFunction));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(MultimapFunctions.class);
    }
}
