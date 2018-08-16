// TBD:LICENSE

package eu.coldrye.junit.util;

import org.junit.platform.commons.util.Preconditions;

import java.nio.file.Path;

public final class PreconditionsEx {

  public static Path notBlank(Path path, String message) {

    Preconditions.notNull(path, message);
    Preconditions.notBlank(path.toString(), message);

    return path;
  }

  // must not be instantiated
  private PreconditionsEx() {

  }
}
