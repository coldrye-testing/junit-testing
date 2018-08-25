# enum-test-base

A simple base class for all your enum testing needs.

The default behaviour of the base class is to make sure that you have 100% test coverage on all of your standard enums.

More complex usage scenarios where you have additional behaviour and properties in your enums are also be supported,
including nested test classes.


## Usage


### Maven

```xml
<dependency>
  <groupId>eu.coldrye.junit</groupId>
  <artifactId>enum-test-base</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```


### Examples

```java
package org.example;

import eu.coldrye.junit.bases.AbstractEnumTestBase;

public class SampleEnumTest extends AbstractEnumTestBase<SampleEnum> {

  // done, 100% coverage
}
```


## References

- [GitHub](https://github.com/coldrye-java/junit-testing/tree/master/enum-test-base)
- [Sonar](http://sonar.coldrye.eu/dashboard?id=eu.coldrye.junit%3Aenum-test-base)
