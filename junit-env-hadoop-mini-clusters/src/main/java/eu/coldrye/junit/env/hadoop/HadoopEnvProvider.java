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

package eu.coldrye.junit.env.hadoop;

import com.github.sakserv.minicluster.MiniCluster;
import com.github.sakserv.minicluster.impl.HdfsLocalCluster;
import eu.coldrye.junit.env.AbstractEnvProvider;
import eu.coldrye.junit.env.EnvPhase;
import eu.coldrye.junit.util.SocketUtils;
import org.apache.hadoop.fs.FileSystem;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class HadoopEnvProvider extends AbstractEnvProvider {

  static Map<Class<? extends MiniCluster>, ? extends MiniCluster> instances = new ConcurrentHashMap<>();

  @Override
  public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

    return annotated.isAnnotationPresent(HadoopEnvProvided.class)
      && (
        FileSystem.class.equals(classOrInterface)
      || HdfsLocalCluster.class.equals(classOrInterface)
      || HdfsConfig.class.equals(classOrInterface)
      );
  }

  @Override
  public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

    if (FileSystem.class == classOrInterface) {
      try {
        return getStore().get(HdfsLocalCluster.class, HdfsLocalCluster.class).getHdfsFileSystemHandle();
      }
      catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    } else if (HdfsLocalCluster.class == classOrInterface) {
      return getStore().get(HdfsLocalCluster.class, HdfsLocalCluster.class);
    } else if (HdfsConfig.class == classOrInterface) {
      return getStore().get(HdfsConfig.class, HdfsConfig.class);
    }

    throw new IllegalArgumentException("TODO:classOrInterface not supported");
  }

  @Override
  public void setUpEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception {

    Store store = getStore();

    switch (phase) {

      case INIT: {

        synchronized (HadoopEnvProvider.class) {

          if (!annotated.isAnnotationPresent(HadoopEnvConfig.class)) {
            throw new IllegalArgumentException("no @HadoopEnvConfig specified, unable to set up the environment during INIT");
          }

          // if initialized ...

          HadoopMiniClusterFactory miniClusterFactory = new HadoopMiniClusterFactory();

          HadoopEnvConfig envConfig = annotated.getAnnotation(HadoopEnvConfig.class);

          Class<? extends HadoopEnvConfigFactory> factoryClass = envConfig.value();
          Constructor constructor = factoryClass.getConstructor();
          HadoopEnvConfigFactory factory = (HadoopEnvConfigFactory) constructor.newInstance();

          HdfsConfig hdfsConfig = factory.hdfsConfig();
          if (!Objects.isNull(hdfsConfig)) {
            Integer[] ports = SocketUtils.findAvailableTcpPorts(2).toArray(new Integer[0]);
            hdfsConfig.setHdfsNamenodePort(ports[0]);
            hdfsConfig.setHdfsNamenodeHttpPort(ports[1]);
            HdfsLocalCluster instance = miniClusterFactory.createNewHdfsLocalClusterInstance(hdfsConfig);
            instance.start();
            store.put(HdfsLocalCluster.class, instance);
            store.put(HdfsConfig.class, hdfsConfig);
          }
        }
        break;
      }
    }
  }

  @Override
  public void tearDownEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception {

    Store store = getStore();

    switch (phase) {

      case DEINIT: {

//        new HadoopEnvDeinitializer().run(getStore());
        break;
      }
    }
  }
}
