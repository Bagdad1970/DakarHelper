package emailhandler;

import jakarta.mail.*;
import jakarta.mail.search.FromStringTerm;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class EmailHandler {

    private EmailConfig config;

    private final String saveDir;
    private final List<String> vendors;
    private Folder folder;

    private final Map<String, List<String>> vendorFilepaths;

    public EmailHandler(EmailConfig config, String saveDir, List<String> vendors) {
        this.config = config;
        this.saveDir = saveDir;
        this.vendors = vendors;
        vendorFilepaths = new HashMap<>();
    }

    public Map<String, List<String>> getVendorFilepaths() {
        return new HashMap<>(vendorFilepaths);
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

    public void run() {
        initConnection();

        try {
            FromStringTerm term = new FromStringTerm(config.getFromTerm());
            Message[] messages = folder.search(term);
            log.info("Found {} messages from '{}'", messages != null ? messages.length : 0, config.getFromTerm());

            if (messages != null && messages.length > 0) {
                FileHandler fileHandler = new FileHandler(vendors, saveDir);

                MessageHandler messageHandler = new MessageHandler(vendors, fileHandler);
                messageHandler.processMessages(messages);

                vendorFilepaths.clear();
                for (String vendor : vendors) {
                    List<String> paths = messageHandler.getCollectedFiles().get(vendor);
                    vendorFilepaths.put(vendor, paths);
                }

                log.info("Final vendorFilepaths: {}", vendorFilepaths);
            }
            else {
                log.info("No messages to process");
            }

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