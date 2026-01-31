package emailhandler;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeUtility;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class MessageHandler {

    private static final Set<String> EXCEL_EXTENSIONS = Set.of(".xls", ".xlsx");

    private Map<String, Boolean> vendorVisits;
    private final List<String> vendors;
    private final FileHandler fileHandler;

    @Getter
    private final Map<String, List<String>> collectedFiles;

    public MessageHandler(List<String> vendors, FileHandler fileHandler) {
        this.vendors = vendors;
        this.fileHandler = fileHandler;
        collectedFiles = new HashMap<>();
        initVendorVisits();
        initVendorFiles();
    }

    private void initVendorVisits() {
        vendorVisits = new HashMap<>();
        for (String vendor : vendors) {
            vendorVisits.put(vendor, false);
        }
    }

    private void initVendorFiles() {
        for (String vendor : vendors) {
            collectedFiles.put(vendor, new ArrayList<>());
        }
    }

    public void processMessages(Message[] messages) {
        List<Message> reversedMessages = Arrays.asList(messages);
        Collections.reverse(reversedMessages);
        log.info("Processing {} messages", reversedMessages.size());

        int messageIndex = 0;
        while (hasUnvisitedCompanies() && messageIndex < reversedMessages.size()) {
            Message message = reversedMessages.get(messageIndex);
            try {
                String subject = message.getSubject();
                log.debug("Processing message #{}: subject='{}'", messageIndex, subject != null ? subject : "[no subject]");
            }
            catch (MessagingException ignored) {
                log.debug("Processing message #{} (subject unavailable)", messageIndex);
            }

            processMessage(message);
            messageIndex++;
        }
    }

    private boolean hasUnvisitedCompanies() {
        return vendorVisits.containsValue(false);
    }

    private void processMessage(Message message) {
        try {
            String subject = message.getSubject();
            String vendorTitle = findVendorTitleInText(subject);
            log.debug("Subject-based vendor match: '{}'", vendorTitle.isEmpty() ? "[none]" : vendorTitle);

            if (message.getContent() instanceof Multipart multipart) {
                processMultipartMessage(vendorTitle, multipart);
            }
        }
        catch (IOException | MessagingException e) {
            log.error("Error extracting message content", e);
        }
        catch (Exception e) {
            log.error("Unexpected error in processMessage", e);
        }
    }

    private void processMultipartMessage(String vendorTitle, Multipart multipart) {
        try {
            if (vendorTitle.isEmpty()) {
                vendorTitle = findVendorTitleInBody(multipart);
                log.debug("Body-based vendor match: '{}'", vendorTitle.isEmpty() ? "[none]" : vendorTitle);
            }

            if (!vendorTitle.isEmpty() && !vendorVisits.get(vendorTitle)) {
                log.info("Processing first message for vendor: {}", vendorTitle);
                saveExcelFiles(vendorTitle, multipart);
                vendorVisits.put(vendorTitle, true);
            }
            else if (!vendorTitle.isEmpty()) {
                log.debug("Vendor '{}' already processed — skipping", vendorTitle);
            }
            else {
                log.debug("No matching vendor found in message — skipping");
            }
        }
        catch (IOException | MessagingException e) {
            log.error("Error during multipart processing", e);
        }
        catch (Exception e) {
            log.error("Unexpected error in processMultipartMessage", e);
        }
    }

    private String findVendorTitleInBody(Multipart multipart) throws IOException, MessagingException {
        BodyPart textBodyPart = extractTextBodyPart(multipart);
        if (textBodyPart != null) {
            String textContent = textBodyPart.getContent().toString();
            return findVendorTitleInText(textContent);
        }
        log.debug("No text/plain part found in multipart");
        return "";
    }

    private BodyPart extractTextBodyPart(Multipart multipart) throws IOException, MessagingException {
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

    private String findVendorTitleInText(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        String lowerText = text.toLowerCase();
        Optional<String> match = vendorVisits.keySet().stream()
                .filter(vendorTitle -> lowerText.contains(vendorTitle.toLowerCase()))
                .findFirst();

        match.ifPresent(vendor -> log.debug("Matched vendor '{}' in text", vendor));
        return match.orElse("");
    }

    private void saveExcelFiles(String vendorTitle, Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                processBodyPartForExcel(vendorTitle, bodyPart);
            }
        }
        catch (MessagingException e) {
            log.error("Error iterating multipart parts", e);
        }
    }

    private void processBodyPartForExcel(String vendorTitle, BodyPart bodyPart) throws MessagingException {
        try {
            String encodedFilename = bodyPart.getFileName();
            if (encodedFilename != null) {
                String filename = MimeUtility.decodeText(encodedFilename);

                if (isExcelFile(filename)) {
                    Path filepath = fileHandler.saveExcelFile(vendorTitle, filename, bodyPart);
                    if (filepath != null) {
                        collectedFiles.get(vendorTitle).add(filepath.toString());
                    }
                }
            } else {
                log.debug("Skipping body part with no filename");
            }
        }
        catch (UnsupportedEncodingException e) {
            log.warn("Failed to decode filename", e);
        }
        catch (MessagingException e) {
            log.error("Error accessing body part", e);
            throw e;
        }
        catch (Exception e) {
            log.error("Unexpected error in processBodyPartForExcel", e);
        }
    }

    private static boolean isExcelFile(String filename) {
        return EXCEL_EXTENSIONS.stream()
                .anyMatch(filename::endsWith);
    }

}