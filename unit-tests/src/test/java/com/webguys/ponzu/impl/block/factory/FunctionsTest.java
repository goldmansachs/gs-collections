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

package com.webguys.ponzu.impl.block.factory;

import java.util.Collections;
import java.util.Map;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.collection.MutableCollection;
import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.impl.block.function.primitive.DoubleFunctionImpl;
import com.webguys.ponzu.impl.block.function.primitive.IntegerFunctionImpl;
import com.webguys.ponzu.impl.block.function.primitive.LongFunctionImpl;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.list.Interval;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.set.mutable.SetAdapter;
import com.webguys.ponzu.impl.set.mutable.UnifiedSet;
import com.webguys.ponzu.impl.test.Verify;
import com.webguys.ponzu.impl.test.domain.Person;
import org.junit.Assert;
import org.junit.Test;

import static com.webguys.ponzu.impl.factory.Iterables.*;

public class FunctionsTest
{
    private static final Function<String, Integer> STRING_LENGTH = new Function<String, Integer>()
    {
        public Integer valueOf(String object)
        {
            return Integer.valueOf(object.length());
        }
    };

    private static final Function<Integer, Boolean> IS_ODD = new Function<Integer, Boolean>()
    {
        public Boolean valueOf(Integer object)
        {
            return Boolean.valueOf(object.intValue() % 2 != 0);
        }
    };

    private static final Function<Boolean, String> BOOLEAN_STRING = new Function<Boolean, String>()
    {
        public String valueOf(Boolean object)
        {
            return object.toString();
        }
    };

    @Test
    public void getPassThru()
    {
        Object object = new Object();
        Assert.assertSame(object, Functions.getPassThru().valueOf(object));
    }

    @Test
    public void getFixedValue()
    {
        Assert.assertEquals(Integer.valueOf(5), Functions.getFixedValue(5).valueOf(null));
    }

    @Test
    public void getToClass()
    {
        Assert.assertSame(Integer.class, Functions.getToClass().valueOf(0));
    }

    @Test
    public void getMathSinFunction()
    {
        Function<Number, Double> function = Functions.getMathSinFunction();
        Assert.assertEquals(Math.sin(1.0), function.valueOf(1), 0.0);
    }

    @Test
    public void getNumberPassThru()
    {
        Function<Number, Number> function = Functions.getNumberPassThru();
        Assert.assertEquals(1, function.valueOf(1));
    }

    @Test
    public void getIntegerPassThru()
    {
        Function<Integer, Integer> function = Functions.getIntegerPassThru();
        Assert.assertEquals(Integer.valueOf(1), function.valueOf(1));
    }

    @Test
    public void getLongPassThru()
    {
        Function<Long, Long> function = Functions.getLongPassThru();
        Assert.assertEquals(Long.valueOf(1), function.valueOf(1L));
    }

    @Test
    public void getDoublePassThru()
    {
        Function<Double, Double> function = Functions.getDoublePassThru();
        Assert.assertEquals(Double.valueOf(1).doubleValue(), function.valueOf(1.0).doubleValue(), 0.0);
    }

    @Test
    public void getStringPassThru()
    {
        Function<String, String> function = Functions.getStringPassThru();
        Assert.assertEquals("hello", function.valueOf("hello"));
    }

    @Test
    public void getStringTrim()
    {
        Assert.assertEquals("hello", Functions.getStringTrim().valueOf(" hello  "));
    }

    @Test
    public void getToString()
    {
        Function<Object, String> function = Functions.getToString();
        Assert.assertEquals("1", function.valueOf(1));
        Assert.assertEquals("null", function.valueOf(null));
    }

    @Test
    public void getStringToInteger()
    {
        Function<String, Integer> function = Functions.getStringToInteger();
        Assert.assertEquals(Integer.valueOf(1), function.valueOf("1"));
    }

    @Test
    public void firstNotNullValue()
    {
        Function<Object, Integer> function =
                Functions.firstNotNullValue(Functions.<Object, Integer>getFixedValue(null), Functions.getFixedValue(1), Functions.getFixedValue(2));
        Assert.assertEquals(Integer.valueOf(1), function.valueOf(null));
    }

    @Test
    public void firstNotEmptyStringValue()
    {
        Function<Object, String> function =
                Functions.firstNotEmptyStringValue(Functions.getFixedValue(""), Functions.getFixedValue("hello"), Functions.getFixedValue(""));
        Assert.assertEquals("hello", function.valueOf(null));
    }

    @Test
    public void firstNotEmptyCollectionValue()
    {
        Function<Object, ImmutableList<String>> function = Functions.firstNotEmptyCollectionValue(
                Functions.<Object, ImmutableList<String>>getFixedValue(Lists.immutable.<String>of()),
                Functions.<Object, ImmutableList<String>>getFixedValue(Lists.immutable.of("hello")),
                Functions.<Object, ImmutableList<String>>getFixedValue(Lists.immutable.<String>of()));
        Assert.assertEquals(iList("hello"), function.valueOf(null));
    }

    @Test
    public void ifTrue()
    {
        String result = "1";
        Assert.assertSame(result, Functions.ifTrue(Predicates.alwaysTrue(), Functions.getPassThru()).valueOf(result));
        Assert.assertNull(result, Functions.ifTrue(Predicates.alwaysFalse(), Functions.getPassThru()).valueOf(result));
    }

    @Test
    public void ifElse()
    {
        String result1 = "1";
        String result2 = "2";
        Assert.assertSame(result1, Functions.ifElse(Predicates.alwaysTrue(), Functions.getFixedValue(result1), Functions.getFixedValue(result2)).valueOf(null));
        Assert.assertSame(result2, Functions.ifElse(Predicates.alwaysFalse(), Functions.getFixedValue(result1), Functions.getFixedValue(result2)).valueOf(null));
    }

    @Test
    public void chains()
    {
        Function<String, Integer> toInteger = Functions.getStringToInteger();
        Function<Object, String> toString = Functions.getToString();

        Assert.assertEquals("42", Functions.chain(toInteger, toString).valueOf("42"));
        Assert.assertEquals(Integer.valueOf(42), Functions.chain(toString, toInteger).valueOf(42));

        Function<String, Integer> chain = Functions.chain(toInteger, toString).chain(toInteger);
        Assert.assertEquals(Integer.valueOf(42), chain.valueOf("42"));
        Assert.assertEquals("42", Functions.chain(toString, toInteger).chain(toString).valueOf(42));

        Assert.assertEquals("42", Functions.chain(toInteger, toString).chain(toInteger).chain(toString).valueOf("42"));
        Assert.assertEquals(Integer.valueOf(42), Functions.chain(toString, toInteger).chain(toString).chain(toInteger).valueOf(42));

        Assert.assertEquals(Integer.valueOf(42), Functions.chain(toInteger, toString).chain(toInteger).chain(toString).chain(toInteger).valueOf("42"));
        Assert.assertEquals(Integer.valueOf(42), Functions.chain(toString, toInteger).chain(toString).chain(toInteger).chain(toString).chain(toInteger).valueOf(42));
    }

    @Test
    public void chain_two()
    {
        Function<Boolean, Integer> chain = Functions.chain(BOOLEAN_STRING, STRING_LENGTH);
        Assert.assertEquals(Integer.valueOf(5), chain.valueOf(Boolean.FALSE));
    }

    @Test
    public void chain_three()
    {
        Function<String, String> chain = Functions.chain(STRING_LENGTH, IS_ODD).chain(BOOLEAN_STRING);
        Assert.assertEquals("true", chain.valueOf("foo"));
    }

    @Test
    public void chain_four()
    {
        Function<Integer, Boolean> chain = Functions.chain(IS_ODD, BOOLEAN_STRING).chain(STRING_LENGTH).chain(IS_ODD);
        Assert.assertEquals(Boolean.TRUE, chain.valueOf(Integer.valueOf(4)));
    }

    @Test
    public void intValueFunctionToComparator()
    {
        MutableList<Integer> list = Interval.oneTo(100).toList();
        Collections.shuffle(list);
        list.sortThis(Comparators.byFunction(new IntegerFunctionImpl<Integer>()
        {
            public int intValueOf(Integer integer)
            {
                return integer.intValue();
            }
        }));
        Assert.assertEquals(Interval.oneTo(100).toList(), list);
    }

    @Test
    public void doubleValueFunctionToComparator()
    {
        MutableList<Double> list = FastList.newListWith(5.0, 4.0, 3.0, 2.0, 1.0);
        Collections.shuffle(list);
        list.sortThis(Comparators.byFunction(new DoubleFunctionImpl<Double>()
        {
            public double doubleValueOf(Double each)
            {
                return each.doubleValue();
            }
        }));
        Assert.assertEquals(FastList.newListWith(1.0, 2.0, 3.0, 4.0, 5.0), list);
    }

    @Test
    public void longValueFunctionToComparator()
    {
        MutableList<Long> list = FastList.newListWith(5L, 4L, 3L, 2L, 1L);
        Collections.shuffle(list);
        list.sortThis(Comparators.byFunction(new LongFunctionImpl<Long>()
        {
            public long longValueOf(Long each)
            {
                return each.longValue();
            }
        }));
        Assert.assertEquals(FastList.newListWith(1L, 2L, 3L, 4L, 5L), list);
    }

    @Test
    public void classFunctionToString()
    {
        Assert.assertEquals("object.getClass()", Functions.getToClass().toString());
    }

    @Test
    public void mathSinToString()
    {
        Assert.assertEquals("Math.sin()", Functions.getMathSinFunction().toString());
    }

    @Test
    public void mathStringToIntegerToString()
    {
        Assert.assertEquals("stringToInteger", Functions.getStringToInteger().toString());
    }

    @Test
    public void longValue()
    {
        Assert.assertEquals("LongPassThruFunction", Functions.getLongPassThru().toString());
    }

    @Test
    public void pair()
    {
        Person john = new Person("John", "Smith");
        Person jane = new Person("Jane", "Smith");
        Person johnDoe = new Person("John", "Doe");
        MutableList<Person> people = FastList.newListWith(john, jane, johnDoe);
        MutableList<Person> sorted = people.sortThisBy(Functions.pair(Person.LAST, Person.FIRST));
        Assert.assertEquals(FastList.newListWith(johnDoe, jane, john), sorted);
    }

    @Test
    public void key()
    {
        MutableMap<String, Integer> map = UnifiedMap.newWithKeysValues("One", 1);
        MutableSet<Map.Entry<String, Integer>> entries = SetAdapter.adapt(map.entrySet());
        MutableSet<String> keys = entries.transform(Functions.<String>getKeyFunction());
        Assert.assertEquals(UnifiedSet.newSetWith("One"), keys);
    }

    @Test
    public void value()
    {
        MutableMap<String, Integer> map = UnifiedMap.newWithKeysValues("One", 1);
        MutableSet<Map.Entry<String, Integer>> entries = SetAdapter.adapt(map.entrySet());
        MutableSet<Integer> values = entries.transform(Functions.<Integer>getValueFunction());
        Assert.assertEquals(UnifiedSet.newSetWith(1), values);
    }

    @Test
    public void size()
    {
        ImmutableList<ImmutableList<Integer>> list = Lists.immutable.of(Lists.immutable.of(1), Lists.immutable.of(1, 2), Lists.immutable.of(1, 2, 3));
        ImmutableList<Integer> sizes = list.transform(Functions.getSizeOf());
        Assert.assertEquals(FastList.newListWith(1, 2, 3), sizes);
    }

    @Test
    public void squaredCollection()
    {
        MutableCollection<Integer> squareCollection = FastList.newListWith(1, 2, 3, 4, 5).transform(Functions.squaredInteger());
        Verify.assertContainsAll(squareCollection, 1, 4, 9, 16, 25);
    }

    @Test
    public void withDefault()
    {
        Object expected = new Object();
        Assert.assertSame(expected, Functions.withDefault(Functions.getFixedValue(null), expected).valueOf(new Object()));

        Object expected2 = new Object();
        Assert.assertSame(expected2, Functions.withDefault(Functions.getFixedValue(expected2), expected).valueOf(new Object()));
    }
}
