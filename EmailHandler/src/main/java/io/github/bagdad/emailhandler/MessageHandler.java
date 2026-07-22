package io.github.bagdad.emailhandler;

import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import io.github.bagdad.findhandler.FileHandler;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class MessageHandler {

    private final Map<String, Boolean> vendorVisits;

    private final List<VendorWithMaxFileDateTime> vendors;

    @Getter
    private final Map<String, List<String>> vendorFilepathes;

    public MessageHandler(List<VendorWithMaxFileDateTime> vendors) {
        this.vendors = vendors;
        this.vendorFilepathes = new HashMap<>();
        this.vendorVisits = new HashMap<>();
        initVendorVisits();
    }

    private void initVendorVisits() {
        for (VendorWithMaxFileDateTime vendor : vendors) {
            vendorVisits.put(vendor.getTitle(), false);
        }
    }

    public void processMessages(Message[] messages) {
        log.info("Processing {} messages", messages.length);

        if (messages.length == 0) {
            return;
        }

        List<Message> reversedMessages = Arrays.asList(messages);
        Collections.reverse(reversedMessages);

        for (int i = 0; i < reversedMessages.size() && hasUnvisitedCompanies(); i++) {
            processMessage(reversedMessages.get(i));
        }
    }

    private boolean hasUnvisitedCompanies() {
        return vendorVisits.containsValue(false);
    }

    private void processMessage(Message message) {
        log.info("Processing message");

        try {
            String subject = message.getSubject();
            VendorWithMaxFileDateTime vendor = findVendorInText(subject);

            if (!vendor.getTitle().isEmpty()) {
                OffsetDateTime sentDateTime = MessageHandlerUtils.getSentOffsetDateTime(message);

                if (vendor.getMaxDateTime().isBefore(sentDateTime)) {
                    if (message.getContent() instanceof Multipart multipart) {
                        processMultipartInMessage(vendor, multipart);
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
    }

    private void processMultipartInMessage(VendorWithMaxFileDateTime vendor, Multipart multipart) {
        String vendorTitle = vendor.getTitle();

        if (vendorTitle.isEmpty()) {
            VendorWithMaxFileDateTime vendorFromBody = findVendorInMessageBody(multipart);
            vendorTitle = vendorFromBody.getTitle();
        }

        if (!vendorTitle.isEmpty() && !vendorVisits.get(vendorTitle)) {
            log.info("Processing first message for vendor: {}", vendorTitle);
            saveExcelFiles(vendorTitle, multipart);
            vendorVisits.put(vendorTitle, true);
        }
    }

    private VendorWithMaxFileDateTime findVendorInMessageBody(Multipart multipart) {
        log.info("Finding vendor title in message body");

        BodyPart textBodyPart = MessageHandlerUtils.extractTextBodyPart(multipart);

        if (textBodyPart == null) {
            return new VendorWithMaxFileDateTime("");
        }

        try {
            String textContent = textBodyPart.getContent().toString();
            return findVendorInText(textContent);
        }
        catch (IOException | MessagingException e) {
            log.error("Error during multipart processing", e);
        }

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

    private void processExcelBodyPart(String vendorTitle, BodyPart bodyPart) {
        try {
            String encodedFilename = bodyPart.getFileName();
            if (encodedFilename != null) {
                String filename = MimeUtility.decodeText(encodedFilename);

                if (MessageHandlerUtils.isExcelFile(filename)) {
                    Path filepath = FileHandler.saveVendorFile(vendorTitle, filename, bodyPart.getInputStream());
                    vendorFilepathes.computeIfAbsent(vendorTitle, _ -> new ArrayList<>()).add(filepath.toString());
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            log.error("Failed to decode filename", e);
        }
        catch (MessagingException | IOException e) {
            log.error("Error accessing body part", e);
        }
    }

    private VendorWithMaxFileDateTime findVendorInText(String text) {
        log.info("Finding vendor title in text");

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