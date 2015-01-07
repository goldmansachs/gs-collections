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

package com.gs.collections.impl.block.factory;

import java.util.Collection;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class FunctionsSerializationTest
{
    @Test
    public void throwing()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRU\n"
                        + "aHJvd2luZ0Z1bmN0aW9uQWRhcHRlcgAAAAAAAAABAgABTAAQdGhyb3dpbmdGdW5jdGlvbnQAQUxj\n"
                        + "b20vZ3MvY29sbGVjdGlvbnMvaW1wbC9ibG9jay9mdW5jdGlvbi9jaGVja2VkL1Rocm93aW5nRnVu\n"
                        + "Y3Rpb247eHIAPmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZ1bmN0aW9uLmNoZWNrZWQu\n"
                        + "Q2hlY2tlZEZ1bmN0aW9uAAAAAAAAAAECAAB4cHA=",
                Functions.throwing(null));
    }

    @Test
    public void getPassThru()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRQ\n"
                        + "YXNzVGhydUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.getPassThru());
    }

    @Test
    public void getTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRU\n"
                        + "cnVlRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                Functions.getTrue());
    }

    @Test
    public void getFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "YWxzZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.getFalse());
    }

    @Test
    public void getIntegerPassThru()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRJ\n"
                        + "bnRlZ2VyUGFzc1RocnVGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions.getIntegerPassThru());
    }

    @Test
    public void getLongPassThru()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRM\n"
                        + "b25nUGFzc1RocnVGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions.getLongPassThru());
    }

    @Test
    public void getDoublePassThru()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRE\n"
                        + "b3VibGVQYXNzVGhydUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.getDoublePassThru());
    }

    @Test
    public void getStringTrim()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "dHJpbmdUcmltRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                Functions.getStringTrim());
    }

    @Test
    public void getFixedValue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "aXhlZFZhbHVlRnVuY3Rpb24AAAAAAAAAAQIAAUwABXZhbHVldAASTGphdmEvbGFuZy9PYmplY3Q7\n"
                        + "eHBw",
                Functions.getFixedValue(null));
    }

    @Test
    public void getToClass()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRD\n"
                        + "bGFzc0Z1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.getToClass());
    }

    @Test
    public void getMathSinFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRN\n"
                        + "YXRoU2luRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                Functions.getMathSinFunction());
    }

    @Test
    public void squaredInteger()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "cXVhcmVkSW50ZWdlckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.squaredInteger());
    }

    @Test
    public void getToString()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRU\n"
                        + "b1N0cmluZ0Z1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.getToString());
    }

    @Test
    public void getStringToInteger()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "dHJpbmdUb0ludGVnZXJGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions.getStringToInteger());
    }

    @Test
    public void withDefault()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRE\n"
                        + "ZWZhdWx0RnVuY3Rpb24AAAAAAAAAAQIAAkwADGRlZmF1bHRWYWx1ZXQAEkxqYXZhL2xhbmcvT2Jq\n"
                        + "ZWN0O0wACGZ1bmN0aW9udAAwTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24v\n"
                        + "RnVuY3Rpb247eHBwc3IAQGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuRnVu\n"
                        + "Y3Rpb25zJFBhc3NUaHJ1RnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                Functions.withDefault(Functions.getPassThru(), null));
    }

    @Test
    public void nullSafe()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRO\n"
                        + "dWxsU2FmZUZ1bmN0aW9uAAAAAAAAAAECAAJMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlv\n"
                        + "bnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO0wACW51bGxWYWx1ZXQAEkxqYXZhL2xhbmcv\n"
                        + "T2JqZWN0O3hwc3IAQGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuRnVuY3Rp\n"
                        + "b25zJFBhc3NUaHJ1RnVuY3Rpb24AAAAAAAAAAQIAAHhwcA==",
                Functions.nullSafe(Functions.getPassThru()));
    }

    @Test
    public void firstNotNullValue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "aXJzdE5vdE51bGxGdW5jdGlvbgAAAAAAAAABAgABWwAJZnVuY3Rpb25zdAAxW0xjb20vZ3MvY29s\n"
                        + "bGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO3hwdXIAMVtMY29tLmdzLmNvbGxl\n"
                        + "Y3Rpb25zLmFwaS5ibG9jay5mdW5jdGlvbi5GdW5jdGlvbjsuOJB4J5QjBAIAAHhwAAAAAA==",
                Functions.firstNotNullValue());
    }

    @Test
    public void firstNotEmptyStringValue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "aXJzdE5vdEVtcHR5U3RyaW5nRnVuY3Rpb24AAAAAAAAAAQIAAVsACWZ1bmN0aW9uc3QAMVtMY29t\n"
                        + "L2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjt4cHVyADFbTGNvbS5n\n"
                        + "cy5jb2xsZWN0aW9ucy5hcGkuYmxvY2suZnVuY3Rpb24uRnVuY3Rpb247LjiQeCeUIwQCAAB4cAAA\n"
                        + "AAA=",
                Functions.firstNotEmptyStringValue());
    }

    @Test
    public void firstNotEmptyCollectionValue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "aXJzdE5vdEVtcHR5Q29sbGVjdGlvbkZ1bmN0aW9uAAAAAAAAAAECAAFbAAlmdW5jdGlvbnN0ADFb\n"
                        + "TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVuY3Rpb247eHB1cgAxW0xj\n"
                        + "b20uZ3MuY29sbGVjdGlvbnMuYXBpLmJsb2NrLmZ1bmN0aW9uLkZ1bmN0aW9uOy44kHgnlCMEAgAA\n"
                        + "eHAAAAAA",
                Functions.<Integer, String, Collection<String>>firstNotEmptyCollectionValue());
    }

    @Test
    public void synchronizedEach()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "eW5jaHJvbml6ZWRGdW5jdGlvbgAAAAAAAAABAgABTAAIZnVuY3Rpb250ADBMY29tL2dzL2NvbGxl\n"
                        + "Y3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjt4cHA=",
                Functions.synchronizedEach(null));
    }

    @Test
    public void bind_procedure()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRC\n"
                        + "aW5kUHJvY2VkdXJlAAAAAAAAAAECAAJMAAhkZWxlZ2F0ZXQAMkxjb20vZ3MvY29sbGVjdGlvbnMv\n"
                        + "YXBpL2Jsb2NrL3Byb2NlZHVyZS9Qcm9jZWR1cmU7TAAIZnVuY3Rpb250ADBMY29tL2dzL2NvbGxl\n"
                        + "Y3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjt4cHBw",
                Functions.bind((Procedure<Object>) null, null));
    }

    @Test
    public void bind_procedure2()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRC\n"
                        + "aW5kUHJvY2VkdXJlMgAAAAAAAAABAgACTAAIZGVsZWdhdGV0ADNMY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9ibG9jay9wcm9jZWR1cmUvUHJvY2VkdXJlMjtMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29s\n"
                        + "bGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO3hwcHA=",
                Functions.bind((Procedure2<Object, Object>) null, null));
    }

    @Test
    public void bind_object_int_procedure()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRC\n"
                        + "aW5kT2JqZWN0SW50UHJvY2VkdXJlAAAAAAAAAAECAAJMAAhkZWxlZ2F0ZXQARUxjb20vZ3MvY29s\n"
                        + "bGVjdGlvbnMvYXBpL2Jsb2NrL3Byb2NlZHVyZS9wcmltaXRpdmUvT2JqZWN0SW50UHJvY2VkdXJl\n"
                        + "O0wACGZ1bmN0aW9udAAwTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVu\n"
                        + "Y3Rpb247eHBwcA==",
                Functions.bind((ObjectIntProcedure<Object>) null, null));
    }

    @Test
    public void bind_function2_parameter()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRC\n"
                        + "aW5kRnVuY3Rpb24yAAAAAAAAAAECAAJMAAhkZWxlZ2F0ZXQAMUxjb20vZ3MvY29sbGVjdGlvbnMv\n"
                        + "YXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uMjtMAAlwYXJhbWV0ZXJ0ABJMamF2YS9sYW5nL09i\n"
                        + "amVjdDt4cHBw",
                Functions.bind((Function2<Object, Object, Object>) null, null));
    }

    @Test
    public void getKeyFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRN\n"
                        + "YXBLZXlGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions.getKeyFunction());
    }

    @Test
    public void getValueFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRN\n"
                        + "YXBWYWx1ZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.getValueFunction());
    }

    @Test
    public void getSizeOf()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "aXplRnVuY3Rpb24AAAAAAAAAAQIAAHhyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5m\n"
                        + "dW5jdGlvbi5wcmltaXRpdmUuSW50ZWdlckZ1bmN0aW9uSW1wbAAAAAAAAAABAgAAeHA=",
                Functions.getSizeOf());
    }

    @Test
    public void chain()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "dW5jdGlvbkNoYWluAAAAAAAAAAECAAJMAAlmdW5jdGlvbjF0ADBMY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjtMAAlmdW5jdGlvbjJxAH4AAXhwcHA=",
                Functions.chain(null, null));
    }

    @Test
    public void chainChain()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "dW5jdGlvbkNoYWluAAAAAAAAAAECAAJMAAlmdW5jdGlvbjF0ADBMY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjtMAAlmdW5jdGlvbjJxAH4AAXhwc3EAfgAAcHBw\n",
                Functions.chain(null, null).chain(null));
    }

    @Test
    public void chainBoolean()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRC\n"
                        + "b29sZWFuRnVuY3Rpb25DaGFpbgAAAAAAAAABAgACTAAJZnVuY3Rpb24xdAAwTGNvbS9ncy9jb2xs\n"
                        + "ZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVuY3Rpb247TAAJZnVuY3Rpb24ydABBTGNvbS9n\n"
                        + "cy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0Jvb2xlYW5GdW5jdGlv\n"
                        + "bjt4cHBw",
                Functions.chainBoolean(null, null));
    }

    @Test
    public void chainByte()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRC\n"
                        + "eXRlRnVuY3Rpb25DaGFpbgAAAAAAAAABAgACTAAJZnVuY3Rpb24xdAAwTGNvbS9ncy9jb2xsZWN0\n"
                        + "aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVuY3Rpb247TAAJZnVuY3Rpb24ydAA+TGNvbS9ncy9j\n"
                        + "b2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0J5dGVGdW5jdGlvbjt4cHBw\n",
                Functions.chainByte(null, null));
    }

    @Test
    public void chainChar()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRD\n"
                        + "aGFyRnVuY3Rpb25DaGFpbgAAAAAAAAABAgACTAAJZnVuY3Rpb24xdAAwTGNvbS9ncy9jb2xsZWN0\n"
                        + "aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVuY3Rpb247TAAJZnVuY3Rpb24ydAA+TGNvbS9ncy9j\n"
                        + "b2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0NoYXJGdW5jdGlvbjt4cHBw\n",
                Functions.chainChar(null, null));
    }

    @Test
    public void chainDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRE\n"
                        + "b3VibGVGdW5jdGlvbkNoYWluAAAAAAAAAAECAAJMAAlmdW5jdGlvbjF0ADBMY29tL2dzL2NvbGxl\n"
                        + "Y3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjtMAAlmdW5jdGlvbjJ0AEBMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvRG91YmxlRnVuY3Rpb247\n"
                        + "eHBwcA==",
                Functions.chainDouble(null, null));
    }

    @Test
    public void chainFloat()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "bG9hdEZ1bmN0aW9uQ2hhaW4AAAAAAAAAAQIAAkwACWZ1bmN0aW9uMXQAMExjb20vZ3MvY29sbGVj\n"
                        + "dGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO0wACWZ1bmN0aW9uMnQAP0xjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9GbG9hdEZ1bmN0aW9uO3hw\n"
                        + "cHA=",
                Functions.chainFloat(null, null));
    }

    @Test
    public void chainInt()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRJ\n"
                        + "bnRGdW5jdGlvbkNoYWluAAAAAAAAAAECAAJMAAlmdW5jdGlvbjF0ADBMY29tL2dzL2NvbGxlY3Rp\n"
                        + "b25zL2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjtMAAlmdW5jdGlvbjJ0AD1MY29tL2dzL2Nv\n"
                        + "bGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9wcmltaXRpdmUvSW50RnVuY3Rpb247eHBwcA==\n",
                Functions.chainInt(null, null));
    }

    @Test
    public void chainLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRM\n"
                        + "b25nRnVuY3Rpb25DaGFpbgAAAAAAAAABAgACTAAJZnVuY3Rpb24xdAAwTGNvbS9ncy9jb2xsZWN0\n"
                        + "aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVuY3Rpb247TAAJZnVuY3Rpb24ydAA+TGNvbS9ncy9j\n"
                        + "b2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0xvbmdGdW5jdGlvbjt4cHBw\n",
                Functions.chainLong(null, null));
    }

    @Test
    public void chainShort()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "aG9ydEZ1bmN0aW9uQ2hhaW4AAAAAAAAAAQIAAkwACWZ1bmN0aW9uMXQAMExjb20vZ3MvY29sbGVj\n"
                        + "dGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO0wACWZ1bmN0aW9uMnQAP0xjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9TaG9ydEZ1bmN0aW9uO3hw\n"
                        + "cHA=",
                Functions.chainShort(null, null));
    }

    @Test
    public void getOneFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "aXJzdE9mUGFpckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.firstOfPair());
    }

    @Test
    public void getTwoFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "ZWNvbmRPZlBhaXJGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions.secondOfPair());
    }

    @Test
    public void classForName()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRD\n"
                        + "bGFzc0Zvck5hbWVGdW5jdGlvbgAAAAAAAAABAgAAeHIAPmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZ1bmN0aW9uLmNoZWNrZWQuQ2hlY2tlZEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.classForName());
    }

    @Test
    public void getSwappedPairFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "d2FwcGVkUGFpckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.swappedPair());
    }
}
