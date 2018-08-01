# junit-testing

This project aims to provide you with useful extensions for and other additions to JUnit5.


## [junit-common](./junit-common)

Provides commonly used code for ``junit-testing`` projects.


## [junit-testing-common](./junit-testing-common)

Provides commonly used test code for ``junit-testing`` projects.


## [junit-env-extension](./junit-env-extension)

The extension provides you with an infrastructure for setting up and tearing down long
running environments during your tests, something that you would not want to do before
or after each test or before or after each test class.

For example starting and stopping an embedded Hadoop mini cluster or an embedded Kafka.
Another example would be to pull and start a given docker image before that the first
test is executed and tear it down when all tests have been executed.


## [junit-env-hadoop-mini-clusters](./junit-env-hadoop-mini-clusters)

Work in progress.


## [file-assertions](./file-assertions)

Assertions for files and their content.


## References

- [Github](https://github.com/coldrye-java/junit-testing)
- [Sonar](http://sonar.coldrye.eu/dashboard?id=eu.coldrye.junit%3Ajunit-testing)
