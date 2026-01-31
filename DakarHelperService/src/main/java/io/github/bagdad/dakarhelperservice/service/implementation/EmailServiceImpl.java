package io.github.bagdad.dakarhelperservice.service.implementation;

import emailhandler.EmailConfig;
import emailhandler.EmailHandler;
import io.github.bagdad.dakarhelperservice.helper.EmailHelper;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.dakarhelperservice.service.interfaces.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailConfig emailConfig;

    private final VendorServiceImpl vendorService;
    private final VendorFileServiceImpl vendorFileService;

    public EmailServiceImpl(EmailConfig emailConfig, VendorServiceImpl vendorService, VendorFileServiceImpl vendorFileService) {
        this.emailConfig = emailConfig;
        this.vendorService = vendorService;
        this.vendorFileService = vendorFileService;
    }

    public void synchronizeExcelFiles() {
        List<Vendor> vendors = vendorService.findAll();

        List<String> vendorTitles = vendors.stream()
                .map(Vendor::getTitle)
                .toList();

        if (vendorTitles.isEmpty()) {
            return;
        }

        EmailHandler emailHandler = new EmailHandler(emailConfig, System.getenv("SAVE_DIR"), vendorTitles);

        emailHandler.run();

        List<VendorFile> vendorFiles = EmailHelper.mapVendorFilesAndVendors(emailHandler, vendors);

        vendorFileService.batchInsert(vendorFiles);
    }

}
