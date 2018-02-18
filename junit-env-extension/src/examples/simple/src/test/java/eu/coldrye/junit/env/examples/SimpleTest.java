package eu.coldrye.junit.env.examples;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EnvExtension.class)
@Environment(SimpleEnvProvider.class)
public class SimpleTest {

  @Test
  public void helloWorld(@SimpleEnvProvided HelloWorldClient client) {

    HelloWorldResponse response = client.helloWorld();
    Assertions.assertEquals("hello world", response.data());
  }
}
