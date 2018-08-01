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

import com.github.sakserv.minicluster.impl.HdfsLocalCluster;
import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.Environment;
import eu.coldrye.junit.env.hadoop.HadoopEnvConfig;
import eu.coldrye.junit.env.hadoop.HadoopEnvProvided;
import eu.coldrye.junit.env.hadoop.HadoopEnvProvider;
import eu.coldrye.junit.env.hadoop.HdfsConfig;
import org.apache.hadoop.fs.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EnvExtension.class)
@Environment(HadoopEnvProvider.class)
@HadoopEnvConfig(CustomHadoopEnvConfigFactory.class)
public class HdfsTest {

  @HadoopEnvProvided
  private FileSystem dfs;

  @HadoopEnvProvided
  private HdfsLocalCluster hdfsLocalCluster;

  @HadoopEnvProvided
  private HdfsConfig hdfsConfig;

  @Test
  public void dfsIsAvailable() {

    Assertions.assertNotNull(dfs);
  }

  @Test
  public void hdfsLocalClusterIsAvailable() {

    Assertions.assertNotNull(hdfsLocalCluster);
  }

  @Test
  public void hdfsConfigIsAvailable() throws Exception {

    Assertions.assertNotNull(hdfsConfig);
  }
}
