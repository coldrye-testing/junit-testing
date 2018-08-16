package eu.coldrye.junit.diff;

import java.nio.file.Path;

public class DiffReportResult {

  private static final String NEWLINE = "\n";

  private static final String INDENT = "\t";
  
  private Path basePath;

  private Path origin;

  private Path expected;

  private Path actual;

  private Path patch;

  private Path patched;

  public DiffReportResult(Path basePath, Path origin, Path expected, Path actual, Path patch, Path patched) {

    this.basePath = basePath;
    this.origin = origin;
    this.expected = expected;
    this.actual = actual;
    this.patch = patch;
    this.patched = patched;
  }

  public Path getOrigin() {

    return this.origin;
  }

  public Path getExpected() {

    return expected;
  }

  public Path getActual() {

    return actual;
  }

  public String toString() {

    StringBuilder builder = new StringBuilder();

    builder
      .append(INDENT)
      .append("A report was written to ")
      .append(basePath)
      .append(NEWLINE)

      .append(INDENT)
      .append(INDENT)
      .append(expected.getFileName())
      .append(NEWLINE)

      .append(INDENT)
      .append(INDENT)
      .append(actual.getFileName())
      .append(NEWLINE)

      .append(INDENT)
      .append(INDENT)
      .append(patch.getFileName())
      .append(NEWLINE)

      .append(INDENT)
      .append(INDENT)
      .append(patched.getFileName())
      .append(NEWLINE);

    return builder.toString();
  }
}
