package io.github.bagdad.emailhandler;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Set;

@Slf4j
public class MessageHandlerUtils {

    private static final Set<String> EXCEL_EXTENSIONS = Set.of(".xls", "xlsx");

    private static final String MESSAGE_SENT_DATE_HEADER = "Date";

    static BodyPart extractTextBodyPart(Multipart multipart) throws IOException, MessagingException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);

            if (bodyPart.getContentType().contains("multipart/alternative")) {
                Multipart nestedMultipart = (Multipart) bodyPart.getContent();
                for (int j = 0; j < nestedMultipart.getCount(); j++) {
                    BodyPart nestedPart = nestedMultipart.getBodyPart(j);
                    if (nestedPart.getContentType().contains("text/plain")) {
                        log.debug("Found text/plain alternative part");
                        return nestedPart;
                    }
                }
            }
            log.debug("No text/plain part found in nested multipart");
        }
        return null;
    }

    static OffsetDateTime getSentOffsetDateTime(Message message) throws MessagingException {
        String[] dateHeaders = message.getHeader(MESSAGE_SENT_DATE_HEADER);
        if (dateHeaders == null || dateHeaders.length == 0) {
            return null;
        }

        String rawDate = dateHeaders[0].trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss X", Locale.ENGLISH);

        try {
            return OffsetDateTime.parse(rawDate, formatter);
        }
        catch (DateTimeParseException e) {
            log.error("Unable to parse Date header", e);
            throw new IllegalArgumentException("Не удалось распарсить заголовок Date: " + rawDate, e);
        }
    }

    static boolean isExcelFile(String filename) {
        return EXCEL_EXTENSIONS.stream()
                .anyMatch(filename::endsWith);
    }

}
