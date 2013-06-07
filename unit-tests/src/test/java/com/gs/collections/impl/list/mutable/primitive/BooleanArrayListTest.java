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

package com.gs.collections.impl.list.mutable.primitive;

import java.lang.reflect.Field;
import java.util.BitSet;

import com.gs.collections.api.block.function.primitive.ObjectBooleanIntToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanIntProcedure;
import com.gs.collections.impl.math.MutableInteger;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BooleanArrayList}.
 */
public class BooleanArrayListTest extends AbstractBooleanListTestCase
{
    private final BooleanArrayList list = this.classUnderTest();

    @Override
    protected final BooleanArrayList classUnderTest()
    {
        return BooleanArrayList.newListWith(true, false, true);
    }

    @Override
    protected BooleanArrayList newWith(boolean... elements)
    {
        return BooleanArrayList.newListWith(elements);
    }

    @Test
    public void testBooleanArrayListWithInitialCapacity() throws Exception
    {
        BooleanArrayList arrayList = new BooleanArrayList(7);
        Verify.assertEmpty(arrayList);
        Field items = BooleanArrayList.class.getDeclaredField("items");
        items.setAccessible(true);
        Assert.assertEquals(64L, ((BitSet) items.get(arrayList)).size());
        BooleanArrayList arrayList1 = new BooleanArrayList(64);
        Assert.assertEquals(64L, ((BitSet) items.get(arrayList1)).size());
        BooleanArrayList arrayList2 = new BooleanArrayList(65);
        Assert.assertEquals(128L, ((BitSet) items.get(arrayList2)).size());
    }

    @Test
    public void addAtIndexAtCapacity() throws Exception
    {
        BooleanArrayList listWithCapacity = new BooleanArrayList(64);
        for (int i = 0; i < 64; i++)
        {
            listWithCapacity.add((i & 1) == 0);
        }
        listWithCapacity.addAtIndex(64, true);
        Field items = BooleanArrayList.class.getDeclaredField("items");
        items.setAccessible(true);
        Assert.assertEquals(128L, ((BitSet) items.get(listWithCapacity)).size());
    }

    @Override
    @Test
    public void size()
    {
        super.size();
        Verify.assertSize(0, new BooleanArrayList());
        Verify.assertSize(0, new BooleanArrayList(1));
        Verify.assertSize(1, BooleanArrayList.newListWith(false));
        Verify.assertSize(3, this.list);
        Verify.assertSize(3, BooleanArrayList.newList(this.list));
    }

    @Override
    @Test
    public void with()
    {
        super.with();
        BooleanArrayList emptyList = new BooleanArrayList();
        BooleanArrayList arrayList = emptyList.with(true);
        BooleanArrayList arrayList0 = new BooleanArrayList().with(false, false);
        BooleanArrayList arrayList1 = new BooleanArrayList().with(true, true, false);
        BooleanArrayList arrayList2 = new BooleanArrayList().with(true, true, false, true);
        BooleanArrayList arrayList3 = new BooleanArrayList().with(true, true, false, true, false);
        Assert.assertSame(emptyList, arrayList);
        Assert.assertEquals(BooleanArrayList.newListWith(true), arrayList);
        Assert.assertEquals(BooleanArrayList.newListWith(false, false), arrayList0);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false), arrayList1);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, true), arrayList2);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, true, false), arrayList3);
    }

    @Test
    public void injectIntoWithIndex()
    {
        BooleanArrayList list = BooleanArrayList.newListWith(true, false, true);
        MutableInteger result = list.injectIntoWithIndex(new MutableInteger(0), new ObjectBooleanIntToObjectFunction<MutableInteger, MutableInteger>()
        {
            public MutableInteger valueOf(MutableInteger object, boolean value, int index)
            {
                return object.add((value ? 1 : 0) + index);
            }
        });
        Assert.assertEquals(new MutableInteger(5), result);
    }

    @Test
    public void injectInto()
    {
        BooleanArrayList list = BooleanArrayList.newListWith(true, false, true);
        MutableInteger result = list.injectInto(new MutableInteger(0), new ObjectBooleanToObjectFunction<MutableInteger, MutableInteger>()
        {
            public MutableInteger valueOf(MutableInteger object, boolean value)
            {
                return object.add(value ? 1 : 0);
            }
        });
        Assert.assertEquals(new MutableInteger(2), result);
    }

    @Test
    public void forEachWithIndex()
    {
        final String[] sum = new String[2];
        sum[0] = "";
        sum[1] = "";
        this.list.forEachWithIndex(new BooleanIntProcedure()
        {
            public void value(boolean each, int index)
            {
                sum[0] += index + ":" + each;
            }
        });
        this.newWith().forEachWithIndex(new BooleanIntProcedure()
        {
            public void value(boolean each, int index)
            {
                sum[1] += index + ":" + each;
            }
        });
        Assert.assertEquals("0:true1:false2:true", sum[0]);
        Assert.assertEquals("", sum[1]);
    }

}
