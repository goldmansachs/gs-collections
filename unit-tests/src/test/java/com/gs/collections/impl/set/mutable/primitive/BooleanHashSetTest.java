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

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class BooleanHashSetTest
{
    private final BooleanHashSet set0 = new BooleanHashSet();
    private final BooleanHashSet set1 = BooleanHashSet.newSetWith(false);
    private final BooleanHashSet set2 = BooleanHashSet.newSetWith(true);
    private final BooleanHashSet set3 = BooleanHashSet.newSetWith(true, false);

    @Test
    public void construction() throws Exception
    {
        BooleanHashSet set = new BooleanHashSet();
        Field table = BooleanHashSet.class.getDeclaredField("state");
        table.setAccessible(true);
        Assert.assertEquals(0, table.get(set));
    }

    @Test
    public void newSet()
    {
        BooleanHashSet setFromList = BooleanHashSet.newSet(BooleanArrayList.newListWith(true, true, false));
        BooleanHashSet setFromSet0 = BooleanHashSet.newSet(this.set0);
        BooleanHashSet setFromSet1 = BooleanHashSet.newSet(this.set1);
        BooleanHashSet setFromSet2 = BooleanHashSet.newSet(this.set2);
        BooleanHashSet setFromSet3 = BooleanHashSet.newSet(this.set3);
        Assert.assertEquals(this.set3, setFromList);
        Assert.assertEquals(this.set0, setFromSet0);
        Assert.assertEquals(this.set1, setFromSet1);
        Assert.assertEquals(this.set2, setFromSet2);
        Assert.assertEquals(this.set3, setFromSet3);
    }

    @Test
    public void size()
    {
        Verify.assertSize(0, new BooleanHashSet());
        Verify.assertSize(0, this.set0);
        Verify.assertSize(1, this.set1);
        Verify.assertSize(1, this.set2);
        Verify.assertSize(2, this.set3);
    }

    @Test
    public void empty()
    {
        Verify.assertEmpty(this.set0);
        Assert.assertFalse(this.set0.notEmpty());
        Verify.assertNotEmpty(this.set1);
        Assert.assertTrue(this.set1.notEmpty());
        Verify.assertNotEmpty(this.set2);
        Assert.assertTrue(this.set2.notEmpty());
        Verify.assertNotEmpty(this.set3);
        Assert.assertTrue(this.set3.notEmpty());
    }

    @Test
    public void clear()
    {
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

    @Test
    public void contains()
    {
        Assert.assertFalse(this.set0.contains(true));
        Assert.assertFalse(this.set0.contains(false));
        Assert.assertTrue(this.set1.contains(false));
        Assert.assertFalse(this.set1.contains(true));
        Assert.assertTrue(this.set2.contains(true));
        Assert.assertFalse(this.set2.contains(false));
        Assert.assertTrue(this.set3.contains(true));
        Assert.assertTrue(this.set3.contains(false));
    }

    @Test
    public void containsAll()
    {
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

    @Test
    public void add()
    {
        Assert.assertTrue(this.set0.add(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set0);
        BooleanHashSet set = new BooleanHashSet();
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

    @Test
    public void remove()
    {
        Assert.assertTrue(this.set3.remove(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set3);
        BooleanHashSet set = BooleanHashSet.newSetWith(true, false);
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

    @Test
    public void removeAll()
    {
        Assert.assertFalse(this.set0.removeAll());
        Assert.assertFalse(this.set1.removeAll());
        Assert.assertFalse(this.set2.removeAll());
        Assert.assertFalse(this.set3.removeAll());

        Assert.assertTrue(this.set3.removeAll(true, true));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set3);
        BooleanHashSet set = BooleanHashSet.newSetWith(true, false);
        Assert.assertTrue(set.removeAll(true, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), set);
        BooleanHashSet sett = BooleanHashSet.newSetWith(true, false);
        Assert.assertTrue(sett.removeAll(false, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), sett);

        Assert.assertFalse(this.set2.removeAll(false, false));
        BooleanHashSet sett2 = BooleanHashSet.newSetWith(true);
        Assert.assertTrue(sett2.removeAll(true, true));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett2);
        Assert.assertTrue(this.set2.removeAll(true, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2);

        Assert.assertFalse(this.set1.removeAll(true, true));
        BooleanHashSet sett3 = BooleanHashSet.newSetWith(false);
        Assert.assertTrue(sett3.removeAll(false, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett3);
        Assert.assertTrue(this.set1.removeAll(true, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1);

        Assert.assertFalse(this.set0.removeAll(true, true));
        Assert.assertFalse(this.set0.removeAll(true, false));
        Assert.assertFalse(this.set0.removeAll(false, false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set0);
    }

    @Test
    public void removeAllIterable()
    {
        Assert.assertFalse(this.set0.removeAll(new BooleanArrayList()));
        Assert.assertFalse(this.set1.removeAll(new BooleanArrayList()));
        Assert.assertFalse(this.set2.removeAll(new BooleanArrayList()));
        Assert.assertFalse(this.set3.removeAll(new BooleanArrayList()));

        Assert.assertTrue(this.set3.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set3);
        BooleanHashSet set = BooleanHashSet.newSetWith(true, false);
        Assert.assertTrue(set.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), set);
        BooleanHashSet sett = BooleanHashSet.newSetWith(true, false);
        Assert.assertTrue(sett.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), sett);

        Assert.assertFalse(this.set2.removeAll(BooleanArrayList.newListWith(false, false)));
        BooleanHashSet sett2 = BooleanHashSet.newSetWith(true);
        Assert.assertTrue(sett2.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett2);
        Assert.assertTrue(this.set2.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2);

        Assert.assertFalse(this.set1.removeAll(true, true));
        BooleanHashSet sett3 = BooleanHashSet.newSetWith(false);
        Assert.assertTrue(sett3.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), sett3);
        Assert.assertTrue(this.set1.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1);

        Assert.assertFalse(this.set0.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertFalse(this.set0.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertFalse(this.set0.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set0);
    }

    @Test
    public void with()
    {
        BooleanHashSet hashSet = new BooleanHashSet().with(false);
        BooleanHashSet hashSet1 = new BooleanHashSet().with(true);
        BooleanHashSet hashSet2 = new BooleanHashSet().with(true).with(false);
        BooleanHashSet hashSet3 = new BooleanHashSet().with(false).with(true);
        Assert.assertEquals(this.set1, hashSet);
        Assert.assertEquals(this.set2, hashSet1);
        Assert.assertEquals(this.set3, hashSet2);
        Assert.assertEquals(this.set3, hashSet3);
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set3.with(true));
    }

    @Test
    public void withAll()
    {
        BooleanHashSet hashSet = new BooleanHashSet().withAll(BooleanArrayList.newListWith(false));
        BooleanHashSet hashSet1 = new BooleanHashSet().withAll(BooleanArrayList.newListWith(true));
        BooleanHashSet hashSet2 = new BooleanHashSet().withAll(BooleanArrayList.newListWith(true, false));
        BooleanHashSet hashSet3 = new BooleanHashSet().withAll(BooleanArrayList.newListWith(true, false));
        Assert.assertEquals(this.set1, hashSet);
        Assert.assertEquals(this.set2, hashSet1);
        Assert.assertEquals(this.set3, hashSet2);
        Assert.assertEquals(this.set3, hashSet3);
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.set3.withAll(BooleanHashSet.newSetWith(true, false)));
    }

    @Test
    public void without()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set3.without(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), BooleanHashSet.newSetWith(true, false).without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set3.without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set2.without(false));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2.without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set1.without(true));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1.without(false));
        Assert.assertEquals(new BooleanHashSet(), this.set0.without(true));
        Assert.assertEquals(new BooleanHashSet(), this.set0.without(false));
    }

    @Test
    public void withoutAll()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set3.withoutAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), BooleanHashSet.newSetWith(true, false).withoutAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), BooleanHashSet.newSetWith(true, false).withoutAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set3.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.set2.withoutAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set2.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), BooleanHashSet.newSetWith(true).withoutAll(BooleanArrayList.newListWith(false, true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.set1.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), this.set1.withoutAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(BooleanHashSet.newSetWith(), BooleanHashSet.newSetWith(false).withoutAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(new BooleanHashSet(), this.set0.withoutAll(BooleanArrayList.newListWith(true)));
        Assert.assertEquals(new BooleanHashSet(), this.set0.withoutAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(new BooleanHashSet(), this.set0.withoutAll(BooleanArrayList.newListWith(false, true)));
    }

    @Test
    public void toArray()
    {
        Assert.assertEquals(0L, this.set0.toArray().length);

        Assert.assertEquals(1L, this.set1.toArray().length);
        Assert.assertFalse(this.set1.toArray()[0]);

        Assert.assertEquals(1L, this.set2.toArray().length);
        Assert.assertTrue(this.set2.toArray()[0]);

        Assert.assertEquals(2L, this.set3.toArray().length);
        Assert.assertTrue(this.set3.toArray()[0]);
        Assert.assertFalse(this.set3.toArray()[1]);
    }

    @Test
    public void testEquals()
    {
        Verify.assertNotEquals(this.set1, this.set0);
        Verify.assertNotEquals(this.set1, this.set2);
        Verify.assertNotEquals(this.set1, this.set3);
        Verify.assertNotEquals(this.set2, this.set0);
        Verify.assertNotEquals(this.set2, this.set3);
        Verify.assertNotEquals(this.set3, this.set0);
        Verify.assertEqualsAndHashCode(BooleanHashSet.newSetWith(false, true), this.set3);
        Verify.assertEqualsAndHashCode(BooleanHashSet.newSetWith(true, false), this.set3);

        Verify.assertPostSerializedEqualsAndHashCode(this.set0);
        Verify.assertPostSerializedEqualsAndHashCode(this.set1);
        Verify.assertPostSerializedEqualsAndHashCode(this.set2);
        Verify.assertPostSerializedEqualsAndHashCode(this.set3);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(UnifiedSet.newSet().hashCode(), this.set0.hashCode());
        Assert.assertEquals(UnifiedSet.newSetWith(false).hashCode(), this.set1.hashCode());
        Assert.assertEquals(UnifiedSet.newSetWith(true).hashCode(), this.set2.hashCode());
        Assert.assertEquals(UnifiedSet.newSetWith(true, false).hashCode(), this.set3.hashCode());
    }

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
        Assert.assertTrue(booleanIterator3.hasNext());
        Assert.assertTrue(booleanIterator3.next());
        Assert.assertTrue(booleanIterator3.hasNext());
        Assert.assertFalse(booleanIterator3.next());
        Assert.assertFalse(booleanIterator3.hasNext());
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                booleanIterator3.next();
            }
        });
    }

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
        Assert.assertEquals("truefalse", sum[3]);
    }

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

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.set0.anySatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.set1.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set1.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set2.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.set2.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set3.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set3.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set3.anySatisfy(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(this.set0.allSatisfy(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertFalse(this.set1.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.set1.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set2.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.set2.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.set3.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.set3.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.set3.allSatisfy(BooleanPredicates.and(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
        Assert.assertTrue(this.set3.allSatisfy(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.isTrue())));
    }

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

    @Test
    public void detectIfNone()
    {
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
        Assert.assertTrue(this.set3.detectIfNone(BooleanPredicates.or(BooleanPredicates.isFalse(), BooleanPredicates.isTrue()), false));
        Assert.assertFalse(this.set3.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(this.set3.detectIfNone(BooleanPredicates.isTrue(), false));
    }

    @Test
    public void collect()
    {
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

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", this.set0.toString());
        Assert.assertEquals("[false]", this.set1.toString());
        Assert.assertEquals("[true]", this.set2.toString());
        Assert.assertTrue("[true, false]".equals(this.set3.toString())
                || "[false, true]".equals(this.set3.toString()));

    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("", this.set0.makeString());
        Assert.assertEquals("false", this.set1.makeString());
        Assert.assertEquals("true", this.set2.makeString());
        Assert.assertEquals("true, false", this.set3.makeString());

        Assert.assertEquals("", this.set0.makeString("/"));
        Assert.assertEquals("false", this.set1.makeString("/"));
        Assert.assertEquals("true", this.set2.makeString("/"));
        Assert.assertEquals("true/false", this.set3.makeString("/"));

        Assert.assertEquals("[]", this.set0.makeString("[", "/", "]"));
        Assert.assertEquals("[false]", this.set1.makeString("[", "/", "]"));
        Assert.assertEquals("[true]", this.set2.makeString("[", "/", "]"));
        Assert.assertEquals("[true/false]", this.set3.makeString("[", "/", "]"));
    }

    @Test
    public void appendString()
    {
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
        Assert.assertEquals("true, false", appendable3.toString());

        StringBuilder appendable4 = new StringBuilder();
        this.set3.appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(this.set3.toString(), appendable4.toString());
    }
}
