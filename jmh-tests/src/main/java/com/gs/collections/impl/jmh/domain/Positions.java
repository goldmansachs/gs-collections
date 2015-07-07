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

package com.gs.collections.impl.jmh.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PrimitiveIterator;
import java.util.Random;

import com.gs.collections.api.set.Pool;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.apache.commons.lang.RandomStringUtils;

public class Positions
{
    private static final int DEFAULT_SIZE = 3_000_000;
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final PrimitiveIterator.OfDouble DOUBLES = RANDOM.ints(1, 100).asDoubleStream().iterator();
    private static final PrimitiveIterator.OfInt INTS = RANDOM.ints(1, 100).iterator();
    private final Pool<Account> accountPool = UnifiedSet.newSet();
    private final Pool<Product> productPool = UnifiedSet.newSet();
    private final Pool<String> stringPool = UnifiedSet.newSet();

    private final FastList<Position> gscPositions;
    private final ArrayList<Position> jdkPositions;

    public Positions()
    {
        this(DEFAULT_SIZE);
    }

    public Positions(int size)
    {
        this.gscPositions = FastList.newWithNValues(size, this::createPosition);
        this.jdkPositions = new ArrayList<>(FastList.newWithNValues(size, this::createPosition));
    }

    public Positions shuffle()
    {
        this.gscPositions.shuffleThis();
        Collections.shuffle(this.jdkPositions);
        return this;
    }

    public Position createPosition()
    {
        String accountName = this.stringPool.put(RandomStringUtils.randomNumeric(5));
        String category = this.stringPool.put(RandomStringUtils.randomAlphabetic(1).toUpperCase());
        String productName = this.stringPool.put(RandomStringUtils.randomNumeric(3));
        Account account = this.accountPool.put(new Account(accountName));
        Product product = this.productPool.put(new Product(productName, category, DOUBLES.nextDouble()));
        return new Position(account, product, INTS.nextInt());
    }

    public FastList<Position> getGscPositions()
    {
        return this.gscPositions;
    }

    public ArrayList<Position> getJdkPositions()
    {
        return this.jdkPositions;
    }
}
