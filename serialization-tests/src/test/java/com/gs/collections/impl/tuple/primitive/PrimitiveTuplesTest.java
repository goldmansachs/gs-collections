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

package com.gs.collections.impl.tuple.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class PrimitiveTuplesTest
{
    @Test
    public void byteObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuQnl0ZU9iamVj\n"
                        + "dFBhaXJJbXBsAAAAAAAAAAECAAJCAANvbmVMAAN0d290ABJMamF2YS9sYW5nL09iamVjdDt4cABw\n",
                PrimitiveTuples.pair((byte) 0, null));
    }

    @Test
    public void booleanObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuQm9vbGVhbk9i\n"
                        + "amVjdFBhaXJJbXBsAAAAAAAAAAECAAJaAANvbmVMAAN0d290ABJMamF2YS9sYW5nL09iamVjdDt4\n"
                        + "cABw",
                PrimitiveTuples.pair(false, null));
    }

    @Test
    public void charObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuQ2hhck9iamVj\n"
                        + "dFBhaXJJbXBsAAAAAAAAAAECAAJDAANvbmVMAAN0d290ABJMamF2YS9sYW5nL09iamVjdDt4cABh\n"
                        + "cA==",
                PrimitiveTuples.pair('a', null));
    }

    @Test
    public void doubleObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuRG91YmxlT2Jq\n"
                        + "ZWN0UGFpckltcGwAAAAAAAAAAQIAAkQAA29uZUwAA3R3b3QAEkxqYXZhL2xhbmcvT2JqZWN0O3hw\n"
                        + "AAAAAAAAAABw",
                PrimitiveTuples.pair(0.0, null));
    }

    @Test
    public void floatObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuRmxvYXRPYmpl\n"
                        + "Y3RQYWlySW1wbAAAAAAAAAABAgACRgADb25lTAADdHdvdAASTGphdmEvbGFuZy9PYmplY3Q7eHAA\n"
                        + "AAAAcA==",
                PrimitiveTuples.pair(0.0f, null));
    }

    @Test
    public void intObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuSW50T2JqZWN0\n"
                        + "UGFpckltcGwAAAAAAAAAAQIAAkkAA29uZUwAA3R3b3QAEkxqYXZhL2xhbmcvT2JqZWN0O3hwAAAA\n"
                        + "AHA=",
                PrimitiveTuples.pair(0, null));
    }

    @Test
    public void longObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuTG9uZ09iamVj\n"
                        + "dFBhaXJJbXBsAAAAAAAAAAECAAJKAANvbmVMAAN0d290ABJMamF2YS9sYW5nL09iamVjdDt4cAAA\n"
                        + "AAAAAAAAcA==",
                PrimitiveTuples.pair(0L, null));
    }

    @Test
    public void shortObjectPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuU2hvcnRPYmpl\n"
                        + "Y3RQYWlySW1wbAAAAAAAAAABAgACUwADb25lTAADdHdvdAASTGphdmEvbGFuZy9PYmplY3Q7eHAA\n"
                        + "AHA=",
                PrimitiveTuples.pair((short) 0, null));
    }

    @Test
    public void objectBytePair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0Qnl0\n"
                        + "ZVBhaXJJbXBsAAAAAAAAAAECAAJCAAN0d29MAANvbmV0ABJMamF2YS9sYW5nL09iamVjdDt4cABw\n",
                PrimitiveTuples.pair(null, (byte) 0));
    }

    @Test
    public void objectBooleanPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0Qm9v\n"
                        + "bGVhblBhaXJJbXBsAAAAAAAAAAECAAJaAAN0d29MAANvbmV0ABJMamF2YS9sYW5nL09iamVjdDt4\n"
                        + "cABw",
                PrimitiveTuples.pair(null, false));
    }

    @Test
    public void objectCharPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0Q2hh\n"
                        + "clBhaXJJbXBsAAAAAAAAAAECAAJDAAN0d29MAANvbmV0ABJMamF2YS9sYW5nL09iamVjdDt4cABh\n"
                        + "cA==",
                PrimitiveTuples.pair(null, 'a'));
    }

    @Test
    public void objectDoublePair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0RG91\n"
                        + "YmxlUGFpckltcGwAAAAAAAAAAQIAAkQAA3R3b0wAA29uZXQAEkxqYXZhL2xhbmcvT2JqZWN0O3hw\n"
                        + "AAAAAAAAAABw",
                PrimitiveTuples.pair(null, 0.0));
    }

    @Test
    public void objectFloatPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0Rmxv\n"
                        + "YXRQYWlySW1wbAAAAAAAAAABAgACRgADdHdvTAADb25ldAASTGphdmEvbGFuZy9PYmplY3Q7eHAA\n"
                        + "AAAAcA==",
                PrimitiveTuples.pair(null, 0.0f));
    }

    @Test
    public void objectIntPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0SW50\n"
                        + "UGFpckltcGwAAAAAAAAAAQIAAkkAA3R3b0wAA29uZXQAEkxqYXZhL2xhbmcvT2JqZWN0O3hwAAAA\n"
                        + "AHA=",
                PrimitiveTuples.pair(null, 0));
    }

    @Test
    public void objectLongPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0TG9u\n"
                        + "Z1BhaXJJbXBsAAAAAAAAAAECAAJKAAN0d29MAANvbmV0ABJMamF2YS9sYW5nL09iamVjdDt4cAAA\n"
                        + "AAAAAAAAcA==",
                PrimitiveTuples.pair(null, 0L));
    }

    @Test
    public void objectShortPair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5wcmltaXRpdmUuT2JqZWN0U2hv\n"
                        + "cnRQYWlySW1wbAAAAAAAAAABAgACUwADdHdvTAADb25ldAASTGphdmEvbGFuZy9PYmplY3Q7eHAA\n"
                        + "AHA=",
                PrimitiveTuples.pair(null, (short) 0));
    }
}
