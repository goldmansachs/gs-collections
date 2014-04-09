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

package com.gs.collections.impl.set.immutable.primitive;

import com.gs.collections.impl.factory.primitive.BooleanSets;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableBooleanHashSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLnByaW1pdGl2ZS5J\n"
                        + "bW11dGFibGVCb29sZWFuU2V0U2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHcEAAAAAHg=\n",
                BooleanSets.immutable.with());

        Verify.assertSerializedForm(
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLnByaW1pdGl2ZS5J\n"
                        + "bW11dGFibGVCb29sZWFuU2V0U2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHcFAAAAAQB4\n",
                BooleanSets.immutable.with(false));

        Verify.assertSerializedForm(
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLnByaW1pdGl2ZS5J\n"
                        + "bW11dGFibGVCb29sZWFuU2V0U2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHcFAAAAAQF4\n",
                BooleanSets.immutable.with(true));

        Verify.assertSerializedForm(
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLnByaW1pdGl2ZS5J\n"
                        + "bW11dGFibGVCb29sZWFuU2V0U2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHcGAAAAAgAB\n"
                        + "eA==",
                BooleanSets.immutable.with(false, true));
    }
}
