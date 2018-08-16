# junit5-env-extension

The extension provides you with an infrastructure for setting up and tearing down long
running environments during your integration tests, something that you would not want
to do before each or after each test, or, before all or after all tests in a test class.

For example starting and stopping an embedded Hadoop mini cluster or an embedded Kafka.
Another example would be to pull and start a given docker image before that the first
test is executed and tear it down when all tests have been executed.

## Usage

### Maven

Include the following dependency in your pom and you are good to go

```
<dependency>
  <groupId>eu.coldrye.junit</groupId>
  <artifactId>junit-env-extension</artifactId>
  <version>1.0.3</version>
</dependency>
```

### Junit

In your test classes, you just have to annotate the test class with 
``@ExtendWith(EnvExtension.class)`` and then provide the environments
that you need for your tests, e.g.

```
package eu.coldrye.junit.env.examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.Environment;

@ExtendWith(EnvExtension.class)
@Environment(ExampleEnvProvider.class)
public class SampleTest {

  @Test
  public void clientMustRespondWithHelloWorld(@ExampleEnvProvided ExampleClient client) {
     Response response = client.helloWorld();
     Assertions.assertEquals("hello world", response.data());
  }
}
```

The ``ExampleEnvProvider`` would then for example pull a docker image from your registry
and start the container, waiting for it and the various services it provides to become
available.

The annotated parameter on the test method ``clientMustRespondWithHelloWorld()`` will be 
resolved by the ``ExampleEnvProvider`` and the test will be provided with an instance of
the requested client.

See the provided [examples](https://github.com/coldrye-java/junit-testing/tree/master/junit-env-extension/src/examples) 
for more information.

## Phases

### ``EnvPhase.INIT``

This phase is entered at the beginning of the Junit ``BEFORE_ALL`` phase. It is entered once
before that the preparation of test instances and, subsequently, the execution of the first test
can take place.

During this phase, all associated ``EnvProvider``S will be instructed to set up their environments.

This is the point where ``EnvProvider``S can initialize a complex environment, e.g. a docker
container, and wait for it to become online.

### ``EnvPhase.DEINIT``

The phase is entered as soon as the last test in a given ``TestPlan`` was executed and shortly
before that the test runner will terminate.

During this phase, all associated ``EnvProvider``S will be instructed to tear down their environments.

This is the point where ``EnvProvider``S can tear down a complex environment, e.g. a docker
container, and wait for it to become offline, subsequently tearing down the container and also
getting rid of the image and so on.

### ``EnvPhase.PREPARE``

During this phase, which is entered during both post processing of test instances and the resolving
of method parameters, all available ``EnvProvider``S will be consulted whether they can provide 
instances for both fields and methods.

This is the point where ``EnvProvider``S can set up instances of requested services or beans based
on some configuration for either test classes or test methods thereof.

### ``EnvPhase.BEFORE_ALL``

The phase is entered before all tests in a given test class will be run, and ``EnvProvider``S will be
instructed to set up their provided environments accordingly.

This is the point where ``EnvProvider``S can set up parts of their environments for a specific test class,
e.g. provide shared fixtures in the file system or the database.

### ``EnvPhase.AFTER_ALL``

The phase is entered after all tests in a given test class have been run, and ``EnvProvider``S will be
instructed to tear down their provided environments accordingly.

This is the point where ``EnvProvider``S can tear down parts of their environments for a specific test
class, e.g. restore file system state or clear the database etc.

### ``EnvPhase.BEFORE_EACH``

The phase is entered before each test in a given test class will be run, and ``EnvProvider``S will be
instructed to set up their provided environments accordingly.

This is the point where ``EnvProvider``S can set up parts of their environments for a specific test
class or test method thereof.

### ``EnvPhase.AFTER_EACH``

The phase is entered after each test in a given test class will be run, and ``EnvProvider``S will be
instructed to tear down their provided environments accordingly.

This is the point where ``EnvProvider``S can tear down parts of their environments for a specific test
class or test method thereof.


## References

- [Github](https://github.com/coldrye-java/junit-testing/tree/master/junit-env-extension)
- [Sonar](http://sonar.coldrye.eu/dashboard?id=eu.coldrye.junit%3Ajunit-env-extension)
