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

import java.util.Collection;

import ponzu.api.block.function.Function;
import ponzu.api.block.procedure.Procedure;
import ponzu.impl.test.Verify;
import org.junit.Test;

public class FunctionsTest
{
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
    public void bind()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyQx\n"
                        + "AAAAAAAAAAECAAJMAAx2YWwkZGVsZWdhdGV0ADJMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9j\n"
                        + "ay9wcm9jZWR1cmUvUHJvY2VkdXJlO0wADHZhbCRmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlv\n"
                        + "bnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO3hwcHA=",
                Functions.bind((Procedure<Object>) null, (Function<Object, ?>) null));
    }

    @Test
    public void getKeyFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRN\n"
                        + "YXBLZXlGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions.<Object>getKeyFunction());
    }

    @Test
    public void getValueFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRN\n"
                        + "YXBWYWx1ZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.<Object>getValueFunction());
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
    public void getOneFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRG\n"
                        + "aXJzdE9mUGFpckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions.<Object>firstOfPair());
    }

    @Test
    public void getTwoFunction()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9ucyRT\n"
                        + "ZWNvbmRPZlBhaXJGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions.<Object>secondOfPair());
    }
}
