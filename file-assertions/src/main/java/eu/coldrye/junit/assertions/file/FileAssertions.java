// TBD:LICENSE

package eu.coldrye.junit.assertions.file;

import eu.coldrye.junit.diff.DiffReport;
import eu.coldrye.junit.diff.DiffReportFactory;
import eu.coldrye.junit.diff.DiffReportResult;
import eu.coldrye.junit.diff.DiffReportWriter;
import eu.coldrye.junit.util.DiffReportUtils;
import eu.coldrye.junit.util.FileUtils;
import eu.coldrye.junit.util.PathUtils;
import eu.coldrye.junit.util.PreconditionsEx;
import eu.coldrye.junit.util.StringUtils;
import org.junit.platform.commons.util.Preconditions;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileAssertions {

  public static final Path DEFAULT_OUTPUT_PATH = Paths.get(
    System.getProperty("user.dir"), "target", "assertions", "file", "reports");

  public static final String DEFAULT_ENCODING = "UTF-8";

  public static final String EXPECTED_CONTENT_MATCH = "content of both expected and actual should have matched";

  private static Path outputPath = DEFAULT_OUTPUT_PATH;

  private static String encoding = DEFAULT_ENCODING;

  // non final for testing purposes
  private static DiffReportFactory diffReportFactory = new DiffReportFactory();

  private static DiffReportWriter diffReportWriter = new DiffReportWriter();

  /**
   * Sets the output path to which diffs and patches will be written to.
   * <p>
   * Defaults to {@code {user.dir}/target/assertions/file/reports}.
   *
   * @param outputPath
   */
  public static void setOutputPath(Path outputPath) {

    PreconditionsEx.notBlank(outputPath, "outputPath must not be blank");

    FileAssertions.outputPath = outputPath;
  }

  /**
   * Gets the output path.
   *
   * @return the output path
   */
  public static Path getOutputPath() {

    return outputPath;
  }

  /**
   * Sets the expected encoding. Defaults to {@code UTF-8}.
   *
   * @param encoding
   */
  public static void setEncoding(String encoding) {

    Preconditions.notBlank(encoding, "encoding must not be blank"); // NOSONAR

    FileAssertions.encoding = encoding;
  }

  /**
   * Cleans up all of the caller's reports.
   */
  public static void cleanUp() throws IOException {

    FileUtils.deleteRecursively(outputPath.resolve(PathUtils.get(DiffReportUtils.resolveCaller())));
  }

  /**
   * Cleans up all of the classes' reports.
   */
  public static void cleanUp(Class<?> clazz) throws IOException {

    FileUtils.deleteRecursively(outputPath.resolve(PathUtils.get(clazz)));
  }

  public static void assertEquals(Path expected, String actual) throws IOException {

    assertEquals(expected, actual, EXPECTED_CONTENT_MATCH);
  }

  public static void assertEquals(Path expected, String actual, String message) throws IOException {

    Method method = DiffReportUtils.resolveCaller();
    Class<?> clazz = method.getDeclaringClass();
    File expectedFile = FileUtils.resolve(clazz, expected);

    assertEquals(expectedFile, actual, message);
  }

  public static void assertEquals(Path expected, Reader actual) throws IOException {

    assertEquals(expected, actual, EXPECTED_CONTENT_MATCH);
  }

  public static void assertEquals(Path expected, Reader actual, String message) throws IOException {

    Method method = DiffReportUtils.resolveCaller();
    Class<?> clazz = method.getDeclaringClass();
    File expectedFile = FileUtils.resolve(clazz, expected);

    assertEquals(expectedFile, actual, message);
  }

  /**
   * Asserts that the two files @{code expected} and {@code actual} have equal content.
   *
   * @param expected
   * @param actual
   * @throws IOException
   * @see #assertEquals(File, File, String)
   */
  public static void assertEquals(Path expected, Path actual) throws IOException {

    assertEquals(expected, actual, EXPECTED_CONTENT_MATCH);
  }

  /**
   * Asserts that the two files @{code expected} and {@code actual} have equal content.
   *
   * @param expected
   * @param actual
   * @param message
   * @throws IOException
   * @see #assertEquals(File, File, String)
   */
  public static void assertEquals(Path expected, Path actual, String message) throws IOException {

    Method method = DiffReportUtils.resolveCaller();
    Class<?> clazz = method.getDeclaringClass();
    File expectedFile = FileUtils.resolve(clazz, expected);
    File actualFile = FileUtils.resolve(clazz, actual);

    assertEquals(expectedFile, actualFile, message);
  }

  public static void assertEquals(File expected, String actual) throws IOException {

    assertEquals(expected, actual, EXPECTED_CONTENT_MATCH);
  }

  /**
   * Asserts that the expected file has the specified actual content.
   *
   * @param expected
   * @param actual
   * @param message
   * @throws IOException
   */
  public static void assertEquals(File expected, String actual, String message) throws IOException {

    assertEquals0(expected, actual, message);
  }

  public static void assertEquals(File expected, Reader actual) throws IOException {

    assertEquals(expected, actual, EXPECTED_CONTENT_MATCH);
  }

  public static void assertEquals(File expected, Reader actual, String message) throws IOException {

    assertEquals0(expected, actual, message);
  }

  /**
   * Asserts that the two files @{code expected} and {@code actual} have equal content.
   *
   * @param expected
   * @param actual
   * @throws IOException
   * @see #assertEquals(File, File, String)
   */
  public static void assertEquals(File expected, File actual) throws IOException {

    assertEquals(expected, actual, EXPECTED_CONTENT_MATCH);
  }

  /**
   * Asserts that the two files @{code expected} and {@code actual} have equal content.
   * <p>
   * Both the expected file and the actual file must be plain text. Binary files are not supported.
   * By default, all files are assumed to be encoded using UTF-8.
   * You may change this behaviour by setting the encoding using {@link #setEncoding(String)}.
   * <p>
   * If the two files differ, then a report will be written to the output path, containing the expected file,
   * the actual file, a diff report and a new version nonExistent the expected file including the applied changes from
   * the diff report.
   * <p>
   * The report will be located in a sub directory named after the calling context's method and declaring class, e.g.
   * {@code ./target/assertions/file/reports/org/mylib/SomeFileTest/makeSureFilesMatch/1/}.
   *
   * @param expected
   * @param actual
   * @param message
   * @throws IOException
   * @see #setEncoding(String)
   * @see #setOutputPath(Path)
   */
  public static void assertEquals(File expected, File actual, String message) throws IOException {

    assertEquals0(expected, actual, message);
  }

  private static void assertEquals0(File expected, Object actual, String message) throws IOException {

    DiffReport report = diffReportFactory.createReport(expected, actual, encoding);
    if (report.hasDiff()) {

      Path root = DiffReportUtils.determineReportRoot(outputPath,
        EXPECTED_CONTENT_MATCH.equals(message) ? StringUtils.EMPTY : message);
      DiffReportResult result = diffReportWriter.write(root, report);

      throw FileAssertionError.contentMismatch(result, message);
    }
  }

  /**
   * Asserts that the specified {@code path} exists.
   *
   * @param path
   * @see #assertExists(File, String)
   */
  public static void assertExists(Path path) {

    assertExists(path, "expected path to exist");
  }

  /**
   * Asserts that the specified {@code path} exists.
   *
   * @param path
   * @param message
   * @see #assertExists(File, String)
   */
  public static void assertExists(String path, String message) {

    Preconditions.notBlank(path, "path must not be blank"); // NOSONAR

    assertExists(Paths.get(path), message);
  }

  /**
   * Asserts that the specified {@code path} exists.
   *
   * @param path
   * @param message
   * @see #assertExists(File, String)
   */
  public static void assertExists(Path path, String message) {

    PreconditionsEx.notBlank(path, "path must not be blank"); // NOSONAR

    assertExists(path.toFile(), message);
  }

  /**
   * Asserts that the specified (local) {@code file} exists.
   *
   * @param file
   * @see #assertExists(File, String)
   */
  public static void assertExists(File file) {

    assertExists(file, "expected file to exist");
  }

  /**
   * Asserts that the specified (local) {@code file} exists.
   *
   * @param file
   * @param message
   */
  public static void assertExists(File file, String message) {

    Preconditions.notNull(file, "file must not be null"); // NOSONAR
    Preconditions.notBlank(message, "message must not be blank"); // NOSONAR

    if (!file.exists()) {

      throw FileAssertionError.nonExistent(file.toPath(), message);
    }
  }

  // exposed for testing purposes
  static void setDiffReportFactory(DiffReportFactory factory) {

    Preconditions.notNull(factory, "factory must not be null"); // NOSONAR

    FileAssertions.diffReportFactory = factory;
  }

  // exposed for testing purposes
  static void setDiffReportWriter(DiffReportWriter writer) {

    Preconditions.notNull(writer, "writer must not be null"); // NOSONAR

    FileAssertions.diffReportWriter = writer;
  }

  // must not be instantiated
  private FileAssertions() {

  }
}
