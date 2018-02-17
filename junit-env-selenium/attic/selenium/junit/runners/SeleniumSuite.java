package attic.selenium.junit.runners;

import attic.selenium.ServerManager;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * TBD:DOCUMENT
 */
public class SeleniumSuite extends Suite {

  /**
   * We keep track of all active suites so that we start and stop/destroy the embedded selenium server once, only.
   */
  static final List<Runner> activeSuites = new ArrayList<>();

  /**
   * Called reflectively on classes annotated with <code>@RunWith(SeleniumSuite.class)</code>
   *
   * @param klass   the root class
   * @param builder builds runners for classes in the suite
   */
  public SeleniumSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {

    super(klass, builder);
  }

  @Override
  public void run(RunNotifier notifier) {

    try {
      synchronized (ServerManager.INSTANCE) {
        if (SeleniumSuite.activeSuites.isEmpty()) {
          //SeleniumServerManager.INSTANCE.configure(...);
          ServerManager.INSTANCE.start();
        }
        SeleniumSuite.activeSuites.add(this);
      }
      super.run(notifier);
    } finally {
      synchronized (ServerManager.INSTANCE) {
        SeleniumSuite.activeSuites.remove(this);
        if (SeleniumSuite.activeSuites.isEmpty()) {
          ServerManager.INSTANCE.stop();
        }
      }
    }
  }
}
