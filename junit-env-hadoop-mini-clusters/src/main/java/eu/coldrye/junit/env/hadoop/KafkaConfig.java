package eu.coldrye.junit.env.hadoop;

import lombok.Data;

import java.util.Properties;

@Data
public class KafkaConfig {

  private String kafkaHostname;

  private Integer kafkaPort;

  private Integer kafkaBrokerId;

  private Properties kafkaProperties;

  private String kafkaTempDir;

  private String zookeeperConnectionString;
}
