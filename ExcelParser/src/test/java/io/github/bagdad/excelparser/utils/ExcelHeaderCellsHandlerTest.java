package io.github.bagdad.excelparser.utils;

import io.github.bagdad.excelparser.headerparser.CellFindStatus;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import io.github.bagdad.models.excelparser.ExcelHeaderCellDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelHeaderCellsHandlerTest {

    private static ExcelHeaderCellsHandler excelHeaderCellsHandler;

    @BeforeAll
    static void setup() {
        List<ExcelHeaderCellDto> excelHeaderCellDtos = new ArrayList<>();
        excelHeaderCellDtos.add(new ExcelHeaderCellDto("опт", Category.PRICE, "", "", CellStatus.PROCESSED));
        excelHeaderCellDtos.add(new ExcelHeaderCellDto("розни", Category.PRICE, "", "", CellStatus.IGNORED));

        excelHeaderCellsHandler = new ExcelHeaderCellsHandler(excelHeaderCellDtos);
    }

    @Test
    void getHeaderCellFindStatus1() {
        String cellValue = "оптовый";

        CellFindStatus res = excelHeaderCellsHandler.getHeaderCellFindStatus(cellValue);

        assertEquals(CellFindStatus.STARTS, res);
    }

    @Test
    void getHeaderCellFindStatus2() {
        String cellValue = "к опт";

        CellFindStatus res = excelHeaderCellsHandler.getHeaderCellFindStatus(cellValue);

        assertEquals(CellFindStatus.CONTAINS, res);
    }

    @Test
    void getHeaderCellFindStatus3() {
        String cellValue = "розница";

        CellFindStatus res = excelHeaderCellsHandler.getHeaderCellFindStatus(cellValue);

        assertEquals(CellFindStatus.ABSENTS, res);
    }

}
