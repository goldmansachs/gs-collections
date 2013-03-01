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

package com.gs.collections.impl.bag.mutable.primitive;

import java.util.NoSuchElementException;

import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BooleanHashBag}.
 */
public class BooleanHashBagTest
{
    @Test
    public void empty()
    {
        Assert.assertTrue(new BooleanHashBag().isEmpty());
        Assert.assertFalse(new BooleanHashBag().notEmpty());
        Assert.assertFalse(BooleanHashBag.newBagWith(true, false, true).isEmpty());
        Assert.assertTrue(BooleanHashBag.newBagWith(true, false, true).notEmpty());
    }

    @Test
    public void newBag()
    {
        Assert.assertEquals(
                BooleanHashBag.newBagWith(true, false, true, false, true),
                BooleanHashBag.newBag(BooleanArrayList.newListWith(true, false, true, false, true)));
    }

    @Test
    public void size()
    {
        Verify.assertSize(0, new BooleanHashBag());
        Verify.assertSize(3, BooleanHashBag.newBagWith(true, false, true));
    }

    @Test
    public void sizeDistinct()
    {
        Assert.assertEquals(0, new BooleanHashBag().sizeDistinct());
        Assert.assertEquals(1, BooleanHashBag.newBagWith(true).sizeDistinct());
        Assert.assertEquals(1, BooleanHashBag.newBagWith(true, true, true).sizeDistinct());
        Assert.assertEquals(2, BooleanHashBag.newBagWith(true, false, true, false, true).sizeDistinct());
    }

    @Test
    public void add()
    {
        BooleanHashBag bag = new BooleanHashBag();
        Assert.assertTrue(bag.add(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), bag);
        Assert.assertTrue(bag.add(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), bag);
        Assert.assertTrue(bag.add(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), bag);
        Assert.assertTrue(bag.add(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false), bag);
    }

    @Test
    public void addOccurrences()
    {
        BooleanHashBag bag = new BooleanHashBag();
        bag.addOccurrences(false, 3);
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false), bag);
        bag.addOccurrences(false, 2);
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false, false, false), bag);
        bag.addOccurrences(true, 0);
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false, false, false), bag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOccurrences_throws()
    {
        new BooleanHashBag().addOccurrences(true, -1);
    }

    @Test
    public void remove()
    {
        BooleanHashBag bag1 = new BooleanHashBag();
        Assert.assertFalse(bag1.remove(false));
        Assert.assertEquals(new BooleanHashBag(), bag1);
        Assert.assertTrue(bag1.add(false));
        Assert.assertTrue(bag1.add(false));
        Assert.assertTrue(bag1.remove(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(false), bag1);
        Assert.assertTrue(bag1.remove(false));
        Assert.assertEquals(new BooleanHashBag(), bag1);

        BooleanHashBag bag2 = new BooleanHashBag();
        Assert.assertFalse(bag2.remove(true));
        Assert.assertEquals(new BooleanHashBag(), bag2);
        Assert.assertTrue(bag2.add(true));
        Assert.assertTrue(bag2.add(true));
        Assert.assertTrue(bag2.remove(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), bag2);
        Assert.assertTrue(bag2.remove(true));
        Assert.assertEquals(new BooleanHashBag(), bag2);
    }

    @Test
    public void removeOccurrences()
    {
        BooleanHashBag bag1 = new BooleanHashBag();
        bag1.addOccurrences(true, 5);
        Assert.assertTrue(bag1.removeOccurrences(true, 2));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, true), bag1);
        Assert.assertFalse(bag1.removeOccurrences(true, 0));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, true), bag1);
        Assert.assertTrue(bag1.removeOccurrences(true, 5));
        Assert.assertEquals(new BooleanHashBag(), bag1);
        Assert.assertFalse(bag1.removeOccurrences(true, 5));
        Assert.assertEquals(new BooleanHashBag(), bag1);

        BooleanHashBag bag2 = new BooleanHashBag();
        bag2.addOccurrences(false, 5);
        Assert.assertTrue(bag2.removeOccurrences(false, 2));
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false), bag2);
        Assert.assertFalse(bag2.removeOccurrences(false, 0));
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false), bag2);
        Assert.assertTrue(bag2.removeOccurrences(false, 5));
        Assert.assertEquals(new BooleanHashBag(), bag2);
        Assert.assertFalse(bag2.removeOccurrences(false, 5));
        Assert.assertEquals(new BooleanHashBag(), bag2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeOccurrences_throws()
    {
        new BooleanHashBag().removeOccurrences(true, -1);
    }

    @Test
    public void clear()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, false, true, false, true);
        bag.clear();
        Assert.assertEquals(new BooleanHashBag(), bag);
    }

    @Test
    public void addAll()
    {
        BooleanHashBag bag = new BooleanHashBag();
        Assert.assertTrue(bag.addAll(BooleanArrayList.newListWith(true, false, true, false, true)));
        Assert.assertFalse(bag.addAll(new BooleanArrayList()));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true), bag);
        Assert.assertTrue(bag.addAll(BooleanHashBag.newBagWith(true, false, true, false, true)));
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false, false, true, true, true, true, true, true), bag);
    }

    @Test
    public void removeAll()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, false, true, false, true);
        Assert.assertFalse(bag.removeAll());
        Assert.assertTrue(bag.removeAll(true, true));
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false), bag);

        BooleanHashBag bag2 = BooleanHashBag.newBagWith(true, false, true, false, true);
        Assert.assertFalse(bag2.removeAll());
        Assert.assertTrue(bag2.removeAll(true, false));
        Assert.assertEquals(new BooleanHashBag(), bag2);
    }

    @Test
    public void removeAll_iterable()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, false, true, false, true);
        Assert.assertFalse(bag.removeAll(new BooleanArrayList()));
        Assert.assertTrue(bag.removeAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false), bag);
        Assert.assertTrue(bag.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(new BooleanHashBag(), bag);

        BooleanHashBag bag2 = BooleanHashBag.newBagWith(true, false, true, false, true);
        Assert.assertTrue(bag2.removeAll(BooleanHashBag.newBagWith(true, false)));
        Assert.assertEquals(new BooleanHashBag(), bag2);
    }

    @Test
    public void with()
    {
        BooleanHashBag hashBag = new BooleanHashBag().with(true);
        BooleanHashBag hashBag0 = new BooleanHashBag().with(true, false);
        BooleanHashBag hashBag1 = new BooleanHashBag().with(true, false, true);
        BooleanHashBag hashBag2 = new BooleanHashBag().with(true).with(false).with(true).with(false);
        BooleanHashBag hashBag3 = new BooleanHashBag().with(true).with(false).with(true).with(false).with(true);
        Assert.assertEquals(BooleanHashBag.newBagWith(true), hashBag);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), hashBag0);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), hashBag1);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false), hashBag2);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true), hashBag3);
    }

    @Test
    public void withAll()
    {
        BooleanHashBag hashBag = new BooleanHashBag().withAll(BooleanHashBag.newBagWith(true));
        BooleanHashBag hashBag0 = new BooleanHashBag().withAll(BooleanHashBag.newBagWith(true, false));
        BooleanHashBag hashBag1 = new BooleanHashBag().withAll(BooleanHashBag.newBagWith(true, false, true));
        BooleanHashBag hashBag2 = new BooleanHashBag().withAll(BooleanArrayList.newListWith(true, false, true, false));
        BooleanHashBag hashBag3 = new BooleanHashBag().withAll(BooleanArrayList.newListWith(true, false, true, false, true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), hashBag);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), hashBag0);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), hashBag1);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false), hashBag2);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true), hashBag3);
    }

    @Test
    public void without()
    {
        BooleanHashBag mainHashBag = BooleanHashBag.newBagWith(true, false, true, false, true);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, false, true), mainHashBag.without(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), mainHashBag.without(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true), mainHashBag.without(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), mainHashBag.without(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), mainHashBag.without(false));
        Assert.assertEquals(new BooleanHashBag(), mainHashBag.without(true));
        Assert.assertEquals(new BooleanHashBag(), mainHashBag.without(false));
    }

    @Test
    public void withoutAll()
    {
        BooleanHashBag mainHashBag = BooleanHashBag.newBagWith(true, false, true, false, true);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, true), mainHashBag.withoutAll(BooleanHashBag.newBagWith(false, false)));
        Assert.assertEquals(new BooleanHashBag(), mainHashBag.withoutAll(BooleanHashBag.newBagWith(true, false)));
        Assert.assertEquals(new BooleanHashBag(), mainHashBag.withoutAll(BooleanHashBag.newBagWith(true, false)));
    }

    @Test
    public void contains()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, true, false, false, false);
        Assert.assertTrue(bag.contains(true));
        Assert.assertTrue(bag.contains(false));
        Assert.assertFalse(new BooleanHashBag().contains(true));
        Assert.assertFalse(new BooleanHashBag().contains(false));
    }

    @Test
    public void containsAll()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, true, false, false, false);
        Assert.assertTrue(bag.containsAll(true, false));
        Assert.assertTrue(bag.containsAll(true, true));
        Assert.assertTrue(bag.containsAll(false, false));
        Assert.assertFalse(BooleanHashBag.newBagWith(true, true).containsAll(false, true, false));
        Assert.assertFalse(new BooleanHashBag().containsAll(false, true, false));
    }

    @Test
    public void containsAll_iterable()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, true, false, false, false);
        Assert.assertTrue(bag.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(bag.containsAll(BooleanArrayList.newListWith(true, true)));
        Assert.assertTrue(bag.containsAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertFalse(BooleanHashBag.newBagWith(true, true).containsAll(BooleanArrayList.newListWith(false, true, false)));
        Assert.assertFalse(new BooleanHashBag().containsAll(BooleanArrayList.newListWith(false, true, false)));
    }

    @Test
    public void testEqualsAndHashCode()
    {
        Verify.assertPostSerializedEqualsAndHashCode(new BooleanHashBag());
        Verify.assertPostSerializedEqualsAndHashCode(BooleanHashBag.newBagWith(true, true, false, false, false));
        Assert.assertNotEquals(BooleanHashBag.newBagWith(true, false), BooleanHashBag.newBagWith(true, true));
        Assert.assertNotEquals(BooleanHashBag.newBagWith(true, false), BooleanHashBag.newBagWith(true, true, false));
        Assert.assertNotEquals(new BooleanHashBag(), BooleanHashBag.newBagWith(true));
    }

    @Test
    public void anySatisfy()
    {
        final int[] count = {0};
        BooleanHashBag bag = BooleanHashBag.newBagWith(false, true, false);
        Assert.assertTrue(bag.anySatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                count[0]++;
                return value;
            }
        }));
        Assert.assertEquals(2, count[0]);
        Assert.assertTrue(BooleanHashBag.newBagWith(false, false, false).anySatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void allSatisfy()
    {
        final int[] count = {0};
        BooleanHashBag bag = BooleanHashBag.newBagWith(false, true, false);
        Assert.assertFalse(bag.allSatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                count[0]++;
                return !value;
            }
        }));
        Assert.assertEquals(2, count[0]);
        Assert.assertTrue(BooleanHashBag.newBagWith(false, false, false).allSatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void noneSatisfy()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(false, true, false);
        Assert.assertFalse(bag.noneSatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return value;
            }
        }));
        Assert.assertTrue(BooleanHashBag.newBagWith(false, false, false).noneSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void forEach()
    {
        final int[] sum = new int[1];
        BooleanHashBag.newBagWith(true, false, false, true, true, true).forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += each ? 1 : 2;
            }
        });

        Assert.assertEquals(8L, sum[0], 0.0);
    }

    @Test
    public void count()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, false, false, true, true, true);
        Assert.assertEquals(4L, bag.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(2L, bag.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(6L, bag.count(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
    }

    @Test
    public void select()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(false, true, false, false, true, true, true);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, true, true), bag.select(BooleanPredicates.isTrue()));
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false), bag.select(BooleanPredicates.isFalse()));
    }

    @Test
    public void reject()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(false, true, false, false, true, true, true);
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false), bag.reject(BooleanPredicates.isTrue()));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, true, true), bag.reject(BooleanPredicates.isFalse()));
    }

    @Test
    public void detectIfNone()
    {
        BooleanHashBag bag1 = BooleanHashBag.newBagWith(true, true, true);
        Assert.assertFalse(bag1.detectIfNone(BooleanPredicates.isFalse(), false));
        Assert.assertTrue(bag1.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(bag1.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertTrue(bag1.detectIfNone(BooleanPredicates.isTrue(), true));

        BooleanHashBag bag2 = BooleanHashBag.newBagWith(false, false, false);
        Assert.assertTrue(bag2.detectIfNone(BooleanPredicates.isTrue(), true));
        Assert.assertFalse(bag2.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(bag2.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(bag2.detectIfNone(BooleanPredicates.isFalse(), false));
    }

    @Test
    public void collect()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, false, false, true, true, true);
        BooleanToObjectFunction<String> stringValueOf = new BooleanToObjectFunction<String>()
        {
            public String valueOf(boolean parameter)
            {
                return parameter ? "true" : "false";
            }
        };
        Assert.assertEquals(HashBag.newBagWith("true", "false", "false", "true", "true", "true"), bag.collect(stringValueOf));
        BooleanHashBag bag1 = BooleanHashBag.newBagWith(false, false);
        Assert.assertEquals(HashBag.newBagWith("false", "false"), bag1.collect(stringValueOf));
        BooleanHashBag bag2 = BooleanHashBag.newBagWith(true, true);
        Assert.assertEquals(HashBag.newBagWith("true", "true"), bag2.collect(stringValueOf));
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", new BooleanHashBag().toString());
        Assert.assertEquals("[true]", BooleanHashBag.newBagWith(true).toString());
        Assert.assertEquals("[true, true, true]", BooleanHashBag.newBagWith(true, true, true).toString());

        BooleanHashBag bar = BooleanHashBag.newBagWith(false, true);
        Assert.assertTrue(
                bar.toString(),
                bar.toString().equals("[false, true]")
                        || bar.toString().equals("[true, false]"));
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("", new BooleanHashBag().makeString());
        Assert.assertEquals("true", BooleanHashBag.newBagWith(true).makeString());
        Assert.assertEquals("true, true, true", BooleanHashBag.newBagWith(true, true, true).makeString());

        BooleanHashBag bag1 = BooleanHashBag.newBagWith(false, true);
        Assert.assertTrue(
                bag1.makeString(),
                bag1.makeString().equals("false, true")
                        || bag1.makeString().equals("true, false"));

        BooleanHashBag bag2 = BooleanHashBag.newBagWith(false, true);
        Assert.assertTrue(
                bag2.makeString("[", "/", "]"),
                bag2.makeString("[", "/", "]").equals("[false/true]")
                        || bag2.makeString("[", "/", "]").equals("[true/false]"));

        BooleanHashBag bag3 = BooleanHashBag.newBagWith(false, true);
        Assert.assertTrue(
                bag3.makeString("/"),
                bag3.makeString("/").equals("false/true")
                        || bag3.makeString("/").equals("true/false"));
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        new BooleanHashBag().appendString(appendable);
        Assert.assertEquals("", appendable.toString());

        StringBuilder appendable0 = new StringBuilder();
        BooleanHashBag.newBagWith(true).appendString(appendable0);
        Assert.assertEquals("true", appendable0.toString());

        StringBuilder appendable1 = new StringBuilder();
        BooleanHashBag.newBagWith(true, true, true).appendString(appendable1);
        Assert.assertEquals("true, true, true", appendable1.toString());

        StringBuilder appendable2 = new StringBuilder();
        BooleanHashBag bag1 = BooleanHashBag.newBagWith(false, false, true);
        bag1.appendString(appendable2);
        Assert.assertTrue(appendable2.toString(), "false, false, true".equals(appendable2.toString())
                || "true, false, false".equals(appendable2.toString()));
        StringBuilder appendable3 = new StringBuilder();
        BooleanHashBag bag2 = BooleanHashBag.newBagWith(false, true);
        bag2.appendString(appendable3, "/");
        Assert.assertTrue(appendable3.toString(), "false/true".equals(appendable3.toString())
                || "true/false".equals(appendable3.toString()));
        StringBuilder appendable4 = new StringBuilder();
        BooleanHashBag bag4 = BooleanHashBag.newBagWith(false, true);
        bag4.appendString(appendable4, "[", "/", "]");
        Assert.assertTrue(appendable4.toString(), "[false/true]".equals(appendable4.toString())
                || "[true/false]".equals(appendable4.toString()));
    }

    @Test
    public void toList()
    {
        MutableBooleanList list = BooleanHashBag.newBagWith(true, true, true, false).toList();
        Assert.assertTrue(list.equals(BooleanArrayList.newListWith(true, true, true, false))
                || list.equals(BooleanArrayList.newListWith(false, true, true, true)));
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), BooleanHashBag.newBagWith(true, false, false, true, true, true).toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, true, true, true, true), BooleanHashBag.newBagWith(true, false, false, true, true, true).toBag());
    }

    @Test
    public void asLazy()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, false, false, true, true, true);
        Assert.assertEquals(bag.toBag(), bag.asLazy().toBag());
        Verify.assertInstanceOf(LazyBooleanIterable.class, bag.asLazy());
    }

    @Test
    public void iterator()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith(true, false, false, true, true, true);
        final BooleanIterator iterator = bag.booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertFalse(iterator.hasNext());

        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator.next();
            }
        });
    }
}
