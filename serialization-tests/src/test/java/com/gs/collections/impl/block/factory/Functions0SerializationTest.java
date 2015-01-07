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

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class Functions0SerializationTest
{
    @Test
    public void throwing()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "VGhyb3dpbmdGdW5jdGlvbjBBZGFwdGVyAAAAAAAAAAECAAFMABF0aHJvd2luZ0Z1bmN0aW9uMHQA\n"
                        + "Qkxjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9ibG9jay9mdW5jdGlvbi9jaGVja2VkL1Rocm93aW5n\n"
                        + "RnVuY3Rpb24wO3hyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5jaGVj\n"
                        + "a2VkLkNoZWNrZWRGdW5jdGlvbjAAAAAAAAAAAQIAAHhwcA==",
                Functions0.throwing(null));
    }

    @Test
    public void getTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "VHJ1ZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions0.getTrue());
    }

    @Test
    public void getFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "RmFsc2VGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions0.getFalse());
    }

    @Test
    public void newFastList()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "TmV3RmFzdExpc3RGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions0.newFastList());
    }

    @Test
    public void newUnified()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "TmV3VW5pZmllZFNldEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions0.newUnifiedSet());
    }

    @Test
    public void newHashBag()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "TmV3SGFzaEJhZ0Z1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions0.newHashBag());
    }

    @Test
    public void newUnifiedMap()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "TmV3VW5pZmllZE1hcEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                Functions0.newUnifiedMap());
    }

    @Test
    public void zeroAtomicInteger()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "QXRvbWljSW50ZWdlclplcm9GdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions0.zeroAtomicInteger());
    }

    @Test
    public void zeroAtomicLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczAk\n"
                        + "QXRvbWljTG9uZ1plcm9GdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                Functions0.zeroAtomicLong());
    }

    @Test
    public void value()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5QYXNzVGhydUZ1\n"
                        + "bmN0aW9uMAAAAAAAAAABAgABTAAGcmVzdWx0dAASTGphdmEvbGFuZy9PYmplY3Q7eHBw",
                Functions0.value(null));
    }
}
