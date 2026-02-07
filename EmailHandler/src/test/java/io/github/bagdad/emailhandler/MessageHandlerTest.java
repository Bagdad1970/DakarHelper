package io.github.bagdad.emailhandler;

import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        messageHandler = new MessageHandler(vendors, null);
    }

    @Test
    void Finding_vendor_title_in_empty_text_must_return_empty_vendor() {
        String text = "";
        VendorWithMaxFileDateTime expected = new VendorWithMaxFileDateTime("");

        VendorWithMaxFileDateTime vendor = messageHandler.findVendorTitleInText(text);

        assertThat(vendor).isEqualTo(expected);
    }

    @Test
    void Finding_vendor_title_in_text_without_this_vendor_must_return_empty_vendor() {
        String text = "Some text with missed vendor";
        VendorWithMaxFileDateTime expected = new VendorWithMaxFileDateTime("");

        VendorWithMaxFileDateTime vendor = messageHandler.findVendorTitleInText(text);

        assertThat(vendor).isEqualTo(expected);
    }

    @Test
    void Finding_vendor_title_in_text_with_this_vendor_must_return_vendor() {
        String text = "Some text with vendor1";

        VendorWithMaxFileDateTime vendor = messageHandler.findVendorTitleInText(text);

        assertThat(vendor).isEqualTo(vendors.get(0));
    }


}
