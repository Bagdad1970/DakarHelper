package io.github.bagdad.dakarhelperservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class DakarHelperTestConfiguration {

    private static final String POSTGRES_CONTAINER_NAME = "postgres:18";

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresSQLDBContainer() {
        return new PostgreSQLContainer(DockerImageName.parse(POSTGRES_CONTAINER_NAME));
    }

}
