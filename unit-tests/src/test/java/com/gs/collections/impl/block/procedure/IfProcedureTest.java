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
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IfProcedureTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IfProcedureTest.class);

    @Test
    public void procedure()
    {
        MutableList<String> list1 = Lists.mutable.of();
        MutableList<String> list2 = Lists.mutable.of();
        Procedure<String> ifProcedure = new IfProcedure<>("1"::equals, list1::add, list2::add);
        LOGGER.info("{}", ifProcedure);
        MutableList<String> list = FastList.newListWith("1", "2");
        Iterate.forEach(list, ifProcedure);
        Assert.assertEquals(1, list1.size());
        Verify.assertContains("1", list1);
        Assert.assertEquals(1, list2.size());
        Verify.assertContains("2", list2);
    }
}
