// TBD:LICENSE

package eu.coldrye.junit.assertions.file;

import eu.coldrye.junit.diff.DiffReportResult;
import eu.coldrye.junit.util.PreconditionsEx;
import org.junit.platform.commons.util.Preconditions;

import java.nio.file.Path;
import java.util.Objects;

public class FileAssertionError extends AssertionError {

  private static final String NEWLINE = "\n";

  private FileAssertionError(String message) {

    super(message);
  }

  public static FileAssertionError contentMismatch(DiffReportResult result, String message) {

    Preconditions.notNull(result, "result must not be null");
    Preconditions.notBlank(message, "message must not be blank");

    StringBuilder builder = new StringBuilder();

    builder
      .append("The expected file '")
      .append(result.getOrigin())
      .append("' and actual file '")
      .append(result.getActual())
      .append("' differ.");
    appendCustomMessage(builder, message);
    builder
      .append(NEWLINE)
      .append(result.toString());

    return new FileAssertionError(builder.toString());
  }

  public static FileAssertionError nonExistent(Path path, String message) {

    PreconditionsEx.notBlank(path, "path must not be blank");
    Preconditions.notBlank(message, "message must not be blank");

    StringBuilder builder = new StringBuilder();

    builder
      .append("The specified path '")
      .append(path.toString())
      .append("' does not exist.");
    appendCustomMessage(builder, message);

    return new FileAssertionError(builder.toString());
  }

  private static void appendCustomMessage(StringBuilder builder, String message) {

    if (Objects.nonNull(message)) {

      builder
        .append(NEWLINE)
        .append(message);
    }
  }
}
