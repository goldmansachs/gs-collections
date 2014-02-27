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

package com.gs.collections.impl.list.mutable

import com.gs.collections.impl.Prelude._
import org.junit.Test
import java.lang.{StringBuilder, Exception}
import com.gs.collections.api.collection.MutableCollection
import com.gs.collections.api.list.MutableList
import com.gs.collections.api.multimap.MutableMultimap
import com.gs.collections.api.tuple.Pair
import com.gs.collections.impl.multimap.list.FastListMultimap

class MultiReaderFastListScalaTest extends MultiReaderFastListTestTrait
{
    override val classUnderTest = MultiReaderFastList.newListWith(1, 2, 3)

    @Test
    def newList_safe
    {
        this.assertWritersNotBlocked
        {
            MultiReaderFastList.newList
            ()
        }
        this.assertReadersNotBlocked
        {
            MultiReaderFastList.newList
            ()
        }
    }

    @Test
    def newListCapacity_safe
    {
        this.assertWritersNotBlocked
        {
            MultiReaderFastList.newList(5)
            ()
        }
        this.assertReadersNotBlocked
        {
            MultiReaderFastList.newList(5)
            ()
        }
    }

    @Test
    def newListIterable_safe
    {
        this.assertWritersNotBlocked
        {
            MultiReaderFastList.newList(FastList.newListWith(1, 2))
            ()
        }
        this.assertReadersNotBlocked
        {
            MultiReaderFastList.newList(FastList.newListWith(1, 2))
            ()
        }
    }

    @Test
    def newListWith_safe
    {
        this.assertWritersNotBlocked
        {
            MultiReaderFastList.newListWith(1, 2)
            ()
        }
        this.assertReadersNotBlocked
        {
            MultiReaderFastList.newListWith(1, 2)
            ()
        }
    }

    @Test
    def clone_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.clone
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.clone
        }
    }

    @Test
    def distinct_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.distinct
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.distinct
        }
    }

    @Test
    def sortThis_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.sortThis
        }

        this.assertReadersBlocked
        {
            this.classUnderTest.sortThis
        }
    }

    @Test
    def sortThis_withComparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.sortThis(null)
        }

        this.assertReadersBlocked
        {
            this.classUnderTest.sortThis(null)
        }
    }

    @Test
    def sortThisBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.sortThisBy[String]({
                _: Int => ""
            })
        }

        this.assertReadersBlocked
        {
            this.classUnderTest.sortThisBy[String]({
                _: Int => ""
            })
        }
    }

    @Test
    def subList_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.subList(0, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.subList(0, 1)
        }
    }

    @Test
    def get_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.get(1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.get(1)
        }
    }

    @Test
    def indexOf_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.get(1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.get(1)
        }
    }

    @Test
    def lastIndexOf_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.get(1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.get(1)
        }
    }

    @Test
    def addWithIndex_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.add(1, 4)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.add(1, 4)
        }
    }

    @Test
    def addAllWithIndex_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.addAll(1, FastList.newListWith(3, 4, 5))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.addAll(1, FastList.newListWith(3, 4, 5))
        }
    }

    @Test
    def removeWithIndex_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.remove(1)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.remove(1)
        }
    }

    @Test
    def set_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.set(1, 4)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.set(1, 4)
        }
    }

    @Test
    def reverseForEach_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.reverseForEach({
                _: Int => ()
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.reverseForEach({
                _: Int => ()
            })
        }
    }

    @Test
    def asReversed_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.asReversed()
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.asReversed()
        }
        val reverseIterable = this.classUnderTest.asReversed()
        this.assertWritersBlocked
        {
            reverseIterable.forEach
            {
                _: Int => ()
            }
        }
        this.assertReadersNotBlocked
        {
            reverseIterable.forEach
            {
                _: Int => ()
            }
        }
    }

    @Test
    def forEachWithIndex_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.forEachWithIndex(0, 2,
            {
                (_: Int, _: Int) => ()
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.forEachWithIndex(0, 2,
            {
                (_: Int, _: Int) => ()
            })
        }
    }

    @Test
    def toReversed_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toReversed
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toReversed
        }
    }

    @Test
    def reverseThis_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.reverseThis
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.reverseThis
        }
    }

    @Test
    def toStack_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toStack
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toStack
        }
    }

    @Test
    def takeWhile_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.takeWhile({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.takeWhile({
                _: Int => true
            })
        }
    }

    @Test
    def dropWhile_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.dropWhile({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.dropWhile({
                _: Int => true
            })
        }
    }

    @Test
    def partitionWhile_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.partitionWhile({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.partitionWhile({
                _: Int => true
            })
        }
    }

    @Test
    def listIterator_safe
    {
        this.assertWritersNotBlocked
        {
            try
            {
                this.classUnderTest.listIterator
            }
            catch
                {
                    case e: Exception => ()
                }
        }
        this.assertReadersNotBlocked
        {
            try
            {
                this.classUnderTest.listIterator
            }
            catch
                {
                    case e: Exception => ()
                }
        }
    }

    @Test
    def listIteratorIndex_safe
    {
        this.assertWritersNotBlocked
        {
            try
            {
                this.classUnderTest.listIterator(1)
            }
            catch
                {
                    case e: Exception => ()
                }
        }
        this.assertReadersNotBlocked
        {
            try
            {
                this.classUnderTest.listIterator(1)
            }
            catch
                {
                    case e: Exception => ()
                }
        }
    }

    @Test
    def iteratorWithReadLock_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.withReadLockAndDelegate({
                each: MutableList[Int] =>
                {
                    each.iterator;
                    ()
                }
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.withReadLockAndDelegate({
                each: MutableList[Int] =>
                {
                    each.iterator;
                    ()
                }
            })
        }
    }

    @Test
    def iteratorWithWriteLock_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.withWriteLockAndDelegate({
                each: MutableList[Int] =>
                {
                    each.iterator;
                    ()
                }
            })
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.withWriteLockAndDelegate({
                each: MutableList[Int] =>
                {
                    each.iterator;
                    ()
                }
            })
        }
    }

    @Test
    def newEmpty_safe
    {
        this.assertWritersNotBlocked
        {
            this.classUnderTest.newEmpty
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.newEmpty
        }
    }

    @Test
    def toImmutable_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toImmutable
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toImmutable
        }
    }

    @Test
    def asUnmodifiable_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.asUnmodifiable
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.asUnmodifiable
        }
    }

    @Test
    def asSynchronized_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.asSynchronized
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.asSynchronized
        }
    }

    @Test
    def iterator_safe
    {
        this.assertWritersNotBlocked
        {
            try
            {
                this.classUnderTest.iterator
            }
            catch
                {
                    case e: Exception => ()
                }
        }
        this.assertReadersNotBlocked
        {
            try
            {
                this.classUnderTest.iterator
            }
            catch
                {
                    case e: Exception => ()
                }
        }
    }

    @Test
    def equals_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.equals(null)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.equals(null)
        }
    }

    @Test
    def hashCode_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.hashCode
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.hashCode
        }
    }

    @Test
    def collect_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.collect[String]({
                _: Int => ""
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.collect[String]({
                _: Int => ""
            })
        }
    }

    @Test
    def collectIf_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.collectIf[String](
            {
                num: Int => num > 1
            },
            {
                num: Int => "" + num
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.collectIf[String](
            {
                num: Int => num > 1
            },
            {
                num: Int => "" + num
            })
        }
    }

    @Test
    def flatCollect_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.flatCollect[Int]({
                num: Int => FastList.newListWith(num)
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.flatCollect[Int]({
                num: Int => FastList.newListWith(num)
            })
        }
    }

    @Test
    def collectWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.collectWith[String, String]({
                (_: Int, _: String) => ""
            }, "")
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.collectWith[String, String]({
                (_: Int, _: String) => ""
            }, "")
        }
    }

    @Test
    def select_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.select({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.select({
                _: Int => true
            })
        }
    }

    @Test
    def selectWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.selectWith(
            {
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.selectWith(
            {
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def selectInstancesOf_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.selectInstancesOf(Int.getClass)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.selectInstancesOf(Int.getClass)
        }
    }

    @Test
    def reject_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.reject({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.reject({
                _: Int => true
            })
        }
    }

    @Test
    def rejectWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.rejectWith(
            {
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.rejectWith(
            {
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def partition_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.partition({
                num: Int => num > 1
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.partition({
                num: Int => num > 1
            })
        }
    }

    @Test
    def groupBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.groupBy[String]({
                num: Int => num.toString
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.groupBy[String]({
                num: Int => num.toString
            })
        }
    }

    @Test
    def groupByEach_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.groupByEach({
                num: Int => FastList.newListWith(num)
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.groupByEach({
                num: Int => FastList.newListWith(num)
            })
        }
    }

    @Test
    def zip_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.zip(FastList.newListWith("1", "1", "2"))
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.zip(FastList.newListWith("1", "1", "2"))
        }
    }

    @Test
    def zipByIndex_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.zipWithIndex
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.zipWithIndex
        }
    }

    @Test
    def chunk_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.chunk(2)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.chunk(2)
        }
    }

    @Test
    def with_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.`with`(4)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.`with`(4)
        }
    }

    @Test
    def without_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.without(1)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.without(1)
        }
    }

    @Test
    def withAll_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.withAll(FastList.newListWith(2, 4))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.withAll(FastList.newListWith(2, 4))
        }
    }

    @Test
    def withoutAll_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.withoutAll(FastList.newListWith(1, 2))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.withoutAll(FastList.newListWith(1, 2))
        }
    }

    @Test
    def selectAndRejectWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.selectAndRejectWith(
            {
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.selectAndRejectWith(
            {
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def collect_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.collect[String, MutableCollection[String]](
            {
                _: Int => ""
            },
            FastList.newList[String])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.collect[String, MutableCollection[String]](
            {
                _: Int => ""
            },
            FastList.newList[String])
        }
    }

    @Test
    def collectIf_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.collectIf[String, MutableCollection[String]](
            {
                num: Int => num > 1
            },
            {
                num: Int => "" + num
            },
            FastList.newList[String])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.collectIf[String, MutableCollection[String]](
            {
                num: Int => num > 1
            },
            {
                num: Int => "" + num
            },
            FastList.newList[String])
        }
    }

    @Test
    def flatCollect_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.flatCollect[Int, MutableList[Int]]({
                num: Int => FastList.newListWith(num)
            }, FastList.newList[Int])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.flatCollect[Int, MutableList[Int]]({
                num: Int => FastList.newListWith(num)
            }, FastList.newList[Int])
        }
    }

    @Test
    def collectWith_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.collectWith[String, String, MutableList[String]]({
                (_: Int, _: String) => ""
            }, "", FastList.newList[String])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.collectWith[String, String, MutableList[String]]({
                (_: Int, _: String) => ""
            }, "", FastList.newList[String])
        }
    }

    @Test
    def select_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.select({
                _: Int => true
            }, FastList.newList[Int])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.select({
                _: Int => true
            }, FastList.newList[Int])
        }
    }

    @Test
    def selectWith_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.selectWith({
                (_: Int, _: Int) => true
            }, 1, FastList.newList[Int])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.selectWith({
                (_: Int, _: Int) => true
            }, 1, FastList.newList[Int])
        }
    }

    @Test
    def reject_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.reject({
                _: Int => true
            }, FastList.newList[Int])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.reject({
                _: Int => true
            }, FastList.newList[Int])
        }
    }

    @Test
    def rejectWith_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.rejectWith({
                (_: Int, _: Int) => true
            }, 1, FastList.newList[Int])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.rejectWith({
                (_: Int, _: Int) => true
            }, 1, FastList.newList[Int])
        }
    }

    @Test
    def groupBy_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.groupBy[String, MutableMultimap[String, Int]](
            {
                num: Int => num.toString
            },
            FastListMultimap.newMultimap[String, Int])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.groupBy[String, MutableMultimap[String, Int]](
            {
                num: Int => num.toString
            },
            FastListMultimap.newMultimap[String, Int])
        }
    }

    @Test
    def groupByEach_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.groupByEach[Int, MutableMultimap[Int, Int]]({
                num: Int => FastList.newListWith(num)
            },
            FastListMultimap.newMultimap[Int, Int])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.groupByEach[Int, MutableMultimap[Int, Int]]({
                num: Int => FastList.newListWith(num)
            },
            FastListMultimap.newMultimap[Int, Int])
        }
    }

    @Test
    def zip_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.zip(FastList.newListWith[String]("1", "1", "2"),
                FastList.newList[Pair[Int, String]])
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.zip(FastList.newListWith("1", "1", "2"), FastList.newList[Pair[Int, String]])
        }
    }

    @Test
    def zipByIndex_withTarget_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.zipWithIndex(FastList.newList[Pair[Int, java.lang.Integer]]())
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.zipWithIndex(FastList.newList[Pair[Int, java.lang.Integer]])
        }
    }

    @Test
    def forEach_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.forEach({
                _: Int => ()
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.forEach({
                _: Int => ()
            })
        }
    }

    @Test
    def forEachWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.forEachWith({
                (_: Int, _: Int) => ()
            }, 0)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.forEachWith({
                (_: Int, _: Int) => ()
            }, 0)
        }
    }

    @Test
    def contains_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.contains(1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.contains(1)
        }
    }

    @Test
    def containsAll_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.containsAll(FastList.newListWith(1, 2))
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.containsAll(FastList.newListWith(1, 2))
        }
    }

    @Test
    def containsAllIterable_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.containsAll(FastList.newListWith(1, 2))
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.containsAll(FastList.newListWith(1, 2))
        }
    }

    @Test
    def containsAllArguments_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.containsAllArguments("1", "2")
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.containsAllArguments("1", "2")
        }
    }

    @Test
    def noneSatisfy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.noneSatisfy({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.noneSatisfy({
                _: Int => true
            })
        }
    }

    @Test
    def noneSatisfyWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.noneSatisfyWith({
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.noneSatisfyWith({
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def allSatisfy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.allSatisfy({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.allSatisfy({
                _: Int => true
            })
        }
    }

    @Test
    def allSatisfyWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.allSatisfyWith({
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.allSatisfyWith({
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def anySatisfy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.anySatisfy({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.anySatisfy({
                _: Int => true
            })
        }
    }

    @Test
    def anySatisfyWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.anySatisfyWith({
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.anySatisfyWith({
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def toList_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toList
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toList
        }
    }

    @Test
    def toMap_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toMap(
            {
                num: Int => num
            },
            {
                _: Int => 0
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toMap(
            {
                num: Int => num
            },
            {
                _: Int => 0
            })
        }
    }

    @Test
    def toSortedMap_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedMap(
            {
                num: Int => num
            },
            {
                _: Int => 0
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedMap(
            {
                num: Int => num
            },
            {
                _: Int => 0
            })
        }
    }

    @Test
    def toSortedMap_withComparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedMap[Int, Int](
            {
                (o1: Int, o2: Int) => o1.compareTo(o2)
            },
            {
                num: Int => num
            },
            {
                _: Int => 0
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedMap[Int, Int](
            {
                (o1: Int, o2: Int) => o1.compareTo(o2)
            },
            {
                num: Int => num
            },
            {
                _: Int => 0
            })
        }
    }

    @Test
    def toSet_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSet
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSet
        }
    }

    @Test
    def toBag_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toBag
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toBag
        }
    }

    @Test
    def asLazy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.asLazy
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.asLazy
        }
    }

    @Test
    def toSortedList_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedList
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedList
        }
    }

    @Test
    def toSortedList_withComparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedList({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedList({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
    }

    @Test
    def toSortedListBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedListBy[String]({
                (num: Int) => "" + num
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedListBy[String]({
                (num: Int) => "" + num
            })
        }
    }

    @Test
    def toSortedSet_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedSet
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedSet
        }
    }

    @Test
    def toSortedSet_withComparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedSet({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedSet({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
    }

    @Test
    def toSortedSetBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toSortedSetBy[String]({
                (num: Int) => "" + num
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toSortedSetBy[String]({
                (num: Int) => "" + num
            })
        }
    }

    @Test
    def count_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.count({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.count({
                _: Int => true
            })
        }
    }

    @Test
    def countWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.countWith({
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.countWith({
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def detect_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.detect({
                _: Int => true
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.detect({
                _: Int => true
            })
        }
    }

    @Test
    def detectIfNone_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.detectIfNone(
            {
                _: Int => true
            },
            {
                () => 1
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.detectIfNone(
            {
                _: Int => true
            },
            {
                () => 1
            })
        }
    }

    @Test
    def detectWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.detectWith({
                (_: Int, _: Int) => true
            }, 1)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.detectWith({
                (_: Int, _: Int) => true
            }, 1)
        }
    }

    @Test
    def detectWithIfNone_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.detectWithIfNone(
            {
                (_: Int, _: Int) => true
            },
            1,
            {
                () => 1
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.detectWithIfNone(
            {
                (_: Int, _: Int) => true
            },
            1,
            {
                () => 1
            })
        }
    }

    @Test
    def min_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.min
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.min
        }
    }

    @Test
    def max_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.max
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.max
        }
    }

    @Test
    def min_withComparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.min({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.min({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
    }

    @Test
    def max_withComparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.max({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.max({
                (o1: Int, o2: Int) => o1.compareTo(o2)
            })
        }
    }

    @Test
    def minBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.minBy[String]({
                (num: Int) => "" + num
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.minBy[String]({
                (num: Int) => "" + num
            })
        }
    }

    @Test
    def maxBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.maxBy[String]({
                (num: Int) => "" + num
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.maxBy[String]({
                (num: Int) => "" + num
            })
        }
    }

    @Test
    def add_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.add(4)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.add(4)
        }
    }

    @Test
    def addAll_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.addAll(FastList.newListWith(3, 4))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.addAll(FastList.newListWith(3, 4))
        }
    }

    @Test
    def addAllIterable_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.addAllIterable(FastList.newListWith(3, 4))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.addAllIterable(FastList.newListWith(3, 4))
        }
    }

    @Test
    def remove_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.remove(1)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.remove(1)
        }
    }

    @Test
    def removeAll_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.removeAll(FastList.newListWith(1, 2))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.removeAll(FastList.newListWith(1, 2))
        }
    }

    @Test
    def removeAllIterable_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.removeAllIterable(FastList.newListWith(1, 2))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.removeAllIterable(FastList.newListWith(1, 2))
        }
    }

    @Test
    def retainAll_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.retainAll(FastList.newListWith(1, 2))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.retainAll(FastList.newListWith(1, 2))
        }
    }

    @Test
    def retainAllIterable_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.retainAllIterable(FastList.newListWith(1, 2))
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.retainAllIterable(FastList.newListWith(1, 2))
        }
    }

    @Test
    def removeIf_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.removeIf({
                _: Int => true
            })
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.removeIf({
                _: Int => true
            })
        }
    }

    @Test
    def removeIfWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.removeIfWith({
                (_: Int, _: Int) => true
            }, 0)
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.removeIfWith({
                (_: Int, _: Int) => true
            }, 0)
        }
    }

    @Test
    def injectInto_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.injectInto[Int](0, (inject: Int, value: Int) => value + inject)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.injectInto[Int](0, (inject: Int, value: Int) => value + inject)
        }
    }

    @Test
    def injectIntoWith_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.injectIntoWith[Int, Int](0, (_: Int, value: Int, _: Int) => value, 0)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.injectIntoWith[Int, Int](0, (injected: Int, value: Int, _: Int) => value + injected, 0)
        }
    }

    @Test
    def getFirst_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.getFirst
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.getFirst
        }
    }

    @Test
    def getLast_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.getLast
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.getLast
        }
    }

    @Test
    def notEmpty_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.notEmpty
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.notEmpty
        }
    }

    @Test
    def isEmpty_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.isEmpty
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.isEmpty
        }
    }

    @Test
    def clear_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.clear
        }
        this.assertReadersBlocked
        {
            this.classUnderTest.clear
        }
    }

    @Test
    def size_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.size
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.size
        }
    }

    @Test
    def toArray_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toArray
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toArray
        }
    }

    @Test
    def toArrayWithTarget_safe
    {
        this.assertWritersBlocked
        {
            val array: Array[java.lang.Integer] = new Array[java.lang.Integer](10)
            this.classUnderTest.toArray(array)
        }
        this.assertReadersNotBlocked
        {
            val array: Array[java.lang.Integer] = new Array[java.lang.Integer](10)
            this.classUnderTest.toArray(array)
        }
    }

    @Test
    def toString_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.toString
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.toString
        }
    }

    @Test
    def makeString_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.makeString
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.makeString
        }
    }

    @Test
    def makeString_withSeparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.makeString(", ")
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.makeString(", ")
        }
    }

    @Test
    def makeString_withStartEndSeparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.makeString("[", "]", ", ")
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.makeString("[", "]", ", ")
        }
    }

    @Test
    def appendString_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.appendString(new StringBuilder)
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.appendString(new StringBuilder)
        }
    }

    @Test
    def appendString_withSeparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.appendString(new StringBuilder, ", ")
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.appendString(new StringBuilder, ", ")
        }
    }

    @Test
    def appendString_withStartEndSeparator_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.appendString(new StringBuilder, "[", "]", ", ")
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.appendString(new StringBuilder, "[", "]", ", ")
        }
    }

    @Test
    def sumOfInt_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.sumOfInt({
                _: Int => 0
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.sumOfInt({
                _: Int => 0
            })
        }
    }

    @Test
    def sumOfDouble_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.sumOfDouble({
                _: Int => 0.0
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.sumOfDouble({
                _: Int => 0.0
            })
        }
    }

    @Test
    def aggregateBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.aggregateBy[String, Int]({
                num: Int => num.toString
            },
            {
                () => 0
            },
            {
                (_: Int, _: Int) => 0
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.aggregateBy[String, Int]({
                num: Int => num.toString
            },
            {
                () => 0
            },
            {
                (_: Int, _: Int) => 0
            })
        }
    }

    @Test
    def aggregateInPlaceBy_safe
    {
        this.assertWritersBlocked
        {
            this.classUnderTest.aggregateInPlaceBy[String, Int]({
                num: Int => num.toString
            },
            {
                () => 0
            },
            {
                (_: Int, _: Int) => ()
            })
        }
        this.assertReadersNotBlocked
        {
            this.classUnderTest.aggregateInPlaceBy[String, Int]({
                num: Int => num.toString
            },
            {
                () => 0
            },
            {
                (_: Int, _: Int) => ()
            })
        }
    }
}
