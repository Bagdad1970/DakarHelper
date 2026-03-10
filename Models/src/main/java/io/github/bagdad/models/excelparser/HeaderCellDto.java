package io.github.bagdad.models.excelparser;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HeaderCellDto {

    private String originalName;

    private Category category;

    private String subcategoryName;

    private Boolean isProcessing;

}
