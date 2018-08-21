// TBD:LICENSE

package eu.coldrye.junit.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DeleteOnExitHook {

  private static final List<Path> paths = new ArrayList<>();
  private static final Thread shutdownThread = new Thread(DeleteOnExitHook::runHook);

  static {

    Runtime.getRuntime().addShutdownHook(shutdownThread);
  }

  public static final void add(Path path) {

    paths.add(path);
  }

  static final void runHook() {

    for (Path path: paths) {

      try {

        FileUtils.deleteRecursively(path);
      } catch (IOException ex) {

        // just ignore
      }
    }
  }

  // must not be instantiated
  private DeleteOnExitHook() {

  }
}
