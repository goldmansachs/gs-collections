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

package com.gs.collections.impl.jmh.domain;

import java.math.BigDecimal;

public final class Product
{
    private final String name;
    private final String category;
    private final double price;

    public Product(String name, String category, double price)
    {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName()
    {
        return this.name;
    }

    public double getPrice()
    {
        return this.price;
    }

    public BigDecimal getPrecisePrice()
    {
        return BigDecimal.valueOf(this.getPrice());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        Product account = (Product) o;

        return this.name.equals(account.name);
    }

    public String getCategory()
    {
        return this.category;
    }

    @Override
    public int hashCode()
    {
        return this.name.hashCode();
    }

    @Override
    public String toString()
    {
        return "Product{"
                + "name='" + this.name + '\''
                + ", category='" + this.category + '\''
                + ", price=" + this.price
                + '}';
    }
}
