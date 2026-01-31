package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCell;
import io.github.bagdad.dakarhelperservice.model.ExcelHeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.ExcelProduct;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.excelparser.model.Product;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import io.github.bagdad.models.excelparser.ExcelHeaderCellDto;
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

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.addName("name1", "Product 1");
        product1.addPrice("price", new BigDecimal("9.99"));
        product1.addQuantity("count1", 10);

        Product product2 = new Product();
        product2.addName("name2", "Product 2");
        product2.addPrice("wholesale", new BigDecimal("10.99"));
        product2.addQuantity("count1", 5);

        products.add(product1);
        products.add(product2);

        // act
        List<ExcelProduct> result = ExcelParserHelper.mapToExcelProducts(vendorFile, products);

        // assert
        assertThat(result).isNotNull().hasSize(2);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.get(0).getVendorFileId()).isEqualTo(1L);
            softAssertions.assertThat(result.get(0).getNames()).isEqualTo(Map.of("name1", "Product 1"));
            softAssertions.assertThat(result.get(0).getPrices()).isEqualTo(Map.of("price", new BigDecimal("9.99")));
            softAssertions.assertThat(result.get(0).getQuantities()).isEqualTo(Map.of("count1", 10));

            softAssertions.assertThat(result.get(1).getVendorFileId()).isEqualTo(1L);
            softAssertions.assertThat(result.get(1).getNames()).isEqualTo(Map.of("name2", "Product 2"));
            softAssertions.assertThat(result.get(1).getPrices()).isEqualTo(Map.of("wholesale", new BigDecimal("10.99")));
            softAssertions.assertThat(result.get(1).getQuantities()).isEqualTo(Map.of("count1", 5));
        });
    }

    @Test
    void mapToExcelProducts_shouldReturnEmptyList_whenInputIsEmpty() {
        VendorFile vendorFile = new VendorFile();
        vendorFile.setId(1L);

        List<ExcelProduct> result = ExcelParserHelper.mapToExcelProducts(vendorFile, Collections.emptyList());

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void mapToExcelProducts_shouldHandleNullMaps() {
        // arrange
        VendorFile vendorFile = new VendorFile();
        vendorFile.setId(1L);

        List<Product> products = new ArrayList<>();
        Product emptyProduct = new Product();
        products.add(emptyProduct);

        // act
        List<ExcelProduct> result = ExcelParserHelper.mapToExcelProducts(vendorFile, products);

        // assert
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(0).getVendorFileId()).isEqualTo(1L);
        assertThat(result.get(0).getNames()).isEmpty();
        assertThat(result.get(0).getPrices()).isEmpty();
        assertThat(result.get(0).getQuantities()).isEmpty();
    }

    @Test
    void mapToExcelHeaderCellDtos_shouldMapCorrectly() {
        // arrange
        ExcelHeaderCellWithSubcategory cell1 = new ExcelHeaderCellWithSubcategory();
        cell1.setOriginName("originalName1");
        cell1.setCategory(Category.NAME);
        cell1.setSubcategoryName("subcategory_name1");
        cell1.setNormalizedName("normalized_name1");
        cell1.setCellStatus(CellStatus.PROCESSED);

        ExcelHeaderCellWithSubcategory cell2 = new ExcelHeaderCellWithSubcategory();
        cell2.setOriginName("original_name2");
        cell2.setCategory(Category.PRICE);
        cell2.setSubcategoryName("subcategory_name2");
        cell2.setNormalizedName("normalized_name2");
        cell2.setCellStatus(CellStatus.IGNORED);

        List<ExcelHeaderCellWithSubcategory> input = List.of(cell1, cell2);

        // act
        List<ExcelHeaderCellDto> result = ExcelParserHelper.mapToExcelHeaderCellDtos(input);

        // assert
        assertThat(result).isNotNull().hasSize(2);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.get(0).getOriginName()).isEqualTo("originalName1");
            softly.assertThat(result.get(0).getCategory()).isEqualTo(Category.NAME);
            softly.assertThat(result.get(0).getSubcategoryName()).isEqualTo("subcategory_name1");
            softly.assertThat(result.get(0).getNormalizedName()).isEqualTo("normalized_name1");
            softly.assertThat(result.get(0).getCellStatus()).isEqualTo(CellStatus.PROCESSED);

            softly.assertThat(result.get(1).getOriginName()).isEqualTo("original_name2");
            softly.assertThat(result.get(1).getCategory()).isEqualTo(Category.PRICE);
            softly.assertThat(result.get(1).getSubcategoryName()).isEqualTo("subcategory_name2");
            softly.assertThat(result.get(1).getNormalizedName()).isEqualTo("normalized_name2");
            softly.assertThat(result.get(1).getCellStatus()).isEqualTo(CellStatus.IGNORED);
        });
    }

    @Test
    void mapToExcelHeaderCellDtos_shouldReturnEmptyList_whenInputIsEmpty() {
        List<ExcelHeaderCellWithSubcategory> excelHeaderCellWithSubcategories = new ArrayList<>();

        List<ExcelHeaderCellDto> result = ExcelParserHelper.mapToExcelHeaderCellDtos(excelHeaderCellWithSubcategories);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void mapToExcelHeaderCellDtos_shouldHandleNullFields() {
        // arrange
        ExcelHeaderCellWithSubcategory cell = new ExcelHeaderCellWithSubcategory();
        List<ExcelHeaderCellWithSubcategory> input = List.of(cell);

        // act
        List<ExcelHeaderCellDto> result = ExcelParserHelper.mapToExcelHeaderCellDtos(input);

        // assert
        assertThat(result).isNotNull().hasSize(1);
        ExcelHeaderCellDto dto = result.get(0);

        assertThat(dto).isNotNull();
        assertThat(dto.getOriginName()).isNull();
        assertThat(dto.getCategory()).isNull();
        assertThat(dto.getSubcategoryName()).isNull();
        assertThat(dto.getNormalizedName()).isNull();
        assertThat(dto.getCellStatus()).isNull();
    }

    @Test
    void creating_Subcategory_Mapping_Without_Subcategories_Returns_Empty_Subcategory_Mapping() {
        List<ExcelHeaderCell> excelHeaderCells = new ArrayList<>();

        ExcelHeaderCell excelHeaderCell1 = new ExcelHeaderCell();
        //excelHeaderCell1.setId();



    }


}