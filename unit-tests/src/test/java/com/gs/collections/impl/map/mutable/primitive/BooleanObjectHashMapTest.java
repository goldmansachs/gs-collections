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

package com.gs.collections.impl.map.mutable.primitive;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.BooleanObjectPredicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.BooleanObjectProcedure;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions0;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class BooleanObjectHashMapTest
{
    private final BooleanObjectHashMap<String> map0 = new BooleanObjectHashMap<String>();
    private final BooleanObjectHashMap<String> map1 = BooleanObjectHashMap.newWithKeysValues(false, "0");
    private final BooleanObjectHashMap<String> map2 = BooleanObjectHashMap.newWithKeysValues(true, "1");
    private final BooleanObjectHashMap<String> map3 = BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0");

    @Test
    public void removeKey()
    {
        Assert.assertNull(this.map0.removeKey(true));
        Assert.assertNull(this.map0.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map0);

        Assert.assertNull(this.map1.removeKey(true));
        Assert.assertEquals("0", this.map1.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map1);

        Assert.assertNull(this.map2.removeKey(false));
        Assert.assertEquals("1", this.map2.removeKey(true));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map2);

        Assert.assertEquals("1", this.map3.removeKey(true));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map3);
        Assert.assertEquals("0", this.map3.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map3);
        Verify.assertIterableEmpty(this.map3);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0");
        Assert.assertEquals("0", map.removeKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), map);
    }

    @Test
    public void put()
    {
        Assert.assertNull(this.map0.put(true, "1"));
        Assert.assertFalse(this.map0.isEmpty());
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map0);
        Assert.assertNull(this.map0.put(false, "0"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertNull(map.put(false, "0"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), map);

        Assert.assertNull(this.map1.put(true, "1"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map1);
        Assert.assertEquals("0", this.map1.put(false, "2"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "2"), this.map1);

        Assert.assertNull(this.map2.put(false, "0"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map2);
        Assert.assertEquals("1", this.map2.put(true, "2"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "2", false, "0"), this.map2);

        Assert.assertEquals("1", this.map3.put(true, "2"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "2", false, "0"), this.map3);
        Assert.assertEquals("0", this.map3.put(false, "3"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "2", false, "3"), this.map3);
    }

    @Test
    public void getIfAbsentPut()
    {
        Function0<String> trueString = new Function0<String>()
        {
            public String value()
            {
                return "true";
            }
        };

        Assert.assertEquals("true", this.map0.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "true"), this.map0);

        Function0<String> falseString = new Function0<String>()
        {
            public String value()
            {
                return "false";
            }
        };
        Assert.assertEquals("false", this.map0.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "true", false, "false"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertEquals("false", map.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "false"), map);

        Assert.assertEquals("0", this.map1.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1);
        Assert.assertEquals("true", this.map1.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "true", false, "0"), this.map1);

        Assert.assertEquals("1", this.map2.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2);
        Assert.assertEquals("false", this.map2.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "false"), this.map2);

        Assert.assertEquals("0", this.map3.getIfAbsentPut(false, falseString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);
        Assert.assertEquals("1", this.map3.getIfAbsentPut(true, trueString));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);

    }

    @Test
    public void getIfAbsentPutWith()
    {
        Function<String, String> toUpperCase = new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.toUpperCase();
            }
        };

        Assert.assertEquals("TRUE", this.map0.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE"), this.map0);
        Assert.assertEquals("FALSE", this.map0.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "FALSE"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertEquals("FALSE", map.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "FALSE"), map);

        Assert.assertEquals("0", this.map1.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1);
        Assert.assertEquals("TRUE", this.map1.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "0"), this.map1);

        Assert.assertEquals("1", this.map2.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2);

        Assert.assertEquals("FALSE", this.map2.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "FALSE"), this.map2);

        Assert.assertEquals("0", this.map3.getIfAbsentPutWith(false, toUpperCase, "false"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);

        Assert.assertEquals("1", this.map3.getIfAbsentPutWith(true, toUpperCase, "true"));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);
    }

    @Test
    public void getIfAbsentPutWithKey()
    {
        BooleanToObjectFunction<String> toUpperCase = new BooleanToObjectFunction<String>()
        {
            public String valueOf(boolean booleanParameter)
            {
                return String.valueOf(booleanParameter).toUpperCase();
            }
        };

        Assert.assertEquals("TRUE", this.map0.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE"), this.map0);
        Assert.assertEquals("FALSE", this.map0.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "FALSE"), this.map0);

        BooleanObjectHashMap<String> map = BooleanObjectHashMap.newMap();
        Assert.assertEquals("FALSE", map.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "FALSE"), map);

        Assert.assertEquals("0", this.map1.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1);
        Assert.assertEquals("TRUE", this.map1.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "TRUE", false, "0"), this.map1);

        Assert.assertEquals("1", this.map2.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2);

        Assert.assertEquals("FALSE", this.map2.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "FALSE"), this.map2);

        Assert.assertEquals("0", this.map3.getIfAbsentPutWithKey(false, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);

        Assert.assertEquals("1", this.map3.getIfAbsentPutWithKey(true, toUpperCase));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1", false, "0"), this.map3);
    }

    @Test
    public void get()
    {
        Assert.assertNull(this.map0.get(true));
        Assert.assertNull(this.map0.get(false));

        Assert.assertNull(this.map1.get(true));
        Assert.assertEquals("0", this.map1.get(false));

        Assert.assertNull(this.map2.get(false));
        Assert.assertEquals("1", this.map2.get(true));

        Assert.assertEquals("0", this.map3.get(false));
        Assert.assertEquals("1", this.map3.get(true));
    }

    @Test
    public void clear()
    {
        this.map0.clear();
        this.map1.clear();
        this.map2.clear();
        this.map3.clear();
        Verify.assertIterableEmpty(this.map0);
        Verify.assertIterableEmpty(this.map1);
        Verify.assertIterableEmpty(this.map2);
        Verify.assertIterableEmpty(this.map3);
    }

    @Test
    public void containsKey()
    {
        Assert.assertFalse(this.map0.containsKey(true));
        Assert.assertFalse(this.map0.containsKey(false));

        Assert.assertNull(this.map0.put(true, ""));
        Assert.assertTrue(this.map0.containsKey(true));

        Assert.assertNull(this.map0.put(false, ""));
        Assert.assertTrue(this.map0.containsKey(true));

        Assert.assertFalse(this.map1.containsKey(true));
        Assert.assertTrue(this.map1.containsKey(false));

        Assert.assertFalse(this.map2.containsKey(false));
        Assert.assertTrue(this.map2.containsKey(true));

        Assert.assertTrue(this.map3.containsKey(true));
        Assert.assertTrue(this.map3.containsKey(false));
    }

    @Test
    public void containsValue()
    {
        Assert.assertFalse(this.map3.containsValue(null));
        Assert.assertTrue(this.map3.containsValue("0"));
        Assert.assertTrue(this.map3.containsValue("1"));
        Assert.assertFalse(this.map3.containsValue("2"));

        Assert.assertEquals("1", this.map3.put(true, null));
        Assert.assertTrue(this.map3.containsValue(null));
        Assert.assertNull(this.map3.removeKey(true));

        Assert.assertEquals("0", this.map3.put(false, null));
        Assert.assertTrue(this.map3.containsValue(null));
    }

    @Test
    public void size()
    {
        Verify.assertIterableSize(0, this.map0);
        this.map0.put(false, "");
        Verify.assertIterableSize(1, this.map0);
        Verify.assertIterableSize(1, this.map1);
        Verify.assertIterableSize(1, this.map2);
        Verify.assertIterableSize(2, this.map3);
        this.map3.removeKey(true);
        Verify.assertIterableSize(1, this.map3);
    }

    @Test
    public void updateValue()
    {
        Function<Integer, Integer> incrementFunction = new Function<Integer, Integer>()
        {
            public Integer valueOf(Integer integer)
            {
                return integer + 1;
            }
        };
        Function0<Integer> zeroFactory = Functions0.value(0);

        BooleanObjectHashMap<Integer> map1 = new BooleanObjectHashMap<Integer>();
        Assert.assertEquals(Integer.valueOf(1), map1.updateValue(false, zeroFactory, incrementFunction));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 1), map1);
        Assert.assertEquals(Integer.valueOf(2), map1.updateValue(false, zeroFactory, incrementFunction));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 2), map1);
        Assert.assertEquals(Integer.valueOf(1), map1.updateValue(true, zeroFactory, incrementFunction));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 2, true, 1), map1);
        Assert.assertEquals(Integer.valueOf(2), map1.updateValue(true, zeroFactory, incrementFunction));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 2, true, 2), map1);
    }

    @Test
    public void updateValueWith()
    {
        Function2<Integer, Integer, Integer> incrementFunction = AddFunction.INTEGER;
        Function0<Integer> zeroFactory = Functions0.value(0);

        BooleanObjectHashMap<Integer> map1 = new BooleanObjectHashMap<Integer>();
        Assert.assertEquals(Integer.valueOf(1), map1.updateValueWith(false, zeroFactory, incrementFunction, 1));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 1), map1);
        Assert.assertEquals(Integer.valueOf(2), map1.updateValueWith(false, zeroFactory, incrementFunction, 1));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 2), map1);
        Assert.assertEquals(Integer.valueOf(1), map1.updateValueWith(true, zeroFactory, incrementFunction, 1));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 2, true, 1), map1);
        Assert.assertEquals(Integer.valueOf(2), map1.updateValueWith(true, zeroFactory, incrementFunction, 1));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, 2, true, 2), map1);
    }

    @Test
    public void select()
    {
        BooleanObjectPredicate<String> trueAnd1 = new BooleanObjectPredicate<String>()
        {
            public boolean accept(boolean value, String object)
            {
                return value && "1".equals(object);
            }
        };
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map0.select(trueAnd1));

        BooleanObjectPredicate<String> trueAnd0 = new BooleanObjectPredicate<String>()
        {
            public boolean accept(boolean value, String object)
            {
                return value && "0".equals(object);
            }
        };
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map1.select(trueAnd0));
        BooleanObjectPredicate<String> falseAnd0 = new BooleanObjectPredicate<String>()
        {
            public boolean accept(boolean value, String object)
            {
                return !value && "0".equals(object);
            }
        };
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1.select(falseAnd0));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map2.select(trueAnd0));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2.select(trueAnd1));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map3.select(trueAnd0));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map3.select(trueAnd1));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map3.select(falseAnd0));

        Assert.assertEquals(FastList.newList(), this.map0.select(Predicates.equal("0")));
        Assert.assertEquals(FastList.newList(), this.map1.select(Predicates.equal("1")));
        Assert.assertEquals(FastList.newListWith("0"), this.map1.select(Predicates.equal("0")));
        Assert.assertEquals(FastList.newList(), this.map2.select(Predicates.equal("0")));
        Assert.assertEquals(FastList.newListWith("1"), this.map2.select(Predicates.equal("1")));
        Assert.assertEquals(FastList.newList(), this.map3.select(Predicates.equal("5")));
        Assert.assertEquals(FastList.newListWith("0"), this.map3.select(Predicates.equal("0")));
        Assert.assertEquals(FastList.newListWith("1"), this.map3.select(Predicates.equal("1")));

        Assert.assertEquals(FastList.newList(), this.map0.select(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newList(), this.map3.select(Predicates.equal("5"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0"), this.map3.select(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map3.select(Predicates.equal("1"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newList(), this.map1.select(Predicates.equal("1"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0"), this.map1.select(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newList(), this.map2.select(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map2.select(Predicates.equal("1"), FastList.<String>newList()));
    }

    @Test
    public void reject()
    {
        BooleanObjectPredicate<String> trueAnd1 = new BooleanObjectPredicate<String>()
        {
            public boolean accept(boolean value, String object)
            {
                return value && "1".equals(object);
            }
        };
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map0.reject(trueAnd1));

        BooleanObjectPredicate<String> trueAnd0 = new BooleanObjectPredicate<String>()
        {
            public boolean accept(boolean value, String object)
            {
                return value && "0".equals(object);
            }
        };
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map1.reject(trueAnd0));
        BooleanObjectPredicate<String> falseAnd0 = new BooleanObjectPredicate<String>()
        {
            public boolean accept(boolean value, String object)
            {
                return !value && "0".equals(object);
            }
        };
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map1.reject(falseAnd0));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map2.reject(trueAnd0));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), this.map2.reject(trueAnd1));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0", true, "1"), this.map3.reject(trueAnd0));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(false, "0"), this.map3.reject(trueAnd1));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "1"), this.map3.reject(falseAnd0));

        Assert.assertEquals(FastList.newList(), this.map0.reject(Predicates.equal("0")));
        Assert.assertEquals(FastList.newListWith("0"), this.map1.reject(Predicates.equal("1")));
        Assert.assertEquals(FastList.newList(), this.map1.reject(Predicates.equal("0")));
        Assert.assertEquals(FastList.newListWith("1"), this.map2.reject(Predicates.equal("0")));
        Assert.assertEquals(FastList.newList(), this.map2.reject(Predicates.equal("1")));
        Assert.assertEquals(FastList.newListWith("0", "1"), this.map3.reject(Predicates.equal("5")));
        Assert.assertEquals(FastList.newListWith("1"), this.map3.reject(Predicates.equal("0")));
        Assert.assertEquals(FastList.newListWith("0"), this.map3.reject(Predicates.equal("1")));

        Assert.assertEquals(FastList.newList(), this.map0.reject(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0"), this.map1.reject(Predicates.equal("1"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newList(), this.map1.reject(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map2.reject(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newList(), this.map2.reject(Predicates.equal("1"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0", "1"), this.map3.reject(Predicates.equal("5"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map3.reject(Predicates.equal("0"), FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0"), this.map3.reject(Predicates.equal("1"), FastList.<String>newList()));
    }

    @Test
    public void forEachWithIndex()
    {
        final String[] concat = {"", "", "", ""};

        this.map0.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String each, int index)
            {
                concat[0] += each;
                concat[0] += index;
            }
        });
        this.map1.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String each, int index)
            {
                concat[1] += each;
                concat[1] += index;
            }
        });
        this.map2.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String each, int index)
            {
                concat[2] += each;
                concat[2] += index;
            }
        });
        this.map3.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String each, int index)
            {
                concat[3] += each;
                concat[3] += index;
            }
        });

        Assert.assertEquals("", concat[0]);
        Assert.assertEquals("00", concat[1]);
        Assert.assertEquals("10", concat[2]);
        Assert.assertTrue(concat[3], "0011".equals(concat[3]) || "1001".equals(concat[3]));
    }

    @Test
    public void forEachValue()
    {
        final String[] concat = {"", "", "", ""};

        this.map0.forEachValue(new Procedure<String>()
        {
            public void value(String each)
            {
                concat[0] += each;
            }
        });
        this.map1.forEachValue(new Procedure<String>()
        {
            public void value(String each)
            {
                concat[1] += each;
            }
        });
        this.map2.forEachValue(new Procedure<String>()
        {
            public void value(String each)
            {
                concat[2] += each;
            }
        });
        this.map3.forEachValue(new Procedure<String>()
        {
            public void value(String each)
            {
                concat[3] += each;
            }
        });

        Assert.assertEquals("", concat[0]);
        Assert.assertEquals("0", concat[1]);
        Assert.assertEquals("1", concat[2]);
        Assert.assertTrue(concat[3], "01".equals(concat[3]) || "10".equals(concat[3]));
    }

    @Test
    public void forEachKey()
    {
        final String[] concat = {"", "", "", ""};

        this.map0.forEachKey(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                concat[0] += each;
            }
        });
        this.map1.forEachKey(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                concat[1] += each;
            }
        });
        this.map2.forEachKey(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                concat[2] += each;
            }
        });
        this.map3.forEachKey(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                concat[3] += each;
            }
        });

        Assert.assertEquals("", concat[0]);
        Assert.assertEquals("false", concat[1]);
        Assert.assertEquals("true", concat[2]);
        Assert.assertTrue(concat[3], "falsetrue".equals(concat[3]) || "truefalse".equals(concat[3]));
    }

    @Test
    public void forEachKeyValue()
    {
        final String[] concat = {"", "", "", ""};

        this.map0.forEachKeyValue(new BooleanObjectProcedure<String>()
        {
            public void value(boolean each, String parameter)
            {
                concat[0] += each;
                concat[0] += parameter;
            }
        });
        this.map1.forEachKeyValue(new BooleanObjectProcedure<String>()
        {
            public void value(boolean each, String parameter)
            {
                concat[1] += each;
                concat[1] += parameter;
            }
        });
        this.map2.forEachKeyValue(new BooleanObjectProcedure<String>()
        {
            public void value(boolean each, String parameter)
            {
                concat[2] += each;
                concat[2] += parameter;
            }
        });
        this.map3.forEachKeyValue(new BooleanObjectProcedure<String>()
        {
            public void value(boolean each, String parameter)
            {
                concat[3] += each;
                concat[3] += parameter;
            }
        });

        Assert.assertEquals("", concat[0]);
        Assert.assertEquals("false0", concat[1]);
        Assert.assertEquals("true1", concat[2]);
        Assert.assertTrue(concat[3], "false0true1".equals(concat[3]) || "true1false0".equals(concat[3]));
    }

    @Test
    public void isEmpty()
    {
        Verify.assertIterableEmpty(this.map0);
        Assert.assertFalse(this.map1.isEmpty());
        Assert.assertFalse(this.map2.isEmpty());
        Assert.assertFalse(this.map3.isEmpty());
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.map0.notEmpty());
        Assert.assertTrue(this.map1.notEmpty());
        Assert.assertTrue(this.map2.notEmpty());
        Assert.assertTrue(this.map3.notEmpty());
    }

    @Test
    public void getFirst()
    {
        Assert.assertNull(this.map0.getFirst());
        Assert.assertEquals("0", this.map1.getFirst());
        Assert.assertEquals("1", this.map2.getFirst());
        Assert.assertTrue(this.map3.getFirst(), "0".equals(this.map3.getFirst()) || "1".equals(this.map3.getFirst()));
    }

    @Test
    public void getLast()
    {
        Assert.assertNull(this.map0.getLast());
        Assert.assertEquals("0", this.map1.getLast());
        Assert.assertEquals("1", this.map2.getLast());
        Assert.assertTrue(this.map3.getLast(), "0".equals(this.map3.getLast()) || "1".equals(this.map3.getLast()));
    }

    @Test
    public void contains()
    {
        Assert.assertFalse(this.map0.contains(null));
        Assert.assertTrue(this.map1.contains("0"));
        Assert.assertTrue(this.map2.contains("1"));
        Assert.assertTrue(this.map3.contains("0"));
        Assert.assertTrue(this.map3.contains("1"));

        Assert.assertNull(this.map0.put(true, null));
        Assert.assertTrue(this.map0.contains(null));
        Assert.assertNull(this.map0.removeKey(true));
        Assert.assertFalse(this.map0.contains(null));

        Assert.assertFalse(this.map1.contains("5"));
        Assert.assertFalse(this.map2.contains("5"));
        Assert.assertFalse(this.map3.contains("5"));
    }

    @Test
    public void containsAllIterable()
    {
        Assert.assertTrue(this.map3.containsAllIterable(FastList.newListWith("0", "1")));
        Assert.assertTrue(this.map1.containsAllIterable(FastList.newListWith("0")));
        Assert.assertFalse(this.map0.containsAllIterable(FastList.newListWith("0", "1")));
        Assert.assertFalse(this.map2.containsAllIterable(FastList.newListWith("1", "5", "9")));
    }

    @Test
    public void containsAll()
    {
        Assert.assertTrue(this.map3.containsAll(FastList.newListWith("0", "1")));
        Assert.assertTrue(this.map1.containsAll(FastList.newListWith("0")));
        Assert.assertFalse(this.map0.containsAll(FastList.newListWith("0", "1")));
        Assert.assertFalse(this.map2.containsAll(FastList.newListWith("1", "5", "9")));
    }

    @Test
    public void containsAllArguments()
    {
        Assert.assertTrue(this.map3.containsAllArguments("0", "1"));
        Assert.assertTrue(this.map1.containsAllArguments("0"));
        Assert.assertFalse(this.map0.containsAllArguments("0", "1"));
        Assert.assertFalse(this.map2.containsAllArguments("1", "5", "9"));
    }

    @Test
    public void selectWith()
    {
        Predicate2<String, Integer> isEven = new Predicate2<String, Integer>()
        {
            public boolean accept(String argument1, Integer argument2)
            {
                return (Integer.parseInt(argument1) & (argument2 - 1)) == 0;
            }
        };
        Assert.assertEquals(FastList.newListWith(), this.map0.selectWith(isEven, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0"), this.map1.selectWith(isEven, 2, FastList.<String>newList()));

        Predicate2<String, Integer> isOdd = new Predicate2<String, Integer>()
        {
            public boolean accept(String argument1, Integer argument2)
            {
                return (Integer.parseInt(argument1) & (argument2 - 1)) != 0;
            }
        };
        Assert.assertEquals(FastList.newListWith(), this.map1.selectWith(isOdd, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith(), this.map2.selectWith(isEven, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map2.selectWith(isOdd, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map3.selectWith(isOdd, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0"), this.map3.selectWith(isEven, 2, FastList.<String>newList()));
    }

    @Test
    public void rejectWith()
    {
        Predicate2<String, Integer> isEven = new Predicate2<String, Integer>()
        {
            public boolean accept(String argument1, Integer argument2)
            {
                return (Integer.parseInt(argument1) & (argument2 - 1)) == 0;
            }
        };
        Assert.assertEquals(FastList.newListWith(), this.map0.rejectWith(isEven, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith(), this.map1.rejectWith(isEven, 2, FastList.<String>newList()));

        Predicate2<String, Integer> isOdd = new Predicate2<String, Integer>()
        {
            public boolean accept(String argument1, Integer argument2)
            {
                return (Integer.parseInt(argument1) & (argument2 - 1)) != 0;
            }
        };
        Assert.assertEquals(FastList.newListWith("0"), this.map1.rejectWith(isOdd, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map2.rejectWith(isEven, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith(), this.map2.rejectWith(isOdd, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0"), this.map3.rejectWith(isOdd, 2, FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1"), this.map3.rejectWith(isEven, 2, FastList.<String>newList()));
    }

    @Test
    public void selectInstancesOf()
    {
        BooleanObjectHashMap<Number> numbers = BooleanObjectHashMap.<Number>newWithKeysValues(true, 0, false, 1.0);

        Assert.assertEquals(FastList.newListWith(0), numbers.selectInstancesOf(Integer.class));
        Assert.assertEquals(FastList.newListWith(1.0), numbers.selectInstancesOf(Double.class));
        Assert.assertEquals(FastList.newListWith(), numbers.selectInstancesOf(Float.class));
        Assert.assertTrue(FastList.newListWith(0, 1.0).equals(numbers.selectInstancesOf(Number.class))
                || FastList.newListWith(1.0, 0).equals(numbers.selectInstancesOf(Number.class)));
    }

    @Test
    public void partition()
    {
        PartitionIterable<String> partition = this.map3.partition(Predicates.equal("0"));
        Assert.assertEquals(FastList.newListWith("0"), partition.getSelected());
        Assert.assertEquals(FastList.newListWith("1"), partition.getRejected());
    }

    @Test
    public void detect()
    {
        Assert.assertNull(this.map0.detect(Predicates.equal("0")));
        Assert.assertEquals("0", this.map1.detect(Predicates.equal("0")));
        Assert.assertNull(this.map2.detect(Predicates.equal("0")));
        Assert.assertEquals("0", this.map3.detect(Predicates.equal("0")));

        Assert.assertNull(this.map0.detect(Predicates.equal("1")));
        Assert.assertNull(this.map1.detect(Predicates.equal("1")));
        Assert.assertEquals("1", this.map2.detect(Predicates.equal("1")));
        Assert.assertEquals("1", this.map3.detect(Predicates.equal("1")));
    }

    @Test
    public void detectIfNone()
    {
        Function0<String> string5 = new Function0<String>()
        {
            public String value()
            {
                return "5";
            }
        };
        Assert.assertEquals("5", this.map0.detectIfNone(Predicates.equal("0"), string5));
        Assert.assertEquals("0", this.map1.detectIfNone(Predicates.equal("0"), string5));
        Assert.assertEquals("5", this.map2.detectIfNone(Predicates.equal("0"), string5));
        Assert.assertEquals("0", this.map3.detectIfNone(Predicates.equal("0"), string5));

        Assert.assertEquals("5", this.map0.detectIfNone(Predicates.equal("1"), string5));
        Assert.assertEquals("1", this.map2.detectIfNone(Predicates.equal("1"), string5));
        Assert.assertEquals("5", this.map1.detectIfNone(Predicates.equal("1"), string5));
        Assert.assertEquals("1", this.map3.detectIfNone(Predicates.equal("1"), string5));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(0L, this.map0.count(Predicates.equal("0")));
        Assert.assertEquals(1L, this.map1.count(Predicates.equal("0")));
        Assert.assertEquals(0L, this.map2.count(Predicates.equal("0")));
        Assert.assertEquals(1L, this.map3.count(Predicates.equal("0")));

        Assert.assertEquals(0L, this.map0.count(Predicates.equal("1")));
        Assert.assertEquals(0L, this.map1.count(Predicates.equal("1")));
        Assert.assertEquals(1L, this.map2.count(Predicates.equal("1")));
        Assert.assertEquals(1L, this.map3.count(Predicates.equal("1")));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.map0.anySatisfy(Predicates.equal("0")));
        Assert.assertTrue(this.map1.anySatisfy(Predicates.equal("0")));
        Assert.assertFalse(this.map2.anySatisfy(Predicates.equal("0")));
        Assert.assertTrue(this.map3.anySatisfy(Predicates.equal("0")));

        Assert.assertFalse(this.map0.anySatisfy(Predicates.equal("1")));
        Assert.assertTrue(this.map2.anySatisfy(Predicates.equal("1")));
        Assert.assertFalse(this.map1.anySatisfy(Predicates.equal("1")));
        Assert.assertTrue(this.map3.anySatisfy(Predicates.equal("1")));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.map0.allSatisfy(Predicates.equal("0")));
        Assert.assertTrue(this.map1.allSatisfy(Predicates.equal("0")));
        Assert.assertFalse(this.map2.allSatisfy(Predicates.equal("0")));
        Assert.assertFalse(this.map3.allSatisfy(Predicates.equal("0")));

        Assert.assertTrue(this.map0.allSatisfy(Predicates.equal("1")));
        Assert.assertTrue(this.map2.allSatisfy(Predicates.equal("1")));
        Assert.assertFalse(this.map1.allSatisfy(Predicates.equal("1")));
        Assert.assertFalse(this.map3.allSatisfy(Predicates.equal("1")));
    }

    @Test
    public void injectInto()
    {
        Function2<String, String, String> concat = new Function2<String, String, String>()
        {
            public String value(String argument1, String argument2)
            {
                return argument1 + '-' + argument2;
            }
        };

        Assert.assertEquals("Start", this.map0.injectInto("Start", concat));
        Assert.assertEquals("Start-0", this.map1.injectInto("Start", concat));
        Assert.assertEquals("Start-1", this.map2.injectInto("Start", concat));
        Assert.assertTrue(this.map3.injectInto("Start", concat),
                "Start-0-1".equals(this.map3.injectInto("Start", concat))
                        || "Start-1-0".equals(this.map3.injectInto("Start", concat)));
    }

    @Test
    public void intInjectInto()
    {
        IntObjectToIntFunction<String> function = new IntObjectToIntFunction<String>()
        {
            public int intValueOf(int intParameter, String objectParameter)
            {
                return intParameter + objectParameter.length();
            }
        };
        Assert.assertEquals(1, this.map0.injectInto(1, function));
        Assert.assertEquals(2, this.map1.injectInto(1, function));
        Assert.assertEquals(2, this.map2.injectInto(1, function));
        Assert.assertEquals(3, this.map3.injectInto(1, function));
    }

    @Test
    public void longInjectInto()
    {
        LongObjectToLongFunction<String> function = new LongObjectToLongFunction<String>()
        {
            public long longValueOf(long longParameter, String objectParameter)
            {
                return longParameter + objectParameter.length();
            }
        };
        Assert.assertEquals(1L, this.map0.injectInto(1L, function));
        Assert.assertEquals(2L, this.map1.injectInto(1L, function));
        Assert.assertEquals(2L, this.map2.injectInto(1L, function));
        Assert.assertEquals(3L, this.map3.injectInto(1L, function));
    }

    @Test
    public void floatInjectInto()
    {
        FloatObjectToFloatFunction<String> function = new FloatObjectToFloatFunction<String>()
        {
            public float floatValueOf(float floatParameter, String objectParameter)
            {
                return floatParameter + objectParameter.length();
            }
        };
        Assert.assertEquals(1.0f, this.map0.injectInto(1.0f, function), 0.0f);
        Assert.assertEquals(2.0f, this.map1.injectInto(1.0f, function), 0.0f);
        Assert.assertEquals(2.0f, this.map2.injectInto(1.0f, function), 0.0f);
        Assert.assertEquals(3.0f, this.map3.injectInto(1.0f, function), 0.0f);
    }

    @Test
    public void doubleInjectInto()
    {
        DoubleObjectToDoubleFunction<String> function = new DoubleObjectToDoubleFunction<String>()
        {
            public double doubleValueOf(double doubleParameter, String objectParameter)
            {
                return doubleParameter + objectParameter.length();
            }
        };
        Assert.assertEquals(1.0, this.map0.injectInto(1.0, function), 0.0);
        Assert.assertEquals(2.0, this.map1.injectInto(1.0, function), 0.0);
        Assert.assertEquals(2.0, this.map2.injectInto(1.0, function), 0.0);
        Assert.assertEquals(3.0, this.map3.injectInto(1.0, function), 0.0);
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(FastList.newList(), this.map0.toList());
        Assert.assertEquals(FastList.newListWith("0"), this.map1.toList());
        Assert.assertEquals(FastList.newListWith("1"), this.map2.toList());
        Assert.assertTrue(FastList.newListWith("0", "1").equals(this.map3.toList())
                || FastList.newListWith("1", "0").equals(this.map3.toList()));
    }

    @Test
    public void toSortedList()
    {
        Assert.assertEquals(FastList.newList(), this.map0.toSortedList());
        Assert.assertEquals(FastList.newListWith("0"), this.map1.toSortedList());
        Assert.assertEquals(FastList.newListWith("1"), this.map2.toSortedList());
        Assert.assertEquals(FastList.newListWith("0", "1"), this.map3.toSortedList());
        Assert.assertEquals(FastList.newListWith("1", "0"), this.map3.toSortedList(Comparators.reverseNaturalOrder()));
    }

    @Test
    public void toSortedListBy()
    {
        Assert.assertEquals(FastList.newListWith("one", "zero"), BooleanObjectHashMap.newWithKeysValues(false, "zero", true, "one").toSortedListBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(2);
            }
        }));
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(UnifiedSet.newSet(), this.map0.toSet());
        Assert.assertEquals(UnifiedSet.newSetWith("0"), this.map1.toSet());
        Assert.assertEquals(UnifiedSet.newSetWith("1"), this.map2.toSet());
        Assert.assertEquals(UnifiedSet.newSetWith("0", "1"), this.map3.toSet());
    }

    @Test
    public void toSortedSet()
    {
        Assert.assertEquals(TreeSortedSet.newSet(), this.map0.toSortedSet());
        Assert.assertEquals(TreeSortedSet.newSetWith("0"), this.map1.toSortedSet());
        Assert.assertEquals(TreeSortedSet.newSetWith("1"), this.map2.toSortedSet());
        Assert.assertEquals(TreeSortedSet.newSetWith("0", "1"), this.map3.toSortedSet());

        Assert.assertEquals(TreeSortedSet.newSetWith("zero", "one"), BooleanObjectHashMap.newWithKeysValues(false, "zero", true, "one").toSortedSetBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(1);
            }
        }));
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(HashBag.newBag(), this.map0.toBag());
        Assert.assertEquals(HashBag.newBagWith("0"), this.map1.toBag());
        Assert.assertEquals(HashBag.newBagWith("1"), this.map2.toBag());
        Assert.assertEquals(HashBag.newBagWith("0", "1"), this.map3.toBag());
    }

    @Test
    public void toMap()
    {
        Function<String, Integer> keyfunction = new Function<String, Integer>()
        {
            public Integer valueOf(String object)
            {
                return Integer.parseInt(object);
            }
        };
        Function<String, Integer> valueFunction = new Function<String, Integer>()
        {
            public Integer valueOf(String object)
            {
                return Integer.parseInt(object) - 1;
            }
        };
        Assert.assertEquals(UnifiedMap.newMap(), this.map0.toMap(keyfunction, valueFunction));
        Assert.assertEquals(UnifiedMap.newWithKeysValues(0, -1), this.map1.toMap(keyfunction, valueFunction));
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, 0), this.map2.toMap(keyfunction, valueFunction));
        Assert.assertEquals(UnifiedMap.newWithKeysValues(0, -1, 1, 0), this.map3.toMap(keyfunction, valueFunction));
    }

    @Test
    public void toSortedMap()
    {
        Function<String, Integer> keyfunction = new Function<String, Integer>()
        {
            public Integer valueOf(String object)
            {
                return Integer.parseInt(object);
            }
        };
        Function<String, Integer> valueFunction = new Function<String, Integer>()
        {
            public Integer valueOf(String object)
            {
                return Integer.parseInt(object) - 1;
            }
        };
        Assert.assertEquals(TreeSortedMap.newMap(), this.map0.toSortedMap(keyfunction, valueFunction));
        Assert.assertEquals(TreeSortedMap.newMapWith(0, -1), this.map1.toSortedMap(keyfunction, valueFunction));
        Assert.assertEquals(TreeSortedMap.newMapWith(1, 0), this.map2.toSortedMap(keyfunction, valueFunction));
        Assert.assertEquals(TreeSortedMap.newMapWith(0, -1, 1, 0), this.map3.toSortedMap(keyfunction, valueFunction));
        Assert.assertEquals(TreeSortedMap.newMapWith(1, 0, 0, -1), this.map3.toSortedMap(Comparators.reverseNaturalOrder(), keyfunction, valueFunction));
    }

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(new String[0], this.map0.toArray());
        Assert.assertArrayEquals(new String[]{"0"}, this.map1.toArray());
        Assert.assertArrayEquals(new String[]{"1"}, this.map2.toArray());
        Assert.assertTrue(Arrays.equals(new String[]{"0", "1"}, this.map3.toArray())
                || Arrays.equals(new String[]{"1", "0"}, this.map3.toArray()));

        Assert.assertArrayEquals(new String[2], this.map0.toArray(new String[2]));
        Assert.assertArrayEquals(new String[]{"0"}, this.map1.toArray(new String[0]));
        Assert.assertArrayEquals(new String[]{"1"}, this.map2.toArray(new String[1]));
        Assert.assertTrue(Arrays.equals(new String[]{"0", "1"}, this.map3.toArray(new String[2]))
                || Arrays.equals(new String[]{"1", "0"}, this.map3.toArray(new String[2])));
    }

    @Test
    public void min()
    {
        Assert.assertEquals("0", this.map1.min());
        Assert.assertEquals("1", this.map2.min());
        Assert.assertEquals("0", this.map3.min());
        Assert.assertEquals("1", this.map3.min(Comparators.reverseNaturalOrder()));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_throws_empty()
    {
        this.map0.min();
    }

    @Test
    public void max()
    {
        Assert.assertEquals("0", this.map1.max());
        Assert.assertEquals("1", this.map2.max());
        Assert.assertEquals("1", this.map3.max());
        Assert.assertEquals("0", this.map3.max(Comparators.reverseNaturalOrder()));
    }

    @Test(expected = NoSuchElementException.class)
    public void max_throws_empty()
    {
        this.map0.max();
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals("zero", BooleanObjectHashMap.newWithKeysValues(false, "zero", true, "one").minBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(1);
            }
        }));

        Assert.assertEquals("zero", BooleanObjectHashMap.newWithKeysValues(false, "zero").minBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(1);
            }
        }));

        Assert.assertEquals("one", BooleanObjectHashMap.newWithKeysValues(true, "one").minBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(1);
            }
        }));
    }

    @Test(expected = NoSuchElementException.class)
    public void minBy_throws_empty()
    {
        this.map0.minBy(Functions.getStringPassThru());
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals("one", BooleanObjectHashMap.newWithKeysValues(false, "zero", true, "one").maxBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(1);
            }
        }));

        Assert.assertEquals("one", BooleanObjectHashMap.newWithKeysValues(true, "one").maxBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(1);
            }
        }));

        Assert.assertEquals("zero", BooleanObjectHashMap.newWithKeysValues(false, "zero").maxBy(new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object.substring(1);
            }
        }));
    }

    @Test(expected = NoSuchElementException.class)
    public void maxBy_throws_empty()
    {
        this.map0.maxBy(Functions.getStringPassThru());
    }

    @Test
    public void sumOfInt()
    {
        IntFunction<String> function = new IntFunction<String>()
        {
            public int intValueOf(String anObject)
            {
                return anObject.length();
            }
        };
        Assert.assertEquals(0L, this.map0.sumOfInt(function));
        Assert.assertEquals(1L, this.map1.sumOfInt(function));
        Assert.assertEquals(1L, this.map2.sumOfInt(function));
        Assert.assertEquals(2L, this.map3.sumOfInt(function));
    }

    @Test
    public void sumOfFloat()
    {
        FloatFunction<String> function = new FloatFunction<String>()
        {
            public float floatValueOf(String anObject)
            {
                return anObject.length();
            }
        };
        Assert.assertEquals(0.0, this.map0.sumOfFloat(function), 0.0);
        Assert.assertEquals(1.0, this.map1.sumOfFloat(function), 0.0);
        Assert.assertEquals(1.0, this.map2.sumOfFloat(function), 0.0);
        Assert.assertEquals(2.0, this.map3.sumOfFloat(function), 0.0);
    }

    @Test
    public void sumOfLong()
    {
        LongFunction<String> function = new LongFunction<String>()
        {
            public long longValueOf(String anObject)
            {
                return anObject.length();
            }
        };
        Assert.assertEquals(0L, this.map0.sumOfLong(function));
        Assert.assertEquals(1L, this.map1.sumOfLong(function));
        Assert.assertEquals(1L, this.map2.sumOfLong(function));
        Assert.assertEquals(2L, this.map3.sumOfLong(function));
    }

    @Test
    public void sumOfDouble()
    {
        DoubleFunction<String> function = new DoubleFunction<String>()
        {
            public double doubleValueOf(String anObject)
            {
                return anObject.length();
            }
        };
        Assert.assertEquals(0.0, this.map0.sumOfDouble(function), 0.0);
        Assert.assertEquals(1.0, this.map1.sumOfDouble(function), 0.0);
        Assert.assertEquals(1.0, this.map2.sumOfDouble(function), 0.0);
        Assert.assertEquals(2.0, this.map3.sumOfDouble(function), 0.0);
    }

    @Test
    public void collect()
    {
        Function<String, String> concat = new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object + "concat";
            }
        };
        Assert.assertEquals(FastList.newList(), this.map0.collect(concat));
        Assert.assertEquals(FastList.newListWith("0concat"), this.map1.collect(concat));
        Assert.assertEquals(FastList.newListWith("1concat"), this.map2.collect(concat));
        Assert.assertTrue(FastList.newListWith("0concat", "1concat").equals(this.map3.collect(concat))
                || FastList.newListWith("1concat", "0concat").equals(this.map3.collect(concat)));
    }

    @Test
    public void collectWith()
    {
        Function2<String, String, String> concat = new Function2<String, String, String>()
        {
            public String value(String argument1, String argument2)
            {
                return argument1 + argument2;
            }
        };
        Assert.assertEquals(FastList.newList(), this.map0.collectWith(concat, "concat", FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("0concat"), this.map1.collectWith(concat, "concat", FastList.<String>newList()));
        Assert.assertEquals(FastList.newListWith("1concat"), this.map2.collectWith(concat, "concat", FastList.<String>newList()));
        Assert.assertTrue(FastList.newListWith("0concat", "1concat").fastListEquals(this.map3.collectWith(concat, "concat", FastList.<String>newList()))
                || FastList.newListWith("1concat", "0concat").fastListEquals(this.map3.collectWith(concat, "concat", FastList.<String>newList())));
    }

    @Test
    public void flatCollect()
    {
        Function<String, FastList<Character>> toChars = new Function<String, FastList<Character>>()
        {
            public FastList<Character> valueOf(String object)
            {
                FastList<Character> list = FastList.newList();
                char[] chars = object.toCharArray();
                for (char aChar : chars)
                {
                    list.add(aChar);
                }
                return list;
            }
        };

        Assert.assertEquals(UnifiedSet.newSetWith('z', 'e', 'r', 'o', 'n'),
                BooleanObjectHashMap.newWithKeysValues(false, "zero", true, "one").flatCollect(toChars).toSet());
    }

    @Test
    public void collectIf()
    {
        Function<String, String> concat = new Function<String, String>()
        {
            public String valueOf(String object)
            {
                return object + "concat";
            }
        };
        Predicate<String> isEven = new Predicate<String>()
        {
            public boolean accept(String each)
            {
                return (Integer.parseInt(each) & 1) == 0;
            }
        };
        Assert.assertEquals(FastList.newList(), this.map0.collectIf(isEven, concat));
        Assert.assertEquals(FastList.newListWith("0concat"), this.map1.collectIf(isEven, concat));
        Assert.assertEquals(FastList.newListWith(), this.map2.collectIf(isEven, concat));
        Assert.assertEquals(FastList.newListWith("0concat"), this.map3.collectIf(isEven, concat));

        Predicate<String> isOdd = new Predicate<String>()
        {
            public boolean accept(String each)
            {
                return (Integer.parseInt(each) & 1) != 0;
            }
        };
        Assert.assertEquals(FastList.newList(), this.map0.collectIf(isOdd, concat));
        Assert.assertEquals(FastList.newListWith("1concat"), this.map2.collectIf(isOdd, concat));
        Assert.assertEquals(FastList.newListWith(), this.map1.collectIf(isOdd, concat));
        Assert.assertEquals(FastList.newListWith("1concat"), this.map3.collectIf(isOdd, concat));
    }

    @Test
    public void zip()
    {
        MutableList<Pair<String, Integer>> zip1 = this.map3.zip(FastList.newListWith(0, 5, 6));
        MutableList<Pair<String, Integer>> zip2 = this.map3.zip(FastList.newListWith(1));

        Assert.assertTrue(FastList.newListWith(Tuples.pair("0", 0), Tuples.pair("1", 5)).equals(zip1)
                || FastList.newListWith(Tuples.pair("1", 0), Tuples.pair("0", 5)).equals(zip1));
        Assert.assertTrue(FastList.newListWith(Tuples.pair("0", 1)).equals(zip2)
                || FastList.newListWith(Tuples.pair("1", 1)).equals(zip2));
    }

    @Test
    public void zipWithIndex()
    {
        Assert.assertTrue(FastList.newListWith(Tuples.pair("0", 0), Tuples.pair("1", 1)).equals(this.map3.zipWithIndex())
                || FastList.newListWith(Tuples.pair("1", 0), Tuples.pair("0", 1)).equals(this.map3.zipWithIndex()));
    }

    @Test
    public void chunk()
    {
        RichIterable<RichIterable<String>> chunk1 = this.map3.chunk(1);
        RichIterable<RichIterable<String>> chunk2 = this.map1.chunk(1);

        Assert.assertTrue(FastList.newListWith(FastList.newListWith("0"), FastList.newListWith("1")).equals(chunk1)
                || FastList.newListWith(FastList.newListWith("1"), FastList.newListWith("0")).equals(chunk1));
        Assert.assertEquals(FastList.newListWith(FastList.newListWith("0")), chunk2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_throws_negative_size()
    {
        this.map3.chunk(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_throws_zero_size()
    {
        this.map3.chunk(0);
    }

    @Test
    public void aggregateInPlaceBy()
    {
        Function0<AtomicInteger> valueCreator = new Function0<AtomicInteger>()
        {
            public AtomicInteger value()
            {
                return new AtomicInteger(0);
            }
        };
        Procedure2<AtomicInteger, Integer> sumAggregator = new Procedure2<AtomicInteger, Integer>()
        {
            public void value(AtomicInteger aggregate, Integer value)
            {
                aggregate.addAndGet(value);
            }
        };
        BooleanObjectHashMap<Integer> collection = BooleanObjectHashMap.newWithKeysValues(true, 1, false, 2);
        MapIterable<String, AtomicInteger> aggregation = collection.aggregateInPlaceBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(1, aggregation.get("1").intValue());
        Assert.assertEquals(2, aggregation.get("2").intValue());
    }

    @Test
    public void aggregateBy()
    {
        Function0<Integer> valueCreator = new Function0<Integer>()
        {
            public Integer value()
            {
                return Integer.valueOf(0);
            }
        };
        Function2<Integer, Integer, Integer> sumAggregator = new Function2<Integer, Integer, Integer>()
        {
            public Integer value(Integer aggregate, Integer value)
            {
                return aggregate + value;
            }
        };
        BooleanObjectHashMap<Integer> collection = BooleanObjectHashMap.newWithKeysValues(true, 1, false, 2);
        MapIterable<String, Integer> aggregation = collection.aggregateBy(Functions.getToString(), valueCreator, sumAggregator);
        Assert.assertEquals(1, aggregation.get("1").intValue());
        Assert.assertEquals(2, aggregation.get("2").intValue());
    }

    @Test
    public void groupBy()
    {
        BooleanObjectHashMap<String> map1 = BooleanObjectHashMap.newWithKeysValues(true, "zero", false, "one");
        BooleanObjectHashMap<String> map2 = BooleanObjectHashMap.newWithKeysValues(true, "two", false, "three");

        FastListMultimap<Character, String> expected1 = FastListMultimap.newMultimap(Tuples.pair('z', "zero")
                , Tuples.pair('o', "one"));
        FastListMultimap<Character, String> expected2 = FastListMultimap.newMultimap(Tuples.pair('t', "two")
                , Tuples.pair('t', "three"));

        Function<String, Character> firstChar = new Function<String, Character>()
        {
            public Character valueOf(String object)
            {
                return object.charAt(0);
            }
        };

        final Multimap<Character, String> actual1 = map1.groupBy(firstChar);
        final Multimap<Character, String> actual2 = map2.groupBy(firstChar);

        Verify.assertSize(expected1.size(), actual1);
        expected1.forEachKeyValue(new Procedure2<Character, String>()
        {
            public void value(Character argument1, String argument2)
            {
                Assert.assertTrue(actual1.containsKeyAndValue(argument1, argument2));
            }
        });

        Verify.assertSize(expected2.size(), actual2);
        expected2.forEachKeyValue(new Procedure2<Character, String>()
        {
            public void value(Character argument1, String argument2)
            {
                Assert.assertTrue(actual2.containsKeyAndValue(argument1, argument2));
            }
        });
    }

    @Test
    public void groupByEach()
    {
        BooleanObjectHashMap<String> map1 = BooleanObjectHashMap.newWithKeysValues(true, "zero", false, "nine");

        Function<String, UnifiedSet<Character>> toChars = new Function<String, UnifiedSet<Character>>()
        {
            public UnifiedSet<Character> valueOf(String object)
            {
                UnifiedSet<Character> list = UnifiedSet.newSet();
                char[] chars = object.toCharArray();
                for (char aChar : chars)
                {
                    list.add(aChar);
                }
                return list;
            }
        };

        FastListMultimap<Character, String> expected = FastListMultimap.newMultimap(Tuples.pair('z', "zero"), Tuples.pair('e', "zero"), Tuples.pair('r', "zero"), Tuples.pair('o', "zero")
                , Tuples.pair('n', "nine"), Tuples.pair('i', "nine"), Tuples.pair('e', "nine"));
        final Multimap<Character, String> actual = map1.groupByEach(toChars);

        expected.forEachKeyValue(new Procedure2<Character, String>()
        {
            public void value(Character argument1, String argument2)
            {
                Assert.assertTrue(actual.containsKeyAndValue(argument1, argument2));
            }
        });
    }

    @Test
    public void testEquals()
    {
        Assert.assertNotEquals(this.map0, this.map1);
        Assert.assertNotEquals(this.map0, this.map2);
        Assert.assertNotEquals(this.map0, this.map3);
        Assert.assertNotEquals(this.map1, this.map2);
        Assert.assertNotEquals(this.map1, this.map3);
        Assert.assertNotEquals(this.map2, this.map3);
        Verify.assertEqualsAndHashCode(BooleanObjectHashMap.newWithKeysValues(false, "0", true, "1"), this.map3);
        Assert.assertNotEquals(BooleanObjectHashMap.newWithKeysValues(false, "1", true, "0"), this.map3);
        Assert.assertNotEquals(BooleanObjectHashMap.newWithKeysValues(false, "1"), this.map1);
        Assert.assertNotEquals(BooleanObjectHashMap.newWithKeysValues(true, "0"), this.map2);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(UnifiedMap.newMap().hashCode(), this.map0.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, "0").hashCode(), this.map1.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(true, "1").hashCode(), this.map2.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, "0", true, "1").hashCode(), this.map3.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, null, true, null).hashCode(), BooleanObjectHashMap.newWithKeysValues(false, null, true, null).hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", this.map0.toString());
        Assert.assertEquals("[false=0]", this.map1.toString());
        Assert.assertEquals("[true=1]", this.map2.toString());
        Assert.assertTrue(
                this.map3.toString(),
                "[false=0, true=1]".equals(this.map3.toString())
                        || "[true=1, false=0]".equals(this.map3.toString()));
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("", this.map0.makeString());
        Assert.assertEquals("false=0", this.map1.makeString());
        Assert.assertEquals("true=1", this.map2.makeString());
        Assert.assertTrue(
                this.map3.makeString(),
                "false=0, true=1".equals(this.map3.makeString())
                        || "true=1, false=0".equals(this.map3.makeString()));
        Assert.assertTrue(
                this.map3.makeString("/"),
                "false=0/true=1".equals(this.map3.makeString("/"))
                        || "true=1/false=0".equals(this.map3.makeString("/")));

        Assert.assertTrue(
                this.map3.makeString("{", ". ", "}"),
                "{false=0. true=1}".equals(this.map3.makeString("{", ". ", "}"))
                        || "{true=1. false=0}".equals(this.map3.makeString("{", ". ", "}")));
    }

    @Test
    public void appendString()
    {
        Appendable appendable = new StringBuilder();
        this.map0.appendString(appendable);
        Assert.assertEquals("", appendable.toString());

        Appendable appendable0 = new StringBuilder();
        this.map1.appendString(appendable0);
        Assert.assertEquals("false=0", appendable0.toString());

        Appendable appendable1 = new StringBuilder();
        this.map2.appendString(appendable1);
        Assert.assertEquals("true=1", appendable1.toString());

        Appendable appendable3 = new StringBuilder();
        this.map3.appendString(appendable3);
        Assert.assertTrue(
                appendable3.toString(),
                "false=0, true=1".equals(appendable3.toString())
                        || "true=1, false=0".equals(appendable3.toString()));

        Appendable appendable4 = new StringBuilder();
        this.map3.appendString(appendable4, "[", "/", "]");
        Assert.assertTrue(
                appendable4.toString(),
                "[false=0/true=1]".equals(appendable4.toString())
                        || "[true=1/false=0]".equals(appendable4.toString()));
    }

    @Test
    public void withKeyValue()
    {
        BooleanObjectHashMap<String> hashMap = new BooleanObjectHashMap<String>().withKeyValue(true, "one");
        BooleanObjectHashMap<String> hashMap0 = new BooleanObjectHashMap<String>().withKeysValues(true, "one", false, "two");
        BooleanObjectHashMap<String> hashMap1 = this.map3.withKeyValue(true, "one");
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one"), hashMap);
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one", false, "two"), hashMap0);
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one", false, "0"), hashMap1);
    }

    @Test
    public void withoutKey()
    {
        BooleanObjectHashMap<String> hashMap = BooleanObjectHashMap.newWithKeysValues(true, "one");
        BooleanObjectHashMap<String> hashMap0 = BooleanObjectHashMap.newWithKeysValues(true, "one", false, "two");
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one"), hashMap.withoutKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), hashMap.withoutKey(true));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), hashMap.withoutKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one"), hashMap0.withoutKey(false));
        Assert.assertEquals(BooleanObjectHashMap.newMap(), hashMap0.withoutKey(true));
    }

    @Test
    public void withoutAllKeys()
    {
        BooleanObjectHashMap<String> hashMap0 = BooleanObjectHashMap.newWithKeysValues(true, "one", false, "two");
        Assert.assertEquals(BooleanObjectHashMap.newWithKeysValues(true, "one"),
                hashMap0.withoutAllKeys(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanObjectHashMap.newMap(),
                hashMap0.withoutAllKeys(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanObjectHashMap.newMap(),
                BooleanObjectHashMap.newWithKeysValues(true, "one", false, "two")
                        .withoutAllKeys(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanObjectHashMap.newMap(),
                BooleanObjectHashMap.newMap().withoutAllKeys(BooleanArrayList.newListWith(true, false)));
    }

    @Test
    public void iterator()
    {
        UnifiedSet<String> expected = UnifiedSet.newSetWith("0", "1");
        UnifiedSet<String> actual = UnifiedSet.newSet();

        final Iterator<String> iterator = this.map3.iterator();
        Assert.assertTrue(iterator.hasNext());
        actual.add(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        actual.add(iterator.next());
        Assert.assertFalse(iterator.hasNext());

        Assert.assertEquals(expected, actual);
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator.next();
            }
        });

        BooleanObjectHashMap<String> map1 = BooleanObjectHashMap.newWithKeysValues(false, "zero", true, "one");
        final Iterator<String> iterator1 = map1.iterator();
        Verify.assertThrows(IllegalStateException.class, new Runnable()
        {
            public void run()
            {
                iterator1.remove();
            }
        });
        iterator1.next();
        iterator1.remove();
        Assert.assertTrue(map1.toString(), BooleanObjectHashMap.newWithKeysValues(false, "zero").equals(map1)
                || BooleanObjectHashMap.newWithKeysValues(true, "one").equals(map1));
        iterator1.next();
        iterator1.remove();
        Assert.assertEquals(BooleanObjectHashMap.newMap(), map1);
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator1.remove();
            }
        });
    }

}
