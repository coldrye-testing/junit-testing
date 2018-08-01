/*
 * Copyright 2018 coldrye.eu, Carsten Klein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.coldrye.junit.env.hadoop.examples;

import eu.coldrye.junit.env.hadoop.HadoopEnvConfigFactory;
import eu.coldrye.junit.env.hadoop.HbaseConfig;
import eu.coldrye.junit.env.hadoop.HdfsConfig;
import org.apache.hadoop.conf.Configuration;

public class CustomHadoopEnvConfigFactory implements HadoopEnvConfigFactory {

  @Override
  public HdfsConfig hdfsConfig() {

    HdfsConfig result = new HdfsConfig();
    result.setHdfsFormat(true);
    result.setHdfsTempDir("/tmp");
    result.setHdfsEnableRunningUserAsProxyUser(false);
    result.setHdfsNumDatanodes(3);
    result.setHdfsEnablePermissions(false);
    Configuration config = new Configuration();
    result.setHdfsConfig(config);
    return result;
  }

  @Override
  public HbaseConfig hbaseConfig() {

    HbaseConfig result = new HbaseConfig();
    return result;
  }
}
