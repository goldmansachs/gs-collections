package com.gs.collections.impl.list.immutable.primitive;

import com.gs.collections.api.list.primitive.ImmutableBooleanList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableBooleanSingletonListTest extends AbstractImmutableBooleanListTestCase
{
    @Override
    protected ImmutableBooleanList classUnderTest()
    {
        return BooleanArrayList.newListWith(true).toImmutable();
    }

    @Override
    @Test
    public void testEquals()
    {
        super.testEquals();
        Assert.assertNotEquals(this.newWith(true), this.newWith());
    }
}
