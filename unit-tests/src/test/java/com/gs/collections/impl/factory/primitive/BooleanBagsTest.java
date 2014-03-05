/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.factory.primitive;

import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.factory.bag.primitive.ImmutableBooleanBagFactory;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class BooleanBagsTest
{
    @Test
    public void immutables()
    {
        ImmutableBooleanBagFactory bagFactory = BooleanBags.immutable;
        Assert.assertEquals(new BooleanHashBag(), bagFactory.of());
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of());
        Assert.assertEquals(BooleanHashBag.newBagWith(true), bagFactory.of(true));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), bagFactory.of(true, false));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), bagFactory.of(true, false, true));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false), bagFactory.of(true, false, true, false));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true, false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true), bagFactory.of(true, false, true, false, true));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true, false, true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true, false), bagFactory.of(true, false, true, false, true, false));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true, false, true, false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true, false, true), bagFactory.of(true, false, true, false, true, false, true));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true, false, true, false, true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true, false, true, true), bagFactory.of(true, false, true, false, true, false, true, true));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true, false, true, false, true, true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true, false, true, true, true), bagFactory.of(true, false, true, false, true, false, true, true, true));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true, false, true, false, true, true, true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true, false, true, true, true, false), bagFactory.of(true, false, true, false, true, false, true, true, true, false));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.of(true, false, true, false, true, false, true, true, true, false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), bagFactory.ofAll(BooleanHashBag.newBagWith(true, false, true)));
        Verify.assertInstanceOf(ImmutableBooleanBag.class, bagFactory.ofAll(BooleanHashBag.newBagWith(true, false, true)));
    }

    @Test
    public void emptyBag()
    {
        Verify.assertEmpty(BooleanBags.immutable.of());
        Assert.assertSame(BooleanBags.immutable.of(), BooleanBags.immutable.of());
        Verify.assertPostSerializedIdentity(BooleanBags.immutable.of());
    }

    @Test
    public void newBagWith()
    {
        ImmutableBooleanBag bag = BooleanBags.immutable.of();
        Assert.assertEquals(bag, BooleanBags.immutable.of(bag.toArray()));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(true));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(true, false));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(true, false, true));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(true, false, true, false));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(true, false, true, false, true));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(true, false, true, false, true, false));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(true, false, true, false, true, false, true));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(true, false, true, false, true, false, true, true));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(true, false, true, false, true, false, true, true, true));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(true, false, true, false, true, false, true, true, true, false));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(true, false, true, false, true, false, true, true, true, false, true));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(true, false, true, false, true, false, true, true, true, false, true, false));
    }

    @SuppressWarnings("RedundantArrayCreation")
    @Test
    public void newBagWithArray()
    {
        ImmutableBooleanBag bag = BooleanBags.immutable.of();
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(new boolean[]{true}));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(new boolean[]{true, false}));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(new boolean[]{true, false, true}));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(new boolean[]{true, false, true, false}));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(new boolean[]{true, false, true, false, true}));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(new boolean[]{true, false, true, false, true, false}));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(new boolean[]{true, false, true, false, true, false, true}));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(new boolean[]{true, false, true, false, true, false, true, true}));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(new boolean[]{true, false, true, false, true, false, true, true, true}));
        Assert.assertEquals(bag = bag.newWith(false), BooleanBags.immutable.of(new boolean[]{true, false, true, false, true, false, true, true, true, false}));
        Assert.assertEquals(bag = bag.newWith(true), BooleanBags.immutable.of(new boolean[]{true, false, true, false, true, false, true, true, true, false, true}));
    }

    @Test
    public void newBagWithBag()
    {
        ImmutableBooleanBag bag = BooleanBags.immutable.of();
        BooleanHashBag booleanHashBag = BooleanHashBag.newBagWith(true);
        Assert.assertEquals(bag = bag.newWith(true), booleanHashBag.toImmutable());
        Assert.assertEquals(bag = bag.newWith(false), booleanHashBag.with(false).toImmutable());
        Assert.assertEquals(bag = bag.newWith(true), booleanHashBag.with(true).toImmutable());
        Assert.assertEquals(bag = bag.newWith(false), booleanHashBag.with(false).toImmutable());
        Assert.assertEquals(bag = bag.newWith(true), booleanHashBag.with(true).toImmutable());
        Assert.assertEquals(bag = bag.newWith(false), booleanHashBag.with(false).toImmutable());
        Assert.assertEquals(bag = bag.newWith(true), booleanHashBag.with(true).toImmutable());
        Assert.assertEquals(bag = bag.newWith(true), booleanHashBag.with(true).toImmutable());
        Assert.assertEquals(bag = bag.newWith(true), booleanHashBag.with(true).toImmutable());
        Assert.assertEquals(bag = bag.newWith(false), booleanHashBag.with(false).toImmutable());
        Assert.assertEquals(bag = bag.newWith(true), booleanHashBag.with(true).toImmutable());
    }

    @Test
    public void newBagWithWithBag()
    {
        Assert.assertEquals(new BooleanHashBag(), BooleanBags.immutable.ofAll(new BooleanHashBag()));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), BooleanBags.immutable.ofAll(BooleanHashBag.newBagWith(true)));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), BooleanBags.immutable.ofAll(BooleanHashBag.newBagWith(true, false)));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), BooleanBags.immutable.ofAll(BooleanHashBag.newBagWith(true, false, true)));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(BooleanBags.class);
    }
}
