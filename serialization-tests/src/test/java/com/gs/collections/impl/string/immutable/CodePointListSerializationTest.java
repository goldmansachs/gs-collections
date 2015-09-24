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

package com.gs.collections.impl.string.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CodePointListSerializationTest
{
    public static final String EMPTY_CODE_LIST = "rO0ABXNyADZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdHJpbmcuaW1tdXRhYmxlLkNvZGVQb2lu\n"
            + "dExpc3QAAAAAAAAAAgIAAUwACmNvZGVQb2ludHN0ADhMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9s\n"
            + "aXN0L3ByaW1pdGl2ZS9JbW11dGFibGVJbnRMaXN0O3hwc3IARmNvbS5ncy5jb2xsZWN0aW9ucy5p\n"
            + "bXBsLmxpc3QuaW1tdXRhYmxlLnByaW1pdGl2ZS5JbW11dGFibGVJbnRFbXB0eUxpc3QAAAAAAAAA\n"
            + "AQIAAHhw";
    public static final String HELLO_WORLD_STRING = "rO0ABXNyADZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdHJpbmcuaW1tdXRhYmxlLkNvZGVQb2lu\n"
            + "dExpc3QAAAAAAAAAAgIAAUwACmNvZGVQb2ludHN0ADhMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9s\n"
            + "aXN0L3ByaW1pdGl2ZS9JbW11dGFibGVJbnRMaXN0O3hwc3IARmNvbS5ncy5jb2xsZWN0aW9ucy5p\n"
            + "bXBsLmxpc3QuaW1tdXRhYmxlLnByaW1pdGl2ZS5JbW11dGFibGVJbnRBcnJheUxpc3QAAAAAAAAA\n"
            + "AQIAAVsABWl0ZW1zdAACW0l4cHVyAAJbSU26YCZ26rKlAgAAeHAAAAAMAAAASAAAAGUAAABsAAAA\n"
            + "bAAAAG8AAAAgAAAAVwAAAG8AAAByAAAAbAAAAGQAAAAh";

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                2L,
                EMPTY_CODE_LIST,
                CodePointList.from(""));
    }

    @Test
    public void serializedFormNotEmpty()
    {
        Verify.assertSerializedForm(
                2L,
                HELLO_WORLD_STRING,
                CodePointList.from("Hello World!"));
    }
}
