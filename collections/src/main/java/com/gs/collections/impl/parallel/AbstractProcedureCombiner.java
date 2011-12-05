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

package com.gs.collections.impl.parallel;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.utility.Iterate;

public abstract class AbstractProcedureCombiner<BT>
        implements Combiner<BT>
{
    private static final long serialVersionUID = 1L;

    private boolean useCombineOne;

    protected AbstractProcedureCombiner(boolean useCombineOne)
    {
        this.useCombineOne = useCombineOne;
    }

    public void combineAll(Iterable<BT> thingsToCombine)
    {
        Iterate.forEach(thingsToCombine, new Procedure<BT>()
        {
            public void value(BT object)
            {
                AbstractProcedureCombiner.this.combineOne(object);
            }
        });
    }

    public boolean useCombineOne()
    {
        return this.useCombineOne;
    }

    public void setCombineOne(boolean newBoolean)
    {
        this.useCombineOne = newBoolean;
    }
}
