/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.block.procedure;

import java.io.IOException;

import com.gs.collections.api.block.procedure.Procedure;

public class AppendStringProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;
    private final Appendable appendable;
    private final String separator;
    private boolean first = true;

    public AppendStringProcedure(Appendable appendable, String separator)
    {
        this.appendable = appendable;
        this.separator = separator;
    }

    public void value(T each)
    {
        try
        {
            if (this.first)
            {
                this.first = false;
            }
            else
            {
                this.appendable.append(this.separator);
            }
            this.appendable.append(String.valueOf(each));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
