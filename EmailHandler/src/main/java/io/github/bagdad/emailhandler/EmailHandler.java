package io.github.bagdad.emailhandler;

import io.github.bagdad.models.emailhandler.VendorWithFilepathes;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import jakarta.mail.*;
import jakarta.mail.search.FromStringTerm;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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

            FromStringTerm term = new FromStringTerm(config.getFromTerm());

            Message[] messages = folder.search(term);

            if (messages != null && messages.length > 0) {
                FileHandler fileHandler = new FileHandler(config.getSaveDir());

                MessageHandler messageHandler = new MessageHandler(vendors, fileHandler);
                messageHandler.processMessages(messages);

                for (VendorWithMaxFileDateTime vendor : vendors) {
                    List<String> paths = messageHandler.getVendorFiles().get(vendor.getTitle());
                    VendorWithFilepathes vendorWithFilepathes = new VendorWithFilepathes(vendor.getId(), vendor.getTitle(), paths);
                    vendorsWithFilepathes.add(vendorWithFilepathes);
                }

                log.info("Final vendor filepaths: {}", vendorsWithFilepathes);
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
            log.error("Unexpected error in run()", e);
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
        else {
            log.debug("Folder was not open");
        }
    }

}