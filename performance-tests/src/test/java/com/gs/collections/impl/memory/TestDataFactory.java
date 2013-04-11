package com.gs.collections.impl.memory;

import java.util.Random;

import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.set.primitive.IntSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;

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

    public static IntSet createRandomSet(int size)
    {
        if (size > 0)
        {
            MutableIntSet set = new IntHashSet();
            Random random = new Random((long) size);
            while (set.size() < size)
            {
                set.add(random.nextInt(size * 10));
            }
            return set;
        }
        return new IntHashSet();
    }

    public static ImmutableList<Integer> createImmutableList(int size)
    {
        return TestDataFactory.create(size)
                .collect(INTEGER_VALUE_OF)
                .toList()
                .toImmutable();
    }

    public static ImmutableList<Integer> createRandomImmutableList(int size)
    {
        return TestDataFactory.createRandomSet(size)
                .collect(INTEGER_VALUE_OF)
                .toList()
                .toImmutable();
    }

}
