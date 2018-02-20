package eu.coldrye.junit.env.hadoop.examples;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.Environment;
import eu.coldrye.junit.env.hadoop.HadoopEnvConfig;
import eu.coldrye.junit.env.hadoop.HadoopEnvProvided;
import eu.coldrye.junit.env.hadoop.HadoopEnvProvider;
import org.apache.hadoop.fs.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EnvExtension.class)
@Environment(HadoopEnvProvider.class)
@HadoopEnvConfig(CustomHadoopEnvConfigFactory.class)
public class HdfsTest {

  @HadoopEnvProvided
  private FileSystem dfs;

  @Test
  public void dfsIsAvailable() {

    Assertions.assertNotNull(dfs);
  }
}
