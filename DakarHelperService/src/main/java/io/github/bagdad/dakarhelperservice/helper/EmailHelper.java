package io.github.bagdad.dakarhelperservice.helper;

import emailhandler.EmailHandler;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.model.VendorFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmailHelper {

    public static List<VendorFile> mapVendorFilesAndVendors(EmailHandler emailHandler, List<Vendor> vendors) {
        Map<String, List<String>> vendorFilepaths = emailHandler.getVendorFilepaths();

        if (vendorFilepaths.isEmpty()) {
            return new ArrayList<>();
        }

        List<VendorFile> vendorFiles = new ArrayList<>();

        for (Vendor vendor : vendors) {
            String vendorTitle = vendor.getTitle();

            if (vendorFilepaths.containsKey(vendorTitle)) {
                for (String filepath : vendorFilepaths.get(vendorTitle)) {
                    VendorFile vendorFile = new VendorFile();

                    vendorFile.setVendorId(vendor.getId());
                    vendorFile.setFilepath(filepath);

                    vendorFiles.add(vendorFile);
                }
            }
        }
        return vendorFiles;
    }

}
