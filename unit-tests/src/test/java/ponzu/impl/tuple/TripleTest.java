/*
 * Copyright 2012 Kevin Birch <kmb@pobox.com>.
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

package ponzu.impl.tuple;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created: 2012-08-07 18:51
 */
public class TripleTest
{
    private Triple<Integer, Integer, Integer> cut;

    @Before
    public void setUp()
    {
        this.cut = Tuples.triple(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    }

    @Test
    public void access()

    {
        Assert.assertThat(Integer.valueOf(1), CoreMatchers.equalTo(this.cut.getOne()));
        Assert.assertThat(Integer.valueOf(2), CoreMatchers.equalTo(this.cut.getTwo()));
        Assert.assertThat(Integer.valueOf(3), CoreMatchers.equalTo(this.cut.getThree()));
    }

    @Test
    public void testToString()

    {
        Assert.assertThat(String.format("(%s, %s, %s)", this.cut.getOne(), this.cut.getTwo(), this.cut.getThree()), CoreMatchers.equalTo(this.cut.toString()));
    }
}
