package eu.coldrye.junit.diff;

import eu.coldrye.junit.util.FileUtils;
import eu.coldrye.junit.util.PreconditionsEx;
import org.junit.platform.commons.util.Preconditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DiffReportWriter {

  public DiffReportResult write(Path outputPath, DiffReport report) throws IOException {

    PreconditionsEx.notBlank(outputPath, "outputPath must not be blank"); // NOSONAR
    Preconditions.notNull(report, "report must not be null"); // NOSONAR

    File expected = report.getExpected();
    String extension = FileUtils.extension(expected);
    Path expectedPath = outputPath.resolve(FileUtils.extend("expected", extension));
    String expectedName = expectedPath.getFileName().toString();
    Path actualPath = outputPath.resolve(FileUtils.extend("actual", extension));
    Path patchPath = outputPath.resolve(FileUtils.extend(expectedName, "patch"));
    Path patchedPath = outputPath.resolve(FileUtils.extend(expectedName, "new"));

    DiffMatchPatch dmp = new DiffMatchPatch();
    Files.createDirectories(outputPath);
    writeExpected(expectedPath, dmp, report);
    writeActual(actualPath, dmp, report);
    writePatch(patchPath, report);

    return new DiffReportResult(outputPath, expected.toPath(), expectedPath, actualPath, patchPath, patchedPath);
  }

  private void writeExpected(Path path, DiffMatchPatch dmp, DiffReport report) throws IOException {

    String content = dmp.diff_text1(report.getDiffs());
    FileUtils.writeFile(path.toFile(), content, report.getEncoding());
  }

  private void writeActual(Path path, DiffMatchPatch dmp, DiffReport report) throws IOException {

    String content = dmp.diff_text2(report.getDiffs());
    FileUtils.writeFile(path.toFile(), content, report.getEncoding());
  }

  private void writePatch(Path path, DiffReport report) throws IOException {

    PatchRenderer renderer = new PatchRenderer();
    String content = renderer.render(report.getDiffs());
    FileUtils.writeFile(path.toFile(), content, report.getEncoding());
  }
}
