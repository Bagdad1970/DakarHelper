package io.github.bagdad.excelparser.utils;

import io.github.bagdad.excelparser.headerparser.CellFindStatus;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import io.github.bagdad.models.excelparser.ExcelHeaderCellDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
public class ExcelHeaderCellsHandler {

    private final List<ExcelHeaderCellDto> excelHeaderCellDtos;

    public ExcelHeaderCellsHandler(List<ExcelHeaderCellDto> excelHeaderCellDtos) {
        this.excelHeaderCellDtos = excelHeaderCellDtos;
    }

    public CellFindStatus getHeaderCellFindStatus(String cellValue) {
        if (cellValue.isEmpty()) {
            return CellFindStatus.ABSENTS;
        }

        for (ExcelHeaderCellDto dto : excelHeaderCellDtos) {
            String originName = dto.getOriginName();

            if (cellValue.startsWith(originName)) {
                if (dto.getCellStatus() == CellStatus.IGNORED) {
                    return CellFindStatus.ABSENTS;
                }
                return CellFindStatus.STARTS;
            }
            if (cellValue.contains(originName)) {
                if (dto.getCellStatus() == CellStatus.IGNORED) {
                    return CellFindStatus.ABSENTS;
                }
                return CellFindStatus.CONTAINS;
            }
        }
        return CellFindStatus.ABSENTS;
    }

    public Category getHeaderCellCategory(String cellValue) {
        if (cellValue.isEmpty()) {
            return null;
        }

        for (ExcelHeaderCellDto dto : excelHeaderCellDtos) {
            if (cellValue.startsWith(dto.getOriginName())) {
                return dto.getCategory();
            }
        }
        return null;
    }

}
