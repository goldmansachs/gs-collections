package com.gs.collections.impl.memory;

import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.primitive.IntInterval;

public final class TestDataFactory
{

    public static final IntToObjectFunction<Integer> INTEGER_VALUE_OF = new IntToObjectFunction<Integer>()
    {
        public Integer valueOf(int each)
        {
            return Integer.valueOf(each);
        }
    };

    private TestDataFactory()
    {
    }

    public static IntList create(int size)
    {
        if (size > 0)
        {
            return IntInterval.from(-(size / 2) + 1).to(size / 2);
        }
        return new IntArrayList();
    }

    public static ImmutableList<Integer> createImmutableList(int size)
    {
        return TestDataFactory.create(size).collect(INTEGER_VALUE_OF).toList().toImmutable();
    }
}
