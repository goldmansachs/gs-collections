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

package com.gs.collections.impl.block.procedure.primitive;

import java.io.Serializable;

/**
 * An IntIntProcedure is a two argument Closure which has no return argument and takes an int as the first and
 * second arguments.
 *
 * @deprecated since 3.0 use {@link com.gs.collections.api.block.procedure.primitive.IntIntProcedure}
 */
@Deprecated
public interface IntIntProcedure extends Serializable
{
    void value(int each, int index);
}
