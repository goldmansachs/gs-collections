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

package com.gs.collections.codegenerator.tools;

import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.NumberRenderer;
import org.stringtemplate.v4.StringRenderer;

public class IntegerOrStringRenderer implements AttributeRenderer
{
    private final AttributeRenderer numberRenderer = new NumberRenderer();
    private final AttributeRenderer stringRenderer = new StringRenderer();

    public String toString(Object object, String formatString, Locale locale)
    {
        if (!(object instanceof String))
        {
            throw new RuntimeException("Only works on Strings");
        }
        try
        {
            Integer integer = Integer.valueOf((String) object);
            return this.numberRenderer.toString(integer, formatString, locale);
        }
        catch (NumberFormatException ignored)
        {
            return this.stringRenderer.toString(object, formatString, locale);
        }
    }
}
