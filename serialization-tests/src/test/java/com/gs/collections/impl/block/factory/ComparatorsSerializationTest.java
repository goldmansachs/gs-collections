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

package com.gs.collections.impl.block.factory;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ComparatorsSerializationTest
{
    @Test
    public void naturalOrder()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JE5hdHVyYWxPcmRlckNvbXBhcmF0b3IAAAAAAAAAAQIAAHhw",
                Comparators.naturalOrder());
    }

    @Test
    public void reverseNaturalOrder()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JFJldmVyc2VDb21wYXJhdG9yAAAAAAAAAAECAAFMAApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9D\n"
                        + "b21wYXJhdG9yO3hwc3IASGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29t\n"
                        + "cGFyYXRvcnMkTmF0dXJhbE9yZGVyQ29tcGFyYXRvcgAAAAAAAAABAgAAeHA=",
                Comparators.reverseNaturalOrder());
    }

    @Test
    public void reverse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JFJldmVyc2VDb21wYXJhdG9yAAAAAAAAAAECAAFMAApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9D\n"
                        + "b21wYXJhdG9yO3hwc3IASGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29t\n"
                        + "cGFyYXRvcnMkTmF0dXJhbE9yZGVyQ29tcGFyYXRvcgAAAAAAAAABAgAAeHA=",
                Comparators.reverse(Comparators.naturalOrder()));
    }

    @Test
    public void safeNullsLow()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JFNhZmVOdWxsc0xvd0NvbXBhcmF0b3IAAAAAAAAAAQIAAUwAFW5vdE51bGxTYWZlQ29tcGFyYXRv\n"
                        + "cnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRvcjt4cHNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5i\n"
                        + "bG9jay5mYWN0b3J5LkNvbXBhcmF0b3JzJE5hdHVyYWxPcmRlckNvbXBhcmF0b3IAAAAAAAAAAQIA\n"
                        + "AHhw",
                Comparators.safeNullsLow(Comparators.naturalOrder()));
    }

    @Test
    public void safeNullsHigh()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JFNhZmVOdWxsc0hpZ2hDb21wYXJhdG9yAAAAAAAAAAECAAFMABVub3ROdWxsU2FmZUNvbXBhcmF0\n"
                        + "b3J0ABZMamF2YS91dGlsL0NvbXBhcmF0b3I7eHBzcgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5Db21wYXJhdG9ycyROYXR1cmFsT3JkZXJDb21wYXJhdG9yAAAAAAAAAAEC\n"
                        + "AAB4cA==",
                Comparators.safeNullsHigh(Comparators.naturalOrder()));
    }

    @Test
    public void chain()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JENoYWluZWRDb21wYXJhdG9yAAAAAAAAAAECAAFbAAtjb21wYXJhdG9yc3QAF1tMamF2YS91dGls\n"
                        + "L0NvbXBhcmF0b3I7eHB1cgAXW0xqYXZhLnV0aWwuQ29tcGFyYXRvcjv3sdhVvN0hoAIAAHhwAAAA\n"
                        + "AXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3JzJE5h\n"
                        + "dHVyYWxPcmRlckNvbXBhcmF0b3IAAAAAAAAAAQIAAHhw",
                Comparators.chain(Comparators.naturalOrder()));
    }

    @Test
    public void fromFunctions()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5jb21wYXJhdG9yLkZ1bmN0aW9u\n"
                        + "Q29tcGFyYXRvcgAAAAAAAAABAgACTAAKY29tcGFyYXRvcnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRv\n"
                        + "cjtMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1\n"
                        + "bmN0aW9uO3hwc3IASGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29tcGFy\n"
                        + "YXRvcnMkTmF0dXJhbE9yZGVyQ29tcGFyYXRvcgAAAAAAAAABAgAAeHBzcgBAY29tLmdzLmNvbGxl\n"
                        + "Y3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5GdW5jdGlvbnMkVG9TdHJpbmdGdW5jdGlvbgAAAAAA\n"
                        + "AAABAgAAeHA=",
                Comparators.fromFunctions(Functions.getToString()));
    }

    @Test
    public void fromFunctions2()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JENoYWluZWRDb21wYXJhdG9yAAAAAAAAAAECAAFbAAtjb21wYXJhdG9yc3QAF1tMamF2YS91dGls\n"
                        + "L0NvbXBhcmF0b3I7eHB1cgAXW0xqYXZhLnV0aWwuQ29tcGFyYXRvcjv3sdhVvN0hoAIAAHhwAAAA\n"
                        + "AnNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5jb21wYXJhdG9yLkZ1bmN0aW9uQ29t\n"
                        + "cGFyYXRvcgAAAAAAAAABAgACTAAKY29tcGFyYXRvcnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRvcjtM\n"
                        + "AAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0\n"
                        + "aW9uO3hwc3IASGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29tcGFyYXRv\n"
                        + "cnMkTmF0dXJhbE9yZGVyQ29tcGFyYXRvcgAAAAAAAAABAgAAeHBzcgBAY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuYmxvY2suZmFjdG9yeS5GdW5jdGlvbnMkVG9TdHJpbmdGdW5jdGlvbgAAAAAAAAAB\n"
                        + "AgAAeHBzcQB+AAVxAH4ACnEAfgAM",
                Comparators.fromFunctions(Functions.getToString(), Functions.getToString()));
    }

    @Test
    public void fromFunctions3()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JENoYWluZWRDb21wYXJhdG9yAAAAAAAAAAECAAFbAAtjb21wYXJhdG9yc3QAF1tMamF2YS91dGls\n"
                        + "L0NvbXBhcmF0b3I7eHB1cgAXW0xqYXZhLnV0aWwuQ29tcGFyYXRvcjv3sdhVvN0hoAIAAHhwAAAA\n"
                        + "A3NyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5jb21wYXJhdG9yLkZ1bmN0aW9uQ29t\n"
                        + "cGFyYXRvcgAAAAAAAAABAgACTAAKY29tcGFyYXRvcnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRvcjtM\n"
                        + "AAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0\n"
                        + "aW9uO3hwc3IASGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29tcGFyYXRv\n"
                        + "cnMkTmF0dXJhbE9yZGVyQ29tcGFyYXRvcgAAAAAAAAABAgAAeHBzcgBAY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuYmxvY2suZmFjdG9yeS5GdW5jdGlvbnMkVG9TdHJpbmdGdW5jdGlvbgAAAAAAAAAB\n"
                        + "AgAAeHBzcQB+AAVxAH4ACnEAfgAMc3EAfgAFcQB+AApxAH4ADA==",
                Comparators.fromFunctions(Functions.getToString(), Functions.getToString(), Functions.getToString()));
    }

    @Test
    public void powerSet()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JFBvd2VyU2V0Q29tcGFyYXRvcgAAAAAAAAABAgAAeHA=",
                Comparators.powerSet());
    }

    @Test
    public void ascendingCollectionSizeComparator()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JEFzY2VuZGluZ0NvbGxlY3Rpb25TaXplQ29tcGFyYXRvcgAAAAAAAAABAgAAeHA=",
                Comparators.ascendingCollectionSizeComparator());
    }

    @Test
    public void descendingCollectionSizeComparator()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JERlc2NlbmRpbmdDb2xsZWN0aW9uU2l6ZUNvbXBhcmF0b3IAAAAAAAAAAQIAAHhw",
                Comparators.descendingCollectionSizeComparator());
    }

    @Test
    public void compareByFirst()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JEJ5Rmlyc3RPZlBhaXJDb21wYXJhdG9yAAAAAAAAAAECAAFMAApjb21wYXJhdG9ydAAWTGphdmEv\n"
                        + "dXRpbC9Db21wYXJhdG9yO3hwcA==",
                Comparators.byFirstOfPair(null));
    }

    @Test
    public void compareBySecond()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3Jz\n"
                        + "JEJ5U2Vjb25kT2ZQYWlyQ29tcGFyYXRvcgAAAAAAAAABAgABTAAKY29tcGFyYXRvcnQAFkxqYXZh\n"
                        + "L3V0aWwvQ29tcGFyYXRvcjt4cHA=",
                Comparators.bySecondOfPair(null));
    }
}
