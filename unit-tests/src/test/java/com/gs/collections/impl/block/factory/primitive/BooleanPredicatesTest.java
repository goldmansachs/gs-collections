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

package com.gs.collections.impl.block.factory.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public final class BooleanPredicatesTest
{
    @Test
    public void testEqual()
    {
        Assert.assertTrue(BooleanPredicates.equal(true).accept(true));
        Assert.assertTrue(BooleanPredicates.equal(false).accept(false));
        Assert.assertFalse(BooleanPredicates.equal(true).accept(false));
        Assert.assertFalse(BooleanPredicates.equal(false).accept(true));
    }

    @Test
    public void testIsTrue()
    {
        Assert.assertTrue(BooleanPredicates.isTrue().accept(true));
        Assert.assertFalse(BooleanPredicates.isTrue().accept(false));
    }

    @Test
    public void testIsFalse()
    {
        Assert.assertTrue(BooleanPredicates.isFalse().accept(false));
        Assert.assertFalse(BooleanPredicates.isFalse().accept(true));
    }

    @Test
    public void testAnd()
    {
        Assert.assertFalse(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.equal(true)).accept(false));
        Assert.assertFalse(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.equal(false)).accept(false));
        Assert.assertFalse(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.equal(true)).accept(false));
        Assert.assertTrue(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.equal(false)).accept(false));

        Assert.assertTrue(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.equal(true)).accept(true));
        Assert.assertFalse(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.equal(false)).accept(false));
        Assert.assertFalse(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.equal(true)).accept(true));
        Assert.assertFalse(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.equal(false)).accept(true));

        Assert.assertFalse(BooleanPredicates.and(BooleanPredicates.isFalse(), value -> !value).accept(true));
        Assert.assertTrue(BooleanPredicates.and(BooleanPredicates.isFalse(), value -> !value).accept(false));
    }

    @Test
    public void testOr()
    {
        Assert.assertFalse(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.equal(true)).accept(false));
        Assert.assertTrue(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.equal(false)).accept(false));
        Assert.assertTrue(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.equal(true)).accept(false));
        Assert.assertTrue(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.equal(false)).accept(false));

        Assert.assertTrue(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.equal(true)).accept(true));
        Assert.assertTrue(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.equal(false)).accept(true));
        Assert.assertTrue(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.equal(true)).accept(true));
        Assert.assertFalse(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.equal(false)).accept(true));

        Assert.assertTrue(BooleanPredicates.or(BooleanPredicates.isFalse(), value -> !value).accept(false));
        Assert.assertFalse(BooleanPredicates.or(BooleanPredicates.isFalse(), value -> !value).accept(true));
    }

    @Test
    public void testNot()
    {
        Assert.assertTrue(BooleanPredicates.not(BooleanPredicates.isTrue()).accept(false));
        Assert.assertFalse(BooleanPredicates.not(BooleanPredicates.isTrue()).accept(true));
        Assert.assertTrue(BooleanPredicates.not(BooleanPredicates.isFalse()).accept(true));
        Assert.assertFalse(BooleanPredicates.not(BooleanPredicates.isFalse()).accept(false));
        Assert.assertTrue(BooleanPredicates.not(true).accept(false));
        Assert.assertFalse(BooleanPredicates.not(true).accept(true));
        Assert.assertTrue(BooleanPredicates.not(false).accept(true));
        Assert.assertFalse(BooleanPredicates.not(false).accept(false));
    }

    @Test
    public void testAlwaysTrue()
    {
        Assert.assertTrue(BooleanPredicates.alwaysTrue().accept(false));
        Assert.assertTrue(BooleanPredicates.alwaysTrue().accept(true));
    }

    @Test
    public void testAlwaysFalse()
    {
        Assert.assertFalse(BooleanPredicates.alwaysFalse().accept(false));
        Assert.assertFalse(BooleanPredicates.alwaysFalse().accept(true));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(BooleanPredicates.class);
    }
}
