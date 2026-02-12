package io.github.bagdad.dakarhelperservice.config;

import io.github.bagdad.emailhandler.EmailConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class EmailHandlerConfig {

    @Value("${app.email.login}")
    private String login;

    @Value("${app.email.password}")
    private String password;

    @Value("${app.email.protocol}")
    private String protocol;

    @Value("${app.email.host}")
    private String host;

    @Value("${app.email.folder-name}")
    private String folderName;

    @Value("${app.email.from-term}")
    private String fromTerm;

    @Value("${app.email.save-dir}")
    private Path saveDir;

    @Bean
    public EmailConfig emailConfig() {
        return EmailConfig.builder()
                .login(login)
                .password(password)
                .protocol(protocol)
                .host(host)
                .folderName(folderName)
                .fromTerm(fromTerm)
                .saveDir(saveDir)
                .build();
    }

}
