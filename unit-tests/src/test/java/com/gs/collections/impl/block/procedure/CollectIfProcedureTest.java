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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import org.junit.Assert;
import org.junit.Test;

public class CollectIfProcedureTest
{
    private static final int THE_ANSWER = 42;

    @Test
    public void constructorWithSize()
    {
        CollectIfProcedure<Integer, String> underTestTrue = new CollectIfProcedure<Integer, String>(10, Functions.getToString(), Predicates.alwaysTrue());
        CollectIfProcedure<Integer, String> underTestFalse = new CollectIfProcedure<Integer, String>(10, Functions.getToString(), Predicates.alwaysFalse());
        underTestTrue.value(THE_ANSWER);
        underTestFalse.value(THE_ANSWER);
        Assert.assertTrue(underTestTrue.getCollection().contains("42"));
        Assert.assertFalse(underTestFalse.getCollection().contains("42"));
    }
}
