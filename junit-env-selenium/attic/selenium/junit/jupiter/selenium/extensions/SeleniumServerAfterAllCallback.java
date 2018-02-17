package attic.selenium.junit.jupiter.selenium.extensions;

import attic.selenium.ServerManager;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SeleniumServerAfterAllCallback implements AfterAllCallback {

  @Override
  public void afterAll(ExtensionContext context) throws Exception {

    if (CONFIGURATION.selenium.useEmbeddedServer) {
      ServerManager.INSTANCE.stop();
    }
  }
}
