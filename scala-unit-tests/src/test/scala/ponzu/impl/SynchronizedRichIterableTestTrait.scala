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

package ponzu.impl

import java.lang.StringBuilder

import list.mutable.FastList
import multimap.list.FastListMultimap
import org.junit.Test

import ponzu.api.RichIterable
import ponzu.api.collection.MutableCollection
import ponzu.api.tuple.Pair
import ponzu.api.multimap.MutableMultimap
import Prelude._
import ponzu.api.list.MutableList

trait SynchronizedRichIterableTestTrait extends SynchronizedMutableIterableTestTrait /* with RichIterableTestTrait */
{
    val classUnderTest: RichIterable[String]

    @Test
    def size_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.size
        }
    }

    @Test
    def isEmpty_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.isEmpty
        }
    }

    @Test
    def notEmpty_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.notEmpty
        }
    }

    @Test
    def getFirst_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getFirst
        }
    }

    @Test
    def getLast_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getLast
        }
    }

    @Test
    def contains_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.contains(null)
        }
    }

    @Test
    def containsAllIterable_synchronized
    {
        this.assertSynchronized
        {
            val iterable: java.lang.Iterable[_] = FastList.newList[AnyRef]
            this.classUnderTest.containsAllIterable(iterable)
        }
    }

    @Test
    def containsAllArguments_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.containsAllArguments("", "", "")
        }
    }

    @Test
    def filter_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filter({
                _: String => false
            })
        }
    }

    @Test
    def filter_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filter({
                _: String => false
            }, FastList.newList[String])
        }
    }

    @Test
    def filterNot_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filterNot({
                _: String => true
            })
        }
    }

    @Test
    def filterNot_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filterNot({
                _: String => true
            }, null)
        }
    }

    @Test
    def partition_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.partition({
                _: String => true
            })
        }
    }

    @Test
    def transform_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.transform({
                _: String => null
            })
        }
    }

    @Test
    def transform_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.transform[String, MutableCollection[String]](
            {
                _: String => ""
            },
            FastList.newList[String])
        }
    }

    @Test
    def transformIf_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.transformIf({
                _: String => false
            },
            {
                _: String => ""
            })
        }
    }

    @Test
    def transformIf_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.transformIf[String, MutableCollection[String]](
            {
                _: String => false
            },
            {
                _: String => ""
            },
            FastList.newList[String])
        }
    }

    @Test
    def flatTransform_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.flatTransform({
                _: String => FastList.newList[String]
            })
        }
    }

    @Test
    def flatTransform_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.flatTransform[String, MutableList[String]]({
                _: String => FastList.newList[String]
            }, FastList.newList[String])
        }
    }

    @Test
    def find_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.find({
                _: String => false
            })
        }
    }

    @Test
    def findIfNone_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.findIfNone({
                _: String => false
            },
            {
                () => ""
            })
        }
    }

    @Test
    def count_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.count({
                _: String => false
            })
        }
    }

    @Test
    def anySatisfy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.anySatisfy({
                _: String => true
            })
        }
    }

    @Test
    def allSatisfy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.allSatisfy({
                _: String => false
            })
        }
    }

    @Test
    def foldLeft_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.foldLeft[String]("",
            {
                (_: String, _: String) => ""
            })
        }
    }

    @Test
    def toList_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toList
        }
    }

    @Test
    def toSortedList_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSortedList
        }
    }

    @Test
    def toSortedList_with_comparator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSortedList(null)
        }
    }

    @Test
    def toSortedListBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSortedListBy[String]((string: String) => string)
        }
    }

    @Test
    def toSet_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSet
        }
    }

    @Test
    def toMap_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toMap({
                _: String => ""
            },
            {
                _: String => ""
            })
        }
    }

    @Test
    def toArray_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toArray
        }
    }

    @Test
    def toArray_with_target_synchronized
    {
        this.assertSynchronized
        {
            val array: Array[String] = new Array[String](this.classUnderTest.size())
            this.classUnderTest.toArray(array)
        }
    }

    @Test
    def max_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.max({
                (_: String, _: String) => 0
            })
        }
    }

    @Test
    def min_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.min({
                (_: String, _: String) => 0
            })
        }
    }

    @Test
    def max_without_comparator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.max()
        }
    }

    @Test
    def min_without_comparator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.min()
        }
    }

    @Test
    def maxBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.maxBy[String]((string: String) => string)
        }
    }

    @Test
    def minBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.minBy[String]((string: String) => string)
        }
    }

    @Test
    def makeString_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.makeString
        }
    }

    @Test
    def makeString_with_separator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.makeString(", ")
        }
    }

    @Test
    def makeString_with_start_separator_end_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.makeString("[", ", ", "]")
        }
    }

    @Test
    def appendString_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.appendString(new StringBuilder)
        }
    }

    @Test
    def appendString_with_separator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.appendString(new StringBuilder, ", ")
        }
    }

    @Test
    def appendString_with_start_separator_end_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.appendString(new StringBuilder, "[", ", ", "]")
        }
    }

    @Test
    def groupBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.groupBy({
                _: String => ""
            })
        }
    }

    @Test
    def groupBy_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.groupBy[String, MutableMultimap[String, String]](
            {
                _: String => ""
            },
            FastListMultimap.newMultimap[String, String])
        }
    }

    @Test
    def toString_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toString
        }
    }

    @Test
    def zip_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zip[String](FastList.newList[String])
        }
    }

    @Test
    def zip__with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zip[String, FastList[Pair[String, String]]](FastList.newList[String](),
                FastList.newList[Pair[String, String]]())
        }
    }

    @Test
    def zipWithIndex_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zipWithIndex()
        }
    }

    @Test
    def zipWithIndex__with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zipWithIndex(FastList.newList[Pair[String, java.lang.Integer]]())
        }
    }
}
