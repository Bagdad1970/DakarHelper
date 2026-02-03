package io.github.bagdad.dakarhelperservice.model;

import io.github.bagdad.models.excelparser.Category;
import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class Subcategory {

    @Id
    private Long id;

    private Category category;

    private String name;

}
