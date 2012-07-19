package com.gs.collections.impl.stack.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedStackTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLlN5bmNocm9uaXpl\n" +
                        "ZFN0YWNrAAAAAAAAAAECAAJMAAhkZWxlZ2F0ZXQAK0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL3N0\n" +
                        "YWNrL011dGFibGVTdGFjaztMAARsb2NrdAASTGphdmEvbGFuZy9PYmplY3Q7eHBzcgAwY29tLmdz\n" +
                        "LmNvbGxlY3Rpb25zLmltcGwuc3RhY2subXV0YWJsZS5BcnJheVN0YWNrAAAAAAAAAAEMAAB4cHcE\n" +
                        "AAAAAHhxAH4AAw==",
                SynchronizedStack.of(ArrayStack.newStack()));
    }
}