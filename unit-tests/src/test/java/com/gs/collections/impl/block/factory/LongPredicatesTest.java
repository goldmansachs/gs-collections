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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class LongPredicatesTest
{
    private static final Function<Long, Long> LONG_VALUE = Long::longValue;

    @Test
    public void isOdd()
    {
        Assert.assertTrue(LongPredicates.isOdd().accept(1L));
        Assert.assertFalse(LongPredicates.isOdd().accept(-2L));
    }

    @Test
    public void isEven()
    {
        Assert.assertTrue(LongPredicates.isEven().accept(-42L));
        Assert.assertTrue(LongPredicates.isEven().accept(0L));
        Assert.assertFalse(LongPredicates.isEven().accept(1L));
    }

    @Test
    public void attributeIsOdd()
    {
        Assert.assertTrue(LongPredicates.attributeIsOdd(LONG_VALUE).accept(1L));
        Assert.assertFalse(LongPredicates.attributeIsOdd(LONG_VALUE).accept(-2L));
    }

    @Test
    public void attributeIsEven()
    {
        Assert.assertTrue(LongPredicates.attributeIsEven(LONG_VALUE).accept(-42L));
        Assert.assertTrue(LongPredicates.attributeIsEven(LONG_VALUE).accept(0L));
        Assert.assertFalse(LongPredicates.attributeIsEven(LONG_VALUE).accept(1L));
    }

    @Test
    public void isZero()
    {
        Assert.assertTrue(LongPredicates.isZero().accept(0L));
        Assert.assertFalse(LongPredicates.isZero().accept(1L));
        Assert.assertFalse(LongPredicates.isZero().accept(-1L));
    }

    @Test
    public void isPositive()
    {
        Assert.assertFalse(LongPredicates.isPositive().accept(0L));
        Assert.assertTrue(LongPredicates.isPositive().accept(1L));
        Assert.assertFalse(LongPredicates.isPositive().accept(-1L));
    }

    @Test
    public void isNegative()
    {
        Assert.assertFalse(LongPredicates.isNegative().accept(0L));
        Assert.assertFalse(LongPredicates.isNegative().accept(1L));
        Assert.assertTrue(LongPredicates.isNegative().accept(-1L));
    }

    @Test
    public void attributeIsZero()
    {
        Assert.assertTrue(LongPredicates.attributeIsZero(Integer::longValue).accept(0));
        Assert.assertFalse(LongPredicates.attributeIsZero(Integer::longValue).accept(1));
    }

    @Test
    public void attributeIsPositive()
    {
        Assert.assertTrue(LongPredicates.attributeIsPositive(Integer::longValue).accept(1));
        Assert.assertFalse(LongPredicates.attributeIsPositive(Integer::longValue).accept(0));
        Assert.assertFalse(LongPredicates.attributeIsPositive(Integer::longValue).accept(-1));
    }

    @Test
    public void attributeIsNegative()
    {
        Assert.assertTrue(LongPredicates.attributeIsNegative(Integer::longValue).accept(-1));
        Assert.assertFalse(LongPredicates.attributeIsNegative(Integer::longValue).accept(0));
        Assert.assertFalse(LongPredicates.attributeIsNegative(Integer::longValue).accept(1));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(LongPredicates.class);
    }
}
