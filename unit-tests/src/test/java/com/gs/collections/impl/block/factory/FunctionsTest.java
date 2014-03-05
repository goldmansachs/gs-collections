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

package com.gs.collections.impl.block.factory;

import java.util.Collections;
import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.function.primitive.DoubleFunctionImpl;
import com.gs.collections.impl.block.function.primitive.IntegerFunctionImpl;
import com.gs.collections.impl.block.function.primitive.LongFunctionImpl;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.SetAdapter;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Person;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

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
    public void getDefaultToString()
    {
        Function<Object, String> function = Functions.getNullSafeToString("N/A");
        Assert.assertEquals("1", function.valueOf(1));
        Assert.assertEquals("N/A", function.valueOf(null));
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
                Functions.getFixedValue(Lists.immutable.<String>of()),
                Functions.getFixedValue(Lists.immutable.of("hello")),
                Functions.getFixedValue(Lists.immutable.<String>of()));
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
    public void chainBoolean()
    {
        Function<String, Integer> toInteger = Functions.getStringToInteger();
        BooleanFunction<Integer> integerToBool = new BooleanFunction<Integer>()
        {
            public boolean booleanValueOf(Integer integerObject)
            {
                return integerObject.intValue() >= 0;
            }
        };
        Functions.BooleanFunctionChain<String, Integer> booleanFunctionChain = Functions.chainBoolean(toInteger, integerToBool);
        Assert.assertTrue(booleanFunctionChain.booleanValueOf("45"));
        Assert.assertFalse(booleanFunctionChain.booleanValueOf("-45"));
    }

    @Test
    public void chainByte()
    {
        Function<String, Integer> toInteger = Functions.getStringToInteger();
        ByteFunction<Integer> integerToByte = new ByteFunction<Integer>()
        {
            public byte byteValueOf(Integer integerObject)
            {
                return integerObject.byteValue();
            }
        };
        Functions.ByteFunctionChain<String, Integer> byteFunctionChain = Functions.chainByte(toInteger, integerToByte);
        Assert.assertEquals((byte) 45, byteFunctionChain.byteValueOf("45"));
        Assert.assertEquals((byte) -45, byteFunctionChain.byteValueOf("-45"));
    }

    @Test
    public void chainChar()
    {
        Function<Object, String> toString = Functions.getToString();
        CharFunction<String> stringToChar = new CharFunction<String>()
        {
            public char charValueOf(String stringObject)
            {
                return stringObject.charAt(0);
            }
        };
        Functions.CharFunctionChain<Object, String> charFunctionChain = Functions.chainChar(toString, stringToChar);
        Assert.assertEquals('g', charFunctionChain.charValueOf("gscollections"));
        Assert.assertEquals('-', charFunctionChain.charValueOf("-4"));
    }

    @Test
    public void chainDouble()
    {
        Function<String, Integer> toInteger = Functions.getStringToInteger();
        DoubleFunction<Integer> integerToDouble = new DoubleFunction<Integer>()
        {
            public double doubleValueOf(Integer integerObject)
            {
                return integerObject.doubleValue();
            }
        };
        Functions.DoubleFunctionChain<String, Integer> doubleFunctionChain = Functions.chainDouble(toInteger, integerToDouble);
        Assert.assertEquals(146.0, doubleFunctionChain.doubleValueOf("146"), 0.0);
        Assert.assertEquals(-456.0, doubleFunctionChain.doubleValueOf("-456"), 0.0);
    }

    @Test
    public void chainFloat()
    {
        Function<Integer, String> toString = new Function<Integer, String>()
        {
            public String valueOf(Integer object)
            {
                return String.valueOf(object);
            }
        };
        FloatFunction<String> stringToFloat = new FloatFunction<String>()
        {
            public float floatValueOf(String stringObject)
            {
                return Float.valueOf(stringObject).floatValue();
            }
        };
        Functions.FloatFunctionChain<Integer, String> floatFunctionChain = Functions.chainFloat(toString, stringToFloat);
        Assert.assertEquals(146.0, floatFunctionChain.floatValueOf(146), 0.0);
        Assert.assertEquals(-456.0, floatFunctionChain.floatValueOf(-456), 0.0);
    }

    @Test
    public void chainInt()
    {
        Function<Float, String> toString = new Function<Float, String>()
        {
            public String valueOf(Float object)
            {
                return String.valueOf(object);
            }
        };
        IntFunction<String> stringToLength = new IntegerFunctionImpl<String>()
        {
            public int intValueOf(String stringObject)
            {
                return stringObject.length();
            }
        };
        Functions.IntFunctionChain<Float, String> intFunctionChain = Functions.chainInt(toString, stringToLength);
        Assert.assertEquals(5, intFunctionChain.intValueOf(Float.valueOf(145)));
        Assert.assertEquals(6, intFunctionChain.intValueOf(Float.valueOf(-145)));
    }

    @Test
    public void chainLong()
    {
        Function<Float, String> toString = new Function<Float, String>()
        {
            public String valueOf(Float object)
            {
                return String.valueOf(object);
            }
        };
        LongFunction<String> stringToLengthLong = new LongFunction<String>()
        {
            public long longValueOf(String stringObject)
            {
                return Long.valueOf(stringObject.length()).longValue();
            }
        };
        Functions.LongFunctionChain<Float, String> longFunctionChain = Functions.chainLong(toString, stringToLengthLong);
        Assert.assertEquals(5L, longFunctionChain.longValueOf(Float.valueOf(145)));
        Assert.assertEquals(6L, longFunctionChain.longValueOf(Float.valueOf(-145)));
    }

    @Test
    public void chainShort()
    {
        Function<Integer, String> toString = new Function<Integer, String>()
        {
            public String valueOf(Integer object)
            {
                return String.valueOf(object);
            }
        };
        ShortFunction<String> stringToShort = new ShortFunction<String>()
        {
            public short shortValueOf(String stringObject)
            {
                return Short.valueOf(stringObject).shortValue();
            }
        };
        Functions.ShortFunctionChain<Integer, String> shortFunctionChain = Functions.chainShort(toString, stringToShort);
        Assert.assertEquals((short) 145, shortFunctionChain.shortValueOf(145));
        Assert.assertEquals((short) -145, shortFunctionChain.shortValueOf(-145));
    }

    @Test
    public void chain_two_chainBoolean()
    {
        Functions.FunctionChain<Boolean, String, Integer> chain = Functions.chain(BOOLEAN_STRING, STRING_LENGTH);
        BooleanFunction<Integer> integerToBool = new BooleanFunction<Integer>()
        {
            public boolean booleanValueOf(Integer integerObject)
            {
                return integerObject.intValue() >= 0;
            }
        };
        Functions.BooleanFunctionChain<Boolean, Integer> booleanChain = chain.chainBoolean(integerToBool);
        Assert.assertTrue(booleanChain.booleanValueOf(Boolean.TRUE));
    }

    @Test
    public void chain_two_chainByte()
    {
        Functions.FunctionChain<Boolean, String, Integer> chain = Functions.chain(BOOLEAN_STRING, STRING_LENGTH);
        ByteFunction<Integer> integerToByte = new ByteFunction<Integer>()
        {
            public byte byteValueOf(Integer integerObject)
            {
                return integerObject.byteValue();
            }
        };
        Functions.ByteFunctionChain<Boolean, Integer> byteChain = chain.chainByte(integerToByte);
        Assert.assertEquals((byte) 5, byteChain.byteValueOf(Boolean.FALSE));
    }

    @Test
    public void chain_three_chainChar()
    {
        Functions.FunctionChain<String, Boolean, String> chain = Functions.chain(STRING_LENGTH, IS_ODD).chain(BOOLEAN_STRING);
        CharFunction<String> stringToChar = new CharFunction<String>()
        {
            public char charValueOf(String stringObject)
            {
                return stringObject.charAt(0);
            }
        };
        Functions.CharFunctionChain<String, String> charChain = chain.chainChar(stringToChar);
        Assert.assertEquals('t', charChain.charValueOf("foo"));
    }

    @Test
    public void chain_three_chainDouble()
    {
        Functions.FunctionChain<Boolean, String, Integer> chain = Functions.chain(BOOLEAN_STRING, STRING_LENGTH);
        DoubleFunction<Integer> integerToDouble = new DoubleFunction<Integer>()
        {
            public double doubleValueOf(Integer integerObject)
            {
                return integerObject.doubleValue();
            }
        };
        Functions.DoubleFunctionChain<Boolean, Integer> doubleChain = chain.chainDouble(integerToDouble);
        Assert.assertEquals(4.0, doubleChain.doubleValueOf(Boolean.TRUE), 0.0);
    }

    @Test
    public void chain_three_chainFloat()
    {
        Functions.FunctionChain<String, Boolean, String> chain = Functions.chain(STRING_LENGTH, IS_ODD).chain(BOOLEAN_STRING);
        FloatFunction<String> stringToFloat = new FloatFunction<String>()
        {
            public float floatValueOf(String stringObject)
            {
                return Integer.valueOf(stringObject.length()).floatValue();
            }
        };
        Functions.FloatFunctionChain<String, String> floatChain = chain.chainFloat(stringToFloat);
        Assert.assertEquals(5.0, floatChain.floatValueOf("12.2"), 0);
    }

    @Test
    public void chain_three_chainInt()
    {
        Functions.FunctionChain<String, Boolean, String> chain = Functions.chain(STRING_LENGTH, IS_ODD).chain(BOOLEAN_STRING);
        IntFunction<String> stringToLength = new IntegerFunctionImpl<String>()
        {
            public int intValueOf(String stringObject)
            {
                return stringObject.length();
            }
        };
        Functions.IntFunctionChain<String, String> intChain = chain.chainInt(stringToLength);
        Assert.assertEquals(4, intChain.intValueOf("gsc"));
        Assert.assertNotEquals(4, intChain.intValueOf("kata"));
    }

    @Test
    public void chain_three_chainLong()
    {
        Functions.FunctionChain<String, Boolean, String> chain = Functions.chain(STRING_LENGTH, IS_ODD).chain(BOOLEAN_STRING);
        LongFunction<String> stringToLengthLong = new LongFunction<String>()
        {
            public long longValueOf(String stringObject)
            {
                return Long.valueOf(stringObject.length()).longValue();
            }
        };
        Functions.LongFunctionChain<String, String> longChain = chain.chainLong(stringToLengthLong);
        Assert.assertEquals(4L, longChain.longValueOf("gsc"));
        Assert.assertNotEquals(4L, longChain.longValueOf("kata"));
    }

    @Test
    public void chain_three_chainShort()
    {
        Functions.FunctionChain<String, Boolean, String> chain = Functions.chain(STRING_LENGTH, IS_ODD).chain(BOOLEAN_STRING);
        ShortFunction<String> stringToShort = new ShortFunction<String>()
        {
            public short shortValueOf(String stringObject)
            {
                return Integer.valueOf(stringObject.length()).shortValue();
            }
        };
        Functions.ShortFunctionChain<String, String> shortChain = chain.chainShort(stringToShort);
        Assert.assertEquals((short) 4, shortChain.shortValueOf("gsc"));
        Assert.assertNotEquals((short) 4, shortChain.shortValueOf("kata"));
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
        MutableList<Person> sorted = people.sortThisBy(Functions.pair(Person.TO_LAST, Person.TO_FIRST));
        Assert.assertEquals(FastList.newListWith(johnDoe, jane, john), sorted);
    }

    @Test
    public void key()
    {
        MutableMap<String, Integer> map = UnifiedMap.newWithKeysValues("One", 1);
        MutableSet<Map.Entry<String, Integer>> entries = SetAdapter.adapt(map.entrySet());
        MutableSet<String> keys = entries.collect(Functions.<String>getKeyFunction());
        Assert.assertEquals(UnifiedSet.newSetWith("One"), keys);
    }

    @Test
    public void value()
    {
        MutableMap<String, Integer> map = UnifiedMap.newWithKeysValues("One", 1);
        MutableSet<Map.Entry<String, Integer>> entries = SetAdapter.adapt(map.entrySet());
        MutableSet<Integer> values = entries.collect(Functions.<Integer>getValueFunction());
        Assert.assertEquals(UnifiedSet.newSetWith(1), values);
    }

    @Test
    public void size()
    {
        ImmutableList<ImmutableList<Integer>> list = Lists.immutable.of(Lists.immutable.of(1), Lists.immutable.of(1, 2), Lists.immutable.of(1, 2, 3));
        ImmutableList<Integer> sizes = list.collect(Functions.getSizeOf());
        Assert.assertEquals(FastList.newListWith(1, 2, 3), sizes);
    }

    @Test
    public void squaredCollection()
    {
        MutableCollection<Integer> squareCollection = FastList.newListWith(1, 2, 3, 4, 5).collect(Functions.squaredInteger());
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

    @Test
    public void nullSafe()
    {
        Object expected = new Object();
        Function<Object, Object> throwsFunction = new ThrowsFunction();
        Assert.assertSame(expected, Functions.nullSafe(throwsFunction, expected).valueOf(null));
        Assert.assertSame(expected, Functions.nullSafe(Functions.getFixedValue(expected)).valueOf(new Object()));
        Assert.assertNull(Functions.nullSafe(throwsFunction).valueOf(null));
    }

    @Test
    public void classForName()
    {
        Class<?> objectClass = Functions.classForName().valueOf("java.lang.Object");
        Assert.assertSame(Object.class, objectClass);
    }

    @Test
    public void bind_function2_parameter()
    {
        MutableCollection<Integer> multiplied = FastList.newListWith(1, 2, 3, 4, 5).collect(Functions.bind(new Function2<Integer, Integer, Integer>()
        {
            public Integer value(Integer value, Integer parameter)
            {
                return value * parameter;
            }
        }, 2));
        Verify.assertContainsAll(multiplied, 2, 4, 6, 8, 10);
    }

    private static class ThrowsFunction implements Function<Object, Object>
    {
        public Object valueOf(Object object)
        {
            throw new RuntimeException();
        }
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(Functions.class);
    }
}
