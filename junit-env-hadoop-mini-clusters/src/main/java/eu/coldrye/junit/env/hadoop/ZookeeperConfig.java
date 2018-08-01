package eu.coldrye.junit.env.hadoop;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ZookeeperConfig {

  private boolean shared;

  private Integer port;

  private String tempDir;

  private String zookeeperConnectionString;

  private int electionPort = -1;

  private int quorumPort = -1;

  private boolean deleteDataDirectoryOnClose = true;

  private int serverId = -1;

  private int tickTime = -1;

  private int maxClientCnxns = -1;

  private Map<String, Object> customProperties = new HashMap<>();
}
