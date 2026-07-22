package io.github.bagdad.models.excelparser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class HeaderCellDto {

    private String originalName;

    private Category category;

    private String subcategoryName;

    private Boolean isProcessing;

}
