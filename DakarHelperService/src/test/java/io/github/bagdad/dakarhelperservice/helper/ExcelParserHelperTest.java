package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.Product;
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
    void Mapping_to_excel_products_must_return_excel_product_collection() {
        // arrange
        Long vendorId = 1L;

        ExcelProduct excelProduct1 = ExcelProduct.builder()
                .name("Product 1")
                .prices(Map.of("price", new BigDecimal("9.99")))
                .quantities(Map.of("count1", 10))
                .build();

        ExcelProduct excelProduct2 = ExcelProduct.builder()
                .name("Product 2")
                .prices(Map.of("wholesale", new BigDecimal("10.99")))
                .quantities(Map.of("count1", 5))
                .build();

        List<ExcelProduct> excelProducts = List.of(excelProduct1, excelProduct2);

        // act
        List<Product> result = ExcelParserHelper.mapToProducts(vendorId, excelProducts);

        // assert
        assertThat(result).isNotNull().hasSize(2);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.get(0).getVendorId()).isEqualTo(1L);
            softAssertions.assertThat(result.get(0).getName()).isEqualTo("Product 1");
            softAssertions.assertThat(result.get(0).getPrices()).isEqualTo(Map.of("price", new BigDecimal("9.99")));
            softAssertions.assertThat(result.get(0).getQuantities()).isEqualTo(Map.of("count1", 10));

            softAssertions.assertThat(result.get(1).getVendorId()).isEqualTo(1L);
            softAssertions.assertThat(result.get(1).getName()).isEqualTo("Product 2");
            softAssertions.assertThat(result.get(1).getPrices()).isEqualTo(Map.of("wholesale", new BigDecimal("10.99")));
            softAssertions.assertThat(result.get(1).getQuantities()).isEqualTo(Map.of("count1", 5));
        });
    }

    @Test
    void Mapping_to_products_with_empty_product_collection_must_return_empty_collection() {
        Long vendorId = 1L;

        List<Product> result = ExcelParserHelper.mapToProducts(vendorId, Collections.emptyList());

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void Mapping_to_header_cell_dtos_must_return_header_cell_dtos_collection() {
        // arrange
        HeaderCellWithSubcategory cell1 = HeaderCellWithSubcategory.builder()
                .originalName("originalName1")
                .normalizedName("normalized_name1")
                .subcategoryName("subcategory_name1")
                .category(Category.NAME)
                .cellStatus(CellStatus.PROCESSED)
                .build();

        HeaderCellWithSubcategory cell2 = HeaderCellWithSubcategory.builder()
                .originalName("original_name2")
                .normalizedName("normalized_name2")
                .subcategoryName("subcategory_name2")
                .category(Category.PRICE)
                .cellStatus(CellStatus.IGNORED)
                .build();

        List<HeaderCellWithSubcategory> headerCellWithSubcategories = List.of(cell1, cell2);

        // act
        List<HeaderCellDto> result = ExcelParserHelper.mapToHeaderCellDtos(headerCellWithSubcategories);

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
    void Mapping_to_header_cell_dtos_with_empty_collection_must_return_empty_collection() {
        List<HeaderCellDto> result = ExcelParserHelper.mapToHeaderCellDtos(Collections.emptyList());

        assertThat(result).isNotNull().isEmpty();
    }


}