package eu.coldrye.junit.env.hadoop;

import lombok.Data;
import org.apache.hadoop.conf.Configuration;

@Data
public class HbaseConfig {

  private Integer hbaseMasterPort;

  private Integer hbaseMasterInfoPort;

  private Integer numRegionServers;

  private String hbaseRootDir;

  private Integer zookeeperPort;

  private String zookeeperConnectionString;

  private String zookeeperZnodeParent;

  private Boolean hbaseWalReplicationEnabled;

  private Configuration hbaseConfiguration;
}
