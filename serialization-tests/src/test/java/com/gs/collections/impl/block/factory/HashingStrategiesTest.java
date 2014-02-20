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

package com.gs.collections.impl.block.factory;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class HashingStrategiesTest
{
    @Test
    public void defaultHashingStrategy()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJERlZmF1bHRTdHJhdGVneQAAAAAAAAABAgAAeHA=",
                HashingStrategies.defaultStrategy());
    }

    @Test
    public void nullSafeHashingStrategy()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJE51bGxTYWZlSGFzaGluZ1N0cmF0ZWd5AAAAAAAAAAECAAFMABNub25OdWxsU2FmZVN0\n"
                        + "cmF0ZWd5dAAuTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svSGFzaGluZ1N0cmF0ZWd5O3hw\n"
                        + "c3IAR2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuSGFzaGluZ1N0cmF0ZWdp\n"
                        + "ZXMkRGVmYXVsdFN0cmF0ZWd5AAAAAAAAAAECAAB4cA==",
                HashingStrategies.nullSafeHashingStrategy(HashingStrategies.defaultStrategy()));
    }

    @Test
    public void fromFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJEZ1bmN0aW9uSGFzaGluZ1N0cmF0ZWd5AAAAAAAAAAECAAFMAAhmdW5jdGlvbnQAMExj\n"
                        + "b20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO3hwc3IAQGNvbS5n\n"
                        + "cy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuRnVuY3Rpb25zJFRvU3RyaW5nRnVuY3Rp\n"
                        + "b24AAAAAAAAAAQIAAHhw",
                HashingStrategies.fromFunction(Functions.getToString()));
    }

    @Test
    public void identityHashingStrategy()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJElkZW50aXR5SGFzaGluZ1N0cmF0ZWd5AAAAAAAAAAECAAB4cA==",
                HashingStrategies.identityStrategy());
    }
}
