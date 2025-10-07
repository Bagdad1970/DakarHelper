package io.github.bagdad1970.dakarhelper.model.email;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.config.Config;
import jakarta.mail.*;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.FromStringTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EmailHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailHandler.class);

    private static final String PROTOCOL = "imaps";
    private static final String HOST = "imap.mail.ru";
    private static final String FOLDER_NAME = "INBOX";
    private static final String FROM_TERM = "Отдел продаж Краснодар <sales23@dakar61.ru>";
    private static final Set<String> EXCEL_EXTENSIONS = Set.of(".xls", ".xlsx");

    private Map<String, Path> companyDirs;
    private Map<String, Boolean> companyVisits;
    private final List<Company> companies;
    private Folder emailFolder;

    public EmailHandler(List<Company> companies) {
        this.companies = companies;
        this.companyDirs = new HashMap<>();
        this.companyVisits = new HashMap<>();
        initialize();
    }

    private void initialize() {
        initCompanyVisits();
        createCompanyDirs();
        initConnection();
    }

    private void initCompanyVisits() {
        for (Company company : companies) {
            String companyName = company.getName();
            companyVisits.put(companyName, false);
        }
    }

    public Map<String, Path> getCompanyDirs() {
        return companyDirs;
    }

    private void createCompanyDirs() {
        String baseDir = System.getProperty("company.dir");
        if (baseDir == null || baseDir.trim().isEmpty()) {
            throw new IllegalStateException("System property 'company.dir' is not set");
        }

        for (Company company : companies) {
            String companyName = company.getName();
            Path companyPath = Paths.get(baseDir, companyName);

            try {
                Files.createDirectories(companyPath);
                companyDirs.put(companyName, companyPath);
                LOGGER.debug("Created directory for company: {}", companyName);
            }
            catch (IOException e) {
                LOGGER.error("Failed to create directory for company: {}", companyName, e);
                throw new RuntimeException("Failed to create company directories", e);
            }
        }
    }

    private void initConnection() {
        LOGGER.info("Сonnecting to email");

        Properties props = new Properties();
        props.put("mail.store.protocol", PROTOCOL);
        props.put("mail.host", HOST);

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore();
            store.connect(Config.getProperty("login"), Config.getProperty("password"));

            emailFolder = store.getFolder(FOLDER_NAME);
            emailFolder.open(Folder.READ_ONLY);
            LOGGER.info("Successfully connected to email");
        }
        catch (MessagingException e) {
            LOGGER.error("Failed to connect to email", e);
            throw new RuntimeException("Email connection failed", e);
        }
    }

    public void readEmail() {
        LOGGER.info("Reading email messages");

        if (emailFolder == null) {
            LOGGER.error("Email folder is not initialized");
            return;
        }

        try {
            Message[] messages = emailFolder.search(new FromStringTerm(FROM_TERM));
            processMessages(messages);
        }
        catch (MessagingException e) {
            LOGGER.error("Failed to search for messages", e);
        }
        finally {
            close();
        }
    }

    private void processMessages(Message[] messages) {
        List<Message> reversedMessages = Arrays.asList(messages);
        Collections.reverse(reversedMessages);

        int messageIndex = 0;
        while (hasUnvisitedCompanies() && messageIndex < reversedMessages.size()) {
            Message message = reversedMessages.get(messageIndex);
            processSingleMessage(message);
            messageIndex++;
        }
    }

    private void processSingleMessage(Message message) {
        try {
            String subject = message.getSubject();
            String companyName = findCompanyNameInText(subject);

            if (message.getContent() instanceof Multipart multipart) {
                processMultipartMessage(multipart, companyName);
            }
        } catch (IOException | MessagingException e) {
            LOGGER.error("Failed to process message", e);
        }
    }

    private void processMultipartMessage(Multipart multipart, String companyName) {
        try {
            if (companyName.isEmpty()) {
                companyName = findCompanyNameInBody(multipart);
            }

            if ( !companyName.isEmpty() && !companyVisits.get(companyName) ) {
                Path companyFolder = companyDirs.get(companyName);
                saveExcelFiles(companyFolder, multipart);
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
        return companyDirs.keySet().stream()
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

    private void saveExcelFiles(Path companyDir, Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                processBodyPartForExcel(companyDir, bodyPart);
            }
        } catch (MessagingException e) {
            LOGGER.error("Failed to process multipart for Excel files", e);
        }
    }

    private void processBodyPartForExcel(Path companyDir, BodyPart bodyPart) throws MessagingException {
        try {
            String filename = bodyPart.getFileName();
            if (filename != null && isExcelFile(filename)) {
                String decodedFilename = MimeUtility.decodeText(filename);
                Path filepath = companyDir.resolve(decodedFilename);
                saveFile(bodyPart, filepath);
                LOGGER.debug("Saved Excel file: {}", decodedFilename);
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

    private void saveFile(BodyPart bodyPart, Path filepath) {
        try (InputStream inputStream = bodyPart.getInputStream();
             FileOutputStream fileOutput = new FileOutputStream(filepath.toFile())) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException | MessagingException e) {
            LOGGER.error("Failed to save file: {}", filepath, e);
            throw new RuntimeException("File save failed", e);
        }
    }

    private boolean hasUnvisitedCompanies() {
        return companyVisits.containsValue(false);
    }

    public void close() {
        if (emailFolder != null && emailFolder.isOpen()) {
            try {
                emailFolder.close(false);
                emailFolder.getStore().close();
                LOGGER.info("Email connection closed");
            }
            catch (MessagingException e) {
                LOGGER.error("Failed to close email connection", e);
            }
        }
    }

}