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

package com.gs.collections.impl.collection.mutable;

import java.util.ArrayList;
import java.util.List;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions2;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import org.junit.Assert;
import org.junit.Test;

public class CollectionAdapterAsUnmodifiableTest extends UnmodifiableMutableCollectionTestCase<Integer>
{
    @Override
    protected MutableCollection<Integer> getCollection()
    {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        return new CollectionAdapter<Integer>(list).asUnmodifiable();
    }

    @Override
    @Test
    public void select()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().select(Predicates.alwaysTrue()));
        Assert.assertNotEquals(this.getCollection().toList(), this.getCollection().select(Predicates.alwaysFalse()));
    }

    @Override
    @Test
    public void selectWith()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().selectWith(Predicates2.alwaysTrue(), null));
        Assert.assertNotEquals(this.getCollection().toList(), this.getCollection().selectWith(Predicates2.alwaysFalse(), null));
    }

    @Override
    @Test
    public void reject()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().reject(Predicates.alwaysFalse()));
        Assert.assertNotEquals(this.getCollection().toList(), this.getCollection().reject(Predicates.alwaysTrue()));
    }

    @Override
    @Test
    public void rejectWith()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().rejectWith(Predicates2.alwaysFalse(), null));
        Assert.assertNotEquals(this.getCollection().toList(), this.getCollection().rejectWith(Predicates2.alwaysTrue(), null));
    }

    @Override
    @Test
    public void partition()
    {
        PartitionMutableCollection<?> partition = this.getCollection().partition(Predicates.alwaysTrue());
        Assert.assertEquals(this.getCollection().toList(), partition.getSelected());
        Assert.assertNotEquals(this.getCollection().toList(), partition.getRejected());
    }

    @Override
    @Test
    public void collect()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().collect(Functions.getPassThru()));
        Assert.assertNotEquals(this.getCollection().toList(), this.getCollection().collect(Functions.getToClass()));
    }

    @Override
    @Test
    public void collectWith()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().collectWith(Functions2.fromFunction(Functions.getPassThru()), null));
        Assert.assertNotEquals(this.getCollection().toList(), this.getCollection().collectWith(Functions2.fromFunction(Functions.getToClass()), null));
    }

    @Override
    @Test
    public void collectIf()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().collectIf(Predicates.alwaysTrue(), Functions.getPassThru()));
        Assert.assertNotEquals(this.getCollection().toList(), this.getCollection().collectIf(Predicates.alwaysFalse(), Functions.getToClass()));
    }
}
