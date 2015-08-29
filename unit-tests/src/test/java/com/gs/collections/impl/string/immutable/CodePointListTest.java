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

import java.io.IOException;
import java.util.stream.Collectors;

import com.gs.collections.api.IntIterable;
import com.gs.collections.api.list.primitive.ImmutableIntList;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.list.immutable.primitive.AbstractImmutableIntListTestCase;
import org.junit.Assert;
import org.junit.Test;

public class CodePointListTest extends AbstractImmutableIntListTestCase
{
    @Override
    protected ImmutableIntList classUnderTest()
    {
        return CodePointList.build(1, 2, 3);
    }

    @Override
    protected ImmutableIntList newWith(int... elements)
    {
        return CodePointList.build(elements);
    }

    @Override
    @Test
    public void max()
    {
        Assert.assertEquals(9L, this.newWith(1, 2, 9).max());
        Assert.assertEquals(32L, this.newWith(1, 0, 9, 30, 31, 32).max());
        Assert.assertEquals(32L, this.newWith(0, 9, 30, 31, 32).max());
        Assert.assertEquals(31L, this.newWith(31, 0, 30).max());
        Assert.assertEquals(39L, this.newWith(32, 39, 35).max());
        Assert.assertEquals(this.classUnderTest().size(), this.classUnderTest().max());
    }

    @Override
    @Test
    public void min()
    {
        Assert.assertEquals(1L, this.newWith(1, 2, 9).min());
        Assert.assertEquals(0L, this.newWith(1, 0, 9, 30, 31, 32).min());
        Assert.assertEquals(31L, this.newWith(31, 32, 33).min());
        Assert.assertEquals(32L, this.newWith(32, 39, 35).min());
        Assert.assertEquals(1L, this.classUnderTest().min());
    }

    @Override
    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(this.newWith(1, 0, 2).allSatisfy(IntPredicates.greaterThan(0)));
        Assert.assertTrue(this.newWith(1, 2, 3).allSatisfy(IntPredicates.greaterThan(0)));
        Assert.assertFalse(this.newWith(1, 0, 31, 32).allSatisfy(IntPredicates.greaterThan(0)));
        Assert.assertFalse(this.newWith(1, 0, 31, 32).allSatisfy(IntPredicates.greaterThan(0)));
        Assert.assertTrue(this.newWith(1, 2, 31, 32).allSatisfy(IntPredicates.greaterThan(0)));
        Assert.assertFalse(this.newWith(32).allSatisfy(IntPredicates.equal(33)));
        IntIterable iterable = this.newWith(0, 1, 2);
        Assert.assertFalse(iterable.allSatisfy(value -> 3 < value));
        Assert.assertTrue(iterable.allSatisfy(IntPredicates.lessThan(3)));

        IntIterable iterable1 = this.classUnderTest();
        int size = iterable1.size();
        Assert.assertEquals(size == 0, iterable1.allSatisfy(IntPredicates.greaterThan(3)));
        Assert.assertEquals(size < 3, iterable1.allSatisfy(IntPredicates.lessThan(3)));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.newWith(1, 2).anySatisfy(IntPredicates.greaterThan(0)));
        Assert.assertFalse(this.newWith(1, 2).anySatisfy(IntPredicates.equal(0)));
        Assert.assertTrue(this.newWith(31, 32).anySatisfy(IntPredicates.greaterThan(0)));
        Assert.assertTrue(this.newWith(2, 31, 32).anySatisfy(IntPredicates.greaterThan(0)));
        Assert.assertFalse(this.newWith(1, 31, 32).anySatisfy(IntPredicates.equal(0)));
        Assert.assertTrue(this.newWith(32).anySatisfy(IntPredicates.greaterThan(0)));
        IntIterable iterable = this.newWith(0, 1, 2);
        Assert.assertTrue(iterable.anySatisfy(value -> value < 3));
        Assert.assertFalse(iterable.anySatisfy(IntPredicates.greaterThan(3)));

        IntIterable iterable1 = this.classUnderTest();
        int size = iterable1.size();
        Assert.assertEquals(size > 3, iterable1.anySatisfy(IntPredicates.greaterThan(3)));
        Assert.assertEquals(size != 0, iterable1.anySatisfy(IntPredicates.lessThan(3)));
    }

    @Override
    @Test
    public void testToString()
    {
        StringBuilder expectedString = new StringBuilder("[");
        int size = this.classUnderTest().size();
        for (int each = 0; each < size; each++)
        {
            expectedString.appendCodePoint(each + 1);
            expectedString.append(each == size - 1 ? "" : ", ");
        }
        expectedString.append(']');
        Assert.assertEquals(expectedString.toString(), this.classUnderTest().toString());
    }

    @Override
    @Test
    public void makeString()
    {
        ImmutableIntList list = this.classUnderTest();
        StringBuilder expectedString = new StringBuilder("");
        StringBuilder expectedString1 = new StringBuilder("");
        int size = list.size();
        for (int each = 0; each < size; each++)
        {
            expectedString.appendCodePoint(each + 1);
            expectedString1.appendCodePoint(each + 1);
            expectedString.append(each == size - 1 ? "" : ", ");
            expectedString1.append(each == size - 1 ? "" : "/");
        }
        Assert.assertEquals(expectedString.toString(), list.makeString());
        Assert.assertEquals(expectedString1.toString(), list.makeString("/"));
        Assert.assertEquals(this.classUnderTest().toString(), this.classUnderTest().makeString("[", ", ", "]"));
    }

    @Override
    @Test
    public void appendString()
    {
        StringBuilder expectedString = new StringBuilder("");
        StringBuilder expectedString1 = new StringBuilder("");
        int size = this.classUnderTest().size();
        for (int each = 0; each < size; each++)
        {
            expectedString.appendCodePoint(each + 1);
            expectedString1.appendCodePoint(each + 1);
            expectedString.append(each == size - 1 ? "" : ", ");
            expectedString1.append(each == size - 1 ? "" : "/");
        }
        ImmutableIntList list = this.classUnderTest();
        StringBuilder appendable2 = new StringBuilder();
        list.appendString(appendable2);
        Assert.assertEquals(expectedString.toString(), appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        list.appendString(appendable3, "/");
        Assert.assertEquals(expectedString1.toString(), appendable3.toString());
        StringBuilder appendable4 = new StringBuilder();
        this.classUnderTest().appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(this.classUnderTest().toString(), appendable4.toString());
    }

    @SuppressWarnings("StringBufferMayBeStringBuilder")
    @Test
    public void appendStringStringBuffer()
    {
        StringBuffer expectedString = new StringBuffer("");
        StringBuffer expectedString1 = new StringBuffer("");
        int size = this.classUnderTest().size();
        for (int each = 0; each < size; each++)
        {
            expectedString.appendCodePoint(each + 1);
            expectedString1.appendCodePoint(each + 1);
            expectedString.append(each == size - 1 ? "" : ", ");
            expectedString1.append(each == size - 1 ? "" : "/");
        }
        ImmutableIntList list = this.classUnderTest();
        StringBuffer appendable2 = new StringBuffer();
        list.appendString(appendable2);
        Assert.assertEquals(expectedString.toString(), appendable2.toString());
        StringBuffer appendable3 = new StringBuffer();
        list.appendString(appendable3, "/");
        Assert.assertEquals(expectedString1.toString(), appendable3.toString());
        StringBuffer appendable4 = new StringBuffer();
        this.classUnderTest().appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(this.classUnderTest().toString(), appendable4.toString());
    }

    @Test
    public void appendStringAppendable()
    {
        StringBuilder expectedString = new StringBuilder();
        StringBuilder expectedString1 = new StringBuilder();
        int size = this.classUnderTest().size();
        for (int each = 0; each < size; each++)
        {
            expectedString.appendCodePoint(each + 1);
            expectedString1.appendCodePoint(each + 1);
            expectedString.append(each == size - 1 ? "" : ", ");
            expectedString1.append(each == size - 1 ? "" : "/");
        }
        ImmutableIntList list = this.classUnderTest();
        SBAppendable appendable2 = new SBAppendable();
        list.appendString(appendable2);
        Assert.assertEquals(expectedString.toString(), appendable2.toString());
        SBAppendable appendable3 = new SBAppendable();
        list.appendString(appendable3, "/");
        Assert.assertEquals(expectedString1.toString(), appendable3.toString());
        SBAppendable appendable4 = new SBAppendable();
        this.classUnderTest().appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(this.classUnderTest().toString(), appendable4.toString());
    }

    @Test
    public void collectCodePointUnicode()
    {
        Assert.assertEquals(
                "あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06".codePoints().boxed().collect(Collectors.toList()),
                CodePointList.from("あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06").collect(i -> i));
        Assert.assertEquals(
                "あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06".codePoints().boxed().collect(Collectors.toList()),
                CodePointList.from("あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06").collect(i -> i));
    }

    @Test
    public void selectCodePointUnicode()
    {
        String string = CodePointList.from("あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06").select(Character::isBmpCodePoint).buildString();
        Assert.assertEquals("あいう", string);
    }

    @Test
    public void allSatisfyUnicode()
    {
        Assert.assertTrue(CodePointList.from("あいう").allSatisfy(Character::isBmpCodePoint));
        Assert.assertFalse(CodePointList.from("\uD840\uDC00\uD840\uDC03\uD840\uDC06").allSatisfy(Character::isBmpCodePoint));
    }

    @Test
    public void anySatisfyUnicode()
    {
        Assert.assertTrue(CodePointList.from("あいう").anySatisfy(Character::isBmpCodePoint));
        Assert.assertFalse(CodePointList.from("\uD840\uDC00\uD840\uDC03\uD840\uDC06").anySatisfy(Character::isBmpCodePoint));
    }

    @Test
    public void noneSatisfyUnicode()
    {
        Assert.assertFalse(CodePointList.from("あいう").noneSatisfy(Character::isBmpCodePoint));
        Assert.assertTrue(CodePointList.from("\uD840\uDC00\uD840\uDC03\uD840\uDC06").noneSatisfy(Character::isBmpCodePoint));
    }

    @Test
    public void forEachUnicode()
    {
        StringBuilder builder = new StringBuilder();
        CodePointList.from("あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06").forEach(builder::appendCodePoint);
        Assert.assertEquals("あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06", builder.toString());
    }

    @Test
    public void asReversedForEachUnicode()
    {
        StringBuilder builder = new StringBuilder();
        CodePointList.from("あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06").asReversed().forEach(builder::appendCodePoint);
        Assert.assertEquals("\uD840\uDC06う\uD840\uDC03い\uD840\uDC00あ", builder.toString());
        CodePointList.from("").asReversed().forEach((int codePoint) -> Assert.fail());
    }

    @Test
    public void toReversedForEachUnicode()
    {
        StringBuilder builder = new StringBuilder();
        CodePointList.from("あ\uD840\uDC00い\uD840\uDC03う\uD840\uDC06").toReversed().forEach(builder::appendCodePoint);
        Assert.assertEquals("\uD840\uDC06う\uD840\uDC03い\uD840\uDC00あ", builder.toString());
        CodePointList.from("").toReversed().forEach((int codePoint) -> Assert.fail());
    }

    @Test
    public void distinctUnicode()
    {
        Assert.assertEquals(
                "\uD840\uDC00\uD840\uDC03\uD840\uDC06",
                CodePointList.from("\uD840\uDC00\uD840\uDC03\uD840\uDC06\uD840\uDC00\uD840\uDC03\uD840\uDC06").distinct().buildString());
    }

    @Override
    public void toReversed()
    {
        super.toReversed();
        Assert.assertEquals("cba", CodePointList.from("abc").toReversed().buildString());
    }

    private static class SBAppendable implements Appendable
    {
        private final StringBuilder builder = new StringBuilder();

        @Override
        public Appendable append(char c) throws IOException
        {
            return this.builder.append(c);
        }

        @Override
        public Appendable append(CharSequence csq) throws IOException
        {
            return this.builder.append(csq);
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) throws IOException
        {
            return this.builder.append(csq, start, end);
        }

        @Override
        public String toString()
        {
            return this.builder.toString();
        }
    }
}
