package attic.selenium;

import org.openqa.selenium.remote.server.SeleniumServer;

public class ServerManager {

  public final static ServerManager INSTANCE = new ServerManager();

  private SeleniumServer server;

  private int usageCount = 0;

  ServerManager() {

  }

  public synchronized void start() {

    if (usageCount == 0) {
      throw new RuntimeException("not implemented yet");
    }
    usageCount++;
  }

  public synchronized void stop() {

    if (server != null) {
      usageCount--;
      if (usageCount == 0) {
        throw new RuntimeException("not implemented yet");
        // this.server = null;
      }
    }
  }
}
