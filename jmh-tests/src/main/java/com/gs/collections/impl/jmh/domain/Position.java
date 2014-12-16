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

public final class Position
{
    private final Account account;
    private final Product product;
    private final int quantity;

    public Position(Account account, Product product, int quantity)
    {
        this.account = account;
        this.product = product;
        this.quantity = quantity;
    }

    public Account getAccount()
    {
        return this.account;
    }

    public Product getProduct()
    {
        return this.product;
    }

    public String getCategory()
    {
        return this.product.getCategory();
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public double getMarketValue()
    {
        return this.quantity * this.product.getPrice();
    }

    public BigDecimal getPreciseMarketValue()
    {
        return BigDecimal.valueOf(this.quantity).multiply(this.product.getPrecisePrice());
    }
}
