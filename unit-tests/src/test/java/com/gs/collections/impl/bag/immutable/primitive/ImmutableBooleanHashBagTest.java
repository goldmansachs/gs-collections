package com.gs.collections.impl.bag.immutable.primitive;

import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;

public class ImmutableBooleanHashBagTest extends AbstractImmutableBooleanBagTestCase
{
    @Override
    protected ImmutableBooleanBag classUnderTest()
    {
        return ImmutableBooleanHashBag.newBagWith(true, false, true);
    }
}
