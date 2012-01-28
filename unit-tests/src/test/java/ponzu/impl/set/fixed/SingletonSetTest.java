/*
 * Copyright 2011 Goldman Sachs.
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

package ponzu.impl.set.fixed;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Function3;
import ponzu.api.block.function.Generator;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.partition.set.PartitionMutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Twin;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.factory.Predicates2;
import ponzu.impl.block.function.AddFunction;
import ponzu.impl.block.function.Constant;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.factory.Sets;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.set.mutable.SynchronizedMutableSet;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.test.Verify;
import ponzu.impl.tuple.Tuples;

import static ponzu.impl.factory.Iterables.*;

/**
 * JUnit test for {@link SingletonSet}.
 */
public class SingletonSetTest extends AbstractMemoryEfficientMutableSetTestCase
{
    private SingletonSet<String> set;
    private MutableSet<Integer> intSet;

    @Before
    public void setUp()
    {
        this.set = new SingletonSet<String>("1");
        this.intSet = Sets.fixedSize.of(1);
    }

    @Override
    protected MutableSet<String> classUnderTest()
    {
        return new SingletonSet<String>("1");
    }

    @Override
    protected MutableSet<String> classUnderTestWithNull()
    {
        return new SingletonSet<String>(null);
    }

    @Test
    public void nonUniqueWith()
    {
        Twin<String> twin1 = Tuples.twin("1", "1");
        Twin<String> twin2 = Tuples.twin("1", "1");
        SingletonSet<Twin<String>> set = new SingletonSet<Twin<String>>(twin1);
        set.with(twin2);
        Assert.assertSame(set.getFirst(), twin1);
    }

    @Override
    @Test
    public void asSynchronized()
    {
        super.asSynchronized();
        Verify.assertInstanceOf(SynchronizedMutableSet.class, Sets.fixedSize.of("1").asSynchronized());
    }

    @Test
    public void contains()
    {
        this.assertUnchanged();
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        MutableSet<String> one = Sets.fixedSize.of("1");
        MutableSet<String> oneA = UnifiedSet.newSet();
        oneA.add("1");
        Verify.assertEqualsAndHashCode(one, oneA);
        Verify.assertPostSerializedEqualsAndHashCode(one);
    }

    @Test
    public void remove()
    {
        try
        {
            this.set.remove("1");
            Assert.fail("Should not allow remove from SingletonSet");
        }
        catch (UnsupportedOperationException ignored)
        {
            this.assertUnchanged();
        }
    }

    @Test
    public void addDuplicate()
    {
        try
        {
            this.set.add("1");
            Assert.fail("Should not allow adding a duplicate to SingletonSet");
        }
        catch (UnsupportedOperationException ignored)
        {
            this.assertUnchanged();
        }
    }

    @Test
    public void add()
    {
        try
        {
            this.set.add("2");
            Assert.fail("Should not allow add to SingletonSet");
        }
        catch (UnsupportedOperationException ignored)
        {
            this.assertUnchanged();
        }
    }

    @Test
    public void addingAllToOtherSet()
    {
        MutableSet<String> newSet = UnifiedSet.newSet(Sets.fixedSize.of("1"));
        newSet.add("2");
        Verify.assertContainsAll(newSet, "1", "2");
    }

    private void assertUnchanged()
    {
        Verify.assertSize(1, this.set);
        Verify.assertContains("1", this.set);
        Verify.assertNotContains("2", this.set);
    }

    @Test
    public void forEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        this.intSet.forEach(CollectionAddProcedure.on(result));
        Verify.assertSize(1, result);
        Verify.assertContainsAll(result, 1);
    }

    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        this.intSet.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Verify.assertSize(1, result);
        Verify.assertContainsAll(result, 1);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        this.intSet.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        Verify.assertContainsAll(result, 1);
    }

    @Test
    public void select()
    {
        Verify.assertContainsAll(this.intSet.filter(Predicates.lessThan(3)), 1);
        Verify.assertEmpty(this.intSet.filter(Predicates.greaterThan(3)));
    }

    @Test
    public void selectWith()
    {
        Verify.assertContainsAll(this.intSet.filterWith(Predicates2.<Integer>lessThan(), 3), 1);
        Verify.assertEmpty(this.intSet.filterWith(Predicates2.<Integer>greaterThan(), 3));
    }

    @Test
    public void reject()
    {
        Verify.assertEmpty(this.intSet.filterNot(Predicates.lessThan(3)));
        Verify.assertContainsAll(this.intSet.filterNot(
                Predicates.greaterThan(3),
                UnifiedSet.<Integer>newSet()),
                1);
    }

    @Test
    public void rejectWith()
    {
        Verify.assertEmpty(this.intSet.filterNotWith(Predicates2.<Integer>lessThan(), 3));
        Verify.assertContainsAll(
                this.intSet.filterNotWith(
                        Predicates2.<Integer>greaterThan(),
                        3,
                        UnifiedSet.<Integer>newSet()),
                1);
    }

    @Test
    public void partition()
    {
        PartitionMutableSet<Integer> partition = this.intSet.partition(Predicates.lessThan(3));
        Assert.assertEquals(mSet(1), partition.getSelected());
        Assert.assertEquals(mSet(), partition.getRejected());
    }

    @Test
    public void collect()
    {
        Verify.assertContainsAll(this.intSet.transform(Functions.getToString()), "1");
        Verify.assertContainsAll(this.intSet.transform(Functions.getToString(), UnifiedSet.<String>newSet()),
                "1");
    }

    @Test
    public void flatCollect()
    {
        Function<Integer, MutableSet<String>> function =
                new Function<Integer, MutableSet<String>>()
                {
                    public MutableSet<String> valueOf(Integer object)
                    {
                        return UnifiedSet.newSetWith(String.valueOf(object));
                    }
                };
        Verify.assertSetsEqual(UnifiedSet.newSetWith("1"), this.intSet.flatTransform(function));
        Verify.assertListsEqual(
                FastList.newListWith("1"),
                this.intSet.flatTransform(function, FastList.<String>newList()));
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(1), this.intSet.find(Predicates.equal(1)));
        Assert.assertNull(this.intSet.find(Predicates.equal(6)));
    }

    @Test
    public void detectWith()
    {
        Assert.assertEquals(Integer.valueOf(1), this.intSet.findWith(Predicates2.equal(), 1));
        Assert.assertNull(this.intSet.findWith(Predicates2.equal(), 6));
    }

    @Test
    public void detectIfNoneWithBlock()
    {
        Generator<Integer> function = new Constant<Integer>(6);
        Assert.assertEquals(Integer.valueOf(1), this.intSet.findIfNone(Predicates.equal(1), function));
        Assert.assertEquals(Integer.valueOf(6), this.intSet.findIfNone(Predicates.equal(6), function));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.intSet.allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(this.intSet.allSatisfy(Predicates.equal(2)));
    }

    @Test
    public void allSatisfyWith()
    {
        Assert.assertTrue(this.intSet.allSatisfyWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(this.intSet.allSatisfyWith(Predicates2.equal(), 2));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.intSet.anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(this.intSet.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void anySatisfyWith()
    {
        Assert.assertFalse(this.intSet.anySatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertTrue(this.intSet.anySatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(1, this.intSet.count(Predicates.instanceOf(Integer.class)));
        Assert.assertEquals(0, this.intSet.count(Predicates.instanceOf(String.class)));
    }

    @Test
    public void countWith()
    {
        Assert.assertEquals(1, this.intSet.countWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(0, this.intSet.countWith(Predicates2.instanceOf(), String.class));
    }

    @Test
    public void collectIf()
    {
        Verify.assertContainsAll(this.intSet.transformIf(
                Predicates.instanceOf(Integer.class),
                Functions.getToString()), "1");
        Verify.assertContainsAll(this.intSet.transformIf(
                Predicates.instanceOf(Integer.class),
                Functions.getToString(),
                FastList.<String>newList()), "1");
    }

    @Test
    public void collectWith()
    {
        Function2<Integer, Integer, Integer> addFunction =
                new Function2<Integer, Integer, Integer>()
                {
                    public Integer value(Integer each, Integer parameter)
                    {
                        return each + parameter;
                    }
                };
        Assert.assertEquals(
                UnifiedSet.newSetWith(2),
                this.intSet.transformWith(addFunction, 1));
        Assert.assertEquals(
                FastList.newListWith(2),
                this.intSet.transformWith(addFunction, 1, FastList.<Integer>newList()));
    }

    @Test
    public void getFirst()
    {
        Assert.assertEquals(Integer.valueOf(1), this.intSet.getFirst());
    }

    @Test
    public void getLast()
    {
        Assert.assertEquals(Integer.valueOf(1), this.intSet.getLast());
    }

    @Test
    public void isEmpty()
    {
        Verify.assertNotEmpty(this.intSet);
        Assert.assertTrue(this.intSet.notEmpty());
    }

    @Test
    public void removeAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                SingletonSetTest.this.intSet.removeAll(Lists.fixedSize.of(1, 2));
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
                SingletonSetTest.this.intSet.retainAll(Lists.fixedSize.of(2));
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
                SingletonSetTest.this.intSet.clear();
            }
        });
    }

    @Override
    @Test
    public void iterator()
    {
        super.iterator();
        Iterator<Integer> iterator = this.intSet.iterator();
        for (int i = this.intSet.size(); i-- > 0; )
        {
            Integer integer = iterator.next();
            Assert.assertEquals(1, integer.intValue() + i);
        }
    }

    @Test
    public void injectInto()
    {
        Integer result = this.intSet.foldLeft(1, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(2), result);
    }

    @Test
    public void injectIntoWith()
    {
        Integer result =
                this.intSet.foldLeftWith(1,
                        new Function3<Integer, Integer, Integer, Integer>()
                        {
                            public Integer value(Integer injectedValued, Integer item, Integer parameter)
                            {
                                return injectedValued + item + parameter;
                            }
                        },
                        0);
        Assert.assertEquals(Integer.valueOf(2), result);
    }

    @Test
    public void toArray()
    {
        Object[] array = this.intSet.toArray();
        Verify.assertSize(1, array);
        Integer[] array2 = this.intSet.toArray(new Integer[1]);
        Verify.assertSize(1, array2);
    }

    @Test
    public void selectAndRejectWith()
    {
        Twin<MutableList<Integer>> result =
                this.intSet.partitionWith(Predicates2.equal(), 1);
        Verify.assertSize(1, result.getOne());
        Verify.assertEmpty(result.getTwo());
    }

    @Test
    public void removeWithPredicate()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                SingletonSetTest.this.intSet.removeIf(Predicates.isNull());
            }
        });
    }

    @Test
    public void toList()
    {
        MutableList<Integer> list = this.intSet.toList();
        list.add(2);
        list.add(3);
        list.add(4);
        Verify.assertContainsAll(list, 1, 2, 3, 4);
    }

    @Test
    public void toSortedList()
    {
        Assert.assertEquals(FastList.newListWith(1), this.intSet.toSortedList(Collections.<Integer>reverseOrder()));
    }

    @Test
    public void toSortedListBy()
    {
        Assert.assertEquals(FastList.newListWith(1), this.intSet.toSortedListBy(Functions.getIntegerPassThru()));
    }

    @Test
    public void toSet()
    {
        MutableSet<Integer> set = this.intSet.toSet();
        Verify.assertContainsAll(set, 1);
    }

    @Test
    public void toMap()
    {
        MutableMap<Integer, Integer> map =
                this.intSet.toMap(Functions.getIntegerPassThru(), Functions.getIntegerPassThru());
        Verify.assertContainsAll(map.keySet(), 1);
        Verify.assertContainsAll(map.values(), 1);
    }

    @Override
    @Test
    public void testClone()
    {
        Verify.assertShallowClone(this.set);
        MutableSet<String> cloneSet = this.set.clone();
        Assert.assertNotSame(cloneSet, this.set);
        Verify.assertEqualsAndHashCode(UnifiedSet.newSetWith("1"), cloneSet);
    }

    @Test
    public void newEmpty()
    {
        MutableSet<String> newEmpty = this.set.newEmpty();
        Verify.assertInstanceOf(UnifiedSet.class, newEmpty);
        Verify.assertEmpty(newEmpty);
    }

    @Test
    @Override
    public void min_null_throws()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        super.min_null_throws();
    }

    @Test
    @Override
    public void max_null_throws()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        super.max_null_throws();
    }

    @Test
    @Override
    public void min_null_throws_without_comparator()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        super.min_null_throws_without_comparator();
    }

    @Test
    @Override
    public void max_null_throws_without_comparator()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        super.max_null_throws_without_comparator();
    }
}
