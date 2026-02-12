package io.github.bagdad.emailhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

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

    private Path saveDir;

}
