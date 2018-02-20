package eu.coldrye.junit.env.hadoop.examples;

import eu.coldrye.junit.env.hadoop.HadoopEnvConfigFactory;
import eu.coldrye.junit.env.hadoop.HdfsConfig;

public class CustomHadoopEnvConfigFactory implements HadoopEnvConfigFactory {

  public HdfsConfig defaultHdfsConfig() {

    HdfsConfig result = new HdfsConfig();
    result.setHdfsFormat(true);
    result.setHdfsTempDir("/tmp");
    return result;
  }
}
