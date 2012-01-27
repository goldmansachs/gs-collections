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

package ponzu.impl.block.factory;

import ponzu.impl.test.Verify;
import org.junit.Test;

public class StringFunctionsTest
{
    @Test
    public void length()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRMZW5ndGhGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                StringFunctions.length());
    }

    @Test
    public void toLowerCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb0xvd2VyQ2FzZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.toLowerCase());
    }

    @Test
    public void toUpperCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1VwcGVyQ2FzZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.toUpperCase());
    }

    @Test
    public void trim()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUcmltRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                StringFunctions.trim());
    }

    @Test
    public void subString()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRTdWJTdHJpbmdGdW5jdGlvbgAAAAAAAAABAgACSQAKYmVnaW5JbmRleEkACGVuZEluZGV4\n"
                        + "eHAAAAAAAAAAAQ==",
                StringFunctions.subString(0, 1));
    }
}
