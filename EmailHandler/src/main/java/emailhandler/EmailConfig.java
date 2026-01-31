package emailhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfig {

    private String login;

    private String password;

    private String protocol;

    private String host;

    private String folderName;

    private String fromTerm;

}
