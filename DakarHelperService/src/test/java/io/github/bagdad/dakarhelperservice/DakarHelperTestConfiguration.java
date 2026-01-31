package io.github.bagdad.dakarhelperservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class DakarHelperTestConfiguration {

    private static final String POSTGRES_CONTAINER_NAME = "postgres:18";

    private static final String POSTGRES_DB_NAME = "test_dakar_helper_db";

    @Value("spring.datasource.username")
    private String username;

    @Value("spring.datasource.password")
    private String password;

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresSQLDBContainer() {
        return new PostgreSQLContainer(DockerImageName.parse(POSTGRES_CONTAINER_NAME));
    }

}
