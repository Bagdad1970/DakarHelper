package io.github.bagdad.emailhandler;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MessageHandlerUtilsTest {

    private static GreenMail greenMail;

    @Test
    void test() throws MessagingException, IOException {
        greenMail = new GreenMail(ServerSetupTest.SMTP_IMAP);
        greenMail.start();

        MimeMessage message = new MimeMessage();

        try {
            GreenMailUtil.sendTextEmailTest(
                    "to@localhost", "from@localhost", "some subject", "Sent using available port detection");
            assertEquals("Sent using available port detection", greenMail.getReceivedMessages()[0].getContent());
        }
        finally {
            greenMail.stop();
        }
    }

}
