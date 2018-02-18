# junit-testing

This project aims to provide you with useful extensions for and other additions to JUnit5.


## junit-env-extension

The extension provides you with an infrastructure for setting up and tearing down long
running environments during your tests, something that you would not want to do before
or after each test or before or after each test class.

For example starting and stopping an embedded Hadoop mini cluster or an embedded Kafka.
Another example would be to pull and start a given docker image before that the first
test is executed and tear it down when all tests have been executed.


## References

- [Github](https://github.com/coldrye-java/junit-testing)
