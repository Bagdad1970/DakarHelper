package io.github.bagdad.findhandler.dakarhelperservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class DakarHelperTestConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresSQLDBContainer() {
        return new PostgreSQLContainer(DockerImageName.parse("postgres:18"));
    }

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:8"));
    }

}
