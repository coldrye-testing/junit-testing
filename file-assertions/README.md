# file-assertions

A simple assertion framework for files and file content.

The assertions can be used with all testing frameworks and are not limited to just JUnit5, although they make use of 
some of the available infrastructure provided by the JUnit5 platform.


## Features

- Comparing files by their content gives you extended reports from which you can derive new fixtures from.


## Non Goals


### File Content Level Assertions (FileAssertions.assertEquals(...))

- not to be used with very large files unless you have plenty of both time and memory in order to handle the load,
  so keep it crisp and simple
- does not work with binary files, if you need a binary diff, you will have to use different tools
- there will never be any content level ``assertNotEquals`` assertions, use ``assertEquals`` with the proper fixtures
  for that purpose


## Usage


### Maven

```xml
<dependency>
  <groupId>eu.coldrye.junit</groupId>
  <artifactId>file-assertions</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <scope>test</scope>
</dependency>
```


### Examples

```java
package org.example;

import eu.coldrye.junit.assertions.file.FileAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

public class SampleTest {

  @BeforeAll
  public static void setUp() throws IOException {
  
    // clean up all reports for this class
    FileAssertions.cleanUp(SampleTest.class);
  }
  
  @Test
  public void sampleExists() {

    FileAssertions.assertExists('/tmp', "/tmp folder missing");
  }
  
  @Test
  public void sampleEquals() {
    
    FileAssertions.assertEquals('/tmp/foo.txt', "/tmp/bar.txt");
  }
}
```

Running the ``sampleEquals`` test, provided that both the expected and the actual file exist and that their content
differs, will give you the following result under the default output path, which is ``${project.output.directory}/assertions/file/reports``:

```
/org/example/SampleTest/sampleEquals/1/
  expected.txt
  expected.txt.new
  expected.txt.patch
  actual.txt
```

Now, you can compare ``actual.txt`` to ``expected.txt`` and if ``actual.txt`` is the expected result, update your 
existing test fixture by simply copying over ``expected.txt.new``.

For a more exhaustive example see the [available test suite](https://github.com/coldrye-java/junit-testing/tree/master/file-assertions).


## Similar Projects

- [junit-addons](http://junit-addons.sourceforge.net/)

  JUnit4 only. No diff support.


## References

- [GitHub](https://github.com/coldrye-java/junit-testing/tree/master/file-assertions)
- [Sonar](http://sonar.coldrye.eu/dashboard?id=eu.coldrye.junit%3Afile-assertions)
