package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.excelparser.headerparser.model.ExcelProduct;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelParserHelperTest {

    @Test
    void mapToExcelProducts_shouldMapCorrectly() {
        // arrange
        VendorFile vendorFile = new VendorFile();
        vendorFile.setId(1L);

        List<ExcelProduct> excelProducts = new ArrayList<>();
        ExcelProduct excelProduct1 = new ExcelProduct();
        excelProduct1.setName("Product 1");
        excelProduct1.addPrice("price", new BigDecimal("9.99"));
        excelProduct1.addQuantity("count1", 10);

        ExcelProduct excelProduct2 = new ExcelProduct();
        excelProduct2.setName("Product 2");
        excelProduct2.addPrice("wholesale", new BigDecimal("10.99"));
        excelProduct2.addQuantity("count1", 5);

        excelProducts.add(excelProduct1);
        excelProducts.add(excelProduct2);

        // act
        List<Product> result = ExcelParserHelper.mapToExcelProducts(vendorFile, excelProducts);

        // assert
        assertThat(result).isNotNull().hasSize(2);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.get(0).getVendorFileId()).isEqualTo(1L);
            softAssertions.assertThat(result.get(0).getName()).isEqualTo("Product 1");
            softAssertions.assertThat(result.get(0).getPrices()).isEqualTo(Map.of("price", new BigDecimal("9.99")));
            softAssertions.assertThat(result.get(0).getQuantities()).isEqualTo(Map.of("count1", 10));

            softAssertions.assertThat(result.get(1).getVendorFileId()).isEqualTo(1L);
            softAssertions.assertThat(result.get(1).getName()).isEqualTo("Product 2");
            softAssertions.assertThat(result.get(1).getPrices()).isEqualTo(Map.of("wholesale", new BigDecimal("10.99")));
            softAssertions.assertThat(result.get(1).getQuantities()).isEqualTo(Map.of("count1", 5));
        });
    }

    @Test
    void mapToExcelProducts_shouldReturnEmptyList_whenInputIsEmpty() {
        VendorFile vendorFile = new VendorFile();
        vendorFile.setId(1L);

        List<Product> result = ExcelParserHelper.mapToExcelProducts(vendorFile, Collections.emptyList());

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void mapToExcelProducts_shouldHandleNullMaps() {
        // arrange
        VendorFile vendorFile = new VendorFile();
        vendorFile.setId(1L);

        List<ExcelProduct> excelProducts = new ArrayList<>();
        ExcelProduct emptyExcelProduct = new ExcelProduct();
        excelProducts.add(emptyExcelProduct);

        // act
        List<Product> result = ExcelParserHelper.mapToExcelProducts(vendorFile, excelProducts);

        // assert
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(0).getVendorFileId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isNull();
        assertThat(result.get(0).getPrices()).isEmpty();
        assertThat(result.get(0).getQuantities()).isEmpty();
    }

    @Test
    void mapToExcelHeaderCellDtos_shouldMapCorrectly() {
        // arrange
        HeaderCellWithSubcategory cell1 = new HeaderCellWithSubcategory();
        cell1.setOriginName("originalName1");
        cell1.setCategory(Category.NAME);
        cell1.setSubcategoryName("subcategory_name1");
        cell1.setNormalizedName("normalized_name1");
        cell1.setCellStatus(CellStatus.PROCESSED);

        HeaderCellWithSubcategory cell2 = new HeaderCellWithSubcategory();
        cell2.setOriginName("original_name2");
        cell2.setCategory(Category.PRICE);
        cell2.setSubcategoryName("subcategory_name2");
        cell2.setNormalizedName("normalized_name2");
        cell2.setCellStatus(CellStatus.IGNORED);

        List<HeaderCellWithSubcategory> input = List.of(cell1, cell2);

        // act
        List<HeaderCellDto> result = ExcelParserHelper.mapToExcelHeaderCellDtos(input);

        // assert
        assertThat(result).isNotNull().hasSize(2);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.get(0).getOriginalName()).isEqualTo("originalName1");
            softly.assertThat(result.get(0).getCategory()).isEqualTo(Category.NAME);
            softly.assertThat(result.get(0).getSubcategoryName()).isEqualTo("subcategory_name1");
            softly.assertThat(result.get(0).getNormalizedName()).isEqualTo("normalized_name1");
            softly.assertThat(result.get(0).getCellStatus()).isEqualTo(CellStatus.PROCESSED);

            softly.assertThat(result.get(1).getOriginalName()).isEqualTo("original_name2");
            softly.assertThat(result.get(1).getCategory()).isEqualTo(Category.PRICE);
            softly.assertThat(result.get(1).getSubcategoryName()).isEqualTo("subcategory_name2");
            softly.assertThat(result.get(1).getNormalizedName()).isEqualTo("normalized_name2");
            softly.assertThat(result.get(1).getCellStatus()).isEqualTo(CellStatus.IGNORED);
        });
    }

    @Test
    void mapToExcelHeaderCellDtos_shouldReturnEmptyList_whenInputIsEmpty() {
        List<HeaderCellWithSubcategory> excelHeaderCellWithSubcategories = new ArrayList<>();

        List<HeaderCellDto> result = ExcelParserHelper.mapToExcelHeaderCellDtos(excelHeaderCellWithSubcategories);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void mapToExcelHeaderCellDtos_shouldHandleNullFields() {
        // arrange
        HeaderCellWithSubcategory cell = new HeaderCellWithSubcategory();
        List<HeaderCellWithSubcategory> input = List.of(cell);

        // act
        List<HeaderCellDto> result = ExcelParserHelper.mapToExcelHeaderCellDtos(input);

        // assert
        assertThat(result).isNotNull().hasSize(1);
        HeaderCellDto dto = result.get(0);

        assertThat(dto).isNotNull();
        assertThat(dto.getOriginalName()).isNull();
        assertThat(dto.getCategory()).isNull();
        assertThat(dto.getSubcategoryName()).isNull();
        assertThat(dto.getNormalizedName()).isNull();
        assertThat(dto.getCellStatus()).isNull();
    }

    @Test
    void creating_Subcategory_Mapping_Without_Subcategories_Returns_Empty_Subcategory_Mapping() {
        List<HeaderCell> headerCells = new ArrayList<>();

        HeaderCell headerCell1 = new HeaderCell();
        //excelHeaderCell1.setId();



    }


}