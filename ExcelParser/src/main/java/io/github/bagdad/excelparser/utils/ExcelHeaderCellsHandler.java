package io.github.bagdad.excelparser.utils;

import io.github.bagdad.excelparser.headerparser.CellFindStatus;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
public class ExcelHeaderCellsHandler {

    private final List<HeaderCellDto> headerCellDtos;

    public ExcelHeaderCellsHandler(List<HeaderCellDto> headerCellDtos) {
        this.headerCellDtos = headerCellDtos;
    }

    public CellFindStatus findHeaderCellFindStatus(String cellValue) {
        if (cellValue.isEmpty()) {
            return CellFindStatus.ABSENTS;
        }

        for (HeaderCellDto dto : headerCellDtos) {
            String originName = dto.getOriginalName();

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

    public Category findHeaderCellCategory(String cellValue) {
        if (cellValue.isEmpty()) {
            return null;
        }

        for (HeaderCellDto dto : headerCellDtos) {
            if (cellValue.startsWith(dto.getOriginalName())) {
                return dto.getCategory();
            }
        }
        return null;
    }

    public void add(HeaderCellDto newHeaderCellDto) {
        headerCellDtos.add(newHeaderCellDto);
    }

    public void addAll(List<HeaderCellDto> newHeaderCellDtos) {
        headerCellDtos.addAll(newHeaderCellDtos);
    }

}
