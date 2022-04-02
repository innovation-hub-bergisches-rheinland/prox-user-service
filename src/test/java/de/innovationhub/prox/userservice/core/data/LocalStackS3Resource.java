package de.innovationhub.prox.userservice.core.data;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.containers.GenericContainer;

public class LocalStackS3Resource implements QuarkusTestResourceLifecycleManager {
  private GenericContainer<?> localstack;

  @Override
  public Map<String, String> start() {
    localstack =
        new GenericContainer<>("localstack/localstack:0.14")
            .withExposedPorts(4566)
            .withEnv("START_WEB", "0")
            .withEnv("SERVICES", "s3");

    localstack.start();

    Map<String, String> conf = new HashMap<>();
    var localstackUrl = "http://localhost:" + localstack.getMappedPort(4566);
    conf.put("s3.url", localstackUrl);
    conf.put("quarkus.s3.endpoint-override", localstackUrl);
    return conf;
  }

  @Override
  public void stop() {
    localstack.stop();
  }
}
