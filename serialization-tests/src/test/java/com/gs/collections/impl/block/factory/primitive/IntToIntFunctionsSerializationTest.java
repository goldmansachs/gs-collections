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

package com.gs.collections.impl.block.factory.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class IntToIntFunctionsSerializationTest
{
    @Test
    public void increment()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRUb0ludEZ1bmN0aW9ucyRJbmNyZW1lbnRJbnRUb0ludEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==\n",
                IntToIntFunctions.increment());
    }

    @Test
    public void decrement()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRUb0ludEZ1bmN0aW9ucyREZWNyZW1lbnRJbnRUb0ludEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==\n",
                IntToIntFunctions.decrement());
    }

    @Test
    public void add()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRUb0ludEZ1bmN0aW9ucyRBZGRJbnRUb0ludEZ1bmN0aW9uAAAAAAAAAAECAAFJAAhpbnRUb0Fk\n"
                        + "ZHhwAAAAAA==",
                IntToIntFunctions.add(0));
    }

    @Test
    public void subtract()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRUb0ludEZ1bmN0aW9ucyRTdWJ0cmFjdEludFRvSW50RnVuY3Rpb24AAAAAAAAAAQIAAUkADWlu\n"
                        + "dFRvU3VidHJhY3R4cAAAAAA=",
                IntToIntFunctions.subtract(0));
    }
}
