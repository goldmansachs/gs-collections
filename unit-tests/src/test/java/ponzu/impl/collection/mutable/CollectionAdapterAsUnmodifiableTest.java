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

package ponzu.impl.collection.mutable;

import java.util.ArrayList;
import java.util.List;

import ponzu.api.collection.MutableCollection;
import ponzu.api.partition.PartitionMutableCollection;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.Functions2;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.factory.Predicates2;
import ponzu.impl.test.Verify;
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
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().filter(Predicates.alwaysTrue()));
        Verify.assertNotEquals(this.getCollection().toList(), this.getCollection().filter(Predicates.alwaysFalse()));
    }

    @Override
    @Test
    public void selectWith()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().filterWith(Predicates2.alwaysTrue(), null));
        Verify.assertNotEquals(this.getCollection().toList(), this.getCollection().filterWith(Predicates2.alwaysFalse(), null));
    }

    @Override
    @Test
    public void reject()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().filterNot(Predicates.alwaysFalse()));
        Verify.assertNotEquals(this.getCollection().toList(), this.getCollection().filterNot(Predicates.alwaysTrue()));
    }

    @Override
    @Test
    public void rejectWith()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().filterNotWith(Predicates2.alwaysFalse(), null));
        Verify.assertNotEquals(this.getCollection().toList(), this.getCollection().filterNotWith(Predicates2.alwaysTrue(), null));
    }

    @Override
    @Test
    public void partition()
    {
        PartitionMutableCollection<?> partition = this.getCollection().partition(Predicates.alwaysTrue());
        Assert.assertEquals(this.getCollection().toList(), partition.getSelected());
        Verify.assertNotEquals(this.getCollection().toList(), partition.getRejected());
    }

    @Override
    @Test
    public void collect()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().transform(Functions.getPassThru()));
        Verify.assertNotEquals(this.getCollection().toList(), this.getCollection().transform(Functions.getToClass()));
    }

    @Override
    @Test
    public void collectWith()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().transformWith(Functions2.fromFunction(Functions.getPassThru()), null));
        Verify.assertNotEquals(this.getCollection().toList(), this.getCollection().transformWith(Functions2.fromFunction(Functions.getToClass()), null));
    }

    @Override
    @Test
    public void collectIf()
    {
        Assert.assertEquals(this.getCollection().toList(), this.getCollection().transformIf(Predicates.alwaysTrue(), Functions.getPassThru()));
        Verify.assertNotEquals(this.getCollection().toList(), this.getCollection().transformIf(Predicates.alwaysFalse(), Functions.getToClass()));
    }
}
