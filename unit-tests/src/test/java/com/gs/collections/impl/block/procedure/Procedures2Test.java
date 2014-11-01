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

import java.io.IOException;

import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class Procedures2Test
{
    @Test
    public void throwing()
    {
        Verify.assertThrowsWithCause(
                RuntimeException.class,
                IOException.class,
                () -> Procedures2.throwing((a, b) -> { throw new IOException(); }).value(null, null));
    }

    @Test
    public void asProcedure2()
    {
        CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(FastList.<Integer>newList());
        Procedure2<Integer, Object> procedure2 = Procedures2.fromProcedure(procedure);
        procedure2.value(1, null);
        Assert.assertEquals(FastList.newListWith(1), procedure.getResult());
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(Procedures2.class);
    }
}
