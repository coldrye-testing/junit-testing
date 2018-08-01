package eu.coldrye.junit.assertions.file;

import eu.coldrye.junit.util.DiffReportUtils;
import eu.coldrye.junit.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AssertEqualsTest {

  private static final String SIMPLE_EXPECTED = "fixtures/simple/expected.txt";
  private static final String SIMPLE_EXPECTED_PATCH = "fixtures/simple/expected.txt.patch";
  private static final String SIMPLE_EXPECTED_PATCHED = "fixtures/simple/expected.txt.new";
  private static final String SIMPLE_ACTUAL = "fixtures/simple/actual.txt";

  private static final String COMPACTED_JSON_EXPECTED = "fixtures/json/expected.json";
  private static final String COMPACTED_JSON_EXPECTED_PATCH = "fixtures/json/expected.json.patch";
  private static final String COMPACTED_JSON_EXPECTED_PATCHED = "fixtures/json/expected.json.new";
  private static final String COMPACTED_JSON_ACTUAL = "fixtures/json/actual.json";

  private static final String WEIRD_MESSAGE = "\\ weird ^ : , \n` \" / message";

  private static final String ENC_UTF8 = "UTF-8";

  @Test
  public void shouldWriteSingleReport() throws IOException {

    FileAssertions.cleanUp();

    // we expect a failure here
    try {

      FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), pathOf(SIMPLE_ACTUAL));
    } catch (AssertionError ex) {
      // and we will ignore it to run our actual tests
    }

    Path basePath = DiffReportUtils.determineMostRecentReportRoot(FileAssertions.getOutputPath());
    runTests(basePath, SIMPLE_EXPECTED, SIMPLE_ACTUAL, SIMPLE_EXPECTED_PATCH, SIMPLE_EXPECTED_PATCHED);
  }

  @Test
  public void shouldWriteMultipleReports() throws IOException {

    FileAssertions.cleanUp();

    // we expect a failure here
    try {

      Assertions.assertAll(
        () -> FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), pathOf(SIMPLE_ACTUAL)),
        () -> FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), pathOf(SIMPLE_ACTUAL), WEIRD_MESSAGE)
      );
    } catch (AssertionError ex) {
      // and we will ignore it to run our actual tests
    }

    Path basePath = DiffReportUtils.determineMostRecentReportRoot(FileAssertions.getOutputPath()).getParent();
    Path weirdPath = DiffReportUtils.determineMostRecentReportRoot(FileAssertions.getOutputPath(), WEIRD_MESSAGE)
      .getParent();
    Assertions.assertAll(
      () -> runTests(basePath.resolve("1"), SIMPLE_EXPECTED, SIMPLE_ACTUAL, SIMPLE_EXPECTED_PATCH,
        SIMPLE_EXPECTED_PATCHED),
      () -> runTests(weirdPath.resolve("1"), SIMPLE_EXPECTED, SIMPLE_ACTUAL, SIMPLE_EXPECTED_PATCH,
        SIMPLE_EXPECTED_PATCHED)
    );
  }

  @Test
  public void shouldNotFailOnCompactJson() throws IOException {

    FileAssertions.cleanUp();

    // we expect a failure here
    try {

      FileAssertions.assertEquals(pathOf(COMPACTED_JSON_EXPECTED), pathOf(COMPACTED_JSON_ACTUAL));
    } catch (AssertionError ex) {
      // and we will ignore it to run our actual tests
    }

    Path basePath = DiffReportUtils.determineMostRecentReportRoot(FileAssertions.getOutputPath());
    runTests(basePath, COMPACTED_JSON_EXPECTED, COMPACTED_JSON_ACTUAL, COMPACTED_JSON_EXPECTED_PATCH,
      COMPACTED_JSON_EXPECTED_PATCHED);
  }

  @Test
  public void shouldNotFailWhenUsingWeirdMessages() throws IOException {

    FileAssertions.cleanUp();

    // we expect a failure here
    try {

      FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), pathOf(SIMPLE_ACTUAL), WEIRD_MESSAGE);
    } catch (AssertionError ex) {
      // and we will ignore it to run our actual tests
    }

    Path basePath = DiffReportUtils.determineMostRecentReportRoot(FileAssertions.getOutputPath(), WEIRD_MESSAGE);
    runTests(basePath, SIMPLE_EXPECTED, SIMPLE_ACTUAL, SIMPLE_EXPECTED_PATCH, SIMPLE_EXPECTED_PATCHED);
  }

  @Test
  public void shouldNotFailOnPathAndStringWithNoMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), stringOf(SIMPLE_EXPECTED, ENC_UTF8));
  }

  @Test
  public void shouldNotFailOnPathAndStringWithMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), stringOf(SIMPLE_EXPECTED, ENC_UTF8), "message");
  }

  @Test
  public void shouldNotFailOnPathAndReaderWithNoMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), readerOf(pathOf(SIMPLE_EXPECTED), ENC_UTF8));
  }

  @Test
  public void shouldNotFailOnPathAndReaderWithMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(pathOf(SIMPLE_EXPECTED), readerOf(pathOf(SIMPLE_EXPECTED), ENC_UTF8), "message");
  }

  @Test
  public void shouldNotFailOnFileAndStringWithNoMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(fileOf(pathOf(SIMPLE_EXPECTED)), stringOf(SIMPLE_EXPECTED, ENC_UTF8));
  }

  @Test
  public void shouldNotFailOnFileAndStringWithMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(fileOf(pathOf(SIMPLE_EXPECTED)), stringOf(SIMPLE_EXPECTED, ENC_UTF8), "message");
  }

  @Test
  public void shouldNotFailOnFileAndReaderWithNoMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(fileOf(pathOf(SIMPLE_EXPECTED)), readerOf(pathOf(SIMPLE_EXPECTED), ENC_UTF8));
  }

  @Test
  public void shouldNotFailOnFileAndReaderWithMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(fileOf(pathOf(SIMPLE_EXPECTED)), readerOf(pathOf(SIMPLE_EXPECTED), ENC_UTF8),
      "message");
  }

  @Test
  public void shouldNotFailOnFileAndFileWithNoMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(fileOf(pathOf(SIMPLE_EXPECTED)), fileOf(pathOf(SIMPLE_EXPECTED)));
  }

  @Test
  public void shouldNotFailOnFileAndFileWithMessage() throws IOException {

    FileAssertions.cleanUp();
    FileAssertions.assertEquals(fileOf(pathOf(SIMPLE_EXPECTED)), fileOf(pathOf(SIMPLE_EXPECTED)), "message");
  }

  private String stringOf(String path, String encoding) throws IOException {

    return new String(Files.readAllBytes(fileOf(pathOf(path)).toPath()), encoding);
  }

  private Reader readerOf(Path path, String encoding) throws IOException {

    return new InputStreamReader(new FileInputStream(fileOf(path)), encoding);
  }

  private Path pathOf(String path) {

    if (path.startsWith(".") || path.startsWith("..") || path.startsWith("/")) {

      return Paths.get(path);
    } else {

      return Paths.get("/", path);
    }
  }

  private File fileOf(Path path) throws IOException {

    return FileUtils.resolve(AssertEqualsTest.class, path);
  }

  private void runTests(Path basePath, String expected, String actual, String patch, String patched) {

    String expectedName = Paths.get(expected).getFileName().toString();
    Path expectedPath = basePath.resolve(expectedName);
    Path actualPath = basePath.resolve(Paths.get(actual).getFileName());
    Path patchPath = basePath.resolve(FileUtils.extend(expectedName, "patch"));
    Path patchedPath = basePath.resolve(FileUtils.extend(expectedName,"new"));

    Assertions.assertAll(

      () -> FileAssertions.assertExists(expectedPath, expectedPath.toString()),
      () -> FileAssertions.assertEquals(Paths.get("/", expected), expectedPath),
      () -> FileAssertions.assertExists(actualPath, actualPath.toString()),
      () -> FileAssertions.assertEquals(Paths.get("/", actual), actualPath),
      () -> FileAssertions.assertExists(patchPath, patchPath.toString()),
      () -> FileAssertions.assertEquals(Paths.get("/", patch), patchPath),
      () -> FileAssertions.assertExists(patchedPath, patchedPath.toString()),
      () -> FileAssertions.assertEquals(Paths.get("/", patched), patchedPath, patchedPath.toString()),
      () -> FileAssertions.assertEquals(Paths.get("/", actual), actualPath, actualPath.toString())
    );
  }
}
