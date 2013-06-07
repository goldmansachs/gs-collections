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

package com.gs.collections.impl.set.mutable.primitive;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.collection.mutable.primitive.AbstractMutableBooleanCollectionTestCase;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.math.MutableInteger;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractBooleanSetTestCase extends AbstractMutableBooleanCollectionTestCase
{
    private MutableBooleanSet set0;
    private MutableBooleanSet set1;
    private MutableBooleanSet set2;
    private MutableBooleanSet set3;

    @Override
    protected abstract MutableBooleanSet classUnderTest();

    @Override
    protected abstract MutableBooleanSet newWith(boolean... elements);

    @Override
    protected MutableBooleanSet newMutableCollectionWith(boolean... elements)
    {
        return BooleanHashSet.newSetWith(elements);
    }

    @Override
    protected MutableSet<Object> newObjectCollectionWith(Object... elements)
    {
        return UnifiedSet.newSetWith(elements);
    }

    @Before
    public void setup()
    {
        this.set0 = this.newWith();
        this.set1 = this.newWith(false);
        this.set2 = this.newWith(true);
        this.set3 = this.newWith(true, false);
    }

    @Override
    @Test
    public void newCollectionWith()
    {
        MutableBooleanSet set = this.classUnderTest();
        Verify.assertSize(2, set);
        Assert.assertTrue(set.containsAll(true, false, true));
    }

    @Override
    @Test
    public void isEmpty()
    {
        super.isEmpty();
        Verify.assertEmpty(this.set0);
        Verify.assertNotEmpty(this.set1);
        Verify.assertNotEmpty(this.set2);
        Verify.assertNotEmpty(this.set3);
    }

    @Override
    @Test
    public void notEmpty()
    {
        super.notEmpty();
        Assert.assertFalse(this.set0.notEmpty());
        Assert.assertTrue(this.set1.notEmpty());
        Assert.assertTrue(this.set2.notEmpty());
        Assert.assertTrue(this.set3.notEmpty());
    }

    @Override
    @Test
    public void clear()
    {
        super.clear();
        this.set0.clear();
        this.set1.clear();
        this.set2.clear();
        this.set3.clear();
        Verify.assertEmpty(this.set0);
        Verify.assertEmpty(this.set1);
        Verify.assertEmpty(this.set2);
        Verify.assertEmpty(this.set3);
        Assert.assertFalse(this.set1.contains(false));
        Assert.assertFalse(this.set2.contains(true));
        Assert.assertFalse(this.set3.contains(true));
        Assert.assertFalse(this.set3.contains(false));
    }

    @Override
    @Test
    public void contains()
    {
        super.contains();
        Assert.assertFalse(this.set0.contains(true));
        Assert.assertFalse(this.set0.contains(false));
        Assert.assertTrue(this.set1.contains(false));
        Assert.assertFalse(this.set1.contains(true));
        Assert.assertTrue(this.set2.contains(true));
        Assert.assertFalse(this.set2.contains(false));
        Assert.assertTrue(this.set3.contains(true));
        Assert.assertTrue(this.set3.contains(false));
    }

    @Override
    @Test
    public void containsAllArray()
    {
        super.containsAllArray();
        Assert.assertFalse(this.set0.containsAll(true));
        Assert.assertFalse(this.set0.containsAll(true, false));
        Assert.assertTrue(this.set1.containsAll(false, false));
        Assert.assertFalse(this.set1.containsAll(true, true));
        Assert.assertFalse(this.set1.containsAll(true, false, true));
        Assert.assertTrue(this.set2.containsAll(true, true));
        Assert.assertFalse(this.set2.containsAll(false, false));
        Assert.assertFalse(this.set2.containsAll(true, false, false));
        Assert.assertTrue(this.set3.containsAll(true, true));
        Assert.assertTrue(this.set3.containsAll(false, false));
        Assert.assertTrue(this.set3.containsAll(false, true, true));
    }

    @Override
    @Test
    public void containsAllIterable()
    {
        super.containsAllIterable();
        Assert.assertFalse(this.set0.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertFalse(this.set0.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(this.set1.containsAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertFalse(this.set1.containsAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertFalse(this.set1.containsAll(BooleanArrayList.newListWith(true, false, true)));
        Assert.assertTrue(this.set2.containsAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertFalse(this.set2.containsAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertFalse(this.set2.containsAll(BooleanArrayList.newListWith(true, false, false)));
        Assert.assertTrue(this.set3.containsAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertTrue(this.set3.containsAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertTrue(this.set3.containsAll(BooleanArrayList.newListWith(false, true, true)));
    }

    @Override
    @Test
    public void add()
    {
        Assert.assertTrue(this.set0.add(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set0);
        MutableBooleanSet set = this.newWith();
        Assert.assertTrue(set.add(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), set);
        Assert.assertFalse(this.set1.add(false));
        Assert.assertTrue(this.set1.add(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set1);
        Assert.assertFalse(this.set2.add(true));
        Assert.assertTrue(this.set2.add(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set2);
        Assert.assertFalse(this.set3.add(true));
        Assert.assertFalse(this.set3.add(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set3);
    }

    @Override
    @Test
    public void addAllArray()
    {
        Assert.assertTrue(this.set0.addAll(true, false, true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set0);
        Assert.assertFalse(this.set1.addAll(false, false));
        Assert.assertTrue(this.set1.addAll(true, false, true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set1);
        Assert.assertFalse(this.set2.addAll(true, true));
        Assert.assertTrue(this.set2.addAll(true, false, true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set2);
        Assert.assertFalse(this.set3.addAll(true, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set3);
    }

    @Override
    @Test
    public void addAllIterable()
    {
        Assert.assertTrue(this.set0.addAll(BooleanHashSet.newSetWith(true, false, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set0);
        Assert.assertFalse(this.set1.addAll(BooleanHashSet.newSetWith(false, false)));
        Assert.assertTrue(this.set1.addAll(BooleanHashSet.newSetWith(true, false, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set1);
        Assert.assertFalse(this.set2.addAll(BooleanHashSet.newSetWith(true, true)));
        Assert.assertTrue(this.set2.addAll(BooleanHashSet.newSetWith(true, false, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set2);
        Assert.assertFalse(this.set3.addAll(BooleanHashSet.newSetWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set3);
    }

    @Override
    @Test
    public void remove()
    {
        Assert.assertTrue(this.set3.remove(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set3);
        MutableBooleanSet set = this.newWith(true, false);
        Assert.assertTrue(set.remove(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), set);
        Assert.assertFalse(this.set2.remove(false));
        Assert.assertTrue(this.set2.remove(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2);
        Assert.assertFalse(this.set1.remove(true));
        Assert.assertTrue(this.set1.remove(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1);
        Assert.assertFalse(this.set0.remove(true));
        Assert.assertFalse(this.set0.remove(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set0);
    }

    @Override
    @Test
    public void removeAll()
    {
        super.removeAll();
        Assert.assertFalse(this.set0.removeAll());
        Assert.assertFalse(this.set1.removeAll());
        Assert.assertFalse(this.set2.removeAll());
        Assert.assertFalse(this.set3.removeAll());

        Assert.assertTrue(this.set3.removeAll(true, true));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set3);
        MutableBooleanSet set = this.newWith(true, false);
        Assert.assertTrue(set.removeAll(true, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), set);
        MutableBooleanSet sett = this.newWith(true, false);
        Assert.assertTrue(sett.removeAll(false, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), sett);

        Assert.assertFalse(this.set2.removeAll(false, false));
        MutableBooleanSet sett2 = this.newWith(true);
        Assert.assertTrue(sett2.removeAll(true, true));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett2);
        Assert.assertTrue(this.set2.removeAll(true, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2);

        Assert.assertFalse(this.set1.removeAll(true, true));
        MutableBooleanSet sett3 = this.newWith(false);
        Assert.assertTrue(sett3.removeAll(false, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett3);
        Assert.assertTrue(this.set1.removeAll(true, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1);

        Assert.assertFalse(this.set0.removeAll(true, true));
        Assert.assertFalse(this.set0.removeAll(true, false));
        Assert.assertFalse(this.set0.removeAll(false, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set0);
    }

    @Override
    @Test
    public void removeAllIterable()
    {
        super.removeAllIterable();
        Assert.assertFalse(this.set0.removeAll(new BooleanArrayList()));
        Assert.assertFalse(this.set1.removeAll(new BooleanArrayList()));
        Assert.assertFalse(this.set2.removeAll(new BooleanArrayList()));
        Assert.assertFalse(this.set3.removeAll(new BooleanArrayList()));

        Assert.assertTrue(this.set3.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set3);
        MutableBooleanSet set = this.newWith(true, false);
        Assert.assertTrue(set.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), set);
        MutableBooleanSet sett = this.newWith(true, false);
        Assert.assertTrue(sett.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), sett);

        Assert.assertFalse(this.set2.removeAll(BooleanArrayList.newListWith(false, false)));
        MutableBooleanSet sett2 = this.newWith(true);
        Assert.assertTrue(sett2.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett2);
        Assert.assertTrue(this.set2.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2);

        Assert.assertFalse(this.set1.removeAll(true, true));
        MutableBooleanSet sett3 = this.newWith(false);
        Assert.assertTrue(sett3.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett3);
        Assert.assertTrue(this.set1.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1);

        Assert.assertFalse(this.set0.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertFalse(this.set0.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertFalse(this.set0.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set0);
    }

    @Override
    @Test
    public void with()
    {
        super.with();
        MutableBooleanCollection emptySet = this.newWith();
        MutableBooleanCollection set = emptySet.with(false);
        MutableBooleanSet set1 = this.newWith().with(true);
        MutableBooleanSet set2 = this.newWith().with(true).with(false);
        MutableBooleanSet set3 = this.newWith().with(false).with(true);
        Assert.assertSame(emptySet, set);
        Assert.assertEquals(this.set1, set);
        Assert.assertEquals(this.set2, set1);
        Assert.assertEquals(this.set3, set2);
        Assert.assertEquals(this.set3, set3);
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set3.with(true));
    }

    @Override
    @Test
    public void withAll()
    {
        super.withAll();
        MutableBooleanCollection emptySet = this.newWith();
        MutableBooleanCollection set = emptySet.withAll(BooleanArrayList.newListWith(false));
        MutableBooleanSet set1 = this.newWith().withAll(BooleanArrayList.newListWith(true));
        MutableBooleanSet set2 = this.newWith().withAll(BooleanArrayList.newListWith(true, false));
        MutableBooleanSet set3 = this.newWith().withAll(BooleanArrayList.newListWith(true, false));
        Assert.assertSame(emptySet, set);
        Assert.assertEquals(this.set1, set);
        Assert.assertEquals(this.set2, set1);
        Assert.assertEquals(this.set3, set2);
        Assert.assertEquals(this.set3, set3);
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set3.withAll(BooleanHashSet.newSetWith(true, false)));
    }

    @Override
    @Test
    public void without()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set3.without(false));
        Assert.assertSame(this.set3, this.set3.without(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.newWith(true, false).without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set3.without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set2.without(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2.without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set1.without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1.without(false));
        Assert.assertEquals(new BooleanHashSet(), this.set0.without(true));
        Assert.assertEquals(new BooleanHashSet(), this.set0.without(false));
    }

    @Override
    @Test
    public void withoutAll()
    {
        super.withoutAll();
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set3.withoutAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertSame(this.set3, this.set3.withoutAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.newWith(true, false).withoutAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.newWith(true, false).withoutAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set3.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set2.withoutAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.newWith(true).withoutAll(BooleanArrayList.newListWith(false, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set1.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1.withoutAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.newWith(false).withoutAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(new BooleanHashSet(), this.set0.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(new BooleanHashSet(), this.set0.withoutAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(new BooleanHashSet(), this.set0.withoutAll(BooleanArrayList.newListWith(false, true)));
    }

    @Override
    @Test
    public void toArray()
    {
        super.toArray();
        Assert.assertEquals(0L, this.set0.toArray().length);

        Assert.assertEquals(1L, this.set1.toArray().length);
        Assert.assertFalse(this.set1.toArray()[0]);

        Assert.assertEquals(1L, this.set2.toArray().length);
        Assert.assertTrue(this.set2.toArray()[0]);

        Assert.assertEquals(2L, this.set3.toArray().length);
        Assert.assertTrue(Arrays.equals(new boolean[]{false, true}, this.set3.toArray())
            || Arrays.equals(new boolean[]{true, false}, this.set3.toArray()));
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();
        Assert.assertEquals(new BooleanArrayList(), this.set0.toList());
        Assert.assertEquals(BooleanArrayList.newListWith(false), this.set1.toList());
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.set2.toList());
        Assert.assertTrue(BooleanArrayList.newListWith(false, true).equals(this.set3.toList())
            || BooleanArrayList.newListWith(true, false).equals(this.set3.toList()));
    }

    @Override
    @Test
    public void toSet()
    {
        super.toSet();
        Assert.assertEquals(new BooleanHashSet(), this.set0.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set1.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set2.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(false, true), this.set3.toSet());
    }

    @Override
    @Test
    public void toBag()
    {
        Assert.assertEquals(new BooleanHashBag(), this.set0.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false), this.set1.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(true), this.set2.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false, true), this.set3.toBag());
    }

    @Override
    @Test
    public void testEquals()
    {
        Assert.assertNotEquals(this.set1, this.set0);
        Assert.assertNotEquals(this.set1, this.set2);
        Assert.assertNotEquals(this.set1, this.set3);
        Assert.assertNotEquals(this.set2, this.set0);
        Assert.assertNotEquals(this.set2, this.set3);
        Assert.assertNotEquals(this.set3, this.set0);
        Verify.assertEqualsAndHashCode(this.newWith(false, true), this.set3);
        Verify.assertEqualsAndHashCode(this.newWith(true, false), this.set3);

        Verify.assertPostSerializedEqualsAndHashCode(this.set0);
        Verify.assertPostSerializedEqualsAndHashCode(this.set1);
        Verify.assertPostSerializedEqualsAndHashCode(this.set2);
        Verify.assertPostSerializedEqualsAndHashCode(this.set3);
    }

    @Override
    @Test
    public void testHashCode()
    {
        super.testHashCode();
        Assert.assertEquals(UnifiedSet.newSet().hashCode(), this.set0.hashCode());
        Assert.assertEquals(UnifiedSet.newSetWith(false).hashCode(), this.set1.hashCode());
        Assert.assertEquals(UnifiedSet.newSetWith(true).hashCode(), this.set2.hashCode());
        Assert.assertEquals(UnifiedSet.newSetWith(true, false).hashCode(), this.set3.hashCode());
        Assert.assertEquals(UnifiedSet.newSetWith(false, true).hashCode(), this.set3.hashCode());
        Assert.assertNotEquals(UnifiedSet.newSetWith(false).hashCode(), this.set3.hashCode());
    }

    @Override
    @Test
    public void booleanIterator()
    {
        final BooleanIterator booleanIterator0 = this.set0.booleanIterator();
        Assert.assertFalse(booleanIterator0.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                booleanIterator0.next();
            }
        });

        final BooleanIterator booleanIterator1 = this.set1.booleanIterator();
        Assert.assertTrue(booleanIterator1.hasNext());
        Assert.assertFalse(booleanIterator1.next());
        Assert.assertFalse(booleanIterator1.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                booleanIterator1.next();
            }
        });

        final BooleanIterator booleanIterator2 = this.set2.booleanIterator();
        Assert.assertTrue(booleanIterator2.hasNext());
        Assert.assertTrue(booleanIterator2.next());
        Assert.assertFalse(booleanIterator2.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                booleanIterator2.next();
            }
        });

        final BooleanIterator booleanIterator3 = this.set3.booleanIterator();
        BooleanHashSet actual = new BooleanHashSet();
        Assert.assertTrue(booleanIterator3.hasNext());
        actual.add(booleanIterator3.next());
        Assert.assertTrue(booleanIterator3.hasNext());
        actual.add(booleanIterator3.next());
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), actual);
        Assert.assertFalse(booleanIterator3.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                booleanIterator3.next();
            }
        });
    }

    @Override
    @Test
    public void forEach()
    {
        final String[] sum = new String[4];
        for (int i = 0; i < sum.length; i++)
        {
            sum[i] = "";
        }
        this.set0.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += each;
            }
        });

        this.set1.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[1] += each;
            }
        });

        this.set2.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[2] += each;
            }
        });

        this.set3.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[3] += each;
            }
        });

        Assert.assertEquals("", sum[0]);
        Assert.assertEquals("false", sum[1]);
        Assert.assertEquals("true", sum[2]);
        Assert.assertTrue("truefalse".equals(sum[3]) || "falsetrue".equals(sum[3]));
    }

    @Test
    public void injectInto()
    {
        ObjectBooleanToObjectFunction<MutableInteger, MutableInteger> function = new ObjectBooleanToObjectFunction<MutableInteger, MutableInteger>()
        {
            public MutableInteger valueOf(MutableInteger object, boolean value)
            {
                return object.add(value ? 1 : 0);
            }
        };
        Assert.assertEquals(new MutableInteger(1), BooleanHashSet.newSetWith(true, false, true).injectInto(new MutableInteger(0), function));
        Assert.assertEquals(new MutableInteger(1), BooleanHashSet.newSetWith(true).injectInto(new MutableInteger(0), function));
        Assert.assertEquals(new MutableInteger(0), BooleanHashSet.newSetWith(false).injectInto(new MutableInteger(0), function));
        Assert.assertEquals(new MutableInteger(0), new BooleanHashSet().injectInto(new MutableInteger(0), function));
    }

    @Override
    @Test
    public void size()
    {
        super.size();
        Verify.assertSize(2, this.classUnderTest());
    }

    @Override
    @Test
    public void count()
    {
        Assert.assertEquals(0L, this.set0.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(0L, this.set1.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(1L, this.set1.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(0L, this.set2.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(1L, this.set3.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(0L, this.set3.count(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
        Assert.assertEquals(1L, this.set3.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(1L, this.set3.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(2L, this.set3.count(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        super.anySatisfy();
        Assert.assertFalse(this.set0.anySatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.set1.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set1.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set2.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.set2.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set3.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set3.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set3.anySatisfy(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

    @Override
    @Test
    public void allSatisfy()
    {
        super.allSatisfy();
        Assert.assertTrue(this.set0.allSatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.set1.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set1.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set2.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.set2.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.set3.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.set3.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set3.allSatisfy(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
        Assert.assertTrue(this.set3.allSatisfy(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

    @Override
    @Test
    public void noneSatisfy()
    {
        Assert.assertFalse(this.set0.noneSatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.set1.noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.set1.noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.set2.noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set2.noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set3.noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.set3.noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.set3.noneSatisfy(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
        Assert.assertFalse(this.set3.noneSatisfy(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

    @Override
    @Test
    public void select()
    {
        Verify.assertEmpty(this.set0.select(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Verify.assertEmpty(this.set1.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, this.set1.select(BooleanPredicates.isFalse()));
        Verify.assertEmpty(this.set2.select(BooleanPredicates.isFalse()));
        Verify.assertSize(1, this.set2.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, this.set3.select(BooleanPredicates.isFalse()));
        Verify.assertSize(1, this.set3.select(BooleanPredicates.isTrue()));
        Verify.assertEmpty(this.set3.select(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
        Verify.assertSize(2, this.set3.select(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

    @Override
    @Test
    public void reject()
    {
        Verify.assertEmpty(this.set0.reject(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Verify.assertEmpty(this.set2.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(1, this.set2.reject(BooleanPredicates.isFalse()));
        Verify.assertEmpty(this.set1.reject(BooleanPredicates.isFalse()));
        Verify.assertSize(1, this.set1.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(1, this.set3.reject(BooleanPredicates.isFalse()));
        Verify.assertSize(1, this.set3.reject(BooleanPredicates.isTrue()));
        Verify.assertEmpty(this.set3.reject(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
        Verify.assertSize(2, this.set3.reject(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

    @Override
    @Test
    public void detectIfNone()
    {
        super.detectIfNone();
        Assert.assertTrue(this.set0.detectIfNone(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), true));
        Assert.assertFalse(this.set0.detectIfNone(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), false));
        Assert.assertTrue(this.set1.detectIfNone(BooleanPredicates.isTrue(), true));
        Assert.assertFalse(this.set1.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(this.set1.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(this.set1.detectIfNone(BooleanPredicates.isFalse(), false));
        Assert.assertTrue(this.set2.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(this.set2.detectIfNone(BooleanPredicates.isFalse(), false));
        Assert.assertTrue(this.set2.detectIfNone(BooleanPredicates.isTrue(), true));
        Assert.assertTrue(this.set2.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertTrue(this.set3.detectIfNone(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue()), true));
        Assert.assertFalse(this.set3.detectIfNone(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue()), false));
        Assert.assertFalse(this.set3.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(this.set3.detectIfNone(BooleanPredicates.isTrue(), false));
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();
        BooleanToObjectFunction<Boolean> function = new BooleanToObjectFunction<Boolean>()
        {
            public Boolean valueOf(boolean parameter)
            {
                return !parameter;
            }
        };
        Assert.assertEquals(UnifiedSet.newSetWith(true, false), this.set3.collect(function));
        Assert.assertEquals(UnifiedSet.newSetWith(false), this.set2.collect(function));
        Assert.assertEquals(UnifiedSet.newSetWith(true), this.set1.collect(function));
        Assert.assertEquals(UnifiedSet.newSetWith(), this.set0.collect(function));
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();
        Assert.assertEquals("[]", this.set0.toString());
        Assert.assertEquals("[false]", this.set1.toString());
        Assert.assertEquals("[true]", this.set2.toString());
        Assert.assertTrue("[true, false]".equals(this.set3.toString())
            || "[false, true]".equals(this.set3.toString()));

    }

    @Override
    @Test
    public void makeString()
    {
        super.makeString();
        Assert.assertEquals("", this.set0.makeString());
        Assert.assertEquals("false", this.set1.makeString());
        Assert.assertEquals("true", this.set2.makeString());
        Assert.assertTrue("true, false".equals(this.set3.makeString())
            || "false, true".equals(this.set3.makeString()));

        Assert.assertEquals("", this.set0.makeString("/"));
        Assert.assertEquals("false", this.set1.makeString("/"));
        Assert.assertEquals("true", this.set2.makeString("/"));
        Assert.assertTrue(this.set3.makeString("/"), "true/false".equals(this.set3.makeString("/"))
            || "false/true".equals(this.set3.makeString("/")));

        Assert.assertEquals("[]", this.set0.makeString("[", "/", "]"));
        Assert.assertEquals("[false]", this.set1.makeString("[", "/", "]"));
        Assert.assertEquals("[true]", this.set2.makeString("[", "/", "]"));
        Assert.assertTrue(this.set3.makeString("[", "/", "]"), "[true/false]".equals(this.set3.makeString("[", "/", "]"))
            || "[false/true]".equals(this.set3.makeString("[", "/", "]")));
    }

    @Override
    @Test
    public void appendString()
    {
        super.appendString();
        StringBuilder appendable = new StringBuilder();
        this.set0.appendString(appendable);
        Assert.assertEquals("", appendable.toString());

        StringBuilder appendable1 = new StringBuilder();
        this.set1.appendString(appendable1);
        Assert.assertEquals("false", appendable1.toString());

        StringBuilder appendable2 = new StringBuilder();
        this.set2.appendString(appendable2);
        Assert.assertEquals("true", appendable2.toString());

        StringBuilder appendable3 = new StringBuilder();
        this.set3.appendString(appendable3);
        Assert.assertTrue("true, false".equals(appendable3.toString())
            || "false, true".equals(appendable3.toString()));

        StringBuilder appendable4 = new StringBuilder();
        this.set3.appendString(appendable4, "[", ", ", "]");
        Assert.assertTrue("[true, false]".equals(appendable4.toString())
            || "[false, true]".equals(appendable4.toString()));
    }

    @Override
    @Test
    public void asLazy()
    {
        super.asLazy();
        Verify.assertInstanceOf(LazyBooleanIterable.class, this.set0.asLazy());
        Assert.assertEquals(this.set0, this.set0.asLazy().toSet());
        Assert.assertEquals(this.set1, this.set1.asLazy().toSet());
        Assert.assertEquals(this.set2, this.set2.asLazy().toSet());
        Assert.assertEquals(this.set3, this.set3.asLazy().toSet());
    }

    @Override
    @Test
    public void asSynchronized()
    {
        super.asSynchronized();
        Verify.assertInstanceOf(SynchronizedBooleanSet.class, this.set0.asSynchronized());
        Assert.assertEquals(new SynchronizedBooleanSet(this.set0), this.set0.asSynchronized());
        Assert.assertEquals(new SynchronizedBooleanSet(this.set1), this.set1.asSynchronized());
        Assert.assertEquals(new SynchronizedBooleanSet(this.set2), this.set2.asSynchronized());
        Assert.assertEquals(new SynchronizedBooleanSet(this.set3), this.set3.asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        super.asUnmodifiable();
        Verify.assertInstanceOf(UnmodifiableBooleanSet.class, this.set0.asUnmodifiable());
        Assert.assertEquals(new UnmodifiableBooleanSet(this.set0), this.set0.asUnmodifiable());
        Assert.assertEquals(new UnmodifiableBooleanSet(this.set1), this.set1.asUnmodifiable());
        Assert.assertEquals(new UnmodifiableBooleanSet(this.set2), this.set2.asUnmodifiable());
        Assert.assertEquals(new UnmodifiableBooleanSet(this.set3), this.set3.asUnmodifiable());
    }

}
