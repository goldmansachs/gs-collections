# GS Collections is now Eclipse Collections
We are pleased to announce that GS Collections has been migrated to [the Eclipse Foundation](https://eclipse.org/org/foundation/), re-branded as [Eclipse Collections](https://www.eclipse.org/collections/).

Eclipse Collections 7.0 has the exact same feature set as GS Collections 7.0.

Going forward, any new features will be developed in Eclipse Collections. We will only port critical bug fixes to GS Collections.
To support smooth migration for users, we implemented a conversion application [gsc-ec-converter](https://github.com/eclipse/gsc-ec-converter). Please plan your migration accordingly from GS Collections to Eclipse Collections.

# GS Collections

[![][travis img]][travis]
[![][maven img]][maven]
[![][release img]][release]
[![][license img]][license]

GS Collections is a collections framework for Java. It has JDK-compatible List, Set and Map implementations with a rich API and set of utility classes that work with any JDK compatible Collections, Arrays, Maps or Strings. The iteration protocol was inspired by the Smalltalk collection framework.
The library modules in GS Collections are compatible with Java 5 (gs-collections-api, gs-collections, and gs-collections-testutils).

## Quick Example
GS Collections puts iteration methods on the container types. Lambdas are simulated using anonymous inner classes. Here's a code example that demonstrates the usual style of programming with GS Collections.

```java
MutableList<Person> people = FastList.newListWith(person1, person2, person3);
MutableList<String> sortedLastNames = people.collect(Person.TO_LAST_NAME).sortThis();
System.out.println("Comma separated, sorted last names: " + sortedLastNames.makeString());
```

Person.TO_LAST_NAME is defined as a constant Function in the Person class.

```java
public static final Function<Person, String> TO_LAST_NAME = new Function<Person, String>()
{
    public String valueOf(Person person)
    {
        return person.lastName;
    }
};

```
In Java 8, the Function can be replaced with a lambda:

```java
MutableList<String> sortedLastNames = people.collect(person -> person.getLastName()).sortThis();
```

Or, a method reference:

```java
MutableList<String> sortedLastNames = people.collect(Person::getLastName).sortThis();
```

## Why GS Collections?
* Improves readability and reduces duplication of iteration code (enforces DRY/OAOO)
* Implements several, high-level iteration patterns (select, reject, collect, inject into, etc.) on "humane" container interfaces which are extensions of the JDK interfaces
* Provides a consistent mechanism for iterating over Collections, Arrays, Maps, and Strings
* Provides replacements for ArrayList, HashSet, and HashMap optimized for performance and memory usage
* Performs more "behind-the-scene" optimizations in utility classes
* Encapsulates a lot of the structural complexity of parallel iteration and lazy evaluation
* Adds new containers including Bag, Interval, Multimap, BiMap, and immutable versions of all types
* Has been under active development since 2005 and is a mature library

## Documentation
The best way to learn about GS Collections is to dive into the [code kata](https://github.com/goldmansachs/gs-collections-kata). The kata is a fun way to learn idiomatic GS Collections usage and hone your skills through practice.
Please refer to the [wiki](https://github.com/goldmansachs/gs-collections/wiki) for more details.

For more comprehensive documentation, take a look at the [Reference Guide](http://www.goldmansachs.com/gs-collections/documents/GS%20Collections%20Reference%20Guide%205.0.0.pdf).

## Contributions
We currently do all development in an internal Subversion repository and are not prepared to take external contributions. However, we watch the [issue tracker](https://github.com/goldmansachs/gs-collections/issues) for bug reports and feature requests.

## FAQ
### Why is Goldman Sachs open-sourcing GS Collections?

* We believe that GS Collections offers a significant advantage over existing solutions. We hope others will benefit from it.
* We believe in the power of the technical community to help improve GS Collections.
* Technology is a huge part of what we do at Goldman Sachs. GS Collections exemplifies our commitment to technology.
* We use open source software in many of our operations. We have benefited from the work of others and we'd like to give something back.

### Does Goldman Sachs use GS Collections?
Yes, we use GS Collections in many of our internal applications.

## Acquiring GS Collections

### Download
* [Version 7.0.0](http://www.goldmansachs.com/gs-collections/binaries/gs-collections-7.0.0.zip)
* [Older versions](https://github.com/goldmansachs/gs-collections/releases)

### Maven
```xml
<dependency>
  <groupId>com.goldmansachs</groupId>
  <artifactId>gs-collections-api</artifactId>
  <version>7.0.0</version>
</dependency>

<dependency>
  <groupId>com.goldmansachs</groupId>
  <artifactId>gs-collections</artifactId>
  <version>7.0.0</version>
</dependency>

<dependency>
  <groupId>com.goldmansachs</groupId>
  <artifactId>gs-collections-testutils</artifactId>
  <version>7.0.0</version>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>com.goldmansachs</groupId>
  <artifactId>gs-collections-forkjoin</artifactId>
  <version>7.0.0</version>
</dependency>
```

### Gradle

```groovy
compile 'com.goldmansachs:gs-collections-api:7.0.0'
compile 'com.goldmansachs:gs-collections:7.0.0'
testCompile 'com.goldmansachs:gs-collections-testutils:7.0.0'
compile 'com.goldmansachs:gs-collections-forkjoin:7.0.0'
```

### Ivy

```xml
<dependency org="com.goldmansachs" name="gs-collections-api" rev="7.0.0" />
<dependency org="com.goldmansachs" name="gs-collections" rev="7.0.0" />
<dependency org="com.goldmansachs" name="gs-collections-testutils" rev="7.0.0" />
<dependency org="com.goldmansachs" name="gs-collections-forkjoin" rev="7.0.0"/>
```

[travis]:https://travis-ci.org/goldmansachs/gs-collections
[travis img]:https://travis-ci.org/goldmansachs/gs-collections.svg?branch=master

[maven]:http://search.maven.org/#search|gav|1|g:"com.goldmansachs"%20AND%20a:"gs-collections"
[maven img]:https://maven-badges.herokuapp.com/maven-central/com.goldmansachs/gs-collections/badge.svg

[release]:https://github.com/goldmansachs/gs-collections/releases
[release img]:https://img.shields.io/github/release/goldmansachs/gs-collections.svg

[license]:LICENSE-2.0.txt
[license img]:https://img.shields.io/badge/License-Apache%202-blue.svg
