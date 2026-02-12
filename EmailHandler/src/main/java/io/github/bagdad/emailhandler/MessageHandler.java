package io.github.bagdad.emailhandler;

import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
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
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
public class MessageHandler {

    private Map<String, Boolean> vendorVisits;

    private final List<VendorWithMaxFileDateTime> vendors;

    private final FileHandler fileHandler;

    @Getter
    private final Map<String, List<String>> vendorFiles;

    public MessageHandler(List<VendorWithMaxFileDateTime> vendors, FileHandler fileHandler) {
        this.vendors = vendors;
        this.fileHandler = fileHandler;
        this.vendorFiles = new HashMap<>();
        initVendorVisits();
    }

    private void initVendorVisits() {
        this.vendorVisits = new HashMap<>();
        for (VendorWithMaxFileDateTime vendor : vendors) {
            vendorVisits.put(vendor.getTitle(), false);
        }
    }

    public void processMessages(Message[] messages) {
        List<Message> reversedMessages = Arrays.asList(messages);
        Collections.reverse(reversedMessages);
        log.info("Processing {} messages", reversedMessages.size());

        int messageIndex = 0;
        while (hasUnvisitedCompanies() && messageIndex < reversedMessages.size()) {
            Message message = reversedMessages.get(messageIndex);

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
            VendorWithMaxFileDateTime vendor = findVendorTitleInText(subject);

            OffsetDateTime sentDateTime = MessageHandlerUtils.getSentOffsetDateTime(message);
            if (!vendor.getTitle().isEmpty()) {
                if (vendor.getMaxDateTime().isBefore(sentDateTime)) {
                    if (message.getContent() instanceof Multipart multipart) {
                        processMultipartMessage(vendor.getTitle(), multipart);
                    }
                }
                else {
                    vendorVisits.remove(vendor.getTitle());
                }
            }
        }
        catch (IOException | MessagingException e) {
            log.error("Error extracting message content", e);
        }
        catch (Exception e) {
            log.error("Unexpected error: ", e);
        }
    }

    private void processMultipartMessage(String vendorTitle, Multipart multipart) {
        try {
            if (vendorTitle.isEmpty()) {
                VendorWithMaxFileDateTime vendor = findVendorTitleInBody(multipart);
                vendorTitle = vendor.getTitle();
            }

            if (!vendorTitle.isEmpty()) {
                if (!vendorVisits.get(vendorTitle)) {
                    log.info("Processing first message for vendor: {}", vendorTitle);
                    saveExcelFiles(vendorTitle, multipart);
                    vendorVisits.put(vendorTitle, true);
                }
                else {
                    log.debug("Vendor '{}' already processed — skipping", vendorTitle);
                }
            }
            else {
                log.debug("No matching vendor found in message — skipping");
            }
        }
        catch (IOException | MessagingException e) {
            log.error("Error during multipart processing", e);
        }
        catch (Exception e) {
            log.error("Unexpected error: ", e);
        }
    }

    private VendorWithMaxFileDateTime findVendorTitleInBody(Multipart multipart) throws IOException, MessagingException {
        BodyPart textBodyPart = MessageHandlerUtils.extractTextBodyPart(multipart);
        if (textBodyPart != null) {
            String textContent = textBodyPart.getContent().toString();
            return findVendorTitleInText(textContent);
        }
        log.debug("No text/plain part found in multipart");
        return new VendorWithMaxFileDateTime("");
    }

    private void saveExcelFiles(String vendorTitle, Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                processExcelBodyPart(vendorTitle, bodyPart);
            }
        }
        catch (MessagingException e) {
            log.error("Error iterating multipart parts", e);
        }
    }

    private void processExcelBodyPart(String vendorTitle, BodyPart bodyPart) throws MessagingException {
        try {
            String encodedFilename = bodyPart.getFileName();
            if (encodedFilename != null) {
                String filename = MimeUtility.decodeText(encodedFilename);

                if (MessageHandlerUtils.isExcelFile(filename)) {
                    Path filepath = fileHandler.saveExcelFile(vendorTitle, filename, bodyPart);
                    vendorFiles.computeIfAbsent(vendorTitle, _ -> new ArrayList<>()).add(filepath.toString());
                }
            }
            else {
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
            log.error("Unexpected error: ", e);
        }
    }

    VendorWithMaxFileDateTime findVendorTitleInText(String text) {
        if (text == null || text.isBlank()) {
            return new VendorWithMaxFileDateTime("");
        }

        String lowerText = text.toLowerCase();
        Optional<VendorWithMaxFileDateTime> match = vendors.stream()
                .filter(vendor -> lowerText.contains(vendor.getTitle().toLowerCase()))
                .findFirst();

        return match.orElse(new VendorWithMaxFileDateTime(""));
    }

}