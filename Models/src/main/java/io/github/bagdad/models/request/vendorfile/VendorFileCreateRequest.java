package io.github.bagdad.models.request.vendorfile;

import lombok.Data;

@Data
public class VendorFileCreateRequest {

    private Long vendorId;

    private String filepath;

}
