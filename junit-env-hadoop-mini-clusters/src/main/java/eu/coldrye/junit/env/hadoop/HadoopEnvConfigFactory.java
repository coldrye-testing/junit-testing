package eu.coldrye.junit.env.hadoop;

public interface HadoopEnvConfigFactory {

  default HdfsConfig defaultHdfsConfig() {

    return null;
  }

  default ZkConfig defaultZkConfig() {

    return null;
  }

  default HbaseConfig defaultHbaseConfig() {

    return null;
  }

  default KafkaConfig defaultKafkaConfig() {

    return null;
  }
}
