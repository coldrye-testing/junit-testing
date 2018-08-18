// TBD:LICENSE

package eu.coldrye.junit.util;

import org.junit.platform.commons.util.Preconditions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

public final class FileUtils {

  private static final String DOT = ".";

  public static Path createTempFile() throws IOException {

    return createTempFile(null, null, true);
  }

  public static Path createTempFile(String prefix, String suffix, boolean deleteOnExit) throws IOException {

    return createTempFile(null, prefix, suffix, deleteOnExit);
  }

  public static Path createTempFile(Path path, String prefix, String suffix, boolean deleteOnExit,
                                    FileAttribute<?>... attrs) throws IOException {

    Path result = Files.createTempFile(path, prefix, suffix, attrs);
    if (deleteOnExit) {

      DeleteOnExitHook.add(result);
    }

    return result;
  }

  public static Path createTempDirectory() throws IOException {

    return createTempDirectory(null,true);
  }

  public static Path createTempDirectory(String prefix, boolean deleteOnExit) throws IOException {

    return createTempDirectory(null, prefix, deleteOnExit);
  }

  public static Path createTempDirectory(Path path, String prefix, boolean deleteOnExit) throws IOException {

    return createTempDirectory(path, prefix, deleteOnExit);
  }

  public static Path createTempDirectory(Path path, String prefix, boolean deleteOnExit, FileAttribute<?>... attrs)
    throws IOException {

    Path result = Files.createTempDirectory(path, prefix, attrs);
    if (deleteOnExit) {

      DeleteOnExitHook.add(result);
    }

    return result;
  }

  public static String extend(String name, String extension) {

    Preconditions.notBlank(name, "name must not be blank"); // NOSONAR
    Preconditions.notBlank(extension, "extension must not be blank"); // NOSONAR

    return name + "." + extension;
  }

  public static String extension(File file) {

    Preconditions.notNull(file, "file must not be null"); // NOSONAR

    String name = file.getName();
    int beginIndex = 0;
    if (name.startsWith(DOT)) {

      beginIndex++;
    }

    return name.substring(name.indexOf(DOT, beginIndex) + 1);
  }

  public static void writeFile(File file, String content, String encoding) throws IOException {

    Preconditions.notNull(file, "file must not be null"); // NOSONAR
    Preconditions.notNull(content, "content must not be null"); // NOSONAR
    Preconditions.notBlank(encoding, "encoding must not be blank"); // NOSONAR

    try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), encoding))) {

      pw.print(content);
    }
  }

  public static String readFile(File file, String encoding) throws IOException {

    Preconditions.notNull(file, "file must not be null"); // NOSONAR
    Preconditions.notBlank(encoding, "encoding must not be blank"); // NOSONAR

    return new String(Files.readAllBytes(file.toPath()), encoding);
  }

  /**
   * Tries to resolves the specified path from the class path of the specified class clazz, and, if that fails,
   * the path will be resolved from the filesystem.
   *
   * @param path
   * @return
   */
  public static File resolve(Class<?> clazz, Path path) throws IOException {

    PreconditionsEx.notBlank(path, "path must not be blank"); // NOSONAR

    File resolved;
    ClassPathResource cpr = ClassPathResource.of(clazz, path);
    if (cpr.exists()) {

      resolved = cpr.file();
    } else {

      resolved = path.toFile();
    }

    return resolved;
  }

  /**
   * Recursively deletes the specified path.
   *
   * Important: There are no sanity checks whatever, so be careful.
   *
   * @param path
   */
  public static void deleteRecursively(Path path) throws IOException {

    PreconditionsEx.notBlank(path, "path must not be blank"); // NOSONAR

    Deque<Path> pending = new ArrayDeque<>();
    pending.push(path);
    while (!pending.isEmpty()) {

      Path next = pending.pop();
      File file = next.toFile();
      if (file.exists()) {

        if (file.isDirectory()) {

          List<Path> contained = new ArrayList<>();
          try (Stream<Path> stream = Files.list(next)) {

            stream.forEach(contained::add);
            if (contained.isEmpty()) {

              Files.delete(next);
            } else {

              pending.push(next);
              contained.forEach(pending::push);
            }
          }
        } else {

          Files.delete(next);
        }
      }
    }
  }

  // must not be instantiated
  private FileUtils() {

  }
}
