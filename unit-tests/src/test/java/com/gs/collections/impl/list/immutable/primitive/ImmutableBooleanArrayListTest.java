package com.gs.collections.impl.list.immutable.primitive;

import com.gs.collections.api.list.primitive.ImmutableBooleanList;
import com.gs.collections.impl.factory.primitive.BooleanLists;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableBooleanArrayList}.
 */
public class ImmutableBooleanArrayListTest extends AbstractImmutableBooleanListTestCase
{
    @Override
    protected ImmutableBooleanList classUnderTest()
    {
        return ImmutableBooleanArrayList.newListWith(true, false, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newCollection_throws_empty()
    {
        ImmutableBooleanArrayList.newListWith();
    }

    @Test(expected = IllegalArgumentException.class)
    public void newCollection_throws_single()
    {
        ImmutableBooleanArrayList.newListWith(true);
    }

    @Override
    @Test
    public void newCollection()
    {
        super.newCollection();
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true), ImmutableBooleanArrayList.newList(BooleanArrayList.newListWith(true, false, true)));
    }

    @Override
    @Test
    public void size()
    {
        super.size();
        Verify.assertSize(3, ImmutableBooleanArrayList.newList(BooleanArrayList.newListWith(true, false, true)));
        Verify.assertSize(3, BooleanLists.immutable.ofAll(ImmutableBooleanArrayList.newList(BooleanArrayList.newListWith(true, false, true))));
    }
}
