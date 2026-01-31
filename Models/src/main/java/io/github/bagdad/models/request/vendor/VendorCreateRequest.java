package io.github.bagdad.models.request.vendor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendorCreateRequest {

    @NotBlank(message = "Vendor title must be not empty")
    private String title;

}
