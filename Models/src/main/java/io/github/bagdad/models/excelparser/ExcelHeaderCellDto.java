package io.github.bagdad.models.excelparser;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ExcelHeaderCellDto {

    private String originName;

    private Category category;

    private String subcategoryName;

    private String normalizedName;

    private CellStatus cellStatus;

    public ExcelHeaderCellDto(String originName, Category category, String subcategoryName, CellStatus cellStatus) {
        this.originName = originName;
        this.category = category;
        this.subcategoryName = subcategoryName;
        this.normalizedName = originName;
        this.cellStatus = cellStatus;
    }

}
