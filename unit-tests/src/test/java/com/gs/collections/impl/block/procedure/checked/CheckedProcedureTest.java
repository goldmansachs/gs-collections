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

package com.gs.collections.impl.block.procedure.checked;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CheckedProcedureTest
{
    @Test
    public void dateProcedure()
    {
        Procedure<Date> procedure = new CheckedProcedure<Date>()
        {
            @Override
            public void safeValue(Date date)
            {
                Assert.assertNotNull(date.toString());
            }
        };
        procedure.value(new Date());
    }

    @Test
    public void collectionProcedure()
    {
        Procedure<Collection<String>> procedure = new CheckedProcedure<Collection<String>>()
        {
            @Override
            public void safeValue(Collection<String> collection)
            {
                Verify.assertNotEmpty(collection);
            }
        };
        procedure.value(Lists.fixedSize.of("1"));
    }

    @Test
    public void mapProcedure()
    {
        Procedure<Map<String, String>> procedure = new CheckedProcedure<Map<String, String>>()
        {
            @Override
            public void safeValue(Map<String, String> map)
            {
                Verify.assertContainsKey("1", map);
            }
        };
        procedure.value(Maps.fixedSize.of("1", "1"));
    }

    @Test
    public void checkedObjectIntProcedure()
    {
        boolean success = false;
        try
        {
            ObjectIntProcedure<Object> objectIntProcedure = new CheckedObjectIntProcedure<Object>()
            {
                @Override
                public void safeValue(Object object, int index)
                {
                    throw new RuntimeException();
                }
            };
            objectIntProcedure.value(null, 0);
        }
        catch (RuntimeException ignored)
        {
            success = true;
        }
        Assert.assertTrue(success);
    }

    @Test
    public void numberProcedure()
    {
        Procedure<Integer> procedure = new CheckedProcedure<Integer>()
        {
            @Override
            public void safeValue(Integer integer)
            {
                Assert.assertEquals(Integer.valueOf(1), integer);
            }
        };
        procedure.value(1);
    }

    @Test
    public void timestampProcedure()
    {
        Procedure<Timestamp> procedure = new CheckedProcedure<Timestamp>()
        {
            @Override
            public void safeValue(Timestamp timestamp)
            {
                Assert.assertNotNull(timestamp.toString());
            }
        };
        procedure.value(new Timestamp(0));
    }
}
