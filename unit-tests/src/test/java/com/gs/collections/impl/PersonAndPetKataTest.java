/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.IntBag;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.primitive.IntSet;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.primitive.PrimitiveTuples;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersonAndPetKataTest
{
    private MutableList<Person> people;

    @Before
    public void setUp() throws Exception
    {
        this.people = FastList.newListWith(
                new Person("Mary", "Smith").addPet(PetType.CAT, "Tabby", 2),
                new Person("Bob", "Smith").addPet(PetType.CAT, "Dolly", 3).addPet(PetType.DOG, "Spot", 2),
                new Person("Ted", "Smith").addPet(PetType.DOG, "Spike", 4),
                new Person("Jake", "Snake").addPet(PetType.SNAKE, "Serpy", 1),
                new Person("Barry", "Bird").addPet(PetType.BIRD, "Tweety", 2),
                new Person("Terry", "Turtle").addPet(PetType.TURTLE, "Speedy", 1),
                new Person("Harry", "Hamster").addPet(PetType.HAMSTER, "Fuzzy", 1).addPet(PetType.HAMSTER, "Wuzzy", 1)
        );
    }

    @Test
    public void doAnyPeopleHaveCats()
    {
        boolean result =
                this.people.anySatisfy(person -> person.hasPet(PetType.CAT));
        Assert.assertTrue(result);

        boolean result1 =
                this.people.anySatisfyWith(Person::hasPet, PetType.CAT);
        Assert.assertTrue(result1);
    }

    @Test
    public void doAnyPeopleHaveCatsUsingStreams()
    {
        boolean result =
                this.people.stream().anyMatch(person -> person.hasPet(PetType.CAT));
        Assert.assertTrue(result);
    }

    @Test
    public void doAllPeopleHaveCats()
    {
        boolean result =
                this.people.allSatisfy(person -> person.hasPet(PetType.CAT));
        Assert.assertFalse(result);

        boolean result1 =
                this.people.allSatisfyWith(Person::hasPet, PetType.CAT);
        Assert.assertFalse(result1);
    }

    @Test
    public void doAllPeopleHaveCatsUsingStreams()
    {
        boolean result =
                this.people.stream().allMatch(person -> person.hasPet(PetType.CAT));
        Assert.assertFalse(result);
    }

    @Test
    public void doNoPeopleHaveCats()
    {
        boolean result =
                this.people.noneSatisfy(person -> person.hasPet(PetType.CAT));
        Assert.assertFalse(result);

        boolean result1 =
                this.people.noneSatisfyWith(Person::hasPet, PetType.CAT);
        Assert.assertFalse(result1);
    }

    @Test
    public void doNoPeopleHaveCatsUsingStreams()
    {
        boolean result =
                this.people.stream().noneMatch(person -> person.hasPet(PetType.CAT));
        Assert.assertFalse(result);
    }

    @Test
    public void howManyPeopleHaveCats()
    {
        int count =
                this.people.count(person -> person.hasPet(PetType.CAT));
        Assert.assertEquals(2, count);

        int count1 =
                this.people.countWith(Person::hasPet, PetType.CAT);
        Assert.assertEquals(2, count1);
    }

    @Test
    public void howManyPeopleHaveCatsUsingStreams()
    {
        long count =
                this.people.stream().filter(person -> person.hasPet(PetType.CAT)).count();
        Assert.assertEquals(2, count);
    }

    @Test
    public void getPeopleWithCats()
    {
        MutableList<Person> peopleWithCats =
                this.people.select(person -> person.hasPet(PetType.CAT));
        Verify.assertSize(2, peopleWithCats);

        MutableList<Person> peopleWithCats1 =
                this.people.selectWith(Person::hasPet, PetType.CAT);
        Verify.assertSize(2, peopleWithCats1);
    }

    @Test
    public void getPeopleWithCatsUsingStreams()
    {
        List<Person> peopleWithCats =
                this.people.stream().filter(person -> person.hasPet(PetType.CAT)).collect(Collectors.toList());
        Verify.assertSize(2, peopleWithCats);
    }

    @Test
    public void getPeopleWhoDontHaveCats()
    {
        MutableList<Person> peopleWithNoCats =
                this.people.reject(person -> person.hasPet(PetType.CAT));
        Verify.assertSize(5, peopleWithNoCats);

        MutableList<Person> peopleWithNoCats1 =
                this.people.rejectWith(Person::hasPet, PetType.CAT);
        Verify.assertSize(5, peopleWithNoCats1);
    }

    @Test
    public void getPeopleWhoDontHaveCatsUsingStreams()
    {
        List<Person> peopleWithNoCats =
                this.people.stream()
                        .filter(person -> !person.hasPet(PetType.CAT))
                        .collect(Collectors.toList());
        Verify.assertSize(5, peopleWithNoCats);
    }

    @Test
    public void partitionPeopleByCatOwnersAndNonCatOwners()
    {
        PartitionMutableList<Person> catsAndNoCats =
                this.people.partition(person -> person.hasPet(PetType.CAT));
        Verify.assertSize(2, catsAndNoCats.getSelected());
        Verify.assertSize(5, catsAndNoCats.getRejected());

        PartitionMutableList<Person> catsAndNoCats1 =
                this.people.partitionWith(Person::hasPet, PetType.CAT);
        Verify.assertSize(2, catsAndNoCats1.getSelected());
        Verify.assertSize(5, catsAndNoCats1.getRejected());
    }

    @Test
    public void partitionPeopleByCatOwnersAndNonCatOwnersUsingStreams()
    {
        Map<Boolean, List<Person>> catsAndNoCats =
                this.people.stream().collect(Collectors.partitioningBy(person -> person.hasPet(PetType.CAT)));
        Verify.assertSize(2, catsAndNoCats.get(true));
        Verify.assertSize(5, catsAndNoCats.get(false));
    }

    @Test
    public void findPersonNamedMarySmith()
    {
        Person result =
                this.people.detect(person -> person.named("Mary Smith"));
        Assert.assertEquals("Mary", result.getFirstName());
        Assert.assertEquals("Smith", result.getLastName());

        Person result1 =
                this.people.detectWith(Person::named, "Mary Smith");
        Assert.assertEquals("Mary", result1.getFirstName());
        Assert.assertEquals("Smith", result1.getLastName());
    }

    @Test
    public void findPersonNamedMarySmithUsingStreams()
    {
        Person result =
                this.people.stream().filter(person -> person.named("Mary Smith")).findFirst().get();
        Assert.assertEquals("Mary", result.getFirstName());
        Assert.assertEquals("Smith", result.getLastName());
    }

    @Test
    public void getTheNamesOfBobSmithPets()
    {
        Person person =
                this.people.detectWith(Person::named, "Bob Smith");
        MutableList<String> names =
                person.getPets().collect(Pet::getName);
        Assert.assertEquals("Dolly, Spot", names.makeString());
    }

    @Test
    public void getTheNamesOfBobSmithPetsUsingStreams()
    {
        Person person =
                this.people.stream().filter(each -> each.named("Bob Smith")).findFirst().get();
        List<String> names =
                person.getPets().stream().map(Pet::getName).collect(Collectors.toList());
        Assert.assertEquals("Dolly, Spot", names.stream().collect(Collectors.joining(", ")));
    }

    @Test
    public void getAllPets()
    {
        Assert.assertEquals(
                UnifiedSet.newSetWith(PetType.values()),
                this.people.flatCollect(Person::getPetTypes).toSet()
        );
    }

    @Test
    public void getAllPetsUsingStreams()
    {
        Assert.assertEquals(
                new HashSet<>(Arrays.asList(PetType.values())),
                this.people.stream().flatMap(person -> person.getPetTypes().stream()).collect(Collectors.toSet())
        );
    }

    @Test
    public void groupPeopleByLastName()
    {
        Multimap<String, Person> byLastName =
                this.people.groupBy(Person::getLastName);
        Verify.assertIterableSize(3, byLastName.get("Smith"));
    }

    @Test
    public void groupPeopleByLastNameUsingStreams()
    {
        Map<String, List<Person>> byLastName =
                this.people.stream().collect(Collectors.groupingBy(Person::getLastName));
        Verify.assertIterableSize(3, byLastName.get("Smith"));
    }

    @Test
    public void groupPeopleByTheirPets()
    {
        Multimap<PetType, Person> peopleByPets =
                this.people.groupByEach(Person::getPetTypes);
        RichIterable<Person> catPeople = peopleByPets.get(PetType.CAT);
        Assert.assertEquals(
                "Mary, Bob",
                catPeople.collect(Person::getFirstName).makeString()
        );
        RichIterable<Person> dogPeople = peopleByPets.get(PetType.DOG);
        Assert.assertEquals(
                "Bob, Ted",
                dogPeople.collect(Person::getFirstName).makeString()
        );
    }

    @Test
    public void groupPeopleByTheirPetsUsingStreams()
    {
        Map<PetType, List<Person>> peopleByPets = new HashMap<>();
        this.people.stream().forEach(
                person -> person.getPetTypes().stream().forEach(
                        petType -> peopleByPets.computeIfAbsent(petType, e -> new ArrayList<>()).add(person)));
        List<Person> catPeople = peopleByPets.get(PetType.CAT);
        Assert.assertEquals(
                "Mary, Bob",
                catPeople.stream().map(Person::getFirstName).collect(Collectors.joining(", "))
        );
        List<Person> dogPeople = peopleByPets.get(PetType.DOG);
        Assert.assertEquals(
                "Bob, Ted",
                dogPeople.stream().map(Person::getFirstName).collect(Collectors.joining(", "))
        );
    }

    @Test
    public void getTotalNumberOfPets()
    {
        long numberOfPets = this.people.sumOfInt(Person::getNumberOfPets);
        Assert.assertEquals(9, numberOfPets);
    }

    @Test
    public void getTotalNumberOfPetsUsingStreams()
    {
        int numberOfPets = this.people.stream().mapToInt(Person::getNumberOfPets).sum();
        Assert.assertEquals(9, numberOfPets);
    }

    @Test
    public void getAgesOfPets()
    {
        IntList sortedAges = this.people.asLazy()
                .flatCollect(Person::getPets)
                .collectInt(Pet::getAge)
                .toSortedList();
        IntSet uniqueAges = sortedAges.toSet();
        IntSummaryStatistics stats = new IntSummaryStatistics();
        sortedAges.forEach(stats::accept);
        Assert.assertTrue(sortedAges.allSatisfy(IntPredicates.greaterThan(0)));
        Assert.assertTrue(sortedAges.allSatisfy(i -> i > 0));
        Assert.assertFalse(sortedAges.anySatisfy(i -> i == 0));
        Assert.assertTrue(sortedAges.noneSatisfy(i -> i < 0));
        Assert.assertEquals(IntHashSet.newSetWith(1, 2, 3, 4), uniqueAges);
        Assert.assertEquals(2.0d, sortedAges.median(), 0.0);
        Assert.assertEquals(stats.getMin(), sortedAges.min());
        Assert.assertEquals(stats.getMax(), sortedAges.max());
        Assert.assertEquals(stats.getSum(), sortedAges.sum());
        Assert.assertEquals(stats.getAverage(), sortedAges.average(), 0.0);
        Assert.assertEquals(stats.getCount(), sortedAges.size());
    }

    @Test
    public void getAgesOfPetsUsingStreams()
    {
        List<Integer> sortedAges = this.people.stream()
                .flatMap(person -> person.getPets().stream())
                .map(Pet::getAge)
                .sorted()
                .collect(Collectors.toList());
        Set<Integer> uniqueAges = new HashSet<>(sortedAges);
        IntSummaryStatistics stats = sortedAges.stream().collect(Collectors.summarizingInt(i -> i));
        Assert.assertTrue(sortedAges.stream().allMatch(i -> i > 0));
        Assert.assertFalse(sortedAges.stream().anyMatch(i -> i == 0));
        Assert.assertTrue(sortedAges.stream().noneMatch(i -> i < 0));
        Assert.assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4)), uniqueAges);
        Assert.assertEquals(stats.getMin(), sortedAges.stream().mapToInt(i -> i).min().getAsInt());
        Assert.assertEquals(stats.getMax(), sortedAges.stream().mapToInt(i -> i).max().getAsInt());
        Assert.assertEquals(stats.getSum(), sortedAges.stream().mapToInt(i -> i).sum());
        Assert.assertEquals(stats.getAverage(), sortedAges.stream().mapToInt(i -> i).average().getAsDouble(), 0.0);
        Assert.assertEquals(stats.getCount(), sortedAges.size());
    }

    @Test
    public void getCountsByPetType()
    {
        Bag<PetType> counts =
                this.people.asLazy()
                        .flatCollect(Person::getPets)
                        .collect(Pet::getType)
                        .toBag();
        Assert.assertEquals(2, counts.occurrencesOf(PetType.CAT));
        Assert.assertEquals(2, counts.occurrencesOf(PetType.DOG));
        Assert.assertEquals(2, counts.occurrencesOf(PetType.HAMSTER));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.SNAKE));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.TURTLE));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.BIRD));
    }

    @Test
    public void getCountsByPetTypeUsingStreams()
    {
        Map<PetType, Long> counts =
                this.people.stream()
                        .flatMap(person -> person.getPets().stream())
                        .collect(Collectors.groupingBy(Pet::getType, Collectors.counting()));
        Assert.assertEquals(Long.valueOf(2L), counts.get(PetType.CAT));
        Assert.assertEquals(Long.valueOf(2L), counts.get(PetType.DOG));
        Assert.assertEquals(Long.valueOf(2L), counts.get(PetType.HAMSTER));
        Assert.assertEquals(Long.valueOf(1L), counts.get(PetType.SNAKE));
        Assert.assertEquals(Long.valueOf(1L), counts.get(PetType.TURTLE));
        Assert.assertEquals(Long.valueOf(1L), counts.get(PetType.BIRD));
    }

    @Test
    public void getTop3Pets()
    {
        MutableList<ObjectIntPair<PetType>> favorites =
                this.people.asLazy()
                        .flatCollect(Person::getPets)
                        .collect(Pet::getType)
                        .toBag()
                        .topOccurrences(3);
        Verify.assertSize(3, favorites);
        Verify.assertContains(PrimitiveTuples.pair(PetType.CAT, 2), favorites);
        Verify.assertContains(PrimitiveTuples.pair(PetType.DOG, 2), favorites);
        Verify.assertContains(PrimitiveTuples.pair(PetType.HAMSTER, 2), favorites);
    }

    @Test
    public void getTop3PetsUsingStreams()
    {
        List<Map.Entry<PetType, Long>> favorites =
                this.people.stream()
                        .flatMap(p -> p.getPets().stream())
                        .collect(Collectors.groupingBy(Pet::getType, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .sorted(Comparator.comparingLong(e -> -e.getValue()))
                        .limit(3)
                        .collect(Collectors.toList());
        Verify.assertSize(3, favorites);
        Verify.assertContains(new AbstractMap.SimpleEntry<>(PetType.CAT, Long.valueOf(2)), favorites);
        Verify.assertContains(new AbstractMap.SimpleEntry<>(PetType.DOG, Long.valueOf(2)), favorites);
        Verify.assertContains(new AbstractMap.SimpleEntry<>(PetType.HAMSTER, Long.valueOf(2)), favorites);
    }

    @Test
    public void getBottom3Pets()
    {
        MutableList<ObjectIntPair<PetType>> leastFavorites =
                this.people
                        .asLazy()
                        .flatCollect(Person::getPets)
                        .collect(Pet::getType)
                        .toBag()
                        .bottomOccurrences(3);
        Verify.assertSize(3, leastFavorites);
        Verify.assertContains(PrimitiveTuples.pair(PetType.SNAKE, 1), leastFavorites);
        Verify.assertContains(PrimitiveTuples.pair(PetType.TURTLE, 1), leastFavorites);
        Verify.assertContains(PrimitiveTuples.pair(PetType.BIRD, 1), leastFavorites);
    }

    @Test
    public void getCountsByPetAge()
    {
        IntBag counts =
                this.people.asLazy()
                        .flatCollect(Person::getPets)
                        .collectInt(Pet::getAge)
                        .toBag();
        Assert.assertEquals(4, counts.occurrencesOf(1));
        Assert.assertEquals(3, counts.occurrencesOf(2));
        Assert.assertEquals(1, counts.occurrencesOf(3));
        Assert.assertEquals(1, counts.occurrencesOf(4));
        Assert.assertEquals(0, counts.occurrencesOf(5));
    }

    @Test
    public void getCountsByPetAgeUsingStreams()
    {
        Map<Integer, Long> counts =
                this.people.stream()
                        .flatMap(person -> person.getPets().stream())
                        .collect(Collectors.groupingBy(Pet::getAge, Collectors.counting()));
        Assert.assertEquals(Long.valueOf(4), counts.get(1));
        Assert.assertEquals(Long.valueOf(3), counts.get(2));
        Assert.assertEquals(Long.valueOf(1), counts.get(3));
        Assert.assertEquals(Long.valueOf(1), counts.get(4));
        Assert.assertNull(counts.get(5));
    }

    public static final class Person
    {
        private final String firstName;
        private final String lastName;
        private final MutableList<Pet> pets = FastList.newList();

        private Person(String firstName, String lastName)
        {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName()
        {
            return this.firstName;
        }

        public String getLastName()
        {
            return this.lastName;
        }

        public boolean named(String name)
        {
            return name.equals(this.firstName + ' ' + this.lastName);
        }

        public boolean hasPet(PetType petType)
        {
            return this.pets.anySatisfyWith(Predicates2.attributeEqual(Pet::getType), petType);
        }

        public MutableList<Pet> getPets()
        {
            return this.pets;
        }

        public MutableBag<PetType> getPetTypes()
        {
            return this.pets.collect(Pet::getType, HashBag.newBag());
        }

        public Person addPet(PetType petType, String name, int age)
        {
            this.pets.add(new Pet(petType, name, age));
            return this;
        }

        public int getNumberOfPets()
        {
            return this.pets.size();
        }
    }

    public static class Pet
    {
        private final PetType type;
        private final String name;
        private final int age;

        public Pet(PetType type, String name, int age)
        {
            this.type = type;
            this.name = name;
            this.age = age;
        }

        public PetType getType()
        {
            return this.type;
        }

        public String getName()
        {
            return this.name;
        }

        public int getAge()
        {
            return this.age;
        }
    }

    public enum PetType
    {
        CAT, DOG, HAMSTER, TURTLE, BIRD, SNAKE
    }
}
