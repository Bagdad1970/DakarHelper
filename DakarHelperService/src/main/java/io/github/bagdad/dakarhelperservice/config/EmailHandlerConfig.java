package io.github.bagdad.dakarhelperservice.config;

import emailhandler.EmailConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    private String saveDir;

    @Bean
    public EmailConfig emailConfig() {
        EmailConfig config = new EmailConfig();

        config.setLogin(login);
        config.setPassword(password);
        config.setProtocol(protocol);
        config.setHost(host);
        config.setFolderName(folderName);
        config.setFromTerm(fromTerm);
        config.setSaveDir(saveDir);

        return config;
    }

}
