package io.github.bagdad.models.emailhandler;

import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class VendorWithMaxFileDateTime {

    private Long id;

    private String title;

    private OffsetDateTime maxDateTime = OffsetDateTime.MIN;

    public VendorWithMaxFileDateTime(String title) {
        this.title = title;
    }

}
