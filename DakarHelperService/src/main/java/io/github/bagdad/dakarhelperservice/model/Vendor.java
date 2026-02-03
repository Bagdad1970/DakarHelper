package io.github.bagdad.dakarhelperservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class Vendor {

    @Id
    private Long id;

    private String title;

}
