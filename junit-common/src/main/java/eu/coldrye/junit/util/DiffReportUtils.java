// TBD:LICENSE

package eu.coldrye.junit.util;

import java.lang.reflect.Method;
import java.nio.file.Path;

public final class DiffReportUtils {

  public static Path determineReportRoot(Path root, String message) {

    return DiffReportUtils.determineReportRoot(root, resolveCaller(), message);
  }

  public static Path determineReportRoot(Path root, Method method, String message) {

    Path targetRoot = root.resolve(PathUtils.get(method).resolve(StringUtils.slug(message)));
    int index = 1;
    do {
      Path path = targetRoot.resolve(String.valueOf(index));

      if (!path.toFile().exists()) {

        return path;
      }

      index++;
    } while (true);
  }

  public static Path determineMostRecentReportRoot(Path root) {

    return determineMostRecentReportRoot(root, null);
  }

  public static Path determineMostRecentReportRoot(Path root, String message) {

    Path basePath = determineReportRoot(root, message);

    return basePath.getParent().resolve(Integer.toString(Integer.valueOf(basePath.getFileName().toString()) - 1));
  }

  public static Method resolveCaller() {

    return StackTraceUtils.resolveCaller(method -> ReflectionUtils.isAnnotatedBy(method, "Test"));
  }

  // must not be instantiated
  private DiffReportUtils() {

  }
}
