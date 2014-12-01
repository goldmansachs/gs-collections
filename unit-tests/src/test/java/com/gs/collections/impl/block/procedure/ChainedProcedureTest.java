/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class ChainedProcedureTest
{
    @Test
    public void procedure()
    {
        MutableList<String> list1 = Lists.mutable.of();
        MutableList<String> list2 = Lists.mutable.of();
        Procedure<String> procedure1 = new CollectionAddProcedure<>(list1);
        Procedure<String> procedure2 = new CollectionAddProcedure<>(list2);
        ChainedProcedure<String> chainedProcedure = ChainedProcedure.with(procedure1, procedure2);

        MutableList<String> list = FastList.newListWith("1", "2");
        Iterate.forEach(list, chainedProcedure);

        Assert.assertEquals(list, list1);
        Assert.assertEquals(list, list2);
    }
}
