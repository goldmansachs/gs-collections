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

package com.gs.collections.impl.map.mutable.primitive;

import java.io.IOException;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;

public class BooleanObjectHashMap<V>
{
    private boolean containsTrueKey;
    private boolean containsFalseKey;

    private V trueValue;
    private V falseValue;

    public static <V> BooleanObjectHashMap<V> newMap()
    {
        return new BooleanObjectHashMap<V>();
    }

    public static <V> BooleanObjectHashMap<V> newWithKeysValues(boolean key, V value)
    {
        return new BooleanObjectHashMap<V>().withKeyValue(key, value);
    }

    public static <V> BooleanObjectHashMap<V> newWithKeysValues(boolean key1, V value1, boolean key2, V value2)
    {
        return new BooleanObjectHashMap<V>().withKeysValues(key1, value1, key2, value2);
    }

    public V put(boolean key, V value)
    {
        if (key)
        {
            V oldValue = this.trueValue;
            this.containsTrueKey = true;
            this.trueValue = value;
            return oldValue;
        }
        V oldValue = this.falseValue;
        this.containsFalseKey = true;
        this.falseValue = value;
        return oldValue;
    }

    public V get(boolean key)
    {
        return key ? this.trueValue : this.falseValue;
    }

    public V getIfAbsentPut(boolean key, Function0<? extends V> function)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                return this.trueValue;
            }
            this.containsTrueKey = true;
            this.trueValue = function.value();
            return this.trueValue;
        }
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        this.containsFalseKey = true;
        this.falseValue = function.value();
        return this.falseValue;
    }

    public <P> V getIfAbsentPutWith(boolean key, Function<? super P, ? extends V> function, P parameter)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                return this.trueValue;
            }
            this.containsTrueKey = true;
            this.trueValue = function.valueOf(parameter);
            return this.trueValue;
        }
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        this.containsFalseKey = true;
        this.falseValue = function.valueOf(parameter);
        return this.falseValue;
    }

    public V getIfAbsentPutWithKey(boolean key, BooleanToObjectFunction<? extends V> function)
    {
        if (key)
        {
            if (this.containsTrueKey)
            {
                return this.trueValue;
            }
            this.containsTrueKey = true;
            this.trueValue = function.valueOf(true);
            return this.trueValue;
        }
        if (this.containsFalseKey)
        {
            return this.falseValue;
        }
        this.containsFalseKey = true;
        this.falseValue = function.valueOf(false);
        return this.falseValue;
    }

    public V removeKey(boolean key)
    {
        if (key)
        {
            V oldValue = this.trueValue;
            this.containsTrueKey = false;
            this.trueValue = null;
            return oldValue;
        }
        V oldValue = this.falseValue;
        this.containsFalseKey = false;
        this.falseValue = null;
        return oldValue;
    }

    public boolean containsKey(boolean key)
    {
        return key ? this.containsTrueKey : this.containsFalseKey;
    }

    public boolean containsValue(V value)
    {
        return (this.containsTrueKey && nullSafeEquals(this.trueValue, value))
                || (this.containsFalseKey && nullSafeEquals(this.falseValue, value));
    }

    public int size()
    {
        return (this.containsTrueKey ? 1 : 0) + (this.containsFalseKey ? 1 : 0);
    }

    public boolean isEmpty()
    {
        return !this.containsTrueKey && !this.containsFalseKey;
    }

    public boolean notEmpty()
    {
        return this.containsTrueKey || this.containsFalseKey;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof BooleanObjectHashMap))
        {
            return false;
        }

        BooleanObjectHashMap<V> other = (BooleanObjectHashMap<V>) obj;

        if (this.size() != other.size())
        {
            return false;
        }

        if (this.containsTrueKey && (!other.containsKey(true) || !nullSafeEquals(this.trueValue, other.get(true))))
        {
            return false;
        }

        if (this.containsFalseKey && (!other.containsKey(false) || !nullSafeEquals(this.falseValue, other.get(false))))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = 0;

        if (this.containsTrueKey)
        {
            result += 1231 ^ (this.trueValue == null ? 0 : this.trueValue.hashCode());
        }

        if (this.containsFalseKey)
        {
            result += 1237 ^ (this.falseValue == null ? 0 : this.falseValue.hashCode());
        }

        return result;
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);

            if (this.containsTrueKey)
            {
                appendable.append("true=").append(String.valueOf(this.trueValue));
            }
            if (this.containsFalseKey)
            {
                if (this.containsTrueKey)
                {
                    appendable.append(separator);
                }
                appendable.append("false=").append(String.valueOf(this.falseValue.toString()));
            }
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public BooleanObjectHashMap<V> withKeyValue(boolean key, V value)
    {
        this.put(key, value);
        return this;
    }

    public BooleanObjectHashMap<V> withKeysValues(boolean key1, V value1, boolean key2, V value2)
    {
        this.put(key1, value1);
        this.put(key2, value2);
        return this;
    }

    private static boolean nullSafeEquals(Object value, Object other)
    {
        if (value == null)
        {
            if (other == null)
            {
                return true;
            }
        }
        else if (other == value || value.equals(other))
        {
            return true;
        }
        return false;
    }
}
