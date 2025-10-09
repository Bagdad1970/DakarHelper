package io.github.bagdad1970.dakarhelper.model.email;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.config.Config;
import jakarta.mail.*;
import jakarta.mail.search.FromStringTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class EmailHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailHandler.class);

    private static final String PROTOCOL = "imaps";
    private static final String HOST = "imap.mail.ru";
    private static final String FOLDER_NAME = "INBOX";
    private static final String FROM_TERM = "Отдел продаж Краснодар <sales23@dakar61.ru>";

    private List<Company> companies;
    private FileHandler fileHandler;
    private Folder emailFolder;

    public EmailHandler(List<Company> companies) {
        this.companies = companies;
        this.fileHandler = new FileHandler(companies);

        initConnection();
    }

    public Map<String, Path> getCompanyDirs() {
        return fileHandler.getCompanyDirs();
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

            if (messages != null) {
                MessageHandler messageHandler = new MessageHandler(companies, fileHandler);
                messageHandler.processMessages(messages);
            }
        }
        catch (MessagingException e) {
            LOGGER.error("Failed to search for messages", e);
        }
        finally {
            close();
        }
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