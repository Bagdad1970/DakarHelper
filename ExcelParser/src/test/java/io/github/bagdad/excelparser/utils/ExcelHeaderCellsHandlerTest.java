package io.github.bagdad.excelparser.headerparser.utils;

import io.github.bagdad.excelparser.headerparser.model.CellFindStatus;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExcelHeaderCellsHandlerTest {

    private static ExcelHeaderCellsHandler excelHeaderCellsHandler;

    @BeforeAll
    static void setup() {
        List<HeaderCellDto> headerCellDtos = new ArrayList<>();
        headerCellDtos.add(new HeaderCellDto("starts", Category.PRICE, "", true));
        headerCellDtos.add(new HeaderCellDto("contains", Category.PRICE, "", true));
        headerCellDtos.add(new HeaderCellDto("ignore starts", Category.PRICE, "", false));
        headerCellDtos.add(new HeaderCellDto("__ignore contains", Category.PRICE, "", false));
        headerCellDtos.add(new HeaderCellDto("present cell value", Category.NAME, "", true));

        excelHeaderCellsHandler = new ExcelHeaderCellsHandler(headerCellDtos);
    }

    @Test
    void Cell_value_that_starts_with_processed_header_must_return_STARTS_status() {
        String cellValue = "startswith";

        CellFindStatus res = excelHeaderCellsHandler.findHeaderCellFindStatus(cellValue);

        assertThat(res).isEqualTo(CellFindStatus.STARTS);
    }

    @Test
    void Cell_value_that_contains_processed_header_must_return_CONTAINS_status() {
        String cellValue = "some contains";

        CellFindStatus res = excelHeaderCellsHandler.findHeaderCellFindStatus(cellValue);

        assertThat(res).isEqualTo(CellFindStatus.CONTAINS);
    }

//    @Test
//    void Cell_value_that_starts_with_ignored_header_must_return_ABSENTS_status() {
//        String cellValue = "ignore_starts";
//
//        CellFindStatus res = excelHeaderCellsHandler.findHeaderCellFindStatus(cellValue);
//
//        assertThat(res).isEqualTo(CellFindStatus.ABSENTS);
//    }
//
//    @Test
//    void Cell_value_that_contains_ignored_header_must_return_ABSENTS_status() {
//        String cellValue = "ignore_contains";
//
//        CellFindStatus res = excelHeaderCellsHandler.findHeaderCellFindStatus(cellValue);
//
//        assertThat(res).isEqualTo(CellFindStatus.ABSENTS);
//    }

    @Test
    void Cell_value_that_does_not_match_any_header_must_return_ABSENTS_status() {
        String cellValue = "absents";

        CellFindStatus res = excelHeaderCellsHandler.findHeaderCellFindStatus(cellValue);

        assertThat(res).isEqualTo(CellFindStatus.ABSENTS);
    }

    @Test
    void Finding_category_with_empty_cell_value_must_return_null() {
        String cellValue = "";

        Category category = excelHeaderCellsHandler.findHeaderCellCategory(cellValue);

        assertThat(category).isNull();
    }

    @Test
    void Finding_category_with_present_cell_value_must_return_its_category() {
        String cellValue = "present cell value";

        Category category = excelHeaderCellsHandler.findHeaderCellCategory(cellValue);

        assertThat(category).isEqualTo(Category.NAME);
    }

    @Test
    void Finding_category_with_missing_cell_value_must_return_null() {
        String cellValue = "missing cell value";

        Category category = excelHeaderCellsHandler.findHeaderCellCategory(cellValue);

        assertThat(category).isNull();
    }

}
