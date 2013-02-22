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
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.BooleanFunction0;
import com.gs.collections.api.block.function.primitive.BooleanToBooleanFunction;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanBooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanBooleanProcedure;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class BooleanBooleanHashMapTest
{
    private final BooleanBooleanHashMap map0 = new BooleanBooleanHashMap();
    private final BooleanBooleanHashMap map1 = BooleanBooleanHashMap.newWithKeysValues(false, false);
    private final BooleanBooleanHashMap map2 = BooleanBooleanHashMap.newWithKeysValues(true, true);
    private final BooleanBooleanHashMap map3 = BooleanBooleanHashMap.newWithKeysValues(true, true, false, false);

    @Test
    public void removeKey()
    {
        this.map0.removeKey(true);
        this.map0.removeKey(false);
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map0);

        this.map1.removeKey(true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map1);
        this.map1.removeKey(false);
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map1);

        this.map2.removeKey(false);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map2);
        this.map2.removeKey(true);
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map2);

        this.map3.removeKey(true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map3);
        this.map3.removeKey(false);
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map3);
        Verify.assertEmpty(this.map3);

        BooleanBooleanHashMap map = BooleanBooleanHashMap.newWithKeysValues(true, true, false, false);
        map.removeKey(false);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), map);
    }

    @Test
    public void put()
    {
        this.map0.put(true, true);
        Assert.assertFalse(this.map0.isEmpty());
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map0);
        this.map0.put(false, false);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map0);

        BooleanBooleanHashMap map = BooleanBooleanHashMap.newMap();
        map.put(false, false);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), map);

        this.map1.put(true, true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map1);
        this.map1.put(false, true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, true), this.map1);

        this.map2.put(false, false);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map2);
        this.map2.put(true, false);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, false, false, false), this.map2);

        this.map3.put(true, false);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, false, false, false), this.map3);
        this.map3.put(false, true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, false, false, true), this.map3);
    }

    @Test
    public void getIfAbsentPut()
    {
        BooleanFunction0 function0 = new BooleanFunction0()
        {
            public boolean value()
            {
                return false;
            }
        };

        Assert.assertFalse(this.map0.getIfAbsentPut(true, function0));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, false), this.map0);

        BooleanFunction0 function1 = new BooleanFunction0()
        {
            public boolean value()
            {
                return true;
            }
        };
        Assert.assertTrue(this.map0.getIfAbsentPut(false, function1));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, false, false, true), this.map0);

        BooleanBooleanHashMap map = BooleanBooleanHashMap.newMap();
        Assert.assertTrue(map.getIfAbsentPut(false, function1));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, true), map);

        Assert.assertFalse(this.map1.getIfAbsentPut(false, function1));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map1);
        Assert.assertFalse(this.map1.getIfAbsentPut(true, function0));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, false, false, false), this.map1);

        Assert.assertTrue(this.map2.getIfAbsentPut(true, function0));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map2);
        Assert.assertTrue(this.map2.getIfAbsentPut(false, function1));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, true), this.map2);

        Assert.assertFalse(this.map3.getIfAbsentPut(false, function1));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map3);
        Assert.assertTrue(this.map3.getIfAbsentPut(true, function0));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map3);

    }

    @Test
    public void getIfAbsentPutWith()
    {
        BooleanFunction<String> stringLengthIsEven = new BooleanFunction<String>()
        {
            public boolean booleanValueOf(String object)
            {
                return (object.length() & 1) == 0;
            }
        };

        Assert.assertTrue(this.map0.getIfAbsentPutWith(true, stringLengthIsEven, "true"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map0);
        Assert.assertFalse(this.map0.getIfAbsentPutWith(false, stringLengthIsEven, "false"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map0);

        BooleanBooleanHashMap map = BooleanBooleanHashMap.newMap();
        Assert.assertFalse(map.getIfAbsentPutWith(false, stringLengthIsEven, "false"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), map);

        Assert.assertFalse(this.map1.getIfAbsentPutWith(false, stringLengthIsEven, "false"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map1);
        Assert.assertTrue(this.map1.getIfAbsentPutWith(true, stringLengthIsEven, "true"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map1);

        Assert.assertTrue(this.map2.getIfAbsentPutWith(true, stringLengthIsEven, "true"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map2);

        Assert.assertFalse(this.map2.getIfAbsentPutWith(false, stringLengthIsEven, "false"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map2);

        Assert.assertFalse(this.map3.getIfAbsentPutWith(false, stringLengthIsEven, "false"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map3);

        Assert.assertTrue(this.map3.getIfAbsentPutWith(true, stringLengthIsEven, "true"));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map3);
    }

    @Test
    public void getIfAbsentPutWithKey()
    {
        BooleanToBooleanFunction lengthIsEven = new BooleanToBooleanFunction()
        {
            public boolean valueOf(boolean booleanParameter)
            {
                return (String.valueOf(booleanParameter).length() & 1) == 0;
            }
        };

        Assert.assertTrue(this.map0.getIfAbsentPutWithKey(true, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map0);
        Assert.assertFalse(this.map0.getIfAbsentPutWithKey(false, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map0);

        BooleanBooleanHashMap map = BooleanBooleanHashMap.newMap();
        Assert.assertFalse(map.getIfAbsentPutWithKey(false, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), map);

        Assert.assertFalse(this.map1.getIfAbsentPutWithKey(false, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map1);
        Assert.assertTrue(this.map1.getIfAbsentPutWithKey(true, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map1);

        Assert.assertTrue(this.map2.getIfAbsentPutWithKey(true, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map2);

        Assert.assertFalse(this.map2.getIfAbsentPutWithKey(false, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map2);

        Assert.assertFalse(this.map3.getIfAbsentPutWithKey(false, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map3);

        Assert.assertTrue(this.map3.getIfAbsentPutWithKey(true, lengthIsEven));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), this.map3);
    }

    @Test
    public void get()
    {
        Assert.assertFalse(this.map0.get(true));
        Assert.assertFalse(this.map0.get(false));

        Assert.assertFalse(this.map1.get(true));
        Assert.assertFalse(this.map1.get(false));
        this.map1.put(false, true);
        Assert.assertTrue(this.map1.get(false));

        Assert.assertFalse(this.map2.get(false));
        Assert.assertTrue(this.map2.get(true));

        Assert.assertFalse(this.map3.get(false));
        Assert.assertTrue(this.map3.get(true));
        this.map3.removeKey(false);
        Assert.assertFalse(this.map3.get(false));
        Assert.assertTrue(this.map3.get(true));
        this.map3.removeKey(true);
        Assert.assertFalse(this.map3.get(false));
        Assert.assertFalse(this.map3.get(true));
    }

    @Test
    public void clear()
    {
        this.map0.clear();
        this.map1.clear();
        this.map2.clear();
        this.map3.clear();
        Verify.assertEmpty(this.map0);
        Verify.assertEmpty(this.map1);
        Verify.assertEmpty(this.map2);
        Verify.assertEmpty(this.map3);
    }

    @Test
    public void containsKey()
    {
        Assert.assertFalse(this.map0.containsKey(true));
        Assert.assertFalse(this.map0.containsKey(false));

        this.map0.put(true, false);
        Assert.assertTrue(this.map0.containsKey(true));

        this.map0.put(false, true);
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
        Assert.assertTrue(this.map3.containsValue(false));
        Assert.assertTrue(this.map3.containsValue(true));
        Assert.assertFalse(this.map2.containsValue(false));
        Assert.assertFalse(this.map1.containsValue(true));

        this.map1.put(true, true);
        Assert.assertTrue(this.map1.containsValue(true));

        this.map2.put(false, false);
        Assert.assertTrue(this.map3.containsValue(false));
    }

    @Test
    public void size()
    {
        Verify.assertSize(0, this.map0);
        this.map0.put(false, false);
        Verify.assertSize(1, this.map0);
        Verify.assertSize(1, this.map1);
        Verify.assertSize(1, this.map2);
        Verify.assertSize(2, this.map3);
        this.map3.removeKey(true);
        Verify.assertSize(1, this.map3);
    }

    @Test
    public void updateValue()
    {
        BooleanToBooleanFunction flip = new BooleanToBooleanFunction()
        {
            public boolean valueOf(boolean booleanParameter)
            {
                return !booleanParameter;
            }
        };

        BooleanBooleanHashMap map1 = new BooleanBooleanHashMap();
        Assert.assertTrue(map1.updateValue(false, false, flip));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, true), map1);
        Assert.assertFalse(map1.updateValue(false, false, flip));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), map1);
        Assert.assertFalse(map1.updateValue(true, true, flip));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false, true, false), map1);
        Assert.assertTrue(map1.updateValue(true, true, flip));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false, true, true), map1);
    }

    @Test
    public void select()
    {
        BooleanBooleanPredicate trueAndTrue = new BooleanBooleanPredicate()
        {
            public boolean accept(boolean value1, boolean value2)
            {
                return value1 && value2;
            }
        };
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map0.select(trueAndTrue));

        BooleanBooleanPredicate trueAndFalse = new BooleanBooleanPredicate()
        {
            public boolean accept(boolean value1, boolean value2)
            {
                return value1 && !value2;
            }
        };
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map1.select(trueAndFalse));
        BooleanBooleanPredicate falseAndFalse = new BooleanBooleanPredicate()
        {
            public boolean accept(boolean value1, boolean value2)
            {
                return !value1 && !value2;
            }
        };
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map1.select(falseAndFalse));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map2.select(trueAndFalse));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map2.select(trueAndTrue));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map3.select(trueAndFalse));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map3.select(trueAndTrue));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map3.select(falseAndFalse));

        Assert.assertEquals(new BooleanArrayList(), this.map0.select(BooleanPredicates.isFalse()));
        Assert.assertEquals(new BooleanArrayList(), this.map1.select(BooleanPredicates.isTrue()));
        Assert.assertEquals(BooleanArrayList.newListWith(false), this.map1.select(BooleanPredicates.isFalse()));
        Assert.assertEquals(new BooleanArrayList(), this.map2.select(BooleanPredicates.isFalse()));
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.map2.select(BooleanPredicates.isTrue()));
        Assert.assertEquals(new BooleanArrayList(), this.map3.select(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertEquals(BooleanArrayList.newListWith(false), this.map3.select(BooleanPredicates.isFalse()));
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.map3.select(BooleanPredicates.isTrue()));
    }

    @Test
    public void reject()
    {
        BooleanBooleanPredicate trueAndTrue = new BooleanBooleanPredicate()
        {
            public boolean accept(boolean value1, boolean value2)
            {
                return value1 && value2;
            }
        };
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map0.reject(trueAndTrue));

        BooleanBooleanPredicate trueAndFalse = new BooleanBooleanPredicate()
        {
            public boolean accept(boolean value1, boolean value2)
            {
                return value1 && !value2;
            }
        };
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map1.reject(trueAndFalse));
        BooleanBooleanPredicate falseAndFalse = new BooleanBooleanPredicate()
        {
            public boolean accept(boolean value1, boolean value2)
            {
                return !value1 && !value2;
            }
        };
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map1.reject(falseAndFalse));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map2.reject(trueAndFalse));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), this.map2.reject(trueAndTrue));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false, true, true), this.map3.reject(trueAndFalse));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(false, false), this.map3.reject(trueAndTrue));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), this.map3.reject(falseAndFalse));

        Assert.assertEquals(new BooleanArrayList(), this.map0.reject(BooleanPredicates.isFalse()));
        Assert.assertEquals(BooleanArrayList.newListWith(false), this.map1.reject(BooleanPredicates.isTrue()));
        Assert.assertEquals(new BooleanArrayList(), this.map1.reject(BooleanPredicates.isFalse()));
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.map2.reject(BooleanPredicates.isFalse()));
        Assert.assertEquals(new BooleanArrayList(), this.map2.reject(BooleanPredicates.isTrue()));
        Assert.assertEquals(BooleanArrayList.newListWith(false, true), this.map3.reject(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.map3.reject(BooleanPredicates.isFalse()));
        Assert.assertEquals(BooleanArrayList.newListWith(false), this.map3.reject(BooleanPredicates.isTrue()));
    }

    @Test
    public void forEachValue()
    {
        final String[] concat = {"", "", "", ""};

        this.map0.forEachValue(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                concat[0] += each;
            }
        });
        this.map1.forEachValue(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                concat[1] += each;
            }
        });
        this.map2.forEachValue(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                concat[2] += each;
            }
        });
        this.map3.forEachValue(new BooleanProcedure()
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

        this.map0.forEachKeyValue(new BooleanBooleanProcedure()
        {
            public void value(boolean argument1, boolean argument2)
            {
                concat[0] += argument1;
                concat[0] += argument2;
            }
        });
        this.map1.forEachKeyValue(new BooleanBooleanProcedure()
        {
            public void value(boolean each, boolean parameter)
            {
                concat[1] += each;
                concat[1] += parameter;
            }
        });
        this.map2.forEachKeyValue(new BooleanBooleanProcedure()
        {
            public void value(boolean each, boolean parameter)
            {
                concat[2] += each;
                concat[2] += parameter;
            }
        });
        this.map3.forEachKeyValue(new BooleanBooleanProcedure()
        {
            public void value(boolean each, boolean parameter)
            {
                concat[3] += each;
                concat[3] += parameter;
            }
        });

        Assert.assertEquals("", concat[0]);
        Assert.assertEquals("falsefalse", concat[1]);
        Assert.assertEquals("truetrue", concat[2]);
        Assert.assertTrue(concat[3], "falsefalsetruetrue".equals(concat[3]) || "truetruefalsefalse".equals(concat[3]));
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.map0);
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
    public void contains()
    {
        Assert.assertFalse(this.map0.contains(false));
        Assert.assertTrue(this.map1.contains(false));
        Assert.assertTrue(this.map2.contains(true));
        Assert.assertTrue(this.map3.contains(true));
        Assert.assertTrue(this.map3.contains(false));

        this.map0.put(true, false);
        Assert.assertTrue(this.map0.contains(false));
        this.map0.removeKey(true);
        Assert.assertFalse(this.map0.contains(false));

        Assert.assertFalse(this.map1.contains(true));
        Assert.assertFalse(this.map2.contains(false));
    }

    @Test
    public void containsAll()
    {
        Assert.assertTrue(this.map3.containsAll(BooleanArrayList.newListWith(false, true)));
        Assert.assertTrue(this.map1.containsAll(BooleanArrayList.newListWith(false)));
        Assert.assertFalse(this.map0.containsAll(BooleanArrayList.newListWith(false, true)));
        Assert.assertFalse(this.map2.containsAll(BooleanArrayList.newListWith(true, false, false)));
    }

    @Test
    public void containsAllArguments()
    {
        Assert.assertTrue(this.map3.containsAll(false, true));
        Assert.assertTrue(this.map1.containsAll(false));
        Assert.assertFalse(this.map0.containsAll(false, true));
        Assert.assertFalse(this.map2.containsAll(true, false, false));
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertTrue(this.map0.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(this.map1.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(this.map2.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(this.map3.detectIfNone(BooleanPredicates.isFalse(), true));

        Assert.assertFalse(this.map0.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertTrue(this.map2.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(this.map1.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertTrue(this.map3.detectIfNone(BooleanPredicates.isTrue(), false));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(0L, this.map0.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(1L, this.map1.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(0L, this.map2.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(1L, this.map3.count(BooleanPredicates.isFalse()));

        Assert.assertEquals(0L, this.map0.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(0L, this.map1.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(1L, this.map2.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(1L, this.map3.count(BooleanPredicates.isTrue()));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(this.map0.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.map1.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.map2.anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.map3.anySatisfy(BooleanPredicates.isFalse()));

        Assert.assertFalse(this.map0.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.map2.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.map1.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.map3.anySatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.map0.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.map1.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.map2.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.map3.allSatisfy(BooleanPredicates.isFalse()));

        Assert.assertTrue(this.map0.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.map2.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.map1.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.map3.allSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(new BooleanArrayList(), this.map0.toList());
        Assert.assertEquals(BooleanArrayList.newListWith(false), this.map1.toList());
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.map2.toList());
        Assert.assertTrue(BooleanArrayList.newListWith(false, true).equals(this.map3.toList())
                || BooleanArrayList.newListWith(true, false).equals(this.map3.toList()));
    }

    @Test
    public void toSortedList()
    {
        Assert.assertEquals(new BooleanArrayList(), this.map0.toSortedList());
        Assert.assertEquals(BooleanArrayList.newListWith(false), this.map1.toSortedList());
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.map2.toSortedList());
        Assert.assertEquals(BooleanArrayList.newListWith(false, true), this.map3.toSortedList());
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(new BooleanHashSet(), this.map0.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(false), this.map1.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(true), this.map2.toSet());
        Assert.assertEquals(BooleanHashSet.newSetWith(false, true), this.map3.toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(new BooleanHashBag(), this.map0.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false), this.map1.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(true), this.map2.toBag());
        Assert.assertEquals(BooleanHashBag.newBagWith(false, true), this.map3.toBag());
    }

    @Test
    public void toArray()
    {
        Assert.assertTrue(Arrays.equals(new boolean[0], this.map0.toArray()));
        Assert.assertTrue(Arrays.equals(new boolean[]{false}, this.map1.toArray()));
        Assert.assertTrue(Arrays.equals(new boolean[]{true}, this.map2.toArray()));
        Assert.assertTrue(Arrays.equals(new boolean[]{false, true}, this.map3.toArray())
                || Arrays.equals(new boolean[]{true, false}, this.map3.toArray()));
    }

    @Test
    public void collect()
    {
        BooleanToObjectFunction<String> concat = new BooleanToObjectFunction<String>()
        {
            public String valueOf(boolean booleanParameter)
            {
                return "concat" + booleanParameter;
            }
        };
        Assert.assertEquals(FastList.newList(), this.map0.collect(concat));
        Assert.assertEquals(FastList.newListWith("concatfalse"), this.map1.collect(concat));
        Assert.assertEquals(FastList.newListWith("concattrue"), this.map2.collect(concat));
        Assert.assertTrue(FastList.newListWith("concatfalse", "concattrue").equals(this.map3.collect(concat))
                || FastList.newListWith("concattrue", "concatfalse").equals(this.map3.collect(concat)));
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
        Verify.assertEqualsAndHashCode(BooleanBooleanHashMap.newWithKeysValues(false, false, true, true), this.map3);
        Assert.assertNotEquals(BooleanBooleanHashMap.newWithKeysValues(false, true, true, false), this.map3);
        Assert.assertNotEquals(BooleanBooleanHashMap.newWithKeysValues(false, true), this.map1);
        Assert.assertNotEquals(BooleanBooleanHashMap.newWithKeysValues(true, false), this.map2);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(UnifiedMap.newMap().hashCode(), this.map0.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, false).hashCode(), this.map1.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(true, true).hashCode(), this.map2.hashCode());
        Assert.assertEquals(UnifiedMap.newWithKeysValues(false, false, true, true).hashCode(), this.map3.hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", this.map0.toString());
        Assert.assertEquals("[false=false]", this.map1.toString());
        Assert.assertEquals("[true=true]", this.map2.toString());
        Assert.assertTrue(
                this.map3.toString(),
                "[false=false, true=true]".equals(this.map3.toString())
                        || "[true=true, false=false]".equals(this.map3.toString()));
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("", this.map0.makeString());
        Assert.assertEquals("false=false", this.map1.makeString());
        Assert.assertEquals("true=true", this.map2.makeString());
        Assert.assertTrue(
                this.map3.makeString(),
                "false=false, true=true".equals(this.map3.makeString())
                        || "true=true, false=false".equals(this.map3.makeString()));
        Assert.assertTrue(
                this.map3.makeString("/"),
                "false=false/true=true".equals(this.map3.makeString("/"))
                        || "true=true/false=false".equals(this.map3.makeString("/")));

        Assert.assertTrue(
                this.map3.makeString("{", ". ", "}"),
                "{false=false. true=true}".equals(this.map3.makeString("{", ". ", "}"))
                        || "{true=true. false=false}".equals(this.map3.makeString("{", ". ", "}")));
    }

    @Test
    public void appendString()
    {
        Appendable appendable = new StringBuilder();
        this.map0.appendString(appendable);
        Assert.assertEquals("", appendable.toString());

        Appendable appendable0 = new StringBuilder();
        this.map1.appendString(appendable0);
        Assert.assertEquals("false=false", appendable0.toString());

        Appendable appendable1 = new StringBuilder();
        this.map2.appendString(appendable1);
        Assert.assertEquals("true=true", appendable1.toString());

        Appendable appendable3 = new StringBuilder();
        this.map3.appendString(appendable3);
        Assert.assertTrue(
                appendable3.toString(),
                "false=false, true=true".equals(appendable3.toString())
                        || "true=true, false=false".equals(appendable3.toString()));

        Appendable appendable4 = new StringBuilder();
        this.map3.appendString(appendable4, "[", "/", "]");
        Assert.assertTrue(
                appendable4.toString(),
                "[false=false/true=true]".equals(appendable4.toString())
                        || "[true=true/false=false]".equals(appendable4.toString()));
    }

    @Test
    public void withKeyValue()
    {
        BooleanBooleanHashMap hashMap = new BooleanBooleanHashMap().withKeyValue(true, true);
        BooleanBooleanHashMap hashMap0 = new BooleanBooleanHashMap().withKeysValues(true, true, false, true);
        BooleanBooleanHashMap hashMap1 = this.map3.withKeyValue(true, true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), hashMap);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, true), hashMap0);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true, false, false), hashMap1);
    }

    @Test
    public void withoutKey()
    {
        BooleanBooleanHashMap hashMap = BooleanBooleanHashMap.newWithKeysValues(true, true);
        BooleanBooleanHashMap hashMap0 = BooleanBooleanHashMap.newWithKeysValues(true, true, false, true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), hashMap.withoutKey(false));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), hashMap.withoutKey(true));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), hashMap.withoutKey(false));
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true), hashMap0.withoutKey(false));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(), hashMap0.withoutKey(true));
    }

    @Test
    public void withoutAllKeys()
    {
        BooleanBooleanHashMap hashMap0 = BooleanBooleanHashMap.newWithKeysValues(true, true, false, true);
        Assert.assertEquals(BooleanBooleanHashMap.newWithKeysValues(true, true),
                hashMap0.withoutAllKeys(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(),
                hashMap0.withoutAllKeys(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(),
                BooleanBooleanHashMap.newWithKeysValues(true, true, false, true)
                        .withoutAllKeys(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(BooleanBooleanHashMap.newMap(),
                BooleanBooleanHashMap.newMap().withoutAllKeys(BooleanArrayList.newListWith(true, false)));
    }

    @Test
    public void intIterator()
    {
        BooleanHashSet expected = BooleanHashSet.newSetWith(false, true);
        BooleanHashSet actual = new BooleanHashSet();

        final BooleanIterator iterator = this.map3.booleanIterator();
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
    }
}
