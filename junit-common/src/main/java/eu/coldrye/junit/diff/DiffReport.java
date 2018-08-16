package eu.coldrye.junit.diff;

import eu.coldrye.junit.diff.DiffMatchPatch.Diff;
import eu.coldrye.junit.diff.DiffMatchPatch.Operation;
import lombok.Getter;

import java.io.File;
import java.util.LinkedList;

@SuppressWarnings("squid:S1319")
public class DiffReport {

  @Getter
  private File expected;

  @Getter
  private LinkedList<Diff> diffs;

  @Getter
  private String encoding;

  public DiffReport(File expected, LinkedList<Diff> diffs, String encoding) {

    this.expected = expected;
    this.diffs = diffs;
    this.encoding = encoding;
  }

  public boolean hasDiff() {

    // if the two files match then there will be a single entry
    return !(diffs.size() == 1 && diffs.get(0).operation == Operation.EQUAL);
  }
}
