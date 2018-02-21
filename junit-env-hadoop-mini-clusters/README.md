# junit5-env-hadoop-mini-clusters

The project provides you with an environment provider for hadoop mini clusters, for use
with ``junit-env-extension``.



## Usage

### Maven

Include the following dependency in your pom and you are good to go

```
<dependency>
  <groupId>eu.coldrye.junit</groupId>
  <artifactId>junit-env-hadoop-mini-clusters</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Junit

```
package eu.coldrye.junit.env.hadoop.examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.Environment;

import eu.coldrye.junit.env.hadoop.HadoopEnvProvider;
import eu.coldrye.junit.env.hadoop.HadoopEnvProvided;
import eu.coldrye.junit.env.hadoop.HadoopEnvConfig;


@ExtendWith(EnvExtension.class)
@Environment(HadoopEnvProvider.class)
@HadoopEnvConfig(CustomHadoopEnvConfigFactory.class)
public class SampleTest {

  @HadoopEnvProvided
  private Filesystem dfs;

  @Test
  public void writeSomethingToTheDfs() {
    // ...
  }
}
```

See the provided [examples](https://github.com/coldrye-java/junit-testing/tree/master/junit-env-hadoop-mini-clusters/src/examples) 
for more information.


## References

- [Github](https://github.com/coldrye-java/junit-testing/tree/master/junit-env-hadoop-mini-clusters)
- [Sonar](http://sonar.coldrye.eu/dashboard?id=eu.coldrye.junit%3Ajunit-env-hadoop-mini-clusters)
- [hadoop-mini-clusters](https://github.com/sakserv/hadoop-mini-clusters)
