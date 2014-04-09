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

package com.gs.collections.impl.block.function;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class AddFunctionSerializationTest
{
    @Test
    public void addDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5BZGRGdW5jdGlv\n"
                        + "biRBZGREb3VibGVGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                AddFunction.DOUBLE);
    }

    @Test
    public void addFloat()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5BZGRGdW5jdGlv\n"
                        + "biRBZGRGbG9hdEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                AddFunction.FLOAT);
    }

    @Test
    public void addInteger()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5BZGRGdW5jdGlv\n"
                        + "biRBZGRJbnRlZ2VyRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                AddFunction.INTEGER);
    }

    @Test
    public void addLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5BZGRGdW5jdGlv\n"
                        + "biRBZGRMb25nRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                AddFunction.LONG);
    }

    @Test
    public void addString()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5BZGRGdW5jdGlv\n"
                        + "biRBZGRTdHJpbmdGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                AddFunction.STRING);
    }

    @Test
    public void addCollection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5BZGRGdW5jdGlv\n"
                        + "biRBZGRDb2xsZWN0aW9uRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                AddFunction.COLLECTION);
    }
}
