/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.block.factory.primitive;

import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class PrimitiveFunctionsSerializationTest
{
    @Test
    public void integerIsPositive()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRJbnRlZ2VySXNQb3NpdGl2ZQAAAAAAAAABAgAAeHA=",
                PrimitiveFunctions.integerIsPositive());
    }

    @Test
    public void unboxNumberToInt()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveE51bWJlclRvSW50AAAAAAAAAAECAAB4cA==",
                PrimitiveFunctions.unboxNumberToInt());
    }

    @Test
    public void unboxIntegerToByte()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEludGVnZXJUb0J5dGUAAAAAAAAAAQIAAHhw",
                PrimitiveFunctions.unboxIntegerToByte());
    }

    @Test
    public void unboxIntegerToChar()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEludGVnZXJUb0NoYXIAAAAAAAAAAQIAAHhw",
                PrimitiveFunctions.unboxIntegerToChar());
    }

    @Test
    public void unboxIntegerToInt()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEludGVnZXJUb0ludAAAAAAAAAABAgAAeHA=",
                PrimitiveFunctions.unboxIntegerToInt());
    }

    @Test
    public void unboxNumberToFloat()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveE51bWJlclRvRmxvYXQAAAAAAAAAAQIAAHhw",
                PrimitiveFunctions.unboxNumberToFloat());
    }

    @Test
    public void unboxNumberToLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveE51bWJlclRvTG9uZwAAAAAAAAABAgAAeHA=",
                PrimitiveFunctions.unboxNumberToLong());
    }

    @Test
    public void unboxNumberToDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveE51bWJlclRvRG91YmxlAAAAAAAAAAECAAB4cA==",
                PrimitiveFunctions.unboxNumberToDouble());
    }

    @Test
    public void unboxIntegerToFloat()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEludGVnZXJUb0Zsb2F0AAAAAAAAAAECAAB4cA==",
                PrimitiveFunctions.unboxIntegerToFloat());
    }

    @Test
    public void unboxIntegerToLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEludGVnZXJUb0xvbmcAAAAAAAAAAQIAAHhw",
                PrimitiveFunctions.unboxIntegerToLong());
    }

    @Test
    public void unboxIntegerToShort()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEludGVnZXJUb1Nob3J0AAAAAAAAAAECAAB4cA==",
                PrimitiveFunctions.unboxIntegerToShort());
    }

    @Test
    public void unboxIntegerToDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEludGVnZXJUb0RvdWJsZQAAAAAAAAABAgAAeHA=",
                PrimitiveFunctions.unboxIntegerToDouble());
    }

    @Test
    public void unboxDoubleToDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveERvdWJsZVRvRG91YmxlAAAAAAAAAAECAAB4cA==",
                PrimitiveFunctions.unboxDoubleToDouble());
    }

    @Test
    public void unboxFloatToFloat()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyRVbmJveEZsb2F0VG9GbG9hdAAAAAAAAAABAgAAeHA=",
                PrimitiveFunctions.unboxFloatToFloat());
    }

    @Test
    public void sumByInt()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyQxAAAAAAAAAAECAAJMAAx2YWwkZnVuY3Rpb250AD1MY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvSW50RnVuY3Rpb247TAALdmFsJGdyb3VwQnl0\n"
                        + "ADBMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjt4cHBw",
                PrimitiveFunctions.sumByIntFunction(null, null));
    }

    @Test
    public void sumByLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyQzAAAAAAAAAAECAAJMAAx2YWwkZnVuY3Rpb250AD5MY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvTG9uZ0Z1bmN0aW9uO0wAC3ZhbCRncm91cEJ5\n"
                        + "dAAwTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVuY3Rpb247eHBwcA==\n",
                PrimitiveFunctions.sumByLongFunction(null, null));
    }

    @Test
    public void sumByFloat()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyQyAAAAAAAAAAECAANMAAxjb21wZW5zYXRpb250AD1MY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9tYXAvcHJpbWl0aXZlL011dGFibGVPYmplY3REb3VibGVNYXA7TAAMdmFsJGZ1bmN0aW9u\n"
                        + "dAA/TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0Zsb2F0\n"
                        + "RnVuY3Rpb247TAALdmFsJGdyb3VwQnl0ADBMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9m\n"
                        + "dW5jdGlvbi9GdW5jdGlvbjt4cHNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJs\n"
                        + "ZS5wcmltaXRpdmUuT2JqZWN0RG91YmxlSGFzaE1hcAAAAAAAAAABDAAAeHB3BAAAAAB4cHA=",
                PrimitiveFunctions.sumByFloatFunction(null, null));
    }

    @Test
    public void sumByDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByaW1pdGl2ZUZ1\n"
                        + "bmN0aW9ucyQ0AAAAAAAAAAECAANMAAxjb21wZW5zYXRpb250AD1MY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9tYXAvcHJpbWl0aXZlL011dGFibGVPYmplY3REb3VibGVNYXA7TAAMdmFsJGZ1bmN0aW9u\n"
                        + "dABATGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0RvdWJs\n"
                        + "ZUZ1bmN0aW9uO0wAC3ZhbCRncm91cEJ5dAAwTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2sv\n"
                        + "ZnVuY3Rpb24vRnVuY3Rpb247eHBzcgBBY29tLmdzLmNvbGxlY3Rpb25zLmltcGwubWFwLm11dGFi\n"
                        + "bGUucHJpbWl0aXZlLk9iamVjdERvdWJsZUhhc2hNYXAAAAAAAAAAAQwAAHhwdwQAAAAAeHBw",
                PrimitiveFunctions.sumByDoubleFunction(null, null));
    }
}
