package io.github.bagdad.models.emailhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VendorWithMaxFileDateTime {

    private Long id;

    private String title;

    private OffsetDateTime maxDateTime = OffsetDateTime.MIN;

    public VendorWithMaxFileDateTime(String title) {
        this.title = title;
    }

}
