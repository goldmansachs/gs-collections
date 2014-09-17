/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.lazy.primitive;

import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import org.junit.Test;

public class CollectIterableSerializationTest
{
    @Test
    public void intSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0SW50\n"
                        + "SXRlcmFibGUkSW50RnVuY3Rpb25Ub1Byb2NlZHVyZQAAAAAAAAABAgABTAAIZnVuY3Rpb250AD1M\n"
                        + "Y29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvSW50RnVuY3Rp\n"
                        + "b247eHBzcgBKY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmltaXRpdmVG\n"
                        + "dW5jdGlvbnMkVW5ib3hJbnRlZ2VyVG9JbnQAAAAAAAAAAQIAAHhw");
        CollectIntIterable<Integer> collectIntIterable = new CollectIntIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.unboxIntegerToInt());

        collectIntIterable.forEach(null);
    }

    @Test
    public void doubleSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0RG91\n"
                        + "YmxlSXRlcmFibGUkRG91YmxlRnVuY3Rpb25Ub1Byb2NlZHVyZQAAAAAAAAABAgABTAAIZnVuY3Rp\n"
                        + "b250AEBMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvRG91\n"
                        + "YmxlRnVuY3Rpb247eHBzcgBNY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5Q\n"
                        + "cmltaXRpdmVGdW5jdGlvbnMkVW5ib3hJbnRlZ2VyVG9Eb3VibGUAAAAAAAAAAQIAAHhw");
        CollectDoubleIterable<Integer> collectDoubleIterable = new CollectDoubleIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.unboxIntegerToDouble());

        collectDoubleIterable.forEach(null);
    }

    @Test
    public void floatSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0Rmxv\n"
                        + "YXRJdGVyYWJsZSRGbG9hdEZ1bmN0aW9uVG9Qcm9jZWR1cmUAAAAAAAAAAQIAAUwACGZ1bmN0aW9u\n"
                        + "dAA/TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0Zsb2F0\n"
                        + "RnVuY3Rpb247eHBzcgBMY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5Qcmlt\n"
                        + "aXRpdmVGdW5jdGlvbnMkVW5ib3hJbnRlZ2VyVG9GbG9hdAAAAAAAAAABAgAAeHA=");

        CollectFloatIterable<Integer> collectFloatIterable = new CollectFloatIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.unboxIntegerToFloat());

        collectFloatIterable.forEach(null);
    }

    @Test
    public void longSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0TG9u\n"
                        + "Z0l0ZXJhYmxlJExvbmdGdW5jdGlvblRvUHJvY2VkdXJlAAAAAAAAAAECAAFMAAhmdW5jdGlvbnQA\n"
                        + "Pkxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9Mb25nRnVu\n"
                        + "Y3Rpb247eHBzcgBLY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmltaXRp\n"
                        + "dmVGdW5jdGlvbnMkVW5ib3hJbnRlZ2VyVG9Mb25nAAAAAAAAAAECAAB4cA==");

        CollectLongIterable<Integer> collectLongIterable = new CollectLongIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.unboxIntegerToLong());

        collectLongIterable.forEach(null);
    }

    @Test
    public void shortSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0U2hv\n"
                        + "cnRJdGVyYWJsZSRTaG9ydEZ1bmN0aW9uVG9Qcm9jZWR1cmUAAAAAAAAAAQIAAUwACGZ1bmN0aW9u\n"
                        + "dAA/TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL1Nob3J0\n"
                        + "RnVuY3Rpb247eHBzcgBMY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5Qcmlt\n"
                        + "aXRpdmVGdW5jdGlvbnMkVW5ib3hJbnRlZ2VyVG9TaG9ydAAAAAAAAAABAgAAeHA=");

        CollectShortIterable<Integer> collectShortIterable = new CollectShortIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.unboxIntegerToShort());

        collectShortIterable.forEach(null);
    }

    @Test
    public void byteSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0Qnl0\n"
                        + "ZUl0ZXJhYmxlJEJ5dGVGdW5jdGlvblRvUHJvY2VkdXJlAAAAAAAAAAECAAFMAAhmdW5jdGlvbnQA\n"
                        + "Pkxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9CeXRlRnVu\n"
                        + "Y3Rpb247eHBzcgBLY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmltaXRp\n"
                        + "dmVGdW5jdGlvbnMkVW5ib3hJbnRlZ2VyVG9CeXRlAAAAAAAAAAECAAB4cA==");

        CollectByteIterable<Integer> collectByteIterable = new CollectByteIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.unboxIntegerToByte());

        collectByteIterable.forEach(null);
    }

    @Test
    public void charSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0Q2hh\n"
                        + "ckl0ZXJhYmxlJENoYXJGdW5jdGlvblRvUHJvY2VkdXJlAAAAAAAAAAECAAFMAAhmdW5jdGlvbnQA\n"
                        + "Pkxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9DaGFyRnVu\n"
                        + "Y3Rpb247eHBzcgBLY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmltaXRp\n"
                        + "dmVGdW5jdGlvbnMkVW5ib3hJbnRlZ2VyVG9DaGFyAAAAAAAAAAECAAB4cA==");

        CollectCharIterable<Integer> collectCharIterable = new CollectCharIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.unboxIntegerToChar());

        collectCharIterable.forEach(null);
    }

    @Test
    public void booleanSerialization()
    {
        LazyIterableTestHelper<Integer> integerLazyIterableTestHelper = new LazyIterableTestHelper<Integer>(
                "rO0ABXNyAFhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5sYXp5LnByaW1pdGl2ZS5Db2xsZWN0Qm9v\n"
                        + "bGVhbkl0ZXJhYmxlJEJvb2xlYW5GdW5jdGlvblRvUHJvY2VkdXJlAAAAAAAAAAECAAFMAAhmdW5j\n"
                        + "dGlvbnQAQUxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9C\n"
                        + "b29sZWFuRnVuY3Rpb247eHBzcgBKY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9y\n"
                        + "eS5QcmltaXRpdmVGdW5jdGlvbnMkSW50ZWdlcklzUG9zaXRpdmUAAAAAAAAAAQIAAHhw");

        CollectBooleanIterable<Integer> collectBooleanIterable = new CollectBooleanIterable<Integer>(
                integerLazyIterableTestHelper,
                PrimitiveFunctions.integerIsPositive());

        collectBooleanIterable.forEach(null);
    }
}
