package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.models.response.product.ProductQueryItem;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ProductHelperTest {

    @Test
    void Calculating_seller_price_with_integer_margin() {
        BigDecimal price = BigDecimal.valueOf(100.50);
        BigDecimal margin = BigDecimal.valueOf(10);

        BigDecimal res = ProductHelper.calculateSellerPrice(price, margin);

        assertThat(res).isNotNull();
        assertThat(res).isEqualByComparingTo(BigDecimal.valueOf(110.55));
    }

    @Test
    void Calculating_price_with_rational_margin() {
        BigDecimal price = BigDecimal.valueOf(100);
        BigDecimal margin = BigDecimal.valueOf(10.50);

        BigDecimal res = ProductHelper.calculateSellerPrice(price, margin);

        assertThat(res).isNotNull();
        assertThat(res).isEqualByComparingTo(BigDecimal.valueOf(110.50));
    }

    @Test
    void Calculating_price_with_small_price_and_small_rational_margin() {
        BigDecimal price = BigDecimal.valueOf(1.5);
        BigDecimal margin = BigDecimal.valueOf(1.5);

        BigDecimal res = ProductHelper.calculateSellerPrice(price, margin);

        assertThat(res).isNotNull();
        assertThat(res).isEqualByComparingTo(BigDecimal.valueOf(1.5225));
    }

    @Test
    void Calculating_price_without_margin_returns_price_itself() {
        BigDecimal price = BigDecimal.valueOf(100.50);
        BigDecimal margin = null;

        BigDecimal res = ProductHelper.calculateSellerPrice(price, margin);

        assertThat(res).isNotNull();
        assertThat(res).isEqualByComparingTo(BigDecimal.valueOf(100.50));
    }

    @Test
    void Mapping_product_to_product_response_without_vendors_throws_exception() {
        List<Vendor> vendors = List.of();
        Product product = Product.builder()
                .id("1")
                .vendorId(1L)
                .name("Hankook")
                .prices(Map.of("price", BigDecimal.valueOf(10.00)))
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("count", 10))
                .totalQuantity(10)
                .build();
        BigDecimal margin = BigDecimal.valueOf(10);

        assertThatThrownBy(() -> ProductHelper.mapToProductQueryItem(product, vendors, margin))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void Mapping_product_to_product_data_returns_product_data() {
        List<Vendor> vendors = List.of(Vendor.builder()
                .id(1L)
                .title("vendor1")
                .build()
        );
        Product product = Product.builder()
                .id("1")
                .vendorId(1L)
                .name("Hankook")
                .prices(Map.of("price", BigDecimal.valueOf(10.00)))
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("count", 10))
                .totalQuantity(10)
                .build();
        BigDecimal margin = BigDecimal.valueOf(10);

        ProductQueryItem res = ProductHelper.mapToProductQueryItem(product, vendors, margin);

        ProductQueryItem expected = ProductQueryItem.builder()
                    .name("Hankook")
                    .minPrice(BigDecimal.valueOf(10.00))
                    .priceWithMargin(BigDecimal.valueOf(11.00))
                    .totalQuantity(10)
                    .vendorTitle("vendor1")
                    .build();

        assertThat(res.getName()).isEqualTo(expected.getName());
        assertThat(res.getVendorTitle()).isEqualTo(expected.getVendorTitle());
        assertThat(res.getMinPrice()).isEqualByComparingTo(expected.getMinPrice());
        assertThat(res.getTotalQuantity()).isEqualTo(expected.getTotalQuantity());
        assertThat(res.getPriceWithMargin()).isEqualByComparingTo(expected.getPriceWithMargin());
    }

    @Test
    void Creating_field_conditions_with_all_fields_must_return_collection_with_all_criteria() {
        ProductQuery productQuery = ProductQuery.builder()
                .vendorIds(List.of(1L, 2L))
                .name("Hankook")
                .minPrice(BigDecimal.valueOf(100.00))
                .quantity(5)
                .build();

        List<Criteria> res = ProductHelper.createFieldConditions(productQuery);

        List<Criteria> expected = List.of(
                where("vendor_id").in(List.of(1L, 2L)),
                where("name").regex(Pattern.compile("Hankook", Pattern.CASE_INSENSITIVE)),
                where("min_price").lte(BigDecimal.valueOf(100.00)),
                where("total_quantity").gte(5)
        );

        assertThat(res).isEqualTo(expected);
    }

    @Test
    void Creating_sorting_conditions_with_empty_conditions_must_return_empty_collection() {
        LinkedHashMap<String, Integer> sortingConditions = new LinkedHashMap<>();
        sortingConditions.put("name", 1);
        sortingConditions.put("min_price", -1);
        sortingConditions.put("total_quantity", 0);

        ProductQuery productQuery = ProductQuery.builder()
                .sortingConditions(sortingConditions)
                .build();

        List<Sort.Order> res = ProductHelper.createSortingConditions(productQuery);

        List<Sort.Order> expected = List.of(
                new Sort.Order(Sort.Direction.DESC, "name"),
                new Sort.Order(Sort.Direction.ASC, "min_price")
        );

        assertThat(res).isEqualTo(expected);
    }

    @Test
    void Creating_sorting_conditions_must_return_collection_with_all_sorting_conditions() {
        LinkedHashMap<String, Integer> sortingConditions = new LinkedHashMap<>();
        sortingConditions.put("name", 1);
        sortingConditions.put("min_price", -1);
        sortingConditions.put("total_quantity", 0);

        ProductQuery productQuery = ProductQuery.builder()
                .sortingConditions(sortingConditions)
                .build();

        List<Sort.Order> res = ProductHelper.createSortingConditions(productQuery);

        List<Sort.Order> expected = List.of(
                new Sort.Order(Sort.Direction.DESC, "name"),
                new Sort.Order(Sort.Direction.ASC, "min_price")
        );

        assertThat(res).isEqualTo(expected);
    }


}
