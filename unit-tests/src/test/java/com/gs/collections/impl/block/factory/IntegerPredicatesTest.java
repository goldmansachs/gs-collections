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

public class IntegerPredicatesTest
{
    private static final Function<Integer, Integer> INT_VALUE = integer -> integer;

    @Test
    public void isOdd()
    {
        Assert.assertTrue(IntegerPredicates.isOdd().accept(1));
        Assert.assertFalse(IntegerPredicates.isOdd().accept(-2));
    }

    @Test
    public void isEven()
    {
        Assert.assertTrue(IntegerPredicates.isEven().accept(-42));
        Assert.assertTrue(IntegerPredicates.isEven().accept(0));
        Assert.assertFalse(IntegerPredicates.isEven().accept(1));
    }

    @Test
    public void attributeIsOdd()
    {
        Assert.assertTrue(IntegerPredicates.attributeIsOdd(INT_VALUE).accept(1));
        Assert.assertFalse(IntegerPredicates.attributeIsOdd(INT_VALUE).accept(-2));
    }

    @Test
    public void attributeIsEven()
    {
        Assert.assertTrue(IntegerPredicates.attributeIsEven(INT_VALUE).accept(-42));
        Assert.assertTrue(IntegerPredicates.attributeIsEven(INT_VALUE).accept(0));
        Assert.assertFalse(IntegerPredicates.attributeIsEven(INT_VALUE).accept(1));
    }

    @Test
    public void attributeIsZero()
    {
        Assert.assertFalse(IntegerPredicates.attributeIsZero(INT_VALUE).accept(-42));
        Assert.assertTrue(IntegerPredicates.attributeIsZero(INT_VALUE).accept(0));
        Assert.assertFalse(IntegerPredicates.attributeIsZero(INT_VALUE).accept(1));
    }

    @Test
    public void attributeIsPositive()
    {
        Assert.assertFalse(IntegerPredicates.attributeIsPositive(INT_VALUE).accept(-42));
        Assert.assertFalse(IntegerPredicates.attributeIsPositive(INT_VALUE).accept(0));
        Assert.assertTrue(IntegerPredicates.attributeIsPositive(INT_VALUE).accept(1));
    }

    @Test
    public void attributeIsNegative()
    {
        Assert.assertTrue(IntegerPredicates.attributeIsNegative(INT_VALUE).accept(-42));
        Assert.assertFalse(IntegerPredicates.attributeIsNegative(INT_VALUE).accept(0));
        Assert.assertFalse(IntegerPredicates.attributeIsNegative(INT_VALUE).accept(1));
    }

    @Test
    public void isZero()
    {
        Assert.assertTrue(IntegerPredicates.isZero().accept(0));
        Assert.assertFalse(IntegerPredicates.isZero().accept(1));
        Assert.assertFalse(IntegerPredicates.isZero().accept(-1));
    }

    @Test
    public void isPositive()
    {
        Assert.assertFalse(IntegerPredicates.isPositive().accept(0));
        Assert.assertTrue(IntegerPredicates.isPositive().accept(1));
        Assert.assertFalse(IntegerPredicates.isPositive().accept(-1));
    }

    @Test
    public void isNegative()
    {
        Assert.assertFalse(IntegerPredicates.isNegative().accept(0));
        Assert.assertFalse(IntegerPredicates.isNegative().accept(1));
        Assert.assertTrue(IntegerPredicates.isNegative().accept(-1));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(IntegerPredicates.class);
    }
}
