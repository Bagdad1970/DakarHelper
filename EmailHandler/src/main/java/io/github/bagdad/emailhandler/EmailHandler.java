package io.github.bagdad.emailhandler;

import io.github.bagdad.models.emailhandler.VendorWithFilepathes;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import io.github.bagdad.findhandler.FileHandler;
import jakarta.mail.*;
import jakarta.mail.search.FromStringTerm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class EmailHandler {

    private final EmailConfig config;

    private final List<VendorWithMaxFileDateTime> vendors;

    private Folder folder;

    public EmailHandler(EmailConfig config, List<VendorWithMaxFileDateTime> vendors) {
        this.config = config;
        this.vendors = vendors;
    }

    private void initConnection() {
        Properties props = new Properties();
        props.put("mail.store.protocol", config.getProtocol());
        props.put("mail.host", config.getHost());

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore();
            store.connect(config.getLogin(), config.getPassword());
            log.info("Connected to IMAP store at {}", config.getHost());

            folder = store.getFolder(config.getFolderName());
            folder.open(Folder.READ_ONLY);
            log.info("Folder '{}' opened in READ_ONLY mode", config.getFolderName());
        }
        catch (MessagingException e) {
            log.error("Email connection failed", e);
            throw new RuntimeException("Email connection failed", e);
        }
        catch (Exception e) {
            log.error("Unexpected error during IMAP connection init", e);
        }
    }

    public List<VendorWithFilepathes> readEmail() {
        initConnection();

        try {
            List<VendorWithFilepathes> vendorsWithFilepathes = new ArrayList<>();

            Message[] messages = folder.search(new FromStringTerm(config.getFromTerm()));

            if (messages.length > 0) {
                FileHandler fileHandler = new FileHandler(config.getSaveDir());

                MessageHandler messageHandler = new MessageHandler(vendors, fileHandler);
                messageHandler.processMessages(messages);

                for (VendorWithMaxFileDateTime vendor : vendors) {
                    List<String> paths = messageHandler.getVendorFiles().get(vendor.getTitle());

                    if (!paths.isEmpty()) {
                        VendorWithFilepathes vendorWithFilepathes = VendorWithFilepathes.builder()
                                .id(vendor.getId())
                                .title(vendor.getTitle())
                                .filepathes(paths)
                                .build();
                        vendorsWithFilepathes.add(vendorWithFilepathes);
                    }
                }

                log.info("Final vendor filepathes: {}", vendorsWithFilepathes);
            }
            else {
                log.info("No messages to process");
            }

            return vendorsWithFilepathes;

        }
        catch (MessagingException e) {
            log.error("Error while searching/fetching messages", e);
        }
        catch (Exception e) {
            log.error("Unexpected error: ", e);
        }
        finally {
            close();
        }

        return Collections.emptyList();
    }

    public void close() {
        if (folder != null && folder.isOpen()) {
            try {
                folder.close(false);
                folder.getStore().close();
                log.info("IMAP folder and store closed successfully");
            }
            catch (MessagingException e) {
                log.error("Error closing folder/store", e);
            }
            catch (Exception e) {
                log.error("Unexpected error during close()", e);
            }
        }
    }

}