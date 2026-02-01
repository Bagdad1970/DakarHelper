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

    private String normalizedName;

    private CellStatus cellStatus;

    public HeaderCellDto(String originalName, Category category, String subcategoryName, CellStatus cellStatus) {
        this.originalName = originalName;
        this.category = category;
        this.subcategoryName = subcategoryName;
        this.normalizedName = originalName;
        this.cellStatus = cellStatus;
    }

}
