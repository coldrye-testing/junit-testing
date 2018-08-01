package eu.coldrye.junit.env.hadoop;

import com.github.sakserv.minicluster.impl.HbaseLocalCluster;
import com.github.sakserv.minicluster.impl.HdfsLocalCluster;
import com.github.sakserv.minicluster.impl.KafkaLocalBroker;

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

  KafkaLocalBroker createNewKafkaLocalBrokerInstance(KafkaConfig config) {

    return new KafkaLocalBroker.Builder()
      .setKafkaBrokerId(config.getKafkaBrokerId())
      .setKafkaHostname(config.getKafkaHostname())
      .setKafkaPort(config.getKafkaPort())
      .setKafkaProperties(config.getKafkaProperties())
      .setKafkaTempDir(config.getKafkaTempDir())
      .setZookeeperConnectionString(config.getZookeeperConnectionString())
      .build();
  }

  HbaseLocalCluster createNewHbaseLocalClusterInstance(HbaseConfig config) {

    return new HbaseLocalCluster.Builder()
      .setHbaseConfiguration(config.getHbaseConfiguration())
      .setHbaseMasterInfoPort(config.getHbaseMasterInfoPort())
      .setHbaseMasterPort(config.getHbaseMasterPort())
      .setHbaseRootDir(config.getHbaseRootDir())
      .setHbaseWalReplicationEnabled(config.getHbaseWalReplicationEnabled())
      .setNumRegionServers(config.getNumRegionServers())
      .setZookeeperConnectionString(config.getZookeeperConnectionString())
      .setZookeeperPort(config.getZookeeperPort())
      .setZookeeperZnodeParent(config.getZookeeperZnodeParent())
      .build();
  }
}
