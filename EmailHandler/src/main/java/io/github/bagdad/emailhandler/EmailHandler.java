package io.github.bagdad.emailhandler;

import io.github.bagdad.common.ConfigManager;
import io.github.bagdad.emailhandler.config.EmailConfig;
import io.github.bagdad.models.emailhandler.VendorWithFilepathes;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import jakarta.mail.*;
import jakarta.mail.search.FromStringTerm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class EmailHandler {

    private final List<VendorWithMaxFileDateTime> vendors;

    private Folder folder;

    static {
        ConfigManager.loadConfig("EmailHandler/src/main/resources/config/email.json", EmailConfig.class);
    }

    public EmailHandler(List<VendorWithMaxFileDateTime> vendors) {
        this.vendors = vendors;
    }

    private void connectToEmailFolder() {
        log.info("Connecting to email");

        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", ConfigManager.getConfig(EmailConfig.class).getProtocol());
            props.put("mail.host", ConfigManager.getConfig(EmailConfig.class).getHost());

            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore();
            store.connect(
                    ConfigManager.getConfig(EmailConfig.class).getLogin(),
                    ConfigManager.getConfig(EmailConfig.class).getPassword()
            );

            folder = store.getFolder(ConfigManager.getConfig(EmailConfig.class).getFolderName());
            folder.open(Folder.READ_ONLY);
        }
        catch (MessagingException e) {
            log.error("Email connection failed", e);
            throw new RuntimeException("Email connection failed", e);
        }
    }

    public List<VendorWithFilepathes> readEmail() {
        log.info("Reading email");

        connectToEmailFolder();

        List<VendorWithFilepathes> vendorsWithFilepathes = new ArrayList<>();

        try {
            Message[] messages = folder.search(new FromStringTerm(ConfigManager.getConfig(EmailConfig.class).getFromTerm()));

            MessageHandler messageHandler = new MessageHandler(vendors);
            messageHandler.processMessages(messages);

            if (messageHandler.getVendorFilepathes().isEmpty()) {
                return Collections.emptyList();
            }

            for (VendorWithMaxFileDateTime vendor : vendors) {
                List<String> paths = messageHandler.getVendorFilepathes().get(vendor.getTitle());

                if (!paths.isEmpty()) {
                    VendorWithFilepathes vendorWithFilepathes = VendorWithFilepathes.builder()
                            .id(vendor.getId())
                            .title(vendor.getTitle())
                            .filepathes(paths)
                            .build();
                    vendorsWithFilepathes.add(vendorWithFilepathes);
                }
            }

        }
        catch (MessagingException e) {
            log.error("Error while searching/fetching messages", e);
        }
        finally {
            close();
        }

        return vendorsWithFilepathes;
    }

    public void close() {
        closeFolder();
        closeStore();
    }

    private void closeFolder() {
        log.info("Closing folder");

        if (folder != null && folder.isOpen()) {
            try {
                folder.close(false);
            }
            catch (MessagingException e) {
                log.error("Error when closing folder", e);
                throw new RuntimeException("Folder is not closed");
            }
        }
    }

    private void closeStore() {
        log.info("Closing folder and store");

        if (folder != null && folder.isOpen()) {
            try {
                folder.getStore().close();
            }
            catch (MessagingException e) {
                log.error("Error closing store", e);
                throw new RuntimeException("Store is not closed");
            }
        }
    }

}