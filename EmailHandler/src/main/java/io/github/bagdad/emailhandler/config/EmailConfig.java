package io.github.bagdad.emailhandler.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailConfig {

    private String login;

    private String password;

    private String protocol;

    private String host;

    private String folderName;

    private String fromTerm;

}
