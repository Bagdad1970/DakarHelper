package io.github.bagdad1970.dakarhelper.model.email;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private static final Set<String> EXCEL_EXTENSIONS = Set.of(".xls", ".xlsx");
    private Map<String, Boolean> companyVisits;
    private List<Company> companies;
    private FileHandler fileHandler;

    public MessageHandler(List<Company> companies, FileHandler fileHandler) {
        this.companies = companies;
        this.fileHandler = fileHandler;
        this.companyVisits = new HashMap<>();

        initCompanyVisits();
    }

    private void initCompanyVisits() {
        for (Company company : companies) {
            String companyName = company.getName();
            companyVisits.put(companyName, false);
        }
    }

    public void processMessages(Message[] messages) {
        List<Message> reversedMessages = Arrays.asList(messages);
        Collections.reverse(reversedMessages);

        int messageIndex = 0;
        while (hasUnvisitedCompanies() && messageIndex < reversedMessages.size()) {
            Message message = reversedMessages.get(messageIndex);
            processSingleMessage(message);
            messageIndex++;
        }
    }

    private boolean hasUnvisitedCompanies() {
        return companyVisits.containsValue(false);
    }

    private void processSingleMessage(Message message) {
        try {
            String subject = message.getSubject();
            String companyName = findCompanyNameInText(subject);

            if (message.getContent() instanceof Multipart multipart) {
                processMultipartMessage(companyName, multipart);
            }
        } catch (IOException | MessagingException e) {
            LOGGER.error("Failed to process message", e);
        }
    }

    private void processMultipartMessage(String companyName, Multipart multipart) {
        try {
            if (companyName.isEmpty()) {
                companyName = findCompanyNameInBody(multipart);
            }

            if ( !companyName.isEmpty() && !companyVisits.get(companyName) ) {
                saveExcelFiles(companyName, multipart);
                companyVisits.put(companyName, true);
                LOGGER.info("Processed files for company: {}", companyName);
            }
        } catch (IOException | MessagingException e) {
            LOGGER.error("Failed to process multipart message", e);
        }
    }

    private String findCompanyNameInBody(Multipart multipart) throws IOException, MessagingException {
        BodyPart textBodyPart = extractTextBodyPart(multipart);
        if (textBodyPart != null) {
            String textContent = textBodyPart.getContent().toString();
            return findCompanyNameInText(textContent);
        }
        return "";
    }

    private String findCompanyNameInText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String lowerText = text.toLowerCase();
        return companyVisits.keySet().stream()
                .filter(companyName -> lowerText.contains(companyName.toLowerCase()))
                .findFirst()
                .orElse("");
    }

    private BodyPart extractTextBodyPart(Multipart multipart) throws IOException, MessagingException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);

            if (bodyPart.getContentType().contains("multipart/alternative")) {
                Multipart nestedMultipart = (Multipart) bodyPart.getContent();
                for (int j = 0; j < nestedMultipart.getCount(); j++) {
                    BodyPart nestedPart = nestedMultipart.getBodyPart(j);
                    if (nestedPart.getContentType().contains("text/plain")) {
                        return nestedPart;
                    }
                }
            }
        }
        return null;
    }

    private void saveExcelFiles(String companyName, Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                processBodyPartForExcel(companyName, bodyPart);
            }
        } catch (MessagingException e) {
            LOGGER.error("Failed to process multipart for Excel files", e);
        }
    }

    private void processBodyPartForExcel(String companyName, BodyPart bodyPart) throws MessagingException {
        try {
            String filename = bodyPart.getFileName();
            if (filename != null) {
                String decodedFilename = MimeUtility.decodeText(filename);

                if (isExcelFile(decodedFilename))
                    fileHandler.saveExcelFile(companyName, decodedFilename, bodyPart);
            }
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("Failed to decode filename: {}", bodyPart.getFileName(), e);
        }
        catch (MessagingException e) {
            LOGGER.error("Failed to get filename from body part", e);
        }
    }

    private boolean isExcelFile(String filename) {
        String lowerFilename = filename.toLowerCase();
        return EXCEL_EXTENSIONS.stream().anyMatch(lowerFilename::endsWith);
    }

}
