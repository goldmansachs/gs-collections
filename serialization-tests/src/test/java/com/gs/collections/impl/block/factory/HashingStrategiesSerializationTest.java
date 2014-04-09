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

public class HashingStrategiesSerializationTest
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

    @Test
    public void chainedHashingStrategy()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJENoYWluZWRIYXNoaW5nU3RyYXRlZ3kAAAAAAAAAAQIAAVsAEWhhc2hpbmdTdHJhdGVn\n"
                        + "aWVzdAAvW0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL0hhc2hpbmdTdHJhdGVneTt4cHVy\n"
                        + "AC9bTGNvbS5ncy5jb2xsZWN0aW9ucy5hcGkuYmxvY2suSGFzaGluZ1N0cmF0ZWd5O2dYZQViLT3o\n"
                        + "AgAAeHAAAAABc3IAT2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuSGFzaGlu\n"
                        + "Z1N0cmF0ZWdpZXMkSWRlbnRpdHlIYXNoaW5nU3RyYXRlZ3kAAAAAAAAAAQIAAHhw",
                HashingStrategies.chain(HashingStrategies.identityStrategy()));
    }

    @Test
    public void fromBooleanFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJEJvb2xlYW5GdW5jdGlvbkhhc2hpbmdTdHJhdGVneQAAAAAAAAABAgABTAAIZnVuY3Rp\n"
                        + "b250AEFMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvQm9v\n"
                        + "bGVhbkZ1bmN0aW9uO3hwc3IAUGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3Rvcnku\n"
                        + "U3RyaW5nRnVuY3Rpb25zJFRvUHJpbWl0aXZlQm9vbGVhbkZ1bmN0aW9uAAAAAAAAAAECAAB4cA==\n",
                HashingStrategies.fromBooleanFunction(StringFunctions.toPrimitiveBoolean()));
    }

    @Test
    public void fromByteFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJEJ5dGVGdW5jdGlvbkhhc2hpbmdTdHJhdGVneQAAAAAAAAABAgABTAAIZnVuY3Rpb250\n"
                        + "AD5MY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvQnl0ZUZ1\n"
                        + "bmN0aW9uO3hwc3IATWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuU3RyaW5n\n"
                        + "RnVuY3Rpb25zJFRvUHJpbWl0aXZlQnl0ZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                HashingStrategies.fromByteFunction(StringFunctions.toPrimitiveByte()));
    }

    @Test
    public void fromCharFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJENoYXJGdW5jdGlvbkhhc2hpbmdTdHJhdGVneQAAAAAAAAABAgABTAAIZnVuY3Rpb250\n"
                        + "AD5MY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvQ2hhckZ1\n"
                        + "bmN0aW9uO3hwc3IATWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuU3RyaW5n\n"
                        + "RnVuY3Rpb25zJFRvUHJpbWl0aXZlQ2hhckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                HashingStrategies.fromCharFunction(StringFunctions.toPrimitiveChar()));
    }

    @Test
    public void fromDoubleFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJERvdWJsZUZ1bmN0aW9uSGFzaGluZ1N0cmF0ZWd5AAAAAAAAAAECAAFMAAhmdW5jdGlv\n"
                        + "bnQAQExjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9Eb3Vi\n"
                        + "bGVGdW5jdGlvbjt4cHNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0\n"
                        + "cmluZ0Z1bmN0aW9ucyRUb1ByaW1pdGl2ZURvdWJsZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                HashingStrategies.fromDoubleFunction(StringFunctions.toPrimitiveDouble()));
    }

    @Test
    public void fromFloatFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJEZsb2F0RnVuY3Rpb25IYXNoaW5nU3RyYXRlZ3kAAAAAAAAAAQIAAUwACGZ1bmN0aW9u\n"
                        + "dAA/TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0Zsb2F0\n"
                        + "RnVuY3Rpb247eHBzcgBOY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5TdHJp\n"
                        + "bmdGdW5jdGlvbnMkVG9QcmltaXRpdmVGbG9hdEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                HashingStrategies.fromFloatFunction(StringFunctions.toPrimitiveFloat()));
    }

    @Test
    public void fromIntFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJEludEZ1bmN0aW9uSGFzaGluZ1N0cmF0ZWd5AAAAAAAAAAECAAFMAAhmdW5jdGlvbnQA\n"
                        + "PUxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9JbnRGdW5j\n"
                        + "dGlvbjt4cHNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1\n"
                        + "bmN0aW9ucyRUb1ByaW1pdGl2ZUludEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                HashingStrategies.fromIntFunction(StringFunctions.toPrimitiveInt()));
    }

    @Test
    public void fromLongFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJExvbmdGdW5jdGlvbkhhc2hpbmdTdHJhdGVneQAAAAAAAAABAgABTAAIZnVuY3Rpb250\n"
                        + "AD5MY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvTG9uZ0Z1\n"
                        + "bmN0aW9uO3hwc3IATWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuU3RyaW5n\n"
                        + "RnVuY3Rpb25zJFRvUHJpbWl0aXZlTG9uZ0Z1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                HashingStrategies.fromLongFunction(StringFunctions.toPrimitiveLong()));
    }

    @Test
    public void fromShortFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJFNob3J0RnVuY3Rpb25IYXNoaW5nU3RyYXRlZ3kAAAAAAAAAAQIAAUwACGZ1bmN0aW9u\n"
                        + "dAA/TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL1Nob3J0\n"
                        + "RnVuY3Rpb247eHBzcgBOY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5TdHJp\n"
                        + "bmdGdW5jdGlvbnMkVG9QcmltaXRpdmVTaG9ydEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                HashingStrategies.fromShortFunction(StringFunctions.toPrimitiveShort()));
    }
}
