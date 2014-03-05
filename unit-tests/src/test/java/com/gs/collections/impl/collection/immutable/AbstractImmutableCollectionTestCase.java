/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.collection.immutable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.ByteToObjectFunction;
import com.gs.collections.api.block.function.primitive.CharToObjectFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleToObjectFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatToObjectFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongToObjectFunction;
import com.gs.collections.api.block.function.primitive.ShortToObjectFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.collection.primitive.ImmutableByteCollection;
import com.gs.collections.api.collection.primitive.ImmutableCharCollection;
import com.gs.collections.api.collection.primitive.ImmutableDoubleCollection;
import com.gs.collections.api.collection.primitive.ImmutableFloatCollection;
import com.gs.collections.api.collection.primitive.ImmutableIntCollection;
import com.gs.collections.api.collection.primitive.ImmutableLongCollection;
import com.gs.collections.api.collection.primitive.ImmutableShortCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions0;
import com.gs.collections.impl.block.factory.Functions2;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.StringFunctions;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public abstract class AbstractImmutableCollectionTestCase
{
    public static final Predicate<Integer> ERROR_THROWING_PREDICATE = new Predicate<Integer>()
    {
        public boolean accept(Integer each)
        {
            throw new AssertionError();
        }
    };

    public static final Predicates2<Integer, Class<Integer>> ERROR_THROWING_PREDICATE_2 = new Predicates2<Integer, Class<Integer>>()
    {
        public boolean accept(Integer argument1, Class<Integer> argument2)
        {
            throw new AssertionError();
        }
    };

    protected abstract ImmutableCollection<Integer> classUnderTest();

    protected abstract <T> MutableCollection<T> newMutable();

    @Test
    public void selectWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(
                this.<Integer>newMutable().withAll(integers).select(IntegerPredicates.isOdd()),
                integers.selectWith(Predicates2.in(), iList(1, 3, 5, 7, 9)));
    }

    @Test
    public void selectWith_target()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(
                this.<Integer>newMutable().with(101).withAll(integers).select(IntegerPredicates.isOdd()),
                integers.selectWith(Predicates2.in(), iList(1, 3, 5, 7, 9), this.<Integer>newMutable().with(101)));
    }

    @Test
    public void rejectWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(
                this.<Integer>newMutable().withAll(integers).reject(IntegerPredicates.isOdd()),
                integers.rejectWith(Predicates2.in(), iList(1, 3, 5, 7, 9)));
    }

    @Test
    public void rejectWith_target()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(
                this.<Integer>newMutable().with(100).withAll(integers).reject(IntegerPredicates.isOdd()),
                integers.rejectWith(Predicates2.in(), iList(1, 3, 5, 7, 9), this.<Integer>newMutable().with(100)));
    }

    @Test
    public void partition()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        PartitionImmutableCollection<Integer> partition = integers.partition(IntegerPredicates.isOdd());
        Assert.assertEquals(integers.select(IntegerPredicates.isOdd()), partition.getSelected());
        Assert.assertEquals(integers.select(IntegerPredicates.isEven()), partition.getRejected());
    }

    @Test
    public void partitionWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        PartitionImmutableCollection<Integer> partition = integers.partitionWith(Predicates2.in(), integers.select(IntegerPredicates.isOdd()));
        Assert.assertEquals(integers.select(IntegerPredicates.isOdd()), partition.getSelected());
        Assert.assertEquals(integers.select(IntegerPredicates.isEven()), partition.getRejected());
    }

    @Test
    public void collectWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableCollection<String> expected = integers.collect(Functions.chain(Functions.getToString(), StringFunctions.append("!")));
        ImmutableCollection<String> actual = integers.collectWith(new Function2<Integer, String, String>()
        {
            public String value(Integer argument1, String argument2)
            {
                return argument1 + argument2;
            }
        }, "!");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void collect_target()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        final MutableCollection<String> strings = this.<String>newMutable();
        integers.forEach(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                strings.add(each.toString());
            }
        });
        MutableCollection<String> target = this.<String>newMutable();
        MutableCollection<String> actual = integers.collect(Functions.getToString(), target);
        Assert.assertEquals(strings, actual);
        Assert.assertSame(target, actual);
    }

    @Test
    public void collectWith_target()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableCollection<String> expected = this.<String>newMutable().with("?").withAll(integers.collect(Functions.chain(Functions.getToString(), StringFunctions.append("!"))));
        MutableCollection<String> targetCollection = this.<String>newMutable().with("?");
        MutableCollection<String> actual = integers.collectWith(new Function2<Integer, String, String>()
        {
            public String value(Integer argument1, String argument2)
            {
                return argument1 + argument2;
            }
        }, "!", targetCollection);

        Assert.assertEquals(expected, actual);
        Assert.assertSame(targetCollection, actual);
    }

    @Test
    public void injectInto()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Integer result = integers.injectInto(0, AddFunction.INTEGER);
        Assert.assertEquals(FastList.newList(integers).injectInto(0, AddFunction.INTEGER_TO_INT), result.intValue());
    }

    @Test
    public void injectIntoInt()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER).longValue(),
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_INT));
    }

    @Test
    public void injectIntoLong()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER).longValue(),
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_LONG));
    }

    @Test
    public void injectIntoDouble()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER).doubleValue(),
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_DOUBLE),
                0.0);
    }

    @Test
    public void injectIntoFloat()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER).floatValue(),
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_FLOAT),
                0.0);
    }

    @Test
    public void sumFloat()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_FLOAT),
                this.classUnderTest().sumOfFloat(new FloatFunction<Integer>()
                {
                    public float floatValueOf(Integer integer)
                    {
                        return integer.floatValue();
                    }
                }),
                0.0);
    }

    @Test
    public void sumDouble()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_DOUBLE),
                this.classUnderTest().sumOfDouble(new DoubleFunction<Integer>()
                {
                    public double doubleValueOf(Integer integer)
                    {
                        return integer.doubleValue();
                    }
                }),
                0.0);
    }

    @Test
    public void sumInteger()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_INT),
                this.classUnderTest().sumOfInt(new IntFunction<Integer>()
                {
                    public int intValueOf(Integer integer)
                    {
                        return integer;
                    }
                }));
    }

    @Test
    public void sumLong()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_LONG),
                this.classUnderTest().sumOfLong(new LongFunction<Integer>()
                {
                    public long longValueOf(Integer integer)
                    {
                        return integer.longValue();
                    }
                }));
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + this.classUnderTest().makeString() + ']');
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + this.classUnderTest().makeString(", ") + ']');
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), this.classUnderTest().makeString("[", ", ", "]"));
    }

    @Test
    public void appendString()
    {
        Appendable builder1 = new StringBuilder();
        this.classUnderTest().appendString(builder1);
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + builder1.toString() + ']');

        Appendable builder2 = new StringBuilder();
        this.classUnderTest().appendString(builder2, ", ");
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + builder2.toString() + ']');

        Appendable builder3 = new StringBuilder();
        this.classUnderTest().appendString(builder3, "[", ", ", "]");
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), builder3.toString());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), this.classUnderTest().toString());
    }

    @Test
    public void select()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.select(Predicates.lessThan(integers.size() + 1)));
        Verify.assertIterableEmpty(integers.select(Predicates.greaterThan(integers.size())));
    }

    @Test
    public void reject()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Verify.assertIterableEmpty(integers.reject(Predicates.lessThan(integers.size() + 1)));
        Assert.assertEquals(integers, integers.reject(Predicates.greaterThan(integers.size())));
    }

    @Test
    public void collect()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.collect(Functions.getIntegerPassThru()));
    }

    @Test
    public void collectBoolean()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableBooleanCollection immutableCollection = integers.collectBoolean(PrimitiveFunctions.integerIsPositive());
        Verify.assertSize(1, immutableCollection);
    }

    @Test
    public void collectByte()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableByteCollection immutableCollection = integers.collectByte(PrimitiveFunctions.unboxIntegerToByte());
        Verify.assertSize(integers.size(), immutableCollection);
        ByteToObjectFunction<Integer> function = new ByteToObjectFunction<Integer>()
        {
            public Integer valueOf(byte byteParameter)
            {
                return Integer.valueOf(byteParameter);
            }
        };
        Assert.assertEquals(integers, immutableCollection.collect(function));
    }

    @Test
    public void collectChar()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableCharCollection immutableCollection = integers.collectChar(PrimitiveFunctions.unboxIntegerToChar());
        Verify.assertSize(integers.size(), immutableCollection);
        CharToObjectFunction<Integer> function = new CharToObjectFunction<Integer>()
        {
            public Integer valueOf(char charParameter)
            {
                return Integer.valueOf(charParameter);
            }
        };
        Assert.assertEquals(integers, immutableCollection.collect(function));
    }

    @Test
    public void collectDouble()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableDoubleCollection immutableCollection = integers.collectDouble(PrimitiveFunctions.unboxIntegerToDouble());
        Verify.assertSize(integers.size(), immutableCollection);
        DoubleToObjectFunction<Integer> function = new DoubleToObjectFunction<Integer>()
        {
            public Integer valueOf(double doubleParameter)
            {
                return Integer.valueOf((int) doubleParameter);
            }
        };
        Assert.assertEquals(integers, immutableCollection.collect(function));
    }

    @Test
    public void collectFloat()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableFloatCollection immutableCollection = integers.collectFloat(PrimitiveFunctions.unboxIntegerToFloat());
        Verify.assertSize(integers.size(), immutableCollection);
        FloatToObjectFunction<Integer> function = new FloatToObjectFunction<Integer>()
        {
            public Integer valueOf(float floatParameter)
            {
                return Integer.valueOf((int) floatParameter);
            }
        };
        Assert.assertEquals(integers, immutableCollection.collect(function));
    }

    @Test
    public void collectInt()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableIntCollection immutableCollection = integers.collectInt(PrimitiveFunctions.unboxIntegerToInt());
        Verify.assertSize(integers.size(), immutableCollection);
        IntToObjectFunction<Integer> function = new IntToObjectFunction<Integer>()
        {
            public Integer valueOf(int intParameter)
            {
                return Integer.valueOf(intParameter);
            }
        };
        Assert.assertEquals(integers, immutableCollection.collect(function));
    }

    @Test
    public void collectLong()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableLongCollection immutableCollection = integers.collectLong(PrimitiveFunctions.unboxIntegerToLong());
        Verify.assertSize(integers.size(), immutableCollection);
        LongToObjectFunction<Integer> function = new LongToObjectFunction<Integer>()
        {
            public Integer valueOf(long longParameter)
            {
                return Integer.valueOf((int) longParameter);
            }
        };
        Assert.assertEquals(integers, immutableCollection.collect(function));
    }

    @Test
    public void collectShort()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableShortCollection immutableCollection = integers.collectShort(PrimitiveFunctions.unboxIntegerToShort());
        Verify.assertSize(integers.size(), immutableCollection);
        ShortToObjectFunction<Integer> function = new ShortToObjectFunction<Integer>()
        {
            public Integer valueOf(short shortParameter)
            {
                return Integer.valueOf(shortParameter);
            }
        };
        Assert.assertEquals(integers, immutableCollection.collect(function));
    }

    @Test
    public void flatCollect()
    {
        RichIterable<String> actual = this.classUnderTest().flatCollect(new Function<Integer, MutableList<String>>()
        {
            public MutableList<String> valueOf(Integer integer)
            {
                return Lists.fixedSize.of(String.valueOf(integer));
            }
        });

        ImmutableCollection<String> expected = this.classUnderTest().collect(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.classUnderTest().chunk(0);
    }

    @Test
    public void detect()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(Integer.valueOf(1), integers.detect(Predicates.equal(1)));
        Assert.assertNull(integers.detect(Predicates.equal(integers.size() + 1)));
    }

    @Test
    public void detectWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(Integer.valueOf(1), integers.detectWith(Predicates2.equal(), Integer.valueOf(1)));
        Assert.assertNull(integers.detectWith(Predicates2.equal(), Integer.valueOf(integers.size() + 1)));
    }

    @Test
    public void detectIfNone()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Function0<Integer> function = new PassThruFunction0<Integer>(integers.size() + 1);
        Assert.assertEquals(Integer.valueOf(1), integers.detectIfNone(Predicates.equal(1), function));
        Assert.assertEquals(Integer.valueOf(integers.size() + 1), integers.detectIfNone(Predicates.equal(integers.size() + 1), function));
    }

    @Test
    public void detectWithIfNone()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Integer sum = Integer.valueOf(integers.size() + 1);
        Function0<Integer> function = new PassThruFunction0<Integer>(sum);
        Assert.assertEquals(Integer.valueOf(1), integers.detectWithIfNone(Predicates2.equal(), Integer.valueOf(1), function));
        Assert.assertEquals(sum, integers.detectWithIfNone(Predicates2.equal(), sum, function));
    }

    @Test
    public void allSatisfy()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(integers.allSatisfy(Predicates.equal(0)));
    }

    @Test
    public void allSatisfyWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfyWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(integers.allSatisfyWith(Predicates2.equal(), 0));
    }

    @Test
    public void noneSatisfy()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.noneSatisfy(Predicates.instanceOf(String.class)));
        Assert.assertFalse(integers.noneSatisfy(Predicates.equal(1)));
    }

    @Test
    public void noneSatisfyWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.noneSatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertFalse(integers.noneSatisfyWith(Predicates2.equal(), 1));
    }

    @Test
    public void anySatisfy()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(integers.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void anySatisfyWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertTrue(integers.anySatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void count()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers.size(), integers.count(Predicates.instanceOf(Integer.class)));
        Assert.assertEquals(0, integers.count(Predicates.instanceOf(String.class)));
    }

    @Test
    public void countWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers.size(), integers.countWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(0, integers.countWith(Predicates2.instanceOf(), String.class));
    }

    @Test
    public void collectIf()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.collectIf(Predicates.instanceOf(Integer.class),
                Functions.getIntegerPassThru()));
    }

    @Test
    public void getFirst()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(Integer.valueOf(1), integers.getFirst());
    }

    @Test
    public void getLast()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(Integer.valueOf(integers.size()), integers.getLast());
    }

    @Test
    public void isEmpty()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        Assert.assertFalse(immutableCollection.isEmpty());
        Assert.assertTrue(immutableCollection.notEmpty());
    }

    @Test
    public void iterator()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        final Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            Integer integer = iterator.next();
            Assert.assertEquals(i + 1, integer.intValue());
        }
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator.next();
            }
        });
    }

    @Test
    public void toArray()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableList<Integer> copy = FastList.newList(integers);
        Assert.assertArrayEquals(integers.toArray(), copy.toArray());
        Assert.assertArrayEquals(integers.toArray(new Integer[integers.size()]), copy.toArray(new Integer[integers.size()]));
    }

    @Test
    public void toSortedList()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableList<Integer> copy = FastList.newList(integers);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(copy.sortThis(Collections.<Integer>reverseOrder()), list);
        MutableList<Integer> list2 = integers.toSortedList();
        Assert.assertEquals(copy.sortThis(), list2);
    }

    @Test
    public void toSortedSet()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableSortedSet<Integer> set = integers.toSortedSet();
        Verify.assertListsEqual(integers.toSortedList(), set.toList());
    }

    @Test
    public void toSortedSetWithComparator()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableSortedSet<Integer> set = integers.toSortedSet(Comparators.<Integer>reverseNaturalOrder());
        Assert.assertEquals(integers.toSet(), set);
        Assert.assertEquals(integers.toSortedList(Comparators.<Integer>reverseNaturalOrder()), set.toList());
    }

    @Test
    public void toSortedSetBy()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableSortedSet<Integer> set = integers.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSet(integers), set);
    }

    @Test
    public void forLoop()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        for (Integer each : immutableCollection)
        {
            Assert.assertNotNull(each);
        }
    }

    private ImmutableCollection<Integer> classUnderTestWithNull()
    {
        return this.classUnderTest().reject(Predicates.equal(1)).newWith(null);
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws()
    {
        this.classUnderTestWithNull().min(Comparators.naturalOrder());
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws()
    {
        this.classUnderTestWithNull().max(Comparators.naturalOrder());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min(Comparators.naturalOrder()));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().max(Comparators.reverse(Comparators.naturalOrder())));
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws_without_comparator()
    {
        this.classUnderTestWithNull().min();
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws_without_comparator()
    {
        this.classUnderTestWithNull().max();
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(this.classUnderTest().size()), this.classUnderTest().max());
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(this.classUnderTest().size()), this.classUnderTest().maxBy(Functions.getIntegerPassThru()));
    }

    @Test
    public void iteratorRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableCollectionTestCase.this.classUnderTest().iterator().remove();
            }
        });
    }

    @Test
    public void add()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).add(1);
            }
        });
    }

    @Test
    public void remove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).remove(Integer.valueOf(1));
            }
        });
    }

    @Test
    public void clear()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).clear();
            }
        });
    }

    @Test
    public void removeAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).removeAll(Lists.fixedSize.of());
            }
        });
    }

    @Test
    public void retainAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).retainAll(Lists.fixedSize.of());
            }
        });
    }

    @Test
    public void addAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).addAll(Lists.fixedSize.<Integer>of());
            }
        });
    }

    @Test
    public void aggregateByMutating()
    {
        Function0<Counter> valueCreator = new Function0<Counter>()
        {
            public Counter value()
            {
                return new Counter();
            }
        };
        Procedure2<Counter, Integer> sumAggregator = new Procedure2<Counter, Integer>()
        {
            public void value(Counter aggregate, Integer value)
            {
                aggregate.add(value);
            }
        };
        MapIterable<String, Counter> actual = this.classUnderTest().aggregateInPlaceBy(Functions.getToString(), valueCreator, sumAggregator);
        MapIterable<String, Counter> expected = this.classUnderTest().toBag().aggregateInPlaceBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void aggregateByNonMutating()
    {
        Function0<Integer> valueCreator = Functions0.value(0);
        Function2<Integer, Integer, Integer> sumAggregator = Functions2.integerAddition();
        MapIterable<String, Integer> actual = this.classUnderTest().aggregateBy(Functions.getToString(), valueCreator, sumAggregator);
        MapIterable<String, Integer> expected = this.classUnderTest().toBag().aggregateBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(expected, actual);
    }
}
