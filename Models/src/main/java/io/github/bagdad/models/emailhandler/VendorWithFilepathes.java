package io.github.bagdad.models.emailhandler;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class VendorWithFilepathes {

    private Long id;

    private String title;

    private List<String> filepathes;

}
