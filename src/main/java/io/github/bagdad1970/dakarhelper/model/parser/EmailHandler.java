package io.github.bagdad1970.dakarhelper.model.parser;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import jakarta.mail.*;
import jakarta.mail.internet.MimeUtility;

import io.github.bagdad1970.dakarhelper.config.Config;
import jakarta.mail.search.FromStringTerm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EmailHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Properties props = new Properties() {{
        put("mail.store.protocol", "imaps");
        put("mail.host", "imap.mail.ru");
    }};

    private Map<String, Path> companyFolders = new HashMap<>();
    private final List<Company> companies;
    private final String rootFolder;
    private Folder emailFolder;

    public EmailHandler(String rootFolder, List<Company> companies) {
        this.rootFolder = rootFolder;
        this.companies = companies;

        initConnection();
    }

    private void initConnection() {
        LOGGER.info("connecting to email");
        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore();
            store.connect(Config.getProperty("login"),
                    Config.getProperty("password"));

            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
        }
        catch (MessagingException exception) {
            LOGGER.error("messing failed", exception);
            System.out.println(exception.getMessage());
        }
    }

    private void initFolders() {
        LOGGER.info("initializing folders");

        File directory = new File(rootFolder);

        if ( !directory.exists() )
            directory.mkdir();

        createSubjectFolders();
    }

    private void createSubjectFolders() {
        for (Company company : companies) {
            String companyName = company.getName();

            Path filepath = Paths.get(rootFolder, companyName);
            File subjectFolder = filepath.toFile();
            if ( !subjectFolder.exists() ) {
                subjectFolder.mkdir();
            }

            companyFolders.put(companyName, filepath);
        }
    }

    public void readEmail() {
        LOGGER.info("reading email");

        initFolders();

        try {
            Message[] messages = emailFolder.search(new FromStringTerm("Отдел продаж Краснодар <sales23@dakar61.ru>"));

            for (Message message : messages) {
                try {
                    if (message.getContent() instanceof Multipart multipart) {
                        Path companyFolder = getCompanyFolder(multipart);

                        saveFiles(companyFolder, multipart);
                    }
                }
                catch (IOException exception) {
                    LOGGER.error("input/output failed", exception);
                }
                catch (Exception exception) {
                    LOGGER.error("failed", exception);
                }
            }
        }
        catch (MessagingException exception) {
            LOGGER.error("messaging failed", exception);
        }
    }

    private Path getCompanyFolder(Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String contentType = bodyPart.getContentType();

                if (contentType.contains("multipart/alternative")) {
                    Multipart nestedMultipart = (Multipart) bodyPart.getContent();
                    for (int j = 0; j < nestedMultipart.getCount(); j++) {
                        BodyPart nestedPart = nestedMultipart.getBodyPart(j);
                        String nestedContentType = nestedPart.getContentType();

                        if (nestedContentType.contains("text/plain")) {
                            String content = nestedPart.getContent().toString();

                            for (String key : companyFolders.keySet()) {
                                if (content.contains(key))
                                    return companyFolders.get(key);
                            }
                        }
                    }
                }
            }
        }
        catch (MessagingException exception) {
            LOGGER.error("messaging failed", exception);
        }
        catch (IOException exception) {
            LOGGER.error("input/output failed", exception);
        }
        catch (Exception exception) {
            LOGGER.error("failed", exception);
        }

        return null;
    }

    private static void saveFiles(Path subjectFolder, Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String filename = bodyPart.getFileName();

                if (filename != null) {
                    String decodedFilename = MimeUtility.decodeText(filename);

                    if (decodedFilename.endsWith(".xls") || decodedFilename.endsWith(".xlsx")) {
                        Path filepath = Paths.get(subjectFolder.toString(), decodedFilename);
                        saveFile(bodyPart, filepath);
                    }
                }
            }
        }
        catch (MessagingException exception) {
            LOGGER.error("messaging failed", exception);
        }
        catch (UnsupportedEncodingException exception) {
            LOGGER.error("encoding failed", exception);
        }
    }

    private static void saveFile(BodyPart bodyPart, Path filepath) {
        try (InputStream inputStream = bodyPart.getInputStream();
             FileOutputStream fileOutput = new FileOutputStream(filepath.toFile()))
        {
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) != -1) {
                fileOutput.write(buf, 0, bytesRead);
            }
        }
        catch (IOException exception) {
            LOGGER.error("input/output failed", exception);
        }
        catch (Exception exception) {
            LOGGER.error("failed", exception);
        }
    }

}
