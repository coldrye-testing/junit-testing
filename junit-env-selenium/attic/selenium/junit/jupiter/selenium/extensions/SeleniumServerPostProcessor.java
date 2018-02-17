package attic.selenium.junit.jupiter.selenium.extensions;

import attic.selenium.ServerManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class SeleniumServerPostProcessor implements TestInstancePostProcessor {

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {

    if (CONFIGURATION.selenium.useEmbeddedServer) {
      ServerManager.INSTANCE.start();
    }
  }
}
