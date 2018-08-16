package eu.coldrye.junit.env.hadoop;

import lombok.Data;
import org.apache.hadoop.conf.Configuration;

@Data
public class HdfsConfig {

  private Integer hdfsNamenodePort;
  private Integer hdfsNamenodeHttpPort;
  private String hdfsTempDir;
  private Integer hdfsNumDatanodes;
  private Boolean hdfsEnablePermissions;
  private Boolean hdfsFormat;
  private Boolean hdfsEnableRunningUserAsProxyUser;
  private Configuration hdfsConfig;

  void setHdfsNamenodePort(int port) {
    hdfsNamenodePort = port;
  }

  void setHdfsNamenodeHttpPort(int port) {
    hdfsNamenodeHttpPort = port;
  }
}
