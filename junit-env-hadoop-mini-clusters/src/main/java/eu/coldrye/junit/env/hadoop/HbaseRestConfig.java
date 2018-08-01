package eu.coldrye.junit.env.hadoop;

import lombok.Data;

@Data
public class HbaseRestConfig {

  private Integer hbaseRestPort;

  private Integer hbaseRestInfoPort;

  private String hbaseRestHost;

  private Boolean hbaseRestReadOnly;

  private Integer hbaseRestThreadMin;

  private Integer hbaseRestThreadMax;
}
