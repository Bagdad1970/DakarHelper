package io.github.bagdad.emailhandler;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageHandlerTest {

    private static List<VendorWithMaxFileDateTime> vendors;
    private static MessageHandler messageHandler;

    @BeforeAll
    static void setupVendors() {
        VendorWithMaxFileDateTime vendor1 = VendorWithMaxFileDateTime.builder()
                .title("vendor1")
                .maxDateTime(OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0)))
                .build();

        vendors = List.of(vendor1);

        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setSaveDir(Path.of("/app/data"));

        messageHandler = new MessageHandler(vendors, emailConfig);
    }

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

    @Test
    void Finding_vendor_title_in_empty_text_must_return_empty_vendor() {
        String text = "";
        VendorWithMaxFileDateTime expected = new VendorWithMaxFileDateTime("");

        VendorWithMaxFileDateTime vendor = messageHandler.findVendorInText(text);

        assertThat(vendor).isEqualTo(expected);
    }

    @Test
    void Finding_vendor_title_in_text_without_this_vendor_must_return_empty_vendor() {
        String text = "Some text with missed vendor";
        VendorWithMaxFileDateTime expected = new VendorWithMaxFileDateTime("");

        VendorWithMaxFileDateTime vendor = messageHandler.findVendorInText(text);

        assertThat(vendor).isEqualTo(expected);
    }

    @Test
    void Finding_vendor_title_in_text_with_this_vendor_must_return_vendor() {
        String text = "Some text with vendor1";

        VendorWithMaxFileDateTime vendor = messageHandler.findVendorInText(text);

        assertThat(vendor).isEqualTo(vendors.get(0));
    }


}
