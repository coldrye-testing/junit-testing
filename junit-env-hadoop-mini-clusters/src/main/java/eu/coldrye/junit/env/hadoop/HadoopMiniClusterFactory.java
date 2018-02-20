package eu.coldrye.junit.env.hadoop;

import com.github.sakserv.minicluster.impl.HdfsLocalCluster;

public class HadoopMiniClusterFactory {

  HdfsLocalCluster createNewHdfsLocalClusterInstance(HdfsConfig config) {

    return new HdfsLocalCluster.Builder()
      .setHdfsConfig(config.getHdfsConfig())
      .setHdfsEnablePermissions(config.getHdfsEnablePermissions())
      .setHdfsEnableRunningUserAsProxyUser(config.getHdfsEnableRunningUserAsProxyUser())
      .setHdfsFormat(config.getHdfsFormat())
      .setHdfsNamenodeHttpPort(config.getHdfsNamenodeHttpPort())
      .setHdfsNamenodePort(config.getHdfsNamenodePort())
      .setHdfsNumDatanodes(config.getHdfsNumDatanodes())
      .setHdfsTempDir(config.getHdfsTempDir())
      .build();
  }
}
