package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.models.emailhandler.VendorWithFilepathes;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmailHelper {

    public static List<VendorFile>  mapVendorFilesAndVendors(
            List<VendorWithFilepathes> vendorFilepathes,
            List<VendorWithMaxFileDateTime> vendors
    ) {
        if (vendorFilepathes == null || vendorFilepathes.isEmpty() ||
                vendors == null || vendors.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<String>> filepathesByTitle = vendorFilepathes.stream()
                .filter(v -> v.getTitle() != null && v.getFilepathes() != null)
                .collect(Collectors.toMap(
                        VendorWithFilepathes::getTitle,
                        VendorWithFilepathes::getFilepathes,
                        (existing, replacement) -> existing
                ));

        List<VendorFile> vendorFiles = new ArrayList<>();
        for (VendorWithMaxFileDateTime vendor : vendors) {
            if (vendor.getId() == null || vendor.getTitle() == null) {
                continue;
            }

            List<String> filepathes = filepathesByTitle.get(vendor.getTitle());
            if (filepathes == null || filepathes.isEmpty()) {
                continue;
            }

            for (String filepath : filepathes) {
                if (filepath == null || filepath.trim().isEmpty()) {
                    continue;
                }

                VendorFile vendorFile = VendorFile.builder()
                        .vendorId(vendor.getId())
                        .filepath(filepath.trim())
                        .build();

                vendorFiles.add(vendorFile);
            }
        }

        return vendorFiles;
    }

}
