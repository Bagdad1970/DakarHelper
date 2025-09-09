package io.github.bagdad1970.dakarhelper.model.parser.excel;

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

    private Map<String, Path> companyDirs = new HashMap<>();
    private Map<String, Boolean> companyVisits = new HashMap<>();
    private final List<Company> companies;
    private final String rootDir;
    private Folder emailFolder;

    public EmailHandler(String rootDir, List<Company> companies) {
        this.rootDir = rootDir;
        this.companies = companies;

        initCompanyVisits();
        initDirectories();
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

    private void initDirectories() {
        LOGGER.info("initializing directories");

        File directory = new File(rootDir);

        if ( !directory.exists() )
            directory.mkdir();

        createCompanyDirs();
    }

    private void createCompanyDirs() {
        for (Company company : companies) {
            String companyName = company.getName();

            Path filepath = Paths.get(rootDir, companyName);
            File companiesDir = filepath.toFile();
            if ( !companiesDir.exists() ) {
                companiesDir.mkdir();
            }

            companyDirs.put(companyName, filepath);
        }
    }

    public void readEmail() {
        LOGGER.info("reading email");

        try {
            Message[] messages = emailFolder.search(new FromStringTerm("Отдел продаж Краснодар <sales23@dakar61.ru>"));

            List<Message> reverseMessages = Arrays.asList(messages);
            Collections.reverse(reverseMessages);

            for (Message message : reverseMessages) {
                try {
                    if (message.getContent() instanceof Multipart multipart) {
                        Map.Entry<String, Path> companyDir = getCompanyDir(multipart);

                        if ( companyDir != null) {
                            Path companyFolder = companyDir.getValue();
                            String companyName = companyDir.getKey();

                            if (companyVisits.get(companyName) == false) {
                                saveFiles(companyFolder, multipart);
                                companyVisits.put(companyName, true);
                            }
                        }
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

    private Map.Entry<String, Path> getCompanyDir(Multipart multipart) {
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

                            for (Map.Entry<String, Path> entry : companyDirs.entrySet()) {
                                if ( content.contains(entry.getKey()) )
                                    return entry;
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

    private static void saveFiles(Path companyDir, Multipart multipart) {
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);

                String filename = bodyPart.getFileName();
                if (filename != null) {
                    String decodedFilename = MimeUtility.decodeText(filename);

                    if (decodedFilename.endsWith(".xls") || decodedFilename.endsWith(".xlsx")) {
                        Path filepath = Paths.get(companyDir.toString(), decodedFilename);
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
